package com.zhangyuhao.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.zhangyuhao.cms.StringUtils;
import com.zhangyuhao.common.CmsContant;
import com.zhangyuhao.common.CmsError;
import com.zhangyuhao.common.CmsMessage;
import com.zhangyuhao.dao.ArticleRep;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.entity.Channel;
import com.zhangyuhao.entity.Comment;
import com.zhangyuhao.entity.Complain;
import com.zhangyuhao.entity.User;
import com.zhangyuhao.service.ArticleService;
import com.zhangyuhao.web.HLUtils;
 
@Controller
@RequestMapping("article")
public class ArticleController extends BaseController{

	@Autowired
	ArticleService aservice;
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
	//注入spring的线程池
	 @Autowired
	 ThreadPoolTaskExecutor executor;
	
	/**
	 * 点击审核，返回数据
	 * @param id
	 * @return
	 */
	@RequestMapping("getDetail")
	@ResponseBody
	public CmsMessage getDetail(int id){
		System.out.println(id);
		if(id<=0){
			
		}
		//获取文章详情
		Article article = aservice.getById(id);
		//不存在
		if(article==null){
			return new CmsMessage(CmsError.NOT_EXIST,"文章不存在", null);
		}
		//返回数据
		return new CmsMessage(CmsError.SUCCESS, "", article);
	}
	/**
	 * 轮播图下方列表，根据id查询文章
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("detail")
	public String detail(HttpServletRequest request,int id){
		Article article = aservice.getById(id);
		request.setAttribute("article", article);
		System.out.println(article);
		String user_ip = request.getRemoteAddr();
		String key = "nums"+id+user_ip;
		String redisKey = (String) redisTemplate.opsForValue().get(key);
		if(redisKey==null){
			
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					article.setNum(article.getNum()+1);
					aservice.updNum(article);
					redisTemplate.opsForValue().set(key, "", 5, TimeUnit.MINUTES);
				}
			});
			
			
			/*System.out.println("还没有");
			Integer articleId = article.getId();
			 User user = article.getUser();
			 Integer user_id = user.getId();
			System.err.println(articleId);
			System.err.println(user_id);
			ArrayList arrayList = new ArrayList();
			arrayList.add(articleId);
			arrayList.add(user_id);
			redisTemplate.opsForList().leftPushAll("LiuLan_Article", arrayList);
			redisTemplate.expire("LiuLan_Article", 5, TimeUnit.MINUTES);
		}else{
			System.out.println("有了");
			System.out.println("===="+range);*/
		}
		
		
		
		return "detail";
	}
	/**
	 * 发布评论
	 * @param request
	 * @param articleId
	 * @param content
	 * @return
	 */
	@RequestMapping("postcomment")
	@ResponseBody
	public CmsMessage postcomment(HttpServletRequest request,int articleId,String content){
		User login = (User) request.getSession().getAttribute(CmsContant.USER_Key);
		//未登录
		if(login==null){
			return new CmsMessage(CmsError.NOT_LOGIN, "您尚未登录", null);
		}
		Comment c = new Comment();
		c.setUserId(login.getId());
		c.setContent(content);
		c.setArticleId(articleId);
		int result = aservice.addComment(c);
		if(result>0){
			return new CmsMessage(CmsError.SUCCESS,"成功",null);
		}
		return new CmsMessage(CmsError.FAILED_UPDATE_DB, "异常原因失败", null);
	}
	
	/**
	 * 获取评论
	 * @param request
	 * @param id
	 * @param page
	 * @return
	 */
	@RequestMapping("comments")
	public String comments(HttpServletRequest request,int id,int page){
		PageInfo<Comment> commentPage = aservice.getCommetns(id,page);
		request.setAttribute("commentPage", commentPage);
		return "comments";
	}
	
	@RequestMapping("delPl")
	public String delPl(int id,int articleId){
		System.out.println(id);
		System.out.println(articleId);
		int result = aservice.delPl(id,articleId);
		return "redirect:/article/comments";
	}
	/**
	 * 跳转到投诉的页面
	 * @param request
	 * @param articleId
	 * @return
	 */
	@RequestMapping(value="complain",method=RequestMethod.GET)
	public Object complain(HttpServletRequest request,int articleId){
		Article article = aservice.getById(articleId);
		request.setAttribute("article", article);
		Complain complain = new Complain();
		complain.setArticleId(articleId);
		request.setAttribute("complain",complain );
		return "article/complain";
	}
	/**
	 * 接收投诉页面提交的数据
	 * @param request
	 * @param complain
	 * @param file
	 * @param result
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@RequestMapping(value="complain",method=RequestMethod.POST)
	public Object complain(HttpServletRequest request,@ModelAttribute("complain")@Valid
			Complain complain,BindingResult result,MultipartFile file) throws IllegalStateException, IOException{
			if(!StringUtils.isUrl(complain.getScrUrl())){
				result.rejectValue("scrUrl", "", "不是合法的Url地址");
			}
			if(result.hasErrors()){
				return "article/complain";
			}
			User user = (User) request.getSession().getAttribute(CmsContant.USER_Key);
			String picUrl = this.processFile(file);
			complain.setPicture(picUrl);
			
			//加上投诉人
			if(user!=null){
				complain.setUserId(user.getId());
			}else{
				complain.setUserId(0);
			}
			aservice.addComplain(complain);
		return "redirect:/article/detail?id="+complain.getArticleId();
	}
	
	@RequestMapping("complains")
	public String complains(HttpServletRequest request,int articleId,
			@RequestParam(defaultValue="1")int page){
			PageInfo<Complain> complianPage = aservice.getComplains(articleId,page);
			request.setAttribute("complianPage", complianPage);
			return "article/complainslist";
	}
	
	
	
}
