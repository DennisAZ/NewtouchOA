<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>任务查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<style>
</style>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/esb/server/user/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var pageMgr;
function doInit(){
  var beginParameters = {
      inputId:'startTime',
      property:{isHaveTime:true}
      ,bindToBtn:'beginDateImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:true}
      ,bindToBtn:'endDateImg'
  };
  new Calendar(endParameters);
  var requestURL = "<%=contextPath%>/yh/core/esb/server/taskstatus/act/YHTaskStatusAct/getUploadPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    paramFunc: getParam,
    colums: [
	   {type:"text", name:"seqId", text:"流水号", width:'8%',align:'center',render:serialNumber},
     {type:"text", name:"guid", text:"GUID", width:'15%',align:'center'},
      {type:"text", name:"fromId", text:"发送方", width:'10%',align:'center',render:userNameFunc},
       {type:"text", name:"filePath", text:"文件", width:'10%',align:'center',render:fileRender},
       {type:"text", name:"toId", text:"接收方", width:'10%',align:'center',render:userNameFunc},
       {type:"text", name:"status", text:"状态", width:'5%',align:'center',render:statusRender},
       {type:"hidden", name:"failedMessage", text:"错误信息", width:'5%',align:'center'},
       {type:"text", name:"createTime", text:"发送开始时间", width:'15%',align:'center' ,render:splitDateTime},
       {type:"text", name:"completetime", text:"发送结束时间", width:'15%',align:'center' ,render:splitDateTime},
       {type:"text", name:"time", text:"用时", width:'10%',align:'center' }
       ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}
function getParam(){
  queryParam = $("queryForm").serialize();
  return queryParam;
}
function serialNumber(cellData, recordIndex, columInde){
  if(cellData){
    return "No."+cellData;
  }
}

function ipAddress() {
  return '';
}

function statusRender(cellData, recordIndex, columInde) {
  if (cellData * 1 >= 1 && cellData * 1 < 5) {
    return ["发送中", "<span style=\"color:green;\">发送完毕</span>","", "<a href=\"javascript:void(0)\" onclick=\"failedMessage("+recordIndex+")\" style=\"color:red;\" title=\"点击查看错误详情\">发送失败</span>"][cellData * 1 - 1];
  }
  else {
    return "";
  }
}

function failedMessage(recordIndex){
  if($("tr_"+recordIndex+"_msg")){
    $("tr_"+recordIndex+"_msg").remove();
    return;
  }
  var failedMessage = pageMgr.getCellData(recordIndex,"failedMessage");
  var tr = new Element('tr',{"id":"tr_"+recordIndex+"_msg","width":"100%"}).update("<td colspan='7' align='center'>错误信息："+failedMessage+"</td>");
  $('tr_'+recordIndex).insert({after: tr});
}
//清空时间组件
function empty_date(){
  $("startTime").value="";
  $("endTime").value="";
}

function fileRender(cellData, recordIndex, columInde) {
  if (cellData) {
    return cellData.split("\\").pop();
  }
}

function splitDateTime(cellData, recordIndex, columInde){
  if(cellData){
    return cellData.substr(0,19);
  }else{
	return "";
  }
}
function queryTypeChange(value) {
  if  (value == '1') {
    $('dateRange').show();
  } else {
    $('dateRange').hide();
  }
}
function query() {
  pageMgr.search();
}
function ext(){
  var pars = Form.serialize($('queryForm'));
  location =  "<%=contextPath%>/yh/core/esb/server/taskstatus/act/YHTaskStatusAct/expUpload.act?"+pars;
}
</script>
</head>

<body onload="doInit()">

<table border="0" width="" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/system.gif" align="absmiddle"><span class="big3"> 任务状态查询</span><br>

    </td>
  </tr>
</table>
<br>
<fieldset>
<form  name="queryForm" id="queryForm">
<table id="flowTable" border="0" width="100%"  class="TableList"  >
<tr class="TableLine2">
  <td align="left"> 
  流水号：从
<input type="text" value="" id="startNo" name="startNo"> 
到
<input type="text" value="" id="endNo" name="endNo"> 
  </td> 
  </tr>
  <tr class="TableLine1">
  <td align="left">查询类型：
  <select  id="queryType" name="queryType" onchange="queryTypeChange(this.value)">
  <option value="0">当天</option>
  <option value="1">时间范围</option>
  <option value="2">三天内</option>
  <option value="3">本周</option>
  <option value="4">本月</option>
  </select>
  <span id="dateRange" style="display:none">
 发送时间： 从
  <input type="text" id="startTime" name="startTime" size="20" maxlength="19" readonly class="BigStatic" value="">
  <img id="beginDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      到
  <input type="text" id="endTime" name="endTime" size="20" maxlength="19"  readonly class="BigStatic">
  <img id="endDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
  <a href="javascript:empty_date()">清空</a></span>
  <input onclick="query()" value="查询" type="button" class="SmallButton">
  <input type="button" value="导出" class="BigButton" onClick="ext();" title="导出" name="button">
    </td> 
  </tr>
  </table>
</fieldset>

<br>
<div id="listDiv">
</div>
</body>
</html>
