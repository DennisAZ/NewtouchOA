<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%


	String seqId=request.getParameter("picId");	
	String subDir=request.getParameter("subDir");	
	if(subDir==null){
		subDir="";
	}
	


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文件夹</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
function checkForm(){
	if($("folderName").value.trim()==""){
		alert("文件夹名不能为空！");
		$("folderName").focus();
		$("folderName").select();
		return false;
	}
	
	if(checkStr($("folderName").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("folderName").select();
		$("folderName").focus();
		return false;
	}
	
	return true;
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}
function sendForm(){
	//alert("aaa");
	var folderName = $("folderName").value;
	if(checkForm()){
		var url= requestURL+"/newFolder.act?seqId=<%=seqId%>&subDir=<%=subDir%>&folderName=" + folderName;
	  var json = getJsonRs(encodeURI(url));
	  //alert("rsText:"+rsText);
	  if(json.rtState == "1"){
	    alert(json.rtMsrg); 
	    return ;
	  }
	  var prc = json.rtData;
	  
	  var sucuss=prc.sucuss;
	  var isExist=prc.isExist;
	  var flag=prc.flag;
	  //alert("sucuss:"+sucuss +"  isExist:"+isExist+"  flag:"+flag);
	  if(flag=="true"){
			//alert("aaa");
			$("folderNameStr").innerHTML=folderName;
			parent.opener.location.reload();
			$("formDiv").hide();
			$("returnDiv").show();		
			
	  }else{
	  	$("formDiv").hide();
	  	$("folderError").innerHTML=$("folderName").value;  	
			$("errorDir").show();
	  }
	}	
	//window.opener.document.location.reload();	
	//parent.opener.location.reload();
}

function toBack(){
	//alert("tt");
	$("errorDir").hide();
	$("formDiv").show();
	$("folderName").focus();
	$("folderName").select();
}
</script>
</head>
<body>
<body class="" onload="form1.folderName.focus();">
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3"><img src="<%=contextPath %>/core/funcs/picture/images/newfolder.gif" align="middle"> <b>新建文件夹</b></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1">
<table class="TableBlock" width="90%" align="center">
    <tr class="TableData">
      <td width="60">文件夹名：</td>
      <td><input type="text" class="BigInput" size="20" name="folderName" id="folderName" value=""></td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="5" nowrap>
        <input type="button" value="保存" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="window.close();">
      </td>
    </tr>
  </table>
</form>
</div>

<div id="returnDiv" style="display: none">
<table class="MessageBox" align="center" width="330">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">新建文件夹 <span id="folderNameStr"> </span> 成功</div>
    </td>
  </tr>
</table>
<br> 
<div align="center"><input type="button" class="BigButton" value="关闭" onclick="window.close();"></div>
</div>


<div id="errorDir" style="display: none">
<table class="MessageBox" align="center" width="310">
  <tr>
    <td class="msg error">
      <h4 class="title">错误</h4>
      <div class="content" style="font-size:12pt">文件夹 <span id="folderError"> </span>  已存在</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="toBack();"></center> &nbsp;&nbsp;
</div>



</body>
</html>