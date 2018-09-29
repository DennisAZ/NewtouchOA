<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>菜单设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";

function doInit(){
  //getMenuPara();
  //getMenuDisplay();
  var urlSin = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/getMenuExpandSingle.act";
  var rtJsonSin = getJsonRs(urlSin);
  if (rtJsonSin.rtState == "0") {
    //bindJson2Cntrl(rtJsonSin.rtData);
    if(rtJsonSin.rtData[0].paraValue == 1){
      document.getElementById("MENU_EXPAND_SINGLE").checked = true;
    }
  }else {
    alert(rtJsonSin.rtMsrg); 
  }
  
  
}
function getMenuDisplay(){
  var urlDis = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/getMenuDisplay.act";
  var rtJsonDis = getJsonRs(urlDis);
  if (rtJsonDis.rtState == "0") {
    bindJson2Cntrl(rtJsonDis.rtData);
    var display = rtJsonDis.rtData[0].paraValue.split(',');
    for(var i = 0; i < display.length - 1; i++){
      var strs = display[i];
      if(strs == "SHORTCUT"){
        document.getElementById("MENU_SHORTCUT").checked = true;
      }else if(strs == "WINEXE"){
        document.getElementById("MENU_WINEXE").checked = true;
      }else if(strs == "URL"){
        document.getElementById("MENU_URL").checked = true;
      }
    }
  }else {
    alert(rtJsonDis.rtMsrg); 
  }
}

function getMenuPara(){
  var url = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/getMenuPara.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    //bindJson2Cntrl(rtJson.rtData);
    $("TOP_MENU_NUM").value = rtJson.rtData.paraValue;
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function updateTopMenuNum(){
  var url = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/updateTopMenuNum.act";
  var rtJson = getJsonRs(url, "topMenuNum="+topMenuNum);
  if (rtJson.rtState == "0") {
    location = "<%=contextPath %>/core/funcs/menu/parasubmit.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function commit(){
  var reg = /^[0-9]*$/;
  //var topMenuNum = document.getElementById("TOP_MENU_NUM").value;
  //if(!reg.test(topMenuNum)){
  //  alert("错误 ,界面顶部一级菜单每行显示个数应为整数！");
  //  $("TOP_MENU_NUM").focus();
  //  $("TOP_MENU_NUM").select();
  //  return false;
  //}
  //updateTopMenuNum();
  var sumStrs = "";
  var menuShorcut = ""; //document.getElementById("MENU_SHORTCUT").checked;
  var menuWinexe = "";//document.getElementById("MENU_WINEXE").checked;
  var menuUrl = "";//document.getElementById("MENU_URL").checked;
  var menuExpandSingle = document.getElementById("MENU_EXPAND_SINGLE").checked;
  var urls = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/updateMenuNavigation.act";
  var rtJsons = getJsonRs(urls, "menuShorcut="+menuShorcut+"&menuWinexe="+menuWinexe+"&menuUrl="+menuUrl+"&menuExpandSingle="+menuExpandSingle);
  if (rtJsons.rtState == "0") {
    location = "<%=contextPath %>/core/funcs/menu/parasubmit.jsp";
  }else {
    alert(rtJsons.rtMsrg); 
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3">&nbsp;菜单设置</span>
    </td>
  </tr>
</table>
<br>
<div align="center">

<form name="form1" method="post" id="form1">
<table class="TableList" width="95%">
  <tr class="TableHeader" align="center">
    <td width="150">参数</td>
    <td>数值</td>

    <td width="300">备注</td>
  </tr>
  <!-- 
  <tr class="TableData" align="center" height="30">
    <td width="150"><b>快捷菜单栏<br>每行显示个数</b></td>
    <td align="left">
       <input type="input" name="TOP_MENU_NUM" id="TOP_MENU_NUM" class="BigInput" value="" size="10">
    </td>

    <td width="300" align="left">
       快捷菜单栏在哪里？<br>
       在软件主界面上方，<img src="<%=imgPath%>/mytable.gif">桌面 左侧。该栏中的快捷菜单，可以由用户在自己的控制面板中设置，管理员也可以在系统管理->用户管理中批量设置<br>
       如果快捷菜单栏的菜单个数多于设置的每行显示个数，菜单会分多行显示
    </td>
  </tr>
   -->
  <tr class="TableData" align="center" height="30">
    <td><b>导航菜单</b></td>

    <td nowrap align="left">
       <!-- <input type="checkbox" name="MENU_SHORTCUT" id="MENU_SHORTCUT"><label for="MENU_SHORTCUT">显示菜单快捷组</label><br>-->
       <!-- <input type="checkbox" name="MENU_WINEXE" id="MENU_WINEXE"><label for="MENU_WINEXE">显示Windows快捷组</label><br>-->
       <!-- <input type="checkbox" name="MENU_URL" id="MENU_URL"><label for="MENU_URL">显示常用网址</label><br>-->
       <input type="checkbox" name="MENU_EXPAND_SINGLE" id="MENU_EXPAND_SINGLE"><label for="MENU_EXPAND_SINGLE">同时只能展开一个一级菜单</label><br>
    </td>
    <td align="left">

       左侧导航菜单是否显示菜单快捷组、Windows快捷组、常用网址等

    </td>
  </tr>
  <tr class="TableControl" align="center">
    <td colspan="3">
       <Input type="button" name="submit" class="BigButton" value="保存" OnClick="commit()">
    </td>
  </tr>
</table>
</form>

</div>

</body>
</html>