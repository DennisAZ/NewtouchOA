<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  ArrayList<YHCensorWords> wordList = (ArrayList<YHCensorWords>)request.getAttribute("wordList");
  int sumSize = wordList.size();
  Object userName = request.getAttribute("userName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>词语过滤管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/censorwords/js/censorWordsUtil.js"></script>
<script type="text/javascript">
var sumSize = "<%=sumSize%>";
var userName = "<%=userName%>";
function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function confirmDel() {
  if(confirm("确认要删除所选词语吗！")) {
    return true;
  }else {
    return false;
  }
}

function confirmAll() {
  if(confirm("确认要删除所有词语吗！")) {
    return true;
  }else {
    return false;
  }
}

function deleteAll(){
  if(!confirmAll()) {
  	return ;
  }  
  var url = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/deleteAllCensorWords.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    //alert(rtJson.rtMsrg); 
    location = "<%=contextPath %>/core/funcs/system/censorwords/manage/deleteAll.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  } 
}
function deleteAllUser() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除短消息，请至少选择其中一个。");
    return;
  }
  if(!confirmDel()) {
  	return ;
  }   
  var url = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/deleteCensorWords.act";
  var rtJson = getJsonRs(url, "idStrs=" + idStrs);
  if (rtJson.rtState == "0") {
      //alert(rtJson.rtMsrg); 
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}

</script>
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp;查询结果 （最多显示200记录）</span>
    </td>
</table>
<form id="form1">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.censorwords.data.YHCensorWords.java"/>

<table class="TableBlock" width="100%">
<%
  for(int i = 0; i < wordList.size(); i++) {
    YHCensorWords cen = wordList.get(i);
%>
  <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
    <td align="center">&nbsp;<input type="checkbox" name="deleteFlag" id="deleteFlag" value="<%=cen.getSeqId()%>"></td>
    <td><%=cen.getFind()%></td>
    <td><%=cen.getReplacement()%></td>
    <td nowrap align="center"><%=userName%></td>
    <td nowrap align="center">
    <a href="<%=contextPath%>/core/funcs/system/censorwords/manage/edit.jsp?seqId=<%=cen.getSeqId()%>">编辑</a>
    </td>
  </tr>
<%
  } 
  if(wordList.size()>0){ 
%>
<thead class="TableHeader">
  <td width="40" align="center">选</td>
  <td align="center">不良词语</td>
  <td align="center">替换为</td>
  <td width="100" align="center">添加人</td>
  <td width="60" align="center">操作</td>
</thead>
<tr class="TableControl">
  <td colspan="7">&nbsp;
    <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"/><label for="checkAlls">全选</label> &nbsp;
    <img src="<%=imgPath%>/delete.gif" align="absMiddle"><a href="javascript:deleteAllUser();" title="删除所选词语">删除</a>&nbsp;
  </td>
</tr>
</table>
<br>
</form>
<%
  }else{
%>
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的查询结果</div>
    </td>
  </tr>
</table>
<% 
  }
%>
<div align="center">
  <input type="button"  value="返回" class="BigButton" onClick="window.history.back();">
</div>
</body>
</html>