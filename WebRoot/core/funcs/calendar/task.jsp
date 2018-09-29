<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的任务</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
function deleteTaskById(seqId){
  msg='确认要删除该任务吗？';
  if(window.confirm(msg)) {
    var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/deleteTaskById.act?seqId="+seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location = "<%=contextPath%>/core/funcs/calendar/task.jsp";
  }
}
function updateTask(seqId){
  URL="<%=contextPath%>/core/funcs/calendar/edittask.jsp?seqId="+seqId;
  window.location=URL;
}
function taskNote(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/tasknote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=180,width=170,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function check_all(){
  for (i=0;i<document.getElementsByName("email_select").length;i++){
    if(document.getElementsByName("allbox")[0].checked)
      document.getElementsByName("email_select").item(i).checked=true;
    else
      document.getElementsByName("email_select").item(i).checked=false;
   }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked)
      document.getElementsByName("email_select").checked=true;
    else
      document.getElementsByName("email_select").checked=false;
   }
}
function check_one(el){
   if(!el.checked)
      document.getElementsByName("allbox")[0].checked=false;
}
function get_checked(){
  checked_str="";
  for(i=0;i<document.getElementsByName("email_select").length;i++){
    el=document.getElementsByName("email_select").item(i);
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  if(i==0){
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}
function deleteTask(){
  delete_str=get_checked();
  delete_str = delete_str.substr(0,delete_str.length-1);
  if(delete_str==""){
    alert("要删除事务，请至少选择其中一条。");
    return;
  }
  msg='确认要删除所选事务吗？';
  if(window.confirm(msg)){
    window.location = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/deleteTask.act?seqIds="+delete_str;
  }
}
function  doOnload(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/selectTaskByPage.act";
  var cfgs = {
      dataAction: requestURL,
      container: "listDiv",
      colums: [
          {type:"selfdef",name:"email_select", text:"选择",align:"center", width:"5%",render:toCheck},
         {type:"hidden", name:"seqId", text:"ID",align:"center", width:"10%"},
         {type:"hidden", name:"important",align:"center", text:"重要级别", width:"10%"},
         {type:"text", name:"taskNo", text:"序号",align:"center", width:"5%"},
         {type:"text", name:"subject", text:"任务标题", width:"24%",render:toSubject,align:"left"},
         {type:"text", name:"taskStatus", text:"状态",align:"center", width:"7%",render:toTaskStatus},
         {type:"text", name:"rate", text:"完成", width:"18%",render:toRate},
         {type:"text", name:"taskType", text:"类别",align:"center", width:"5%",render:toTaskType},
         {type:"text", name:"color", text:"颜色",align:"center", width:"10%",render:toColor},
         {type:"data", name:"beginDate", text:"起始日期",align:"center", width: "8%", dataType:"date"},
         {type:"data", name:"endDate", text:"结束日期",align:"center", width: "8%",dataType:"date"},  
         {type:"hidden", name:"managerId", text:"安排人Id",align:"center", width:"10%"},
         {type:"hidden", name:"userId", text:"userId",align:"center", width:"10%"}, 
         {type:"selfdef", width: "8%",align:"center",text:"操作",render:opts}]
    };
 var pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      document.getElementById("deleteAll").style.display = "";
    }else{
      var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"340"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无符合条件的日程安排</div>"
        + "</td></tr>"
         );
      var tableTemp = "<div align='center'><table class='MessageBox' aligb='center' width='340'><tr>"
      + "<td class='msg info'>"
      + "<div class='content' style='font-size:12pt'>无符合条件的日程安排</div>"
      + "</td></tr></table></div>"
     $("listDiv").update(tableTemp);
  }
  // 初始化时间
  var date1Parameters = {
      inputId:'minDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'maxDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}
function toCheck(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var managerId = this.getCellData(recordIndex,"managerId");
  var userId = this.getCellData(recordIndex,"userId");
  if(managerId.trim()==''||managerId.trim()==userId){
    return "<input type='checkbox'  id='email_select' name='email_select' value='" + seqId + "' onClick='check_one(self);'>";
  }else{
    return "";
  }
}
function toSubject(cellData, recordIndex, columInde){
  var importantNames = ["未指定","重要/紧急","重要/不紧急","不重要/紧急","不重要/不紧急"];
  var seqId = this.getCellData(recordIndex,"seqId");
  var important = this.getCellData(recordIndex,"important");
  if(important.trim()==''){
    important = 0;
  }
  return "<a href='javascript:taskNote("+seqId+");' class='CalLevel"+important+"' title='"+importantNames[important]+"'>"+cellData+"</a>";
}
function toRate(cellData, recordIndex, columInde){
  return "<div style='background:#00AA00;width:"+parseInt(cellData * 1.4)+"px;height:18px;margin-top:2px;float:left;'></div><div style='float:right;'>&nbsp;"+cellData+"%</div>"
}
function toColor(cellData, recordIndex, columInde){
  var colorNames = ["未指定","红色类别","黄色类别","绿色类别","橙色类别","蓝色类别","紫色类别"];
  if(cellData.trim()==''){
    cellData = 0;
  }
  var colorName = colorNames[cellData];
  if(cellData=='0'){
    cellData = "";
  }
  return "<span class='CalColor"+cellData+"'>"+colorName+"</span>";
}
function toTaskStatus(cellData, recordIndex, columInde){
  var statusNames = ["未开始","进行中 ","已完成","等待其他人","已推迟"];
  
  return statusNames[cellData-1];
}
function toTaskType(cellData, recordIndex, columInde){
  var taskTypeName = "工作";
  var taskType = this.getCellData(recordIndex,"taskType");
  if(taskType=='2'){
    taskTypeName = "个人";
  }
  return taskTypeName;
}
function newTask(){
  window.location = "<%=contextPath%>/core/funcs/calendar/addtask.jsp";
}
function opts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var managerId = this.getCellData(recordIndex,"managerId");
  var userId = this.getCellData(recordIndex,"userId");
  if(managerId.trim()==''||managerId.trim()==userId){
    return "<a href='#' onclick='updateTask("+seqId+")'>编辑</a>&nbsp;<a href='#' onclick='deleteTaskById("+seqId+")'>删除</a>";
  }else{
    return "<a href='#' onclick='updateTask("+seqId+")'>编辑</a>";
  }
 
}
function checkForm(){
  var minTime = document.getElementById("minDate");
  var maxTime = document.getElementById("maxDate");
  if(minTime.value!=""){
    if(!isValidDateStr(minTime.value)){
      alert("起始日期格式不正确,应形如 2010-02-01");
      minTime.focus();
      minTime.select();
      return false;
      }
  }
  if(maxTime.value!=""){
    if(!isValidDateStr(maxTime.value)){
      alert("起始日期格式不正确,应形如 2010-02-01");
      maxTime.focus();
      maxTime.select();
      return false;
     }
  }
  if(minTime.value!=""&&maxTime.value!=""){
    var beginArray = (minTime.value).split("-");
    var endArray = (maxTime.value).split("-");
    if(compareDate(minTime , maxTime)){
      alert("起始日期不能大于结束日期!");
      minTime.focus();
      maxTime.select();
      return false ;
    } 
  }
  
  return true;
}
function Init(){
  if(checkForm()){
     document.form1.submit();
  }
  
}
function compareDate(beginDate , endDate) {
  d1Arr=beginDate.value.split('-');
  d2Arr=endDate.value.split('-');
  v1=new Date(d1Arr[0],d1Arr[1],d1Arr[2]);
  v2=new Date(d2Arr[0],d2Arr[1],d2Arr[2]);
  return v1>v2;
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;新建任务</span><br>
    </td>
  </tr>
</table>
<div align="center">
<input type="button" value="新建任务" class="BigButton" onClick="newTask();" title="新建任务">
</div>
<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;任务管理</span>
    </td>
 </tr>
</table>
<div id="listDiv"  ></div>
<div id="deleteAll" style="display:none">&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="check_all();">
      <label for="allbox_for">全选</label>&nbsp;
      <a href='javascript:deleteTask();' title='删除所选工作事物'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>删除</a>&nbsp;
</div>
&nbsp;&nbsp;
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 任务查询</span><br>
    </td>
  </tr>
</table>&nbsp;&nbsp;


<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHTaskAct/selectTaskByTerm.act"  method="post" id="form1" name="form1"  >
  <table class="TableBlock" width="450" align="center">
      <tr>
      <td nowrap class="TableData" width="100"> 日期：</td>
      <td class="TableData">
        <input type="text" id="minDate" name="minDate" size="12" maxlength="10" class="BigInput" value="">
        <img id="date1" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" > 至&nbsp;
        <input type="text" id="maxDate" name="maxDate" size="12" maxlength="10" class="BigInput" value="">
        <img id="date2" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    
    <tr>
      <td nowrap class="TableData"> 状态：</td>
      <td class="TableData">
        <select id="taskStatus" name="taskStatus" class="BigSelect">
          <option value="">所有</option>
          <option value="1">未开始</option>
          <option value="2">进行中</option>
          <option value="3">已完成</option>
          <option value="4">等待其他人</option>
          <option value="5">已推辞</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 优先级：</td>
      <td class="TableData">
        <select id="important" name="important" class="BigSelect">
          <option value="">所有</option>
          <option value="0">未指定</option>
          <option value="1">重要/紧急</option>
          <option value="2">重要/不紧急</option>
          <option value="3">不重要/紧急</option>
          <option value="4">不重要/不紧急</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 任务类型：</td>
      <td class="TableData">
        <select id="taskType" name="taskType" class="BigSelect">
          <option value="">所有</option>
          <option value="1">工作</option>
          <option value="2">个人</option> 
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 任务内容：</td>
      <td class="TableData">
        <input id="content" name="content" size="33" class="BigInput">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" class="BigButton" onclick="Init();" >
      </td>
    </tr>
  </table>
</form>
</body>
</html>
