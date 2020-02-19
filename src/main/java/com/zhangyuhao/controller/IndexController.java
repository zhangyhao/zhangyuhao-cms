package com.zhangyuhao.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.entity.Category;
import com.zhangyuhao.entity.Channel;
import com.zhangyuhao.entity.Link;
import com.zhangyuhao.entity.Slide;
import com.zhangyuhao.service.ArticleService;
import com.zhangyuhao.web.HLUtils;

@Controller
public class IndexController {
 
	@Autowired
	ArticleService articleService;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	

	
	
	@GetMapping("index")
	public Object es(String es,HttpServletRequest request,@RequestParam(defaultValue="1")int page){
		System.out.println(es);
		/*List<Article> list = articleService.es(es);
		for (Article article : list) {
			System.out.println(article);
			PageInfo<Article> pageInfo = new PageInfo<Article>(list); 
		}*/
		long start = System.currentTimeMillis();
		PageInfo<Article> findByHighLight = (PageInfo<Article>) HLUtils.findByHighLight(elasticsearchTemplate, Article.class, page, 5, new String[]{"title"}, "id", es);
		long end = System.currentTimeMillis();
		System.err.println("本次es查询消耗了"+(end-start)+"毫秒");
		request.setAttribute("articlePage", findByHighLight);
		request.setAttribute("key", es);
		Thread t1 = new Thread(){
			@Override
			public void run() {
				//获取所有的栏目
				List<Channel> channels = articleService.getChannels();
				request.setAttribute("channels", channels);
			}
		};
		Thread t3 = new Thread(){
			@Override
			public void run() {
				//获取最新文章
				//0.redis作为缓存来优化最新文章
				//1.从redis中查询最新文章
				
				List<Article> redisArticles = redisTemplate.opsForList().range("new_articles", 0, -1);
				//2.判断redis中查询的是否为空
				if(redisArticles==null||redisArticles.size()==0){
					//3.如果为空
					//4.从mysql中查询最新文章
					List<Article> lastArticle = articleService.lastList();
					System.out.println("从mysql中查询了最新文章");
					redisTemplate.opsForList().leftPushAll("new_articles", lastArticle.toArray());
					redisTemplate.expire("new_articles", 5, TimeUnit.MINUTES);
					//并且返回给前台
					request.setAttribute("lastArticles", lastArticle);
				}else{
					//5.如果不为空,直接返回给前台
					System.out.println("从redis中查询了最新文章");
					request.setAttribute("lastArticles", redisArticles);
				}
			}
		};
		
		t1.start();
		t3.start();
		
	
		return "index";
	}
	/**
	 * 首页面
	 * @param request
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"index","/"})
	public String index(HttpServletRequest request,@RequestParam(defaultValue="1")int page) throws Exception{
		Thread t1 = new Thread(){
			@Override
			public void run() {
				//获取所有的栏目
				List<Channel> channels = articleService.getChannels();
				request.setAttribute("channels", channels);
			}
		};
		Thread t2 = new Thread(){
			@Override
			public void run() {
				//获取热门文章
				PageInfo<Article> arInfo = articleService.hotList(page);
				request.setAttribute("articlePage", arInfo);
			}
		};
		Thread t3 = new Thread(){
			@Override
			public void run() {
				//获取最新文章
				//0.redis作为缓存来优化最新文章
				//1.从redis中查询最新文章
				
				List<Article> redisArticles = redisTemplate.opsForList().range("new_articles", 0, -1);
				//2.判断redis中查询的是否为空
				if(redisArticles==null||redisArticles.size()==0){
					//3.如果为空
					//4.从mysql中查询最新文章
					List<Article> lastArticle = articleService.lastList();
					System.out.println("从mysql中查询了最新文章");
					//添加redis数据
					redisTemplate.opsForList().leftPushAll("new_articles", lastArticle.toArray());
					redisTemplate.expire("new_articles", 5, TimeUnit.MINUTES);
					//并且返回给前台
					request.setAttribute("lastArticles", lastArticle);
				}else{
					//5.如果不为空,直接返回给前台
					System.out.println("从redis中查询了最新文章");
					request.setAttribute("lastArticles", redisArticles);
				}
			}
		};
		Thread t4 = new Thread(){
			@Override
			public void run() {
				List<Slide> slides = articleService.getSlides();
				request.setAttribute("slides", slides);
			}
		};
		
		Thread t5 = new Thread(){
			@Override
			public void run() {
				List<Link> list = articleService.link();
				request.setAttribute("list", list);
			}
		};
		Thread t6 = new Thread(){
			@Override
			public void run() {
				List<Article> hot_list = redisTemplate.opsForList().range("hot_articles", 0, -1);
				if(hot_list==null||hot_list.size()==0){
					List<Article> list = articleService.hot_List();
					System.out.println("从mysql中获取热门文章");
					redisTemplate.opsForList().leftPushAll("hot_articles", list.toArray());
					redisTemplate.expire("hot_articles", 5, TimeUnit.MINUTES);
					request.setAttribute("hot_list", list);
				}else{
					System.out.println("从redis中获取热门文章");
					request.setAttribute("hot_list", hot_list);
				}
			}
		};
		
		
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		t6.join();
		return "index";
	}
	/**
	 * 点击左侧 导航
	 * @param request  请求
	 * @param channleId  栏目的id
	 * @param catId 分类的id
	 * @param page 页码
	 * @return
	 * @throws InterruptedException 
	 */
	@RequestMapping("channel")
	public String channel(HttpServletRequest request,
			int channelId,
			@RequestParam(defaultValue="0") int catId,
			@RequestParam(defaultValue="1")  int page) throws InterruptedException {
		
		Thread  t1 =  new Thread() {
			public void run() {
		// 获取所有的栏目
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
			};
		};
		
		Thread  t2 =  new Thread() {
			public void run() {
		// 当前栏目下  当前分类下的文章
		PageInfo<Article> articlePage= articleService.getArticles(channelId,catId, page);
		request.setAttribute("articlePage", articlePage);
			};
		};
		
		Thread  t3 =  new Thread() {
			public void run() {
		// 获取最新文章
		List<Article> lastArticles = articleService.lastList();
		request.setAttribute("lastArticles", lastArticles);
			};
		};
		
		Thread  t4 =  new Thread() {
			public void run() {
		// 轮播图
		List<Slide> slides = articleService.getSlides();
		request.setAttribute("slides", slides);
		
			};
		};
		
		// 获取当前栏目下的所有的分类 catId
		Thread  t5 =  new Thread() {
			public void run() {
		// 
		List<Category> categoris= articleService.getCategoriesByChannelId(channelId);
		request.setAttribute("categoris", categoris);
		System.err.println("categoris is " + categoris);
			};
		};
		Thread t6 = new Thread(){
			@Override
			public void run() {
				List<Article> hot_List = articleService.hot_List();
				request.setAttribute("hot_list", hot_List);
			}
		};
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		t6.join();
		// 参数回传
		request.setAttribute("catId", catId);
		request.setAttribute("channelId", channelId);
		
		return "channel";
	}
}
