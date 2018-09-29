<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<html>
<head>
<title>进度日志详情</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(seqId) {
  var requestUrl = "<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId2.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
   alert(rtJson.rtMsrg); 
   return;
 }
  var prcs = rtJson.rtData;
  if (prcs.length > 0) {
      for(var i = 0;i < prcs.length;i++) {
          var prc = prcs[i];
          var writeTime = prc.writeTime;
          var progress = prc.progress;
          var percent = prc.percent;
          var attachmentName = prc.attachmentName;
          var attachmentId = prc.attachmentId;
          if (writeTime == null) {
            writeTime = "";
          }
          if (progress == null) {
            progress = "";
          }
          if (attachmentName == null) {
            attachmentName = "";
          }
          var div = new Element('div').update("日志时间:&nbsp; " + writeTime + "<br><br>"
              + "撰写日志时间 &nbsp;" + writeTime + " &nbsp;进度:&nbsp; " + percent + "%<br><br>"
              + "进度详情: &nbsp;" + progress + "<br><br>"
              + "附件:&nbsp;<input type='hidden' id='attachmentId' name='attachmentId' value='" + attachmentId  + "'>" 
              + "<input type='hidden' id='attachmentName' name='attachmentName' value='" + attachmentName  + "'>"
              + "<span id='showAtt'></span>");
         
          $('listDiv').appendChild(div);
      }
  }
  attachMenuUtil("showAtt","work_plan",null,$('attachmentName').value ,$('attachmentId').value,true);
}
</script>
</head>
<body bgcolor="#FFFFCC" topmargin="5" onload="doInit('<%=request.getParameter("seqId")%>');">
<div id="listDiv" align="center" class="small"></div>
</body>
</html>
