<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHSysProps" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userIdStr = request.getParameter("userIdStr");
  if (userIdStr == null) {
    userIdStr = "";
  }
  String postPriv = request.getParameter("postPriv");
  if (postPriv == null) {
    postPriv = "";
  }
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  boolean isAdmin = loginUser.isAdmin();
%>
<div>
<table id="baseInfo" class="TableBlock" width="95%" align="center">
  <tr>
    <td nowrap class="TableContent" width="120">用户名：</td>
    <td nowrap class="TableContent">
      <font style="color:red">*</font>
      <input type="text" id="userId" name="userId" class="BigInput" size="10" maxlength="20" value="" onblur="checkUserId()">&nbsp;
      <span id="userIdSpan"></span>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableContent">真实姓名：</td>
    <td nowrap class="TableContent">
      <font style="color:red">*</font>
      <input type="text" id="userName" readonly name="userName" class="BigInput" size="10" maxlength="30" value="">&nbsp;
    </td>
  </tr>
  <% if(!"admin".equals(userIdStr)){ %>
  <tr>
    <td nowrap class="TableContent">主角色：</td>
    <td nowrap class="TableContent">
      &nbsp;
      <select id="userPriv" name="userPriv" class="BigSelect">
      </select>&nbsp;&nbsp;<a href="javascript:selectPriv()">指定辅助角色</a>
    </td>
  </tr>
  <%} else {%>
  <tr>
    <td nowrap class="TableContent">主角色：</td>
    <td nowrap class="TableContent">
      &nbsp;
      <span class="Big"><b>系统管理员</b></span>&nbsp;&nbsp;<a href="javascript:selectPriv()">指定辅助角色</a>
    </td>
  </tr>
  <% } %>
  <tr id="privs" style="display:none;">
    <td nowrap class="TableData">辅助角色：</td>
    <td class="TableData">
      <input type="hidden" name="role" id="role" class="BigInput" size="25" maxlength="25" value="">
      <textarea cols=30 name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectRole(['role','roleDesc'],'','','1')">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      <br/>辅助角色仅用于扩展主角色的模块权限    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableContent">部门：</td>
    <td nowrap class="TableContent">
    <select id="deptId" disabled readonly name="deptId" style="height:22px;FONT-SIZE: 12pt;">
        </select>
      <a href="javascript:selectDeptOther()">指定其它所属部门</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    </td>
  </tr>
  
  <tr id="deptOther" style="display:none;">
    <td nowrap class="TableData">所属部门：</td>
    <td class="TableData">
      <input type="hidden" name="dept"  id="dept" class="BigInput" size="25" maxlength="25" value="">
      <textarea cols=30 name="deptDesc" id="deptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="">添加</a>
      <a href="javascript:;" class="orgClear" onClick="">清空</a>
    </td>
  </tr> 
  <% if(!"admin".equals(userIdStr)){ %>
  <tr>
    <td nowrap class="TableContent" width="120">管理范围：</td>
    <td nowrap class="TableContent">
      <select id="postPriv" name="postPriv" class="BigSelect" onchange="manageDept(this.value)">    
        <option value="0">本部门</option>
        <% if("1".equals(postPriv)) {%>
        <option value="1">全体</option>
        <option value="2">指定部门</option>
        <% }else if("2".equals(postPriv)) {%>
        <option value="2">指定部门</option>
        <% } %>
      </select>
      <span id="modulePrivDiv" style="display:none"><a href="javascript:modulePriv();">按模块设置管理范围</a></span>
                   在管理型模块中起约束作用
    </td>
  </tr>
  <%} %>
  <tr id="manageDept" style="display:none;">
    <td nowrap class="TableContent">管理范围（部门）：</td>
    <td class="TableContent">
      <input type="hidden" name="postDeptId" id="postDeptId" class="BigInput" size="25" maxlength="25" value="">
      <textarea cols=30 name="postDeptIdDesc" id="postDeptIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['postDeptId','postDeptIdDesc'],'','',1)">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('postDeptId').value='';$('postDeptIdDesc').value='';">清空</a>
    </td>
  </tr> 
  
  <tr>
    <td nowrap class="TableContent">考勤排班类型：</td>
    <td nowrap class="TableContent">
      <select disabled id="dutyType" readonly name="dutyType" class="BigSelect">
      </select>
    </td>
  </tr>

</table>
</div>


