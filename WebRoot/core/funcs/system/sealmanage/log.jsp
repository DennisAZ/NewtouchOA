<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String logType = request.getParameter("logType");
  if (logType == null) {
    logType = "";
  }
  String sealName = request.getParameter("sealName");
  sealName = YHUtility.encodeSpecial(sealName);
  if (sealName == null) {
    sealName = "";
  }
  String beginTime = request.getParameter("beginTime");
  if (beginTime == null) {
    beginTime = "";
  }
  String endTime = request.getParameter("endTime");
  if (endTime == null) {
    endTime = "";
  }
  String user = request.getParameter("user");
  if (user == null) {
    user = "";
  }
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>印章日志</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/sealmanage/js/sealManageUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function doInit(){
  var param = "logType=<%=logType%>&sealName=<%=sealName%>&beginTime=<%=beginTime%>&endTime=<%=endTime%>&user=<%=user%>";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/system/sealmanage/act/YHSealLogAct/getSealLogList.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"selfdef", text:"选择", width: "10%", render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data",width: "10%", name:"sId", text:"印章名称", render:getSealName},   
       {type:"data", width: "10%", name:"logType", text:"日志类型", render:getLogType},    
       {type:"data", width: "10%", name:"userId", text:"操作者", render:getUserOpName},  
       {type:"data", width: "10%", name:"clientType", text:"操作类别"},  
       {type:"data", width: "20%", name:"logTime", text:"记录时间", render:logTimeFunc}, 
       {type:"data", width: "10%", name:"result", text:"描述", render:resultFunc}, 
       {type:"data", width: "20%", name:"ipAdd", text:"IP/MAC地址", render:ipAddFunc}]
  };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    //var mrs = " 共 "+ total + " 个符合条件且可管理的用户";
    //WarningMsrg(mrs, 'msrg');
    showCntrl('delOpt');
  }else{
    WarningMsrg('无印章使用日志！', 'msrg');
  }
  
  var beginParameters = {
      inputId:'beginTime',
      property:{isHaveTime:true}
      ,bindToBtn:'beginTimeImg'
  };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:true}
      ,bindToBtn:'endTimeImg'
  };
  new Calendar(endParameters);

  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  var time = date.toLocaleTimeString();
  $('endTime').value = y + '-' + m + '-' + d + ' ' + time;
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}

function getSealName(cellData, recordIndex, columIndex){
  
  var sId = this.getCellData(recordIndex,"sId");
  var urls = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealLogAct/getSealName.act";
  var rtJsons = getJsonRs(urls , "sId=" + sId);
  if(rtJsons.rtState == '0'){
    return "<center>" + rtJsons.rtData + "</center>";
  }
}

function getUserOpName(cellData, recordIndex, columIndex){
  var urls = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealLogAct/getUserOpName.act";
  var rtJsons = getJsonRs(urls , "userId=" +  cellData);
  if(rtJsons.rtState == '0'){
    return "<center>" + rtJsons.rtData + "</center>";
  }
}

function getLogType(cellData, recordIndex, columIndex){
  if(cellData == "addseal"){
    cellData = "加盖印章";
    return "<center>" + cellData + "</center>";
  }else if(cellData == "delseal"){
    cellData = "删除印章";
    return "<center>" + cellData + "</center>";
  }
  else if(cellData == "verify"){
    cellData = "验证印章";
    return "<center>" + cellData + "</center>";
  }else if(cellData == "makeseal"){
    cellData = "制作印章";
    return "<center>" + cellData + "</center>";
  }else if(cellData == "setseal"){
    cellData = "印章授权";
    return "<center>" + cellData + "</center>";
  }else if(cellData == "writeseal"){
    cellData = "恢复印章";
    return "<center>" + cellData + "</center>";
  }
}

/**
 * 全选
 */
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
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("请至少选择一条日志。");
    return;
  }
  if(!confirmDel()) {
   return ;
  }  
  var url = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealLogAct/deleteSealLog.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function confirmDel() {
  if(confirm("确认要删除所选用户吗？")) {
    return true;
  }else {
    return false;
  }
}

function searchCommit(){
  var beginDate = $("beginTime").value;
  var endDate = $("endTime").value;
  if(!Test($("beginTime"),$("endTime"))){
    return;
  }
  var query = "";
  query = $("form1").serialize();
  location = "<%=contextPath%>/core/funcs/system/sealmanage/log.jsp?"+query;
  
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3">&nbsp;印章日志查询</span>
    </td>
  </tr>
</table>

<form method="post" name="form1" id="form1">
<table width="500px" class="TableList" align="center" >
  <tr>
    <td nowrap class="TableContent"> 印章名称：</td>
    <td nowrap class="TableData">
    <input type="text" class="SmallInput" size=10  name="sealName" id="sealName">
    </td>
    <td nowrap class="TableContent"> 日志类别：</td>
    <td nowrap class="TableData">
      <select name="logType" id="logType" class="SmallSelect">
      	<option value="">所有日志</option>
      	<option value="addseal">加盖印章</option>
      	<option value="delseal">委托日志</option>
      	<option value="makeseal">制作印章</option>
      	<option value="setseal">印章授权</option>
      	<option value="writeseal">恢复印章</option>       	
      </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"> 使用者：</td>
    <td nowrap class="TableData" colspan=3>
    <input type="hidden" class="SmallInput" size=20 readOnly name="user" id="user">
    <textarea name="userDesc" id="userDesc" readOnly rows="1" class="SmallStatic" cols="20"></textarea>
    <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['user','userDesc']);">选择</a>
    <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
      <td nowrap class="TableContent"> 发生时间范围：</td>
      <td nowrap class="TableData" colspan=3>
        从 <input type="text" name="beginTime" id="beginTime" size="20" maxlength="20" class="BigInput" value="">
        <img id="beginTimeImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        至 <input type="text" name="endTime" id="endTime" size="20" maxlength="20" class="BigInput" value="">
        <img id="endTimeImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
  </tr>
  <tr class="TableFooter" >
      <td nowrap align="center" colspan=4>
      	<input type="button" onclick="searchCommit();" class="BigButton" value="查询">&nbsp;
      </td>
  </tr>
</form>
</table>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;印章日志</span>&nbsp;
    </td>
</table>
<br/>
<div id="listContainer" style="display:none">
</div>
<div id="delOpt" style="display:none">
<table id="beSortTable" class="TableList" width="100%">
   <tr class='TableControl'>
     <td colspan='10'>
       &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
       <a href="javascript:deleteAllUser();" title="删除所选印章"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除</a>&nbsp;
     </td>
   </tr>
</table>
</div>
<div id="msrg">
</div>


</table>
</body>
</html>