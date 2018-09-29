<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<%
String emailId = request.getParameter("emailId");
%>
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>读邮件</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var emailId = "<%=emailId %>";
function doInit() {
  var url = contextPath + "/yh/core/funcs/system/resManage/act/YHEmailResManageAct/getEmail.act";
  var json = getJsonRs(url , "emailId=" + emailId);
  if (json.rtState == "0") {
    var data = json.rtData;
    var subject = data.subject;
    var important = data.important;
    if(important=='0' || important=="")
      importantDesc="";
   else if(important=='1')
     importantDesc="<span class=\"TextColor1\">重要</span>";
   else if(important=='2')
     importantDesc="<span class=\"TextColor2\">非常重要</span>";
    $("title").update(subject + importantDesc);
    $("fromName").update(data.fromName);
    $("toName").update(data.toName);
    $("sendTime").update(data.sendTime);
    $("copyToName").update(data.copyToName);
    $("content").update(data.content);
    var attId = data.attId;
    var attName = data.attName;
    var  selfdefMenu = {
      	office:["downFile","dump","read"], 
        img:["downFile","dump","play"],  
        music:["downFile","dump","play"],  
  	    video:["downFile","dump","play"], 
  	    others:["downFile","dump"]
  		}
    if (attName && attId) 
    attachMenuSelfUtil($("attachment11"),"email",attName ,attId, '','',emailId,selfdefMenu);
  }
}
</script>
</head>
<body onload="doInit()">
<table class="TableBlock" width="90%" align="center">
    <tr>
      <td class="TableHeader" height=30>
        <img src="<%=imgPath %>/email_open.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
        <span id="title"></span>
        </td>
    </tr>
    <tr>
      <td class="TableData" width="20%">
        <b>发件人：</b><span id="fromName"></span><br>
        <b>收件人：</b><span id="toName"></span><br>
         <b>抄　送：</b><span id="copyToName"></span><br>
         <b>发送于：</b><span id="sendTime"></span>
      </td>
    </tr>
    <tr>
      <td class="TableData"> <b>附　件：</b><span id="attachment11"></span></td>
    </tr>
    <tr class="big">
      <td class="TableData" height="160" valign="top"  id="content">
      </td>
    </tr>
  </table>
<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="history.back();">
</div>
        
</body>
</html>