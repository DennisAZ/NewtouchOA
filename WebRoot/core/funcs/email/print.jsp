<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
String mailId = request.getParameter("mailId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>邮件打印 </title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
  var userInfo = getUserInfo();
  if(userInfo){
    $('userId').innerHTML = userInfo.userId;
    $('userIdDesc').innerHTML = userInfo.userName;
  }
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
   
   
   /*
   if($('toId').value != "-1"){
     bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
     var toIdDesc = $('toIdDesc').innerHTML;
     if(toIdDesc.indexOf(",") != -1){
       showAllUser('toIdDesc','showAllUser',80);
       $('showReadStatus').style.display = "";
      }
    }   
 */
    bindDesc([{cntrlId:"toId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    $('toId2').innerHTML = $('toId').value;
    bindDesc([{cntrlId:"fromId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
    ,{cntrlId:"copyToId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
   ]);
    $('fromId2').innerHTML = $('fromId').value;  
    $('copyToId2').innerHTML = $('copyToId').value;  
    if(rtJson.rtData.data.copyToId){
      showTr('copyToIdTr',null);
     }
    if(rtJson.rtData.data.attachmentName){
      showTr('atttr');
      var attachs = rtJson.rtData.data.attachmentName.split('*');
      for(var i = 0; i <attachs.length; i++){
         if(!attachs[i]){
           continue;
         }
         
         var ur = "<u>" + attachs[i] + "</u>&nbsp;&nbsp;";
         $("attach").insert(ur,'bottom');
       }
     }
  }
}
</script>
</head>
<STYLE type=text/css>.HdrPaddingTop {
	PADDING-TOP: 5px
}
.HdrPaddingBttm {
	PADDING-BOTTOM: 5px
}
BODY {
	MARGIN-TOP: 2px; MARGIN-LEFT: 0px; COLOR: #000000; MARGIN-RIGHT: 0px
}
A {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
BODY {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TABLE {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TD {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TR {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TABLE {
	BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px
}
.FF {
	COLOR: #000000
}
</STYLE>

<body onload="doInit()">
<P>
<TABLE cellSpacing=0 cellPadding=3 width=640 align=center border=0>
  <TBODY>
  <TR>
    <TD style="BORDER-BOTTOM: #000099 1px solid" colSpan="2"></TD>
  </TR>
  <TR>
    <TD noWrap><FONT class=FF><span id="userIdDesc"></span> &lt;<span id="userId"></span>&gt;</FONT></TD>
    <TD noWrap align=right><FONT class=FF>打印时间： <%=YHUtility.getCurDateTimeStr() %> 
  </FONT></TD></TR></TR>
  <TR>
    <TD colSpan=2></TD>
  </TR>
  </TBODY>
</TABLE>

<TABLE cellSpacing=0 cellPadding=0 width=640 align=center border=0>
  <TBODY>
  <TR>
    <TD colSpan=2>
      <HR color=#808080 SIZE=3>
    </TD></TR>
  <TR>
    <TD class=HdrPaddingBttm id=MsgHeaders noWrap  width=70><B>发件人：</B></TD>
    <TD class=HdrPaddingBttm width="100%"><span id="fromIdDesc"></span> &lt;<span id="fromId2"></span><input type="hidden" value="" id="fromId">&gt; </TD>
  </TR>
  <TR>
    <TD class=HdrPaddingBttm noWrap width=70><B>发送：</B> </TD>
    <TD class=HdrPaddingBttm width="100%" id="sendTime"></TD>
  </TR>
  <TR>
    <TD class=HdrPaddingBttm noWrap width=70><B>收件人：</B> </TD>
    <TD class=HdrPaddingBttm width="100%"><span id="toIdDesc"></span> &lt;<span id="toId2"></span><input type="hidden" value="" id="toId">&gt;  </TD>
  </TR>
  <TR style="display:none" id="copyToIdTr">
    <TD class=HdrPaddingBttm noWrap width=70><B>抄送：</B> </TD>
    <TD class=HdrPaddingBttm width="100%"><span id="copyToIdDesc"></span> &lt;<span id="copyToId2"></span><input type="hidden" value="" id="copyToId">&gt; </TD>
  </TR>
  <TR>
    <TD class=HdrPaddingBttm noWrap width=70><B>主题：</B> </TD>
    <TD class=HdrPaddingBttm width="100%" id="subject"> </TD>
  </TR>
  <TR id="atttr" style="display:none">
    <TD class=HdrPaddingBttm noWrap width=70 ><B>附件：</B> </TD>
    <TD class=HdrPaddingBttm width="100%" id="attach"> </TD>
  </TR>
  <TR>
    <TD colSpan=2>
      <HR color=#808080 SIZE=3>
    </TD>
  </TR>
  </TBODY>
</TABLE>

<TABLE cellSpacing=0 cellPadding=0 width=640 align=center border=0>
  <TBODY>
  <TR>
    <TD class=HdrPaddingBttm width="100%" id="content">
    </TD>
  </TR>
  </TBODY>
</TABLE>

</body>
</html>