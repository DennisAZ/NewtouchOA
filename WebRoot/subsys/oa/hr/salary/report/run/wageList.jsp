<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
<%@ page  import="yh.subsys.oa.hr.salary.submit.data.*"%> 
<%@ page  import="java.util.List"%>
<%@ page  import="java.util.Map"%>
<html>
<head>
<%
	String deptId = request.getParameter("deptId");
	if(deptId == null){
	  deptId = "";
	}
	List<YHSalItem> listSalItem = (List<YHSalItem>)request.getAttribute("listSalItem");
	List<YHSalPerson> listPerson = (List<YHSalPerson>)request.getAttribute("listPerson");
	String title = "";
  for(int i=0; i<listSalItem.size(); i++){
    title += "S" + listSalItem.get(i).getSlaitemId() + ",";
  }
%>
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
	getOrgDescFunc();
	$('salMonth').innerHTML = window.parent.salMonth;
	$('flowId').value = window.parent.flowId;
	var total = $('total').value;
	for(var i = 0; i < total; i++){
		var userId = $('userId_'+i).value;
		doAttendance(i,userId);
	}
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

function doSubmit(){
	var pars = Form.serialize($('form1')); 
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/setReportInfo.act"; 
	var json = getJsonRs(url,pars); 
	if(json.rtState == "0"){ 
		window.location = "<%=contextPath %>/subsys/oa/hr/salary/report/run/newRemind.jsp?deptId=<%=deptId%>";
	}else{ 
	  alert("信息修改失败"); 
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

function getOrgDescFunc(){
  bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
}

function doAttendance(i,userId){
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
    $('show_'+i).innerHTML = temp2;
  } 
  else {
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td>
      <input type="hidden" id="deptId" name="deptId" value="<%=deptId%>">
      <img src="<%=imgPath %>/salary.gif" align="middle"><span class="big3"> <span class="big3" id="deptIdDesc" name="deptIdDesc"></span>绩效上报（<span class="big3" id="salMonth" name="salMonth"></span>）</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<form id="form1" name="form1" method="post" action="">
<table align="center" id="cal_table" class="TableBlock" >
  <input type="hidden" id="flowId" name="flowId">
  <input type="hidden" id="total" name="total" value="<%=listPerson.size()%>">
  <input type="hidden" id="title" name="title" value="<%=title%>">
	<tbody>
	  <tr align="center" class="TableHeader">
	    <td nowrap="" width="100px"><b>姓名</b></td>
	    <%if(listSalItem!=null && listSalItem.size()>0){
	    	 for(int i=0; i<listSalItem.size(); i++){%>  
	    <td nowrap="" width="100px" align="center" style="cursor:pointer" onclick=""><b><%=listSalItem.get(i).getItemName() %></b></td>
	    <%}
	    }%>
	    <td nowrap="" width="100px" align="center" style="cursor:pointer" onclick=""><b>员工表现</b></td>
      <td nowrap="" width="100px" align="center" style="cursor:pointer" onclick=""><b>相关操作</b></td>
	  </tr>
	    <%if(listPerson!=null && listPerson.size()>0){ 
	        for(int i = 0; i < listPerson.size(); i++){%>
	  <tr align="center" class="TableLine1">
      <td nowrap="" ><%=listPerson.get(i).getUserName() %>
        <input type="hidden" id="sdId_<%=i %>" name="sdId_<%=i %>" value="<%=listPerson.get(i).getSdId() %>">
        <input type="hidden" id="hsdId_<%=i %>" name="hsdId_<%=i %>" value="<%=listPerson.get(i).getHsdId() %>">
        <input type="hidden" id="userId_<%=i %>" name="userId_<%=i %>" value="<%=listPerson.get(i).getUserId() %>">
      </td>
        <%  List<String> slist = listPerson.get(i).getSlist();
            Map<String,Double> smap = listPerson.get(i).getSmap();
            for(int j = 0; j < slist.size(); j++){
              String s = (String)slist.get(j);
              double value = smap.get(s);
              String valueStr = YHUtility.getFormatedStr(value,1);%>
      <td nowrap="" align="center"><input type="text" size="10" value="<%=valueStr %>" class="SmallInput" name="<%=s %>_<%=i %>" id="<%=s %>_<%=i %>" style="text-align: right;"></td>
          <%}%>
      <td nowrap="" align="left"><span id="show_<%=i %>" name=""></span></td>
      <td nowrap="" align="center"><a href="javaScript:userInfo(<%=listPerson.get(i).getUserId() %>)">员工表现详情</a></td>
	  </tr>
	      <%} 
      }%>    
	</tbody>
</table>
<br>
<div align="center">
  <input type="button" value="确 定" class="BigButton" onclick="doSubmit()">
</div>
</form>
</body>
</html>