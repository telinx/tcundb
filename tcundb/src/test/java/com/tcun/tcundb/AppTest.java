package com.tcun.tcundb;

import java.math.BigDecimal;
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
import com.tcun.handle.SingleObjectHandler;
import com.tcun.process.BeanProcesser;
import com.tcun.vo.Product;

public class AppTest {
    
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
    public void testQueryForObject(){
        DruidDataSource dataSource = this.getDruidDataSource();
        TcunTemplate tcte = new TcunTemplateExecutor();
        tcte.setDataSource(dataSource);
        
        String sql = "select id, name, stock, status, createDate from product where id={id} and name = {name} and createDate<{createDate}";
        sql = "select stock from product where id={id} and name = {name} and createDate<{createDate}";
        
        Product product = new Product();
        product.setId(1);
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("name", "扩瞳");
        params.put("createDate", "20170603");
        
        Map resuktMap = null;
        /*1.-----------------------------------------*/
        //product = tcte.queryForObjectByBean(sql, new BeanHandler<>(Product.class), product);
//        
        //resuktMap = tcte.queryForObjectByBean(sql, new MapHandler<>(HashMap.class), product);
        /*2.-----------------------------------------*/
        //product = tcte.queryForObjectByParams(sql, new BeanHandler<>(Product.class), 1);
//
        //resuktMap = tcte.queryForObjectByParams(sql, new MapHandler<>(HashMap.class), 1);
        /*3.-----------------------------------------*/
         //product = tcte.queryForObjectByBean(new BeanHandler<>(Product.class), product);
        /*4.-----------------------------------------*/
        //product = tcte.queryForObjectByMap(sql, new BeanHandler<Product>(Product.class), params);

        //resuktMap = tcte.queryForObjectByMap(sql, new MapHandler<>(HashMap.class), params);
        
        BigDecimal stock =  new BigDecimal(tcte.queryForObjectByMap(sql, new SingleObjectHandler<>(Integer.class), params)+"") ;
     
        System.out.println("status-->" + stock);
        
        //System.out.println("resuktMap--->" + JSON.toJSONString(resuktMap));
        
        //System.out.println("product--->" + JSON.toJSONString(product));
    }
    
    @Test
    public void testQueryForList(){
        DruidDataSource dataSource = this.getDruidDataSource();
        TcunTemplate tcte = new TcunTemplateExecutor();
        tcte.setDataSource(dataSource);
        
        String sql = "select id, name, stock, status, DATE_FORMAT(createDate,'%Y-%m-%d') from product where createDate<={createDate}";
        
        Product product = new Product();
        product.setId(1);
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("name", "扩瞳");
        params.put("createDate", "20170602");
        
        List<Product> products = null;
        List<Map> maps = null;
        /*1.-----------------------------------------*/
//        products = tcte.queryForListByBean(sql, new BeanHandler<>(Product.class), product);
//        
//        maps = tcte.queryForListByBean(sql, new MapHandler<>(HashMap.class), product);
        /*2.-----------------------------------------*/
//        products = tcte.queryForListByParams(sql, new BeanHandler<>(Product.class), 1);
//
//        maps = tcte.queryForListByParams(sql, new MapHandler<>(HashMap.class), 1);
        /*3.-----------------------------------------*/
          products = tcte.queryForListByBean(new BeanHandler<>(Product.class), product);
        /*4.-----------------------------------------*/
        products = tcte.queryForListByMap(sql, new BeanHandler<Product>(Product.class), params);

        maps = tcte.queryForListByMap(sql, new MapHandler<>(HashMap.class), params);
     
        
        System.out.println("maps--->" + JSON.toJSONString(maps));
        
        System.out.println("products--->" + JSON.toJSONString(products));
    }
    
    
    @Test
    public void testUpdate(){
        DruidDataSource dataSource = this.getDruidDataSource();
        TcunTemplate tcun = new TcunTemplateExecutor();
        tcun.setDataSource(dataSource);
        
        String sql = "update product set name={name} where id={id}";
        
        
        Product product = new Product();
        product.setId(6);
        product.setName("扩瞳1");
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", 35);
        params.put("name", "美女蛇1");

        
        List<Product> products = null;
        List<Map> maps = null;
       
        
        System.out.println("update : " + tcun.updateByBean(sql, product));
        System.out.println("update : " + tcun.updateByMap(sql, params));
        System.out.println("insert : " +  tcun.updateByParams(sql, "扩瞳2", 24));

    }
    
    
    @Test
    public void testQueryForInsert(){
        
        DruidDataSource dataSource = this.getDruidDataSource();
        TcunTemplate tcun = new TcunTemplateExecutor();
        tcun.setDataSource(dataSource);
        
        String sql = "insert into product(id, name, stock, status) values ({id}, {name}, {stock}, {status})";
        
        Product product = new Product();
        product.setId(6);
        product.setName("扩瞳");
        product.setStock(199);
        product.setStatus(true);
        
        Map<String, Object> params = new HashMap<>();
        params.put("id", 35);
        params.put("name", "美女蛇");
        params.put("stock", 3310);
        params.put("status", Boolean.FALSE);
        
        List<Product> products = null;
        List<Map> maps = null;
        
        System.out.println("insert : " + tcun.insertByBean(product));
        
        System.out.println("insert : " + tcun.insertByBean(sql, product));
 
        System.out.println("insert : " + tcun.insertByParams(sql, 38, "西门庆", 88, false));
        
        System.out.println("insert : " + tcun.insertByMap(sql, params));

    }
    
    @Test
    public void testQueryForBatchInsert(){
        DruidDataSource dataSource = this.getDruidDataSource();
        TcunTemplate tcun = new TcunTemplateExecutor();
        tcun.setDataSource(dataSource);
        
        String sql = "insert into product(id, name, stock, status) values ({id}, {name}, {stock}, {status})";
        
        List<Product> products = new ArrayList<>();
        
        Product product2 = new Product();
        product2.setId(40);
        product2.setName("武汉1");
        product2.setStock(11);
        product2.setStatus(true);
        
        Product product1 = new Product();
        product1.setId(41);
        product1.setName("武大郎2");
        product1.setStock(1231);
        product1.setStatus(false);
        
        products.add(product1);
        products.add(product2);
        
        
        
        List<Map<String, Object>> paramsList = new ArrayList<Map<String, Object>>();
        
        Map<String, Object> params1 = new HashMap<>();
        params1.put("id", 38);
        params1.put("name", "潘金莲");
        params1.put("stock", 3310);
        params1.put("status", false);
        
        Map<String, Object> params2 = new HashMap<>();
        params2.put("id", 39);
        params2.put("name", "母老虎");
        params2.put("stock", 3310);
        params2.put("status", true);
        
        paramsList.add(params1);
        paramsList.add(params2);

        
        //System.out.println("batchInsertByBean-->" + Arrays.toString(tcun.batchInsertByMap(sql, paramsList)));
        
        //System.out.println("batchInsertByBean-->" + Arrays.toString(tcun.batchInsertByBean(products)));
        
        System.out.println("batchInsertByBean-->" + Arrays.toString(tcun.batchInsertByBean(sql, products)));

    }
    

}
