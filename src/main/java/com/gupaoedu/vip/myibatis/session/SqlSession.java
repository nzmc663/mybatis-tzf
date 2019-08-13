package com.gupaoedu.vip.myibatis.session;
/**
 * sqlSession顶层抽象
 *
 * @author tzf
 */
public interface SqlSession {
    /**
     * 获取全局配置
     */
    Configuration getConfiguration();
    /**
     * 根据Class获取Mappper
     */
    <T> T getMapper(Class<T> clazz);
    /**
     * 查询
     */
    <T> T selectOne(String statement, Object[] parameter, Class pojo);
}
