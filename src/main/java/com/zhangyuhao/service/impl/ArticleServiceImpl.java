package com.zhangyuhao.service.impl;
 
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhangyuhao.common.CmsContant;
import com.zhangyuhao.dao.ArticleMapper;
import com.zhangyuhao.dao.ArticleRep;
import com.zhangyuhao.dao.SlideMapper;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.entity.Category;
import com.zhangyuhao.entity.Channel;
import com.zhangyuhao.entity.Comment;
import com.zhangyuhao.entity.Complain;
import com.zhangyuhao.entity.Favorite;
import com.zhangyuhao.entity.Link;
import com.zhangyuhao.entity.Slide;
import com.zhangyuhao.service.ArticleService;
@Service
public class ArticleServiceImpl implements ArticleService {

	
	@Autowired
	private ArticleMapper amapper;
	@Autowired
	private SlideMapper slidMapper;
	@Autowired
	ArticleRep rep;
	
	//查找文章表
	@Override
	public PageInfo<Article> listByUser(Integer id, int page) {
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		PageInfo<Article> articlePage= new PageInfo<Article>(amapper.listByUser(id));
		
		return articlePage;
	}
	@Override
	public int delete(int id) {
		// TODO Auto-generated method stub
		return amapper.updateStatus(id,CmsContant.ARTICLE_STATUS_DEL);
	}
	@Override
	public List<Channel> getChannels() {
		// TODO Auto-generated method stub
		return amapper.getAllChannels();
	}
	@Override
	public List<Category> getCategorisByCid(int cid) {
		// TODO Auto-generated method stub
		return amapper.getCategorisByCid(cid);
	}

	@Override
	public int add(Article article) {
		// TODO Auto-generated method stub
		return amapper.add(article);
	}
	@Override
	public Article getById(int id) {
		// TODO Auto-generated method stub
		return amapper.getById(id);
	}
	@Override
	public int update(Article article, Integer id) {
		Article articleSrc = this.getById(article.getId());
		if(articleSrc.getUserId() != id) {
			// todo 如果  不是自己的文章 需要。。。。。
		}
		return amapper.update(article);
	}
	
	@Override
	public PageInfo<Article> list(int status, int page,Integer xl1, Integer xl2) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		
		return new PageInfo<Article>(amapper.list(status,xl1,xl2));
	}
	@Override
	public Article getInfoById(int id) {
		// TODO Auto-generated method stub
		return amapper.getInfoById(id);
	}
	@Override
	public int setHot(int id, int status) {
		// TODO Auto-generated method stub
		return amapper.setHot(id,status);
	}
	@Override
	public int setCheckStatus(int id, int status) {
		// TODO Auto-generated method stub
		return amapper.CheckStatus(id,status);
	}
	@Override
	public PageInfo<Article> hotList(int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Article>(amapper.hotList());
	}
	@Override
	public List<Article> lastList() {
		// TODO Auto-generated method stub
		return amapper.lastList(CmsContant.PAGE_SIZE);
	}
	@Override
	public List<Slide> getSlides() {
		// TODO Auto-generated method stub
		return slidMapper.getSlides();
	}
	
	@Override
	public PageInfo<Article> getArticles(int channelId, int catId, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Article>(amapper.getArticles(channelId,catId));
	}
	@Override
	public List<Category> getCategoriesByChannelId(int channelId) {
		// TODO Auto-generated method stub
		return amapper.getCategoriesByChannelId(channelId);
	}
	
	@Override
	public int addComment(Comment c) {
		int result = amapper.addComment(c);
		//文章评论数自增
		if(result>0){
			amapper.increaseCOmmentCnt(c.getArticleId());
		}
		return result;
	}
	@Override
	public PageInfo<Comment> getCommetns(int id, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Comment>(amapper.getComments(id));
	}
	@Override
	public int delPl(int id,int articleId) {
		int i = amapper.delPl(id);
		if(i>0){
			amapper.delCommentCnt(articleId);
		}
		return i;
	}
	
	@Override
	public List<Link> link() {
		// TODO Auto-generated method stub
		return amapper.link();
	}
	
	@Override
	public int addUrl(Link l) {
		// TODO Auto-generated method stub
		return amapper.addUrl(l);
	}
	@Override
	public Link toupd(Integer id) {
		// TODO Auto-generated method stub
		return amapper.toup(id);
	}
	
	@Override
	public int updUrl(Link l) {
		// TODO Auto-generated method stub
		return amapper.updUrl(l);
	}
	@Override
	public int del(Integer id) {
		// TODO Auto-generated method stub
		return amapper.del(id);
	}
	@Override
	public int addComplain(@Valid Complain complain) {
		// TODO Auto-generated method stub
		//添加投诉到数据库
		int result = amapper.addCoplain(complain);
		//增加投诉数量
		if(result>0){
			amapper.increaseComplainCnt(complain.getArticleId());
		}
		return 0;
	}
	@Override
	public PageInfo<Complain> getComplains(int articleId, int page) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, CmsContant.PAGE_SIZE);
		return new PageInfo<Complain>(amapper.getComplains(articleId));
	}
	@Override
	public List<Article> es(String es) {
		// TODO Auto-generated method stub
		List<Article> list = rep.findByContent(es);
		return list;
	}
	
	@Override
	public List<Article> hot_List() {
		// TODO Auto-generated method stub
		return amapper.hot_list();
	}
	
	@Override
	public int LiuLan(int id) {
		// TODO Auto-generated method stub
		return amapper.LiuLan(id);
	}
	@Override
	public void updNum(Article article) {
		// TODO Auto-generated method stub
		amapper.updNum(article);
	}
	
	@Override
	public int favorite(Integer article_id, Integer user_id) {
		// TODO Auto-generated method stub
		return amapper.favorite(article_id,user_id);
	}
	@Override
	public Favorite selectFavo(Integer id) {
		// TODO Auto-generated method stub
		return amapper.selectFavo(id);
	}
	
	@Override
	public List<Favorite> lookFavorite(Integer id) {
		// TODO Auto-generated method stub
		return amapper.lookFavorite(id);
	}
	
	@Override
	public int delFav(Integer id) {
		// TODO Auto-generated method stub
		return amapper.delFav(id);
	}
}
