<%@ page language="java" import="yh.core.funcs.person.data.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<%
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userSeqId = user.getSeqId();
  String userPriv = user.getUserPriv();
  String type = request.getParameter("type");
  if(type==null){
    type="";
  }
  String error = "导入成功！";
  if(type!=null&&type.equals("2")){
    error = "导入出错,可能文件数据不正确！";
    //error = "导入成功！";
  }
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>导入和导出</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function checkFromExp(){
  var minDate = $("minDate");
  var maxDate = $("maxDate");
  var contentName="";
  if(document.getElementById("calendarContent").checked==false && document.getElementById("affairContent").checked==false && document.getElementById("taskContent").checked==false){
    alert("至少选择一种导出内容");
    return false;
  }
  if(minDate.value!=''&&!isValidDateStr(minDate.value)){
    alert("起始日期格式不对,应形如 2010-02-01");
    minDate.focus();
    minDate.select();
    return false;
  }
  if(maxDate.value!=''&&!isValidDateStr(maxDate.value)){
    alert("截止日期格式不对,应形如 2010-02-01");
    maxDate.focus();
    maxDate.select();
    return false;
  }
  if(maxDate.value!=''&&minDate.value!=''){
    var beginInt;
    var endInt;
    var beginArray = minDate.value.split("-");
    var endArray = maxDate.value.split("-");
    for(var i = 0 ; i<beginArray.length; i++){
      beginInt = parseInt(" " + beginArray[i]+ "",10);  
      endInt = parseInt(" " + endArray[i]+ "",10);
      if((beginInt - endInt) > 0){
        alert("起始日期不能大于截止日期!");
        maxDate.focus();
        maxDate.select();
        return false;
      }else if(beginInt - endInt<0){
        return true;
      }  
    }  
  }
  return true;
}
function my_export(){
   if(checkFromExp()){
     var contentName = '';
     var calendarContent = null;
     var affairContent = null;
     var taskContent = null
     if(document.getElementById("calendarContent").checked==true){
       contentName += "日程";
      calendarContent = $("calendarContent").value;
     }
     if(document.getElementById("affairContent").checked==true){
       affairContent = $("affairContent").value;
       contentName += "周期性事务";
     }
     if(document.getElementById("taskContent").checked==true){
       taskContent = $("taskContent").value;
       contentName += "任务";
     }
     document.getElementById("contentName").value= contentName;
     if(document.getElementById("OutlookOa").options[document.getElementById("OutlookOa").options.selectedIndex].value=="1"){


       var deptOut = "";
       if($("deptOut")){
         deptOut = $("deptOut").value;
       }
       var roleOut = "";
       if($("roleOut")){
         roleOut = $("roleOut").value;
       } 
       var userOut = "";
       if($("userOut")){
         userOut = $("userOut").value;
       }
     
       var minDate = $("minDate").value;
       var maxDate = $("maxDate").value;
       var calType = $("calType").value;
       if (maxDate) {
         maxDate += " 23:59:59";
       }
       var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/exportCSV.act?deptOut="+deptOut+"&roleOut="+roleOut+"&userOut="+roleOut+"&minDate="+minDate+"&maxDate="+maxDate+"&calType="+calType+"&calendarContent="+calendarContent+"&affairContent="+affairContent+"&taskContent="+taskContent;
       location.href = URL;
     }else{
       window.form1.submit();
     }
   } 
}
var file_name;
function CheckForm(){
  if(document.form2.xmlFile.value==""){ 
     alert("请选择要导入的文件！");
     return (false);
  }
  if(document.form2.xmlFile.value!="") {
    var file_temp=document.form2.xmlFile.value;
    $("xmlFileName").value=file_temp;
    var Pos;
    Pos=file_temp.lastIndexOf("\\");
    file_name=file_temp.substring(Pos+1,file_temp.length);
    document.form2.fileName.value=file_name;
 
  }
   return (true);
}
function my_inport(){
  //alert($("xmlFile").value);
  if(CheckForm()){
    var fileName = $("fileName").value;
    var file=fileName.lastIndexOf("\.");
    fileName=fileName.substring(file+1,fileName.length);
    var impType = 1;
    if(document.form2.fromOutLookOA.item(0).checked){
      if(fileName!='csv'){
        alert("只能导入CSV文件!");
        return;
      }
      impType = 1;
    }else{
      if(fileName!='xml'){
        alert("只能导入XML文件!");
        return;
      } 
      impType = 2; 
     // document.form2.action="import_xml.php";
    } 
    $("impType").value = impType;
    document.form2.submit();   
    //var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/improt.act?impType="+impType;
    //var rtJson = getJsonRs(URL, mergeQueryString($("form2")));
   // if(rtJson.rtState == "1"){
     // alert(rtJson.rtMsrg); 
    //  return ;
   // }
   // $("bodyDiv").style.display = "none";
    //$("returnDiv").style.display = "";
    
  }
}
function doOnload(){
  var date1Parameters = {
      inputId:'minDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1Img'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'maxDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2Img'
  };
  new Calendar(date2Parameters);
  var type='<%=type%>';
  if(type=='1'||type=='2'){
    $("bodyDiv").style.display = "none";
    $("returnDiv").style.display = "";
  }
}
function checkChecked(){
  if(document.getElementById("calendarContent").checked==false &&document.getElementById("taskContent").checked==false){
    document.getElementById("displayType").style.display = "none";
  }else{
    document.getElementById("displayType").style.display = "";
  }
}
function returnFun(){
  window.location.href = "<%=contextPath%>/core/funcs/calendar/inout.jsp";
}
</script>
</head>

<body class="" topmargin="5" onload="doOnload();">
<div id="bodyDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/inout.gif" align="absmiddle"><span class="big3"> &nbsp;日程数据导出</span>
    </td>
  </tr>
</table>

<br>
  <form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/export.act"  method="post" name="form1" id="form1">
 <table class="TableBlock" width="550" align="center">
  <%if(userPriv!=null&&userPriv.equals("1")){%>
  <tr>
    <td nowrap class="TableContent">选择范围(部门）：</td>
    <td class="TableData">
     <input type="hidden" name="deptOut" id="deptOut" value="">
      <textarea cols=35 name="deptOutDesc" id="deptOutDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['deptOut','deptOutDesc'])">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('deptOut').value='';$('deptOutDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">选择范围(角色）：</td>
    <td class="TableData">
      <input type="hidden" name="roleOut" id="roleOut" value="">
      <textarea cols=35 name="roleOutDesc" id="roleOutDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectRole(['roleOut','roleOutDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('roleOut').value='';$('roleOutDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">选择范围(人员)：</td>
    <td  class="TableData"> 
     <input type="hidden" name="userOut" id="userOut" value=""  />
      <textarea name="userOutDesc" cols=35 id="userOutDesc"  rows=2 class="BigStatic" wrap="yes" readonly ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser(['userOut','userOutDesc']);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('userOut').value='';$('userOutDesc').value='';">清空</a>
    </td>
  </tr>
  <%} %>
    <tr>
      <td nowrap class="TableData" > 日期：</td>
      <td class="TableData">
        <input type="text" id="minDate" name="minDate" size="12" maxlength="10" class="BigInput" >
        <img id="date1Img" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" > 至&nbsp;
        <input type="text" id="maxDate" name="maxDate" size="12" maxlength="10" class="BigInput" >
        <img id="date2Img" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" > 导出用于：</td>
      <td class="TableData">
       <select id="OutlookOa" name="outlookOa" class="BigSelect">
          <option value="1">Outlook</option>
          <option value="2">OA系统</option>
       </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" > 导出内容：</td>
      <td class="TableData">
      	<input type="checkbox" name="calendarContent" id="calendarContent" checked onclick="checkChecked();"><label for="calendarContent" >日程</label>
      	<input type="checkbox" name="affairContent" id="affairContent"><label for="affairContent">周期性事务</label>
      	<input type="checkbox" name="taskContent" id="taskContent" onclick="checkChecked();"><label for="taskContent" >任务</label>
      </td>
    </tr>
    <tr id="displayType" style="display:;">
      <td nowrap class="TableData"> 事务类型：</td>
      <td class="TableData">
        <select id="calType" name="calType" class="BigSelect">
          <option value="">所有</option>
          <option value="1">工作事务</option>
          <option value="2">个人事务</option>
        </select>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      	<input type="hidden" id="contentName" name="contentName" value="">
        <input type="button" value="导出" class="BigButton" onClick="my_export();">
      </td>
    </tr>
  </table>
</form>

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/inout.gif" align="absmiddle"><span class="big3"> &nbsp;日程数据导入</span>
    </td>
  </tr>
</table>
<br>

<form enctype="multipart/form-data"  action="<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/improt.act"  method="post" id="form2" name="form2" onsubmit="return CheckForm();">
<table class="TableBlock" width="550" align="center">
   <%if(userPriv!=null&&userPriv.equals("1")){%>
  <tr>
    <td nowrap class="TableContent">选择范围(部门）：</td>
    <td class="TableData">
     <input type="hidden" name="dept" id="dept" value="">
      <textarea cols=35 name="deptDesc" id="deptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">选择范围(角色）：</td>
    <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
      <textarea cols=35 name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">选择范围(人员)：</td>
    <td class="TableData">
      <input type="hidden" name="user" id="user" value=""  />
      <textarea name="userDesc" cols=35 id="userDesc"  rows=2 class="BigStatic" wrap="yes" readonly ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
    </td>
  </tr>
  <%} %>
  <tr height="25">
    <td nowrap class="TableContent">选择要导入的文件：</td>
    <td class="TableData">
       <input type="file" id="xmlFile" value="" name="xmlFile" class="BigInput" size="35">
       <input type="hidden" id="fileName" name="fileName">
         <input type="hidden" id="xmlFileName" name="xmlFileName">
         <input type="hidden" id="impType" name="impType" value=""></input>
   </td>
  </tr>
  <tr height="25">
    <td nowrap class="TableContent">文件来自：</td>
    <td class="TableData">
       <input type="radio" name="fromOutLookOA" id="outLook" value="1" onClick="span_erea.style.display=''"><label for="OUTLOOK">Outlook</label>
       <input type="radio" name="fromOutLookOA" id="OASystem" value="2" onClick="span_erea.style.display='none'"checked><label for="OA_SYSTEM">OA系统</label>
   </td>
  </tr>
  <tr id="span_erea" style="display:none;">
    <td nowrap class="TableData"> 导入至：</td>
    <td class="TableData">
    	<input type="radio"  name="calAffTask" id="calendarContentImp" value="1" checked><label for="CALENDAR_CONTENT2">日程</label>
    	<input type="radio" name="calAffTask" id="affairContentImp" value="2"><label for="AFFAIR_CONTENT2">周期性事务</label>
    	<input type="radio" name="calAffTask" id="affairContentImp" value="3"><label for="TASK_CONTENT2">任务</label>
    </td>
  </tr>
  <tr align="center" class="TableControl">
   <td colspan="2" nowrap>
     <input type="button" value="导入" class="BigButton" onClick="my_inport();">
   </td>
 </tr>
</table>
</form>
</div>
<div id="returnDiv" style="display:none">
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt"><%=error %></div>
    </td>
  </tr>
</table>
<center>
	<input type="button" value="返回" class="BigButton" onClick="returnFun();">
		
</center>

</div>
</body>
</html>