<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = (String)request.getParameter("seqId");
  if(seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>显示内容</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/smsbox.css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/smsutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/sms/js/sms.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
  function doInit() {  
    bindSmsBody(seqId);
  }
  function bindSmsBody(seqId){
    var url = contextPath + "/yh/core/funcs/sms/act/YHSmsAct/showSmsBody.act?seqId=" + seqId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      var smsBody = rtJson.rtData; 
      var userInfo =  getPersonInfo(smsBody.fromId);
      if(userInfo){
        $("userId").innerHTML = userInfo.userName;
        $("userId").title = userInfo.deptName;
      }
      var type = "<IMG alt=\"" + getSmsTypeDesc(smsBody.smsType) + "\" src=\"" + "<%=imgPath%>" + "/sms/sms_type"+ smsBody.smsType +".gif\" align=absMiddle>&nbsp;&nbsp;" + getSmsTypeDesc(smsBody.smsType);
      $("smsType").innerHTML = type;
      $("timers").innerHTML = smsBody.sendTime;
      $("content").innerHTML = smsBody.content;
    }
  }
</script>
</head>
<body onload="doInit()">
<div  class="module_sms listColor">
       <DIV class=head ><DIV CLASS=moduleHeader id="smsType"></DIV></div>
    <div class="module_body">
      <div style="color: #0000ff;">     
       <u id="userId"  style="cursor: hand;"></u>&nbsp;&nbsp;<span id="timers"></span>
      </div>
      <div style="height:10px">&nbsp;</div>
      <div id="content" class="content">
      </div>
      </div>
</div>
</body>
</html>