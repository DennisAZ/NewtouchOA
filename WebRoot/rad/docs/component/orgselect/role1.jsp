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
<textarea  id="roleName4" name="roleName4"></textarea>
<input value="" id="roleId4" name="roleId4"/><br />
<input type="button" value="只能选低角色" onclick="selectRole(['roleId4','roleName4'],null,null,true)"/>
<br />
<textarea  id="roleName3" name="roleName3"></textarea><br/>
<input id="roleId3" name="roleId3"/><br />
<input type="button" value="支持textarea" onclick="selectRole(['roleId3','roleName3'])"/>
</body>
</html>