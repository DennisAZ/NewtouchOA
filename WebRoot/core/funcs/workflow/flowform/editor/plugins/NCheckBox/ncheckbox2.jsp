<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<script src="/yh/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>Insert title here</title>
<script type="text/javascript">
var dialog	= window.parent ;
var oEditor	= dialog.InnerDialogLoaded() ;
var oDOM = oEditor.FCK.EditorDocument ;
var oActiveEl = dialog.Selection.GetSelectedElement() ;
window.onload = function()
{
	oEditor.FCKLanguageManager.TranslatePage(document) ;
	if ( oActiveEl && oActiveEl.tagName == 'INPUT' && oActiveEl.type == 'checkbox' )
	{
		GetE('txtName').value		= oActiveEl.title ;
		GetE('txtValue').value		= oEditor.FCKBrowserInfo.IsIE ? oActiveEl.value : GetAttribute( oActiveEl, 'value' ) ;
		GetE('txtSelected').checked	= oActiveEl.checked ;
	}
	else
		oActiveEl = null ;
	dialog.SetOkButton( true ) ;
	dialog.SetAutoSize( true ) ;
	SelectField( 'txtName' ) ;
}

function Ok()
{
  var sort = getIndexOfPreName("DATA",oDOM) ;
  
	oEditor.FCKUndo.SaveUndoStep() ;

	oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'INPUT', {title: GetE('txtName').value, type: 'checkbox',name:"DATA_" + sort } ) ;

	if ( oEditor.FCKBrowserInfo.IsIE )
		oActiveEl.value = GetE('txtValue').value ;
	else
		SetAttribute( oActiveEl, 'value', GetE('txtValue').value ) ;
	
	var bIsChecked = GetE('txtSelected').checked ;
	SetAttribute( oActiveEl, 'checked', bIsChecked ? 'checked' : null ) ;	// For Firefox
	oActiveEl.checked = bIsChecked ;

	return true ;
}
		</script>
	</head>
	<body style="OVERFLOW: hidden" scroll="no">
		<table height="100%" width="100%">
			<tr>
				<td align="center">
					<table border="0" cellpadding="0" cellspacing="0" width="80%">
						<tr>
							<td>
								<span fckLang="DlgCheckboxName">控件名称： </span>
								<input type="text" size="20" id="txtName" style="WIDTH: 100%">
							</td>
						</tr>
						<tr>
							<td>
								<span fckLang="DlgCheckboxValue">Value</span>
								<input type="text" size="20" id="txtValue" style="WIDTH: 100%">
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" id="txtSelected"><label for="txtSelected" fckLang="DlgCheckboxSelected">Checked</label></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
