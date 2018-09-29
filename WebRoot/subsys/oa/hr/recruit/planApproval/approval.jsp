<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
  String flag = request.getParameter("flag");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审批招聘计划</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var flag = "<%=flag%>";

function doInit(){
	if(flag == 1){
		$("appButton").value = "批准";
		$("appButton").title = "批准";
	}
	else{
		$("appButton").value = "不批准";
		$("appButton").title = "不批准";
	}
	setDate();
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/plan/act/YHHrRecruitPlanAct/getRecruitPlanDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		$("createUser").innerHTML = staffNameFunc(data.createUserId);
		$("approvePerson").innerHTML = staffNameFunc(data.approvePerson);
		$('planDitch').innerHTML = getPlanItem(data.planDitch);
		//$("requDeptDesc").innerHTML = deptFunc(data.requDept);
    if(data.startDate){
      $("startDate").innerHTML = data.startDate.substr(0,10);
    }		
    if(data.endDate){
      $("endDate").innerHTML = data.endDate.substr(0,10);
    }
    if(data.approveDate){
      $("approveDate").value = data.approveDate.substr(0,10);
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

//日期
function setDate(){
  var date1Parameters = {
     inputId:'approveDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
}

/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(cellData){
  var url = contextPath + "/yh/subsys/oa/hr/recruit/plan/act/YHHrRecruitPlanAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function deptFunc(cellData, recordIndex, columIndex){
  var url = contextPath + "/yh/subsys/oa/hr/recruit/plan/act/YHHrRecruitPlanAct/getDeptName.act";
  var rtJson = getJsonRs(url, "deptIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getPlanItem(seqId){
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

function approval(){
	var url = contextPath + "/yh/subsys/oa/hr/recruit/plan/act/YHHrRecruitPlanAct/doApproval.act?seqId=<%=seqId%>&flag="+flag+"&approveDate="+$('approveDate').value+"&approveComment="+encodeURIComponent($('approveComment').value)+"&createUserId="+$('createUserId').value;
	var rtJsons = getJsonRs(url);
  var prc = rtJsons.rtData;
  if(rtJsons.rtState == '0'){
	  history.go(-1);
  }else{
    alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3">审批招聘计划</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">名称：</td>
    <td align="left" class="TableData" width="180"><div id="planName"></div> </td>
    <td align="left" width="120" class="TableContent">发起人：</td>
    <td align="left" class="TableData" width="180"><div id="createUser"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">招聘渠道：</td>
    <td align="left" class="TableData" width="180"><div id="planDitch"></div> </td>
    <td align="left" width="120" class="TableContent">预算费用：</td>
    <td align="left" class="TableData Content" width="180"><span id="planBcws"></span>元</td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">招聘人数：</td>
    <td align="left" class="TableData" width="180"><span id="planRecrNo"></span>人</td>
    <td align="left" width="120" class="TableContent">登记日期：</td>
    <td align="left" class="TableData Content" width="180"><div id="registerTime"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">开始日期：</td>
    <td align="left" class="TableData" width="180"><div id="startDate"></div></td>
    <td align="left" width="120" class="TableContent">结束日期：</td>
    <td align="left" class="TableData Content" width="180"><div id="endDate"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">招聘说明：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="recruitDirection"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">招聘备注：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="recruitRemark"></div></td>
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
    <td align="left" width="120" class="TableContent">审批人：</td>
    <td align="left" class="TableData" colspan="3"><div id="approvePerson"></div></td>
  </tr>
    <td align="left" width="120" class="TableContent">审批日期：</td>
    <td align="left" class="TableData" colspan="3">
      <input type="text" name="approveDate" id="approveDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">审批意见：</td>
    <td align="left" class="TableData Content" colspan="3">
      <textarea id="approveComment" name="approveComment" cols="78" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="hidden" id="createUserId" name="createUserId">
      <input type="button" id="appButton" value="批准" class="BigButton" onClick="approval();" title="批准">&nbsp;&nbsp;&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onClick="history.go(-1)" title="返回">
    </td>
  </tr>
</table>
</body>
</html>