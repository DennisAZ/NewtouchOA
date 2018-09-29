<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHRegistProps" %>
<%@ page import="yh.core.data.YHAuthKeys" %>
<%
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>动态密码卡管理</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/grid.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit() {
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHSecureCardAct/getSecureCard.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render: checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"keySn",  width: '15%', text:"卡号" ,align: 'center'},
         {type:"hidden", name:"userId", text:"顺序号", dataType:"int"},
         {type:"data", name:"userName",  width: '10%', text:"用户" ,align: 'center'},
         {type:"data", name:"deptName",  width: '10%', text:"部门" ,align: 'center' },
         {type:"data", name:"keyInfo",  width: '20%', text:"数据" ,align: 'center', render:cutString},
         {type:"selfdef", text:"操作", width: '15%',render:opts}]
    };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('暂无动态密码卡数据', 'msrg');
  }
}  

function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"290\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + seqId + "\" onclick=\"checkSelf()\" ></center>";
}

function cutString(cellData, recordIndex, columIndex){
	var cutString = this.getCellData(recordIndex,"keyInfo");
	if(cutString.length > 20){
		return cutString.substring(0,19)+"...";
	}
	else{
		return cutString;
	}
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
  var keySn = this.getCellData(recordIndex,"keySn");
  var userName = this.getCellData(recordIndex,"userName");
  var returnStr = "<center>"
	if(userName == ""){
		returnStr += "<a href=javascript:bind(" + recordIndex + "," + keySn + ")>绑定用户</a>&nbsp;"
	}
  returnStr += "<a href=javascript:passwordsynInput(" + recordIndex + ")>同步</a>&nbsp;"
             + "</center>";
  
  return returnStr;
}

function bind(recordIndex,keySn){
  if($("tr_"+recordIndex+"_input")){
    $("tr_"+recordIndex+"_input").remove();
  }
  if($("tr_"+recordIndex+"_msg")){
    $("tr_"+recordIndex+"_msg").remove();
    return;
  }
  var temp = "<input type=\"hidden\" name=\"userName_bind"+recordIndex+"\" id=\"userName_bind"+recordIndex+"\" value=\"\">"
	          +"<input type=\"text\" name=\"userNameDesc_bind"+recordIndex+"\" id=\"userNameDesc_bind"+recordIndex+"\" class=\"BigStatic\" readonly size=\"15\">"
	          +"<a href=\"javascript:;\" class=\"orgAdd\" onClick=\"selectSingleUser(['userName_bind"+recordIndex+"', 'userNameDesc_bind"+recordIndex+"']);\">添加</a>&nbsp;&nbsp;"
	          +"<input type=\"button\" value=\"确定\" onclick=\"bindUser("+recordIndex+","+keySn+")\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  var tr = new Element('tr',{"id":"tr_"+recordIndex+"_msg","width":"100%"}).update("<td colspan='6' align='right'>请选择要绑定的用户："+temp+"</td>");
  $('tr_'+recordIndex).insert({after: tr});
}

function bindUser(recordIndex,keySn){
	if($("userName_bind"+recordIndex).value == ""){
		alert("绑定人员不能为空！");
	}
	var url = "<%=contextPath%>/yh/core/funcs/person/act/YHSecureCardAct/bindUser.act?userId="+$("userName_bind"+recordIndex).value+"&keySn="+keySn;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		window.location.reload();
	}
}

function importSecureCard(){
  window.location = "<%=contextPath%>/core/funcs/person/importSecureCard.jsp";
}

function importBindCard(){
	  window.location = "<%=contextPath%>/core/funcs/person/importBindCard.jsp";
	}

function passwordsynInput(recordIndex){
	  if($("tr_"+recordIndex+"_msg")){
	    $("tr_"+recordIndex+"_msg").remove();
	  }
	  if($("tr_"+recordIndex+"_input")){
	    $("tr_"+recordIndex+"_input").remove();
	    return;
	  }
	  var temp = "<input type=\"text\" name=\"password_"+recordIndex+"\" id=\"password_"+recordIndex+"\" size=\"10\">&nbsp;&nbsp;"
	            +"<input type=\"button\" value=\"确定\" onclick=\"passwordsyn("+recordIndex+")\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	  var tr = new Element('tr',{"id":"tr_"+recordIndex+"_input","width":"100%"}).update("<td colspan='6' align='right'>请输入动态密码："+temp+"</td>");
	  $('tr_'+recordIndex).insert({after: tr});
	}

function passwordsyn(recordIndex){
  if($("password_"+recordIndex).value.length != 6 || isNaN($("password_"+recordIndex).value)){
    alert("动态密码应为6位数字！");
    return;
  }
  var seqId = pageMgr.getCellData(recordIndex,"seqId");
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHSecureCardAct/passwordsyn.act?password="+$("password_"+recordIndex).value+"&seqId="+seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
	  alert(rtJson.rtData)
    window.location.reload();
  }
}

/**
 * 删除多个文件
 * @return
 */
function deleteAll(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除卡信息，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要删除已选中的卡信息吗？")) {
    return ;
  } 
  var requestURLStr = contextPath + "/yh/core/funcs/person/act/YHSecureCardAct";
  var url = requestURLStr + "/deleteSecureCard.act";
  var rtJson = getJsonRs(url, "seqIds="+idStrs );
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
   alert(rtJson.rtMsrg); 
  }
}

function unBind(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要解除OA用户和卡的绑定，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要解除OA用户和卡的绑定吗？")) {
    return ;
  } 
  var requestURLStr = contextPath + "/yh/core/funcs/person/act/YHSecureCardAct";
  var url = requestURLStr + "/unBindSecureCard.act";
  var rtJson = getJsonRs(url, "seqIds="+idStrs );
  if (rtJson.rtState == "0") {
    window.location.reload();
  }else {
   alert(rtJson.rtMsrg); 
  }
}

/**
 * 全选

 * @param field
 * @return
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
</script>
</head>
<body onload="doInit()">
<table cellscpacing="1" cellpadding="3" width="100%">
  <tr>
    <td class="Big">
      <img src="<%=imgPath%>/notify_open.gif"></img><span class="big3">&nbsp;动态密码卡管理 </span>&nbsp;&nbsp;
      <input type='button' value='批量绑定' class="BigButton" onClick='importBindCard();' title='批量绑定'>&nbsp;&nbsp;
      <input type='button' value='导入卡信息' class="BigButton" onClick='importSecureCard()' title='导入卡信息'>
    </td>
  </tr>
</table>
<div id="listContainer" style="display:none;">
</div>
<div id="delOpt" style="display:none">
<table id="beSortTable" class="TableList" width="100%">
   <tr class='TableControl'>
     <td colspan='10'>
       &nbsp;<input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this)"><label for='checkAlls'>全选</label> &nbsp;
       <input type='button' value='删除' class='SmallButtonW' onClick='deleteAll();' title='删除'>&nbsp;&nbsp;
       <input type='button' value='解除绑定' class='SmallButtonW2' onClick='unBind();' title='解除绑定'>
     </td>
   </tr>
</table>
</div>

<div id="msrg">
</div>
</body>
</html>