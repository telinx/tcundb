package com.tcun.statement;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcun.util.TimeUtil;

/**
 * @ClassName PreparedStatementHandler
 * @Description (sql执行预处理)
 * @author pcdalao
 * @Date 2017年5月31日 上午10:30:13
 * @version 1.0.0
 */
public final class SQLPreparedHandler {
    
	private static SQLPreparedHandler sph = null;
	
	private SQLPreparedHandler(){
	    
	    
	}
	
	

	public static SQLPreparedHandler getInstance() {
	    
	    if(null == sph){
	        
	        synchronized(SQLPreparedHandler.class){
	            
	            if(null == sph){
	                
	                sph = new SQLPreparedHandler();
	                
	            }
	            
	        }
	        
	    }
	    
		return sph;
		
	}


	/**
	 * @Description (参数为date时，将其日期格式化返回)
	 * @param params
	 */
	public void formatParams(Object... params) {
	    
		for (int i = 0, cols = params.length; i < cols; i++) {
		    
			Object value = params[i];
			
			if (value == null){
			    
			    continue;
			    
			}
				
			if (value instanceof Date) {
			    
				params[i] = TimeUtil.getDateTime(value, null);
				
			} else if (value.getClass().isEnum()) {
			    
				params[i] = params[i].toString();
				
			} else if (value instanceof Boolean) {
			    
				params[i] = (Boolean) value ? 1 : 0;
				
			}
		}
	}

	/**
	 * @Description (参数为日期时，将sql中日期？格式化为to_date(?,'yyyy-mm-dd hh24:mi:ss'),返回)
	 * @param sequence
	 * @param sql
	 * @param params
	 * @return
	 */
	public String format(boolean sequence, String sql, Object[] params) {
	    
		int cols = params.length;
		
		Object[] args = new Object[cols];
		
		boolean found = false; // 包含时间参数
		
		for (int i = 0; i < cols; i++) {
		    
			args[i] = "?";
			
			Object value = params[i];
			
			if (value == null)
			    
				continue;
			
			if (value instanceof Date) {  
			    
				if (sequence) {
				    
					args[i] = "to_date(?,'yyyy-mm-dd hh24:mi:ss')";
					
					found = true;
					
				}
				
				params[i] = TimeUtil.getDateTime(value, null);
				
			} else if (value.getClass().isEnum()) {
			    
				params[i] = value.toString();
				
			}
			
		}
		
		if (found) {
	 
		    String format = sql.replaceAll("(\\{)(.+?)(\\})", "%s");
			
			sql = String.format(format, args);
			
		}
		
		System.out.println("excute SQL：" + sql + ", values :" + Arrays.toString(params));
		
		return sql;
		
	}
	

	/**
	 * @Description (根据类名得到表名，如userAccountInfo -》 user_account_info )
	 * @param camel
	 * @return
	 */
	public static String camel2underscore(String camel) {
	    
		camel = camel.replaceAll("([a-z])([A-Z])", "$1_$2");
		
		return camel.toLowerCase();
		
	}
	
	/**
	 * @Description (根据表名得到类型如-》user_account_info -》 userAccountInfo)
	 * @param underscore
	 * @return
	 */
	public static String underscore2camel(String underscore) {
	    
		if (!underscore.contains("_")) {
		    
			return underscore;
			
		}
		StringBuffer buf = new StringBuffer();
		
		underscore = underscore.toLowerCase();
		
		Matcher m = Pattern.compile("_([a-z])").matcher(underscore);
		
		while (m.find()) {
		    
			m.appendReplacement(buf, m.group(1).toUpperCase());
			
		}
		
		return m.appendTail(buf).toString();
		
	}
	

	/**
	 * @Description (语句和参数调整，而后输出到控制台)
	 * @param sql
	 * @param params
	 */
	public static void printOut(final String sql, final Object[] params) {
	    
		if (!match(sql, params)) {
		    
			System.out.println(sql);
			
			return;
			
		}

		int cols = params.length;
		
		Object[] values = new Object[cols];
		
		System.arraycopy(params, 0, values, 0, cols);
		
		for (int i = 0; i < cols; i++) {
		    
			Object value = values[i];
			
			if (value instanceof Date) {
			    
				values[i] = TimeUtil.getDateTime(value, null);
				
			} else if (value instanceof String) {
			    
				values[i] = toQuote(value);
				
			} else if (value instanceof Boolean) {
			    
				values[i] = (Boolean) value ? 1 : 0;
				
			}
		}
		
		String statement = String.format(sql.replaceAll("\\?", "%s"), values);
		
		System.out.println(statement);
	}

	/**
	 * @Description ( 判断sql中?和参数的实际个数是否匹配)
	 * @param sql
	 * @param params
	 * @return
	 */
	private static boolean match(String sql, Object[] params) {
	    
		Matcher m = Pattern.compile("(\\?)").matcher(sql);
		
		int count = 0;
		
		while (m.find()) {
		    
			count++;
			
		}
		
		return count == params.length;
		
	}

	private static String toQuote(Object value) {
	    
		return "'" + value + "'";
		
	}
}
