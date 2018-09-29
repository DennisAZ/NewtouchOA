<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>列车时刻查询 - 详细信息</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script Language="JavaScript">
var pageMgr;
var color = 0;
function Init(){
	 var ftime = "${param.ftime}";
	 var etime = "${param.etime}";
	 var day = "${param.day}";
	 var dayTemp = "";
	 switch(day)  {
     case '0' : dayTemp = "当天";break;
     case '1' : dayTemp = "次日";break;
     case '2' : dayTemp = "第三天";break;
     case '3' : dayTemp = "第四天";break;
     case '4' : dayTemp = "第五天";break;
     case '5' : dayTemp = "第六天";break;
     case '6' : dayTemp=  "第七天";break;
	 }
	 $('ftime').innerHTML += ftime;
	 $('etime').innerHTML += dayTemp + etime;

	 var minute = etime.substring(3,5) - ftime.substring(3,5);
	 if(minute < 0) minute = minute + 60;
	 var hour = etime.substring(0,2) - ftime.substring(0,2) + day*24;
	 if(minute < 0) hour--;
	 $('allTime').innerHTML += hour+"小时"+minute+"分钟";
	 var allPrice = "${param.allPrice}";
	 var price = allPrice.split(",");
	 for(var i = 0 ; i < price.length ; i++){
		  if($("price"+(i+1)) && price[i] != 'null' ){
			  $("price"+(i+1)).innerHTML += price[i];
			}
	 }

	 var seqId = "${param.seqId}";
	 var requestURL = "<%=contextPath%>/yh/core/funcs/utilapps/info/train/act/YHTrainAct/searchTrainInfo.act?seqId="+seqId;
	 var cfgs;
   cfgs = {
     dataAction: requestURL,
     container: "listDiv",
     colums: [
        {type: "text", name: "urlNo", text: "站次", width: 100,align: 'center',render:add},
        {type: "text", name: "station", text: "经停站", width: 180,align: 'center',render:upStation}, 
        {type: "text", name: "arrive", text: "到站时间", width: 80,align:'center',render:uptime1}, 
        {type: "text", name: "depart", text: "发车时间", width: 80,align:'center',render:uptime2}, 
        {type: "text", name: "distance", text: "公里数", width: 60,align:'center',render:upString},
        {type: "text", name: "day", text: "天数", width: '20%',align:'center'}]
   };
   pageMgr = new YHJsPage(cfgs);
   pageMgr.show();
}

function add(cellData, recordIndex, columIndex){
	return recordIndex+1;
}

function uptime1(cellData, recordIndex, columIndex){
	var time = cellData.substring(10,16);
	return time.trim() == "00:00" ? "<--始发站-->" : time;
}

function uptime2(cellData, recordIndex, columIndex){
  var time = cellData.substring(10,16);
  return time.trim() == "00:00" ? "<--终点站-->" : time;
}

function upString(cellData, recordIndex, columIndex){
  return cellData+"公里";
}

function upStation(cellData, recordIndex, columIndex){
	var pstart = "${param.pstart}";
	var pend = "${param.pend}";
	if(pstart.trim() == '' || pend.trim() == '') return cellData;
	if(pstart.trim() == cellData) color = 1;
	var tempString = (color == 1 ? "<font color='blue'>"+cellData+"</font>" : cellData);
	if(pend.trim() == cellData) color = 0;
	return tempString;
}

</script>

<body class="bodycolor" topmargin="5" onload="Init()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 乘车基本信息 - ${param.train}次（${param.start}--${param.end}）${param.kind}列车</span><br>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="95%" align="center">
  <tr class="TableHeader">
    <td nowrap align="center">乘车时刻 </td>
    <td nowrap align="center" colspan="3">票价信息 </td>
  </tr>
  <tr class="TableData">
    <td nowrap id="ftime">开车时间： </td>
    <td nowrap id="price1">硬座票价： </td>
    <td nowrap id="price3">硬卧上： </td>
    <td nowrap id="price6">软卧上： </td>
  </tr>
  <tr class="TableData">
    <td nowrap id="etime">到达时间： </td>
    <td nowrap> </td>
    <td nowrap id="price4">硬卧中： </td>
    <td nowrap> </td>
  </tr>
  <tr class="TableData">
    <td nowrap id="allTime">运行时间： </td>
    <td nowrap id="price2">软座票价： </td>
    <td nowrap id="price5">硬卧下： </td>
    <td nowrap id="price7">软卧下： </td>
  </tr>
</table>

<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td width="100%"></td>
 </tr>
</table>

<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/infofind.gif" align="absmiddle"><span class="big3"> 列车经停站次 - ${param.train}次（
    <c:choose><c:when test="${param.pstart == ''}">${param.start}</c:when><c:otherwise>${param.pstart}</c:otherwise></c:choose> --
    <c:choose><c:when test="${param.pend == ''}">${param.end}</c:when><c:otherwise>${param.pend}</c:otherwise></c:choose> ）${param.kind}列车 - 全程${param.distance}公里，共经过${param.countStation}个车站</span><br>
    </td>
  </tr>
</table>

<br>
<div align="center" id="listDiv"></div>

<br>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="history.go(-1);">
</div>
</body>
</html>
