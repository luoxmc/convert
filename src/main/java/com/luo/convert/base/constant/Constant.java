package com.luo.convert.base.constant;

import com.luo.convert.controller.util.Configuration;

public interface Constant {
	
	/**上传文件保存目录**/
	String FILE_SAVE_PATH = Configuration.getString("file.savePath");
	
	/**转换后文件保存目录**/
	String CONVERT_SAVE_PATH = Configuration.getString("file.convertPath");
	
}
