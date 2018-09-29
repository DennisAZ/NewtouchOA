<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String curDateStr = YHUtility.getCurDateTimeStr();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>今天</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style>
      html {
                   overflow:auto;  /*这个可以去掉IE6,7的滚动*/
                   _overflow-x:hidden;/*去掉IE6横向滚动*/
                }
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>

<script type="text/javascript">
var menuData1 = [{ name:'<div style="padding-top:5px;margin-left:10px">今日事务<div>',action:setAction,extData:'1'}
  ,{ name:'<div style="padding-top:5px;margin-left:10px">撰写日志<div>',action:setAction,extData:'2'}
  ]
  var menuData2 = [{ name:'<div style="padding-top:5px;margin-left:10px">全部<div>',action:set_status,extData:'0'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">未开始<div>',action:set_status,extData:'1'}
  ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">进行中<div>',action:set_status,extData:'2'}
  ,{ name:'<div style="color:#FF0000;padding-top:5px;margin-left:10px">已超时<div>',action:set_status,extData:'3'}
  ,{ name:'<div style="color:#00AA00;padding-top:5px;margin-left:10px">已完成<div>',action:set_status,extData:'4'}
  ]
function setAction(){
  if(arguments[2]==1){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendar.jsp?ldwmNewStatus=list";
    window.open(URL,"calendar","height=400,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=400,top=220,resizable=yes");

  }else if(arguments[2]==2){
    var URL = "<%=contextPath%>/core/funcs/calendar/addcalendardiary.jsp?seqId=";
    window.open(URL,"calendar","height=530,width=630,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=200,top=100,resizable=yes");
   }
}
function showMenu(event){
  var menu = new Menu({bindTo:$('new') , menuData:menuData1 , attachCtrl:true});
  menu.show(event);
}
function showMenuStatus(event){
  var menu = new Menu({bindTo:$('status') , menuData:menuData2 , attachCtrl:true});
  menu.show(event);
}
function set_status(){
  var statusTemp = arguments[2];
  var URL = "<%=contextPath%>/core/funcs/calendar/list.jsp?statusTemp="+statusTemp;
  window.location = URL;
}
function check_all(){
  
  var t =document.getElementsByName("email_select");
  for (i=0;i<document.getElementsByName("email_select").length;i++){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").item(i).checked=true;
    }else{
      document.getElementsByName("email_select").item(i).checked=false;
    }
  }
  if(i==0){
    if(document.getElementsByName("allbox")[0].checked){
      document.getElementsByName("email_select").checked=true;
    }else{
      document.getElementsByName("email_select").checked=false;
    }
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
  if(i==0) {
    el=document.getElementsByName("email_select");
    if(el.checked){  
      val=el.value;
      checked_str+=val + ",";
    }
  }
  return checked_str;
}
function delete_arrang(){
  var delete_str=get_checked();
  if(delete_str==""){
     alert("要删除任务，请至少选择其中一条。");
     return;
  }
  msg='确认要删除所选任务吗？';
  if(window.confirm(msg)){
    var url="<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/deleteCalendar.act?seqIds="+delete_str+"&statusTemp="+document.getElementById("statusTemp").value;
    window.location=url;
  }
}
function doOnload(){
  var status1 = '<%=request.getParameter("statusTemp")%>';
  var statusName;
  if(status1=="null"){
    statusName = "全部";
    status1 = "0";
  }else{
    if(status1=='0'){
      statusName = "全部";
    }
    if(status1=='1'){
      statusName = "未开始";
    }
    if(status1=='2'){
      statusName = "进行中";
    }
    if(status1=='3'){
      statusName = "已超时";
    }
    if(status1=='4'){
      statusName = "已完成";
    }   
  } 
  var colorTypes = ["","#0000FF","#0000FF","#FF0000","#00AA00"];
  document.getElementById("statusName").innerHTML=statusName;
  document.getElementById("statusName").style.color=colorTypes[status1];
  document.getElementById("statusTemp").value=status1;
  var statusTemp = document.getElementById("statusTemp").value;
  var url = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarByStatus.act?statusTemp="+statusTemp;
  var cfgs = {
    dataAction: url,
    container: "listDiv",
    colums: [
       {type:"selfdef",name:"email_select", text:"选择",align:"center", width:"5%",render:toCheck},
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"data", name:"calTime", text:"起始日期",align:"center", width: "14%", dataType:"dateTime",render:toCalDate},
       {type:"data", name:"endTime", text:"结束日期",align:"center", width: "14%",dataType:"dateTime",render:toEndDate},           
       {type:"text", name:"content", text:"内容", width: "40%",render:toContent,align:"left"},
       {type:"text", name:"calType", text:"类型",align:"center", width: "8%",render:toCalType},
       {type:"hidden", name:"overStatus", text:"状态", width:"12%"},
       {type:"hidden", name:"calLevel", text:"重要级别", width:"12%"},
       {type:"hidden", name:"managerId", text:"安排人Id", width:"12%"},
       {type:"hidden", name:"userId", text:"userId", width:"12%"},
       {type:"selfdef",name:"opts",align:"center", text:"操作", width:"13%",render:toOpts}]
  };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
  document.getElementById("deleteAll").style.display = "";
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的日程安排</div></td></tr>"
        );
    $('listDiv').innerHTML = "";
    $('listDiv').appendChild(table);  
    
  }
}
function toCalType(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var calType = this.getCellData(recordIndex,"calType");
  var calTypeDesc = '工作事务';
  if(calType=='2'){
    calTypeDesc = "个人事务";
  }
  return calTypeDesc;
}
function toCalDate(cellData, recordIndex, columIndex){
  var calTime = this.getCellData(recordIndex,"calTime");

  return calTime.substr(0,16);
}
function toEndDate(cellData, recordIndex, columIndex){

  var endTime = this.getCellData(recordIndex,"endTime"); 
  return endTime.substr(0,16);
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var overStatus = this.getCellData(recordIndex,"overStatus"); 
  var userId = this.getCellData(recordIndex,"userId");
  var managerId = this.getCellData(recordIndex,"managerId");
  var overStatusName = "完成";
  if(overStatus=='1'){
    overStatusName = "未完成";
  }
  if(overStatus.trim()==''){
    overStatus = 0;
  }
  var str = "";
  if(managerId.trim()==''||userId==managerId.trim()){
    str = "<a href='#' onclick='updateStatus_cal("+seqId+","+overStatus+");'>"+overStatusName+"</a> "
    + "<a href='#' onclick='update_cal("+seqId+");'>修改</a> "
    + "<a href='#' onclick='del_cal("+seqId+");'>删除</a> "
  }else{
    str = "<a href='#' onclick='updateStatus_cal("+seqId+","+overStatus+");'>"+overStatusName+"</a> ";
  }
    return str;
}
function toCheck(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var userId = this.getCellData(recordIndex,"userId");
  var managerId = this.getCellData(recordIndex,"managerId");
  if(managerId.trim()==''||userId==managerId.trim()){
    return "<input type='checkbox'  id='email_select' name='email_select' value='" + seqId + "' onClick='check_one(self);'>";
  }else{
    return "";
  }
}
function toContent(cellData, recordIndex, columIndex){
  var curDateStr = '<%=curDateStr%>';
  var seqId = this.getCellData(recordIndex,"seqId");
  var calTime = this.getCellData(recordIndex,"calTime");
  var endTime = this.getCellData(recordIndex,"endTime"); 
  var content = this.getCellData(recordIndex,"content"); 
  var calLevel = this.getCellData(recordIndex,"calLevel"); 
  var overStatus = this.getCellData(recordIndex,"overStatus");
  var statusName = "状态 : 进行中";
  var style_color = "#0000FF";
  if(curDateStr<calTime){
    statusName = "状态：未开始";
    style_color = "#0000FF";
  }
  if(curDateStr>endTime){
    statusName = "状态：已超时";
    style_color = "#FF0000";
  }
  if(overStatus=='1'){
    statusName = "状态：已完成";
    style_color = "#00AA00";
  }
  var calLevelNames = ['未指定','重要/紧急','重要/不紧急','不重要/紧急','不重要/不紧急'];
  var status = "";
  return "<span class='CalLevel"+calLevel+"' title='" + calLevelNames[calLevel] + "'>&nbsp</span><a a href='#' onclick='my_note(" + seqId +");' style='color:"+style_color+";' title='"+statusName+"'>"+ content +"</a>";
}
function del_cal(seqId){
  var msg = "确认要删除此任务吗？";
  if(window.confirm(msg)){
    var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/deleteCalendarById.act?seqId="+seqId+"&ldwm=list";
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.reload();
  }
}
function updateStatus_cal(seqId,status){
  if(status=='0'){
    status = '1';
    var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+status+"&ldwm=list";
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.reload();
  }else{
    status = '0';
    var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+status+"&ldwm=list";
    var rtJson = getJsonRs(URL);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    window.location.reload();
  }

}
function update_cal(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/editcalendar.jsp?seqId="+seqId+"&ldwm=list";
  window.location.href=URL;
}
function my_note(seqId){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status=";
  window.open(URL,"my_note","height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
}
function set_view(temp){
  var status = document.getElementById("statusTemp").value;
  if(temp=='list'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/list.jsp?statusTemp="+status;
  }
  if(temp=='day'){
    document.location.href = "<%=contextPath%>/core/funcs/calendar/day.jsp?statusStr="+status;
  }
  if(temp=='week'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/week.jsp?statusStr="+status;
  }
  if(temp=='month'){  
    document.location.href = "<%=contextPath%>/core/funcs/calendar/month.jsp?status="+status;
  }
  setCookie("calendarType",temp);
}
/**
 * 设置cookie
 */
function setCookie(name,value){
  var Days = 30;
  var exp  = new Date();
  exp.setTime(exp.getTime() + Days*24*60*60*1000);
  document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
</script>
<body class="" topmargin="5" onload="doOnload();" style="margin-right:18px;">
<div class="PageHeader">
	<div class="fleft">
	  <input type="hidden" value="" name="OVER_STATUS">
	  <input type="hidden" value="2010" name="YEAR">
	  <input type="hidden" value="02" name="MONTH">
	  <input type="hidden" value="22" name="DAY">
	  <a id="status" href="javascript:;" class="dropdown" onclick="showMenuStatus(event);" hidefocus="true"><span id="statusName">全部</span></a>&nbsp;
	  <input type="hidden" id="statusTemp" name="statusTemp" value="0">
	</div>
	<div class="right">
		 <a class="ToolBtn" href="<%=contextPath%>/core/funcs/calendar/selectcalendar.jsp"><span>查询</span></a>
	  <a id="new" href="javascript:;" class="dropdown" onclick="showMenu(event);" hidefocus="true"><span>新建</span></a>&nbsp;
	  <a class="calendar-view list-view" href="javascript:set_view('list');" title="列表视图"></a>
	  <a class="calendar-view day-view" href="javascript:set_view('day');" title="日视图"></a>
	  <a class="calendar-view week-view" href="javascript:set_view('week');" title="周视图"></a>
	  <a class="calendar-view month-view" href="javascript:set_view('month');" title="月视图"></a>
	</div>
	<div class="clear">
</div>
<br></br>
<div id="listDiv"></div>
<div id="deleteAll" style="display:none"> 
  &nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="check_all();">
      <label for="allbox_for">全选</label>&nbsp;
      <a href='javascript:delete_arrang();' title='删除所选工作事物'><img src='<%=imgPath%>/delete.gif' align='absMiddle'>删除</a>&nbsp;
 </div>
</body>
</html>
