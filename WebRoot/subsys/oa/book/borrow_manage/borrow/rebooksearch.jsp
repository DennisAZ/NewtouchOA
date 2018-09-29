<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.subsys.oa.book.data.*"%>
<%@ page  import="java.util.List"%>
<%@ page import=" java.net.URLEncoder;" %>
<html>
<head>
<%
    List<YHBookManage> manages = (List<YHBookManage>)request.getAttribute("manage");
		String seqId = (String)request.getAttribute("toId");
		String bookNo = (String)request.getAttribute("bookNo");
		String startDate = (String)request.getAttribute("startDate");
		String endDate = (String)request.getAttribute("endDate");
		String status = (String)request.getAttribute("status");

%>
<title>还书查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
 function gotoBack(){
  window.location.href = contextPath + "/subsys/oa/book/borrow_manage/borrow/query.jsp";
  }
 function returnBook(seqId,bookNo1){
   //$("form1").submit();
  // alert(bookNo1);
   var userId = "<%=seqId %>";
   var bookNo = "<%=bookNo %>";
   var startDate = "<%=startDate %>";
   var endDate = "<%=endDate %>";
   var status = "<%=status %>";
  // alert(userId+"=="+bookNo+"=="+startDate+"=="+status);
   var param = "userId="+userId + "&bookNo="+bookNo+"&startDate="+startDate+"&endDate="+endDate+"&status="+status+"&seqId="+seqId+"&bookNo1="+bookNo1;
   var url = contextPath+"/yh/subsys/oa/book/act/YHReturnBookManageAct/updateBookManage.act?"+param;
   window.location.href = url;
   return true;
 }
 
 function deleteSave(seqId,bookNo2){
   var userId = "<%=seqId %>";
   var bookNo = "<%=bookNo %>";
   var startDate = "<%=startDate %>";
   var endDate = "<%=endDate %>";
   var status = "<%=status %>";
   msg='此操作不是真正意义上的删除，可以到历史纪录查询中查到';
   if(window.confirm(msg))
   {
     var param = "userId="+userId + "&bookNo="+bookNo+"&startDate="+startDate+"&endDate="+endDate+"&status="+status+"&seqId="+seqId+"&bookNo2="+bookNo2;
     var url = contextPath+"/yh/subsys/oa/book/act/YHReturnBookManageAct/deleteSaveBook.act?"+param;
     window.location.href = url;
     return true;
   }
  }
</script>
</head>
<body topmargin="5" class="bodycolor" onload="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath %>/book.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 还书查询 </span><br>
   </td>
 </tr>
</table>
<%if(manages!=null && manages.size()>0){ %>
<table class="TableList"  width="95%" align="center">
 <tr class="TableHeader">
    <td nowrap align="center">借书人</td>
    <td nowrap align="center">书  名</td>
    <td nowrap align="center">借书日期</td>
    <td nowrap align="center">还书日期</td>
    <td nowrap align="center">登记人</td>
    <td nowrap align="center">状  态</td>
    <td nowrap align="center">备  注</td>
    <td nowrap align="center">操  作</td>
 </tr>
 <%
   for(int i=0; i < manages.size(); i++){
 %>
 <tr class="TableLine">
      <td nowrap align="center"><%= manages.get(i).getBorPersonName()%></td>
      <td nowrap align="center"><%= manages.get(i).getBookName()%></td>
      <td nowrap align="center"><%= manages.get(i).getBorrowDate()%></td>
      <td nowrap align="center"><%= manages.get(i).getReturnDate()%></td>
      <td nowrap align="center"><%= manages.get(i).getRegUserName()%></td>
      <%
       if(manages.get(i).getStatus().equalsIgnoreCase("1") && manages.get(i).getBookStatus().equalsIgnoreCase("1")){
      %>
      <td nowrap align="center">已还</td>
      <%} else { %>
        <td nowrap align="center">未还</td>
      <%} %>
      <td nowrap align="center"><%= manages.get(i).getBorrowRemark()%></td>   
      <% //if(($BOOK_STATUS2=='0' and $STATUS=='1') or ($BOOK_STATUS2=='1' and $STATUS=='0'))
      if("0".equalsIgnoreCase(manages.get(i).getBookStatus()) && "1".equalsIgnoreCase(manages.get(i).getStatus()) || "1".equalsIgnoreCase(manages.get(i).getBookStatus()) && "0".equalsIgnoreCase(manages.get(i).getStatus()) ){ %>
	      <td nowrap align="center" >
	       <a style="color:blue" href="javascript:returnBook(<%=manages.get(i).getSeqId()%>,'<%=URLEncoder.encode(manages.get(i).getBookNo(),"utf-8")%>');">还书</a>
	      </td>
      <%} 
       if(manages.get(i).getStatus().equalsIgnoreCase("1") && manages.get(i).getBookStatus().equalsIgnoreCase("1")){
      %>
        <td nowrap align="center" >
          <a style="color:blue" href="javascript:deleteSave(<%=manages.get(i).getSeqId()%>,'<%=URLEncoder.encode(manages.get(i).getBookNo(),"utf-8") %>');">删除并存为历史纪录</a>
       </td>
      <%} %>
 </tr>
 <%  }
   %>
</table>
<%}else{ %>
  <table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">没有符合条件的借书记录</div>
    </td>
  </tr>
</tbody>
</table><br>

<%} %>
<br>
<div align="center">
<input type="button"  value="返回" class="BigButton" onClick="gotoBack();">
</div>
<form action="#" id="form1" name="form1">
  <input type="hidden" name="bookNo" id="bookNo" value="<%=bookNo %>">
  <input type="hidden" name="startDate" id="startDate" value="<%=startDate %>">
  <input type="hidden" name="endDate" id="endDate" value="<%=endDate %>">
  <input type="hidden" name="status" id="status" value="<%=status %>">
</form>
</body>
</html>