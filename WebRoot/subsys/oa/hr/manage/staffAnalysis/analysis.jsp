<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String chose = request.getParameter("chose") == null ? "" :  request.getParameter("chose");
  String deptId = request.getParameter("deptId") == null ? "" :  request.getParameter("deptId");
  String sumField = request.getParameter("sumField") == null ? "" :  request.getParameter("sumField");
  String module = request.getParameter("module") == null ? "" :  request.getParameter("module");
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/FusionCharts.js"></script>
<script type="text/javascript">
var chose = "<%=chose%>";
var deptId = "<%=deptId%>";
var sumField = "<%=sumField%>";
var module = "<%=module%>";
var strXML = "<%=data%>";

var choseStr = "";
function doInit(){
}

</script>
</head>
<body>

<center>
 <div align="center" style="display:none" id="NoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>没有统计数据</div></td>

  </tr>
  </table>
  </div>
<!-- START Script Block for Chart FactorySum -->
<div id="FactorySumDiv" align="center">
 <br>
 <div id="chart1div" align="center">

</div>
<script type="text/javascript"> 
    //Instantiate the Chart 
 if(strXML!=""){
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
 } else {
	 $("NoData").style.display = 'block';
	 }
</script>
</center>
 <form name='frmUpdate' id="frmUpdate" >

</body>
</html>
