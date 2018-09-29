<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var jso = [
       	  {title:"流程分类管理", content:"", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowsort/flowsortlist.jsp", imgUrl:imgPath + "/asset.gif", useIframe:true}
          ,{title:"流程分类添加", content:"", contentUrl:"<%=contextPath %>/core/funcs/workflow/flowsort/flowsortinput.jsp", imgUrl:imgPath + "/edit.gif", useIframe:true}
          ];
</script>
</head>
<body onload="buildTab(jso, 'flowsortdiv', 800)">
 <div id="flowsortdiv">
 
 </div>
</body>
</html>