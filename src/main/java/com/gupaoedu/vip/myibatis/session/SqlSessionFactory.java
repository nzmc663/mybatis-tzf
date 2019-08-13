package com.gupaoedu.vip.myibatis.session;
/**
 * 会话工厂类
 *
 * @author tzf
 */
public class SqlSessionFactory {
    /**
     * 全局配置类
     */
    private Configuration configuration;
    /**
     * build返回sqlSession工厂类,解析配置文件的工作在Configuration的构造函数中
     */
    public SqlSessionFactory build(){
        this.configuration = new Configuration();
        return this;
    }
    /**
     * 返回默认的sqlSession
     */
    public SqlSession openSqlSession(){ return new DefaultSqlSession(configuration); }
}
