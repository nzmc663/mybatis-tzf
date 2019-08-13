package com.gupaoedu.vip.myibatis.cache;
/**
 * 缓存Key的设计
 *
 * @author tzf
 */
public class CacheKey {
    /**
     * 默认hash值
     */
    private static final int DEFAULT_HASHCODE = 17;
    /**
     * 默认倍数
     */
    private static final int DEFAULT_MULTIPLIER = 37;

    private int hashCode;
    private int count;
    private int multiplier;

    public CacheKey() {
        this.hashCode = DEFAULT_HASHCODE;
        this.multiplier = DEFAULT_MULTIPLIER;
    }
    /**
     * 计算CacheKey中的hashCode
     */
    public void update(Object object) {
        int baseHashCode = object == null ? 1 : object.hashCode();
        count++;
        baseHashCode *= count;
        hashCode = multiplier * hashCode +baseHashCode;
    }
    /**
     * 获取hashCode
     */
    public int getCode() { return hashCode; }
}
