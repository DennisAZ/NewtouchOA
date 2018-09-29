<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<link  rel="stylesheet"  href  ="<%=cssPath  %>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript"  src="js/weather.js"></script>

<%YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); %>
<script  type="text/javascript">
/**
 * 设置选中值


 */
var initSelectState = function(e,value){
  e.childElements().each(function(e,i){
    if(e.value == value){
      e.writeAttribute({'selected': 'selected'});
      //e.selected = "selected";
    }
  });
}

/**
 * 初始化下拉菜单的选项
 */
var initSelectOption = function(element,json){
  json.each(function(e,i){
    element.options.add(new Option(e.menuName,e.seqId));
  });
  
}

/**
 * 默认界面主题
 * @return
 */

function initTheme(){
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getTheme.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var select = document.getElementById("THEME");
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
var soundFlag = false;

function doInit(){

  //init_province();
  
  var rtSelect = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/getMenu.act");
  if (rtSelect.rtState == "0"){
    var menus = [{
      menuName: "无",
      seqId: ""
    }].concat(rtSelect.rtData);
    initSelectOption($('MENU_EXPAND'), menus);
  }

  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/initForm.act");
  if (rtJson.rtState == "0"){
    if (rtJson.rtData.SHOW_THEME == "0") {
      $('themeSel').hide();
    }
    else {
      initTheme();
    }
    if (rtJson.rtData.MENU_IMAGE == "0"){
      $('MENU_IMAGE0').checked = true;
    }
    else if(rtJson.rtData.MENU_IMAGE == "1"){
      $('MENU_IMAGE1').checked = true;
    }
    if ((!rtJson.rtData.SHOW_WEATHER) || (rtJson.rtData.SHOW_WEATHER == '0')){
      //$('WEATHER0').hide();
      //$('WEATHER1').hide();
      $('SHOW_WEATHER').checked = false;
      $('area_select').hide();
    }
    init_province();
    SetCity(rtJson.rtData.WEATHER_CITY);
    
    if(rtJson.rtData.SHOW_RSS == '0'){
      //$('SHOW_RSS').checked = false;
    }
    else{
      //$('SHOW_RSS').checked = true;
    }
    if(rtJson.rtData.CALL_SOUND >= 0){
      $('CUSTOM_CALL_SOUND').hide();
      $('changeSound').hide();
    }
    else {
      soundFlag = true;
      $('uploadSound').hide();
    }

    //解决chrome兼容性问题

    var menuType = rtJson.rtData.MENU_TYPE;
    menuType = menuType * 1 || 1;

    initSelectState($('MENU_EXPAND'), rtJson.rtData.MENU_EXPAND);
    initSelectState($('NEV_MENU_OPEN'), rtJson.rtData.NEV_MENU_OPEN);
    initSelectState($('MENU_TYPE'), menuType);
    initSelectState($('PANEL'), rtJson.rtData.PANEL);
    initSelectState($('SMS_ON'), rtJson.rtData.SMS_ON);
    initSelectState($('THEME'), rtJson.rtData.THEME);
    initSelectState($('CALL_SOUND'), rtJson.rtData.CALL_SOUND);
    initSelectState($('FX'), rtJson.rtData.fx);
  }else {
    alert('获取属性失败');
  }
}

var submitForm = function(){
  if($('CALL_SOUND').value == '-1' && !$F('CUSTOM_SOUND')){
    if (!soundFlag) {
      alert('请重新选择短信提示音');
      $('CALL_SOUND').select();
      return;
    }
  }

  if($('SHOW_WEATHER').checked){
    if ($('WEATHER_CITY').value.length != 5){
      alert('请选择天气预报城市');
      $('WEATHER_CITY').select();
      return;
    }
  }

  $('form1').submit();
  /*
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/setTheme.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    alert("修改属性成功!");
  }else{
    alert("属性更改失败");
  }
  */
}

function changeHome(index) {
  var value = "0";
  if (index == 1) {
    value = "1";
  }
  else if (index == 2){
    value = "2";
  }
  var pars = "?name=classicHome&value=" + index;
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/updateUserParam.act";
  var json = getJsonRs(url, pars);
  if(json.rtState == "0"){
    if (value == "1") {
      top.location.replace(contextPath + "/core/frame/2/index.jsp");
    }
    else if(value == "2"){
      top.location.replace(contextPath + "/core/frame/5/index.jsp");
    }
    else {
      top.location.replace(contextPath + "/core/frame/webos/index.jsp");
    }
  }else{
    alert("属性更改失败");
  }
}

function selectSound() {

  if ($F('CALL_SOUND') != -1) {
    $('CUSTOM_CALL_SOUND').hide();
  }
  else {
    $('CUSTOM_CALL_SOUND').show();
  }
  
  var sound = $F('CALL_SOUND');
  if (sound > 0) {
    playSound("<%=contextPath%>/core/frame/wav/" + sound + ".swf");
  } else if (sound == -1) {
    $('CUSTOM_CALL_SOUND').show();
  }
}

function playSound(name) {
  top.sms.playSound && top.sms.playSound(name);
}

function deleteSound() {
  soundFlag = false;
  $('uploadSound').show();
  $('changeSound').hide();
}

function playMySound() {
  top.playSound && top.playSound('<%=contextPath%>/theme/sound/<%=user.getSeqId()%>.swf');
}
</script>
</head>

<body onLoad="doInit()">
<div class="PageHeader" id="left_top">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/control_theme.gif" align="abstop"/><span class="big3">界面设置  - <%= user.getUserName() %></span><br>
    </td>
  </tr>
</table>
</div>
<form action="<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/setTheme.act"  method="post" name="form1" id="form1" enctype="multipart/form-data">
<table class="TableTop" style="width: 80%">
  <tr>
    <td class="left">
    </td>
    <td class="center">
    界面菜单

    </td>
    <td class="right">
    </td>
  </tr>
</table>
<table class="TableBlock no-top-border" width="80%" align="left">
    <tr id="themeSel" style="display:none;">
      <td nowrap class="TableData" width=20%>界面主题：</td>
      <td class="TableData">
        <select name="THEME" id="THEME">
        </select>
      </td>
    </tr>
    <tr style="display:none;">
      <td nowrap class="TableData">桌面背景图片：</td>
      <td class="TableData">
        <select name="THEME2" id="THEME2">
          <option value="">开发中...</option>
        </select>
      </td>
    <tr>

    <tr style="display:none;">
      <td nowrap class="TableData" width=20%>菜单图标：</td>
      <td class="TableData">
        <font color=gray>提示：选择不显示图标或单一图标会加速慢速网络时的登录速度</font><br>
         <input type="radio" name="MENU_IMAGE" value="0" id="MENU_IMAGE0" ><label for="MENU_IMAGE0" style="cursor:pointer">每个菜单使用不同图标</label>
         <input type="radio" name="MENU_IMAGE" value="1" id="MENU_IMAGE1" ><label for="MENU_IMAGE1" style="cursor:pointer">不显示菜单图标</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=20%>默认导航菜单状态：</td>
      <td class="TableData">
        <select name="NEV_MENU_OPEN" id="NEV_MENU_OPEN">
          <option value="0">隐藏</option>
          <option value="1">显示</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=20%>默认展开菜单：</td>
      <td class="TableData">
        <select name="MENU_EXPAND" id="MENU_EXPAND">
        </select>
      </td>
    </tr>
    <tr style="display:none;">
      <td nowrap class="TableData" width=20%>WebOS动画效果：</td>
      <td class="TableData">
        <select name="FX" id="FX">
          <option value="1">启用</option>
          <option value="0">禁用</option>
        </select>
      </td>
    </tr>
    <tr style="display:none;">
      <td nowrap class="TableData" width=20%>界面切换：</td>
      <td class="TableData">
        &nbsp;&nbsp;<input type="button" value="切换为 WebOS" class="BigButtonC" onclick="changeHome(0)">&nbsp;&nbsp;
        <input type="button" value="切换为经典界面" class="BigButtonC" onclick="changeHome(1)">&nbsp;&nbsp;
        <input type="button" value="切换为梦幻界面" class="BigButtonC" onclick="changeHome(2)">
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"> 登录选项</td>
    </tr>
    <tr>
      <td nowrap class="TableData">登录模式：</td>
      <td class="TableData">
        <select name="MENU_TYPE" id="MENU_TYPE">
          <option value="1">在本窗口打开OA</option>
          <option value="2">在新窗口打开OA，显示工具栏</option>
          <option value="3">在新窗口打开OA，无工具栏</option>
        </select>
        重新登录后生效


      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">登录后显示的左侧面板：</td>
      <td class="TableData">
        <select name="PANEL" id="PANEL">
          <option value="1">导航</option>
          <option value="2">组织</option>
          <option value="3">短信</option>
          <option value="4">搜索</option>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"> 内部短信设置</td>
    </tr>
    <tr>
      <td nowrap class="TableData">短信提醒窗口弹出方式：</td>
      <td class="TableData">
        <select name="SMS_ON" id="SMS_ON">
          <option value="1">自动</option>
          <option value="0">手动</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">短信提示音：</td>
      <td class="TableData">
        <select name="CALL_SOUND" id="CALL_SOUND" onChange="selectSound()">
          <option value="-1">自定义</option>
          <option value="9">长语音</option>
          <option value="8">短语音</option>
          <option value="2">激光</option>
          <option value="3">水滴</option>
          <option value="4">手机</option>
          <option value="5">电话</option>
          <option value="6">鸡叫</option>
          <option value="7">OICQ</option>
          <option selected="selected" value="0">无</option>
        </select>
        <span id="CUSTOM_CALL_SOUND">
          <span id="changeSound">
            <a href="javascript:playMySound();">播放</a>
            <a href="javascript:deleteSound();">更改提示音</a>
          </span>
          <span id="uploadSound">
            <input type="file" name="CUSTOM_SOUND" id="CUSTOM_SOUND" class="BigInput" size="20">&nbsp;
            <font color="#FF0000">仅限Flash文件(swf格式)</font>
          </span>
        </span>
        <div align="right" id="sms_sound"></div>
      </td>
    </tr>
   <tr id="WEATHER0" style="display:none;">
    <td colspan=2 class="TableHeader">天气预报</td>
   </tr>
   <tr id="WEATHER1" style="display:none;">
    <td nowrap class="TableData">是否显示：</td>
    <td nowrap class="TableData">
        <input type="checkbox" name="SHOW_WEATHER" id="SHOW_WEATHER" checked="checked" onClick="this.checked?$('area_select').show():$('area_select').hide()">
        <label for="SHOW_WEATHER">显示天气预报</label>
    </td>
   </tr>
   <tr id="area_select" style="" class="WEATHER">
    <td nowrap class="TableData">默认城市：</td>
    <td nowrap class="TableData">
  <select id="province" name="PROVINCE" onChange="Province_onchange(this.options.selectedIndex);">
    <option value="选择省">选择省</option>
  </select>
  <select id="WEATHER_CITY" name="WEATHER_CITY" >
    <option value="0">选择城市</option>
  </select>
    </td>
   </tr>
<!--
   <tr id="RSS0">
    <td colspan=2 class="TableHeader">今日资讯</td>
   </tr>
   <tr id="RSS1">
    <td nowrap class="TableData">是否显示：</td>
    <td nowrap class="TableData">
        <input type="checkbox" name="SHOW_RSS" id="SHOW_RSS">
        <label for="SHOW_RSS">显示今日资讯</label>
    </td>
   </tr>
 -->
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存设置并应用" class="BigButtonC" onclick="submitForm()">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>

<br>

</body>
</html>