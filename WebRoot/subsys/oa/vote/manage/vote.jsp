<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>投票</title>
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
<script type="text/javascript">
var seqId = "<%=seqId%>";
function doInit () {
  var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/showVote.act?seqId=" + seqId;
  cfgs = {
   dataAction: url,
   container: "giftList",
   colums: [ 
      {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
      {type:"hidden", name:"parentId", text:"ID",align:"center", width:"1%"},
      {type:"text", name:"subject", text:"标题 ", width: "6%",align:"center",render:toSubject},
      {type:"text", name:"type", text:"类型 ", width: "6%",align:"center",render:toType},
      {type:"text", name:"caoZuo", text:"操作", width: "10%",align:"center",render:toCaoZuo}]
 };
 pageMgr = new YHJsPage(cfgs);
 pageMgr.show();
 var total = pageMgr.pageInfo.totalRecord;//是否有数据
 if(total <= 0){
   var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
       + "<td class='msg info'><div class='content' style='font-size:12pt'>无已发布的投票!</div></td></tr>"
       );
   $('giftList').style.display = "none";
   $('returnNull').update(table); 
 }
}
//返回标题
function toSubject(cellData,recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var subject =  this.getCellData(recordIndex,"subject");
  return "<a href='javascript:showSubject(" + seqId + ")'>" + subject + "</a>";
}
//参与投票
function showSubject(seqId) {
  var URL = "<%=contextPath%>/subsys/oa/vote/manage/showVote.jsp?seqId=" + seqId;
  myleft = (screen.availWidth-780)/2 ;
  window.open(URL,"read_vote","height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
//操作
function toCaoZuo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var type = this.getCellData(recordIndex,"type");
  var subject = this.getCellData(recordIndex,"subject");
  var re1 = /\'/gi;
  subject = subject.replace(re1,"&lsquo;");
  subject = encodeURIComponent(subject);
  if (type != "2") {
    return "<a href=\"<%=contextPath%>/subsys/oa/vote/manage/item/index.jsp?seqId=" + seqId + "&subject=" + subject + ";\">投票项</a>&nbsp;"
           + "<a href='javascript:update(" + seqId + ");'>修改</a>&nbsp;&nbsp;"
           + "<a href='javascript:deleteVote(" + seqId + ");'>删除 </a>";
  } else {
    return "<a href='javascript:update(" + seqId + ");'>修改</a>&nbsp;&nbsp;"
           + "<a href='javascript:deleteVote(" + seqId + ");'>删除 </a>";
  }
}
//返回类型
function toType(cellData, recordIndex,columInde){
  var type =  this.getCellData(recordIndex,"type");
  if (type == "0") {
    return "单选";
  }
  if (type == "1") {
    return "多选";
  }
  if (type == "2") {
    return "文本输入";
  }
}
//新建子投票
function createVote () {
  var url = "<%=contextPath%>/subsys/oa/vote/manage/addVote.jsp?seqId=" + seqId;
  window.location.href = url;
}
//投票项
function showInfo(seqId,subject) {
  subject = encodeURIComponent(subject);
  window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/item/index.jsp?seqId=" + seqId + "&subject=" + subject;
}
//修改
function update (seqId) {
  window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/updateVote.jsp?seqId="+seqId;
}
//删除
function deleteVote (seqId) {
  var msg = "确认要删除！";
  if (window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/deleteVote.act?seqIds=" + seqId + ",";
    var json = getJsonRs(url);
    window.location.reload();
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/vote.gif" align="absmiddle"><span class="big3"> 新建子投票</span><br></td>
  </tr>
</table>
 
<div align="center">
   <input type="button" class="BigButton" value="新建子投票" onclick="javascript:createVote();">
</div>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/vote.gif" align="absmiddle"><span class="big3"> 管理子投票</span><br></td>
  </tr>
</table>
 <br>
<div id="giftList"></div>
<div id="returnNull"></div>
<br>
<div align="center">
   <input type="button" class="BigButton" value="返回" onClick="javascript:history.back()">
</div>
</body>
</html>