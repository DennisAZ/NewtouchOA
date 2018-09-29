<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String mStatus = request.getParameter("mStatus");
	if(YHUtility.isNullorEmpty(mStatus)){
		mStatus = "0";
	}
	
	String statusSpan = "";
	if("0".equals(mStatus.trim())){
		statusSpan = "待批会议";
	}
	if("1".equals(mStatus.trim())){
		statusSpan = "已准会议";
	}
	if("2".equals(mStatus.trim())){
		statusSpan = "进行中会议";
	}
	if("3".equals(mStatus.trim())){
		statusSpan = "未批准会议";
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会议申请</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/applayMeetinglogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var pageMgr = null;
var status = "<%=mStatus.trim() %>";
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getAppMeetingListJson.act?mStatus=<%=mStatus%>";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"mName",  width: '10%', text:"名称", render:alignFunc},       
         {type:"data", name:"mProposer",  width: '10%', text:"申请人", render:mProposerFunc},
         {type:"data", name:"mAttendee",  width: '25%', text:"出席人员", render:mAttendeeFunc},
         {type:"data", name:"mStart",  width: '10%', text:"开始时间", render:mDateFunc},
         {type:"data", name:"mEnd",  width: '10%', text:"结束时间", render:mDateFunc},
         {type:"data", name:"mRoom",  width: '10%', text:"会议室", render:mRoomFunc},
         {type:"hidden", name:"mStatus",  width: '10%', text:"申请状态", render:alignFunc},
         {type:"hidden", name:"mAttendeeOut", text:"未出席人员", render:alignFunc},
         {type:"hidden", name:"runId"},
         {type:"hidden", name:"flowId"},
         {type:"selfdef", text:"操作", width: '15%',render:optsList}]
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
      WarningMsrg("无<%=statusSpan %>", 'msrg');
    }
}



</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="22" height="18"><span class="big3" id="statusSpan">&nbsp; <%=statusSpan %></span>
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