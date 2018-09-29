<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
 String newId = request.getParameter("newId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="<%=contextPath %>/subsys/portal/guoyan/style/css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/leaderact/js/leaderact.js"></script>
<title>类容</title>
<script type="text/javascript">
function doInit(){
  var newsId = "<%=newId%>";
  var url =  contextPath + "/yh/subsys/portal/guoyan/leaderact/act/YHLeaderactAct/getOneNews.act";
  var param = "newId=" + newsId;
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    $('subject').innerHTML = data.subject;
    $('content').innerHTML = data.content;
  }
}
</script>
</head>
<body onload="doInit()">
<div>您当前的位置:首页 > 领导活动 </div>
<br/>
<div>
  <center><font id="subject" size="4"></font></center><br/>
   <center><font id="content" size="2"></font></center>
</div>
</body>
</html>