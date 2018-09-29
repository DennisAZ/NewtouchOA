<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@ include file="/core/inc/header.jsp" %>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy年MM月");
String date=curTime.format(new Date());
String USER_ID=request.getParameter("userId");
String FLOW_ID=request.getParameter("flowId");
%>




<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<title>员工表现详情</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" >
  var startTime="";
  var endTime="";


  function doInit(){
    doUserNameFunc();
    doAttendance();
  
  }

//获取员工姓名
function doUserNameFunc(){ 
  var name='';
  var url = contextPath + "/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=<%=USER_ID%>");
 
  if (rtJson.rtState == "0") {
     name= rtJson.rtData ;
  } else {
    alert(rtJson.rtMsrg); 
  }
  
  $('staffName').innerHTML=name;

}

function doAttendance(){
    
  var url = contextPath + "/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/getAttendance.act";
  var rtJson = getJsonRs(url, "userIdStr=<%=USER_ID%>&flowId=<%=FLOW_ID%>");
 
   if (rtJson.rtState == "0") {
      var data=rtJson.rtData;
	  if(data.lateCount!=0) data.lateCount="<font color='red'>"+data.lateCount+"</font>";
	  if(data.ealyCount!=0) data.ealyCount="<font color='red'>"+data.ealyCount+"</font>";
	  if(data.outCount!=0) data.outCount="<font color='red'>"+data.outCount+"</font>";
	  if(data.leaveCount!=0) data.leaveCount="<font color='red'>"+data.leaveCount+"</font>";
	  if(data.evectionCount!=0) data.evectionCount="<font color='red'>"+data.evectionCount+"</font>";
	  if(data.overtimeCount!=0) data.overtimeCount="<font color='red'>"+data.overtimeCount+"</font>";
	  if(data.awardCount!=0) data.awardCount="<font color='red'>"+data.awardCount+"</font>";
	  if(data.punishCount!=0) data.punishCount="<font color='red'>"+data.punishCount+"</font>";
	  if(data.calCount!=0) data.calCount="<font color='red'>"+data.calCount+"</font>";
	  if(data.calCount1!=0) data.calCount1="<font color='red'>"+data.calCount1+"</font>";
	  if(data.diaryCount!=0) data.diaryCount="<font color='red'>"+data.diaryCount+"</font>";
	  if(data.checkCount!=0) data.checkCount="<font color='red'>"+data.checkCount+"</font>";

	  bindJson2Cntrl(data); 
	  startTime=data.startDate;
	  endTime=data.endDate;
	 
	  //显示月份
	  var date=startTime.replace('-','年').replace('-','月').substr(0,8);
	  $('date').innerHTML=date;
     } else {
     alert(rtJson.rtMsrg); 
  }
}

function openDetail(index){
  if(index==1){ //迟到
  window.open('selectduty.jsp?userId=<%=USER_ID%>&startTime='+startTime+'&endTime='+endTime,"",'height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
  else if(index==2){//早退
  window.open('selectduty.jsp?userId=<%=USER_ID%>&startTime='+startTime+'&endTime='+endTime,"",'height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
  else if(index==3){//外出
  window.open('out.jsp?userId=<%=USER_ID%>&flowId=<%=FLOW_ID%>','','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
  else if(index==4){//请假
  window.open('leave.jsp?userId=<%=USER_ID%>&flowId=<%=FLOW_ID%>','','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
  else if(index==5){//出差
  window.open('evection.jsp?userId=<%=USER_ID%>&flowId=<%=FLOW_ID%>','','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
  else if(index==6){//加班
  window.open('overtime.jsp?userId=<%=USER_ID%>&flowId=<%=FLOW_ID%>','','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
 
  else if(index==7){//奖励
  window.open('award.jsp?userId=<%=USER_ID%>&startTime='+startTime+'&endTime='+endTime,'','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
 else if(index==8){//惩罚
  window.open('punish.jsp?userId=<%=USER_ID%>&startTime='+startTime+'&endTime='+endTime,'','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
 else if(index==9){//日志
  window.open('diary.jsp?userId=<%=USER_ID%>&startTime='+startTime+'&endTime='+endTime,'','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }
 else if(index==10){//考核
  window.open('score.jsp?userId=<%=USER_ID%>&startTime='+startTime+'&endTime='+endTime,'','height=700,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
  }


}






</script>

<body class="bodycolor" topmargin="5" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="17" height="17"><span class="big3"><span id="staffName"></span>表现详情(<span id="date"></span>)</span><br>
    </td>
  </tr>
</table>

<table class="TableBlock" width="90%" align="center">
  <tr>
    <td nowrap align="left" class="TableContent">项目</td>
    <td nowrap align="left" class="TableContent" >子项目</td>
    <td nowrap align="center" class="TableContent">次数</td>
    <td nowrap align="center" class="TableContent"  >描述</td>
  </tr>
  <tr>
    <td nowrap align="left" rowspan=6 class="TableContent">考勤</td>
    <td nowrap align="left" class="TableContent" >迟到</td>
    <td nowrap align="center" class="TableData" ><div id="lateCount"></div></td>
    <td nowrap align="center" class="TableData" ><a  href="javascript:openDetail(1);" >明细</a></td>
  </tr>
  <tr>
  	<td nowrap align="left" class="TableContent" >早退</td>  
    <td nowrap align="center" class="TableData" ><div id="ealyCount"></div></td>
    <td nowrap align="center" class="TableData" ><font color="#0066CC"><a  href="javascript:openDetail(2);" >明细</a></font></td>
  </tr>
  <tr>
  	<td nowrap align="left" class="TableContent" >外出</td>
    <td nowrap align="center" class="TableData" ><div id="outCount"></div></td>
    <td nowrap align="center" class="TableData" ><font color="#0066CC"><a  href="javascript:openDetail(3);" >明细</a></font></td>
  </tr>
  <tr>
  	<td nowrap align="left" class="TableContent" >请假</td>
    <td nowrap align="center" class="TableData" ><div id="leaveCount"></div></td>
    <td nowrap align="center" class="TableData" ><font color="#0066CC"><a  href="javascript:openDetail(4);" >明细</a></font></td>
  </tr>
  <tr>
  	<td nowrap align="left" class="TableContent" >出差</td>  
    <td nowrap align="center" class="TableData" ><div  id="evectionCount"></div></td>
    <td nowrap align="center" class="TableData" ><font color="#0066CC"><a  href="javascript:openDetail(5);" >明细</a></font></td>
  </tr>
  <tr>
  	<td nowrap align="left" class="TableContent" >加班</td>  
    <td nowrap align="center" class="TableData" ><div id="overtimeCount"></div></td>
    <td nowrap align="center" class="TableData" ><a  href="javascript:openDetail(6);" >明细</a></td>
  </tr>
  <tr>
    <td nowrap align="left" rowspan=2 class="TableContent">奖惩</td>
    <td nowrap align="left" class="TableContent" >奖励</td>  
    <td nowrap align="center" class="TableData" ><div id="awardCount"></div></td>
    <td nowrap align="center" class="TableData" ><a  href="javascript:openDetail(7);" >明细</a></td>
  </tr>
  <tr>
  	<td nowrap align="left" class="TableContent" >惩罚</td>
    <td nowrap align="center" class="TableData" ><div id="punishCount"></div></td>
    <td nowrap align="center" class="TableData" ><a  href="javascript:openDetail(8);" >明细</a></td>
  </tr>
  <tr>
    <td nowrap align="left" rowspan=2 class="TableContent">日程</td>
    <td nowrap align="left" class="TableContent" >共计</td>  
    <td nowrap align="center" class="TableData" ><div id="calCount"></div></td>
    <td nowrap align="center" class="TableData" ></td>
  </tr>
  <tr>
  	<td nowrap align="left" class="TableContent" >已完成</td>
    <td nowrap align="center" class="TableData" ><div id="calCount1"></div></td>
    <td nowrap align="center" class="TableData" ></td>
  </tr>
  <tr>
    <td nowrap align="left"  class="TableContent">日志</td>
    <td nowrap align="left" class="TableContent" >共计</td>  
    <td nowrap align="center" class="TableData" ><div id="diaryCount"></div></td>
    <td nowrap align="center" class="TableData" ><a  href="javascript:openDetail(9);" >明细</a></td>
  </tr>
  <tr>
    <td nowrap align="left"  class="TableContent">考核</td>
    <td nowrap align="left" class="TableContent" >共计</td>  
    <td nowrap align="center" class="TableData" ><div id="checkCount"></div></td>
    <td nowrap align="center" class="TableData" ><a  href="javascript:openDetail(10);" >明细</a></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>

</html>
