<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String sqlId = request.getParameter("sqlId");
  String codeClassId = request.getParameter("codeClassId");
  if (sqlId == null) {
	sqlId = "";
  }
  if (codeClassId == null) {
    codeClassId = "";
    }
  String classNo = request.getParameter("classNo");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加或修改代码</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var sqlId = "<%=sqlId%>";
var classNo = "<%=classNo%>";
//alert('<%=codeClassId%>');
function doInit() {
  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "classNo", tableName: "CODE_CLASS", codeField: "CLASS_NO", nameField: "CLASS_DESC", value: classNo, isMustFill: "1", filterField: "", filterValue: "", order: "", reloadBy: "", actionUrl: ""});
  mgr.loadData();
  mgr.bindData2Cntrl();
  document.getElementById("classCode").focus();

  if (sqlId) {
    var url = "<%=contextPath%>/yh/core/codeclass/act/YHCodeClassAct/getCodeItem.act";
    var rtJson = getJsonRs(url, "sqlId=" + sqlId);
    //alert(rsText);
    if (rtJson.rtState == "0") { 
      
      document.getElementById("sqlId").value = rtJson.rtData.sqlId;
      document.getElementById("classCodeOld").value = rtJson.rtData.classCode;
      document.getElementById("classNo").value = rtJson.rtData.classNo;
      document.getElementById("classCode").value = rtJson.rtData.classCode;   
      
      document.getElementById("sortNo").value = rtJson.rtData.sortNo;

      document.getElementById("classDesc").value = rtJson.rtData.classDesc;
    }else {
      alert(rtJson.rtMsrg); 
    }
  }
}

function check() {
  var cntrl = document.getElementById("classCode")
  if(!cntrl.value) {
	alert("代码编号不能为空！");
	cntrl.focus();
	return false;
  }


  cntrl = document.getElementById("sortNo");
  if (!cntrl.value) {   
  	alert("排序号不能为空！");
  	cntrl.focus();
  	return false;
  }
  
  if(!isNumber(cntrl.value)){
	alert("必须填入数字！");
	cntrl.focus();
  	return false;
  }
 

  cntrl = document.getElementById("classDesc");
  if(!cntrl.value) {
  	alert("代码描述不能为空！");
  	cntrl.focus();
  	return false;
  }
  return true;
}

function commitItem() {
  if(!check()){
    return;
  }

  classNo = document.getElementById("classNo").value;

  if (sqlId) {
    var url = "<%=contextPath%>/yh/core/codeclass/act/YHCodeClassAct/updateCodeItem.act";
    var rtJson = getJsonRs(url, mergeQueryString($("form2")));
    
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      document.getElementById("classCodeOld").value = document.getElementById("classCode").value ;
      window.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    }
  }else {
    var url = "<%=contextPath%>/yh/core/codeclass/act/YHCodeClassAct/addCodeItem.act";
 
    var rtJson = getJsonRs(url, mergeQueryString($("form2")));

    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      $("form2").reset();
      document.getElementById("classNo").value = classNo;
      document.getElementById("classCode").focus();
    }else {
      alert(rtJson.rtMsrg); 
    }
  }
}

function goback() {
  window.location.href = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/listCodeItem.act?classNo=" + document.getElementById("classNo").value ;
}
function returnBack(){
  var codeClassId =  '<%=codeClassId%>';
  window.location.href = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/listCodeItem.act?seqId="+codeClassId;
}
</script>
</head>
<body onload="doInit()">
<form method="post" name="form2" id="form2" >
<%
  if(sqlId.equals("")) {
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><span class="big3"><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>&nbsp;添加代码
    </span></td>
  </tr>
</table>
<%
  }else {
%>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><span class="big3"><img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img>&nbsp;修改代码
   </span> </td>
  </tr>
</table>
<%
  }
%>
  <input type="hidden" id="sqlId" name="sqlId" value="">
  <input type="hidden" id="classCodeOld" name="classCodeOld" value="">
<table class="TableBlock" align="center" width="300">
  <tr class="TableLine1">
    <td> 主分类:</td>
    <td><select name="classNo" id="classNo" class="BigSelect"></select></td>
  </tr>
  <tr class="TableLine2">
    <td>代码编号:</td>
    <td><font style="color:red">*</font><input type="text" id="classCode" name="classCode" class="SmallInput" value=""/></td>
  </tr>
  <tr class="TableLine1">
    <td>排序号:</td>
    <td><font style="color:red">*</font><input type="text" id="sortNo" name="sortNo" class="SmallInput" value=""/></td>
  </tr>
  <tr class="TableLine2">
    <td>代码描述:</td>
    <td>
      <font style="color:red">*</font><input type="text" id="classDesc" name="classDesc" class="SmallInput" value=""/>
  	</td>
  </tr>
  <tr class="TableLine1">
    <td colspan="2" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
      <input type="button" value="返回" class="SmallButton" onclick="returnBack();">
    </td>
  </tr>
</table>
</form>
</body>
</html>