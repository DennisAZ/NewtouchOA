<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>薪酬统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script language="JavaScript" src="Charts/FusionCharts.js"></script>
<script type="text/javascript">
function doInit(){
  var myDate = new Date();
  setYearAndMonth(myDate.getFullYear() , myDate.getMonth()+1);
}
function setYearAndMonth(year , month){
  var option1;
  var option2;
  for(var i = 2000 ; i<2102 ; i++){
    $('salYear').insert(option1);
    option1 = new Element('option', {"value" : i});
    if (i == year) {
      option1.selected = true;
    }
    option1.insert(i+'年');
  }
  for(var i = 1 ; i<14 ; i++){
    $('salMonth').insert(option2);
    option2 = new Element('option', {"value" : i});
    if (i == month) {
      option2.selected = true;
    }
    option2.insert(i+'月');
  }
}
function doSubmit(){
  var deptStr = $('dept').value;
  if (!deptStr) {
    alert("请选择部门!");
    return;
  }
  var url = contextPath + "/yh/subsys/oa/hr/salary/welfare_manager/act/YHSalaryAnalysisAct/doAnalysis.act";
  var param = "dept=" + deptStr + "&salYear=" + $('salYear').value + "&salMonth=" +  $('salMonth').value;
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
      var tableData = data.tableData;
      var table = "<table width='100%' border=\"0\"  class=\"TableList\"  style=\"clear:both;table-layout:fixed;\" >" + getHeader(header);
      for (var i = 0 ;i< tableData.length ;i++) {
        table += getRow(tableData[i] , data.dataCount , i)
      }
      var cols = getCols(header);
      table += getCount(data.dataCount , cols);
      table += "</table>";
      $('container').update(table);
    } else if ($('MAP_TYPE2').checked) {
      imageReader(data.tableData);
    } else {
      imageReader2(data.tableData);
    }
    
  }
}
function imageReader2(data) {
  var chartData = {"chart":{ "caption" : "薪酬统计分析" ,  "xAxisName" : "",   "yAxisName" : "部门总计" }, "data" : []};
  var chart1 = new FusionCharts("/yh/subsys/oa/examManage/examOnline/FusionCharts/Column3D.swf", "chart1Id", "600","300", "0", "0");
  chart1.setTransparent("false");
  for (var i = 0 ;i< data.length ;i++) {
    var deptName = data[i][0];
    var count = data[i][data[i].length - 1];
    var d = { "label" : deptName, "value" :  count}
    chartData.data.push(d);
  }
  chart1.setJSONData(chartData);
  chart1.render("container");
}
                                       
function imageReader(data) {
  var chart = new FusionCharts("/yh/subsys/oa/examManage/examOnline/FusionCharts/Pie2D.swf", "ChartId", "600", "300", "0", "0");
  var chartData = {"chart":{ "caption" : "薪酬统计分析" ,  "xAxisName" : "",   "yAxisName" : "部门总计" }, "data" : []};
  
  for (var i = 0 ;i< data.length ;i++) {
    var deptName = data[i][0];
    var count = data[i][data[i].length - 1];
    var d = { "label" : deptName, "value" :  count}
    chartData.data.push(d);
  }   
  chart.setJSONData(chartData);
  chart.render("container");
}
function getCount(count , cols) {
  var str = "<tr class='TableControl'><td align='center' colspan="+(cols + 1)+">合计</td><td>"+count+"</td><td>&nbsp;</td></tr>" ;
  return str;
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
  str += "<td>"+((data[i-1] / dataCount).toFixed(2) * 100)+"%</td></tr>";
  return str;
}
function getCols(header) {
  var hs = header.split(",");
  return hs.length - 1;
}
function getHeader(header) {
  var hs = header.split(",");
  var str = "<tr class='TableHeader'><td>部门</td>" ;
  for (var i = 0 ;i < hs.length ; i++) {
    var ss = hs[i];
    if (ss) {
      str += "<td>"+ss+"</td>";
    }
  }
  str += "<td>总计</td>";
  str += "<td>所占比例</td>";
  str += "</tr>"
  return str;
}
</script>
</head>
<body class="bodycolor" topmargin="5"  onLoad="doInit()" >
<form action="" method="post" name="form1" id="form1" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/finance1.gif" align="absMiddle"><span class="big3">&nbsp;薪酬统计分析 </span>
   </td>
 </tr>
</table>
<br>
<table align="center" width="50%" class="TableBlock">
    <tr>
      <td nowrap  class="TableContent" width="80" >统计部门：</td>
      <td class="TableData" nowrap   colspan="3">
      <input type="hidden" value="" name="dept" id="dept">
      <textarea rows="5" cols="40" id="deptDesc" name="deptDesc" class="BigStatic"></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['dept', 'deptDesc'],14)">选择</a>  
        <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap  class="TableContent" width="80" >工资月份：</td>
      <td class="TableData" nowrap   colspan="3">
      年度：<select id="salYear" name="salYear"></select>
                    月份：<select id="salMonth" name="salMonth"></select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80" >统 计 图 ：</td>
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
