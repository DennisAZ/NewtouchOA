<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String typeId = request.getParameter("typeId");
String storeId = request.getParameter("storeId"); 
String proSeqId = request.getParameter("proSeqId");
if(YHUtility.isNullorEmpty(storeId)){
	storeId = "0";
}
if(YHUtility.isNullorEmpty(typeId)){
	typeId = "0";
}
if(YHUtility.isNullorEmpty(proSeqId)){
	proSeqId = "0";
}
//System.out.println(typeId+"==="+storeId+"=="+proSeqId);
%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公用品信息</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"><!--
function doInit(){
	 var par = "storeId="+ <%=storeId%> +"&typeId="+<%=typeId%>+"&proSeqId="+<%=proSeqId%>;
	 var url = contextPath+'/yh/subsys/oa/officeProduct/query/act/YHOfficeQueryAct/getOneOfficeProductInfo.act';
   var rtJson = getJsonRs(url,par);
   if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
   var prcs = rtJson.rtData;
   //alert(rsText);
   for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		
		$('officeName').innerHTML =  prc.proName;
		$('officeDesc').innerHTML =  prc.proDesc;
		$('officeType').innerHTML =  prc.typeName;
		$('officeCode').innerHTML =  prc.proCode;
		$('officeUnit').innerHTML =  prc.proUnit;
		$('officePrice').innerHTML =  prc.proPrice;
		$('officeSupplier').innerHTML =  prc.proSupplier;
		$('officeLower').innerHTML =  prc.proLowstock;
		$('officeMax').innerHTML =  prc.proMaxstock;
		var lowStock = parseInt(prc.proLowstock);
		var maxStock = parseInt(prc.proMaxstock);
		var proStock = parseInt(prc.proStock);
		var proPrice = prc.proPrice;
		//alert(proPrice);
		if(proPrice == 0){
         proPrice =1;
			}
		var countPrice = Math.round(proStock * proPrice);
		//	alert(typeof(countPrice));
	    if(proStock > maxStock || proStock < lowStock){
      if(proStock > maxStock){
        $('officeStock').innerHTML = '<font color=red>'+ proStock + '&nbsp;&nbsp;高于最高警戒库存</font>';
      }
      if(proStock < maxStock){
    	  $('officeStock').innerHTML = '<font color=red>'+ proStock + '&nbsp;&nbsp;低于最低警戒库存</font>';
      }
    }else{
    	$('officeStock').innerHTML = proStock;
    }
	    $('countPrice').innerHTML = countPrice +'元';
	}
}
--></script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3">&nbsp;&nbsp;办公用品信息 </span>
    </td>
  </tr>
</table>
<br>
<form action="update.php"  method="post" name="form1" onSubmit="return CheckForm();">
<table width="500" class="TableBlock" align="center" >
    <tr>
    <td nowrap class="TableData" width="30%">办公用品名称： </td>
    <td nowrap class="TableData" id="officeName"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品描述： </td>
    <td nowrap class="TableData" id="officeDesc"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品类别： </td>
    <td nowrap class="TableData" id="officeType"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">办公用品编码： </td>
    <td nowrap class="TableData" id="officeCode"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">计量单位： </td>
    <td nowrap class="TableData" id="officeUnit"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">单价： </td>
    <td nowrap class="TableData" id="officePrice"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">供应商： </td>
    <td nowrap class="TableData" id="officeSupplier"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">最低警戒库存： </td>
    <td nowrap class="TableData" id="officeLower"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">最高警戒库存： </td>
    <td nowrap class="TableData" id="officeMax"></td>
   </tr>
   <tr>
    <td nowrap class="TableData">当前库存： </td>
    <td nowrap class="TableData" id="officeStock">
   </tr>
   <tr>
    <td nowrap class="TableData">当前库存总价： </td>
    <td nowrap class="TableData" id ="countPrice"></td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2" align="center">
    	  <input type="button" value="申请" class="BigButton" onClick="window.location.href='<%=contextPath %>/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/OfficeDepositoryInfo.act?PRO_ID=<%=proSeqId%>&DEPOSITORY=<%=storeId %>&TYPE_ID=<%=typeId %>'"> 
    	 <%-- <input type="button" value="申请" class="BigButton" onClick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/personal/personRecord.jsp?proSeqId=<%=proSeqId%>&storeId=<%=storeId %>&typeId=<%=typeId %>'"> --%>
        <input type="button" value="返回" class="BigButton" title="返回查询页面" name="button1" onClick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/query/query.jsp';">
    </td>
   </tfoot>
</table>
</form>



</body>
</html>