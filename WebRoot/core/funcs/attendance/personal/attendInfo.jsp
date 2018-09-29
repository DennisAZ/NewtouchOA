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
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function loadData1() {
  var jso = [
             {title:"个人考勤统计", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/statitic/index.jsp" ,useIframe:true}
             ,{title:"上下班登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/personal/registerduty.jsp" ,useIframe:true}
             //,{title:"上下班记录", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/personal/selectduty.jsp" ,useIframe:true}
             ,{title:"加班登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/overtime/index.jsp" , useIframe:true}
             ,{title:"值班登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/duty/index.jsp" , useIframe:true}
             ,{title:"外出登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/personal/out.jsp" , useIframe:true}
             ,{title:"请假登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/personal/leave.jsp" ,useIframe:true}
             ,{title:"出差登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/core/funcs/attendance/personal/evection.jsp" , useIframe:true}
             ,{title:"年休假登记", useTextContent:false , imgUrl:"<%=imgPath%>/views/attendance.gif", contentUrl:"<%=contextPath %>/custom/attendance/personal/annualleave/index.jsp" , useIframe:true}
             ];
  buildTab(jso, 'contentDiv', 800);
}


function doInit() {
  doInitData();
}

function doInitData(){
  var data = {
      //fix:true,      //默认第一个不能收缩
 panel:'menuList',
 index:'1',
 data:[{title:'个人考勤'}]
 };
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
  loadData();
  menu.showItem(this,{},1);
}

function loadData(){
  var arrayJso = new Array("个人考勤统计","上下班登记","上下班记录","加班登记","值班登记","外出登记","请假登记","出差登记","年休假登记","个人补登记");
    var container = document.getElementById(index);
    //var table = document.createElement("table");
    var table = new Element("table", {
      "class": "TableBlock LeftMenu"
    });
    var tbody = document.createElement("tbody");
    table.width = "100%";
    for(var i = 0; i < arrayJso.length; i++){
      var showTr = document.createElement("tr");
      var showTd = document.createElement("td");
      mouseOverHander(showTd);
      showTd.className = "TableData";
      showTr.style.cursor = "pointer";
      showTd.align = "center";
      showTd.id = i;
      showTd.name = arrayJso[i];
      showTr.style.cursor = "pointer";
      showTd.onclick = function(){
        clickPriv(this);
      }
      container.appendChild(table);
      table.appendChild(tbody);
      tbody.appendChild(showTr);
      showTr.appendChild(showTd);
      showTd.innerHTML = showTd.name;
      }
}

function mouseOverHander(show1td){
  show1td.onmouseover = function(){
    show1td.style.backgroundColor = "#edf6db";
  }
  show1td.onmouseout = function(){
    show1td.style.backgroundColor = "#FFFFFF";
  }
}

/**
 * 鼠标点击事件
 */
function clickPriv(field){
  var seqId = field.id;
  var userId = field.name;
  var parent = window.parent.hrms;
  if(seqId == "0"){
    parent.location = contextPath + "/core/funcs/attendance/personal/manage.jsp";
  }
  if(seqId == "1"){
    parent.location = contextPath + "/core/funcs/attendance/personal/registerduty.jsp";
  }
  if(seqId == "2"){
    parent.location = contextPath + "/core/funcs/attendance/personal/selectduty.jsp";
  }
  if(seqId == "3"){
    parent.location = contextPath + "/custom/attendance/personal/overtime/index.jsp";
  }
  if(seqId == "4"){
    parent.location = contextPath + "/custom/attendance/personal/duty/index.jsp";
  }
  if(seqId == "5"){
    parent.location = contextPath + "/core/funcs/attendance/personal/out.jsp";
  }
  if(seqId == "6"){
    parent.location = contextPath + "/core/funcs/attendance/personal/leave.jsp";
  }
  if(seqId == "7"){
    parent.location = contextPath + "/core/funcs/attendance/personal/evection.jsp";
  }
  if(seqId == "8"){
    parent.location = contextPath + "/custom/attendance/personal/annualleave/index.jsp";
  }
  if(seqId == "9"){
    parent.location = contextPath + "/subsys/oa/fillRegister/attendScore/registerIndex.jsp";
  }
  if(seqId == "10"){
    parent.location = contextPath + "/custom/attendance/personal/overtimetotal/index.jsp";
  }
    //parent.location = contextPath + "/subsys/oa/hr/score/submit/scoreIndex.jsp?userId="+seqId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&currPage=1";
    //parent.location = contextPath + "/subsys/oa/hr/score/submit/scoreData.jsp?userId="+seqId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&groupId="+groupId+"&checkFlag="+checkFlag+"&currPage=1";
    //parent.location = contextPath + "/subsys/oa/hr/score/submit/scoreIndex.jsp?userId="+seqId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&checkFlag="+checkFlag+"&currPage=1";
}

function openDesign(){ 
  //alert('icon' + dd.extData);
 window.open("../flowdesign/index.jsp?flowId=<%=flowId%>","flow_design","height=600,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=10,left=10,resizable=yes");
}

function delFlowType(){
  if(confirm("确定删除吗？\n这将删除以下数据：\n1,流程描述与步骤设置。\n2,依托于该流程的所有工作。")){
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
</script>
</head>
<body onload="doInit()">

<div id="menuList">
<input type="hidden" name="groupId" id="groupId" value="">
<input type="hidden" name="checkFlag" id="checkFlag" value="">
</div>
</body>
</html>