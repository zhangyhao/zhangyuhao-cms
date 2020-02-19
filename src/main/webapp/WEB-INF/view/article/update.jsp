<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %> 

修改文章
<form name="articleform" id="articleform">
 <input type="hidden" name="id" value="${article.id}">
	<div class="form-group">
		<label for="title">标题</label>
		<input type="text" class="form-control" id="title" name="title" value="${article.title}" placeholder="请输入文章">
	</div> 
	<div class="form-group">
		<label for="channel">栏目</label>
		<select class="form-control" id="channel" name="channelId">
			<option value="0">请选择</option>
			<c:forEach items="${channels}" var="cat">
			<option value="${cat.id}" ${article.channelId==cat.id?"selected":""}>${cat.name}</option>
			</c:forEach>
		</select>
	</div>
	
	<div class="form-group">
		<label for="category">分类</label>
		<select class="form-control" id="category" name="categoryId">
		</select>
	</div>
	
	<div class="form-group">
		<label for="examleFormControlFile1">文章图片</label>
		<img src="/pic/${article.picture}" alt="未找到" width="100px" height="100px">
		<input type="file" class="form-control-file" id="file" name="file">
	</div>
	
	<div class="form-group">
		<label for="content1">文章内容</label>
		<textarea name="content1" id="contentId" cols="200" rows="200" style="width:700px;height:200px;visibility:hidden;">${content1}</textarea>
	</div>
	 <div class="form-group">
  	<input type="button" class="btn btn-primary mb-2" value="提交" onclick="commitArticle()">
  </div> 
</form>
<script>
function channelChange(){

	console.log("选中的数据是 " + $("#channel").val())
	$.post("/user/getCategoris",{cid:$("#channel").val()},
			function(data){
				$("#category").empty();
				for ( var i in data) {
					if(data[i].id=='${article.categoryId}'){
						$("#category").append("<option selected value='"+ data[i].id+"'>"+data[i].name+"</option>")	
					}
					else{
						$("#category").append("<option value='"+ data[i].id+"'>"+data[i].name+"</option>")
					}	
				}
	})

}

$("#channel").change(function(){
	channelChange();
	
})

	 $(document).ready( function(){
		 
		 // 执行 栏目下拉框的改变事件
		 channelChange();
		 
		KindEditor.ready(function(K) {
			//    textarea[name="content1"]
			editor = K.create('#contentId', {
			cssPath : '/kindeditor/plugins/code/prettify.css',
			//uploadJson : '/resource/kindeditor/jsp/upload_json.jsp',
			//uploadJson:'/file/upload.do',
			uploadJson:'/file/uploads.do',
			fileManagerJson:'/file/manager',
			//fileManagerJson : '/resource/kindeditor/jsp/file_manager_json.jsp',
			allowFileManager : true,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
				}
			});
			prettyPrint();
		});
      
	 }); 
	 
// 提交的文章的修改
function commitArticle(){
	  alert(editor.html());
	  
	//  var formdata = new FormData($("#articleform"))
	// 生成formData  异步提交的数据包含附件  
	var formData = new FormData($( "#articleform" )[0]);
	  
	console.log("11111111")
	
	   // 把文章内容存放到formData 中
	  formData.set("content",editor.html());
	console.log("222222222222")
	 
	  $.ajax({url:"updateArticle",
		  dataType:"json",
		  data:formData,
		  // 让jQuery 不要再提交数据之前进行处理
		  processData : false,
		  // 提交的数据不能加消息头
		  contentType : false,
		  // 提交的方式 
		  type:"post",
		  // 成功后的回调函数
		  success:function(data){
			  //$("#workcontent").load("/user/articles")
			  //  
			  showWork($("#postLink"),"/user/articles")
		  }
	 })
	  
}
	 
</script>