<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

</head>
<body>
<script type="text/javascript" src="<%=contextPath %>/raw/lh/fckeditor/fckeditor.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript"><!--
function saveForm(){
  var oEditor = FCKeditorAPI.GetInstance('FCKeditor1') ;
  
  var param = 'path=raw/lh/data/formTemplt&content='+ encodeURIComponent(oEditor.GetXHTML());
  var json = getJsonRs(contextPath + "/yh/core/funcs/smartform/act/YHFormAct/doSaveForm.act", param);
  
  if(json.rtState == '0'){
    alert(json.rtMsrg);
  }else{
    alert(text);
  }  
}

</script>
 <div>
<script type="text/javascript">
var sBasePath = contextPath+'/raw/lh/fckeditor/';
var oFCKeditor = new FCKeditor( 'FCKeditor1' ) ;
oFCKeditor.BasePath    = sBasePath ;
oFCKeditor.Height = 500 ;
oFCKeditor.Config['CustomConfigurationsPath'] = contextPath + '/raw/lh/js/smartform.config.js' ;
var sSkinPath = sBasePath + 'editor/skins/silver/';
oFCKeditor.Config['SkinPath'] = sSkinPath ;
oFCKeditor.Config['PreloadImages'] =
                sSkinPath + 'images/toolbar.start.gif' + ';' +
                sSkinPath + 'images/toolbar.end.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonarrow.gif' ;
                
oFCKeditor.ToolbarSet   = 'form' ;
oFCKeditor.Value   = '' ;

oFCKeditor.Create();

</script>
</div>
<div style="border: 1px solid #6E91C7">
<iframe name="preview" src="DtreeTest.jsp">


</iframe>
</div>


</body>
</html>