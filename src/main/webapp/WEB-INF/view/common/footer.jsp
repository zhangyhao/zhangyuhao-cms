<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
    
    <nav class="navbar navbar-expand-lg navbar-dark  justify-content-center" >
	<c:forEach items="${list}" var="link">
        		<tr>
        			<td><h6><a href="${link.url}" style="color:red">${link.name}</a>&nbsp;&nbsp;&nbsp;</h6></td>
        		</tr>
        	</c:forEach>
</nav>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark justify-content-center" >
	<font color="green"><h2>版权所有  违者必究</h2>
	</font>
</nav> 
6