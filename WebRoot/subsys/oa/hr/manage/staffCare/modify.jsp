<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑员工关怀信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"  type="text/css" />
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/subsys/oa/hr/manage/staffCare/js/staffCareLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
  getSecretFlag("HR_STAFF_CARE1","careType");
  setDate();
  getSysRemind("smsRemindDiv","smsRemind",57);
  moblieSmsRemind("sms2RemindDiv","sms2Remind",57);
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct/getCareDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    if(data.careDate){
      $("careDate").value = data.careDate.substr(0,10);
    }
    if(data.byCareStaffs){
      bindDesc([{cntrlId:"byCareStaffs", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(data.participants){
      bindDesc([{cntrlId:"participants", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    fckContentStr = data.careContent;
    if(data.attachmentId){
      $("returnAttId").value = data.attachmentId;
      $("returnAttName").value = data.attachmentName;
      var selfdefMenu = {
          office:["downFile","dump","read","edit","deleteFile"], 
          img:["downFile","dump","play","deleteFile"],  
          music:["downFile","play","deleteFile"],  
          video:["downFile","play","deleteFile"], 
          others:["downFile","deleteFile"]
      }
      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
    }else{
      $('attr').innerHTML = "无附件";
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function FCKeditor_OnComplete( editorInstance ) {
  editorInstance.SetData( fckContentStr ) ;
}

//日期
function setDate(){
  var date1Parameters = {
     inputId:'careDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
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

function doSubmit(){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder');
  $("careContent").value = oEditor.GetXHTML();
  if(checkForm()){
    $("form1").submit();
    //alert("通过。。。。");
  }
}

function checkForm(){
  if($("byCareStaffsDesc").value == ""){
    alert("被关怀员工不能为空！");
    $("byCareStaffsDesc").focus();
    return (false);
  }

  if($("careDate").value == ""){
    alert("关怀日期不能为空！");
    $("careDate").focus();
    return (false);
  }

  if($("participantsDesc").value == ""){
    alert("参与人不能为空！");
    $("participantsDesc").focus();
    return (false);
  }

  if($("careContent").value == ""){
    alert("关怀内容不能为空！");
    return (false);
  }

  var careFees = $("careFees").value;
  if(careFees){
    if(!checkRate(careFees)){
        alert("您填写的关怀开支费用格式错误，请输入正整数");
        $("careFees").focus();
        $("careFees").select();
      return (false);
    }
  }
  return true;
}

//判断正整数  
function checkRate(input){ 
  var re = /^[1-9]+[0-9]*]*$/;
  if(!re.test(input)) {  
    return false;  
  }  
  return true;
}  

//插入图片
function InsertImage(src){
  var oEditor = FCKeditorAPI.GetInstance('fileFolder') ;
  if ( oEditor.EditMode == FCK_EDITMODE_WYSIWYG )   {
    oEditor.InsertHtml( "<img src='"+ src  + "'/>") ;
  }
}
//删除附件
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct/delFloatFile.act?delAttachId=" + attachId + "&seqId=<%=seqId%>";
  //var json = getJsonRs(url);
  var json=getJsonRs(url);
  if(json.rtState =='1'){
    alert(json.rtMsrg);
    return false;
  }else{
    prcsJson=json.rtData;
    var updateFlag=prcsJson.updateFlag;
    if(updateFlag == "1"){
      return true;
    }else{
      return false;
    }
  }
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑员工关怀信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/care/act/YHHrStaffCareAct/updateCareInfo.act"  method="post" name="form1" id="form1" onsubmit="">
  <table class="TableBlock" width="80%" align="center">
    <tr>
      <td nowrap class="TableData">关怀类型： </td>
      <td class="TableData" >
        <select name="careType" id="careType"  title="关怀类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">关怀类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td>
      <td nowrap class="TableData">关怀开支费用：</td>
      <td class="TableData">
        <input type="text" name="careFees" id="careFees" class="BigInput" size="15">(元)
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">被关怀员工：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="hidden" name="byCareStaffs" id="byCareStaffs" value="" >
        <input type="text" name="byCareStaffsDesc" id="byCareStaffsDesc" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['byCareStaffs', 'byCareStaffsDesc'],null,null,1);">添加</a>
      </td>
      <td nowrap class="TableData">关怀日期：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="careDate" id="careDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">关怀效果： </td>
      <td class="TableData" colspan=3>
        <textarea name="careEffects" id="careEffects" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">参与人：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="participants" id="participants" value="">
        <textarea cols="40" name="participantsDesc" id="participantsDesc" rows="2" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['participants', 'participantsDesc'],null,null,1);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('participants').value='';$('participantsDesc').value='';">清空</a>
      </td>
    </tr> 
    <tr class="TableData" id="attachment2">
      <td nowrap>附件文档：</td>
      <td nowrap colspan=3>
      <input type = "hidden" id="returnAttId" name="returnAttId"></input>
      <input type = "hidden" id="returnAttName" name="returnAttName"></input>
      <span id="attr"></span> 
      </td>
   </tr>  
   <tr height="25" id="attachment1">
      <td nowrap class="TableData"><span id="ATTACH_LABEL">附件上传：</span></td>
       <td class="TableData" colspan="3">
      <script>ShowAddFile();</script>
      <script></script>
      <script></script> 
      <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
      <%--插入图片 --%>
      <input type="hidden" id="moduel" name="moduel" value="">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
      <input type="hidden" id="imgattachmentName" name="imgattachmentName">
     </td>
   </tr>
   <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData" colspan=3>
        <span id="smsRemindDiv" style="display: none"><input type="checkbox" name="smsRemind" id="smsRemind" value="" onclick="checkBox('smsRemind')"><label for="smsRemind">使用内部短信提醒</label>&nbsp;&nbsp;</span>
        <span id="sms2RemindDiv" style="display: none"><input type="checkbox" name="sms2Remind" id="sms2Remind" value="" onclick="checkBox('sms2Remind')"><label for="sms2Remind">使用手机短信提醒 </label>&nbsp;&nbsp;</span>
      </td>
   </tr>
    <tr id="EDITOR">
      <td class="TableData" colspan="4"> 关怀内容：<font color="red">*</font>
        <div>
         <script language=JavaScript>    
          var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
          var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
          oFCKeditor.BasePath = sBasePath ;
          oFCKeditor.Height = 200;
          var sSkinPath = sBasePath + 'editor/skins/office2003/';
          oFCKeditor.Config['SkinPath'] = sSkinPath ;
          oFCKeditor.Config['PreloadImages'] =
                          sSkinPath + 'images/toolbar.start.gif' + ';' +
                          sSkinPath + 'images/toolbar.end.gif' + ';' +
                          sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                          sSkinPath + 'images/toolbar.buttonarrow.gif' ;
          //oFCKeditor.Config['FullPage'] = true ;
           oFCKeditor.ToolbarSet = "fileFolder";
          oFCKeditor.Value = '' ;
          oFCKeditor.Create();
         </script>
        </div>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="hidden" name="careContent" id="careContent" value="">
        <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
        <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath %>/subsys/oa/hr/manage/staffCare/manage.jsp'">
      </td>
    </tr>
  </table>
</form>

</body>
</html>