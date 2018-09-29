<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
<title>组织机构选择</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
</script>
</head>
<body>
<form action="" name="form1">
<p>
  <input type="hidden" name="user" id="user" value="" />
  <textarea name="userDesc" id="userDesc"  rows="10" cols="80">
  </textarea>
  <input type="button" onclick="selectUser(['user1', 'user1Desc']);" value="选择人员 "></input>
  <input type="button" onclick="$('user').value='';$('userDesc').value='';" value="清除"></input>
  <br />
  <input type="hidden" name="dept" id="dept" value="" />
  <textarea name="deptDesc" id="deptDesc"  rows="10" cols="80">
  </textarea>
  <input type="button" onclick="selectDept();" value="选择部门"></input>
  <input type="button" onclick="$('dept').value='';$('deptDesc').value='';" value="清除"></input>
  <br />
  <input type="hidden" name="role" id="role" value="" />
  <textarea name="roleDesc" id="roleDesc"  rows="10" cols="80">
  </textarea>
  <input type="button" onclick="selectRole();" value="选择角色"></input>
  <input type="button" onclick="$('role').value='';$('roleDesc').value='';" value="清除"></input>
</p>
</form>
</body>
</html>