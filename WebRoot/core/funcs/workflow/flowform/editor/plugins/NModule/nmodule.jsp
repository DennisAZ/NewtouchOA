<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>Insert title here</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var dialog = window.parent;
var oEditor	= dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var oActiveEl = dialog.Selection.GetSelectedElement();
window.onload = function(){
  oEditor.FCKLanguageManager.TranslatePage(document);
  if(oActiveEl && oActiveEl.tagName == 'IMG'){
    GetE('moduleName').value = oActiveEl.value;
    GetE('moduleName').value = oActiveEl.value;
  }else
    oActiveEl = null;
    dialog.SetOkButton(true);
    dialog.SetAutoSize(true);
    SelectField('moduleName');//在编辑区域设置刚创建的控件为选中状态
}
function Ok(){  //把表单插入到fck的工作区当中
  var moduleName = GetE('moduleName');
  if(moduleName.value == ""){
    alert("业务名称不能为空");
    return;
  }
  if(oActiveEl == null && !checkUnique(GetE('moduleName').value,oDOM)){
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
  oActiveEl = CreateNamedElement( oEditor, oActiveEl, 'IMG', {title: GetE('moduleName').value,id:idtr,name:idtr,src:"<%=contextPath%>/core/funcs/workflow/flowform/editor/plugins/NModule/data.gif"});
  SetAttribute(oActiveEl, 'border', 0);
  SetAttribute(oActiveEl, 'align', 'absMiddle');
  SetAttribute(oActiveEl, 'class', 'MODULE');
  SetAttribute(oActiveEl, 'className', 'MODULE');
  SetAttribute(oActiveEl, 'value', moduleName.value.replace("\"","&quot;"));
  oActiveEl.style.cursor = 'hand';
  <% if ("1".equals(isDev)) { %>
  if ($("isAuto").checked) {
    var param = "module=" + GetE('moduleName').value;
    var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowFormUtilAct/autoProduce.act";
    if (confirm("确认生成代码吗？这样有可能覆盖原来的代码！")) {
      var json = getJsonRs(url , param);
    }
  }
  <% } %>
  return true ;
}
</script>
</head>
<body style="overflow: hidden">
<table width="100%" style="height: 100%">
		<tr>
			<td align="center">
				<table cellspacing="3" cellpadding="0" border="0">
          <tr>
              <td nowrap><b>模块名称</b>：              </td>
              <td nowrap align=left>
               <Input id="moduleName" name="moduleName" type="text" size="20">
              </td>
          </tr>
          <% if ("1".equals(isDev)) { %>
          <tr>
              <td nowrap><b>自动生成代码:</b>：
              </td>
              <td nowrap align=left>
               <Input id="isAuto" name="isAuto" type="checkbox" size="20">
              </td>
          </tr>
          <% } %>
				</table>
			</td>
		</tr>
</table>
</body>
</html>