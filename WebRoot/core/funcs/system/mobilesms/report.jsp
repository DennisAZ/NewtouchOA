<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String beginDate = request.getParameter("beginDate");
  if (beginDate == null) {
    beginDate = "";
  }
  String endDate = request.getParameter("endDate");
  if (endDate == null) {
    endDate = "";
  }
  
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String loginUserPriv = person.getUserPriv();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>手机短信统计</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/grid.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/mobilesms/js/mobilesmsLogic.js"></script>
<script type="text/javascript">
var loginUserPriv = "<%=loginUserPriv%>";
var beginDate = "<%=beginDate%>";
var endDate = "<%=endDate%>";

function doInit() {
  var url =  contextPath + "/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getReportSearchList.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"userId", text:"用户编号"},
       {type:"data", width: 150, name:"deptName", text:"部门", render:deptFunc},  
       {type:"data", width: 130, name:"userName", text:"用户", render:userFunc},  
       {type:"selfdef", width: 80, name:"success", text:"发送成功", render:sendSuccess},       
       {type:"selfdef", width: 80, name:"not", text:"未发送", render:sendNo},
       {type:"selfdef", width: 80, name:"timeOut", text:"发送超时", render:sendTimeOut},
       {type:"selfdef", text:"操作", width: 200, render:opts}]
       //{type:"opts", text:"操作", width: 300, opts:[{clickFunc:doDelete, text:"删除该用户的发送记录  "}]}]
  };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  //$('numUser').innerHTML = total;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 "+ total + " 条记录";
    WarningMsrg(mrs, 'msrg');
    showCntrl('delOpt');
  }else{
    WarningMsrg('无符合条件的手机短信记录', 'msrg');
  }
  //var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
  //var rtJsons = getJsonRs(urls , "deptId=" +  deptId);
  //if(rtJsons.rtState == '0'){
   // document.getElementById("newUser").innerHTML = rtJsons.rtData;
   // document.getElementById("manageUser").innerHTML = rtJsons.rtData;
  //}
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:doDelete("+seqId+");\">删除该用户的发送记录</a></center>";
}

function deptFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function userFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function sendSuccess(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getSendSuccess.act";
  var rtJson = getJsonRs(url, "seqId="+seqId+"&beginDate="+beginDate+"&endDate="+endDate);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  }
}
function sendNo(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getSendNo.act";
  var rtJson = getJsonRs(url, "seqId="+seqId+"&beginDate="+beginDate+"&endDate="+endDate);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  }
}
function sendTimeOut(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getsendTimeOut.act";
  var rtJson = getJsonRs(url, "seqId="+seqId+"&beginDate="+beginDate+"&endDate="+endDate);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  }
}

function mobilNo(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var phone = sms3Phone(seqId);
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getPersonPhone.act";
  var rtJson = getJsonRs(url, "mobilNo="+phone);
  if (rtJson.rtState == "0") {
    var userName = rtJson.rtData.userName;
    var mobilNoHidden = rtJson.rtData.mobilNoHidden;
    if(userName != "" && mobilNoHidden == "1"){
      return "不公开";
    }else{
      var phone = sms3Phone(seqId);
      return phone;
    }
  }
}

function doDelete(seqId) {
  if(!confirmSendSign()) {
   	return ;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/deleteSendSign.act";
  var rtJson = getJsonRs(url, "sumStrs=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * subject 描画事件
 */
function bindsubjectRenderAction(cellData, recordIndex, columIndex) {
  var cntrl = $("sub_" + recordIndex + "_" + columIndex);
  var diaId = this.getCellData(recordIndex,"seqId");
  var userId = this.getCellData(recordIndex,"userId");
  cntrl.observe("click", commentEdit.bind(this,diaId,userId));
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;手机短信发送统计(按人员) </span>
    </td>
  </tr>
</table>
 <br/>
<div id="listContainer" style="display:none">
</div>
<div id="msrg"></div>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="window.history.back();">
</div>
</body>
</html>