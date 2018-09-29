<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <% 
 String runId = request.getParameter("runId");
 String flowId = request.getParameter("flowId");
 String prcsId = request.getParameter("prcsId");
 boolean manage = false;
 String sManage = request.getParameter("isManage");
 if (sManage != null) {
   manage = Boolean.valueOf(sManage);
 }
 String sortId = request.getParameter("sortId");
 if (sortId == null) {
   sortId = "";
 }
 String skin = request.getParameter("skin");
 if (skin == null) {
   skin = "";
 }
%>
<html>
<head>
<title>委托工作</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var isManage = <%=manage%>;

var freeOther = 2;//委托参数，默认为自由委托。
//var prcsMsg = {runName:'dd'
  //  ,flowName:'ddd' ,userOldId:11, opFlag:1, freeOther:2 ,smsRemind:true,prcsList:[{id : 1, prcsName : 'ssss' , userName:'ssss,ssss,sss,'},{id : 2, prcsName : 'ssss' , userName:'ssss,ssss,sss,'},{id : 3, prcsName : 'ssss' , userName:'ssss,ssss,sss,'}]};
var rootUrl = contextPath + "/yh/core/funcs/workflow/act/YHDelegateAct";
function doInit() {
  var url = rootUrl + "/getDelegateMsg.act";
  var par = "type=1&runId=" + runId + "&flowId=" + flowId + "&prcsId=" + prcsId  + "&isManage=" + isManage;
  var json = getJsonRs(url , par);
  if (json.rtState != "0") {
	alert(json.rtMsrg);
	return ;
  }
  var prcsMsg = json.rtData;
  $('flowNameB').update(prcsMsg.flowName);
  freeOther = prcsMsg.freeOther;
  if (prcsMsg.smsRemind) {
    $('smsRemind').checked = true; 
  }
  if (prcsMsg.sms2Priv) {
    $('sms2RemindSpan').show();
    if (prcsMsg.sms2Remind) {
      $('sms2Remind').checked = true;
    }
  }
  $('smsContent').value = "工作流委托提醒：" + prcsMsg.runName;
  $("opFlag").value =  prcsMsg.opFlag;
  $('userOldId').value = prcsMsg.userOldId;
  $('topFlag').value = prcsMsg.topFlag;
}
function delegate(){
  if (!$('user').value) {
    alert('请指定委托人！');
    return ;
  }
  var url = rootUrl + "/freeDelegate.act?sortId=<%=sortId%>&skin=<%=skin%>";
  var json = getJsonRs(url ,$('otherForm').serialize());
  if (json.rtState == '0') {
	alert(json.rtMsrg);
	var ParentWindow = parent.opener.window;
    ParentWindow.location.reload();
    window.close();
  }
}

var is_ie = document.all ? true : false;
function LoadDialogWindow(URL, parent, loc_x, loc_y, width, height)
{
  if(is_ie){
    window.showModalDialog(URL,parent,"edge:raised;scroll:1;status:0;help:0;resizable:1;dialogWidth:"+width+"px;dialogHeight:"+height+"px;dialogTop:350px;dialogLeft:480px",true);
  } else {
    window.open(URL,parent,"height="+height+",width="+width+",status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes,modal=yes,dependent=yes,dialog=yes,minimizable=no",true);
  }
}
function LoadWindow(PRCS_ID_I){
  //自由委托
  if (freeOther == 2) {
    selectSingleUser();
  //仅允许委托当前步骤经办人
  } else if (freeOther == 1) {
    var URL = contextPath + "/core/funcs/workflow/flowrun/list/others/userSelectPrcs.jsp?flowId=<%=flowId%>&runId=<%=runId%>&prcsId=" + PRCS_ID_I;
    var w = 250;
    var h = 300;
    var loc_y = loc_x = 200;
    if (is_ie) {
      loc_x=document.body.scrollLeft + event.clientX - event.offsetX;
      loc_y=document.body.scrollTop + event.clientY - event.offsetY + 210;
    }
    LoadDialogWindow(URL,self,loc_x, loc_y, w, h);
  } 
}

</script>
</head>
<body topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><span class="big3" id="runMsgSpan"> 办理中委托工作</span><br>
    </td>
  </tr>
</table>

<form  method="post" name="otherForm" id=otherForm>
<table class="TableBlock" width="90%" align="center">
	<tr class="TableHeader">
      <td nowrap align="center" class="Big" colspan="3"><b id="flowNameB">flowName</b></td>
    </tr>
    <tr class="TableData">
      <td nowrap width="10%">第<%=prcsId %>步骤：(当前步骤)</td>
      <td nowrap>
      	 <span style="color:red;font-weight:bold">将工作委托给：</span>
         <input type="text" name="userDesc" id="userDesc" size="10" class="BigStatic" readonly>&nbsp;
         <a href="javascript:;" class="orgAdd" onClick="LoadWindow(<%=prcsId %>)" title="指定委托对象">选择</a>
         <input type="hidden" name="user" id="user" value="">
      </td>
    </tr>
    <tr class="TableHeader">
      <td colspan="3" height=25px>
      
      <img src="<%=imgPath %>/sms.gif" aling="absmiddle"><b>短信提醒</b></td>
    </tr>
    
     <tr class="TableData">
      <td colspan="3">
       <span id="sms2RemindSpan" style="display:none">
      <img src="<%=imgPath %>/mobile_sms.gif">
      <input type="checkbox" name="sms2Remind" id="sms2Remind">
      <label for="sms2Remind">使用手机短信提醒被委托人</label></span>
        <img src="<%=imgPath %>/sms.gif"><input type="checkbox" name="smsRemind" id="smsRemind" ><label for="smsRemind">使用内部短信提醒被委托人</label>
        
<br>
短信内容：<input type="text" id="smsContent" name="smsContent" value="" size="62" maxlength="100" class="SmallInput">
     </td>
    </tr>

    <tr class="TableControl">
      <td nowrap align="center" colspan="3">
      	<input type="hidden"  name="runId" id="runId" value="<%=runId %>">
      	<input type="hidden"  name="opFlag" id="opFlag" value="">
      	<input type="hidden"  name="topFlag" id="topFlag" value="">
      	<input type="hidden" name="userOldId" id="userOldId" value="">
		<input type="hidden" name="flowId" id="flowId" value="<%=flowId %>">
        <input type="hidden"  name="prcsId" id="prcsId" value="<%=prcsId %>">
        <input type="button"  id="submitButton" onclick="delegate()" value="确定" class="BigButton" name="submitButton">&nbsp;&nbsp;
		<input type="button"  value="关闭" class="BigButton" name="back" onClick="window.close()">
      </td>
    </tr>
</table>
</form>
</body>
</html>