<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>  
<%
String id = request.getParameter("id");
String name = request.getParameter("name");
if (id == null) {
  id = "";
}
if (name == null) {
  name = "";
}
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑门户</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var id = '<%=id %>';
var name = '<%=name %>';
var requestUrl = contextPath + "/yh/core/funcs/portal/act/YHPortalAct";
function doInit() {
  if (id) {
    var url = requestUrl + "/getPortalRemark.act";
    var json = getJsonRs(url , "id=" + id);
    if (json.rtState == "0") {
      $('remark').update(json.rtData);
    }
  }
}
function save(){
  var nameStr = $F("portalName");
  if (checkName(nameStr)) {
    var url = requestUrl + "/savePortal.act";
    var param = $('form1').serialize();
    var json = getJsonRs(url , param );
    if (json.rtState == "0") {
      alert("保存成功！");
      location.href = "portallist.jsp";
    }
  }  
}
function checkName(nameStr) {
  if (!nameStr) {
     alert("门户名称不能为空！");
     $("portalName").focus();
     return false;
  } 
  if ( (id && name != nameStr ) || !id) {
    var url = requestUrl + "/checkPortalName.act";
    var json = getJsonRs(url , "name=" + nameStr);
    if (json.rtState == "0") {
      return true;
    } else {
      alert("此门户名已存在！");
      $("portalName").focus();
      return false;
    }
  }
  return true;
}
</script>
</head>
<body onload="doInit()">
<table  border="0" width="90%" cellspacing="0" cellpadding="3" class="small" style="margin:5px 0px;">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/control_theme.gif" align="abstop"/><span class="big3"> 编辑门户</span><br>
    </td>
  </tr>
</table>
<hr />

<form  method="post" id="form1" name="form1" action="">
<input value="<%=id %>" type="hidden" name="id" id="id"/>
   <table width="450" align="center" class="TableList">
     <tr>
      <td nowrap class="TableContent">门户名称：</td>
       <td class="TableData" align="left">
        <input type="text" name="portalName" id="portalName"  size="30" maxlength="100" class="BigInput" value="<%=name %>">
        <font style='color:red'>*</font>
      </td>
      </tr>
      <tr>
      <tr>
      <td nowrap class="TableContent">备注：</td>
       <td class="TableData" align="left">
        <textarea cols="25" rows="6" name="remark" id="remark"></textarea>
      </td>
      </tr>
      <tr>
      <td colspan="2" align="center"><input type="button" value="保存" class="SmallButton" onclick="save()">
      <input type="button" value="返回" class="SmallButton" onclick="location.href='portallist.jsp'">
      <% 
      if (!"".equals(id)) {
        %> 
        <input type="button" value="设计" class="SmallButton" onclick="location.href='<%=contextPath %>/core/frame/webos/design/index.jsp?type=edit&id=<%=id %>'">
         <% 
      }
      %>
      </td>
      </tr>
      </table>
</form>
</body>
</html>