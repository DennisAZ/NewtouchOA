<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String diaId = request.getParameter("diaId");
if(diaId == null){
  diaId = "";
}
String userId = request.getParameter("userId");
if(userId == null){
  userId = "";
}
String subject = request.getParameter("subject");
if(subject == null){
  subject = "";
}
subject =  subject;     

%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>指定共享范围</title>
<script type="text/javascript">
  var subject = "<%=subject%>";
  var diaId = "<%=diaId%>";
  function doInit(){
    getShare(diaId,'user');
  }
  function doShare(){
    setShare("form1","msrg");
    showCntrl('buttonBlack');
  }
</script>
</head>
<body  topmargin="5" onload="doInit()">
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" margin-top="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle">
    <span class="big3"> 设置共享范围-<%=subject %></span>
    </td>
  </tr>
</table>
<br>
<div id="msrg">
<form id="form1" name="form1">
<table class="TableBlock" width="65%" align="center">
  <tr>
    <td class="TableData">共享范围：</td>
     <td class="TableData">
          <input type="hidden" name="toId" id="user" value="" />
          <textarea id="userDesc"  rows="2" cols="40" readOnly class="BigStatic"></textarea>
        <a href="#" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="#" class="orgClear" onClick="Clear('user','userDesc')">清空</a>
      </td>
  </tr>
  <tr align="center">
    <td colspan="2" nowrap>
    	<input type="hidden" name="diaId" value="<%=diaId %>">
    	<input class="SmallButton" type="button" value="确定" onclick="doShare()"/>&nbsp;&nbsp;
    	 <input  type="button" value="返回" class="SmallButton" onclick="history.back()">
        <!-- <input type="button" value="返回" class="SmallButton" onclick="location='userDiary.jsp?userId=<%=userId %>'"> -->
    </td>
  </tr>
</table> 
</form>  
</div>
<div id="buttonBlack" align="center" style="display:none">
     <!-- <input  type="button" value="返回" class="SmallButton" onclick="share('<%=diaId%>','<%=userId %>')"> -->   
        <input  type="button" value="返回" class="SmallButton" onclick="history.back()">
</div>
</body>

</html>