<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工复职详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/reinstatement/act/YHHrStaffReinstatementAct/getReinstatementDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		getOrgDescFunc();
		$('reinstatementPerson').innerHTML = staffNameFunc(data.reinstatementPerson);
		$('reappointmentType').innerHTML = getReinstatementItem(data.reappointmentType);
		$('nowPosition').innerHTML = data.nowPosition;
    if(data.applicationDate){
      $("applicationDate").innerHTML = data.applicationDate.substr(0,10);
    }		
    if(data.reappointmentTimePlan){
      $("reappointmentTimePlan").innerHTML = data.reappointmentTimePlan.substr(0,10);
    } 
    if(data.reappointmentTimeFact){
      $("reappointmentTimeFact").innerHTML = data.reappointmentTimeFact.substr(0,10);
    } 
    if(data.firstSalaryTime){
      $("firstSalaryTime").innerHTML = data.firstSalaryTime.substr(0,10);
    } 
    $('materialsCondition').innerHTML = data.materialsCondition;
    $("reappointmentState").innerHTML = data.reappointmentState;
		$("remark").innerHTML = data.remark;
		$("addTime").innerHTML = data.addTime;

		if(data.attachmentId){
			$("returnAttId").value = data.attachmentId;
			$("returnAttName").value = data.attachmentName;
			var selfdefMenu = {
          office:["downFile","read","edit"], 
          img:["downFile","play"],  
          music:["downFile","play"],  
          video:["downFile","play"], 
          others:["downFile"]
      }
      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
		}else{
			$('attr').innerHTML = "无附件";
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}

/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(cellData){
  var url = contextPath + "/yh/subsys/oa/hr/manage/reinstatement/act/YHHrStaffReinstatementAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getReinstatementItem(seqId){
  var urls = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId="+seqId;
  var rtJsons = getJsonRs(urls);
  var prc = rtJsons.rtData;
  if(rtJsons.rtState == '0'){
    if(prc.codeName){
      return prc.codeName;
    }
    return "";
  }else{
    alert(rtJson.rtMsrg);
  }
}

function getOrgDescFunc(){
  bindDesc([{cntrlId:"reappointmentDept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 员工复职详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">姓名：</td>
    <td align="left" class="TableData" width="180"><div id="reinstatementPerson"></div> </td>
    <td align="left" width="120" class="TableContent">复职类型：</td>
    <td align="left" class="TableData" width="180"><div id="reappointmentType"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">担任职务：</td>
    <td align="left" class="TableData" width="180"><div id="nowPosition"></div> </td>
    <td align="left" width="120" class="TableContent">复职部门：</td>
    <td align="left" class="TableData Content" width="180"><div id="reappointmentDeptDesc"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">申请日期：</td>
    <td align="left" class="TableData" width="180"><div id="applicationDate"></div></td>
    
    <td align="left" width="120" class="TableContent">拟复职日期：</td>
    <td align="left" class="TableData Content" width="180"><div id="reappointmentTimePlan"></div></td>
  </tr>
  
  <tr>
    <td align="left" width="120" class="TableContent">实际复职日期：</td>
    <td align="left" class="TableData Content" width="180"><div id=reappointmentTimeFact></div></td>
    <td align="left" width="120" class="TableContent">工资恢复日期：</td>
    <td align="left" class="TableData" width="180"><div id="firstSalaryTime"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">复职手续办理：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="materialsCondition"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">复职说明：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="reappointmentState"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">备注：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="remark"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">附件文档：</td>
    <td align="left" class="TableData" colspan="3">
			<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
    </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">登记时间：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="addTime"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="hidden" id="reappointmentDept" name="reappointmentDept">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>