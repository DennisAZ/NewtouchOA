<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title id="title"></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style type="text/css">
<!--
span{word-warp:break-word;word-break:break-all;}

</style>
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
  var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarById.act?seqId="+seqId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  if(prc.seqId){
    var seqId = prc.seqId;
    var userId = prc.userId;
    var calType = prc.calType
    var calLevel = prc.calLevel;
    var content = prc.content;
    var managerId = prc.managerId;
    var managerName = prc.managerName;
    var calLevel = prc.calLevel;
    var calTime = prc.calTime;
    var endTime = prc.endTime;
    var overStatus = prc.overStatus;  
    var status = prc.status;
    if(calLevel.trim()==''){
      calLevel = 0;
    }
    var statusName = '已完成';
    if(status=='1'){
      statusName='未开始';
    }
    if(status=='0'){
      statusName='进行中';
    }
    if(status=='2'){
      statusName='已超时';
    }
    if(overStatus=='1'){
      status = 3;
      statusName = '已完成';
    }
    if(managerName!=''){
      managerName = "安排人：("+managerName+")";
    }

    var colorTypes = ["#0000FF","#0000FF","#FF0000","#00AA00"];
    var calLevelNames = ['未指定','重要/紧急','重要/不紧急','不重要/紧急','不重要/不紧急'];
    document.getElementById("time").innerHTML=calTime.substr(0,16) + " - " + endTime.substr(0,16);
    document.getElementById("status").innerHTML=statusName;
    document.getElementById("calLevel").innerHTML = calLevelNames[calLevel];
    document.getElementById("calLevel").className = 'CalLevel'+calLevel;
    document.getElementById("content").innerHTML = (content || "").replace(/\r\n/g, "<br/>").replace(/\n/g, "<br/>").replace(/\r/g, "<br/>");
    document.getElementById("fontcolor").style.color = colorTypes[status];
    document.getElementById("managerName").innerHTML = managerName;
  }
  

}
</script>
<body class="bodyClass" topmargin="5"  onload="doOnload();">
<div class="small">
<span id="time"></span><br>
<span id="managerName"></span>
 <br>
 <span id="calLevel" >不重要/紧急</span>&nbsp;
<font id="fontcolor" color=''><b id="status"></b></font><hr>

<span id="content"></span></div>
</body>
</html>