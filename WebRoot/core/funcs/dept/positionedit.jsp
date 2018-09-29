<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if (treeId == null){
    treeId = "";
  }
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  String deptParent = request.getParameter("deptParent");
  if (deptParent == null){
    deptParent = "";
  }
  String positionName = request.getParameter("positionName");
  if (positionName == null){
    positionName = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/views.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/grid.js" ></script>
<script type="text/javascript">
var treeId = "<%=treeId%>";
var seqId = "<%=seqId%>";
var deptParent = "<%=deptParent%>";
var positionName = "<%=positionName%>";

function doInit(){
  //bindDesc([{cntrlId:"positionFlag", dsDef:"SPECIAL_FLAG,SEQ_ID,FLAG_DESC"}]);
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/getDept.act?treeId=" + seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    document.getElementById("positionNoOld").value = document.getElementById("positionNo").value ;
  } else {
	alert(rtJson.rtMsrg); 
  }
  document.getElementById("deptSeqId").value = treeId;
}

function onSelectTable(){ 
  var curr = document.getElementById('pre4').value; 
  //selectCode ({sort:1,tableNo:'',codeField:'',nameField:'',filterField:'',filterValue:'',currValue:'',orderBy:''}); 
  selectCode ({sort:'2',tableNo:'11114',codeField:'分类',nameField:'flagSortDesc',codeFieldNo:'编号',nameFieldNo:'seqId',filterField:'flagCode',filterValue:'',currValue:curr,orderBy:''}); 
}

function commitDept(){
  var reg = /^[0-9]*$/;
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
  var positionNo = document.form1.positionNo;
  var positionName = document.form1.positionName;
  var sortNo = document.form1.sortNo;
  if(!reg.test(positionNo.value)) {
	alert("岗位编码只能输入数字！");
	deptNo.focus();
	return false;
  }
  if(document.form1.positionNo.value == "") { 
    alert("岗位编码不能为空！");
	return (false);
  }
  if(document.form1.sortNo.value == "") { 
	alert("拍序号不能为空！");
	return (false);
  }
  if(positionName.value == ""){ 
    alert("岗位名称不能为空！");
	return (false);
  }
  //document.getElementById("positionFlag") = document.getElementById("pre4");
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/updateDp.act?treeId=" + treeId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0") {
    formDiv.style.display = "none";
    show.style.display = "";
    //alert(rtJson.rtMsrg);
  } else {
	alert(rtJson.rtMsrg);
  }
  
}

function nextDept(){
  location.href="/yh/core/funcs/dept/deptinput.jsp?deptLocal=" + deptLocal + "&deptParent=" + deptParent;
}

function deletePosition(){
  if(confirm("确认要删除该岗位吗？这将同时删除该岗位的权限和人员！")) {
    var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/deletePosition.act?seqId=" + seqId;
    var rtJson = getJsonRs(url, mergeQueryString($("form1"))); 
	$("form1").reset();
    if(rtJson.rtState == '0') {
	  alert(rtJson.rtMsrg); 
    } else { 
	  alert(rtJson.rtMsrg); 
    } 
  } else {
     return false;
  }
  window.parent.positionList.location.reload();
}

function ClearUser(TO_ID, TO_NAME){
  if(TO_ID == "" || TO_ID == "undefined" || TO_ID == null) {
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
</script>
</head>
<body class="" topmargin="5" onload="doInit()">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
   <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 岗位</span>
    </td>
    <td align="right">
      <input type="button" value="新建岗位" class="BigButton" onClick="location='/yh/core/funcs/dept/positioninput.jsp?treeId='+treeId;" title=""新建岗位"">&nbsp;&nbsp;
      <input type="button" value="导入" class="BigButton" onClick="parent.dept_main.location='import.php';" title="导入岗位/成员单位">&nbsp;&nbsp;
      <input type="button" value="导出" class="BigButton" onClick="parent.dept_main.location='export.php';" title="导出岗位/成员单位">&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
   </tr>
 </table>
 <div id="formDiv">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" width="20" HEIGHT="22"><span class="big3"> 编辑岗位- 当前岗位:[<%=positionName%>]</span>
    </td>
  </tr>
 </table>
 <form action=""  method="post" name="form1" id="form1">
 <table class="TableBlock" width=500" align="center">
 <input type="hidden" name="seqId" id="seqId" value="">
 <input type="hidden" name="positionNoOld" id="positionNoOld" value="">
 <input type="hidden" name="deptSeqId" id="deptSeqId" value="">
 <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.dept.data.YHDeptPosition"/>
   <tr>
    <td nowrap class="TableData">岗位编码：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="positionNameDesc" id="positionNameDesc" class="BigInput" size="25" maxlength="25" value="">
      <input type="text" name="positionNo" id="positionNo" class="BigInput" size="25" maxlength="25">&nbsp;
      <font style='color:red'>*</font>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">排序号：</td>
    <td nowrap class="TableData">
      <input type="text" name="sortNo" id="sortNo" class="BigInput" size="25" maxlength="25">&nbsp;
      <font style='color:red'>*</font>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">岗位名称：</td>
      <td class="TableData">
        <input type="text" name="positionName" id="positionName" class="BigInput" size="25" maxlength="25" value="">&nbsp;
        <font style='color:red'>*</font>
        <input type="hidden" name="positionNameDesc" id="positionNameDesc" class="BigInput" size="25" maxlength="25" value="">
      </td>
   </tr>
    <td nowrap class="TableData">岗位详细描述：</td>
    <td nowrap class="TableData">
       <textarea name="positionDesc" id="positionDesc" class="SmallInput" cols="60" rows="3"></textarea>
    </td>
   </tr>
   <tr>
   <tr>
    <td nowrap class="TableData">标记：</td>
    <td nowrap class="TableData">
       <input type="hidden" name="positionFlagDesc" id="positionFlagDesc" value="">
       <textarea cols="45" name="positionFlag" id="pre4" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="onSelectTable()">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser('positionFlag', 'positionFlagDesc')">清空</a>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="DEPT_ID" value="1">
        <input type="button" value="保存修改" class="BigButton" title="保存修改" name="button" onclick="commitDept();">
    <input type="button" value="删除当前岗位" class="BigButtonC" title="删除当前岗位" onclick="deletePosition();">
    </td>
   </table>
  </form>
 <br>
 <table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
  <tr>
   <td background="/images/dian1.gif" width="100%"></td>
  </tr>
 </table>
 <br>
 </div>
 <div align="center" style="display:none" id="show">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">岗位信息已保存修改</div>
    </td>
  </tr>
 </table>
 </div>
</body>
</html>