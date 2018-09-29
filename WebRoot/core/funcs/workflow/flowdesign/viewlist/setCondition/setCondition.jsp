<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String flowId = request.getParameter("flowId");
String seqId = request.getParameter("seqId");
String isList = request.getParameter("isList"); 
if(isList == null  || "".equals(isList)){
  isList = "";
}else {
  isList = "1";
}
String openflag = request.getParameter("openflag");
if(openflag == null ){
  openflag = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置条件</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var flowId = "<%=flowId%>";
var isList = "<%=isList%>";
var openflag = "<%=openflag %>";
var itemS = [];
var extend = "[主办人会签意见],[从办人会签意见],[公共附件名称],[公共附件个数],[当前步骤号],[当前流程设计步骤号],[当前主办人姓名],[当前主办人角色号],[当前主办人角色],[当前主办人辅助角色],[当前主办人部门],[当前主办人上级部门]";
var actionPath = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct";

function addCondition(flag)
{
  var vITEM_NAME = $('formItemSelect').value;
  var vCONDITION = $('conditionSelect').value;
  var vITEM_VALUE = $('itemValue').value;
  if ($('itemValue').type == "checkbox") {
    if ($('itemValue').checked) {
      vITEM_VALUE = "on";
    } else {
      vITEM_VALUE = "";
    }
  }
  
  if(vITEM_VALUE.indexOf("'")>=0)
  {
    alert("值中不能含有'号");
    return;
  }

  if(($('conditionSelect').value == "=" 
    || $('conditionSelect').value == "<>" ) 
    && $('checkType').checked==true)
  {
     if(vCONDITION == "=")
       vCONDITION = "==";
     else
       vCONDITION = "!==";

     vITEM_VALUE = $('itemType').value;
  }
  var str="'"+ vITEM_NAME + "'" + vCONDITION + "'" + vITEM_VALUE +"'";
  createTr(flag , str);
}
function createTr(flag ,str ){
  var tab;
  var img;
  var trClass;
  if(flag == 1){
  	tab = $('prcsInTab');
  	img = '<image style="cursor:pointer" src="<%=imgPath%>/edit.gif" align="absmiddle" onclick="edit(this,1)"> <image style="cursor:pointer" src="<%=imgPath%>/delete.gif" align="absmiddle" onclick="delRule(this,1)">';
  } else {
  	tab = $('prcsOutTab');
    img = '<image style="cursor:pointer" src="<%=imgPath%>/edit.gif" align="absmiddle" onclick="edit(this,0)"> <image style="cursor:pointer" src="<%=imgPath%>/delete.gif" align="absmiddle" onclick="delRule(this,0)">';
  }
  var trs = tab.getElementsByTagName('tr');
  for(var i=1;i < trs.length;i++){
    var tabValue = trs[i].innerText;
    if(tabValue.indexOf(str) >= 0){
      alert("条件重复！");
      return;
    }
  }

  if(tab.rows.length%2 == 1 ) {
    trClass="TableLine1";
  }else{
    trClass="TableLine2";
  } 
  var newRow = tab.insertRow(-1);
  newRow.setAttribute("className" , trClass);
  var first = newRow.insertCell(-1);
  first.setAttribute("align" , "center");
  $(first).update("[" + (tab.rows.length-1) + "]");
  var second = newRow.insertCell(-1);
  $(second).update( str );
  var third = newRow.insertCell(-1);
  third.setAttribute("align","center");
  $(third).update(img);
}
function myTip(){
   if($('tip').style.display == "none"){
     $('tip').style.display = "";
   }else{
     $('tip').style.display = "none";
   }
}
function createSelected(select , items){
  itemS = items;
  for(var i = 0 ;i < items.length ;i++){
    var item = items[i];
    if (item) {
      var option  = new Element('option').update(item.title);
      option.value = item.title;
      $(select).appendChild(option);
    }
  } 
  var exs = extend.split(",");
  for(var i = 0 ;i < exs.length ;i++){
    var item = exs[i];
    if (item) {
      var option  = new Element('option').update(item);
      option.value = item;
      $(select).appendChild(option);
    }
  }
  setVal(itemS[0].title);
}
function setVal(sel){
  var it = null;
  for(var i = 0 ;i < itemS.length ;i++){
    var item = items[i];
    if (item.title == sel) {
      it = item; 
      break;
    }
  } 
  var val  = "";
  if (it) {
    val  = it.value;
  }
  if (it != null && it.type == "select") {
    $('divValue').update("值：<select id=\"itemValue\">" + it.content + "</select>");
  } else if(it != null && it.type == "radio") {
    $('divValue').update("值：<select id=\"itemValue\">" + it.content + "</select>");
  } else if(it != null && it.type == "checkbox") {
    var checked = "";
    if (val == 'on') {
      checked = "checked";
    }
    $('divValue').update("值：<input id=\"itemValue\" type='checkbox' value='on' "+checked+" />");
  } else {
    $('divValue').update("值：<input type=\"text\" value=\""+ val +"\" class=\"SmallInput\" id=\"itemValue\" size=20>");
  }
  
}
function changeType(checkbox){
  if(checkbox.checked){
    $('divValue').hide();
    $('divType').style.display = 'inline';
  }else{
    $('divType').hide();
    $('divValue').style.display = 'inline';
  }
}

function delRule(obj, flag){
  var tab;
  if(flag == 1){
    tab = $('prcsInTab');
  }else{
    tab = $('prcsOutTab');
  }
  var tr=obj.parentNode.parentNode;
  var no=tr.rowIndex;
  tab.deleteRow(tr.rowIndex);
  for(var i=no;i<tab.rows.length;i++){
  	tab.rows[i].cells[0].innerText="[" + (tab.rows[i].rowIndex) + "]";
  }
}

function edit(obj, flag){
  var td = obj.parentNode.parentNode.cells[1];
  var obj_edit = $("edit");
  var content = td.innerText;
  obj_edit.value = content;
  td.innerHTML = "";
  td.appendChild(obj_edit);
  obj_edit.style.display="";
  obj_edit.select()
  obj_edit.focus();
}

function updateEdit(edit)
{
  edit.style.display ="none";
  var td = edit.parentNode;
  document.body.appendChild(edit);
  td.innerText = edit.value;
}               
function doInit(){
  var url = actionPath + "/getConditionMsg.act";
  var json = getJsonRs(url , "flowId=" + flowId + "&seqId=" + seqId);
  if(json.rtState == "0"){
    items = json.rtData.items;
    var prcsIdTip = "步骤" + json.rtData.prcsId + "－条件设置";
    $('prcsIdTip').update(prcsIdTip);
    createSelected('formItemSelect',items);
    var prcsInSet = json.rtData.prcsIn.split(',');
    var prcsOutSet = json.rtData.prcsOut.split(',');
    for(var i = 0 ;i < prcsInSet.length;i++){
      var tmp = prcsInSet[i];
      if(tmp){
        createTr(1 , tmp);
      }
    }
    for(var i = 0 ;i < prcsOutSet.length;i++){
      var tmp = prcsOutSet[i];
      if(tmp){
        createTr(0 , tmp);
      }
    }
    if(json.rtData.prcsInSet){
      $('prcsInSet').value = json.rtData.prcsInSet;
    }
    if(json.rtData.prcsOutSet){
      $('prcsOutSet').value = json.rtData.prcsOutSet;
    }
    if(json.rtData.prcsInDesc){
      $('prcsInDesc').value = json.rtData.prcsInDesc;
    }
    if(json.rtData.prcsOutDesc){
      $('prcsOutDesc').value = json.rtData.prcsOutDesc;
    }
  }
}
function commit(flag){
  if(!checkExp($("prcsInSet").value) || !checkExp($("prcsOutSet").value)){
    return;
  }
  var tab_in=$("prcsInTab");
  var tab_out=$("prcsOutTab");
  
  $("prcsIn").value = "";
  $("prcsOut").value = "";
  for(var i=1;i < tab_in.rows.length;i ++) {
    $("prcsIn").value += tab_in.rows[i].cells[1].innerText + ",";
  }
  for(var j=1;j < tab_out.rows.length;j ++) {
    $("prcsOut").value += tab_out.rows[j].cells[1].innerText + ",";
  }
  var url = actionPath + "/updateCondition.act";
  var json = getJsonRs(url , $('conditionForm').serialize());
  if(json.rtState == '0'){
    if (!openflag) {
      if (!flag) {
        if(!isList){
          try {
            opener.location.reload();
          } catch (e) {

          }
          window.close();
        }else{
          history.back();
        }
      }
    }else {
      alert("保存成功！");
    }
  }else{
    alert(json.rtMsrg);
    
  }
}

function checkExp(s)
{
  //检查公式  if(s.indexOf("(")>=0)
  {
  	var num1=s.split("(").length - 1;
  	var num2=s.split(")").length - 1;
  	if(num1!=num2)
  	{
  	  alert("条件表达式书写错误,请检查括号匹配！");
  	  return false;
  	}
  }
  return true;
}
function changeCondition(value){
  if (value == "=" || value == "<>") {
	$('divCheck').style.display = "inline";
	if ($('checkType').checked) {
	  $('divType').style.display =  "inline";
	  $('divValue').style.display = "none";
	} else {
	  $('divType').style.display = "none";
	  $('divValue').style.display =  "inline";
	}
  } else {
    $('divCheck').hide();
    if ($('divType').style.display == "inline"){
      $('divType').hide();
      $('divValue').style.display = "inline";
    }
  }
}
function turn(type) {
  commit(1);
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct/turn.act";
  var json = getJsonRs(url , "flowId=" + flowId + "&seqId=" + seqId + "&type=" + type);
  if (json.rtState == '0') {
    if (!json.rtData) {
      if (type) {
        alert("无上一步骤！");
      } else {
        alert("无下一步骤！");
      }
      return;
    }
    var url = contextPath + "/core/funcs/workflow/flowdesign/viewlist/setCondition/setCondition.jsp?flowId="+flowId + "&isList=" + isList + "&openflag=" + openflag;
    if (json.rtData) {
      url += "&seqId=" + json.rtData;
    }
    this.location.href = url;
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3" id="prcsIdTip"> 条件设置</span>
    	&nbsp;<a class="dropdown" href="javascript:myTip();"><span>使用说明</span></a>
      保存并转到：<input onclick='turn(1)'  class='SmallButtonW' value="上一步" type="button">&nbsp;<input onclick='turn()'  class='SmallButtonW' value="下一步" type="button">
    </td>
  </tr>
</table>

<form method="post" id="conditionForm" name="conditionForm">
<table border="0" width="90%" align="center" class="TableList">
    <tr id="tip" style="display:none">
      <td class="TableData"><b>条件设置使用说明：</b><br>
        条件列表处用于存储全部条件，每一行为一个条件。<br>
        如果不设置条件公式，所有条件之间均为“与”的关系。<br>
        如果设置条件公式，条件公式中需要引用条件列表中的条件，引用方法为使用条件编号。<br>
        <b>例如：</b><br>
        “满足条件1或者条件2”的条件公式为：<b>[1] or [2]</b><br>
        “满足条件1或者条件2，且满足条件3”的条件公式为：<b>([1] or [2]) and [3]</b><br>
        “满足条件1，且不满足条件2”的条件公式为：<b>[1] and ![2]</b><br>
      </td>
    </tr>
    <tr>
      <td height=30  class="TableHeader"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b> 条件生成器</b></td>
    </tr>
    <tr >
      <td class="TableData"><b>
        字段 <select id="formItemSelect" width="20px" onchange="setVal(this.value)">
       </select>
        条件 <select id="conditionSelect" onchange="changeCondition(this.value);" >
       <option value="=">等于</option><option value="<>">不等于</option><option value=">">大于</option><option value="<">小于</option><option value=">=">大于等于</option><option value="<=">小于等于</option><option value="include">包含</option><option value="exclude">不包含</option>
       </select>
        <span id="divCheck" style="display:inline"><input type="checkbox" name="checkType" id="checkType" onclick="changeType(this);"><label for="checkType">类型判断</label>&nbsp;</span>
        <span id="divType" style="display:none">
        &nbsp;类型 <select id="itemType" class="SmallSelect">
             <option value="数值">数值</option>
             <option value="日期">日期</option>
             <option value="日期+时间">日期+时间</option>
        	  </select>
        </span>
        <span id="divValue" style="display:inline">
               值 <input type="text" class="SmallInput" id="itemValue" size=20></span>
        <div align="center" style="margin:5px 0 5px 0">
        <input type="button" class="BigButtonC" value="添加到转入条件列表" onclick="addCondition(1)">&nbsp;&nbsp;
      <input type="button" class="BigButtonC" value="添加到转出条件列表" onclick="addCondition(0)"></div>
        </b>
      </td>
    </tr>
    <tr  class="TableHeader">
      <td height=30><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b> 转入条件列表</b></td>
    </tr>
    <tr>
      <td style="background-color:#ffffff">
      	<b>合理设定转入条件，可形成流程的条件分支，但数据满足转入条件，才可转入本步骤</b>
      	<table id="prcsInTab" width=100% align="center"  border=0>
  	          <tr class="TableHeader">
  	            <td nowrap align="center" width=50>编号</td>
  	            <td nowrap align="center">条件描述</td>
  	            <td nowrap align="center" width=100>操作</td>
  	          </tr>
        </table>
        <b>转入条件公式(条件与逻辑运算符之间需空格，如[1] AND [2])</b><br>
        <input type="text" class="BigInput" size=71 name="prcsInSet" id="prcsInSet" value=""><br>
        <b>不符合条件公式时，给用户的文字描述：<b>
        <input type="text" class="BigInput" size=71 name="prcsInDesc" id="prcsInDesc" value="">
      </td>
    </tr>
    <tr  class="TableHeader">
      <td height=30><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b> 转出条件列表</b></td>
    </tr>
    <tr>
      <td style="background-color:#ffffff">
      	<b>合理设定转出条件，可对表单数据进行校验</b>
      	<table id="prcsOutTab" width=100% align="center" border=0>
  	          <tr class="TableHeader">
  	            <td nowrap align="center" width=50>编号</td>
  	            <td nowrap align="center">条件描述</td>
  	            <td nowrap align="center" width=100>操作</td>
  	          </tr>
  
        </table>
        <b>转出条件公式(条件与逻辑运算符之间需空格，如[1] AND [2])</b><br>
        <input type="text" class="BigInput" size=71 name="prcsOutSet" id="prcsOutSet" value=""><br>
        <b>不符合条件公式时，给用户的文字描述：<b>
        <input type="text" class="BigInput" size=71 name="prcsOutDesc" id="prcsOutDesc" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      	<input type="hidden" name="prcsIn" id="prcsIn" value="">
      	<input type="hidden" name="prcsOut" id="prcsOut" value="">
        <input type='hidden' value="<%=flowId %>" name="flowId">
        <input type="hidden" value="<%=seqId %>" name="seqId">

        <input type="button"  value="保 存" class="BigButton" onclick="commit()">      
                <% if(isList == null || "".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="history.back();">
  <% } %>
      </td>
    </tr>
</table>
 </form>

<input type="text" id="edit" onblur="updateEdit(this);"  class="SmallInput" size="50" style="display:none;" />
</body>
</html>
