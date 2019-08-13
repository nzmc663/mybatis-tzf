package com.gupaoedu.vip.myibatis.session;

import com.gupaoedu.vip.myibatis.executor.Executor;
/**
 * 默认sqlSession实现类，提供给应用层操作API
 */
public class DefaultSqlSession implements SqlSession {
    /**
     * 全局配置类引用
     */
    private Configuration configuration;
    /**
     * 执行器
     */
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        this.executor = configuration.newExecutor();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public <T> T getMapper(Class <T> clazz) {
        return configuration.getMapper(clazz,this);
    }

    public <T> T selectOne(String statement, Object[] parameter, Class pojo) {
        String sql = getConfiguration().getMappedStatement(statement);
        // 打印代理对象时会自动调用toString()方法，触发invoke()
        return executor.query(sql,parameter,pojo);
    }
}
