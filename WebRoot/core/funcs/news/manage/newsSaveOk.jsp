<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
      <%
  String publish = request.getParameter("publish");
  if (publish == null) {
    publish = "";
  }
  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>发送提示</title>
</head>
<script type="text/javascript">
var publish = "<%=publish%>";
var url = null;
function doInit() {
	if(publish == '1'){//发布状态别人可以查看
		$('msg').update('新闻发布成功');
		   url = '/yh/core/funcs/news/manage/newsList.jsp';
	}
	if(publish == '0'){//保存处于未发布状态，别人看不到
		$('msg').update('新闻保存成功');
		  url = '/yh/core/funcs/news/manage/newsAdd.jsp';
    }
}

function back() {	
	window.location.href = "<%=contextPath%>/core/funcs/news/manage/newsAdd.jsp";
  //top.dispParts("<%=contextPath%>/core/funcs/news/manage/index.jsp");
}
</script>
<body onload="doInit()">
 <TABLE class=MessageBox width=280 align=center>
<TBODY>
<TR>
<TD id="msgInfo" class="msg info">
<H4 class=title>提示</H4>
<DIV class=content id="msg"
style="FONT-SIZE: 12pt">新闻保存成功！</DIV></TD></TR></TBODY></TABLE><BR>
<DIV align=center>
<INPUT class=BigButton onclick="back();" type=button value=返回> 
</DIV>
</body>
</html>