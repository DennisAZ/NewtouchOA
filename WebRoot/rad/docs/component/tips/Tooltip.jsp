<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/cmp/tab.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Tooltip.js" ></script>
<script type="text/javascript">
function test(){
  var a = $('v');
  new Tooltip('input',{width:300,backgroundColor:'#FAFCFD', // Default background color
    borderColor:'#CCEFF5', // Default border color
    textColor:'#999999', // Default text color (use CSS value)
    maxWidth:150, // Default tooltip width
    mouseFollow:true},'这是一个tesdddd');
  new Tooltip(a,{width:100,backgroundColor:'red', // Default background color
    borderColor:'blue', // Default border color
    textColor:'#999999', // Default text color (use CSS value)
    maxWidth:500, // Default tooltip width
    mouseFollow:false},'这是一个dddd');
}
</script>
</head>

<BODY onload="test()">
  <input type="text" id="input" name="" value="" /><br/>
<a href="#" id="v">dddddd</a>
</body>
</html>