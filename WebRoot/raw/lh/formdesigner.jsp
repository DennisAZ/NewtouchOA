<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>智能表单设计器</title>
 <link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/raw/lh/fckeditor/fckeditor.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function jsonToInput(){
	var data = $H(widgetData);
	var dataDiv = $('widgetDataDiv');
	if(dataDiv){
	  document.submitForm.removeChild(dataDiv);
	}
	
	dataDiv = document.createElement("div");
	dataDiv.id = "widgetDataDiv";
	data.each(function(pair){ 
	  var hid = document.createElement('input');
 	  hid.type = "hidden";
 	  hid.name = pair.key;
 	  hid.value = pair.value;
 	  dataDiv.appendChild(hid);
 	}); 
	document.submitForm.appendChild(dataDiv);
}
</script>
</head>
<body>
<form name="submitForm" id="submitForm" action="<%=contextPath %>/raw/lh/formdesign/YHFormDesignAct/previewForm.act" method="post" target="preview">
<script type="text/javascript">
widgetData = {} ;
var sBasePath = contextPath+'/raw/lh/fckeditor/';
var oFCKeditor = new FCKeditor( 'htmlContent' ) ;
oFCKeditor.BasePath    = sBasePath ;
var sSkinPath = sBasePath + 'editor/skins/office2003/';
oFCKeditor.Config['SkinPath'] = sSkinPath ;
oFCKeditor.Config['PreloadImages'] =
                sSkinPath + 'images/toolbar.start.gif' + ';' +
                sSkinPath + 'images/toolbar.end.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonarrow.gif' ;
//oFCKeditor.Config['FullPage'] = true ;
oFCKeditor.ToolbarSet    = 'designHtml' ;
oFCKeditor.Value = '' ;
oFCKeditor.Create();
</script>
</form>
</body>
</html>