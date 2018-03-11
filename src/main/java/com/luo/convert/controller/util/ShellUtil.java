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
			BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = br.readLine()) != null) {  
	            sb.append(line).append("\n");
	            log.info("===================shell执行当前结果:"+sb.toString());
	        }
	        String info = sb.toString();
	        int err_code = ps.waitFor();
			log.info("===================shell执行状态代码:"+err_code);
			result.put("status", String.valueOf(err_code));
	        result.put("reason", info);
		} catch (Exception e) {
			result.put("status", "99");
			result.put("reason", e.getMessage());
		}
		return result;
    }
	
}
