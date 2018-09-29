<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<title>公告通知审批</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>

<script type="text/javascript">
var seqId = "<%=seqId%>";
var newsInfo = null;
var toId = null;
var userId = null;
var privId = null;

function doInit(){
	 var sendParameters = {
		      inputId:'sendTime',
		      property:{isHaveTime:true}
		      ,bindToBtn:'sendTimeImg'
		  };
		  new Calendar(sendParameters);
	 var beginParameters = {
		      inputId:'beginDate',
		      property:{isHaveTime:false}
		      ,bindToBtn:'beginDateImg'
		  };
		  new Calendar(beginParameters);
	 var endParameters = {
			  inputId:'endDate',
			  property:{isHaveTime:false}
			  ,bindToBtn:'endDateImg'
		  };
		  new Calendar(endParameters);
	 var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyAuditingAct/beforeAuditingPass.act?seqId='+seqId;
	 var json = getJsonRs(url);
	 if(json.rtState == "0"){
	   var data = json.rtData;
	   var toNameTitle = data.toNameTitle;
	      var toNameStr = data.toNameStr;
	      var privNameTitle = data.privNameTitle;
	      var privNameStr = data.privNameStr;
	      var userNameTitle = data.userNameTitle;
	      var userNameStr = data.userNameStr;
	      var title = toNameTitle + privNameTitle + userNameTitle;
	      var str = toNameStr + privNameStr + userNameStr;
		$('subject').update(data.subject);
	    $('subject').title = data.subjectTitle;
	    $('fromId').title = "部门:"+data.deptName;
	    $('fromId').style.cursor = "hand";
	    $('fromId').update(data.fromName);   
	    $('toId').update(str);   
	    $('toId').title = title;
	    $('sendTime').value = data.sendTime.substring(0,19);
	    $('beginDate').value = data.beginDate;
	    $('endDate').value = data.endDate;
	    $('seqId').value = data.seqId;
	 }else{
		 document.body.innerHTML = json.rtMsrg;
	 } 
}

function checkForm()
{
   if($('reason').value=="")
   { 
     alert("审批意见不能为空！");
     return false;
   }
   var regex = /^((\d{2}(([02468][048])|([13579][26]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|([1-2][0-9])))))|(\d{2}(([02468][1235679])|([13579][01345789]))[\-\/\s]?((((0?[13578])|(1[02]))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\-\/\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\-\/\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))/; //日期部分
   if(document.form1.beginDate.value!=""){
     if(!regex.test(document.form1.beginDate.value)){
   	 alert("输入的日期格式错误");
   	 document.form1.beginDate.value = "";
   	 document.form1.beginDate.focus();
       return false;
     }
   }
   if(document.form1.endDate.value!=""){
       if(!regex.test(document.form1.endDate.value)){
     	 alert("输入的日期格式错误");
     	document.form1.endDate.value = "";
     	 document.form1.endDate.focus();
         return false;
       }
  }
   if(document.form1.sendTime.value!=""){
       if(!regex.test(document.form1.sendTime.value)){
     	 alert("输入的日期格式错误");
     	document.form1.sendTime.value = "";
     	 document.form1.sendTime.focus();
         return false;
       }
  }
   if(document.form1.endDate.value!=""||document.form1.endDate.value ){
		 if(document.form1.endDate.value<document.form1.beginDate.value) {
		    alert("生效日期不能晚于终止日期");
		    return false;
		 }
  }		 
   return (true);
}


function resetTime(){
	  var  currentDate = new Date();
	  currentDate = currentDate.format('yyyy-MM-dd hh:mm:ss');
	  document.form1.sendTime.value = currentDate;
 }

function resetDate(){
	  var  currentDate = new Date();
	  currentDate = currentDate.format('yyyy-MM-dd');
	  document.form1.beginDate.value = currentDate;
}

function sendForm(){
	if(checkForm()){
		  var url = "<%=contextPath %>/yh/core/funcs/notify/act/YHNotifyAuditingAct/operation.act";
		  $("form1").action = url;
		  $("form1").submit();
    }
}
</script>
</head>
<body onload="doInit();">
<form  action=""  method="post" name="form1" id="form1">
<table border="0" width="90%" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/search.gif"><span class="big3"> 公告通知审批</span>
    </td>
  </tr>
</table>
<table class="TableBlock" width="95%" align="center">
    <tr>
     <td nowrap class="TableData" width=35%>标题：</td>
     <td class="TableData" title="" id="subject"></td>
    </tr>
    <tr>
     <td nowrap class="TableData">发布人：</td>
     <td class="TableData" id="fromId"><u title="部门：" style="cursor:pointer"></u></td>
    </tr>
   <tr id="rang_user">
     <td nowrap class="TableData">发布范围：</td>
     <td style="cursor:pointer" title="" class="TableData" id="toId"></td>
   </tr>
   <tr id="rang_role">
     <td nowrap class="TableData">发布时间：</td>
     <td class="TableData">
          <input type="text" id="sendTime" name="sendTime" size="20" maxlength="20" class="BigInput" value="">
          <img id="sendTimeImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
          &nbsp;&nbsp;<a href="javascript:resetTime();">设置为当前时间</a>
      </td>
  </tr>
  <tr id="url_address">
     <td nowrap class="TableData">生效日期：</td>
     <td class="TableData">
     <input type="text" id ="beginDate" name="beginDate" class="BigInput" size="10" maxlength="10"  value="">
     <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
     &nbsp;&nbsp;<a href="javascript:resetDate();">设置为当前日期</a>
     </td>
  </tr>
  <tr>
    <td nowrap class="TableData">终止日期：</td>
    <td class="TableData">
    <input type="text" id ="endDate" name="endDate" class="BigInput" size="10" maxlength="10"  value="">
    <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
           为空为手动终止
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">审批意见：<font style='color:red'>*</font></td>
    <td class="TableData"><textarea cols="50" id="reason" name="reason" rows="2" class="BigInput" wrap="no"></textarea></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="2" nowrap>
    <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.notify.data.YHNotify"/>
    <input type="hidden" id="operation" name="operation" value="0">
    <input type="hidden" id="seqId" name="seqId" value="">
    <input type="button" value="不批准" class="BigButton" onClick="sendForm();">&nbsp;
    <input type="button" value="返回" class="BigButton" onClick="javascript:window.history.go(-1);">
    </td>
  </tr>
 </table>
</form>
<script>
function bindValidDtFunc1() {
	bindAssertDateTimePrcBatch([{id:"sendTime", type:"dt"}]);
}
function bindValidDtFunc2() {
	bindAssertDateTimePrcBatch([{id:"beginDate", type:"d"}]);
}
function bindValidDtFunc3() {
	bindAssertDateTimePrcBatch([{id:"endDate", type:"d"}]);
}
bindValidDtFunc1();
bindValidDtFunc2();
bindValidDtFunc3();
</script>
</body>
</html>