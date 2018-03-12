package com.luo.convert.base.constant;

import com.luo.convert.controller.util.Configuration;

public interface Constant {
	
	/**上传文件保存目录**/
	String FILE_SAVE_PATH = Configuration.getString("file.savePath");
	
	/**转换后文件保存目录**/
	String CONVERT_SAVE_PATH = Configuration.getString("file.convertPath");
	
	/**批量转换上传目录**/
	String  FILE_UPLOAD_PATH = Configuration.getString("file.uploadPath");
	
	/**脚本存放路径**/
	String SHELL_PATH = Configuration.getString("file.shellPath");
	
	/**文件上传文件夹再项目中的映射路径**/
	String MAPPING_PATH = Configuration.getString("file.mappingPath");
	
}
