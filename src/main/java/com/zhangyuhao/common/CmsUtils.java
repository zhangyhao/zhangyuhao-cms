package com.zhangyuhao.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.digest.DigestUtils;

public class CmsUtils {

	/**
	 * 加盐加密
	 * 
	 */ 
	public static String encry(String scr,String salt){
		/*byte[] md5 = DigestUtils.md5(salt+scr+salt);
		try {
			String enPwd = new String(md5,"UTF-8");
			return enPwd;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return salt+scr+salt;
		}*/
		return DigestUtils.md5Hex(salt+scr+salt);
	}
}
