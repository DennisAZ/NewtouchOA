<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>周期性事务</title>
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
function checkForm(){
  var minTime = document.getElementById("minTime");
  var maxTime = document.getElementById("maxTime");
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
    if(compareDate(minTime , maxTime)){
        alert("起始日期不能大于结束日期!");
        minTime.focus();
        maxTime.select();
        return false;
    }
  }
  return true;
}
function compareDate(beginDate , endDate) {
  d1Arr=beginDate.value.split('-');
  d2Arr=endDate.value.split('-');
  v1 = new Date(d1Arr[0],d1Arr[1],d1Arr[2]);
  v2 = new Date(d2Arr[0],d2Arr[1],d2Arr[2]);
  return v1>v2;
}
function my_note(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/affairnote.jsp?seqId="+seqId;
  window.open(URL,"calendar","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=280,resizable=yes");    
}
function new_affair(){
  window.location.href = "<%=contextPath%>/core/funcs/calendar/addaffair.jsp";
}
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairByPagin.act";

  var cfgs = {
      dataAction: requestURL,
      container: "listDiv",
      colums: [
         {type:"hidden", name:"seqId", text:"ID", width:"10%"},
         {type:"data", name:"beginTime", text:"起始日期",sortDef:{type:0, direct:"asc"}, width:"13%",align:"center", dataType:"dateTime",render:toBeginTime},
         {type:"data", name:"endTime", text:"结束日期",align:"center", width:"13%",dataType:"dateTime",render:toEndTime},           
         {type:"text", name:"type", text:"提醒类型", width: "8%",render:toType,align:"center"},
         {type:"text", name:"remindDate", text:"提醒日期",align:"center", width: "8%",render:toRemidDate},
         {type:"text", name:"remindTime", text:"提醒时间",align:"center", width:"8%"},
         {type:"text", name:"content", text:"事务内容", width:"36%",align:"left",render:toContent},
         {type:"hidden", name:"managerId", text:"指定人员", width:""},
         {type:"hidden", name:"userId", text:"userId", width:""},
         {type:"selfdef", width:"8%",align:"center",text:"操作",render:opts}]
    };
    var pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
    }else{
    var table = new Element('table',{"class":"MessageBox" ,"align":"center" ,"width":"280"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无事务记录</div>"
        + "</td></tr>"
         );
    var table1 = "<div align='center'><table class='MessageBox' align='center' width='280'><tr><td class='msg info'>"
    + "<div class='content' style='font-size:12pt'>无事务记录</div>"
    + "</td></tr></table></div>";
     $("listDiv").update(table1);
  }
  // 初始化时间
  var date1Parameters = {
      inputId:'minTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'maxTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
  
}
function toBeginTime(cellData, recordIndex, columInde){
  return cellData.substr(0,19);
}
function toEndTime(cellData, recordIndex, columInde){
  return cellData.substr(0,19);
}
function toContent(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#' onclick='my_note("+seqId+");'>"+cellData+"</a>";
}
function toType(cellData, recordIndex, columInde){
  var typeNames = ["按日提醒","按周提醒","按月提醒","按年提醒"];
  return typeNames[cellData-2];
}
function toRemidDate(cellData, recordIndex, columInde){
  var type = this.getCellData(recordIndex,"type");
  var remindDate = this.getCellData(recordIndex,"remindDate");
  var weekNames = ["一","二","三","四","五","六","日"];
  var week_day_month ="";
  if(type=='3'){
    week_day_month = "周"+weekNames[parseInt(remindDate)-1];  
  }
  if(type=='4'){
    week_day_month= remindDate+'日';
  }
  if(type=='5'){
    week_day_month = remindDate.split('-')[0]+'月'+remindDate.split('-')[1]+'日';
  }
  return week_day_month;
}
function delete_affair(seqId){
  var msg='确认要删除该事务吗？';
  if(window.confirm(msg)) {
    var requestURL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/deleteAffairById.act?seqId="+seqId;
    var rtJson = getJsonRs(requestURL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.href = "<%=contextPath%>/core/funcs/calendar/Cycaffair.jsp";
  }
}
function update_affair(seqId){
   window.location.href = "<%=contextPath%>/core/funcs/calendar/editaffair.jsp?seqId="+seqId;
}
function opts(cellData, recordIndex, columInde){
  var userId = this.getCellData(recordIndex,"userId");
  var seqId = this.getCellData(recordIndex,"seqId");
  var managerId = this.getCellData(recordIndex,"managerId");
  if(managerId.trim()==''||managerId==userId){
    return "<a href='#' onclick='update_affair("+seqId+")'>编辑</a>&nbsp;<a href='#' onclick='delete_affair("+seqId+")'>删除</a>";
  }else{
    return "<a href='#' onclick='my_note("+seqId+");'>查看</a>";
  }
  
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;新建周期性事务</span><br>
    </td>
  </tr>
</table>
<div align="center">
<input type="button" value="新建周期性事务" class="BigButtonC" onClick="new_affair();" title="新建周期性事务">
</div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;周期性事务管理</span>
    </td>
   </tr>
</table>
<br>
<div id="listDiv" ></div>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;周期性事务查询</span>
    </td>
  </tr>
</table>&nbsp;&nbsp;

<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHAffairAct/selectAffairByTerm.act"  method="post" name="form1" onsubmit="return checkForm();">
<table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData" width="100"> 日期：</td>
      <td class="TableData">
        <input  type="text" id="minTime" name="minTime" size="12" maxlength="10" class="BigInput" value="">
        <img id="date1" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer"> 至&nbsp;
        <input type="text" id="maxTime" name="maxTime" size="12" maxlength="10" class="BigInput" value="">
        <img id="date2" align="absMiddle" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 事务内容：</td>
      <td class="TableData">
        <input id="content" name="content" size="33" class="BigInput">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="submit" value="查询" class="BigButton">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
