<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><div >
   <table id = "curd" cellscpacing="1" cellpadding="3" width="450"> 
    <tr class="TableLine1">
      <td>页面显示标记:</td>
      <td>
        <input type = "text" name = "indexTitle" value = "标记" class="SmallInput">
      </td>
    <tr class="TableLine2">
      <td>主页面文件名:</td>
      <td>
        <input type = "text"  name = "indexFileName" value = "index" class="SmallInput">
      </td>
    </tr>

     <tr class="TableLine1">
      <td>主页面模板名</td>
      <td>
        <input type = "text"  name = "indexTemplateName" value = "indexNew.vm" class="SmallInput">
      </td>
    </tr>
         <tr class="TableLine2">
    <td>编辑页面文件名:</td>
    <td>
        <input type = "text"  name = "inputFileName" value = "input" class="SmallInput">
    </td>
    </tr>
   <tr class="TableLine1">
    <td>编辑页面模板名称:</td>
    <td>
        <input type = "text"  name = "inputTemplateName" value = "inputNew.vm" class="SmallInput">
    </td>
   </tr>
   <tr class="TableLine2">
    <td>展示页面名称:</td>
    <td>
        <input type = "text"  name = "listFileName"  value = "list" class="SmallInput">
    </td>
   </tr>
   <tr class="TableLine1">
    <td>展示页面模板名称:</td>
    <td>
        <input type = "text"  name = "listTemplateName" value = "listNew.vm" class="SmallInput">
    </td>
  </tr>
  <tr class="TableLine2">
    <td>页面输出地址:</td>
    <td>
      <input type = "text" name = "pageOutPath" value = "D:/project/yh/webroot/yh/test/code" class="SmallInput">
    </td>
  </tr>
  <tr class="TableLine1">
    <td>页面导航地址:</td>
    <td>
        <input type = "text" name = "pageUrl" value = "/test/code" class="SmallInput">
    </td>
  </tr>

  <tr class="TableLine2">
    <td>页面模板地址:</td>
    <td>
        <input type = "text" name = "pageTemlateUrl" value = "D:/project/yh/templates/curd" class="SmallInput">
    </td>
  </tr>

  </table>
  </div>