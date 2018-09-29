<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingmanagelogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingCycleListJson.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"mrName",  width: '20%', text:"名称", render:mrNameFunc},       
         {type:"data", name:"mProposer",  width: '10%', text:"申请人", render:mProposerFunc},
         {type:"data", name:"mStart",  width: '12%', text:"开始时间", render:mStartFunc},
         {type:"data", name:"mRoom",  width: '10%', text:"会议室", render:mRoomFunc},
         {type:"hidden", name:"cycleNo",  width: '10%', text:"会议周期号", render:mRoomFunc},
         {type:"hidden", name:"mStatus",  width: '10%', text:"预约状态"},
         {type:"selfdef", text:"操作", width: '18%',render:optsList}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      $("totalSpan").innerHTML = total;
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      $("spanDiv").style.display = 'none';
      WarningMsrg('无待批周期性会议', 'msrg');
    }
}

function mStatusFunc(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/checkRoom.act?seqId=" + seqId;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    if(data == "0"){
      return "<center>无冲突</center>";
    }else{
      return "<center><a href=javascript:conflictFunc("+data+")><font color='red'>预约冲突</font></a></center>";
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="22" height="18"><span class="big3">&nbsp;待批周期性会议</span>
   </td>
   <td valign="bottom" class="small1"><div id="spanDiv" style="display:'';">共&nbsp;<span class="big4" id="totalSpan"></span>&nbsp;条会议记录</div>
    </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">

</div>
<div id="msrg">
</div>
</body>
</html>