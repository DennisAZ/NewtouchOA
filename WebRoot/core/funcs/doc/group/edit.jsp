<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑部门组</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";


function doInit(){
  if (seqId) {
    var url = "<%=contextPath%>/yh/core/funcs/doc/group/act/YHDeptGroupAct/getDeptGroup.act";
    var rtJson = getJsonRs(url, "seqId="+seqId);
    if(rtJson.rtState == "0"){
      $("groupName").value = rtJson.rtData.groupName;
      $("orderNo").value = rtJson.rtData.orderNo;
      $("dept").value = rtJson.rtData.dept;
      $("deptDesc").value = rtJson.rtData.deptDesc;
    }else{
      alert(rtJson.rtMsrg);
    }
  }
}

function commit(){
  var groupName = document.getElementById("groupName");
  if(groupName.value.trim() == ""){ 
    alert("组名不能为空！");
    groupName.focus();
    groupName.select();
	return false;
  }
  reg = /['"]/g;
  if (groupName.value.match(reg)) {
    alert("组名不能有\"'\"和\"\"\"字符！");
    $('groupName').focus();
    return false;
  }
  var groupName = document.getElementById("groupName").value;
  var orderNo = document.getElementById("orderNo").value;
  var dept = document.getElementById("dept").value;

  var url = "<%=contextPath%>/yh/core/funcs/doc/group/act/YHDeptGroupAct/addDeptGroup.act";
  if (seqId) {
     url = "<%=contextPath%>/yh/core/funcs/doc/group/act/YHDeptGroupAct/updateDeptGroup.act?seqId="+seqId;
  }
 
  var rtJson = getJsonRs(url, "groupName="+encodeURIComponent(groupName)+"&orderNo="+orderNo + "&dept=" + dept );
  if(rtJson.rtState == "0"){
    location = "<%=contextPath %>/core/funcs/doc/group/index.jsp";
  }else{
    alert(rtJson.rtMsrg);
  }
}

</script>
</head>
<body topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">&nbsp;编辑用户组</span>
    </td>
  </tr>
</table>

<br>
<form name="form1" id="form1">
  <table class="TableBlock" width="400" align="center">
    <tr>
      <td nowrap class="TableContent" width="80">部门组名称：</td>
      <td class="TableData"><input type="text" name="groupName" id="groupName" size="30" class="BigInput" value=""></td>
    </tr>
    <tr>
      <td nowrap class="TableContent">排 序 号：</td>
      <td class="TableData"><input type="text" name="orderNo" id="orderNo" size="20" class="BigInput" value=""></td>
    </tr>
    <tr>
    <td nowrap class="TableContent">部  门：</td>
      <td class="TableData">
        <input type="hidden" name="dept" id="dept" value="">
        <textarea cols=48 name="deptDesc" id="deptDesc" rows=10 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button" value="提交" class="BigButton" title="提交数据" name="button1" OnClick="commit()">&nbsp&nbsp&nbsp&nbsp
          <input type="button" value="返回" class="BigButton" title="返回" name="button2" OnClick="location='index.jsp'">
      </td>
    </tr>
    </form>
</table>
</body>
</html>