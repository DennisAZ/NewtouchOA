	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
		<%@ page  import="java.util.List"%>
  <%@ page  import="java.lang.*"%>
  <%@ page  import="yh.subsys.oa.book.data.*"%> 
<html>
<head>
<title>历史纪录查询</title>
<%
 List<YHBookManage>  manages = (List<YHBookManage>)request.getAttribute("manages");
 String seqId = (String)request.getAttribute("toId");
 String bookNo = (String)request.getAttribute("bookNo");
 String startDate = (String)request.getAttribute("startDate");
 String endDate = (String)request.getAttribute("endDate");
 String status = (String)request.getAttribute("status");
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script>
function delete_borrow(BORROW_ID,TO_ID,BOOK_NO,BOOK_STATUS)
{
  msg='确认要删除该记录吗？';
  if(window.confirm(msg))
  {
     URL="delete.php?BORROW_ID=" + BORROW_ID + "&TO_ID=" + TO_ID + "&BOOK_NO=" + BOOK_NO + "&BOOK_STATUS=" + BOOK_STATUS;
     window.location=URL;
  }
}

function goBack(){
  var url = contextPath + "/subsys/oa/book/borrow_manage/borrow/query.jsp?flag=0";
  window.location.href=url;
}

function deleteBorrow(seqId){
  var  msg='确认要删除该记录吗？';
  if(window.confirm(msg)){
    var url = contextPath + "/yh/subsys/oa/book/act/YHBookRuleAct/deleteHistory.act?seqId="+seqId;
		var rtJson = getJsonRs(url);
		if(rtJson.rtState == '0'){
      $("form1").submit();
      return true;
	  }
  }
}
</script>
</head>

<body class="bodycolor" topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="absmiddle"><span class="big3"> 历史纪录查询 </span><br>
   </td>
 </tr>
</table>
<% 
  if(manages !=null && manages.size()>0){%>
     <table class="TableList" width="95%" align="center">
		 <tr class="TableHeader">
		    <td nowrap align="center">借书人</td>
		    <td nowrap align="center">书  名</td>
		    <td nowrap align="center">借书日期</td>
		    <td nowrap align="center">还书日期</td>
		    <td nowrap align="center">实还日期</td>
		    <td nowrap align="center">登记人</td>
		    <td nowrap align="center">状  态</td>
		    <td nowrap align="center">备  注</td>    
		    <td nowrap align="center">操  作</td>
		 </tr>
		 <%
		   for(int i=0; i<manages.size(); i++){%>
		      <tr class="TableLine1">
				      <td nowrap align="center"><%=manages.get(i).getBorPersonName() %>&nbsp;</td>
				      <td nowrap align="center"><%=manages.get(i).getBookName() %>&nbsp;</td>
				      <td nowrap align="center"><%=manages.get(i).getBorrowDate() %>&nbsp;</td>
				      <td nowrap align="center"><%=manages.get(i).getReturnDate() %>&nbsp;</td>
				      
				         <%
				           if(manages.get(i).getRealReturnTime() != null){%>
				               <td nowrap align="center"> <%=manages.get(i).getRealReturnTime() %></td>
				          <%} else{%>
				               <td nowrap align="center"> &nbsp;</td>
				          <%}%>
				      
				      <td nowrap align="center"><%=manages.get(i).getRegUserName() %>&nbsp;</td>
				      <td nowrap align="center">
				        <%
				          if("1".equalsIgnoreCase(manages.get(i).getBookStatus()) && "1".equalsIgnoreCase(manages.get(i).getStatus())){%>
				                      已还
				        <% } if("0".equalsIgnoreCase(manages.get(i).getBookStatus()) && "1".equalsIgnoreCase(manages.get(i).getStatus()) || "1".equalsIgnoreCase(manages.get(i).getBookStatus()) && "0".equalsIgnoreCase(manages.get(i).getStatus())){
				         %>
				                    未还
				         <% 
				        }%>
				      </td>
				      <td nowrap align="center"><%=manages.get(i).getBorrowRemark() %>&nbsp;</td>      
				      <td nowrap align="center">
				        <%
				          if("1".equalsIgnoreCase(manages.get(i).getBookStatus()) && "1".equalsIgnoreCase(manages.get(i).getStatus())){%>
				             <a href="javascript:deleteBorrow(<%=manages.get(i).getSeqId() %>);"> 彻底删除</a>
				        <%} %>
				      &nbsp;</td>
          </tr>
		 <%  }
		 %>
		 </table>
 <%}else{%>
    <table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">没有历史记录</div>
    </td>
  </tr>
</tbody>
</table><br>
 <%}%>
<br>
<div align="center">
<input type="button"  value="返回" class="BigButton" onclick="goBack();return false;">
</div>
<form action="<%=contextPath %>/yh/subsys/oa/book/act/YHBookRuleAct/findHistory.act" id="form1" name="form1">
  <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
  <input type="hidden" name="bookNo" id="bookNo" value="<%=bookNo %>">
  <input type="hidden" name="startDate" id="startDate" value="<%=startDate %>">
  <input type="hidden" name="endDate" id="endDate" value="<%=endDate %>">
  <input type="hidden" name="status" id="status" value="<%=status %>">
</form>
</body>
</html>