<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<html> 

<head> 

<title>上传图片</title> 
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<meta content="noindex, nofollow" name="robots"> 
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script language="javascript">
var oEditor = window.parent.InnerDialogLoaded() ; 
Event.observe(window, 'load', function() {
  $('imageFileUpload').action = oEditor.FCKConfig['uploadImageUrl'] + "?saveFileTo=" + oEditor.FCKConfig['uploadImageTo'];

});
function checkUploadForm(){
  if($('imageFile').present()){
    return true;
  }else{
	alert('文件不能为空!')
	$('imageFile').focus();
  	return false;
  }
}
</script>  

</head> 

<body scroll="no" style="OVERFLOW: hidden" onload=""> 
<form id="imageFileUpload" action="" method="post" enctype="multipart/form-data">
<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0"> 
<tr> 
<td> 
上传：
</td> 
<td> 
<INPUT TYPE="file" NAME="imageFile" id="imageFile"/>
</td> 
</tr> 
<tr> 
<td  colspan="2"> 
<INPUT TYPE="submit" VALUE="Upload Image" onclick="return checkUploadForm()">
</td> 
</tr> 
</table> 
</form>
</body> 

</html> 

