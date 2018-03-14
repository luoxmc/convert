package com.luo.convert.controller.service;

import java.io.File;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.luo.convert.base.constant.Constant;
import com.luo.convert.controller.util.FileUtil;
import com.luo.convert.controller.util.ShellUtil;

@Service
public class MainService {
	private static Logger log = Logger.getLogger(MainService.class);
	
	/**
	 * 转换scss/less 到 css
	 * @param code
	 * @param type 0-scss 1-less
	 * @return
	 */
	public Map<String,String> convert(String code,String type){
		String savePath = Constant.FILE_SAVE_PATH;//上传文件保存路径
	    String convertPath = Constant.CONVERT_SAVE_PATH;//转换后文件保存路径
	    String suffix = ".scss";//后缀
	    String command = "scss ";//命令前缀
	    if("1".equals(type)){
	    	suffix = ".less";
	    	command = "/opt/install/node_modules/less/bin/lessc ";//我的less装的有问题，只能用全路径执行，正常的直接用lessc 就行了
	    }
		String fileName = String.valueOf(System.currentTimeMillis());//时间戳命名避免重复
		
		String prePath =  savePath + fileName + suffix;
		String afterPath = convertPath + fileName + ".css";
		
		FileUtil.createFile(prePath, code);//存放上传的代码
		
		StringBuffer sb =  new StringBuffer();
		sb.append(command);
		sb.append(prePath);
		sb.append(" ");
		sb.append(afterPath);
		if("0".equals(type)){
			sb.append(" --sourcemap=none");
		}
		String sh = sb.toString();
		Map<String,String> map = ShellUtil.callShell(sh);//执行转换脚本
		if(map.get("status").equals("0")){
			String after = FileUtil.readToString(afterPath);//读取转换后的代码
			map.put("code", after);
			log.info("转换后的文件为："+ afterPath);
		}
		return map;
	}
	
	/**
	 * 转换指定路径的压缩包内的代码
	 * @param path
	 * @param name
	 * @param type
	 * @return 转换后的文件下载路径
	 */
	public Map<String,String> covertFile(String path,String name,String type){
		String shellPath = Constant.SHELL_PATH;
		StringBuffer sb =  new StringBuffer();
		sb.append(shellPath);
		sb.append(" ");
		sb.append(path);
		sb.append(" ");
		sb.append(name);
		sb.append(" ");
		sb.append(type);
		Map<String,String> map = ShellUtil.callShell(sb.toString());//执行转换脚本
		if(map.get("status").equals("0")){
			String uploadPath = Constant.FILE_UPLOAD_PATH;
			String mappingPath = Constant.MAPPING_PATH;
			File file = new File(path + "deal_" + name);
			if(file.exists()){
				String tmp = path.replace(uploadPath, "");// /srv/www/app/convert/files/upload/20180312/544690/  转换后  20180312/544690/
				map.put("url", mappingPath + tmp + "deal_" + name);//域名加上此路径即可下载
			}else{
				map.put("status", "998");
				map.put("reason", "转换失败，请检查文件格式");
			}
		}
		return map;
	}
	
	
}
