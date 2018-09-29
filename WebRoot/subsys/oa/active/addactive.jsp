<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*,yh.core.funcs.calendar.act.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat  dateFormat2 = new SimpleDateFormat("yyyy年-MM月-w周-dd日");
  SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormat4 = new SimpleDateFormat("MM/dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  String curDateStr = dateFormat1.format(date);
  String dateStr = request.getParameter("date");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建活动安排</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style>
.DayCalendar{background:#D6E4EF;border:1px #E4ECF3 solid;position:relative;margin: 1px 0px;padding:0px 3px;}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function SelectUser1(MODULE_ID,TO_ID, TO_NAME, MANAGE_FLAG,FORM_NAME)
{
  URL="/module/user_select?MODULE_ID="+MODULE_ID+"&TO_ID="+TO_ID+"&TO_NAME="+TO_NAME+"&MANAGE_FLAG="+MANAGE_FLAG+"&FORM_NAME="+FORM_NAME+"&DEPT_ID=2";
  loc_y=loc_x=200;
  if(is_ie)
  {
     loc_x=document.body.scrollLeft+event.clientX-100;
     loc_y=document.body.scrollTop+event.clientY+170;
  }
  LoadDialogWindow(URL,self,loc_x, loc_y, 400, 350);//这里设置了选人窗口的宽度和高度
}
function ClearUser1(TO_ID, TO_NAME)
{
  if(TO_ID=="" || TO_ID=="undefined" || TO_ID== null)
  {
     TO_ID="TO_ID";
     TO_NAME="TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value="";
  document.getElementsByName(TO_NAME)[0].value="";
}
 
function CheckForm(){
  var active = $("activeTime");
  if(active.value==""){ 
    alert("周活动时间不能为空！");
    active.focus();
    active.select();
    return (false);
  }
   if(!isValidDateStr(active.value)){
     alert("起始日期格式不对,应形如 2010-02-01");
     active.focus();
     active.select();
     return false;
   }
  if(document.form1.activeUser.value==""){ 
    alert("出席人员不能为空！");
    return (false);
  }
   if(document.form1.activeContent.value==""){ 
     alert("内容不能为空！");
     return (false);
   }
 
   return (true);
}
function doOnload(){
  var date = '<%=dateStr%>';
  $("activeTime").value = date;

  var date1Parameters = {
      inputId:'activeTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date'
  };
  new Calendar(date1Parameters);
}
function doInit(){
  if(CheckForm()){
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/active/act/YHActiveAct/addActive.act";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    parent.opener.location.reload();
    document.getElementById("listDiv").style.display="none";
    document.getElementById("returnDiv").style.display="block";
  }
}
</script>
</head>
 
 
<body class="" topmargin="5" onload="doOnload();">
<div id="listDiv" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/untitled.bmp" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;新建活动安排</span>
    </td>
  </tr>
</table>
  <form action="#"  method="post" id="form1" name="form1" onsubmit="return CheckForm();">
 <table class="TableBlock" width="450" align="center">

    <tr>
      <td nowrap class="TableData">时间：</td>
      <td class="TableData">
        <INPUT type="text" id="activeTime" name="activeTime" value="<%=curDateStr %>" class=BigInput size="10">
        <img id="date" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
        &nbsp;        
        <select id="hour" name="hour">
         <option value="00" >00</option>
         <option value="01" >01</option>
         <option value="02" >02</option>
         <option value="03" >03</option>
        <option value="04" >04</option>
        <option value="05" >05</option>
        <option value="06" >06</option>
        <option value="07" >07</option>
        <option value="08" >08</option>
        <option value="09" >09</option>
        <option value="10" >10</option>
        <option value="11" >11</option>
        <option value="12" >12</option>
       <option value="13" >13</option>
       <option value="14" >14</option>
       <option value="15" >15</option>
       <option value="16" >16</option>
       <option value="17" >17</option>
       <option value="18" >18</option>
       <option value="19" >19</option>
       <option value="20" >20</option>
       <option value="21" >21</option>
       <option value="22" >22</option>
       <option value="23" >23</option>
    </select>
        <b>:</b>
        <select id="min" name="min">
     <option value="00" >00</option>
     <option value="15" >15</option>
     <option value="30" >30</option>
     <option value="45" >45</option>
        </select>       	
      </td>
    </tr>
    <tr height="30">
      <td nowrap class="TableData" width="90"> 出席：</td>
      <td nowrap class="TableData">
          <input type="hidden" name="activeUser" id="activeUser" value=""  />
      <textarea name="activeUserDesc" id="activeUserDesc"  rows="2" cols="30" readonly="readonly" ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser(['activeUser','activeUserDesc'],3);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('activeUser').value='';$('activeUserDesc').value='';">清空</a>
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData"> 内容：</td>
      <td class="TableData">
        <textarea id="activeContent" name="activeContent" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="ACT_ID" value="">
        <input type="hidden" name="CAL_TIME" value="">
        <input type="button" value="确定" class="BigButton" onclick="doInit()">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="parent.close();">
      </td>
    </tr>
  </table>
</form>
</div>
<div id="returnDiv" style="display:none">
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">保存成功</div>
    </td>
  </tr>
</table>
 
<br>
<center>
	
<input type="button" class="BigButton" value="继续新建" onclick="location.reload();"></input>
<input type="button" class="BigButton" value="关闭" onclick="window.close();">
</center>

</div>

</body>
</html>