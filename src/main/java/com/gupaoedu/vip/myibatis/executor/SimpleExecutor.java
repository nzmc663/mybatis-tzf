package com.gupaoedu.vip.myibatis.executor;
/**
 * 基础执行器
 *
 * @author tzf
 */
public class SimpleExecutor implements Executor {

    @Override
    public <T> T query(String statement, Object[] parameters, Class pojo) {
        StatementHandler statementHandler = new StatementHandler();
        return statementHandler.query(statement,parameters,pojo);
    }

}
