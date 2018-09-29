<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String flowId = request.getParameter("flowId"); 
  String seqId = request.getParameter("seqId"); 
  if (YHUtility.isNullorEmpty(seqId)) {
    seqId = "";
  }
%>
<title>设置定时启动任务</title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js" ></script>
<script type="text/javascript">
var aff_type="day";
var selItem = 1;
var seqId = '<%=seqId %>';
function sel_change()
{
   if(aff_type != "")
      document.getElementById(aff_type).style.display="none";
   if(form1.TYPE.value=="1")
      aff_type="once";
   if(form1.TYPE.value=="2")
      aff_type="day";
   if(form1.TYPE.value=="3")
      aff_type="week";
   if(form1.TYPE.value=="4")
      aff_type="mon";
   if(form1.TYPE.value=="5")
      aff_type="year";
   document.getElementById(aff_type).style.display="";
}
function doInit(){
  var date = new Date();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  var week = date.getDay();
  if (seqId) {
    var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowTimerAct/getTimer.act";      
    var json = getJsonRs(url ,"seqId=" + seqId);   
    if(json.rtState == "0"){ 
      var data = json.rtData;
      var type = data.type;
      $('TYPE').value = type;
      
      var privUser = data.privUser;
      var privUserName = data.privUserName;

      $('privUserName').innerHTML = privUserName;
      $('privUser').value = privUser;
      
      var remindDate = data.remindDate;
      var remindTime = data.remindTime;

      if (type == '1') {
        var time = remindDate + " " + remindTime;
        $('REMIND_TIME1').value = time;
        aff_type="once";
        selItem = 0
      } else if (type == '2') {
        $('REMIND_TIME2').value = remindTime;
        aff_type="day";
        selItem = 1
      } else if (type == '3') {
        week = remindDate;
        $('REMIND_TIME3').value = remindTime;
        aff_type="week";
        selItem = 2
      } else if (type == '4') {
        day = remindDate;
        $('REMIND_TIME4').value = remindTime;
        aff_type="mon";
        selItem = 3
      } else if (type == '5') {
        mon = remindDate.split("-")[0];
        day = remindDate.split("-")[1];
        $('REMIND_TIME5').value = remindTime;
        aff_type="year";
        selItem = 4
      }
    } 
  }
  
  
  document.getElementById("day").style.display="none";
  document.getElementById(aff_type).style.display="";
  document.form1.TYPE.selectedIndex=selItem;
  
  var beginParameters = {
      //时间接收input框的id
      inputId:'REMIND_TIME1',
      property:{isHaveTime:true}
  };
  new Calendar(beginParameters);

  
  createMon(mon , "REMIND_DATE5_MON");
  createDay(day, "REMIND_DATE5_DAY");
  createDay(day , "REMIND_DATE4");
  createWeek(week , "REMIND_DATE3");
}
function check_form()
{
	if(document.form1.privUser.value=="")
	{
		alert("发起人不能为空！");
		return(false);
	}
	var type = document.form1.TYPE.value;
	if(document.getElementsByName("REMIND_TIME"+type)[0].value == "")
	{
	    alert("提醒时间不能为空！");
		return(false);
	}
	return (true);
}
function clearUser(input, hidden){
  if($(input).tagName == 'INPUT'){
    $(input).value = "";  
  }else if($(input).tagName == 'TEXTAREA'){
    $(input).innerHTML = '';  
  }
  $(hidden).value = "";
}
function createMon(val , ctrl) {
  var note = $(ctrl);
  for (var i = 0 ;i < 12 ;i++) {
    var o = new Element("option" , {"value":i+1});
    o.update((i+1) + "月");
    if (i+1 == val) {
      o.selected = true;
    }
    note.appendChild(o);
  }
}
function getWeek(i) {
  var tmp = "星期日";
  if ( i == 1) {
    tmp = "星期一";
  }else if ( i == 2) {
    tmp = "星期二";
  }else if ( i == 3) {
    tmp = "星期三";
  }else if ( i == 4) {
    tmp = "星期四";
  }else if ( i == 5) {
    tmp = "星期五";
  }else if ( i == 6) {
    tmp = "星期六";
  }
  return tmp;
}
function createWeek(val , ctrl) {
  var note = $(ctrl);
  for (var i = 1 ;i < 7 ;i++) {
    var o = new Element("option" , {"value":i});
    var tmp = ""
    o.update(getWeek(i));
    if (i == val) {
      o.selected = true;
    }
    note.appendChild(o);
  }

  var o = new Element("option" , {"value":0});
  o.update(getWeek(0));
  if (0 == val) {
    o.selected = true;
  }
  note.appendChild(o);
}
function createDay(val , ctrl) {
  var note = $(ctrl);
  for (var i = 1 ;i < 32 ;i++) {
    var o = new Element("option" , {"value":i});
    o.update(i + "日");
    if (i == val) {
      o.selected = true;
    }
    note.appendChild(o);
  }
}
function saveTimer() {
  if (check_form()) {
    var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowTimerAct/saveTimer.act";      
    var json = getJsonRs(url , $('form1').serialize());   
    if(json.rtState == "0"){  
      alert("保存成功！");
      location.href= contextPath + "/core/funcs/workflow/flowtype/set_timer/index.jsp?flowId=<%=flowId %>";
    }else{        
      alert(json.rtMsrg);      
    } 
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">
<form  method="post" name="form1" id="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="../img/task.gif" align="absMiddle" align="absmiddle"><span class="big3">设置定时启动任务</span>
    </td>
  </tr>
</table>

<br>
 <table class="TableBlock" width="480" align="center">
     <tr style="display:none">
      <td nowrap class="TableData">流程名称：</td>
      <td class="TableData">
      <input type="hidden" name="flowId" id="flowId" value="<%=flowId %>"/>&nbsp;
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">发起人：</td>
      <td class="TableData">
	      <input type="hidden" name="privUser"  id="privUser" value="">
        <textarea cols="35" name="privUserName" id="privUserName" rows="8" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['privUser','privUserName'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('privUserName', 'privUser')">清空</a>
      </td>
    </tr>
	<tr>
      <td nowrap class="TableData">提醒类型：</td>
      <td class="TableData">
        <select name="TYPE" id="TYPE" class="BigSelect" onChange="sel_change()">
        	<option value="1">只运行一次</option>
          <option value="2">按日发起</option>
          <option value="3">按周发起</option>
          <option value="4">按月发起</option>
          <option value="5">按年发起</option>
        </select>
      </td>
    </tr>
    <tr id="day">
      <td nowrap class="TableData"> 发起时间：</td>
      <td class="TableData">
        <input name="REMIND_TIME2" ID="REMIND_TIME2" size="10" class="BigInput" value="" onClick="showTime(this, false)">
      </td>
    </tr>
    <tr id="once" style="display:none">
      <td nowrap class="TableData"> 发起时间：</td>
      <td class="TableData">
        <input name="REMIND_TIME1" id="REMIND_TIME1" size="20" class="BigInput" value="" onClick="">&nbsp;&nbsp;为空为当前时间
      </td>
    </tr>
    <tr id="week" style="display:none">
      <td nowrap class="TableData"> 发起时间：</td>
      <td class="TableData">
        <select name="REMIND_DATE3" id="REMIND_DATE3" class="BigSelect">
        </select>&nbsp;&nbsp;
        <input name="REMIND_TIME3" id="REMIND_TIME3" size="10" class="BigInput" value="" onClick="showTime(this, false)">
      </td>
    </tr>
    <tr id="mon" style="display:none">
      <td nowrap class="TableData"> 发起时间：</td>
      <td class="TableData">
        <select name="REMIND_DATE4" id="REMIND_DATE4" class="BigSelect">
        </select>&nbsp;&nbsp;
        <input name="REMIND_TIME4" id="REMIND_TIME4" size="10" class="BigInput" value="" onClick="showTime(this, false)">
      </td>
    </tr>
    <tr id="year" style="display:none">
      <td nowrap class="TableData"> 发起时间：</td>
      <td class="TableData">
        <select name="REMIND_DATE5_MON" id="REMIND_DATE5_MON" class="BigSelect">
        </select>&nbsp;&nbsp;
        <select name="REMIND_DATE5_DAY" id="REMIND_DATE5_DAY" class="BigSelect">
        </select>&nbsp;&nbsp;
        <input name="REMIND_TIME5" id="REMIND_TIME5" size="10" class="BigInput" value="" onClick="showTime(this, false)">
      </td>
    </tr>
	<tr>
	   <td colspan="2" align="center"><input type="button" class="SmallButton" value="<% if (YHUtility.isNullorEmpty(seqId)) {  %>增加<% }else{  %>保存<% } %>" onclick="saveTimer()"/>&nbsp;
	   	<input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
	   	<input type="button" class="SmallButton" value="返回" onclick="location='index.jsp?flowId=<%=flowId %>'"/></td>
	</tr>
  </table>
</form>
</body>
</html>