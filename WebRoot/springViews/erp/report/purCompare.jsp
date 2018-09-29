<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>采购额趋势统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/springViews/js/jquery-1.4.2.js">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/springViews/js/json.js"/></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/springViews/js/FusionCharts.js"></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script type="text/javascript">
function doInit(){
  initTime();
}


function checkForm(){
  var beginTime = $F('beginTime');
  var endTime = $F('endTime');
  if (endTime && beginTime && beginTime > endTime){ 
    alert("结束时间不能小于起始时间！");
    return false;
  }
  return true;
}
function initTime(){
  var beginTimePara = {
      inputId:'beginTime',
      property:{isHaveTime:false},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
  
  var endTimePara = {
      inputId:'endTime',
      property:{isHaveTime:false},
      bindToBtn:'endTimeImg'
  };
  
  new Calendar(endTimePara);

  var date = new Date();
}
function doSubmit(){
	if(checkForm()){
	var proId=jQuery('#proId').val();
  
  var beginTime=jQuery('#beginTime').val();
  var endTime=jQuery('#endTime').val();
  var url = contextPath + "/yh/subsys/oa/hr/salary/welfare_manager/act/YHSalaryAnalysisAct/doPurCompareReport.act";
  var param = "proId=" + proId + "&beginTime="+beginTime+"&endTime="+endTime;
  var json  = getJsonRs(url , param);

  	if (json.rtState == '0') {
  	  $('container').update("");
    var data = json.rtData;
    var header = data.tableHeader;
    if (!header) {
      alert("未查询到结果!");
      return ;
    }
      imageReader(data.tableData);
      
    
     }
	 }
	
}
                                      
function imageReader(data) {
  var chart = new FusionCharts("/yh/subsys/oa/examManage/examOnline/FusionCharts/Line.swf", "ChartId", "600", "300", "0", "0");
  var chartData = {"chart":{ "caption" : "采购单价趋势分析" ,  "xAxisName" : "采购日期",   "yAxisName" : "单价" }, "data" : []};
  
  for (var i = 0 ;i< data.length ;i++) {
    var deptName = data[i][0];
    var count = data[i][data[i].length - 1];
    var d = { "label" : deptName, "value" :  count}
    chartData.data.push(d);
  }   
  chart.setJSONData(chartData);
  chart.render("container");
}
function getRow(data , dataCount , j) {
  var deptName = data[0];
  var className = "TableLine1";
  if (j%2 == 0) {
    className = "TableLine2";
  }
  var str = "<tr class='"+className+"'><td>"+deptName+"</td>" ;
  var i = 1;
  for (;i < data.length ; i++) {
    var ss = data[i];
    str += "<td>"+ss+"</td>";
  }
  str += "</tr>";
  return str;
}
function getCols(header) {
  var hs = header.split(",");
  return hs.length - 1;
}
function getHeader(header) {
  var hs = header.split(",");
  var str = "<tr class='TableHeader'>" ;
  for (var i = 0 ;i < hs.length ; i++) {
    var ss = hs[i];
    if (ss) {
      str += "<td>"+ss+"</td>";
    }
  }
  str += "</tr>"
  return str;
}
function  dioShow(){
	 	var url="<%=contextPath%>/springViews/erp/report/selectProduct.jsp";
 
 	var str=openDialogResize(url,  520, 400);
 	var arrStr=str.split(",");
	jQuery('#proId').val(arrStr[0]);
	jQuery('#proName').val(arrStr[1]);
	
}
</script>
</head>
<body class="bodycolor" topmargin="5"  onLoad="doInit()" >
<form action="" method="post" name="form1" id="form1" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/finance1.gif" align="absMiddle"><span class="big3">&nbsp;采购单价趋势统计分析 </span>
   </td>
 </tr>
</table>
<br>
<table align="center" width="50%" class="TableBlock">
    <tr>
      <td nowrap  class="TableContent" width="80" >请选择产品：</td>
      <td class="TableData" nowrap   colspan="3">
      		<input type="hidden" class="BigInput" id="proId" readOnly>
      		<input type="text" class="BigInput" id="proName" readOnly>
      		<input type="button" value="选择产品"  class="BigButton" onclick="dioShow()">
      		
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">
      日期：
      </td>
      <td nowrap class="TableData">
      	起始时间： 
        <input type="text" id="beginTime" name="beginTime" size="12" maxlength="12" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
        截止时间：
        <input type="text" id="endTime" name="endTime" size="12" maxlength="12" class="BigInput" value="" readonly>
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80" >采购单价趋势统计 图 ：</td>
      <td class="TableData" nowrap colspan="3">
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE4' value="3" checked ><label for="SUMFIELD2">折线图</label>&nbsp; 
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"  colspan="4" >
	  <div align="center">
	  <input type="button"  class="BigButton" value="确定" onClick="doSubmit()" />
	  </div>
	  </td>
    </tr>
    </table>
</form>
<br/>
<div id="container" align="center">

</div>
</body>
</html>
