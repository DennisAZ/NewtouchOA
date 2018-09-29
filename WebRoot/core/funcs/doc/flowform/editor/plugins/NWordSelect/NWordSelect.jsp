<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
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
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'img', {title: '主题词选择控件:' + itemName.value.replace("\"","&quot;"),id:idtr,name:idtr});
  SetAttribute(oActiveEl, 'src', '<%=contextPath%><%=moduleContextPath %>/flowform/editor/plugins/NWordSelect/word.gif' ) ;
  SetAttribute(oActiveEl, 'class', 'WORDSELECT');
  SetAttribute(oActiveEl, 'className', 'WORDSELECT');
  SetAttribute(oActiveEl, 'align', 'absMiddle');
  SetAttribute(oActiveEl, 'border', 0);
  SetAttribute(oActiveEl, 'type', selectType.value);
  SetAttribute(oActiveEl, 'value', itemName.value.replace("\"","&quot;"));
  oActiveEl.style.cursor = 'hand';
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
              <td nowrap><b>选择分割符</b>：              </td>
              <td nowrap align=left>
               <select id="selectType">
               	  <option value="0" selected>逗号</option>
               	  <option value="1">空格</option>
               </select>
              </td>
          </tr>
				</table>
			</td>
		</tr>
</table>
</body>
</html>