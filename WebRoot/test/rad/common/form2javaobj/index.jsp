<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>yh</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script language="JavaScript">
/**
 * 单个简单对象
 */
function testSingle() {
  var param = {dtoClass: "test.core.dto.YHInnerBean11",
      name: "yzq",
      id: "yzq"};
  param = $H(param).toQueryString();
  var rtJson = getJsonRs("<%=contextPath%>/test/core/act/YHTestFormBeanAct/testSingle.act", param);
  document.getElementById("outMsrg").innerHTML = rsText;
}
/**
 * 主从对象（一个从对象）
 */
function testMulDetl() {
  var param = {dtoClass: "test.core.dto.YHInnerBean1",
      name: "bean1Name",
      id: "bean1Id",
      bean11Class: "test.core.dto.YHInnerBean11",
      bean11Cnt: 2,
      name_bean11_0: "name_bean11_0",
      id_bean11_0: "id_bean11_0",
      name_bean11_1: "name_bean11_1",
      id_bean11_1: "id_bean11_1",
      bean12Class: "test.core.dto.YHInnerBean12",
      bean12Cnt: 2,
      no_bean12_0: "no_bean12_0",
      desc_bean12_0: "desc_bean12_0",
      no_bean12_1: "no_bean12_1",
      desc_bean12_1: "desc_bean12_1"};
  param = $H(param).toQueryString();
  var rtJson = getJsonRs("<%=contextPath%>/test/core/act/YHTestFormBeanAct/testMulDetl.act", param);
  document.getElementById("outMsrg").innerHTML = rsText;
}
/**
 * 递归情况
 */
function testRecurve() {
  var param = {dtoClass: "test.core.dto.YHTestBean",
      field1: 12,
      field2: 1223,
      field3: 5.67,
      field4: "您好",
      field5: "2009-01-02",
      field6: "2009-01-03",
      bean1Class: "test.core.dto.YHInnerBean1",
      bean1Cnt: 2,
      name_bean1_0: "bean1Name",
      id_bean1_0: "bean1Id",
      bean11Class: "test.core.dto.YHInnerBean11",
      bean11_bean1_0Cnt: 2,
      name_bean1_0_bean11_0: "name_bean11_0",
      id_bean1_0_bean11_0: "id_bean11_0",
      name_bean1_0_bean11_1: "name_bean11_1",
      id_bean1_0_bean11_1: "id_bean11_1",
      bean12Class: "test.core.dto.YHInnerBean12",
      bean12_bean1_0Cnt: 2,
      no_bean1_0_bean12_0: "no_bean12_0",
      desc_bean1_0_bean12_0: "desc_bean12_0",
      no_bean1_0_bean12_1: "no_bean12_1",
      desc_bean1_0_bean12_1: "desc_bean12_1",
      name_bean1_1: ">>bean11Name",
      id_bean1_1: ">>bean11Id",
      bean11_bean1_1Cnt: 2,
      name_bean1_1_bean11_0: ">>name_bean11_0",
      id_bean1_1_bean11_0: ">>id_bean11_0",
      name_bean1_1_bean11_1: ">>name_bean11_1",
      id_bean1_1_bean11_1: ">>id_bean11_1",
      bean12_bean1_1Cnt: 2,
      no_bean1_1_bean12_0: ">>no_bean12_0",
      desc_bean1_1_bean12_0: ">>desc_bean12_0",
      no_bean1_1_bean12_1: ">>no_bean12_1",
      desc_bean1_1_bean12_1: ">>desc_bean12_1"};
  param = $H(param).toQueryString();
  var rtJson = getJsonRs("<%=contextPath%>/test/core/act/YHTestFormBeanAct/testRecurve.act", param);
  document.getElementById("outMsrg").innerHTML = rsText;
}
</script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >
<input type="button" onclick="testSingle();" value="单个对象">
<input type="button" onclick="testMulDetl();" value="主从对象（多个从对象）">
<input type="button" onclick="testRecurve();" value="递归嵌套（3个层次）">
<textarea id="outMsrg" rows="20" cols="70"></textarea>
</html>