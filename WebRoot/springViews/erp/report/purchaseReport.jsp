<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>采购额统计分析</title>
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
	var purType=jQuery('#purType').val();
  
  var beginTime=jQuery('#beginTime').val();
  var endTime=jQuery('#endTime').val();
  var url = contextPath + "/yh/subsys/oa/hr/salary/welfare_manager/act/YHSalaryAnalysisAct/doPurchaseReport.act";
  var param = "purType=" + purType + "&beginTime="+beginTime+"&endTime="+endTime;
  var json  = getJsonRs(url , param);

  	if (json.rtState == '0') {
  	  $('container').update("");
    var data = json.rtData;
    var header = data.tableHeader;
    if (!header) {
      alert("未查询到结果!");
      return ;
    }
    if ($('MAP_TYPE1').checked) {
    var sum=0.0;
      var tableData = data.tableData;
      var table = "<table width='100%' border=\"0\"  class=\"TableList\"  style=\"clear:both;table-layout:fixed;\" >" + getHeader(header);
      for (var i = 0 ;i< tableData.length ;i++) {
        table += getRow(tableData[i] , data.dataCount , i)
      var total= tableData[i].toString();
     	 var arrSum=total.split(",");
       	 sum+=parseFloat(arrSum[1]);
      }
      	table+="<tr><td>采购额总计</td><td>"+sum+"</td></tr>"
      var cols = getCols(header);
      table += "</table>";
      $('container').update(table);
    } else if ($('MAP_TYPE2').checked) {
      imageReader(data.tableData);
      
    } else {
      imageReader2(data.tableData);
    }
    
     }
	 }
	
}
                                      
function imageReader(data) {
  var chart = new FusionCharts("/yh/subsys/oa/examManage/examOnline/FusionCharts/Pie2D.swf", "ChartId", "600", "300", "0", "0");
  var chartData = {"chart":{ "caption" : "采购额分析" ,  "xAxisName" : "采购员",   "yAxisName" : "采购额总计" }, "data" : []};
  
  for (var i = 0 ;i< data.length ;i++) {
    var deptName = data[i][0];
    var count = data[i][data[i].length - 1];
    var d = { "label" : deptName, "value" :  count}
    chartData.data.push(d);
  }   
  chart.setJSONData(chartData);
  chart.render("container");
}
function imageReader2(data) {
  var chartData = {"chart":{ "caption" : "采购额分析" ,  "xAxisName" : "采购员",   "yAxisName" : "采购额总计" }, "data" : []};
  var chart1 = new FusionCharts("/yh/subsys/oa/examManage/examOnline/FusionCharts/Column3D.swf", "chart1Id", "600","300", "0", "0");
  chart1.setTransparent("false");
  for (var i = 0 ;i< data.length ;i++) {
    var saleName = data[i][0];
    var total = data[i][data[i].length - 1];
    var d = { "label" : saleName, "value" :  total}
    chartData.data.push(d);
  }
  chart1.setJSONData(chartData);
  chart1.render("container");
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
</script>
</head>
<body class="bodycolor" topmargin="5"  onLoad="doInit()" >
<form action="" method="post" name="form1" id="form1" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/finance1.gif" align="absMiddle"><span class="big3">&nbsp;采购额统计分析 </span>
   </td>
 </tr>
</table>
<br>
<table align="center" width="50%" class="TableBlock">
    <tr>
      <td nowrap  class="TableContent" width="80" >统计方式：</td>
      <td class="TableData" nowrap   colspan="3">
      	<select id="purType">
      		<option value="purperson">采购员</option>
      		<option value="supplier">供货商</option>
      		<option value="product">产品</option>
      	</select>
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
      <td nowrap class="TableContent" width="80" >采购额统计 图 ：</td>
      <td class="TableData" nowrap colspan="3">
       <input type="radio" name='MAP_TYPE' id='MAP_TYPE1' value="0" checked><label for="SUMFIELD1">列表</label>&nbsp;
      	 <input type="radio" name='MAP_TYPE' id='MAP_TYPE2' value="1" ><label for="SUMFIELD1">饼图</label>&nbsp;
         <input type="radio" name='MAP_TYPE' id='MAP_TYPE3' value="2" ><label for="SUMFIELD2">柱状图</label>&nbsp; 
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
