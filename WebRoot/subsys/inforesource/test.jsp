<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>CheckTree v 0.2 by JJ Geewax</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="metadataSelect.js" ></script>
<script>

function tomanage(){
  var url = contextPath + "/yh/subsys/inforesouce/act/YHMateShowAct/toManage.act";
   window.open(url);
}

function test(){
  alert("来自父元素的测试");
}


</script>

<link type="text/css" rel="stylesheet" href="img/checktree.css" />


</head>
<body>


<h2>树测试</h2>
   <input type="button" value="test" onclick="tomanage();"/>
   <table>
   	  <tr>      
      <td class="TableData">
        <input type="hidden" id="mate" name="mateId" value="">
        <textarea cols=40 id="mateDesc" name="mateDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectMate(['mate','mateDesc']);">添加</a><!--
       <a href="javascript:;" class="orgClear" onClick="ClearUser('toId','toName')">清空</a>
       &nbsp;&nbsp;&nbsp;&nbsp;
      --></td>
    </tr>
   </table>
</body>
</html>
