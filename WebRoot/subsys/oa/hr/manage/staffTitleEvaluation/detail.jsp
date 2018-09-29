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
<title>职称评定详细信息</title>
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
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/titleEvaluation/act/YHHrStaffTitleEvaluationAct/getTitleEvaluationDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		$('byEvaluStaffs').innerHTML = staffNameFunc(data.byEvaluStaffs);
		$('approvePerson').innerHTML = staffNameFunc(data.approvePerson);
		$('getMethod').innerHTML = getTitleEvaluationItem(data.getMethod);
    if(data.reportTime){
      $("reportTime").innerHTML = data.reportTime.substr(0,10);
    }		
    if(data.receiveTime){
      $("receiveTime").innerHTML = data.receiveTime.substr(0,10);
    } 
    if(data.approveNextTime){
      $("approveNextTime").innerHTML = data.approveNextTime.substr(0,10);
    }
    $('approveNext').innerHTML = data.approveNext;
		$("employPost").innerHTML = data.employPost;
		$("employCompany").innerHTML = data.employCompany;
    if(data.startDate){
      $("startDate").innerHTML = data.startDate.substr(0,10);
    } 
    if(data.endDate){
      $("endDate").innerHTML = data.endDate.substr(0,10);
    }
		$("remark").innerHTML = data.remark;
		$("addTime").innerHTML = data.addTime;

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
  var url = contextPath + "/yh/subsys/oa/hr/manage/titleEvaluation/act/YHHrStaffTitleEvaluationAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getTitleEvaluationItem(seqId){
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

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 职称评定详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">姓名：</td>
    <td align="left" class="TableData" width="180"><div id="byEvaluStaffs"></div> </td>
    <td align="left" width="120" class="TableContent">批准人：</td>
    <td align="left" class="TableData" width="180"><div id="approvePerson"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">获取职称：</td>
    <td align="left" class="TableData" width="180"><div id="postName"></div> </td>
    <td align="left" width="120" class="TableContent">获取方式：</td>
    <td align="left" class="TableData Content" width="180"><div id="getMethod"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">申报时间：</td>
    <td align="left" class="TableData" width="180"><div id="reportTime"></div></td>
    
    <td align="left" width="120" class="TableContent">获取时间：</td>
    <td align="left" class="TableData Content" width="180"><div id="receiveTime"></div></td>
  </tr>
  
  <tr>
    <td align="left" width="120" class="TableContent">下次申报时间：</td>
    <td align="left" class="TableData Content" width="180"><div id="approveNextTime"></div></td>
    <td align="left" width="120" class="TableContent">下次申报职称：</td>
    <td align="left" class="TableData" width="180"><div id="approveNext"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">聘用职务：</td>
    <td align="left" class="TableData" width="180"><div id="employPost"></div></td>
    <td align="left" width="120" class="TableContent">聘用单位：</td>
    <td align="left" class="TableData" width="180"><div id="employCompany"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">聘用开始时间：</td>
    <td align="left" class="TableData" width="180"><div id="startDate"></div></td>
    <td align="left" width="120" class="TableContent">聘用结束时间：</td>
    <td align="left" class="TableData" width="180"><div id="endDate"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">评定详情：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="remark"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">登记时间：</td>
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