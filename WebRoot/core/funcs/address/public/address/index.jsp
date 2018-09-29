<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String groupId = request.getParameter("groupId");
  if (groupId == null){
    groupId = "";
  }
  String groupName = request.getParameter("groupName");
  groupName = YHUtility.encodeSpecial(groupName);
  if (groupName == null){
    groupName = "";
  }
  String groupFlag = request.getParameter("groupFlag");
  if (groupFlag == null){
    groupFlag = "";
  }
  String userId = request.getParameter("userId");
  if (userId == null){
    userId = "";
  }
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  boolean isAdminRole = loginUser.isAdminRole();
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/address/public/js/publicUtil.js"></script>
<script type="text/javascript">
var isAdmin = <%=loginUser.isAdminRole()%>;
var groupIdStr = "<%=groupId%>";
var groupName = "";
var groupFlag = "<%=groupFlag%>";
var userIds = "<%=userId%>";
var isPriv = "0";
function doInit(){
  checkSelectBox();
  checkSelectCopyBox();
  isPrivFunc();
  var count = 0;
  if(groupFlag != "0"){
    var urls = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getGroupName.act";
    var rtJsons = getJsonRs(urls, "seqId="+groupIdStr); //根据groupId获取groupName
    if(rtJsons.rtState == "0"){
      groupName = rtJsons.rtData[0].groupName;
      //document.getElementById("groupName").innerHTML = groupName;
      document.getElementById("groupManage").innerHTML = groupName;
      
    }else{
  	  alert(rtJsons.rtMsrg); 
    }
  }else if(groupFlag == "0"){
    //document.getElementById("groupName").innerHTML = "默认";
    document.getElementById("groupManage").innerHTML = "默认";
  }
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicManageContact.act?seqId="+groupIdStr+"&userId="+userIds;
  var cfgs = "";
  if(isPriv == "1"){
     cfgs = {
        dataAction: url,
        container: "listContainer",
        sortIndex: 2,
        sortDirect: "asc",
        colums: [
           {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
           {type:"hidden", name:"groupId", text:"是否登录", dataType:"int"},
           {type:"data", name:"psnNo",  width: '10%', text:"排序号" , render:psnNoFunc, sortDef:{type: 1, direct:"asc"}},
           {type:"data", name:"psnName",  width: '10%', text:"姓名", render:psnNameFunc, sortDef:{type: 0, direct:"asc"}},       
           {type:"data", name:"sex",  width: '10%',   text:"性别", render:sexRender},
           {type:"data", name:"deptName",  width: '15%', text:"单位名称", render:deptNameFunc, sortDef:{type: 0, direct:"asc"}},
           {type:"data", name:"telNoDept",  width: '10%', text:"工作电话", render:telNoDeptFunc}, 
           //{type:"data", name:"telNoHome",  width: 100, text:"家庭电话", render:telNoHomeFunc},
           {type:"data", name:"mobilNo", width: '20%', text:"手机群发", render:mobilNoFunc},
           {type:"data", name:"email",  width: '10%', text:"电子邮件", render:emailFunc}, 
           {type:"selfdef", text:"操作", width: '15%',render:opts}]
      };
  }else{
     cfgs = {
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
           {type:"data", name:"deptName",  width: '15%', text:"单位名称", render:deptNameFunc, sortDef:{type: 0, direct:"asc"}},
           {type:"data", name:"telNoDept",  width: '10%', text:"工作电话", render:telNoDeptFunc}, 
           //{type:"data", name:"telNoHome",  width: 100, text:"家庭电话", render:telNoHomeFunc},
           {type:"data", name:"mobilNo", width: '10%', text:"手机群发", render:mobilNoFunc, bindAction:bindAction, bindTitleAction: bindTitleAction, selfTitleStyle:{cursor:"pointer", textDecoration:"underline"}},
           {type:"data", name:"email",  width: '10%', text:"电子邮件", render:emailFunc}, 
           {type:"selfdef", text:"操作", width: '15%',render:opts}]
      };
  }
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
      WarningMsrg('通讯簿尚无记录', 'msrg');
    }
    if(isPriv == "1"){
      $("newPerson").style.display = "none";  
      $("delOpt").style.display = "none";  
    }
}

function bindAction(cellData, recordIndex, columIndex) {
  var mobilNo = this.getCellData(recordIndex,"mobilNo");
  //var cntrl = $("span_" + recordIndex + "_" + columIndex);
  //cntrl.observe("click", function(){alert(this.pageInfo.pageIndex);}.bind(this));
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

function isPrivFunc(){
  var urls = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicSupportPriv.act";
  var rtJsons = getJsonRs(urls , "groupId=" +  groupIdStr);
  if(rtJsons.rtState == '0'){
    if(rtJsons.rtData == "1"){
      isPriv = "0";
    }else{
      isPriv = "1";
    }
  }
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var psnName = this.getCellData(recordIndex,"psnName");
  var groupId = this.getCellData(recordIndex,"groupId");
  var urls = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicSupportPriv.act";
  var rtJsons = getJsonRs(urls , "groupId=" +  groupIdStr);
  if(rtJsons.rtState == '0'){
    if(rtJsons.rtData == "1"){
      return "<center><a href=\"javascript:detail("+seqId+");\">详情</a>&nbsp;<a href='edit.jsp?seqId="+seqId+"&groupId="+groupId+"&groupName="+encodeURIComponent(groupName)+"');\">编辑</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
    }else{
      return "<center><a href=\"javascript:detail("+seqId+");\">详情</a></center>";
    }
  }
}

function detail(seqId){
  var URL = "/yh/core/funcs/address/public/address/adddetail.jsp?seqId="+seqId;
  openDialog(URL,'750', '650');
}

function commitSearch(){
  location = "<%=contextPath %>/core/funcs/address/public/address/search.jsp";
}

function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
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
  var sumStrs = idStrs.substr(0,idStrs.length-1);
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/deletePublicContact.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function deleteSingle(seqId){
  if(!confirmDel()) {
  	return ;
  }  
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/deletePublicContact.act";
  var rtJson = getJsonRs(url, "sumStrs=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function checkSelectCopyBox(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicChangePersonGroup.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('selectCopyBoxId');
  selectObj.lenght = 1;
  if(rtJson.rtState == "0"){
  	for(var i = 0; i < rtJson.rtData.length; i++){
  	  var option = document.createElement("option");
  	  option.value = rtJson.rtData[i].seqId;
  	  option.innerHTML = rtJson.rtData[i].groupName;
  	  selectObj.appendChild(option);
      if(groupIdStr && (groupIdStr == rtJson.rtData[i].seqId)){
        $('selectCopyBoxId').value = groupIdStr;
      }
  	}
    if(groupFlag == "0"){
      <%if(isAdminRole) { %>
        selectObj.options[0].selected = true;
      <% } %>
    }
  }
}

function checkSelectBox(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicChangePersonGroup.act";
  var rtJson = getJsonRs(url);
  var selectObj =  $('selectBoxId');
  selectObj.length = 1;
  if(rtJson.rtState == "0"){
   for(var i = 0; i < rtJson.rtData.length; i++){
     var option = document.createElement("option");
     option.value = rtJson.rtData[i].seqId;
     option.innerHTML = rtJson.rtData[i].groupName;
     selectObj.appendChild(option);
      if(groupIdStr && (groupIdStr == rtJson.rtData[i].seqId)){
        $('selectBoxId').value = groupIdStr;
      }
   }
    if(groupFlag == "0"){
      <%if(isAdminRole) { %>
        selectObj.options[0].selected = true;
      <% } %>
    }
  }
}

function changeBox(dom){
  var ids = getChecked();
  if(ids == "") {
    alert("要转移联系人分组，请至少选择其中一条数据");
    //$('opForm').reset();
    //window.location.reload();
    checkSelectBox();
    return;
  }
  var groupId = dom.value;
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/changePublicGroup.act?seqIds="+ ids + "&groupId=" + groupId +"&groupIdOld="+ groupIdStr;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    window.location.reload();
    parent.menu.location.reload();
  }
}

/**
 * 复制到其它组
 */
function changeCopyBox(dom){
  var ids = getChecked();
  if(ids == "") {
    alert("要转移联系人分组，请至少选择其中一条数据");
    //$('opForm').reset();
    //window.location.reload();
    checkSelectCopyBox();
    return;
  }
  var groupId = dom.value;
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/changePublicCopyGroup.act?seqIds="+ ids + "&groupId=" + groupId +"&groupIdOld="+ groupIdStr;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    window.location.reload();
    parent.menu.location.reload();
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">

<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;公共地址簿&nbsp;(<span class="big3" id="groupManage"></span>)</span>
     <span id="newPerson"> &nbsp;&nbsp;&nbsp;<input type="button" value="新建联系人" class="BigButton" onClick="location='newcontact.jsp?seqId=<%=groupId%>';" title="在“客户”分组中创建新的联系人，录入相关信息"></span>
    </td>
  </tr>
</table>
<div id="listContainer" style="display:none;">
</div>
<div id="delOpt" style="display:none">
  <table id="beSortTable" class="TableList" width="100%" align="center">
   <tr class='TableControl'>
     <td colspan='10'>
       &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
       <input type='button' value='删除' class='SmallButtonW' onClick='deleteAllUser();' title='删除邮件'>
       &nbsp; 转到
       <select id="selectBoxId"  onChange="changeBox(this);">
       <%if(isAdminRole) { %>
         <option value=0>默认</option>
       <% } %>
       </select>
         &nbsp; 复制到       <select id="selectCopyBoxId"  onChange="changeCopyBox(this);">
       <%if(isAdminRole) { %>
         <option value=0>默认</option>
        <% } %>
       </select>
     </td>
   </tr>
  </table>
</div>
<div id="msrg">
</div>
</body>
</html>