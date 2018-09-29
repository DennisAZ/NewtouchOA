<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.extuser.data.YHExtUser" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  ArrayList<YHExtUser> extList = (ArrayList<YHExtUser>)request.getAttribute("extList");
  int sumSize = extList.size();
 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>接口用户管理</title>
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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var sumSize = "<%=sumSize%>";
var sum = 0;
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/extuser/act/YHExtUserAct/getCount.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    //alert(rsText);
    for(var i = 0; i < rtJson.rtData.length; i++){
      sum = rtJson.rtData[i].sum;
    }
  }else{
  	alert(rtJson.rtMsrg); 
   }
}
function confirmDel() {	
  var str = window.prompt("删除后将不可恢复，确认删除请输入大写字母“OK”","")   
  if(str == "OK"){
      
     return true;
  }else {
    return false;
  }
}

function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function deleteAllUser() {
  if(!confirmDel()){
    return;
  }
  
  var deleteAllFlags = document.getElementsByName("deleteFlag");
  var flag = false;
  var idStrs = "";
  for(var i = 0; i < deleteAllFlags.length; i++) {
	if(deleteAllFlags[i].checked) {
      idStrs += deleteAllFlags[i].value + "," ;	
      flag = true;
	}	  
  }
  if(!flag) {
    alert("请选择需要删除的用户！");
    return;
  }   
  var url = "<%=contextPath %>/yh/core/funcs/system/extuser/act/YHExtUserAct/deleteExtUser.act";
  var seqIdStr = idStrs.split(',');
  var sumStr = seqIdStr.length-1;
  for(var i = 0; i < seqIdStr.length - 1; i++){
    var seqID = seqIdStr[i];
    var rtJson = getJsonRs(url, "idStrs=" + seqID);
    if (rtJson.rtState == "0") {
      //alert(rtJson.rtMsrg); 
      var URL="<%=contextPath %>/core/funcs/system/extuser/deleteExt.jsp?sumSize=" + sumStr;
      window.location = URL;
      
      //getJsonRs(url, null);
    }else {
      alert(rtJson.rtMsrg); 
    } 
  }
}

function commit(){
  var seqId = document.getElementById("seqId").value;
  var type = document.getElementById("type").value;
  var beginIp = document.getElementById("beginIp").value;
  var endIp = document.getElementById("endIp").value;
  var remark = document.getElementById("remark").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHIpRuleAct/updateIpRule.act?seqId="+seqId+"&beginIp="+beginIp+"&endIp="+endIp+"&remark="+remark+"&type="+type;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  parent.location = "<%=contextPath %>/core/funcs/system/accesscontrol/index.jsp";
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> <a name="bottom">&nbsp;接口用户管理</span>
    </td>
  </tr>
</table>
  <table class="TableBlock" width="100%">
    <%
      String authModule = "";
      String remark = "";
      String postFix = "";
      for(int i = 0; i < extList.size(); i++) {
        YHExtUser ext = extList.get(i);
        if(ext.getAuthModule() == null || ext.getAuthModule() == ""){
          authModule = "&nbsp;";
        }
        else{
          authModule = ext.getAuthModule();
        }
        if(ext.getRemark() == null || ext.getRemark() == ""){
          remark = "&nbsp;";
        }
        else{
          remark = ext.getRemark();
        }
        if(ext.getPostfix() == null || ext.getPostfix() == ""){
          postFix = "&nbsp;";
        }
        else{
          postFix = ext.getPostfix();
        }
        if(ext.getAuthModule() == null || ext.getAuthModule().equals("")){
          authModule = "&nbsp;";
        }else if(ext.getAuthModule().equals("1")){
          authModule = "内部短信";
        }else if(ext.getAuthModule().equals("4")){
          authModule = "工作流";
        }else if(ext.getAuthModule().equals("1,4,")||ext.getAuthModule().equals("4,1,")){
          authModule = "内部短信,工作流";
        }
    %>
    <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
      <td>&nbsp;<input type="checkbox" name="deleteFlag" id="deleteFlag" value="<%=ext.getSeqId()%>"></td>
      <td nowrap align="center"><%=ext.getUserId()%></td>
      <td nowrap align="center" title="<%=(ext.getUseFlag().equals("1"))?"启用":"停用"%>"><img src="<%=imgPath%>/<%=(ext.getUseFlag().equals("1"))?"correct":"error"%>.gif"></td>
      <td><%=authModule%></td>
      <td><%=remark%></td>
      <td><%=postFix%></td>
      <td nowrap align="center">
      <a href="<%=contextPath%>/core/funcs/system/extuser/newuse.jsp?seqId=<%=ext.getSeqId()%>"> 编辑</a>
      </td>
    </tr>
    <%} 
      if(extList.size()>0){ 
    %>
     <thead class="TableHeader">
      <td nowrap align="center" width="40">选择</td>
      <td nowrap align="center">用户名</td>
      <td nowrap align="center">启用</td>
      <td nowrap align="center">授权模块</td>
      <td nowrap align="center">备注</td>
      <td nowrap align="center">内容后缀</td>
      <td nowrap align="center">操作</td>
    </thead>
    <tr class="TableControl">
      <td colspan="10">
        <input type="checkbox" id="checkAlls" name="checkAlls" onclick="checkAll(this)"/><label for="checkAlls">全选</label> &nbsp;
        <input type="button"  value="删除" class="SmallButton" onClick="deleteAllUser();" title="删除所选用户"> &nbsp;
      </td>
    </tr>
  </table>
  <%
  }else{
%>
<br>
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">尚未定义访问规则</div>
    </td>
  </tr>
</table>
<% 
  }
%>
</body>
</html>