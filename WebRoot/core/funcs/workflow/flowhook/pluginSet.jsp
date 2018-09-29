<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<table width="95%" class="TableBlock" align="center">
    <tr id="pluginDiv">
      <td nowrap class="TableContent">插件程序名称：</td>
      <td class="TableData">
        <input onClick="javascript:selPlugin()" type="text" name="plugin" id="plugin" size="20" maxlength="100" class="SmallStatic" value="" readonly> <a href="javascript:;" onClick="javascript:selPlugin()">添加</a>
        <a href="javascript:;" onClick="javascript:remove_plugin()">清空</a>
        一般无需设置
      </td>
    </tr>
    <tr >
      <td class="TableData" colSpan=2><b>
        字段 <select id="formItemSelect">
        
       </select>
        条件 <select id="conditionSelect" onchange="changeCondition(this.value);">
        <option value="=">等于</option>
        <option value="<>">不等于</option>
             <option value=">">大于</option>
             <option value="<">小于</option>
             <option value=">=">大于等于</option>
             <option value="<=">小于等于</option>
             <option value="include">包含</option>
             <option value="exclude">不包含</option>
        </select>
        <span id="divCheck" style="display:inline"><input type="checkbox" name="checkType" id="checkType" onclick="changeType(this);"><label for="checkType">类型判断</label>&nbsp;</span>
        <span id="divType" style="display:none">
        &nbsp;类型 <select id="itemType" class="SmallSelect">
             <option value="数值">数值</option>
             <option value="日期">日期</option>
             <option value="日期+时间">日期+时间</option>
        	  </select>
        </span>
        <span id="divValue" style="display:inline">
               值 <input type="text" class="SmallInput" id="itemValue" size=20></span>
        <div align="center" style="margin:5px 0 5px 0">
        <input type="button" class="BigButtonC" value="添加到条件列表" onclick="addCondition(1)">&nbsp;&nbsp;
        </b>
      </td>
    </tr>
    <tr  class="TableHeader">
      <td height=30 colSpan=2><b> 转入条件列表</b></td>
    </tr>
    <tr>
      <td style="background-color:#ffffff" colSpan=2>
      	<b>合理设定转入条件，可形成流程的条件分支，但数据满足转入条件，才可转入本步骤</b>
      	<table id="prcsInTab" width=100% align="center"  border=0>
  	          <tr class="TableHeader">
  	            <td nowrap align="center" width=50>编号</td>
  	            <td nowrap align="center">条件描述</td>
  	            <td nowrap align="center" width=100>操作</td>
  	          </tr>
        </table>
        <b>转入条件公式(条件与逻辑运算符之间需空格，如[1] AND [2])</b><br>
        <input type="text" class="BigInput" size=71 name="prcsInSet" id="prcsInSet" value=""><br>
      </td>
    </tr>
    <tr>
      <td class="TableData" colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" onClick="checkForm()">
        <input type="button" onClick="window.close()" class="BigButton" value="关闭">
      </td>
    </tr>
    </table>
<input type="text" id="edit" onblur="updateEdit(this);"  class="SmallInput" size="50" style="display:none;" />
<input type="hidden" name="prcsIn" id="prcsIn" value="">