<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String isList = request.getParameter("isList");
if(isList == null ){
  isList = "";
}
%>
<table id="other" width="95%"  class=TableList align="center">
  <tr class="TableLine1">
    <td  class="TableContent" width=100px>办理时限：</td>
    <td  class="TableData"  colspan="3">
      <input  onkeyup="value=value.replace(/[^\d-.]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d-.]/g,''))"   type="text" id="timeOut" name="timeOut" size="20" maxlength="100" class="SmallInput" value="">
                 小时，表示接收工作后办理的时限，为空表示不限时<br/>
    </td>
  </tr>
  
  <tr class="TableLine2">
    <td  class="TableContent" width=100px>超时计算方法：</td>
    <td  class="TableData"  colspan="3">
      <input type="radio" name="timeOutTypeDesc"  value="0" id="timeOutType1" checked><label for="timeouttype1">优先按接收时间计算</label>
      <input type="radio" name="timeOutTypeDesc" value="1" id="timeOutType2" ><label for="timeouttype2">按上一步转交时间计算</label>  
      <input type="hidden" id="timeOutType" name="timeOutType" value="">
    </td>
  </tr>
  
  <tr class="TableLine1">
    <td class="TableContent" width=100px>是否排除双休日：</td>
    <td  class="TableData"  colspan="3">
      <input type="checkbox" name="timeExcept1" id="timeExcept1" value="0" ><label for="timeexcept1">周六</label>
      <input type="checkbox" name="timeExcept2" id="timeExcept2" value="1"><label for="timeexcept2">周日</label>     	      
    </td>
  </tr>
  
  <tr class="TableLine2">
    <td  class="TableContent" width=100px>是否启用呈批单：</td>
    <td  class="TableData" colspan="3">
      <b>选择呈批单：</b>
      <select id="dispAip" name="dispAip" >
        <option value="0" selected>无</option>
        
      </select><br/>
                说明:呈批单是既能提取电子表单数据同时可以直接进行手写签批的版式文件格式。



                若使用此功能，需先到流程设计-属性设置-打印模板中设计呈批单   	      
    </td>
  </tr>
  <tr class="TableLine2"  style="display:none">
    <td  class="TableContent" width=100px>是否强制归档：</td>
    <td  class="TableData">
     <select id="extend" name="extend">
     <option value="" selected>否</option>
     <option value="1">是</option>
     </select>
    </td>
    <td  class="TableContent" width=100px>是否分配文号：</td>
    <td  class="TableData" style="display:">
     <select id="extend1" name="extend1">
     <option value="" selected>否</option>
     <option value="1">是</option>
     </select>
    </td>
  </tr>
  <tr class="TableLine1">
    <td  class="TableContent" width=100px>插件程序名称：</td>
    <td  class="TableData"  colspan="3">
      <input type="text" id="plugin" name="plugin" size="20" maxlength="100" onclick="selPlugin()" class="SmallStatic" value="" readonly>
      <a href="javascript:void(0)" onclick="selPlugin()">添加</a>
      <a href="javascript:void(0)" onclick="$('plugin').value=''">清空</a>
               一般无需设置
     &nbsp;<a href="javascript:;" title="如没有软件开发商特殊定制开发的的插件程序，请勿填写。插件程序为class文件，例如：MyPlugin.class，放置于/WEB-INF/classes/yh/plugins/workflow下。插件程序将在本步骤执行完毕后被自动调用执行。">说明</a>     
    </td>
  </tr>
  <tr class=TableControl><td colspan=4><div align="center">
  <input type="button"  value="保存" class="BigButton" onclick="commit()">&nbsp;&nbsp;&nbsp;&nbsp;
  <% if("".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="parent.location.reload();">
  <% } %>
</div></td></tr>
</table>
