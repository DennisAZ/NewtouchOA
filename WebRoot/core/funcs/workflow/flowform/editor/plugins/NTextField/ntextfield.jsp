<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>单行输入框</title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'INPUT'&& oActiveEl.type == 'text'){
    GetE('txtName').value = oActiveEl.title;
    GetE('txtValue').value = oActiveEl.value;
    var txtFontSizeFull = oActiveEl.style.fontSize;
    //var txtFontSizeFull = oActiveEl.getAttribute('style').fontSize;
    GetE('txtFontSize').value = txtFontSizeFull.substr(0, txtFontSizeFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522
    GetE('txtMax').value = GetAttribute(oActiveEl, 'maxLength');
    //var txtSizeFull = oActiveEl.getAttribute('style').width;
    var txtSizeFull = oActiveEl.style.width;
    GetE('txtSize').value = txtSizeFull.substr(0, txtSizeFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522
    //var txtHeight = oActiveEl.getAttribute('style').height;
    var txtHeight = oActiveEl.style.height;
    GetE('txtHeight').value = txtHeight.substr(0, txtHeight.length - 2);
    GetE('txtAlign').value = oActiveEl.style.textAlign;
    //GetE('txtAlign').value = oActiveEl.getAttribute('style').textAlign;
    var HIDDEN = oActiveEl.getAttribute('HIDDEN');
    if(HIDDEN == '1')
        GetE('HIDDEN').checked	= true;
    else
        HIDDEN = 0;

  SetAttribute( oActiveEl, 'HIDDEN', HIDDEN ) ;
   item_id = oActiveEl.getAttribute('name').substr(5);
  }else
    oActiveEl = null ;
  dialog.SetOkButton(true) ;
  dialog.SetAutoSize(true) ;
  SelectField('txtName') ;//在编辑区域设置刚创建的控件为选中状态}

function Ok(){  
  var txtName = GetE('txtName');
  if(txtName.value　==　""){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique(GetE('txtName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var reg1 = /[^\d]/g;
  var str = GetE('txtMax').value;
  if (str && str.match(reg1)) {
    alert("最多字符数只能为数字！");
    return ;
  }
  str = GetE('txtFontSize').value;
  if (str && str.match(reg1)) {
    alert("字体大小只能为数字！");
    return ;
  }
  str = GetE('txtSize').value;
  if (str && str.match(reg1)) {
    alert("输入框宽度只能为数字！");
    return ;
  }
  str = GetE('txtHeight').value;
  if (str && str.match(reg1)) {
    alert("输入框高度只能为数字！");
    return ;
  }
  //把表单插入到fck的工作区当中
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'INPUT', {title: GetE('txtName').value,id:idtr ,name:idtr});
  //if(oEditor.FCKBrowserInfo.IsIE)
    //oActiveEl.value = GetE('txtValue').value;
 // else
    SetAttribute(oActiveEl, 'value', GetE('txtValue').value);
    SetAttribute(oActiveEl, 'maxlength', GetE('txtMax').value);
    SetAttribute(oActiveEl, 'align', GetE('txtAlign').value);
  if(GetE('txtFontSize').value != ""){
    oActiveEl.style.fontSize = GetE('txtFontSize').value + 'px';//加上px可能在firefox也会正确显示 by dq 090522
   }
  if(GetE('txtAlign').value != ""){
    oActiveEl.style.textAlign = GetE('txtAlign').value;
  }
  if(GetE('txtSize').value != ""){
    oActiveEl.style.width = GetE('txtSize').value + 'px';
  }
  if(GetE('txtHeight').value != ""){
    oActiveEl.style.height=GetE('txtHeight').value + 'px';
  }
  if(GetE('HIDDEN').checked)
    SetAttribute( oActiveEl, 'HIDDEN', '1' ) ;
  else
    SetAttribute( oActiveEl, 'HIDDEN', '0' ) ;
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
						<td>
							<span fcklang="DlgTextName">输入框名称</span><br />
							<input id="txtName" type="text" size="10" />
						</td>
						<td width="20%">&nbsp;</td>
						<td>
							<span fcklang="DlgImgAlign">对齐方式</span><br />
							<select id="txtAlign">
								<option value="left" fcklang="DlgTableAlignLeft">Left</option>
								<option value="center" fcklang="DlgTableAlignCenter">Center</option>
								<option value="right" fcklang="DlgTableAlignRight">Right</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<span fcklang="DlgTextSize">字体大小</span><br />
							<input id="txtFontSize" type="text" size="10" />
						</td>
						<td width="20%">&nbsp;</td>
						<td>
							<span fcklang="DlgTextMaxChars">最多字符数</span><br />
							<input id="txtMax" type="text" size="10" />
						</td>
					</tr>
					<tr>
						<td>
							输入框宽度<br />
							<input id="txtSize" type="text" size="10" />
						</td>
							<td width="20%">&nbsp;</td>
						<td>
							输入框高度<br />
							<input id="txtHeight" type="text" size="10">
						</td>
					</tr>
					<tr>
						<td>
							<span fcklang="DlgTextValue">默认值</span><br />
							<input id="txtValue" type="text" size="10"/>
						</td>
            <td width="20%">&nbsp;</td>
            <td>
              <span>隐藏</span><br />
              <input id="HIDDEN" type="checkbox"  />
            </td>
					</tr>				
				</table>
			</td>
		</tr>
</table>
</body>
</html>