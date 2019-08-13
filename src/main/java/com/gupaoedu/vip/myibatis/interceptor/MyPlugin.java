package com.gupaoedu.vip.myibatis.interceptor;

import com.gupaoedu.vip.myibatis.annotation.Intercepts;
import com.gupaoedu.vip.myibatis.plugin.Interceptor;
import com.gupaoedu.vip.myibatis.plugin.Invocation;
import com.gupaoedu.vip.myibatis.plugin.Plugin;

import java.util.Arrays;

/**
 * 自定义插件
 *
 * @author tzf
 */
@Intercepts("query")
public class MyPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String statement = (String) invocation.getArgs()[0];
        Object[] parameter = (Object[]) invocation.getArgs()[1];
        Class clazz = (Class) invocation.getArgs()[2];
        System.out.println("插件输出：SQL：[" + statement + "]");
        System.out.println("插件输出：Parameters：" + Arrays.toString(parameter));
        System.out.println("插件输出：Class：" + clazz.getName());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.warp(target, this);
    }
}
