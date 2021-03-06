package com.zhangyuhao.entity;
 
import java.io.Serializable;

//友情链接
public class Link implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2862975411761243855L;
	private Integer id;
	private String url;
	private String name;
	private String created;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	@Override
	public String toString() {
		return "Link [id=" + id + ", url=" + url + ", name=" + name
				+ ", created=" + created + "]";
	}
	public Link(Integer id, String url, String name, String created) {
		super();
		this.id = id;
		this.url = url;
		this.name = name;
		this.created = created;
	}
	public Link() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Link other = (Link) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
