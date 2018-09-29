<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.userInfo.data.YHPdaUserInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="yh.core.util.YHUtility" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
String area = (String) request.getAttribute("area") == null ? "" : (String) request.getAttribute("area") ;
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
List userInfos = (List) request.getAttribute("userInfos");
%>
<!doctype html>
<html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<meta name="viewport" content="width=device-width" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/pda/style/list.css" />
</head>
<body>
<div id="list_top">
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/pda/userInfo/index.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
  <span class="list_top_center">人员查询</span>
</div>
<div id="list_main" class="list_main">
<% 
int count = userInfos.size();
for(int i = 0 ; i < count ; i++){
  if(i >= 20)
    break;
  YHPdaUserInfo userInfo = (YHPdaUserInfo)userInfos.get(i);
  String userName = userInfo.getUserName() == null ? "" : userInfo.getUserName();
  String privName = userInfo.getPrivName() == null ? "" : userInfo.getPrivName();
  String userPriv = userInfo.getUserPriv() == null ? "" : userInfo.getUserPriv();
  String deptId = userInfo.getDeptId() == null ? "" : userInfo.getDeptId();
  String deptName = userInfo.getDeptName() == null ? "" : userInfo.getDeptName();
  String sex = userInfo.getSex();
  String telNoDept = userInfo.getTelNoDept() == null ? "" : userInfo.getTelNoDept();
  String mobilNo = userInfo.getMobilNo() == null ? "" : userInfo.getMobilNo();
  String email = userInfo.getEmail() == null ? "" : userInfo.getEmail();
  String mobilNoHidden = userInfo.getMobilNoHidden() == null ? "" : userInfo.getMobilNoHidden();
  if(sex.equals("0")){
    sex = "男";
  }
  else if(sex.equals("1")){
    sex = "女";
  }
  %>
   <div class="list_item">
      <%=i+1 %>.<b><%=userName %></b>：<%=sex %><br>
      部门：<%=deptName %><br>
      角色：<%=privName %><br>
  <% 
  if(!YHUtility.isNullorEmpty(telNoDept)){
  %>
      工作电话：<%=telNoDept %><br>
	<%
	}
	if(!YHUtility.isNullorEmpty(mobilNo)){
	%>手机：<% 
	  if(!mobilNoHidden.equals("1")){
      out.println(mobilNo+"<br>");
    }
    else {
      out.println("不公开<br>");
    }
	}
	if(!YHUtility.isNullorEmpty(email)){
	%>
    Email：<u><%=email %></u><br>
	<%
	}
	%>
   </div>
<% 
}
if(count == 0)
  out.println("<div id=\"message\" class=\"message\">无符合条件的记录</div>");
%>
</div>
<div id="list_bottom">
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
