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
  var beginParameters = {
      //时间接收input框的id
      inputId:'timeInput',
      //isHaveTime是否有时间
      property:{yearRange:[2005,2010]
                  ,month:['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sept','Oct','Nov','Dec']
                  ,week:['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']}
      ,bindToBtn:'imgBtn'
  };
  new Calendar(beginParameters);
}
</script>
</head>

<body onload="doInit()">
<input value="" type="text" id="timeInput"/><img src="<%=imgPath %>/calendar.gif" id="imgBtn"/>
</body>
</html>