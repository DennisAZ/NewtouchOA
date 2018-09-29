function show(rowId) {
  $('#table_div2').dialog({
    title: "详情",
    height: 420,
    width: 520,
    modal: true
  });
  var divId = document.getElementById("divId");
  divId.value = rowId;
  
  var record = $('#divieeeeee2').jqGrid('getRowData',rowId);
  
  document.getElementById("fieldNo").value = $('#'+rowId+'_fieldNo').val();
  var fieldNo = document.getElementById("fieldNo").value;
    
  document.getElementById("fieldName").value = $('#'+rowId+'_fieldName').val();
  var fieldName = document.getElementById("fieldName").value;
  
  var propName = $('#'+rowId+'_fieldName').val();
  var str = propName;
  var strs = propName.split("_");
  var result = "";
  for(var i = 0;i < strs.length; i++){
    var strt = strs[i].substr(0,1).toUpperCase()+strs[i].substr(1).toLowerCase();
    result += strt;
  }
  var endRes = result.substr(0,1).toLowerCase()+result.substr(1);
    
  document.getElementById("propName").value = endRes;
  var propName = document.getElementById("propName").value;
    
  document.getElementById("fieldDesc").value = $('#'+rowId+'_fieldDesc').val();
  var fieldDesc = document.getElementById("fieldDesc").value;
    
  document.getElementById("fkTableNo").value = record.fkTableNo == 'null' ? '' : record.fkTableNo;
  var fkTableNo = document.getElementById("fkTableNo").value;
    
  document.getElementById("fkTableNo2").value = record.fkTableNo2 == 'null' ? '' : record.fkTableNo2;
  var fkTableNo2 = document.getElementById("fkTableNo2").value;
    
  document.getElementById("fkRelaFieldNo").value = record.fkRelaFieldNo == 'null' ? '' : record.fkRelaFieldNo;
  var fkRelaFieldNo = document.getElementById("fkRelaFieldNo").value;
    
  document.getElementById("fkNameFieldNo").value = record.fkNameFieldNo == 'null' ? '' : record.fkNameFieldNo;
  var fkNameFieldNo = document.getElementById("fkNameFieldNo").value;
    
  document.getElementById("fkFilter").value = record.fkFilter == 'null' ? '' : record.fkFilter;
  var fkFilter = document.getElementById("fkFilter").value;
    
  document.getElementById("codeClass").value = record.codeClass == 'null' ? '' : record.codeClass;
  var codeClass = document.getElementById("codeClass").value;
    
  document.getElementById("defaultValue").value = record.defaultValue == 'null' ? '' : record.defaultValue;
  var defaultValue = document.getElementById("defaultValue").value;
    
  document.getElementById("formatRule").value = record.formatRule == 'null' ? '' : record.formatRule;
  var formatRule = document.getElementById("formatRule").value;
    
  document.getElementById("errorMsrg").value = record.errorMsrg == 'null' ? '' : record.errorMsrg;
  var errorMsrg = document.getElementById("errorMsrg").value;
    
  document.getElementById("fieldPrecision").value = record.fieldPrecision == 'null' ? '' : record.fieldPrecision;
  var fieldPrecision = document.getElementById("fieldPrecision").value;
    
  document.getElementById("fieldScale").value = record.fieldScale == 'null' ? '' : record.fieldScale;
  var fieldScale = document.getElementById("fieldScale").value;
    
  document.getElementById("displayLen").value = record.displayLen == 'null' ? '' : record.displayLen;
  var displayLen = document.getElementById("displayLen").value;
  
  document.getElementById("fkNameFieldNo2").value = record.fkNameFieldNo2 == 'null' ? '' : record.fkNameFieldNo2;
  var fkNameFieldNo2 = document.getElementById("fkNameFieldNo2").value;
  
  var select = document.getElementById("dataType");
  var option = select.getElementsByTagName("option");
  for ( var i = 0; i < option.length; i++) {
    if (option[i].value == record.dataType) {
      option[i].selected = true;
    }
  }
  var selectisPrimKey = document.getElementById("isPrimaryKey");
  var option = selectisPrimKey.getElementsByTagName("option");
  for ( var i = 0; i < option.length; i++) {
    if (option[i].value == record.isPrimaryKey){
      option[i].selected = true;
    }
  }
  
  var selectisIdentity = document.getElementById("isIdentity");
  var option = selectisIdentity.getElementsByTagName("option");
  for ( var i = 0; i < option.length; i++) {
    if (option[i].value == record.isIdentity){
      option[i].selected = true;
    }
  }
  
  var selectisMustFill = document.getElementById("isMustFill");
  var option = selectisMustFill.getElementsByTagName("option");
  for ( var i = 0; i < option.length; i++) {
    if (option[i].value == record.isMustFill){
      option[i].selected = true;
   }
  }
  
  var selectformatMode = document.getElementById("formatMode");
  var option = selectformatMode.getElementsByTagName("option");
  for ( var i = 0; i < option.length; i++) {
    if (option[i].value == record.formatMode){
      option[i].selected = true;
   }
  }
}

function save2() {
  var divId = document.getElementById("divId").value;
  var reg=/^[0-9]*$/;
  var fieldNo = document.getElementById("fieldNo");
  var fieldName = document.getElementById("fieldName");
  var fieldDesc = document.getElementById("fieldDesc");
  var fieldPrecision = document.getElementById("fieldPrecision");
  var fieldScale = document.getElementById("fieldScale");
  var displayLen = document.getElementById("displayLen");
  if(!reg.test(fieldNo.value)){
    alert("字段编码只能输入数字!");
    fieldNo.focus();
	  return false;
	}
  if((fieldNo.value.length == 0)||(fieldNo.value.length !=8 )){
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
  if(!reg.test(fieldScale.value)){
    alert("小数位数只能输入数字!");
    fieldScale.focus();
    return false;
  }
  if(!reg.test(fieldPrecision.value)){
    alert("数值长度只能输入数字!");
    fieldPrecision.focus();
    return false;
  }
  if(!reg.test(displayLen.value)){
    alert("显示长度只能输入数字!");
    displayLen.focus();
    return false;
  }
			
  $("#"+divId+"_fieldNo").val(document.getElementById("fieldNo").value);
  $("#"+divId+"_fieldName").val(document.getElementById("fieldName").value);
  $("#"+divId+"_fieldDesc").val(document.getElementById("fieldDesc").value);

  $("#divieeeeee2").jqGrid('setRowData', divId,{
    "propName":document.getElementById("propName").value,
    "fkTableNo":document.getElementById("fkTableNo").value,
    "fkTableNo2":document.getElementById("fkTableNo2").value,
    "fkRelaFieldNo":document.getElementById("fkRelaFieldNo").value,
    "fkNameFieldNo":document.getElementById("fkNameFieldNo").value,
    "fkFilter":document.getElementById("fkFilter").value,
    "codeClass":document.getElementById("codeClass").value,
    "defaultValue":document.getElementById("defaultValue").value,
    "formatMode":document.getElementById("formatMode").value,
    "formatRule":document.getElementById("formatRule").value,
    "errorMsrg":document.getElementById("errorMsrg").value,
    "fieldPrecision":document.getElementById("fieldPrecision").value,
    "fieldScale":document.getElementById("fieldScale").value,
    "dataType":document.getElementById("dataType").value,
    "isPrimaryKey":document.getElementById("isPrimaryKey").value,
    "isIdentity":document.getElementById("isIdentity").value,
    "displayLen":document.getElementById("displayLen").value,
    "isMustFill":document.getElementById("isMustFill").value,
    "fkNameFieldNo2":document.getElementById("fkNameFieldNo2").value
  });
  var select = document.getElementById("dataType");
  var option = select.getElementsByTagName("option");
  for(var i=0; i<option.length; i++){
    if(option[i].value== document.getElementById("dataType").value){
      option[i].selected=true;
    }
  }
		
  var selectisPrimKey = document.getElementById("isPrimaryKey");
  var option = selectisPrimKey.getElementsByTagName("option");
  for(var i=0; i<option.length; i++){
    if(option[i].value== document.getElementById("isPrimaryKey").value){
      option[i].selected=true;
    }
  }
		
  var selectisIdentity = document.getElementById("isIdentity");
  var option = selectisIdentity.getElementsByTagName("option");
  for(var i=0; i<option.length; i++){
    if(option[i].value== document.getElementById("isIdentity").value){
      option[i].selected=true;
    }
  }
  		
  var selectisMustFill = document.getElementById("isMustFill");
  var option = selectisMustFill.getElementsByTagName("option");
  for(var i=0; i<option.length; i++){
    if(option[i].value== document.getElementById("isMustFill").value){
      option[i].selected=true;
    }
  }
  		
  var selectformatMode = document.getElementById("formatMode");
  var option = selectformatMode.getElementsByTagName("option");
  for(var i=0; i<option.length; i++){
    if(option[i].value== document.getElementById("formatMode").value){
      option[i].selected=true;
    }
  }
  $('#table_div2').dialog("close");
}

function closeDiv2(){
  $('#table_div2').dialog("close");
}

function check2() {
  var reg = /^[0-9]*$/;
  var tableNo = document.dataList.tableNo;
  var tableName = document.getElementById("tableName");
  var className = document.getElementById("className");
  var tableDesc = document.getElementById("tableDesc");
  var fieldNo = document.getElementById("fieldNo");
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
  if(isExistsTableNo2(tableNo.value)){
    alert("表编码已被使用，请选择其他表编码");
    tableNo.select();
    return false;
  }
  var ids = $("#divieeeeee2").jqGrid('getDataIDs');
  for ( var i = 0; i < ids.length; i++) {
    var fieldNo = document.getElementById(ids[i]+"_fieldNo");
    var fieldName = document.getElementById(ids[i]+"_fieldName");
    var fieldDesc = document.getElementById(ids[i]+"_fieldDesc");
    if(fieldNo == null){
      continue;
    }
    if (!reg.test(fieldNo.value)) {
      alert("字段编码只能输入数字!");
      fieldNo.focus();
      return false;
    }
    if ((fieldNo.value.length == 0) || (fieldNo.value.length != 8)) {
      alert("字段编码输入长度为8位");
      fieldNo.focus();
      return false;
    }
    if ((fieldName.value.length == 0)) {
      alert("字段名称不能为空");
      fieldName.focus();
      return false;
    }
    if ((fieldDesc.value.length == 0)) {
      alert("字段描述不能为空");
      fieldDesc.focus();
      return false;
  	}
	}
  return true;
}

function isExistsTableNo2(tableNo){
  var returnValue;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDefAct/existsTableNo.act?tableNo="+tableNo+"&seqId="+$('#seqId').val();
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

function addField2(){
  var id = $("#divieeeeee2").jqGrid('getDataIDs')[$("#divieeeeee2").jqGrid('getDataIDs').length-1];
  var value = parseInt($('#'+id+'_fieldNo').val()) + 1;
  var len = (value+"").length;
  for(var i = 0; i < $('#'+id+'_fieldNo').val().length - len; i++){
    value = "0" + value;
  }
  jQuery("#divieeeeee2").jqGrid('addRowData',parseInt(id)+1,{
    fieldNo:value,
    isIdentity:0, 
    isMustFill:0, 
    isPrimaryKey:0
    });
}

function show2(index) {
  var record = $("#divieeeeee").jqGrid('getRowData',index);
  var div = document.getElementById("tableForm");
  var div1 = document.getElementById("divi");
  var div2 = document.getElementById("table_div");
  div.style.display = "";
  div1.style.display = "none";
  div2.style.display = "";
  var tableNo = record.tableNo;
  document.getElementById("tableNo").value = record.tableNo;
  document.getElementById("tableName").value = record.tableName;
  document.getElementById("className").value = record.className;
  document.getElementById("tableDesc").value = record.tableDesc;
  document.getElementById("categoryNo").value = record.categoryNo;
  document.getElementById("seqId").value = record.seqId;
  
  //隐藏字段 tableNo
  document.getElementById("tableNoDiv").value = document.getElementById("tableNo").value;
  $("#divieeeeee2").jqGrid({
    url: "/yh/yh/rad/dsdef/act/YHDsDefMoreAct/testMethod2.act?dtoClass=yh.core.data.YHPageQueryParamNew&nameStr=seqId, tableNo, fieldNo, fieldName, propName, fieldDesc, fkTableNo, fkTableNo2, fkRelaFieldNo, fkNameFieldNo, fkFilter, codeClass, defaultValue, formatMode, formatRule, errorMsrg, fieldPrecision, fieldScale, dataType, isIdentity, displayLen, isMustFill, isPrimaryKey, fkNameFieldNo2&tableNo=" + tableNo,
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
      {name:'fkNameFieldNo2' ,index:'fkNameFieldNo2'  ,width:100   ,hidden: true},
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
        $("#divieeeeee2").jqGrid('setRowData',ids[i],{act:str});
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
//  seqId, tableNo, fieldNo, fieldName, propName, fieldDesc, fkTableNo, fkTableNo2, fkRelaFieldNo, fkNameFieldNo, fkFilter, codeClass, defaultValue
//  , formatMode, formatRule, errorMsrg, fieldPrecision, fieldScale, dataType, isIdentity, displayLen, isMustFill, isPrimaryKey
}
function buttonOnclick2(rowId){
  $('#divieeeeee2').jqGrid('delRowData',rowId);
}

function showField(recod, index) {
	var div2 = document.getElementById("table_div2");
	var width = 480;
	var height = 310;
	div2.style.left = (screen.width - width) / 2;
	div2.style.top = (screen.height - height) / 2 - 150;
	div2.style.width = width;
	div2.style.height = height;
	div2.style.zIndex = 100;
	if (div2.record) {
		alert("1");
		recod = div2.record;
	} else {
		alert("2");
		div2.record = recod;
	}
	div2.style.display = "";
	//改前的值
	document.getElementById("fieldNo").value = recod.getField('fieldNo').value;
	document.getElementById("fieldNo").index = "YH_Grid_table_td_"
			+ recod.getField('fieldNo').key + index;

	document.getElementById("fieldName").value = recod.getField('fieldName').value;
	document.getElementById("fieldName").index = "YH_Grid_table_td_"
			+ recod.getField('fieldName').key + index;

	document.getElementById("fieldDesc").value = recod.getField('fieldDesc').value;
	document.getElementById("fieldDesc").index = "YH_Grid_table_td_"
			+ recod.getField('fieldDesc').key + index;

	document.getElementById("propName").value = recod.getField('propName').value;
	document.getElementById("propName").index = "YH_Grid_table_td_"
			+ recod.getField('propName').key + index;

	document.getElementById("fkTableNo").value = recod.getField('fkTableNo').value;
	document.getElementById("fkTableNo").index = "YH_Grid_table_td_"
			+ recod.getField('fkTableNo').key + index;

	document.getElementById("fkTableNo2").value = recod.getField('fkTableNo2').value;
	document.getElementById("fkTableNo2").index = "YH_Grid_table_td_"
			+ recod.getField('fkTableNo2').key + index;

	document.getElementById("fkRelaFieldNo").value = recod
			.getField('fkRelaFieldNo').value;
	document.getElementById("propName").index = "YH_Grid_table_td_"
			+ recod.getField('propName').key + index;

	document.getElementById("fkNameFieldNo").value = recod
			.getField('fkNameFieldNo').value;
	document.getElementById("fkNameFieldNo").index = "YH_Grid_table_td_"
			+ recod.getField('fkNameFieldNo').key + index;

	document.getElementById("fkFilter").value = recod.getField('fkFilter').value;
	document.getElementById("fkFilter").index = "YH_Grid_table_td_"
			+ recod.getField('fkFilter').key + index;

	document.getElementById("codeClass").value = recod.getField('codeClass').value;
	document.getElementById("codeClass").index = "YH_Grid_table_td_"
			+ recod.getField('codeClass').key + index;

	document.getElementById("defaultValue").value = recod
			.getField('defaultValue').value;
	document.getElementById("defaultValue").index = "YH_Grid_table_td_"
			+ recod.getField('defaultValue').key + index;

	document.getElementById("formatMode").value = recod.getField('formatMode').value;
	document.getElementById("formatMode").index = "YH_Grid_table_td_"
			+ recod.getField('formatMode').key + index;

	document.getElementById("formatRule").value = recod.getField('formatRule').value;
	document.getElementById("formatRule").index = "YH_Grid_table_td_"
			+ recod.getField('formatRule').key + index;

	document.getElementById("errorMsrg").value = recod.getField('errorMsrg').value;
	document.getElementById("errorMsrg").index = "YH_Grid_table_td_"
			+ recod.getField('errorMsrg').key + index;

	document.getElementById("fieldPrecision").value = recod
			.getField('fieldPrecision').value;
	document.getElementById("fieldPrecision").index = "YH_Grid_table_td_"
			+ recod.getField('fieldPrecision').key + index;

	document.getElementById("fieldScale").value = recod.getField('fieldScale').value;
	document.getElementById("fieldScale").index = "YH_Grid_table_td_"
			+ recod.getField('fieldScale').key + index;

	document.getElementById("dataType").value = recod.getField('dataType').value;
	document.getElementById("dataType").index = "YH_Grid_table_td_"
			+ recod.getField('dataType').key + index;

	document.getElementById("isPrimaryKey").value = recod.getField('isPrimaryKey').value;
	document.getElementById("isPrimaryKey").index = "YH_Grid_table_td_"
			+ recod.getField('isPrimaryKey').key + index;

	document.getElementById("isIdentity").value = recod.getField('isIdentity').value;
	document.getElementById("isIdentity").index = "YH_Grid_table_td_"
			+ recod.getField('isIdentity').key + index;

	document.getElementById("displayLen").value = recod.getField('displayLen').value;
	document.getElementById("displayLen").index = "YH_Grid_table_td_"
			+ recod.getField('displayLen').key + index;

	document.getElementById("isMustFill").value = recod.getField('isMustFill').value;
	document.getElementById("isMustFill").index = "YH_Grid_table_td_"
			+ recod.getField('isMustFill').key + index;
	
	document.getElementById("fieldNoDiv").value = document.getElementById("fieldNo").value
	alert(document.getElementById("fieldNoDiv").value+"++++");

}

function deleteTable2(index) {
  var record = $("#divieeeeee").jqGrid('getRowData',index);
  document.getElementById("tableNo").value = record.tableNo;
  document.getElementById("seqId").value = record.seqId;
  var tableNoId = document.getElementById("tableNo").value;
  var seqId = document.getElementById("seqId").value;
  if(confirm("确认删除")){
    var url = '/yh/yh/rad/dsdef/act/YHDsDefAct/deleteDsDef.act';
    jQuery.ajax({
      url: url,
      data: 'seqId=' + seqId + '&tableNoF=' + tableNoId,
      dataType: "json",
      success: function(json) {
        if (json.rtState == "0") {
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
  }
}

function responseDelete() {
	if (xmlHttp.readyState == 4) {
		if (xmlHttp.status == 200) {
			var table_div = document.getElementById("divieeeeee");
			alert(table_div);
			var responseText = xmlHttp.responseText;
			var index = document.getElementById(table_div);
			index.innerHTML = responseText;
		}
	}
}
function deleteField(seqId) {
	alert(seqId);
	var url = '/yh/yh/rad/dsdef/act/YHDsDefDeleteFieldAct/testMethod.act';
	//location.href="/yh/yh/rad/dsdef/act/YHDsDefDeleteFieldAct/testMethod.act?tableNoField="+tableNo;
	var rtJson = getJsonRs(url, 'seqId=' + seqId); 
	if (rtJson.rtState == '0') { 
	alert('删除成功'); 
	window.location.reload(); 
	}else { 
	alert(rtJson.rtMsrg); 
	} 
}

function onChange2(){
  var its = $("#divieeeeee2").jqGrid('getDataIDs');
  for(var i = 0; i < its.length; i++){
    if(!document.getElementById(its[i] + "_fieldNo")){
      continue;
    }
    var fieldNoId = document.getElementById(its[i]+"_fieldNo").value;
    var length = fieldNoId.length;
    var fieldLast;
    if(length > 3){
      fieldLast = fieldNoId.substr(length-3,length-1);
    }else {
      fieldLast = fieldNoId;
    }
    document.getElementById(its[i]+"_fieldNo").value = document.getElementById("tableNo").value + fieldLast;
  }
}

function onChTableName2(){
  var tableName = document.getElementById("tableName").value;
  var className = document.getElementById("className").value;
  var str = tableName;
  var strs = tableName.split("_");
  var result = "";
  for(var i = 0;i < strs.length; i++){
    var strt = strs[i].substr(0,1).toUpperCase()+strs[i].substr(1).toLowerCase();
    result += strt;
  }
  document.getElementById("className").value = "YH"+result;
  document.getElementById("tableName").value = tableName.toUpperCase();
}

function onFieldTableName(){
  var fieldName = document.getElementById("fieldName").value;
  var propName = document.getElementById("propName").value;
  var str = fieldName;
  var strs = fieldName.split("_");
  var result = "";
  for(var i = 0;i < strs.length; i++){
    var strt = strs[i].substr(0,1).toUpperCase()+strs[i].substr(1).toLowerCase();
    result += strt;
  }
  var sss = result.substr(0,1).toLowerCase()+result.substr(1);
  document.getElementById("propName").value = sss;
  document.getElementById("fieldName").value = fieldName.toUpperCase();
}