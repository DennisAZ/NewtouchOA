<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.oa.book.data.*"%>
<%
	List<YHBookManager> managers = (List<YHBookManager>)request.getAttribute("managers");
%>
<html>
<head>
<title>设置管理员 </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript">
	function jumpto(){
   window.location = contextPath + "/subsys/oa/book/setmanager/addManager.jsp"
	}

	function deleteManager(seqId){
	  var  msg='确认要删除该记录吗？';
	  if(window.confirm(msg)){	   
	    var url = contextPath + "/yh/subsys/oa/book/act/YHSetBookManagerAct/delManager.act?seqId="+seqId;
			window.location.href = url;
			return true;
	  }
	}
</script>

</head>
 
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="8" class="small">
  <tr>
    <td class="Big"><img src="<%= imgPath %>/notify_new.gif"><span class="big3"> 设置管理员 </span><br>
    </td>
  </tr>
</table>
 
<div align="center">
<input type="button"  value="设置管理员" class="BigButton" onClick="javascript:jumpto();return false;" title="设置管理员">
</div>
 
<br>
 
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%= imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%= imgPath %>/notify_open.gif"><span class="big3"> 管理管理员</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
<%
  if(managers != null && managers.size() >0){%>
    <table class="TableList"  width="600"  align="center" >
       <tr class="TableHeader">
          <td nowrap align="center">管理员</td>
          <td nowrap align="center">所管部门</td>
          <td nowrap align="center">操作</td>
       </tr> 
    <%
      for(int i=0; i<managers.size(); i++){%>
       <tr class="TableLine1">
          <td align="left" width="50%"><%=managers.get(i).getManagerNames() %></td>
          <td align="left" width="50%"><%=managers.get(i).getDeptNames() %></td>
          <td align="left" width="50%" nowrap>
    	       <a href="<%=contextPath %>/yh/subsys/oa/book/act/YHSetBookManagerAct/editManager.act?seqId=<%=managers.get(i).getSeqId()%>">编辑</a>&nbsp;&nbsp;
    	       <a href="javascript:void(0);" onclick="deleteManager(<%=managers.get(i).getSeqId()%>);">删除</a>    	 
          </td>
      </tr>       
      <%}%> 
   </table> 
<%}else {%>
 <table align="center" width="230" class="MessageBox">
  <tbody><tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div style="font-size: 12pt;" class="content">没有管理员记录!</div>
    </td>
  </tr>
</tbody>
</table>  
<%}%>
</div>
</body>
</html>
