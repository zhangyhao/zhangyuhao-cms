package com.zhangyuhao.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.zhangyuhao.cms.FileUtils;
 
public class BaseController {

	@Value("${upload.path}")
	String picRootPath;
	
	@Value("${pic.path}")
	String picUrl;
	
	protected String processFile(MultipartFile file) throws IllegalStateException, IOException{
		//判断目标目录时间是否存在
		//picRootPath+""
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String subPath = sdf.format(new Date());
		File path = new File(picRootPath+"/"+subPath);
		//路径不存在则创建
		if(!path.exists()){
			path.mkdirs();
		}
		//计算新的文件名
		String suffixName = FileUtils.getSuffixName(file.getOriginalFilename());
		//随机生成文件名
		String fileName = UUID.randomUUID().toString()+suffixName;
		//文件另存
		file.transferTo(new File(picRootPath+"/"+subPath+"/"+fileName));
		return subPath+"/"+fileName;
	}
}
