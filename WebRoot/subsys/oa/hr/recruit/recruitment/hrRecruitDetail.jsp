<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.subsys.oa.hr.recruit.hrPool.act.YHHrRecruitPoolAct"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>招聘录入详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/recruitment/js/recruitmentListLogic.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct/getHrRecruitDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	//alert(rsText);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		$("assessingOfficer").innerHTML = getPersonName(data.assessingOfficer);
		$("presentPosition").innerHTML = selectCodeById(data.presentPosition);
		if(data.assPassTime){
			$("assPassTime").innerHTML = data.assPassTime.substr(0,10);
		}
		if(data.onBoardingTime){
			$("onBoardingTime").innerHTML = data.onBoardingTime.substr(0,10);
		}
		if(data.startingSalaryTime){
			$("startingSalaryTime").innerHTML = data.startingSalaryTime.substr(0,10);
		}
		if(data.department){
			getDeptName("department");
		}
		if(data.type){
			$("type").innerHTML = selectCodeById(data.type);
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}
//根据部门seqId获取绑定值的部门名称getDeptName("proDept");
function getDeptName(deptIdDiv){
	if($(deptIdDiv) && $(deptIdDiv).value.trim() && $(deptIdDiv).value != "0" && $(deptIdDiv).value != "ALL_DEPT"){
		bindDesc([{cntrlId:deptIdDiv, dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
	}else if($(deptIdDiv) && ($(deptIdDiv).value == "0" || $(deptIdDiv).value == "ALL_DEPT")){
		$(deptIdDiv).value = "0";
		$(deptIdDiv+"Desc").innerHTML = "全体部门";
	}
}
</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/hr_manage.gif" width="17" height="17"><span class="big3"> 招聘录入详细信息</span><br></td>
  </tr>
</table>
<table class="TableBlock" width="90%" align="center">
   <tr>
   		<td nowrap align="left" width="120" class="TableContent">计划编号：</td>
			<td nowrap align="left" class="TableData" ><span id="planNo"></span></td>
			<td nowrap align="left" width="120" class="TableContent">应聘人姓名：</td>
			<td nowrap align="left" class="TableData" ><span id="applyerName"></span></td>
   </tr>
   <tr>
    	<td nowrap align="left" width="120" class="TableContent">招聘岗位：</td>
      <td nowrap align="left" class="TableData" ><span id="jobStatus"></span></td>
      <td nowrap align="left" width="120" class="TableContent">OA中用户名：</td>
      <td nowrap align="left" class="TableData" ><span id="oaName"></span></td>
   </tr>
    <tr>
    	<td nowrap align="left" width="120" class="TableContent">录用负责人：</td>
      <td nowrap align="left" class="TableData"><span id="assessingOfficer"></span></td>
      <td nowrap align="left" width="120" class="TableContent">录入日期：</td>
      <td nowrap align="left" class="TableData"><span id="assPassTime"></span></td>
    </tr>
    <tr>
      <td nowrap align="left" width="120" class="TableContent" >招聘部门：</td>
      <td nowrap align="left" class="TableData" colspan="3">
      	<input type="hidden" id="department" value="">
      	<span id="departmentDesc"></span>
      </td>
    </tr> 
    <tr>
    	<td nowrap align="left" width="120" class="TableContent">员工类型：</td>
      <td nowrap align="left" class="TableData"><span id="type"></span></td>
      <td nowrap align="left" width="120" class="TableContent">行政登记：</td>
      <td nowrap align="left" class="TableData"><span id="administrationLevel"></span></td>
    </tr>  
    <tr>
    	<td nowrap align="left" width="120" class="TableContent">职务：</td>
      <td nowrap align="left" class="TableData"><span id="jobPosition"></span></td>
      <td nowrap align="left" width="120" class="TableContent">职称：</td>
      <td nowrap align="left" class="TableData"><span id="presentPosition"></span></td>
    </tr>  
    <tr>
    	<td nowrap align="left" width="120" class="TableContent">正式入职时间：</td>
      <td nowrap align="left" class="TableData"><span id="onBoardingTime"></span></td>
      <td nowrap align="left" width="120" class="TableContent">正式起薪时间：</td>
      <td nowrap align="left" class="TableData"><span id="startingSalaryTime"></span></td>
    </tr>  
    <tr>
      <td nowrap align="left" width="120" class="TableContent">备注：</td>
      <td nowrap align="left" class="TableData" colspan="3"><span id="remark"></span></td>
    </tr> 
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="关闭" class="BigButton" onclick="window.close();" title="关闭窗口">
      </td>
    </tr>
  </table>
</body>
</html>