<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>关怀提醒</title>
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffCare/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffCare/js/staffCareLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
	getSysRemind("smsRemindDiv","smsRemind",57);
	moblieSmsRemind("sms2RemindDiv","sms2Remind",57);
	var  date = new Date();
	$('datetime1').innerHTML = date.getYear()+'年'+(date.getMonth()+1)+'月';
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct/getStaffInfoeListJson.act";
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    sortIndex: 1,
    sortDirect: "desc",
    colums: [
       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRenderCare},
       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
       {type:"data", name:"userId",  width: '15%', text:"员工姓名" ,align: 'center' },
       {type:"data", name:"sex",  width: '15%', text:"性别" ,align: 'center' ,render:sexFunc},
       {type:"data", name:"staffBirth",  width: '15%', text:"生日" ,align: 'center' ,render:splitDateFunc},
       {type:"data", name:"mobilNo",  width: '20%', text:"手机" ,align: 'center' },
       {type:"data", name:"staffEmail",  width: '20%', text:"电子邮件" ,align: 'center' }]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 " + total + " 条记录 ！";
    showCntrl('delOpt');
  }else{
    WarningMsrg('本月没有员工过生日', 'msrg');
  }
}

//判断是否要显示短信提醒 
function getSysRemind(remidDiv,remind,type){ 
var requestUrl = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=" + type; 
var rtJson = getJsonRs(requestUrl); 
if(rtJson.rtState == "1"){ 
  alert(rtJson.rtMsrg); 
  return ; 
} 
var prc = rtJson.rtData; 
//alert(rsText);
var allowRemind = prc.allowRemind;;//是否允许显示 
var defaultRemind = prc.defaultRemind;//是否默认选中 
var mobileRemind = prc.mobileRemind;//手机默认选中 
if(allowRemind=='2'){ 
  $(remidDiv).style.display = 'none'; 
}else{
  $(remidDiv).style.display = ''; 
  if(defaultRemind=='1'){ 
    $(remind).checked = true; 
  } 
}
if(document.getElementById(remind).checked){
  document.getElementById(remind).value = "1";
}else{
  document.getElementById(remind).value = "0";
}
}
//设置提醒值

function checkBox(ramCheck){
  if(document.getElementById(ramCheck).checked){
    document.getElementById(ramCheck).value = "1";
  }else{
    document.getElementById(ramCheck).value = "0";
  }
}
//判断是否要显示手机短信提醒 
function moblieSmsRemind(remidDiv,remind,type){
  var requestUrl = contextPath + "/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=" + type; 
  var rtJson = getJsonRs(requestUrl); 
  if(rtJson.rtState == "1"){ 
    alert(rtJson.rtMsrg); 
    return ; 
  } 
  var prc = rtJson.rtData; 
  //alert(rsText);
  var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
  if(moblieRemindFlag == '2'){ 
    $(remidDiv).style.display = '';
    $(remind).checked = true;
  }else if(moblieRemindFlag == '1'){ 
    $(remidDiv).style.display = '';
    $(remind).checked = false;
  }else{
    $(remidDiv).style.display = 'none'; 
  }
  if(document.getElementById(remind).checked){
    document.getElementById(remind).value = "1";
  }else{
    document.getElementById(remind).value = "0";
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/care.gif" align="absMiddle" width="17"><span class="big3">&nbsp;本月生日员工(<span id="datetime1"></span>)  </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <input type="button" value="发送短息" onclick="sendInfo();"></input>&nbsp;
      </td>
 </tr>
</table>
</div><p><br>
<div id="msrg">
</div>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/notify.gif" align="absMiddle" width="17"><span class="big3">&nbsp;员工关怀提醒  </span>
   </td>
 </tr>
</table>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct/staffCareReminder.act" method="post" name="form1" id="form1" >
  <table class="TableBlock" width="50%" align="center">
    <tr>
      <td nowrap class="TableData">提醒对象：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="reminder" id="reminder" value="">
        <textarea cols="40" name="reminderDesc" id="reminderDesc" rows="3" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['reminder', 'reminderDesc'],null,null,1);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('reminder').value='';$('reminderDesc').value='';">清空</a>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData" colspan=3>
        <span id="smsRemindDiv" style="display: none"><input type="checkbox" name="smsRemind" id="smsRemind" value="" onclick="checkBox('smsRemind')"><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>
        <span id="sms2RemindDiv" style="display: none"><input type="checkbox" name="sms2Remind" id="sms2Remind" value="" onclick="checkBox('sms2Remind')"><label for="sms2Remind">使用手机短信提醒 </label>&nbsp;&nbsp;</span>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 提醒内容：</td>
      <td class="TableData" colspan=3>
        <textarea cols="40" name="content" id="content" rows="3" style="overflow-y:auto;" class="BigInput" wrap="yes" ></textarea>
      </td>
    </tr>
    <tr>
      <td align="center" class="TableData" colspan=4 >
        <input type="submit" value="保存" >
      </td>
    </tr>
  </table>
</form>
</body>
</html>