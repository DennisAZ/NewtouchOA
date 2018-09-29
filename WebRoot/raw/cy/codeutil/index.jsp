<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/raw/cy/codeutil/code.js" ></script>
<title>代码自动生成</title>
</head>
<body>
<div>代码自动生成</div>
  <div id = "pojo">
  <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>tableNos:</td>
    <td>
        <input type = "text" name = "tableNos" value = "">
    </td>
  </tr>
   <tr class="TableLine2">
    <td>tableNo:</td>
    <td>
        <input type = "text" name = "tableNos" value = "11114">
    </td>
  </tr>
    <tr class="TableLine1">
    <td>pojoPagckageName:</td>
    <td>
        <input type = "text" name = "pojoPagckageName" value = "test.cy.code">
    </td>
  </tr>
      <tr class="TableLine2">
    <td>pojoOutPath:</td>
    <td>
        <input type = "text" name = "pojoOutPath" value = "D:/project/yh/src/test/cy/code">
    </td>
  </tr>
       <tr class="TableLine1">
    <td>pojoTemplateName:</td>
    <td>
        <input type = "text" name = "pojoTemplateName" value = "db2JavaCode.vm">
    </td>
  </tr>
       <tr class="TableLine2">
    <td>pojoTemlateUrl:</td>
    <td>
        <input type = "text" name = "templateNameUrl" value = "D:/project/yh/templates/db">
    </td>
  </tr>
  </table>
  </div>
  
  
  <div id = "act">
   <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>actOutPath:</td>
    <td>
        <input type = "text" name = "actOutPath" value = "D:/project/yh/src/test/cy/code/act">
    </td>
  </tr>
   <tr class="TableLine2">
    <td>actPackageName:</td>
    <td>
        <input type = "text" name = "actPackageName" value = "test.cy.code.act">
    </td>
        <tr class="TableLine2">
    <td>actTemplateName:</td>
    <td>
        <input type = "text" name = "actTemplateName" value = "curdact.vm">
    </td>
  </tr>
       <tr class="TableLine1">
    <td>actTemlateUrl:</td>
    <td>
        <input type = "text" name = "actTemlateUrl" value = "D:/project/yh/templates/db/curd">
    </td>
  </tr>
       <tr class="TableLine2">
    <td>actFileNamePre</td>
    <td>
        <input type = "text" name = "actFileNamePre" value = "">
    </td>
  </tr>
  </table>
  </div>
  
  <input type="button" onclick="loadData('fieldTab','<%=contextPath%>/yh/rad/velocity/act/YHCodeUtilAct/showField.act','11114')" value="显示">
  <div id = "curd">
    <table id = "fieldTab">
      <tr class = "TableHeader ">
        <th>字段名</th><th>显示名称</th><th>显示宽度</th><th>是否显示</th><th>是否必填</th>
      </tr>
    </table>
       <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>indexTemplateName:</td>
    <td>
        <input type = "text" name = "indexTemplateName" value = "index.vm">
    </td>
  </tr>
   <tr class="TableLine2">
    <td>indexTitle:</td>
    <td>
        <input type = "text" name = "indexTitle" value = "标记">
    </td>
        <tr class="TableLine2">
    <td>indexFileName:</td>
    <td>
        <input type = "text" name = "indexFileName" value = "index">
    </td>
  </tr>
       <tr class="TableLine1">
    <td>pageOutPath:</td>
    <td>
        <input type = "text" name = "pageOutPath" value = "D:/project/yh/webroot/yh/test/code">
    </td>
  </tr>
       <tr class="TableLine2">
    <td>pageUrl:</td>
    <td>
        <input type = "text" name = "pageUrl" value = "/test/code">
    </td>
  </tr>
     <tr class="TableLine1">
    <td>inputFileName:</td>
    <td>
        <input type = "text" name = "inputFileName" value = "input">
    </td>
  </tr>
  
       <tr class="TableLine2">
    <td>listFileName:</td>
    <td>
        <input type = "text" name = "listFileName" value = "list">
    </td>
  </tr>
     <tr class="TableLine1">
    <td>listTemplateName:</td>
    <td>
        <input type = "text" name = "listTemplateName" value = "list.vm">
    </td>
  </tr>
       <tr class="TableLine2">
    <td>pageTemlateUrl:</td>
    <td>
        <input type = "text" name = "pageTemlateUrl" value = "D:/project/yh/templates/curd">
    </td>
  </tr>
      <tr class="TableLine1">
    <td>inputTemplateName:</td>
    <td>
        <input type = "text" name = "inputTemplateName" value = "input.vm">
    </td>
  </tr>
  </table>
    request.put("listFields", list2);
    request.put("inputFields", list);
  </div>
</body>
</html>