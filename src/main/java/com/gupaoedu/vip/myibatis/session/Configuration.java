package com.gupaoedu.vip.myibatis.session;

import com.gupaoedu.vip.myibatis.annotation.Entity;
import com.gupaoedu.vip.myibatis.annotation.Select;
import com.gupaoedu.vip.myibatis.binding.MapperRegistry;
import com.gupaoedu.vip.myibatis.executor.CachingExecutor;
import com.gupaoedu.vip.myibatis.executor.Executor;
import com.gupaoedu.vip.myibatis.executor.SimpleExecutor;
import com.gupaoedu.vip.myibatis.plugin.Interceptor;
import com.gupaoedu.vip.myibatis.plugin.InterceptorChain;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * myibatis 全局配置文件类
 *
 * @author tzf
 */
public class Configuration {
    /**
     * sql映射关系配置，使用注解时不用重复配置
     */
    public static final ResourceBundle SQL_MAPPINGS;
    /**
     * 全局配置文件
     */
    public static final ResourceBundle PROPERTIES;
    /**
     * 维护接口和工厂类关系
     */
    public static final MapperRegistry MAPPED_REGISTRY = new MapperRegistry();
    /**
     * 维护接口方法与Sql关系
     */
    public static final Map<String,String> MAPPED_STATEMENTS = new HashMap<>();
    /**
     * 插件链
     */
    private InterceptorChain interceptorChain = new InterceptorChain();
    /**
     * 所有Mapper接口
     */
    private List<Class<?>> mapperList = new ArrayList <>();
    /**
     * 类所有文件
     */
    private List<String> classPaths = new ArrayList <>();

    static{
        SQL_MAPPINGS = ResourceBundle.getBundle("sql");
        PROPERTIES = ResourceBundle.getBundle("mybatis");
    }

    public Configuration() {
        //Note：在properties和注解中重复配置sql会覆盖
        //1、解析sql.properties
        for (String key : SQL_MAPPINGS.keySet()){
            Class mapper = null;
            Class pojo = null;
            // properties中的value用--隔开，第一个是sql语句
            String statement = SQL_MAPPINGS.getString(key).split("--")[0];
            // properties中的value用--隔开，第二个是需要转换的poji类型
            String pojoStr = SQL_MAPPINGS.getString(key).split("--")[1];
            try {
                mapper = Class.forName(key.substring(0,key.lastIndexOf(".")));
                pojo = Class.forName(pojoStr);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //2、解析Mapper接口配置，扫描注册
        String mapperPath = PROPERTIES.getString("mapper.path");
        scanPackage(mapperPath);
        for (Class<?> mapper : mapperList){
            parsingClass(mapper);
        }
        //3、解析插件，可配置多个插件
        String pluginPathValue = PROPERTIES.getString("plugin.path");
        String[] pluginPaths = pluginPathValue.split(",");
        if (pluginPaths != null){
            for (String plugin : pluginPaths){
                Interceptor interceptor = null;
                try {
                    interceptor = (Interceptor) Class.forName(plugin).newInstance();
                }catch (Exception e){
                    e.printStackTrace();
                }
                interceptorChain.addInterceptor(interceptor);
            }
        }
    }
    /**
     * 根据全局配置文件的Mapper的接口路径，扫描所有接口
     */
    private void scanPackage(String mapperPath) {
        String classPath = this.getClass().getResource("/").getPath();
        mapperPath = mapperPath.replace(".",File.separator);
        String mainPath = classPath + mapperPath;
        doPath(new File(mainPath));
        for (String className : classPaths){
            className =  className.replace(classPath.replace("/","\\").replaceFirst("\\\\",""),"")
                                    .replace("\\",".")
                                    .replace(".class","");
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            }catch (Exception e){
                e.printStackTrace();
            }
            if ( clazz!=null && clazz.isInterface()){
                mapperList.add(clazz);
            }
        }
    }
    /**
     * 获取文件或文件夹下所有的类.class
     */
    private void doPath(File file) {
        //如果文件夹，遍历
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files){
                doPath(f);
            }
        }else if (file.getName().endsWith(".class")){//如果是.class文件结尾直接添加
            classPaths.add(file.getPath());
        }
    }
    /**
     * 解析Mapper接口上配置的注解(sql语句)
     */
    private void parsingClass(Class<?> mapper) {
        //1、解析类上的注解
        //如果有@Entity注解，说明是查询数据库的接口
        if (mapper.isAnnotationPresent(Entity.class)){
            for (Annotation annotation : mapper.getAnnotations()){
                if (annotation.annotationType().equals(Entity.class)){
                    //注册接口与实体类的关系
                    MAPPED_REGISTRY.addMapper(mapper,((Entity)annotation).value());
                }
            }
        }

        //2、解析方法上的注解
        for (Method method : mapper.getMethods()){
            //解析@Select注解的sql语句
            if (method.isAnnotationPresent(Select.class)){
                for (Annotation annotation : method.getDeclaredAnnotations()){
                    if (annotation.annotationType().equals(Select.class)){
                        //注册接口类型+方法名和sql语句的映射关系
                        String statement = method.getDeclaringClass().getName()+"."+method.getName();
                        MAPPED_STATEMENTS.put(statement,((Select) annotation).value());
                    }
                }
            }
        }
    }



    /**
     * 创建执行器，当开启缓存时使用缓存装饰
     * 当配置插件时，使用插件代理
     */
    public Executor newExecutor() {

        Executor executor = null;

        if (PROPERTIES.getString("cache.enabled").equals("true")){
            executor = new CachingExecutor(new SimpleExecutor());
        }else {
            executor = new SimpleExecutor();
        }
        // 目前只拦截了Executor，所有的插件都对Executor进行代理，没有对拦截类和方法签名进行判断
        if (interceptorChain.hasPlugin()){
            return (Executor) interceptorChain.pluginAll(executor);
        }
        return null;
    }

    /**
     * 根据class获取代理mapper
     */
    public <T> T getMapper(Class<T> clazz, DefaultSqlSession sqlSession) {
        return MAPPED_REGISTRY.getMapper(clazz,sqlSession);
    }

    /**
     * 根据sql配置的主键id从sql映射缓存中获取sql
     */
    public String getMappedStatement(String id) {
        return MAPPED_STATEMENTS.get(id);
    }

    /**
     * 根据statement判断是否存在映射的sql
     */
    public boolean hasStatement(String statementName){ return MAPPED_STATEMENTS.containsKey(statementName);}
}
