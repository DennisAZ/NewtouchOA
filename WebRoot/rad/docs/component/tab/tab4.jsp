<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>同步加载多标签</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<link href="<%=cssPath %>/cmp/tab.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
function doInit(){
  var ca = [{title:"标签一", contentUrl:"tab2/tab1.jsp" , useTextContent:true ,onload:tabOpen.bind(this , '标签一')}
    ,{title:"标签二", contentUrl:"tab2/tab2.jsp" , useTextContent:true , isShow:true}
    ,{title:"标签三", contentUrl:"tab1/tab3.jsp",useIframe:true ,onload:tabOpen.bind(this , '标签三')}
    ]
  var rigthContent = "右边的内容<input type='text' value=''/><input type=\"button\" value=\"保存\"/>";
  buildTab(ca , "content" , 0 , rigthContent);
}
function tabOpen(tab) {
  alert("你刚打开了：" + tab);
}
</script>
</head>

<body onload="doInit();">
<div id="content"></div>
</body>
</html>