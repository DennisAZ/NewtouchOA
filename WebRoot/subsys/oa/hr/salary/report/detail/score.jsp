<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String flowTitle ="";
String rankman ="";
String groupId = "";
String participant = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
String beginDate ="";
String beginDate1 ="";

 
   //获取该月第一天和左后一天  
      SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");
      Calendar   cDay1   =   Calendar.getInstance();   
      cDay1.setTime(new Date());   
      int lastDay   =   cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);   
      Date   lastDate   =   cDay1.getTime();   
      lastDate.setDate(lastDay);   
    
    String date=curTime.format(lastDate);
    String startDate=date.substring(0,7)+"-00";
  
String endDate =request.getParameter("startTime");
String endDate1 = request.getParameter("endTime");
String cd = "1";
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
<head>
<title>考试信息</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
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
var param = "flowTitle=<%=flowTitle%>&rankman=<%=rankman%>&participant=<%=participant%>&groupId=<%=groupId%>"
            + "&beginDate=<%=beginDate%>&beginDate1=<%=beginDate1%>"
            + "&endDate=<%=endDate%>&endDate1=<%=endDate1%>&cd=<%=cd%>";
var cdEnd = "<%=cd%>";
function doInitFlow() {
  var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/selectList.act?" + param;
  cfgs = {
   dataAction: url,
   container: "giftList",
   colums: [
      {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
      {type:"hidden", name:"seqIds", text:"ID",align:"center", width:"1%"},
      {type:"text", name:"flowTitle", text:"考核任务名称", width: "10%",align:"center"},
      {type:"text", name:"rankman", text:"考核人", width: "8%",align:"center",render:toRankmanStr},
      {type:"text", name:"participant", text:"被考核人", width: "8%",align:"center",render:toParticipantStr},
      {type:"text", name:"groupId", text:"考核指标集", width: "10%",align:"center",render:toGroupId},
      {type:"text", name:"anonmity", text:"匿名", width: "6%",align:"center",render:toAnonmity},
      {type:"text", name:"beginDate", text:"生效日期", width: "8%",align:"center",sortDef:{type:0, direct:"desc"},render:toBeginDate},
      {type:"text", name:"endDate", text:"终止日期", width: "8%",align:"center",render:toEndDate},
      {type:"text", name:"zhuangTai", text:"状态", width: "6%",align:"center",render:toFlow},
      {type:"text", name:"caoZuo", text:"操作", width: "6%",align:"center",render:toCao},
      {type:"text", name:"daoChu", text:"导出", width: "10%",align:"center",render:toDaoChu}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据

  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无满足条件的考核任务!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  } else {
    $("toDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInitFlow();">
<div><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3">&nbsp;考核任务查询结果</span><br>
</div>
 <div align="center" id="toDiv"> </div>
<div id="giftList"></div>
<div id="returnNull"></div>
<br>
<div align="center"><input type="button" value="关闭" onClick="window.close();" class="BigButton">&nbsp;&nbsp;
</div>
</body>
</html>