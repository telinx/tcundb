package com.tcun.process;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.ResultSetMetaData;

public interface Processer {
    
    /**
     * @Description (输入结果集与类的类型，结果集转成Bean)
     * @param rs
     * @param type
     * @return
     * @throws SQLException
     */
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException;

    /**
     * @param <T>
     * @Description (输入结果集与类的类型，结果集转成包含Bean的List)
     * @param rs
     * @param type
     * @return
     * @throws SQLException
     */
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException;
    
  
}
