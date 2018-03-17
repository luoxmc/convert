package com.luo.convert.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.luo.convert.base.response.JsonResponse;
import com.luo.convert.controller.service.CommentService;

@Controller
@RequestMapping("/api/comment")
public class CommentController {
	private static Logger log = Logger.getLogger(CommentController.class);
	
	@Autowired
	private CommentService service;
	
	
	@RequestMapping("/add")
	@ResponseBody
    public JsonResponse doAdd(HttpServletRequest request, @RequestParam(value="name",required=false) String name,
    		@RequestParam(value="email",required=false) String email,@RequestParam("content") String content,
    		@RequestParam(value="join_id",required=false) Integer joinId,@RequestParam(value="reply_id",required=false) Integer replyId,
    		@RequestParam(value="reply_name",required=false) String replyName,@RequestParam("type") Integer type,
    		@RequestParam("article_id") Integer articleId){
	    log.debug("...add comment...");
	    Map<String,String> map = service.addComment(request,name,email,content,joinId,replyId,replyName,type,articleId);
	    JsonResponse response = JsonResponse.success();
	    response.add("result", map);
	    return response;
    }
	
	/**
	 * 分页查询评论
	 * @param page
	 * @param articleId
	 * @return
	 */
	@RequestMapping("/queryPage")
	@ResponseBody
    public JsonResponse doQuery(@RequestParam("page_num") int page,@RequestParam("article_id") Integer articleId){
		Map<String,Object> result = service.queryPage(page,articleId);
		return JsonResponse.success().add("result", result);
    }
	
}
