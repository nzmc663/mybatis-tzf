package com.gupaoedu.vip.myibatis.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器链，存放所有拦截器，和对代理对象进行循环代理
 *
 * @author tzf
 */
public class InterceptorChain {

    private final List<Interceptor> interceptors = new ArrayList <>();
    /**
     * 添加拦截器
     */
    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }
    /**
     * 对被拦截对象进行层层代理
     */
    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptors){
            target = interceptor.plugin(target);
        }
        return target;
    }
    /**
     * 判断是否有插件
     */
    public boolean hasPlugin() {
        return interceptors!=null && !interceptors.isEmpty();
    }
}
