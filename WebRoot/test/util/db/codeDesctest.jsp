<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js" ></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js" ></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="/yh/rad/codeSel/codeSel.js" ></script>
<script type="text/Javascript" src="/yh/rad/grid/grid.js" ></script>

<title>codeDescTest</title>
<script type="text/javascript">
function onSubmit(){
  //var url = "<%=contextPath%>/yh/core/act/YHCode2DescAct/code2Desc.act";
  //var rtJson = getJsonRs(url, "queryParam=7,8,9;oa_department,SEQ_ID,DEPT_NAME");
  //document.getElementById("codedesc").value = rsText;
  
  
  bindDesc([{cntrlId:"code", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}
    ,{cntrlId:"code2", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}
    ,{cntrlId:"code3", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
}
</script>
</head>
<body>
<input type="hidden" name="code" id="code" value="7,8,9"></input>
<textarea rows="15" cols="20" id="codeDesc" ></textarea>
<input type="hidden" name="code2" id="code2" value="7,8,9"></input>
<textarea rows="15" cols="20" id="code2Desc" ></textarea>
<input type="hidden" name="code3" id="code3" value="7,8,9"></input>
<textarea rows="15" cols="20" id="code3Desc" ></textarea>
<input type="button" value="code转换测试" onclick="onSubmit()">
</body>
</html>