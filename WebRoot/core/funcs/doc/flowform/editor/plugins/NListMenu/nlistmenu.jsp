<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>下拉列表控件</title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument ;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  oListText = document.getElementById('cmbText');
  oListText.style.width = oListText.offsetWidth;
  if(oActiveEl && oActiveEl.tagName == 'SELECT'){
    GetE('txtName').value = GetAttribute( oActiveEl, 'title' );
    GetE('txtSelValue').value = oActiveEl.value;
    GetE('txtLines').value = GetAttribute( oActiveEl, 'size' );
    if(oActiveEl.getAttribute('child') == null){
      GetE('txtChild').value = "";
    }else{
	  GetE('txtChild').value = oActiveEl.getAttribute('child');
    }
    var txtWidthFull = oActiveEl.style.width;
    GetE('txtWidth').value = txtWidthFull.substr(0, txtWidthFull.length - 2);//这里的substr是为了去掉末尾的'px' by dq 090522

    for(var i = 0; i < oActiveEl.options.length; i++ ){
      var sText = oActiveEl.options[i].value;
      AddComboOption( oListText, sText, sText );
    }
  }else
    oActiveEl = null;
	dialog.SetOkButton( true );
	dialog.SetAutoSize( true );
	SelectField( 'txtName' );//在编辑区域设置刚创建的控件为选中状态}

function Ok(){  //把表单插入到fck的工作区当中
  var txtName = GetE('txtName');
  if(txtName.value == ''){
    alert("控件名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique(GetE('txtName').value,oDOM)){
    alert("控件名称必须唯一");
    return;
  }
  //alert(sort);
  var reg1 = /[^\d]/g;
  str = GetE('txtWidth').value;
  if (str && str.match(reg1)) {
    alert("控件宽度只能为数字！");
    return ;
  }
  str = GetE('txtLines').value;
  if (str && str.match(reg1)) {
    alert("高度只能为数字！");
    return ;
  }
  oEditor.FCKUndo.SaveUndoStep();
  var sSize = GetE('txtLines').value;
  if(sSize == null||isNaN(sSize)||sSize <= 1){
    sSize = '';
  }
  var sort ,idtr;
  if(!oActiveEl){
    sort =  getIndexOfPreName("DATA",oDOM);
    idtr = 'DATA_' + sort;
  }else{ 
    idtr = oActiveEl.name;
  }
  oActiveEl = CreateNamedElement(oEditor, oActiveEl, 'SELECT', {title: GetE('txtName').value,id:idtr ,name:idtr ,src:"<%=contextPath%><%=moduleContextPath %>/flowForm/editor/plugins/NListMenu/listmenu.gif"});
	//if(oEditor.FCKBrowserInfo.IsIE)
	 // oActiveEl.value = GetE('txtValue').value;
	//else
  var sChild = GetE('txtChild').value;
  SetAttribute(oActiveEl, 'CHILD', sChild);
  SetAttribute(oActiveEl, 'size', sSize);
  if(GetE('txtWidth').value != ""){
    oActiveEl.style.width = GetE('txtWidth').value + 'px';
	// Remove all options.
  }
  while(oActiveEl.options.length > 0){
    oActiveEl.remove(0);
  }
	// Add all available options.
  for(var i = 0; i < oListText.options.length; i++){
    var sText = oListText.options[i].value;
    if(sText.length == 0) 
	  sText = sText;
	  var oOption = AddComboOption( oActiveEl, sText, sText, oDOM);
		if(sText == GetE('txtSelValue').value){
          SetAttribute(oOption, 'selected', 'selected');
          oOption.selected = true;
		}
	}
  return true;
}

function mytip(item){
  if($(item).style.display=="none"){
    $(item).style.display="";
  }else{
    $(item).style.display="none";   
  }
}

function Move(steps){
  ChangeOptionPosition(oListText, steps);
//	ChangeOptionPosition( oListValue, steps ) ;
}

function ChangeOptionPosition(combo, steps){
  var iActualIndex = combo.selectedIndex;
  if(iActualIndex < 0)
    return ;
  var iFinalIndex = iActualIndex + steps;
  if(iFinalIndex < 0)
    iFinalIndex = 0;
  if(iFinalIndex > (combo.options.length - 1))
    iFinalIndex = combo.options.length - 1;
  if(iActualIndex == iFinalIndex)
    return ;
  var oOption = combo.options[iActualIndex];
  var sText = HTMLDecode(oOption.innerHTML);
    //	var sValue	= oOption.value ;
  combo.remove(iActualIndex);
  oOption = AddComboOption(combo, sText, sText, null, iFinalIndex);
  oOption.selected = true;
}

function Delete(){
  RemoveSelectedOptions(oListText);
//	RemoveSelectedOptions( oListValue ) ;
}
function RemoveSelectedOptions(combo){
	// Save the selected index
  var iSelectedIndex = combo.selectedIndex;
  var oOptions = combo.options ;
	// Remove all selected options
  for(var i = oOptions.length - 1; i >= 0; i--){
    if(oOptions[i].selected) combo.remove(i);
  }
	// Reset the selection based on the original selected index
  if(combo.options.length > 0){
  if(iSelectedIndex >= combo.options.length) iSelectedIndex = combo.options.length - 1;
    combo.selectedIndex = iSelectedIndex;
  }
}

function Add(){
  var oTxtText = document.getElementById("txtText");
  AddComboOption(oListText, oTxtText.value, oTxtText.value);
  oListText.selectedIndex = oListText.options.length - 1;
  oTxtText.value	= '';
  oTxtText.focus();
}

function AddComboOption(combo, optionText, optionValue, documentObject, index){
  var oOption;
  if(documentObject)
    oOption = documentObject.createElement("OPTION");
  else
    oOption = document.createElement("OPTION");
  if(index != null)
    combo.options.add(oOption, index);
  else
    combo.options.add(oOption);
    oOption.innerHTML = optionText.length > 0 ? HTMLEncode(optionText) : '&nbsp;';
    oOption.value = optionValue;
  return oOption;
}

function SetSelectedValue(){
  var iIndex = oListText.selectedIndex;
  if(iIndex < 0) return;
  var oTxtText = document.getElementById("txtSelValue");
  oTxtText.value = oListText.options[iIndex].value;
}

function HTMLEncode(text){
  if(!text)
    return '';
  text = text.replace(/&/g, '&amp;');
  text = text.replace(/</g, '&lt;');
  text = text.replace(/>/g, '&gt;');
  return text ;
}

function HTMLDecode(text){
  if(!text)
    return '' ;
  text = text.replace(/&gt;/g, '>');
  text = text.replace(/&lt;/g, '<');
  text = text.replace(/&amp;/g, '&');
  return text ;
}

function Modify(){
  var iIndex = oListText.selectedIndex;
  if(iIndex < 0) return ;
  var oTxtText = document.getElementById("txtText") ;
	//var oTxtValue	= document.getElementById( "txtValue" ) ;
  oListText.options[iIndex].innerHTML = HTMLEncode(oTxtText.value);
  oListText.options[iIndex].value = oTxtText.value;
	//oListValue.options[ iIndex ].innerHTML	= HTMLEncode( oTxtValue.value ) ;
	//oListValue.options[ iIndex ].value		= oTxtValue.value ;
  oTxtText.value = '';
	//oTxtValue.value	= '' ;
  oTxtText.focus();
}

function Select(combo){
  var iIndex = combo.selectedIndex;
  oListText.selectedIndex = iIndex;
  var oTxtText = document.getElementById("txtText");
  oTxtText.value = oListText.value;
}
</script>
</head>
<body style="overflow: hidden ;font-size:12pt">
		<table width="92%" height="100%">
			<tr>
				<td>
					<table width="100%">
						<tr>
							<td nowrap>控件名称</td>
							<td width="100%" colSpan="3">
								<input id="txtName" style="WIDTH: 98%" type="text">
							</td>
						</tr>
						<tr>
							<td nowrap>关联子菜单名称</td>
							<td width="100%"  colSpan="3">
             <input id="txtChild" type="text" class="SmallInput" size="20">
               <a href="#" onClick="mytip('tip')">说明</a>
                <div id="tip" style="display:none"><b>说明：</b>若关联子菜单，需要子下拉菜单设置的时候在每个选项后加上特殊标记以记录与父菜单关系，形如“子菜单项目|父菜单项目”，则父菜单发生变化，子菜单会随之自动刷新筛选。</div>
               </td>
             </tr>
						<tr>
							<td nowrap>初始选定</td>
							<td width="100%" colSpan="3"><input id="txtSelValue" style="WIDTH: 96%; BACKGROUND-COLOR: buttonface" type="text" readonly></td>
						</tr>
						<tr>
							<td nowrap>控件高度</td>
							<td nowrap>
								<input id="txtLines" type="text" size="10" value="">&nbsp;行
							</td>
							<td nowrap>控件宽度
								<input id="txtWidth" type="text" size="10" value="">&nbsp;
							</td>
						</tr>
					</table>
					<br>
					<hr style="POSITION: absolute">
					<span style="LEFT: 10px; POSITION: relative; TOP: -7px" class="BackColor">&nbsp;
						列表值&nbsp;</span>
					<table width="100%">
						<tr>
							<td width="70%">下拉菜单项目<br>
								<input id="txtText" style="WIDTH: 98%" type="text" name="txtText">
							</td>
							<td vAlign="bottom" align="right">
								<input onclick="Add();" type="button" fckLang="DlgSelectBtnAdd" value="新增">&nbsp;&nbsp;
								<input onclick="Modify();" type="button" fckLang="DlgSelectBtnModify" value="修改">
							</td>
						</tr>
						<tr>
							<td>
								<select id="cmbText" style="WIDTH: 98%" onchange="GetE('cmbText').selectedIndex = this.selectedIndex;Select(this);"
									size="5" name="cmbText"></select>
							</td>
							<td vAlign="bottom">
								<input style="WIDTH: 98%" onclick="Move(-1);" type="button" fckLang="DlgSelectBtnUp" value="上移">
								<br>
								<input style="WIDTH: 98%" onclick="Move(1);" type="button" fckLang="DlgSelectBtnDown" value="下移">
							</td>
						</tr>
						<TR>
							<TD colSpan="2">
								<INPUT onclick="SetSelectedValue();" type="button" fckLang="DlgSelectBtnSetValue" value="设为初始化时选定值">&nbsp;&nbsp;
								<input onclick="Delete();" type="button" fckLang="DlgSelectBtnDelete" value="删除"></TD>
						</TR>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>