<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");

%>
<html>
<head>
<title>收文签收</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css"/>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript">
var seqId = "<%=seqId %>";

function doInit(){
  
  var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocRegisterAct/getRecReg.act?rec_seqId="+seqId;
  var json = getJsonRs(url); 
  if(json.rtState == "0"){
    var data = json.rtData;
    if (data.isCancel == '0') {
      $('fromUnit').update(data.fromDeptName);
      $('title').update(data.title);
      $('sendDoc').update(data.recDoc);
      $('sendTime').update(data.sendTime);
      $('recDocName').value = data.recDocName;
      $('recDocId').value = data.recDocId;
      if (data.recDocId) {
        $('showRecDoc').update(data.recDocName);
       /// attachMenuUtil("showRecDoc","doc",null,data.recDocName,data.recDocId,true,"doc","","",true);
      }
    } else {
      $('noshow').show();
      $('show').hide();
    }
  }
  
}
function signUp(){
  if (confirm("确认签收！")) {
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocSignAct/sign.act?seqId=" + seqId;
    var json = getJsonRs(url);
    if (json.rtState = '0') {
      location.href='<%=contextPath %>/core/funcs/doc/receive/sign/index.jsp';
    }
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif"/><span class="big3">签收公文</span>
    </td>
  </tr>
</table>
<div id="noshow" align="center" style="display:none">
<table class="MessageBox" width="300">
    <tbody>
        <tr>
            <td id="outMsgInfo" class="msg info">
            公文已被收回不能签收！
            </td>
        </tr>
    </tbody>
</table>
</div>
<table class="TableList"  width="400px" align="center">
  <TR>
      <TD class="TableData"  align="right">来文文号：</TD>
      <TD class="TableData" align="left"><div id="sendDoc"></div></TD>
  </TR>
   <TR>
      <TD class="TableData"  align="right">来文标题：</TD>
      <TD class="TableData"  align="left"><div id="title"></div></TD>
  </TR>
   <TR>
      <TD class="TableData"  align="right">来文单位：</TD>
      <TD class="TableData"  align="left"><div id="fromUnit"></div></TD>
  </TR>
   <TR>
      <TD class="TableData"  align="right"> 发送时间：</TD>
      <TD class="TableData"  align="left"><div id="sendTime"></div></TD>
  </TR>
   <TR>
      <TD class="TableData"  align="right">公文正文：</TD>
      <TD class="TableData"  align="left"><span id="showRecDoc"></span>
      <input type="hidden" id="recDocId" name="recDocId">
        <input type="hidden" id="recDocName" name="recDocName">
      </TD>
  </TR>
    <tr align="center" class="TableControl" >
      <td colspan="2" nowrap>
      <span id="show">
       <input type="button" value="签收" class="BigButton" onClick="javascript:signUp();">
      </span>
       &nbsp; &nbsp;
        <input type="button" value="返回" class="BigButton" onClick="javascript:location.href='<%=contextPath %>/core/funcs/doc/receive/sign/index.jsp'">
      </td>
    </tr>
  </table>
</body>
</html>