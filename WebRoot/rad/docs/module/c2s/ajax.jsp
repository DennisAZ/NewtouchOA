<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<style type="text/css"><!--
body {
  margin: 5px 5px 5px 5px;
  background-color: #ffffff;
}
/* ========== Text Styles ========== */
hr { color: #000000}
#title {
  font-size: 16pt;
  font-family: 宋体;
  font-style: normal;
  font-weight: bold;
  color: #000000;
}
#contentTable
{
 font-size: 10pt;
 font-style: normal;
 font-weight: normal;
 color: #000000;
 background-color: #888888;
 text-decoration: none;
}
#contentTable th {
  background-color: #CCCCCC;
  text-decoration: none;
}
#contentTable td {
  background-color: #FFFFFF;
}
--></style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
  function doSubmit(){
    var url = contextPath + "/yh/rad/docs/iframe/c2s/YHClient2ServerDemo/ajax1.act";
    var rtJson  = getJsonRs(url);
    if(rtJson.rtState == '0'){
      alert(rtJson.rtData);
    }else{
      alert(rtJson.rtMsrg);
    }
  }

  function doSubmit2(){
    var url = contextPath + "/yh/rad/docs/iframe/c2s/YHClient2ServerDemo/ajax2.act";
    var rtJson  = getTextRs(url);
    alert(rtJson);
  }
</script>
</head>
<body>
<button onclick="doSubmit()" >ajax方式1测试</button>
<button onclick="doSubmit2()" >ajax方式2测试</button><br>
注意:ajax方式1与ajax方式1的区别主要在服务端的写法不一样！<br><br>
<table id="contentTable" border="0" cellpadding="3" cellspacing="1">
  <tr><td>ajax请求方法</td><td>描述</td></tr>
  
  <tr><td>getTextRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>同步方式，以文本形式从服务器取得响应<br>
   @formId            表单对象的name或者ID<br>
 @actionUrl         服务器端活动路径<br>
 @callBackFunc      响应处理函数，函数的接口是 callBackFunc()<br>
 @isClearKiloSplit  是否处理千分符号<br>
  </td></tr>
  <tr><td>getBooleanRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td> 同步方式，以布尔值形式从服务器取得响应<br>
    @formId            表单对象的name或者ID<br>
 @actionUrl         服务器端活动路径<br>
 @callBackFunc      响应处理函数，函数的接口是 callBackFunc()<br>
 @isClearKiloSplit  是否处理千分符号<br>
  </td></tr>
  <tr><td>getIntRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td> 同步方式，以整型形式从服务器取得响应<br>
   @formId            表单对象的name或者ID<br>
 @actionUrl         服务器端活动路径<br>
 @callBackFunc      响应处理函数，函数的接口是 callBackFunc()<br>
 @isClearKiloSplit  是否处理千分符号<br>
  </td></tr>
  <tr><td>getFloatRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>同步方式，以浮点型形式从服务器取得响应<br>
   @formId            表单对象的name或者ID<br>
 @actionUrl         服务器端活动路径<br>
 @callBackFunc      响应处理函数，函数的接口是 callBackFunc()<br>
 @isClearKiloSplit  是否处理千分符号<br>
  </td></tr>
  <tr><td>getJsonRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td> 同步方式，以JSON形式从服务器取得响应<br>
   @formId            表单对象的name或者ID<br>
 @actionUrl         服务器端活动路径<br>
 @callBackFunc      响应处理函数，函数的接口是 callBackFunc()<br>
 @isClearKiloSplit  是否处理千分符号<br>
  </td></tr>
  <tr><td>getXmlRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>同步方式，以xml形式从服务器取得响应<br> @formId            表单对象的name或者ID
 <br> @actionUrl         服务器端活动路径
 <br> @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 <br> @isClearKiloSplit  是否处理千分符号
  </td></tr>
    
  <tr><td>getTextRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>异步方式，以文本形式从服务器取得响应 <br> @formId            表单对象的name或者ID
 <br> @actionUrl         服务器端活动路径
 <br> @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 <br> @isClearKiloSplit  是否处理千分符号
  </td></tr>
  <tr><td>getBooleanRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>异步方式，以布尔值形式从服务器取得响应<br> @formId            表单对象的name或者ID
 <br> @actionUrl         服务器端活动路径
 <br> @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 <br> @isClearKiloSplit  是否处理千分符号
  </td></tr>
  <tr><td>getIntRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit) </td><td>异步方式，以整型形式从服务器取得响应 <br> @formId            表单对象的name或者ID
 <br> @actionUrl         服务器端活动路径
 <br> @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 <br> @isClearKiloSplit  是否处理千分符号
  </td></tr>
  <tr><td>getFloatRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>异步方式，以浮点型形式从服务器取得响应<br> @formId            表单对象的name或者ID
 <br> @actionUrl         服务器端活动路径
 <br> @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 <br> @isClearKiloSplit  是否处理千分符号
  </td></tr>
  <tr><td>getJsonRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>异步方式，以JSON形式从服务器取得响应 <br> @formId            表单对象的name或者ID
 <br> @actionUrl         服务器端活动路径
 <br> @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 <br> @isClearKiloSplit  是否处理千分符号</td></tr>
  <tr><td>getXmlRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit)</td><td>异步方式，以xml形式从服务器取得响应 <br> @formId            表单对象的name或者ID
 <br> @actionUrl         服务器端活动路径
 <br> @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 <br> @isClearKiloSplit  是否处理千分符号</td></tr>
  
</table>
</body>
</html>