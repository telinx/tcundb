package com.tcun.util;

public enum NamingConventions {
    /**
     * 驼峰命名法则
     */
    CAMEL("camel"),
    /**
     * 下划线命名法则
     */
    UNDERSCORE("underscore");

    private String value;

    NamingConventions(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
