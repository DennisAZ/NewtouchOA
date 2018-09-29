<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<table id="otherInfo" class="TableBlock" width="95%" align="center">
  <tr>
    <td nowrap class="TableData">内部邮箱容量：</td>
    <td nowrap class="TableData">
      <input type="text" id="emailCapacity" name="emailCapacity" class="BigInput" size="5" maxlength="9" value="100"
        onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
      >&nbsp;MB
                为空则表示不限制大小
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">个人文件柜容量：</td>
    <td nowrap class="TableData">
      <input type="text" id="folderCapacity" name="folderCapacity" class="BigInput" size="5" maxlength="9" value="100"
        onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
      >&nbsp;MB
                 为空则表示不限制大小
    </td>
  </tr>
  
  <tr  style="display:none">
    <td nowrap class="TableData">Internet邮箱数量：</td>
    <td nowrap class="TableData">
      <input type="text" id="webmailNum" name="webmailNum" class="BigInput" size="5" maxlength="9" value=""
        onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
      >&nbsp;个
                为空则表示不限制数量
    </td>
  </tr>
  
  <tr  style="display:none">
    <td nowrap class="TableData">每个Internet邮箱容量：</td>
    <td nowrap class="TableData">
      <input type="text" id="webmailCapacity" name="webmailCapacity" class="BigInput" size="5" maxlength="9" value=""
        onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
      >&nbsp;MB
                为空则表示不限制大小
    </td>
  </tr>

  <tr>
    <td nowrap class="TableData">绑定IP地址：</td>
    <td nowrap class="TableData">
      <textarea id="bindIp" name="bindIp" class="BigInput" cols="50" rows="2"></textarea><br/>
                为空则该用户不绑定固定的IP地址，绑定多个IP地址用英文逗号(,)隔开<br>也可以绑定IP段，如“192.168.0.60,192.168.0.100-192.168.0.200”表示<br>192.168.0.60或192.168.0.100到192.168.0.200这个范围内都可以登录
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableData">备注：</td>
    <td nowrap class="TableData">
      <textarea id="remark" name="remark" class="BigInput" cols="50" rows="2"></textarea>
    </td>
  </tr>
</table>
