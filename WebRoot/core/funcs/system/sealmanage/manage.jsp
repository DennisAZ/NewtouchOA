<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String sealId = request.getParameter("sealId");
  if (sealId == null) {
    sealId = "";
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
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>签章管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/sealmanage/js/sealManageUtil.js"></script>
<script type="text/javascript">
function doInit(){
  var param = "sealId=<%=sealId%>&sealName=<%=sealName%>&beginTime=<%=beginTime%>&endTime=<%=endTime%>";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/system/sealmanage/act/YHSealAct/getSealList.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    colums: [
       {type:"selfdef", text:"选择", width: "10%", render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data",width: "10%", name:"sealId", text:"印章ID", render:sealIdFunc},   
       {type:"data", width: "20%", name:"sealName", text:"印章名称", render:sealNameFunc},    
       {type:"data", width: "10%", name:"certStr", text:"证书绑定"},  
       {type:"data", width: "20%", name:"userStr", text:"授权范围", render:subjectRender},  
       {type:"data", width: "20%", name:"creatTime", text:"创建时间", render:creatTimeFunc},     
       {type:"selfdef", text:"操作", width: "10%",render:opts}]
       //{type:"opts", text:"操作", width: 250, opts:[{clickFunc:doEdit, text:"查看 "},{clickFunc:doPriv, text:"设置权限 "},{clickFunc:doSeal, text:"恢复印章 "}]}]
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
    WarningMsrg('尚未创建印章！', 'msrg');
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

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><a href=\"javascript:doEdit("+seqId+");\">查看</a>&nbsp;&nbsp;<a href=\"javascript:doPriv("+seqId+");\">设置权限 </a>&nbsp;&nbsp;</center>";
  //<a href=\"javascript:doSeal("+seqId+");\">恢复印章</a>
}

function subjectRender(cellData, recordIndex, columIndex) {
  var urls = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/getUserName.act";
  var rtJsons = getJsonRs(urls , "userId=" +  cellData);
  if(rtJsons.rtState == '0'){
    return rtJsons.rtData;
  }
}

function doPriv(seqId){
  location.href = "<%=contextPath %>/core/funcs/system/sealmanage/set.jsp?seqId=" + seqId;
}

function doSeal(seqId){
  alert("开发中。。。");
}

/**
 * 编辑
 */
function doEdit(seqId){
 // location.href = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/getShowInfo.act?seqId=" + seqId;
  show_info(seqId);
}

/**
 * 查看页面
 */
function show_info(seqId){
  var url = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/getShowInfo.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    var result = rtJson.rtData
    showInfoStr(result);
  }else {
    alert("无印章信息!");
  }
}


function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
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
  var url = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/deleteSeal.act";
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
  location = "<%=contextPath%>/core/funcs/system/sealmanage/manage.jsp?"+query;
}

function HideDialog(){
  var div = $('apply');
  div.style.display = "none";
  var overlay = $('overlay');
  overlay.style.display = "none";
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;印章查询</span>&nbsp;
    </td>
  </tr>
</table>
 
<form method="post" name="form1" id="form1">
<table width="400px" class="TableList" align="center" >
  <tr>
    <td nowrap class="TableContent" width=80> 印章ID：</td>
    <td nowrap class="TableData" width=110>
    <input type="text" class="SmallInput" size=10  name="sealId" id="sealId" value="">
    </td>
    <td nowrap class="TableContent" width=80> 印章名称：</td>
    <td nowrap class="TableData" width=110>
    <input type="text" class="SmallInput" size=10  name="sealName" id="sealName" value="">
    </td>
  </tr>
      <td nowrap class="TableContent"> 制章时间范围：</td>
      <td nowrap class="TableData" colspan=3>
        从 <input type="text" name="beginTime" id="beginTime" size="20" maxlength="20" class="BigInput" value="">
        <img id="beginTimeImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        <br>至 <input type="text" name="endTime" id="endTime" size="20" maxlength="20" class="BigInput" value="">
        <img id="endTimeImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
  </tr>
  <tr class="TableFooter" >
      <td nowrap align="center" colspan=4>
       <input type="button" class="BigButton" value="查询" onClick="searchCommit();">&nbsp;
      </td>
  </tr>
</table>
</form>

<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;印章权限管理</span>&nbsp;
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

<div id="overlay" style="width: 1238px; height: 610px; display: '';"></div>
<div id="apply" class="ModalDialog" style="display:none;width:400px;">
  <div class="header"><span id="title" class="title">印章信息</span><a class="operation" href="javascript:HideDialog();"><img src="<%=imgPath%>/close.png"/></a></div>
  <div id="apply_body" class="body" align="center">
   <table class="TableList" width="100%">
    <tr>
     <td class="TableData" colspan=2 align="center">
  <OBJECT 
          id=DMakeSealV61 
          style="left: 0px; top: 0px" 
          classid="clsid:3F1A0364-AD32-4E2F-B550-14B878E2ECB1" 
          VIEWASTEXT 
          width="200"
          height="200"
          codebase='<%=contextPath%>/core/funcs/system/sealmanage/sealmaker/MakeSealV6.ocx#version=1,0,2,8'>
  
  <PARAM NAME="_Version" VALUE="65536">
  <PARAM NAME="_ExtentX" VALUE="2646">
  <PARAM NAME="_ExtentY" VALUE="1323">
  <PARAM NAME="_StockProps" VALUE="0">
</OBJECT>
       </td>
     </tr>
     <tr>
      <td class="TableContent" width=80>印章ID</td>
      <td class="TableData"><span id="seal_id"></span></td>
    </tr>
     <tr>
      <td class="TableContent">印章名称</td>
      <td class="TableData"><span id="seal_name"></span></td>
    </tr>    
     <tr>
      <td class="TableContent">印章尺寸</td>
      <td class="TableData"><span id="seal_size"></span></td>
    </tr> 
     <tr>
      <td class="TableContent">印章签名</td>
      <td class="TableData"><span id="seal_sign"></span></td>
    </tr> 
     <tr>
      <td class="TableContent">证书绑定</td>
      <td class="TableData"><span id="seal_cert"></span></td>
    </tr> 
  </table>
  </div>
  <div id="footer" class="footer">
    <input class="BigButton" onclick="HideDialog()" type="button" value="关闭"/>
  </div>
</div>
</body>
</html>