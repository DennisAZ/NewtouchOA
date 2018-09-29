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
		
		#upForm{
			padding:5px;
			padding-left:15px;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
	<script type="text/javascript">
		
		/*文件类型判断*/
		function limitFile(form, file) {
			var extType=parent.document.getElementById("extType").value;
			var folder = parent.document.getElementById("type").value;
			var id = parent.document.getElementById("id").value;
						
			//后缀名数组
			/*var extArray;
			if(extType=="pic") {
				extArray = new Array(".gif", ".jpeg",".jpg", ".png",".bmp",".tif"); 
			}
			else{
				extArray=new Array(".doc",".xls",".ppt",".txt",".gif", ".jpg", ".png",".bmp",".rar",".zip",".psd");
			}
			
			var allowSubmit = false;
			if (!file) {
				return;
			}
			while (file.indexOf("\\") != -1) {
				file = file.slice(file.indexOf("\\") + 1);
			}
			ext = file.slice(file.indexOf(".")).toLowerCase();
			for (var i = 0; i < extArray.length; i++) {
				if (extArray[i] == ext) {
					allowSubmit = true;
					break;
				}
			}*/
			//if (allowSubmit) {
				//生成进度动画
				$("upButton").disabled=true;
				$("upGif").show();
				form.action="fileAction.do?op=upload&id="+id+"&type="+folder;
				form.submit();
			/*} else {
				alert("只允许上传以下文件类型：" + (extArray.join("  ")) + ",请重新选择文件上传");
			}*/
		}
  </script>
  </head>
  
  <body>
	<div id="upload">
    	<fieldset>
        	<legend class="blackblue ">上传文件</legend>
            <div id="upForm">
            <form style="padding:0px; margin:0px;" method="post" action="" enctype="multipart/form-data">
                <input class="butSize1" style="width:300px" id="file" type="file" name="file"/>&nbsp;
                <input id="upButton" class="butSize1" type="button"  style="cursor:pointer;" value="上传" onClick="limitFile(this.form, this.form.file.value)"/> 
                <div id="upGif" style="display:none; text-align:center"><img style="vertical-align:middle" src="crm/images/gif/uploading.gif" alt="上传中..."/>上传中...</div>
            </form>
            </div>
        </fieldset>
  	</div>
  </body>
</html>
