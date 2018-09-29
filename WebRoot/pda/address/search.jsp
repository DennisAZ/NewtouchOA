<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.pda.address.data.YHPdaAddress" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="yh.oa.tools.StaticData"%>
<%
String contextPath = request.getContextPath();
if (contextPath.equals("")) {
  contextPath = "/yh";
}
YHPerson loginPerson = (YHPerson)session.getAttribute("LOGIN_USER");
List addresses = (List) request.getAttribute("addresses");
String psnName = (String) request.getAttribute("psnName") == null ? "" : (String) request.getAttribute("psnName") ;
String deptName = (String) request.getAttribute("deptName") == null ? "" : (String) request.getAttribute("deptName") ;
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
  <div class="list_top_left"><a class="ButtonBack" href="<%=contextPath %>/pda/address/index.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
  <span class="list_top_center">通讯簿查询</span>
</div>
<div id="list_main" class="list_main">
<%
int pageSize = (Integer)request.getAttribute("pageSize");
int thisPage = (Integer)request.getAttribute("thisPage");
int totalPage = (Integer)request.getAttribute("totalPage");
int count = addresses.size();
for(int i = 0 ; i < count ; i++){
  if(i >= pageSize)
    break;
  YHPdaAddress address = (YHPdaAddress)addresses.get(i);
  String groupName = address.getGroupName();
  String psnName1 = address.getPsnName();
  String sex = address.getSex();
  String deptName1 = address.getDeptName();
  String ministration = address.getMinistration();
  Date birthday = address.getBirthday();
  String birthdayStr = "";
  if(birthday != null){
    birthdayStr = birthday.toString().substring(0 , 10);
  }
  String telNoDept = address.getTelNoDept();
  String telNoHome = address.getTelNoHome();
  String mobilNo = address.getMobilNo();
  String email = address.getEmail();%>
  
  <div class="list_item">
  <%=i+1+(thisPage-1)*pageSize %>.<b><%=groupName %></b> <%=psnName1 %>：<%=sex %><br>
  单位：<%=deptName1 %><br>
  职务：<%=ministration %><br>
  生日：<%=birthdayStr %><br>
  工作电话：<%=telNoDept %><br>
  家庭电话：<%=telNoHome %><br>
  手机：<%=mobilNo %><br>
 Email：<%=email %><br>
</div>
<% 
}
if(count==0)
  out.println("<div id=\"message\" class=\"message\">无符合条件的记录</div>");
%>
</div>
<div id="list_bottom">
  <div class="list_bottom_left">
  <div id="pageArea" class="pageArea">
           第<span id="pageNumber" class="pageNumber"><%=thisPage %>/<%=totalPage %></span>页

    <%if(thisPage == 1 || thisPage == 0){ %>
      <a href="javascript:void(0);" id="pageFirst" class="pageFirstDisable" title="首页"></a>
      <a href="javascript:void(0);" id="pagePrevious" class="pagePreviousDisable" title="上一页"></a>
    <%} else{%>
      <a href="<%=contextPath %>/yh/pda/address/act/YHPdaAddressAct/search.act?psnName=<%=psnName %>&deptName=<%=deptName %>&P=<%=loginPerson.getSeqId() %>&thisPage=1" id="pageFirst" class="pageFirst" title="首页"></a>
      <a href="<%=contextPath %>/yh/pda/address/act/YHPdaAddressAct/search.act?psnName=<%=psnName %>&deptName=<%=deptName %>&P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage-1 %>" id="pagePrevious" class="pagePrevious" title="上一页"></a>
    <%}
      if(thisPage == totalPage ){%>
      <a href="javascript:void(0);" id="pageNext" class="pageNextDisable" title="下一页"></a>
      <a href="javascript:void(0);" id="pageLast" class="pageLastDisable" title="末页"></a>
    <%} else{%>
    <a href="<%=contextPath %>/yh/pda/address/act/YHPdaAddressAct/search.act?psnName=<%=psnName %>&deptName=<%=deptName %>&P=<%=loginPerson.getSeqId() %>&thisPage=<%=thisPage+1 %>" id="pageNext" class="pageNext" title="下一页"></a>
    <a href="<%=contextPath %>/yh/pda/address/act/YHPdaAddressAct/search.act?psnName=<%=psnName %>&deptName=<%=deptName %>&P=<%=loginPerson.getSeqId() %>&thisPage=<%=totalPage %>" id="pageLast" class="pageLast" title="末页"></a>
    <%} %>
  </div>
  </div>
  <div class="list_bottom_right"><a class="ButtonHome" href="<%=contextPath %>/pda/main.jsp?P=<%=loginPerson.getSeqId() %>"></a></div>
</div>
</body>
<script type="text/javascript" src="<%=contextPath %>/pda/js/logic.js"></script>
</html>
