<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%

String SMS_ID_STR=request.getParameter("SMS_ID_STR");
String NAV_MAIN_URL=request.getParameter("NAV_MAIN_URL");

String urlOrg[] = NAV_MAIN_URL.split(",");
String jsoStr="";
for(int i=0;i<urlOrg.length;i++){
   String url=urlOrg[i];
   jsoStr+=" {title:'工作"+(i+1)+"',contentUrl:'"+contextPath+url+"',imgUrl:'"+imgPath+"/notify_new.gif',useIframe:true},";
  
}

if(jsoStr.endsWith(",")){
  jsoStr=jsoStr.substring(0, jsoStr.length()-1);
}


jsoStr="["+jsoStr+"]";

%>

<head>
<title>全部详情</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">

var jsoStr="<%=jsoStr%>";


var jso = strToJson(jsoStr);

  
  function strToJson(str){  
	    var json = eval('(' + str + ')');  
	    return json;  
	} 
  
function doInit(){
	
	buildTab(jso, 'smsdiv', 800);
}
  
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
</body>
</html>
