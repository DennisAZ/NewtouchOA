<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<html>
<head>
<title>图书借阅</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<%
  String status = (String)request.getAttribute("status");
  String flag = "待批借阅";
  if("0".equals(status)){
    flag = "待批借阅";
  }else if("1".equals(status)){
    flag = "已准借阅";
  }else{
    flag = "未准借阅";
  }
  List<YHBookManage> manages = (List<YHBookManage>)request.getAttribute("manages");
%>
<script Language="JavaScript">
function delete_manage(BORROW_ID)
{
 var msg='确认要删借图记录吗？';
 var status = "<%=status%>";
 if(window.confirm(msg)){
  var URL= contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/deleteManage.act?seqId=" + BORROW_ID + "&status="+status;
  window.location=URL;
 }
}

function return_book(BORROW_ID)
{
 msg='确认要还该书吗？';
 var status = "<%=status%>";
 if(window.confirm(msg))
 {
   var URL= contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/returnBooks.act?seqId=" + BORROW_ID + "&status="+status;
  window.location=URL;
 }
}

function delete_flag(BORROW_ID)
{
 var msg='确认要删除吗？';
 var status = "<%=status%>";
 if(window.confirm(msg))
 {
   var URL= contextPath + "/yh/subsys/oa/book/act/YHBookQueryAct/deleteFlag.act?seqId=" + BORROW_ID + "&status=" +status + "&delFlag=1";
   window.location=URL;
 }
}
</script>
</head>

<body class="bodycolor" topmargin="5">
<%
  if(manages.size() > 0){%>
   <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
  <td class="Big"><img src="<%=imgPath%>/book.gif" width="22" height="18"><span class="big3"> 
      <%=flag%>
  </span>
  </td>
  <td valign="bottom" class="small1" align="center">共<span class="big4">&nbsp;<%=manages.size() %></span>&nbsp;条记录
  </td>
</tr>
</table>

<table class="TableList" width="95%" align="center">
<tr class="TableHeader">
  <td nowrap align="center">书名</td>
  <td nowrap align="center">图书编号</td>  
  <td nowrap align="center">借书日期</td>
  <td nowrap align="center">还书日期</td>
  <td nowrap align="center">登记人</td>
  <td nowrap align="center">状态</td>
  <td nowrap align="center">操作</td>
</tr>
<%
	for(int i=0; i<manages.size(); i++){
	  String state = "";	  
	  if("0".equals(manages.get(i).getBookStatus())  && "0".equals(manages.get(i).getStatus())){
	    state = "借书待批";	    
	  }else if("0".equals(manages.get(i).getBookStatus())  && "1".equals(manages.get(i).getStatus())){
	    state = "借书已准";	    
	  }else if("0".equals(manages.get(i).getBookStatus())  && "2".equals(manages.get(i).getStatus())){
	    state = "借书未准";	  
	  }else if("1".equals(manages.get(i).getBookStatus())  && "0".equals(manages.get(i).getStatus())){
	    state = "还书待批";
	  }else if("1".equals(manages.get(i).getBookStatus())  && "1".equals(manages.get(i).getStatus())){
	    state = "还书已准";	   
	  }else if("1".equals(manages.get(i).getBookStatus())  && "2".equals(manages.get(i).getStatus())){
	    state = "还书未准";	    
	  }
%>
  <tr class="TableLine1">
	  <td align="center"><%=manages.get(i).getBookName() %></td>
	  <td align="center"><%=manages.get(i).getBookNo() %></td>
	  <td align="center"><%=manages.get(i).getBorrowDate() %></td>
	  <td align="center"><%=manages.get(i).getReturnDate() %></td>
	  <td align="center"><%=manages.get(i).getRegUserName() %></td>
	  <td align="center">
	    <%=state %>
	  </td>     
	  <td nowrap align="center">
	   <%
	   if("0".equals(manages.get(i).getBookStatus())  && "0".equals(manages.get(i).getStatus())){%>
		   <a href="javascript:void(0);" onclick="delete_manage( <%=manages.get(i).getSeqId()%>);">删除</a>		    
		 <%}else if("0".equals(manages.get(i).getBookStatus()) && "1".equals(manages.get(i).getStatus())){%>
		      <a href="javascript:void(0);" onclick="return_book(<%=manages.get(i).getSeqId() %>);">还书</a>
		 <% }else if("0".equals(manages.get(i).getBookStatus())&& "2".equals(manages.get(i).getStatus())){%>
		       <a href="javascript:void(0);" onclick="delete_manage(<%=manages.get(i).getSeqId() %>);" >删除</a>
		 <% }else if("1".equals(manages.get(i).getBookStatus()) && "0".equals(manages.get(i).getStatus())){%>		    
		 <% }else if("1".equals(manages.get(i).getBookStatus())  && "1".equals(manages.get(i).getStatus())){%>
		     <a href="javascript:void(0);" onclick="delete_flag(<%=manages.get(i).getSeqId() %>);">删除</a>
		  <% }else if("1".equals(manages.get(i).getBookStatus())  && "2".equals(manages.get(i).getStatus())){%>
		     <a href="javascript:void(0);" onclick="return_book(<%=manages.get(i).getSeqId() %>);">还书</a>
		  <%}%> 
    </td>
</tr>
<%}%>
</table>
    
<%}else{%>
  <table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">无<%=flag %></div>
    </td>
  </tr>
</tbody>
</table><br>
<%}%>

</body>

</html>
