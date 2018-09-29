	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
	<%@ page  import="java.util.List"%>
  <%@ page  import="java.lang.*"%>
  <%@ page  import="yh.subsys.oa.book.data.*"%> 
  <%@ page import=" java.net.URLEncoder;" %>
<html>
<head>
<title>借书管理</title>
  <%
    List<YHBookManage> bmanages = (List<YHBookManage>)request.getAttribute("bmanages");
    List<YHBookManage> rmanages = (List<YHBookManage>)request.getAttribute("rmanages");
    List<YHBookManage> regManages = (List<YHBookManage>)request.getAttribute("regManages");
  	YHPage pages = (YHPage)request.getAttribute("page");
  %>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/sysyearlog.js"></script>
  <script type="text/javascript">
      function gotoPage(){
        window.location.href = contextPath + "/subsys/oa/book/borrow_manage/return/index.jsp";
      }

      function goPage(currNo){
        var url = contextPath + "/yh/subsys/oa/book/act/YHBookRuleAct/index.act?currNo="+currNo;     
        window.location.href=url;   
      }
      function returnBook(borrowId, bookNo){
       var url = contextPath +"/yh/subsys/oa/book/act/YHBookRuleAct/returnBookByAdmin.act?bookNo="+bookNo +"&borrowId=" +borrowId;
       window.location.href=url;  
      }
  </script>
</head>

<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 借书登记 </span><br>

    </td>
  </tr>
</table>

<div align="center">
<input type="button"  value="借书登记" class="BigButton" onclick="gotoPage(); return false;" title="借书登记">
</div>

<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/book.gif" align="absmiddle"><span class="big3"> 借书管理</span>&nbsp;
    </td>
    <% 
      if(pages.getTotalRowNum() > 0){%>
         <td align="right" valign="bottom" class="small1">
                               共<span class="big4">&nbsp;<%=pages.getTotalRowNum() %></span>&nbsp;条&nbsp;&nbsp;
                               每页显示<%=pages.getPageSize() %>条
        </td>
      <%}%> 
    <%
      if(pages.getTotalPageNum() >= 1){%>
      <% %>
         <td align="center" valign="bottom" class="small1">
           <%
             if(pages.getCurrentPageIndex() > 1){%>
               <a class="A1" href="javascript:void(0);" onclick="goPage(1);">首页</a>&nbsp;
           <%}%>
           <%
             if(pages.getCurrentPageIndex() < pages.getTotalPageNum()){%>
               <a class="A1" href="javascript:void(0);" onclick="goPage(<%=pages.getTotalPageNum() %>);">末页</a>&nbsp;
            <%}%>
		        
		          <%
		            if(pages.getCurrentPageIndex() -2 >0){
		              for(int no=pages.getCurrentPageIndex() -2; no<pages.getCurrentPageIndex(); no++ ){%>
		                	<a class="A1" href="javascript:void(0);" onclick="goPage(<%=no %>);">[<%=no %>]&nbsp;</a>&nbsp;
		             <%}
		          }else{
		              for(int no=1; no<pages.getCurrentPageIndex(); no++){%>
		                 <a class="A1" href="javascript:void(0);" onclick="goPage(<%=no%>);">[<%=no %>]&nbsp;</a>&nbsp;
		          <%}
		          }%>
		          
		          <%if(pages.getTotalRowNum() > 0){ %>
		         [<%=pages.getCurrentPageIndex() %>]&nbsp;
		          <%} %>
		        
		         <% 
		            if(pages.getCurrentPageIndex()+2 < pages.getTotalPageNum()){
		              for(int no=pages.getCurrentPageIndex()+1; no<pages.getCurrentPageIndex()+2; no++){%>
		                 <a class="A1" href="javascript:void(0);" onclick="goPage(<%=no%>);">[<%=no %>]&nbsp;</a>&nbsp;
		            <%}
		            }else{
		              for(int no=pages.getCurrentPageIndex()+1; no<pages.getTotalPageNum(); no++){%>
		                <a class="A1" href="javascript:void(0);" onclick="goPage(<%=no%>);">[<%=no %>]&nbsp;</a>&nbsp;
		              <%}
		            }		         
		         %>
		        <%
		          if(pages.getCurrentPageIndex() >1){%>
		          <a class="A1" href="javascript:void(0);" onclick="goPage(<%=pages.getCurrentPageIndex()-1 %>);">上一页&nbsp;</a>&nbsp;
		        <%}%>
		              
		        <%
		          if(pages.getCurrentPageIndex() < pages.getTotalPageNum()){%>
		           <a class="A1" href="javascript:void(0);" onclick="goPage(<%=pages.getCurrentPageIndex()+1%>);">下一页&nbsp;</a>&nbsp;
		          <%}%>     
    </td> 
    <%}%> 
    </tr>
</table>
<% 
  if(pages.getTotalRowNum() >0 ){%>
 <table class="TableList"  width="95%" align="center">
  <tr class="TableHeader">
    <td nowrap align="center">借书人</td>
    <td nowrap align="center">书  名</td>
    <td nowrap align="center">借书日期</td>
    <td nowrap align="center">还书日期</td>
    <td nowrap align="center">登记人</td>

    <td nowrap align="center">备  注</td>
    <td nowrap align="center" width="6%">操  作</td>
  </tr>
  
  <%
    for(int i=0; i<regManages.size(); i++){%>
     <tr class="TableLine1">
        <td nowrap align="center"><%=regManages.get(i).getBorPersonName() %>&nbsp;</td>
	      <td nowrap align="center"><%=regManages.get(i).getBookName() %>&nbsp;</td>
	      <td nowrap align="center"><%=regManages.get(i).getBorrowDate() %>&nbsp;</td>
	      <td nowrap align="center"><%=regManages.get(i).getReturnDate() %>&nbsp;</td>
	      <td nowrap align="center"><%=regManages.get(i).getRegUserName() %>&nbsp;</td>
	      <td nowrap align="center"><%=regManages.get(i).getBorrowRemark() %>&nbsp;</td>
        <td nowrap align="center"><a href="javascript:void(0);" onclick="returnBook(<%=regManages.get(i).getSeqId() %>, '<%=URLEncoder.encode(regManages.get(i).getBookNo(),"utf-8")%>');return false;">还书</a></td>
    </tr>
    <%}%>
  </table>
<%}else{%>
  <table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">没有借书确认</div>
    </td>
  </tr>
</tbody>
</table><br>
<%}%>

<table border="0" width="95%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/book.gif" align="absmiddle"><span class="big3"> 借书确认</span>&nbsp;
    </td>
  </tr>
</table>

<%
  if(bmanages!=null && bmanages.size()>0){%>
    <table class="TableList" width="95%" align="center">
		  <tr class="TableHeader">
		    <td nowrap align="center">借书人</td>
		    <td nowrap align="center">书  名</td>
		    <td nowrap align="center">借书日期</td>
		    <td nowrap align="center">还书日期</td>
		    <td nowrap align="center">登记人</td>
		    <td nowrap align="center">备  注</td>
		    <td nowrap align="center" width="6%">操  作</td>
		  </tr>
		  <%
		    for(int i=0; i<bmanages.size(); i++){%>
		  <tr class="TableLine1">
	      <td nowrap align="center"><%=bmanages.get(i).getBorPersonName() %>&nbsp;</td>
	      <td nowrap align="center"><%=bmanages.get(i).getBookName() %>&nbsp;</td>
	      <td nowrap align="center"><%=bmanages.get(i).getBorrowDate() %>&nbsp;</td>
	      <td nowrap align="center"><%=bmanages.get(i).getReturnDate() %>&nbsp;</td>
	      <td nowrap align="center"><%=bmanages.get(i).getRegUserName() %>&nbsp;</td>
	      <td nowrap align="center"><%=bmanages.get(i).getBorrowRemark() %>&nbsp;</td>
	      <td nowrap align="center">
	      	<a href="<%=contextPath%>/yh/subsys/oa/book/act/YHBookRuleAct/agreeBorrOrNot.act?seqId=<%=bmanages.get(i).getSeqId()%>&flag=1&bookNo=<%=URLEncoder.encode(bmanages.get(i).getBookNo(), "utf-8") %>&toId=<%=bmanages.get(i).getBuserId() %>">
	      	同意</a>
	      	<a href="<%=contextPath%>/yh/subsys/oa/book/act/YHBookRuleAct/agreeBorrOrNot.act?seqId=<%=bmanages.get(i).getSeqId()%>&flag=0&bookNo=<%= URLEncoder.encode(bmanages.get(i).getBookNo(),"utf-8") %>&toId=<%=bmanages.get(i).getBuserId() %>">
	      	退回</a>
	      </td>
      </tr>
		   <%}%>		  
		  </table>    
 <%}else{%>
  <table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">没有借书确认</div>
    </td>
  </tr>
</tbody>
</table><br>
 <%}%>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/book.gif" align="absmiddle"><span class="big3"> 还书确认</span>&nbsp;
    </td>
  </tr>
</table>
<%
  if(rmanages != null && rmanages.size() >0){%>
    <table class="TableList" width="95%" align="center">
		  <tr class="TableHeader">
		    <td nowrap align="center">借书人</td>
		    <td nowrap align="center">书  名</td>
		    <td nowrap align="center">借书日期</td>
		    <td nowrap align="center">还书日期</td>
		    <td nowrap align="center">登记人</td>
		
		    <td nowrap align="center">备  注</td>
		    <td nowrap align="center" width="6%">操  作</td>
		  </tr>
		    <%
		      for(int i=0; i<rmanages.size(); i++){%>
		    <tr class="TableLine1">
		      <td nowrap align="center"><%=rmanages.get(i).getBorPersonName()%>&nbsp;</td>
		      <td nowrap align="center"><%=rmanages.get(i).getBookName() %>&nbsp;</td>		
		      <td nowrap align="center"><%=rmanages.get(i).getBorrowDate() %>&nbsp;</td>
		      <td nowrap align="center"><%=rmanages.get(i).getReturnDate() %>&nbsp;</td>
		      <td nowrap align="center"><%=rmanages.get(i).getRegUserName() %>&nbsp;</td>
		      <td nowrap align="center"><%=rmanages.get(i).getBorrowRemark() %>&nbsp;</td>
		      <td nowrap align="center">
		      <a href="<%=contextPath%>/yh/subsys/oa/book/act/YHBookRuleAct/agreeReturnOrNot.act?seqId=<%=rmanages.get(i).getSeqId()%>&flag=1&bookNo=<%= URLEncoder.encode(rmanages.get(i).getBookNo(),"utf-8") %>&toId=<%=rmanages.get(i).getBuserId() %>">同意</a>
		      <a href="<%=contextPath%>/yh/subsys/oa/book/act/YHBookRuleAct/agreeReturnOrNot.act?seqId=<%=rmanages.get(i).getSeqId()%>&flag=0&bookNo=<%= URLEncoder.encode(rmanages.get(i).getBookNo(), "utf-8") %>&toId=<%=rmanages.get(i).getBuserId() %>">退回</a>
	
		      	</td>
      </tr>
		    <%}%>
		  
		  </table>
 <%} else{%>
    <table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">没有还书确认</div>
    </td>
  </tr>
</tbody>
</table><br>
 <%}%>
</body>
</html>