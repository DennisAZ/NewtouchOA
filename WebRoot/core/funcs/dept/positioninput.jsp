<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if (treeId == null) {
    treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
  if (deptParent == null) {
    deptParent = "";
  }
  String deptLocal = request.getParameter("deptLocal");
  if (deptLocal == null){
    deptLocal = "";
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
var deptLocal = "<%=deptLocal%>";
var deptParent = "<%=deptParent%>";

function doInit() {
  document.getElementById("deptSeqId").value = treeId;
  //alert(document.getElementById("deptSeqId").value);
  document.getElementById("positionNoOld").value = document.getElementById("positionNo").value ;
}
	
function doSubmit(event) {
  var reg = /^[0-9]*$/;
  var sortNo = document.form1.sortNo;
  var positionNo = document.form1.positionNo;
  var positionName = document.form1.positionName;
  if(!reg.test(positionNo.value)){
    alert("岗位编码只能输入数字！");
    positionNo.focus();
	return false;
  }
  if(positionNo.value == ""){ 
    alert("岗位编码不能为空！");
	return (false);
  }
  if(!reg.test(sortNo.value)){
    alert("拍序号只能输入数字！");
    sortNo.focus();
	return false;
  }
  if(sortNo.value == ""){ 
    alert("拍序号不能为空！");
	return (false);
  }
  if(positionName.value == ""){ 
    alert("岗位名称不能为空！");
	return (false);
  }
  event = event || window.event;
  var rtJson = getJsonRs(contextPath + "/yh/core/funcs/dept/act/YHDeptPositionAct/insertDp.act", mergeQueryString($("form1")));
  //alert(rsText);
  if (rtJson.rtState == 0) {
	bindJson2Cntrl(rtJson.rtData);
	$("form1").reset();
	alert(rtJson.rtMsrg);
  }else {
	alert(rtJson.rtMsrg);
  }
  window.parent.positionList.location.reload();
}

function onSelectTable(){ 
  var curr = document.getElementById('pre4').value; 
  //selectCode ({sort:1,tableNo:'',codeField:'',nameField:'',filterField:'',filterValue:'',currValue:'',orderBy:''}); 
  selectCode ({sort:'2',tableNo:'11114',codeField:'分类',nameField:'flagSortDesc',codeFieldNo:'编号',nameFieldNo:'seqId',filterField:'flagCode',filterValue:'',currValue:curr,orderBy:''}); 
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
  URL = "/yh/core/funcs/dept/userselect.jsp?treeId=" + treId + "&TO_ID=" + TO_ID + "&TO_NAME=" + TO_NAME;
  openDialog(URL,'400', '350');
}

</script>
</head>
<body class="" topmargin="5" onload="doInit()">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 部门/成员单位管理</span>
    </td>
    <td align="right">
      <input type="button" value="新建岗位" class="BigButton" onClick="location='/yh/core/funcs/dept/positioninput.jsp?treeId='+treeId;" title="新建岗位">&nbsp;&nbsp;
      <input type="button" value="导入" class="BigButton" onClick="parent.dept_main.location='import.php';" title="导入岗位/成员单位">&nbsp;&nbsp;
      <input type="button" value="导出" class="BigButton" onClick="parent.dept_main.location='export.php';" title="导出岗位/成员单位">&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
    </tr>
 </table>
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" height="22" WIDTH="22"><span class="big3"> 新建岗位 </span>
    </td>
  </tr>
 </table>
 <form action="" method="post" name="form1" id="form1" onSubmit='return check()'>
  <table class="TableBlock" width="450" align="center">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.dept.data.YHDeptPosition"/>
  <input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" name="deptSeqId" id="deptSeqId" value="">
  <input type="hidden" name="positionNoOld" id="positionNoOld" value="">
    <td nowrap class="TableData">岗位编码：</td>
    <td nowrap class="TableData">
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
       <textarea cols="45" name="positionFlag" id="pre4" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
       <a href="javascript:;" class="orgAdd" onClick="onSelectTable()">添加</a>
       <a href="javascript:;" class="orgClear" onClick="ClearUser('positionFlag', 'positionFlagDesc')">清空</a>
    </td>
   </tr>
    <td nowrap class="TableControl" colspan="2" align="center">
       <input type="button" value="新 建" class="BigButton" title="新建" name="button" onclick="doSubmit();">
    </td> 
  </table>
 </form>
 <br>
 <div class="small" align=center style="font-color:white">
 <a href="javascript:why_view()">为什么无法新建岗位？</a>
 <div>
 <br>
 <div id="why_dept" class="small1" style="display:none">
     您可能使用了遨游浏览器，遨游浏览器有一个广告猎手功能，其网址屏蔽规则设置的过于严格，某些字母组合开头的网址（如ad开头的页面网址）会被屏蔽，建议合理进行相关设置，或关闭其广告猎手功能，或使用IE浏览器。
 <div>
</body>
</html>