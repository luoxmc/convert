package com.luo.convert.controller.service;

import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.luo.convert.jpa.entity.Comment;
import com.luo.convert.jpa.repository.CommentRepository;

@Service
public class CommentService {
	private static Logger log = Logger.getLogger(CommentService.class);
	
	@Autowired
	private CommentRepository commentRepository;
	
	public void addComment(){
		log.info("test......");
		Comment comment = new Comment();
		comment.setName("test");
		comment.setContent("content_test");
		comment.setCreateDate(new Date());
		comment.setType(1);
		this.commentRepository.save(comment);
	}
	
	/**
	 * 分页查询 
	 * @param num 页数
	 * @return
	 */
	public List<Comment> queryPage(int num){
		int count = this.commentRepository.findCountByType(1);//总条数
		int countPage = count/5 + (count%5)>0 ? 1:0 ;
		if( countPage < num ){
			throw new RuntimeException("当前页无数据");
		}
		int start = num*5 - 5;
		int end = num*5;
		return this.commentRepository.findByPage(start, end);
	}
	
}
