package com.tcun.handle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 * @ClassName ResultSetProcessor
 * @Description TODO(结果集处理器)
 * @author pcdalao
 * @Date 2017年5月27日 上午11:36:14
 * @version 1.0.0
 * @param <T>
 */
public interface ResultSetHandler {

    <T> T process(ResultSet rs) throws SQLException;
    
    <T> List<T> processList(ResultSet rs) throws SQLException;
    
    <T> Class<T> getType();
    
}
