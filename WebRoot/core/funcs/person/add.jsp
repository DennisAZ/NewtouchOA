<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.global.YHRegistProps" %>
<%@ page import="yh.core.data.YHAuthKeys" %>
<%
  String deptId = request.getParameter("deptId");
  if (deptId == null) {
    deptId = "";
  }
  String registFlag = request.getParameter("registFlag");
  if (registFlag == null) {
    registFlag = "";
  }
  String maxSum = String.valueOf(YHRegistProps.getInt(YHAuthKeys.USER_CNT + ".yh"));
  if (maxSum == null) {
    maxSum = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建用户</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script Language="JavaScript">
var deptId = "<%=deptId%>";
var registFlag = "<%=registFlag%>";

function doInit(){
  if(registFlag == "0"){
    $("noRegist").style.display = '';
    $("regist").style.display = 'none';
  }else{
    $("noRegist").style.display = 'none';
    $("regist").style.display = '';
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="regist" style="display:''">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">用户增加成功</div>
    </td>
  </tr>
</table>
<div align="center">
   <input type="button" value="继续新建用户" class="BigButton" title="继续新建用户" onclick="location='indutypersoninput.jsp?deptId=<%= deptId%>'">&nbsp;&nbsp;
   <!--  <input type="button" value="建立档案" class="BigButton" title="建立档案" onclick="">&nbsp;&nbsp;-->
   <input type="button" value="关 闭" class="BigButton" title="关闭窗口" onclick="window.close();">
</div>
</div>

<div id="noRegist" style="display:none">
<table class="MessageBox" align="center" width="500"> 
<tr> 
<td class="msg info"> 
<h4 class="title">提示</h4> 
<div class="content" style="font-size:12pt">已经达到系统的最大授权用户数(<%=maxSum%>)，不能再增加允许登录OA用户</div> 
</td> 
</tr> 
</table> 
<br><center><input type="button" class="BigButton" value="返回" onclick="history.back();"></center>
</div>
</body>
</body>
</html>