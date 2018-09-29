<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String index = request.getParameter("index") == null ? "" : request.getParameter("index"); 
  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId");
  String flag = request.getParameter("flag") == null ? "" : request.getParameter("flag");
  String isConnection = YHSysProps.getProp("isConnection");
  if(YHUtility.isNullorEmpty(isConnection)){
    isConnection = "0";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">


<style type="text/css">

.clearfix {
display:block;
margin:0;
padding:0;
bottom:0;
position:fixed;
width:100%;
z-index:88888;
}

.m-chat .chatnote {
float:left;
height:25px;
line-height:25px;
position:absolute;
}
.m-chat {
-moz-background-clip:border;
-moz-background-inline-policy:continuous;
-moz-background-origin:padding;
background:transparent url(http://xnimg.cn/imgpro/chat/xn-pager.png) repeat-x scroll 0 -396px;
border-left:1px solid #B5B5B5;
display:block;
height:25px;
margin:0 15px;
position:relative;
}
.operateLeft{
	float: left;
	font-size: 12px;
	text-align: right;
	width: 80px;
	border-right-width: 1px;
	border-right-style: solid;
	border-right-color: #999;
}
.operateRight{
	float: left;
	font-size: 12px;
	text-align: left;

}


</style>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
var index = '<%=index%>';
var flag = '<%=flag%>';
var tabs = null;
function doInit() {
  var jso = [
             {title:"待签收公文", useTextContent:false , imgUrl:"<%=imgPath%>/sys_config[1].gif", contentUrl:"<%=contextPath %>/subsys/jtgwjh/receiveDoc/manage/waitReceive.jsp",useIframe:true}
            ,{title:"已签收公文", useTextContent:false ,imgUrl:"<%=imgPath%>/sys_config[1].gif", contentUrl:"<%=contextPath %>/subsys/jtgwjh/receiveDoc/manage/waitHandle.jsp?isConnection=<%=isConnection %>", useIframe:true,isShow:flag}
            ,{title:"下载中公文", useTextContent:false ,imgUrl:"<%=imgPath%>/sys_config[1].gif", contentUrl:"<%=contextPath %>/subsys/jtgwjh/receiveDoc/manage/receiving.jsp", useIframe:true}
            ];
  tabs = buildTab(jso, 'contentDiv', 800);
}
</script>
</head>
<body onload="doInit()">
<div id="contentDiv"></div>

</body>
</html>