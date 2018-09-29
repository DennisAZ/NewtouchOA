<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>内部邮件</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script type="text/javascript" >
function doInit(){
  showCalendar("startTime", true, "startTimeImg");
  showCalendar("endTime", true, "endTimeImg");
  bindSelfBox("showBox");
}

function whichBox(value){
  if(value == '发件箱') {
  	$("userDescTitle").innerHTML = "收件人";
  } else {
    $("userDescTitle").innerHTML = "发件人";
    //if($('form1').boxId.selectedIndex == 2){
   // }
  }
  if (value == '0') {
	  $("outUsers").show();
	  $('outUsers').focus();
	  $('outUsersR').hide();
     $('outUsersCopy').hide();
     $('outUsersBcc').hide();
  } else if (value == '发件箱')  {
	  $("outUsers").hide();
      $('outUsersR').show();
      $('outUsersCopy').show();
      $('outUsersBcc').show();
  } else {
    $("outUsers").hide();
    $('outUsersR').hide();
    $('outUsersCopy').hide();
    $('outUsersBcc').hide();
  }
}
function getQueryType(){
  var queryType = 1;
  if($('form1').boxId.value == "已删除"){
     queryType = 3;
   }else if($('form1').boxId.value == "发件箱"){
     queryType = 2;
   }else {
     queryType = 1;
    }
  return queryType;
}
/**
 * 邮件查询
 */
function doSearch(){
  var type = getQueryType();
  var name = encodeURI($('showBox').options[$('showBox').selectedIndex].innerHTML);
  mailSearch(type,"form1",name);
}

var ctrlConst = "toWebmail";
function selectAddress(ctrl){
  window.onbeforeunload = null;
  if (ctrl) {
    ctrlConst = ctrl;
  } else {
    ctrlConst = "toWebmail";
  }
  var url = contextPath + "/core/funcs/email/new/emailselect/MultiEmailSelect.jsp";
  openDialogResize(url,  710, 400);
}
</script>
</head>

<body  topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/cmp/email/infofind.gif" align="absmiddle">
    	&nbsp;&nbsp;<span class="big3">查询邮件</span><br>
    </td>
  </tr>
</table>

<br>
<form  name="form1" id="form1">
<table class="TableBlock" width="450" align="center">
  <tr class="TableData">
      <td nowrap align="center">选择邮箱：</td>
      <td nowrap>
        <select name="boxId" id="showBox" onChange="whichBox(this.value);">
          <option value="0">收件箱</option>
          <option value="发件箱">发件箱</option>
          <option value="已删除">已删除邮件箱</option>
        </select>
      </td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">邮件状态：</td>
      <td nowrap>
        <select name="mailStatus">
          <option value="">所有</option>
          <option value="0">未读</option>
          <option value="1">已读</option>
        </select>
      </td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">日期：</td>
      <td nowrap>
        <input type="text" name="startTime" id="startTime" size="20" maxlength="19" class="BigInput" value="">
        <img id="startTimeImg" align="absMiddle" src="<%=imgPath%>/cmp/email/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
        <input type="text" name="endTime" id="endTime" size="20" maxlength="19" class="BigInput" value="">
        <img id="endTimeImg"  align="absMiddle" src="<%=imgPath%>/cmp/email/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
  </tr>
  <tr class="TableData" id="TO" style="">
      <td nowrap align="center"><span id="userDescTitle">发件人</span>：</td>
      <td nowrap>
      <input type="hidden" name="userId" id="userId" value="">
      <textarea  id="userIdDesc" rows="2" cols=20  style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
      <a href="#" class="orgAdd" onClick="selectUser(['userId', 'userIdDesc']);">添加</a>
      <a href="#" class="orgClear" onClick="Clear('userId','userIdDesc')">清空</a> 
      </td>
      </tr>
  <tr class="TableData" id="outUsers">
      <td nowrap class="TableData" nowrap >外部发件人：</td>
      <td class="TableData">
        <textarea style="width:70%;height: 40px;" name="toWebmail" id="toWebmail" rows="1" style="overflow-y:auto;"  wrap="yes" ></textarea>
        <!-- <a href="#" class="orgAdd" onClick="alert('完善中')" title="添加外部收件人">添加</a> -->
        <a href="javascript:void(0)" class="orgAdd" onClick="selectAddress('toWebmail')" title="从通讯簿添加收信人">添加</a>
        <a href="#" class="orgClear" onClick="$('toWebmail').value=''" title="添加外部收件人">清空</a><br />
                 &nbsp;&nbsp;多个外部收件人请使用","分隔
      </td>
  </tr>
  <tr class="TableData" id="outUsersR" style="display:none">
      <td nowrap class="TableData" nowrap >外部发件人：</td>
      <td class="TableData">
        <textarea style="width:70%;height: 40px;" name="toWebmailR" id="toWebmailR" rows="1" style="overflow-y:auto;"  wrap="yes" ></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectAddress('toWebmailR')" title="从通讯簿添加收信人">添加</a>
        <a href="#" class="orgClear" onClick="$('toWebmailR').value=''" >清空</a><br />
                 &nbsp;&nbsp;多个外部发件人请使用","分隔
      </td>
  </tr>
  <tr class="TableData" id="outUsersCopy" style="display:none">
      <td nowrap class="TableData" nowrap >外部抄送人：</td>
      <td class="TableData">
        <textarea style="width:70%;height: 40px;" name="toWebmailCopy" id="toWebmailCopy" rows="1" style="overflow-y:auto;"  wrap="yes" ></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectAddress('toWebmailCopy')" title="从通讯簿添加收信人">添加</a>
        <a href="#" class="orgClear" onClick="$('toWebmailCopy').value=''" >清空</a><br />
                 &nbsp;&nbsp;多个外部抄送人请使用","分隔
      </td>
  </tr>
  <tr class="TableData" id="outUsersBcc" style="display:none">
      <td nowrap class="TableData" nowrap >外部密送人：</td>
      <td class="TableData">
        <textarea style="width:70%;height: 40px;" name="toWebmailBcc" id="toWebmailBcc" rows="1" style="overflow-y:auto;"  wrap="yes" ></textarea>
        <a href="javascript:void(0)" class="orgAdd" onClick="selectAddress('toWebmailBcc')" title="从通讯簿添加收信人">添加</a>
        <a href="#" class="orgClear" onClick="$('toWebmailBcc').value=''" >清空</a><br />
                 &nbsp;&nbsp;多个外部密送人请使用","分隔
      </td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">邮件主题包含文字：</td>
      <td nowrap><input type="text" name="subject" class="BigInput" size="20"></td>
  </tr>
  <tr>
    <td nowrap class="TableData" align="center">邮件内容[关键词1]：</td>
    <td class="TableData"><input type="text" name="key1" class="BigInput" size="20"></td>
  </tr>
  <tr>
    <td nowrap class="TableData" align="center">邮件内容[关键词2]：</td>
    <td class="TableData"><input type="text" name="key2" class="BigInput" size="20"></td>
  </tr>
  <tr>
    <td nowrap class="TableData" align="center">邮件内容[关键词3]：</td>
    <td class="TableData"><input type="text" name="key3" class="BigInput" size="20"></td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">附件文件名包含文字：</td>
      <td nowrap><input type="text" name="attachmentName" class="BigInput" size="20"></td>
  </tr>
  <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="hidden" name="boxId2" value="">
          <input type="button" value="查询" class="BigButton" title="进行文件查询" onclick="doSearch()">&nbsp;&nbsp;
      </td>
  </tr>
</table>
</form>
</body>
</html>