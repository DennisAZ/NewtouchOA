<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
String seqId = String.valueOf(person.getSeqId());
%>
<html>
<head>
<title>工作计划</title>
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">
var pageMgr = null;
var cfgs = null;
function doInit(){
  var timestamps = new Date().valueOf();
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/workSelect.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    moduleName:"work_plan",   
    colums: [
       {type:"text", name:"seqId", text:"序号 ",align:"center", width:"5%",sortDef:{type:0, direct:"desc"}},
       {type:"text", name:"name", text:"计划名称", width: "10%",align:"center",render:toName,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"bengTime", text:"起始时间", width: "6%",align:"center",render:toDate,sortDef:{type:0,direct:"asc"}},
       {type:"text", name:"endTime", text:"结束时间", width: "6%",align:"center",render:toDate2},
       {type:"text", name:"type", text:"计划类别", width: "6%",align:"center",sortDef:{type:0,direct:"asc"}},
       {type:"text", name:"userName", text:"负责人",align:"center", width: "6%",render:toUser},
       {type:"text", name:"userDesc", text:"参与人",align:"center", width: "6%",render:toDesc},
       {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
       {type:"text", name:"attach", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"text", name:"publish", text:"状态", width:"6%",align:"center",render:toPublish},
       {type:"text", name:"caozuo", text:"操作 ",width:"10%",align:"center",render:toCaozuo},
       {type:"hidden", name:"create", text:"创建人 ",width:"6%",align:"center"}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件工作计划</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }
  var div = new Element('div',{"class":"TableControl","align":"center", style: "padding-top: 10px"}).update("<input type='button' class='BigButton' value='全部删除' onClick='javascript:workDeleteAll();' title='删除所有自己发布的工作计划' >");
  $('giftList').appendChild(div); 
  typeName();
}
function toName(cellData, recordIndex, columIndex){
  var name = this.getCellData(recordIndex,"name");
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='javascript:workDetail(" + seqId +")'>" +name + "</a>&nbsp;<input type='button' onClick='myOpen(" + seqId +")' class='SmallButton' value='进度图' title='查看进度图'>";
}
//时间前10
function toDate(cellData, recordIndex, columIndex){
  var bengTime = this.getCellData(recordIndex,"bengTime");
  return bengTime.substr(0,10);
}
//时间前10
function toDate2(cellData, recordIndex, columIndex){
  var endTime = this.getCellData(recordIndex,"endTime");
  if (endTime != null && endTime != "") {
    return endTime.substr(0,10);
  }else {
    return "";
  }
}
//状态
function toPublish(cellData, recordIndex, columIndex){
  var date = new Date();
  var year = date.getYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  if (month < 10) {
    month = "0" + month;
  }
  if (day < 10) {
    day = "0" + day;
  }
  var zong = year + "-" + month + "-" + day;
  
  var publish = this.getCellData(recordIndex,"publish");
  var bengTime2 = this.getCellData(recordIndex,"bengTime");
  var endTime2 = this.getCellData(recordIndex,"endTime");
  
  var endTime = endTime2.substr(0,10);
  var bengTime = bengTime2.substr(0,10);
  if (publish == "2") {
    return "<font color='red'>暂停</font>";
  }
  if (publish != "0" && (publish == "3" || endTime <= zong ) && endTime != null && endTime != "") {
    return "<font color='#FF0000'>已结束</font>";
  }
  if (publish == "1" && bengTime <= zong && (endTime == null || endTime == "")) {
    return "<font color='#00AA00'>进行中</font>";
  }
  if (publish == "1" && bengTime <= zong && endTime > zong) {
    return "<font color='#00AA00'>进行中</font>";
  }
  if (publish == "0") {
    return "<font color='#FF0000'>未发布</font>";
  }
  if (bengTime > zong) {
    return "<font>未开始</font>";
  }
  
}
//操作
function toCaozuo(cellData, recordIndex, columIndex){
  var date = new Date();
  var year = date.getYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  if (month < 10) {
    month = "0" + month;
  }
  if (day < 10) {
    day = "0" + day;
  }
  var zong = year + "-" + month + "-" + day;
  
  var publish = this.getCellData(recordIndex,"publish");
  var endTime2 = this.getCellData(recordIndex,"endTime");
  var bengTime2 = this.getCellData(recordIndex,"bengTime");
  var seqId = this.getCellData(recordIndex,"seqId");
  var createId = this.getCellData(recordIndex,"seqId");
  
  var endTime = endTime2.substr(0,10);
  var bengTime = bengTime2.substr(0,10);
  var create = this.getCellData(recordIndex,"create");
  var userName = this.getCellData(recordIndex,"userName");
  if (userName == null || userName == "") {
    userName = "0";
  }
  var loginId = <%=seqId%>;
  if (publish == "2") {
    if (loginId == '1' || loginId == create) {
      return "<a href=javascript:updateWork('" + seqId + "');>修改</a>&nbsp;"
      + "<a href=javascript:workDelete('" + seqId + "')>删除</a>&nbsp;"
      + "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
      + "<a href=javascript:updateJix('" + seqId + "','1');>继续</a>&nbsp;&nbsp;"
      + "<a href=javascript:updateJiesu('" + seqId + "','3','" + zong + "');>结束</a>";
    }
    for (var i = 0; i < userName.split(",").length; i++) {
      if (loginId == userName.split(",")[i]) {
        return "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
        + "<a href=javascript:updateJix('" + seqId + "','1');>继续</a>&nbsp;&nbsp;"
        + "<a href=javascript:updateJiesu('" + seqId + "','3','" + zong + "');>结束</a>";
      }
    }
  }
  if (publish != "0" && (publish == "3" || endTime <= zong) && endTime != null && endTime != "") {
    if (loginId == '1' || loginId == create) {
      return "<a href=javascript:updateWork('" + seqId + "');>修改</a>&nbsp;"
      + "<a href=javascript:workDelete('" + seqId + "')>删除</a>&nbsp;"
      + "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
      + "<a href=javascript:updateJiesu('" + seqId + "','1','');>生效</a>";
    }
    for (var i = 0; i < userName.split(",").length; i++) {
      if (loginId == userName.split(",")[i]) {
        return "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
        + "<a href=javascript:updateJiesu('" + seqId + "','1','');>生效</a>";
      }
    }
  }
  if (bengTime > zong) {
    if (loginId == '1' || loginId == create) {
      return "<a href=javascript:updateWork('" + seqId + "');>修改</a>&nbsp;"
      + "<a href=javascript:workDelete('" + seqId + "')>删除</a>&nbsp;"
      + "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
      + "<a href=javascript:updateShengX('" + seqId + "','1','" + zong + "');>生效</a>";
    }
    for (var i = 0; i < userName.split(",").length; i++) {
      if (loginId == userName.split(",")[i]) {
        return "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
        + "<a href=javascript:updateShengX('" + seqId + "','1','" + zong + "');>生效</a>";
      }
    }
  }
  if (publish == "1" && (endTime == null || endTime == "") && bengTime <= zong) {
    if (loginId == '1' || loginId == create) {
      return "<a href=javascript:updateWork('" + seqId + "');>修改</a>&nbsp;"
      + "<a href=javascript:workDelete('" + seqId + "')>删除</a>&nbsp;"
      + "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
      + "<a href=javascript:updateJix('" + seqId + "','2');>暂停</a>&nbsp;&nbsp;"
      + "<a href=javascript:updateJiesu('" + seqId + "','3','" + zong + "');>结束</a>";
    }
    for (var i = 0; i < userName.split(",").length; i++) {
      if (loginId == userName.split(",")[i]) {
        return "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
        + "<a href=javascript:updateJix('" + seqId + "','2');>暂停</a>&nbsp;&nbsp;"
        + "<a href=javascript:updateJiesu('" + seqId + "','3','" + zong + "');>结束</a>";
      }
    }
  }
  if (publish == "1" && endTime > zong && bengTime <= zong) {
    if (loginId == '1' || loginId == create) {
      return "<a href=javascript:updateWork('" + seqId + "');>修改</a>&nbsp;"
      + "<a href=javascript:workDelete('" + seqId + "')>删除</a>&nbsp;"
      + "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
      + "<a href=javascript:updateJix('" + seqId + "','2');>暂停</a>&nbsp;&nbsp;"
      + "<a href=javascript:updateJiesu('" + seqId + "','3','" + zong + "');>结束</a>";
    }
    for (var i = 0; i < userName.split(",").length; i++) {
      if (loginId == userName.split(",")[i]) {
        return "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
        + "<a href=javascript:updateJix('" + seqId + "','2');>暂停</a>&nbsp;&nbsp;"
        + "<a href=javascript:updateJiesu('" + seqId + "','3','" + zong + "');>结束</a>";
      }
    }
  }
  if (publish == "0") {
    if (loginId == '1' || loginId == create) {
      return "<a href=javascript:updateWork('" + seqId + "');>修改</a>&nbsp;"
      + "<a href=javascript:workDelete('" + seqId + "')>删除</a>&nbsp;"
      + "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
      + "<a href=javascript:updateJiesu('" + seqId + "','1','');>生效</a>";
    }
    for (var i = 0; i < userName.split(",").length; i++) {
      if (loginId == userName.split(",")[i]) {
        return + "<a href=javascript:opinion('" + seqId + "');>批注</a>&nbsp;<br>"
        + "<a href=javascript:updateJiesu('" + seqId + "','1','');>生效</a>";
      }
    }
  }
}

function typeName() {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanTypeAct/planType.act";
  var json = getJsonRs(url);
  var rtData = json.rtData;
  for(var i = 0; i < rtData.length; i++) {
    var opt = document.createElement("option");      
    opt.value = rtData[i].seqId;      
    opt.text = rtData[i].typeName;       
    var selectObj = $('WORK_TYPE');
    selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0);
  }
}
function getParam(){
  queryParam = $("form1").serialize();
  return queryParam;
}
//查询
function queryGift(){
  document.getElementById("type").value = document.getElementById("WORK_TYPE").value;
  document.getElementById("status").value = document.getElementById("SELECT_STATUS").value;
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('giftList').style.display="";
    $('returnNull').style.display="none"; 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件工作计划 </div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').style.display=""; 
    $('returnNull').update(table);  
  }
}
//负责人
function toUser(cellData,recordIndex,columIndex){
  var userName = this.getCellData(recordIndex,"userName");
  var seqId = "?seqId=" + userName;
  var requestUrl = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/userName.act" + seqId;
  var rtJson = getJsonRs(requestUrl);
  var userList = rtJson.rtData;
  return userList;
}
//参与人
function toDesc(cellData,recordIndex,columIndex){
  var userDesc = this.getCellData(recordIndex,"userDesc");
  var seqId = "?seqId=" + userDesc;
  var requestUrl = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/userName.act" + seqId;
  var rtJson = getJsonRs(requestUrl);
  var userList = rtJson.rtData;
  return userList;
}
//详情
function workDetail(seqId) {
  window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/workSelect2.act?seqId=" + seqId,"myOpen","height=650,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
}
//进度图
function myOpen(seqId) {
  window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai2.act?seqId=" + seqId,"myOpen2","status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=250,top=0,resizable=yes");
}
//删除所有自己发布的
function workDeleteAll() {
  var msg = "确认要删除所有工作计划吗?";
  if (window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/deleteWorkAll.act";
    var rtJson = getJsonRs(url);
    queryGift();
  }
}
//修改工作计划
function updateWork(seqId) {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updatePlu2.act?seqId=" + seqId;
  window.location = url;
}
//领导批注
function opinion(seqId) {
  window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + seqId,"myOpen","height=550,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
}
//删除
function workDelete(seqId) {
    var msg = "确认要删除该工作计划吗?";
    if (window.confirm(msg)) {
      var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/deleteWork.act?seqId=" + seqId;
      var rtJson = getJsonRs(url);
      queryGift();
    }
}
//继续,暂停
function updateJix(seqId,pub) {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updatePlu.act?seqId=" + seqId + "&pub=" + pub;
  var rtJson = getJsonRs(url);
  queryGift();
}
//结束,生效
function updateJiesu(seqId,pub,time) {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updatePlu4.act?seqId=" + seqId + "&pub=" + pub + "&time=" + time;
  var rtJson = getJsonRs(url);
  queryGift();
}
//今日计划
function workYes(userId) {
  window.open("<%=contextPath%>/core/funcs/calendar/workplan/month.jsp?userId="+userId,"myYes","height=650,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
}
//未发布中的生效
function updateShengX(seqId,pub,time) {
  var url = "<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/updateShengX.act?seqId=" + seqId + "&pub=" + pub + "&time=" + time;
  var rtJson = getJsonRs(url);
  queryGift();
}
</script>
</head>
<body onLoad="doInit();" topmargin="5">
<table border="0" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=contextPath%>/core/styles/style3/img/edit.gif" width="18"
      height="18">&nbsp;<span class="big3">工作计划管理 </span> <select
      name="WORK_TYPE" id="WORK_TYPE"
      onchange="queryGift()">
      <option value="0">所有计划</option>
    </select> <select name="SELECT_STATUS" id="SELECT_STATUS"
      onchange="queryGift()">
      <option value="0">所有任务</option>
      <option value="2">结束计划</option>
      <option value="1">未结束计划</option>
    </select></td>
  </tr>
</table>
<div>说明：OA管理员管理所有的工作计划；创建人、负责人管理自己创建或负责的工作计划。</div>
<br>
<form method="post" name="form1" id="form1">
<input type="hidden" name="type" id="type" value="0">
<input type="hidden" name="status" id="status" value="0">
</form>
<div id="giftList" style="padding-left: 10px;"></div>
<div id="returnNull">
</div>
<br>
</body>
</html>