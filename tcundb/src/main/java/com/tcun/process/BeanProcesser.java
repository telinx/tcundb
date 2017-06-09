package com.tcun.process;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @ClassName BeanProcesser
 * @Description TODO(结果集转换成类)
 * @author pcdalao
 * @Date 2017年5月27日 下午4:13:00
 * @version 1.0.0
 */
public class BeanProcesser extends AbstractBeanProcesser{
    

    @Override
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        
        PropertyDescriptor[] props = this.propertyDescriptors(type);
        
        ResultSetMetaData rsmd = rs.getMetaData();
        
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);
       
        return this.createBean(rs, type, props, columnToProperty);
    }

    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
       
        List<T> results = new ArrayList<>();
        
        if(!rs.next()){
            
            return results;
            
        }
        
        PropertyDescriptor[] props = this.propertyDescriptors(type);
        
        ResultSetMetaData rsmd = rs.getMetaData();
        
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);
        
        do {
            
            results.add(this.createBean(rs, type, props, columnToProperty));
            
        } while (rs.next());
        
        return results;
    }

}
