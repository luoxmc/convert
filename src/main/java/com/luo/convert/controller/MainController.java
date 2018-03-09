package com.luo.convert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luo.convert.base.response.JsonResponse;

@Controller
@RequestMapping("/api")
public class MainController {
	
	@RequestMapping("/convert")
	@ResponseBody
    public JsonResponse doConvert(){
		JsonResponse response = JsonResponse.success();
		return response;
    }
}
