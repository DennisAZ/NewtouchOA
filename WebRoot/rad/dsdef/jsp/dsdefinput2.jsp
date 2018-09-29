<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/core/inc/t6.jsp"%>
<title>列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=jsPath%>/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jquery-ui-patch.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=jsPath%>/ui/jqGrid/jquery.jqGrid.src.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqGrid/ui.jqgrid.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/zTree/zTreeStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/ui/jqueryUI/base/jquery.ui.all.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=jsPath%>/ui/zTree/jquery.ztree.all-3.1.js"></script>
<script type="text/javascript" src="<%=contextPath %>/rad/dsdef/js/table2.js"></script>
<script type="text/javascript">
function doSbumit2() {
  var reg = /^[0-9]*$/;
  var tableNo = document.dataList.tableNo;
  var tableName = document.getElementById("tableName");
  var className = document.getElementById("className");
  var tableDesc = document.getElementById("tableDesc");
  var fieldNo = document.getElementById("fieldNo");
  if(isExistsTableNo2(tableNo.value)){
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
  
  document.getElementById("count").value = $("#divieeeeee2").jqGrid('getDataIDs').length;
  var ids = $("#divieeeeee2").jqGrid('getDataIDs');
  for(var i = 0; i < ids.length; i++){
    var fieldNo = document.getElementById(ids[i]+"_fieldNo");
		var fieldName = document.getElementById(ids[i]+"_fieldName");
		var fieldDesc = document.getElementById(ids[i]+"_fieldDesc");
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
  
  var subStr = "";
  var ids = $("#divieeeeee2").jqGrid('getDataIDs');
  for(var i = 0; i < ids.length; i++){
    var rowId = ids[i];
    var record = $("#divieeeeee2").jqGrid('getRowData', rowId);
    for(var p in record){
      if(p == 'fieldNo' || p == 'fieldName' || p == 'fieldDesc'){
        subStr = subStr + "&" + p + "_" + rowId + "=" + encodeURIComponent($('#'+rowId+"_"+p).val());
      }
      else{
      	subStr = subStr + "&" + p + "_" + rowId + "=" + encodeURIComponent(record[p]);
      }
    }
  }
  
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDefAct/insertDsDef.act";
  jQuery.ajax({
    type: "POST",
    url: url,
    data: $("#dataList").serialize()+subStr,
    dataType: "json",
    success: function(json) {
      if (json.rtState == "0") {
        alert(json.rtMsrg);
				window.location.reload();        
      }
      else{
        alert(json.rtMsrg);
      }
    },
    error: function(json) {
      alert(json.rtMsrg);
    }
  });
  //$("dataList").reset();
}
function checkTabIsExist2(){
  var returnValue;
  var tableName = $("#tableName").val();
  if(!tableName){
    return false;
  }
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/tabIsExist.act?tableName="+tableName;
  jQuery.ajax({
    url: url,
    async: false,
    dataType: "json",
    success: function(json) {
      if (json.rtState == "0") {
        if(json.rtData == "1"){
          returnValue = true;
        }else{
          returnValue = false;
        }
      }
      else{
        returnValue = false;
      }
    },
    error: function(json) {
      returnValue = false;
    }
  });
  return returnValue;
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
function getPhyics2(){
  if(!$("#tableName").val()){
    alert("tableName不能为空!");
    $("#tableName").select();
    return;
  }
  if(isExistForTab2($("#tableName").val())){
     alert("此表的数据字典已经存在!");
     return;
  }
  if(!checkTabIsExist2()){
    alert("此表的物理结构不存在!");
    $("#tableName").select();
    return;
  }
  if(!confirm("确认要抽取此表的结构?")){
    return;
  }
  var tableName = $("#tableName").val();
  var tableNo = $("#tableNo").val();
  
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/getPhysicsDbInfo2.act?tableName="+tableName + "&tableNo="+tableNo;
  $("#divieeeeee2").jqGrid({
    url: url,
    datatype: "json",
    height: "230px",
    colNames:['seqId', 'tableNo', 'fieldNo', 'fieldName', 'propName', 'fieldDesc', 'fkTableNo', 'fkTableNo2', 'fkRelaFieldNo'
              , 'fkNameFieldNo', 'fkFilter', 'codeClass', 'defaultValue', 'formatMode', 'formatRule', 'errorMsrg', 'fieldPrecision'
              , 'fieldScale', 'dataType', 'isIdentity' , 'displayLen', 'isMustFill', 'isPrimaryKey', 'fkNameFieldNo2', '操作'],
    colModel:[
      {name:'seqId'         ,index:'seqId'          ,width:80    ,hidden: true},    
      {name:'tableNo'       ,index:'tableNo'        ,width:100   ,hidden: true},
      {name:'fieldNo'       ,index:'fieldNo'        ,width:100   ,hidden: false   ,editable: true},
      {name:'fieldName'     ,index:'fieldName'      ,width:100   ,hidden: false   ,editable: true},
      {name:'propName'      ,index:'propName'       ,width:100   ,hidden: true},
      {name:'fieldDesc'     ,index:'fieldDesc'      ,width:100   ,hidden: false   ,editable: true},
      {name:'fkTableNo'     ,index:'fkTableNo'      ,width:100   ,hidden: true},
      {name:'fkTableNo2'    ,index:'fkTableNo2'     ,width:100   ,hidden: true},
      {name:'fkRelaFieldNo' ,index:'fkRelaFieldNo'  ,width:100   ,hidden: true},
      {name:'fkNameFieldNo' ,index:'fkNameFieldNo'  ,width:100   ,hidden: true},
      {name:'fkFilter'      ,index:'fkFilter'       ,width:100   ,hidden: true},
      {name:'codeClass'     ,index:'codeClass'      ,width:100   ,hidden: true},
      {name:'defaultValue'  ,index:'defaultValue'   ,width:100   ,hidden: true},
      {name:'formatMode'    ,index:'formatMode'     ,width:100   ,hidden: true},
      {name:'formatRule'    ,index:'formatRule'     ,width:100   ,hidden: true},
      {name:'errorMsrg'     ,index:'errorMsrg'      ,width:100   ,hidden: true},
      {name:'fieldPrecision',index:'fieldPrecision' ,width:100   ,hidden: true},
      {name:'fieldScale'    ,index:'fieldScale'     ,width:100   ,hidden: true},
      {name:'dataType'      ,index:'dataType'       ,width:100   ,hidden: true},
      {name:'isIdentity'    ,index:'isIdentity'     ,width:100   ,hidden: true},
      {name:'displayLen'    ,index:'displayLen'     ,width:100   ,hidden: true},
      {name:'isMustFill'    ,index:'isMustFill'     ,width:100   ,hidden: true},
      {name:'isPrimaryKey'  ,index:'isPrimaryKey'   ,width:100   ,hidden: true},
      {name:'fkNameFieldNo2' ,index:'fkNameFieldNo2',width:100   ,hidden: true},
      {name:'act'           ,index:'act'            ,width:100},
    ],
    rowNum:999,
    height: '100%',
    pager: '',
    //sortname: 'id',
    viewrecords: true,
    sortorder: "desc",
    toolbar: [true, "top"],
    gridComplete: function(){
      var ids = $("#divieeeeee2").jqGrid('getDataIDs');
      for(var i = 0; i < ids.length; i++){
        var rowId = ids[i];
        jQuery('#divieeeeee2').editRow(rowId);
        
        var str = "<center>"
          + "<a href=javascript:show(" + rowId + ");><font color='blue'>详情</font></a> "
          + "<a href=javascript:buttonOnclick2(" + rowId + ");><font color='blue'>删除</font></a> "
          + "</center>";
        $("#divieeeeee2").jqGrid('setRowData',rowId,{act:str});
        var value = $('#' + rowId + '_fieldNo').val();
        if(value){
          continue;
        }
        if((parseInt(rowId) + 1) < 10){
          value = '00'+(parseInt(rowId) + 1);
        }
        else if((parseInt(rowId) + 1) < 100){
          value = '0'+(parseInt(rowId) + 1);
        }
        else if((parseInt(rowId) + 1) < 1000){
          value = (parseInt(rowId) + 1);
        }
        $('#' + rowId + '_fieldNo').val(value);
      } 
    }
  });
  var toolbar = $("<div id='toolbarDiv'></div>").toolbar({
    btns: [{
      text: "添加字段",
      icon:'',
      handler: addField2
    }]
  });
  $("#t_divieeeeee2").append(toolbar);
}

function currencyFmatter (cellvalue, options, rowObject){
  alert(rowObject[3])
  if(rowObject[3] == "" || rowObject[3] == null || rowObject[3] == "undefined"){
    return;
  }
  return toPropName(rowObject[3]);
}

function toPropName(fieldName){
  var strs = fieldName.split("_");
  var result = "";
  for(var i = 0;i < strs.length; i++){
    var strt = strs[i].substr(0,1).toUpperCase()+strs[i].substr(1).toLowerCase();
    result += strt;
 }
  var propName = result.substr(0,1).toLowerCase()+result.substr(1);
  return propName;
}

function isExistForTab2(tableName){
  var returnValue;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/isExistForTab.act?tableName="+tableName ;
  jQuery.ajax({
    url: url,
    async: false,
    dataType: "json",
    success: function(json) {
      if (json.rtState == "0") {
        if(json.rtData.isExist == "1"){
          returnValue = true;
        }else{
          returnValue = false;
        }
      }
      else{
        returnValue = true;
      }
    },
    error: function(json) {
      returnValue = true;
    }
  });
  return returnValue;
}

function isExistsTableNo2(tableNo){
  var returnValue;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDefAct/existsTableNo.act?tableNo="+tableNo+"&seqId=0";
  jQuery.ajax({
    url: url,
    async: false,
    dataType: "json",
    success: function(json) {
      if (json.rtState == "0") {
        if(json.rtData.isExistsTableNo == "1"){
          returnValue = true;
        }else{
          returnValue = false;
         }
      }
      else{
        returnValue = true;
      }
    },
    error: function(json) {
      returnValue = true;
    }
  });
  return returnValue;
}

function onSelectTable() {
  var record = newWindow(contextPath + "/rad/CodeUtil/curd/tableList.jsp");
  if(record){
    $('#fkTableNo').val(record.tableNo);
  }
}

function onSelectTable2() {
  var record = newWindow(contextPath + "/rad/CodeUtil/curd/tableList.jsp?categoryNo=2");
  if(record){
    $('#fkTableNo2').val(record.tableNo);
  }
}

function onSelectField() {
  if($('#fkTableNo').val().trim() == "" || isNaN($('#fkTableNo').val())){
    alert("请选择参照表！");
    return;
  }
  var record = newWindow(contextPath + "/rad/CodeUtil/curd/fieldList.jsp?tableNo="+$('#fkTableNo').val());
  if(record){
    $('#fkRelaFieldNo').val(record.fieldNo);
  }
}

function onSelectField2() {
  if($('#fkTableNo').val().trim() == "" || isNaN($('#fkTableNo').val())){
    alert("请选择参照表！");
    return;
  }
  var record = newWindow(contextPath + "/rad/CodeUtil/curd/fieldList.jsp?tableNo="+$('#fkTableNo').val());
  if(record){
    $('#fkNameFieldNo').val(record.fieldNo);
  }
}

function onSelectField3() {
  if($('#fkTableNo2').val().trim() == "" || isNaN($('#fkTableNo2').val())){
    alert("请选择参照表！");
    return;
  }
  var record = newWindow(contextPath + "/rad/CodeUtil/curd/fieldList.jsp?tableNo="+$('#fkTableNo2').val());
  if(record){
    $('#fkFilter').val(record.fieldNo);
  }
}

function onSelectField4() {
  if($('#fkTableNo2').val().trim() == "" || isNaN($('#fkTableNo2').val())){
    alert("请选择参照表！");
    return;
  }
  var record = newWindow(contextPath + "/rad/CodeUtil/curd/fieldList.jsp?tableNo="+$('#fkTableNo2').val());
  if(record){
    $('#fkNameFieldNo2').val(record.fieldNo);
  }
}

function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  child = window.showModalDialog(url,
      '',
      'dialogWidth:523px;scroll:auto;dialogHeight:290px;help:no;directories:no;location:no;menubar:no;resizeable:no;status:no;toolbar:no;');
  return child;
}
</script>
</head>
<body>
<form action=""	method="post" id="dataList" name="dataList">
	<input type=hidden name="id" id="count" value=0> 
	<input type=hidden name="YHDsTable" value="yh.core.data.YHDsTable"> 
	<input type=hidden name="YHDsField" value="yh.core.data.YHDsField">
	<div class=tableDiv >
	<div class="tableClass">
 <table id="table">
	<tr>
		<td>表编码</td>
		<td><input type="text" id="tableNo" name="tableNo" size="13" maxlength ="5" class = "SmallInput" onkeyup="onChange2(this.value)"><font style='color:red'>*</font></td>
		<td>表名称</td>
		<td><input type="text" id="tableName" name="tableName" size="13" class="SmallInput" onchange="onChTableName2(this.value)"><font style='color:red'>*</font></td>
		<td>类名称</td>
		<td><input type="text" id="className" name="className" size="13" class="SmallInput" ><font style='color:red'>*</font></td>
		<td>表描述</td>
		<td><input type="text" id="tableDesc" name="tableDesc" size="13" class="SmallInput"><font style='color:red'>*</font></td>
		<td>表类型</td>
		<td><select id="categoryNo" name="categoryNo">
			<option value="1">代码表</option>
			<option value="2">小编码表</option>
			<option value="3">参数表</option>
			<option value="4">数据主表</option>
			<option value="5">数据从表</option>
			<option value="6">多对多关系表</option>
		</select></td>
		<td> <input type="button" name="submit" class="SmallButtonW " onclick="doSbumit2();" value="提交"></td>
        <td> <input type="button" class="SmallButtonC " onclick="getPhyics2()" value="抽取物理结构"></td>
	</tr>
 </table>
 </div>
 </div>
 </form>
	<div id="divi2" class="ui-layout-center">
	  <table id="divieeeeee2"></table>
	  <div id="pager2"></div>
	</div>
<br>
<div id="table_div" style="display: none;" name="tableN_div" class="TableLine1 ">
	<input type="hidden" id="divId" value=""/>
  <table id="tableDiv" class="TableLine1  ">
	<thead>
		<tr>
			<td>字段编码</td>
			<td><input type=text id="fieldNo" name="fieldNo" value="" maxlength="8" class ="SmallInput"></td>
			<td>字段名称</td>
			<td><input type=text id="fieldName" name="fieldName" value="" class ="SmallInput" onchange ="onFieldTableName2(this.value)"></td>
		</tr>
		<tr>
			<td>字段描述</td>
			<td><input type=text id="fieldDesc" name="fieldDesc" value="" class="SmallInput">
			    <input type=hidden id="seqId" name="seqId" size=13 value=0></td>
			<td>属性名称</td>
			<td><input type=text id="propName" name="propName" value="" class ="SmallInput"></td>
		</tr>
		<tr>
			<td>参照表</td>
			<td><input type=text id="fkTableNo" name="fkTableNo" value="" class ="SmallInput" onclick="onSelectTable()"></td>
			<td>参照表2</td>
			<td><input type=text id="fkTableNo2" name="fkTableNo2" value="" class="SmallInput" onclick="onSelectTable2()"></td>
		</tr>
		<tr>
			<td>关联字段</td>
			<td><input type=text id="fkRelaFieldNo" name="fkRelaFieldNo" value="" class="SmallInput" onclick="onSelectField()"></td>
			<td>筛选字段</td>
			<td><input type=text id="fkFilter" name="fkFilter" value="" class="SmallInput"  onclick="onSelectField3()"></td>
		</tr>
		<tr>
			<td>显示字段</td>
			<td><input type=text id="fkNameFieldNo" name="fkNameFieldNo" value="" class="SmallInput" onclick="onSelectField2()"></td>
			<td>编码类别</td>
			<td><input type=text id="codeClass" name="codeClass" value="" class="SmallInput"></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td>显示字段2</td>
			<td><input type=text id="fkNameFieldNo2" name="fkNameFieldNo2" value="" class = "SmallInput" onclick="onSelectField4()"></td>
		</tr>
		<tr>
			<td>缺省值</td>
			<td><input type=text id="defaultValue" name="defaultValue" value="" class ="SmallInput"></td>
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
			<td colspan="3">
				<select id="isMustFill" name="isMustFill">
					<option value="1">是</option>
					<option value="0">否</option>
				</select>
			</td>
		<tr height="60px">
			<td colspan="4" align="center">
				<input type="button" name="saveDiv" value="保存" onclick="save2()" class="SmallButton ">
				<input type="button" name="close" value="关闭" onclick="closeDiv2()" class="SmallButton ">
			</td>
		</tr>
	</thead>
	<tbody id="tbodyDiv">
   </tbody>
 </table>
</div>
</body>
</html>
