<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>日期选择</title>
    <link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
    <link rel="stylesheet" href ="<%=cssPath%>/style.css">
	<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
  <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
    <script type="text/javascript"><!--
    function doInit(){
      var parameters = {
          inputId:'calen',
          property:{isHaveTime:true,week:['周未','周一','周二','周三','周四','周五','周六']}
      };
      
      new Calendar(parameters);
      new Calendar({inputId:'calen2',bindToBtn:'buttonId',property:{format:'yy/M/d'}});
    }
     
    --></script>  
</head>
<body onload="doInit()">
带时间，星期的显示方式为：['周未','周一','周二','周三','周四','周五','周六']
日期：<input type="text" value="" id="calen"/></br>
不带时间，星期显示为默认方式，日历显示在按扭上,输出格式为:yy/M/d
日期<input type="text" value="" id="calen2"/>
<input type="button" id="buttonId" value="日期">
<div id="fff" style="width:600px; height:400px;"></div>
</body>
</html>