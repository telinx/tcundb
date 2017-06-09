package com.tcun.tcundb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.tcun.handle.ResultSetHandler;

public interface TcunTemplate {
    
    
    public void setDataSource(DataSource dataSource);
    
    
    public String getNamingConventions();
    
    
    public void setNamingConventions(String namingConventions);
   
    
    public <T> T queryForObjectByParams(String sql, ResultSetHandler handler, Object... params);

   
    public <T> T queryForObjectByBean(String sql, ResultSetHandler handler, Object bean);
    
    
    public <T> T queryForObjectByBean(ResultSetHandler handler, Object bean);
    
   
    public <T> T queryForObjectByMap(String sql, ResultSetHandler handler, Map<String, Object> params);
    
   
    public <T> List<T> queryForListByParams(String sql, ResultSetHandler handler,  Object... params);
    
    
    public <T> List<T> queryForListByBean(String sql, ResultSetHandler handler, Object bean);
    
   
    public <T> List<T> queryForListByBean(ResultSetHandler handler, Object bean);
    
   
    public <T> List<T> queryForListByMap(String sql, ResultSetHandler handler, Map<String, Object> params);
    
    
    public int updateByParams(String sql, Object... params);
    
    
    public <T> int updateByBean(String sql, T bean);
    
    
    public <T> int updateByMap(String sql, Map<String, Object> params);
    
    
    public int deleteByParams(String sql, Object... params);
    
    
    public <T> int deleteByBean(String sql, T bean);
    
    
    public <T> int deleteByBean(T bean);
    
    
    public <T> int deleteByMap(String sql, Map<String, Object> params);
    
    
    public int insertByParams(String sql, Object... params);
    
    
    public <T> int insertByBean(String sql, T bean);
    
    
    public <T> int insertByBean(T bean);
    
    
    public <T> int insertByMap(String sql, Map<String, Object> params);
    
    
    public <T> int[] batchInsertByBean(String sql, List<T> beans);
    
    
    public <T> int[] batchInsertByBean(List<T> beans);
    
    
    public <T> int[] batchInsertByMap(String sql, List<Map<String, Object>> paramsList);
    
    
    public <T> T callProcedureByParams(String sql, ResultSetHandler handler, Object... params);

    
    public <T> T callProcedureByBean(String sql, ResultSetHandler handler, Object bean);
    
    
    public <T> T callProcedureByMap(String sql, ResultSetHandler handler, Map<String, Object> params);
    
    
    public <T> List<T> callProcedureGetListByParams(String sql, ResultSetHandler handler, Object... params);

    
    public <T> List<T> callProcedureGetListByBean(String sql, ResultSetHandler handler, Object bean);
    
    
    public <T> List<T> callProcedureGetListByMap(String sql, ResultSetHandler handler, Map<String, Object> params);
    
    
}
