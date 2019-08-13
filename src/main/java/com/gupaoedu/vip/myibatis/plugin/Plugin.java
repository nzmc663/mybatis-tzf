package com.gupaoedu.vip.myibatis.plugin;

import com.gupaoedu.vip.myibatis.annotation.Intercepts;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理类，用于代理被拦截对象
 * 同时提供了创建代理类的方法
 */
public class Plugin implements InvocationHandler {
    /**
     * 被代理对象
     */
    private Object target;
    /**
     * 插件拦截器
     */
    private Interceptor interceptor;
    /**
     * @param target 被代理对象
     * @param interceptor 拦截器（插件）
     */
    public Plugin(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    /**
     * 对被代理对象进行代理，返回代理类
     */
    public static Object warp(Object object, Interceptor interceptor) {
        Class clazz = object.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new Plugin(object, interceptor));
    }
    /**
     * 反射调用
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 自定义的插件上有@Interceprs注解,指定了拦截的方法
        if (interceptor.getClass().isAnnotationPresent(Intercepts.class)){
            // 如果是被拦截的方法，则进入自定义拦截器的逻辑
            if (method.getName().equals(interceptor.getClass().getAnnotation(Intercepts.class).value())){
                return interceptor.intercept(new Invocation(target,method,args));
            }
        }
        // 执行原逻辑
        return method.invoke(target,method,args);
    }


}
