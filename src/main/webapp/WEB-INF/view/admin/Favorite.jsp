<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head> 
<body>
<table class="table table-hover">
	
         <tbody>
			<c:forEach items="${list}" var="l">
			<h5 style="color:blue">${l.title}</h5>
			时间:${l.created}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void()" onclick="delFav(${l.id},${l.user_id})">删除</a>
			
			</c:forEach>
        </tbody>
        
        <!-- 分页 -->
          <nav aria-label="Page navigation example">
		  <ul class="pagination justify-content-center">
		    <li class="page-item">
		      <a class="page-link" href="#" tabindex="-1" aria-disabled="true" onclick="gopage('1')">Previous</a>
		    </li>
		   	<c:forEach begin="1" end="${pageInfo.pages}" varStatus="i">
		   		<li class="page-item"><a class="page-link" href="#" onclick="gopage(${i.index})">${i.index}</a></li>
		   	</c:forEach>
		    <li class="page-item">
		      <a class="page-link" href="#" onclick="gopage(${pageInfo.pages})">Next</a>
		    </li>
		  </ul>
		</nav>
        
</table>
<script type="text/javascript">

function gopage(page) {
	$("#workcontent").load("/admin/shoucang?page="+page);
}

function delFav(id,user_id) {
	if(window.confirm("确定删除？")){
		$.post("/admin/favDel",{id:id,user_id:user_id},function(msg){
			if(msg>0){
				alert("删除成功 !");
				location.reload();
			}else{
			alert(msg.err);
			}
				
		})
	}
}
</script>
</body>
</html>