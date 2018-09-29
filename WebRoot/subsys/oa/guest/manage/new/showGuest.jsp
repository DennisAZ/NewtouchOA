<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String seqId = request.getParameter("seqId")==null ? "" :request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>添加贵宾信息</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script Language="JavaScript"> 
function doOnload() {
  var seqId = '<%=seqId%>';
  getGuestById(seqId);
}
function getGuestById(seqId){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/getGuestById.act?seqId=" + seqId; 
  var json=getJsonRs(requestURL); 
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  if(prc.seqId){
    var seqId = prc.seqId;
    //$("seqId").value = prc.seqId;
    $("guestNum").innerHTML = prc.guestNum;
    $("guestTypeDesc").innerHTML = prc.guestTypeDesc;
    $("guestName").innerHTML = prc.guestName;
 
    var guestDinerStr = "否";
    if(prc.guestDiner=='1'){
      guestDinerStr = "是";
    }
    $("guestDiner").innerHTML = guestDinerStr;
    $("guestUnit").innerHTML = prc.guestUnit;
    $("guestPhone").innerHTML = prc.guestPhone;
    $("guestAttendTime").innerHTML = prc.guestAttendTime.substr(0,10);
    $("guestLeaveTime").innerHTML = prc.guestLeaveTime.substr(0,10);
    //$("guestDept").innerHTML = prc.guestDept;
    //$("guestDeptName").innerHTML = prc.deptName;
   // $("guestCreator").innerHTML = prc.guestCreator;
    $("guestCreator").innerHTML = prc.guestCreatorName;
    $("guestNote").innerHTML = prc.guestNote;
    $("attachmentId").value = prc.attachmentId;
    $("attachmentName").value = prc.attachmentName;
    doOnloadFile();
  }
}
var  selfdefMenu = {
  	office:["downFile","dump","read"], 
    img:["downFile","dump","play"],  
    music:["downFile","dump","play"],  
    video:["downFile","dump","play"], 
    others:["downFile","dump"]
	}

function doOnloadFile(seqId){
  var attr = $("attr");
  attachMenuSelfUtil(attr,"guest",$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu);
}
</script>
</head>
<body  topmargin="5" onload="doOnload();">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/logistics.gif" width="24" height="24"><span class="big3">贵宾详细信息</span><br>
    </td>
  </tr>
</table>
<table class="TableBlock" width="100%">
  <tr class="TableLine2">
      <td nowrap align="left" width="90" class="TableContent">来宾编号：</td>
      <td nowrap align="left" class="TableData" colspan="2" id="guestNum"></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="90" class="TableContent">来宾类型：</td>
      <td nowrap align="left" class="TableData" colspan="2" id="guestTypeDesc"></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="90" class="TableContent">来宾姓名：</td>
      <td nowrap align="left" class="TableData" colspan="2" id="guestName"></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="90" class="TableContent">来宾所在单位：</td>
      <td nowrap align="left" class="TableData" colspan="2" id="guestUnit"></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="90" class="TableContent">来宾联系电话：</td>
      <td nowrap align="left" class="TableData" colspan="2" id="guestPhone"></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="90" class="TableContent">来会时间：</td>
      <td nowrap align="left" class="TableData" colspan="2" id="guestAttendTime"></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="90" class="TableContent">离会时间：</td>
      <td nowrap align="left" class="TableData" colspan="2" id="guestLeaveTime"></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="90" class="TableContent">是否用餐：</td>
      <td align="left" class="TableData" colspan="2" id="guestDiner"></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="90" class="TableContent">数据录入人：</td>
      <td align="left" class="TableData" colspan="2" id="guestCreator"></td>
  </tr>
  <tr class="TableLine2">
      <td nowrap align="left" width="90" class="TableContent">备注：</td>
      <td align="left" class="TableData" colspan="2" id="guestNote"></td>
  </tr>
  <tr class="TableLine1">
      <td nowrap align="left" width="90" class="TableContent">附件：</td>
      <td class="TableData" >
      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
           <input type="hidden" id="moduel" name="moduel" value="guest">
        <span id="attr">无附件</span>
      </td>
  </tr>
  <tr align="center" class="TableControl">
      <td colspan="3">
        <input type="button" value="打印" class="BigButton" onclick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
  </tr>
</table>
</body>
</html>
