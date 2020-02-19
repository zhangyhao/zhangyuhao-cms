package com.zhangyuhao.common;

import java.io.Serializable;
/**
 * 消息统一
 * @author 23529
 *
 */
public class CmsMessage implements Serializable{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 6152592730959342206L;
 
	int code;//1 是成功 2 其他原因失败
	String error;//失败的具体的原因
	Object data;//成功的情况下返回的数据内容
	public CmsMessage(int code, String error, Object data) {
		super();
		this.code = code;
		this.error = error;
		this.data = data;
	}
	public CmsMessage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "CmsMessage [code=" + code + ", error=" + error + ", data="
				+ data + "]";
	}
	
	
	
}
