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
      //fix:true,      //默认第一个不能收缩

	panel:'menuList',
	index:'1',
	data:[{title:'被考核人员列表'}]
	};
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
  loadData();
  menu.showItem(this,{},1);
}

function loadData(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreDataAct/getScoreFlowData.act?flowId="+flowId;
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
      var show1tr =  new Element("tr");
      var show1td = new Element("td");
      mouseOverHander(show1td);
      show1td.className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
      show1tr.style.cursor = "pointer";
      show1td.align = "center";
      show1td.id = rtJson.rtData[i].seqId;
      show1td.userId = rtJson.rtData[i].userId;
      show1td.deptId = rtJson.rtData[i].deptId;
      show1td.userName = rtJson.rtData[i].userName;
      if(rtJson.rtData[i].img == "1"){
        show1td.insert("<img src='<%=imgPath%>/score_flag.gif' align='absmiddle'>&nbsp;"); 
      }
      show1td.onclick = function(){
        clickPriv(this);
      }
      show1td.insert(rtJson.rtData[i].userName);
      show1tr.appendChild(show1td);
      tbody.appendChild(show1tr);
      table.appendChild(tbody);
      container.appendChild(table);
    }        
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
</div>
</body>
</html>