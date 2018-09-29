<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
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
//  ,{title:"导入与导出", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/inout.jsp" , imgUrl:"<%=imgPath%>/inout.gif",useIframe:true}            

function doInit() {
  var jso = [
             {title:"我的日程", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/calendarindex.jsp" , imgUrl:"<%=imgPath%>/calendar.gif",useIframe:true}            
             ,{title:"周期性事务", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/Cycaffair.jsp" , imgUrl:"<%=imgPath%>/calendar.gif",useIframe:true}            
             ,{title:"我的任务", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/task.jsp" , imgUrl:"<%=imgPath%>/calendar.gif",useIframe:true}            
             ,{title:"导入与导出", useTextContent:false , contentUrl:"<%=contextPath %>/core/funcs/calendar/inout.jsp" , imgUrl:"<%=imgPath%>/inout.gif",useIframe:true}            
             ];

  buildTab(jso, 'contentDiv', 600);
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
function remind(){
  //"<a href='#' onclick='remind();'><img src='<%=imgPath%>/iask_small.gif'></img ></a>"
  alert("开发中...");
  //window.open('<%=contextPath %>/core/funcs/calendar/iask.jsp?FUNC=calendar','iask','top=0,left=0,width=600,height=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes')";
}
</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>

</body>
</html>