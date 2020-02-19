package com.zhangyuhao.service;

import java.util.List;

import javax.validation.Valid;

import com.github.pagehelper.PageInfo;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.entity.Category;
import com.zhangyuhao.entity.Channel;
import com.zhangyuhao.entity.Comment;
import com.zhangyuhao.entity.Complain;
import com.zhangyuhao.entity.Favorite;
import com.zhangyuhao.entity.Link;
import com.zhangyuhao.entity.Slide;
 
public interface ArticleService {

	PageInfo<Article> listByUser(Integer id, int page);

	int delete(int id);

	List<Channel> getChannels();


	List<Category> getCategorisByCid(int cid);

	int add(Article article);

	int update(Article article, Integer id);

	Article getById(int id);

	PageInfo<Article> list(int status, int page, Integer xl1, Integer xl2);

	Article getInfoById(int id);

	int setHot(int id, int status);

	int setCheckStatus(int id, int status);

	PageInfo<Article> hotList(int page);

	List<Article> lastList();

	List<Slide> getSlides();

	PageInfo<Article> getArticles(int channelId, int catId, int page);

	List<Category> getCategoriesByChannelId(int channelId);

	int addComment(Comment c);

	PageInfo<Comment> getCommetns(int id, int page);

	int delPl(int id, int articleId);

	List<Link> link();

	int addUrl(Link l);

	Link toupd(Integer id);

	int updUrl(Link l);

	int del(Integer id);

	int addComplain(@Valid Complain complain);

	PageInfo<Complain> getComplains(int articleId, int page);

	List<Article> es(String es);

	List<Article>  hot_List();

	int LiuLan(int id);

	void updNum(Article article);

	int favorite(Integer article_id, Integer user_id);

	Favorite selectFavo(Integer id);

	List<Favorite> lookFavorite(Integer id);

	int delFav(Integer id);

}
