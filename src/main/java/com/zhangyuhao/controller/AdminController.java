package com.zhangyuhao.controller;

import io.netty.handler.codec.http.HttpRequest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhangyuhao.cms.StringUtils;
import com.zhangyuhao.common.CmsContant;
import com.zhangyuhao.common.CmsError;
import com.zhangyuhao.common.CmsMessage;
import com.zhangyuhao.dao.ArticleRep;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.entity.Favorite;
import com.zhangyuhao.entity.Link;
import com.zhangyuhao.entity.User;
import com.zhangyuhao.service.ArticleService;
 
@RequestMapping("admin")
@Controller
public class AdminController {

	@Autowired
	ArticleService articleservice;
	@Autowired
	KafkaTemplate kafkaTemplate;
	@Autowired
	ArticleRep rep;
	
	//跳转管理员页面
	@RequestMapping("index")
	public String index(){
		return "admin/index";
	}
	/**
	 * 文章管理
	 * @param request
	 * @param status
	 * @param page
	 * @param xl1
	 * @param xl2
	 * @return
	 */
	@RequestMapping("article")
	public String article(HttpServletRequest request,int status,int page,Integer xl1,Integer xl2){
		PageInfo<Article> articlePage =  articleservice.list(status,page,xl1,xl2);
		request.setAttribute("articlePage", articlePage);
		request.setAttribute("status", status);
		return "admin/article/list";
	}
	/**
	 * 设置热门
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("setArticeHot")
	@ResponseBody
	public CmsMessage setArticeHot(int id,int status){
		/**
		 * 数据合法性校验
		 */
		if(status!=0 &&status!=1){
			
		}
		if(id<0){
			
		}
		Article article = articleservice.getInfoById(id);
		if(article==null){
			
		}
		if(article.getStatus()==status){
			
		}
		int result = articleservice.setHot(id,status);
		if(result<1){
			return new CmsMessage(CmsError.FAILED_UPDATE_DB, "设置失败,请重试", null);
		}
		return new CmsMessage(CmsError.SUCCESS,"成功",null);
	}
	
	/**
	 * 审核
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("setArticeStatus")
	@ResponseBody
	public CmsMessage  setArticeStatus(int id,int status) {
		
		/**
		 * 数据合法性校验 
		 */
		Article article = articleservice.getInfoById(id);
		if(status !=1 && status!=2) {
			return new CmsMessage(CmsError.NOT_VALIDATED_ARGURMENT,"status参数值不合法",null);
		}
		if(status==1){
			rep.save(article);
			System.out.println("12312321321");
		}
		if(id<0) {
			return new CmsMessage(CmsError.NOT_VALIDATED_ARGURMENT,"id参数值不合法",null);
		}
		
		
		if(article==null) {
			return new CmsMessage(CmsError.NOT_EXIST,"数据不存在",null);
		}
		
		/**
		 * 
		 */
		if(article.getStatus()==status) {
			return new CmsMessage(CmsError.NEEDNT_UPDATE,"数据无需更改",null);
		}
		
		/**
		 *  修改数据
		 */
		int result = articleservice.setCheckStatus(id,status);
		if(result<1)
			return new CmsMessage(CmsError.FAILED_UPDATE_DB,"设置失败，请稍后再试",null);
		
		
		return new CmsMessage(CmsError.SUCCESS,"成功",null);
		
	}
	
	@RequestMapping("link")
	public Object link(HttpServletRequest request,int page){
		PageHelper.startPage(page, 5);
		List<Link> list = articleservice.link();
		PageInfo<Link> pg = new PageInfo<Link>(list);
		request.setAttribute("list", list);
		request.setAttribute("pg", pg);
		return "/admin/link";
	}
	
	@RequestMapping("addUrl")
	@ResponseBody
	public Object addUrl(Link l){
		int i = articleservice.addUrl(l);
		return i;
	}
	
	@RequestMapping("toupd")
	@ResponseBody
	public Object toupd(Integer id){
		Link l = articleservice.toupd(id);
		if(l==null){
			return new CmsMessage(CmsError.NOT_EXIST, "用户不存在", null);
		}
		return new CmsMessage(CmsError.SUCCESS, "", l);
	}
	
	@RequestMapping("upd")
	@ResponseBody
	public Object updUrl(Link l){
		int i = articleservice.updUrl(l);
		return i;
	}
	
	@RequestMapping("del")
	@ResponseBody
	public Object del(Integer id){
		int i = articleservice.del(id);
		return i;
	}
	
	@RequestMapping("favorite")
	@ResponseBody
	public Object favorite(Integer id,HttpServletRequest request){
		Article article = articleservice.getById(id);
		User user = article.getUser();
		Integer user_id = user.getId();
		Integer article_id = article.getId();
		//地址判断
		//String addr = request.getRemoteAddr();
		/*String url1 = "https:"+addr+".com";
		boolean url = StringUtils.isUrl(url1);
		System.out.println(url);*/
		Favorite f = articleservice.selectFavo(article_id);
		if(f==null){
			int i=articleservice.favorite(article_id,user_id);
			return i;
		}
		return 0;
	}
	
	@RequestMapping("shoucang")
	public Object getFavorite(HttpSession session,HttpServletRequest request,@RequestParam(defaultValue="1")int page){
		User u = (User) session.getAttribute(CmsContant.USER_Key);
		System.out.println(u.getId());
		PageHelper.startPage(page, 5);
		List<Favorite> list = articleservice.lookFavorite(u.getId());
		PageInfo<Favorite> pageInfo = new PageInfo<Favorite>(list);
		request.setAttribute("list", list);
		request.setAttribute("pageInfo", pageInfo);
		return "/admin/Favorite";
	}
	
	@RequestMapping("favDel")
	@ResponseBody
	public Object delFav(Integer id,Integer user_id,HttpSession session,HttpServletRequest request){
		User u = (User) session.getAttribute(CmsContant.USER_Key);
		if(u.getId()!=user_id){
			request.setAttribute("err", "不是登录用户的数据");
		}
		int i = articleservice.delFav(id);
		return i;
		
	}
}
