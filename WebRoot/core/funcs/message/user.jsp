<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String id = request.getParameter("id");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
  var id = "<%=id%>";
  function doInit() {  
    var url = "<%=contextPath%>/yh/core/funcs/sms/act/YHSmsAct/listDeptUser.act";
    var rtJson = getJsonRs(url, "id=" + id);
    if (rtJson.rtState == "0") {
       var table = document.getElementById("tbody");
       for(var i = 0; i < rtJson.rtData.length; i++) {
         var show1tr = document.createElement("tr");
       	var show1td = document.createElement("td");
       	show1tr.className = "TableControl";
         show1td.className = "menulines1";
         show1tr.style.cursor = "pointer";
         show1td.style.backgroundColor = "rgb(255, 255, 255)";
         show1td.align = "center";
         show1td.id = rtJson.rtData[i].seqId;
         
         show1td.onclick = function(){
           clickUser(this);
         }
         show1td.innerHTML = rtJson.rtData[i].userId;
         show1tr.appendChild(show1td);
         table.appendChild(show1tr);
      }    
    }
  }

  function clickUser(field) {
    var pram = trim(window.parent.dialogArguments.document.getElementById("toIdDesc").value);
    var pramId = trim(window.parent.dialogArguments.document.getElementById("toId").value);
    if(field.style.backgroundColor != "rgb(0, 255, 0)"){
      field.style.backgroundColor = "rgb(0, 255, 0)";
      window.parent.dialogArguments.document.getElementById("toIdDesc").value  = trim(pram + trim(field.innerHTML) + ",");
      window.parent.dialogArguments.document.getElementById("toId").value = trim(pramId + trim(field.id) + ",");
    }else{
      field.style.backgroundColor = "rgb(255, 255, 255)";
      var prams = pram.split(',');
      var pramString = "";
      var ids = pramId.split(',');
      var idString = "";
      for(var i = 0; i < prams.length; i++){
        if(trim(field.innerHTML) != prams[i] && prams[i] != ""){
          pramString += prams[i];
          pramString += ",";
          idString += ids[i];
          idString += ",";
    	}
      }
      window.parent.dialogArguments.document.getElementById("toIdDesc").value = trim(pramString);
      window.parent.dialogArguments.document.getElementById("toId").value = trim(idString);
    }
  }

  function addAll() {
    var tableObj = document.getElementById("tbody");
    var tds = tableObj.getElementsByTagName("td");
    var paramString = "";
    var idString = "";
    for(var i = 0; i < tds.length; i++){
      paramString += tds[i].innerHTML;
      paramString += ",";
      idString += tds[i].id;
      idString += ",";
      tds[i].style.backgroundColor = "rgb(0, 255, 0)";
    }
    window.parent.dialogArguments.document.getElementById("toIdDesc").value = trim(paramString);
    window.parent.dialogArguments.document.getElementById("toId").value = trim(idString);
  }

  function delAll() {
    window.parent.dialogArguments.document.getElementById("toIdDesc").value  = "";
    window.parent.dialogArguments.document.getElementById("toId").value = "";
    
    var tableObj = document.getElementById("tbody");
    var tds = tableObj.getElementsByTagName("td");
	for(var i = 0; i < tds.length; i++) {
	  tds[i].style.backgroundColor = "rgb(255, 255, 255)";
    }
  }
</script>
</head>
<body class="bodycolor" topmargin="1" leftmargin="0" onload="doInit()">
<div id="personDiv">
<table class="TableBlock" width="100%" name="personTable" id="personTable">
  <tr class=TableHeader>
    <td align="center">选择收件人 <input type="button" class="SmallButton" value="确定" onclick="window.parent.close()"></td>
  </tr>
    
  <tr class="TableControl">
    <td onclick="javascript:addAll();" style="cursor:pointer" align="center">全部添加</td>
  </tr>

  <tr class="TableControl">
    <td onclick="javascript:delAll();" style="cursor:pointer" align="center">全部删除</td>
  </tr>
  
  <tbody id="tbody">
  </tbody>
  
  <tr class=TableControl>
    <td align="center"><input type="button" class="BigButton" value="确定" onclick="window.parent.close()"></td>
  </tr>
  
</table>
</div>
</body>
</html>