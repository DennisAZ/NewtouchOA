<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script src="<%=contextPath %>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>签章控件设定</title>
<script type="text/javascript">
var dialog	= window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'IMG'){
    GetE('itemName').value	= oActiveEl.getAttribute("value");
      var dataFld1 =  oActiveEl.getAttribute("dataFld");
      if (!dataFld1) {
        dataFld1 = "";
      }
    GetE('itemList').value	= dataFld1
    if(oActiveEl.getAttribute('sign_type') != null){
  		var type_arr = oActiveEl.getAttribute('sign_type').split(",");
  		for(var i = 1; i <type_arr.length; i++)
  		{
  		    if(type_arr[i-1] == 1)
  		    {
  		        GetE("SIGN_TYPE"+i).checked = true;
  		    }
  		    else
  		    {
  		        GetE("SIGN_TYPE"+i).checked = false;
  		    }
  		}
    }
    if(oActiveEl.getAttribute('sign_color') != null){
    	GetE('SIGN_COLOR').value = oActiveEl.getAttribute('sign_color') ;
    }
  }else {
    oActiveEl = null;
  }
  dialog.SetOkButton(true);
  dialog.SetAutoSize(true);
  SelectField('itemName');//在编辑区域设置刚创建的控件为选中状态，该函数在fck_dialog_common.js 第100行定义 by dq 090520
}

function Ok(){
  var itemName = GetE('itemName');
  var itemList = GetE('itemList');
  if(itemName.value == ""){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique('签章控件:' + GetE('itemName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  if(GetE("SIGN_TYPE1").checked == false && GetE("SIGN_TYPE2").checked == false)
  {
      alert("至少选择一个类型");
      return;
  }
  //var control_Gtml="<img src=\"sign.gif\" class=\"SIGN\" align=absmiddle border=0 style=\"cursor:pointer\" value=\""+ITEM_NAME.value.replace("\"","&quot;")+"\" title=\"签章控件："+ITEM_NAME.value.replace("\"","&quot;")+"\" dataFld=\""+ITEM_LIST.value+"\">";
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'IMG', {title: '签章控件:' + GetE('itemName').value,id:idtr ,name:idtr ,src:"<%=contextPath%>/core/funcs/workflow/flowform/editor/plugins/NSign/sign.gif"});
    
  SetAttribute(oActiveEl, 'value', itemName.value.replace("\"","&quot;"));
  SetAttribute(oActiveEl, 'border', 0);
  SetAttribute(oActiveEl, 'align', 'absMiddle');
  SetAttribute(oActiveEl, 'class', 'SIGN');
  SetAttribute(oActiveEl, 'className', 'SIGN');
  SetAttribute(oActiveEl, 'dataFld', itemList.value);
  SetAttribute( oActiveEl, 'SIGN_COLOR'	, GetE('SIGN_COLOR').value ) ;	//手写颜色
	var sign_type = "";
	if(GetE("SIGN_TYPE1").checked)
	{
	    sign_type += "1,";
	}
	else
	{
	    sign_type += "0,";
	}
	if(GetE("SIGN_TYPE2").checked)
	{
	    sign_type += "1,";
	}
	else
	{
	    sign_type += "0,";
	}
	SetAttribute( oActiveEl, 'sign_type'	, sign_type ) ;
  	//SetAttribute( oActiveEl, 'src', '/module/html_editor/editor/images/calendar.gif' ) ;
  oActiveEl.style.cursor = 'hand';
	//var bIsChecked = GetE('txtSelected').checked ;
	//SetAttribute( oActiveEl, 'checked', bIsChecked ? 'checked' : null ) ;	// For Firefox
	//oActiveEl.checked = bIsChecked ;
  return true;
}
</script>
</head>
<body style="overflow-x: hidden ;font-size:12pt">

<table border="0" width="100%" class="TableList" align="center">
  <tr class="TableContent">
      <td nowrap align=right>控件名称：      </td>
      <td nowrap>
      <Input name="itemName" id="itemName" type="text" class="SmallInput" size="30">
      </td>
  </tr>
  <tr class="TableContent">
      <td nowrap align=right>验证锁定字段<br>(用,号分隔)：      </td>
      <td nowrap>
       <textarea name="itemList" id="itemList" class="SmallInput" cols="28" rows="4" title="不填写则不验证"></textarea>
      </td>
  </tr>
  <tr class="TableContent" style="display:none">
  <TD noWrap>手写颜色： </TD>
<TD noWrap><SELECT id=SIGN_COLOR> <OPTION style="BACKGROUND-COLOR: red" selected value=0x0000FF>红色</OPTION> <OPTION style="BACKGROUND-COLOR: green" value=0x00FF00>绿色</OPTION> <OPTION style="BACKGROUND-COLOR: blue" value=0xFF0000>蓝色</OPTION> <OPTION style="BACKGROUND-COLOR: black" value=0x000000>黑色</OPTION> <OPTION style="BACKGROUND-COLOR: white" value=0xFFFFFF>白色</OPTION></SELECT> </TD>
  </tr>
  <tr>
  <TD noWrap>控件类型： </TD>
<TD noWrap><INPUT id=SIGN_TYPE1 value=1 CHECKED type=checkbox><label for="SIGN_TYPE1">盖章</label> <INPUT id=SIGN_TYPE2 value=2 type=checkbox CHECKED><label for="SIGN_TYPE2">手写 </label></TD>
  </tr>
</table>
</body>
</html>
