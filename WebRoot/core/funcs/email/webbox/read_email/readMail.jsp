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
String boxId = request.getParameter("boxId") == null ? "0" : request.getParameter("boxId");

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
  //alert(seqId);
  //updateFlag([{'seqId':seqId,'dsDef':"EMAIL,READ_FLAG,1"}]);
  if(checkIsDel(mailId)){
    WarningMsrg("此邮件已被删除!","msrg","info")
    return;
   }
  var url = "<%=contextPath %>/yh/core/funcs/email/act/YHInnerEMailAct/webmailDeatil.act?seqId=" + seqId + "&boxId=0";
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
   
    var content2 =  rtJson.rtData.data.content;
    rtJson.rtData.data.fromWebmail = rtJson.rtData.data.fromWebmail.replace(/</g,"&lt;").replace(/>/g,"&gt;");
    bindJson2Cntrl(rtJson.rtData.data);
    //大附件加载
   if(hasLagerAttachment(rtJson.rtData.data.seqId)){
     var imgurl = imgPath + "/cmp/email/download.gif";
      var refreshHtml = "<a href=\"javascript:refreshAttachment(" + rtJson.rtData.data.seqId + ");\"><img src=\"" + imgurl + "\" align=\"absmiddle\"> 收取本邮件的附件</a>";
      $("showAtt").innerHTML = refreshHtml;
      showTr('atttr');
    }else{
      if(rtJson.rtData.data.attachmentName){
        showTr('atttr');
        showTr('bachAtt');
        attachMenuUtil("showAtt","email",null,rtJson.rtData.data.attachmentName,rtJson.rtData.data.attachmentId,true);
       }
    }
   if($('toId').value != "-1"){
     //$('toId').value = rtJson.rtData.tomail;
     //bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    // var toIdDesc = $('toIdDesc').innerHTML;
    if (rtJson.rtData.tomail) {
      var tomail = rtJson.rtData.tomail.replaceAll("<","&lt;");
      tomail = tomail.replaceAll(">" , "&gt;");
      //alert(tomail);
      $('toIdDesc').update(tomail);
    }
    if (rtJson.rtData.data.copyToId || rtJson.rtData.ccmail) {
      if (rtJson.rtData.ccmail) {
        var tomail = rtJson.rtData.ccmail.replaceAll("<","&lt;");
        tomail = tomail.replaceAll(">" , "&gt;");
        $('copyToIdDesc').update(tomail);
      }
      showTr('copyToIdtr',null);
    }
    /*
     if(toIdDesc.indexOf(",") != -1){
	     showAllUser('toIdDesc','showAllUser',80);
	     $('showReadStatus').style.display = "";
      }*/
    } 	  
    showAllUser('copyToIdDesc','showCopyUser',80);
  }
  readMailById(mailId);
  $('smsTable').style.display = "";
}
function checkIsDel(mailId){
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/checkIsDel.act?mailId=" + mailId + "&type=1";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData.isDelete;
  }else{
   return false;
  }
}
function print2(seqId){
  var actionUrl = contextPath + "/core/funcs/email/print.jsp?seqId=" + seqId;
  window.open(actionUrl,'','menubar=1,toolbar=1,status=1,resizable=1');
}
function hasLagerAttachment(seqId){
  var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/hasLagerAttachment.act?bodyId=" + seqId ;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == "1"){
      return true;
    }else{
      return false;
     }
  }else{
   return false;
  }
}
/**
 * 
 */
function refreshAttachment(seqId){
  var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/refreshLagerAttachment.act?bodyId=" + seqId ;
  var rtJson = getJsonRsAsyn(url,'',callBackFunc,true);
  var url = imgPath + "/cmp/email/inbox_sending.gif";
  var temp = "<img src=\"" + url + "\" align=\"absmiddle\"> 已提交服务器，正在收取附件...";
  $("showAtt").innerHTML = temp;
}
function callBackFunc(data){
  var obj = data.rtData;
  if(obj.refreshState == "0"){
    var attachmentId = obj.attachmentId;
    var attachmentName = obj.attachmentName;
    $("attachmentId").value = obj.attachmentId;
    $("attachmentName").value = obj.attachmentName;
    attachMenuUtil("showAtt","email",null,attachmentName,attachmentId,true);
    showTr('bachAtt');
  }else{
    var url = imgPath + "/cmp/email/inbox_failure.png";
    var failure = "<img src=\"" + url + "\" align=\"absmiddle\"> 附件收取失败，重新收取";
    var refreshHtml = "<a href=\"javascript:refreshAttachment(" + obj.bodyId + ");\"><img src=\"" + failure + "\" align=\"absmiddle\"> 收取本邮件的附件</a>";
    $("showAtt").innerHTML = refreshHtml;
  }
}
</script>
</head>
<body class="bodycolor" topmargin=10 leftmargin=5 onload="doInit();">

  <table class="TableBlock" width="100%" align="center" style="display:none" id="smsTable">
    <tr class="TableData" height="25" id="subjecttr">
      <td class="TableContent" align="center" width="20%"><b>主　题：</b></td>
      <td id="subject" name="subject"  width="80%"></td>
    </tr>
    <tr class="TableData" height="25" id="fromIdtr">
      <td class="TableContent" align="center" width="20%"><b>发件人：</b></td>
      <td  width="80%">
          <span id="fromWebmail"></span> &nbsp;&nbsp;</td>
       </tr>
    <tr class="TableData" height="25" id="toIdtr">
      <td class="TableContent" align="center" width="20%" ><b>收件人：</b></td>
      <td width="80%"><input type="hidden" id="toId" name="toId" ><span id="toIdDesc"></span>
        <a id="showAllUser" style="display:none">全部名单</a>&nbsp;
         <a id="showReadStatus" href="javascript:readStatus('<%=seqId %>',1);" style="display:none">查看邮件状态</a>
      </td>
    </tr>
    <tr class="TableData" height="25" id="copyToIdtr" style="display:none">
      <td class="TableContent" align="center" width="20%"><b>抄　送：</b></td>
      <td width="80%">
        <input type="hidden" id="copyToId" name="copyToId">
      	<span id="copyToIdDesc" name="copyToIdDesc"></span>
        <a id="showCopyUser" style="display:none" >全部名单</a>
      </td>
    </tr>
    <tr class="TableData" height="25" id="sendTimetr">
      <td class="TableContent" align="center" width="20%"><b>发送于：</b></td>
      <td width="80%"><span id="sendTime" name="sendTime"></span> </td>
    </tr>
    <tr class="TableData" height="25" id="atttr" style="display:none">
      <td class="TableContent" align="center" width="20%"> <b>附　件：</b></td>
     <td width="80%"> <div id="showAtt" style="float:left;"></div>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<div  id="bachAtt" style="display:none">
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
      <td height="160" valign="top" id="webmailContent" name="content" class="contentSelf" sytle="line-height:normal" ></td>
    </tr>
</table>
<div id="msrg"></div>
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
  var url = contextPath + "/core/funcs/email/inbox/index.jsp?boxId=<%=boxId%>";
  deleteMail(1,url,id);
}
</script>
</body>
</html>