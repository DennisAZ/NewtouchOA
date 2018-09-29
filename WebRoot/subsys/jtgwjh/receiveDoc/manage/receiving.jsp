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
<title>发送详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/src/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/jtgwjh/sendDoc/js/logic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/util.js"></script>
<script type="text/javascript">
var requestUrl = "<%=contextPath%>/yh/core/esb/client/act/YHEsbConfigAct";
function doInit(){
  var url =  requestUrl + "/isOnline.act";
  var rtJson = getJsonRsAsyn(url , null , updateState1);
  
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/getReceiving.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"message",  width: '10%', text:"公文标题" ,align: 'center'},
       {type:"data", name:"fromId",  width: '10%', text:"发送单位" ,align: 'center'},
       {type:"data", name:"status",  width: '5%', text:"状态" ,align: 'center'},
       {type:"data", name:"guid",  width: '10%', text:"任务ID" ,align: 'center'}
       ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('无下载信息', 'msrg');
  }
}

function renderStatus(cellData, recordIndex, columIndex){
  var tasks = this.getCellData(recordIndex,"tasks");
  var complete = this.getCellData(recordIndex,"complete");
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<div><a href=javascript:detailSendInfo(" + seqId + ")>"+complete+"/"+tasks+"</a>";
}



function reSend(guid, toId){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/docSend/act/YHDocSendAct/sendDocInfoTasks.act";
  var rtJson = getJsonRs(url, "guid="+guid+"&toId="+toId);
  if(rtJson.rtState == "0"){
    alert("已重新加入发送队列中！");
    window.location.reload();
  }
  else{
    alert(rtJson.rtMsrg);
  }
}

function updateState(rtJson) {
  var isline = false;
  if(rtJson.rtState == "0"){
    if (rtJson.rtData) {
      isline = true;
    } 
  } else {
    isline = false;
  }
  if (isline) {
    $('state').update("<img  src=\"../images/a1.gif\" align=\"absmiddle\">已连接");
  } else {
    $('state').update("<img  src=\"../images/a0.gif\" align=\"absmiddle\">未连接");
  }
}

function myrefresh(){
  window.location.reload();
}

setTimeout('myrefresh()',1000*10); //指定1秒刷新一次
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 下载中公文</span><br>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="350" align="center">
  <tr class="TableData">
    <td nowrap class="TableData" align="center" style="width:200px;">数据交换平台连接状态：</td>
    <td align="center">
       <span id="state"><img  src="../images/a0.gif" align="absmiddle">未连接</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100%;" >
</div>
<div id="delOpt" style="display:none">
</div>

<div id="msrg">
</div>

</body>
</html>