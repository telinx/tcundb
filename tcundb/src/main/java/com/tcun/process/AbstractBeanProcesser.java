package com.tcun.process;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tcun.util.ClassUtil;



public abstract class AbstractBeanProcesser implements Processer{
    
    private static final int PROPERTY_NOT_FOUND = -1;
    
    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<>();
    
    static{
        
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
        
    }
  
    /**
 
     * @Description (输入结果集、类的类型、，结果转成Bean， 描述Java Bean的属性、列数组，将结果集转成Bean)
     * @param rs
     * @param type
     * @param props
     * @param columnToProperty
     * @return
     * @throws SQLException
     */
    public <T> T createBean(ResultSet rs, Class<T> type, PropertyDescriptor[] props, int[] columnToProperty)
            throws SQLException{
        
        T bean = ClassUtil.newInstance(type);
        
        for(int i = 1; i < columnToProperty.length; i ++){
            
            if(columnToProperty[i] == PROPERTY_NOT_FOUND){
                
                continue;
                
            }
            
            PropertyDescriptor prop = props[columnToProperty[i]];
            
            Class<?> propType = prop.getPropertyType();
            
            Object value = this.processColumn(rs, i, propType);
            
            if(propType != null && value == null && propType.isPrimitive()){
                
                value = primitiveDefaults.get(propType);
                
            }
            
            this.callSetter(bean, prop, value);
            
        }
        
        return bean;
    }
    
    /**
     * @Description (将值赋值给枚举)
     * @param enumType
     * @param value
     * @return
     * @throws SQLException
     */
    public Enum<?> valueOf(Class<?> enumType, Object value) throws SQLException{
        
        try {

            Enum<?>[] elements = (Enum<?>[]) enumType.getMethod("values").invoke(enumType);

            boolean ordinal = String.valueOf(elements[0].ordinal()).equals(elements[0].toString());

            String readMethodName = ordinal ? "ordinal" : "name";

            if (ordinal && String.class.isInstance(value)) {
                
                value = Integer.parseInt(String.valueOf(value));
                
            }

            Method readMethod = enumType.getMethod(readMethodName);
            
            for (Enum<?> element : elements) {
                
                if (readMethod.invoke(element).equals(value)) {
                    
                    return element;
                    
                }
            }
            
            return null;
            
        } catch (Exception e) {
            
            throw new SQLException(e);
            
        }
        
    }
 
    /**
     * @Description (调用实例的setter方法，将值赋给实例属性)
     * @param target
     * @param props
     * @param value
     */
    public void callSetter(Object target, PropertyDescriptor prop, Object value) throws SQLException{
        
        Method setter = prop.getWriteMethod();
        
        if(null == setter){
            
            return;
            
        }
        
        Class<?>[] params = setter.getParameterTypes();
        
        try {
            
            if(value instanceof java.util.Date){
                
                final String dateType = params[0].getName();
                
                if("java.sql.Date".equals(dateType)){
                    
                    value = new java.sql.Date(((java.util.Date)value).getTime());
                    
                }else if ("java.sql.Time".equals(dateType)) {
                    
                    value = new java.sql.Time(((java.util.Date) value).getTime());
                    
                } else if ("java.sql.Timestamp".equals(dateType)) {
                    
                    value = new java.sql.Timestamp(((java.util.Date) value).getTime());
                    
                }
            }
            
            if(params[0].isEnum()){
                
                if(BigDecimal.class.isInstance(value)){
                    
                    value = ((BigDecimal)value).intValue();
                    
                }
                
                final Class<?> enumType = params[0];
                
                if (String.class.isInstance(value)) {
                    
                    value = valueOf(enumType, value);
                    
                } else if (Integer.class.isInstance(value)) {
                    
                    value = valueOf(enumType, value);
                    
                }
                
            }
            
            if (this.isCompatibleType(value, params[0])) {
                
                setter.invoke(target, new Object[] { value });
                
            } else {
                
                throw new SQLException("Cannot set " + prop.getName() 
                
                        + ": incompatible types, cannot convert "
                        
                        + value.getClass().getName() + " to "
                        
                        + params[0].getName());
                
            }
            
        } catch (Exception e) {
           
            throw new SQLException("Cannot set " + prop.getName() + ": " + e.getMessage());
            
        }
        
    }
    
    /**
     * @Description (返回判断是否有关系)
     * @param value
     * @param Type
     * @return
     */
    public boolean isCompatibleType(Object value, Class<?> type){
        
        if (value == null || type.isInstance(value)) {
            
            return true;
            
        } else if (type.equals(Integer.TYPE) && Integer.class.isInstance(value)) {
            
            return true;    

        } else if (type.equals(Long.TYPE) && Long.class.isInstance(value)) {
            
            return true;  

        } else if (type.equals(Double.TYPE) && Double.class.isInstance(value)) {
            
            return true;

        } else if (type.equals(Float.TYPE) && Float.class.isInstance(value)) {
            
            return true;

        } else if (type.equals(Short.TYPE) && Short.class.isInstance(value)) {
            
            return true;

        } else if (type.equals(Byte.TYPE) && Byte.class.isInstance(value)) {
            
            return true;

        } else if (type.equals(Character.TYPE) && Character.class.isInstance(value)) {
            
            return true;

        } else if (type.equals(Boolean.TYPE) && Boolean.class.isInstance(value)) {
            
            return true;

        } else if (type.isEnum() && Integer.class.isInstance(value)) {
            
            return true;
            
        } else if (type.isEnum() && String.class.isInstance(value)) {
            
            return true;
            
        }
        
        return false;
        
    }
    
  
    
    /**
     * @Description (输入类得到该类的属性)
     * @param c
     * @return
     * @throws SQLException
     */
    public PropertyDescriptor[] propertyDescriptors(Class<?> c) throws SQLException{
        
        BeanInfo beanInfo = null;
        
        try {
            
            beanInfo = Introspector.getBeanInfo(c);
            
        } catch (IntrospectionException e) {
            
            throw new SQLException("Bean introspection failed: "+ e.getMessage());
            
        }
        
        PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        
        
        props = Arrays.copyOfRange(props, 1, props.length);
        
        return props;
        
    }
  
    
    /**
     * ResultSetMetaData可用于获取关于 ResultSet 对象中列的类型和属性信息的对象
     * @Description (对象的属性与结果集列对应，如Person： age, name, address,列： name, age, address,则返回的是[1, 0, 2])
     * @param rsmd
     * @param props
     * @return
     * @throws SQLException
     */
    public int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException{
        
        int cols = rsmd.getColumnCount();
        
        int[] columnToProperty = new int[cols + 1];
        
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);
        
        Map<String, Integer> propertyMap = new HashMap<String, Integer>();
        
        for(int i = 0; i<props.length; i++){
            
            PropertyDescriptor prop = props[i];
            
            propertyMap.put(prop.getName().replaceAll("_", "").toLowerCase(), i);
            
        }
        
        for(int col = 1; col <= cols; col++){
            
            String columnName = rsmd.getColumnLabel(col);
            
            columnName = (null == columnName || 0 == columnName.length()) ? rsmd.getColumnName(col) : columnName;
            
            String propertyName = columnName.replaceAll("_", "").toLowerCase();
            
            if(propertyMap.containsKey(propertyName)){
                
                columnToProperty[col] = propertyMap.get(propertyName);
                
            }
            
        }
        
        return columnToProperty;
    } 
    
    /**
     * @Description (处理结果集第几列的)
     * @param rs
     * @param index
     * @param propType
     * @return
     * @throws SQLException
     */
    public Object processColumn(ResultSet rs, int index, Class<?> propType)throws SQLException {
        
        if(!propType.isPrimitive() && rs.getObject(index) == null){
            
            return null;
            
        }
        if(propType.equals(String.class)){
            
            return rs.getString(index);
            
        }else if(propType.equals(Integer.TYPE) || propType.equals(Integer.class)){
            
            return Integer.valueOf(rs.getInt(index));
            
        }else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            
            return Long.valueOf(rs.getLong(index));

        } else if (propType.equals(Double.TYPE) || propType.equals(Double.class)) {
           
            return Double.valueOf(rs.getDouble(index));
            

        } else if (propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            
            return Float.valueOf(rs.getFloat(index));
            

        } else if (propType.equals(Short.TYPE) || propType.equals(Short.class)) {
           
            return Short.valueOf(rs.getShort(index));
            

        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            
            return Byte.valueOf(rs.getByte(index));
            
        } else if (propType.equals(Timestamp.class)) {
            
            return rs.getTimestamp(index);
            
        } else if (propType.equals(Date.class)) {
            
            return rs.getTimestamp(index);
            
        } else if (propType.equals(SQLXML.class)) {
            
            return rs.getSQLXML(index);
            
        } else {
            
            return rs.getObject(index);
            
        }
    }
}
