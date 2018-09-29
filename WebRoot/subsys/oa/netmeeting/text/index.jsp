<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网络会议</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/netmeeting/text/manage/js/util.js"></script>
<script> 
var pageMgr = null;
function doInit(){
	getNetmeetingState();
  var url = "<%=contextPath%>/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/getNetmeetingInfoList.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"createUserId",  width: '15%', text:"召集人" ,align: 'center' },
       {type:"data", name:"subject",  width: '20%', text:"会议主题" ,align: 'center' ,render:addString},
       {type:"data", name:"toId",  width: '20%', text:"参会人员" ,align: 'center' ,render:staffNameFunc},
       {type:"data", name:"beginTime",  width: '20%', text:"开始时间" ,align: 'center' ,render:subStringTime },
       {type:"selfdef", text:"操作", width: '20%',render:opts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('没有网络会议', 'msrg');
  }
}

function addString(cellData, recordIndex, columIndex){
	return cellData ;
}

// 单位员工名称
function staffNameFunc(cellData, recordIndex, columIndex){
  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function subStringTime(cellData, recordIndex, columIndex){
	var beginTime = this.getCellData(recordIndex,"beginTime");
	return beginTime.substring(0,19);
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
	var count = 0;
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/joinNeetingCount.act?seqId=" + seqId;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
	  var count = rtJson.rtData.count;
  } else {
    alert(rtJson.rtMsrg); 
  }
  return "<center><a href=javascript:joinMeeting(" + recordIndex + ")>进入文本会议(" + count + "人)</a></center>";
}

function joinMeeting(recordIndex){
	var seqId = pageMgr.getCellData(recordIndex,"seqId");
	var subject = pageMgr.getCellData(recordIndex,"subject");
	var createUserId = pageMgr.getCellData(recordIndex,"createUserId");
  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/joinNeeting.act?seqId=" + seqId;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
	  location.href = "<%=contextPath%>/subsys/oa/netmeeting/text/meeting.jsp?seqId=" + seqId + "&subject=" + subject + "&createUserId=" + createUserId;
  } else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify.gif" align="absMiddle" width="17"><span class="big3">&nbsp;网络会议列表    </span>
   </td>
 </tr>
</table>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
</table>
</div><p><br>
<div id="msrg">
</div>
</body>
</html>