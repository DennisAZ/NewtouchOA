<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String isList = request.getParameter("isList");
if(isList == null ){
  isList = "";
}
%>
<div>
<table id="baseInfo" align="center" class=TableList>
  <tr class="TableLine1">
    <td   class="TableContent">序号：</td>
    <td   class="TableData">
      <input type="text" id="prcsId" name="prcsId" size="2" maxlength="10"  value="">
    </td>
  </tr>

  <tr class="TableLine2">
    <td   class="TableContent" >节点类型：</td>
    <td class="TableData">
      <select id="childFlow" name="childFlow"  onchange="prcsTypeChange()">
        <option value="0" selected>步骤节点</option>
        <option value="1" >子流程节点</option>
      </select>
    </td>
  </tr>
  
  <tbody id="childFlowTbody" style="display:none">
    <tr class="TableLine1">
      <td   class="TableContent" >子流程类型：</td>
      <td class="TableData">
        <select id="childFlowName" name="childFlowName"  onchange="childFlowChange(this.value)">
          <option value="0" selected>请选择子流程类型</option> 
        </select>
      </td>
    </tr>
    
    <tr class="TableLine2">
      <td   class="TableContent" >是否拷贝公共附件：</td>
      <td class="TableData">
      	<input type="radio" name="copyAttach" id="attach0" value="0" checked onclick=""><label for="attach0">否</label>
      	<input type="radio" name="copyAttach" id="attach1" value="1"  onclick=""><label for="attach1">是</label>
      </td>
    </tr>
    
    <tr class="TableLine1">
      <td class="TableContent" >拷贝表单字段：</td>
      <td class="TableData">
        <div align="center">
          <span>
            <select id="fldParent" name="fldParent" size="10"><optgroup label="父流程字段">
            </optgroup>
            </select>
          </span>
          
          <span id="fldSubSpan" style="display:none">
            <select id="fldChild" name="fldChild" size="10">
              <optgroup label="子流程字段">
              </optgroup>
            </select>
          </span></div>
          <div>
          <input type="button" class="BigButtonB" value="添加映射关系" onclick="addRelation()" />
        </div>
      </td>
    </tr>
    
    <tr class="TableLine2">
      <td   class="TableContent" >表单字段映射：</td>
      <td class="TableData">
        <div id="relationDiv"></div>
        <input type="hidden" name="relation" id="relation" value="" />
      </td>
    </tr>
    
    <tr class="TableLine1">
      <td   class="TableContent" >结束后动作：</td>
      <td>
        <input type="radio" name="overAct" id="overAct0" value="0" checked onclick="overActClick()"><label for="act0">结束并更新父流程节点为结束</label>
        <input type="radio" name="overAct" id="overAct1" value="1"  onclick="overActClick()"><label for="act1">结束并返回父流程步骤</label>
      </td>
    </tr>
    
    <tr class="TableLine2" id="selback1"  style="display:none">
      <td   class="TableContent" >返回步骤：</td>
      <td class="TableData">
        <select id="prcsBack" name="prcsBack" onchange="clearPrcsUser()">
          <option value="0" selected>请选择返回的步骤</option>
        </select>
     </td>
   </tr>
   
  
   <tr class="TableLine1" id="selback2"  style="display:none">
   	<td   class="TableContent" > 返回步骤默认经办人</td>
   	<td class="TableData">
      <b>主办人：</b>
      <input type="hidden" id="backUserHo" name="backUserHo" value="">
      <input type="text" id="backUserHoDesc" name="backUserHoDesc" value="" size="10" class="SmallStatic" readonly><br>
      <b>经办人：</b>
      <input type="hidden" id="backUserOp" name="backUserOp" value="">
      <textarea cols=30 id="backUserOpDesc" name="backUserOpDesc" rows="4" class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd"   onClick="addUserAfterValidate('backUserHo','backUserHoDesc','backUserOp','backUserOpDesc')" title="指定经办人和主办人">指定经办/主办人</a>
      <a href="javascript:;" class="orgClear"  onClick="clearUser('backUserHoDesc','backUserHo');clearUser('backUserOpDesc','backUserOp')" title="清空经办人和主办人">清空</a>
    </td>
   </tr>
  </tbody>
  
  <tbody id="processTbody" >
    <tr class="TableLine1">
      <td   class="TableContent" >步骤名称：</td>
      <td class="TableData">
        <input type="text"   id='prcsName' name="prcsName" size="30" maxlength="100" class="SmallInput" value="">
      </td>
    </tr>
    <tr style="background-color: #FFFFFF">
      <td  class="TableContent" >下一步骤：</td>
      <td style="border-top:1px #606275 solid;">
        <input type="hidden" name="prcsTo" value="" id="prcsTo"/>
        <div id="next_process"></div>
      </td>
    </tr>
    
  </tbody>
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
