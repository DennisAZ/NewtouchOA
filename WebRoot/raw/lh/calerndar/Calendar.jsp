<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>日期选择</title>
    <link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
    <link rel="stylesheet" href ="<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/raw/lh/fckeditor/fckeditor.js"></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
  <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
  <script type="text/javascript" src="Time1.0.js"></script>
      
    <script type="text/javascript"><!--
    var timeField = null;
    function doInit(){
      //时间配置参数
      var parameters = {
          inputId:'calen',
          property:{isHaveTime:true}
      };
     // alert("ss");
     //新建 一个时间对象
     new Calendar(parameters);
      //alert("ss2");
      new Calendar({inputId:'calen2',bindToBtn:'buttonId'});

     // new Time({inputId:'calen3',bindToBtn:'buttonId2'});
      
    }
     function showTime(time , isHaveDate) {
       if (!isHaveDate) {
         $(time).value = "";
       }
       timeField = time;
       var url = contextPath + "/raw/lh/calerndar/clock.jsp";
       openDialog(url ,  280, 120);
     }
    --></script>  
</head>
<body onload="doInit()">
<input type="text" value="" id="calen"/>
<input type="text" value="" onKeyUp="value=value.replace(/\D{2}[0-2][0-3]/g,'')"/>
<input type="text" value="" id="calen2"/>
<input type="button" id="buttonId" value="日期">
<input type="text" value="" id="calen3" size="9"/>
<input type="button" id="buttonId2" onclick="showTime('calen3')" value="时间">
<div id="fff" style="width:600px; height:400px;"></div>
</body>
</html>