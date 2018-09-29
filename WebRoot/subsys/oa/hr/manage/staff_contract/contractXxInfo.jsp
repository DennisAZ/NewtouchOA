<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.manage.staff_contract.data.*"%> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.util.Date"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
List<YHHrStaffContract> contractList = (List<YHHrStaffContract>)request.getAttribute("contractXXInfoList");
YHHrStaffContract contract = contractList.get(0);
String contractType = contract.getContractType();
String contractState = contract.getStatus();



String userName = (String)request.getAttribute("userName");

String makeSub="";//合同签订日期
String trailSub="";//试用生效日期
String trailOver="";//试用到期日期
String probation="";  //合同转正日期
String effective=""; //合同生效日期
String contractEt="";//合同到期日期
String contractRt=""; //合同解除日期
String makeContract = String.valueOf(contract.getMakeContract());
if(!YHUtility.isNullorEmpty(makeContract) && makeContract!="null"){
  makeSub =	makeContract.substring(0,10);
}
System.out.print("contract.getTrailEffectiveTime()>>>>>>>>>>>>"+contract.getTrailEffectiveTime());
 String trailEffective = String.valueOf(contract.getTrailEffectiveTime());
 if(!YHUtility.isNullorEmpty(trailEffective) && trailEffective!="null"){
	 trailSub =	trailEffective.substring(0,10);
	}
String trailOverTime =  String.valueOf(contract.getTrailOverTime());
if(!YHUtility.isNullorEmpty(trailOverTime) && trailOverTime!="null"){
	trailOver =	trailOverTime.substring(0,10);
	}
 String probationEnd = String.valueOf(contract.getProbationEndDate()); 
 if(!YHUtility.isNullorEmpty(probationEnd) && probationEnd!="null"){
	 probation =	probationEnd.substring(0,10);
 }
 String probationEffective = String.valueOf(contract.getProbationEffectiveDate());
 if(!YHUtility.isNullorEmpty(probationEffective) && probationEffective!="null"){
	 effective =	probationEffective.substring(0,10);
 }
String contractEndTime = String.valueOf(contract.getContractEndTime());
if(!YHUtility.isNullorEmpty(contractEndTime) && contractEndTime!="null"){
	 contractEt =	contractEndTime.substring(0,10);
}
String contractRemoveTime = String.valueOf(contract.getContractRemoveTime());
if(!YHUtility.isNullorEmpty(contractRemoveTime) && contractRemoveTime!="null"){
	contractRt =	contractRemoveTime.substring(0,10);
}
%>
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/setting/codeJs/hrCodeJs.js"></script>

<script type="text/javascript">
function goto(){
		window.location.href= contextPath + "/yh/subsys/oa/hr/manage/staff_contract/act/YHNewContractInfoAct/findContractInfo.act";
	}

function doInit(){
	showAttachment();
	var contractTypes = '<%=contractType%>';
	 var contractStates = '<%=contractState%>';
	 var objType = getCodeById(contractTypes);
	 var objState = getCodeById(contractStates);
	 var stateType = objType.codeName;
	 var stateName = objState.codeName;
	 
	 
	 $('contractType').innerHTML = stateType;
	 $('contractState').innerHTML = stateName == undefined ? "" : stateName;
}
function showAttachment(){
	var attachmentId = "<%=YHUtility.null2Empty(contract.getAttachmentId())%>";
	var attachmentName = "<%= YHUtility.encodeSpecial(YHUtility.null2Empty(contract.getAttachmentName()))%>";
	//alert(attachmentName);
	if(attachmentId){
		$("returnAttId").value = attachmentId;
		$("returnAttName").value = attachmentName;
		var selfdefMenu = {
      office:["downFile","read","edit"], 
      img:["downFile","play"],  
      music:["downFile","play"],  
      video:["downFile","play"], 
      others:["downFile"]
 	 }
  	attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=contract.getSeqId()%>',selfdefMenu);
	}
}

</script>


</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img height="17" width="17" src="<%=imgPath %>/meeting.gif"><span class="big3"> 合同详细信息</span><br>
    </td>
  </tr>
</tbody></table>
<br>
<table width="90%" align="center" class="TableBlock">
  <tbody><tr>
    <td nowrap="" width="120" align="left" class="TableContent">姓名：</td>
      <td nowrap="" width="180" align="left" class="TableData" ><%=userName ==null?"":userName %></td>
    <td nowrap="" width="120" align="left" class="TableContent">合同编号：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=contract.getStaffContractNo()==null?"":contract.getStaffContractNo() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">合同类型：</td>
    <td nowrap="" width="180" align="left" class="TableData" id ="contractType" name="contractType"></td>
    <td nowrap="" width="120" align="left" class="TableContent">合同属性：</td>
    <td width="180" align="left" class="TableData"><%=contract.getContractSpecialization()==null?"":contract.getContractSpecialization() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">合同状态：</td>
  <td nowrap="" width="180" align="left" class="TableData" id="contractState" name="contractState"></td>
    
    <td nowrap="" width="120" align="left" class="TableContent">合同签订日期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=makeSub==null?"":makeSub %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">试用生效日期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=trailSub==null?"":trailSub   %></td>
    <td nowrap="" width="120" align="left" class="TableContent">试用到期日期：</td>
    <td width="180" align="left" class="TableData"><%=trailOver==null?"":trailOver %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">试用天数：</td>
    <td width="180" align="left" class="TableData"><%=contract.getProbationaryPeriod()==null?"":contract.getProbationaryPeriod() %></td>
    <td nowrap="" width="120" align="left" class="TableContent">合同是否转正：</td>
    <%if(contract.getPassOrNot().equalsIgnoreCase("1")){ %>
      <td nowrap="" width="180" align="left" class="TableData">是</td>
    <%}else{ %>
      <td nowrap="" width="180" align="left" class="TableData">否</td>
    <%} %>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">合同转正日期：</td>
    <td width="180" align="left" class="TableData"><%=probation==null?"":probation %></td>
    <td nowrap="" width="120" align="left" class="TableContent">合同生效日期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=effective==null?"":effective %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">合同到期日期：</td>
    <td width="180" align="left" class="TableData"><%=contractEt==null?"":contractEt %></td>
    <td nowrap="" width="120" align="left" class="TableContent">合同是否解除：</td>
    <%if(contract.getRemoveOrNot().equalsIgnoreCase("1")){ %>
       <td width="180" align="left" class="TableData">是</td>
    <%}else{ %>
       <td width="180" align="left" class="TableData">否</td>
   <%} %>
  </tr>
  <tr>    
    <td nowrap="" width="120" align="left" class="TableContent">合同解除日期：</td>
    <td width="180" align="left" class="TableData"><%=contractRt==null?"":contractRt %></td>
    <td nowrap="" width="120" align="left" class="TableContent">签约次数：</td>
    <td width="180" align="left" class="TableData"><%=contract.getSignTimes()==null?"":contract.getSignTimes() %> </td>
  </tr>
  <tr>    
    <td nowrap="" width="120" align="left" class="TableContent">登记日期：</td>
    <td nowrap="" width="180" align="left" colspan="3" class="TableData"><%=contract.getAddTime()==null?"":contract.getAddTime() %></td>
  </tr>
    <%
  	if(!YHUtility.isNullorEmpty(contract.getAttachmentId())){
  %>
  <tr>
    <td nowrap="" align="left" width="120" class="TableContent" >附件文档：</td>
     <td nowrap align="left" class="TableData" colspan="3">
			<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
    </td>
  </tr>
  <%
  	}
  
  %>
  <tr>
  	<td nowrap="" width="120" align="left" class="TableContent">备注：</td>
    <td align="left" colspan="3" class="TableData"><%=contract.getRemark()==null?"":contract.getRemark() %></td>
  </tr>
  
  <tr align="center" class="TableControl">
    <td colspan="4">
    <input type="button" value="返回" class="BigButton" onClick="javascript:goto(); return false;">
  </tr>
</tbody></table>
</body>
</html>