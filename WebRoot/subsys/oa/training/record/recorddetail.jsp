<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>培训记录详细信息</title>
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/training/act/YHTrainingRecordAct/getRecordDetail.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    $('staffUserId').innerHTML = assessingOfficerFunc(data.staffUserId);
  }else{
    alert(rtJson.rtMsrg); 
  }
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/hr_manage.gif" width="17" height="17"><span class="big3"> 培训记录详细信息</span><br></td>
  </tr>
</table>
<table class="TableBlock" width="90%" align="center">
   <tr>
    <td nowrap align="left" width="120" class="TableContent">培训计划编号：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="tPlanNo"></div></td>
    <td nowrap align="left" width="120" class="TableContent">培训计划名称：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="tPlanName"></div></td>
  </tr>
  <tr>
   <td nowrap align="left" width="120" class="TableContent">培训机构：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="tInstitutionName"></div></td>
    <td nowrap align="left" width="120" class="TableContent">培训费用：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="trainningCost"></div></td>
  </tr>
  <tr>
   <td nowrap align="left" width="120" class="TableContent">培训考核成绩：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="tExamResults"></div></td>
    <td nowrap align="left" width="120" class="TableContent">培训考核等级：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="tExamLevel"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">受训人：</td>
    <td class="TableData" colspan=3><div id="staffUserId"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">出勤情况：</td>
    <td nowrap align="left" class="TableData" colspan=3><div id="dutySituation"></div></td>
  </tr>
  <tr>
   <td nowrap align="left" width="120" class="TableContent">总结完成情况：</td>
    <td nowrap align="left" class="TableData" colspan=3><div id="trainningSituation"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">评论：</td>
    <td nowrap align="left" class="TableData" colspan=3><div id="tComment"></div></td>
  </tr> 
  <tr>
    <td nowrap align="left" width="120" class="TableContent">备注：</td>
    <td nowrap align="left" class="TableData" colspan=3><div id="remark"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan=4 nowrap>
      <input type="button" value="关闭" class="BigButton" onclick="window.close();" title="关闭窗口">
    </td>
  </tr>
  </table>
<div id="msrg">
</div>
</body>
</html>