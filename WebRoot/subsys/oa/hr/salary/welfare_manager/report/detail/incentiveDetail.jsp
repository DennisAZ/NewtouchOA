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
<title>奖惩详细信息</title>
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
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getIncentiveDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		$('incentiveItem').innerHTML = getIncentiveItem(data.incentiveItem);
		$('incentiveType').innerHTML = incentiveTypeFunc(data.incentiveType);
		$('staffName').innerHTML = staffNameFunc(data.staffName);
		$("incentiveAmount").innerHTML = insertKiloSplit(data.incentiveAmount,2);
		$("salaryMonth").innerHTML = data.salaryMonth;
		$("addScore").innerHTML = data.addScore;
		$("reduceScore").innerHTML = data.reduceScore;
		$("incentiveDescription").innerHTML = data.incentiveDescription;
		$("addTime").innerHTML = data.addTime;
		$("remark").innerHTML = data.remark;
		if(data.incentiveTime){
			$("incentiveTime").innerHTML = data.incentiveTime.substr(0,10);
		}
		if(data.yearScore == "0"){
			$("yearScore").innerHTML = "";
		}else{
			$("yearScore").innerHTML = data.yearScore;
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
function getIncentiveItem(seqId){
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
/**
 * 奖惩属性
 * @param cellData
 * @return
 */
function incentiveTypeFunc(cellData){
	var str = "";
	if(cellData == 1){
		str = "奖励";
		$("reduceDiv").innerHTML = "加分";
		$("addScore").show();
	}
	if(cellData == 2){
		str = "惩罚";
		$("reduceDiv").innerHTML = "减分";
		$("reduceScore").show();
	}
	return str ;
}
/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(cellData){
  var url = contextPath + "/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
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
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 奖惩详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td nowrap align="left" width="120" class="TableContent">姓名：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="staffName"></div> </td>
    <td nowrap align="left" width="120" class="TableContent">奖惩项目：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="incentiveItem"></div> </td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">奖惩日期：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="incentiveTime"></div> </td>
    <td nowrap align="left" width="120" class="TableContent">工资月份：</td>
    <td align="left" class="TableData Content" width="180"><div id="salaryMonth"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">奖惩属性：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="incentiveType"></div></td>
    
    <td nowrap align="left" width="120" class="TableContent"><div id="addDiv"> </div> <div id="reduceDiv"> </div></td>
    <td align="left" class="TableData Content" width="180"><div id="addScore" style="display: none"></div> <div id="reduceScore" style="display: none"></div>  </td>
  </tr>
  
  <tr>
    <td nowrap align="left" width="120" class="TableContent">奖惩金额：</td>
    <td align="left" class="TableData Content" width="180"><div id="incentiveAmount"></div></td>
    <td nowrap align="left" width="120" class="TableContent">年终奖惩系数：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="yearScore"></div></td>
  </tr>
  
  <tr>
    <td nowrap align="left" width="120" class="TableContent">奖惩说明：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="incentiveDescription"></div></td>
    <td nowrap align="left" width="120" class="TableContent">登记日期：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="addTime"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">附件文档：</td>
   <td nowrap align="left" class="TableData" colspan="3">
			<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
    </td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">备注：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="remark"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
       <input type="button" value="返回" class="BigButton" onClick="window.history.go(-1);">&nbsp;&nbsp;
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>