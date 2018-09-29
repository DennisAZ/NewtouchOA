<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHConst" %>
<%
  YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER); 
  int deptId = person.getDeptId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>选择经办人和主办人</title>
<base target="_self">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=contextPath%>/core/funcs/doc/workflowUtility/flowUserSelect.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/Javascript" src="FreeFlowUserSelect.js" ></script>
<script type="text/javascript">
var ctroltime=null,key="";
function CheckSend()
{
  var kword = $("kword");
  if(kword.value=="按用户名或姓名搜索...")
     kword.value="";
  if(kword.value=="" && $('search_icon').src.indexOf(imgPath + "/quicksearch.gif")==-1)
  {
     $('search_icon').src= imgPath +  "/quicksearch.gif";
  }
  if(key!=kword.value && kword.value!="")
  {
     key=kword.value;
     doSearch(key);
     if($('search_icon').src.indexOf(imgPath + "/quicksearch.gif")>=0)
     {
         $('search_icon').src= imgPath + "/closesearch.gif";
         $('search_icon').title="清除关键字";
         $('search_icon').onclick=function(){kword.value='按用户名或姓名搜索...';$('search_icon').src= imgPath + "/quicksearch.gif";$('search_icon').title="";$('search_icon').onclick=null;};
     }
  }
  ctroltime=setTimeout(CheckSend,100);
}
var deptId = '<%=deptId%>';
</script>
</head>
<body onload="doInit()">
<div id="left">
<div style="border:1px solid #000000;background:#FFFFFF;">
  <input type="text" id="kword" name="kword" value="按用户名或姓名搜索..."  onfocus="ctroltime=setTimeout(CheckSend,100);" onblur="clearTimeout(ctroltime);if(this.value=='')this.value='按用户名或姓名搜索...';"  class="SmallInput" style="border:0px; color:#A0A0A0;width:145px;"><img id="search_icon" src="<%=imgPath %>/quicksearch.gif" align=absmiddle style="cursor:pointer;">
</div>
</div>
<div id="rightDiv">
<div id="right">
</div>
</div>
<div style="position:absolute;top:330px;left:245px;width:60px;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</body>
</html>