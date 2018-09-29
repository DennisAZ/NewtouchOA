<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;绑定通元系统</span>
    </td>
  </tr>
</table>
<div id="dispBindUserDiv">
	<table id="dispBindUser" class="TableBlock" width="95%" align="center">
	  <tbody>
		  <tr width="100%">
		    <td nowrap class="TableData" width="30%">目前绑定用户:</td>
		    <td nowrap class="TableData" id="dispBindUserId" width="70%"></td>
		  </tr>
		  <tr>
		    <td nowrap class="TableData">用户描述:</td>
		    <td nowrap class="TableData" id="dispBindUserDesc"></td>
		  </tr>
		  <tr>
		    <td nowrap class="TableContent">
		    绑定控制:
		    </td>
		    <td nowrap class="TableContent">
			    <img src="<%=imgPath %>/user_edit.png"/>&nbsp;<a id="rebindUser" href="javascript:void(0)" onclick="showUpdateWindow();return false;">更换绑定用户</a>
			    <img src="<%=imgPath %>/sms/sms_delete.gif"/>&nbsp;<a id="removeBindUser" href="javascript:void(0)" onclick="removeBind();return false;">解除绑定</a>
		    </td>
		  </tr>
	  </tbody>
	</table>
</div>
<br/>
<div id="bindUserDiv">
	<table id="bindUser" class="TableBlock" width="95%" align="center">
	  <thead>
	  </thead>
	  <tbody>
		  <tr>
		    <td nowrap class="TableContent" width="30%">
		    绑定控制:
		    </td>
		    <td nowrap class="TableContent" width="70%">
		      <img src="<%=imgPath %>/user_add.gif"/>&nbsp;<a href="javascript:void(0)" onclick="showBindWindow();return false;">绑定用户</a>
		    </td>
		  </tr>
	  </tbody>
	</table>
</div>
