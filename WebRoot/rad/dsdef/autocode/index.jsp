<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css"
  type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css"
  type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
function doInit(){
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2AutoCode/parserTemp.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    var uri = data.uri;
    var temps = data.templates;
    $('uri').value = uri;
    for(var i = 0 ; i < temps.length ; i ++){
      var temp = temps[i];
      var index = 1;
      if(i%2 == 0){
        index = 2;
       }
      var ck = "";
      if(i==0){
         ck = "checked";
       }
      var html = "<tr class=\"TableLine" + index + "\" style=\"text-align:left\"> " 
        + " <td><input type=\"radio\" name=\"tempName\" value=\"" + temp.name + "\"" + ck + "></input></td> "
        + " <td>" +  temp.name + "</td> "
        + " <td>" +  temp.desc + "</td> "
        + " </tr>";
      $('content').insert(html,"bottom");
    }
  }
}

function doSubmit(){
  var param = $("form1").serialize();
  var url = contextPath + "/rad/dsdef/autocode/templates.jsp?" + param;
  location = url;
}
</script>
<title>代码自动生成</title>
</head>
<body onload="doInit();">
<center>
请选择您要使用的模板:<br/>
<br/>
<form id="form1" name="form1">
<div class="tableClass" >
<table>
  <tr class="TableHeader" style="text-align:left" >
    <td width="80px">选择</td>
    <td width="200px">模板名称</td>
    <td width="400px">模板描述</td>
  </tr>
  <tbody id="content">
  </tbody>
</table>
</div>
<input value="" id="uri" type="hidden" name="uri">
<input type="button" onclick="doSubmit()" value="确定"></input>
</form>
</center>
</body>
</html>