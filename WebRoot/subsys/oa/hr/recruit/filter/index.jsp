<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/extall.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var jso = [
       	  {title:"招聘筛选管理", contentUrl:"<%=contextPath %>/subsys/oa/hr/recruit/filter/hrFilterManage.jsp", imgUrl:"/yh/raw/ljf/imgs/asset.gif", useIframe:true}
          ,{title:"新建招聘筛选信息", contentUrl:"<%=contextPath %>/subsys/oa/hr/recruit/filter/newHrFilter.jsp", imgUrl:"/yh/raw/ljf/imgs/1news.gif", useIframe:true}
          ,{title:"招聘筛选查询", contentUrl:"<%=contextPath %>/subsys/oa/hr/recruit/filter/query.jsp", imgUrl:"/yh/raw/ljf/imgs/1news.gif", useIframe:true}
          ];
</script>
</head>
<body onload="buildTab(jso)">
</body>
</html>