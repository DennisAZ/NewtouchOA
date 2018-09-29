<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
<head>
<title>投票管理</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/system/interface/js/interfaceLogic.js"></script>
<script type="text/Javascript">
var seqId = "<%=seqId%>";
var beginEnd = "<%=sf.format(new Date())%>";
function doForm() {
  var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/showDetail.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  //alert(rsText);
  if (prc.seqId) {
    $("toId").value= prc.toId;
    $("privId").value= prc.privId;
    $("userId").value= prc.userId;
    if ($("toId").value != "" && $("toId").value != "0" && $("toId").value != "ALL_DEPT") {
      bindDesc([{cntrlId:"toId",dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
    }
    if ($("toId").value == "0" || $("toId").value == "ALL_DEPT") {
      $("toIdDesc").value = "全体部门";
    }
    if ($("privId").value != "") {
      bindDesc([{cntrlId:"privId",dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
    }
    if ($("userId").value != "") {
      bindDesc([{cntrlId:"userId",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    bannerFone(prc.subjectFont);
    $("beginDate").value= prc.beginDate.substr(0,10);
    $("endDate").value= prc.endDate.substr(0,10);
    $("subject").value= prc.subject;
    $("subjectMain").value= prc.subjectMain;
    $("seqId").value = prc.seqId;
    $("fromId").value = prc.fromId;
    $("sendTime").value = prc.sendTime;
    $("publish").value = prc.publish;
    $("voteNo").value = prc.voteNo;
    $("parentId").value = prc.parentId;
    $("peaders").value = prc.peaders;
    $("content").value = prc.content;
    $("type").value = prc.type;
    if ($("type").value == "1") {
      $("maxNumDesc").style.display = "";
    }
    $("maxNum").value = prc.maxNum;
    $("minNum").value = prc.minNum;
    $("viewPriv").value = prc.viewPriv;
    $("anonymity").value = prc.anonymity;
    $("top").value = prc.top;
    if ($("top").value == "1") {
      $("topCheck").checked = true;
    }
    if ($("anonymity").value == "1") {
      $("anonymityCheck").checked = true;
    }
    $("attachmentName").value = prc.attachmentName;
    $("attachmentId").value = prc.attachmentId;
    attachMenuUtil("showAtt","vote",null,$('attachmentName').value ,$('attachmentId').value,false);
    if ($("publish").value == "0") {
      $("fabu").update("<input type='button' value='发布' class='BigButton' onClick='javascript:checkForm3();'>");
    }
  }
}

function doInit() {
  doForm();
  //时间
  var parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  moblieSmsRemind('sms2Remind3','sms2Check');
  getSysRemind();
}
//是否允许匿名
function onAnonymity() {
  if ($("anonymityCheck").checked) {
    $("anonymity").value = "1";
  } else {
    $("anonymity").value = "0";
  }
}
//置顶
function onTop() {
  if ($("topCheck").checked) {
    $("top").value = "1";
  } else {
    $("top").value = "0";
  }
}
//表单验证
function checkForm() {
  if ($("subjectMain").value == "") {
    alert("总投票的标题不能为空！");
    $("subjectMain").focus();
    $("subjectMain").select();
    return false;
  }
  if ($("subject").value == "") {
    alert("投票的标题不能为空！");
    $("subject").focus();
    $("subject").select();
    return false;
  }
  if ($("toIdDesc").value == ""  &&  $("privIdDesc").value == "" && $("userIdDesc").value == "") {
    alert("请至少指定一种发布范围！");
    $("toIdDesc").focus();
    $("toIdDesc").select();
    return false;
  }
  if ($("maxNum").value != "0" && !isNumber($("maxNum").value)) {
    alert("最多选择项只能为数字！");
    $("maxNum").focus();
    $("maxNum").select();
    return false;
  }
  if ($("minNum").value != "0" && !isNumber($("minNum").value)) {
    alert("最少选择项只能为数字！");
    $("minNum").focus();
    $("minNum").select();
    return false;
  }
  if ($("maxNum").value != "0" && $("minNum").value != "0" && ($("minNum").value > $("maxNum").value)) {
    alert("最少项不能大于最多项！");
    $("minNum").focus();
    $("minNum").select();
    return false;
  }
  if ($("beginDate").value != "" && $("endDate").value != "" && ($("beginDate").value >= $("endDate").value)) {
    alert("起始时间不能大于等于终止时间！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if ($("endDate").value != "" && ($("endDate").value <= beginEnd)) {
    alert("终止时间不能小于等于当天时间！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  return true;
}
//类型多选
function changeType() {
  if ($("type").value == "1") {
    $("maxNumDesc").style.display = "";
  }else {
    $("maxNumDesc").style.display = "none";
  }
}

//附件上传
function upload_attach(){
  if(checkForm()){
    $("btnFormFile").click();
  }  
}
function handleSingleUpload(rtState,rtMsrg,rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  attachMenuUtil("showAtt","vote",null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if (isUploadBackFun) {
    checkForm2();
    isUploadBackFun = false;
   }
}
//有附件，也执行上传附件

function jugeFile(){
  var formDom  = document.getElementById("formFile");
  var inputDoms  = formDom.getElementsByTagName("input"); 
  for(var i=0; i<inputDoms.length; i++){
    var idval = inputDoms[i].id;
    if(idval.indexOf("ATTACHMENT")!=-1){
      return true;
    }
  } 
  return false; 
}
//上传后可以显示浮动菜单
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var attachNameOld = $('attachmentName').value; 
  var attachIdOld = $('attachmentId').value; 
  var attachNameArrays = attachNameOld.split("*"); 
  var attachIdArrays = attachIdOld.split(","); 
  var attaName = ""; 
  var attaId = ""; 
  for (var i = 0 ; i < attachNameArrays.length ; i++) {
    if (!attachIdArrays[i] || attachIdArrays[i] == attachId) { 
        continue; 
    }
    attaName += attachNameArrays[i] + "*"; 
    attaId += attachIdArrays[i] + ","; 
  }
  $('attachmentId').value = attaId; 
  $('attachmentName').value = attaName;
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/updateVote.act";
    var json = getJsonRs(url,pars);
    if(json.rtState == "1") {
      alert(json.rtMsrg);
      return false;
    }
    return true;
  }
}
//修改数据
function checkForm2() {
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return ;
    }
    
    var actionSize = $('actionSize').value;
    var actionLight = $('actionLight').value;
    var actionFont = $('actionFont').value;
    var actionLights = $('actionLights').value;
    var actionColor = $('actionColor').value;
    var actionFlag = $('actionLightFlag').value;
    
    var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
    $("subjectFont").value = subjectFont;
    
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/updateVote.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      if ($("publish").value != "1") {
        parent.seqId = $("seqId").value;
        window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/add.jsp";
      }
      if ($("publish").value == "1") {
        parent.seqId = $("seqId").value;
        window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/index1.jsp";
      }
    }
  }
}

//发布
function checkForm3() {
  var seqId = $("seqId").value;
  if (checkForm()) {
    var numSeqId = checkForm4(seqId);
    var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectId.act?seqId=" + seqId; //查询投票项
    var json = getJsonRs(url);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } 
    var prc = json.rtData;
    if (($("parentId").value != "" && $("parentId").value != "0") || prc.length > 0 || numSeqId > 0) {
      $("publish").value = "1";
      checkForm2();
    } else {
      alert("投票项目为空且无子投票时，无法发布！请先添加?");
      parent.seqId = seqId;
      window.location.href = "<%=contextPath%>/subsys/oa/vote/manage/add.jsp";
    }
  }
}
//查询子投票项
function checkForm4(seqId) {
  var url = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectId2.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  }
  var prc = json.rtData;
  return prc.length;
}

/** 
 *js代码 
 *是否显示手机短信提醒 
 */
function moblieSmsRemind(remidDiv,remind) {
  var requestUrl = "<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSelectAct/isShowSmsRmind.act?type=11";
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1"){ 
     alert(rtJson.rtMsrg); 
     return ; 
  }
   var prc = rtJson.rtData;
   var moblieRemindFlag = prc.moblieRemindFlag;//手机默认选中 
   if (moblieRemindFlag == '2') {
     $(remidDiv).style.display = ''; 
     $(remind).checked = true;
     document.getElementById("smsSJ").value = "1";
   } else if(moblieRemindFlag == '1') { 
     $(remidDiv).style.display = ''; 
     $(remind).checked = false; 
   } else {
     $(remidDiv).style.display = 'none'; 
   }
}
 
//判断是否要显示短信提醒 
function getSysRemind(){ 
  var requestUrl = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/getSysParaRemind.act?type=11"; 
  var rtJson = getJsonRs(requestUrl); 
  if (rtJson.rtState == "1") { 
    alert(rtJson.rtMsrg); 
    return ; 
  }
  var prc = rtJson.rtData; 
  var allowRemind = prc.allowRemind; 
  var defaultRemind = prc.defaultRemind; 
  var mobileRemind = prc.mobileRemind;
  if (allowRemind == '2') {
    $("smsRemindDiv").style.display = 'none';
  }else{ 
    if (defaultRemind == '1') { 
      $("smsflag2").checked = true;
      document.getElementById("smsflag").value = "1";
    }
  }
}

//选择发送消息
function checkBox2() {
  if (document.getElementById("smsflag2").checked) {
     document.getElementById("smsflag").value = "1";
  }else {
   document.getElementById("smsflag").value = "0";
  }
}
function checkBox3() {
  if (document.getElementById("sms2Check").checked) {
     document.getElementById("smsSJ").value = "1";
  }else {
   document.getElementById("smsSJ").value = "0";
  }
}

function showSubject(){
  var actionSize = $('actionSize').value;
  var actionLight = $('actionLight').value;
  var actionFont = $('actionFont').value;
  var actionLights = $('actionLights').value;
  var actionColor = $('actionColor').value;
  var actionFlag = $('actionLightFlag').value;
  
  var subjectFont = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
  $('subjectMainShow').innerHTML = $('subjectMain').value;
  $('subjectMainShow').setStyle(subjectFont);
  
  $('subject_tr').style.display = "";
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<span class="big3">&nbsp;<img src="<%=imgPath %>/vote.gif" align="absmiddle">&nbsp;修改投票</span> 
<br>
<form method="post" name="form1" id="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.vote.data.YHVoteTitle">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" name="fromId" id="fromId" value="">
<input type="hidden" name="sendTime" id="sendTime" value="">
<input type="hidden" name="publish" id="publish" value="">
<input type="hidden" name="voteNo" id="voteNo" value="">
<input type="hidden" name="parentId" id="parentId" value="">
<input type="hidden" name="peaders" id="peaders" value="">
<table class="TableBlock" width="70%" align="center">
<tr>
   <td nowrap class="TableData"> 总标题：<font color="red">*</font></td>
   <td class="TableData">
     <input type="text" name="subjectMain" id="subjectMain" size="42" maxlength="100" class="BigInput" value="">
   </td>
</tr>
<tr id="subject_tr" style="display: none;height:50px;">
   <td nowrap class="TableData" >预览</td>
   <td class="TableData">
     <span id="subjectMainShow"></span>
   </td>
</tr>
<tr>
  <td nowrap class="TableData">总标题样式：</td>
  <td nowrap class="TableData">
      <input type="hidden" name="FONT_FAMILY" value="">
      <input type="hidden" name="FONT_SIZE" value="">
      <input type="hidden" name="FONT_COLOR" value="">
      <input type="hidden" name="FONT_FILTER" value="">
  <span style="padding-top:5px;padding-left:10px;">
   <a id="showMeunA" href="#" onclick="showFont(event);"><span id="actionNameFont">字体</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;">
   <a id="showMeunB" href="#" onclick="showSize(event);"><span id="actionNameSize">大小</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;">
   <a id="showMeunC" href="#" onclick="showColor(event);"><span id="actionNameColor">颜色</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;display:none;">
   <a id="showMeunD" href="#" onclick="showLight(event);"><span id="actionNameLight">效果</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
  </span>
  <span style="padding-top:5px;padding-left:10px;"><a href="#" onclick="showSubject();">预览</a></span>
  <input type="hidden" name="actionFont" id="actionFont" value="">
  <input type="hidden" name="actionSize" id="actionSize" value="">
  <input type="hidden" name="actionLight" id="actionLight" value="">
  <input type="hidden" name="actionLights" id="actionLights" value="">
  <input type="hidden" name="actionColor" id="actionColor" value="">
  <input type="hidden" name="actionLightFlag" id="actionLightFlag" value="">
  <input type="hidden" name="subjectFont" id="subjectFont" value="">
  </td>
</tr>
<tr>
   <td nowrap class="TableData"> 标题：<font color="red">*</font></td>
   <td class="TableData">
     <input type="text" name="subject" id="subject" size="42" maxlength="100" class="BigInput" value="">
     <span style="color: #151515">&nbsp;&nbsp;&nbsp;&nbsp;<b>此处为投票第一项标题</b></span>
   </td>
</tr>
    <tr>
      <td nowrap class="TableData">发布范围（部门）：</td>
      <td class="TableData">
        <input type="hidden" name="toId" id="toId" value="">
        <textarea cols=38 name="toIdDesc" id="toIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['toId','toIdDesc'], 7);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('toId').value='';$('toIdDesc').value='';">清空</a>
        </td>
    </tr>
    <tr>
      <td nowrap class="TableData">发布范围（角色）：</td>
      <td class="TableData">
        <input type="hidden" name="privId" id="privId" value="">
        <textarea cols=38 name="privIdDesc" id="privIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectRole(['privId','privIdDesc'], 7);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('privId').value='';$('privIdDesc').value='';">清空</a>
       </td>
   </tr>
   <tr>
      <td nowrap class="TableData">发布范围（人员）：</td>
      <td class="TableData">
        <input type="hidden" name="userId" id="userId" value="">
        <textarea cols=38 name="userIdDesc" id="userIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId', 'userIdDesc'] , 7)">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userIdDesc').value='';">清空</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">投票描述：</td>
      <td class="TableData">
        <textarea name="content" id="content" class="BigInput" cols="50" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 类型：</td>
      <td class="TableData">
        <select name="type" id="type" onChange="changeType()">
          <option value="0" >单选</option>
          <option value="1" >多选</option>
          <option value="2" >文本输入</option>
        </select>
        <span id="maxNumDesc" style="display:none">最多允许选择 
        <input type="text" name="maxNum" id="maxNum" value="0" size="2" maxlength="2" class="SmallInput"> 项，&nbsp;最少允许选择 
        <input type="text" name="minNum" id="minNum" value="0" size="2" maxlength="2" class="SmallInput"> 项，0则不限制</span>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 查看投票结果：</td>
      <td class="TableData">
        <select name="viewPriv" id="viewPriv">
          <option value="0" >投票后允许查看</option>
          <option value="1" >投票前允许查看</option>
          <option value="2" >不允许查看</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 匿名投票：</td>
      <td class="TableData">
        <input type="checkbox" name="anonymityCheck" id="anonymityCheck" onClick="onAnonymity();"><label>允许匿名投票</label>
        <input type="hidden" name="anonymity" id="anonymity" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 有效期：</td>
      <td class="TableData">
        生效日期：<input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput" value="" readonly>
         <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">        
        为空为立即生效<br>
        终止日期：<input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" readonly>
<img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">    
        为空为手动终止
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 置顶：</td>
      <td class="TableData"><input type="checkbox" name="topCheck" id="topCheck" onClick="onTop();"><label>使投票置顶，显示为重要</label>
<input type="hidden" name="top" id="top" value="">   
      </td>
    </tr>
    <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData">
      <input type="hidden" id="attachmentName" name="attachmentName" value=""></input>
       <input type="hidden" id="attachmentId" name="attachmentId" value=""></input>
        <span id="showAtt">无附件</span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData">
        <input type="hidden" id="moduel" name="moduel" value="vote">
       <script>ShowAddFile();</script>
     <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script> 
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 短消息提醒：</td>
      <td class="TableData">
      <span id="smsRemindDiv">&nbsp;
      <input type="checkbox" name="smsflag2" id="smsflag2" onClick="checkBox2();">
      <label>使用内部短信提醒</label>&nbsp;&nbsp;</span>
      <span id="sms2Remind3">&nbsp;&nbsp;<input type="checkbox" name="sms2Check" id="sms2Check" onClick="checkBox3();">
      <label>使用手机短信提醒</label>&nbsp;&nbsp;</span>
      <input type="hidden" name="smsSJ" id="smsSJ" value="0">
      <input type="hidden" name="smsflag" id="smsflag" value="0">
     </td>
    </tr>
        <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      <div>
      <span id="fabu"></span>&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="保存" class="BigButton" onClick="checkForm2();">&nbsp;&nbsp;
        &nbsp;&nbsp;<input type="button" value="返回" class="BigButton" onClick="javascript:history.back()">
        </div>
      </td>
    </tr>
  </table>
</form>
<form id="formFile" action="<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
</body>
</html>