<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String typeId = request.getParameter("typeId");
String storeId = request.getParameter("storeId");
if(YHUtility.isNullorEmpty(storeId)){
	storeId = "0";
}
if(YHUtility.isNullorEmpty(typeId)){
	typeId = "0";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品列表</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/productLogic.js"></script>
<script type="text/javascript">
function doInit(){
	var extData = "<%=typeId%>";
	var storeId = "<%=storeId%>";
	getproEditDepositoryNames("officeDepository",extData);
	//depositoryOfType(extData);
	getTypeNamesByStoreId(storeId);
	
}

function checkForm(){
	var query = $("form1").serialize(); 
	location.href = "<%=contextPath %>/subsys/oa/officeProduct/query/listView.jsp?" + query
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="middle"><span class="big3"> 办公用品信息查询</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
 <form action=""  method="post" name="form1" id="form1" >
<table width="450" class="TableBlock" align="center" >
   <tr>
    <td nowrap class="TableData">办公用品名称： </td>
    <td nowrap class="TableData" align="left">
        <input type="text" name="proName" id="proName" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品描述： </td>
    <td nowrap class="TableData" align="left">
        <input type="text" name="proDesc" id="proDesc" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品编码： </td>
    <td nowrap class="TableData" align="left">
        <input type="text" name="proCode" id="proCode" class="BigInput" size="33" maxlength="100">&nbsp;
    </td>
   </tr>
    <tr>
    <td nowrap class="TableData">办公用品库： </td>
    <td nowrap class="TableData" align="left">
      <select name="officeDepository" id = "officeDepository"  onchange = "depositoryOfType(this.value);">
	  		<option value="-1">请选择</option>
		</select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品类别： </td>
    <td nowrap class="TableData" id = "officeType" align="left">
    	<select name="officeProtype" id = "officeProtype" >	 
				<option value='-1'>请选择</option>		
     	</select>
    </td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2">
        <input type="button" value="查询" class="BigButton" title="模糊查询" name="button" onClick="checkForm();">
    </td>
   </tfoot>
</table>
</form>
</div>


</body>
</html>