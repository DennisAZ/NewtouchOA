<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
String id = request.getParameter("id");
%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>设置门户权限</title>
<link  rel="stylesheet"  href  ="<%=cssPath  %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var id = '<%=id %>';
var requestUrl = contextPath + "/yh/core/funcs/portal/act/YHPortalAct"
function doInit() {
  var url = requestUrl + "/getPortalPriv.act?id=" + id;
  var json = getJsonRs(url); 
  if (json.rtState == "0") {
    var priv = json.rtData;
    if (priv.user) {
      $('user').value = priv.user;
      $('userDesc').update(priv.userDesc);
    }
    if (priv.dept) {
      $('dept').value = priv.dept;
      $('deptDesc').update(priv.deptDesc);
    }
    if (priv.role) {
      $('role').value = priv.role;
      $('roleDesc').update(priv.roleDesc);
    }
  }  
}
function setPriv() {
  var url = requestUrl + "/setPortalPriv.act";
  var str = "user=" + $F("user") + "&role=" + $F("role") + "&dept=" + $F("dept") + "&id=" + id; ; 
  var json = getJsonRs(url , str); 
  if (json.rtState == "0") {
    alert("修改成功！");
    return ;
  }  
}
</script>
</head>
<body onload="doInit()">
<table  border="0" width="90%" cellspacing="0" cellpadding="3" class="small" style="margin:5px 0px;">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/control_theme.gif" align="abstop"/><span class="big3"> 门户权限设置</span><br>
    </td>
  </tr>
</table>
<br/>
<table class="TableBlock" width="600" height="100%" align="center">
        <tr>
          <td class="TableHeader" colspan="2">
            配置权限:
          </td>
        </tr>
        <tr>
          <td nowrap class="TableContent"" align="center">授权范围：<br>（部门）</td>
          <td class="TableData">
            <input type="hidden" name="dept" id="dept" value="">
            <textarea cols=40 name="deptDesc" id="deptDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
            <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
            <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
          </td>
        </tr>
        <tr>
          <td  class="TableContent"" align="center">授权范围：<br>（角色）</td>
          <td class="TableData">
            <input type="hidden" name="role" id="role" value="">
            <textarea cols="40" name="roleDesc" id="roleDesc" rows="8" class="BigStatic" wrap="yes" readonly></textarea>
            <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
            <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
          </td>
        </tr>
        <tr>
          <td  class="TableContent"" align="center">授权范围：<br>（人员）</td>
          <td class="TableData">
            <input type="hidden" name="user" id="user" value="">
            <textarea cols="40" name="userDesc" id="userDesc" rows="8" class="BigStatic" wrap="yes" readonly></textarea>
            <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
            <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
          </td>
        </tr>
        <tr>
          <td   class="TableControl" colspan="2" align="center">
               <input type="button" value="返回" onclick="location.href = 'portallist.jsp'" class="BigButton">&nbsp;&nbsp;
              <input type="button" value="修改" onclick="setPriv()" class="BigButton">&nbsp;&nbsp;
          </td>
        </tr>
      </table>
</body>
</html>