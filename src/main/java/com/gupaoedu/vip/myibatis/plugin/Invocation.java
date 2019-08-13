package com.gupaoedu.vip.myibatis.plugin;

import java.lang.reflect.Method;
/**
 * 包装类，对被代理对象进行包装
 *
 * @author tzf
 */
public class Invocation {
    /**
     * 被代理对象
     */
    private Object target;
    /**
     * 被代理方法
     */
    private Method method;
    /**
     * 方法参数
     */
    private Object[] args;

    public Invocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }
    /**
     * 执行调用
     */
    public Object proceed() throws Exception {
        return  method.invoke(target,args);
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }
}
