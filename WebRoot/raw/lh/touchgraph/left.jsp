<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主题词</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var nodes = [{value:"文种"},{value:"业务行为"},{value:"姓名"},{value:"地名"},{value:"组织机构"}];
function doInit() {
  for (var i = 0 ;i < nodes.length;i ++) {
    var node = nodes[i];
    addRow(node , i);
  }
}
function addRow(node , i) {
  var className = "TableLine1";
  if ( i % 2 == 0) {
    calssName = "TableLine2";
  } 
  var tr = new Element("tr",{"class": className});
  var td = new Element("td");
  tr.appendChild(td);
  $("list").appendChild(tr);
  var checkbox = new Element("input");
  checkbox.type = "checkbox";
  checkbox.value = node.value;
  td.style.cursor = "hand";
  td.onmouseover = function() {
    td.style.color = "blue";
    td.style.background = "#ccc";
  }
  td.onmouseout = function() {
    td.style.color = "#000";
    td.style.background = "#fff";
  }
  td.onclick = function() {
    checkbox.checked = true;
    if (checkbox.checked) {
      setOtherDis(checkbox);
      getAllNodes(checkbox.value);
      
    }
  }
  td.appendChild(checkbox);
  td.insert(node.value);
}
function setOtherDis(selected) {
  var checkbox = document.getElementsByTagName("input");
  for (var i = 0 ;i < checkbox.length;i ++) {
    var c = checkbox[i];
    if (c != selected ) {
      c.checked = false;
    }
  }
}
function getAllNodes(subject) {
  var url = contextPath + "/yh/subsys/inforesouce/act/YHTouchGraphAct/getSubject.act?id=";
  var id = parent.main.centerNode.id;
  url = url + id + "&subject=" + encodeURIComponent(subject);
  parent.showTouchGrap(url 
      ,'clickFunTest2'
      ,''
      ,''
      ,'' , true , parent.main);
}
</script>
</head>
<body onload="doInit()">
<table class="TableBlock" width="100%" align="center">
<tr class="TableHeader" style="display:none"><td style="margin-left:5px;"><a href="javascript:parent.location.reload()" style="color:#fff">&lt;&lt; 主题词</a></td></tr>
<tr class="TableHeader"><td  style="margin-left:5px">请选择分类</td></tr>
   <tbody id="list">
     
   </tbody>
</table>
<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" align=center>
     <input type="button" class="SmallButtonW2" value="主题词" onclick="parent.location.reload();"/>
    </td>
  </tr>
</table>
</body>
</html>