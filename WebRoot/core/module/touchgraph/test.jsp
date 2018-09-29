<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试</title>
<script type="text/javascript">
function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
}
function doClick() {
  var text = document.getElementById("searchText").value;
  getSwf("smalltouchgraph").doSearch(text);
}
</script>
</head>
<body>
<input type="text" value="" id="searchText"/><input type="button" value="搜索" onclick="doClick()"/>
<div style="width:202px;height:252">
	<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="smalltouchgraph" width="202" height="252"
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="smalltouchgraph.swf" />
			<param name="quality" value="high" />
			<param name="bgcolor" value="#000000" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="smalltouchgraph.swf" quality="high" bgcolor="#000000"
				width="202" height="252" name="smalltouchgraph" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
</div>
</body>
</html>