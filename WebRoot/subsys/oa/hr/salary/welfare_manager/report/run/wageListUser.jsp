<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
<%@ page  import="yh.subsys.oa.hr.salary.submit.data.*"%> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.util.Map"%>
<html>
<head>
<%
  boolean isreport = false;
  int yesOther = 0;
	String userId = request.getParameter("userId");
	if(userId == null){
	  userId = "";
	}
	List<YHSalItem> listSalItem = (List<YHSalItem>)request.getAttribute("listSalItem");
	YHSalPerson person = (YHSalPerson)request.getAttribute("person");
	String title = "";
  for(int i=0; i<listSalItem.size(); i++){
    if(listSalItem.get(i).getIscomputer().equals("0") && listSalItem.get(i).getIsreport().equals("1")){
      title += "S" + listSalItem.get(i).getSlaitemId() + ",";
    }
  }
%>
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
var yesOther = "<%=yesOther%>";
function doInit(){
	$('userName').innerHTML = staffNameFunc("<%=userId%>");
	$('salMonth').innerHTML = window.parent.salMonth;
	$('flowId').value = window.parent.flowId;
	doAttendance(<%=userId%>);
  $$('input').each(function(e, i) {
    if (e.type != 'text') {
      return;
    }
    e.onkeyup = function() {
      var data = e.value;
      if(isNaN(data)){
        e.value = '';
        return;
      }
    return;
    }
  });
}

/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(cellData){
  var url = contextPath + "/yh/subsys/oa/hr/manage/leave/act/YHHrStaffLeaveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
	var pars = Form.serialize($('form1')); 
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/setReportUserInfo.act"; 
	var json = getJsonRs(url,pars); 
	if(json.rtState == "0"){ 
		window.location = "<%=contextPath %>/subsys/oa/hr/salary/report/run/newRemind2.jsp?userId=<%=userId%>&flowId="+window.parent.flowId;
	}else{ 
	  alert("信息修改失败"); 
	}
}

function doAttendance(userId){
  var url = contextPath + "/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/getAttendance.act";
  var rtJson = getJsonRs(url, "userIdStr="+userId+"&flowId="+$('flowId').value);
 
  if(rtJson.rtState == "0") {
    var data = rtJson.rtData;
    var temp2 = '';
    temp2 += '上下班未登记'+data.attendCount+'次；';
    if(data.lateCount!=0) temp2 += '迟到'+data.lateCount+'次；';
    if(data.ealyCount!=0) temp2 += '早退'+data.ealyCount+'次；';
    if(data.outCount!=0) temp2 += '外出'+data.outCount+'次；';
    if(data.leaveCount!=0) temp2 += '请假'+data.leaveCount+'次；';
    if(data.evectionCount!=0) temp2 += '出差'+data.evectionCount+'次；';
    if(data.overtimeCount!=0) temp2 += '加班'+data.overtimeCount+'次；';
    
    if(data.awardCount!=0 || data.punishCount!=0) temp2 += '奖励'+data.awardCount+'惩罚'+data.punishCount+'；';
    if(data.calCount!=0) temp2 += '日程共'+data.calCount+'完成'+data.calCount1+'；';
    if(data.diaryCount!=0) temp2 += '日志'+data.diaryCount+'篇；';
    if(data.checkCount!=0) temp2 += '考核'+data.checkCount+'次；';
    $('showUser').innerHTML = temp2;
  } 
  else {
    alert(rtJson.rtMsrg); 
  }
}
function userInfo(userId){
  var url = "<%=contextPath %>/subsys/oa/hr/salary/report/detail/staff_detail.jsp?userId="+userId+"&flowId="+$('flowId').value;
  newWindow(url,800,500);
}

/**
 * 打开新窗口  newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}
</script>
</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/salary.gif" align="middle"><span class="big3"> <span class="big3" id="userName" name="userName"></span>绩效上报（<span class="big3" id="salMonth" name="salMonth"></span>）</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<form id="form1" name="form1" method="post" action="">
<table align="center" id="cal_table" class="TableBlock" >
  <input type="hidden" id="userId" name="userId" value="<%=userId %>">
  <input type="hidden" id="flowId" name="flowId" value="">
  <input type="hidden" id="title" name="title" value="<%=title%>">
  <input type="hidden" id="ITEM_COUNT" name="ITEM_COUNT" value="<%=listSalItem.size() %>">
	<tbody>
	  <%
      List<String> slist = person.getSlist();
      Map<String,Double> smap = person.getSmap();
	    if(listSalItem != null && listSalItem.size() > 0){
      //部门上报项
        for(int i = 0; i < listSalItem.size(); i++){ 
          if(listSalItem.get(i).getIscomputer().equals("0") && listSalItem.get(i).getIsreport().equals("1")){
            if(isreport == false){
              isreport = true;
            %>  
     <tr align="center" class="TableHeader">
       <td nowrap="" width="120px"><b>工资项目</b></td>
       <td nowrap="" width="300px" align="right"><b style="padding-right:180px">金额</b></td>
     </tr>
          <% }%>
           
     <tr align="center" class="TableLine1">
     <% 
        String s = (String)slist.get(i);
        double value = smap.get(s);
        String valueStr = YHUtility.getFormatedStr(value,1);
     %>
       <td nowrap="" width="120px"><span id="<%=s %>_index" name="<%=s %>_index"><%=listSalItem.get(i).getItemName() %></span></td>
       <td nowrap="" width="300px" align="left"><input type="text" size="16" value="<%=valueStr %>" class="SmallInput" name="<%=s %>" id="<%=s %>" style="text-align: right;"></td>
     </tr>
      <%}
      }
    }%>
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">备注</td>
       <td nowrap="" width="300px" align="left">
         <textarea rows="3" cols="27" id="memo" name="memo" class="BigInput"><%=person.getMemo() == null ? "" : person.getMemo()%></textarea>
       </td>       
     </tr> 
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">员工表现</td>
       <td nowrap="" width="300px" align="left">
         <span id="showUser" name=""></span>
       </td>       
     </tr> 
     <tr align="center" class="TableLine1">
       <td nowrap="" width="120px">相关操作</td>
       <td nowrap="" width="300px" align="left">
         <a href="javaScript:userInfo(<%=person.getUserId() %>)">员工表现详情</a>
       </td>       
     </tr> 
	</tbody>
</table>
<br>
<div align="center">
  <input type="button" value="确 定" class="BigButton" onclick="doSubmit()">
</div>
</form>
</body>
</html>