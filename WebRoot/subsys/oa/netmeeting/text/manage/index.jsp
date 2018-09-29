<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文本会议管理</title>
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
  var url = "<%=contextPath%>/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/getNetmeetingInfoListJson.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"userName",  width: '15%', text:"召集人" ,align: 'center' },
       {type:"hidden", name:"toIdStr",  width: '15%', text:"参会人员" ,align: 'center' },
       {type:"data", name:"toId",  width: '15%', text:"参会人员" ,align: 'center' ,render:staffNameFunc},
       {type:"data", name:"subject",  width: '15%', text:"会议主题" ,align: 'center' },
       {type:"data", name:"beginTime",  width: '20%', text:"开始时间" ,align: 'center' ,render:subStringTime },
       {type:"data", name:"stop",  width: '15%', text:"当前状态" ,align: 'center' ,render:switchStop },
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
    WarningMsrg('没有文本会议', 'msrg');
  }
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

function switchStop(cellData, recordIndex, columIndex){
  var stop = this.getCellData(recordIndex,"stop");
  switch(stop){
    case '0' : stop = '尚未召开';break;
    case '2' : stop = '<font color=\'#00AA00\'><b>会议进行中 </font>';break;
    case '3' : stop = '<font color=\'#FF0000\'><b>已结束 </font>';break;
  }
  return stop;
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var stop = this.getCellData(recordIndex,"stop");
  var temp = "<center>" + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;";
  switch(stop){
	  case '0' : temp = temp + "<a href=javascript:doStop(" + seqId + ",0)>立即召开</a>&nbsp;";break;
	  case '2' : temp = temp + "<a href=javascript:doStop(" + seqId + ",2)>立即结束</a>&nbsp;";break;
	  case '3' : temp = temp + "<a href=javascript:doStop(" + seqId + ",3)>继续开会</a>&nbsp;";break;
	}
	if(stop != '3')
		  temp = temp + "<a href=javascript:addPerson(" + seqId + ",'" + this.getCellData(recordIndex,"subject") + "',[" + this.getCellData(recordIndex,"toIdStr") + "])>加人</a>&nbsp;";
  temp = temp + "<a href=javascript:history(" + seqId + ",'" + this.getCellData(recordIndex,"subject") + "')>会议记录</a>" + "</center>";
  return temp;
}

//删除
function deleteSingle(seqId){
	if(confirm("确定要删除该项文本会议吗？会议记录将被删除！")){
	  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/deleteNetmeeting.act";
	  var rtJson = getJsonRs(url, "seqId=" + seqId);
	  if (rtJson.rtState == "0") {
	    pageMgr.refreshAll();
	  } else {
	    alert(rtJson.rtMsrg); 
	  }
	}
}

//立即结束
function doStop(seqId,type){
  var url = contextPath + "/yh/subsys/oa/netmeeting/text/act/YHNetmeetingAct/doStop.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId + "&type=" + type);
  if (rtJson.rtState == "0") {
	  pageMgr.refreshAll();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function addPerson(seqId,subject,toIdStr){
  location.href = "<%=contextPath%>/subsys/oa/netmeeting/text/manage/addPerson.jsp?seqId=" + seqId + "&subject=" + subject + "&toIdStr=" + toIdStr;
}

function newNetmeeting(){
	location.href = "<%=contextPath%>/subsys/oa/netmeeting/text/manage/new.jsp";
}

function history(seqId,subject){
  var URL = "<%=contextPath%>/subsys/oa/netmeeting/text/detail.jsp?subject=" + subject + "&seqId=" + seqId;
  newWindow(URL,'820', '500');
}

function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absMiddle" width="17"><span class="big3">&nbsp;创建新的文本会议     </span>
   </td>
 </tr>
</table>
<div align="center">
      <input type="button" class="BigButtonC" value="新建文本会议" onclick="newNetmeeting()">
</div>
<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify.gif" align="absMiddle" width="17"><span class="big3">&nbsp;管理已创建的文本会议   </span>
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