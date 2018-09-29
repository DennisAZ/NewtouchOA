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
<title>词语过滤管理</title>
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

function doInit() {
  var tabArray = [{title:"词语过滤管理", content:"", contentUrl:"<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorWordsAct/getCensorWords.act", imgUrl: imgPath + "/cmp/tab/notify_open.gif", useIframe:true},
                  {title:"新建词语过滤", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/censorwords/new/index.jsp", imgUrl: imgPath + "/cmp/tab/notify_new.gif", useIframe:true},
                  {title:"词语过滤查询", content:"", contentUrl:"<%=contextPath%>/core/funcs/system/censorwords/query/index.jsp", imgUrl: imgPath + "/cmp/tab/infofind.gif", useIframe:true},
                  {title:"过滤模块设置", content:"", contentUrl:"<%=contextPath%>/yh/core/funcs/system/censorwords/act/YHCensorModuleAct/getCensorModule.act", imgUrl: imgPath + "/cmp/tab/notify_open.gif", useIframe:true}];
  buildTab(tabArray,'contentDiv',800);
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<div id="contentDiv"></div>
</body>
</html>