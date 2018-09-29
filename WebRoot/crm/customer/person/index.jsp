<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分栏显示</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
var loginUserPriv = <%=loginUser.getUserPriv()%>;
var loginUserId = <%=loginUser.getSeqId()%>;
var isAdminRole = <%=loginUser.isAdminRole()%>;
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getMaxPrivNo.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    if(rtJson.rtData == loginUserPriv && loginUserId != "1"){
      location = "<%=contextPath%>/crm/customer/person/indexPrivNo.jsp";
    }
  }
}
</script>
</head>
<frameset onload="doInit()" rows="40,*"  cols="*" frameborder="no" border="0" framespacing="0" id="frame1">
  <frame name="user_title" scrolling="no" noresize src="<%=contextPath %>/crm/customer/person/title.jsp" frameborder="NO">
  <frameset rows="*"  cols="210,*" frameborder="0" border="0" framespacing="0">	
    <frame name="navigateFrame" id="navigateFrame" src="<%=contextPath %>/crm/customer/person/utilitylist.jsp" frameborder="NO" noresize scrolling="auto"/>
    <frame name="contentFrame" id="contentFrame" src="<%=contextPath %>/crm/customer/person/query.jsp" frameborder="NO" scrolling="auto">	
  </frameset>
</frameset>
</html>