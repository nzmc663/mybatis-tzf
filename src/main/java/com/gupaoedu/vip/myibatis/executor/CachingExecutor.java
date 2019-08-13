package com.gupaoedu.vip.myibatis.executor;

import com.gupaoedu.vip.myibatis.cache.CacheKey;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 带sql缓存执行器，装饰器模式（装饰基础执行器）
 *
 * @author tzf
 */
public class CachingExecutor implements Executor{

    private Executor delegate;

    private static final Map<Integer,Object> CACHE = new HashMap <>();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T query(String statement, Object[] parameters, Class pojo) {
        //计算CacheKey
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement);
        cacheKey.update(joinStr(parameters));
        //命中缓存则返回
        if (CACHE.containsKey(cacheKey.getCode())){
            System.out.println("命中缓存：" + statement + ",参数："+Arrays.toString(parameters));
            return (T) CACHE.get(cacheKey.getCode());
        }
        Object obj = delegate.query(statement,parameters,pojo);
        CACHE.put(cacheKey.getCode(),obj);
        return (T)obj;
    }
    /**
     * 为了命中缓存,把Object[] 转换为逗号拼接的字符串,因为对象的hashCode都不一样
     */
    private String joinStr(Object[] objs) {
       StringBuilder sb = new StringBuilder();
       if ( objs != null && objs.length > 0){
           for (Object obj : objs){
               sb.append(obj.toString()).append(",");
           }
       }
       int length = sb.length();
       if (length > 0){
           sb.deleteCharAt(length-1);
       }
       return sb.toString();
    }
}
