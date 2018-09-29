<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String isList = request.getParameter("isList");
if(isList == null ){
  isList = "";
}
%>
<div>
<table id="turn" width="95%" align="center" class=TableList>
  <tr class="TableLine1">
    <td  class="TableContent">主办人相关选项</td> 
    <td class="TableData">
      <select  id="topDefault" name="topDefault">
       	<option value=0 selected>明确指定主办人</option>
       	<option value=2 >无主办人会签</option>
       	<option value=1 >先接收者为主办人</option>
      </select>
      &nbsp;<a href="#" title="默认设置为：明确指定主办人。">说明</a>
      <div id="lock">
        <span id="lockinfo" style="font-weight:bold;">是否允许修改主办人相关选项：</span>
        <select  id="userLock" name="userLock">
       	  <option value="1">允许</option>
       	  <option value="0" selected>不允许</option>
      	</select>      
      </div>
    </td> 
  </tr>
  
  <tr class="TableLine2">
    <td class="TableContent">会签选项：</td>
    <td class="TableData"><b>是否允许会签：</b>
      <select id="feedBack" name="feedBack" onchange="signChange(this)" >
        <option value="0" >允许会签</option>
        <option value="1" >禁止会签</option>
        <option value="2" >强制会签</option>
      </select>
      &nbsp;<a href="#" title="说明：如设置强制会签，则不会签不能进行办理完毕操作">说明</a>
      
      <div id="signLookDiv">
        <b>会签意见可见性：</b>
        <select id="signLook" name="signLook">
          <option value="0" >总是可见</option>
          <option value="1" >本步骤经办人之间不可见</option>
          <option value="2" >针对其他步骤不可见</option>
        </select>
      </div>
    </td>
  </tr>
  
  <tr class="TableLine1">
    <td class="TableContent">强制转交：</td>
    <td class="TableData">
      <b>经办人未办理完毕时是否允许主办人强制转交：</b>
      <select  id="turnPriv" name="turnPriv">
        <option value="1">允许</option>
        <option value="0" selected>不允许</option>
      </select>
    </td>   
  </tr>
  
  <tr class="TableLine2">
    <td class="TableContent">回退选项：</td>
    <td class="TableData">
      <b>是否允许回退：</b>
      <select id="allowBack" name="allowBack">
        <option value="0">不允许</option>
        <option value="1" >允许回退上一步骤</option>
        <option value="2" >允许回退之前步骤</option>
      </select>
    </td>   
  </tr>
  
  <tr class="TableLine1">
    <td class="TableContent">并发相关选项：</td>
    <td class="TableData">
      <b>是否允许并发：</b>
      <select  id="syncDeal" name="syncDeal">
        <option value="0">禁止并发</option>
        <option value="1" >允许并发</option>
        <option value="2" >强制并发</option>
      </select><br/>
      <b>并发合并选项：</b>
      <select id="gatherNode" name="gatherNode">
        <option value="0">非强制合并</option>
        <option value="1" >强制合并</option>
      </select>
      <a href="#" title="非强制合并：此步骤主办人在并发分支中任意分支转至后即可进行转交<br>强制合并：所有可能直接转至此步骤的并发步骤都已转至后方可转交下一步">说明</a>    
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


