<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html> 

<head> 

<title>上传图片</title> 

<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 

<meta content="noindex, nofollow" name="robots"> 

<script language="javascript">

var oEditor = window.parent.InnerDialogLoaded() ; 

var FCKIn = oEditor.FCKIn ;  

window.onload = function () { 
window.parent.SetOkButton( true ) ; 
}  
function Ok(){ 
FCKIn.Add(document.getElementById('address').value ) ;  
return true ; 
} 
</script>  

</head> 

<body>图片地址：
<input type="text" readonly id="address" name="address" value="<%=request.getAttribute("address") %>"/>
</body>
</html>