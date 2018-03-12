package com.luo.convert.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.luo.convert.base.response.JsonResponse;
import com.luo.convert.controller.service.MainService;

@Controller
@RequestMapping("/api")
public class MainController {
	private static Logger log = Logger.getLogger(MainController.class);
	
	@Autowired
	private MainService service;
	
	@RequestMapping("/convert")
	@ResponseBody
    public JsonResponse doConvert(@RequestParam("code") String code,@RequestParam("type") String type){
	    Map<String,String> result = service.convert(code, type);
	    if("0".equals(result.get("status"))){
	    	log.info("------转换成功------");
	    	JsonResponse response = JsonResponse.success();
	    	response.addData(result);
	    	return response;
	    }else{
	    	return JsonResponse.failure(-99, result.get("reason"));
	    }
    }
	
	
	@RequestMapping(value = "/file", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse dealFile(HttpServletRequest req) {
		String status = (String) req.getAttribute("uploadStatus");
		if("0".equals(status)){
			return JsonResponse.failure(-11,(String)req.getAttribute("uploadMessage"));
		}
		String path = (String) req.getAttribute("file");
		String name = (String) req.getAttribute("name");
		String type = (String) req.getAttribute("type");
		if(StringUtils.isEmpty(path) || StringUtils.isEmpty(name) || StringUtils.isEmpty(type)){
			return JsonResponse.failure(-12,"文件信息不完整");
		}
		return JsonResponse.success();
	 }
	
}
