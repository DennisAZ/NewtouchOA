<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String contentDetail = (String)request.getAttribute("contentDetail");
  if(contentDetail == null){
    contentDetail = "";
  }
  String userId = (String)request.getAttribute("userId");
  if(userId == null){
    userId = "";
  }

  String sendTime = (String)request.getAttribute("sendTime");
  if(sendTime == null){
    sendTime = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示内容</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var userId = "<%=userId%>";
var sendTime = "<%=sendTime%>";
  var contentDetail = "<%=contentDetail%>";
  function doInit() {  
    var divCont = document.getElementById("content");
    divCont.innerHTML = contentDetail;

    var divPoster = document.getElementById("poster");
    divPoster.innerHTML = userId + "" +sendTime;
  }
</script>
</head>
<body onload="doInit()">
<table cellscpacing="1" cellpadding="3" width="100%">
  <tr>
    <td>
      <img src="<%=imgPath%>/1.gif" alt="个人短信" align="absmiddle"/>个人短信
    </td>
  </tr>
  <tr>
    <td>
      <div id="poster">
      </div>
    </td>
  </tr>
  <tr>
    <td>
      <div id="content">
      </div>
    </td>
  </tr>
</table>
</body>
</html>