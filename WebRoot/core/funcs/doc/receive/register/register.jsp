<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>收文登记</title>
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
<script type="text/Javascript">
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
  
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getRegList2.act" ;
  cfgs = {
      dataAction: url,
      container: "container",
      moduleName:"doc",
      paramFunc: getParam,
      colums:[
              {type:"data", name:"title", text:"公文", width:"20%" ,render: titleRender },
              {type:"data", name:"doc", text:"公文文号", width:"10%"},
             {type:"data", name:"attach", text:"公文正文", width: "15%"},
            {type:"hidden",name:"attachId"},
              {type:"data", name:"fromDept", text:"发送部门", width:"10%", dataType:"dateTime"},
              {type:"data", name:"sendTime", text:"发送时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
               {type:"data", name:"signTime", text:"签收时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
              {type:"data", name:"status", text:"状态", width:"5%", render:statusRender},
              {type:"hidden", name:"isOut"},
              {type:"hidden", name:"seqId"},
              {type:"hidden", name:"fromDeptId"},
            {type:"selfdef",text:"操作", width:"10%",render:opRender}
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
  if (status == '1') {
    result += "<a href='javascript:void(0)' onclick='register("+seqId+")' >登记</a>";
  } 
  result +="&nbsp;&nbsp;<a href='javascript:void(0)' onclick='comeback("+seqId+")'>退回</a>";
  return result;
}
function register(seqId){
  var url = contextPath + "/core/funcs/doc/receive/register/docReg.jsp";
  if (seqId) {
    url += "?rec_seqId=" + seqId;
  }
  window.open(url);
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
  } else {
    return "已签收";
  }
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
function query() {
  pageMgr.search();
}
</script>
</head>
<body onload="doInit();">
<table id="flowTable2" border="0" width="100%" style="height:50px"  >
<tr>
  <td align="center"> 
  <input class="BigButton" type="button" value="收文登记" onclick="register()"/>
  </td></tr></table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3">未登记收文&nbsp;</span>
    </td>
  </tr>
</table>
<fieldset>
<form  name="queryForm" id="queryForm">
<table id="flowTable" border="0" width="100%"  class="TableList"  >
<tr class="TableLine2">
  <td align="left"> 
  标题：
<input  type="text"  value="" id="title" name="title"> &nbsp;&nbsp;
来文文号：  <input type="text" value="" id="sendDocNo" name="sendDocNo"> &nbsp;&nbsp;
来文单位：  <input  type="text"  value="" id="fromDeptName" name="fromDeptName">
  </td> 
  </tr>
  <tr class="TableLine1">
  <td align="left">
 签收时间： 从
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