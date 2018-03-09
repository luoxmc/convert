package com.luo.convert.controller.util;

import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;


public class ShellUtil {
	private static Logger log = Logger.getLogger(ShellUtil.class);
	
	/**
	 * 调用shell命令
	 * @param shellString
	 */
	public static Map<String,String> callShell(String shellString) {  
		log.info("===================shell命令内容:"+shellString);
        Process ps;
        Map<String,String> result = new HashMap<String,String>();
		try {
			ps = Runtime.getRuntime().exec(shellString);
			int err_code = ps.waitFor();
			result.put("status", String.valueOf(err_code));
			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");  
	        }  
	        String info = sb.toString();
	        result.put("reason", info);
		} catch (Exception e) {
			result.put("status", "99");
			result.put("reason", e.getMessage());
		}
		return result;
    }
	
}
