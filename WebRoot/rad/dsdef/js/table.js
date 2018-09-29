function add() {
	var id = document.getElementById("count");
	id.value = parseInt(id.value);
	var table = document.getElementById("tbody");
	var tr = document.createElement("tr");
	var td = document.createElement("td");
	var tableNo = document.getElementById("tableNo");
	var tableValue = document.getElementById("tableNo").value * 1000;
	var valueId;
	if (tableValue == 0) {
		tableValue = "";
		var a = 1000;
		var valueId1 = (a + (parseInt(id.value) + 1)) + "";
		valueId = valueId1.substr(valueId1.length - 3, valueId1.length - 1);
	} else {
		valueId = parseInt(id.value) + 1;
	}

	var input = document.createElement("input");
	input.id = "fieldNo_" + id.value;
	input.setAttribute("type", "text");
	input.setAttribute("id", "fieldNo_" + id.value);
	input.setAttribute("size", "7");
	input.setAttribute("name", "fieldNo_" + id.value);
	input.setAttribute("value", tableValue + valueId);
	input.className="SmallInput";
	input.onchange = function() {
		var count = document.getElementById('count');
		var change = document.getElementById(this.id).value;
		for ( var i = 0; i < parseInt(count.value); i++) {
			if (this.id != ("fieldNo_" + i)
					&& change == document.getElementById("fieldNo_" + i).value) {
				alert("字段编码有重复请仔细检查，重新填写");
			}
		}
	};
	td.appendChild(input);
	tr.appendChild(td);

	td = document.createElement("td");
	var input = document.createElement("input");
	var ddd = id.value;
	input.id = "fieldName_" + id.value;
	input.setAttribute("type", "text");
	input.setAttribute("id", "fieldName_" + id.value);
	input.setAttribute("size", "7");
	input.setAttribute("name", "fieldName_" + id.value);
	input.setAttribute("value", "");
	input.className="SmallInput";
	input.onchange = function() {
	  var fieldName = document.getElementById(this.id).value;
	  var str = fieldName;
	  var strs = fieldName.split("_");
	  var result = "";
	  for(var i = 0;i < strs.length; i++){
	    var strt = strs[i].substr(0,1).toUpperCase()+strs[i].substr(1).toLowerCase();
	    result += strt;
	 }
	  document.getElementById(this.id).value = fieldName.toUpperCase();
	}
	td.appendChild(input);
	tr.appendChild(td);

	td = document.createElement("td");
	var input = document.createElement("input");
	input.id = "fieldDesc_" + id.value;
	input.setAttribute("type", "text");
	input.setAttribute("id", "fieldDesc_" + id.value);
	input.setAttribute("size", "7");
	input.setAttribute("name", "fieldDesc_" + id.value);
	input.setAttribute("value", "");
	input.className="SmallInput";
	td.appendChild(input);
	tr.appendChild(td);

	td = document.createElement("td");
	var input = document.createElement("input");
	input.id = "buttonxq" + id.value;
	input.className = "SmallButton ";
	input.setAttribute("type", "button");
	input.setAttribute("name", "buttonxq" + id.value);
	input.setAttribute("value", "详情");
	input.onclick = function() {
		var id1 = this.id.substr(8);
		show(id1);
	}
	td.appendChild(input);
	tr.appendChild(td);

	td = document.createElement("td");
	var input = document.createElement("input");
	input.id = "button" + id.value;
	input.className = "SmallButton ";
	// input.idF = "fieldNo_"+id.value;
	input.setAttribute("type", "button");
	input.setAttribute("name", "button" + id.value);
	input.setAttribute("value", "删除");
	buttonOnclick(table, input);
	td.appendChild(input);

	input = document.createElement("input");
	input.id = "tableNo_" + id.value;
	input.setAttribute("type", "hidden");
	input.setAttribute("id", "tableNo_" + id.value);
	input.setAttribute("name", "tableNo_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.id = "seqId_" + id.value;
	input.setAttribute("type", "hidden");
	input.setAttribute("id", "seqId_" + id.value);
	input.setAttribute("name", "seqId_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.id = "propName_" + id.value;
	input.setAttribute("type", "hidden");
	input.setAttribute("id", "propName_" + id.value);
	input.setAttribute("name", "propName_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.id = "fkTableNo_" + id.value;
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "fkTableNo_" + id.value);
	input.setAttribute("id", "fkTableNo_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "fkTableNo2_" + id.value);
	input.setAttribute("id", "fkTableNo2_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "fkRelaFieldNo_" + id.value);
	input.setAttribute("id", "fkRelaFieldNo_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "fkNameFieldNo_" + id.value);
	input.setAttribute("id", "fkNameFieldNo_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "fkFilter_" + id.value);
	input.setAttribute("id", "fkFilter_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "codeClass_" + id.value);
	input.setAttribute("id", "codeClass_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "defaultValue_" + id.value);
	input.setAttribute("id", "defaultValue_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);
	
	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "formatMode_" + id.value);
	input.setAttribute("id", "formatMode_" + id.value);
	input.setAttribute("value", "number");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "formatRule_" + id.value);
	input.setAttribute("id", "formatRule_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "errorMsrg_" + id.value);
	input.setAttribute("id", "errorMsrg_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "fieldPrecision_" + id.value);
	input.setAttribute("id", "fieldPrecision_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "fieldScale_" + id.value);
	input.setAttribute("id", "fieldScale_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "dataType_" + id.value);
	input.setAttribute("id", "dataType_" + id.value);
	input.setAttribute("value", "-7");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "isPrimaryKey_" + id.value);
	input.setAttribute("id", "isPrimaryKey_" + id.value);
	input.setAttribute("value", "1");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "isIdentity_" + id.value);
	input.setAttribute("id", "isIdentity_" + id.value);
	input.setAttribute("value", "1");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "displayLen_" + id.value);
	input.setAttribute("id", "displayLen_" + id.value);
	input.setAttribute("value", "");
	td.appendChild(input);

	input = document.createElement("input");
	input.setAttribute("type", "hidden");
	input.setAttribute("name", "isMustFill_" + id.value);
	input.setAttribute("id", "isMustFill_" + id.value);
	input.setAttribute("value", "1");
	td.appendChild(input);
	tr.appendChild(td);
	table.appendChild(tr);
	id.value++;
}
//function createInput("defaultValue_"){
//input = document.createElement("input");
//input.setAttribute("type", "hidden");
//input.setAttribute("name", add + id.value);
//input.setAttribute("id", add + id.value);
//input.setAttribute("value", "");
//}

function show(id) {
	var div = document.getElementById("table_div");
	var divId = document.getElementById("divId");
	divId.value = id;
	var width = 480;
	var height = 310;
	div.style.left = (screen.width - width) / 2;
	div.style.top = (screen.height - height) / 2 - 150;
	div.style.width = width;
	div.style.height = height;
	div.style.zIndex = 100;
	div.style.display = "";

	document.getElementById("fieldNo").value = document
			.getElementById("fieldNo_" + id).value;
	var fieldNo = document.getElementById("fieldNo").value;

	document.getElementById("fieldName").value = document
			.getElementById("fieldName_" + id).value;
	var fieldName = document.getElementById("fieldName").value;
	//alert(fieldName+"99");
  var propName = document.getElementById("fieldName_" + id).value;
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

	document.getElementById("fieldDesc").value = document
			.getElementById("fieldDesc_" + id).value;
	var fieldDesc = document.getElementById("fieldDesc").value;

	document.getElementById("fkTableNo").value = document
			.getElementById("fkTableNo_" + id).value;
	var fkTableNo = document.getElementById("fkTableNo").value;

	document.getElementById("fkTableNo2").value = document
			.getElementById("fkTableNo2_" + id).value;
	var fkTableNo2 = document.getElementById("fkTableNo2").value;

	document.getElementById("fkRelaFieldNo").value = document
			.getElementById("fkRelaFieldNo_" + id).value;
	var fkRelaFieldNo = document.getElementById("fkRelaFieldNo").value;

	document.getElementById("fkNameFieldNo").value = document
			.getElementById("fkNameFieldNo_" + id).value;
	var fkNameFieldNo = document.getElementById("fkNameFieldNo").value;

	document.getElementById("fkFilter").value = document
			.getElementById("fkFilter_" + id).value;
	var fkFilter = document.getElementById("fkFilter").value;

	document.getElementById("codeClass").value = document
			.getElementById("codeClass_" + id).value;
	var codeClass = document.getElementById("codeClass").value;

	document.getElementById("defaultValue").value = document
			.getElementById("defaultValue_" + id).value;
	var defaultValue = document.getElementById("defaultValue").value;

	document.getElementById("formatRule").value = document
			.getElementById("formatRule_" + id).value;
	var formatRule = document.getElementById("formatRule").value;

	document.getElementById("errorMsrg").value = document
			.getElementById("errorMsrg_" + id).value;
	var errorMsrg = document.getElementById("errorMsrg").value;

	document.getElementById("fieldPrecision").value = document
			.getElementById("fieldPrecision_" + id).value;
	var fieldPrecision = document.getElementById("fieldPrecision").value;

	document.getElementById("fieldScale").value = document
			.getElementById("fieldScale_" + id).value;
	var fieldScale = document.getElementById("fieldScale").value;

	document.getElementById("displayLen").value = document
			.getElementById("displayLen_" + id).value;
	var displayLen = document.getElementById("displayLen").value;

	var select = document.getElementById("dataType");
	var option = select.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("dataType_" + id).value) {
			option[i].selected = true;
		}
	}

	var selectisPrimKey = document.getElementById("isPrimaryKey");
	var option = selectisPrimKey.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("isPrimaryKey_" + id).value) {
			option[i].selected = true;
		}
	}

	var selectisIdentity = document.getElementById("isIdentity");
	var option = selectisIdentity.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("isIdentity_" + id).value) {
			option[i].selected = true;
		}
	}

	var selectisMustFill = document.getElementById("isMustFill");
	var option = selectisMustFill.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("isMustFill_" + id).value) {
			option[i].selected = true;
		}
	}

	var selectformatMode = document.getElementById("formatMode");
	var option = selectformatMode.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("formatMode_" + id).value) {
			option[i].selected = true;
		}
	}
}

function deleteField(fieldNoId) {
	var contextPath = element.getAttribute("contextPath");
	var url = contextPath + "yh.rad.dsdef.act.YHDsDefAct?fieldNo=" + fieldNoId
			+ "";
	createXMLHttpRequest();
	xmlHttp.open("get", url, true);
	xmlHttp.onreadystatechange = responseDelete;
	xmlHttp.send(null);
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

function save() {
	var divId = document.getElementById("divId").value;
	var reg = /^[0-9]*$/;
	var fieldNo = document.getElementById("fieldNo");
	var fieldName = document.getElementById("fieldName");
	var fieldDesc = document.getElementById("fieldDesc");
	var fieldPrecision = document.getElementById("fieldPrecision");
	var fieldScale = document.getElementById("fieldScale");
	var displayLen = document.getElementById("displayLen");
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
	if (!reg.test(fieldScale.value)) {
		alert("小数位数只能输入数字!");
		fieldScale.focus();
		return false;
	}
	if (!reg.test(fieldPrecision.value)) {
		alert("数值长度只能输入数字!");
		fieldPrecision.focus();
		return false;
	}
	if (!reg.test(displayLen.value)) {
		alert("显示长度只能输入数字!");
		displayLen.focus();
		return false;
	}
	document.getElementById("fieldNo_" + divId).value = document
			.getElementById("fieldNo").value;
	document.getElementById("fieldName_" + divId).value = document
			.getElementById("fieldName").value;
	document.getElementById("propName_" + divId).value = document
			.getElementById("propName").value;
	document.getElementById("fieldDesc_" + divId).value = document
			.getElementById("fieldDesc").value;
	document.getElementById("fkTableNo_" + divId).value = document
			.getElementById("fkTableNo").value;
	document.getElementById("fkTableNo2_" + divId).value = document
			.getElementById("fkTableNo2").value;
	document.getElementById("fkRelaFieldNo_" + divId).value = document
			.getElementById("fkRelaFieldNo").value;
	document.getElementById("fkNameFieldNo_" + divId).value = document
			.getElementById("fkNameFieldNo").value;
	document.getElementById("fkFilter_" + divId).value = document
			.getElementById("fkFilter").value;
	document.getElementById("codeClass_" + divId).value = document
			.getElementById("codeClass").value;
	document.getElementById("defaultValue_" + divId).value = document
			.getElementById("defaultValue").value;
	document.getElementById("formatMode_" + divId).value = document
			.getElementById("formatMode").value;
	document.getElementById("formatRule_" + divId).value = document
			.getElementById("formatRule").value;
	document.getElementById("errorMsrg_" + divId).value = document
			.getElementById("errorMsrg").value;
	document.getElementById("fieldPrecision_" + divId).value = document
			.getElementById("fieldPrecision").value;
	document.getElementById("fieldScale_" + divId).value = document
			.getElementById("fieldScale").value;
	document.getElementById("dataType_" + divId).value = document
			.getElementById("dataType").value;
	document.getElementById("isPrimaryKey_" + divId).value = document
			.getElementById("isPrimaryKey").value;
	document.getElementById("isIdentity_" + divId).value = document
			.getElementById("isIdentity").value;
	document.getElementById("displayLen_" + divId).value = document
			.getElementById("displayLen").value;
	document.getElementById("isMustFill_" + divId).value = document
			.getElementById("isMustFill").value;

	var select = document.getElementById("dataType");
	var option = select.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("dataType").value) {
			option[i].selected = true;
		}
	}

	var selectisPrimKey = document.getElementById("isPrimaryKey");
	var option = selectisPrimKey.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("isPrimaryKey").value) {
			option[i].selected = true;
		}
	}

	var selectisIdentity = document.getElementById("isIdentity");
	var option = selectisIdentity.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("isIdentity").value) {
			option[i].selected = true;
		}
	}

	var selectisMustFill = document.getElementById("isMustFill");
	var option = selectisMustFill.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("isMustFill").value) {
			option[i].selected = true;
		}
	}

	var selectformatMode = document.getElementById("formatMode");
	var option = selectformatMode.getElementsByTagName("option");
	for ( var i = 0; i < option.length; i++) {
		if (option[i].value == document.getElementById("formatMode").value) {
			option[i].selected = true;
		}
	}
	var div = document.getElementById("table_div");
	div.style.display = "none";
}

function closeDiv() {
	var div = document.getElementById("table_div");
	div.style.display = "none";
}

function check() {
	var id = document.getElementById("count");
	var reg = /^[0-9]*$/;
	var tableNo1 = document.dataList.tableNo1;
	var tableName = document.dataList.tableName;
	var tableDesc = document.dataList.tableDesc;
	var categoryNo = document.dataList.categoryNo;
	// if(!reg.test(tableNo1.value)){
	// alert("表编码请输入数字");
	// tableNo1.focus();
	// tableNo1.value="";
	// return false;
	// }
	// if(!tableNo1.value.length || tableNo1.value.length != 5){
	// alert("表编码输入长度为5位");
	// tableNo1.focus();
	// return false;
	// }
	// if(!tableName.value){
	// alert("表名称不能为空");
	// tableName.focus();
	// return false;
	// }

	for ( var i = 1; i <= parseInt(id.value); i++) {
		var fieldNo = document.getElementById("fieldNo_" + i);
		var fieldName = document.getElementById("fieldName_" + i);
		var fieldDesc = document.getElementById("fieldDesc_" + i);
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

function buttonOnclick(table, input) {
  input.onclick = function() {
	var id = parseInt(this.id.substr(6));
	var ipt = document.getElementById('count');
	// ipt.value = parseInt(ipt.value);
	// ipt.value = ipt.value - 1;
	var tr = this.parentNode.parentNode;
	for ( var i = parseInt(ipt.value)- 1; i > id ; i--) {
		var fieldNoId = document.getElementById("fieldNo_" + i);
		fieldNoId.value = $("fieldNo_" + (i-1)).value;
	}
	 table.removeChild(tr);

	for ( var i = (id + 1); i < parseInt(ipt.value); i++) {
		var fieldButton = "button" + i;
		var button1 = document.getElementById(fieldButton);
		button1.id = "button" + (parseInt(i) - 1);
		// button1.value = "b" + (i - 1);
		// button1.value = "删除" + (parseInt(i) - 1);

		var buttonxqId = "buttonxq" + i;
		var buttonxq1 = document.getElementById(buttonxqId);
		buttonxq1.id = "buttonxq" + (parseInt(i) - 1);

		var fieldNoLastId = "fieldNo_" + (parseInt(i));
		var fieldNo = document.getElementById(fieldNoLastId);
		fieldNo.id = "fieldNo_" + (i - 1);
		fieldNo.name = "fieldNo_" + (i - 1);
		// fieldNo.value = tableValue+(valueId-1);

		var propNameLastId = "propName_" + (parseInt(i));
		var propName = document.getElementById(propNameLastId);
		propName.id = "propName_" + (i - 1);
		propName.name = "propName_" + (i - 1);

		var fkTableNoLastId = "fkTableNo_" + (parseInt(i));
		var fkTableNo = document.getElementById(fkTableNoLastId);
		fkTableNo.id = "fkTableNo_" + (i - 1);
		fkTableNo.name = "fkTableNo_" + (i - 1);

		var fieldNameLastId = "fieldName_" + (parseInt(i));
		var fieldName = document.getElementById(fieldNameLastId);
		fieldName.id = "fieldName_" + (i - 1);
		fieldName.name = "fieldName_" + (i - 1);

		var fieldDescLastId = "fieldDesc_" + (parseInt(i));
		var fieldDesc = document.getElementById(fieldDescLastId);
		fieldDesc.id = "fieldDesc_" + (i - 1);
		fieldDesc.name = "fieldDesc_" + (i - 1);

		var fkTableNo2LastId = "fkTableNo2_" + (parseInt(i));
		var fkTableNo2 = document.getElementById(fkTableNo2LastId);
		fkTableNo2.id = "fkTableNo2_" + (i - 1);
		fkTableNo2.name = "fkTableNo2_" + (i - 1);

		var fkRelaFieldNoLastId = "fkRelaFieldNo_" + (parseInt(i));
		var fkRelaFieldNo = document.getElementById(fkRelaFieldNoLastId);
		fkRelaFieldNo.id = "fkRelaFieldNo_" + (i - 1);
		fkRelaFieldNo.name = "fkRelaFieldNo_" + (i - 1);

		var fkNameFieldNoLastId = "fkNameFieldNo_" + (parseInt(i));
		var fkNameFieldNo = document.getElementById(fkNameFieldNoLastId);
		fkNameFieldNo.id = "fkNameFieldNo_" + (i - 1);
		fkNameFieldNo.name = "fkNameFieldNo_" + (i - 1);

		var fkFilterLastId = "fkFilter_" + (parseInt(i));
		var fkFilter = document.getElementById(fkFilterLastId);
		fkFilter.id = "fkFilter_" + (i - 1);
		fkFilter.name = "fkFilter_" + (i - 1);

		var codeClassLastId = "codeClass_" + (parseInt(i));
		var codeClass = document.getElementById(codeClassLastId);
		codeClass.id = "codeClass_" + (i - 1);
		codeClass.name = "codeClass_" + (i - 1);

		var defaultValueLastId = "defaultValue_" + (parseInt(i));
		var defaultValue = document.getElementById(defaultValueLastId);
		defaultValue.id = "defaultValue_" + (i - 1);
		defaultValue.name = "defaultValue_" + (i - 1);

		var formatModeLastId = "formatMode_" + (parseInt(i));
		var formatMode = document.getElementById(formatModeLastId);
		formatMode.id = "formatMode_" + (i - 1);
		formatMode.name = "formatMode_" + (i - 1);

		var formatRuleLastId = "formatRule_" + (parseInt(i));
		var formatRule = document.getElementById(formatRuleLastId);
		formatRule.id = "formatRule_" + (i - 1);
		formatRule.name = "formatRule_" + (i - 1);

		var errorMsrgLastId = "errorMsrg_" + (parseInt(i));
		var errorMsrg = document.getElementById(errorMsrgLastId);
		errorMsrg.id = "errorMsrg_" + (i - 1);
		errorMsrg.name = "errorMsrg_" + (i - 1);

		var fieldPrecisionLastId = "fieldPrecision_" + (parseInt(i));
		var fieldPrecision = document.getElementById(fieldPrecisionLastId);
		fieldPrecision.id = "fieldPrecision_" + (i - 1);
		fieldPrecision.name = "fieldPrecision_" + (i - 1);

		var fieldScaleLastId = "fieldScale_" + (parseInt(i));
		var fieldScale = document.getElementById(fieldScaleLastId);
		fieldScale.id = "fieldScale_" + (i - 1);
		fieldScale.name = "fieldScale_" + (i - 1);

		var dataTypeLastId = "dataType_" + (parseInt(i));
		var dataType = document.getElementById(dataTypeLastId);
		dataType.id = "dataType_" + (i - 1);
		dataType.name = "dataType_" + (i - 1);

		// isPrimKey
		var isPrimKeyLastId = "isPrimaryKey_" + (parseInt(i));
		var isPrimKey = document.getElementById(isPrimKeyLastId);
		isPrimKey.id = "isPrimaryKey_" + (i - 1);
		isPrimKey.name = "isPrimaryKey_" + (i - 1);

		var isIdentityLastId = "isIdentity_" + (parseInt(i));
		var isIdentity = document.getElementById(isIdentityLastId);
		isIdentity.id = "isIdentity_" + (i - 1);
		isIdentity.name = "isIdentity_" + (i - 1);

		var displayLenLastId = "displayLen_" + (parseInt(i));
		var displayLen = document.getElementById(displayLenLastId);
		displayLen.id = "displayLen_" + (i - 1);
		displayLen.name = "displayLen_" + (i - 1);

		var isMustFillLastId = "isMustFill_" + (parseInt(i));
		var isMustFill = document.getElementById(isMustFillLastId);
		isMustFill.id = "isMustFill_" + (i - 1);
		isMustFill.name = "isMustFill_" + (i - 1);

		var tableNoLastId = "tableNo_" + (parseInt(i));
		var tableNo = document.getElementById(tableNoLastId);
		tableNo.id = "tableNo_" + (i - 1);
		tableNo.name = "tableNo_" + (i - 1);
		}
	ipt.value = parseInt(ipt.value) - 1;
	}
}
function onChange() {
	var it = document.getElementById('count');
	for ( var i = 0; i < parseInt(it.value); i++) {
		var fieldNoId = document.getElementById("fieldNo_" + i).value;
		var length = fieldNoId.length;
		var fieldLast;
		if (length > 3) {
			fieldLast = fieldNoId.substr(length - 3, length - 1);
		} else {
			fieldLast = fieldNoId;
		}
		document.getElementById("fieldNo_" + i).value = document
				.getElementById("tableNo").value
				+ fieldLast;
	}
}

function onChTableName(){
	var tableName = document.getElementById("tableName").value;
	var className = document.getElementById("className").value;
	var str = tableName;
	var strs = tableName.split("_");
	var result = "";
	for(var i = 0;i < strs.length; i++){
	  var strt = strs[i].substr(0,1).toUpperCase()+strs[i].substr(1).toLowerCase();
	  result += strt;
	}
	//var update = str.substr(0,1).toUpperCase()+str.substr(1);
//	var chart;
///	if(update.indexOf("_")!=-1){
	//	chart = "YH"+update.substr(0,update.indexOf("_"))+update.substr(update.indexOf("_")+1,update.length-1);
//	}else{
	//	chart= "YH"+update;
//	}
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
 // var update = str.substr(0,1).toUpperCase()+str.substr(1);
 // alert(update);
 // var chart;
 // if(update.indexOf("_")!=-1){
  //  var endStr = update.substr(update.indexOf("_")+1,update.length-1);
  //  var secStr = endStr.substr(0,1).toUpperCase()+endStr.substr(1);
  //  chart = update.substr(0,update.indexOf("_")).toLowerCase()+secStr;
 // }else{
 //   chart= update;
 // }
  var sss = result.substr(0,1).toLowerCase()+result.substr(1);
  document.getElementById("propName").value = sss;
  document.getElementById("fieldName").value = fieldName.toUpperCase();
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