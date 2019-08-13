package com.gupaoedu.vip.myibatis.executor;
/**
 * 执行器顶层抽象接口
 *
 * @author tzf
 */
public interface Executor {
    /**
     * 查询
     */
    <T> T query(String sql, Object[] parameter, Class pojo);

    //TODO 其它DML操作
}
