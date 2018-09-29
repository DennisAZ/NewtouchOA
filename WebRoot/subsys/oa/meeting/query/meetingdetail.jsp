<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议详细信息</title>
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
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingDetail.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    getOrgDescFunc();
    $('mStatus').innerHTML = mStatusFunc(data.mStatus);
    $('mProposer').innerHTML = mProposerFunc(data.mProposer);
    $('mManager').innerHTML = mProposerFunc(data.mManager);
    $('mRoom').innerHTML = meetingRoomNameFunc(data.mRoom);
    if(data.mStatus == "2" || data.mStatus == "4"){
      $("mAttendeeNotDiv").style.display = '';
    }
    if(data.attachmentId == ""){
      $("attr").innerHTML = "无附件";
    }else{
      $("returnAttId").value = data.attachmentId;
      $("returnAttName").value = data.attachmentName;
      var selfdefMenu = {
          office:["downFile","read","edit"], 
          img:["downFile","play"],  
          music:["downFile","play"],  
          video:["downFile","play"], 
          others:["downFile"]
      }
      attachMenuSelfUtil("attr","meeting",$('returnAttName').value ,$('returnAttId').value, '','',${param.seqId},selfdefMenu);
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getOrgDescFunc(){
  if($("privId") && $("privId").value.trim()){
    bindDesc([{cntrlId:"privId", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
  }
  if($("toId") && $("toId").value.trim() && $("toId").value != "0" && $("toId").value != "ALL_DEPT"){
    bindDesc([{cntrlId:"toId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }else if($("toId") && ($("toId").value == "0" || $("toId").value == "ALL_DEPT")){
    $("toId").value = "0";
    $("toIdDesc").innerHTML = "全体部门";
  }
  if($("secretToId") && $("secretToId").value.trim()){
    bindDesc([{cntrlId:"secretToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if($("mAttendee") && $("mAttendee").value.trim()){
    bindDesc([{cntrlId:"mAttendee", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if($("recorder") && $("recorder").value.trim() && $("recorder").value != "0" && $("recorder").value != "ALL_DEPT"){
    bindDesc([{cntrlId:"recorder", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }else if($("recorder") && ($("recorder").value == "0" || $("recorder").value == "ALL_DEPT")){
    $("recorder").value = "0";
    $("recorderDesc").value = "全体部门";
  }
  if($("equipmentIdStr") && $("equipmentIdStr").value.trim()){
    bindDesc([{cntrlId:"equipmentIdStr", dsDef:"MEETING_EQUIPMENT,SEQ_ID,EQUIPMENT_NAME"}]);
  }
}

//删除附件
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var attachNameOld = $('returnAttName').value; 
  var attachIdOld = $('returnAttId').value; 
  var attachNameArrays = attachNameOld.split("*"); 
  var attachIdArrays = attachIdOld.split(","); 
  var attaName = ""; 
  var attaId = ""; 
  for (var i = 0 ; i < attachNameArrays.length ; i++) {
    if (!attachIdArrays[i] || attachIdArrays[i] == attachId) { 
    	continue; 
    }
    attaName += attachNameArrays[i] + "*"; 
    attaId += attachIdArrays[i] + ","; 
  }
  $('returnAttId').value = attaId; 
  $('returnAttName').value = attaName;
  var requestURL = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct";
  var url = requestURL + "/delFloatFile.act?delAttachId=" + attaId +"&delAttachName=" + encodeURIComponent(attaName) + "&seqId=" + ${param.seqId};
  var json = getJsonRs(url);
	var json=getJsonRs(url);
	if(json.rtState =='1'){
		alert(json.rtMsrg);
		return false;
	}else{
	  prcsJson=json.rtData;
		var updateFlag=prcsJson.updateFlag;
		if(updateFlag == "1"){
		  return true;
		  window.location.reload();
		}else{
			return false;
		}
	}
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="17" height="17"><span class="big3"> 会议详细信息</span><br>
    </td>
  </tr>
</table>
<br>

<table class="TableBlock" width="100%">
  <tr class="TableLine1">
      <td nowrap align="left" width="120" class="TableContent">会议编号：</td>
      <td nowrap align="left" class="TableData"><div id="seqId"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">会议名称：</td>
      <td nowrap align="left" class="TableData"><div id="mName"></div></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="120" class="TableContent">会议主题：</td>
      <td nowrap align="left" class="TableData"><div id="mTopic"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">会议描述：</td>
      <td align="left" class="TableData Content"><div id="mDesc"></div></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="120" class="TableContent">申请人：</td>
      <td nowrap align="left" class="TableData"><div id="mProposer"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">申请时间：</td>
      <td nowrap align="left" class="TableData"><div id="mRequestTime"></div></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="120" class="TableContent">出席人员(内部)：</td>
      <td align="left" class="TableData"><div id="mAttendeeDesc"></div></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="120" class="TableContent">出席人员(外部)：</td>
      <td align="left" class="TableData"><div id="mAttendeeOut"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">发布范围（部门）：</td>
      <td nowrap align="left" class="TableData"><div id="toIdDesc"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">发布范围（角色）：</td>
      <td nowrap align="left" class="TableData"><div id="privIdDesc"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">发布范围（人员）：</td>
      <td nowrap align="left" class="TableData"><div id="secretToIdDesc"></div></td>
  </tr>      
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">开始时间：</td>
      <td nowrap align="left" class="TableData"><div id="mStart"></div></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="120" class="TableContent">结束时间：</td>
      <td nowrap align="left" class="TableData"><div id="mEnd"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">会议室：</td>
      <td nowrap align="left" class="TableData"><div id="mRoom"></div></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">会议室设备：</td>
      <td nowrap align="left" class="TableData"><div id="equipmentIdStrDesc"></div></td>
  </tr>  
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">会议室纪要员：</td>
      <td nowrap align="left" class="TableData"><div id="recorderDesc"></div></td>
  </tr>  
  <tr class="TableLine2">
      <td nowrap align="left" width="120" class="TableContent">会议室管理员：</td>
      <td nowrap align="left" class="TableData"><div id="mManager"></div></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="120" class="TableContent">状态：</td>
      <td nowrap align="left" class="TableData"><div id="mStatus"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">附件文档：</td>
    <td nowrap align="left" class="TableData">
    	<input type = "hidden" id="returnAttId" name="returnAttId"></input>
     	<input type = "hidden" id="returnAttName" name="returnAttName"></input>
      <span id="attr"></span>
    </td>
  </tr>
 <tr class="TableLine2" id="mAttendeeNotDiv" style="display:none">
      <td nowrap align="left" width="120" class="TableContent">缺席人员：</td>
      <td nowrap align="left" class="TableData"><div id="mAttendeeNot"></div></td>
 </tr>
  <tr align="center" class="TableControl">
      <td colspan="2">
        <input type="hidden" name="toId" id="toId" value=""> 
        <input type="hidden" name="privId" id="privId" value="">  
        <input type="hidden" name="secretToId" id="secretToId" value="">   
        <input type="hidden" name="mAttendee" id="mAttendee" value="">  
        <input type="hidden" name="recorder" id="recorder" value="">  
        <input type="hidden" name="equipmentIdStr" id="equipmentIdStr" value="">   
        <input type="button" value="打印" class="BigButton" onClick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
  </tr>
</table>
</body>
</html>