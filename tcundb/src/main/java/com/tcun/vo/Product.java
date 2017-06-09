package com.tcun.vo;


import java.util.Date;

public class Product {
	
	private Integer id;
	
	private String name;
	
	private Integer stock;
	
	private Boolean status;
	
	private Date createDate;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public Integer getStock() {
        return stock;
    }

    
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    
    public Boolean getStatus() {
        return status;
    }

    
    public void setStatus(Boolean status) {
        this.status = status;
    }


    
    public Date getCreateDate() {
        return createDate;
    }


    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
	


	

}
