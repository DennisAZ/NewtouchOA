<%@ page language="java" contentType="text/html;  charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>性能优化</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
</head>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" >
function crateIndex(){
  $('tip').update("创建中.....");
  var url = contextPath + "/yh/core/funcs/youhua/act/YHYouHuaAct/createIndex.act";
  var json = getJsonRs(url);
  if (json.rtState == '0') {
    $('tip').update("创建完毕");
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="">

<div align=center>
   <table class="MessageBox" align="center" width="210">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt"><span id="tip" style="color:red">创建索引之前请先备份数据库！</span></div>
    </td>
  </tr>
</table>
<input type="button" class="BigButton" value="创建索引" onclick="crateIndex()">
</div>
 </body>
</html>
