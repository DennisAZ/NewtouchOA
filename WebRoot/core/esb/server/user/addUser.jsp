<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<body>
<p><p>
<form action="<%=contextPath%>/yh/core/esb/server/user/act/TdUserAct/addUser.act" id="" method="post">
<table class="TableBlock" width="80%" align="center">
    <tr>
  	  <td  nowrap class="TableData">用户账号：</td>
  	  <td  nowrap class="TableData"><input type="text" id="userCode" name="userCode" style="width:120px;"></td>
  	  <td  nowrap class="TableData">用户名称：</td>
  	  <td  nowrap class="TableData"><input type="text" id="userName" name="userName" style="width:120px;"></td>
    </tr>
    <tr>
  	  <td  nowrap class="TableData">用户密码：</td>
  	  <td  nowrap class="TableData" colspan="3"><input type="password" id="password" name="password" style="width:120px;">
      <select id="appId" name="appId" style="width:120px;display:none">
          <option value="1">1</option>
          <option value="2">2</option>
        </select></td>
  	  <!-- 
      <td  nowrap class="TableData"  style="">对应的应用：</td>
  	  <td  nowrap class="TableData" style="">
  	    <select id="appId" name="appId" style="width:120px;">
  	      <option value="1">1</option>
  	      <option value="2">2</option>
  	    </select>
  	  </td>
       -->
    </tr>
    <tr>
  	  <td  nowrap class="TableData">用户类型：</td>
  	  <td  nowrap class="TableData">
  	    <input type="radio" id="userType1" name="userType" value="0" checked>下级单位
  	    <input type="radio" id="userType2" name="userType" value="1">总部
  	  </td>
  	  <td  nowrap class="TableData">用户状态：</td>
  	  <td  nowrap class="TableData">
  	    <input type="radio" id="status1" name="status" value="0" checked>启用
  	    <input type="radio" id="status2" name="status" value="1">未启用
  	  </td>
    </tr>
    <tr>
  	  <td  nowrap class="TableData">用户描述：</td>
  	  <td  nowrap class="TableData" colspan="3"><textarea id="description" name="description" rows="3" style="width:98%;"></textarea></td>
    </tr>
    <tr>
  	  <td  nowrap class="TableData" colspan="4" align="center">
  	    <input type="submit" id="" name="" value="提交">
  	    <input type="reset" id="" name="" value="重置">
  	  </td>
    </tr>
</table>
</form>
</body>
</html>