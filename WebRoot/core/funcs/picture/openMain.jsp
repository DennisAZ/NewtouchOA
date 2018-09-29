<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片管理</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript">
function bbimg(o) {
	//alert(o.id);
  var zoom=parseInt(o.style.zoom, 10)||100;  //o.style.zoom为百分之几如30%
  zoom+=event.wheelDelta/12;
  if(zoom>0){
    o.style.zoom=zoom + "%";
  }
  //alert(o.style.zoom);
  return false;
}
//style="border:1px solid #ff0000" 
</script>

</head>
<body topmargin=3 style="background-color:gray" leftmargin=0>

<table id="pictable"  border="0"  width="100%" height=100% title="点击图片翻页" topmargin=3 cellpadding=0 cellspacing=0    onmousewheel="return bbimg(this);" onclick="parent.open_control.open_pic(1);">
<tr>
	<td align="center" valign="center" class=big height=20>
		<font color=white><b><div id="file_name"></div></b></font>
  </td>
</tr>
<tr>
	<td align="center" valign="center">
    <div id="div_image"><font color=white>正在加载图片...</font></div>
  </td>
</tr>
</table>

</body>
</html>