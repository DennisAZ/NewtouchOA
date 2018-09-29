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
var dialog	= window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.type == 'textarea'){
    GetE('txtName').value = oActiveEl.title;
    GetE('txtValue').value = oActiveEl.innerHTML;
    var txtFontSizeFull = oActiveEl.style.fontSize;
    //var txtFontSizeFull = oActiveEl.getAttribute('style').fontSize;
    GetE('txtFontSize').value	= txtFontSizeFull.substr(0, txtFontSizeFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522
    //var txtWidthFull = oActiveEl.getAttribute('style').width;
    var txtWidthFull = oActiveEl.style.width;
    GetE('txtWidth').value = txtWidthFull.substr(0, txtWidthFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522
    //var txtHeight = oActiveEl.getAttribute('style').height;
    var txtHeightFull = oActiveEl.style.height;
    //var txtHeightFull = oActiveEl.getAttribute('style').height;
    GetE('txtHeight').value = txtHeightFull.substr(0, txtHeightFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522
  }else
    oActiveEl = null ;
    dialog.SetOkButton( true ) ;
    dialog.SetAutoSize( true ) ;
    SelectField( 'txtName' ) ;//在编辑区域设置刚创建的控件为选中状态
}

function Ok(){  //把表单插入到fck的工作区当中
  var txtName = GetE('txtName');
  if(txtName.value==""){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique(GetE('txtName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  var reg1 = /[^\d]/g;
  str = GetE('txtFontSize').value;
  if (str && str.match(reg1)) {
    alert("字体大小只能为数字！");
    return ;
  }
  str = GetE('txtWidth').value;
  if (str && str.match(reg1)) {
    alert("输入框宽度只能为数字！");
    return ;
  }
  str = GetE('txtHeight').value;
  if (str && str.match(reg1)) {
    alert("输入框高度只能为数字！");
    return ;
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oEditor.FCKUndo.SaveUndoStep();
  oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'TEXTAREA', {title: GetE('txtName').value,id:idtr ,name:idtr ,src:contextPath + "<%=moduleContextPath %>/flowform/editor/plugins/NTextArea/textarea.gif"} ) ;
  if(oEditor.FCKBrowserInfo.IsIE)
    oActiveEl.value = GetE('txtValue').value;
  else
    oActiveEl.setAttribute('value', GetE('txtValue').value);
  if(GetE('txtWidth').value){
    oActiveEl.style.width = GetE('txtWidth').value + 'px';
  }
  if(GetE('txtHeight').value){
    oActiveEl.style.height = GetE('txtHeight').value + 'px';
  }
  if(GetE('txtFontSize').value){
    oActiveEl.style.fontSize = GetE('txtFontSize').value + 'px';
  }
	//var bIsChecked = GetE('txtAlign').checked ;
	//SetAttribute( oActiveEl, 'checked', bIsChecked ? 'checked' : null ) ;	// For Firefox
	//oActiveEl.checked = bIsChecked ;
  return true ;
}
</script>
</head>
<body style="overflow: hidden ;font-size:12pt">
		<table height="100%" width="100%">
			<tr>
				<td align="center">
					<table border="0" cellpadding="0" cellspacing="0" width="80%">
						<tr>
							<td>
								控件名称<br>
								<input type="text" id="txtName" size="10">
							</td>
							<td width="25%">&nbsp;</td>
							<td>
								字体大小<br>
								<input id="txtFontSize" type="text" size="10">
							</td>
						</tr>
						<tr>
							<td>
								控件宽度<br>
								<input id="txtWidth" type="text" size="10">
						  </td>
							<td width="20%">&nbsp;</td>
						  <td>
								控件高度<br>
								<input id="txtHeight" type="text" size="10">
							</td>
						</tr>
						<tr>
							<td  colspan="3">
								默认值<br>
								<textarea id="txtValue" style="WIDTH: 96%" rows=4></textarea>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>