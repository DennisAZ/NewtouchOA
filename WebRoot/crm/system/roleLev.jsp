<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>角色级别</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="0"> 
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	
    <style type="text/css">
		#modRoleBorder {
			width:256px;
			border:#3a68aa 2px solid;
			padding:2px;
		}
    	#modRole{
			color:#fff;
			padding:5px;
			background:#3a68aa;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/type.js"></script>
	<script type="text/javascript">
		function changOption(){
		 	var textNum=$("num").value;//读取文本框里面的级别数
			var re = /^[0-9]*$/;
			if(!re.test(textNum))
				{
					alert("请填写数字");
					return false;
				}
			if(textNum>99){
				alert("级别数不超过99");
				return false;
			}
			else{
				waitSubmit("doSave","保存中...");
				waitSubmit("cancel1");
				role.submit();
			}							
		}
			
			
		function lodeXml(){	
			//读取配置文件信息
			var xmlDoc;
			if(window.ActiveXObject){
			 //获得操作的xml文件的对象
				xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
				xmlDoc.async = false;
				xmlDoc.load("system/role.xml");
				if(xmlDoc == null){
					alert('您的浏览器不支持xml文件读取,于是本页面禁止您的操作,推荐使用IE5.0以上可以解决此问题!');	
					return;
				}
			}
			//解析xml文件，判断是否出错
			if(xmlDoc.parseError.errorCode != 0){
			   alert(xmlDoc.parseError.reason);
			   return;
			}
			var level=xmlDoc.getElementsByTagName("num");//读取XML里面的级别数
			var n=level[0].childNodes[0].nodeValue;
			$("num").value=n;//赋值给文本框里面的级别数
		}	
		
		var ms='${msg}';
		if(ms!=""){
			alert(ms);
		}	
		
		window.onload=function(){
			if('${ms}'!=null&&'${ms}'!=""){
				$("num").value='${ms}';
			}else{
				lodeXml();
			}
		}
  </script> 
</head>

<body>
	<div id="modRoleBorder">
        <div id="modRole">
            <form action="userAction.do" method="post" name="role" style="padding:0px; margin:0px">
                <input type="hidden" name="op" value="roleXml">
                可用职级数&nbsp;&nbsp;<input class="inputSize2 inputBoxAlign" type="text" name="xmlNum" id="num" style="width:60px"/>&nbsp;&nbsp;&nbsp;<button class="inputBoxAlign smallBut" style="width:40px" id="doSave" onClick="changOption()">保存</button>&nbsp;&nbsp;<button class ="inputBoxAlign smallBut" style="width:40px" id="cancel1" onClick="cancel()">取消</button>
            </form>
        </div>
    </div>
</body>
</html>
