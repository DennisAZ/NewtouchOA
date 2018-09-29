<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<% 
String seqId = request.getParameter("seqId");
String mailId = request.getParameter("mailId");

%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var mailId = "<%=mailId%>";
function doInit(){
  //标记updateFlag
  var url = "<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/mailDeatil.act?seqId=" + seqId + "&boxId=0";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var importValue = rtJson.rtData.data.important;
    var importHtml = "";
    if(importValue == "1"){
      importHtml = "<font color=\"red\">重要</font>";
    }else if(importValue == "2"){
      importHtml = "<font color=\"red\">非常重要</font>";
    }
    rtJson.rtData.data.subject =  rtJson.rtData.data.subject + "&nbsp;" + importHtml
   	bindJson2Cntrl(rtJson.rtData.data);
   if(rtJson.rtData.data.attachmentName){
     showTr('atttr');
     attachMenuUtil("showAtt","email",null,rtJson.rtData.data.attachmentName,rtJson.rtData.data.attachmentId,true);
    }
   if(rtJson.rtData.data.copyToId){
     showTr('copyToIdtr');
    }
   if($('toId').value != "-1"){
     bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
     var toIdDesc = $('toIdDesc').innerHTML;
     if(toIdDesc.indexOf(",") != -1){
	     showAllUser('toIdDesc','showAllUser',80);
	     $('showReadStatus').style.display = "";
      }
    }   
  bindDesc([{cntrlId:"fromId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
	   ,{cntrlId:"copyToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
  ]);
  showAllUser('copyToIdDesc','showCopyUser',80);
  if(rtJson.rtData.data.toWebmail){
    $('toWebmail').innerHTML = rtJson.rtData.data.toWebmail;
   }
  if(rtJson.rtData.data.toWebmailCopy){
    $('toWebmailCopy').innerHTML = rtJson.rtData.data.toWebmailCopy;
   }
  }
}
function print2(seqId){
  var actionUrl = contextPath + "/core/funcs/email/print.jsp?seqId=" + seqId;
  window.open(actionUrl,'','menubar=1,toolbar=1,status=1,resizable=1');
}
</script>
</head>
<body class="bodycolor" topmargin=10 leftmargin=5 onload="doInit();">

  <table class="TableBlock" width="100%" align="center">
    <tr class="TableData" height="25" id="subjecttr">
      <td class="TableContent" align="center" width="15%"><b>主　题：</b></td>
      <td id="subject" name="subject" width="85%"></td>
    </tr>
    <tr class="TableData" height="25" id="fromIdtr">
      <td class="TableContent" align="center" width="15%"><b>发件人：</b></td>
      <td width="85%"><input type="hidden" id="fromId" name="fromId">
          <span id="fromIdDesc"></span></td>
       </tr>
    <tr class="TableData" height="25" id="toIdtr">
      <td class="TableContent" align="center" width="15%" ><b>收件人：</b></td>
      <td width="85%"><input type="hidden" id="toId" name="toId"><span id="toIdDesc"></span>
         <a id="showAllUser" style="display:none">全部名单</a>&nbsp;
         <a id="showReadStatus" href="javascript:readStatus('<%=seqId %>');" style="display:none">查看邮件状态</a>
   
      </td>
    </tr>
    <tr class="TableData" height="25" id="copyToIdtr" style="display:none">
      <td class="TableContent" align="center" width="15%"><b>抄　送：</b></td>
      <td width="85%">
        <input type="hidden" id="copyToId" name="copyToId">
      	<span id="copyToIdDesc" name="copyToIdDesc"></span>
        <a id="showCopyUser" style="display:none" >全部名单</a>
      </td>
    </tr>
    <tr class="TableData" height="25" id="webmailIdtr">
      <td class="TableContent" align="center" width="15%" ><b>外部收件人：</b></td>
      <td width="85%"><span id="toWebmail"></span>
      </td>
    </tr>
    <tr class="TableData" height="25" id="webmailIdCctr">
      <td class="TableContent" align="center" width="15%" ><b>外部抄送人：</b></td>
      <td width="85%"><span id="toWebmailCopy"></span>
      </td>
    </tr>
    <tr class="TableData" height="25" id="sendTimetr">
      <td class="TableContent" align="center" width="15%"><b>发送于：</b></td>
      <td width="85%"><span id="sendTime" name="sendTime"></span> </td>
    </tr>
    <tr class="TableData" height="25" id="atttr" style="display:none">
      <td class="TableContent" align="center" width="15%"> <b>附　件：</b></td>
      <td width="85%">
        <span id="showAtt" style="float:left;"></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div>
       <a href="javascript:doBachDown();" title="附件打包下载"><img src="<%=imgPath%>/download.gif" align="absMiddle">附件批量下载</a>&nbsp;
        <input type="hidden" value="" id="attachmentName"></input>
        <input type="hidden" value="" id="attachmentId"></input></div>
      </td>
    </tr>
    <tr class="TableData" id="attimage" style="display:none">
      <td colspan="2">
        <img src="<%=imgPath%>/cmp/email/image.gif" align="absmiddle" border="0">&nbsp;附件图片: <br><br>
      </td>
    </tr>

  </table>
<table class="TableBlock no-top-border" width="100%">
    <tr class="TableData Content" id="contenttr">
      <td height="160" valign="top" id="content" name="content"></td>
    </tr>
</table>
<script Language="JavaScript">
function reply()
{
  URL="../../new?reply=2&emailId=<%=seqId%>&BOX_ID=0&FIELD=&ASC_DESC=";
  parent.location=URL;
}
function reply_all()
{
  URL="../../new?reply=1&emailId=<%=seqId%>&BOX_ID=0&FIELD=&ASC_DESC=";
  parent.location=URL;
}
function fw()
{
  URL="../../new?FW=1&emailId=<%=seqId%>&BOX_ID=0&FIELD=&ASC_DESC=";
  parent.location=URL;
}
function deletByShow(id){
  var url = contextPath + "/core/funcs/email/sendbox/index.jsp";
  deleteMail(3,url,id);
}
</script>
</body>
</html>