<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/smsbox.css" />
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/divwin.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/smsutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
var jso = [
           {title:"微讯管理", contentUrl:"<%=contextPath %>/core/funcs/message/index.jsp", useIframe:true}
          ,{title:"提醒管理", contentUrl:"<%=contextPath %>/core/funcs/sms/index.jsp", useIframe:true}
         
          ];
//  
</script>
</head>
<body onload="buildTab(jso, 'smsdiv');">
<div id="smsdiv">
</div>
</body>
</html>