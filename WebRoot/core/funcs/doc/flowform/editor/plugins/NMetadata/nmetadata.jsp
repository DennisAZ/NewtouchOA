<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/subsys/inforesource/metadataSelect.js"></script>
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>配置元数据</title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl){
    if (oActiveEl.metadata) {
      $('metadata').value = oActiveEl.metadata;
    }
    if (oActiveEl.metadataname) {
      $('metadataName').value = oActiveEl.metadataname;
    }
  }else
    oActiveEl = null ;
  dialog.SetOkButton(true) ;
  dialog.SetAutoSize(true) ;
}

function Ok(){  
  oEditor.FCKUndo.SaveUndoStep();
  var value = $('metadata').value;
  var name= $('metadataName').value;
  if (oActiveEl) {
    SetAttribute(oActiveEl, 'metadata', value);
    SetAttribute(oActiveEl, 'metadataname', name);
  }
  return true ;
}
function clearMetadata(){
  $('metadataName').value = "";
  $('metadata').value = "";
}
</script>
</head>
<body style="overflow: hidden">
<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table cellspacing="3" cellpadding="0" border="0">
					<tr>
						<td align=left style="vertical-align:top;">
							<span>元数据：</span>
                            <textarea id="metadataName" readonly class="BigStatic" name="metadataName"  rows="5" cols="20"></textarea>
							<input id="metadata" name="metadata" type="hidden" size="10" value=""/>
                            <a href="javascript:;" class="orgAdd"  onClick="selectMate(['metadata','metadataName'])">添加</a>
                            <a href="javascript:;" class="orgClear"  onClick="clearMetadata()">清空</a>
                         </td>
					</tr>
				</table>
			</td>
		</tr>
</table>
</body>
</html>