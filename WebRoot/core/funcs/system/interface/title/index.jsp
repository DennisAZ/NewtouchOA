<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.global.YHRegistProps" %>
<%@ page import="yh.core.data.YHAuthKeys" %>
<head>
<title>界面设置</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/system/interface/js/interfaceLogic.js"></script>
<script type="text/javascript">
var attachmentId = "";
var seqIdStr = "";

/**
 * 默认界面主题
 * @return
 */

function theme(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getTheme.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var select = document.getElementById("theme");
    //select.value = "0";
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var option = document.createElement("option");
      option.value = rtJson.rtData[i].value;
      option.innerHTML = rtJson.rtData[i].text;
      select.appendChild(option);
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}


function webosLogo(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getWebosLogo.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    if (rtJson.rtData == "1") {
      $('editPic2').show();
    }
    else {
      $('showPic2').show();
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getDefaultStyle(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getDefaultStyle.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    if (rtJson.rtData == "1") {
      $('webos').selected = "selected";
    }
    else {
      $('classic').selected = "selected";
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function deleteWebosLogo(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/deleteWebosLogo.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 默认界面布局
 * @return
 */

function ui(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getUi.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var select = document.getElementById("ui");
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var option = document.createElement("option");
      option.value = rtJson.rtData[i].value;
      option.innerHTML = rtJson.rtData[i].text;
      select.appendChild(option);
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 登录界面模板
 * @return
 */

function template(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getTemplate.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var select = document.getElementById("template");
    //select.value = "0";
    for(var i = 0; i < rtJson.rtData.length; i++) {
      var option = document.createElement("option");
      option.value = rtJson.rtData[i].value;
      option.innerHTML = rtJson.rtData[i].text;
      select.appendChild(option);
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 登录界面模板
 * @return
 */


 function statusText(){
   var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getStatusText.act";
   var rtJson = getJsonRs(url);
   if (rtJson.rtState == "0") {
     var statusText = rtJson.rtData;
     $("statusText").value = statusText.replace(new RegExp("&#13;&#10;", "\g"), "\n");
   } else {
     alert(rtJson.rtMsrg); 
   }
 }

function doInit(){
  var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getRegistOrg.act";
  var rtJsons = getJsonRs(urls);
  if (rtJsons.rtState == "0") {
    if(rtJsons.rtData == "0"){
      //$("butDis").disabled = true;
      //$("ieTitle").disabled = true;
      //$("statusText").disabled = true;
      //$("logOutText").disabled = true;
      $("registDiv").style.display = '';
    }
  } else {
    alert(rtJson.rtMsrg); 
  }
  getDefaultStyle();
  webosLogo();
  theme();
  ui();
  template();
  //$('font_color_link_menu').innerHTML=LoadForeColorTable('set_font_color');
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getInterFaces.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    statusText();
    $('seqIds').value = rtJson.rtData.seqId;
    seqIdStr = rtJson.rtData.seqId;
    attachmentId = rtJson.rtData.attachmentId;
    attachmentId1 = rtJson.rtData.attachmentId1;
    var bannerFont = rtJson.rtData.bannerFont;
    bannerFone(bannerFont);
    showPic(attachmentId);
    showPic1(attachmentId1);
    if(rtJson.rtData.avatarUpload == 1){
      document.getElementById("avatarUpload").checked = true;
    }else{
      document.getElementById("avatarUpload").checked = false;
    }
    if(rtJson.rtData.themeSelect == 1){
      document.getElementById("themeSelect").checked = true;
    }else{
      document.getElementById("themeSelect").checked = false;
    }
    if(rtJson.rtData.loginInterface == 1){
      document.getElementById("loginInterface").checked = true;
    }else{
      document.getElementById("loginInterface").checked = false;
    }
    if(rtJson.rtData.ieTitle == ""){
       $("ieTitle").value = "<%=StaticData.SOFTTITLE%>";
    }
    if(rtJson.rtData.bannerText == ""){
      //$("bannerText").value = "<%=StaticData.SOFTTITLE%>";
   }
  } else {
    alert(rtJson.rtMsrg); 
  }

  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getTableGround.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    //bindJson2Cntrl(rtJson.rtData);
    $('myTableBkGround').value = rtJson.rtData.paraValue;
    $('seqIdBk').value = rtJson.rtData.seqId;
  }else {
    alert(rtJson.rtMsrg); 
  }

  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getMiibeian.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    //bindJson2Cntrl(rtJson.rtData);
    $('miibeian').value = rtJson.rtData.paraValue;
  }else {
    alert(rtJson.rtMsrg); 
  }

  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getOnlineView.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    //bindJson2Cntrl(rtJson.rtData);
    if(rtJson.rtData.paraValue == 1){
      $('onLineView1').checked = true;
      $('onLineView2').checked = false;
    }else if(rtJson.rtData.paraValue == 2){
      $('onLineView2').checked = true;
      $('onLineView1').checked = false;
    }else{
      $('onLineView1').checked = true;
      $('onLineView2').checked = false;
    }
  }else {
    alert(rtJson.rtMsrg); 
  }

  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getLogOutText.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
   // bindJson2Cntrl(rtJson.rtData);
    if(rtJson.rtData.paraValue == "null"){
      $('logOutText').value = "";
    }else{
      $('logOutText').value = rtJson.rtData.paraValue;
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
  showModel(seqIdStr);
}

function commit(){
  var msrg = "";
  var reg = /^[0-9]*$/;
  var imgWidth = $('imgWidth');
  var imgHeight = $('imgHeight');
  var imgWidth2 = $('imgWidth2');
  var imgHeight2 = $('imgHeight2');
  var avatarWidth = $('avatarWidth');
  var avatarHeight = $('avatarHeight');
  if(!reg.test(imgWidth.value)){
	alert("主界面-顶部图标宽度只能输入数字！");
	imgWidth.focus();
	imgWidth.select();
	return false;
  }
  if(!reg.test(imgHeight.value)){
  	alert("主界面-顶部图标高度只能输入数字！");
  	imgHeight.focus();
  	imgHeight.select();
  	return false;
  }
  if(imgWidth.value > 550){
		alert("主界面-顶部图标宽度建议小于550！");
		imgWidth.focus();
		imgWidth.select();
		return false;
  }
  if(imgHeight.value > 64){
  	alert("主界面-顶部图标高度建议小于64！");
  	imgHeight.focus();
  	imgHeight.select();
  	return false;
  }
  if(!reg.test(imgWidth2.value)){
		alert("WebOS界面-顶部图标宽度只能输入数字！");
		imgWidth2.focus();
		imgWidth2.select();
		return false;
  }
  if(!reg.test(imgHeight2.value)){
  	alert("WebOS界面-顶部图标高度只能输入数字！");
  	imgHeight2.focus();
  	imgHeight2.select();
  	return false;
  }
  if(imgWidth2.value > 230){
		alert("WebOS界面-顶部图标宽度建议小于230！");
		imgWidth2.focus();
		imgWidth2.select();
		return false;
  }
  if(imgHeight2.value > 80){
  	alert("WebOS界面-顶部图标高度建议小于80！");
  	imgHeight2.focus();
  	imgHeight2.select();
  	return false;
  }
  if(!reg.test(avatarWidth.value)){
  	alert("用户上传头像最大宽度只能输入数字！");
  	avatarWidth.focus();
  	avatarWidth.select();
  	return false;
  }
  if(!reg.test(avatarHeight.value)){
    alert("用户上传头像最大高度只能输入数字！");
    avatarHeight.focus();
    avatarHeight.select();
    return false;
  }

  //头像规格限制
  if (avatarWidth.value < 16) {
    alert("用户上传头像最大宽度不能小于16像素！");
    avatarWidth.focus();
    avatarWidth.select();
    return false;
  }
  if (avatarHeight.value < 16) {
    alert("用户上传头像最大高度不能小于16像素！");
    avatarHeight.focus();
    avatarHeight.select();
    return false;
  }
  
  var seqIds = $('seqIds').value;
  
  var onlineView = "";
  if($('onLineView1').checked == true){
    onlineView = "1";
  }else if($('onLineView2').checked == true){
    onlineView = "2";
  }

//主界面-顶部图标  上传文件
  var ATTACHMENT = "";
  var ATTACHMENT1 = "";

  var av = $('ATTACHMENT').value;
  var av1 = $('ATTACHMENT1').value;
  if ((av && !/.[png,jpg,gif,bmp,jpeg]$/.test(av.toLowerCase())) 
      || (av1 && !/.[png,jpg,gif,bmp,jpeg]$/.test(av1.toLowerCase()))) {
    alert("图片格式不正确!");
    return;
  }
  if($('ATTACHMENT').value != ""){
    ATTACHMENT = "1";
  }
  if($('ATTACHMENT1').value != ""){
    ATTACHMENT1 = "2";
  }
  if($('ATTACHMENT2').value != ""){
    ATTACHMENT1 = "3";
  }
  var attachmentValue = $('ATTACHMENT').value;
  var attachmentSign = attachmentValue.substr(attachmentValue.lastIndexOf("\\")+1, attachmentValue.length);
  var check = containsChinese(attachmentSign);
  if(check){
    $('ATTACHMENT').select();
    $('ATTACHMENT').focus();
    alert("经典界面-顶部图标:图像名称不能含有中文" );
    return false;
  }


  var attachmentValue = $('ATTACHMENT1').value;
  var attachmentSign = attachmentValue.substr(attachmentValue.lastIndexOf("\\")+1, attachmentValue.length);
  var check = containsChinese(attachmentSign);
  if(check){
    $('ATTACHMENT1').select();
    $('ATTACHMENT1').focus();
    alert("登录界面图片:图像名称不能含有中文" );
    return false;
  }

  var attachmentValue = $('ATTACHMENT2').value;
  var attachmentSign = attachmentValue.substr(attachmentValue.lastIndexOf("\\")+1, attachmentValue.length);
  var check = containsChinese(attachmentSign);
  if(check){
    $('ATTACHMENT2').select();
    $('ATTACHMENT2').focus();
    alert("WebOS界面-顶部图标:图像名称不能含有中文" );
    return false;
  }
  
  var actionSize = $('actionSize').value;
  var actionLight = $('actionLight').value;
  var actionFont = $('actionFont').value;
  var actionLights = $('actionLights').value;
  var actionColor = $('actionColor').value;
  var actionFlag = $('actionLightFlag').value;
  
  var theme = $('theme').value;
  var ui = $('ui').value;
  var template = $('template').value;
  var styleDis = "font-family:" + actionFont + ";font-size:" + actionSize + ";color:" + actionColor + ";filter:" + actionFlag + "(Direction=120, color=" + actionLights + ");";
  //alert(styleDis);
  $("styleDis").value = styleDis;
  $("form1").action = contextPath + "/yh/core/funcs/system/interfaces/act/YHInterFacesAct/updateAll.act?onlineView="+onlineView+"&ATTACHMENT="+ATTACHMENT+"&ATTACHMENT1="+ATTACHMENT1;
  $("form1").submit();
}


function deleteAttachPic(flag){
  if(flag == "0"){
    var url = contextPath + "/yh/core/funcs/system/interfaces/act/YHInterFacesAct/updateAttachMent.act";
    var rtJson = getJsonRs(url,"seqId=" + seqIdStr+"&attachmentId="+attachmentId);
    if(rtJson.rtState == "0"){
      bindJson2Cntrl(rtJson.rtData);
      if(rtJson.rtData.attachmentName){
        if(rtJson.rtData.attachmentName != ""){
          $("attachmentNameShow").innerHTML = rtJson.rtData.attachmentName;
        }else{
          $("attachmentNameShow").innerHTML = "";
        }
      }
      window.location.reload();
    }else{
      alert(rtJson.rtMrsg);
    }
  }
  if(flag == "1"){
    var url = contextPath + "/yh/core/funcs/system/interfaces/act/YHInterFacesAct/updateAttachMent1.act";
    var rtJson = getJsonRs(url,"seqId=" + seqIdStr+"&attachmentId1="+attachmentId1);
    if(rtJson.rtState == "0"){
      bindJson2Cntrl(rtJson.rtData);
      if(rtJson.rtData.attachmentName1){
        if(rtJson.rtData.attachmentName1 != ""){
          $("attachmentNameShow1").innerHTML = rtJson.rtData.attachmentName1;
        }else{
          $("attachmentNameShow1").innerHTML = "";
        }
      }
      window.location.reload();
    }else{
      alert(rtJson.rtMrsg);
    }

  }
}
</script>
</head>
<body onload="doInit();" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><b><span class="big1">&nbsp;界面设置</big></b>
    </td>
  </tr>
</table>
<form enctype="multipart/form-data" method="post" name="form1" id="form1">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.interfaces.data.YHInterface"/>
  <input type="hidden" id="seqIds" name="seqIds" value=""/>
  <input type="hidden" id="seqIdBk" name="seqIdBk" value=""/>
  <table class="TableBlock" width="600" align="center">
  <div id="registDiv" style="display:none">
  </div>
   <tr>
    <td colspan=2 class="TableHeader">IE浏览器窗口标题</td>
   </tr>
   <tr>
    <td nowrap class="TableData">IE浏览器窗口标题：</td>
    <td nowrap class="TableData">
        <input type="text" name="ieTitle" id="ieTitle" class="BigInput" size="40" maxlength="100" value="" style="">&nbsp;
    </td>
   </tr>
   <tr>
    <td colspan=2 class="TableHeader">主界面</td>
   </tr>
   <tr>
    <td nowrap class="TableData">顶部大标题文字：</td>
    <td nowrap class="TableData">
        <input type="text" name="bannerText" id="bannerText" class="BigInput" size="40" maxlength="100" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">顶部大标题样式：</td>
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
    <span style="padding-top:5px;padding-left:10px;">
     <a id="showMeunD" href="#" onclick="showLight(event);"><span id="actionNameLight">效果</span></a><img src="<%=imgPath%>/cal_list.gif" align="absmiddle">&nbsp;
    </span>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">经典界面-顶部图标：</td>
    <td nowrap class="TableData">
       <div id="editPic" style="display:none"><span><a href="javascript:;" id="attachmentNameShow" name="attachmentNameShow"></a></span>
        <input type="button" value="恢复默认" class="BigButton" onClick="deleteAttachPic(0);"></div>
   	 	<div id="showPic" style="display:none"><br><input type="file" name="ATTACHMENT" size=40 class="BigInput" id="ATTACHMENT" ></div>
     
   	   <input type="hidden" id="ATTACHMENT_ID_OLD"  name="ATTACHMENT_ID_OLD" value="">
   	   <input type="hidden" id="attachmentName"  name="attachmentName" value="">
   	   <input type="hidden" id="attachmentId"  name="attachmentId" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD"  name="ATTACHMENT_NAME_OLD" value="">	  
    <br>JPG、GIF、PNG格式，<span class="big4">注意：图像名称不能含有中文</span>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">经典界面-顶部图标宽度：</td>
    <td nowrap class="TableData">
        <input type="text" name="imgWidth" id="imgWidth" class="BigInput" size="10" maxlength="100" value="">&nbsp;像素
      &nbsp;(建议宽度小于550像素)
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">经典界面-顶部图标高度：</td>
    <td nowrap class="TableData">
        <input type="text" name="imgHeight" id="imgHeight" class="BigInput" size="10" maxlength="100" value="">&nbsp;像素&nbsp;(建议高度小于64像素)
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">经典界面-底部状态栏置中文字：</td>
    <td nowrap class="TableData">
        <textarea name="statusText" id="statusText" class="BigInput" cols="44" rows="3"></textarea><br>多行文字可以实现轮换显示
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">WebOS界面-顶部图标：</td>
    <td nowrap class="TableData">
       <div id="editPic2" style="display:none"><span><a href="javascript:;" id="attachmentNameShow2" name="attachmentNameShow2"></a></span>
        <input type="button" value="恢复默认" class="BigButton" onClick="deleteWebosLogo();"></div>
      <div id="showPic2" style="display:none">
      <br><input type="file" name="ATTACHMENT2" size=40 class="BigInput" id="ATTACHMENT2" ></div>
     
       <input type="hidden" id="ATTACHMENT_ID_OLD2"  name="ATTACHMENT_ID_OLD2" value="">
       <input type="hidden" id="attachmentName2"  name="attachmentName2" value="">
       <input type="hidden" id="attachmentId2"  name="attachmentId2" value="">
       <input type="hidden" id="ATTACHMENT_NAME_OLD2"  name="ATTACHMENT_NAME_OLD2" value="">    
    <br>JPG、GIF、PNG格式，<span class="big4">注意：图像名称不能含有中文</span>
    </td>
   </tr>
   <tr>
   <td nowrap class="TableData">WebOS界面-顶部图标宽度：</td>
    <td nowrap class="TableData">
        <input type="text" name="imgWidth2" id="imgWidth2" class="BigInput" size="10" maxlength="100" value="">&nbsp;像素&nbsp;(建议宽度小于230像素)
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">WebOS界面-顶部图标高度：</td>
    <td nowrap class="TableData">
        <input type="text" name="imgHeight2" id="imgHeight2" class="BigInput" size="10" maxlength="100" value="">&nbsp;像素&nbsp;(建议高度小于80像素)
    </td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">自定义桌面背景图片：</td>
    <td nowrap class="TableData">
        <select name="myTableBkGround" id="myTableBkGround">
          <option value="-1" >禁止自定义</option>
          <option value="100" >限制大小100KB</option>
          <option value="200" >限制大小200KB</option>
          <option value="300" >限制大小300KB</option>
          <option value="500" >限制大小500KB</option>
          <option value="1024">限制大小1MB</option>
          <option value="2048">限制大小2MB</option>
          <option value="0" >不限制大小</option>
        </select> 设置用户自定义桌面背景图片的最大尺寸
    </td>
   </tr>
   <tr>
    <td colspan=2 class="TableHeader">登录界面</td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">登录界面模板：</td>
    <td nowrap class="TableData">
        <select name="template" id="template">
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">登录界面图片：</td>
    <td nowrap class="TableData">
       <div id="editPic1" style="display:none"><span><a href="javascript:;" id="attachmentNameShow1" name="attachmentNameShow1"></a></span>
        <input type="button" value="恢复默认" class="BigButton" onClick="deleteAttachPic(1);"></div>
      <div id="showPic1" style="display:none"><br><input type="file" name="ATTACHMENT1" size=40 class="BigInput" id="ATTACHMENT1" ></div> 
       
   	   <input type="hidden" id="ATTACHMENT_ID_OLD1"  name="ATTACHMENT_ID_OLD1" value="">
   	   <input type="hidden" id="attachmentName1"  name="attachmentName1" value="">
   	   <input type="hidden" id="attachmentId1"  name="attachmentId1" value="">
	   <input type="hidden" id="ATTACHMENT_NAME_OLD1"  name="ATTACHMENT_NAME_OLD1" value="">	  
    <br>JPG、GIF、PNG格式，<span class="big4">注意：图像名称不能有中文</span>
    </td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">选择界面布局：</td>
    <td nowrap class="TableData">
        <input type="checkbox" name="loginInterface" id="loginInterface" ><label for="loginInterface">允许用户登录时选择界面布局</label>
    </td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">默认界面布局：</td>
    <td nowrap class="TableData">
        <select name="ui" id="ui">
        <option value="0">标准界面</option>
        </select>
    </td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">网站备案号：</td>
    <td nowrap class="TableData">
        <input type="text" name="miibeian" id="miibeian" class="BigInput" size="25" value="">
    </td>
   </tr>
   <tr style="display:none;">
    <td colspan=2 class="TableHeader">界面主题</td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">选择界面主题：</td>
    <td nowrap class="TableData">
        <input type="checkbox" name="themeSelect" id="themeSelect" value="1"><label for="themeSelect">允许用户选择界面主题</label>
    </td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">默认界面主题：</td>
    <td nowrap class="TableData">
        <select name="theme" id="theme">
        </select>
    </td>
   </tr>
   <tr style="display:none;">
    <td colspan=2 class="TableHeader">界面风格</td>
   </tr>
   <tr style="display:none;">
    <td nowrap class="TableData">缺省界面风格：</td>
    <td nowrap class="TableData">
        <select name="style" id="style">
          <option id="classic" value="0">经典界面</option>
          <option id="webos" value="1">WebOS界面</option>
          <option id="tdoa" value="2">梦幻灵动界面</option>
        </select>
    </td>
   </tr>
   <tr>
    <td colspan=2 class="TableHeader">用户头像</td>
   </tr>
   <tr>
    <td nowrap class="TableData">用户上传头像：</td>
    <td nowrap class="TableData">
        <input type="checkbox" name="avatarUpload" id="avatarUpload" ><label for="avatarUpload">允许用户上传头像</label>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">用户上传头像最大宽度：</td>
    <td nowrap class="TableData">
        <input type="text" name="avatarWidth" id="avatarWidth" class="BigInput" size="10" maxlength="11" value="">&nbsp;像素
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">用户上传头像最大高度：</td>
    <td nowrap class="TableData">
        <input type="text" name="avatarHeight" id="avatarHeight" class="BigInput" size="10" maxlength="11" value="">&nbsp;像素
    </td>
   </tr>
   <tr>
    <td colspan=2 class="TableHeader">其它</td>
   </tr>
   <tr>
    <td nowrap class="TableData">在线人员显示方式：</td>
    <td nowrap class="TableData">
        <input type="radio" name="onLineView" id="onLineView1" value="1" ><label for="onLineView1">树形列表</label>
        <input type="radio" name="onLineView" id="onLineView2" value="2" ><label for="onLineView2">平行列表</label>
    </td>
   </tr>
   <tr>
    <td colspan=2 class="TableHeader">注销提示文字</td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="150">用户点击注销时，显示这里设置的文字：</td>
    <td nowrap class="TableData">
        <textarea name="logOutText" id="logOutText" class="BigInput" cols="44" rows="3"></textarea><br>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
         <input type="hidden" name="actionFont" id="actionFont" value="">
         <input type="hidden" name="actionSize" id="actionSize" value="">
         <input type="hidden" name="actionLight" id="actionLight" value="">
         <input type="hidden" name="actionLights" id="actionLights" value="">
         <input type="hidden" name="actionColor" id="actionColor" value="">
         <input type="hidden" name="actionLightFlag" id="actionLightFlag" value="">
         <input type="hidden" name="styleDis" id="styleDis" value="">
         <input type="hidden" name="ATTACHMENT_NAME" value="">
        <input type="hidden" name="ATTACHMENT_NAME1" value="">
        <input type="button" id="butDis" value="确定" class="BigButton" onclick="commit();">
    </td>
   </tr>
</table>
</form>
</body>
</html>