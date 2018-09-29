<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat,java.net.URLDecoder"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
String subject = request.getParameter("subject") == null ? "" :  request.getParameter("subject");
URLDecoder.decode(subject);
%>
<html>
<head>
<title>投票项目</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
function doInit () {
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/selectItemByVoteId.act?voteId="+seqId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var itemList = rtJson.rtData;
    //投票以及投票项目
    var table = new Element('table',{"class":"TableList","width":"95%" ,"align":"center"}).update("<tbody id='tbody'>"
        +"<tr class='TableHeader' > <td >选项</td>"
        +"<td width='8%'>票数</td>"
        +"<td width='20%'>投票人员</td>"
        +"<td width='10%'>操作</td></tr></tbody>");
    $("itemDiv").appendChild(table);
    for(var i =0 ;i<itemList.length;i++){
      var item = itemList[i];
      var tr = new Element('tr',{});
      $("tbody").appendChild(tr);
      var itemName = item.itemName;
      var re1 = /\'/gi;
      itemName = itemName.replace(re1,"&lsquo;");
      tr.update("<form name='form1' method='post'id='form1' action='#'><td><input type='text' id='itemName_"+item.seqId+"' name='itemName_"+item.seqId+"' value='"+itemName+"'></td>"
          +"<td >"+item.voteCount+"</td>"
          +"<td >"+item.voteUserName+"</td>"
          +"<td nowrap> <input type='button' value='修改' class='SmallButton' onclick='updateItem("+item.seqId+")'>&nbsp;"
          +"<input type='button' value='删除' class='SmallButton' name='button' onclick='deleteItem("+item.seqId+")'></td></form>");
    }
    var tr = new Element('tr',{});
    $("tbody").appendChild(tr);
    tr.update("<form name='form0' method='post' id='form0' action='#'><td colspan='4'> 添加项目："
        +"<input type='text' name='itemName' id='itemName'  value=''>"
        +"<input type='hidden' name='voteId' id='voteId' value='"+seqId+"'>&nbsp;&nbsp;"
        +"<input type='button' class='SmallButton' value='添加' onclick='addItem();'></td></form>");
}
function addItem(){
  var itemName = $("itemName").value;
  var voteId = $("voteId").value;
  //var queryParam = $("form0").serialize();
  var queryParam = "itemName="+encodeURIComponent(encodeURIComponent(itemName))+"&voteId="+voteId;
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/addItem.act?"+queryParam;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  alert("添加成功！");
  window.location.reload();
}

function updateItem(seqId){
  var itemName = $("itemName_"+seqId).value;
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/updateItem.act?itemName="+encodeURIComponent(itemName) + "&seqId="+seqId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  alert("保存成功！");
  window.location.reload();
}
function deleteItem(seqId){
  if(window.confirm("您真的要删除该条选项吗？\n\n注意：该操作不可恢复!")){
    var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/deleteItemById.act?seqId="+seqId;
    var rtJson = getJsonRs(url); 
    if(rtJson.rtState == "1"){
  	alert(rtJson.rtMsrg); 
  	return ; 
    }
    alert("删除成功！");
    window.location.reload();
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/vote.gif" align="absmiddle"><span class="big3" id="suTitle">投票项目管理 - <%=subject%></span>
    </td>
  </tr>
</table>
<div id="itemDiv"></div>
<div align="center">
   <br>
   <input type="button" class="BigButton" value="返回" onclick="javascript:history.back()">
</div>
</body>
</html>