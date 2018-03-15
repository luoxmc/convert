package com.luo.convert.controller.service;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.luo.convert.controller.util.HttpUtil;
import com.luo.convert.controller.util.JsonUtil;
import com.luo.convert.jpa.entity.Comment;
import com.luo.convert.jpa.repository.CommentRepository;

import net.sf.json.JSONObject;

@Service
public class CommentService {
	private static Logger log = Logger.getLogger(CommentService.class);
	
	@Autowired
	private CommentRepository commentRepository;
	
	/**
	 * 添加评论
	 * @param name
	 * @param email
	 * @param content
	 */
	public Map<String,String> addComment(HttpServletRequest request,String name,String email,String content,Integer joinId,Integer replyId,String replyName,Integer type){
		log.info("test......");
		if(StringUtils.isEmpty(name)){//姓名为空查询客户地址当作姓名
			String ip = this.getClientIpAddr(request);
			String adress = this.getAdressByIp(ip);
			if(!StringUtils.isEmpty(adress)){
				name = adress + "网友";
			}
		}
		if(StringUtils.isEmpty(name)){
			name="匿名";
		}
		Comment comment = new Comment();
		comment.setName(name);
		comment.setEmail(email);
		comment.setContent(content);
		comment.setJoinId(joinId);
		comment.setReplyId(replyId);
		comment.setReplyName(replyName);
		comment.setCreateDate(new Date());
		comment.setType(type);
		Comment bean = commentRepository.save(comment);
		Map<String,String> result = this.parseComment(bean);
		return result;
		
	}
	
	/**
	 * 分页查询 
	 * @param num 页数
	 * @return
	 */
	public List<Map<String,String>> queryPage(int num){
		int count = this.commentRepository.findCountByType(1);//总条数
		int countPage = count/5 + (count%5)>0 ? 1:0 ;
		if( countPage < num ){
			throw new RuntimeException("当前页无数据");
		}
		int start = num*5 - 5;
		int end = num*5;
		List<Comment> list = this.commentRepository.findByPage(start, end);
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		for (int i = 0; i < list.size(); i++) {
			Comment bean =list.get(i);
			Map<String,String> map = this.parseComment(bean);
			result.add(map);
		}
		return result;
	}
	
	
	
	
	/**
	* 获取ip.
	*
	* @param request the request
	* @return the client ip addr
	*/
	private String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	/**
	 * 根据ip获取地址
	 * @param ip
	 * @return
	 */
	private String getAdressByIp(String ip){
		String adress = "";
		String result = HttpUtil.sendGet("http://api.map.baidu.com/location/ip", "ak=UfCtovjtd97YegeugRoYgMMoIyqXaGyA&ip="+ip);
		try {
			JSONObject jsonObject = JSONObject.fromObject(result);
			HashMap map = JsonUtil.toHashMap(jsonObject);
			int status = (Integer) map.get("status");
			if(status == 0){
				Map content = (Map) map.get("content");
				adress = (String) content.get("address");
				if(StringUtils.isEmpty(adress)){
					Map address_detail = (Map) content.get("address_detail");
					adress = (String)address_detail.get("province") + (String)address_detail.get("city");
				}
			}
		} catch (Exception e) {
			log.error("ip获取地址，返回数据格式不正确");
		}
		return adress;
	}
	/**
	 * 转map
	 * @param bean
	 * @return
	 */
	private Map<String,String> parseComment(Comment bean){
		Map<String,String> result = new HashMap<String,String>();
		Date date = bean.getCreateDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(date);
		result.put("name", bean.getName());
		result.put("email", StringUtils.isEmpty(bean.getEmail()) ? "":bean.getEmail());
		result.put("content", bean.getContent());
		result.put("join_id", bean.getJoinId() == null ? "" : String.valueOf(bean.getJoinId()));
		result.put("reply_id", bean.getReplyId() == null ? "" : String.valueOf(bean.getReplyId()));
		result.put("reply_name", StringUtils.isEmpty(bean.getReplyName()) ? "" : bean.getReplyName());
		result.put("type", bean.getType() == null ? "":String.valueOf(bean.getType()));
		result.put("create_date", now);
		return result;
	}
	
}
