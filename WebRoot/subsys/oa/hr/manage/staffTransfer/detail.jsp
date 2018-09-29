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
<title>人事调动详细信息</title>
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
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/transfer/act/YHHrStaffTransferAct/getTransferDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		getOrgDescFunc();
		$('transferPerson').innerHTML = staffNameFunc(data.transferPerson);
		$('transferType').innerHTML = getTransferItem(data.transferType);
    if(data.transferDate){
      $("transferDate").innerHTML = data.transferDate.substr(0,10);
    }		
    if(data.transferEffectiveDate){
      $("transferEffectiveDate").innerHTML = data.transferEffectiveDate.substr(0,10);
    } 
    $('tranCompanyBefore').innerHTML = data.tranCompanyBefore;
    $("tranCompanyAfter").innerHTML = data.tranCompanyAfter;
		$("tranPositionBefore").innerHTML = data.tranPositionBefore;
		$("tranPositionAfter").innerHTML = data.tranPositionAfter;
		$("tranPositionAfter").innerHTML = data.tranPositionAfter;
		$("materialsCondition").innerHTML = data.materialsCondition;
		$("tranReason").innerHTML = data.tranReason;
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
  var url = contextPath + "/yh/subsys/oa/hr/manage/transfer/act/YHHrStaffTransferAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getTransferItem(seqId){
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
  bindDesc([{cntrlId:"tranDeptBefore", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  bindDesc([{cntrlId:"tranDeptAfter", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 人事调动详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">姓名：</td>
    <td align="left" class="TableData" width="180"><div id="transferPerson"></div> </td>
    <td align="left" width="120" class="TableContent">调动类型：</td>
    <td align="left" class="TableData" width="180"><div id="transferType"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">调动日期：</td>
    <td align="left" class="TableData" width="180"><div id="transferDate"></div> </td>
    <td align="left" width="120" class="TableContent">调动生效日期：</td>
    <td align="left" class="TableData Content" width="180"><div id="transferEffectiveDate"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">调动前单位：</td>
    <td align="left" class="TableData" width="180"><div id="tranCompanyBefore"></div></td>
    
    <td align="left" width="120" class="TableContent">调动后单位：</td>
    <td align="left" class="TableData Content" width="180"><div id="tranCompanyAfter"></div></td>
  </tr>
  
  <tr>
    <td align="left" width="120" class="TableContent">调动前职务：</td>
    <td align="left" class="TableData Content" width="180"><div id=tranPositionBefore></div></td>
    <td align="left" width="120" class="TableContent">调动后职务：</td>
    <td align="left" class="TableData" width="180"><div id="tranPositionAfter"></div></td>
  </tr>
  
  <tr>
    <td align="left" width="120" class="TableContent">调动前部门：</td>
    <td align="left" class="TableData" width="180"><div id="tranDeptBeforeDesc"></div></td>
    <td align="left" width="120" class="TableContent">调动后部门：</td>
    <td align="left" class="TableData" width="180"><div id="tranDeptAfterDesc"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">调动手续办理：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="materialsCondition"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">调动原因：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="tranReason"></div></td>
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
      <input type="hidden" id="tranDeptBefore" name="tranDeptBefore">
      <input type="hidden" id="tranDeptAfter" name="tranDeptAfter">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>