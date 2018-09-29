<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建分组</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
function commit(){
  var groupName = document.getElementById("groupName");
  if(!groupName.value.trim()){ 
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
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHUserGroupAct/addGroup.act";
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    location = "<%=contextPath %>/core/funcs/dept/group/index.jsp";
  }else{
	alert(rtJson.rtMsrg);
  }
}
</script>
</head>
</html>
<body topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新建分组</span>
    </td>
  </tr>
</table>
<br>
<form name="form1" id="form1">
 <table class="TableBlock" width="400" align="center">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.dept.data.YHUserGroup"/>
  <input type="hidden" id="seqId" name="seqId" value=""/>
    <tr>
      <td nowrap class="TableContent" width="80">用户组名称：<font style='color:red'>*</font></td>
      <td class="TableData"><input type="text" name="groupName" id="groupName" size="30" class="BigInput"></td>
    </tr>
    <tr>
      <td nowrap class="TableContent">排 序 号：</td>
      <td class="TableData"><input type="text" name="orderNo" id="orderNo" size="20" class="BigInput" maxlength="20"></td>
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
