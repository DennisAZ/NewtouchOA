<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String mName = request.getParameter("mName")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("mName"));
  String mProposer = request.getParameter("mProposer")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("mProposer"));
  String beginDate = request.getParameter("beginDate") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("beginDate"));
  String endDate = request.getParameter("endDate") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("endDate"));
  String mRoom = request.getParameter("mRoom") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("mRoom"));
  String mStatus = request.getParameter("mStatus") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("mStatus"));
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingSearchlogic.js"></script>
<script type="text/javascript">
var loginUserId = <%=loginUser.getSeqId()%>;
var pageMgr = null;
function doInit(){
  var param = "";
  param =  "mName="+encodeURI('<%=mName%>')+"&mProposer="+encodeURI('<%=mProposer%>')+"&beginDate="+encodeURI('<%=beginDate%>')+"&endDate="+encodeURI('<%=endDate%>')+"&mRoom="; 
  param +=  encodeURI('<%=mRoom%>')+"&mStatus="+encodeURI('<%=mStatus%>')+"";
  var url =  contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/getSearchMeeting.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"mName",  width: '10%', text:"会议名称", render:meetingCenterFunc},       
         {type:"data", name:"mProposer",  width: '10%', text:"申请人", render:mProposerFunc},
         {type:"data", name:"mAttendee",  width: '35%', text:"出席人员", render:mAttendeeFunc},
         {type:"data", name:"mStart",  width: '10%', text:"开始时间", render:mStartFunc},
         {type:"data", name:"mStatus",  width: '8%', text:"会议状态", render:mStatusFunc},
         {type:"data", name:"aAttendeeOut",  width: '8%', text:"链接", render:detailFunc},
         {type:"data", name:"mRoom",  width: '10%', text:"会议室",render:meetingRoomNameFunc}, 
         {type:"hidden", name:"recorder"},
         {type:"hidden", name:"manager"},
         {type:"selfdef", text:"会议纪要", width: '8%',render:opts}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      count = total;
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无符合条件的会议', 'msrg');
      showCntrl('delOpt');
    }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 查询结果</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>

<div id="msrg">
</div>

<div id="delOpt" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/meeting/query/index.jsp';">&nbsp;&nbsp;
</div>
<input type="hidden" value="" name="mAttendee" id="mAttendee">
<input type="hidden" value="" name="mAttendeeDesc" id="mAttendeeDesc">
</form>
</body>
</html>