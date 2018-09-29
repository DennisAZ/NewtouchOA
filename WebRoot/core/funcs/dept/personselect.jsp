<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if(treeId == null){
    treeId = "";
  }
  String userId = request.getParameter("userId");
  if(userId == null){
    userId = "";
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
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var userId = "<%=userId%>";
var treeId = "<%=treeId%>";
var positionName = "<%=positionName%>";
function doInit(){
  document.getElementById("positionSeqId").value = treeId ;
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/getDeptSelect.act?treeId=" + treeId;
  var rtJson = getJsonRs(url, null);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    if(document.getElementById("positionUsers").value!=""){
      bindDesc([{cntrlId:"positionUsers", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }else{
	alert(rtJson.rtMsrg); 
 }
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

function commitDept(){
  //var seqId = document.getElementById("seqId").value);
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
  var seqId = document.getElementById("seqId").value;
  var positionSeqId = document.getElementById("positionSeqId").value;
  if(seqId == ""){
    seqId = 0;
  }
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/updatePersonDept.act?personSeqId=" + positionSeqId + "&seqId=" + seqId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    formDiv.style.display = "none";
    show.style.display = "";
    //alert(rtJson.rtMsrg);
  }else{
	alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit()">
 <div id="deplist"></div>
 <div id="formDiv">
 <form name="form1" id="form1">
 <input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" name="positionSeqId" id="positionSeqId" value="">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.dept.data.YHPositionPerson"/>
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" width="20" HEIGHT="22"><span class="big3"> 编辑人员- 当前岗位:[<%=positionName%>]</span>
    </td>
  </tr>
 </table>
 <br>
 <tr>
    <td nowrap class="TableData">岗位人员：</td>
    <td nowrap class="TableData">
        <input type="hidden" name="positionUsers" id="positionUsers"  value="">
        <textarea cols="45" name="positionUsersDesc" id="positionUsersDesc" rows="1" style="overflow-y:auto;" class="SmallStatic" wrap="yes" value="" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="SelectUser('positionUsers', 'positionUsersDesc')">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('positionUsers', 'positionUsersDesc')">清空</a>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button" value="保存修改" class="BigButton" title="保存修改" name="button" onclick="commitDept();">
    </td>
    </tr>
 </form>
 </div>
 <div align="center" style="display:none" id="show">
 <table class="MessageBox" align="center" width="410">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">岗位人员信息已保存修改</div>
    </td>
  </tr>
 </table>
 </div>
</body>
</html>