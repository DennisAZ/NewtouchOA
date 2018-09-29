<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkDetail"%>
<%@page import="yh.core.funcs.workplan.data.YHWorkPlan"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="yh.core.funcs.workplan.act.YHCreateTime"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.core.funcs.person.data.YHPerson" %>
<%
YHWorkPlan plan = (YHWorkPlan)request.getAttribute("plan");
YHWorkDetail detail = new YHWorkDetail();
List<YHWorkDetail> list = (List<YHWorkDetail>)request.getAttribute("list");
List<YHWorkDetail> listP = (List<YHWorkDetail>)request.getAttribute("list2");
int sunNum = Integer.parseInt(request.getAttribute("sunWork").toString());
int sun2 = Integer.parseInt(request.getAttribute("sunWork2").toString());
int sunTotal = 0;
YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
boolean flag = false;
boolean fla = false;
int len = 0;
int len2 = 0;
int len3 = 0;
//负责人

if (!YHUtility.isNullorEmpty(plan.getLeader2Desc())) {
  len2 = plan.getLeader2Desc().split(",").length;
  for (int j = 0 ; j < len2; j++) {
    if (plan.getLeader2Desc().split(",")[j].equals(String.valueOf(person.getSeqId()))) {
      flag = true;
      fla = true;
    }
  }
}
//参与人

if (!YHUtility.isNullorEmpty(plan.getLeader1Desc())) {
  len = plan.getLeader1Desc().split(",").length;
  for (int i = 0 ; i < len; i++) {
    if (plan.getLeader1Desc().split(",")[i].equals(String.valueOf(person.getSeqId()))) {
      flag = true;
    }
  }
}
//创建人

  if (plan.getCreator().equals(String.valueOf(person.getSeqId()))) {
    fla = true;
  }
//批注领导
if (!YHUtility.isNullorEmpty(plan.getLeader3Desc())) {
  len3 = plan.getLeader3Desc().split(",").length;
  for (int i = 0 ; i < len3; i++) {
    if (plan.getLeader3Desc().split(",")[i].equals(String.valueOf(person.getSeqId()))) {
      fla = true;
    }
  }
}

Date date = new Date();
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

String year = sf.format(date).substring(0,4);
String mod = sf.format(date).substring(5,7);
String day = sf.format(date).substring(8,10);

int endTime1 = Integer.parseInt(year);//当前时间
int endTime2 = Integer.parseInt(mod);
int endTime3 = Integer.parseInt(day);

String eTime = null;
String eTime2 = null;
String eTime3 = null;

String begin1 = null;
String begin2 = null;
String begin3 = null;

int time1 = 0;//结束时间
int time2 = 0;
int time3 = 0;

int beginTime1 = 0;
int beginTime2 = 0;
int beginTime3 = 0;

if (plan.getEndTime() != null) {
  eTime = String.valueOf(plan.getEndTime()).substring(0,4);
  eTime2 = String.valueOf(plan.getEndTime()).substring(5,7);
  eTime3 = String.valueOf(plan.getEndTime()).substring(8,10);
  time1 = Integer.parseInt(eTime);
  time2 = Integer.parseInt(eTime2);
  time3 = Integer.parseInt(eTime3);
}
if (plan.getStatrTime() != null) {
  begin1 = String.valueOf(plan.getStatrTime()).substring(0,4);
  begin2 = String.valueOf(plan.getStatrTime()).substring(5,7);
  begin3 = String.valueOf(plan.getStatrTime()).substring(8,10);
  beginTime1 = Integer.parseInt(begin1);
  beginTime2 = Integer.parseInt(begin2);
  beginTime3 = Integer.parseInt(begin3);
}

int sun = 0;
String statrTime = String.valueOf(plan.getStatrTime());
String endTime = String.valueOf(plan.getEndTime());
if (YHUtility.isNullorEmpty(endTime) || endTime.equals("null")) {
  int nian = Integer.parseInt(statrTime.substring(0,4));
  int yue = Integer.parseInt(statrTime.substring(5,7));
  String ri = statrTime.substring(8,10);
  if (yue < 12) {
    yue = Integer.parseInt(statrTime.substring(5,7)) + 1;
  }
  if (yue == 12) {
    nian = Integer.parseInt(statrTime.substring(0,4)) + 1;
    yue = 1;
  }
  if (yue < 10) {
    endTime = nian + "-0" + yue + "-" + ri;
  } else {
    endTime = nian + "-" + yue + "-" +ri;
  }
}
int sun3 = 0;
int leng = 0;
if (plan.getLeader1Desc() != null) {
  leng = plan.getLeader1Desc().split(",").length;
}

String curDateStr = null;
if (listP.size() > 0) {
  for (int k = 0; k < listP.size() ; k++) {
    detail = listP.get(k);
    curDateStr = String.valueOf(detail.getWriteTime());
  }
}
Date startday = YHUtility.parseDate(statrTime);
Date endday = YHUtility.parseDate(endTime);
Calendar c = Calendar.getInstance(); 
c.setTime(startday); 
int weekthInt = c.get(Calendar.WEEK_OF_YEAR);   
String start = YHCreateTime.getFullTimeStr(YHCreateTime.getStartEnd(Integer.parseInt(statrTime.substring(0,4)),weekthInt)[0]);//本周的第一天

Date startDate = YHUtility.parseDate(start); //到哪一天 
//String end = getFullTimeStr(getStartEnd(2010,weekthInt)[1]); //本周最后一天

c.setTime(endday);
int weekthInt2 = c.get(Calendar.WEEK_OF_YEAR);  
String end2 = YHCreateTime.getFullTimeStr(YHCreateTime.getStartEnd(Integer.parseInt(endTime.substring(0,4)),weekthInt2)[1]);//本周的最后一天天

//得到相差多少天 
int spanDays = YHCreateTime.getIntervalDays(startDate,YHUtility.parseDate(end2)); 
List list2 = YHCreateTime.getDays(startDate, endday, spanDays);
Collections.sort(list2);
int startth = 0;
int endth = 0;
if (listP.size() > 0) {
for(int i = 0;i < list2.size();i++){
  if(statrTime.equals(String.valueOf(list2.get(i)).substring(0,10))){
    startth = i;
  }
  if(curDateStr.equals(String.valueOf(list2.get(i)).substring(0,10))){
    endth = i;
  }
}
}
int td1 = startth-0;
int td2 = endth-startth+1;
int td3 = spanDays-endth;

int days = YHCreateTime.getIntervalDays(startday, endday);
int week = 0;
if (list2.size()%7 != 0) {
  week = list2.size()/7 + 1;
}
if (list2.size()%7 == 0) {
  week = list2.size()/7;
}
if (week == 0) {
  week = 1;
}
int cols = week * 20;
%>
<html>
<head>
<title>进度图</title>
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
  function myOpen(seqid) {
    window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetai.act?seqId=" + seqid,"myOpenT","height=550,width=800,top=90,left=290,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=1");
  }
  function myOpen3(seqId) {
    window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHWorkDetailAct/selectDetaiId.act?seqId=" + seqId,"myOpen3","height=550,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
  }
  function workSelect2(seqid) {
    window.open("<%=contextPath%>/yh/core/funcs/workplan/act/YHPlanWorkAct/workSelect2.act?seqId=" + seqid,"myOpenN","height=650,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
  }
  function showOpen(seqid,name) {
    var param1 = "seqId=" + seqid + "&name=" + name;
    var param = encodeURI(param1);
    var url = contextPath + "/yh/core/funcs/workplan/act/YHWorkPersonAct/selectPerson.act?" + param;
    window.open(url,"showOpen","height=500,width=800,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=90,left=290,resizable=yes");
  }
  function showOpen2(i,seqid,name) {
    var num = i ;
    var prcs;
    if (document.getElementById(i).style.display == "none") {
      document.getElementById(i).style.display = "block";
      var param1 = "seqId=" + seqid + "&name=" + name;
      var param = encodeURI(param1);
      var requestUrl = contextPath + "/yh/core/funcs/workplan/act/YHWorkPersonAct/selectPerson2.act?" + param;
      var rtJson = getJsonRs(requestUrl);
      if(rtJson.rtState == "1") {
       alert(rtJson.rtMsrg); 
       return;
      }
      prcs = rtJson.rtData;
      if (prcs.length > 0) {
        var table = new Element('table',{ "class":"small" ,"cellspacing":"0","cellpadding":"3","border":"1","style":"border-collapse: collapse","width":"100%"}).update("<tbody id='tbody"+name+"'><tr align='center' class='TableHeader'>"
            + "<td width='120'>起始时间</td>"
            + "<td width='120'>结束时间</td>"
            + "<td>计划任务</td>"
            + "<td>附件</td>"
            + "<td>相关资源</td>"
            + "</tr><tbody>");
        $(i).update(table);
        for (var i = 0;i < prcs.length ;i++) {
          var prc = prcs[i];     
          var pbegeiDate = prc.pbegeiDate;
          var pendDate = prc.pendDate;
          var pplanContent = prc.pplanContent;
          var puseResource = prc.puseResource;
          var attachmentName = prc.attachmentName;
          var attachmentId = prc.attachmentId;
          var tr = new Element('tr',{"align":"center","class":"TableData","title":"状态"});
          $('tbody'+name).appendChild(tr);
          tr.update("<td>" + pbegeiDate + "</td>"
              + "<td>" + pendDate + "</td>"
              + "<td>" + pplanContent + "</td>"
              + "<td align='left'><input type='hidden' id='attachmentI_" + i + "' name='attachmentI_" + i + "' value='" + attachmentId + "'>"
              + "<input type='hidden' id='attachmentNam_" + i + "' name='attachmentNam_" + i + "' value='" + attachmentName + "'>"
              + "<span id='showAt_" + i + "'></span></td>"
              + "<td>" + puseResource + "</td>");
          attachMenuUtil("showAt_" + i ,"work_plan",null,$('attachmentNam_' + i).value ,$('attachmentI_' + i).value,true, i);
        }
      }
    } else {
      //location.reload();
      document.getElementById(i).style.display = "none";
    }
  }
  function backId(seqId) {
    window.open("<%=contextPath%>/core/funcs/workplan/show/note.jsp?seqId=" + seqId,"myOpen54","height=250,width=350,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=120,left=500,resizable=yes");
  }
  function doInit() {
    var lenght = parseInt($('count').value);
    for(var i = 0; i < lenght ; i++) {
      if($('attachmentName_' + i)) {
        attachMenuUtil("showAtt_" + i ,"work_plan",null,$('attachmentName_' + i).value ,$('attachmentId_' + i).value,true, i);
      }
    }
    doInit3();
    doInit4();
  }
  function doInit3() {
    var lenght = parseInt($('numCount').value);
    for (var i = 0; i < lenght ; i++) {
      var numCount1 = "leader1" + i;
      var numCounVlue1 = document.getElementById + "(leader1" + i + ")";
      
      if (numCounVlue1.value != "") {
        bindDesc([{cntrlId:numCount1, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
      }
    }
  }
  function doInit4() {
    var lenght = parseInt($('count').value);
    for(var i = 0; i < lenght ; i++) {
      var writerCoun = "writerName" + i; 
      var writerCounVlue = document.getElementById + "(writerName" + i + ")";
      if(writerCounVlue.value != ""){
        bindDesc([{cntrlId:writerCoun, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
      }
    }
  }
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/edit.gif" width="22" height="18">&nbsp;
  <span class="big3"> 进度图  (<%=plan.getName() %>&nbsp;&nbsp;<%if(plan.getStatrTime() != null){%><%=plan.getStatrTime() %><%} %>&nbsp;至&nbsp;<%=endTime%>)</span><br>
  </td>
 </tr>
</table>
<table align="center" style='border-collapse: collapse' border=1
  cellspacing=0 cellpadding=3 bordercolor='#000000' width="100%"
  class="TableList">
  <tr class="TableData" valign="middle">
    <td nowrap rowspan="3" align="center" width="120">姓名  </td>
    <%for (int i = 0; i< list2.size(); i ++) {%>
    <td nowrap colspan="7"><%=list2.get(i).toString().substring(0, 11) %></td>
  <%
  i = i+6;
   } %>
  </tr>
  <tr class="TableData" valign="top">
 <%for (int i = 0; i < list2.size(); i ++) {%>
    <td nowrap align="center" bgcolor="#cccccc" style="cursor: hand" title="<%=list2.get(i).toString().substring(0, 11) %>"><%=list2.get(i).toString().substring(8, 11) %></td>
   <%}%>
  </tr>
 <tr class="TableData" valign="top">
<%for (int i = 0; i < week; i ++) {%>
    <td nowrap align="center">日</td>
    <td  nowrap align="center">一</td>
    <td  nowrap align="center">二</td>
    <td  nowrap align="center">三</td>
    <td  nowrap align="center">四</td>
    <td  nowrap align="center">五</td>
    <td  nowrap align="center">六</td>
   <%} %>
  </tr>
  <%if (plan.getLeader1Desc() == null) { %><input type="hidden" value="0" id="numCount" name="numCount"><% }%>
 <%if (!YHUtility.isNullorEmpty(plan.getLeader1Desc())) {
   int num = plan.getLeader1Desc().split(",").length;%>
    <input type="hidden" value="<%=num%>" id="numCount" name="numCount">
   <%for (int i = 0; i < num; i++) {%>
 <tr class="TableData" valign="top">
    <td  nowrap width="120" align="center">
    <span id="leader1<%=i%>Desc"></span>
    <input type="hidden" name="leader1<%=i%>" id="leader1<%=i%>" value="<%=plan.getLeader1Desc().split(",")[i] %>">&nbsp;
    <a href="javascript:showOpen('<%=plan.getSeqId()%>','<%=plan.getLeader1Desc().split(",")[i]%>');">
    <img style="cursor: hand" src="<%=imgPath%>/edit.gif" title="指派工作计划" width="16px"></a> 
    <a href="javascript:showOpen2('<%=i %>','<%=plan.getSeqId()%>','<%=plan.getLeader1Desc().split(",")[i] %>');">
    <img style="cursor:pointer" src="<%=imgPath%>/search.gif" title="查看/关闭任务" width="14px"></a></td>  
    <%
    if (listP.size() > 0) {
       if(td1>0){
    %>
    <!--  怎么夸时间,div  background="<%=imgPath%>/finish.gif"-->
    <td class="small" align="center"  style="cursor:pointer" height="20" colspan="<%=td1%>" bgcolor="#cccccc"></td>
 <%} %>
    <td class="small" bgcolor="#cccccc" style="cursor:pointer" colspan="<%=td2+td3%>" height="20">
     <div>
    <table>
    <tr>
    <%
    int sunOne = 0;
    for (int k = -1; k < listP.size()-1 ; k++) {
      int temp = 0;
      if(k==-1){
        detail = listP.get(0);
        sunOne = 0;
      }else{
        detail = listP.get(k);
        sunOne = detail.getPercent();
      }
        temp = detail.getPercent();
        YHWorkDetail detail2 = listP.get(k+1);      
        if (plan.getLeader1Desc().split(",")[i].equals(detail2.getWriter())) {
            if(!plan.getLeader1Desc().split(",")[i].equals(detail.getWriter())){
              sunOne = 0;
            }
              for(int j = k+1;j<listP.size();j++){
                 detail = listP.get(j);
                 temp = detail.getPercent() - sunOne;
                break;
            }
              //sun2 = sun2 + temp; 
      %>
      <td background="<%=imgPath%>/finish.gif" height="20" class="borderTd">
      
      <a href="javascript:backId(<%=detail.getSeqId()%>);"><%=temp%>%&nbsp;</a>
      </td>
      <%}}%>
       </tr>
    </table>
      </div> 
      </td>
      <%
      }else{
      %>
   <td class="small" align="center"  style="cursor:pointer" height="20" colspan="<%=list2.size()%>"></td>
  <%} %>
  </tr>
   <!-- 计划任务的显示-->
 <tr>
 <td class="small" colspan="<%=list2.size()+1%>" id="<%=i%>" style="display:none"></td>
  </tr>
  
 <%}
 }%>
</table>
<br>
<%if (listP.size() > 0) {%>
<table align="center" style='border-collapse: collapse' border=1
  cellspacing=0 cellpadding=3 bordercolor='#000000' width="100%"
  class="small">
  <%
  if (sun2%leng <= 4 && sun2%leng > 0 && sun2 >=leng) {
    sunTotal = sun2/leng ;
  }
  if (sun2%leng > 4 && sun2 >=leng) {
    sunTotal = sun2/leng + 1;
  }
  if (sun2%leng == 0 && sun2 >=leng) {
    sunTotal = sun2/leng;
  }
  if (sun2 > 100) {
    sunTotal = 100;
  }
  %>
<tr>
<td width="40">总进度</td>
 <td class="small" align="center" style="cursor:pointer" background="<%=imgPath%>/finish.gif" width="<%=sunTotal%>%" height="20" title="完成<%=sunTotal%>"><%=sunTotal %>%</td>
  <td style="cursor:pointer" title="剩余<%=100-sunTotal%>%"></td>
  </tr>
</table>
<%
}if (list.size() > 0) {
%>

<table class="TableList"  align="center" width="100%" border="0" cellspacing="0" cellpadding="3">
<tr class="TableHeader"><td colspan="4" align="center"><font size="3">领导批注信息</font></td></tr>
<tr align='center' class="TableLine1"><td>批注领导</td>
<td width='120' align='center'>批注内容</td>
<td>附件</td>
<td width='120' align='center'>批注时间</td></tr>
 <% for (int i =0; i < list.size(); i++) {
    detail = list.get(i);
 %>
<tr align="center" class="TableLine1" title="批注">
<td><%if(detail.getWriter() != null){%>
<div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="<%=detail.getWriter()%>">
    <%
   }else {
 %>
 <div id="writerName<%=i%>Desc"></div>
    <input type="hidden" name="writerName<%=i%>" id="writerName<%=i%>" value="">
 <%} %>
</td>
<td><%if(detail.getProgress() != null){%><%=detail.getProgress() %><%} %></td>
<td><%if(detail.getAttachmentName() != null){%>
<input type="hidden" id="attachmentId_<%=i %>" name="attachmentId" value="<%=detail.getAttachmentId()%>">
        <input type="hidden" id="attachmentName_<%=i %>" name="attachmentName" value="<%=detail.getAttachmentName() %>">
        <input type="hidden" id="ensize" name="ensize">
        <span id="showAtt_<%=i %>"></span>
<%}%>
</td>
<td align='center'><%if(detail.getWriteTime() != null){%><%=detail.getWriteTime() %><%} %></td>
</tr>
<%} %>
 </table>
 <%} else {%>
<table class="MessageBox" align="center" width="340">
<tr><td class='msg info'>
<div class='content' style='font-size:12pt'>没有批注内容</div>
</td></tr></table>
<%} %>


<!--  &nbsp;<input type="button" value="撰写进度日志" class="BigButtonC"> -->
<div id="listDiv" align="center"></div>
<div align="center">
<%if(flag) { 
  if(plan.getEndTime() != null && (time1 > endTime1 || (time1 == endTime1 && time2 > endTime2) || (time1 == endTime1 && time2 == endTime2 && time3 >= endTime3)) && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))){%>
<input type="button" value="撰写进度日志" class="BigButtonC" onClick="myOpen3('<%=request.getParameter("seqid")%>');">
<%}if(plan.getEndTime() == null && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))){%>
  <input type="button" value="撰写进度日志" class="BigButtonC" onClick="myOpen3('<%=request.getParameter("seqid")%>');">
<%}}%>&nbsp;
  <%if(fla && (beginTime1 < endTime1 || (beginTime1 == endTime1 && beginTime2 < endTime2) || (beginTime1 == endTime1 && beginTime2 == endTime2 && beginTime3 <= endTime3))){%><input type="button" value="领导批注"
  class="BigButton" onClick="myOpen(<%=request.getParameter("seqid")%>);"> <%}%>&nbsp;<input
  type="button" value="工作计划详情" class="BigButtonC"
  onclick="workSelect2(<%=request.getParameter("seqid")%>);">
&nbsp;<input type="button" value="刷新" class="BigButton"
  onclick="location.reload();"> &nbsp;<input type="button"
  value="关闭" class="BigButton" onClick="window.close();"></div>
<input type='hidden' id=count name=count value="<%=list.size() %>"/>
</body>
</html>