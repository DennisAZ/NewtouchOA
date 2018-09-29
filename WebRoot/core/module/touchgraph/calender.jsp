<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择日期</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<style type="text/css">
.Calendar{
  font: 12px Arial, Helvetica, sans-serif;overflow:hidden;text-align:center;width:189px;color:#333333;
  -moz-border-radius:4px;-webkit-border-radius:4px; border:#718BB7 1px solid;background:#f1f1f1;padding:2px;
}
.Calendar .top{
  -moz-border-radius:3px 3px 0px 0px;-webkit-border-top-left-radius:3px;-webkit-border-top-right-radius:3px;padding:5px;
  background:url(calendar/calendar_top.gif) repeat-x 0px 0px; height:14px;color:#ffffff;line-height:14px;overflow:hidden;clear:both;
}
.Calendar .arrow{
  background:url(calendar/calendar_top.gif) repeat-x 0px -53px;width:13px;height:13px;
  border:#448DAE 1px solid;-moz-border-radius:7px;-webkit-border-radius:7px; display:block;
}
.Calendar .arrow div{width:0px; height:1px; margin-top:2px; font-size:0px; overflow:hidden;}
.Calendar .arrow .right{border-left:#36547F 4px solid; border-bottom:#C7D8ED 4px solid;border-top:#D6E2F2 4px solid;margin-left:5px;@margin-left:1px;}
.Calendar .arrow .left{border-right:#36547F 4px solid; border-bottom:#C7D8ED 4px solid;border-top:#D6E2F2 4px solid;margin-left:4px;@margin-left:0px;}
.Calendar .top b{ cursor:pointer; text-align:center; display:block; float:left;}
.Calendar .week{background:url(calendar/calendar_top.gif) repeat-x 0px -25px;height:19px;border-bottom:#A3BAD9 1px solid; clear:both;}
.Calendar .week div{width:27px;height:19px;line-height:19px;float:left;text-align:center;}
.Calendar .day{overflow:hidden;margin:2px 0px; font-weight:bold; cursor:default; clear:both;}
.Calendar .day a{
  display:block;margin:1px;width:25px;height:18px;line-height:18px;outline: none; float:left;
  star:expression(this.onFocus=this.blur()); color:#576E9E;text-decoration:none;
}
.Calendar .day a:hover{
  background:url(calendar/calendar_top.gif) repeat-x 0px -26px;width:25px;
  line-height:18px;border-width:0px; color:#576E9E;height:18px;
}
.Calendar .day .today{ 
  background:url(calendar/calendar_top.gif) repeat-x 0px -51px; border:#36547F 1px solid;width:23px;height:16px;
  line-height:16px; border:#448DAE 1px solid;
}
.Calendar .day .select{background:url(calendar/calendar_top.gif) repeat-x 0px -4px;color:#ffffff;}
.Calendar .day .disable{color:#aaaaaa;}
.Calendar .bottom{
  color:#666666; background:url(calendar/calendar_top.gif) repeat-x 0px -45px;height:20px;line-height:20px;border:#A3BAD9 1px solid;padding:5px;
  -moz-border-radius:0px 0px 3px 3px; -webkit-border-bottom-left-radius:3px; -webkit-border-bottom-right-radius:3px;clear:both;
}
.Calendar .bottom .time{width:92px;overflow:hidden;float:left;height:20px; text-align:center;}
.Calendar .bottom .time input{
  width:18px;@width:16px;height:14px;overflow:hidden; border:#99BBE8 1px solid;
  background-color:#F1F1F1; color:#666666;font: 14px Arial, Helvetica, sans-serif;
}
.Calendar .bottom .btn{
  background:url(calendar/calendar_top.gif) repeat-x 0px -4px;height:16px;border:#A3BAD9 1px solid;color:#ffffff;float:left;line-height:16px;
  -moz-border-radius:4px;-webkit-border-radius:4px;width:40px; text-align:center;display:block;cursor:pointer; margin-top:1px;
}

</style>

<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
function getOpenner()
{
   if(is_moz)
      return parent.opener;
   else
      return parent.dialogArguments;
}
var parent_window = getOpenner();


var daysNum=[31,28,31,30,31,30,31,31,30,31,30,31];
var week = ['日','一','二','三','四','五','六'];
var month=['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
var yearRange = [1920,2030];
var now =new Date();
//用来保存当前所选的时间
var date={
  year:now.getFullYear(),
  month:now.getMonth() + 1,
  day:now.getDate(),
  week:now.getDay()
};
var div = null;
var selectYear = null;
var selectMonth = null;
var bindInput = null;
var format = "yyyy-MM-dd";
function doInit(){
  bindInput = parent_window.$(parent_window.bindToInput);
  createCalDiv();
}
function createCalDiv(){
  div = new Element("div");
  with(div){
    id = "calendarDiv";
    className = "Calendar";
    style.display = "";
    style.zIndex = 99;
  }
  $("con").appendChild(div);
  var temp="<div id='Top' class='top'><b class='arrow'><div class='left'></div></b><b class='arrow' style='float:right;'><div class='right'></div></b></div>"
    + "<div id='weeks' class='week'></div><div id='Days' class='day'></div>"
    + "<div class='bottom'><div class='btn'>今日</div><div id='Time' class='time'></div><div class='btn' style='float:right'>取消</div></div>";
  div.update(temp);
  var days = $('Days');
  selectMonth = new Element('b', { id:  'Month' }).update("<span>aa</span><select style='display:none'></select>");
  selectMonth.setStyle({width:'92px'});
  
  selectYear = new Element('b', { id:  'Year' }).update("<span>bb</span><select style='display:none'></select>");
  selectYear.setStyle({width:'36px'});
  var top = div.firstChild;
  var left = top.firstChild;
  $(top).insert(selectMonth , {after:left}); 
  $(top).insert(selectYear , {after:selectMonth}); 
  selectDay = null;
  selectTime = null;
  //设定所有星期 
  temp="";
  for(i=0;i< week.length;++i){
    temp+="<div>"+ week[i]+"</div>";
  }
  $( "weeks").update(temp);
  
  //设定所有年
  temp="";
  for(i = yearRange[0];i <= yearRange[1];++i){
    temp+="<option value='"+i+"'>"+i+"</option>";
  }
  var selectYearS =selectYear.lastChild;
  var selectYearSpan = selectYear.firstChild;
  $(selectYearS).update(temp);
  selectYearS.value = date.year;
  selectYearSpan.innerHTML = date.year;
  //设定所有月
  temp="";
  for(i=0;i<month.length;++i){
    temp+="<option value='"+(i+1)+"'>"+month[i]+"</option>";
  }
  var selectMonthS = selectMonth.lastChild;
  var selectMonthSpan = selectMonth.firstChild;
  $(selectMonthS).update(temp);
  selectMonthS.value = date.month;
  selectMonthSpan.innerHTML = month[date.month-1];
  
  registerListener();
  initDatesByYM(date.year,date.month);
}
function selectMonthMouHandler(){
  tmpThis = selectMonth.firstChild;
  if(tmpThis.style.display != 'none'){
    $(tmpThis).hide();
    $(selectMonth.lastChild).show();
    $(selectMonth.lastChild).focus();
    $(selectYear.lastChild).blur();
  }
}
function selectYearMouHandler(){
  tmpThis = selectYear.firstChild;
  if(tmpThis.style.display != 'none'){
    $(tmpThis).hide();
    $(selectYear.lastChild).show();
    $(selectYear.lastChild).focus();
    $(selectMonth.lastChild).blur();
  }
}
function registerListener(){
  selectYear.observe('mousedown',selectYearMouHandler.bindAsEventListener(this));
  selectMonth.observe('mousedown',selectMonthMouHandler.bindAsEventListener(this));
  $(selectMonth.lastChild).observe('blur',selectMonthBlurHandler.bindAsEventListener(this));
  $(selectYear.lastChild).observe('blur',selectYearBlurHandler.bindAsEventListener(this));
  selectMonth.lastChild.onchange = function(){
    this.blur();
  }
  selectYear.lastChild.onchange = function(){
    this.blur();
  }
  var divlf = div.lastChild.firstChild;
  Event.observe(divlf,'click',gotoToday.bind(this));
  var divll = div.lastChild.lastChild;
  Event.observe(divll,'click',cancel.bind(this));
  var days = $('Days');
  Event.observe(days,'click', clickDate.bind(this));
  var left = div.firstChild.firstChild;
  Event.observe(left,'click', preMonth.bind(this));
  var nextSibling = left.nextSibling;
  Event.observe(nextSibling,'click',nextMonth.bind(this));
}
function initDatesByYM(year,month){
  var days = $('Days'); 
  days.update("");
  first = new Date(year,month-1,1).getDay();
  if(first>0)
    var temp="<div style='width:"+(27*first)+"px;height:20px;float:left;'></div>";//占位而已
  else
    var temp="";
  var i;
  now = new Date();
  nowYear = now.getFullYear();nowMonth=now.getMonth();nowDate=now.getDate();
  for(i=1;i <= daysNum[month-1];++i){
    temp += "<a href='#' class='";
    var idTmp = "";
    if(year == nowYear&&month==nowMonth+1&&i==nowDate) {
      temp += "today";
      idTmp = " id='todayA'";
    }
    if(year ==   date.year&&month== date.month&&i==date.day) {
      temp += " select";
    }
    temp+="' "+ idTmp +">" + i + "</a>";
  }
  if(year%4==0&&month==2){//如果是闰年
    temp+="<a href='#' class='";
    var idTmp = "";
    if(year==nowYear&&month==nowMonth+1&&i==nowDate) {
      temp+="today";
      idTmp = " id='todayA'";        
    }
    if(year==date.year&&month==date.month&&i==date.day) {
      temp+=" select";
    }
    temp+="' "+ idTmp +">" + i + "</a>";
  }
  days.update(temp);
  selectDay = days.select('.select');
}
//跳转至上一个月
function preMonth(){
  var span = selectYear.firstChild;
  year1 = span.innerHTML;
  month1 = selectMonth.lastChild.value;
  if(month1>1) month1--; 
  else{
    month1=12;
    year1--; 
    $(span).update(year1);
  }
  $(selectMonth.firstChild).update(month[month1-1]);
  selectMonth.lastChild.value = month1;
  initDatesByYM(year1,month1);
}
//跳转至下一个月
function nextMonth(){
  var span = selectYear.firstChild;
  year1 = span.innerHTML;
  month1 = selectMonth.lastChild.value;
  if(month1<12) month1++;
  else{
    month1=1;
    year1++; 
    $(span).update(year1);
  }
  $(selectMonth.firstChild).update(month[month1-1]);
  selectMonth.lastChild.value = month1;
  selectYear.lastChild.value = year1;
  initDatesByYM(year1,month1);
}
//点击日期事件
function clickDate(e){
  var el = Event.findElement(e, 'A'); 
  if(el){
    setTimeToInput(el);
  }
  window.close();
}
function setTimeToInput(el) {
  selectDay.className = "";
  el.className = "select";
  selectDay = el;
  date.year = selectYear.lastChild.value;
  date.month = selectMonth.lastChild.value;
  date.day = el.innerHTML;
  
  
  var lastDate=new Date(date.year,date.month-1,date.day);
  date.week = lastDate.getDay();
  
  var str = lastDate.format(format);
  bindInput.value = str;
  parent_window.setHSliderValue();
}
Date.prototype.format = function(format)   
{   
   var o = {   
      "M+" : this.getMonth()+1, //month  
      "d+" : this.getDate(),    //day  
      "h+" : this.getHours(),   //hour  
      "m+" : this.getMinutes(), //minute  
      "s+" : this.getSeconds(), //second  ‘

    //quarter  
      "q+" : Math.floor((this.getMonth()+3)/3), 
      "S" : this.getMilliseconds() //millisecond  
   }   
   if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));   
    for(var k in o)if(new RegExp("("+ k +")").test(format))   
      format = format.replace(RegExp.$1,   
        RegExp.$1.length==1 ? o[k] :    
          ("00"+ o[k]).substr((""+ o[k]).length));   
    return format;   
 }
function selectMonthBlurHandler(){
   var tmpThis = selectMonth.lastChild;
   if(selectMonth.firstChild.innerHTML != month[parseInt(tmpThis.value) - 1]){
     $(selectMonth.firstChild).update(month[parseInt(tmpThis.value) - 1]);
     initDatesByYM(selectYear.lastChild.value , tmpThis.value);
   }
   tmpThis.hide();
   selectMonth.firstChild.show();
 }

function selectYearBlurHandler(){
   var tmpThis = selectYear.lastChild;
   if(selectYear.firstChild.innerHTML != tmpThis.value){
     $(selectYear.firstChild).update(tmpThis.value);
     initDatesByYM( tmpThis.value , selectMonth.lastChild.value);
   }
   tmpThis.hide();
   selectYear.firstChild.show();
 }
function cancel() {
  window.close();
}
function gotoToday(){
  now = new Date();
  year=now.getFullYear();
  month=now.getMonth()+1;
  $(selectMonth.firstChild).update(month[month-1]);
  selectMonth.lastChild.value = month;
  $(selectYear.firstChild).update(year);
  selectYear.lastChild.value = year;
 
  initDatesByYM(year,month);
  var el = $('todayA');
  if (el) {
    setTimeToInput(el);
  }
  window.close();
}
</script>
</head>

<body onload="doInit()" style="margin-top:3px;margin-bottom:0px;margin-left:0px;margin-right:0px;padding:0px">
<div align=center id="con"></div>
</body>
</html>