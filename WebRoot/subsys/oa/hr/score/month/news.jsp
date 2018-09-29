<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
%>
<html>
<head>
<title>新建考核任务</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreFlowLogic.js"></script>
<script type="text/javascript">
var userId = '<%=userId%>';
function doInit(){
  /** 
  * 获取下拉框选项 
  * getSecretFlag("RMS_SECRET","secret"); 
  * @param parentNo 代码编号 
  * @param optionType 绑定的div 
  * @param extValue 要选中的值 
  * @return 
  */ 
  var requestURLStr = contextPath + "/yh/subsys/oa/hr/score/act/YHScoreDataAct"; 
  var url = requestURLStr + "/getSelectOption.act?userId=" + userId; 
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){ 
  alert(rtJson.rtMsrg); 
  return ; 
  } 

  var prcs = rtJson.rtData; 
  var selects = document.getElementById("optionType"); 
  for(var i=0;i<prcs.length;i++){ 
  var prc = prcs[i]; 
  var option = document.createElement("option"); 
  option.value = prc.value; 
  option.innerHTML = prc.text; 
  selects.appendChild(option); 
 
  }
}


</script>
</head>
 
<body class="bodycolor" topmargin="5" onLoad="doInit();">
<table class="MessageBox" align="center" width="420">
<tr>
    <td nowrap class="TableData">是否允许查看用户列表：</td>
    <td nowrap class="TableData">
    	  <select name="optionType" id="optionType" class="BigSelect">
         
        </select>
     </td>
   </tr>

</table>


</body>
</html>
