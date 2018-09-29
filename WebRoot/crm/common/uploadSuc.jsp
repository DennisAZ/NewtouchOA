<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>上传文件</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" type="text/css" href="crm/css/style.css">
    <style type="text/css">
    	#upload{
			width:420px; 
			text-align:left;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">
		function isLoad(){
			if(parent.document.getElementById("extType")!=null){
				setTimeout("reloadPra()",1000);
			}
		}
		function reloadPra(){
			var ext=parent.document.getElementById("extType").value;
			var type=parent.document.getElementById("type").value;
			var hasIcon=parent.document.getElementById("hasIcon").value;
			parent.document.location.href="fileAction.do?op=attList&extType="+ext+"&type="+type+"&id=${id}"+"&hasIcon="+hasIcon;
			
			/* 刷新详情页面 */
			//if(parent.parent.document.getElementById("isDesc")!=null&&parent.parent.document.getElementById("isDesc").value=="1"){
			if(ext=="reload"){
				parent.parent.history.go(0);
			}
			else if(parent.document.getElementById("hasIcon").value!="0"){
				if(ext=="descList"||parent.parent.frames['ifrList']==null){
					var img1 = null;
					if(parent.parent.document.getElementById("${id}n") != null){
						img1=parent.parent.document.getElementById("${id}n");
					}
					else if(parent.parent.document.getElementById("${id}y") != null){
						img1=parent.parent.document.getElementById("${id}y");
					}
					if(img1!=null&&img1.src!="crm/images/content/attach.gif"){
						img1.src="crm/images/content/attach.gif";
					}
				}
				else if(parent.parent.frames['ifrList']!=null){
					var img2 = null;
					if(parent.parent.frames['ifrList'].document.getElementById("${id}n") != null){
						img2=parent.parent.frames['ifrList'].document.getElementById("${id}n");
					}
					else if(parent.parent.frames['ifrList'].document.getElementById("${id}y")!= null){
						img2=parent.parent.frames['ifrList'].document.getElementById("${id}y");
					}
					if(img2!=null&&img2.src!="images/content/attach.gif"){
						img2.src="crm/images/content/attach.gif";
					}
				}
			}
		}
     
	window.onload=function(){
		isLoad();
	}
  </script>
  </head>
  
  <body>
  	<div id="upload">
    	<fieldset>
        	<legend class="blackblue ">上传附件</legend>
    		&nbsp;&nbsp;<img src="crm/images/content/suc.gif" style="vertical-align:middle;"/>&nbsp;上传成功！&nbsp;
  		</fieldset>
  	</div>
  </body>
</html>
