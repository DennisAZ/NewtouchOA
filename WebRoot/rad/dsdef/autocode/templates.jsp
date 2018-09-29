<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String tempName = request.getParameter("tempName");
%>
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
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2AutoCode/parserTempXml.act?tempName=<%=tempName%>" ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    var params = data.params;
    for(var i = 0 ; i < params.length ; i ++){
      var param = params[i];
      var index = 1;
      if(i%2 == 0){
        index = 2;
       }
      var paramValueSet = "";
      if(param.type == "String"){
        paramValueSet = "<input type=\"text\" id=\"" + param.name + "\" name=\"" + param.name + "\">";
      }else if(param.type == "Grid"){
        paramValueSet = getGridSetHtml();
      }
      var html = "<tr class=\"TableLine" + index + "\" style=\"text-align:left\"> " 
        + " <td>" +  param.name + "</td> "
        + " <td>" + paramValueSet + "</td> "
        + " <td>" +  param.desc + "</td> "
        + " </tr>";
      $('content').insert(html,"bottom");
    }
    if($('tempPoj')){
      $('tempPoj').value = "<%=tempName%>";
    }
  }
}
function getGridSetHtml(){
  var html = "<input type=\"button\" value=\"刷新数据\" onclick=\"refrcahData()\" class=\"BigButton\">"
    + "<table><tr><td><table id = \"fieldTab\"><tr class =\"TableHeader \">"
    + "<th>字段名</th><th>显示名称</th><th>显示宽度</th><th>是否显示</th><th>是否必填</th><th>显示控件类型</th>"
    + "</tr></table></td></tr></table><input type=\"hidden\" name=\"filedsVal\" id=\"filedsVal\">"
    + "<input type=\"button\" value=\"确认字段信息\" onclick=\"inputs('filedsVal','fieldTab')\" class=\"BigButton\">";
}
function doSubmit(){
  var param = $("form1").serialize();
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2AutoCode/code2java.act";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == "0"){
    alert(rtJson.rtMsrg);
  }else{
    alert(rtJson.rtMsrg);
   }
}

function refrcahData() {
  var tabNo =  $('tempTabNo').value;
  var tab = $('fieldTab');
  var index = tab.rows.length;
  var j = 0;
  for(var i = 1; i < index ; i++){
    tab.deleteRow(1);
    //j = i;
   }
  //alert("j : " + j);
  var keys = selectHashMap.keys();
  for(var i = 0 ; i < keys.length ; i++){
    selectHashMap.unset(keys[i]);
    selectShowHashMap.unset(keys[i]);
  } 
  var rkeys = radioHashMap.keys();
  for(var i = 0 ; i < keys.length ; i++){
    radioHashMap.unset(keys[i]);
    radioShowHashMap.unset(keys[i]);
  } 
  loadData('fieldTab',pagesLoadUrl,tabNo);
}
</script>
<title>模板设置页面</title>
</head>
<body onload="doInit()">
<center>
请设置模板参数:<br/>
<br/>
<form id="form1" name="form1">
<div class="tableClass" >
<table>
  <tr class="TableHeader" style="text-align:left" >
    <td width="80px">参数名称</td>
    <td width="200px">参数值</td>
    <td width="400px">参数描述</td>
  </tr>
  <tbody id="content">
  </tbody>
</table>
</div>
<input type="button" onclick="doSubmit()" value="确定"></input>
</form>
</center>
</body>
</html>