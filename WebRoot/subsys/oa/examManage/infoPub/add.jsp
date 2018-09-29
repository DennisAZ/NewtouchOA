<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
<head>
<title>新建考试信息</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/infoPub.js"></script>
<script type="text/javascript">
var beginEnd = "<%=sf.format(new Date())%>";
</script>
</head>
 
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 新建考试信息</span>
    </td>
  </tr>
</table>
<form id="form1" method="post" name="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.examManage.data.YHExamFlow">
<input type="hidden" name="sendTime" id="sendTime" value="<%=sf.format(new Date()) %>">
<input type="hidden" name="flowFlag" id="flowFlag" value="">
<input type="hidden" name="anonymity" id="anonymity" value="">
<table width="70%" align="center" class="TableBlock">
    <tr>
      <td nowrap class="TableData">考试名称：<font color="red">*</font></td>
      <td class="TableData">
        <input type="text" name="flowTitle" id="flowTitle" size="40" maxlength="40" class="BigInput" value="">
      </td>
    </tr>
 
    <tr>
      <td nowrap class="TableData">参加考试人：<font color="red">*</font></td>
      <td class="TableData">
        <input type="hidden" name="participant" id="participant" value="">
        <textarea cols=40 name="participantDesc" id="participantDesc" rows=5 class="BigStatic" wrap="yes" readonly></textarea>
        <br><a href="javascript:;" class="orgAdd" onClick="selectUser(['participant', 'participantDesc'])">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('participant').value='';$('participantDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">试卷：<font color="red">*</font></td>
      <td class="TableData">
         <select name="paperId" id="paperId">
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 有效期：</td>
      <td class="TableData">
        生效日期：<input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput" value="" readonly>
    <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
        为空为立即生效(<font color="red">默认当天</font>)<br>
        终止日期：<input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" readonly>
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
        为空为手动终止
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData">
       <span id="smsRemindDiv">
      <input type="checkbox" name="smsflag2" id="smsflag2" onClick="checkBox2();">
      <label>使用内部短信提醒</label>&nbsp;&nbsp;</span>
      <span id="sms2Remind3">&nbsp;&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" onClick="checkBox3();">
      <label>使用手机短信提醒</label>&nbsp;&nbsp;</span>
      <input type="hidden" name="smsSJ" id="smsSJ" value="0">
      <input type="hidden" name="smsflag" id="smsflag" value="0">     
</td>
    </tr>
     <tr>
      <td nowrap class="TableData">描述：</td>
      <td class="TableData">
        <textarea name="flowDesc" id="flowDesc" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
    <tr class="TableControl">
      <td colspan="2" nowrap align="center">
        <input type="button" value="发布" class="BigButton" onClick="checkForm2();">&nbsp;&nbsp;
    <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>