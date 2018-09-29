<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.*,java.text.*" %>
<%@ page import="yh.core.funcs.email.data.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<title>查看邮件状态 ：</title>
<script type="text/javascript">
function deleteMailByMailId(bodyId){
  var msg = "";
  var delete_str = "";
  delete_str = getChecked();
  if(delete_str == "") {
    alert("要删除邮件，请至少选择其中一封。");
    return;
  }
   msg='确认要删除所选邮件吗？\n注意：如果删除内部收件人未读邮件，内部收件人将不会接收到该邮件！';
  if(window.confirm(msg)) {
    var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deleteMailByMailId.act?mailId="+ delete_str + "&bodyId="+ bodyId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg);
    }
    location.reload();
  }
}
</script>
<%
  List<YHEmail> statusList = (List<YHEmail>)request.getAttribute("statusList");
  int bodyId = (Integer)request.getAttribute("bodyId");
%>
</head>
<body onload="doInit();">
	<table cellscpacing="1" cellpadding="3" width="100%" class="TableList">
	  <tr class="TableHeader">
	    <td nowrap align="center">选择</td>
	    <td nowrap align="center">状态</td>
	    <td nowrap align="center">收信人</td>
	    <td nowrap align="center">操作</td>
	  </tr>
	  <%
     int k = 0;
     for(int i = 0; i < statusList.size(); i++) {
       YHEmail em = statusList.get(i);
   %>
	  <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
	    <td nowrap align="center"><input type="checkbox" name="check_diay_search" value="<%=em.getSeqId() %>"/></td>
	    <td nowrap align="center">
	      <span>
        <%if(em.getDeleteFlag().equals("1")){ %><img vspace="0" hspace="2" src="<%=imgPath%>/email_delete.gif" title="收信人已删除">
        <%}else{
            if(em.getReadFlag().equals("0")){ %><img  vspace="0" hspace="2" src="<%=imgPath%>/cmp/email/email_close.gif" title="未提醒">
            <%}else{ %> <img  vspace="0" hspace="2" src="<%=imgPath%>/cmp/email/inbox/email_open.gif" title="收信人已读">
            <%}
           }%>
        </span>
      </td>
	    <td nowrap align="center">
	      <input type="hidden" id="toId<%=k%>" name="toId<%=k%>" value="<%=em.getToId()%>">
        <span id="toId<%=k%>Desc"></span>
	    </td>
	    <td>
	     <%if("0".equals(em.getReadFlag())){ %>
         <A href="<%=contextPath%>/core/funcs/email/new?sendEdit=1&seqId=<%=bodyId%>">编辑</A>&nbsp;&nbsp;&nbsp;&nbsp; 
         <A href="<%=contextPath%>/core/funcs/email/new?resend=1&emailId=<%=bodyId%>&resendUserId=<%=em.getToId()%>">再次发送</A>&nbsp; 
       <%}else {%>
         <A href="<%=contextPath%>/core/funcs/email/new?resend=1&emailId=<%=bodyId%>&resendUserId=<%=em.getToId()%>">再次发送</A>&nbsp; 
       <%} %>
      </td>
	  </tr>
 <% k++;
 } %>
 <!-- <tr class="TableControl">
<td colspan="10">
 <div style="float:left;">
   <input type="checkbox" name="allbox" id="allbox" onClick="checkSearchAll();">
   <label for="allbox">全选</label>&nbsp;
   <a href="javascript:deleteMailByMailId(<%=bodyId%>);" title="删除所选邮件"><img src="<%=imgPath%>/cmp/email/inbox/delete.gif" align="absMiddle">删除</a>&nbsp;
</div> 
</td>
</tr>-->
	</table>
</body>
<script type="text/javascript">
function doInit(){
  for(var i =0 ; i < <%=k%> ;i++){
    bindDesc([{cntrlId:"toId"+i, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}
</script>
</html>