<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
 <%
  String mStatus = request.getParameter("mStatus") == null ? "" :  request.getParameter("mStatus");
  String name = "";
  if (mStatus.equals("0")) {
    name = "待批会议";
  }
  if (mStatus.equals("1")) {
    name = "已准会议";
  }
  if (mStatus.equals("2")) {
    name = "进行中会议";
  }
  if (mStatus.equals("3")) {
    name = "未批准会议";
  }
  if (mStatus.equals("4")) {
    name = "已结束会议";
  }
%>
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script> 
var mStatus = "<%=mStatus%>";
var name = "<%=name%>";
var pageMgr = null;
function doInit(){
  autoBeginEnd();
  var url = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingListJson.act?mStatus="+mStatus;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"mrName",  width: '16%', text:"名称", render:mrNameFunc},       
         {type:"data", name:"mProposer",  width: '10%', text:"申请人", render:mProposerFunc},
         {type:"data", name:"mStart",  width: '14%', text:"开始时间", render:mStartFunc},
         
         {type:"data", name:"mRoom",  width: '10%', text:"会议室", render:mRoomFunc},
         <%if("0".equals(mStatus)){%>
         {type:"data", name:"mStatus",  width: '10%', text:"预约状态", render:mStatusFunc},
         <%}else{%>
         {type:"hidden", name:"mStatus",  width: '8%', text:"预约状态", render:meetingCenterFunc},
         <%}%>
         {type:"hidden", name:"runId"},
         {type:"hidden", name:"flowId"},
         {type:"selfdef", text:"操作", width: '34%',render:opts}]
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
      WarningMsrg('无'+name, 'msrg');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="22" height="18"><span class="big3">&nbsp;<%=name%></span>
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