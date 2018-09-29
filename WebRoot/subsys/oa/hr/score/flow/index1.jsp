<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
 %>
<html>
<head>
<title>考核任务</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/score/js/scoreFlowLogic.js"></script>
<script type="text/javascript">
var dayTime = "<%=sf.format(new Date())%>";
var voteStatus = "0";
function doInit() {
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/selectFlow.act";
  cfgs = {
   dataAction: url,
   container: "giftList",
   colums: [
      {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
      {type:"hidden", name:"seqIds", text:"ID",align:"center", width:"1%"},
      {type:"hidden", name:"groupFlag", text:"groupFlag",align:"center", width:"1%"},
      {type:"text", name:"flowTitle", text:"考核任务名称", width: "6%",align:"center"},
      {type:"text", name:"rankman", text:"考核人", width: "6%",align:"center",render:toRankmanStrFunc},
      {type:"text", name:"participant", text:"被考核人", width: "6%",align:"center",render:toParticipantStrFunc},
      {type:"text", name:"groupId", text:"考核指标集", width: "6%",align:"center",render:toGroupId},
      {type:"text", name:"anonmity", text:"匿名", width: "6%",align:"center",render:toAnonmity},
      {type:"text", name:"beginDate", text:"生效日期", width: "6%",align:"center",sortDef:{type:0, direct:"desc"},render:toBeginDate},
      {type:"text", name:"endDate", text:"终止日期", width: "6%",align:"center",render:toEndDate},
      {type:"text", name:"voteStatus", text:"状态", width: "10%",align:"center",render:toFlow},
      {type:"selfdef", text:"操作", width: '10%', align:"center", render:optsIndex1}]
 };
 pageMgr = new YHJsPage(cfgs);
 pageMgr.show();
 var total = pageMgr.pageInfo.totalRecord;//是否有数据
 if(total <= 0){
   var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
       + "<td class='msg info'><div class='content' style='font-size:12pt'>无已发布的考核任务!</div></td></tr>"
       );
   $('giftList').style.display = "none";
   $('returnNull').update(table); 
 } else {
   $("toDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
 }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/score.gif" align="absmiddle"><span class="big3">&nbsp;管理已发布的考核任务</span><br>
    </td>
    </tr>
</table>
<div id="toDiv" align="center"></div>
<div id="giftList"></div>
<div id="returnNull"></div>
</body>
 
</html>

