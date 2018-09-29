<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>上传图片</title>
    <link rel="shortcut icon" href="favicon.ico"/>
   
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <style type="text/css">
    	body{
			background-color:#FFFFFF;
		}	
		.upFormLayer {
			text-align:left;
		}
		.redWarn {
			margin:0px;
			padding:1px 5px 0 5px;
			height:18px;
		}
		.tipsDiv {
			border:#b1a707 1px solid;
			padding:2px 5px 0 5px;
			height:18px;
			background: #fdfaca; 
			/*color: #b36810;*/
		}
	</style>
    
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript">
		//上传logo
		function limitFile(form, file){
			if (!file) {
				return;
			}
			var errMsg="";
			//后缀名数组
			var extArray = new Array(".gif", ".jpeg",".jpg", ".png"); 
			//文件类型判断
			if(!checkSuf(file,extArray)){
				errMsg = "只允许上传以下文件类型：" + (extArray.join("  ")) + ",请重新选择文件上传";
			}
			if(errMsg != ""){
				alert(errMsg);
			}
			else{
				$("upbotton").disabled=true;
				$("failDiv").hide();
				$("sucDiv").hide();
				$("loadinf").show();
				form.action="fileAction.do?op=upload&id=${id}&type=logo&picPath="+encodeURIComponent('${path}');
				form.submit();
			}
		}
		
		window.onload=function(){
			if('${msg}'!=''){
				if('${msg}'=='suc'){
					if('${picPath}'!=''&&parent.document.getElementById('uploadImg')!=null){
						parent.document.getElementById('uploadImg').src='${picPath}';
					}
					$('sucDiv').show();
				}
				else if('${msg}'=='clear'){
					if(parent.document.getElementById('uploadImg')!=null){
						parent.document.getElementById('uploadImg').src='images/nav/nav_logo.png';
					}
					$('sucDiv').show();
				}
				else if('${msg}'=='fail'){
					$('failDiv').show();
					$('failDiv').innerHTML='${errorMsg}';
				}
			}
		}
  </script>
  </head>
  
  <body>
  	<div class="upFormLayer">
        <form style="padding:0px; margin:0px;" method="post" action="" enctype="multipart/form-data">
        	<div style="padding-bottom:4px;">
            <input class="butSize1 inputBoxAlign" style="width:200px;" id="file" type="file" name="file"/>&nbsp;<input class="butSize3 inputBoxAlign" type="button" id="upbotton" style="cursor:pointer; width:50px" value="上传" onClick="limitFile(this.form, this.form.file.value)"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="butSize1 inputBoxAlign" type="button" id="upbotton" style="cursor:pointer;" value="恢复默认" onClick="self.location.href='empAction.do?op=clearTopOrgLogo'"/>
            </div>
            <div id="loadinf" class="deepGray" style="display:none"><img style="vertical-align:middle" src="images/gif/uploading.gif" alt="上传中..."/>&nbsp;上传中...</div>
            <span id="failDiv" class="redWarn" style="display:none"></span>
            <div id="sucDiv" style="display:none"><logic:notEmpty name="msg"><span class="tipsDiv"><logic:equal name="msg" value="suc">上传成功！</logic:equal><logic:equal name="msg" value="clear">已恢复为默认图片</logic:equal></span>&nbsp;&nbsp;<a href="javascript:void(0)" onClick="parent.parent.frames['topFrame'].location.reload();">点击刷新logo</a></logic:notEmpty></div>
        </form>
    </div>
  </body>
</html>
