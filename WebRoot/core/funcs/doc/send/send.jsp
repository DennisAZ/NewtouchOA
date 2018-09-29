<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/core/inc/header.jsp" %>
     <%@ page import="yh.core.funcs.doc.util.YHDocUtility"  %>
<%
String runId = request.getParameter("runId");
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>发文拟稿</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var runId = '<%=runId%>';
var cfgs = null;
var pageMgr = null;
//已经发送的部门
var depts = "";
var deptRetNameArray = null;
/**
 * 选择部门
 * @return
 */
function selectDept(retArray , moduleId,privNoFlag , noAllDept) {
  deptRetNameArray = retArray;
  var url = contextPath + "/core/funcs/doc/group/MultiDeptSelect.jsp?1=1";
  var has = false;
  if (moduleId) {
    url += "&moduleId=" + moduleId;
  }
  if (privNoFlag) {
    url += "&privNoFlag=" + privNoFlag;
  }
  if (noAllDept) {
    url += "&noAllDept=" + noAllDept;
  }
  openDialogResize(url, 530, 400);
}
function doInit() {
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
  
  var url2 = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/getRemindType.act";
  var json = getJsonRs(url2);
  if (json.rtState == '0') {
    isShowRemind("remindSms" , "remindDocSend", json.rtData.smsPriv , json.rtData.smsRemind);
    isShowRemind("remindSms2" , "remindDocSend2", json.rtData.sms2Priv , json.rtData.sms2Remind);
  }
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/getSendMessage.act?runId="+ runId;
  cfgs = {
      dataAction: url,
      container: "listContainer",
      moduleName:"doc",
      paramFunc: getParam,
      colums:[
              {type:"data", name:"toDeptName", text:"发送部门", width:"30%"},
              {type:"hidden", name:"toDept"},
              {type:"data", name:"sendTime", text:"发送时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
              {type:"data", name:"signTime", text:"签收时间", width:"15%", dataType:"dateTime",format:'yyyy-mm-dd HH:MM:ss'},
              {type:"data", name:"status", text:"状态", width:"5%", render:statusRender},
              {type:"hidden", name:"isOut"},
              {type:"hidden", name:"seqId"},
              {type:"data", name:"isCancel", text:"收回情况", width:"5%", render:isCancelRender},
              {type:"selfdef",text:"操作", width:"10%", render:opRender}]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $("hasSend").show();
    $("hasnotSend").hide();
    $('sendDpt').hide();
  } else {
    $("hasSend").hide();
    $("hasnotSend").show();
    $('sendDpt').show();
  }
}
function isShowRemind(ctrl , ctrl2, priv , isCheck) {
  if (priv) {
    $(ctrl).show();
    $(ctrl2).checked = isCheck;
  } else {
    $(ctrl).hide();
  }
}
function isCancelRender(cellData, recordIndex, columIndex){
  var isCancel = this.getCellData(recordIndex,"isCancel");
  if (isCancel == '0') {
    return "未收回";
  }else{
    return "已收回";
  }
}
/**
 * 0 未签收
 * 1 已签收 
 */
function opRender(cellData, recordIndex, columIndex){
  var status = this.getCellData(recordIndex,"status");
  var seqId = this.getCellData(recordIndex,"seqId");
  depts += this.getCellData(recordIndex,"toDept") + "," ;
  var status = this.getCellData(recordIndex,"status");
  var result = "";
  var isCancel = this.getCellData(recordIndex,"isCancel");
  var isout = this.getCellData(recordIndex,"isOut");
  if (isout == '0') {
    if (status == '0') {
      if (isCancel == '0') {
        result += "<a href=\"javascript:void(0)\" onclick='notify(1 , \""+seqId+"\")'>催办</a>";
      }
    }
    if (status == '1') {
      if (isCancel == '0') {
        result += "<a href=\"javascript:void(0)\" onclick='notify(2, \""+seqId+"\")'>催办</a>";
      }
    }
    if (status == '2') {
      if (isCancel == '1') {
        result += "<a href=\"javascript:void(0)\" onclick='notify(3, \""+seqId+"\")'>通知停办</a>";
      }
    }
  }
  if (isCancel == '0') {
    result +="&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick='cancel("+seqId+" , "+ status +")'>收回</a>";
  }else{
    result +="&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick='resend("+seqId+" , "+ status +")'>重新发送</a>";
  }
  return result;
}
function cancel(seqId , status){
  if (confirm("确认收回此公文！")) {
    var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/cancel.act?seqId=" + seqId + "&status=" + status;
    var json = getJsonRs(url);
    if (json.rtState == '0') {
      pageMgr.search();
    }
  }
}
function resend(seqId , status) {
if (confirm("确认重新发送此公文！")) {
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/resend.act?seqId=" + seqId + "&status=" + status;
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    pageMgr.search();
  }
}
}
function notify(flag , seqId) {
  var url = contextPath + "/core/funcs/doc/send/remind.jsp?type=" + flag + "&seqId=" + seqId;
  window.open(url);
}
function statusRender(cellData, recordIndex, columIndex){
  var status = this.getCellData(recordIndex,"status");
  if (status == '0') {
    return "未签收";
  }else if (status == '1'){
    return "已签收";
  }else if (status == '2'){
    return "已登记";
  }
  return "未签收";
}

function send() {
  var docId = parent._docId;
  var docName = parent._docName;
  if (!$('deptId').value) {
    alert("请选择发送部门！");
    return ;
  }
  $('sending').show();
  $('sendDpt').hide();
  
  getOut($('deptId'));
  <% if (YHDocUtility.usingEsb()) { %>
  getOut($('deptId2'));
  <% } %>
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/sendDoc.act";
  var json = getJsonRs(url , $('sendForm').serialize());
  if (json.rtState == "0") {
    $('sending').hide();
    pageMgr.search();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      $("hasSend").show();
      $("hasnotSend").hide();
      $('sendDpt').hide();
    } else {
      $("hasSend").hide();
      $("hasnotSend").show();
      $('sendDpt').show();
    }
  }
}
function clearDept(deptName , deptId) {
  $(deptName).innerHTML = "";
  $(deptId).value = "";
}
function clearDept2(deptName , deptId) {
  $(deptName).value = "";
  $(deptId).value = "";
}
function selectDept2(retArray, moduleId, privNoFlag, noAllDept) {
  deptRetNameArray = retArray;
  var url = contextPath + "/core/funcs/doc/send/MultiDeptSelect.jsp?1=1";
  var has = false;
  if (moduleId) {
    url += "&moduleId=" + moduleId;
  }
  if (privNoFlag) {
    url += "&privNoFlag=" + privNoFlag;
  }
  if (noAllDept) {
    url += "&noAllDept=" + noAllDept;
  }
  openDialogResize(url, 530, 400);
}
function getOut(ctrl) {
  if (!ctrl) {
    return ;
  }
  var value = ctrl.value;
  var vs = value.split(",");
  var dept = "";
  for (var i = 0 ;i < vs.length ; i++) {
    var v = vs[i];
    if (v && !findStr(v)) {
      dept += v + ",";
    }
  }
  ctrl.value = dept;
}
function findStr(v) {
  var vs = depts.split(",");
  for (var i = 0 ;i < vs.length ; i++) {
    var v1 = vs[i];
    if (v1 == v) {
       return true;
    }
  }
  return false; 
}
function cancelAll(){
if (confirm("确认收回所有已发送公文！")) {
  var url = contextPath + "/yh/core/funcs/doc/send/act/YHDocSendAct/cancelAll.act?runId=" + runId;
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    pageMgr.search();
  }
}
}
function addDept() {
  $('sendDpt').show();
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
<body onload="doInit()" >
<div id="canSend" style="display:">
<div id="hasSend">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"><span class="big1" id="notifytitle"> 发送详情</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<form  name="queryForm" id="queryForm">
<fieldset>
<table id="flowTable" border="0" width="100%"  class="TableList"  >
<tr class="TableLine2">
  <td align="left"> 
发送部门：  <input value="" id="toDeptName" name="toDeptName">
状态：<select name="status" id="status">
 <option value="">所有</option>
 <option value="0">未签收</option>
 <option value="1">已签收</option>
  <option value="2">已登记</option>
 </select>&nbsp;&nbsp;
 收回情况：<select name="isCancel" id="isCancel">
 <option value="">所有</option>
 <option value="0">未收回</option>
 <option value="1">已收回</option>
 </select>
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

<div id="listContainer"></div>
  </form>
<table class="TableBlock no-top-border" border=0 width="100%" style="margin:0;display:" id="flowRunOpTab" >
  <tr class="TableData">
  <td colspan="10">
   <input type="button"  onclick="cancelAll()"  value="收回所有公文" class="BigButton"> &nbsp;
   <input type="button"  onclick="addDept()"  value="添加" class="BigButton"> &nbsp;
  </td>
  </tr>
  </table>
</div><br>
<div id="hasnotSend" align="center"  style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="outMsgInfo" class="msg info">
            公文还未发送！
            </td>
        </tr>
    </tbody>
</table>
</div>
<div id="sending" align="center"  style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">
            <img src="<%=contextPath %>/core/funcs/doc/send/image/loadding.gif"/><br/>
             请稍后！公文发送中。。。
            </td>
        </tr>
    </tbody>
</table>
</div>
<div id="sendDpt">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"><span class="big1" id="notifytitle"> 发送公文</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<form action="" method="post" id="sendForm" name="sendForm">
<input type="hidden" value="" id="recDocId" name="recDocId"/>
<input type="hidden" value="" id="recDocName" name="recDocName"/>
<input type="hidden" value="<%=runId %>" id="runId" name="runId"/>
<table width="95%" id="sendDeptTable">
<tr><td>发送部门：</td>
      <td>
      <textarea rows="10" cols="50"  class="BigStatic"  readonly  id="deptName" name="deptName" onDBLClick="selectDept(['deptId','deptName'])"></textarea>
<input type="hidden" value="" id="deptId" name="deptId"/>
 <a href="javascript:void(0);" class="orgAdd"  onClick="selectDept(['deptId','deptName'])">添加</a>
       <a href="javascript:void(0);"   class="orgClear"  onClick="clearDept2('deptName', 'deptId')">清空</a>
      <span id="remindSms" style="display:none"><input type="checkbox"  id="remindDocSend" name="remindDocSend"><label for="remindDocSend">提醒</label></span>
      <span id="remindSms2" style="display:none"><input type="checkbox"  id="remindDocSend2" name="remindDocSend2"><label for="remindDocSend2">手机提醒</label></span>
      </td>
</tr>
<% if (YHDocUtility.usingEsb()) { %>
<tr><td>外部组织机构：</td>
      <td>
      <textarea rows="10"  class="BigStatic"  cols="30" readonly  id="deptName2" name="deptName2" onDBLClick="selectDept2(['deptId','deptName'], null ,null , true)"></textarea>
<input type="hidden" value="" id="deptId2" name="deptId2"/>
 <a href="javascript:void(0);" class="orgAdd"  onClick="selectDept2(['deptId2','deptName2'] , null ,null , true)">添加</a>
       <a href="javascript:void(0);"   class="orgClear"  onClick="clearDept('deptName2', 'deptId2')">清空</a>
      </td>
</tr>
<% } %>
<tr id="sendDept">
      <td align="center" colspan="2">
      <input class="SmallButton"  type="button" value="发送" onclick="send()"> 
      </td>
</tr>
</table>
</form>
</div>
</div>
</body>
</html>