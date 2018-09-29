<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %> 
    <%
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统资源管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script language="JavaScript" src="Charts/FusionCharts.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit() {
  var url = contextPath + "/yh/core/funcs/system/resManage/act/YHResManageAct/getRes.act";
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    $("container").update(json.rtData.container + "&nbsp;");
    $("used").update(json.rtData.used + "&nbsp;");
    $("space").update(json.rtData.space + "&nbsp;");
    
    $("spaceGb").update(json.rtData.spaceGb);
    $("usedGb").update(json.rtData.usedGb);
    $("containerGb").update(json.rtData.containerGb);
    var chart = new FusionCharts("Charts/Pie2D.swf", "ChartId", "500", "300", "0", "0");
	   chart.setJSONData({"chart": 
       { "caption" : "系统资源管理" ,  "xAxisName" : "",   "yAxisName" : "已用空间" }, 
         "data" :   [  { "label" : "可用空间", "value" : json.rtData.spaceGb }, 
                        { "label" : "已用空间", "value" : json.rtData.usedGb }
                          ]});	   
	   chart.render("chartdiv");
  }
}
</script>
</head>
<body onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big">
      <img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> OA所在分区硬盘空间使用情况</span>
    </td>
  </tr>
</table>

<table width="80%" align="center">
  <tr>
    <td style="height:2px;"> </td>
  </tr>
  <tr>
    <td>

<table class="TableBlock" width="500" align="center">
    <tr>
      <td width="80" class="TableData">已用空间：</td>
      <td class="TableData"> <span id="used"></span>B&nbsp;&nbsp;<b><span id="usedGb"></span>GB</b></td>
    </tr>
    <tr>
      <td class="TableData">可用空间：</td>
      <td class="TableData"> <span id="space"></span>B&nbsp;&nbsp;<b><span id="spaceGb"></span>GB</b></td>
    </tr>
    <tr class="TableControl">
      <td nowrap class="TableData">容量：</td>
      <td> <span id="container"></span>B&nbsp;&nbsp;<b><span id="containerGb"></span>GB</b></td>
    </tr>
</table>
    </td>
    <td rowspan="3" align="left" valign="bottom">
      &nbsp;<input type="button" value="系统资源监控查询" class="BigButtonC" onclick="location='resquery.jsp'"><br><br>
    <% if (dbms.equals("mysql")) { %>  
      &nbsp;<input type="button" value="主要模块数据量统计 " class="BigButtonC" onclick="location='module_data_statc.jsp'"><br>&nbsp;
    <% } %>
    </td>
  </tr>

<tr>
  <td style="height:2px;">
  <div id="chartdiv" align="center"> 
         </div>
  
  </td>
</tr>
<tr>
  <td>
<center>
  
</center>
    </td>
  </tr>
</table>
</body>
</html>