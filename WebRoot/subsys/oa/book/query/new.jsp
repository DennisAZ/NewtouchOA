<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page import=" java.net.URLDecoder;" %>
<html>
<head>
<title>借书登记</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css" />
<%
  String bookNo = request.getParameter("bookNo");
  bookNo = URLDecoder.decode(bookNo, "UTF-8").replaceAll("\"", "&quot;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"); 
  YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
%>
<script Language="JavaScript">
function doInit(){  
  var beginParameters = {
      inputId:'borrowDate',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
        };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'returnDate',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
        };
  new Calendar(endParameters); 

  var now = new Date();  
  var nowDate = now.getYear()+"-"+parseMonth(now.getMonth()+1)+"-"+parseMonth(now.getDate());
  $("borrowDate").value = nowDate;
  var milliDate = now.getTime() + 30 * 24 * 60 * 60 *1000;
  var lastDate = new Date(milliDate);
  var returnDate = lastDate.getYear()+"-"+parseMonth(lastDate.getMonth()+1)+"-"+parseMonth(lastDate.getDate());
  $("returnDate").value = returnDate;
}

function parseMonth(month){
  if(month < 10){
    return "0"+month;
  }
  return month;
}

function check(){
  var begin = $("borrowDate").value;
  var end = $("returnDate").value;
  if(begin){
	   if(!isValidDateStr(begin)){
	     alert("开始日期格式不正确,如1999-10-10");
	     document.getElementById("borrowDate").focus();
	     return false;
	   }
  }
  if(end){ 
	   if(!isValidDateStr(end)){
	     alert("结束日期格式不正确,如1999-10-10");
	     document.getElementById("returnDate").focus();
	     return false;
	   }
  } 
  if(begin && end){
	   if(end <= begin){
	     alert("还书日期不能小于借书日期！");
	     return false;
	   }  	  
 }
  return true;
}
function checkForm(){
 if(check()){
  $("form1").submit();
}
  return true;	  
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 借书登记</span>
    </td>
  </tr>
</table>
<br>
<br>
<form action="<%=contextPath%>/yh/subsys/oa/book/act/YHBookQueryAct/toRead.act"  method="post" name="form1" id="form1">
<table class="TableBlock"  width="400" align="center" >  
   <tr>
    <td nowrap class="TableData" width="120">借书人：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="toId" value="<%=user.getSeqId()%>"> 	
      <input type="text" name="toName" size="13" class="BigStatic" value="<%=user.getUserName()%>" readonly>&nbsp;
     </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">图书编号：</td>
    <td class="TableData">
      <input type="text" name="bookNo" class="BigStatic" size="13" maxlength="100" readonly value="<%=bookNo%>">&nbsp;
    </td> 
   </tr>
   <tr>
    <td nowrap class="TableData">借书日期：</td>
    <td nowrap class="TableData">                 
    <input type="text" id="borrowDate" onfocus="this.select();" name="borrowDate" class="BigInput" value="" style="width:80px;" />
	  <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
	     为空为当前日期
    </td>   
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">归还日期：</td>
    <td nowrap class="TableData">
      <input type="text" id="returnDate" onfocus="this.select();" name="returnDate" class="BigInput" value="" style="width:80px;" />
	    <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
	                   为空为从借书之日起30天的日期
    </td> 
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">备注：</td>
    <td nowrap class="TableData">
      <textarea name="bRemark" class="BigInput" cols="35" rows="3"></textarea>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="确定" onclick="checkForm();return false;" class="BigButton" title="保存借书信息" name="button">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close();">
    </td>
   </tr>
  
</table>
</form>
</body>
</html>
