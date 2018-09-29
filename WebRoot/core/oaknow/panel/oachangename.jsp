<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html> 
<head> 
<title>系统设置</title> 
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript">
	function save(){
		var val = $('oaName').value;
		if(val == null || splitStr(val) == ""){
			alert("请填写系统名称");
			$('oaName').focus;
			return false;
		}else{
		  document.getElementById("form1").submit();	
		}
	}
	function splitStr(str){//正在把多个空格转换为一个空格
	  var arr = trim(str);
	  var reg = /\s{2,}/g; 
	  var newStr = arr.replace(reg,""); 
	  return newStr;
	} 
	function doInit(){
		var flag = ${flag};
		if(flag =="1"){
		  window.parent.frames["banner"].location.reload();		  
		}
	}
</script>

</head> 
<body class="mbodycolor" topmargin="5" onload="doInit();"> 
	<br /> 
	<br /> 
	<br /> 
<DIV class="askbody" style="text-align:center;"> 
	<form name="form1" id="form1" action="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowPanelAct/saveOaName.act" method='post'> 
  <TABLE style="border-collapse: collapse;" width="500" height="56" border="1" class="systable"> 
   	<TR class="header"> 
   		<TD colspan="2">系统设置</TD> 			   			
   	</TR> 
   	<TR> 
   		<TD class="sysleftd">系统名称：</TD> 
   		<TD class="sysrightd"><input type="text" name="oaName" id="oaName" class="askincss2" value="${oaName}"/></TD>	
   	</TR>  		
  </TABLE> 
  <br /> 
  <input type="button" value="保存" class="BigButton" onclick="save();"/> 
  </form> 
</DIV> 
</body> 
</html>