<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-3.4.1.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.validate.js"></script>
 <link href="/css/component.css" rel="stylesheet">
<link href="/css/demo.css" rel="stylesheet">
<link href="/css/normalize.css" rel="stylesheet">
<body> 
<div class="container demo-1">
			<div class="content">
				<div id="large-header" class="large-header">
					<canvas id="demo-canvas"></canvas>
					<div class="logo_box">
						<h3>注册页面</h3>
						<form:form modelAttribute="u" max="8" min="2" id="form" action="" method="post">
		<table>
			<tr>
				<Td>用户名</Td>
				<td>
				<form:input path="username"
				 remote="/user/checkname"
				/>
				<form:errors path="username"/>
				</td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
		
			<tr>    
				<Td>密码</Td>
				<td>
				<form:input path="password"/>
				<form:errors path="password"/>
				</td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr><tr>
				<td> </td>
			</tr>
			<tr>
				<td> </td>
			</tr>
			<tr>
				<td align="center" colspan="50">
				
				<input type="submit" value="提交">
				<a href="login" style="color:red">已有账号,去登录页面</a></td>
			<!-- 	<td align="center"><input type="button" onclick="add()" value="提交"></td> -->
			</tr>
		</table>
	
	</form:form>
					</div>
				</div>
					</div><!-- /container -->
	<script src="/js/TweenLite.min.js"></script>
		<script src="/js/EasePack.min.js"></script>
		<script src="/js/rAF.js"></script>
		<script src="/js/demo-1.js"></script>
		<div style="text-align:center;">
			
</div>
	
</body>
<!--  <script type="text/javascript">
	$("#form").validate();
	function add(){
		alert('校验开始')
		$("#form").valid();
		alert('校验结束')
	}
</script> -->
</html>