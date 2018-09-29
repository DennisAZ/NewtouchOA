<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>任务查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<style>
</style>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var pageMgr;
function doInit(){
  var requestURL = "<%=contextPath%>/yh/core/esb/server/demo/act/ESBDemoAct/receiveFilePage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type:"text", name:"fileName", text:"文件名", width:200,align:'center'},
       {type:"text", name:"fileLength", text:"文件大小", width:250,align:'center'},
       {type:"text", name:"fileTime", text:"文件最后修改时间", width:200,align:'center'}
       ]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

</script>
</head>

<body onload="doInit()">

<table border="0" width="" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/system.gif" align="absmiddle"><span class="big3"> 本地缓存目录文件</span><br>

    </td>
  </tr>
</table>
<br>
<div id="listDiv">
</div>
</body>
</html>
