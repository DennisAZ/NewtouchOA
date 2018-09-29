<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<TITLE>预约情况</TITLE>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<style type="text/css">
.test{
   border:0; 
}
.test .testtd td
.test td.testtd{
   border:0px #767367 solid;
}
</style>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/meeting/act/YHMeetingAct/getMeetingInfo.act"; 
  var rtJson = getJsonRs(requestURL);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    if(data.AllTr){
      var table = new Element('table',{"class":"TableBlock" ,"align":"center"}).update(data.AllTr+""
        +"<tr class=TableControl>"
        +"<td colspan='8' align=center>"
        +"<input type='button' value=' 关 闭  ' class='BigButton' onclick='window.close()'>"
        +"</td></tr>");
      $("tableList").appendChild(table);
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</HEAD>
<body  topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/meeting.gif" align="absmiddle"><span class="big3"> 会议预约情况图表</span>
    </td>
  </tr>
</table>  
<br>
<table class=small>
  <tr>
    <td>图例说明：</td>
    <td width=20 bgColor="#378CD9"></td>
    <td width=40>空闲</td>
    <td width=20 bgColor="#ff33ff"></td>
    <td width=40>待批</td>
    <td width=20 bgColor="#00ff00"></td>
    <td width=40>已准</td>
    <td width=20 bgColor="#ff0000"></td>
    <td width=80>使用中</td>
  </tr>
</table>
<div id="tableList"></div>
</body>
</HTML>
