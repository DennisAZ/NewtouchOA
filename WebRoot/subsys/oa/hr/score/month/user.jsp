<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
  String checkFlag = request.getParameter("checkFlag") == null ? "" :  request.getParameter("checkFlag");
  String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
  String groupFlag =  request.getParameter("groupFlag") == null ? "" :  request.getParameter("groupFlag"); 
  String groupId = request.getParameter("groupId") == null ? "" :  request.getParameter("groupId");
  String currPage =  request.getParameter("currPage") == null ? "" :  request.getParameter("currPage"); 
  String month = request.getParameter("month") == null ? "" :  request.getParameter("month");
  String year = request.getParameter("year") == null ? "" :  request.getParameter("year");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreFlowLogic.js"></script>
<script type="text/javascript">
var index = "";
var index2 = "";
var tree = null;
var flowId='<%=flowId%>';
var userId = '<%=userId%>';
var checkFlag = '<%=checkFlag%>';
var groupFlag = '<%=groupFlag%>';
var groupId = '<%=groupId%>';
var year = '<%=year%>';
var month = '<%=month%>';
var currPage = '<%=currPage%>';
function doInit(){	
  $("checkFlag").value = checkFlag;
  $("groupId").value = groupId;
  var url= "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/getScoreOnline.act?flowId=" + flowId + "&userId="+userId+"&currPage="+currPage;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  doInitData();
}

function doInitData(){
  var data = {
      //fix:true,      //默认第一个不能收缩	panel:'menuList',
	index:'1',
	data:[{title:'被考核人员列表'}]
	};
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
  loadData();
  menu.showItem(this,{},1);
}

function loadData(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreShowAct/getScoreFlowMonthData.act?groupId="+groupId;
  var rtJson = getJsonRs(url, null);
  if (rtJson.rtState == "0") {
    var container = document.getElementById(index);
    var table = document.createElement("table");
    var tbody = document.createElement("tbody");
    table.width = "100%";
    if(rtJson.rtData.length == 0){
      var show1tr = document.createElement("tr");
      show1tr.align = "center";
      var show1td = document.createElement("td");
      show1td.innerHTML = "未定义用户";
      show1tr.appendChild(show1td);
      tbody.appendChild(show1tr);
      table.appendChild(tbody);
      container.appendChild(table);
    }
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var show1tr = document.createElement("tr");
      var show1td = document.createElement("td");
      mouseOverHander(show1td);
      show1td.className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
      show1tr.style.cursor = "pointer";
      show1td.align = "center";
      show1td.id = rtJson.rtData[i].seqId;
      show1td.userId = rtJson.rtData[i].userId;
      show1td.roleId = rtJson.rtData[i].userPriv;
      show1td.deptId = rtJson.rtData[i].deptId;
      show1td.userName = rtJson.rtData[i].userName;
      var userId = rtJson.rtData[i].seqId;
      var roleId = rtJson.rtData[i].userPriv;
      if(getOperationFlag(userId, roleId) == "1"){
        show1td.innerHTML = "<img src='<%=imgPath%>/score_flag.gif' align='absmiddle'>&nbsp;"; 
      }
      show1td.onclick = function(){
        clickPrivs(this);
      }
      var score = scoreData(userId);
      show1td.innerHTML = rtJson.rtData[i].userName + "(" +score +"分)";
      show1tr.appendChild(show1td);
      tbody.appendChild(show1tr);
      table.appendChild(tbody);
      container.appendChild(table);
    }        
  } else {
  	alert(rtJson.rtMsrg); 
  }
}

function scoreData(userId){
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreShowAct/getScoreShow.act?userId=" + userId +"&year=" + year + "&month=" +month; 
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "0"){ 
    var data = rtJson.rtData;
    return data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getOperationFlag(userId, roleId){
  var groupIdStr = groupIdFunc(roleId);
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreShowAct/getOperationFlag.act?userId=" + userId +"&groupId=" + groupIdStr + "&year=" + year + "&month=" + month; 
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "0"){ 
    var data = rtJson.rtData;
    return data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function groupIdFunc(roleId){
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreShowAct/getGroupId.act?roleId=" + roleId; 
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "0"){ 
    var data = rtJson.rtData;
    return data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function clickPrivs(field){
  var seqId = field.id;
  var roleId = field.roleId;
  var groupId = groupIdFunc(roleId);
  var groupFlag = getGroupFlag(groupId);
  var checkEnd = getCheckEnd(groupId, seqId);
  var parent = window.parent.hrms;
  var checkFlag = "0";
  //parent.location = "<%=contextPath%>/subsys/oa/hr/score/month/news.jsp?userId="+roleId;
  if(groupFlag == "0"){
    //parent.location = contextPath + "/subsys/oa/hr/score/submit/scoreIndex.jsp?userId="+seqId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&currPage=1";
    parent.location = contextPath + "/subsys/oa/hr/score/month/scoreData.jsp?userId="+seqId+"&groupId="+groupId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&groupId="+groupId+"&checkFlag="+checkFlag+"&year=" + year + "&month=" +month+"&currPage=1";
  }else{
    if(checkEnd == "1"){
      parent.location = contextPath + "/subsys/oa/hr/score/month/monthEnd/scoreIndex.jsp?userId="+seqId+"&groupId="+groupId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&checkFlag="+checkFlag+"&year=" + year + "&month=" +month +"&currPage=1";
    }else{
      parent.location = contextPath + "/subsys/oa/hr/score/month/scoreIndex.jsp?userId="+seqId+"&groupId="+groupId+"&flowId="+flowId+"&groupFlag="+groupFlag+"&checkFlag="+checkFlag+"&year=" + year + "&month=" +month +"&currPage=1";
    }
  }
}

function getPageEnd(seqId){
  var URL = "/yh/subsys/oa/examManage/examOnline/queryExam/scoreIndex.jsp?flowId=" + seqId;
  window.location.href = URL;
}


function getGroupFlag(groupId){
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreShowAct/getGroupFlag.act?groupId=" + groupId; 
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "0"){ 
    var data = rtJson.rtData;
    return data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getCheckEnd(groupId, userId){
  var url = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreShowAct/getCheckEnd.act?groupId=" + groupId + "&userId=" + userId+"&year=" + year + "&month=" +month; 
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "0"){ 
    var data = rtJson.rtData;
    return data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function contactGroup(){
  var parent = window.parent.addressmain;
  parent.location = "<%=contextPath%>/core/funcs/address/private/address/index.jsp";
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="menuList">
<input type="hidden" name="groupId" id="groupId" value="">
<input type="hidden" name="checkFlag" id="checkFlag" value="">
<input type="hidden" name="groupId" id="groupId" value="">
</div>
</body>
</html>