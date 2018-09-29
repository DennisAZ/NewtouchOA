<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
<%
Date date=new Date();
SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
String today=matter.format(date);
String monthFirstDate=today.substring(0,8)+"01";


%>
<html>
<head>
<title>考勤统计</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>

<script>
function doInit(){
  $("startDate").value="<%=monthFirstDate%>";
  $("endDate").value="<%=today%>";
  getDutyType();
  deptFunc();
  setDate();
}

function getDutyType(){       
	   var url = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageQueryAct/getDutyType.act"
	    var rtJson = getJsonRs(url);
	    if(rtJson.rtState == "0"){
	      var data=rtJson.rtData.data;
		      for(var i=0;i<data.length;i++){
		    	  jQuery("#dutyType").append("<option value='"+data[i].type+"'>"+data[i].name+"</option>");
			      }
	    }else{
	      alert(rtJson.rtMsrg); 
	    }
}

function deptFunc(){
	  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/selectDeptToAttendance.act";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "1"){
	    alert(rtJson.rtMsrg); 
	    return ;
	  }
	  var userId = rtJson.rtMsrg;
	  var prcs = rtJson.rtData;
	  var selects = document.getElementById("deptId");
	  for(var i=0;i<prcs.length;i++){
	    var prc = prcs[i];
	    var option = document.createElement("option"); 
	    option.value = prc.value; 
	    option.innerHTML = prc.text; 
	    selects.appendChild(option);
	  }
	  return userId;
	}


function CheckForm1()
{
   if(document.form1.startDate.value=="")
   { alert("起始日期不能为空！");
     return (false);
   }

   if(document.form1.endDate.value=="")
   { alert("截止日期不能为空！");
     return (false);
   }

   return (true);
}



//日期
function setDate(){
  var date1Parameters = {
    inputId:'startDate',
    property:{isHaveTime:false},
    bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
		    inputId:'endDate',
		    property:{isHaveTime:false},
		    bindToBtn:'date2'
		  };
		  new Calendar(date2Parameters);
}

</script>
</head>
<body onLoad="doInit();" class="bodycolor" topmargin="5">
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 考勤统计</span>
    </td>
  </tr>
</table>
<br><br><br>
<table align="center" class="TableList" width=450>
<form action="search.jsp" name="form1" onsubmit="return CheckForm1();">
<tr class=TableHeader >
<td colspan=2>
考勤统计
</td>
</tr>
<tr>
<td class="TableContent">
部门
</td>
<td class="TableData">
<select name="deptId" id="deptId" class="BigSelect">
<option value="ALL_DEPT">所有部门</option>
  </select>
</td>
<tr>
<td class="TableContent">
排班类型
</td>
<td class="TableData">
 <select id="dutyType" name="dutyType" class="BigSelect">
 <option value="ALL_TYPE">所有类型</option>
  </select>
</td>
</tr>
<tr>
<td class="TableContent">
起始日期
</td>
<td class="TableData">
<input type="text" readOnly name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="" onClick="WdatePicker()"/>
<img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
</td>
</tr>
<tr>
<td class="TableContent">
截止日期
</td>
<td class="TableData">
<input type="text" readOnly name="endDate"  id="endDate" size="10" maxlength="10" class="BigInput" value="" onClick="WdatePicker()"/>
<img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
</td>
</tr>
<tr class="TableControl">
<td colspan=2 align=center>
<input type="submit" value="考勤统计" class="BigButton" title="考勤统计">
</td>
</tr>
</table>
</form>

</body>
</html>