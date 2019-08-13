package com.gupaoedu.vip.myibatis.executor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理器
 *
 * @author tzf
 */
public class ResultSetHandler {

    public <T> T handle(ResultSet resultSet, Class type) {
        // 直接通过Class反射生成一个实例
        Object pojo = null;
        try {
            pojo = type.newInstance();
            if (resultSet.next()) {
                for (Field field : pojo.getClass().getDeclaredFields()) {
                    setValue(pojo, field, resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) pojo;
    }

    /**
     * 反射赋值
     */
    private void setValue(Object pojo, Field field, ResultSet resultSet) {
        try {
            Method method = pojo.getClass().getMethod("set" + firstWordCapital(field.getName()),field.getType());
            method.invoke(pojo, getResult(resultSet, field));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单词首字母转为大写,setter方法
     */
    private String firstWordCapital(String word) {
        String first = word.substring(0, 1);
        String tail = word.substring(1);
        return first.toUpperCase() + tail;
    }

    /**
     * 根据反射判断类型，从ResultSet中去对应类型参数
     */
    private Object getResult(ResultSet resultSet, Field field) throws SQLException {
        Class type = field.getType();
        String dataName = humpToUnderline(field.getName());
        if (Integer.class == type) {
            return resultSet.getInt(dataName);
        } else if (String.class == type) {
            return resultSet.getString(dataName);
        } else if (Long.class == type) {
            return resultSet.getLong(dataName);
        } else if (Boolean.class == type) {
            return resultSet.getBoolean(dataName);
        } else if (Double.class == type) {
            return resultSet.getDouble(dataName);
        } else {
            return resultSet.getString(dataName);
        }
    }

    /**
     * 数据库下划线转java驼峰命名
     */
    private String humpToUnderline(String name) {
        StringBuilder sb = new StringBuilder(name);
        int temp = 0;
        if (!name.contains("_")){
            for (int i = 0; i < name.length(); i++){
                if (Character.isUpperCase(name.charAt(i))){
                    sb.insert(i+temp,"_");
                    temp++;
                }
            }
        }
        return sb.toString().toUpperCase();
    }
}


