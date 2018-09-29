<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>Insert title here</title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'IMG'){
    GetE('itemName').value = oActiveEl.getAttribute('value');
    GetE('selectType').value = oActiveEl.getAttribute('type');
  }else
    oActiveEl = null;
    dialog.SetOkButton(true);
    dialog.SetAutoSize(true);
    SelectField('itemName');//在编辑区域设置刚创建的控件为选中状态
}
function Ok(){  //把表单插入到fck的工作区当中
  var itemName = GetE('itemName');
  var selectType = GetE('selectType');
  if(itemName.value == ""){
    alert("控件名称不能为空");
    return;
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("OTHER",oDOM);
    idtr = 'OTHER_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'img', {title: '部门人员控件:' + itemName.value.replace("\"","&quot;"),id:idtr,name:idtr});
  SetAttribute(oActiveEl, 'src', '<%=contextPath%>/core/funcs/workflow/flowform/editor/plugins/NUser/user.gif' ) ;
  SetAttribute(oActiveEl, 'class', 'USER');
  SetAttribute(oActiveEl, 'className', 'USER');
  SetAttribute(oActiveEl, 'align', 'absMiddle');
  SetAttribute(oActiveEl, 'border', 0);
  SetAttribute(oActiveEl, 'type', selectType.value);
  SetAttribute(oActiveEl, 'value', itemName.value.replace("\"","&quot;"));
  oActiveEl.style.cursor = 'hand';
	//var bIsChecked = GetE('txtAlign').checked ;
	//SetAttribute( oActiveEl, 'checked', bIsChecked ? 'checked' : null ) ;	// For Firefox
	//oActiveEl.checked = bIsChecked ;
  return true ;
}
</script>
</head>
<body style="overflow: hidden ;font-size:12pt">
<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table cellspacing="3" cellpadding="0" border="0">
          <tr>
              <td nowrap><b>对应的输入框控件名称</b>：
              </td>
              <td nowrap align=left>
               <Input id="itemName" type="text" size="20">
              </td>
          </tr>
          <tr>
              <td nowrap><b>选择类型</b>：
              </td>
              <td nowrap align=left>
               <select id="selectType">
               	  <option value="0" selected>选择人员</option>
               	  <option value="1">选择部门</option>
               </select>
              </td>
          </tr>
          <tr>
              <td colspan="2"  align=left>
              	<div style="font-size: 10pt;font-family:宋体;color:blue;word-wrap:break-word;width:280px;text-align=left" >
                  <b>说明：</b>选择的人员将回填到该输入框中，可配合自动选人规则中的“从表单字段选择”功能使用。
                </div>
              </td>
          </tr>
				</table>
			</td>
		</tr>
</table>
</body>
</html>