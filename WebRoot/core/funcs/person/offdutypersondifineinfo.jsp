<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<table id="selfDef" class="TableBlock" width="95%" align="center">
  <tr>
    <td class="TableData" width="30%">别名：</td>
    <td class="TableData" width="70%">
      <input type="text" id="byname" name="byname" class="BigInput" size="10" maxlength="20" onblur="checkByname()">
      <span id="bynameSpan"></span><br />
用户可用此别名登录系统，别名不能与其他用户的别名或用户名相同 
    </td>
  </tr>
<!--  -->
  <tr id="passwordDiv" style="display:'';">
    <td nowrap class="TableData">密码：</td>
    <td nowrap class="TableData">
      <input type="password" id="password" name="password" class="BigInput" size="20" maxlength="20"> 
      <font style="color:red">建议填8-20位数字  </font>  
    </td>
  </tr>

  <tr>
    <td nowrap class="TableData">性别：</td>
    <td nowrap class="TableData">
      <select id="sex" name="sex" class="BigSelect">
        <option value="0">男</option>
        <option value="1">女</option>
      </select>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">生日：</td>
    <td nowrap class="TableData">
      <input type="text" id="birthday" name="birthday" size="10" maxlength="10" class="BigInput" value="">
      <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:hand">
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">界面主题：</td>
    <td class="TableData">
      <select id="theme" name="theme" class="BigSelect">
      </select>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData"> 手机：</td>
    <td class="TableData">
      <input type="text" id="mobilNo" name="mobilNo" size="16" maxlength="23" class="BigInput" value=""
        onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
      >
      <input type="checkbox" id="mobilNoHidden" name="mobilNoHidden"><label for="mobilNoHidden">手机号码不公开</label><br>
                填写后可接收OA系统发送的手机短信，手机号码不公开仍可接收短信
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData"> 电子邮件：</td>
    <td class="TableData">
      <input type="text" id="email" name="email" size="25" maxlength="50" class="BigInput" value="">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 工作电话：</td>
    <td class="TableData">
      <input type="text" id="telNoDept" name="telNoDept" size="16" maxlength="23" class="BigInput" value="">
    </td>
  </tr>
</table>