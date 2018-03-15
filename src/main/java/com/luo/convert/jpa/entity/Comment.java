package com.luo.convert.jpa.entity;

import java.util.Date;
import javax.persistence.*;


@Entity
@Table(name = "T_COMMENT")
public class Comment {
		@Id
	    @Column(name = "ID")
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Integer id;
		
	    @Column(name = "NAME")
	    private String name;

	    @Column(name = "EMAIL")
	    private String email;

	    @Column(name = "CONTENT")
	    private String content;

	    @Column(name = "TYPE")
	    private Integer type;

	    @Column(name = "JOIN_ID")
	    private Integer joinId;
	    
	    @Column(name = "REPLY_ID")
	    private Integer replyId;
	    
	    @Column(name = "REPLY_NAME")
	    private String replyName;
	    
	    @Column(name = "CREATE_DATE")
	    private Date createDate;
	    
	    
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public Integer getJoinId() {
			return joinId;
		}

		public void setJoinId(Integer joinId) {
			this.joinId = joinId;
		}

		public Integer getReplyId() {
			return replyId;
		}

		public void setReplyId(Integer replyId) {
			this.replyId = replyId;
		}

		public String getReplyName() {
			return replyName;
		}

		public void setReplyName(String replyName) {
			this.replyName = replyName;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
	    
}
