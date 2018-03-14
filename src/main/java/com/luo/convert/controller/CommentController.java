package com.luo.convert.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.luo.convert.base.response.JsonResponse;
import com.luo.convert.controller.service.CommentService;
import com.luo.convert.jpa.entity.Comment;

@Controller
@RequestMapping("/api/comment")
public class CommentController {
	private static Logger log = Logger.getLogger(CommentController.class);
	
	@Autowired
	private CommentService service;
	
	@RequestMapping("/add")
	@ResponseBody
    public JsonResponse doAdd(){
	    log.info("...add...");
	    service.addComment();
		return JsonResponse.success();
    }
	
	@RequestMapping("/queryPage")
	@ResponseBody
    public JsonResponse doQuery(@RequestParam("page_num") int page){
		List<Comment> list = service.queryPage(page);
		return JsonResponse.success().addData(list);
    }
	
}
