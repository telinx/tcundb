package com.tcun.tcundb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.tcun.handle.BeanHandler;
import com.tcun.handle.MapHandler;
import com.tcun.process.BeanProcesser;
import com.tcun.vo.Product;

public class AppTest2 {
    
    /** 
     * 数据库驱动类名称 
     */  
    private static final String DRIVER = "com.mysql.jdbc.Driver";  
  
    /** 
     * 连接字符串 
     */  
    private static final String URL = "jdbc:mysql://localhost:3306/risk_control";  
  
    /** 
     * 用户名 
     */  
    private static final String USERNAME = "root";  
  
    /** 
     * 密码 
     */  
    private static final String USERPASSWORD = "314159"; 
    
    private static DruidDataSource getDruidDataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(USERPASSWORD);
        dataSource.setInitialSize(1);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(15);
        
        dataSource.setMaxWait(3000);
        dataSource.setTimeBetweenConnectErrorMillis(6000);
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }
    
  
    
    
    @Test
    public void testQueryForInsert(){
        
        DruidDataSource dataSource = this.getDruidDataSource();
        TcunTemplate tcun = new TcunTemplateExecutor();
        tcun.setDataSource(dataSource);
        
        String sql = "insert into product(id, name, stock, status, createDate) values ({id}, {name}, {stock}, {status}, {createDate})";
        
        Product product = new Product();
        product.setId(3);
        product.setName("孙权");
        product.setStock(199);
        product.setStatus(true);
        product.setCreateDate(new Date());
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", 6);
        params.put("name", "银环蛇");
        params.put("stock", 3310);
        params.put("status", Boolean.FALSE);
        params.put("createDate", new Date());
        
        List<Product> products = null;
        List<Map> maps = null;
        
        //System.out.println("insert : " + tcun.insertByBean(product));
//        
        //System.out.println("insert : " + tcun.insertByBean(sql, product));
// 
        //System.out.println("insert : " + tcun.insertByParams(sql, 4, "西门庆", 88, false, new Date()));
//        
        System.out.println("insert : " + tcun.insertByMap(sql, params));

    }
   
    

}
