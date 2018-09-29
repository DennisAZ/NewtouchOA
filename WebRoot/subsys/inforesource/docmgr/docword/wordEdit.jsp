<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String treeId = request.getParameter("treeId");
  if (treeId == null){
    treeId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主题词管理</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/docmgr/docword/js/wordlogic.js"></script>
<script type="text/javascript">
var treeId = "<%=treeId%>";
var parentStr = "";
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/getDept.act?treeId=" + treeId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    if(rtJson.rtData["typeFlag"]==0){
      document.getElementById("typeFlag2").disabled="true";
      document.getElementById("nextNew").style.display="";
    }
    parentStr = rtJson.rtData.parentId;
    deptFunc();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function commitDept(){
  if(checkForm()&& checkWord()){ 
    var url = "<%=contextPath%>/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/updateWord.act?treeId=" + treeId;
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    if(rtJson.rtState == "0"){
	  var prcsJson = rtJson.rtData;
	  var curTree = parent.wordListTree.tree;
	  var curNode = curTree.getNode(treeId);
	  curNode.name = $F('word');
	  if(prcsJson[0].typeFlag=='0'){
	    curNode.imgAddress = "/yh/core/styles/style1/img/dtree/folder.gif";
	  }else{
	    curNode.imgAddress = "/yh/core/styles/style1/img/dtree/file.jpg";
	  }
      curTree.updateNode(curNode.nodeId,curNode); 
      show.style.display="";
      setTimeout("show.style.display='none'",2000);
    }else{
      alert(rtJson.rtMsrg);
    }
  }
}

function deleteDept(){
  if(confirm("确认要主题词类别/主题词吗？这将同时删除所有下级类别和和其中的主题词！")){
    var url = "<%=contextPath%>/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/deleteDept.act?treeId=" + treeId;
    var rtJson = getJsonRs(url, mergeQueryString($("form1"))); 
    if(rtJson.rtState == '0'){
  	  var curTree = parent.wordListTree.tree;
      curTree.removeNode(treeId);
	  window.location.href='<%=contextPath%>/subsys/inforesource/docmgr/docword/wordInput.jsp';
    }else{ 
	  alert(rtJson.rtMsrg); 
    } 
  }else{
    return false;
  }
}

function nextWord(){
  location.href="/yh/subsys/inforesource/docmgr/docword/wordInput.jsp?treeId="+treeId;
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/selectWordToAttendance.act";
  var rtJson = getJsonRs(url, "userDeptId="+treeId);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("parentId");
  var options = document.createElement("option"); 
  options.value = "0";
  options.innerHTML = "无"; 
  selects.appendChild(options);
  for(var i = 0; i < prcs.length; i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    if(prc.value == parentStr){
      option.selected = true;
    }
    selects.appendChild(option);
  }
  return userId;
}
function checkForm(){
  if(!$("sortNo").value.trim()){
   	alert("主题词(类别)排序号不能为空！");
    $("sortNo").focus();
    return false;
  }
  if(!$("word").value.trim()){
    alert("主题词(类别)不能为空！");
    $("word").focus();
    return false;
  }
  if($("parentId").value=="0" && $("typeFlag2").checked){
    alert("不能在主题词(类别)列表下创建主题词！");
    return false;
  }
  for(var i=0;i<$("sortNo").value.length;i++) {
    var ch=$("sortNo").value.substring(i,i+1);
    if ((ch>="0")&&(ch<="9")) {
    }else{
      alert("主题词(类别)排序号包含非数字字符");
      $("sortNo").select();
      $("sortNo").focus();
      return false;
    }
  } 
  return true;
}

function checkWord(){
  var rtJson = getJsonRs(contextPath + "/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/getAjaxCheck.act", mergeQueryString($("form1")));
  if(rtJson.rtState=="0"){
    var data=rtJson.rtData;
    if(data>0){
      alert("此目录下已存在相同的主题词(类别),请选择其他名称！");
      $("word").focus();
      $("word").select();
      return false;
    }
  }
  return true;
}
  </script>
  </head>
<body class="" topmargin="5" onload="doInit()">
 <div id="formDiv">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" width="20" HEIGHT="22"><span class="big3">&nbsp;编辑主题词(类别)</span>
    </td>
  </tr>
 </table>
 <br/>
 <form action=""  method="post" name="form1" id="form1">
 <table class="TableBlock" width=500" align="center">
   <tr>
    <td nowrap class="TableData">主题词(类别)排序号： <font style='color:red'>*</font></td>
    <td nowrap class="TableData">
      <input type="text" name="sortNo" id="sortNo" class="BigInput" size="10" maxlength="200">&nbsp;【用于处于同一层次类别的排序，以及关键词列表的排序】
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">主题词(类别)名称： <font style='color:red'>*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="word" id="word" class="BigInput" size="25" maxlength="25" value="">&nbsp;
    </td>
   </tr>
   <tr>
     <td nowrap class="TableData">类型：<font color="red">*</font></td>
      <td nowrap class="TableData" >
               <input type = "radio" value = "0"  id="typeFlag1" name = "typeFlag" checked/><label for="typeFlag1">主题词类别</label>
        <input type = "radio" value = "1"  id="typeFlag2" name = "typeFlag" /><label for="typeFlag2">主题词</label>
      </td>
   </tr>
   <tr>
    <td nowrap class="TableData">所属类别：</td>
    <td nowrap class="TableData">
    <select id="parentId" name="parentId" style="height:22px;FONT-SIZE: 12pt;">
        </select>
    </td>
   </tr>
   <tr>
     <td nowrap  class="TableControl" colspan="2" align="center">
       <input type="button" value="保存修改" class="BigButton" title="保存修改" name="button" onclick="commitDept();">
       <input type="button" value="删除当前主题词(类别)" class="BigButtonD" title="删除当前主题词(类别)" onclick="deleteDept();">
     </td>
    </tr>
   </table>
  </form>
 <div align="center" style="display:none" id="show">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">主题词(类别)信息已保存修改！</div>
    </td>
  </tr>
 </table>
 </div>
 <div align="center" style="display:none" id="delete">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">主题词(类别)信息已经删除！</div>
    </td>
  </tr>
 </table>
 </div>
 <div id="nextNew" style="display:none">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp; 当前主题词类別 － 相关操作</span>
    </td>
  </tr>
 </table>
 <br>
 <div align="center">
   <input type="button" value="新建此类别下属主题词" class="BigButtonD" title="新建下属主题词(类别)" onclick="nextWord();"><br><br>
 </div>
 </div>
 </div>
</body>
</html>