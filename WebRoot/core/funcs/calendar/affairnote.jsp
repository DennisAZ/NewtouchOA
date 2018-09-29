<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript">
function doOnload(){
  var seqId = '<%=request.getParameter("seqId")%>';
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairById.act?seqId="+seqId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData; 
  if(prc.seqId){
    var seqId = prc.seqId;
    var type = prc.type;
    var beginTime = prc.beginTime;
    var endTime = prc.endTime;
    var remindTime = prc.remindTime;
    var managerName = prc.managerName;
    var content = prc.content;
    var typeNames = ["每日","每周","每月","每年"];
    var weekNames = ["一","二","三","四","五","六","日"];
    var week_day_month = '';
    var time = beginTime.substr(0,10)+" 至 " + endTime;
    if(type=='3'){
      week_day_month = weekNames[parseInt(prc.remindDate)-1];  
    }
    if(type=='4'){
      week_day_month= prc.remindDate+'日';
    }
    if(type=='5'){
      week_day_month = prc.remindDate.split('-')[0]+'月'+prc.remindDate.split('-')[1]+'日';
    }
    if(managerName!=''){
      managerName = "<br>安排人：("+managerName+")";
    }
    //document.getElementById("time").innerHTML = time;
    document.getElementById("type").innerHTML = typeNames[type-2] + week_day_month +" " + remindTime;
    document.getElementById("content").innerHTML = (content || "").replace(/\r\n/g, "<br/>").replace(/\n/g, "<br/>").replace(/\r/g, "<br/>");
    document.getElementById("managerName").innerHTML = managerName;
  }
 
}
</script>
<body class="bodyClass" topmargin="5"  onload="doOnload();">
<div class="small">
<b id="time"></b>
<b id="type"></b>
<b id=managerName></b>
<hr>

<span id="content" style="word-break:break-all;"></span></div>
</body>
</html>