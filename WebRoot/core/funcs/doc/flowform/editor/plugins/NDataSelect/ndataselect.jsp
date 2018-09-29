<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title></title>
<script type="text/javascript">
var dialog	= window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
//-- 获取控件属性 --
var edit_flag = 0;
var g_dataSrc = "";
var parent_window = oEditor;
window.onload = function(){
  var FCK = parent_window.FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  var parent_doc = FCK.EditingArea.Window.document;
  oEditor.FCKLanguageManager.TranslatePage(document);
  FCK.EditingArea.Focus();

  getDataSrc();
  if(oActiveEl && oActiveEl.tagName == 'BUTTON'){
    edit_flag = 1;
    ITEM_NAME.value = oActiveEl.getAttribute("title");
  	var dataSrc =  oActiveEl.getAttribute("data_table");
  	$('dataSrc').value = dataSrc;
  	g_dataSrc = dataSrc;
  	getData(dataSrc);
    //数据输入方式 added by lx 20100518
  	var dataType = oActiveEl.getAttribute("data_type");
  	if(!dataType)
  		dataType = "0";
  	$('dataType'+dataType.toString()).setAttribute("checked",true);
  	var dataField = oActiveEl.getAttribute("data_field");
  	var dataFieldName = oActiveEl.getAttribute("data_fld_name");
  	var dataControl = oActiveEl.getAttribute("data_control");
  	var dataField_arr = dataField.split("`");
  	var dataFieldName_arr = dataFieldName.split("`");
  	var dataControl_arr = dataControl.split("`");
  	var dataQuery = oActiveEl.getAttribute("data_query");
  	if(dataQuery) 
  	  var dataQuery_arr = dataQuery.split("`");
  	
  	  var newRow;
  	  for(i = 0; i < dataField_arr.length - 1; i++){
  	    if(dataQuery_arr) 
  		  var isQuery = dataQuery_arr[i] == '1' ? "是":"否";
  		else
  		  isQuery = "否";
	      newRow = $("map_tbl").insertRow(-1);
	      newRow.setAttribute('className',"TableContent");
	      newRow.setAttribute('align',"center");
	     
	      var cell = newRow.insertCell(-1); 
	      cell.innerHTML = dataField_arr[i];
	      cell = newRow.insertCell(-1); 
	      cell.innerHTML = dataFieldName_arr[i];	     
	      cell = newRow.insertCell(-1); 
	      cell.innerHTML = dataControl_arr[i];	 
	      cell = newRow.insertCell(-1); 
	      cell.innerHTML = isQuery;	  	 	  
	      cell = newRow.insertCell(-1); 
	      cell.innerHTML = '<a href="#" onclick="del(this)">删除</a>';	 	        
  	  }
	}
	ITEM_NAME.focus();
	//oActiveEl = null ;
	dialog.SetOkButton( true );
	dialog.SetAutoSize( true );
	//SelectField( 'txtName' ) ;
}
function Ok(){
  var ITEM_NAME = $('ITEM_NAME');
  if(ITEM_NAME.value == ""){
     alert("控件名称不能为空");
     return;
  }
  if(oActiveEl == null && !checkUnique($('ITEM_NAME').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var dataSrc = $('dataSrc').value;
  var dataField = '';
  var dataFieldName = '';
  var dataControl = '';
  var dataQuery = ''; 
//遍历数据项  for(var i = 1; i < $('map_tbl').rows.length; i++){
  	var row = $('map_tbl').rows[i];
    dataField += row.cells[0].innerHTML + "`";
    dataFieldName += row.cells[1].innerHTML + "`";
    dataControl += row.cells[2].innerHTML + "`";
    if(row.cells[3].innerHTML == '是') 
       queryFlag = '1';
    else 
       queryFlag = '0'; 
    dataQuery += queryFlag + "`";  //查询字段
  }
  var dataType = $('dataType0').checked==true? 0:1;
  if(dataType=="1" && dataQuery.replace("`","").indexOf("1")<0)
  {
  	alert("必须设置查询主键！");
  	return;
  }
  var item_name = ITEM_NAME.value.replace("\"","&quot;");
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  
  var control_html = "<button DATA_TYPE=\""+dataType+"\"  name=\""+ idtr +"\" id=\""+ idtr +"\" title=\"" + item_name + "\" class=\"DATA\" DATA_TABLE=\"" + dataSrc + "\" DATA_FIELD=\"" + dataField + "\" DATA_FLD_NAME=\"" + dataFieldName + "\" DATA_CONTROL=\"" + dataControl + "\" DATA_QUERY=\"" + dataQuery + "\">" + item_name + "</button>";
  //为“撤销”操作做准备，记录当前编辑区域的状态（猜的） by dq 090527
  oEditor.FCKUndo.SaveUndoStep();
  //新版表单设计器程序 by dq 090526
  var FCK = parent_window.FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  var parent_doc = FCK.EditingArea.Window.document;  
  FCK.EditingArea.Focus();
  if(edit_flag == 1){
    //如果是编辑，先把选中的控件删掉 by dq 090526
    FCK.Selection.Delete();
    var sel = parent_doc.selection.createRange();
    sel.pasteHTML(control_html);
  }else{
    //parent_window.EDIT_HTML.insertHtml(control_html); 老版本表单设计器程序  by dq 090526
    var sel = parent_doc.selection.createRange();
    sel.pasteHTML(control_html);
  }
  dialog.CloseDialog();
}

function mytip(){
  if(cal_tip.style.display == "none"){
    cal_tip.style.display = "";
  }else{
    cal_tip.style.display = "none";
  }   
}

function getData(val){
  if(val != ''){
    $('dataField').update("<option value=\"\">选择数据来源</option>");
    $('itemTitle').value = "";
    $('isQuery').checked = false;
    var ss = null;
    if (allList[val]) {
      ss = allList[val].CONTENT;
    } else {
      var url = contextPath + "<%=moduleSrcPath %>/act/YHFormDataSelect/getData.act";
      var json = getJsonRs(url , "dataSrc=" + val);
      if (json.rtState == "0") {
        ss = json.rtData;
      }
    }
    for (var k in ss) {
      var op = new Element("option");
      op.value = k;
      op.innerHTML =ss[k];
      $('dataField').appendChild(op);
    }
    $('dataMapDiv').show();
  }else{
    $('dataMapDiv').hide();
  }
}

function add(){
  var mytable = $('map_tbl');
  var fieldObj =$('dataField');
  var field = fieldObj.value;
  var fieldName = fieldObj.options[fieldObj.selectedIndex].text;
  var dataSrc = $('dataSrc').value;
  if(g_dataSrc != dataSrc && g_dataSrc != ""){
    var msg = "数据来源只能选择一种，变更数据来源，您之前的映射项将被清除，是否继续？";
    if(!window.confirm(msg))
      return;
    else{
      for(var i = 1;i < mytable.rows.length; i++)
        mytable.deleteRow(mytable.rows[i].rowIndex);
    }
  }
  if(field == ""){
    alert("请选择数据库字段");
    return;
  }	
  var control = $('itemTitle').value;
  if(control == ""){
    alert("请填写映射控件名称");
    return;
  }
  for(var i = 1;i < mytable.rows.length; i++){
    var dataField = mytable.rows[i].cells[0].innerHTML;    
	if(dataField == field){
      alert("已经添加！");
      return;
	}
  }
  var queryFlag = $("isQuery").checked == true ? "1" : "0"; 
  var queryFlagDesc = queryFlag == "1" ? "是" : "否";

	//add a row
  var newRow = $("map_tbl").insertRow(-1);
  newRow.setAttribute('className',"TableContent");
  newRow.setAttribute('align',"center");

  var cell = newRow.insertCell(-1); 
  cell.innerHTML = field;
  cell = newRow.insertCell(-1); 
  cell.innerHTML = fieldName;	     
  cell = newRow.insertCell(-1); 
  cell.innerHTML = control;	 
  cell = newRow.insertCell(-1); 
  cell.innerHTML = queryFlagDesc;	  	 	  
  cell = newRow.insertCell(-1); 
  cell.innerHTML = '<a href=\"#\" onclick="del(this)">删除</a>';  
}
	
function del(obj){
  var del_tr = obj.parentNode.parentNode;
  if(isMoz){
    del_tr.parentNode.removeChild(del_tr);
  }else
    del_tr.removeNode(true);
}	
var content = null;
var allList = null;
function getDataSrc(val){
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFormDataSelect/getDataConfig.act";
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var list = json.rtData;
    allList = list;
    for (var k in list) {
      var op = new Element("option");
      op.value = k;
      op.innerHTML =list[k].NAME;
      if (k == val) {
        op.setAttribute("selected" , true);
        content =list[k].CONTENT;
      }
      $('dataSrc').insert(op);
    }
    list = json.selfDef;
    for (var k in list) {
      var op = new Element("option");
      op.value = k;
      op.innerHTML =list[k];
      if (k == val) {
        op.setAttribute("selected" , true);
      }
      $('dataSrc').insert(op);
    }
  }
  
}
</script>
</head>
<body topmargin="0">
<table border="0" width="100%" class="TableList" style="border-bottom:0" align="center">
  <tr class="TableContent">
    <td nowrap>控件名称：</td>
    <td>
    	<input type="text" class="SmallInput" name="ITEM_NAME" id="ITEM_NAME" size=20>
    </td>
  </tr>
  <tr style="display:none">
    <td nowrap class="TableContent">数据选取方式：</td>
    <td class="TableData">
  <input type="radio" name="dataType" id="dataType0" value=0 checked><label for="dataType0">弹出窗口选取</label>
    <input type="radio" name="dataType" id="dataType1" value=1><label for="dataType1">根据录入项自动关联</label>
    </td>
  </tr>
  <tr class="TableContent">
    <td nowrap>数据来源：</td>
    <td>
    	<select name="dataSrc" id="dataSrc" type="SmallSelect" onchange="javascript:getData(this.value);">
    	 <option value="">选择数据来源</option>
         </select>
    </td>
  </tr>
  <tr class="TableContent">
  	<td nowrap>添加映射关系</td>
  	<td id="dataMap"><div id="dataMapDiv" style="display:none">
<select name="dataField" id="dataField" type="SmallSelect">
  <option value="">请选择数据库字段</option>
</select>
—<input type="text" name="itemTitle" id="itemTitle" size=10> <input type="checkbox" name="isQuery" id="isQuery" title="是否作为查询字段"><label for="isQuery">查</label>
<a href="javascript:;" class="orgAdd" onclick="add();">添加</a></div>
    </td>
  </tr>
  <tr class="TableControl" style = "display:none">
  	<td nowrap colspan=2 align="center">
  		<input type="button" onclick="my_submit();" value="确定" class="SmallButton">
  	</td>
  </tr> 
</table> 
<table id="map_tbl" border="0"  width="100%" height="30px" class="TableList" align="center">  
  <tr class="TableHeader">
      <td nowrap align="center">数据库字段</td>
      <td nowrap align="center">字段名称</td>
      <td nowrap align="center">映射控件名称</td>
      <td nowrap align="center">作为查询字段</td>
      <td nowrap align="center">操作</td>
  </tr>
</table>
</body>
</html>