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
      inputId:'timeInput',
      //isHaveTime是否有时间
      property:{isHaveTime:true,format:'yy年M月d日'}
      //设置点击那个按钮(id)弹出
      ,bindToBtn:'imgBtn'
  };
  new Calendar(beginParameters);

  //配置参数
  var beginParameters2 = {
      //时间接收input框的id
      inputId:'timeInput2',
      //isHaveTime是否有时间
      property:{isHaveTime:true,format:'yy年M月d日  hh小时mm'}
      //设置点击那个按钮(id)弹出
      ,bindToBtn:'imgBtn2'
  };
  new Calendar(beginParameters2);
}
</script>
</head>

<body onload="doInit()">
<input value="" type="text" id="timeInput"/><img src="<%=imgPath %>/calendar.gif" id="imgBtn"/>
<input value="" type="text" id="timeInput2"/><img src="<%=imgPath %>/calendar.gif" id="imgBtn2"/>
</body>
</html>