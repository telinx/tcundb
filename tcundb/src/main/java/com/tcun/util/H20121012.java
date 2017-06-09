
package com.tcun.util;
 
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
 
/**
 * @author 码农小江
 * H20121012.java
 * 2012-10-12下午11:40:21
 */
public class H20121012 {
    /**
     * 功能：Java读取txt文件的内容
     * 步骤：1：先获得文件句柄
     * 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流
     * 4：一行一行的输出。readline()。
     * 备注：需要考虑的是异常情况
     * @param filePath
     */
    public static void readTxtFile(String filePath){
        try {
                String encoding="UTF-8";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    StringBuffer sb = new StringBuffer();
                    while((lineTxt = bufferedReader.readLine()) != null){
                        sb.append(lineTxt);
                    }
                    //System.out.println(sb);
                    Map<String, Object> map = JSON.parseObject( replaceBlank(sb.toString()),new TypeReference<Map<String, Object>>(){} );
                   
                    Iterator<Entry<String, Object>> it = map.entrySet().iterator();
                    
                    while (it.hasNext()) {
                        
                        Entry<String, Object> entry = it.next();
                        
                        String key = entry.getKey();
                        
                        System.out.println("key : " + key + "---value : " + entry.getValue());

                    }
                    
//                    map = (Map<String, Object>) map.get("executeSQL");
//                    System.out.println(map.get("getAllEmailAddressesViaResultClass"));
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
     
    }
     
    public static String replaceBlank(String str) {

        String dest = "";

        if (str != null) {

            Pattern p = Pattern.compile("\\t|\r|\n");

            Matcher m = p.matcher(str);

            dest = m.replaceAll("");
            
            p = Pattern.compile("\\s+");

            m = p.matcher(dest);
            
            dest = m.replaceAll(" ");
        }

        return dest;

    }
    
    public static void main(String argv[]) throws IOException{
        String filePath = "C:\\1.txt";
//      "res/";
        //readTxtFile(filePath);
        
        Properties pro = new Properties();
        FileInputStream in = new FileInputStream("C:\\1.properties");
        pro.load(in);
        
        System.out.println(pro);
    }
     
     
 
}