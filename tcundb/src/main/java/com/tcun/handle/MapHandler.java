package com.tcun.handle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tcun.util.ClassUtil;

public class MapHandler<T> implements ResultSetHandler{
    
    private Class<T> type;
    
    public MapHandler(Class<T> type) {
        
        super();
        
        this.type = type;
        
    }

    @Override
    public T process(ResultSet rs) throws SQLException {
        
        
         return rs.next()? this.toMap(type, rs) : null;
        
    }

    @Override
    public List<T> processList(ResultSet rs) throws SQLException {
        
        List<T> results = new ArrayList<>();
        
        while(rs.next()){
            
            results.add(this.toMap(type, rs));
            
        }
        
        return results;
        
    }
    
    
    public <T> T toMap(Class<T> type, ResultSet rs) throws SQLException{
        
        if(type.getSimpleName().indexOf("Map") > 0){
            
            T bean = ClassUtil.newInstance(type);
            
            Map resultMap = (Map) bean;
            
            ResultSetMetaData rsmd = rs.getMetaData();
            
            int cols = rsmd.getColumnCount();
            
            for(int col = 1; col <= cols; col++){
                
                String columnName = rsmd.getColumnLabel(col);
                
                if(null != columnName && columnName.length() > 0){
                    
                    resultMap.put(columnName, rs.getObject(col));
                    
                }  
            }
            
            return bean;
             
         }
         
        return null;
        
    }
    
    

    @Override
    public Class<T> getType() {
        
        return null;
    }

}
