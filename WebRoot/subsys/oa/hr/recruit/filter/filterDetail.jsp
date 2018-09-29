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
<title>招聘筛选详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/filter/js/dealWithLogic.js"></script>
<script type="text/javascript">
function doInit(){
	var requestURLStr = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getHrFilterDetail.act";
	var rtJson = getJsonRs(url, "seqId=<%=seqId%>" );
	//alert(rsText);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		
		if(data.planNo){
			$("planName").innerHTML = getPlanNameByPlanNo(data.planNo);
		}
		if(data.transactorStep){
			$("transactorStep").innerHTML = getPersonName(data.transactorStep);
		}
		if(data.nextTransaStep){
			$("nextTransaStep").innerHTML = getPersonName(data.nextTransaStep);
		}
		if(data.endFlag == "2"){
			$("endFlagInfo").innerHTML = "(已通过筛选)";
		}else if(data.endFlag == "1"){
			$("endFlagInfo").innerHTML = "(未通过筛选)";
			
		}else{
			$("endFlagInfo").innerHTML = "(待筛选)";
		}
		var stepFlag = data.stepFlag;
		if(stepFlag>=2){
			$("transactorStep1Div").show();
			$("filterMethod1").innerHTML = selectCodeById(data.filterMethod1);
			$("transactorStep1").innerHTML = getPersonName(data.transactorStep1);
			$("nextTransaStep1").innerHTML = getPersonName(data.nextTransaStep1);
			if(data.passOrNot1 == "1"){
				$("passOrNot1").innerHTML = "通过";
			}else{
				$("passOrNot1").innerHTML = "未通过";
			}
		}
		if(stepFlag>=3){
			$("transactorStep2Div").show();
			$("filterMethod2").innerHTML = selectCodeById(data.filterMethod2);
			$("transactorStep2").innerHTML = getPersonName(data.transactorStep2);
			$("nextTransaStep2").innerHTML = getPersonName(data.nextTransaStep2);
			if(data.passOrNot2 == "1"){
				$("passOrNot2").innerHTML = "通过";
			}else{
				$("passOrNot2").innerHTML = "未通过";
			}
		}
		if(stepFlag>=4){
			$("transactorStep3Div").show();
			$("filterMethod3").innerHTML = selectCodeById(data.filterMethod3);
			$("transactorStep3").innerHTML = getPersonName(data.transactorStep3);
			$("nextTransaStep3").innerHTML = getPersonName(data.nextTransaStep3);
			if(data.passOrNot3 == "1"){
				$("passOrNot3").innerHTML = "通过";
			}else{
				$("passOrNot3").innerHTML = "未通过";
			}
		}
		if(stepFlag>=5){
			$("transactorStep4Div").show();
			$("filterMethod4").innerHTML = selectCodeById(data.filterMethod4);
			$("transactorStep4").innerHTML = getPersonName(data.transactorStep4);
			$("transactorStep4").innerHTML = getPersonName(data.transactorStep4);
			if(data.passOrNot4 == "1"){
				$("passOrNot4").innerHTML = "通过";
			}else{
				$("passOrNot4").innerHTML = "未通过";
			}
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}

function getPlanNameByPlanNo(planNo){
	var str = "";
	var requestURLStr = "<%=contextPath%>/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getPlanNameByPlanNo.act";
	var rtJson = getJsonRs(url, "planNo=" + encodeURIComponent(planNo) );
	//alert(rsText);
	if(rtJson.rtState == "0"){
		str = rtJson.rtData.planName;
	}else{
		alert(rtJson.rtMsrg);
	}
	return str;
}
function getPersonName(seqId){
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/recruit/filter/act/YHHrRecruitFilterAct";
	var url = requestURLStr + "/getPersonName.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		str = rtJson.rtData.userName;
	}else {
		alert(rtJson.rtMsrg); 
	}
	return str;
}
</script>
</head>
<body onload=" doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/hr_manage.gif" width="17" height="17"><span class="big3"> 招聘筛选详细信息</span><br></td>
  </tr>
</table>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td nowrap class="TableContent" colspan="4" align="center">基本信息<div id="endFlagInfo" style='color:red'></div> </td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">应聘者姓名：</td>
    <td class="TableData" ><span id="employeeName"></span> </td>
   <td nowrap align="left" width="120" class="TableContent">计划名称：</td>
    <td class="TableData"><span id="planName"></span> </td>  
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">应聘岗位：</td>
    <td class="TableData" ><span id="position"></span> </td> 
    <td nowrap align="left" width="120" class="TableContent">所学专业：</td>
    <td class="TableData" ><span id="employeeMajor"></span> </td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">联系电话：</td>
    <td class="TableData"><span id="employeePhone"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">发起人：</td>
    <td class="TableData" ><span id="transactorStep"></span> </td> 
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">下一次筛选办理人：</td>
    <td class="TableData"><span id="nextTransaStep"></span>    </td>
    <td nowrap align="left" width="120" class="TableContent">下一次筛选时间：</td>
    <td class="TableData"><span id="nextDateTime"></span>    </td> 
  </tr>
</table>

<div id="transactorStep1Div" style="display: none;">
<br>
<table class="TableBlock" width="90%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤一</td>
  </tr>
 <tr>
    <td nowrap class="TableData">初选时间：</td>
    <td class="TableData"><span id="filterDateTime1"></span> </td>
    <td nowrap class="TableData">初选方式：</td>
    <td class="TableData" ><span id="filterMethod1"></span> </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">初选内容：</td>
    <td class="TableData" colspan=3><span id="firstContent1"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">初选意见：</td>
    <td class="TableData" colspan=3><span id="firstView1"></span> </td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">初选办理人：</td>
    <td class="TableData" ><span id="transactorStep1"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="passOrNot1"></span></td>
  </tr>
  <tr>
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData"><span id="nextTransaStep1"></span></td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData"><span id="nextDateTime1"></span> </td>
  </tr>
</table>
</div>
<div id="transactorStep2Div" style="display: none;">
<br>
<table class="TableBlock" width="90%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤二</td>
  </tr>
 <tr>
    <td nowrap class="TableData">复选时间：</td>
    <td class="TableData"><span id="filterDateTime2"></span></td>
    <td nowrap class="TableData">复选方式：</td>
    <td class="TableData" > <span id="filterMethod2"></span> </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">复选内容：</td>
    <td class="TableData" colspan=3><span id="firstContent2"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">复选意见：</td>
    <td class="TableData" colspan=3><span id="firstView2"></span></td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">复选办理人：</td>
    <td class="TableData" ><span id="transactorStep2"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="passOrNot2"></span></td>
  </tr>
  <tr>
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData"><span id="nextTransaStep2"></span></td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData"><span id="nextDateTime2"></span></td>
  </tr>
</table>
</div>
<div id="transactorStep3Div" style="display: none;">
<br>
<table class="TableBlock" width="90%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤三</td>
  </tr>
 <tr>
    <td nowrap class="TableData">决选时间：</td>
    <td class="TableData"> <span id="filterDateTime3"></span> </td>
    <td nowrap class="TableData">决选方式：</td>
    <td class="TableData" ><span id="filterMethod3"></span></td> 
  </tr>
  <tr>
    <td nowrap class="TableData">决选内容：</td>
    <td class="TableData" colspan=3><span id="firstContent3"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">决选意见：</td>
    <td class="TableData" colspan=3><span id="firstView3"></span></td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">决选办理人：</td>
    <td class="TableData" ><span id="transactorStep3"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="passOrNot3"></span></td>
  </tr>
  <tr>
    <td nowrap class="TableData">下一步骤办理人：</td>
    <td class="TableData"><span id="nextTransaStep3"></span></td>
    <td nowrap class="TableData">下一次筛选时间：</td>
    <td class="TableData"><span id="nextDateTime3"></span> </td>
  </tr>
</table>
</div>
<div id="transactorStep4Div" style="display: none;">
<br>
<table class="TableBlock" width="90%" align="center" id="table1">
  <tr>
    <td nowrap class="TableData" colspan="4" align="center" >筛选步骤四</td>
  </tr>
 <tr>
    <td nowrap class="TableData">加试时间：</td>
    <td class="TableData"><span id="filterDateTime4"></span> </td>
    <td nowrap class="TableData">加试方式：</td>
    <td class="TableData" ><span id="filterMethod4"></span>  </td> 
  </tr>
  <tr>
    <td nowrap class="TableData">加试内容：</td>
    <td class="TableData" colspan=3><span id="firstContent4"></span> </td>
  </tr>
  <tr>
    <td nowrap class="TableData">加试意见：</td>
    <td class="TableData" colspan=3><span id="firstView4"></span></td>
  </tr>  
  <tr>
  	<td nowrap class="TableData">加试办理人：</td>
    <td class="TableData" ><span id="transactorStep4"></span></td>
    <td nowrap class="TableData">是否通过：</td>
    <td class="TableData"><span id="passOrNot4"></span></td>
  </tr>
</table>
</div>
<table class="TableBlock" width="90%" align="center">
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="关闭" class="BigButton" onclick="window.close();" title="关闭窗口">
      </td>
    </tr>
  </table>

</body>
</html>