<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  String treeId = request.getParameter("treeId");
  if (treeId == null) {
    treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
  if (deptParent == null) {
    deptParent = "";
  }
  String TO_ID = request.getParameter("TO_ID");
  if (TO_ID == null) {
    TO_ID = "";
  }
  String deptLocal = request.getParameter("deptLocal");
  if (deptLocal == null){
    deptLocal = "";
  }
  String TO_NAME = request.getParameter("TO_NAME");
  if (TO_NAME == null) {
    TO_NAME = "";
  }
  String deptParentDesc = request.getParameter("deptParentDesc");
  if (deptParentDesc == null) {
    deptParentDesc = "";
  }
  String parentdesc = (String)request.getAttribute("desc");
  if (parentdesc == null) {
    parentdesc = "";
  }
  
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var postPrivs = <%=loginUser.getPostPriv()%>;
var TO_ID = "<%=TO_ID%>";
var TO_NAME = "<%=TO_NAME%>";
var treeId = "<%=treeId%>";
var deptLocal = "<%=deptLocal%>";
var deptParent = "<%=deptParent%>";
var deptParentDescValue = "<%=deptParentDesc%>";
var parentdesc = "<%=parentdesc%>";

function doInit() {
  //document.getElementById("deptParentDesc").value = deptLocal;
  document.getElementById("deptParent").value = treeId;
  deptFunc();
//var mgr = new SelectMgr();
//mgr.addSelect({cntrlId: "deptParent", tableName: "oa_department", codeField: "SEQ_ID", nameField: "DEPT_NAME", value: <%=deptParent%>, isMustFill: "1", filterField: "", filterValue: "", order: "", reloadBy: "", actionUrl: ""});
//mgr.loadData();
//mgr.bindData2Cntrl();
}

function checkStr(str){ 
  var re=/[\\"']/; 
  return str.match(re); 
}

function checkStdr(str){ 
 var re=/["\/\\:*?"<>|]/; 
 return str.match(re); 
}
	
function doSubmit(event) {
  var reg = /^[0-9]*$/;
  var telphone = /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/;
  var ds = /((\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$)/;
  var patrn = /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/; 
  var re=new RegExp("^(\d{3,4})\-{0,1}(\d{7,8})$");
  var deptNo = document.form1.deptNo;
  var deptName = document.form1.deptName;
  var telNo = document.form1.telNo;
  if(!reg.test(deptNo.value)){
    alert("部门排序号只能输入数字！");
	deptNo.focus();
	deptNo.select();
	return false;
  }
  if(document.form1.deptNo.value == ""){ 
    alert("部门排序号不能为空！");
	return (false);
  }
  if(document.form1.deptName.value == ""){ 
    alert("部门名称不能为空！");
	return (false);
  }
  if(checkStr($("deptName").value)){
    alert("您输入的部门名称含有'双引号'、'单引号 '或者 '\\' 请从新填写");
    $('deptName').focus();
    $('deptName').select();
    return false;
  }
  if(telNo.value){
    if(!patrn.exec(telNo.value)){
      alert("您输入的电话号码格式不正确，请重新填写！");
      telNo.focus();
      telNo.select();
	  return false;
    }
  }
  if($('faxNo').value){
    if(!patrn.exec($('faxNo').value)){
      alert("您输入的传真格式不正确，请重新填写！");
      $('faxNo').focus();
      $('faxNo').select();
	  return false;
    }
  }
  event = event || window.event;
  var rtJson = getJsonRs(contextPath + "/yh/core/funcs/dept/act/YHDeptAct/insertDept.act?treeId="+treeId+"&deptParentDesc="+deptLocal, mergeQueryString($("form1")));
  if (rtJson.rtState == 0) {
	//bindJson2Cntrl(rtJson.rtData);
	alert("新建成功！");
	if(document.getElementById("deptParent").value != "0"){
      document.getElementById("deptNo").value = "";
	  document.getElementById("deptName").value = "";
      document.getElementById("telNo").value = "";
	  document.getElementById("faxNo").value = "";
	  //document.getElementById("deptParentDesc").value = "";
	  //document.getElementById("deptParent").value = "";
      document.getElementById("manager").value = "";
	  document.getElementById("managerDesc").value = "";
      document.getElementById("leader1").value = "";
      document.getElementById("leader1Desc").value = "";
      document.getElementById("leader2").value = "";
      document.getElementById("leader2Desc").value = "";
      document.getElementById("deptFunc").value = "";
    }else{
      $("form1").reset();
    }
  }else {
    alert(rtJson.rtMsrg);
  }
  parent.deptListTree.location.reload();
}

function ClearUser(TO_ID, TO_NAME){
  if(TO_ID == "" || TO_ID == "undefined" || TO_ID == null){
	TO_ID = "TO_ID";
	TO_NAME = "TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value = "";
  document.getElementsByName(TO_NAME)[0].value = "";
}

function why_view(){
  var why_dept = document.getElementById("why_dept");
  if(why_dept.style.display == ''){
     why_dept.style.display = 'none';
  }else{
  	 why_dept.style.display = '';
  }
}

function SelectUser(TO_ID, TO_NAME){
  var treId = treeId;
  URL="/yh/core/funcs/dept/userselect.jsp?treeId="+treId+"&TO_ID="+TO_ID+"&TO_NAME="+TO_NAME;
  openDialog(URL,'400', '350');
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptParent");
  if(postPrivs == "1"){
    var options = document.createElement("option"); 
    options.value = "0";
    options.innerHTML = "无"; 
    selects.appendChild(options);
  }
  for(var i = 0; i < prcs.length; i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    if(prc.value == treeId){
      option.selected = true;
    }
    selects.appendChild(option);
  }
  return userId;
}
</script>
</head>
<body class="" topmargin="5" onload="doInit()">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" height="22" WIDTH="22"><span class="big3">&nbsp;新建部门/成员单位</span>
    </td>
  </tr>
 </table>
 </br>
 <form action="" method="post" name="form1" id="form1" onSubmit='return check()'>
  <table class="TableBlock" width="450" align="center">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.dept.data.YHDepartment"/>
  <input type="hidden" name="seqId" id="seqId" value="">
   <tr>
    <td nowrap class="TableData">部门排序号：</td>
    <td nowrap class="TableData">
      <input type="text" name="deptNo" id="deptNo" class="BigInput" size="10" maxlength="200">&nbsp;用于处于同一层次部门的排序，以及用户列表的排序

      <font style='color:red'>*</font>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">部门名称：</td>
    <td nowrap class="TableData">
      <input type="text" name="deptName" id="deptName" class="BigInput" size="25" maxlength="25">&nbsp;
      <font style='color:red'>*</font>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">电话：</td>
    <td nowrap class="TableData">
      <input type="text" name="telNo" id="telNo" class="BigInput" size="25" maxlength="25">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">传真：</td>
    <td nowrap class="TableData">
      <input type="text" name="faxNo" id="faxNo" class="BigInput" size="25" maxlength="25">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">上级部门：</td>
      <td class="TableData">
      <select id="deptParent" name="deptParent" style="height:22px;FONT-SIZE: 12pt;">
        </select>
      </td>
   </tr>
   <tr>
    <td nowrap class="TableData">部门主管(选填)：  </td>
    <td nowrap class="TableData">
       <input type="hidden" name="manager" id="manager" value="">
       <textarea cols="40" name="managerDesc" id="managerDesc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser(['manager', 'managerDesc']);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser('manager', 'managerDesc')">清空</a>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">上级主管领导(选填)：</td>
    <td nowrap class="TableData">
       <input type="hidden" name="leader1" id="leader1" value="">
       <textarea cols="40" name="leader1Desc" id="leader1Desc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser(['leader1', 'leader1Desc']);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser('leader1', 'leader1Desc')">清空</a>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">上级分管领导(选填)：</td>
    <td nowrap class="TableData">
       <input type="hidden" name="leader2" id="leader2" value="">
       <textarea cols="40" name="leader2Desc" id="leader2Desc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser(['leader2', 'leader2Desc']);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser('leader2', 'leader2Desc')">清空</a>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">部门职能：</td>
    <td nowrap class="TableData">
       <textarea name="deptFunc" id="deptFunc" class="SmallInput" cols="50" rows="5"></textarea>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableControl" colspan="2" align="center">
       <input type="button" value="新 建" class="BigButton" title="新建" name="button" onclick="doSubmit();">
    </td> 
  </table>
 </form>
 <br>
 <div class="small" align=center style="font-color:white">
 <a href="javascript:why_view()">为什么无法新建部门？</a>
 <div>
 <br>
 <div id="why_dept" class="small1" style="display:none">
     您可能使用了遨游浏览器，遨游浏览器有一个广告猎手功能，其网址屏蔽规则设置的过于严格，某些字母组合开头的网址（如ad开头的页面网址）会被屏蔽，建议合理进行相关设置，或关闭其广告猎手功能，或使用IE浏览器。

 <div>
</body>
</html>