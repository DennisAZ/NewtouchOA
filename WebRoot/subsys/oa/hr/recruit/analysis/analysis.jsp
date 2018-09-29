<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String chose = request.getParameter("chose") == null ? "" :  request.getParameter("chose");
  String sumField = request.getParameter("sumField") == null ? "" :  request.getParameter("sumField");
  String data = request.getParameter("data") == null ? "" :  request.getParameter("data");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/FusionCharts.js"></script>
<script type="text/javascript">
var chose = "<%=chose%>";
var sumField = "<%=sumField%>";
var strXML = "<%=data%>";

var choseStr = "";
function doInit(){
}

</script>
</head>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/finance1.gif" align="absMiddle"><span class="big3">&nbsp;人才统计分析图 </span>
   </td>
 </tr>
</table>
<br>
<center>
<!-- START Script Block for Chart FactorySum -->
<div id="FactorySumDiv" align="center">
 <br>
 <div id="chart1div" align="center">
  Chart.
</div>
<script type="text/javascript"> 
    //Instantiate the Chart 
  if(chose == ""){
    choseStr = "Pie3D.swf";
  }
  if(chose == "1"){
    choseStr = "Pie3D.swf";
  }
  if(chose == "2"){
    choseStr = "Column3D.swf";
  }
  var chart1 = new FusionCharts("<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/"+choseStr+"", "chart1Id", "600","300", "0", "0");
  chart1.setTransparent("false");
  chart1.setDataXML(strXML);
  chart1.render("chart1div");
</script>
</center>
 <form name='frmUpdate' id="frmUpdate" >
 <div align="center"><input type="button" value="返回"  class="BigButton"  onClick="window.history.go(-1);"></div>
</body>
</html>
