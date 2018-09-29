<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String sqlId = request.getParameter("sqlId");
	if(sqlId == null) {
	  sqlId = "";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加或修改代码主分类</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var sqlId = "<%=sqlId%>";
function doInit() {
  //传递了seqId，编辑情况  if (sqlId) {
    var url = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/getCodeClass.act";
    var rtJson = getJsonRs(url, "sqlId=" + sqlId);
    //alert(rsText);
    if (rtJson.rtState == "0") {  
      document.getElementById("sqlId").value = rtJson.rtData.sqlId;
      document.getElementById("classNofirst").value = rtJson.rtData.classNo ;
      document.getElementById("classNo").value = rtJson.rtData.classNo;
      document.getElementById("sortNo").value = rtJson.rtData.sortNo;
      document.getElementById("classDesc").value = rtJson.rtData.classDesc;
      if(rtJson.rtData.classLevel == '1'){     
    	 document.getElementById("classLevel1").checked = true;
    	 document.getElementById("classLevel0").checked = false;
      }else{
        document.getElementById("classLevel0").checked = true;
        document.getElementById("classLevel1").checked = false;
      }
      parent.navigateFrame.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    }
  }
  document.getElementById("classNo").focus();
}

function check() {
  var cntrl = document.getElementById("classNo");
  if (!cntrl.value) {
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

  if (sqlId) {
    var url = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/updateCodeClass.act";
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      document.getElementById("classNofirst").value = document.getElementById("classNo").value ;
      parent.navigateFrame.location.reload();
    }else {
      alert(rtJson.rtMsrg); 
    }
  }else {
    var url = "<%=contextPath %>/yh/core/codeclass/act/YHCodeClassAct/addCodeClass.act";
    var rtJson = getJsonRs(url, mergeQueryString($("form1")));
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      parent.navigateFrame.location.reload();
      $("form1").reset();
      document.getElementById("classNo").focus();
    }else {
      alert(rtJson.rtMsrg); 
    }
  }
}
</script>
</head>
<body onload="doInit()">
<form name="form1" id="form1" method="post">
<%
  if(sqlId.equals("")) {
%>   
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><span class="big3">
    <img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>添加代码主分类
    </span></td>
  </tr>
</table>
<%
  }else {
%>    
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><span class="big3">
    <img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img>修改代码主分类
    </span></td>
  </tr>
</table>
<%
  }
%>
  <input type="hidden" name="sqlId" id="sqlId" value=""></input>
  <input type="hidden" id="classNofirst" name="classNofirst" value=""/><br/>
<table class="TableBlock" cellscpacing="1" cellpadding="3" width="300" align="center">
  <tr>
    <td class="TableData">代码编号:</td>
    <td class="TableData"><font style="color:red">*</font><input type="text" id="classNo" name="classNo" class="SmallInput" value=""/></td>
  </tr>
  <tr>
    <td class="TableData">排序号:</td>
    <td class="TableData"><font style="color:red">*</font><input type="text" id="sortNo" name="sortNo" class="SmallInput" value=""/></td>
  </tr>
  <tr>
    <td class="TableData">代码描述:</td>
    <td class="TableData"><font style="color:red">*</font><input type="text" id="classDesc" name="classDesc" class="SmallInput" value=""/></td>
  </tr>
  <tr>
    <td class="TableData">代码级别:</td>
    <td class="TableData">用户自定义:<input type="radio" name="classLevel" id="classLevel1" value="1" checked/>
  	          系统预定义:<input type="radio" name="classLevel"  id="classLevel0" value="0"/>
  	</td>
  </tr>
  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
    </td>
  </tr>
</table>
</form>
</body>
</html>