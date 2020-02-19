<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>cms-个人中心</title>
<script type="text/javascript" src="/js/jquery-3.2.1/jquery.js" ></script>
<link href="/bootstrap-4.3.1/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="/bootstrap-4.3.1/js/bootstrap.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/jquery.validate.js"></script>
<script type="text/javascript" src="/js/jqueryvalidate/localization/messages_zh.js"></script>
 
<style type="text/css">
	.menuselected {
		background:#00FFFF;
	}
	.mymenuselected li:hover {
		background:green;
	}
</style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light" style="background:#6600FF">
  <div class="collapse navbar-collapse" id="navbarSupportedContent" style="background:#00FFFF">
    
    <div>
    	<ul class="nav">
    		<li class="nav-item nav-link"> <img width="35px" height="35px" src="/js/images/guest.jpg"> </li>
    	
    		<li class="nav-item nav-link"><a href="/">Home</a></li>
    		<li class="nav-item nav-link"><a href="/user/home">发布文章</a></li>
    	</ul>
    </div>
  </div>
</nav><!--  头结束 -->
	
	<div class="container row">
		<div class="col-md-2" style="margin-top:20px ; border-right:solid 2px"> 
			<!-- 左侧的菜单 -->
			<ul class="nav flex-column mymenuselected">
				  <li class="nav-item ">
				    <a  class="nav-link active" href="javascript:void()" onclick="showWork($(this),'/admin/article?status=0&page=1')" >文章管理</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" href="javascript:void()" onclick="showWork($(this),'/admin/comment')" >评论管理</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" href="javascript:void()" onclick="showWork($(this),'/admin/link?page=1')" >友情链接管理</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" href="javascript:void()" onclick="showWork($(this),'/admin/user')" >用户管理管理</a>
				  </li>
				  <li class="nav-item">
				    <a class="nav-link" href="javascript:void()" onclick="showWork($(this),'/admin/shoucang?page=1')" >我的收藏夹</a>
				  </li>
				</ul>	
		</div>
		
		<div class="col-md-10" id="workcontent"> 
		
		    
		</div>	
	</div>
	
<!-- 尾开始 -->
<nav class="nav fixed-bottom justify-content-center "  style="background:#00FFFF" height="50px"> 
	         CMS  系统后台管理系统  版权所有 违者必奖 
</nav>

<script type="text/javascript">	
	
	function showWork(obj,url){
		$(".mymenuselected li").removeClass("menuselected");
		obj.parent().addClass("menuselected")		
		$("#workcontent").load(url);
	}
	
</script>


</body>
</html>