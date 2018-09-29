<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String mName = request.getParameter("mName")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("mName"));
  String mProposer = request.getParameter("mProposer")== null ? "" :   YHUtility.encodeSpecial(request.getParameter("mProposer"));
  String beginDate = request.getParameter("beginDate") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("beginDate"));
  String endDate = request.getParameter("endDate") == null ? "" :   YHUtility.encodeSpecial(request.getParameter("endDate"));
  String keyWord1 = request.getParameter("keyWord1") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("keyWord1"));
  String keyWord2 = request.getParameter("keyWord2") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("keyWord2"));
  String keyWord3 = request.getParameter("keyWord3") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("keyWord3"));
  String mRoom = request.getParameter("mRoom") == null ? "" :  YHUtility.encodeSpecial(request.getParameter("mRoom"));

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
var pageMgr = null;
function doInit(){
  var param = "";
  param = "mName=" + encodeURIComponent('<%=mName%>');
  param += "&mProposer=" + encodeURIComponent('<%=mProposer%>');
  param += "&beginDate=" + encodeURIComponent('<%=beginDate%>');
  param += "&endDate=" + encodeURIComponent('<%=endDate%>');
  param += "&mRoom=" + encodeURIComponent('<%=mRoom%>');
  param += "&keyWord1=" + encodeURIComponent('<%=keyWord1%>');
  param += "&keyWord2=" + encodeURIComponent('<%=keyWord2%>');
  param += "&keyWord3=" + encodeURIComponent('<%=keyWord2%>');
  var url =  contextPath + "/yh/subsys/oa/meeting/act/YHMeetingAct/queryMeetingSummary.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"mName",  width: '10%', text:"会议名称", render:meetingCenterFunc},       
         {type:"data", name:"mProposer",  width: '10%', text:"申请人", render:mProposerFunc},
         {type:"data", name:"mStart",  width: '10%', text:"开始时间", render:mStartFunc},
         {type:"data", name:"mEnd",  width: '10%', text:"结束时间", render:mStartFunc},
         {type:"selfdef", text:"查看纪要", width: '8%',render:showSummaryFunc}]
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
      //WarningMsrg('无符合条件的会议', 'msrg');
    	$("msrg").show();
    }
    if(!total){
      $("delOpt").style.display = "none";
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
<div id="delOpt" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/meeting/summary/index.jsp';">&nbsp;&nbsp;
</div>

<div id="msrg" style="display: none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的会议</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="javascript:location.href='<%=contextPath %>/subsys/oa/meeting/summary/index.jsp'"></center>
</div>
</body>
</html>