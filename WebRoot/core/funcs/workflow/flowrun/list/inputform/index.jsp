<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson,yh.core.global.YHSysProps" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");
String flowPrcs = request.getParameter("flowPrcs");
String isNewStr = request.getParameter("isNew");
String isWriteLog = request.getParameter("isWriteLog");
if (isWriteLog == null || "".equals(isWriteLog)) {
  isWriteLog = "0";
} 
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
int userId = loginUser.getSeqId();
if(isNewStr == null){
  isNewStr = "";
}
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/core/inc/header.jsp" %>
<title></title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
   <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
function doInit() {
  skinObjectToSpan(flowrun_list_inputform_index );
}
</script>
</head>
<frameset cols="*" rows="*,30" frameborder="no" border="0" framespacing="0" onload="doInit()">
  <frame src="main.jsp?runId=<%=runId %>&flowId=<%=flowId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>&isNew=<%=isNewStr %>&sortId=<%=sortId %>&skin=<%=skin %>&isWriteLog=<%=isWriteLog %>" name="main" scrolling="auto"  id="main" title="leftFrame" />
  <frame src="operate.jsp?runId=<%=runId %>&flowId=<%=flowId %>&prcsId=<%=prcsId %>&flowPrcs=<%=flowPrcs %>&isNew=<%=isNewStr %>&sortId=<%=sortId %>&skin=<%=skin %>" name="operate"  scrolling="no" id="operate" title="mainFrame"/>
</frameset>
<noframes><body>
</body></noframes>
</html>
