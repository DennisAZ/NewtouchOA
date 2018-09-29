<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>参数设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/netmeeting/video/js/util.js"></script>
<script> 
function doInit(){
  var url = "<%=contextPath %>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/getParameters.act";
  var json = getJsonRs(url);
  if (json.rtState == "0"){
	  var data = json.rtData;
	  $('ip').value = data.ip;
	  $('port').value = data.port;
  } else{
    alert("获取设置失败");
  }
}

function submitForm() {
  if (!$F('ip')){
    alert('IP地址不能为空');
    return;
  }
  if (!$F('port')){
    alert('web端口号不能为空');
    return;
  }
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/netmeeting/video/act/YHVideoMeetingAct/setParameters.act";
  var json = getJsonRs(url,pars);
  if (json.rtState == "0"){
    alert("设置成功");
  } else{
    alert("设置失败");
  }
}

function clear1(){
	$('ip').value = "";
  $('port').value = "";
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<br><br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify.gif" align="absMiddle" width="17"><span class="big3">&nbsp;编辑管理员    </span>
   </td>
 </tr>
</table>

<form action=""  method="post" name="form1" id="form1">
  <table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">红杉树系统IP地址：<font color="red">*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="ip" id="ip" class="BigInput" size="20" maxlength="50" >
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">红杉树系统web端口号：<font color="red">*</font></td>
    <td nowrap class="TableData">
        <input type="text" name="port" id="port" class="BigInput" size="20" maxlength="50" >
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" id="seqId" name="seqId">
      <input type="hidden" value="yh.subsys.oa.netmeeting.video.data.YHVideoMeetingManager" name="dtoClass" id="dtoClass">
      <input type="button" value="确定" onclick="submitForm()">&nbsp;&nbsp;
      <input type="button" value="清空" onclick="clear1()">&nbsp;&nbsp;
    </td>
   </tr>
  </table>
</form>
</body>
</html>