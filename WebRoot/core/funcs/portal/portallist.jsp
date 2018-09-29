<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>门户管理</title>
<link  rel="stylesheet"  href  ="<%=cssPath  %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var requestUrl = contextPath + "/yh/core/funcs/portal/act/YHPortalAct"
function doInit(){
  var url = requestUrl + "/listPortal.act";
  var json = getJsonRs(url); 
  if (json.rtState == "0") {
    var modList = json.rtData;
    if (modList.length > 0) {
      $("modListTable").show();
      for( var i = 0 ;i < modList.length ; i++) {
        var mod = modList[i];
        addModTr(mod , i);
      }
    }else {
      $('noData').show();
    }
  } 
}
function addModTr(mod , i) {
  var className = (i % 2 == 0) ? "TableLine1" :"TableLine2" ;
  var tr = new Element("tr",{"class":className , "id":"tr-" + mod.id});
  $('modListTbody').appendChild(tr);
  var td = new Element("td");
  tr.appendChild(td);
  td.update(mod.name);
  td = new Element("td");
  tr.appendChild(td);
  var op = "&nbsp;<a href='javascript:setPriv("+mod.id+" )'>授权</a>&nbsp;&nbsp;"
    + "<a href='javascript:editPortal("+mod.id+" , \""+mod.name+"\")'>编辑</a>&nbsp;&nbsp;"
    + "<a href='javascript:designPortal("+mod.id+")'>设计</a>&nbsp;&nbsp;"
    +"<a href='javascript:delPortal("+mod.id+")'>删除</a>"
  td.update(op);
}
function editPortal(id , name) {
  location.href ="editPortal.jsp?id=" + id + "&name=" + encodeURIComponent(name);
}
function designPortal(id) {
  window.open(contextPath + "/core/frame/webos/design/index.jsp?type=edit&id=" + id);
}
function setPriv(id) {
  location.href = "setPortalPriv.jsp?id=" + id;
}
function delPortal(id) {
  if (!confirm("确认删除此门户吗？")){
    return;
  }
  var url = requestUrl + "/delPortal.act";
  var json = getJsonRs(url , "id=" + id); 
  if (json.rtState == "0") {
    var tr = $("tr-" + id);
    $('modListTbody').removeChild(tr);
  }
}

</script>
</head>
<body onLoad="doInit()">
<table  border="0" width="90%" cellspacing="0" cellpadding="3" class="small" style="margin:5px 0px;">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/control_theme.gif" align="abstop"/><span class="big3"> 门户管理</span><br>
    </td>
  </tr>
</table>
<br/>
<br/>
<table  id="modListTable"  style="width:600px;margin: 0 auto;display:none" class="TableBlock">
      <tr>
        <td class="TableHeader" colspan="">
        门户名称
        </td>
        <td class="TableHeader" colspan="">
        操作
        </td>
      </tr>
     <tbody id="modListTbody">
    </tbody>
    <tr class="TableControl">
        <td colspan="2" align="center">
          <input type="button" class="SmallButton" value="新增" onclick="location.href='editPortal.jsp'"/>
        </td>
      </tr>
  </table>
  <div id="noData" align="center" style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td class="msg info">尚未定义门户！</td>
        </tr>
    </tbody>
</table>
<div align="center"><input type="button" class="BigButton" value="新增" onclick="location.href='editPortal.jsp'"/></div>
</div>


</body>
</html>