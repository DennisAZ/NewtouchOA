<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
      <%@ include file="/core/inc/header.jsp" %>
    <%

String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String sortName = request.getParameter("sortName");
if (sortName == null) {
  sortName = "";
}
String skin = request.getParameter("skin");
if (skin == null || "".equals(skin)) {
  skin = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建工作</title>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
  <script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
  <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
  <script type="text/javascript">
   var sortId = "<%=sortId%>";
   var sortName = "<%=sortName%>";
   var skin = "<%=skin%>";
   function doInit(){
     if (sortName) {
       sortId = getSortIdsByName(sortName);
     }
     if (sortId) {
       this.leftFrame.location.href = "left.jsp?sortId=" + sortId + "&skin=" + skin;
       this.mainFrame.location.href = "flowtop.jsp?sortId=" + sortId  + "&skin=" + skin;
     } else {
       this.leftFrame.location.href = "left.jsp?skin=" + skin;
       this.mainFrame.location.href = "flowtop.jsp?skin=" + skin;
     }
   }
  </script>
</head>
<frameset cols="220,*" frameborder="no" border="0" framespacing="0" onload="doInit()">
  <frame src=""   scrolling="auto" name="leftFrame"  id="leftFrame" title="leftFrame" />
  <frame src=""  scrolling="auto"  name="mainFrame" id="mainFrame" title="mainFrame" />
</frameset>
<noframes><body>
</body></noframes>
</html>