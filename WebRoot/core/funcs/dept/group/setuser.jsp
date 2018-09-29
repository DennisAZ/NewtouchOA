<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>指定用户</title>
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
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHUserGroupAct/getEditUserGroup.act?seqId="+seqId;
  var rtJson = getJsonRs(url,"seqId="+seqId);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
  	  var userStr = rtJson.rtData[i].userStr;
  	  document.getElementById("user").value = userStr;
  	  if(userStr != ""){
  	    bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"}
      	]);
  	  }
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}

function commit(){
  var user = document.getElementById("user").value;
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHUserGroupAct/updateManageUserGroup.act?seqId="+seqId;
  var rtJson = getJsonRs(url,"user="+user);
  if(rtJson.rtState == "0"){
    location = "<%=contextPath %>/core/funcs/dept/group/index.jsp";
  }else{
    alert(rtJson.rtMsrg);
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;设置用户</span></td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data"  method="post" name="form1" id="form1">
<table class="TableBlock" width="550" align="center">
    <tr>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=48 name="userDesc" id="userDesc" rows=10 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" class="BigButton" OnClick="commit()">&nbsp;&nbsp;
        <input type="button" class="BigButton" value="返回" onclick="location='index.jsp'">
      </td>
    </tr>
   </form>
  </table>
</body>
</html>