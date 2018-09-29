<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<title>时间轴</title>
<script type="text/javascript" src="time.js"></script>
</head>
<body onload="doInit()" >
<div style="">
  	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="time" width="100%" height="40"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="time.swf?isDate1=1" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#869ca7" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="time.swf" quality="high" bgcolor="#869ca7"
				width="501" height="40" name="time" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object></div>
<div align=center>
选择时间范围：从 <input type="text" name="date1" id=date1 size="10" class="SmallInput" value="" readonly>
      <img id="date1Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" align=absmiddle border="0" style="cursor:pointer" >
        至 <input type="text" name="date2" id="date2" size="10" class="SmallInput" value="" readonly>
        <img id="date2Img" src="<%=imgPath%>/calendar.gif" align="absMiddle" align=absmiddle border="0" style="cursor:pointer" >
        <a href="javascript:empty_date();" class="orgClear" onclick="empty_date();">清空</a>
        &nbsp;<select name="dateRange" id="dateRange" class="SmallSelect" onchange="date_change(this.value)">
          <option value="0">快捷选择</option>
          <option value="1">今日</option>
          <option value="2">昨日</option>
          <option value="3">本周</option>
          <option value="4">上周</option>
          <option value="5">本月</option>
          <option value="6">上月</option>
        </select>
</div>
</body>
</html>