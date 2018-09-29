<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
 <%
 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
 YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
 %>
<html>
<head>
<title>投票管理</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var dayTime = "<%=sf.format(new Date())%>";
var userPriv = "<%=user.getUserPriv()%>";
var voteStatus = 0;
function doInit(){
  //outVote();
  var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectVote.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    afterShow:insertTr,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"selfdef", name:"Id", text:"选择", width: "4%",align:"center",render:toMing},
       {type:"text", name:"fromId", text:"发布人", width: "6%",align:"center",render:toFromId},
       {type:"hidden", name:"deptName", text:"发布人(部门)", width: "1%",align:"center"},
       {type:"hidden", name:"toId", text:"发布范围(部门)", width: "1%",align:"center"},
       {type:"hidden", name:"privId", text:"发布范围(角色)", width: "1%",align:"center"},
       {type:"hidden", name:"userId", text:"发布范围(人员)", width: "1%",align:"center"},
       {type:"selfdef", name:"str", text:"发布范围", width: "10%",align:"center",render:toStr},
       {type:"text", name:"subject", text:"标题", width: "6%",align:"center",render:toSubject},
       {type:"text", name:"type", text:"类型",align:"center", width: "6%",render:toType},
       {type:"text", name:"anonymity", text:"匿名",align:"center", width: "6%",render:toAnonymity},
       {type:"text", name:"benginDate", text:"生效日期",align:"center", width: "6%",render:toBenginDate},
       {type:"text", name:"endDate", text:"终止日期",align:"center", width: "6%",render:toEndDate},
       {type:"text", name:"publish", text:"状态",align:"center", width: "6%",render:toPublish},
       {type:"hidden", name:"top", text:"置顶",align:"center", width: "1%"},
       {type:"text", name:"caozuo",text:"操作", width:"10%",align:"center",render:toCaoZuo}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据


  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无已发布的投票!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }else {
   // if (userPriv == "1") {
     // var table = new Element('table',{"class":"TableList","width":"100%"}).update("<tr class='TableControl'><td width='100%' colspan='12'>&nbsp;"
       //  + "<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>"
        //  + "&nbsp;全选&nbsp;<a href='javascript:onDelete();' title='删除所选投票'><img src='<%=imgPath %>/delete.gif' align='absMiddle'>删除</a>&nbsp;"
       //   + "<a href='javascript:onAllDelete();' title='删除所有自己发布的投票'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>全部删除</a>&nbsp;"
        //  + "<a href='javascript:onClearVote();' title='清空投票数据'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>清空数据</a>&nbsp;"
      //    + "<a href='javascript:onClonVote();' title='复制投票，但不复制数据'><img src='/yh/core/funcs/filefolder/images/copy.gif' align='absMiddle'>克隆</a>&nbsp;"
      //    + "<a href='javascript:updateNoTopVote();' title='取消所选投票置顶'><img src='<%=imgPath%>/user_group.gif' align='absMiddle'>取消置顶</a>&nbsp;"
      ///    + "</td></tr>");
   // } else {
     // var table = new Element('table',{"class":"TableList","width":"100%"}).update("<tr class='TableControl'><td width='100%' colspan='12'>&nbsp;"
        //  + "<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>"
        //  + "&nbsp;全选&nbsp;<a href='javascript:onDelete();' title='删除所选投票'><img src='<%=imgPath %>/delete.gif' align='absMiddle'>删除</a>&nbsp;"
        //  + "<a href='javascript:onClearVote();' title='清空投票数据'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>清空数据</a>&nbsp;"
         // + "<a href='javascript:onClonVote();' title='复制投票，但不复制数据'><img src='/yh/core/funcs/filefolder/images/copy.gif' align='absMiddle'>克隆</a>&nbsp;"
        //  + "<a href='javascript:updateNoTopVote();' title='取消所选投票置顶'><img src='<%=imgPath%>/user_group.gif' align='absMiddle'>取消置顶</a>&nbsp;"
       //   + "</td></tr>");
   // }
   // $('giftList').appendChild(table);
  }
}
//清空投票数据
function onClearVote() {
  var seqIdStr = "";
  for(var i = 0; i < document.getElementsByName("voteBox").length;i++) {
    var el = document.getElementsByName("voteBox").item(i);
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("voteBox");
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  if(seqIdStr == "") {
    alert("请至少选择其中一条记录。");
    return;
  }
  msg = '确认要清空所选投票数据？';
  if(window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/clearVote.act?seqIds=" + seqIdStr;
    var json = getJsonRs(url);
    window.location.reload();
  }
}
//克隆投票数据
function onClonVote() {
  var seqIdStr = "";
  for(var i = 0; i < document.getElementsByName("voteBox").length;i++) {
    var el = document.getElementsByName("voteBox").item(i);
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("voteBox");
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  if(seqIdStr == "") {
    alert("请至少选择其中一条记录。");
    return;
  }
  msg = '确认要克隆所选投票数据？';
  if(window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/clonVote.act?seqIds=" + seqIdStr;
    var json = getJsonRs(url);
    window.location.reload();
  }
}

//取消置顶
function updateNoTopVote() {
  var seqIdStr = "";
  for(var i = 0; i < document.getElementsByName("voteBox").length;i++) {
    var el = document.getElementsByName("voteBox").item(i);
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("voteBox");
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  if(seqIdStr == "") {
    alert("请至少选择其中一条记录。");
    return;
  }
  msg = '确认要取消置顶所选投票数据？';
  if(window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/updateNoTopVote.act?seqIds=" + seqIdStr;
    var json = getJsonRs(url);
    window.location.reload();
  }
}
//删除所有

function onAllDelete() {
  msg='确认要删除所有投票吗？\n删除后将不可恢复，确认删除请输入大写字母“OK”';
  if(window.prompt(msg,"") == "OK"){
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/deleteAllVote.act";
    var json = getJsonRs(url);
    window.location.reload();
  }
}
//删除
function onDelete() {
  var seqIdStr = "";
  for(var i = 0; i < document.getElementsByName("voteBox").length;i++) {
    var el = document.getElementsByName("voteBox").item(i);
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("voteBox");
    if (el.checked) {
      var val = el.value;
      seqIdStr += val + ",";
    }
  }
  if(seqIdStr == "") {
    alert("请至少选择其中一条记录。");
    return;
  }
  msg = '确认要删除所选记录？';
  if(window.confirm(msg)) {
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/deleteVote.act?seqIds=" + seqIdStr;
    var json = getJsonRs(url);
    window.location.reload();
  }
}
//返回选择
function toMing(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox' name='voteBox' value='" + seqId +"' onClick='javscript:checkOne(self);'>";
}
//返回发布人


function toFromId(cellData, recordIndex,columInde){
  var fromId =  this.getCellData(recordIndex,"fromId");
  var deptName =  this.getCellData(recordIndex,"deptName");
  return "<u title='部门：" + deptName + "' style='cursor:pointer'>" + fromId + "</u>";
}
//发布范围
function toStr(cellData,recordIndex,columIndex){
  var deptStr = "";
  var privStr = "";
  var userStr = "";
  
  var deptStr2 = "";
  var privStr2 = "";
  var userStr2 = "";
  var toId = this.getCellData(recordIndex,"toId");
  var privId = this.getCellData(recordIndex,"privId");
  var userId = this.getCellData(recordIndex,"userId");
  if (toId != "0" && toId != "ALL_DEPT" && toId != "") {
    var str = "?seqId=" + toId + "&tableName=oa_department&tdName=DEPT_NAME";
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    deptStr2 = "部门：" + userList;
    if (userList.length > 5) {
      userList = userList.substring(0,userList.length - (userList.length - 5));
    }
    userList = "<font color='#0000FF'><b>部门：</b></font>" + userList + "... </br>"
    deptStr = userList
  }
  if (toId == "0" || toId == "ALL_DEPT") {
    deptStr2 = "部门：全体部门";
    deptStr = "<font color='#0000FF'><b>部门：</b></font>全体部门&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</br>";
  }
  if (privId != "") {
    var str = "?seqId=" + privId + "&tableName=USER_PRIV&tdName=PRIV_NAME";
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    privStr2 = "\r角色：" + userList;
    if (userList.length > 5) {
      userList = userList.substring(0,userList.length - (userList.length - 5));
    }
    userList = "<font color='#0000FF'><b>角色：</b></font>" + userList + "... </br>"
    privStr = userList;
  }
  if (userId != "") {
    var str = "?seqId=" + userId + "&tableName=PERSON&tdName=USER_NAME";
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/strString.act" + str;
    var rtJson = getJsonRs(requestUrl);
    var userList = rtJson.rtData;
    userStr2 = "\r人员：" + userList;
    if (userList.length > 5) {
      userList = userList.substring(0,userList.length - (userList.length - 5));
    }
    userList = "<font color='#0000FF'><b>人员：</b></font>" + userList + "... </br>"
    userStr = userList;
  } 
  var strString = deptStr + privStr + userStr ;
  var strString2 = deptStr2 + privStr2 + userStr2 ;
  return "<span title='" + strString2 + "' style='cursor:pointer'>" + strString + "</span>";
}
//返回标题
function toSubject(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var top =  this.getCellData(recordIndex,"top");
  var subject =  this.getCellData(recordIndex,"subject");
  if (top == "1") {
    return "<a href='javascript:showSubject(" + seqId + ")'><font color='red'><b>" + subject + "</b></font></a>";
  } else {
    return "<a href='javascript:showSubject(" + seqId + ")'>" + subject + "</a>";
  }
}
//参与投票
function showSubject(seqId) {
  var URL = "/yh/subsys/oa/vote/manage/showVote.jsp?seqId=" + seqId;
  myleft = (screen.availWidth-780)/2 ;
  window.open(URL,"read_vote","height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
}
//返回类型
function toType(cellData, recordIndex,columInde){
  var type =  this.getCellData(recordIndex,"type");
  if (type == "0") {
    return "单选";
  }
  if (type == "1") {
    return "多选";
  }
  if (type == "2") {
    return "文本输入";
  }
}
//返回匿名
function toAnonymity(cellData, recordIndex,columInde){
  var anonymity =  this.getCellData(recordIndex,"anonymity");
  if (anonymity == "0") {
    return "不允许";
  }
  if (anonymity == "1") {
    return "允许";
  }
}
//返回起始时间
function toBenginDate(cellData, recordIndex,columInde){
  var benginDate =  this.getCellData(recordIndex,"benginDate");
  return benginDate.substr(0,10);
}
//返回终止时间
function toEndDate(cellData, recordIndex,columInde){
  var endDate =  this.getCellData(recordIndex,"endDate");
  return endDate.substr(0,10);
}
//返回状态

function toPublish(cellData, recordIndex,columInde){
  var publish =  this.getCellData(recordIndex,"publish");
  var endDate =  this.getCellData(recordIndex,"endDate");
  var benginDate =  this.getCellData(recordIndex,"benginDate");
  endDate = endDate.substr(0,10);
  benginDate = benginDate.substr(0,10);
  if (publish == "0") {
    voteStatus = 0;
    return "<font color='#FF0000'><b>未发布</b></font>";
  }
  if (publish == "1") {
    if (benginDate > dayTime) {
      voteStatus = 1;
      return "<font color='#00AA00'><b>待生效</b></font>";
    }
    if (benginDate <= dayTime && (endDate > dayTime || endDate == "")) {
      voteStatus = 2;
      return "<font color='#00AA00'><b>生效</b></font>"
    }
    if (endDate <= dayTime) {
      voteStatus = 3;
      return "<font color='#FF0000'><b>终止</b></font>"
    }
  }
}
//操作
function toCaoZuo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var subject = this.getCellData(recordIndex,"subject");
  var type =  this.getCellData(recordIndex,"type");
  var re1 = /\'/gi;
  subject = subject.replace(re1,"&lsquo;");
  subject = encodeURIComponent(subject);
  var toup = "";
  if (type != "2") {
    toup = "<a href=\"<%=contextPath%>/subsys/oa/vote/manage/item/index.jsp?seqId=" + seqId + "&subject=" + subject + ";\">投票项</a>&nbsp;";
  }
  if (voteStatus == "0") {
    return "<a href='javascript:showVote(" + seqId + ");'>子投票</a>&nbsp;&nbsp;"
    + toup
    + "<br><a href='javascript:update(" + seqId + ");'>修改</a>&nbsp;&nbsp;"
    + "<a href=javascript:updatePublish(" + seqId + ",'1');>立即发布 </a>"
  }
  if (voteStatus == "1") {
    return "<a href='javascript:showVote(" + seqId + ");'>子投票</a>&nbsp;&nbsp;"
    + toup
    + "<br><a href='javascript:update(" + seqId + ");'>修改</a>&nbsp;&nbsp;"
    + "<a href=javascript:updateBeginDate('" + seqId + "','BEGIN_DATE','" + dayTime + "');>立即生效 </a>"
  }
  if (voteStatus == "2") {
    return "<a href='javascript:showVote(" + seqId + ");'>子投票</a>&nbsp;&nbsp;"
    + toup
    + "<br><a href='javascript:update(" + seqId + ");'>修改</a>&nbsp;&nbsp;"
    + "<a href=javascript:updateBeginDate('" + seqId + "','END_DATE','" + dayTime + "');>立即终止 </a>"
  }
  if (voteStatus == "3") {
    return "<a href='javascript:showVote(" + seqId + ");'>子投票</a>&nbsp;&nbsp;"
    + toup
    + "<br><a href='javascript:update(" + seqId + ");'>修改</a>&nbsp;&nbsp;"
    + "<a href=javascript:updateBeginDate('" + seqId + "','END_DATE','');>恢复生效</a>"
  }
}

//有一个不选中就不是全选

function checkOne(el) {
  if (!el.checked) {
     document.getElementById("allbox").checked = false;
  }
}
//全部选中
function checkAll() {
  for (var i = 0;i < document.getElementsByName("voteBox").length;i++) {
    if (document.getElementsByName("allbox")[0].checked) {
      document.getElementsByName("voteBox").item(i).checked=true;
    }else {
      document.getElementsByName("voteBox").item(i).checked=false;
    }
  }
  if (i == 0) {
    if (document.getElementsByName("allbox")[0].checked) {
      document.getElementsByName("voteBox").checked = true;
    }else {
      document.getElementsByName("voteBox").checked = false;
    }
  }
}
//投票项

function showInfo(seqId,subject) {
  subject = encodeURIComponent(subject);
  window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/item/index.jsp?seqId=" + seqId + "&subject=" + subject;
}
//子投票项
function showVote(seqId) {
  window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/vote.jsp?seqId="+seqId;
}
//修改
function update(seqId) {
  var url = "<%=contextPath%>/subsys/oa/vote/manage/update.jsp?seqId=" + seqId;
  window.location.href = url;
}
//立即生效,立即终止,恢复终止
function updateBeginDate(seqId,tdName,dayTime) {
  var numSeqId1 = voteSeqId(seqId);
  var numSeqId2 = itemSeqId(seqId);
  if (numSeqId1 > 0 || numSeqId2 > 0) {
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/updateBeginDate.act?seqId=" + seqId + "&tdName=" + tdName + "&dayTime=" + dayTime;
    var json = getJsonRs(url);
    window.location.reload();
  } else {
    alert("请先添加投票项或者子投票！");
    parent.seqId = seqId;
    window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/add.jsp";
  }
}
//立即发布
function updatePublish(seqId,publish) {
  var msg = "确认要立即发布！";
  if (window.confirm(msg)) {
    var numSeqId1 = voteSeqId(seqId);
    var numSeqId2 = itemSeqId(seqId);
    if (numSeqId1 > 0 || numSeqId2 > 0) {
      var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/updatePublish.act?seqId=" + seqId + "&publish=" + publish;
      var json = getJsonRs(url);
      window.location.reload();
    } else {
      alert("请先添加投票项或者子投票！");
      parent.seqId = seqId;
      window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/add.jsp";
    }
  }
}
//查询投票项

function itemSeqId(seqId) {
  var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectId.act?seqId=" + seqId; 
  var json = getJsonRs(url);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  return prc.length;
}
//查询子投票项
function voteSeqId(seqId) {
var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectId2.act?seqId=" + seqId;
var json = getJsonRs(url);
if (json.rtState == '1') { 
  alert(json.rtMsrg); 
  return ; 
}
var prc = json.rtData;
return prc.length;
}
function insertTr(){
  var table = pageMgr.getDataTableDom();
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行

  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align='center';
  mynewcell.innerHTML = "<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.colSpan = "9";
  if (userPriv == "1") {
    mynewcell.innerHTML = "全选 &nbsp;<a href='javascript:onDelete();' title='删除所选投票'><img src='<%=imgPath %>/delete.gif' align='absMiddle'>删除</a>&nbsp;"
    + "<a href='javascript:onAllDelete();' title='删除所有自己发布的投票'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>全部删除</a>&nbsp;"
    + "<a href='javascript:onClearVote();' title='清空投票数据'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>清空数据</a>&nbsp;"
    + "<a href='javascript:onClonVote();' title='复制投票，但不复制数据'><img src='/yh/core/funcs/filefolder/images/copy.gif' align='absMiddle'>克隆</a>&nbsp;"
    + "<a href='javascript:updateNoTopVote();' title='取消所选投票置顶'><img src='<%=imgPath%>/user_group.gif' align='absMiddle'>取消置顶</a>&nbsp;"
  }else{
    mynewcell.innerHTML = "全选 &nbsp;<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>"
    + "<a href='javascript:onDelete();' title='删除所选投票'><img src='<%=imgPath %>/delete.gif' align='absMiddle'>删除</a>&nbsp;"
    + "<a href='javascript:onClearVote();' title='清空投票数据'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>清空数据</a>&nbsp;"
    + "<a href='javascript:onClonVote();' title='复制投票，但不复制数据'><img src='/yh/core/funcs/filefolder/images/copy.gif' align='absMiddle'>克隆</a>&nbsp;"
    + "<a href='javascript:updateNoTopVote();' title='取消所选投票置顶'><img src='<%=imgPath%>/user_group.gif' align='absMiddle'>取消置顶</a>&nbsp;"
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<br>
<span class="big3">&nbsp;<img src="<%=imgPath %>/vote.gif" align="absmiddle">&nbsp;管理已发布的投票</span> 
<br>
 <br>
<div id="giftList"></div>
<div id="returnNull">
</div>
</body>
</html>