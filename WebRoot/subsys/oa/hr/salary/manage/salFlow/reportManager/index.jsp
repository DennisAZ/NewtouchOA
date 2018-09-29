<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String flowId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工资报表</title>
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js"  ></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="/yh/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
function doInit(){
	var arryStr = getSalItemName();
	//alert(arryStr);
	var selected = [];
	var disSelected = [
	 	          	{value:'ALL_BASE',text:'保险基数'},
	 	          	{value:'PENSION_BASE',text:'养老保险'},
	 	          	{value:'PENSION_U',text:'单位养老'},
	 	          	{value:'PENSION_P',text:'个人养老'},
	 	          	{value:'MEDICAL_BASE',text:'医疗保险'},
	 	          	{value:'MEDICAL_U',text:'单位医疗'},
	 	          	{value:'MEDICAL_P',text:'个人医疗'},
	 	          	{value:'FERTILITY_BASE',text:'生育保险'},
	 	          	{value:'FERTILITY_U',text:'单位生育'},
	 	          	{value:'UNEMPLOYMENT_BASE',text:'失业保险'},
	 	          	{value:'UNEMPLOYMENT_U',text:'单位失业'},
	 	          	{value:'UNEMPLOYMENT_P',text:'个人失业'},
	 	          	{value:'INJURIES_BASE',text:'工伤保险'},
	 	          	{value:'INJURIES_U',text:'单位工伤'},
	 	          	{value:'HOUSING_BASE',text:'住房公积金'},
	 	          	{value:'HOUSING_U',text:'单位住房'},
	 	          	{value:'HOUSING_P',text:'个人住房'},
	 	          	{value:'INSURANCE_DATE',text:'投保日期'},
	 	          	{value:'MEMO',text:'备注'}
	 	          
 	];
	disSelected = arryStr.concat(disSelected);
	new ExchangeSelectbox({containerId:'selectItemDiv' 
		,selectedArray:selected 
		,disSelectedArray:disSelected 
		,isOneLevel:false 
		 ,isSort:true
		,selectedChange:exchangeHandler 
		});

	
}
function exchangeHandler(ids){ 
	//alert(ids);
	if(ids){
		$("fieldName").value = ids;
	}
}
function exreport(){
	//var query = $("form1").serialize();
	var selectValue = $("fieldName").value;
	//location.href = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoQueryAct/exportToCSV.act?" + param + "&selectValue=" + selectValue;
}
function sel_change() {
	var val=0;
	var selectStr = document.getElementsByName("DEPTFLAG");
	for (i = 0; i < selectStr.length; i++) {
		if (selectStr[i].checked) {
			val = selectStr[i].value;
		}
	}
	if (val == "1") {
		document.all("SER").style.display = "";
	} else {
		document.all("SER").style.display = "none";
	}
}

function getSalItemName(){
	var arryStr = "";
	var urlStr = "<%=contextPath%>/yh/subsys/oa/hr/salary/salFlow/act/YHHrSalFlowAct";
	var url = urlStr + "/getSalItemNames.act";
	var rtJson = getJsonRs(url, "flowId=<%=flowId%>" );
	//alert(rsText);
	if (rtJson.rtState == "0") {
		arryStr = rtJson.rtData;
	} else {
		alert(rtJson.rtMsrg);
	}
	return arryStr;
}

function exreport(event) {
	var val=0;
	var selectStr = document.getElementsByName("DEPTFLAG");
	for (i = 0; i < selectStr.length; i++) {
		if (selectStr[i].checked) {
			val = selectStr[i].value;
		}
	}
	var userIdStr = $("user").value;
	var deptStr = $("deptStr").value;
	var fldStr = $("fieldName").value;;
	var sumfieldStr = document.getElementsByName("SUMFIELD");
	//alert(sumfieldStr.length);
	var url = "";
	if(sumfieldStr[0].checked){
		if(val == "1" ){
			url = "report.jsp?flowId=<%=flowId%>&userId=" + userIdStr + "&deptId=" + deptStr + "&fldStr=" + fldStr; 
		}
		if(val == "0" ){
			url = "report.jsp?flowId=<%=flowId%>&deptFlag=1&fldStr=" + fldStr; 
		}
	}
	if(sumfieldStr[1].checked){
		if(val == "0" ){
			$("deptFlag").value = "1";
		}
		$("form1").submit();
		return ;
	}
	var loc_x = document.body.scrollLeft + event.clientX - event.offsetX - 100;
	var loc_y = document.body.scrollTop + event.clientY - event.offsetY + 170;
	window.open(url,"report","height=400,width=550,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left=150,resizable=yes");
}
</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3">&nbsp;工资报表</span>
   </td>
  </tr>
</table>
<div align="center">
<form method="post" name="form1" id="form1" action="<%=contextPath %>/yh/subsys/oa/hr/salary/salFlow/act/YHHrSalFlowAct/exportToExcel.act" >
<table class="TableBlock" align="center" >
   <tr>
   	<td nowrap class="TableContent">&nbsp;&nbsp;人员范围： </td>
    <td nowrap class="TableData" colspan="3" align="left">
    <input type="radio" name='DEPTFLAG' id="DEPTFLAG" value="1" checked  onclick="sel_change()">在职人员 &nbsp;<input type="radio" name='DEPTFLAG' id="DEPTFLAG" value="0"  onclick="sel_change()">外部人员&nbsp;
    </td> 
  </tr>
  <tr id="SER">
    <td nowrap class="TableContent">&nbsp;&nbsp;部门： </td>
    <td nowrap class="TableData">
       <input type="hidden" name="deptStr" id="deptStr" value="">
       <textarea cols=21 name="deptStrName" id="deptStrName" rows="2" class="BigStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['deptStr','deptStrName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('deptStr').value='';$('deptStrName').value='';">清空</a>
    </td>
    <td nowrap class="TableContent">&nbsp;&nbsp;人员： </td>
    <td nowrap class="TableData">
      <input type="hidden" name="user"  id="user" value="">
      <textarea cols="21" name="userName" id="userName" rows="2" class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['user', 'userName'],null,null,1)">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userName').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">&nbsp;&nbsp;输出形式： </td>
    <td nowrap class="TableData" colspan="3" align="left">
    <input type="radio" name='SUMFIELD' id="SUMFIELD" value="htm" checked>html报表 &nbsp;<input type="radio" name='SUMFIELD' id="SUMFIELD" value="excel">EXCEL报表&nbsp;
    </td> 
  </tr>
  <tr>	
  <td nowrap class="TableContent">&nbsp;&nbsp;输出内容：</td>
  <td nowrap class="TableData" colspan="3" align="left">
  	<div id="selectItemDiv"></div>
 		<img src="<%=imgPath %>/attention.gif" height="18"> 管理员提示：输出字段为空,则输出全部字段; 按&lt;CTRL&gt;+鼠标左键可以选择多个项目。
	</td>
  </tr>
  <tr align="center" class="TableFooter">
    <td nowrap colspan="4" align="center">
    	 <input type="hidden" name="flowId" id="flowId" value="<%=flowId %>">
       <input type="hidden" name="deptFlag" id="deptFlag" value="">
       <input type="button" value="确定" class="SmallButton"  onClick="exreport(event)">&nbsp;&nbsp;
       <input type="button" value="返回" class="SmallButton"  name="button" onClick="javascript:location='<%=contextPath %>/subsys/oa/hr/salary/manage/salFlow/manage.jsp'">
       <input type="hidden" name="fieldName" id="fieldName" value="">
    </td>
   </tr>
</table>
</form>
</div>


</body>
</html>