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
<title>员工离职详细信息</title>
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
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/getLeaveDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		getOrgDescFunc();
		$('leavePerson').innerHTML = staffNameFunc(data.leavePerson);
		$('quitType').innerHTML = getLeaveItem(data.quitType);
		$('position').innerHTML = data.position;
    if(data.applicationDate){
      $("applicationDate").innerHTML = data.applicationDate.substr(0,10);
    }		
    if(data.quitTimePlan){
      $("quitTimePlan").innerHTML = data.quitTimePlan.substr(0,10);
    }
    if(data.quitTimeFact){
      $("quitTimeFact").innerHTML = data.quitTimeFact.substr(0,10);
    }   
    if(data.lastSalaryTime){
      $("lastSalaryTime").innerHTML = data.lastSalaryTime.substr(0,10);
    } 
    $('trace').innerHTML = data.trace;
    $("materialsCondition").innerHTML = data.materialsCondition;
		$("quitReason").innerHTML = data.quitReason;
		$("remark").innerHTML = data.remark;
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
  var url = contextPath + "/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getLeaveItem(seqId){
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
  bindDesc([{cntrlId:"leaveDept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 员工离职详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">姓名：</td>
    <td align="left" class="TableData" width="180"><div id="leavePerson"></div> </td>
    <td align="left" width="120" class="TableContent">离职类型：</td>
    <td align="left" class="TableData" width="180"><div id="quitType"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">担任职务：</td>
    <td align="left" class="TableData" width="180"><div id="position"></div></td>
    <td align="left" width="120" class="TableContent">离职部门：</td>
    <td align="left" class="TableData" width="180"><div id="leaveDeptDesc"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">申请日期：</td>
    <td align="left" class="TableData" width="180"><div id="applicationDate"></div> </td>
    <td align="left" width="120" class="TableContent">拟离职日期：</td>
    <td align="left" class="TableData Content" width="180"><div id="quitTimePlan"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">实际离职日期：</td>
    <td align="left" class="TableData" width="180"><div id="quitTimeFact"></div> </td>
    <td align="left" width="120" class="TableContent">工资截止日期：</td>
    <td align="left" class="TableData Content" width="180"><div id="lastSalaryTime"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">去向：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="trace"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">离职手续办理：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="materialsCondition"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">离职原因：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="quitReason"></div></td>
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
      <input type="hidden" id="leaveDept" name="leaveDept">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>