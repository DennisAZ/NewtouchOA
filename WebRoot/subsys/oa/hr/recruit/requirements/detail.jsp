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
<title>招聘需求详细信息</title>
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
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/requirements/act/YHHrRecruitRequirementsAct/getRecruitRequirementsDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		$("createUser").innerHTML = staffNameFunc(data.createUserId);
		$("requDeptDesc").innerHTML = deptFunc(data.requDept);
    if(data.requTime){
      $("requTime").innerHTML = data.requTime.substr(0,10);
    }		

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
  var url = contextPath + "/yh/subsys/oa/hr/recruit/requirements/act/YHHrRecruitRequirementsAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function deptFunc(cellData, recordIndex, columIndex){
  var url = contextPath + "/yh/subsys/oa/hr/recruit/requirements/act/YHHrRecruitRequirementsAct/getDeptName.act";
  var rtJson = getJsonRs(url, "deptIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3">招聘需求详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">需求编号：</td>
    <td align="left" class="TableData" width="180"><div id="requNo"></div> </td>
    <td align="left" width="120" class="TableContent">需求部门：</td>
    <td align="left" class="TableData" width="180"><div id="requDeptDesc"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">需求岗位：</td>
    <td align="left" class="TableData" width="180"><div id="requJob"></div> </td>
    <td align="left" width="120" class="TableContent">需求人数：</td>
    <td align="left" class="TableData Content" width="180"><div id="requNum"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">用工日期：</td>
    <td align="left" class="TableData" width="180"><div id="requTime"></div></td>
    
    <td align="left" width="120" class="TableContent">申请人：</td>
    <td align="left" class="TableData Content" width="180"><div id="createUser"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">岗位要求：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="requRequires"></div></td>
  </tr>
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
    <td align="left" width="120" class="TableContent">登记日期：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="addTime"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>