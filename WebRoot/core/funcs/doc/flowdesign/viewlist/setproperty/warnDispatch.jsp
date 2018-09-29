<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String isList = request.getParameter("isList");
if(isList == null ){
  isList = "";
}
%>
<div>
<table id="remind"  width="95%" align="center"   class=TableList>
  <tr class="TableLine1">
    <td class="TableContent" >内部短信提醒设置：</td>
    <td class="TableData">
      <input type="checkbox" id="remindOrnot" name="remindOrnot" onclick="document.getElementById('remindfld').disabled = (this.checked? false : true);"/>
        <label for="remindOrnot">此步骤是否独立设置提醒方式</label>
      <fieldset id="remindfld" style="margin:5px;" disabled="true">
        <legend>提醒开启状态</legend>
        <div>
          <span style="width:100px;font-weight:bold;">下一步骤:</span>
          <input type="checkbox" id="smsRemindNext" name="smsRemindNext"/><img src="img/sms.gif" title="内部短信提醒">
          <input type="checkbox" id="sms2RemindNext" name="sms2RemindNext"/><img src="img/mobile_sms.gif" title="手机短信提醒">
          <input style="display:none" type="checkbox" id="webMailRemindNext" name="webMailRemindNext"/><img src="img/webmail.gif" style="display:none"  title="Internet邮件提醒">            
        </div>
        
        <div>
          <span style="width:100px;font-weight:bold;">发起人:</span>
          <input type="checkbox" id="smsRemindStart" name="smsRemindStart"/><img src="img/sms.gif" title="内部短信提醒">
          <input type="checkbox" id="sms2RemindStart" name="sms2RemindStart"/><img src="img/mobile_sms.gif" title="手机短信提醒">
          <input  style="display:none" type="checkbox" id="webMailRemindStart" name="webMailRemindStart"/><img src="img/webmail.gif" style="display:none"  title="Internet邮件提醒">            
        </div>
        
        <div>
          <span style="width:100px;font-weight:bold;">全部经办人:</span>
          <input type="checkbox" id="smsRemindAll" name="smsRemindAll"/><img src="img/sms.gif" title="内部短信提醒">
          <input type="checkbox" id="sms2RemindAll" name="sms2RemindAll"/><img src="img/mobile_sms.gif" title="手机短信提醒">
          <input  style="display:none" type="checkbox" id="webMailRemindAll" name="webMailRemindAll"/><img src="img/webmail.gif" style="display:none"  title="Internet邮件提醒">            
        </div>       
      </fieldset>    
    </td>
  </tr>

  <tr class="TableLine2">
    <td class="TableContent" >转交时内部邮件通知以下人员：</td>
    <td class="TableData">
      <input type="hidden" id="user" name="user" value="">
      <textarea cols=35 id="userDesc" name="userDesc" rows=2 class="BigStatic" readonly></textarea>
      <a href="javascript:;" class="orgAdd"   onClick="selectUser()">添加</a>
      <a href="javascript:;" class="orgClear"   onClick="clearUser('userDesc', 'user')">清空</a>   
    </td>   
  </tr>
  <tr class=TableControl><td colspan=2><div align="center">
  <input type="button"  value="保存" class="BigButton" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
  <% if("".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="history.back();">
  <% } %>
</div></td></tr>
</table>
</div>

