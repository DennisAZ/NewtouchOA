<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
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
    GetE('itemName').value	= oActiveEl.value;
    GetE('itemList').value	= oActiveEl.dataFld;
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
  //var control_Gtml="<img src=\"sign.gif\" class=\"SIGN\" align=absmiddle border=0 style=\"cursor:pointer\" value=\""+ITEM_NAME.value.replace("\"","&quot;")+"\" title=\"签章控件："+ITEM_NAME.value.replace("\"","&quot;")+"\" dataFld=\""+ITEM_LIST.value+"\">";
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'IMG', {title: '签章控件:' + GetE('itemName').value,id:idtr ,name:idtr ,src:"<%=contextPath%><%=moduleContextPath %>/flowform/editor/plugins/NSign/sign.gif"});
    
  SetAttribute(oActiveEl, 'value', itemName.value.replace("\"","&quot;"));
  SetAttribute(oActiveEl, 'border', 0);
  SetAttribute(oActiveEl, 'align', 'absMiddle');
  SetAttribute(oActiveEl, 'class', 'SIGN');
  SetAttribute(oActiveEl, 'className', 'SIGN');
  SetAttribute(oActiveEl, 'dataFld', itemList.value);
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
      <td nowrap align=right>控件名称：
      </td>
      <td nowrap>
      <Input name="itemName" id="itemName" type="text" class="SmallInput" size="30">
      </td>
  </tr>
  <tr class="TableContent">
      <td nowrap align=right>验证锁定字段<br>(用,号分隔)：
      </td>
      <td nowrap>
       <textarea name="itemList" id="itemList" class="SmallInput" cols="28" rows="4" title="不填写则不验证"></textarea>
      </td>
  </tr>
</table>
</body>
</html>
