<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String treeId = request.getParameter("treeId");
  if (treeId == null){
    treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
  deptParent = YHUtility.encodeSpecial(deptParent);
  if (deptParent == null){
    deptParent = "";
  }
  String deptLocal = request.getParameter("deptLocal");
  deptLocal = YHUtility.encodeSpecial(deptLocal);
  if (deptLocal == null){
    deptLocal = "";
  }
  String parentId = request.getParameter("parentId");
  if (parentId == null){
    parentId = "";
  }
  String textCon = request.getParameter("textCon");
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/dept/js/deptlogic.js"></script>
<script type="text/javascript">
var postPrivs = <%=loginUser.getPostPriv()%>;
var treeId = "<%=treeId%>";
var parentId = "<%=parentId%>";
var deptParent = "<%=deptParent%>";
var deptLocal = "<%=deptLocal%>";
var textCon = "<%=textCon%>";
var deptParentStr = "";
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptAct/getDept.act?treeId=" + treeId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    deptParentStr = rtJson.rtData.deptParent;
    document.getElementById("deptNoOld").value = document.getElementById("deptNo").value ;
    //if(document.getElementById("leader1").value!=""||document.getElementById("leader2").value!=""||document.getElementById("manager").value!=""||document.getElementById("deptParent").value!=""){
    if($("leader1") && $("leader1").value.trim()){
      bindDesc([{cntrlId:"leader1", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if($("leader2") && $("leader2").value.trim()){
      bindDesc([{cntrlId:"leader2", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if($("manager") && $("manager").value.trim()){
      bindDesc([{cntrlId:"manager", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    
//	bindDesc([{cntrlId:"leader1", dsDef:"PERSON,SEQ_ID,USER_NAME"}
//		,{cntrlId:"leader2", dsDef:"PERSON,SEQ_ID,USER_NAME"}
//		,{cntrlId:"manager", dsDef:"PERSON,SEQ_ID,USER_NAME"}
//    	,{cntrlId:"deptParent", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}
//   	]);
    //}
    deptFunc();
  }else{
	alert(rtJson.rtMsrg); 
 }
}

function commitDept(){
  var reg = /^[0-9]*$/;
  //var ddd = @"^(\d)+[-]?(\d){6,12}$";
  var patrn = /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/; 
  var telphone = /^((\(\d{3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}$/;
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
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
    deptNo.focus();
	return false;
  }
  if(document.form1.deptName.value == ""){ 
	alert("部门名称不能为空！");
	deptName.focus();
	return false;
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
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptAct/updateDept.act?treeId=" + treeId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    formDiv.style.display = "none";
    show.style.display = "";
  }else{
	alert(rtJson.rtMsrg);
  }
  parent.deptListTree.location.reload();
}

function nextDept(){
  location.href="/yh/core/funcs/dept/deptinput.jsp?deptLocal=" + deptLocal + "&deptParent=" + deptParent + "&treeId=" + treeId;
}

function deleteDept(){
  if(confirm("确认要删除该部门/成员单位吗？这将同时删除所有下级部门和部门中的用户！")){
    var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptAct/deleteDept.act?treeId=" + treeId;
    var rtJson = getJsonRs(url, mergeQueryString($("form1"))); 
	window.parent.deptinput.location = "<%=contextPath %>/core/funcs/dept/deletedept.jsp";
	parent.deptListTree.location.reload();
    if(rtJson.rtState == '0'){
	  //alert(rtJson.rtMsrg); 
    }else{ 
	  alert(rtJson.rtMsrg); 
    } 
  }else{
     return false;
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

function SelectUser(TO_ID, TO_NAME){
  var treId = treeId;
  URL = "/yh/core/funcs/dept/userselect.jsp?treeId=" + treId + "&TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
  openDialog(URL,'400', '350');
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url, "userDeptId="+treeId+"&deptParent="+encodeURIComponent(deptParent)+"&parentId="+parentId);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptParent");
  if(parentId == "0" || postPrivs == "1"){
    var options = document.createElement("option"); 
    options.value = "0";
    options.innerHTML = "无"; 
    selects.appendChild(options);
  }
  for(var i = 0; i < prcs.length; i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    if (prc.value == "organizationNodeId") {
      option.value = "0"; 
    } else {
      option.value = prc.value; 
    }
    option.innerHTML = prc.text; 
    if(option.value == deptParentStr){
      option.selected = true;
    }
    selects.appendChild(option);
  }
  return userId;
}
</script>
</head>
<body class="" topmargin="5" onload="doInit()">
<!--  
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
   <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp; 部门/成员单位管理</span>
    </td>
   </tr>
 </table>
 -->
 <div id="formDiv">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" width="20" HEIGHT="22"><span class="big3">&nbsp;编辑部门/成员单位&nbsp;－&nbsp;[<%=deptLocal%>]</span>
    </td>
  </tr>
 </table>
 <br/>
 <form action=""  method="post" name="form1" id="form1">
 <table class="TableBlock" width=500" align="center">
 <input type="hidden" name="deptNoOld" id="deptNoOld" value="">
 <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.dept.data.YHDepartment"/>
   <tr>
    <td nowrap class="TableData">部门排序号：</td>
    <td nowrap class="TableData">
        <input type="text" name="deptNo" id="deptNo" class="BigInput" size="10" maxlength="200" value="">&nbsp;用于处于同一层次部门的排序，以及用户列表的排序
        <font style='color:red'>*</font>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">部门名称：</td>
    <td nowrap class="TableData">
        <input type="text" name="deptName" id="deptName" class="BigInput" size="25" maxlength="25" value="">&nbsp;
        <font style='color:red'>*</font>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">电话：</td>
    <td nowrap class="TableData">
        <input type="text" name="telNo" id="telNo" class="BigInput" size="25" maxlength="25" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">传真：</td>
    <td nowrap class="TableData">
        <input type="text" name="faxNo" id="faxNo" class="BigInput" size="25" maxlength="25" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">上级部门：</td>
    <td nowrap class="TableData">
    <select id="deptParent" name="deptParent" style="height:22px;FONT-SIZE: 12pt;">
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">部门主管(选填)：</td>
    <td nowrap class="TableData">
        <input type="hidden" name="manager" id="manager" value="">
        <textarea cols="40" name="managerDesc" id="managerDesc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" value="" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['manager', 'managerDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('manager', 'managerDesc')">清空</a>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">上级主管领导(选填)：</td>
    <td nowrap class="TableData">
        <input type="hidden" name="leader1" id="leader1"  value="">
        <textarea cols="40" name="leader1Desc" id="leader1Desc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" value="" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['leader1', 'leader1Desc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('leader1', 'leader1Desc')">清空</a>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">上级分管领导(选填)：</td>
    <td nowrap class="TableData">
        <input type="hidden" name="leader2" id="leader2" value="">
        <textarea cols="40" name="leader2Desc" id="leader2Desc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" value="" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['leader2', 'leader2Desc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('leader2', 'leader2Desc')">清空</a>&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">部门职能：</td>
    <td nowrap class="TableData">
        <textarea name="deptFunc" id="deptFunc" class="SmallInput" cols="50" rows="5" value=""></textarea>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="DEPT_ID" value="1">
        <input type="button" value="保存修改" class="BigButton" title="保存修改" name="button" onclick="commitDept();">
    <input type="button" value="删除当前部门/成员单位" class="BigButtonD" title="删除当前部门/成员单位" onclick="deleteDept();">
    </td>
   </table>
  </form>
 <br>
 <table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
  <tr>
   <td background="/images/dian1.gif" width="100%"></td>
  </tr>
 </table>
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp; 当前部门/成员单位 － 相关操作</span>
    </td>
  </tr>
 </table>
 <br>
 <div align="center">
   <input type="button" value="新建下级部门/成员单位" class="BigButtonD" title="新建下级部门/成员单位" onclick="nextDept();"><br><br>
 </div>
 </div>
 <div align="center" style="display:none" id="show">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">部门/成员单位信息已保存修改</div>
    </td>
  </tr>
 </table>
 </div>
</body>
</html>