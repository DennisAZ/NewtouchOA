<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  Date curDate = new Date();
  String curDateStr = dateFormat1.format(curDate);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">


<style type="text/css">

.clearfix {
display:block;
margin:0;
padding:0;
bottom:0;
position:fixed;
width:100%;
z-index:88888;
}

.m-chat .chatnote {
float:left;
height:25px;
line-height:25px;
position:absolute;
}
.m-chat {
-moz-background-clip:border;
-moz-background-inline-policy:continuous;
-moz-background-origin:padding;
background:transparent url(http://xnimg.cn/imgpro/chat/xn-pager.png) repeat-x scroll 0 -396px;
border-left:1px solid #B5B5B5;
display:block;
height:25px;
margin:0 15px;
position:relative;
}
.operateLeft{
	float: left;
	font-size: 12px;
	text-align: right;
	width: 80px;
	border-right-width: 1px;
	border-right-style: solid;
	border-right-color: #999;
}
.operateRight{
	float: left;
	font-size: 12px;
	text-align: left;

}


</style>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function doInit() {
  var jso = [
             {title:"考勤审批", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/manage/manage.jsp" ,useIframe:true}
             //,{title:"人员考勤记录", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/manage/attendance.jsp" ,useIframe:true}
             //,{title:"上下班登记统计", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/manage/statisticindex.jsp" ,useIframe:true}
             //,{title:"请假加班统计", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/attendmanage/attendstati/index.jsp" ,useIframe:true}
             //,{title:"人员年休假天数设定", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/attendmanage/user_annualleave/index.jsp" ,useIframe:true}
              ];

  var weeks = ["周日","周一","周二","周三","周四","周五","周六"]; 
  var curDateStr = '<%=curDateStr%>';
  var week = getDayOfWeek(curDateStr);
  buildTab(jso, 'contentDiv', 800,"<div style='float:left; padding-left:10px;padding-top:2px;'><b>今日 "+curDateStr+"  "+weeks[week]+"</b></div>");
}
function openDesign(){ 
  //alert('icon' + dd.extData);
 window.open("../flowdesign/index.jsp?flowId=<%=flowId%>","flow_design","height=600,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=10,left=10,resizable=yes");
}
function delFlowType(){
  
  if(confirm("确定删除吗？\n这将删除以下数据：\n1,流程描述与步骤设置。\n2,依托于该流程的所有工作。"))
  {
    var url =  contextPath + "/yh/core/funcs/workflow/act/YHFlowTypeAct/delFlowType.act?flowId=<%=flowId%>";
  	var json = getJsonRs(url, "flowId=<%=flowId%>");
  	if(json.rtState == '0'){
      alert(json.rtMsrg);
      parent.leftFrame.location.reload();
    //histroy.back();
    //转到列表页

 	}else{
      alert(json.rtMsrg);
  	}
  }
}
function getDayOfWeek(dateStr){
  var day = new Date(Date.parse(dateStr.replace(/-/g, '/'))); //将日期值格式化
  //day.getDay();根据Date返一个星期中的某其中0为星期日
  return day.getDay(); 
}
</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>

</body>
</html>