<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = (String)request.getAttribute("seqId");
String css =(String)request.getAttribute("css");
    

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/flowform.css">
<link rel="stylesheet" href = "<%=cssPath%>/views.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script src="<%=contextPath%>/core/js/cmp/fck/fckeditor/editor/dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<title>css控件</title>
<script type="text/javascript">
var dialog = window.parent;
var oEditor = dialog.InnerDialogLoaded();
var oDOM = oEditor.FCK.EditorDocument;
var seqId = "<%=seqId%>";
window.onload = function(){
  dialog.SetOkButton(true);
  dialog.SetAutoSize(true);
}

function Ok(){
  var url = contextPath + "<%=moduleSrcPath %>/util/YHFlowFormUtilAct/updateCssForm.act?seqId=" + seqId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  alert("保存成功！");
}

</script>
</head>
<body class="bodycolor" style="margin:5px">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle">
    	<span class="big3"> 编辑层叠样式表</span>
    </td>
  </tr>
</table>
 
<div id="desc" align="center">
  <form enctype="multipart/form-data" name="form1" id="form1">
  <input type="hidden" name="seqId" id="seqId" value="<%=seqId%>">
   <input type="hidden" id="dtoClass" name="dtoClass" value="yh.rad.flowform.data.YHFlowFormType"/>
    <textarea id="css" name="css" class="BigInput" cols=65 rows=16><%=css %></textarea><br>
  </form>
</div>
</body>
</html>