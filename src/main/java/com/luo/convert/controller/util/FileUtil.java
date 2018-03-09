package com.luo.convert.controller.util;

import java.io.*;
import org.apache.log4j.Logger;

public class FileUtil {
	private static Logger log = Logger.getLogger(FileUtil.class);
    
    /**
     * 创建文件
     * @param fileName  文件名称
     * @param filecontent   文件内容
     * @return  是否创建成功，成功则返回true
     */
    public static boolean createFile(String path,String filecontent){
        Boolean bool = false;
        File file = new File(path);
        try {
            //如果文件不存在，则创建新的文件
            if(!file.exists()){
                file.createNewFile();
                bool = true;
                log.info("success create file,the file is "+path);
                //创建文件成功后，写入内容到文件里
                writeFileContent(path, filecontent);
            }
        } catch (Exception e) {
        	throw new RuntimeException("代码内容保存失败，错误详情："+e.getMessage());
        }
        return bool;
    }
    
    /**
     * 向文件中写入内容
     * @param filepath 文件路径与名称
     * @param newstr  写入的内容
     * @return
     * @throws IOException
     */
    public static boolean writeFileContent(String filepath,String newstr) throws IOException{
        Boolean bool = false;
        String filein = newstr+"\r\n";//新写入的行，换行
        String temp  = "";
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos  = null;
        PrintWriter pw = null;
        try {
            File file = new File(filepath);//文件路径(包括文件名称)
            //将文件读入输入流
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer buffer = new StringBuffer();
            
            //文件原有内容
            for(int i=0;(temp =br.readLine())!=null;i++){
                buffer.append(temp);
                // 行与行之间的分隔符 相当于“\n”
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(filein);
            
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
        	throw new RuntimeException("代码内容保存失败，错误详情："+e.getMessage());
        }finally {
            //不要忘记关闭
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return bool;
    }
    
    /**
     * 读取文件内容
     * @param fileName
     * @return
     */
    public static String readToString(String path) {  
        String encoding = "UTF-8";
        File file = new File(path);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
        	log.error("The OS does not support " + encoding);
            return null;  
        }  
    }  
    
    /**
     * 删除文件
     * @param fileName 文件名称
     * @return
     */
    public static boolean delFile(String path){
        Boolean bool = false;
        File file  = new File(path);
        try {
            if(file.exists()){
                file.delete();
                bool = true;
            }
        } catch (Exception e) {
        	throw new RuntimeException("文件删除失败，错误详情："+e.getMessage());
        }
        return bool;
    }
	
}
