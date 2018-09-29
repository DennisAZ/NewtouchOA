<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String item = request.getParameter("item");
String callback = request.getParameter("callback");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>选择印章</title>
<meta http-equiv="Content-Type" content="text/html">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script Language="JavaScript">
var item = "<%=item%>";
var callback = "<%=callback%>";
var parentWindowObj = window.dialogArguments;
function click_seal(ID) {
  if (callback) {
    parentWindowObj.<%=callback%>('<%=item%>',ID);
  }
  close();
}
function doInit() {
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowFormViewAct/getAllSeal.act";
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var seals = json.rtData;
    if (seals.length > 0 ) { 
      for (var i = 0 ; i < seals.length ;i ++) {
        var seal = seals[i];
        addRow(seal);
      }
      $('hasData').show();
    } else {
      $('noData').show();
    }
  } 
}
function addRow(seal) {
  var tr = new Element("tr", {'class':'TableData'});
  $('sealList').appendChild(tr);
  
  var td = new Element("td" , {'class': 'menulines'});
  td.align = 'center';
  td.style.cursor = 'hand';
  td.onclick = function() {
    click_seal(seal.id);
  }
  td.update(seal.sealId);
  tr.appendChild(td);
  td = new Element("td" , {'class': 'menulines'});
  td.align = 'center';
  td.style.cursor = 'hand';
  td.onclick = function() {
    click_seal(seal.id);
  }
  
  td.update(seal.sealName);
  tr.appendChild(td);
}
</script>
</head>

<body  topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/system.gif" align="absmiddle"><span class="big3"> 我的印章</span>&nbsp;
    </td>
  </tr>
</table>

<div id="hasData" style="display:none">
<table class="TableBlock" width="100%">
<tr class="TableHeader">
  <td align="center"><b>印章ID</b></td>
  <td align="center"><b>印章名称</b></td>
</tr>

<tbody id="sealList"></tbody>
</table>
</div>
<div id="noData"  align=center style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">无任何印章权限！</td>
        </tr>
    </tbody>
</table>

</div>
</body>
</html>