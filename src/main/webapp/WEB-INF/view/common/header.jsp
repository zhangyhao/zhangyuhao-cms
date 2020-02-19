<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<nav class="navbar navbar-expand-lg navbar-dark  " >
  <div class="collapse navbar-collapse" id="navbarSupportedContent" >
    
    <ul class="navbar-nav mr-auto">
    	<li class="nav-item">
           <a class="nav-link" href="#"><img src="/js/images/logo.png"> </a>
      </li> 
    </ul>
     
   <!--  <form class="form-inline my-2 my-lg-0" style="margin-right:30%" > -->
    	
    	<form class="form-inline my-2 my-lg-0" action="index" method="get">狮子不和狗玩这是规矩。
      <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search" name="es" value="${key }">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
   <!--  </form> -->
    </form>
    
    <div>
    	<ul class="nav">
    		<li class="nav-item nav-link" ><img width="35px" height="35px" src="/js/images/guest.jpg"> </li>
    		
    		<c:if test="${sessionScope.loing_session_key!=null}">
    		<li class="nav-item nav-link">${sessionScope.loing_session_key.username}</li>
    		<li class="nav-item dropdown">
		        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		          	用户信息
		        </a>
		            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
			          <a class="dropdown-item" href="${sessionScope.loing_session_key.role==1?'/admin/index':'/user/home'}">进入个人中心</a>
			          <a class="dropdown-item" href="#">个人设置</a>
			          <div class="dropdown-divider"></div>
			          <a class="dropdown-item" href="/user/logout">登出</a>
			        </div>
		      </li>
		      </c:if>
		     
		      <c:if test="${sessionScope.loing_session_key==null}">
			       <li class="nav-item nav-link"><a href="/user/login">登录</a></li>
		      </c:if>
      
    	</ul>
    </div>
  </div>
</nav><!--  头结束 -->