<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>错误页面</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script type="text/javascript">
		function countToBack(){
			var count=parseInt(document.getElementById("count").innerText);
			if(count<=0)
				history.back();
			else{
				count-=1;
				document.getElementById("count").innerText=count.toString();
			}
		}
		window.onload=function(){
			//是否自动返回
			if('${isBack}'!=''&&'${isBack}'=='1'){
				document.getElementById("countTxt").innerHTML="(<span id='count' class='gray'></span>秒后返回)";
				document.getElementById("count").innerText="3";//设置等待时间（秒）
				setInterval(countToBack,1000);
			}
			
			//是否去除边框
			if(parent.getPopWindowNum()>0){
				document.getElementById("errLayer").className="warnLayer";
				document.body.style.background="#dddddd";
			}
		}
  	</script>
  </head>
  
  <body>
  <div style="text-align:center;">
      <div id="errLayer" class="warnBox">
      	<div id="warnContent1">
        	<div id="warnTitle">操作失败！<span id="countTxt" style="font-size:12px; color:#999999; font-weight:normal;"></span></div>
        	<div class="red"><c:if test="${!empty errorMsg}">原因：<c:out value="${errorMsg}" />&nbsp;</c:if></div>
        </div>
      </div>
  </div>
  </body>
</html>
