package com.tcun.handle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.tcun.process.BeanProcesser;

public class BeanHandler<T> implements ResultSetHandler{
    
    private Class<T> type;
    
    private BeanProcesser procceser;

    public BeanHandler(Class<T> type) {
        
        super();
        
        this.type = type;
        
        this.procceser = procceser = new BeanProcesser();
        
    }

    @Override
    public T process(ResultSet rs) throws SQLException {
        
        return rs.next() ? this.getProcceser().toBean(rs, type) : null;
        
    }

    @Override
    public List<T> processList(ResultSet rs) throws SQLException {
      
        return this.getProcceser().toBeanList(rs, type);
    }
    
    public Class<T> getType() {
        return type;
    }

    
    public void setType(Class<T> type) {
        this.type = type;
    }

    
    public BeanProcesser getProcceser() {
        return procceser;
    }

    
    public void setProcceser(BeanProcesser procceser) {
        this.procceser = procceser;
    }

    
  
}
