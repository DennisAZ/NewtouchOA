<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%> 
<html>
<head>
<title>图书详细资料 </title>
<%	
YHBookInfo book = (YHBookInfo)request.getAttribute("aBook");
List<YHBookManage> daipi = (List<YHBookManage>)request.getAttribute("daipi");
List<YHBookManage> weihuan = (List<YHBookManage>)request.getAttribute("weihuan");  
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 图书详细信息</span>
    </td>
  </tr>
</table>

<table class="TableBlock"  width="450" align="center">
   <tr>
    <td nowrap class="TableData" width="70">部门：</td>
    <td nowrap class="TableData"  width="280">
        <%=book.getDeptName() %>
    </td>
    <td class="TableData" rowspan="6" width="100">
        <%
          if("".equals(book.getAttachmentId()) || null == book.getAttachmentId()){%>
            <center>暂无封面</center> 
         <% }else{%>
           <a href="<%=contextPath %>/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=<%=book.getAttachmentName() %>&attachmentId=<%=book.getAttachmentId() %>&module=book&directView=1">
           <img src="<%=contextPath %>/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=<%=book.getAttachmentName() %>&attachmentId=<%=book.getAttachmentId() %>&module=book" width='100' border=1 alt="文件名:<%=book.getAttachmentName() %>"></img>
           </a>
         <%} %>          
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">书名：</td>
    <td nowrap class="TableData"><%=book.getBookName() %></td>
   </tr>
   <tr>
    <td nowrap class="TableData">编号：</td>
    <td nowrap class="TableData">
     <%=book.getBookNo() %>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">图书类别：</td>
    <td nowrap class="TableData">
      <%=book.getTypeName() %>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">作者：</td>
    <td nowrap class="TableData">
    <%=book.getAuthor() %>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">ISBN号：</td>
    <td nowrap class="TableData">
    <%=book.getIsbn() %>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">出版社：</td>
    <td nowrap class="TableData" colspan="2">
    <%=book.getPubHouse() %>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">出版日期：</td>
    <td nowrap class="TableData" colspan="2">
    <%=book.getPubDate() %>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">存放地点：</td>
    <td nowrap class="TableData" colspan="2">
    <%=book.getArea() %>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">数量：</td>
    <td nowrap class="TableData" colspan="2">
    <%=book.getAmt() %>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">借书待批：</td>
    <td class="TableData" colspan="2">
    <% 
      if(daipi.size()>0){%>
        <table class="TableList"  width="100%"  align="center" >
          <tr align="center">
     <td nowrap class="TableData">借书人</td>
     <td nowrap class="TableData">借书日期</td>
     <td nowrap class="TableData">归还日期</td>
     <td nowrap class="TableData">数量</td>
   </tr>
          <%for(int i=0; i<daipi.size(); i++){%>
            <tr align="center">
				     <td nowrap class="TableData"><%=daipi.get(i).getBorPersonName() %></td>
				     <td nowrap class="TableData"><%=daipi.get(i).getBorrowDate() %></td>
				     <td nowrap class="TableData"><%=daipi.get(i).getReturnDate() %></td>
				     <td nowrap class="TableData">1</td>
				   </tr>
          <%}%>
      </table> 
      <%}else{%>
               没有待批记录
      <%}%>
</td>
  <tr>
    <td nowrap class="TableData">未还记录：</td>
    <td class="TableData" colspan="2">
    <%
    	if(weihuan.size()>0){%>
   <table class="TableList" width="100%" align="center" >
     <tr align="center">
     <td nowrap class="TableData">借书人</td>
     <td nowrap class="TableData">借书日期</td>
     <td nowrap class="TableData">归还日期</td>
     <td nowrap class="TableData">数量</td>
   </tr>
      <%
      		for(int i=0; i<weihuan.size(); i++){%>
      <tr align="center">
		        <td nowrap class="TableData"><%=weihuan.get(i).getBorPersonName() %></td>
				     <td nowrap class="TableData"><%=weihuan.get(i).getBorrowDate() %></td>
				     <td nowrap class="TableData"><%=weihuan.get(i).getReturnDate() %></td>
				     <td nowrap class="TableData">1</td>
   		</tr>   
      <%}%>
  </table> 
    	
    <%}else{%>
        没有未还记录
    <%}%>
  </td>
   </tr>
   <tr>
    <td nowrap class="TableData">价格：</td>
    <td nowrap class="TableData" colspan="2">
      <%=book.getPrice() %>元
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">内容简介：</td>
    <td class="TableData" colspan="2">
      <%=book.getBrief()%>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">借阅范围：</td>
    <td nowrap class="TableData" colspan="2">
      <%=book.getOpenNames() %>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">借阅状态：</td>
    <td nowrap class="TableData" colspan="2">
     <%
       if("0".equals(book.getLend())){%>
                 未借出
     <% }else{%>
                已借出
     <%}
     %>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">新建人：</td>
    <td nowrap class="TableData" colspan="2">
     <%=book.getBorrPerson() %>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">备注：</td>
    <td nowrap class="TableData" colspan="2">
    <%=book.getMemo() %>
    </td>
   </tr>
   <tr align="center" class="TableControl">
     <td colspan="3">
       <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
     </td>
   </tr>

</table>

</body>
</html>