<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String isList = request.getParameter("isList");
if(isList == null ){
  isList = "";
}
%>
<div>
<table id="selectperson" width="95%" align="center" class="TableList">
  <tr class="TableLine1">
    <td  class="TableContent" width=100px>选人过滤规则：</td> 
    <td class="TableData">
      <select id="userFilter" name="userFilter">
        <option value="0" selected>允许选择全部指定的经办人</option>
        <option value="1">只允许选择本部门经办人</option>
        <option value="3">只允许选择上级部门经办人</option>
          <option value="4">只允许选择下级部门经办人</option>
          <option value="2" >只允许选择本角色经办人</option> 
          <option value="21" >只允许选择本部门主管</option> 
      </select>
      <div style="margin-top:5px">说明：选人过滤规则在流程转交选择经办人时生效。默认设置为允许选择全部指定的经办人。</div>
    </td>
  </tr>
    
  <tr class="TableLine2">
    <td   class="TableContent"  width=100px>自动选人规则：</td>
    <td>
      <select id="autoType" name="autoType" onchange="autoTypeSet(this)">
        <option value="0" selected>不进行自动选择</option>
        <option value="1">自动选择流程发起人</option>
          <option value="2">自动选择本部门主管</option>
         <option value="4">自动选择上级主管领导</option>
        <option value="6">自动选择上级分管领导</option>
         <option value="5">自动选择一级部门主管</option>
        <option value="3">指定自动选择默认人员</option>
        <option value="7">按表单字段选择</option>
        <!--<option value="8">自动选择指定步骤主办人</option>-->
        <option value="20">自动选择本部门角色</option>
        <option value="21">自动选择父流程当前主办人</option>
      </select>
      <div id="formItemDiv" style="display:none">
        <span>根据表单字段决定默认办理人(第一个作为主办人)：</span>
        <select id="formListItem" name="formListItem">
        
        </select>
      </div>
      
      <div id="baseUserDiv" style="display:none"><b>部门针对对象:</b>
        <select id="autoBaseUser" name="autoBaseUser">
          <option value="0">当前步骤</option>
          
        </select>   
        &nbsp;<a href="#" title="默认设置为：针对当前步骤主办人。">说明</a>
      </div>
      
     <div id="prcsUserDiv" style="display:none"><b>请指定步骤:</b>
       <select id="autoPrcsUser" name="autoPrcsUser">
         
       </select>
       &nbsp;<a href="#" title="将选择此步骤第一次办理时的主办人">说明</a>
     </div>
     
     <div id="autoUsersetDiv" style="display:none">
        <b>主办人：</b>
        <input type="hidden" id="autoUserHo" name="autoUserHo" value="">
        <input type="text" id="autoUserHoName" name="autoUserHoName" value="" size="10" class="SmallStatic" readonly>
        <font color=red>主办人是某步骤的负责人，只允许主办人编辑表单、公共附件和转交流程</font><br/>
        <b>经办人：</b>
        <input type="hidden" id="autoUserOp" name="autoUserOp" value="">
        <textarea cols=35 id="autoUserOpDesc" name="autoUserOpDesc" rows="4" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="addUser('autoUserHo','autoUserHoName','autoUserOp','autoUserOpDesc')" title="指定经办人和主办人">指定经办/主办人</a>
        <a href="javascript:;" class="orgClear"  onClick="clearUser('autoUserHoName','autoUserHo');clearUser('autoUserOpDesc','autoUserOp')" title="清空经办人和主办人">清空</a>      
     </div>
     
     <div id="roleItemDiv" style="display:none">
        <span>选择相关角色：</span>
        <select id="roleListItem" name="roleListItem">
        
        </select>
      </div>
      
     
     <br>说明：通过自动选人规则,使流程经办人通过指定规则智能选择。默认设置为：不进行自动选择。注意，请同时设置好经办权限，自动选人规则才能生效。

    </td>
  </tr>
  <tr class=TableControl><td colspan=2><div align="center">
  <input type="button"  value="保存" class="BigButton" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
  <% if("".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="parent.location.reload();">
  <% } %>
</div></td></tr>
</table>
</div>
