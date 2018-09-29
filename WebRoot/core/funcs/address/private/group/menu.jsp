<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>地址簿</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/address/private/js/privateUtil.js"></script>
<script type="text/javascript">
var index = "";
var index2 = "";
function doInit(){
	var data = {
	        //fix:true,      //默认第一个不能收缩
			panel:'menuList',
			index:'1',
            index2:'2',
			data:[{title:'联系人分组'},
						{title:'索引（按姓氏）'},
						{title:'查找（关键字）', action:search},
						{title:'管理分组', action:manageGroup}]
			};
	var menu = new MenuList(data);
	index = menu.getContainerId(1);
	loadData();
    index2 = menu.getContainerId(2);
	loadNameIndex();
	menu.showItem(this,{},1);
    
    //location = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getMb.act";
}
//new Insertion.but('netdisk_menu', message);
/**
 * 动态创建列表并读取数据
 */

function loadData(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getContactPersonGroup.act";
  var rtJson = getJsonRs(url, null);
  if (rtJson.rtState == "0") {
    var container = document.getElementById(index);
    var table = document.createElement("table");
    table.className = "TableBlock LeftMenu";
    var tbody = document.createElement("tbody");
    table.width = "100%";
    var show1tr = document.createElement("tr");
    var show1td = document.createElement("td");
    mouseOverHander(show1td);
    show1td.className = "TableData";
    show1td.align = "center";
    //show1td.id = rtJson.rtData[i].seqId;
    show1td.userId = "0";
    show1td.groupFlag = "0";
    show1td.id = "0";
    show1td.groupName = "默认";
    show1td.onclick = function(){
      clickPriv(this);
    }
    show1tr.style.cursor = "pointer";
    show1td.innerHTML ="默认";
    show1tr.appendChild(show1td);
    tbody.appendChild(show1tr);
    table.appendChild(tbody);
    if(rtJson.rtData.length == 0){
      container.appendChild(table);
    }
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var show1tr = document.createElement("tr");
      var show1td = document.createElement("td");
      mouseOverHander(show1td);
      show1td.className = "TableData";
      show1tr.style.cursor = "pointer";
      show1td.align = "center";
      show1td.id = rtJson.rtData[i].seqId;
      show1td.userId = rtJson.rtData[i].userId;
      show1td.groupName = rtJson.rtData[i].groupName;
      show1td.onclick = function(){
        clickPriv(this);
      }
      if(rtJson.rtData[i].userId == ""){
        show1td.innerHTML = rtJson.rtData[i].groupName+"(公共)";
      }else {
        show1td.innerHTML = rtJson.rtData[i].groupName;
      }
      show1tr.appendChild(show1td);
      tbody.appendChild(show1tr);
      table.appendChild(tbody);
      container.appendChild(table);
    }        
  } else {
  	alert(rtJson.rtMsrg); 
  }
}
/**
 * 鼠标点击事件
 */
function clickPriv(field){
  var seqId = field.id;
  var userId = field.userId;
  var groupFlag = field.groupFlag;
  var groupName = field.groupName;
  var parent = window.parent.addressmain;
  parent.location = "<%=contextPath%>/core/funcs/address/private/address/index.jsp?groupId="+seqId+"&groupName="+encodeURIComponent(groupName)+"&groupFlag="+groupFlag+"&userId="+userId;
}

function clickNameIndex(field){
  var seqId = field.id;
  var nameStrs = field.nameStrs;
  var parent = window.parent.addressmain;
  parent.location = "<%=contextPath%>/core/funcs/address/private/address/idxsearch.jsp?seqId="+seqId+"&nameStrs="+encodeURIComponent(nameStrs);
}

function contactGroup(){
  var parent = window.parent.addressmain;
  parent.location = "<%=contextPath%>/core/funcs/address/private/address/index.jsp";
}
/**
 * 索引（按姓氏）
 */
function idxSearch(){
  var parent = window.parent.addressmain;
  //location = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getMb.act";
  
  parent.location = "<%=contextPath%>/core/funcs/address/private/address/idxsearch.jsp";
}

function loadNameIndex(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPrivateMb.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var container = document.getElementById(index2);
    var table = document.createElement("table");
    table.className = "TableBlock LeftMenu";
    var tbody = document.createElement("tbody");
    table.width = "100%";
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var show1tr = document.createElement("tr");
      show1tr.align = "center";
      var show1td = document.createElement("td");
      mouseOverHander(show1td);
      show1td.className = "TableData";
      show1tr.style.cursor = "pointer";
      show1td.id = rtJson.rtData[i].seqId;
      show1td.nameStrs = rtJson.rtData[i].nameStrs;
      show1td.onclick = function(){
        clickNameIndex(this);
      }
      show1td.innerHTML = rtJson.rtData[i].nameStrs.substr(0, rtJson.rtData[i].nameStrs.length - 2);
      show1tr.appendChild(show1td);
      tbody.appendChild(show1tr);
      table.appendChild(tbody);
      container.appendChild(table);
    }        
    if(rtJson.rtData.length == 0){
      var show1tr = document.createElement("tr");
      show1tr.align = "center";
      var show1td = document.createElement("td");
      show1td.innerHTML = "无记录";
      show1tr.appendChild(show1td);
      tbody.appendChild(show1tr);
      table.appendChild(tbody);
      container.appendChild(table);
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}
/**
 * 查找（关键字）
 */
function search(){
  var parent = window.parent.addressmain;
  parent.location = "<%=contextPath%>/core/funcs/address/private/address/search.jsp";
}
/**
 * 管理分组
 */
function manageGroup(){
  var parent = window.parent.addressmain;
  parent.location = "<%=contextPath%>/core/funcs/address/private/group/index.jsp";
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<div id="menuList">
</div>
</body>
</html>