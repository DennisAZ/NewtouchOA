<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  String psnName = request.getParameter("psnName");
  psnName = YHUtility.encodeSpecial(psnName);
  if (psnName == null){
    psnName = "";
  }
  String sex = request.getParameter("sex");
  if (sex == null){
    sex = "";
  }
  String nickName = request.getParameter("nickName");
  nickName = YHUtility.encodeSpecial(nickName);
  if (nickName == null){
    nickName = "";
  }
  String deptName = request.getParameter("deptName");
  deptName = YHUtility.encodeSpecial(deptName);
  if (deptName == null){
    deptName = "";
  }
  String telNoDept = request.getParameter("telNoDept");
  telNoDept = YHUtility.encodeSpecial(telNoDept);
  if (telNoDept == null){
    telNoDept = "";
  }
  String addDept = request.getParameter("addDept");
  addDept = YHUtility.encodeSpecial(addDept);
  if (addDept == null){
    addDept = "";
  }
  String telNoHome = request.getParameter("telNoHome");
  telNoHome = YHUtility.encodeSpecial(telNoHome);
  if (telNoHome == null){
    telNoHome = "";
  }
  String addHome = request.getParameter("addHome");
  addHome = YHUtility.encodeSpecial(addHome);
  if (addHome == null){
    addHome = "";
  }
  String notes = request.getParameter("notes");
  notes = YHUtility.encodeSpecial(notes);
  if (notes == null){
    notes = "";
  }
  String groupId = request.getParameter("groupId");
  if (groupId == null){
    groupId = "";
  }
  String beginDate = request.getParameter("beginDate");
  if (beginDate == null){
    beginDate = "";
  }
  String endDate = request.getParameter("endDate");
  if (endDate == null){
    endDate = "";
  }
  String mobileNo = request.getParameter("mobileNo");
  if (mobileNo == null){
    mobileNo = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>联系人查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/address/private/js/privateUtil.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var psnNames = "<%=psnName%>";
var sexy = "<%=sex%>";
var nickNames = encodeURIComponent("<%=nickName%>");
var deptNames = "<%=deptName%>";
var telNoDepts = "<%=telNoDept%>";
var addDepts = "<%=addDept%>";
var telNoHomes = "<%=telNoHome%>";
var addHomes = "<%=addHome%>";
var notesStr = "<%=notes%>";
var groupIds = "<%=groupId%>";
var beginDate = "<%=beginDate%>";
var endDate = "<%=endDate%>";
var mobileNo = "<%=mobileNo%>";

function doInit(){
  var formIdName = "";
  var toIdName = "";
  var userSum = "";
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getSearchGroup.act?psnName="+encodeURIComponent(psnNames)+"&sex="+sexy+"&nickName="+nickNames
                 +"&deptName="+encodeURIComponent(deptNames)+"&telNoDept="+encodeURIComponent(telNoDepts)+"&addDept="+encodeURIComponent(addDepts)
                 +"&telNoHome="+encodeURIComponent(telNoHomes)+"&addHome="+encodeURIComponent(addHomes)+"&notes="+encodeURIComponent(notesStr)
                 +"&groupId="+groupIds+"&beginDate="+beginDate+"&endDate="+endDate+"&mobileNo="+encodeURIComponent(mobileNo);
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
           {type:"selfdef", text:"选择", width: '10%', render:checkBoxRender},
           {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
           {type:"hidden", name:"userId", text:"公共默认", dataType:"int"},
           {type:"data", name:"psnName",  width: '10%', text:"姓名", render:psnNameFunc, sortDef:{type: 0, direct:"asc"}},       
           {type:"data", name:"sex",  width: '10%',   text:"性别", render:sexRender},
           {type:"data", name:"deptName",  width: '15%', text:"单位名称", render:deptNameFunc, sortDef:{type: 0, direct:"asc"}},
           {type:"data", name:"telNoDept",  width: '10%', text:"工作电话", render:telNoDeptFunc}, 
           {type:"data", name:"mobilNo", width: '10%', text:"手机群发", render:mobilNoFunc, bindAction:bindAction, bindTitleAction: bindTitleAction, selfTitleStyle:{cursor:"pointer", textDecoration:"underline"}},
           {type:"data", name:"email",  width: '10%', text:"电子邮件", render:emailFunc}, 
           {type:"data", name:"groupId",  width: '10%', text:"组名", render:groupNameFunc}, 
           {type:"selfdef", text:"操作", width: '15%',render:opts}]
    };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 "+ total + " 条记录 ！";
    //WarningMsrg(mrs, 'msrg');
    showCntrl('delOpt');
  }else{
    WarningMsrg('无符合条件的联系人', 'msrg');
  }
}

function bindAction(cellData, recordIndex, columIndex) {
  var mobilNo = this.getCellData(recordIndex,"mobilNo");
}
/**
 * 绑定到栏目标题的函数
 * @columeIndex          栏目的索引
 */
function bindTitleAction(columIndex) {
  var cntrl = $("headCell_" + columIndex);
  cntrl.observe('click', function(){
    var recordCnt = this.getRecordCnt();
    var tmpStr = "";
    for (var i = 0; i < recordCnt; i++) {
      if(this.getCellData(i, "mobilNo") != ""){
        tmpStr += this.getCellData(i, "mobilNo");
      }
      if (i < recordCnt - 1) {
        tmpStr += ",";
      }
    }
    location = contextPath + "/core/funcs/mobilesms/new/index.jsp?outUser="+ tmpStr;
  }.bind(this));
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  var userId = this.getCellData(recordIndex,"userId");
  if(userId != ""){
    return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
  }else{
    return "";
  }
}

function checkSelf(){
  var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
  }
}

function groupNameFunc(cellData, recordIndex, columIndex){
  var groupName = "";
  var groupId = cellData;
  if(groupId != 0){
    var urlss = contextPath + '/yh/core/funcs/address/act/YHAddressAct/getGroupName.act';
    var rtJsonss = getJsonRs(urlss, "seqId="+groupId);
    if (rtJsonss.rtState == "0") {
      groupName = rtJsonss.rtData[0].groupName;
    }
  }
  if(groupId == 0){
    groupName = "默认";
  }
  return groupName;
}

function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/deleteContact.act";
  var rtJson = getJsonRs(url, "sumStrs=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
    parent.menu.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var psnName = this.getCellData(recordIndex,"psnName");
  var groupId = this.getCellData(recordIndex,"groupId");
  var userId = this.getCellData(recordIndex,"userId");
  if(userId == ""){
    return "<center><a href=\"javascript:detail("+seqId+");\">详情</a></center>";
  }else{
    return "<center><a href=\"javascript:detail("+seqId+");\">详情</a>&nbsp;<a href='edit.jsp?seqId="+seqId+"&groupId="+groupId+"'>编辑</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
  }
}

function detail(seqId){
  var URL = "/yh/core/funcs/address/private/address/adddetail.jsp?seqId="+seqId;
  openDialog(URL,'750', '610');
}

function commitSearch(){
  location = "<%=contextPath %>/core/funcs/address/private/address/search.jsp";
}

function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function confirmDel() {
  if(confirm("确认要删除所选联系人吗？")) {
    return true;
  } else {
    return false;
  }
}

function deleteAllUser() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除联系人，请至少选择其中一个。");
    return;
  }
  if(!confirmDel()) {
   return ;
  }  
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/deleteContact.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    parent.menu.location.reload();
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}


</script>
</head>
<body topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;查询结果</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;">
</div>
<div id="delOpt" style="display:none">
 <table id="beSortTable" class="TableList" width="100%" align="center">
   <tr class='TableControl'>
     <td colspan='10'>
       &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
       <input type='button' value='删除' class='SmallButtonW' onClick='deleteAllUser();' title='删除邮件'>
     </td>
   </tr>
</table>
</div>
<div id="msrg">
</div>
</br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="location='search.jsp';">
</div>
</body>
</html>