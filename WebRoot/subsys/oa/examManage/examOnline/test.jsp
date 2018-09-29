<%@ page language="java" import=" yh.core.funcs.person.data.YHPerson" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  //判断是否为管理员
  //判断是否自己是审批人员

  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
  String userName = user.getUserName();
  if(userName == null){
    userName = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>进行考试</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/FusionCharts.js"></script>
<script type="text/javascript">
function test(){
	var chart_FactorySum = new FusionCharts("<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/Pie3D.swf", "FactorySum", "600", "300", "0", "0");
  chart_FactorySum.setTransparent("false");

//Provide entire XML data using dataXML method
chart_FactorySum.setDataXML("<Chart caption='按奖惩项目统计'   Background='RRd333' formatNumberScale='0'><set label='积极参加工作' value='9' /><set label='违规操作' value='7' /><set label='违规操作' value='7' /></Chart>")
//Finally, render the chart.
chart_FactorySum.render("FactorySumDiv");
//跳到js link='javascript:t();'
//跳到本页面  link='../../index.jsp;'
//新窗口  link='n-../../index.jsp;'
}
function t(){
  alert("aaa");
}

</script>
</head>
<body onload='test();'>
<center>
 
	<!-- START Script Block for Chart FactorySum -->
	<div id="FactorySumDiv" align="center">
		Chart.
	</div>
	</center>
	
	
	<!-- START Script Block for Chart myFirst -->
	<div id="myFirstDiv" align="center">
		Chart.
	</div>
	<script type="text/javascript">	
		//Instantiate the Chart	
		var chart_myFirst = new FusionCharts("<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/Line.swf", "myFirst", "600", "350", "0", "0");
        chart_myFirst.setTransparent("false");
    
		//Provide entire XML data using dataXML method
		chart_myFirst.setDataXML("<chart caption='按期望工作性质统计（人）' xAxisName='月份' yAxisName= 'Units' showNames= '1' decimalPrecision='0' formatNumberScale='0'  bgcolor='F3f3f3' formatNumberScale='0'><set label='全职' value='6' /><set label='全职' value='4' /></chart>")
		//Finally, render the chart.
		chart_myFirst.render("myFirstDiv");
	</script>
</body>
</html>