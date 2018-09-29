<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHSysProps" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userIdStr = request.getParameter("userIdStr");
  if (userIdStr == null) {
    userIdStr = "";
  }
  //System.out.println(userIdStr);
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  boolean isAdmin = loginUser.isAdminRole();
%>
<table id="privInfo" class="TableBlock" width="95%" align="center">
  <tr>
    <td nowrap class="TableData">用户排序号：</td>
    <td nowrap class="TableData">
      <input type="text" id="userNo" name="userNo" class="BigInput" size="10" value="10" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))">&nbsp;
                用于同角色用户的排序
    </td>
  </tr> 
  
  <tr>
    <td nowrap class="TableData">访问控制：</td>
    <td nowrap class="TableData">
    <% if (!"admin".equals(userIdStr)) { %>
      <input type="checkbox" name="notLogin" id="notLogin" ><label for="notLogin">禁止登录OA系统</label>&nbsp;
      <input type="checkbox" name="notViewUser" id="notViewUser" ><label for="notViewUser">禁止查看用户列表</label>&nbsp;
      <input type="checkbox" name="notViewTable" id="notViewTable" ><label for="notViewTable">禁止显示桌面</label>
    <% } %>
      <input type="checkbox" name="useingKey" id="useingKey" ><label for="useingKey">使用USB KEY登录</label>
    </td>
  </tr>
</table>

