package com.tcun.tcundb;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.tcun.handle.ResultSetHandler;
import com.tcun.process.BeanProcesser;
import com.tcun.statement.SQLPreparedHandler;
import com.tcun.util.NamingConventions;


public  class TcunTemplateExecutor implements TcunTemplate{
    
    private DataSource dataSource;
    
    private boolean sequence = false;
    
    private SQLPreparedHandler sph = SQLPreparedHandler.getInstance();
    
    BeanProcesser beanProcesser = new BeanProcesser();
    
    private String namingConventions = NamingConventions.CAMEL.value();
    

    /**
     * @Description (查询数据库返回实例,输入多个参数)
     * @param sql
     * @param handler
     * @param params
     * @return
     */
    @Override
    public <T> T queryForObjectByParams(String sql, ResultSetHandler handler, Object... params){
        
        PreparedStatement stmt = null;
        
        ResultSet rs = null;
        
        T result = null;
        
        try {
            
            sql = sph.format(sequence, sql, params);
            
            stmt = this.getConnection().prepareStatement(sql.replaceAll("(\\{)(.+?)(\\})", "?"));
            
            this.fillStatement(stmt, params);
            
            rs = stmt.executeQuery();
            
            result = handler.process(rs);
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }finally {
            
            close(rs, stmt, this.getConnection());
            
        }
        
        return result;
        
    }
    
    /**
     * 
     * @Description (查询数据库返回实例,输入对象实例)
     * @param sql
     * @param handler
     * @param bean
     * @return
     */
    @Override
    public <T> T queryForObjectByBean(String sql, ResultSetHandler handler, Object bean){
        
        Object[] paramsArray = this.getParamsByBean(bean, sql);
        
        return this.queryForObjectByParams(sql, handler, paramsArray);
    }
    
    
    
    /**
     * @Description (单表查询，不需要输入sql，查询数据),这个有问题，不能这样做
     * @param handler
     * @param bean
     * @return
     */
    public <T> T queryForObjectByBean(ResultSetHandler handler, Object bean){
        
        String tableName = this.tranferNamingConventions(handler.getType().getSimpleName());
        
        StringBuffer sqlTempalte = new StringBuffer( "select * from " + tableName + " where 1 = 1 ");
        
        String sql = sqlTempalte.toString();
        
        Object[] paramsArray = null;
        
        if(null != bean){
            
            if(bean instanceof Map){
                
                sql = this.getQuerySQLByMap((Map)bean, sqlTempalte);
                
                paramsArray = this.getParamsByMap((Map)bean, sql);
                
            }else{
                
                sql = this.getQuerySQLByBean(bean, sqlTempalte);
                
                paramsArray = this.getParamsByBean(bean, sql);
                
            }
            
            
            
        }

        return this.queryForObjectByParams(sql, handler, paramsArray);
        
    }
    
    /**
     * 
     * @Description (查询数据库返回实例,输入Map实例)
     * @param sql
     * @param handler
     * @param params
     * @return
     */
    public <T> T queryForObjectByMap(String sql, ResultSetHandler handler, Map<String, Object> params){
        
        Object[] paramsArray = this.getParamsByMap(params, sql);
        
        return this.queryForObjectByParams(sql, handler, paramsArray);
        
    }
    /**
     * 
     */
    @Override
    public <T> List<T> queryForListByParams(String sql, ResultSetHandler handler, Object... params) {
        
        PreparedStatement stmt = null;
        
        ResultSet rs = null;
        
        List<T> result = null;
        
        try {
            
            sql = sph.format(sequence, sql, params);
            
            stmt = this.getConnection().prepareStatement(sql.replaceAll("(\\{)(.+?)(\\})", "?"));
            
            this.fillStatement(stmt, params);
            
            rs = stmt.executeQuery();
            
            result = handler.processList(rs);
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }finally {
            
            close(rs, stmt, this.getConnection());
            
        }
        
        return result;
    }
    
    /**
     * 
     */
    @Override
    public <T> List<T> queryForListByBean(String sql, ResultSetHandler handler, Object bean) {
        
        Object[] paramsArray = getParamsByBean(bean, sql);
        
        return this.queryForListByParams(sql, handler, paramsArray);
    }

    /**
     * 
     */
    @Override
    public <T> List<T> queryForListByBean(ResultSetHandler handler, Object bean) {
        
        String tableName = this.tranferNamingConventions(handler.getType().getSimpleName());
        
        StringBuffer sqlTempalte = new StringBuffer( "select * from " + tableName + " where 1 = 1 ");
        
        String sql = sqlTempalte.toString();
        
        Object[] paramsArray = null;
        
        if(null != bean){
            
            if(bean instanceof Map){
                
                sql = this.getQuerySQLByMap((Map)bean, sqlTempalte);
                
                paramsArray = this.getParamsByMap((Map)bean, sql);
                
            }else{
                
                sql = this.getQuerySQLByBean(bean, sqlTempalte);
                
                paramsArray = this.getParamsByBean(bean, sql);
                
            } 
            
        }
        
        return this.queryForListByParams(sql, handler, paramsArray);
    }

    /**
     * 
     */
    @Override
    public <T> List<T> queryForListByMap(String sql, ResultSetHandler handler, Map<String, Object> params) {
        
        Object[] paramsArray = getParamsByMap(params, sql);
        
        return this.queryForListByParams(sql, handler, paramsArray);
        
    }
    
    /**
     * 
     */
    @Override
    public int updateByParams(String sql, Object... params){
        
        PreparedStatement stmt = null;
        
        int result = 0;
        
        try {
            
            sql = sph.format(sequence, sql, params);
            
            stmt = this.getConnection().prepareStatement(sql.replaceAll("(\\{)(.+?)(\\})", "?"));
            
            this.fillStatement(stmt, params);
            
            result = stmt.executeUpdate();
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }finally {
            
            close(stmt, this.getConnection());
            
        }
        
        return result;
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int updateByBean(String sql, T bean){
        
        Object[] paramsArray = getParamsByBean(bean, sql);
        
        return this.updateByParams(sql, paramsArray);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int updateByMap(String sql, Map<String, Object> params){
        
        Object[] paramsArray = getParamsByMap(params, sql);
        
        return this.updateByParams(sql, paramsArray);
        
    }
    
    /**
     * 
     */
    @Override
    public int deleteByParams(String sql, Object... params){
       
        return this.updateByParams(sql, params);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int deleteByBean(String sql, T bean){
        
        return this.updateByBean(sql, bean);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int deleteByBean(T bean){
        
        if(null == bean){
            
            return 0;
            
        }
        
        String tableName = this.tranferNamingConventions(bean.getClass().getSimpleName());
        
        StringBuffer sqlTempalte = new StringBuffer( "delete from " + tableName + " where 1 = 1 ");
        
        String sql = this.getQuerySQLByBean(bean, sqlTempalte);
        
        Object[] paramsArray = this.getParamsByBean(bean, sql);
        
        return this.updateByParams(sql, paramsArray);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int deleteByMap(String sql, Map<String, Object> params){
        
        return this.updateByMap(sql, params);
        
    }
    
    
    /**
     * 
     */
    @Override
    public int insertByParams(String sql, Object... params){
       
        return this.updateByParams(sql, params);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int insertByBean(String sql, T bean){
        
        return this.updateByBean(sql, bean);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int insertByBean(T bean){
        
        if(null == bean){
            
            return 0;
        }
        
        String sql = this.getInsertSQLByBean(bean);
       
        return this.updateByBean(sql, bean);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> int insertByMap(String sql, Map<String, Object> params){
        
        return this.updateByMap(sql, params);
        
    }
    
    
    @Override
    public <T> int[] batchInsertByBean(String sql, List<T> beans){
        
        if(null == beans || beans.size() < 1){
            
            return null;
            
        }
        
        PreparedStatement stmt = null;
        
        int[] result = null;
        
        Object[] paramsArray = getParamsByBean(beans.get(0), sql);
        
        try {
             
            sql = sph.format(sequence, sql, paramsArray);
            
            stmt = this.getConnection().prepareStatement(sql.replaceAll("(\\{)(.+?)(\\})", "?"));
            
            for(T bean : beans){
                
                paramsArray = getParamsByBean(bean, sql);
                
                this.fillStatement(stmt, paramsArray);
                
                stmt.addBatch();
                
            }

            result = stmt.executeBatch();
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }finally {
            
            close(stmt, this.getConnection());
            
        }
        
        return result;
        
    }

    /**
     * 
     */
    @Override
    public <T> int[] batchInsertByBean(List<T> beans){
        
        if(null == beans || beans.size() < 1){
            
            return null;
            
        }
        
        String sql = this.getInsertSQLByBean(beans.get(0));
        
        return this.batchInsertByBean(sql, beans);
        
    }
    
    /**
     * 
     */
    public <T> int[] batchInsertByMap(String sql, List<Map<String, Object>> paramsList){
        
        if(null == paramsList || paramsList.size() < 1){
            
            return null;
            
        }
        
        PreparedStatement stmt = null;
        
        int[] result = null;
        
        Object[] paramsArray = getParamsByMap(paramsList.get(0), sql);
        
        
        try {
             
            sql = sph.format(sequence, sql, paramsArray);
            
            stmt = this.getConnection().prepareStatement(sql.replaceAll("(\\{)(.+?)(\\})", "?"));
            
            for(Map<String, Object> params : paramsList){
                
                paramsArray = getParamsByMap(params, sql);
                
                this.fillStatement(stmt, paramsArray);
                
                stmt.addBatch();
                
            }

            result = stmt.executeBatch();
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }finally {
            
            close(stmt, this.getConnection());
            
        }
        
        return result;
        
    }
    
    
    /**
     * 
     */
    @Override
    public <T> T callProcedureByParams(String sql, ResultSetHandler handler, Object... params){
        
        CallableStatement ctmt = null;
        
        ResultSet rs = null;
        
        T result = null;
        
        try {
            
            sql = sph.format(sequence, sql, params);
            
            ctmt = this.getConnection().prepareCall(sql.replaceAll("(\\{)(.+?)(\\})", "?"));
            
            this.fillStatement(ctmt, params);
            
            rs = ctmt.executeQuery();
            
            result = handler.process(rs);
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }finally {
            
            close(rs, ctmt, this.getConnection());
            
        }
        
        return result;
        
    }
    
    /**
     * 
     */
    @Override
    public <T> T callProcedureByBean(String sql, ResultSetHandler handler, Object bean){
        
        Object[] paramsArray = this.getParamsByBean(bean, sql);
        
        return this.callProcedureByParams(sql, handler, paramsArray);
        
    }
    
    
    /**
     * 
     */
    @Override
    public <T> T callProcedureByMap(String sql, ResultSetHandler handler, Map<String, Object> params){
        
        Object[] paramsArray = this.getParamsByMap(params, sql);
        
        return this.callProcedureByParams(sql, handler, paramsArray);
        
    }
    
    /**
     * 
     */
    @Override
    public <T> List<T> callProcedureGetListByParams(String sql, ResultSetHandler handler, Object... params){
          
        CallableStatement ctmt = null;
        
        ResultSet rs = null;
        
        List<T> result = null;
        
        try {
            
            sql = sph.format(sequence, sql, params);
            
            ctmt = this.getConnection().prepareCall(sql.replaceAll("(\\{)(.+?)(\\})", "?"));
            
            this.fillStatement(ctmt, params);
            
            rs = ctmt.executeQuery();
            
            result = handler.processList(rs);
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }finally {
            
            close(rs, ctmt, this.getConnection());
            
        }
        
        return result;
        
    }
    
    /**
     * 
     */
    @Override
    public <T> List<T> callProcedureGetListByBean(String sql, ResultSetHandler handler, Object bean){
        
        Object[] paramsArray = this.getParamsByBean(bean, sql);

        return this.callProcedureGetListByParams(sql, handler, paramsArray);
        
    }
    
    
    /**
     * 
     */
    @Override
    public <T> List<T> callProcedureGetListByMap(String sql, ResultSetHandler handler, Map<String, Object> params){
        
        Object[] paramsArray = this.getParamsByMap(params, sql);
        
        return this.callProcedureGetListByParams(sql, handler, paramsArray);
        
    }
    
    public Object[] getParamsByMap(Map<String, Object> params, String sql){
        
        if(null == params){
            
            return null;
            
        }
        
        ArrayList<String> conditionNameList = new ArrayList<>();
        
        Pattern p1 = Pattern.compile("(?<=\\{)(.+?)(\\})");
        
        Matcher matcher;
        
        matcher = p1.matcher(sql);
        
        while (matcher.find()) {
            
            String conditionName = matcher.group(0);
            
            conditionName = conditionName.substring(0, conditionName.length() - 1).trim();
            
            conditionNameList.add(conditionName);
            
        }
        
        Object[] paramsArray = new Object[conditionNameList.size()];
        
        for(int i = 0; i < conditionNameList.size(); i++){
            
            String conditionName = conditionNameList.get(i);
            
            paramsArray[i] = params.get(conditionName);
            
        }
        
        return paramsArray;
    }
    
    
    public String getInsertSQLByBean(Object bean){
        
        String tableName = this.tranferNamingConventions(bean.getClass().getSimpleName());
        
        StringBuffer sqlHeader = new StringBuffer(" INSERT INTO " + tableName +"( ");
        
        StringBuffer sqlTail = new StringBuffer(" VALUES ( ");
        
        try {
            
            PropertyDescriptor[] props = beanProcesser.propertyDescriptors(bean.getClass());
            
            for(int i = 0; i < props.length; i++){
                
                PropertyDescriptor prop = props[i];
                
                Method getMethod = prop.getReadMethod();

                Object value = getMethod.invoke(bean, null);
                
                if(null != value){
                    
                    String key = prop.getName();
                    
                    key = this.tranferNamingConventions(key);
                    
                    if(i < (props.length - 1)){
                        
                        sqlHeader.append(key).append(", ");
                        
                        sqlTail.append("{").append(prop.getName()).append("}, ");
                    
                    }else{
                        
                        sqlHeader.append(key).append(" )");
                        
                        sqlTail.append("{").append(prop.getName()).append("}) ");
                        
                    }
                    
                }
                
            }
            
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
        }
        
        return sqlHeader.append(sqlTail).toString();
        
    }
    
    /**
     * @Description (填充sql)
     * @param params
     * @param sql
     * @return
     */
    public String getQuerySQLByBean(Object bean, StringBuffer sql){
        
        try {
            
            PropertyDescriptor[] props = beanProcesser.propertyDescriptors(bean.getClass());
            
            for(PropertyDescriptor prop : props){
                
                Method getMethod = prop.getReadMethod();

                Object value = getMethod.invoke(bean, null);
                
                if(null != value){
                    
                    String key = prop.getName();
                    
                    key = this.tranferNamingConventions(key);
                    
                    sql.append(" and ").append(key).append(" = ").append("{" + prop.getName() + "}");
                    
                }
                
            }
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
        }
        
        return sql.toString();
        
    }
    
    public String getQuerySQLByMap(Map params, StringBuffer sql){
        
        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
        
        while (it.hasNext()) {
            
            Entry<String, Object> entry = it.next();
            
            String key = entry.getKey();
            
            key = this.tranferNamingConventions(key);
            
            Object value = entry.getValue();
            
            if(null != value && value.toString().length()>0){
                
                sql.append(" and ").append(key).append(" = ").append("{" + entry.getKey() + "}");
                
            }

        }
        return sql.toString();
    }
    
    /**
     * @Description (填充Statement)
     * @param bean
     * @param sql
     * @return
     */
    public <T> Object[]  getParamsByBean(T bean, String sql){
        
        if(null == bean){
            
            return null;
            
        }
        
        Object[] paramsArray = null;
        
        try {
            
            PropertyDescriptor[] props = beanProcesser.propertyDescriptors(bean.getClass());

            props = filterPropertyDescriptor(props, sql);

            paramsArray = new Object[props.length];

            for (int i = 1; i <= props.length; i++) {

                Method getMethod = props[i - 1].getReadMethod();

                Object value = getMethod.invoke(bean, null);

                paramsArray[i-1] = value;

            }
            
        } catch (Exception e) {
            
            throw new RuntimeException(e);
            
        }

        return paramsArray;
    }
   
    
    /**
     * @Description (获得条件属性数组)
     * @param props
     * @param sql
     * @return
     */
    public PropertyDescriptor[] filterPropertyDescriptor(PropertyDescriptor[] props, String sql){
        
        ArrayList<PropertyDescriptor> propertyDescriptorList = new ArrayList<>();
        
        ArrayList<String> conditionNameList = new ArrayList<>();
        
        Pattern p1 = Pattern.compile("(?<=\\{)(.+?)(\\})");
        
        Matcher matcher;
        
        matcher = p1.matcher(sql);
        
        while (matcher.find()) {
            
            String conditionName = matcher.group(0);
            
            conditionName = conditionName.substring(0, conditionName.length() - 1).trim();
            
            conditionName = conditionName.replaceAll("_", "").toLowerCase();
            
            conditionNameList.add(conditionName);
            
        }
        
        for(String conditionName : conditionNameList){
            
            
            for(int i = 0; i<props.length; i++){
                
                PropertyDescriptor prop = props[i];
                
                String propertyName = prop.getName().replaceAll("_", "").toLowerCase();
                
                if(conditionName.equals(propertyName)){
                    
                    propertyDescriptorList.add(prop);
                    
                }
            }
        }
        
        return  propertyDescriptorList.toArray(new PropertyDescriptor[propertyDescriptorList.size()]);
        
    }
    
  
    private void fillStatement(PreparedStatement stmt, Object... params) throws SQLException{
        
        if(null == stmt){
            
            return;
            
        }
        
        for(int i =0; i < params.length; i++){
            
            Object param = params[i];
            
            if(sequence && param == null){
                
                stmt.setNull(i+1, Types.VARCHAR);
                
            }else{
          
                stmt.setObject(i +1, param);
            
            }
            
        }
        
    }
    
    public String tranferNamingConventions(String key){
        
        if(this.getNamingConventions().equals(NamingConventions.UNDERSCORE)){
            
            key = sph.camel2underscore(key);
            
        }
        
        return key;
        
    }
    
    /**
     * @Description (关闭rs stmt conn)
     * @param rs
     * @param stmt
     * @param conn
     */
    private void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            close(stmt, conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void close(Statement stmt, Connection conn) {
        try {
            if (stmt != null) {
                stmt.close();
            }
            close(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void close(Connection conn) {
        try {
            if (conn != null && conn.getAutoCommit()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 获得链接
     */
    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    
    public String getNamingConventions() {
        return namingConventions;
    }

    
    public void setNamingConventions(String namingConventions) {
        this.namingConventions = namingConventions;
    }

}
