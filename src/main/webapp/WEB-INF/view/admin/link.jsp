<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<table class="table table-hover">
		<!-- articlePage -->
	  <thead> 
          <tr>
            <th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;id</th>
            <th>地址</th>
            <th>名称</th>
            <th>修改时间</th>
           	<th><a href="javascript:void()" style="color:red" onclick="Url()">添加</a></th>
          </tr>
        </thead>
         <tbody>
        	<c:forEach items="${list}" var="link">
        		<tr>
        			<td>${link.id}</td>
        			<td>${link.url}</td>
        			<td>${link.name}</td>
        			<td>${link.created}</td>
        			<td>
        			<a href="javascript:void()" onclick="toupd(${link.id})">修改</a>
        			<a href="javascript:void()" onclick="del(${link.id})">删除</a>
        			</td>
        		</tr>
        	</c:forEach>
	
        </tbody>
       
        
        </table>
         <!-- 分页 -->
          <nav aria-label="Page navigation example">
		  <ul class="pagination justify-content-center">
		    <li class="page-item">
		      <a class="page-link" href="#" tabindex="-1" aria-disabled="true" onclick="gopage('1')">Previous</a>
		    </li>
		   	<c:forEach begin="1" end="${pg.pages}" varStatus="i">
		   		<li class="page-item"><a class="page-link" href="#" onclick="gopage(${i.index})">${i.index}</a></li>
		   	</c:forEach>
		    <li class="page-item">
		      <a class="page-link" href="#" onclick="gopage(${pg.pages})">Next</a>
		    </li>
		  </ul>
		</nav>
        
        <!-- 添加 -->
<div class="modal fade"   id="tianjia" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog" role="document" style="margin-left:100px;">
    <div class="modal-content" style="width:1200px;" >
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">添加友情链接</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div align="center">
       	名称:<input type="text" id="name" name="name"><br>
   		路径:<input type="text" id="url" name="url"> 
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="addUrl()">确认</button>
      </div>
    </div>
  </div>
</div>

	<!-- 修改 -->
<div class="modal fade"  id="articleUpdate" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true">
  <div class="modal-dialog" role="document" style="margin-left:100px;">
    <div class="modal-content" style="width:1200px;" >
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLongTitle">修改友情链接</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div align="center">
       	名称:<input type="text" id="xname" name="name"><br>
   		路径:<input type="text" id="xurl" name="url"> 
   		<input type="hidden" id="xid" name="id">
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="upd()">修改</button>
      </div>
    </div>
  </div>
</div>        
        
      <script type="text/javascript">
     
  	function gopage(page){
  		$("#workcontent").load("/admin/link?page="+page);
  	}
  	
  	function Url() {
		$("#tianjia").modal('show')
	}
  	
  	function addUrl() {
		var name=$("#name").val();
		var url = $("#url").val();
		$.post("/admin/addUrl",{name:name,url:url},function(msg){
			if(msg>0){
				alert("添加成功!");
				$("#tianjia").modal('hide')
			}else{
				alert("添加失败!");
			}
		})
	}
  	
  	
  	function toupd(id) {
  		
		$.post("/admin/toupd",{id:id},function(obj){
			$("#articleUpdate").modal('show')
			if(obj.code=1){
			$("#xname").val(obj.data.name)
			$("#xurl").val(obj.data.url)
			$("#xid").val(obj.data.id)
			}else{
			alert(obj.err)
			}
		},"json")
	}
  	function upd() {
  		var name=$("#xname").val();
		var url = $("#xurl").val();
		var id = $("#xid").val();
		$.post("/admin/upd",{name:name,url:url,id:id},function(msg){
			if(msg>0){
				alert("修改成功!");
				$("#articleUpdate").modal('hide')
			}else{
				alert("修改失败!");
			}
		})
	}
  	function del(id) {
		if(window.confirm("您确定要删除?")){
			$.post("/admin/del",{id:id},function(msg){
				if(msg>0){
					alert("删除成功!");
					location.reload();
				}else{
					alert("删除失败!");
				}
			})
		}
	}
	</script>  