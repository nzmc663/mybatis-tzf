package com.gupaoedu.vip.myibatis.parameter;

import java.sql.PreparedStatement;

/**
 * 参数处理器
 *
 * @author tzf
 */
public class ParameterHandler {
    /**
     * jdbc statement
     */
    private PreparedStatement statement;

    public ParameterHandler(PreparedStatement statement) {
        this.statement = statement;
    }
    /**
     * 从方法中获取参数，遍历设置sql中的?占位符
     */
    public void setParameters(Object[] parameters) {
        try {
            for (int i = 0; i < parameters.length; i++) {
                int k = i + 1;
                if (parameters[i] instanceof Integer) {
                    statement.setInt(k, (Integer) parameters[i]);
                } else if (parameters[i] instanceof Long) {
                    statement.setLong(k, (Long) parameters[i]);
                } else if (parameters[i] instanceof String) {
                    statement.setString(k, String.valueOf(parameters[i]));
                } else if (parameters[i] instanceof Boolean) {
                    statement.setBoolean(k, (Boolean) parameters[i]);
                } else {
                    statement.setString(k, String.valueOf(parameters[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
