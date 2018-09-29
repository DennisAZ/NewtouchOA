<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String paperId = request.getParameter("paperId") == null ? "" :  request.getParameter("paperId");
String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
%>
<html>
<head>
<title>考试结果统计信息</title>
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
var paperId = "<%=paperId%>";
var flowId = "<%=flowId%>";
var countNum = 0;
var count = 0;
function doInitFlow(){
  checkTitle(paperId);
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamFlowAct/selectQIZ.act?paperId=" + paperId + "&flowId=" + flowId;
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"paperTitle", text:"题目", width: "6%",align:"center"},
       {type:"text", name:"countNum", text:"答题次数", width: "3%",align:"center",render:toCount},
       {type:"text", name:"count", text:"错误次数", width: "3%",align:"center",render:toshowFalse},
       {type:"text", name:"fen", text:"错误率(%)", width: "10%",align:"left",render:toFen}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无相关的考试信息!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  } else {
    $("toDiv").update("共<span class='big4'>&nbsp;" + total + "</span>&nbsp;条")
  }
}
//答题次数
function toCount(cellData,recordIndex,columInde) {
  countNum = 0;
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamFlowAct/showCount.act?flowId=" + flowId;
  var json = getJsonRs(url);
  if(json.rtState == "1") {
    alert(json.rtState);
    return;
  }
  var prcs = json.rtData;
  if (prcs.showCount) {
    countNum = prcs.showCount
    return prcs.showCount;
  } else {
    return "0";
  }
}
//返回百分率
function toFen(cellData, recordIndex,columInde){
  var fen = 0;
  var numFen = 0;
  if (countNum > 0) {
     fen = parseInt((parseInt(count)/parseInt(countNum))*100);
     numFen = fen;
     if (fen == 100) {
       fen = fen - 6;
     }
     if (fen <= 0) {
       fen = 1;
     }
     var fenDiv = "<div style=\"width:" + fen + "%;background:url('<%=imgPath%>/vote_bg.gif');float:left;display:inline;\">&nbsp;</div><div style='float:left;display:inline;'>" + numFen + "%</div>";
     return fenDiv
  } else {
    var fenDiv = "<div style=\"width:1%;background:url('<%=imgPath%>/vote_bg.gif');float:left;display:inline;\">&nbsp;</div><div style='float:left;display:inline;'>" + numFen + "%</div>";
    return fenDiv; 
  }
}
//答题错误次数
function toshowFalse(cellData, recordIndex,columInde) {
  count = 0;
  var url = "<%=contextPath %>/yh/subsys/oa/examManage/act/YHExamFlowAct/showCountFalse.act?recordIndex=" + recordIndex + "&flowId=" + flowId;
  var json = getJsonRs(url);
  if(json.rtState == "1") {
    alert(json.rtState);
    return;
  }
  var prcs = json.rtData;
  if (prcs.showCountFalse) {
    count = prcs.showCountFalse;
    return prcs.showCountFalse; 
  } else {
    return "0";
  }
}
//标题
function checkTitle(paperId) {
  var url = "<%=contextPath %>/yh/subsys/oa/examManage/act/YHExamFlowAct/showTitle.act?paperId=" + paperId;
  var json = getJsonRs(url);
  if(json.rtState == "1") {
    alert(json.rtState);
    return;
  }
  var prcs = json.rtData;
  if (prcs != null) {
    if (prcs.paperTitle != undefined) {
      $("title").update("考试结果统计&nbsp;-&nbsp;" + prcs.paperTitle);
    } else {
      $("title").update("考试结果统计&nbsp;- &nbsp;");
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInitFlow();">
<form name="form1" method="post" id="form1"> 
<div>
<img src="<%=imgPath%>/workflow.gif" align="absmiddle">
<span class="big3" id="title"> </span></div>
<div align="center" id="toDiv"> </div>
<div id="giftList"></div>
<div id="returnNull"></div>
<br>
<div align="center"><input type="button" value="关闭" class="BigButton" onClick="javascript:window.close()"></div>
</form>
</body>
</html>
