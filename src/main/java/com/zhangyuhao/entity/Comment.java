package com.zhangyuhao.entity;
 
import java.util.Date;


public class Comment {
    private Integer id;
    private int articleId;
    private int userId;
    private String content;
    private Date created;
    private String userName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getArticleId() {
		return articleId;
	}
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", articleId=" + articleId + ", userId="
				+ userId + ", content=" + content + ", created=" + created
				+ ", userName=" + userName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Comment(Integer id, int articleId, int userId, String content,
			Date created, String userName) {
		super();
		this.id = id;
		this.articleId = articleId;
		this.userId = userId;
		this.content = content;
		this.created = created;
		this.userName = userName;
	}
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}
