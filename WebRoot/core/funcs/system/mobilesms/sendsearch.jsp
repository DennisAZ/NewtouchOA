<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String sendFlag = request.getParameter("sendFlag");
  if (sendFlag == null) {
    sendFlag = "";
  }
  String user = request.getParameter("user");
  if (user == null) {
    user = "";
  }
  String phone = request.getParameter("phone");
  phone = YHUtility.encodeSpecial(phone);
  if (phone == null) {
    phone = "";
  }
  String content = request.getParameter("content");
  content = YHUtility.encodeSpecial(content);
  if (content == null) {
    content = "";
  }
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
<title>手机短信管理</title>
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

function doInit() {
  var param = "sendFlag=<%=sendFlag%>&user=<%=user%>&phone=" + encodeURIComponent("<%=phone%>") + "&content=" + encodeURIComponent("<%=content%>") + "&beginDate=<%=beginDate%>&endDate=<%=endDate%>";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getSendSearchList.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"selfdef", text:"选择", width: 40, render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"hidden", name:"fromId", text:"发信人"},
       {type:"selfdef", width: 60, name:"fromIdStr", text:"发信人", render:fromName},  
       {type:"selfdef", width: 60, name:"formId", text:"收信人", render:sendName},  
       {type:"selfdef", width: 80, name:"phone", text:"手机号码", render:mobilNo},       
       {type:"data", width: 300, name:"content", text:"内容"},
       {type:"data", width: 120, name:"sendTime", text:"发送时间", render:sendTimeStyle}, 
       {type:"hidden", name:"sendFlag", text:"状态"},
       {type:"selfdef", width: 120, name:"sendFlag", text:"状态", render:sendFlag},
       {type:"selfdef", text:"操作", width: 60, render:opts}]
  };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  //alert(total);
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

function sendTimeStyle(cellData, recordIndex, columIndex){
  return "<div align=\"center\">" + cellData + "</div>";
}

function sendFlag(cellData, recordIndex, columIndex){
  var sendFlag = this.getCellData(recordIndex,"sendFlag");
  if(sendFlag == "0"){
    return "<center>未发送</center>";
  }
  if(sendFlag == "1"){
    return "<center>发送成功</center>";
  }
  if(sendFlag == "2"){
    return "<center>发送超时，请人工确认</center>";
  }
  if(sendFlag == "3"){
    return "<center>发送中...</center>";
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
      return "<center>不公开</center>";
    }else{
      var phone = sms3Phone(seqId);
      return "<center>" + phone + "</center>";
    }
  }
}

function sms3Phone(seqId){
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getSms2MobilNo.act";
  var rtJson = getJsonRs(url, "seqId="+seqId);
  if (rtJson.rtState == "0") {
    var phone = rtJson.rtData.phone;
      return phone;
  }
}

function fromName(cellData, recordIndex, columIndex){
  var fromId = this.getCellData(recordIndex,"fromId");
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId="+fromId);
  if (rtJson.rtState == "0") {
      return "<center>"+rtJson.rtData+"</center>";
  }
}

function sendName(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var phone = sms3Phone(seqId);
  var userName = personMobilNo(phone);
  var psnName = addressMobilNo(phone);
  if(userName == ""){
    if(psnName == ""){
      return "<center>未知</center>";
    }else{
      return "<center>"+psnName+"</center>";
    }
  }else{
    return "<center>"+userName+"</center>";
  }
}

function personMobilNo(phone){
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getPersonPhone.act";
  var rtJson = getJsonRs(url, "mobilNo="+phone);
  if (rtJson.rtState == "0") {
    var userName = rtJson.rtData.userName;
    var mobileNoHidden = rtJson.rtData.mobileNoHidden;
    return userName;
  }
}

function addressMobilNo(phone){
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getAddressPhone.act";
  var rtJson = getJsonRs(url, "mobilNo="+phone);
  if (rtJson.rtState == "0") {
    var psnName = rtJson.rtData.psnName;
    return psnName;
  }
}

function doDelete(seqId) {
  if(!confirmDel()) {
   	return ;
   } 
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/deleteSelectSms2.act";
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

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}

function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function checkSelf(){
  var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
  }
}

function deleteAllUser() {
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var idStrs = "";
  for(var i = 0; i < deleteAllFlags.length; i++) {
	if(deleteAllFlags[i].checked) {
     idStrs += deleteAllFlags[i].value + "," ;	
     flag = true;
	}	  
  }
  if(idStrs == ""){
    alert("请至少选择其中一个联系人");
    return;
  }
  if(!confirmDelAll()) {
  	return ;
  }  
  var sumStrs = idStrs.substr(0,idStrs.length-1);
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/deleteSelectSms2.act";
  var rtJson = getJsonRs(url, "sumStrs=" + sumStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;手机短信查询结果</span>
    </td>
  </tr>
</table>
<br/>
<div id="listContainer" style="display:none">
</div>
<div id="delOpt" style="display:none">
<table id="beSortTable" class="TableList" width="890">
   <tr class='TableControl'>
     <td colspan='10'>
       &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
       <input type='button' value='批量删除' class='SmallButtonW' onClick="deleteAllUser();" title='批量删除'>&nbsp;&nbsp;
     </td>
   </tr>
</table>
</div>
<div id="msrg"></div>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/system/mobilesms/sendmanage.jsp'">
</div>
</body>
</html>