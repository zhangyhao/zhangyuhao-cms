package com.zhangyuhao.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;

import com.alibaba.fastjson.JSON;
import com.zhangyuhao.dao.ArticleRep;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.service.ArticleService;
 
public class ArticlesListener implements MessageListener<String, String>{

	@Autowired
	ArticleService serveice;
	
	@Autowired
	ArticleRep rep;
	//这个方法就是监听消息的方法
	@Override
	public void onMessage(ConsumerRecord<String, String> data) {
		// 接收消息
		String value = data.value();
		System.err.println(value);
		//把json对象类型的串,转程article对象
		Article article = JSON.parseObject(value, Article.class);
		//保存到mysql数据库中
		serveice.add(article);
		/*if(value.startsWith("add")){
			int indexOf = value.indexOf("=");
			Article ar = JSON.parseObject(value.substring(indexOf+1), Article.class);
			rep.save(ar);
		}*/
		
	}

}
