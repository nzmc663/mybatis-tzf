package com.gupaoedu.vip.myibatis.binding;

import com.gupaoedu.vip.myibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/**
 * MapperProxy 代理类，用于户代理Mapper接口
 *
 * @author tzf
 */
public class MapperProxy implements InvocationHandler {

    private SqlSession sqlSession;

    private Class object;

    public MapperProxy(SqlSession sqlSession, Class object) {
        this.sqlSession = sqlSession;
        this.object = object;
    }
    /**
     * 反射调用，所有的mapper接口调用都会走这里
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String mapperInterface = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String statementId = mapperInterface + "." + methodName;
        // 如果根据接口类型+方法名能找到映射的sql，则执行sql
        if (sqlSession.getConfiguration().hasStatement(statementId)){
            return sqlSession.selectOne(statementId,args,object);
        }
        //否则直接执行被代理对象的原方法
        return method.invoke(proxy,args);
    }

}
