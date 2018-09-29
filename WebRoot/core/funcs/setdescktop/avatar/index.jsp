<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>昵称与头像</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript">

var avatarPath = contextPath + "/attachment/avatar/";
var photoPath = contextPath + "/attachment/photo/";
var nickName = '';
function doInit(){
  $('photoShow').hide();
  $('avatarShow').hide();
  
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/avatar/act/YHAvatarAct/getAvatarNickName.act");
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    
    $('nickName').value = data.NICK_NAME;
    nickName = data.NiCK_NAME;
    $('bbsSignature').value = data.BBS_SIGNATURE;

    if (data.AUATAR && /\.\w/.test(data.AUATAR)) {
    　       if (data.AUATAR != 'default.gif') {
    　             showAvatar(data.AUATAR);
    　         }
    }
    
    if (data.PHOTO && /\.\w/.test(data.PHOTO)) {
      if (data.PHOTO != 'default.gif') {
        showPhoto(data.PHOTO);
      }
    }

    initUpload();
  }
}

function checkNickName(){
  if (!$('nickName').value == nickName) {
	  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/checkNickName.act?NICK_NAME=" + $('nickName').value;
	  var json = getJsonRs(url);
	  if (json.rtState == "0") {
	    if (json.rtData) {
	      alert("昵称已被其他人员使用");
	      $('nickName').select();
	    }
	  }else{
	  }
  }
}

function submitForm() {

  if ($('avatar').value && !/[(\.gif),(\.png),(\.jpg),(\.GIF),(\.PNG),(\.JPG)]$/.test($('avatar').value)) {
    alert("头像格式不正确!");
    $('avatar').value = "";
    return;
  }
  if ($('photo').value && !/[(\.gif),(\.png),(\.jpg),(\.GIF),(\.PNG),(\.JPG)]$/.test($('photo').value)) {
    alert("照片格式不正确!");
    $('photo').value = "";
    return;
  }
  $('form1').submit();
}

function deleteAvatar() {
  if (!confirm("确认要删除吗?")) {
    return;
  }
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/avatar/act/YHAvatarAct/resetAvatar.act");
  if (rtJson.rtState == "0") {
	  $('avatarImg').src = "";
	  $('avatarShow').hide();
	  $('avatarUpload').show();
  }
}

function deletePhoto() {
  if (!confirm("确认要删除吗?")) {
    return;
  }
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/avatar/act/YHAvatarAct/resetPhoto.act");
  if (rtJson.rtState == "0") {
    $('photoImg').src = "";
    $('photoShow').hide();
    $('photoUpload').show();
  }
}

function showAvatar(name) {
  $('avatarShow').show();
  $('avatarUpload').hide();
  $('avatarImg').src = avatarPath + name;
}

function showPhoto(name) {
  $('photoShow').show();
  $('photoUpload').hide();
  $('photoImg').src = photoPath + name;
}

function initUpload() {
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/getAvatarConfig.act");
  
  if (rtJson.rtState == "0") {
    if (rtJson.rtData.avatar == '1') {
      $('avatarUpload').show();
      $('avatarDesc').innerHTML = '头像文件要求是gif、jpg、png格式，大小不能超过' + 
      rtJson.rtData.width + '*' + rtJson.rtData.height + '像素。';
    }
  }
}
</script>
<style>
</style>
</head>

<body onload="doInit()">
<div class="PageHeader">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img height="20px" width="20px" src="<%=imgPath%>/avatar.png" align="absmiddle"></img><span class="big3"> 昵称与头像</span><br>
    </td>
  </tr>
</table>
</div>
<form enctype="multipart/form-data" action="<%=contextPath %>/yh/core/funcs/setdescktop/avatar/act/YHAvatarAct/setAvatarNickName.act"  method="post" name="form1" id="form1">
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData"> 昵称（用于讨论区交流等）：</td>
      <td class="TableData">
        <input type="text" name="nickName" id="nickName" size="25" maxlength="25" onblur="checkNickName()" class="BigInput" value="">
      </td>
    </tr>
    <tr id="avatarUpload">
      <td nowrap class="TableData" valign="top"> 上传头像：</td>
      <td class="TableData" id="">
          <input type="file" name="avatar" id="avatar" size="30" class="BigInput" title="选择附件文件" value="">
      <br><span id="avatarDesc"></span>
      </td>
    </tr>
    <tr id="avatarShow">
      <td nowrap class="TableData" valign="top"> 头像：</td>
      <td class="TableData">
        <img id="avatarImg"></img><input type="button" class="BigButton" onclick="deleteAvatar()" value="删除"/>
      </td>
    </tr>
    <tr id="photoUpload">
      <td valign="top" nowrap class="TableData">上传照片：</td>
      <td class="TableData">
        <div id="upload">
          <input type="file" name="photo" id="photo" size="30" class="BigInput" title="选择附件文件" value="">
          <br><span id="photoDesc"></span>
        </div>
      </td>
    </tr>
    <tr id="photoShow">
      <td nowrap class="TableData" valign="top"> 照片：</td>
      <td class="TableData">
        <img id="photoImg"></img><input type="button" class="BigButton" onclick="deletePhoto()" value="删除"/>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">讨论区签名档：</td>
      <td class="TableData">
        <textarea cols=50 name="bbsSignature" id="bbsSignature" rows=3 class="BigInput" wrap="off"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" value="" name="avatarVal" id="avatarVal">
        <input type="hidden" value="" name="ATTACHMENT_NAME" >
        <input type="button" value="保存修改" class="BigButton" onclick="submitForm()">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

<br>

</body>
</html>