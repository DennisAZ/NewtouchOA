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
  String nameStrs = request.getParameter("nameStrs");
  nameStrs = YHUtility.encodeSpecial(nameStrs);
  if (nameStrs == null){
    nameStrs = "";
  }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>通讯簿</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
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
var seqIdStr = "<%=seqId%>";
var nameStrs = "<%=nameStrs%>";
function doInit(){
  var groupName = "";
  var count = 0;
  document.getElementById("groupName").innerHTML = nameStrs.substr(0, nameStrs.length - 1);
  groupName = nameStrs.substr(0, nameStrs.length - 1);
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getNameIndex.act?seqId="+seqIdStr;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 3,
      sortDirect: "asc",
      colums: [
         {type:"selfdef", text:"选择", width: '10%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"hidden", name:"groupId", text:"是否登录", dataType:"int"},
         {type:"data", name:"psnNo",  width: '10%', text:"排序号" , render:psnNoFunc, sortDef:{type: 1, direct:"asc"}},
         {type:"data", name:"psnName",  width: '10%', text:"姓名", render:psnNameFunc, sortDef:{type: 0, direct:"asc"}},       
         {type:"data", name:"sex",  width: '10%',   text:"性别", render:sexRender},
         {type:"data", name:"deptName",  width: '15%', text:"单位名称", sortDef:{type: 0, direct:"asc"}},
         {type:"data", name:"telNoDept",  width: '10%', text:"工作电话", render:telNoDeptFunc}, 
         //{type:"data", name:"telNoHome",  width: 100, text:"家庭电话", render:telNoHomeFunc},
         {type:"data", name:"mobilNo", width: '10%', text:"手机群发", render:mobilNoFunc},
         {type:"data", name:"email",  width: '10%', text:"电子邮件", render:emailFunc}, 
         {type:"selfdef", text:"操作", width: '15%',render:opts}]
    };
    var pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    //$('numUser').innerHTML = total;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 "+ total + " 条记录 ！";
      //WarningMsrg(mrs, 'msrg');
      showCntrl('delOpt');
    }else{
      WarningMsrg('通讯簿尚无记录！', 'msrg');
    }
}



/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"280\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

/**
 * 隐藏显示控件
 * 
 * @param cntrlId
 * @return
 */
function showCntrl(cntrlId) {
  if ($(cntrlId)) {
    if ($(cntrlId).style.display) {
      $(cntrlId).style.display = '';
    } else {
      $(cntrlId).style.display = 'none';
    }
  }
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  if (diaId == "233") {
    return "";
  }else {
    return "<input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" >";
  }
}

function commitSearch(){
  location = "<%=contextPath %>/core/funcs/address/private/address/search.jsp";
  
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var psnName = this.getCellData(recordIndex,"psnName");
  var groupId = this.getCellData(recordIndex,"groupId");
  return "<center><a href=\"javascript:detail("+seqId+",'"+psnName+"');\">详情</a>&nbsp;<a href='edit.jsp?seqId="+seqId+"&groupId="+groupId+"&groupName="+groupName+"');\">编辑</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
}

function detail(seqId, psnName){
  var URL = "/yh/core/funcs/address/private/address/adddetail.jsp?seqId="+seqId+"&psnName="+encodeURIComponent(psnName);
  openDialog(URL,'750', '650');
}

function sexRender(cellData, recordIndex, columIndex){
  if(cellData == "0"){
    return "<center>男</center>";
  }else if(cellData == "1"){
    return "<center>女</center>";
  }else{
    return "";
  }
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

function checkSelf(){
  var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
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
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;索引结果&nbsp;【<span class="big3" id="groupName"></span>】</span>
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
<div id="noCheck" style="display:none;">
<table class="MessageBox" align="center" width="280">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">通讯簿尚无记录</div>
    </td>
  </tr>
</table>
</div>
</body>
</html>