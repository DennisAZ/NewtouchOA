<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="/yh/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>Insert title here</title>
<script type="text/javascript">
var dialog	= window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'INPUT' && oActiveEl.type == 'checkbox' ){
		//GetE('txtName').value		= oActiveEl.title ;
		//GetE('txtValue').value		= oEditor.FCKBrowserInfo.IsIE ? oActiveEl.value : GetAttribute( oActiveEl, 'value' ) ;
		//GetE('txtSelected').checked	= oActiveEl.checked ;
    GetE('itemName').value = oActiveEl.title;
    if(oActiveEl.getAttribute('checked') == "checked"){
      GetE('itemValue').value = oActiveEl.getAttribute('checked');
    }else{
      GetE('itemValue').selectedIndex = oActiveEl.getAttribute('checked');
    }
    //GetE('itemValue').selectedIndex = oActiveEl.getAttribute('checked');
  }else
    oActiveEl = null;
    dialog.SetOkButton(true);
    dialog.SetAutoSize(true);
    SelectField('itemName');
}

function Ok(){
  var itemName = GetE('itemName');
  if(itemName.value == ""){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique(GetE('itemName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
　oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'INPUT', {title: GetE('itemName').value, id:idtr, name:idtr, type: 'checkbox'});
  oActiveEl.removeAttribute("value");
  if(GetE('itemValue').value == ""){
    oActiveEl.checked = false;
  }else{
    oActiveEl.checked = GetE('itemValue').value;
    //SetAttribute(oActiveEl, 'checked', GetE('itemValue').value);
  }
	//var bIsChecked = GetE('txtSelected').checked ;
	//SetAttribute( oActiveEl, 'checked', bIsChecked ? 'checked' : null ) ;	// For Firefox
	//oActiveEl.checked = bIsChecked ;
	return true;
}
</script>
</head>
<body style="overflow: hidden ;font-size:12pt">
<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table cellspacing="3" cellpadding="0" border="0">
          <tr>
            <td nowrap>控件名称：            </td>
            <td nowrap>
               <Input id="itemName" type="text" size="20">
            </td>
          </tr>
          <tr>
            <td nowrap>默认值：
            </td>
            <td nowrap align=left>
              <select id="itemValue">
                <option value="">不选中
                <option value="checked">选中
              </select>
            </td>
          </tr>
				</table>
			</td>
		</tr>
</table>
</body>
</html>
