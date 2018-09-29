<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>zTree</title>
<script type="text/javascript" src="../../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../../js/ui/jquery-ui-1.8.17.custom.min.js"></script>
<link href="css/normalize.css" rel="stylesheet"/>
<link href="css/idealforms/idealforms.css" rel="stylesheet"/>
<link href="css/master.css" rel="stylesheet" media="screen"/>
</head>
<body>
	<form action="<%=contextPath %>/yh/core/funcs/setdescktop/theme/act/YHThemeAct/setTheme.act"  method="post" name="form1" id="myform" enctype="multipart/form-data">
<table class="TableTop" style="width: 80%">
  <tr>
    <td class="left">
    </td>
    <td class="center">
    界面主题与菜单

    </td>
    <td class="right">
    </td>
  </tr>
</table>
<table class="TableBlock no-top-border" width="80%" align="left">
    <tr id="themeSel">
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
    <tr>
      <td nowrap class="TableData" width=20%>WebOS动画效果：</td>
      <td class="TableData">
        <select name="FX" id="FX">
          <option value="1">启用</option>
          <option value="0">禁用</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=20%>界面切换：</td>
      <td class="TableData">
        &nbsp;&nbsp;<input type="button" value="切换为 WebOS" class="BigButtonC" onclick="changeHome(0)">&nbsp;&nbsp;
        <input type="button" value="切换为经典界面" class="BigButtonC" onclick="changeHome(1)">
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
   <tr id="WEATHER0">
    <td colspan=2 class="TableHeader">天气预报</td>
   </tr>
   <tr id="WEATHER1">
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

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="js/jquery.idealforms.js"></script>
<script src="js/scripts.js"></script>
</body>
</html>