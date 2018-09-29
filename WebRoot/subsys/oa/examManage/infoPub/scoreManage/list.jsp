<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String flowTitle = request.getParameter("flowTitle") == null ? "" :  request.getParameter("flowTitle");
String participant = request.getParameter("participant") == null ? "" :  request.getParameter("participant");
String paperId = request.getParameter("paperId") == null ? "" :  request.getParameter("paperId");
String beginDate = request.getParameter("beginDate") == null ? "" :  request.getParameter("beginDate");
String beginDate1 = request.getParameter("beginDate1") == null ? "" :  request.getParameter("beginDate1");
String endDate = request.getParameter("endDate") == null ? "" :  request.getParameter("endDate");
String endDate1 = request.getParameter("endDate1") == null ? "" :  request.getParameter("endDate1");
String cd = request.getParameter("cd") == null ? "" :  request.getParameter("cd");
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/infoPub.js"></script>
<script type="text/javascript">
var param = "flowTitle=<%=flowTitle%>&participant=<%=participant%>&paperId=<%=paperId%>"
            + "&beginDate=<%=beginDate%>&beginDate1=<%=beginDate1%>"
            + "&endDate=<%=endDate%>&endDate1=<%=endDate1%>&cd=<%=cd%>";
var cdEnd = "<%=cd%>";
function doInitFlow() {
  //param = encodeURIComponent(param);
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamFlowAct/selectList.act?" + param;
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"flowTitle", text:"考试名称", width: "6%",align:"center"},
       {type:"text", name:"participant", text:"参加考试人", width: "6%",align:"center",render:toStr},
       {type:"text", name:"paperTimes", text:"考试时长（分钟）", width: "6%",align:"center"},
       {type:"text", name:"beginDate", text:"生效日期", width: "6%",align:"center",render:toBenginDate,sortDef:{type:0, direct:"desc"}},
       {type:"text", name:"endDate", text:"终止日期", width: "6%",align:"center",render:toEndDate},
       {type:"hidden", name:"paperId", text:"paperId", width: "1%",align:"center"},
       {type:"text", name:"caoZuo", text:"操作", width: "10%",align:"center",render:toCao}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的考试信息!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  } else {
    $("toDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
  }
}
//操作
function toCao(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var paperId = this.getCellData(recordIndex,"paperId");
  var participant = this.getCellData(recordIndex,"participant");
  if (cdEnd == "2") {
    return "<a href=javascript:showReader('" + seqId + "','" + paperId +"');>查卷</a>"
    + "&nbsp;&nbsp;<a href=javascript:excelReport(" + seqId + "," + paperId + ");>导出分数</a>"
    + "&nbsp;&nbsp;<a href='javascript:deleteVote(" + seqId + ");'>删除</a>";
  } else {
    return "<a href=javascript:showReader('" + seqId + "','" + paperId +"');>查卷</a>"
    + "&nbsp;&nbsp;<a href=javascript:excelReport(" + seqId + "," + paperId + ");>导出分数</a>";
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInitFlow();">
<div><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3">&nbsp;&nbsp;考试查询结果</span><br>
</div>
 <div align="center" id="toDiv"> </div>
<div id="giftList"></div>
<div id="returnNull"></div>
<br>
<div align="center"><input type="button" value="返回" onClick="javascript:history.back();" class="BigButton">&nbsp;&nbsp;
</div>
</body>
</html>