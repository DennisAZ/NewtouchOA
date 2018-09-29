<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String typeId = request.getParameter("typeId");
if(YHUtility.isNullorEmpty(typeId)){
	typeId = "0";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品类别编辑</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/productLogic.js"></script>
<script type="text/javascript">

function doInit(){
	var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
	var url = requestURI + "/getOfficeTypeById.act";
	var rtJson = getJsonRs(url,"typeId=<%=typeId%>");
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		if(data.typeName){
			$("codeName").value = data.typeName ;
		}
		var typeDepository = data.typeDepository;
		getOfficeDepositoryName("depository",typeDepository);
	}else{
		alert(rtJson.rtMsrg);
	}
}

function checkForm(){
	var codeName = $("codeName").value;
	if(codeName.trim() == ""){
		alert("办公用品类别不能为空！"); 
		$("codeName").focus();
		$("codeName").select();
		return false;
	}
	return true;
}

function doSubmit(){
	if(checkForm()){
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var pars = Form.serialize($('form1'));
		var url = requestURI + "/updateTypeName.act";
		var rtJson = getJsonRs(url,pars);
		if(rtJson.rtState == "0"){
			var prcs = rtJson.rtData;
			var isHave = prcs.isHave;
			if(isHave=="1"){
				$("showFormDiv").hide();
				$("remindInfo").innerHTML = "同类别下已有相同名称的办公用品！"
				$("remindDiv").show();
			}else{
				$("showFormDiv").hide();
				$("remindInfo").innerHTML = "办公用品类别修改成功！"
				$("remindDiv").show();
			}
			
		}else{
			alert(rtJson.rtMsrg); 
		}
	}
	
}

function showForm(){
	window.location.href = "<%=contextPath%>/subsys/oa/officeProduct/product/typeManage.jsp";
}
</script>
</head>
<body onload="doInit();">
<div id="showFormDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3">办公用品类型编辑</span>
    </td>
  </tr>
</table>

 
<br>
<form action=""  method="post" name="form1" id="form1" >
<table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">办公用品类别：</td>
    <td nowrap class="TableData">
        <input type="text" name="codeName" id="codeName" class="BigInput" size="20" maxlength="100" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">所属库：</td>
    <td class="TableData">
       <select name="depository" id="depository" >
       </select>
    </td>
   </tr>
   <tr>
    <td nowrap colspan="2" align="center">
        <input type="hidden" value="<%=typeId %>" name="codeId">
        <input type="button" value="确定" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="history.back();">
    </td>
</table>
</form>
</div>
<div id="remindDiv" style="display: none">
<table class="MessageBox" align="center" width="370">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt"><span id="remindInfo"></span></div>
    </td>
  </tr>
</table>
<br><center>
	 <input type="button" class="BigButton" value="返回" onclick="showForm();">
</center>
</div>


</body>
</html>