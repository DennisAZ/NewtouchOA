<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/cmp/Calendar.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js" ></script>
<script type="text/javascript">
function doInit(){
  //配置参数
  var beginParameters = {
      //时间接收input框的id
      inputId:'timeInput'
      //设置点击那个按钮(id)弹出
      ,bindToBtn:'imgBtn'
  };
  new Calendar(beginParameters);
}
function afterSelectTime(){
  alert("选择时间回调函数");
}
</script>
</head>

<body onload="doInit()">
没有日期的输入框: 
<input value="" type="text" id="timeInput1"/>
<img onclick="showTime('timeInput1');" src="<%=imgPath %>/time.gif"/><br/>
没日期的输入框:
<input value="" type="text" id="timeInput"/><img src="<%=imgPath %>/calendar.gif" id="imgBtn"/>&nbsp;
<img onclick="showTime('timeInput' , true);" src="<%=imgPath %>/time.gif"/><br/>
带有回调函数
<input value="" type="text" id="timeInput3"/>
<img onclick="showTime('timeInput3' , false , afterSelectTime);" src="<%=imgPath %>/time.gif"/><br/>
</body>
</html>