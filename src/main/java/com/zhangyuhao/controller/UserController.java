package com.zhangyuhao.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.zhangyuhao.cms.FileUtils;
import com.zhangyuhao.cms.HtmlUtils;
import com.zhangyuhao.cms.StringUtils;
import com.zhangyuhao.common.CmsContant;
import com.zhangyuhao.entity.Article;
import com.zhangyuhao.entity.Category;
import com.zhangyuhao.entity.Channel;
import com.zhangyuhao.entity.User;
import com.zhangyuhao.service.ArticleService;
import com.zhangyuhao.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
 
	@Value("${upload.path}")
	String picRootPath;
	
	@Value("${pic.path}")
	String picUrl;
	
	@Autowired
	private UserService service;
	
	@Autowired
	private ArticleService articleService;
	
	@RequestMapping("home")
	public String home(){
		return "user/home";
	}
	/**
	 * 跳转登录界面
	 * @return
	 */
	@RequestMapping(value="login",method=RequestMethod.GET)
	public String login(){
		return "user/login";
	}
	/**
	 * 接受登录界面的请求
	 * @param u
	 * @param request
	 * @return
	 */
	 
	@RequestMapping(value="login",method=RequestMethod.POST)
	public String login(User u,HttpServletRequest request,HttpServletResponse response){
		String pwd = new String(u.getPassword());
		User user = service.login(u);
		//登录失败
		if(user==null){
			request.setAttribute("err", "用户名密码错误");
			return "/user/login";
		}
		//登录成功,用户信息存放看到session当中
		request.getSession().setAttribute(CmsContant.USER_Key, user);
		 
		Cookie cookieUserName = new Cookie("username", user.getUsername());
		cookieUserName.setPath("/");
		cookieUserName.setMaxAge(10*24*3600);
		response.addCookie(cookieUserName);
		
		Cookie cookieUserPwd = new Cookie("userpwd", pwd);
		cookieUserPwd.setPath("/");
		cookieUserPwd.setMaxAge(10*24*3600);
		response.addCookie(cookieUserPwd);
		//进入管理界面
		if(user.getRole()==CmsContant.USER_ROLE_ADMIN){
			return "redirect:/admin/index";
		}
		//进入个人中心
		return "redirect:/user/home";
	}
	
	/**
	 * 跳转到注册页面
	 * @param request
	 * @param u
	 * @return
	 */
	@RequestMapping(value="regirct",method=RequestMethod.GET)
	public String regirect(HttpServletRequest request){
		User u = new User();
		request.setAttribute("u", u);
		return "user/regirect";
	}
	
	/*
	 * 从注册页面发过来的请求
	 */
	@RequestMapping(value="regirct",method=RequestMethod.POST)
	public String regirect(HttpServletRequest request,@Valid@ModelAttribute("u")User u,BindingResult result){
		if(result.hasErrors()){
			return "user/regirect";
		}
		//进行唯一验证
		User n = service.getUserByUsername(u.getUsername());
		
		if(n!=null){
			result.rejectValue("username", "", "用户名已经存在");
			return "user/regirect";
		}
		if(StringUtils.isNumber(u.getPassword())){
			result.rejectValue("password","", "密码不能全是数字");
			return "user/regirect";
		}
		int i = service.regirect(u);
		if(i<1){
			request.setAttribute("err", "注册失败,请稍后重试");
			return "/user/regirect";
		}
		return "redirect:/user/login";
	}
	
	@RequestMapping("checkname")
	@ResponseBody
	public boolean checkUserName(String username){
		User user = service.getUserByUsername(username);
		return user==null;
	}
	//查询文章表
	@RequestMapping("articles")
	public String articles(HttpServletRequest request,@RequestParam(defaultValue="1")int page){
		User u = (User) request.getSession().getAttribute(CmsContant.USER_Key);
		PageInfo<Article> articlePage = articleService.listByUser(u.getId(),page);
		request.setAttribute("articlePage", articlePage);
		
		return "/article/list";
	}
	//删除
	@RequestMapping("deletearticle")
	@ResponseBody
	public boolean deletearticle(int id){
		int result = articleService.delete(id);
		return result>0;
	}
	/**
	 * 跳转到发布文章的页面
	 * @param request
	 * @return
	 */
	@RequestMapping("postArticle")
	public String postArticle(HttpServletRequest request){
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
		return "/article/post";
	}
	/**
	 * 获取分类
	 * @param cid
	 * @return
	 */
	@RequestMapping("getCategoris")
	@ResponseBody
	public List<Category> getCategoris(int cid){
		List<Category> categoris = articleService.getCategorisByCid(cid);
		return categoris;
	}
	/**
	 * 发布文章
	 * @param request
	 * @param article
	 * @param file
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping(value="postArticle",method=RequestMethod.POST)
	@ResponseBody
	public boolean postArticle(HttpServletRequest request,Article article,MultipartFile file){
		String picUrl;
		try {
			//处理上传文件
			picUrl = processFile(file);
			article.setPicture(picUrl);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//当前用户是文章的作者
		User loginUser = (User) request.getSession().getAttribute(CmsContant.USER_Key);
		article.setUserId(loginUser.getId());
		return articleService.add(article)>0;
	}
	
	private String processFile(MultipartFile file) throws IllegalStateException, IOException{
		//判断目标目录时间是否存在
		//picRootPath+""
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String subPath = sdf.format(new Date());
		//图片存放的路径
		File file2 = new File(picRootPath+"/"+subPath);
		//路径不存在则创建
		if(!file2.exists()){
			file2.mkdirs();
		}
		//计算新的文件名称
		String suffixName = FileUtils.getSuffixName(file.getOriginalFilename());
		//随机生成文件名
		String fileName = UUID.randomUUID().toString()+suffixName;
		//文件另存
		file.transferTo(new File(picRootPath+"/"+subPath+"/"+fileName));
		return subPath+"/"+fileName;
	}
	
	/**
	 * 跳转到修改文章的页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="updateArticle",method=RequestMethod.GET)
	public String updateArticle(HttpServletRequest request,int id){
		//获取栏目
		List<Channel> channels = articleService.getChannels();
		request.setAttribute("channels", channels);
		//获取文章
		Article article = articleService.getById(id);
		User loginUser = (User) request.getSession().getAttribute(CmsContant.USER_Key);
		if(loginUser.getId()!=article.getUserId()){
			//准备做异常处理的
		}
		request.setAttribute("article", article);
		request.setAttribute("content1", HtmlUtils.htmlspecialchars(article.getContent()));
		return "/article/update";
	}
	
	
	/**
	 * 接受修改文章的页面提交的数据
	 * @param request
	 * @param article
	 * @param file
	 * @return
	 */
	@RequestMapping(value="updateArticle",method=RequestMethod.POST)
	@ResponseBody
	public boolean updateArticle(HttpServletRequest request,Article article,MultipartFile file){
		String picUrl;
		 try {
			picUrl  = processFile(file);
			article.setPicture(picUrl);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 User loginUser = (User) request.getSession().getAttribute(CmsContant.USER_Key);
		int i  = articleService.update(article,loginUser.getId());
		 return i>0;
	}
	
	
	@RequestMapping("comments")
	public String comments(){
		return "user/comment/list";
	}
	//退出登录
	@RequestMapping("logout")
	public Object logout(HttpServletRequest request,HttpServletResponse response){
		 HttpSession session = request.getSession();
		 session.removeAttribute(CmsContant.USER_Key);
		 
		 Cookie cookieUserName = new Cookie("username", "");
			cookieUserName.setPath("/");
			cookieUserName.setMaxAge(0);// 立即过期
			response.addCookie(cookieUserName);
			Cookie cookieUserPwd = new Cookie("userpwd", "");
			cookieUserPwd.setPath("/");
			cookieUserPwd.setMaxAge(0);// 立即过期
			response.addCookie(cookieUserPwd);
		 
		return "redirect:/index";
	}
	
}
