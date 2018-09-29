<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<title>表单数据控件设定</title>
<script type="text/javascript">
var dialog	= window.parent ;//应该是对话框外壳所对应的窗口 by dq 090520
var oEditor = dialog.InnerDialogLoaded() ;//获得最外层界面所对应的window，该函数在对话框外壳程序中(fckdialog.html)定义 by dq 090520

var parent_window = oEditor;
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement() ;

//-- 获取控件属性 --
var edit_flag1=0;
var g_dataSrcFetch="";

window.onload = function() {
  //新版表单设计器 by dq 090526
  var FCK = parent_window.FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  var parent_doc = FCK.EditingArea.Window.document;

  FCK.EditingArea.Focus();
  getDataSrc();
  if (oActiveEl && oActiveEl.tagName == "BUTTON")
  {
  	edit_flag1=1;
  	ITEM_NAME.value = oActiveEl.getAttribute("title");
  	var dataSrc =  oActiveEl.getAttribute("DATA_TABLE");
  	$('dataSrc').value = dataSrc;
  	g_dataSrcFetch = dataSrc;
  	getData(dataSrc);
  	var dataField = oActiveEl.getAttribute("DATA_FIELD");
  	var dataFieldName = oActiveEl.getAttribute("DATA_FLD_NAME");
  	var dataControl = oActiveEl.getAttribute("DATA_CONTROL");

  	var dataField_arr = dataField.split("`");
  	var dataFieldName_arr = dataFieldName.split("`");
  	var dataControl_arr = dataControl.split("`");
  	
  	var newRow;
  	for (i=0; i<dataField_arr.length-1; i++)
  	{
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
	     cell.innerHTML = '<a href="#" onclick="del(this)">删除</a>';	 	        
  	}
  }
  ITEM_NAME.focus();

	dialog.SetOkButton( true ) ;//该函数在fckdialog.html 683行定义 by dq 090520
	dialog.SetAutoSize( true ) ;//该函数在fckdialog.html 213行定义 by dq 090520
}

function Ok()
{
  if(ITEM_NAME.value=="")
  {
     alert("控件名称不能为空");
     return;
  }
  if(oActiveEl == null && !checkUnique(ITEM_NAME.value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  //var parent_window = parent.dialogArguments;

  var dataSrc = $('dataSrc').value;
  var dataField='';
  var dataFieldName='';
  var dataControl=''; 
  //遍历数据项
  for(var i=1;i<$('map_tbl').rows.length;i++)
  {
  	var row = $('map_tbl').rows[i];
    dataField += row.cells[0].innerHTML + "`" ;
    dataFieldName += row.cells[1].innerHTML + "`" ;
    dataControl += row.cells[2].innerHTML + "`" ;
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{
    idtr = oActiveEl.name;
  }
  var item_name = ITEM_NAME.value.replace("\"","&quot;")
  var control_html="<button name=\""+ idtr +"\"  title=\""+item_name+"\" class=\"FETCH\" DATA_TABLE=\""+dataSrc+"\" DATA_FIELD=\""+dataField+"\" DATA_FLD_NAME=\""+dataFieldName+"\" DATA_CONTROL=\""+dataControl+"\">"+item_name+"</button>";

  //为“撤销”操作做准备，记录当前编辑区域的状态（猜的） by dq 090527
  oEditor.FCKUndo.SaveUndoStep() ;
  
  //新版表单设计器程序 by dq 090526
  var FCK = parent_window.FCKeditorAPI.GetInstance('FORM_CONTENT'); //获得表单设计器的顶层对象FCK，该方法定义位置fckeditorapi.js第47行 by dq 090521
  var parent_doc = FCK.EditingArea.Window.document;  
  FCK.EditingArea.Focus();

  if(edit_flag1==1)
  {
    //如果是编辑，先把选中的控件删掉 by dq 090526
    FCK.Selection.Delete();

    var sel = parent_doc.selection.createRange();
    sel.pasteHTML(control_html);
  }
  else {
    //parent_window.EDIT_HTML.insertHtml(control_html); 老版本表单设计器程序  by dq 090526
    var sel = parent_doc.selection.createRange();
    sel.pasteHTML(control_html);
  }
  dialog.CloseDialog();
}

function mytip()
{
  if(cal_tip.style.display=="none")
    cal_tip.style.display="";
  else
    cal_tip.style.display="none";   
}

function getData(val)
{
  if(val != ''){
    $('dataField').update("<option value=\"\">选择数据来源</option>");
    $('itemTitle').value = "";
    var url = contextPath + "/yh/core/funcs/workflow/act/YHFormDataFetch/getData.act";
    var json = getJsonRs(url , "dataSrc=" + val);
    if (json.rtState == "0") {
      ss = json.rtData;
      for (var i = 0 ;i < ss.length ;i++) {
        var k = ss[i];
        var op = new Element("option");
        op.value = k.itemId;
        op.innerHTML = k.title;
        $('dataField').appendChild(op);
      }
      $('dataMapDiv').show();
    }
  }else{
    $('dataMapDiv').hide();
  }
}

function add()
{
	var mytable = $('map_tbl');
	var fieldObj =$('dataField');
	var field = fieldObj.value;
	var fieldName=fieldObj.options[fieldObj.selectedIndex].text;
	
  var dataSrc = $('dataSrc').value;
  if(g_dataSrcFetch != dataSrc && g_dataSrcFetch!="")
  {
    var msg = "数据来源只能选择一种，变更数据来源，您之前的映射项将被清除，是否继续？";
    if(!window.confirm(msg))
      return;
    else
    {
    	for(var i=1;i < mytable.rows.length;i++)
    	  mytable.deleteRow(mytable.rows[i].rowIndex);
    }
  }
  
	if(field=="")
	{
		alert("请选择数据库字段");
		return;
	}	
	var control=$('itemTitle').value;
	if(control=="")
	{
		alert("请填写映射控件名称");
		return;
	}

  for(var i=1;i<mytable.rows.length;i++)
  {
    var dataField = mytable.rows[i].cells[0].innerHTML;    
	  if(dataField==field)
	  {
		  alert("已经添加！");
		  return;
	  }
	}  
	
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
  cell.innerHTML = '<a href=\"#\" onclick="del(this)">删除</a>';  
}

function del(obj)
{
  var del_tr=obj.parentNode.parentNode;
  if(isMoz){
    del_tr.parentNode.removeChild(del_tr);
  } else {
    del_tr.removeNode(true);
  }
}	
function getDataSrc(val){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFormDataFetch/getDataConfig.act";
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var list = json.rtData;
    for (var i = 0 ;i < list.length ;i++) {
      var k = list[i];
      var op = new Element("option");
      op.value = k.formId;
      op.innerHTML = k.flowName;
      if (k.formId == val) {
        op.setAttribute("selected" , true);
      }
      $('dataSrc').insert(op);
    }
  }
}
</script>
</head>

<body topmargin="2" leftmargin="2">

<table border="0" width="100%" class="TableList" style="border-bottom:0" align="center">
  <tr class="TableContent">
    <td nowrap>控件名称：</td>
    <td>
    	<input type="text" class="SmallInput" name="ITEM_NAME" size=20>
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
—<input type="text" name="itemTitle" id="itemTitle" size=10> 
<a href="javascript:;" class="orgAdd" onclick="add();">添加</a></div>
    </td>
  </tr>
  <tr class="TableControl" style = "display:none">
  	<td nowrap colspan=2 align="center">
  		<input type="button" onclick="my_submit();" value="确定" class="SmallButton">
  	</td>
  </tr> 
</table> 
<table id="map_tbl" border="0" width="100%" class="TableList" align="center">  
  <tr class="TableHeader">
      <td nowrap align="center">数据库字段</td>
      <td nowrap align="center">字段名称</td>
      <td nowrap align="center">映射控件名称</td>
      <td nowrap align="center">操作</td>
  </tr>
</table>
</body>
</html>