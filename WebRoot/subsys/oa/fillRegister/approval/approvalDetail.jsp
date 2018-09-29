<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.subsys.oa.training.act.YHHrTrainingPlanAct"%><html>
<head>
<title>补登记信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/util.js"></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getApprovalDetail.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    $('assessingStatus').innerHTML = assessingStatusFunc(data.assessingStatus);
    $('assessingOfficer').innerHTML = assessingOfficerFunc(data.assessingOfficer);
    $('proposer').innerHTML = assessingOfficerFunc(data.proposer);
    $('fillTime').innerHTML = data.fillTime.substr(0, 10);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="17" height="17"><span class="big3"> 补登记详细信息</span><br>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">申请人：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="proposer"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">审批人：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="assessingOfficer"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">审批时间：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="assessingTime"></div></td>

      <td nowrap nowrap align="left" width="120" class="TableContent">审批状态：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="assessingStatus"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">审批意见：</td>
      <td nowrap align="left" class="TableData" colspan="3"><div id="assessingView"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">补登记日期：</td>
      <td nowrap nowrap align="left" class="TableData" colspan="3"><div id="fillTime"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">备注：</td>
      <td nowrap nowrap align="left" class="TableData" colspan="3"><div id="remark"></div></td>
  </tr>
  <tr align="center" class="TableControl">
      <td colspan="4">
      <input type = "hidden" id="sponsoringDepartment" name="sponsoringDepartment"></input>
      <input type = "hidden" id="chargePerson" name="chargePerson"></input>
      <input type = "hidden" id="tJoinDept" name="tJoinDept"></input>
      <input type = "hidden" id="tJoinPerson" name="tJoinPerson"></input>
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
  </tr>
</table>
<div id="msrg">
</div>
</body>
</html>