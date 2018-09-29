<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文件夹</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>

<script Language="JavaScript"> 
var requestURL="<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHSetConfidentialSortAct";
function CheckForm(){
  if(document.form1.sortName.value.trim() == ""){
    alert("文件夹名称不能为空！");
    $("sortName").focus();
    return (false);
  }
  if(checkStr(document.form1.sortName.value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("sortName").select();
		$("sortName").focus();
		return false;
	}
  return (true);
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}

function sendForm(){
	var url=requestURL + "/addFileSortInfo.act";
	if(CheckForm()){
		var pars = Form.serialize($('form1'));
		var rtJson = getJsonRs(url,pars);
  	if(rtJson.rtState == '0'){
    	var prcsJson = rtJson.rtData;
    	//alert(rsText);
    	var isHaveFlag=prcsJson.isHaveFlag;
    	if(isHaveFlag==1){
    		$("formDiv").hide();
		  	$("folderError").innerHTML="文件夹 " + $("sortName").value + " 已存在";  	
				$("errorDir").show();
      }else{
        location.href="<%=contextPath%>/subsys/oa/confidentialFile/setConfidentialFile/index.jsp";
      }
    }else{
  	 	  alert(rtJson.rtMsrg); 
  	}	
	}
	
}

function toBack(){
	$("errorDir").hide();
	$("formDiv").show();
	$("sortName").focus();
	$("sortName").select();
}
</script>
</head> 
<body class="" topmargin="5" onload="document.form1.sortName.focus();"> 
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 新建文件夹</span>
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
<table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData"> 排序号：</td>
      <td class="TableData">
        <input type="text" name="sortNo" id="sortNo" size="20" maxlength="20" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 文件夹名称：<font color="red">*</font></td>
      <td class="TableData">
        <input type="text" name="sortName" id="sortName" size="25" maxlength="100" class="BigInput">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="javascript:location='<%=contextPath%>/subsys/oa/confidentialFile/setConfidentialFile/index.jsp'">
      </td>
    </tr>
  </table>
</form>
</div>

<div id="errorDir" style="display: none">
<table class="MessageBox" align="center" width="310">
	<tr>
		<td class="msg error">
		<h4 class="title"> 提示</h4>
		<div class="content" style="font-size: 12pt"><span id="folderError"> </span> </div>
		</td>
	</tr>
</table>
<br>
<center><input type="button" class="BigButton" value="返回"	onclick="toBack();"></center>&nbsp;&nbsp;
</div> 
</body>
</html>