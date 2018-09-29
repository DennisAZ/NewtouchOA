<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'newList.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
</head>
<body>
<link rel="stylesheet" href="<%=cssPath%>/views.css"
	type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css"
	type="text/css" />
<link rel="stylesheet" type="text/css" href="/yh/rad/dsdef/css/tableList.css" />
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/raw/cy/grid/grid.css" />
<script type="text/javascript" src="<%=contextPath %>/rad/dsdef/js/table.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script language="JavaScript">
function doSbumit(event) {
  var id = document.getElementById("count");
  var reg = /^[0-9]*$/;
  var tableNo = document.dataList.tableNo;
  var tableName = document.getElementById("tableName");
  var className = document.getElementById("className");
  var tableDesc = document.getElementById("tableDesc");
  var fieldNo = document.getElementById("fieldNo");
  if(isExistsTableNo(tableNo.value)){
    alert("表编码已被使用，请选择其他表编码");
  	tableNo.select();
  	return false;
  }
  if(!reg.test(tableNo.value)){
	alert("表编码请输入数字");
	tableNo.focus();
	return false;
  }
  if(!tableNo.value.length || tableNo.value.length != 5){
	alert("表编码输入长度为5位");
	tableNo.focus();
	return false;
  }
  if(!tableName.value){
	alert("表名称不能为空");
	tableName.focus();
    return false;
  }
  if(!className.value){
	alert("类名称不能为空");
	className.focus();
	return false;
  }
  if(!tableDesc.value){
	alert("表描述不能为空");
	tableDesc.focus();
	return false;
  }
  for(var i = 0; i < parseInt(id.value); i++){
    var fieldNo = document.getElementById("fieldNo_" + i);
	var fieldName = document.getElementById("fieldName_" + i);
	var fieldDesc = document.getElementById("fieldDesc_" + i);
	if (!reg.test(fieldNo.value)) {
	  alert("字段编码只能输入数字!");
	  fieldNo.focus();
	  return false;
	}
	if((fieldNo.value.length == 0) || (fieldNo.value.length != 8)){
	  alert("字段编码输入长度为8位");
	  fieldNo.focus();
	  return false;
	}
	if((fieldName.value.length == 0)){
	  alert("字段名称不能为空");
	  fieldName.focus();
	  return false;
	}
	if((fieldDesc.value.length == 0)){
	  alert("字段描述不能为空");
	  fieldDesc.focus();
	  return false;
	}
 }
  event = event || window.event;
  var rtJson = getJsonRs(contextPath + "/yh/rad/dsdef/act/YHDsDefAct/insertDsDef.act", mergeQueryString($("dataList")));
  $("dataList").reset();
  //alert(rsText);
  if(rtJson.rtState == 0){
    alert(rtJson.rtMsrg);
  }else{
    alert(rtJson.rtMsrg);
  }
}
function checkTabIsExist(){
  var tableName = $("tableName").value;
  if(!tableName){
    return false;
  }
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/tabIsExist.act?tableName="+tableName;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    if(rtJson.rtData == "1"){
      return true;
    }else{
      return false;
    }
  }else{
    return true;
  }
}
function dropTab(){
  var tableName = $("tableName").value;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/dropTab.act?tableName="+tableName;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    alert(rtJson.rtMsrg);
  }else{
    alert(rtJson.rtMsrg);
  }
}
function getPhyics(){
  if(!$("tableName").value){
    alert("tableName不能为空!");
    $("tableName").select();
    return;
  }
  if(isExistForTab($("tableName").value)){
     alert("此表的数据字典已经存在!");
     return;
  }
  if(!checkTabIsExist()){
    alert("此表的物理结构不存在!");
    $("tableName").select();
    return;
  }
  if(!confirm("确认要抽取此表的结构?")){
    return;
  }
  var tableName = $("tableName").value;
  var tableNo = $("tableNo").value;
  
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/getPhysicsDbInfo.act?tableName="+tableName + "&tableNo="+tableNo;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    bindField(rtJson.rtData);
  }else{
    alert(rtJson.rtMsrg);
 }
}
function bindField(json){
  clear();
  for(var i = 0 ; i < json.length; i++){
    add();
    var olddsField = $("fieldNo_" + i).value;
    json[i].propName = toPropName(json[i].fieldName);
    bindOneField(json[i],i);
    $("fieldNo_" + i).value = olddsField;
  }
}

function clear(){
  var length = $('tbody').rows.length;
  for(var i = 0 ; i < length ; i ++){
    $('tbody').deleteRow(0);
  }
 $("count").value = "0";
}
function bindOneField(json,index){
  for (var property in json) {
    var value = json[property];  
    var cntrlArray = document.getElementsByName(property + "_" + index);    
    var cntrlCnt = cntrlArray.length;
    if (!cntrlArray || cntrlCnt < 1) {
      continue;
    }
    if (cntrlCnt == 1) {
      var cntrl = cntrlArray[0];
      if (cntrl.tagName.toLowerCase() == "input" && cntrl.type.toLowerCase() == "checkbox") {
        if (cntrl.value == value) {
          cntrl.checked = true;
        }else {
          cntrl.checked = false;
        }
      }else if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {

        cntrl.innerHTML = value;
      }else {
        cntrl.value = value;
      }
    }else {
      for (var i = 0; i < cntrlCnt; i++) {
        var cntrl = cntrlArray[i];
        if (cntrl.value == value) {
          cntrl.checked = true;
        }else {
          cntrl.checked = false;
        }
      }
    }
  }
}

function isExistForTab(tableName){
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/isExistForTab.act?tableName="+tableName ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    if(rtJson.rtData.isExist == "1"){
      return true;
    }else{
      return false;
     }
  }else{
    return true;
 }
}

function isExistsTableNo(tableNo){
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDefAct/existsTableNo.act?tableNo="+tableNo ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    if(rtJson.rtData.isExistsTableNo == "1"){
      return true;
    }else{
      return false;
     }
    
  }else{
    return true;
 }
}
</script>
<form
	action="<%=contextPath %>/yh/rad/dsdef/act/YHDsDefSubmitAct/testMethod.act"
	method="post" id="dataList" name="dataList" onSubmit='return check()'>
	<input type=hidden name="id" id="count" value=0> 
	<input type=hidden name="YHDsTable" value="yh.core.data.YHDsTable"> 
	<input type=hidden name="YHDsField" value="yh.core.data.YHDsField">
	<div class=tableDiv >
	<div class="tableClass">
 <table id="table">
	<tr>
		<td>表编码</td>
		<td><input type=text id=tableNo name=tableNo size=13 maxlength = "5" class = "SmallInput" onkeyup ="onChange(this.value)"><font style='color:red'>*</font></td>
		<td>表名称</td>
		<td><input type=text id=tableName name=tableName size=13 class = "SmallInput" onchange = "onChTableName(this.value)"><font style='color:red'>*</font></td>
		<td>类名称</td>
		<td><input type=text id=className name=className size=13 class = "SmallInput" ><font style='color:red'>*</font></td>
		<td>表描述</td>
		<td><input type=text id=tableDesc name=tableDesc size=13 class = "SmallInput"><font style='color:red'>*</font></td>
		<td>表类型</td>
		<td><select id="categoryNo" name="categoryNo">
			<option value="1">代码表</option>
			<option value="2">小编码表</option>
			<option value="3">参数表</option>
			<option value="4">数据主表</option>
			<option value="5">数据从表</option>
			<option value="6">多对多关系表</option>
		</select></td>
		<td> <input type=button name=submit class="SmallButtonW " onclick="doSbumit();" value="提交"></td>
        <td> <input type=button class="SmallButtonC " onclick="getPhyics()" value="抽取物理结构"></td>
	</tr>
 </table>
 </div>
 </div>
 <input type=button class="SmallButtonW" value="添加字段 " onclick="add()">
 <div class="" style="width:250px;">
 <table  id="tableF">
	<thead>
		<tr>
			<td>字段编码</td>
			<td>字段名称</td>
			<td>字段描述</td>
			<td>按钮1</td>
			<td>按钮2</td>
		</tr>
	</thead>
	<tbody id="tbody">
	</tbody>
 </table>
 </div>
<br>
<div id="table_div" style="display: none; position: absolute;"name="tableN_div" class="TableLine1 ">
	<input type="hidden" id="divId" value=""/>
  <table id="tableDiv" class="TableLine1  ">
	<thead>
		<tr>
			<td>字段编码</td>
			<td><input type=text id="fieldNo" name="fieldNo" value="" maxlength = "8" class = "SmallInput"></td>
			<td>字段名称</td>
			<td><input type=text id="fieldName" name="fieldName" value="" class = "SmallInput" onchange ="onFieldTableName(this.value)"></td>
		</tr>
		<tr>
			<td>字段描述</td>
			<td><input type=text id="fieldDesc" name="fieldDesc" value="" class = "SmallInput">
			    <input type=hidden id="seqId" name="seqId" size=13 value=0></td>
			<td>属性名称</td>
			<td><input type=text id="propName" name="propName" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>参照表</td>
			<td><input type=text id="fkTableNo" name="fkTableNo" value="" class = "SmallInput"></td>
			<td>参照表2</td>
			<td><input type=text id="fkTableNo2" name="fkTableNo2" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>编码字段</td>
			<td><input type=text id="fkRelaFieldNo" name="fkRelaFieldNo" value="" class = "SmallInput"></td>
			<td>名称字段</td>
			<td><input type=text id="fkNameFieldNo" name="fkNameFieldNo" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>筛选条件</td>
			<td><input type=text id="fkFilter" name="fkFilter" value="" class = "SmallInput"></td>
			<td>编码类别</td>
			<td><input type=text id="codeClass" name="codeClass" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>缺省值</td>
			<td><input type=text id="defaultValue" name="defaultValue" value="" class = "SmallInput"></td>
			<td>显示方式</td>
			<td><select id="formatMode">
				<option value="number">数字</option>
				<option value="text">文本</option>
				<option value="data">日期</option>
				<option value="amt">金额</option>
			</select></td>
		</tr>
		<tr>
			<td>格式规则</td>
			<td><input type=text id="formatRule" name="formatRule" value="" class = "SmallInput"></td>
			<td>错误消息</td>
			<td><input type=text id="errorMsrg" name="errorMsrg" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>数值长度</td>
			<td><input type=text id="fieldPrecision" name="fieldPrecision" value="" class = "SmallInput"></td>
			<td>小数位数</td>
			<td><input type=text id="fieldScale" name="fieldScale" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>数据类型</td>
			<td><select id="dataType" name="dataType">
				<option value="-7">BIT</option>
				<option value="-6">TINYINT</option>
				<option value="5">SMALLINT</option>
				<option value="4">INTEGER</option>
				<option value="-5">BIGINT</option>
				<option value="6">FLOAT</option>
				<option value="7">REAL</option>
				<option value="8">DOUBLE</option>
				<option value="2">NUMERIC</option>
				<option value="3">DECIMAL</option>
				<option value="1">CHAR</option>
				<option value="12">VARCHAR</option>
				<option value="-1">LONGVARCHAR</option>
				<option value="91">DATE</option>
				<option value="92">TIME</option>
				<option value="93">TIMESTAMP</option>
				<option value="-2">BINARY</option>
				<option value="-3">VARBINARY</option>
				<option value="-4">LONGVARBINARY</option>
				<option value="0">NULL</option>
				<option value="1111">OTHER</option>
				<option value="2000">JAVA_OBJECT</option>
				<option value="2001">DISTINCT</option>
				<option value="2002">STRUCT</option>
				<option value="2003">ARRAY</option>
				<option value="2004">BLOB</option>
				<option value="2005">CLOB</option>
				<option value="2006">REF</option>
				<option value="70">DATALINK</option>
				<option value="16">BOOLEAN</option>
			</select>
			<td>主键</td>
			<td><select id="isPrimaryKey" name="isPrimaryKey">
				<option value="1">是</option>
				<option value="0">否</option>
			</select></td>
		</tr>
		<tr>
			<td>自增</td>
			<td><select id="isIdentity" name="isIdentity">
				<option value="1">是</option>
				<option value="0">否</option>
			</select></td>
			<td>显示长度</td>
			<td><input type="text" id="displayLen" name="displayLen" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>必填</td>
			<td><select id="isMustFill" name="isMustFill">
				<option value="1">是</option>
				<option value="0">否</option>
			</select></td>
			<td><input type="button" name="saveDiv" value="保存" onclick="save()" class="SmallButton "></td>
			<td><input type="button" name="close" value="关闭" onclick="closeDiv()" class="SmallButton "></td>
		</tr>
	</thead>
	<tbody id="tbodyDiv">
   </tbody>
 </table>
</div>
</form>
</body>
</html>
