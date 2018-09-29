<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String type = request.getParameter("type");
if ("".equals(type) || type==null) {
  type = "0";
}
%>
<html>
<head>
<title>收文签收</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript">
var isSign = "<%=type %>";

var cfgs = null;
var pageMgr = null;
function doInit(){
  var beginParameters = {
      inputId:'startTime',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocSignAct/getRegList.act?isSign=" + isSign;
  cfgs = {
      dataAction: url,
      container: "container",
      moduleName:"doc",
      paramFunc: getParam,
      colums:[
              {type:"data", name:"title", text:"来文标题", width:"20%", render:titleRender},
              {type:"data", name:"doc", text:"来文文号", width:"10%"},
             {type:"data", name:"attach", text:"公文正文", width: "15%"},
            {type:"hidden",name:"attachId"},
              {type:"data", name:"fromDept", text:"来文单位", width:"10%", dataType:"dateTime"},
              {type:"data", name:"sendTime", text:"发送时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
<% if (!"0".equals(type)) { %>
               {type:"data", name:"signTime", text:"签收时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
<% } else { %>
{type:"hidden", name:"signTime"},
<% } %>
              {type:"data", name:"status", text:"状态", width:"5%", render:statusRender},
              {type:"hidden", name:"isOut"},
              {type:"hidden", name:"seqId"},
              {type:"hidden", name:"fromDeptId"},
              
              <% if (!"0".equals(type)) { %>
              {type:"selfdef",text:"操作", width:"15%",render:opRender}
<% } else { %>
{type:"selfdef",text:"操作", width:"10%",render:opRender}
<% } %>
      ]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}
function titleRender (cellData, recordIndex, columIndex) {
  var sub = cellData;
  if (sub.length > 20) {
    sub = sub.substring(0 , 20) + "...";
  }
  cellData = "<span title='" + cellData +"'>" + sub + "</span>";
  return cellData;
}
function opRender(cellData, recordIndex, columIndex){
  var status = this.getCellData(recordIndex,"status");
  var seqId = this.getCellData(recordIndex,"seqId");
  var status = this.getCellData(recordIndex,"status");
  var result = "";
  if (status == '0') {
    result += "<a href='javascript:void(0)' onclick='signUp("+seqId+")'>签收</a>";
  } 
  result +="&nbsp;&nbsp;<a href='javascript:void(0)' onclick='comeback("+seqId+")' >退回</a>";
  return result;
}
function signUp(seqId){
  if (confirm("确认签收！")) {
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocSignAct/sign.act?seqId=" + seqId;
    var json = getJsonRs(url);
    if (json.rtState = '0') {
      pageMgr.search();
    }
  }
}
function comeback(seqId) {
  if (confirm("确认退回！")) {
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocSignAct/comeback.act?seqId=" + seqId;
    var json = getJsonRs(url);
    if (json.rtState = '0') {
      pageMgr.search();
    }
  }
}
function statusRender(cellData, recordIndex, columIndex){
  var status = this.getCellData(recordIndex,"status");
  if (status == '0') {
    return "未签收";
  } else if (status == '1'){
    return "已签收";
  }else if (status == '2'){
    return "已登记";
  }
}
function query() {
  pageMgr.search();
}
function getParam(){
  queryParam = $("queryForm").serialize();
  return queryParam;
}
//清空时间组件
function empty_date(){
  $("startTime").value="";
  $("endTime").value="";
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3"><%if ("0".equals(type)) {
      out.print("未"); 
    }else {out.print("已");} %>签收公文</span>
    </td>
  </tr>
</table>
<fieldset>
<form  name="queryForm" id="queryForm">
<table id="flowTable" border="0" width="100%"  class="TableList"  >
<tr class="TableLine2">
  <td align="left"> 
  标题：
<input value="" id="title" name="title"> &nbsp;&nbsp;
来文文号：  <input value="" id="sendDocNo" name="sendDocNo"> &nbsp;&nbsp;
来文单位：  <input value="" id="fromDeptName" name="fromDeptName">
  </td> 
  </tr>
  <tr class="TableLine1">
  <td align="left">
 发送时间： 从
  <input type="text" id="startTime" name="startTime" size="10" maxlength="19" readonly class="BigStatic" value="">
  <img id="beginDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      到
  <input type="text" id="endTime" name="endTime" size="10" maxlength="19"  readonly class="BigStatic">
  <img id="endDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
  <a href="javascript:empty_date()">清空</a>
  <input onclick="query()" value="查询" type="button" class="SmallButton">
    </td> 
  </tr>
  </table>
</fieldset>
<div id="container"></div>
</body>
</html>