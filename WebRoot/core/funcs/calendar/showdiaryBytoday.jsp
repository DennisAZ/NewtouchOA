<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  Date curDate = new Date();
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  String curDateStr = dateFormat1.format(curDate);
  if(seqId==null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>

<title>今日日志</title>
<script type="text/javascript">
function doInit(){
  var seqId = '<%=seqId%>';
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarDiaryAct/selectDiary.act?seqId="+seqId;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcs = rtJson.rtData;
  if(prcs.length>0){
    var newStrs = "";
    for(var i= 0;i<prcs.length;i++){
      var news = bindOneNewsTemp(prcs[i]);
      newStrs = newStrs + news;
    }
    $('bodyContent').innerHTML =  newStrs;
    for(var i= 0;i<prcs.length;i++){
      if(prcs[i].attachmentId){
        var attrCntrl = "attr_"+ prcs[i].seqId;
        showAttach(prcs[i].attachmentId,prcs[i].attachmentName,attrCntrl,true);
      }
    }
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的日程日志</div></td></tr>"
        );
    $('bodyContent').innerHTML = "";
    $('bodyContent').appendChild(table);
  }

}
function bindOneNewsTemp(json) {
  var obj = json;
  var news = "<div class=subject>"+obj.subject+"</div><div class=diary_type>";
  if (obj.diaType == "1") {
    news += "工作日志";
  } else {
    news += "个人日志";
  }
  news += " | 日志日期："
      + cutDate(obj.diaDate)
      + " | 最后修改："
      + obj.diaTime
      + " | ";
 
    news += "日志作者:" + getUserNameById(obj.userId);

  news += "</DIV>"
      + "<DIV style=\"OVERFLOW-X: auto; OVERFLOW-Y: auto;\" class=content>"
      + obj.content + "</DIV>";
  if (obj.attachmentId.trim()) {
    news += "<DIV class=content><BR><BR>附件：<BR><span id=\"attr_" + obj.seqId + "\"></span></DIV>"
  }
  news += "<DIV class=operate></div></div>";
  return news;
}
</script>
</head>
<body onload="doInit();">
<body >
<br></br>
<div id="bodyContent" ></div>
<br>
</body>
</html>