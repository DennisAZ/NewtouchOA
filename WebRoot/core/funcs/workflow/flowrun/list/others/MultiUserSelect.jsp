<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String single = request.getParameter("isSingle");
boolean isSingle = false;
if (single != null && !"".equals(single)) {
  isSingle = Boolean.valueOf(single);
}
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("flowPrcs");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人员选择</title>
<base target="_self">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/orgselect.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<style type="text/css">
.header {
  height: 23px;
  FONT-WEIGHT: bold;
  FONT-SIZE: 12pt;
  background-color: #f7f8bd;
  cursor: pointer;
  padding-top: 3px;
  border-top-color:#dcdcdc;
  border-top-width:1px;
  border-top-style:solid;
  border-right-color:#dcdcdc;
  border-right-width:1px;
  border-right-style:solid;
  border-left-color:#dcdcdc;
  border-left-width:1px;
  border-left-style:solid;
}
#right {
  padding-top: 10px;
  padding-right: 10px;
  float: left;
  width:200px;
  height:320px;
  text-align: center;
  font-size: 12px;
}
</style>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="MultiUserSelect.js"></script>
<script type="text/javascript">
var isSingle = <%=isSingle%>;//是否是单用户选择
var flowId = "<%=flowId%>";
var prcsId = "<%=prcsId%>";

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
</script>
</head>
<body onload="doInit()" style="padding-right:0px">
<div id="left">

</div>
<div id="right">
</div>
<div style="position:absolute;top:330px;left:245px;height:30px;width:60px;">
  <input type=button class="SmallButtonW" value="确定" onclick="window.close()"/>
</div>
</body>
</html>