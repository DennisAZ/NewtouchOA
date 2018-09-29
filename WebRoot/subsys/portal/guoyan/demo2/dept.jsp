<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header2.jsp" %>
    
<%
String type = request.getParameter("type").trim();
String deptName = request.getParameter("deptName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="style/css/css.css" rel="stylesheet" type="text/css" />
<link href="style/css/css-other.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="style/css/jq-yhtheme.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/pagepilot.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/portal/guoyan/js/portal.js"></script>
<title>部门专栏</title>
<script>
function autoHeight() {
   var autoSize = 600;
   var autoSize2 = 600;
   var mainframe = document.getElementById('deptMenu');
   var mainframe2 = document.getElementById('deptData');
   if( mainframe.document.body.scrollHeight > 600){
    autoSize = mainframe.document.body.scrollHeight ;
   }
   if( mainframe2.document.body.scrollHeight > 600){
     autoSize2 = mainframe2.document.body.scrollHeight ;
    }
   mainframe.style.height = autoSize + 20 + 'px';
   mainframe2.style.height = autoSize2 + 20 + 'px';
 }
function getParentFrame(height2){
  return parent.getPF(height2);
}
</script>
</head>
<body >
<div class="frame-box-two">
<div class="s-left" style="width:200px">
 <iframe id="deptMenu" src="dept-a.jsp?type=<%=type %>&deptName=<%=deptName %>"  name="deptMenu" onload="autoHeight2(this.id);"
                        frameborder="0" scrolling="no"
                        style="width: 100%;background-color: #ffffff;" 
                        marginheight="0" marginwidth="0"></iframe>
</div>
<div class="s-right" >
 <iframe id="deptData" src="modules/bmzl/<%=type %>-bmzz.jsp?deptName=<%=deptName %>" name="deptData"
                        frameborder="0" scrolling="auto" onload="autoHeight2(this.id);"
                        style="width: 100%;background-color: #ffffff;" 
                        marginheight="0" marginwidth="0"></iframe>
</div>
</div>
</body>
</html>