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
<script type="text/javascript" src="<%=contextPath %>/rad/dsdef/js/gridtable2.js"></script>
<script type="text/javascript">
function doInit(){
	$("#divieeeeee").jqGrid({
    url:"/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?dtoClass=yh.core.data.YHPageQueryParamNew&nameStr=seqId,tableNo,tableName,tableDesc,className,classDesc,categoryNo&flag=1",
    datatype: "json",
    height: "230px",
    colNames:['seqId', '表编码', '表名称', '表描述', '类名称', '表类型', 'categoryNo', '操作'],
    colModel:[
      {name:'seqId'				,index:'seqId'					,width:80		,hidden: true},    
      {name:'tableNo'			,index:'tableNo'				,width:60 },
      {name:'tableName'		,index:'tableName'			,width:220},
      {name:'tableDesc'		,index:'tableDesc'			,width:150},
      {name:'className'		,index:'className'			,width:180},
      {name:'classDesc'		,index:'classDesc'			,width:80 },
      {name:'categoryNo'	,index:'categoryNo'			,width:80 	,hidden: true},
      {name:'act'					,index:'act'						,width:150}
    ],
    rowNum:10,
    rowList:[10,20,30],
    pager: '#pager',
    gridComplete: opts,
    //sortname: 'id',
    viewrecords: true,
    sortorder: "desc"
  });
	$("#divieeeeee").jqGrid('navGrid','#pager',{add:false, edit:false, del:false, search:true, refresh:true});
}

function opts(){
	var ids = $("#divieeeeee").jqGrid('getDataIDs');
	for(var i = 0; i < ids.length; i++){
		var rowId = ids[i];
		var str = "<center>"
				 		+ "<a href=javascript:show2(" + rowId + ");><font color='blue'>修改</font></a> "
						+ "<a href=javascript:deleteTable2(" + rowId + ");><font color='blue'>删除</font></a> "
						+ "<a href=javascript:toPhyicsAction2(" + rowId + ");><font color='blue'>生成物理结构</font></a>"
						+ "</center>";
		$("#divieeeeee").jqGrid('setRowData',ids[i],{act:str});
	}	
}

function toPhyicsAction2(index) {
  var record = $("#divieeeeee").jqGrid('getRowData',index);
  var tableNo = record.tableNo;
  toPhyics2(tableNo);
}

function doSbumit2() {
  if(check2()){
	  document.getElementById("count").value = $("#divieeeeee2").jqGrid('getDataIDs').length;
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
	  
	  var url = contextPath + "/yh/rad/dsdef/act/YHDsDefAct/updateDsDef.act";
	  jQuery.ajax({
	    type: "POST",
	    url: url,
	    data: $("#dataList").serialize()+subStr,
	    dataType: "json",
	    success: function(json) {
	      if (json.rtState == "0") {
	        alert(json.rtMsrg);
	        location.reload();
	      }
	      else{
	        alert(json.rtMsrg);
	      }
	    },
	    error: function(json) {
	      alert(json.rtMsrg);
	    }
	  });
  }
}

function checkTabIsExist2(){
  var returnValue;
  var tableName = $("tableName").value;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/tabIsExist.act";
  jQuery.ajax({
    url: url,
    async: false,
    data: "tableName="+tableName,
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
      returnValue = true
    }
  });
  return returnValue;
}

function dropTab2(){
  var tableName = $("tableName").value;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/dropTab.act";
  jQuery.ajax({
    url: url,
    data: "tableName="+tableName,
    dataType: "json",
    success: function(json) {
      if (json.rtState == "0") {
        alert(rtJson.rtMsrg);
      }
      else{
        alert(rtJson.rtMsrg);
      }
    },
    error: function(json) {
      alert(json.rtMsrg);
    }
  });
}

function toPhyics2(tableNo){
  if(checkTabIsExist2()){
    if(window.confirm("此表的物理结构已经存在,是否先删除此表结构!")){
      dropTab2();
    }else{
      return;
    }
  }
  if(!window.confirm("确认生成物理结构!")){
    return;
  }
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/toPhysicsDb.act";
  jQuery.ajax({
    url: url,
    data: "tableNo="+tableNo,
    dataType: "json",
    success: function(json) {
      if (json.rtState == "0") {
        alert(json.rtMsrg);
      }
      else{
        alert(json.rtMsrg);
      }
    },
    error: function(json) {
      alert(json.rtMsrg);
    }
  });
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
<body onload="doInit()">
	<div id="divi" class="ui-layout-center">
	  <table id="divieeeeee"></table>
	  <div id="pager"></div>
	</div>
 <div id="tableForm" style="display: none;" name="tableForm">
 <form  name="dataList" id="dataList"> 
  <input type=hidden name="id" id="count" value=0> 
  <input type=hidden name="YHDsTable" value="yh.core.data.YHDsTable"> 
  <input type=hidden name="YHDsField" value="yh.core.data.YHDsField">
  <input type=hidden name="tableNo1" value="">
  <div  class=tableDiv>
   <table id="table" >
    <thead>
	 <tr>
		<td>表编码</td>
		<td><input type="text" id="tableNo" name="tableNo" onkeyup="onChange2(this.value)" maxlength = "5"><font style='color:red'>*</font></td>
		<td>表名称</td>
		<td><input type="text" id="tableName" name="tableName" onchange="onChTableName2(this.value)"><font style='color:red'>*</font></td>
		<td>类名称</td>
		<td><input type="text" id="className" name="className"><font style='color:red'>*</font></td>
		<td>表描述</td>
		<td><input type="text" id="tableDesc" name="tableDesc"><font style='color:red'>*</font></td>
		<td><input type="hidden" id="seqId" name="seqId"></td>
		<td>表类型</td>
		<td><select id="categoryNo" name="categoryNo">
			<option value="1">代码表</option>
			<option value="2">小编码表</option>
			<option value="3">参数表</option>
			<option value="4">数据主表</option>
			<option value="5">数据从表</option>
			<option value="6">多对多关系表</option>
		</select></td>
		<td><input type="button" name="submi"t class="SmallButtonW " onclick="doSbumit2();" value="提交"/></td>
	 </tr>
	 </thead>
	 <tbody id="tbodytable">
	</tbody>
  </table>
  </div>
 <div id="inputDiv" style="display: none;" name="inputDiv">
 <input type="text" name="tableNoDiv" id="tableNoDiv">
 </div>
 </form>
 <div id="table_div" style="width:200;height:200;display: none; position: absolute; padding-top:50px" name="tableN_div"></div>
 <br>

	<div id="divi2" class="ui-layout-center">
	  <table id="divieeeeee2"></table>
	  <div id="pager2"></div>
	</div>
</div>

 <div id="table_div2" style="display: none;" name="tableN_div2">
 <input type="hidden" id="divId" value="" />
 <table id="tableDiv" class="TableLine1 ">
	<thead>
		<tr>
			<td>字段编码</td>
			<td><input type=text id="fieldNo" name="fieldNo" value="" maxlength ="8" class = "SmallInput"></td>
			<td>字段名称</td>
			<td><input type=text id="fieldName" name="fieldName" value="" class = "SmallInput" onchange ="onFieldTableName(this.value)"></td>
		</tr>
		<tr>
			<td>字段描述</td>
			<td><input type=text id="fieldDesc" name="fieldDesc" value="" class = "SmallInput"></td>
			<td>属性名称</td>
			<td><input type=text id="propName" name="propName" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>参照表</td>
			<td><input type=text id="fkTableNo" name="fkTableNo" value="" class = "SmallInput" onclick="onSelectTable()"></td>
			<td>参照表2</td>
			<td><input type=text id="fkTableNo2" name="fkTableNo2" value="" class = "SmallInput" onclick="onSelectTable2()"></td>
		</tr>
		<tr>
			<td>关联字段</td>
			<td><input type=text id="fkRelaFieldNo" name="fkRelaFieldNo"value="" class = "SmallInput" onclick="onSelectField()"></td>
			<td>筛选字段</td>
			<td><input type=text id="fkFilter" name="fkFilter" value="" class = "SmallInput" onclick="onSelectField3()"></td>
		</tr>
		<tr>
			<td>显示字段</td>
			<td><input type=text id="fkNameFieldNo" name="fkNameFieldNo"value="" class = "SmallInput" onclick="onSelectField2()"></td>
			<td>编码类别</td>
			<td><input type=text id="codeClass" name="codeClass" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td>显示字段2</td>
			<td><input type=text id="fkNameFieldNo2" name="fkNameFieldNo2" value="" class = "SmallInput" onclick="onSelectField4()"></td>
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
			    </select>
			</td>
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
			<td><select id="isPrimaryKey" name=isPrimaryKey>
				<option value="1">是</option>
				<option value="0">否</option>
			    </select>
			</td>
		</tr>
		<tr>
			<td>自增</td>
			<td><select id="isIdentity" name="isIdentity">
				<option value="1">是</option>
				<option value="0">否</option>
			    </select>
			</td>
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
		</tr>
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
