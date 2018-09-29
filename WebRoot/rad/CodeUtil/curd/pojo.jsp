<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div>
  <table id = "pojo" cellscpacing="1" cellpadding="3" width="450"><!--
  <tr class="TableLine1">
    <td>工程:</td>
    <td>
        <input type = "text" id="project" value = "" class="SmallInput">
    </td>
  </tr>
   --><tr class="TableLine2">
    <td>数据库表编码:</td>
    <td>
        <input type = "text" name = "tableNo" id = "pre3" class="SmallInput">
        <input type = "button" value = "选择..." onclick = "onSelectTable()" class="ArrowButton">
    </td>
  </tr>
    <tr class="TableLine1">
    <td>实体类包:</td>
    <td>
        <input type = "text" name = "pojoPagckageName" value = "test.cy.code" class="SmallInput">
        <!-- 
        <input type = "button" value = "选择..." onclick = "onSelectPackage('projectSrcUrl')"> 
        -->
    </td>
  </tr>
      <tr class="TableLine2">
    <td>实体类输出地址:</td>
    <td>
        <input type = "text" name = "pojoOutPath" value = "D:/project/yh/src/test/cy/code" class="SmallInput">
    </td>
  </tr>
       <tr class="TableLine1">
    <td>实体类的模板名称:</td>
    <td>
        <input type = "text"   name = "pojoTemplateName" value = "db2JavaCode.vm" class="SmallInput">
    </td>
  </tr>
       <tr class="TableLine2">
    <td>实体类的模板地址:</td>
    <td>
        <input type = "text" name = "pojoTemlateUrl" value = "D:/project/yh/templates/db" class="SmallInput">
   
    </td>
  </tr>
  </table>
  <input type="button" value="生成代码" onclick="doSubmit()" class="BigButton">
  </div>
