package com.gupaoedu.vip.myibatis.executor;

import com.gupaoedu.vip.myibatis.parameter.ParameterHandler;
import com.gupaoedu.vip.myibatis.session.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 封装jdbc statement 用于操作数据
 *
 * @author tzf
 */
public class StatementHandler {

    private  ResultSetHandler resultSetHandler = new ResultSetHandler();

    public <T> T query(String statement,Object[] parameters,Class pojo){
        Connection connection = null;
        PreparedStatement psmt = null;
        Object result = null;
        try {
            connection = getConnection();
            psmt = connection.prepareStatement(statement);
            ParameterHandler handler = new ParameterHandler(psmt);
            handler.setParameters(parameters);
            psmt.execute();
            result = resultSetHandler.handle(psmt.getResultSet(),pojo);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return (T)result;
    }

    private Connection getConnection() {
        String driver = Configuration.PROPERTIES.getString("jdbc.driver");
        String url =  Configuration.PROPERTIES.getString("jdbc.url");
        String username = Configuration.PROPERTIES.getString("jdbc.username");
        String password = Configuration.PROPERTIES.getString("jdbc.password");
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url,username,password);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e1){
            e1.printStackTrace();
        }
        return connection;
    }

}
