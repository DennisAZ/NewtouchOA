<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
    <%@ include file="/core/inc/header.jsp" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统资源管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript">
var requestUrl = contextPath + "/yh/core/funcs/system/resManage/act/YHResManageAct";
function res_call_back()
{
   //显示“正在进行资源回收，请稍候”
   var msgArea=document.getElementById("msgArea");
   msgArea.style.position = "absolute";
   msgArea.style.display="";
   msgArea.style.top=(document.body.scrollHeight-parseInt(msgArea.style.height))/2 + "px";
   msgArea.style.left=(document.body.scrollWidth-parseInt(msgArea.style.width))/2 + "px";

   document.getElementById("CONFIRM_FLAG").value = "Y";
   form1.target = "hiddenFrame";
   form1.submit();
}

function res_call_back_public()
{
   //显示“正在进行资源回收，请稍候”
   var msgArea=document.getElementById("msgArea");
   msgArea.style.position = "absolute";
   msgArea.style.display="";
   msgArea.style.top=(document.body.scrollHeight-parseInt(msgArea.style.height))/2 + "px";
   msgArea.style.left=(document.body.scrollWidth-parseInt(msgArea.style.width))/2 + "px";

   document.getElementById("CONFIRM_FLAG").value = "N";
   form1.submit();
}

function delete_return(remark)
{
   var msgArea=document.getElementById("msgArea");
   msgArea.style.display="none";
   window.location = contextPath + "/core/funcs/system/res_manage/callback/resultMsg.jsp?remark=" + encodeURIComponent(remark) ;
}
function doInit(){
  var userIdGarbageStr = "";
  var url = requestUrl + "/delGarbageCon.act";
  var json = getJsonRs(url);
  if (json.rtState != "0") {
	return ;
  }
  alert("删除成功！");
  userIdGarbageStr = json.rtData.userIdGarbageStr;
  if (userIdGarbageStr) {
    $("div").show();
  } else {
    res_call_back_public();
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">

<div id="msgArea" style="width:280px;height:150px;border: 1px solid #000000;text-align:center;display:none;" class="TableContent">
  <br><img src="<%=imgPath %>/loading.gif" align="absMiddle"> <h3>资源回收正在进行，请稍候……</h3>
</div>

<form action="<%=contextPath %>/yh/core/funcs/system/resManage/act/YHResManageAct/deleteGarbage.act" target="hiddenFrame" method="post" name="form1">
	<!-- 有个人垃圾时，需要确认，为Y，否则为N  -->
	<input type="hidden" id="CONFIRM_FLAG" name="CONFIRM_FLAG">
</form>

<!-- 隐藏帧，回收资源时提交到这里 -->
<iframe name="hiddenFrame" id="hiddenFrame" width=0 height=0 frameborder=0 scrolling=no></iframe>
<div align="center" id="div" style="display:none">
  <input type="button" value="点击继续" class="BigButton" onclick="res_call_back();" title="点击此按钮开始清除系统垃圾">&nbsp;
  <input type="button" value="返回" class="BigButton" onclick="window.location='index.php';">
</div>
</body>
</html>