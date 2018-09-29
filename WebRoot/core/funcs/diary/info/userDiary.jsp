<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("userId");
  if(userId == null ){
    userId = "";
  }
  String type = request.getParameter("type");
  if(type == null ){
    type = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
var userId = "<%=userId%>";
var type = "<%=type%>";
function doInit(){
  if(type){
    getLastTenByUser('bodyContent');
    $('title').innerHTML = "";
  }else{
    getTenDiaryByUser(userId,'bodyContent');
    $('title').innerHTML = "[" + getUserNameById(userId) + " - 工作日志]";
    showCntrl('searchDiv');
  }
  showCalendar('startDate',false,'startDateImg');
  showCalendar('endDate',false,'endDateImg');
}
function doSearch(){
  var query = "";
  if(checkDate("startDate") == false){
    $("startDate").focus();
    $("startDate").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  if(checkDate("endDate") == false){
    $("endDate").focus();
    $("endDate").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  query = $("from1").serialize();
 // $("from1").subject.value = query;
  location =  contextPath + "/core/funcs/diary/info/search.jsp?" + query;
}
</script>
</head>
<body onload="doInit()">
<div align="center" class="Big1" style="padding-top:10px;">
<b><span id="title"></span></b>
</div>
 
<div id="body_top">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3"> 最近的10篇日志</span>
    </td>
  </tr>
</table>
</div>
 
<div  id = "bodyContent">
</div>
<br>
<div id="searchDiv" style="display:none">
<div style="height:20px"></div>
<form id="from1" name="form1">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3"> 日志查询</span>
	    </td>
	  </tr>
	</table>
	<table class="TableBlock" width="97%" align="center">
	  <tr>
	    <td nowrap class="TableData">起始日期：</td>
	    <td class="TableData"><input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="">
	        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="startDateImg">
	    </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">截止日期：</td>
	    <td class="TableData"><input type="text" name="endDate" id="endDate"  size="10" maxlength="10" class="BigInput" value="">
	        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="endDateImg">
	    </td>
	  </tr>
	    <tr>
	      <td nowrap class="TableData">标题：</td>
	      <td class="TableData"><input type="text" name="subject" class="BigInput" size="40"></td>
	    </tr>
	  <tr>
	    <td nowrap class="TableData">关键词1：</td>
	    <td class="TableData"><input type="text" name="key1" class="BigInput" size="40"></td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">关键词2：</td>
	    <td class="TableData"><input type="text" name="key2" class="BigInput" size="40"></td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">关键词3：</td>
	    <td class="TableData"><input type="text" name="key3" class="BigInput" size="40"></td>
	  </tr>
	  <tr >
	    <td nowrap class="TableControl" colspan="2" align="center">
	        <input type="hidden" name="userId" value="<%=userId%>">
	        <input type="button" value="查询" class="SmallButton" title="进行查询"  onclick="doSearch()">
	    </td>
	  </tr>
	</table>
  </form>
</div>
</body>
</html>