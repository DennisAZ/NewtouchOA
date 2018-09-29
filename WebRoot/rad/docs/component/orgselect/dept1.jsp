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
<input value="" id="deptName" name="deptName"/><br />
<input value="" id="deptId" name="deptId"/><br />
<input type="button" value="选择" onclick="selectDept(['deptId','deptName'])"/>
<br />
<input value="" id="deptName2" name="deptName2"/><br />
<input value="" id="deptId2" name="deptId2"/><br />
<input type="button" value="在线人员" onclick="selectDept(['deptId2','deptName2'],1)"/>
<br />
<input value="" id="deptName4" name="deptName4"/><br />
<input value="" id="deptId4" name="deptId4"/><br />
<input type="button" value="不能选全体部门" onclick="selectDept(['deptId4','deptName4'],null,null,true)"/>
<br />
<textarea  id="deptName3" name="deptName3"></textarea><br/>
<input id="deptId3" name="deptId3"/><br />
<input type="button" value="支持textarea" onclick="selectDept(['deptId3','deptName3'])"/>


</body>
</html>