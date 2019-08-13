package com.gupaoedu.vip.myibatis.binding;

import com.gupaoedu.vip.myibatis.session.DefaultSqlSession;

import java.lang.reflect.Proxy;
/**
 * 用户产生MapperProxy代理类
 *
 * @author tzf
 */
public class MapperProxyFactory<T> {
    /**
     * 被代理接口
     */
    private Class<T> mapperInterface;
    /**
     * 被代理对象
     */
    private Class object;

    public MapperProxyFactory(Class<T> mapperInterface, Class object) {
        this.mapperInterface = mapperInterface;
        this.object = object;
    }
    /**
     * 生成代理对象
     */
    public T newInstance(DefaultSqlSession sqlSession) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new MapperProxy(sqlSession,object));
    }
}
