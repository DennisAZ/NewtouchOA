<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>人员选择</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/cmp/tab.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

</script>
</head>

<body>
<input value="" id="userName" name="userName"/><br />
<input value="" id="userId" name="userId"/><br />
<input type="button" value="选择" onclick="selectUser(['userId','userName'])"/>
<br />
<input value="" id="userName2" name="userName2"/><br />
<input value="" id="userId2" name="userId2"/><br />
<input type="button" value="在线人员" onclick="selectUser(['userId2','userName2'],1)"/>
<br />
<textarea  id="userName3" name="userName3"></textarea><br/>
<input id="userId3" name="userId3"/><br />
<input type="button" value="支持textarea" onclick="selectUser(['userId3','userName3'])"/>


</body>
</html>