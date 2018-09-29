<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String treeId = request.getParameter("treeId");
  if (treeId == null) {
    treeId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主题词管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var treeId = "<%=treeId%>";
function doInit() {
  document.getElementById("parentId").value=treeId;	
  deptFunc();
}

function conShow(){
  document.getElementById("show").style.display="none"; 
}
function doSubmit(event) {
  if(checkForm()&& checkWord()){
    event = event || window.event;
    var rtJson = getJsonRs(contextPath + "/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/insertWord.act?treeId="+treeId, mergeQueryString($("form1")));
    if (rtJson.rtState == 0) {
      var prcsJson = rtJson.rtData;
	  var curTree = parent.wordListTree.tree;
	  var id= document.getElementById("parentId").value;
      if(id==0){
	    parentId1='root';
	  }else{
	    parentId1= id;
	  }
	  var nodeId=prcsJson[0].nodeId;
      var name=prcsJson[0].name;
	  var typeFlag = prcsJson[0].typeFlag;
	  var imgAddress=null;
      if(typeFlag==0){
	    imgAddress = contextPath + "/core/styles/style1/img/dtree/folder.gif";
	  }else {
	    imgAddress = contextPath + "/core/styles/style1/img/dtree/file.jpg";
	  }
	  var node = {
	    		  parentId:parentId1,
	    		  nodeId:nodeId,
	    		  name:name,
	    	  	  isHaveChild:0,
	    		  extData:'',
	    		  imgAddress:imgAddress
	    	      }
      curTree.addNode(node); 
	  document.getElementById("show").style.display="";
	  setTimeout("conShow()",2000);
	  $("form1").reset();
	  document.getElementById("parentId").length=0;
	  deptFunc();
    }else{
      $("show").value="主题词(类别)信息新建失败！"
      alert(rtJson.rtMsrg);
    }
  }
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/subsys/inforesouce/docmgr/act/YHWordTreeAct/selectWordToAttendance.act";
  var rtJson = getJsonRs(url);
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
    if(prc.value == treeId){
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
  if(!$("word").value.trim()){
    alert("主题词(类别)不能为空！");
    $("word").focus();
    return false;
  }
  if($("parentId").value=="0" && $("typeFlag2").checked){
    alert("不能在主题词(类别)列表下直接创建主题词！");
    return false;
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
 <table border="0" width="70%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" height="22" WIDTH="22"><span class="big3">&nbsp;新建主题词(类别)</span>
    </td>
  </tr>
 </table>
 <form action="" method="post" name="form1" id="form1" onSubmit='return check()'>
  <table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">主题词(类别)排序号： <font style='color:red'>*</font></td>
    <td nowrap class="TableData">
      <input type="text" name="sortNo" id="sortNo" class="BigInput" size="10" maxlength="200">&nbsp;【用于处于同一层次类别的排序，以及关键词列表的排序】
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">主题词(类别)名称：<font style='color:red'>*</font></td>
    <td nowrap class="TableData">
      <input type="text" name="word" id="word" class="BigInput" size="25" maxlength="25">&nbsp;      
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
      <td class="TableData">
      <select id="parentId" name="parentId" style="height:22px;FONT-SIZE: 12pt;">
      </select>
      </td>
   </tr>
   <tr>
    <td nowrap class="TableControl" colspan="2" align="center">
       <input type="button" value="新 建" class="BigButton" title="新建" name="button" onclick="doSubmit();">
    </td> 
  </table>
  <div align="center" style="display:none" id="show">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">主题词(类别)信息新建成功！</div>
    </td>
  </tr>
 </table>
 </div>
 </form>
</body>
</html>