<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>借阅查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/rollmanage/js/rollfilelogic.js"></script>
<script type="text/javascript">
function setDate(){
//日期
var date1Parameters = {
   inputId:'sendTime_Min',
   property:{isHaveTime:false}
   ,bindToBtn:'date1'
};
new Calendar(date1Parameters);
var date2Parameters = {
   inputId:'sendTime_Max',
   property:{isHaveTime:false}
   ,bindToBtn:'date2'
};
new Calendar(date2Parameters);
}
function checkDate(){
  var leaveDate1 = document.getElementById("sendTime_Min"); 
  var leaveDate2 = document.getElementById("sendTime_Max"); 
  var leaveDate1Array = leaveDate1.value.trim().split(" "); 
  var leaveDate2Array = leaveDate1.value.trim().split(" "); 
  if(!leaveDate1.value  || !leaveDate2.value){
    if(leaveDate1.value){
      if(!isValidDateStr(leaveDate1.value)){
        alert("日期格式不对，应形如 1999-01-01"); 
        leaveDate1.focus(); 
        leaveDate1.select(); 
        return false; 
      }
    }else if(leaveDate2.value){
      if(!isValidDateStr(leaveDate2.value)){
        alert("日期格式不对，应形如1999-01-01"); 
        leaveDate2.focus(); 
        leaveDate2.select(); 
        return false; 
      }
    }
    return true;
  }
  if(!isValidDateStr(leaveDate1.value)){
    alert("日期格式不对，应形如 1999-01-01"); 
    leaveDate1.focus(); 
    leaveDate1.select(); 
    return false; 
  }
  if(!isValidDateStr(leaveDate2.value)){
    alert("日期格式不对，应形如 1999-01-01"); 
    leaveDate2.focus(); 
    leaveDate2.select();  
    return false; 
  }
  if(leaveDate1.value >= leaveDate2.value){ 
    alert("开始时间不能大于结束时间！"); 
    leaveDate1.select(); 
    leaveDate1.focus(); 
    return (false); 
  }
  return true;
}
function checkForm(){
  if(checkDate() == false ){
    return false;
  }
  return true;
}
function doSubmit(){
  if(checkForm()){
    var query = $("form1").serialize();
    location = "<%=contextPath%>/subsys/oa/rollmanage/rolllend/search.jsp?" + query;
  }
}
function doinit() {
  setDate();
  getSecretFlag("FILE_YEAR","fileYear");
}
</script>
</head>
<body topmargin="5"  onload="doinit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/roll_manage.gif" align="middle"><span class="big3"> 借阅查询</span>
    </td>
  </tr>
</table>

<form action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="70%"  align="center">
  <TR>
      <TD class="TableData">卷库名称：</TD>
      <TD class="TableData">
       <INPUT name="roomName" id="roomName" size=20 maxlength="100" class="BigInput">
      </TD>
      <TD class="TableData">案卷名称：</TD>
      <TD class="TableData">
       <INPUT name="rollName" id="rollName" size=20 maxlength="100" class="BigInput">
      </TD>
  </TR>  
  <TR>
      <TD class="TableData">文件号：</TD>
      <TD class="TableData">
       <INPUT name="fileCode" id="fileCode" size=20 maxlength="100" class="BigInput">
      </TD>
      <TD class="TableData">公文字：</TD>
      <TD class="TableData">
      <input  name="fileWord" id="fileWord" type="text">
      </TD>
  </TR>
   <TR>
      <TD class="TableData">公文年号：</TD>
      <TD class="TableData">
       <select name="fileYear" id="fileYear"><option value="" ></option></select>
      </TD>
      <TD class="TableData">公文期号：</TD>
      <TD class="TableData">
      <input  name="issueNum" id="issueNum"  type="text">
      </TD>
  </TR>
   <TR>
      <TD class="TableData">文件主题词：</TD>
      <TD class="TableData" colspan="3">
       <INPUT name="fileSubject" id="fileSubject" size=20 maxlength="100" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD class="TableData">文件标题：</TD>
      <TD class="TableData">
       <INPUT name="fileTitle" id="fileTitle" size=20 maxlength="100"  class="BigInput">
      </TD>
      <TD class="TableData">文件辅标题：</TD>
      <TD class="TableData">
       <INPUT name="fileTitleo" id="fileTitleo" size=20 maxlength="100" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD class="TableData">发文单位：</TD>
      <TD class="TableData">
       <INPUT name="sendUnit" id="sendUnit" size=20  class="BigInput">
      </TD>
      <TD class="TableData">备注：</TD>
      <TD class="TableData">
        <input type="text" name="remark" id="remark" size="20" maxlength="10" class="BigInput">
      </TD>
  </TR>
  <tr>
  <TD class="TableData">发文日期：</TD>
      <TD class="TableData" colspan="3">
        <input type="text" name="sendTime_Min" id="sendTime_Min" size="10" maxlength="10" class="BigInput" value="">

        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >&nbsp;至&nbsp;

        <input type="text" name="sendTime_Max" id="sendTime_Max" size="10" maxlength="10" class="BigInput" value="">
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </TD>
      </tr>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="查询" class="BigButton" onclick="doSubmit();">
      </td>
    </tr>  
  </table>
</form>
</body>
</html>