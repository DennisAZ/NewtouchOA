<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>

<%
String type = request.getParameter("type");
String userName = request.getParameter("userName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="style/css/css.css" rel="stylesheet" type="text/css" />
<link href="style/css/css-other.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/portal.js"></script>
<title>领导资料</title>
<script>
function getParentFrame(height2){
  return parent.getPF(height2);
}
</script>
</head>
<body>
<div class="s-left" style="width:20%">
 <iframe id="xsqkMenu"  src="xsqk-a.jsp" name="xsqkMenu" onload="autoHeight2(this.id);"
                        frameborder="0" scrolling="no"
                        style="width: 100%;background-color: #ffffff;" 
                        marginheight="0" marginwidth="0"></iframe>
</div>
<div class="s-right" style="width:80%">
 <iframe id="xsqkData" src="modules/research/research_list.jsp?type=1"  name="xsqkData"
                        frameborder="0" scrolling="auto" onload="autoHeight2(this.id);"
                        style="width: 100%;background-color: #ffffff;" 
                        marginheight="0" marginwidth="0"></iframe>
</div>
</body>
</html>