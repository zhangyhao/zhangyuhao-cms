package com.zhangyuhao.dao;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.zhangyuhao.entity.Article;
 
public interface ArticleRep extends ElasticsearchRepository<Article, Integer>{

	List<Article> findByContent(String es);

	
}
