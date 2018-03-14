package com.luo.convert.jpa.entity;

import java.util.Date;
import javax.persistence.*;


@Entity
@Table(name = "T_COMMENT")
public class Comment {
		@Id
	    @Column(name = "ID")
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
	    @Column(name = "NAME")
	    private String name;

	    @Column(name = "EMAIL")
	    private String email;

	    @Column(name = "CONTENT")
	    private String content;

	    @Column(name = "TYPE")
	    private Integer type;

	    @Column(name = "JOIN_ID")
	    private int joinId;
	    
	    @Column(name = "REPLY_ID")
	    private int replyId;
	    
	    @Column(name = "REPLY_NAME")
	    private int replyName;
	    
	    @Column(name = "CREATE_DATE")
	    private Date createDate;
	    
	    
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
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

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public int getJoinId() {
			return joinId;
		}

		public void setJoinId(int joinId) {
			this.joinId = joinId;
		}

		public int getReplyId() {
			return replyId;
		}

		public void setReplyId(int replyId) {
			this.replyId = replyId;
		}

		public int getReplyName() {
			return replyName;
		}

		public void setReplyName(int replyName) {
			this.replyName = replyName;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
	    
}
