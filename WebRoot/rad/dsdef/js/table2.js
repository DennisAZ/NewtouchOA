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

function show(rowId) {
  $('#table_div').dialog({
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

function responseDelete() {
	if (xmlHttp.readyState == 4) {
		if (xmlHttp.status == 200) {
			var table_div = document.getElementById("table_div")
			var responseText = xmlHttp.responseText;
			var index = document.getElementById(table_div);
			index.innerHTML = responseText;
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
  $('#table_div').dialog("close");
}

function closeDiv2() {
  $('#table_div').dialog("close");
}

function show1(recod) {
	var div = document.getElementById("tableForm");
	var div1 = document.getElementById("divieeeeee");
	div1.style.display = "none";
	div.style.display = "";
}

function deleteFieldNull(fieldNoId) {
	var id = document.getElementById("count");
	id.value = parseInt(id.value) + 1;
	var table = document.getElementById("tbody");
	var tr = document.createElement("tr");
	var td = document.createElement("td");
	var tableNo = document.getElementById("tableNo");
	var input = document.createElement("input");
	input.id = "fieldNo_" + id.value;
	td.removeChild(this.id);
	tr.removeChild(td);
	table.removeChild(tr);
}

function buttonOnclick2(rowId) {
  $('#divieeeeee2').jqGrid('delRowData',rowId);
}

function onChange2() {
	var its = $("#divieeeeee2").jqGrid('getDataIDs');
	for ( var i = 0; i < its.length; i++) {
	  if(!document.getElementById(its[i] + "_fieldNo")){
	    continue;
	  }
		var fieldNoId = document.getElementById(its[i] + "_fieldNo").value;
		var length = fieldNoId.length;
		var fieldLast;
		if (length > 3) {
			fieldLast = fieldNoId.substr(length - 3, length - 1);
		} else {
			fieldLast = fieldNoId;
		}
		document.getElementById(its[i] + "_fieldNo").value = document.getElementById("tableNo").value + fieldLast;
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
	//alert(update.substr(0,update.indexOf("_"))+update.substr(update.indexOf("_")+1,update.length-1));
	document.getElementById("tableName").value = tableName.toUpperCase();
	document.getElementById("className").value = "YH"+result;
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