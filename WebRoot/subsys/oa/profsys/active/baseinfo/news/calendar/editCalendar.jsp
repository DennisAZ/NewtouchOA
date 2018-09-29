<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%
  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  String seqId  = YHUtility.isNullorEmpty(request.getParameter("seqId")) ? "" : request.getParameter("seqId");
%>
<html>
<head>
<title>编辑日程</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/style.css">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
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
<script type="text/Javascript" src="<%=contextPath%>/subsys/oa/profsys/js/profsys.js" ></script>
<script type="text/javascript">
var upload_limit=1,limit_type="php,php3,php4,php5,";
//初始化加载

function doOnload() {
  var seqId = '<%=seqId%>';
  var projId = parent.projId;
  if(projId && projId!=''){
    $("projId").value = projId;
  }
  getActiveType($('activeType'));

  getCalendarById(seqId);
  //时间
  var parameters = {
      inputId:'startTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'endTime',
      property:{isHaveTime:true}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
}
function getCalendarById(seqId){
  var URL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/selectCalendarById.act?seqId="+seqId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  if(prc.seqId){
    var seqId = prc.seqId;
    var userId = prc.userId;
    var content = prc.activeContent;
    var startTime = prc.startTime;
    var endTime = prc.endTime;
    var overStatus = prc.overStatus;  
    var status = prc.status;
    $("seqId").value = seqId;
    $("startTime").value = startTime;
    $("endTime").value = endTime;
    $("activeType").value = prc.activeType;
    $("activeLeader").value = prc.activeLeader;
    $("activeLeaderDesc").value = prc.activeLeaderName;
    $("activeContent").value = prc.activeContent;
    $("activePartner").value = prc.activePartner;
    $("attachmentId").value = prc.attachmentId;
    $("attachmentName").value = prc.attachmentName;
    $("projId").value = prc.projId;
    $("overStatus").value = prc.overStatus;
    doOnloadFile();//附件
  }  
}
//表单验证
function checkForm() {
   if ($("startTime").value == "") {
     alert("开始时间不能为空!");
     $("startTime").focus();
     $("startTime").select();
     return false;
   }
   if ($("endTime").value == "") {
     alert("结束时间不能为空!");
     $("endTime").focus();
     $("endTime").select();
     return false;
   }
   if(!checkDate($("startTime"),$("endTime"))){
     return false;
   }
   if($("startTime").value > $("endTime").value){
     alert("开始时间不能小于结束时间!");
     $("startTime").focus();
     $("startTime").select();
     return false;
   }
   if ($("activeContent").value == "") {
     alert("活动内容不能为空!");
     $("activeContent").focus();
     $("activeContent").select();
     return false;
   }

   return true;
}
function checkDate(date1,date2){
   var date1Array  = date1.value.trim().split(" ");
   var date2Array  = date2.value.trim().split(" ");
   var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
   var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
   var re1 = new RegExp(type1); 
   var re2 = new RegExp(type2);
   if(date1Array.length!=2){
     alert("开始时间格式不对，应形如 1999-01-01 12:12:12");
     date1.focus();
     date1.select();
     return false;
   }else{
     if(!isValidDateStr(date1Array[0])||date1Array[1].match(re1) == null || date1Array[1].match(re2) != null){
       alert("开始时间格式不对，应形如 1999-01-01 12:12:12");
       date1.focus();
       date1.select();
       return false;
     }
   }
   if(date2Array.length!=2){
     alert("结束时间格式不对，应形如 1999-01-01 12:12:12");
     date2.focus();
     date2.select();
     return false;
   }else{
     if(!isValidDateStr(date2Array[0])||date2Array[1].match(re1) == null || date2Array[1].match(re2) != null){
       alert("结束时间格式不对，应形如 1999-01-01 12:12:12");
       date2.focus();
       date2.select();
       return false;
     }
   }
   return true;
 }
var isUploadBackFun = false;
//提交申请
function doInit() {
  if (checkForm()) {
    if (jugeFile()) {//如果有没有上传的文件，则进行上传
      $("formFile").submit();
      isUploadBackFun = true;
      return;
    }
    var pars = $('form1').serialize() ;
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/updateProjectCalendar.act";
    var json = getJsonRs(requestURL,pars);
    if (json.rtState == '1') { 
      alert(json.rtMsrg); 
      return ; 
    } else {
      alert("保存成功!");
      var prc = json.rtData;
      //parent.seqId = prc.seqId;
      window.location.reload();
    }
  }
}
var  selfdefMenu = {
  	office:["downFile","dump","read","deleteFile"], 
    img:["downFile","dump","play","deleteFile"],  
    music:["downFile","dump","play","deleteFile"],  
    video:["downFile","dump","play","deleteFile"], 
    others:["downFile","dump","deleteFile"]
	}
function doOnloadFile(){
  var attr = $("showAtt");
  var seqId  = $("seqId").value;
  attachMenuSelfUtil(attr,"profsys",$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu);
}
//附件上传
function upload_attach(){
  if(true){
    $("btnFormFile").click();
  }  
}

function handleSingleUpload(rtState,rtMsrg,rtData) {
  var data = rtData.evalJSON(); 
  $('attachmentId').value += "," + data.attrId;
  $('attachmentName').value += "*" + data.attrName;
  attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,false);
  removeAllFile();
  if(isUploadBackFun){
    doInit();
  }
  return true;
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
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/deleteCalenderFile.act?attachId="+attachId +"&attachName="+encodeURIComponent(attachName)+"&seqId="+attrchIndex;
  var json = getJsonRs(url);
  if(json.rtState == "1") {
    alert(json.rtMsrg);
    return false;
  }
  return true;
}

</script>
</head>
<body onLoad="doOnload();" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/calendar.gif" align="absMiddle" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 编辑日程</span>
    </td>
  </tr>
</table>
<form action="#"  method="post" name="form1" id="form1" onSubmit="return CheckForm();">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.profsys.data.YHProjectCalendar">
<input type="hidden" name="deptId" id="deptId" value="<%=user.getDeptId()%>">
<input type="hidden" name="userId" id="userId" value="<%=user.getSeqId()%>">
<input type="hidden" name="projCalendarType" id="projCalendarType" value="0">
<input type="hidden" name="projId" id="projId" value="">
<input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
<input type="hidden" name="overStatus" id="overStatus" value="2">
 <table class="TableBlock" width="450" align="center">
     <tr>
      <td nowrap class="TableContent"> 起始时间：<font color="red">*</font></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" id="startTime" name="startTime" value="2010-08-11 00:00:00" size="19" maxlength="19">
        <img id="date1" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" align="absMiddle">
    </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 结束时间：<font color="red">*</font></td>
      <td nowrap class="TableData">
        <input type="text" class="BigInput" id="endTime" name="endTime" value="2010-08-11 23:59:00" size="19" maxlength="19">
        <img id="date2" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer"  align="absMiddle">
   </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"> 活动类别：</td>
      <td nowrap class="TableData">
        <select name="activeType" id="activeType" class="SmallSelect">
        	<option value="">请选择类别</option>
        </select>
      </td>
    </tr>
    <tr>
    <td nowrap class="TableContent" width="90">活动负责人：</td>
  	  <td nowrap class="TableData">
  	  <input type="hidden" name="activeLeader" id="activeLeader" value="" size="10" class="BigInput" readonly>
  	    	  <input type="text" name="activeLeaderDesc" id="activeLeaderDesc" value="" size="10" class="BigInput" readonly>
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['activeLeader','activeLeaderDesc']);">选择</a>
      <a href="javascript:;" class="orgClear" onClick="$('activeLeader').value='';$('activeLeaderDesc').value='';">清空 </a>  </td> 
	  </tr>  	
    <tr>
      <td nowrap class="TableContent" width="90">活动参与人：</td>
  	  <td nowrap class="TableData">
        <textarea name="activePartner" id="activePartner" cols="45" rows="2" class="BigInput"></textarea>
      </td>  	  	
  	</tr>
    <tr>
      <td nowrap class="TableContent"> 活动内容：<font color="red">*</font></td>
      <td nowrap class="TableData">
        <textarea id="activeContent" name="activeContent" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>

      <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type="hidden" id="attachmentName" name="attachmentName"></input>
       <input type="hidden" id="attachmentId" name="attachmentId"></input>
        <span id="showAtt"></span>
      </td>
    </tr>
    <tr height="25">
      <td nowrap class="TableData">附件选择：</td>
      <td class="TableData" colspan="3">
        <input type="hidden" id="moduel" name="moduel" value="profsys">
       <script>ShowAddFile();ShowAddImage();</script>
     <script>$("ATTACHMENT_upload_div").innerHTML='<a href="javascript:upload_attach();">上传附件</a>'</script> 
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <div id="but"></div>
      </td>
  </tr>
    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="OP" value="">
        <input type="button" value="保存" class="BigButton" onclick="doInit();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="history.go(-1);">
      </td>
    </tr>

 </table>
</form>
<form id="formFile" action="<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectCalendarAct/uploadFile.act" method="post" enctype="multipart/form-data" target="commintFrame">
  <input id="btnFormFile" name="btnFormFile" type="submit" style="display:none;"></input>
  </form>
  <iframe widht="0" height="0" name="commintFrame" id="commintFrame"></iframe> 
</body>
</html>
