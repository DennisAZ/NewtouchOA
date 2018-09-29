<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  String num = request.getParameter("num");
  if (num == null){
    num = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/views.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/grid.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var num = "<%=num%>";

function doInit(){
  if(num){
    var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/getUserPriv.act?seqId=" + seqId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      bindJson2Cntrl(rtJson.rtData);
      //alert(document.getElementById("funcIdStr").value);
      //document.getElementById("privNoOld").value = document.getElementById("privNo").value ;
      document.getElementById("privNo").value = "";
      document.getElementById("privName").value = "";
    }else{
  	  alert(rtJson.rtMsrg); 
    }
  }
  else if(seqId != null&&seqId != ""){
    //bindDesc([{cntrlId:"positionFlag", dsDef:"SPECIAL_FLAG,SEQ_ID,FLAG_DESC"}]);
    var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/getUserPriv.act?seqId=" + seqId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      bindJson2Cntrl(rtJson.rtData);
      //alert(document.getElementById("funcIdStr").value);
      document.getElementById("privNoOld").value = document.getElementById("privNo").value ;
    }else{
	  alert(rtJson.rtMsrg); 
    }
  }
  //document.getElementById("deptSeqId").value = treeId;
}

function commitDept(){
  var reg = /^[0-9]*$/;
  var formDiv = document.getElementById("formDiv");
  var show = document.getElementById("show");
  var privNo = document.form1.privNo;
  var privName = document.form1.privName;
  if(!reg.test(privNo.value)){
	alert("角色排序号只能输入数字！");
	privNo.focus();
	return false;
  }
  if(document.form1.privNo.value == ""){ 
    alert("角色排序号不能为空！");
	return (false);
  }
  if(privName.value == ""){ 
    alert("角色名称不能为空！");
	return (false);
  }
  if(num){
    var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/insertUserPriv.act";
  }
  else if(seqId != null&&seqId != ""){
  //document.getElementById("positionFlag") = document.getElementById("pre4");
    var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/updateUserPriv.act?seqId=" + seqId;
  }else{
    var url = "<%=contextPath%>/yh/core/funcs/person/act/YHUserPrivAct/insertUserPriv.act";
  }
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    location.href ='<%=contextPath %>/core/funcs/userpriv/manage.jsp';
    //alert(rtJson.rtMsrg);
  } else {
	alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doInit()">
 <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 新建角色</span>
    </td>
  </tr>
 </table>
 <table class="MessageBox" align="center" width="500">
  <tr>
    <td class="msg info">

      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">请合理设置角色序号，它决定了用户的排列位置，也代表了一种管理层次，序号越小，管理层次越高</div>
    </td>
  </tr>
 </table>
 <form action=""  method="post" name="form1" id="form1">
  <table class="TableBlock" width="450" align="center">
    <input type="hidden" name="seqId" id="seqId" value="">
    <input type="hidden" name="privNoOld" id="privNoOld" value="">
    <input type="hidden" name="funcIdStr" id="funcIdStr" value="">
    <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.person.data.YHUserPriv"/>
    <tr>
      <td nowrap class="TableData">角色排序号：</td>
      <td nowrap class="TableData">
        <input type="text" name="privNo" id="privNo" class="BigInput" size="5" maxlength="10" value="">&nbsp;<br>
      </td>
   </tr>
   <tr>
     <td nowrap class="TableData">角色名称：</td>
     <td nowrap class="TableData">
       <input type="text" name="privName" id="privName" class="BigInput" size="25" maxlength="100" value="">&nbsp;
     </td>
   </tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
       <input type="button" value="确定" class="BigButton" name="button" onclick="commitDept();">&nbsp;&nbsp;
       <input type="button" value="返回" class="BigButton" onclick="location='manage.jsp'">
    </td>
  </table>
 </form>
</body>
</html>