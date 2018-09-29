<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品类型定义</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/productLogic.js"></script>
<script type="text/javascript">

function doInit(){
	getOfficeDepositoryName("depository");
	
}

function checkForm(){
	var typeName = $("typeName").value.trim();
	var typeOrder = $("typeOrder").value.trim();
	if(typeName == ""){
		alert("名称不能为空！");
		$("typeName").focus();
		$("typeName").select();
		return false;
	}
	if(typeOrder && (!isPositivInteger(typeOrder) && typeOrder != "0")){
		alert("排序号应为正整数！");
		$("typeOrder").focus();
		$("typeOrder").select();
		return false;
	}
	return true;
}
function doSubmit(){
	if(checkForm()){
		var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/product/act/YHOfficeProductsAct";
		var pars = Form.serialize($('form1'));
		var url = requestURI + "/addOfficeType.act";
		var rtJson = getJsonRs(url,pars);
		if(rtJson.rtState == "0"){
			var prcs = rtJson.rtData;
			var isHave = prcs.isHave;
			if(isHave == "1"){
				$("infoStr").innerHTML = "该库下" + prcs.typeName +"已存在！";
				$("returnDiv").show();
			}else{
				parent.file_tree.location.reload();
				$("typeName").value = "";
				$("typeOrder").value = "";
				$("infoStr").innerHTML = "增加成功！";
				$("returnDiv").show();
			}
		}else{
			alert(rtJson.rtMsrg); 
		}
	}
	
}


</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3">&nbsp;&nbsp;办公用品类型定义</span>
    </td>
  </tr>
</table>
 
<br>
<form action="codedefine.php?flag=1"  method="post" name="form1" id="form1" >
<table width="450" class="TableBlock" align="center" >
   <tr>
    <td nowrap class="TableData">所属库：</td>
    <td class="TableData">
       <select name="depository" id="depository" >
      </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">名称：<font color="red">*</font> </td>
    <td nowrap class="TableData">
        <input type="text" name="typeName" id="typeName" class="BigInput" size="20" maxlength="100" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">排序号：</td>
    <td nowrap class="TableData">
        <input type="text" name="typeOrder" id="typeOrder" class="BigInput" size="20" maxlength="10" value="">&nbsp;
    </td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2" align="center">
        <input type="button" value="确定" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton" >
    </td>
   </tfoot>
</table>
</form>

<div align="center" id="returnDiv" style="display:none">
 <table class="MessageBox" align="center" width="290" >
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt"><span id="infoStr"></span>  </div>
    </td>
  </tr>
</table>
 </div>




</body>
</html>