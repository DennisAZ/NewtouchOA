<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<table width="95%" class="TableBlock" align="center">
    <tr>
      <td class="TableContent">流程：</td>
      <td class="TableData">
      <select id="flows" name="flows" onchange="changeFlow(this.value)">
      <option value="">请选择流程类型</option>
      </select>
      </td>
    </tr>   
    <tr>
      <td class="TableContent">数据映射：</td>
      <td class="TableData">
      <div align="center">
            <span><select name="FLD_PARENT" id="FLD_PARENT" size="10">
            <optgroup label="业务模块字段" id="MODULE_STRING">
              
            </optgroup>
            </select></span>
            <span id="field_sub" style="display:none">
            <select name="fldChild" id="fldChild" size="10">
            <optgroup label="流程字段">
              
            </optgroup>
            </select>
            </span>
            <br />
            <input type="button" class="BigButtonC" value="添加业务模块-流程映射" onClick="map_relation()" />
        </div>
      </td>
    </tr>   
    <tr>
      <td class="TableContent">数据映射关系：</td>
      <td class="TableData">
      <div id="relationIn">
      
      </div>
        <input type="hidden" name="mapIn" id="mapIn" value=""/>
        <input type="hidden" name="mapName" id="mapName" value="" />
      </td>
    </tr>
    <tr>
      <td class="TableData" colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" onClick="checkForm()">
        <input type="button" onClick="window.close()" class="BigButton" value="关闭">
      </td>
    </tr>
    </table>
