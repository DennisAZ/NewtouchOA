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
<title>邮箱管理</title>
<%
List<YHEmailBox> emboxList = (List<YHEmailBox>)request.getAttribute("boxList");
%>
</head>
<body  topmargin="5" >
<table  width="90%" align="center">
     <tr class="TableHeader">
      <td nowrap align="center" width="50">排序号</td>
      <td nowrap align="center">名称</td>
      <td nowrap align="center">占用空间</td>
      <td nowrap align="center">每页显示邮件数</td>
      <td nowrap align="center" width="60">操作</td>
    </tr>
    <tr class="TableData">
      <td nowrap>
      </td>
      <td nowrap align="center">收件箱</td>
      <td>　10240 字节 （约合10M）</td>
      <td nowrap><input type="text" value="" name="PAGESIZE_IN0" class="SmallInput" size="3"> <input type="button" value="设置" class="SmallButton" onClick="SetEmailNums('','PAGESIZE_IN0','0');"></td>
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">发件箱</td>
      <td>　1024 字节 （约合M）</td>
      <td nowrap><input type="text" value="" name="PAGESIZE_OUT" class="SmallInput" size="3"> <input type="button" value="设置" class="SmallButton" onClick="SetEmailNums('','PAGESIZE_OUT','0');"></td>
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">已发送邮件箱</td>
      <td>　1024 字节 （约合M）</td>
      <td nowrap><input type="text" value="" name="PAGESIZE_SENT" class="SmallInput" size="3"> <input type="button" value="设置" class="SmallButton" onClick="SetEmailNums('','PAGESIZE_SENT','0');"></td>
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">外发邮件箱</td>
      <td>　1024 字节 （约合M）</td>
      <td nowrap><input type="text" value="" name="PAGESIZE_WEB" class="SmallInput" size="3"> <input type="button" value="设置" class="SmallButton" onClick="SetEmailNums('','PAGESIZE_WEB','0');"></td>
      <td nowrap align="center"></td>
    </tr>
    <tr class="TableData">
      <td nowrap></td>
      <td nowrap align="center">废件箱</td>
      <td>　1024 字节 （约合M）</td>
      <td nowrap><input type="text" value="" name="PAGESIZE_DEL" class="SmallInput" size="3"> <input type="button" value="设置" class="SmallButton" onClick="SetEmailNums('','PAGESIZE_DEL','0');"></td>
      <td nowrap align="center"></td>
    </tr>
    <%  for(int i = 0; i < emboxList.size(); i++) {
          YHEmailBox embox = emboxList.get(i);
    %>
    <tr class="TableData">
      <td nowrap align="center"><%=embox.getBoxNo() %></td>
      <td nowrap align="center"><%=embox.getBoxName() %></td>
      <td nowrap>　1024 字节 （约合M）</td>
      <td nowrap><input type="text" value="<%=embox.getDefaultCount() %>" id="PAGESIZE_IN<%=embox.getSeqId() %>" class="SmallInput" size="3"> <input type="button" value="设置" class="SmallButton" onClick="setEmailNums(<%=embox.getSeqId() %>,'boxName','PAGESIZE_IN<%=embox.getSeqId() %>');"></td>
      <td nowrap align="center">
      <a href="<%=contextPath %>//core/funcs/email/mailbox/edit.jsp?boxId=<%=embox.getSeqId() %>"> 编辑</a>
      <a href="javascript:deleteMisBox(<%=embox.getSeqId() %>);"> 删除</a>
      </td>
    </tr>
    <%} %>
    <tr class="TableContent">
      <td nowrap align="center" colspan="2"><b>合计：</b></td>
      <td nowrap colspan="3"></td>
    </tr>
</table>
</body>
<script>
function SetEmailNums(boxId,boxName,cntrlId){
  if(1){
    alert('完善中');
    return;
  }
  var defCount = $(cntrlId).value;
  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/setDefaultCount.act?boxId=" + boxId + "&defCount=" + defCount;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert(rtJson.rtMsrg);
  }
}
function deleteMisBox(boxId){
  msg='确认要删除所选邮件吗？';
  if(window.confirm(msg)){
	  var url = contextPath + "/yh/core/funcs/email/act/YHEmailBoxAct/deleteBox.act?boxId=" + boxId;
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert(rtJson.rtMsrg);
	    window.location.reload();
	  }
  }
}
</script>
</html>