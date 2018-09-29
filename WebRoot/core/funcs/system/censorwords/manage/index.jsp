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
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
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

function checkMags(cntrlId){
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == cntrlId && checkArray[i].checked ){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
  return ids;
}

function deleteAllUser() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除词语，请至少选择其中一个。");
    return;
  }
  if(!confirmDel()) {
  	return ;
  }  
  var url = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/deleteCensorWords.act";
  var rtJson = getJsonRs(url, "idStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}

function getUserName(userId){
  var userName = "";
  var url = "<%=contextPath %>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + userId);
  if (rtJson.rtState == "0") {
    userName = rtJson.rtData;
    //$("userNames").innerHTML = userName;
    return userName;
  }else {
    alert(rtJson.rtMsrg); 
  } 
  return userName;
}

</script>
</head>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" WIDTH="16" HEIGHT="16" align="absmiddle"><span class="big3">&nbsp;词语过滤管理</span>
    </td>
<%
  if(wordList.size()>0){ 
%>
    <td align="center" class="small1">
    共<span class="big4">&nbsp;<%=sumSize%></span>&nbsp;个词语
    </td>
    </tr>
<%
  }
%>
</table>

<form id="form1">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.censorwords.data.YHCensorWords.java"/>
<table class="TableBlock" width="100%">
<%
  for(int i = 0; i < wordList.size(); i++) {
    YHCensorWords cen = wordList.get(i);
    String replaceMent = cen.getReplacement();
    String find = cen.getFind();
    if(cen.getReplacement() == null){
      replaceMent = " ";
    }
    if(cen.getFind() == null){
      find = " ";
    }
%>
  <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
    <td align="center">&nbsp;<input type="checkbox" name="deleteFlag" id="deleteFlag" value="<%=cen.getSeqId()%>"></td>
    <td><%=find%></td>
    <td><%=replaceMent%></td>
    <td nowrap align="center"><span id="userNames"><script>document.write(getUserName('<%=cen.getUserId()%>'));</script></span></td>
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

<table class="TableList" width="100%" align="center">
  <tr>
    <td class="TableContent" nowrap align="center" width="80"><b>快捷操作：</b></td>
    <td class="TableControl" nowrap>&nbsp;
   <img src="<%=imgPath%>/delete.gif"> <a href="javascript:deleteAll();" title="删除所有的词语">全部删除</a>&nbsp;&nbsp;
    </td>
  </tr>
</table>
</form>
<%
  }else{
%>
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">暂无添加词语</div>
    </td>
  </tr>
</table>
<% 
  }
%>
</body>
</html>