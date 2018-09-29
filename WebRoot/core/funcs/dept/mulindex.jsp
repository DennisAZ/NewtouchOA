<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if (treeId == null){
    treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
  String deptLocal = request.getParameter("deptLocal");
  if (deptLocal == null){
    deptLocal = "";
  }
%>
<head>
<title>部门管理</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
/**
 * 页面加载初始化
 */
 var treeId = "<%=treeId%>";
 var deptParent = "<%=deptParent%>";
 var deptLocal = encodeURI("<%=deptLocal%>");
function doInit() {
  var tabArray = [{title:"部门管理", content:"", contentUrl:"<%=contextPath%>/core/funcs/dept/deptedit.jsp?treeId=" + treeId + "&deptParent=" + deptParent + "&deptLocal=" + deptLocal, imgUrl: imgPath + "/cmp/tab/asset.gif", useIframe:true}
                 ];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>

<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>
</html>