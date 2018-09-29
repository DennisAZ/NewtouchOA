<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>视频会议信息</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript">
function check(){
  var url = "<%=contextPath%>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/checkUser.act";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    var data = json.rtData;
    if(data.state == 1){
	    var jso = [
	               {title:"视频会议管理", contentUrl:"<%=contextPath%>/subsys/oa/netmeeting/video/manage/manage.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
	              ,{title:"新建视频会议", contentUrl:"<%=contextPath%>/subsys/oa/netmeeting/video/manage/new.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
	              ,{title:"视频会议查询", contentUrl:"<%=contextPath%>/subsys/oa/netmeeting/video/manage/query.jsp", imgUrl: "<%=imgPath%>/notify_new.gif", useIframe:true}
	              ];
	    buildTab(jso, 'smsdiv', 800);
    }
    else if(data.state == 0){
    	window.location.href = "<%=contextPath %>/subsys/oa/netmeeting/video/manage/check.jsp";
    }
    else if(data.state == 2){
      window.location.href = "<%=contextPath %>/subsys/oa/netmeeting/video/manage/error.jsp";
    }
  } else{
    alert("视频会议系统配置失败！");
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="check();">
</body>
</html>
