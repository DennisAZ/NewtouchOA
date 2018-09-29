<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/yh/rad/grid/grid.css"/>

<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src = "/yh/rad/grid/grid.js"></script>
<title>ORM FOR MAP TEST</title>
</head>
<script type="text/javascript">
function testSing(){
  var url = "<%=contextPath%>/test/core/act/YHORMTestAct/testSingle.act";
  var rtjson = getJsonRs(url);
  $('testSing').value = rtjson.data;
}
function testCom(){
  var url = "<%=contextPath%>/test/core/act/YHORMTestAct/testMulDetl.act";
  var rtjson = getJsonRs(url);
  $('testCom').value = rtjson.data;
} 
</script>
<body onload = "loads()">
	<center>
		<font  style="font: '宋体';font-size: 18;font-style:normal;font-weight: bold">orm数据加载测试(表名：DS_TABLE ID:144)</font>
		  <table>
		    <tr>
		      <td> <input type="button" onclick="testSing();" value="单表数据加载测试"> </td>
		      <td>
		        <textarea rows="10" cols="40" id="testSing"></textarea>
		      </td>
		    </tr>
		    <tr>
		      <td> <input type="button" onclick="testCom();" value="主从表数据加载测试"> </td>
		      <td>
		        <textarea rows="10" cols="40" id="testCom"></textarea>
		      </td>
		    </tr>
		  </table>
		</center>
</body>
</html>