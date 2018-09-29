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
<title>编辑社会关系信息</title>
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
<script type="text/javascript"  src="<%=contextPath%>/subsys/oa/hr/manage/staffRelatives/js/staffRelativesLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;

function doInit(){
  getSecretFlag("HR_STAFF_RELATIVES1","relationship");
  getSecretFlag("HR_STAFF_RELATIVES2","politics");
  setDate();
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/relatives/act/YHHrStaffRelativesAct/getRelativesDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    if(data.birthday){
      $("birthday").value = data.birthday.substr(0,10);
    }
    if(data.staffName){
      bindDesc([{cntrlId:"staffName", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
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

//日期
function setDate(){
  var date1Parameters = {
     inputId:'birthday',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);
}
function doSubmit(){
  if(checkForm()){
    $("form1").submit();
    //alert("通过。。。。");
  }
}

function checkForm(){
  if($("staffNameDesc").value == ""){
    alert("员工姓名不能为空！");
    $("staffNameDesc").focus();
    return (false);
  }

  if($("member").value == ""){
    alert("成员姓名不能为空！");
    $("member").focus();
    return (false);
  }

  if($("relationship").value == ""){
    alert("与本人关系不能为空！");
    $("relationship").focus();
    return (false);
  }

  if($("workUnit").value == ""){
    alert("工作单位不能为空！");
    $("workUnit").focus();
    return (false);
  }

  if($("unitAddress").value == ""){
    alert("单位地址不能为空！");
    $("unitAddress").focus();
    return (false);
  }

  var birthday = $("birthday").value;
  if(birthday){
    if(!isValidDateStr(birthday)){
      alert("出生日期格式不对，应形如 2010-01-02");
      $("birthday").focus();
      $("birthday").select();
      return false;
    }
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
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/relatives/act/YHHrStaffRelativesAct/delFloatFile.act?delAttachId=" + attachId + "&seqId=<%=seqId%>";
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
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑社会关系信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/relatives/act/YHHrStaffRelativesAct/updateRelativesInfo.act"  method="post" name="form1" id="form1" onsubmit="">
<table class="TableBlock" width="80%" align="center">
 <tr>
    <td nowrap class="TableData">单位员工：<font color="red">*</font> </td>
    <td class="TableData">
      <input type="hidden" name="staffName" id="staffName" value="">
      <input type="text" name="staffNameDesc" id="staffNameDesc" class=BigInput readonly size="15">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['staffName', 'staffNameDesc'],null,null,1);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('staffName').value='';$('staffNameDesc').value='';">清空</a>
    </td>
    <td nowrap class="TableData">成员姓名：<font color="red">*</font> </td>
    <td class="TableData">
      <input type="text" name="member" id="member" class="BigInput" size="15">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">与本人关系：<font color="red">*</font> </td>
    <td class="TableData" >
      <select name="relationship" id="relationship"  title="与本人关系可在“人力资源设置”->“HRMS代码设置”模块设置。">
        <option value="">与本人关系&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
    </td>
    <td nowrap class="TableData">出生日期：</td>
    <td class="TableData">
      <input type="text" name="birthday" id="birthday" size="11" maxlength="10"  class="BigInput" value="" readonly>
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">政治面貌：</td>
    <td class="TableData" >
      <select name="politics" id="politics"  title="政治面貌可在“人力资源设置”->“HRMS代码设置”模块设置。">
        <option value="">政治面貌&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
      </select>
    </td>
    <td nowrap class="TableData">职业：</td>
    <td class="TableData">
      <input type="text" name="jobOccupation" id="jobOccupation" class="BigInput" size="15">
    </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 担任职务：</td>
      <td class="TableData">
        <input type="text" name="postOfJob" id="postOfJob" class="BigInput" size="15">
      </td>
      <td nowrap class="TableData"> 联系电话（个人）：</td>
      <td class="TableData">
        <input type="text" name="personalTel" id="personalTel" class="BigInput" size="15">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData"> 联系电话（家庭）：</td>
      <td class="TableData">
        <INPUT type="text" name="homeTel" id="homeTel" class=BigInput size="15" value="">
      </td>
      <td nowrap class="TableData"> 联系电话（单位）：</td>
      <td class="TableData">
        <INPUT type="text" name="officeTel" id="officeTel" class=BigInput size="15" value="">
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">工作单位：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <textarea name="workUnit" id="workUnit" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">单位地址：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <textarea name="unitAddress" id="unitAddress" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>

    </tr>   
    <tr>
      <td nowrap class="TableData">家庭住址：</td>
      <td class="TableData" colspan=3>
        <textarea name="homeAddress" id="homeAddress" cols="78" rows="2" class="BigInput" value=""></textarea>
      </td>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="remark" id="remark" cols="78" rows="2" class="BigInput" value=""></textarea>
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
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="hidden" name="seqId" id="seqId" value="<%=seqId %>">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
        <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath %>/subsys/oa/hr/manage/staffRelatives/manage.jsp'">
      </td>
    </tr>
  </table>
</form>

</body>
</html>