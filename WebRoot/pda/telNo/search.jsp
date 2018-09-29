<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.telNo.data.YHPostTel" %>
<%@ page import="java.util.List" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
String area = (String) request.getAttribute("area") == null ? "" : (String) request.getAttribute("area") ;
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
List postTels = (List) request.getAttribute("postTels");
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
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/pda/telNo/index.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
  <span class="list_top_center">区号邮编查询</span>
</div>
<div id="list_main" class="list_main">
<%
int count = postTels.size();
for(int i = 0 ; i < count ; i++){
  if(i >= 20)
    break;
  YHPostTel postTel = (YHPostTel)postTels.get(i);
  String province = postTel.getProvince();
  String city = postTel.getCity();
  String county = postTel.getCounty();
  String town = postTel.getTown();
  String telNo = postTel.getTelNo();
  String postNo = postTel.getPostNo();%>
  
  <div class="list_item">
  <%=i+1 %>.<b><%=province %> - <%=area %></b><br>
  省(直辖市/自治区)：<%=province %><br>
  城市：<%=city %><br>
  区/县：<%=county %><br>
  街道：<%=town %><br>
  区号：<%=telNo %><br>
  邮编：<%=postNo %><br>
</div>
<% 
}
if(count==0)
  out.println("<div id=\"message\" class=\"message\">无符合条件的记录</div>");
%>
</div>
<div id="list_bottom">
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
