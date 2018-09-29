<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String flowId = request.getParameter("seqId");
  String content = request.getParameter("content"); 
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
var arryStr;
function doInit(){
	arryStr = getSalItemName();
	//alert(arryStr);
	var selected = [];
	new ExchangeSelectbox({containerId:'selectItemDiv' 
		,selectedArray:selected 
		,disSelectedArray:arryStr
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
  var fldStr = $("fieldName").value;
  if(fldStr == ""){
	for(var i=0;i<arryStr.length;i++){
	  fldStr += arryStr[i].value + ',';
	}
  }
  var val=0;
  var selectStr = document.getElementsByName("DEPTFLAG");
  for (i = 0; i < selectStr.length; i++) {
    if (selectStr[i].checked) {
      val = selectStr[i].value;
    }
  }
  var userIdStr = $("user").value;
  var isZero = $("isZero").checked;
  var content = encodeURIComponent("<%=YHUtility.encodeSpecial(content)%>");
  var urlStr = "<%=contextPath%>/yh/subsys/oa/hr/salary/salFlow/act/YHHrSalFlowAct";
  var url = urlStr + "/sendEMail.act";
  var rtJson = getJsonRs(url, "flowId=<%=flowId%>&content="+content+"&userIdStr="+userIdStr+"&fldStr="+fldStr+"&isZero="+isZero );
  if (rtJson.rtState == "0") {
	  var data = rtJson.rtData;
	  $('userNameTo').value = data;
	  $('form2').action = "<%=contextPath%>/subsys/oa/hr/salary/manage/salFlow/newRemind.jsp";
	  $('form2').submit();
  } else {
    alert(rtJson.rtMsrg);
  }
}


</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/email.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;EMAIL工资条 (<%=content %>) </span>
   </td>
  </tr>
</table>
<div align="center">
<form method="post" name="form1" id="form1">
<table class="TableBlock" align="center" >
  <tr id="SER">
    <td nowrap class="TableContent">&nbsp;&nbsp;人员： </td>
    <td nowrap class="TableData" colspan="3">
      <input type="hidden" name="user"  id="user" value="">
      <textarea cols="21" name="userName" id="userName" rows="2" class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser(['user', 'userName'],null,null,1)">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userName').value='';">清空</a><br>
      <img src="<%=imgPath %>/attention.gif" height="18"> 人员为空时发送全部人员的工资条 
    </td>
    
  </tr>
  <tr>
    <td nowrap class="TableContent">&nbsp;&nbsp;输出形式： </td>
    <td nowrap class="TableData" colspan="3" align="left">
      <input type="checkbox" id="isZero" name="isZero">不显示数额为零的项目 
    </td> 
  </tr>
  <tr>	
  <td nowrap class="TableContent">&nbsp;&nbsp;输出内容：</td>
  <td nowrap class="TableData" colspan="3" align="left">
  	<div id="selectItemDiv"></div>
	</td>
  </tr>
  <tr class="TableControl">
    <td nowrap colspan="4" align="center">
    	 <input type="hidden" name="FLOW_ID" value="<%=flowId %>">
       <input type="hidden" name="STYLE" value="">
       <input type="button" value="确定" class="SmallButton"  name="submit" onClick="exreport(event)">&nbsp;&nbsp;
       <input type="button" value="返回" class="SmallButton"  name="button" onClick="javascript:location='<%=contextPath %>/subsys/oa/hr/salary/manage/salFlow/manage.jsp'">
       <input type="hidden" name="fieldName" id="fieldName" value="">
    </td>
   </tr>
</table>
</form>
<form action="" id="form2" name="form2" method="post">
<input type="hidden" id="userNameTo" name="userNameTo">
</form>
</div>


</body>
</html>