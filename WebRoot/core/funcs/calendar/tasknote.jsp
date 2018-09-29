<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doOnload(){
  var seqId = '<%=request.getParameter("seqId")%>';
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/selectTaskById.act?seqId="+seqId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  if(prc.seqId){
    var seqId = prc.seqId;
    var beginDate = prc.beginDate;
    var endDate = prc.endDate;
    var subject = prc.subject;
    var content = prc.content;
    var managerName = prc.managerName;
    if(managerName.trim()!=''){
       $("managerName").innerHTML = "(安排人：" + managerName + ")";
    }
    var rate = prc.rate;
    if(beginDate==''){
      beginDate ="无";
    }else{
      beginDate = beginDate.substr(0,10);
    }
    if(endDate==''){
      endDate = "无";
    }else{
      endDate = endDate.substr(0,10); 
    }
    document.getElementById("beginDate").innerHTML = beginDate;
    document.getElementById("endDate").innerHTML = endDate;
    document.getElementById("rate").innerHTML = rate;
    document.getElementById("subject").innerHTML = subject;
    document.getElementById("content").innerHTML = content;
  }
}

</script>
<body class="bodyClass" topmargin="5" onload="doOnload();">
<div class="small">
开始日期 :&nbsp;<b id="beginDate"></b><br>
结束日期 :&nbsp;<b id="endDate"></b>&nbsp;&nbsp;&nbsp;完成<b id="rate"></b>% <br>
<b id="managerName"></b><hr>

<span id="subject"></span><br>
<span id="content" style="word-break:break-all;"></span></div>
</body>
</html>