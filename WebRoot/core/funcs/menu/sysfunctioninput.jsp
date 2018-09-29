<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  request.setCharacterEncoding("UTF-8");
  String seqId = request.getParameter("seqId");
  if(seqId == null) {
    seqId = "";
  }
  String menuName = request.getParameter("menuName");
  if(menuName == null) {
    menuName = "";
  }
  String menuId = request.getParameter("menuId");  
  if(menuId == null) {
    menuId = "";
  }else{
    menuId = menuId.substring(0 , 2);  
  }
  String menuId4 = request.getParameter("menuIdStr");
  if(menuId4 == null) {
    menuId4 = "";
  }else{
    menuId4 = request.getParameter("menuIdStr").substring(0, 4);
  }
  String menuIdStrOld = request.getParameter("menuIdStr");
  if(menuIdStrOld == null) {
    menuIdStrOld = "";
  }
  String menuIdStr = request.getParameter("menuIdStr");
  if(menuIdStr == null) {
    menuIdStr = "";
  }
  String flag = request.getParameter("flag");
  if(flag == null) {
    flag = "";
  }
  String funcName = request.getParameter("funcName");
  if(funcName == null) {
    funcName = "";
  }
  String funcCode = request.getParameter("funcCode");
  if(funcCode == null) {
    funcCode = "";
  }
  String menuIdCode = request.getParameter("menuIdCode");
  if(menuIdCode == null) {
    menuIdCode = "";
  }
  String menuFlag = request.getParameter("menuFlag");
  if(menuFlag == null) {
    menuFlag = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单新建</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>" ;
var menuIdVar="<%=menuId %>";
var menuId4 = "<%=menuId4%>" ;
var menuName = "<%=menuName%>";
var funcName = "<%=funcName%>";
var funcCode = "<%=funcCode%>";
var menuIdCode = "<%=menuIdCode%>";
var menuIdStr = "<%=menuIdStr%>";
var menuFlag = "<%=menuFlag%>";
var flag = "<%=flag%>";
var menuIdStrOld = "<%=menuIdStrOld%>";
var menuIdOverAll = "";
var menuSortOldFunc = "";

function doInit() {
  $("menuIdOld").value = "<%=menuId %>";
  if(seqId){ 
    var url = "<%=contextPath%>/yh/core/funcs/menu/act/YHSysMenuAct/getSysFunction.act";
    var rtJson = getJsonRs(url, "tableNo=11112&seqId=" + seqId);
    if (rtJson.rtState == "0") {
      document.getElementById("seqId").value = rtJson.rtData.seqId;
      var menuId = rtJson.rtData.menuId;
      $("menuIdAll").value = menuId
      menuIdOverAll = menuId;
      document.getElementById("menuIdDesc").value = menuName;
      var menuSort = document.getElementById("menuSort");
      var menuSortOld = document.getElementById("menuSortOld");
      var menuAdd = document.getElementById("menuAdd");
      if(menuId.length == 4) {
        $("menuId").value = menuId.substring(0,2);
        menuSort.value = menuId.substring(2);
        menuSortOld.value = menuId.substring(2);
        $("menuLast").value = menuId.substring(0,2);
      }else {
        $("menuId").value = menuId.substring(0,4);
        $("menuLast").value = menuId.substring(0,4);
        menuAdd.value = menuId.substring(2,4);
        menuSort.value = menuId.substring(4,6);
        menuSortOld.value = menuId.substring(4,6);
      }
      document.getElementById("funcName").value = rtJson.rtData.funcName;
      document.getElementById("funcCode").value = rtJson.rtData.funcCode;
      document.getElementById("icon").value = rtJson.rtData.icon;
    }else {
      alert(rtJson.rtMsrg); 
    }   
  }
  if(menuFlag == "2"){
    document.getElementById("menuId").value = menuIdVar;  
    document.getElementById("menuIdDesc").innerHTML = menuName;
  }
  //document.getElementById("menuIdDesc").innerHTML = menuName;
  //document.getElementById("menuId").value = menuId;  
  if(flag == "1"){
    $("funcName").value = funcName;
    $("funcCode").value = funcCode;
    $("menuId").value = menuIdCode;
    bindDesc([{cntrlId:"menuId", dsDef:"SYS_MENU,MENU_ID,MENU_NAME"}
    	]);
  }
}

function checkStr(str){ 
  var re=/[\\"']/; 
  return str.match(re); 
}

function check() {
  var cntrl = document.getElementById("menuIdDesc");
  if(!cntrl.value) {
    alert("上级菜单不能为空！");
	return false;
  }
  
  cntrl = document.getElementById("menuSort");
  if(!cntrl.value.trim()) {
    alert("子菜单项代码不能为空！");
    cntrl.focus();
	return false;
  }
  
  cntrl = document.getElementById("funcName");
  if(!cntrl.value.trim()) {
    alert("子菜单名称不能为空！");
    cntrl.focus();
	return false;
  }

  if(checkStr($("funcName").value)){
    alert("子菜单名称含有'双引号'、'单引号 '或者 '\\' 请从新填写");
    $('funcName').focus();
    $('funcName').select();
    return false;
  }

  cntrl = document.getElementById("menuSort");
  if(!cntrl.value.trim() || cntrl.value.length != 2) {
    alert("子菜单代码应为2位！");
    cntrl.focus();
	return false;
  }
  return true;
}

function commitItem(){
  if(!check()) {
    return;
  }
  var url = null;
  var rtJson = null;
  menuIdOld = menuIdVar;
  menuId = document.getElementById("menuId").value;
  var menuSort = $('menuSort').value;
  var menuIdOld = $("menuIdOld").value;
  var icon = $("icon").value;
  //alert(menuIdOverAll);
  //alert(menuIdStr);
  //alert(menuId);
  if (seqId) {
    var menuSortOld = $("menuSortOld").value;
    var menuLast = $("menuLast").value;
    var check3 =$("menuIdAll").value;  //用于判断是否是3级目录

    url = "<%=contextPath%>/yh/core/funcs/menu/act/YHSysMenuAct/updateSysFunction.act?menuIdStr="+menuIdStr+"&seqId="+seqId+"&menuSort="+menuSort+"&menuId4="+menuId4+"&menuId="+menuId+"&menuSortOld="+menuSortOld+"&menuIdOld="+menuIdOld+"&menuLast="+menuLast+"&menuIdOverAll="+menuIdOverAll+"&icon="+icon;
    rtJson = getJsonRs(url, mergeQueryString($("functionInfoForm")));
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      if(rtJson.rtMsrg == "成功修改子菜单项") {
        document.getElementById("menuSortOld").value = document.getElementById("menuSort").value ;
      }
      //parent.navigateFrame.location.reload();
      location = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/listSysFunction.act?menuId="+menuIdOld;
    }else {
      alert(rtJson.rtMsrg); 
      $("menuIdDesc").select();
      $("menuIdDesc").focus();
    }     
  }else {
    url = "<%=contextPath%>/yh/core/funcs/menu/act/YHSysMenuAct/addSysFunction.act";
    rtJson = getJsonRs(url, mergeQueryString($("functionInfoForm")));
    if (rtJson.rtState == "0") {
      //alert(rtJson.rtMsrg);
      document.getElementById("menuId").value = menuId;
      location = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/listSysFunction.act?menuId="+menuId;
      //$("functionInfoForm").reset();
      //document.getElementById("menuId").focus();
    }else{
      alert(rtJson.rtMsrg);
      $("menuSort").select();
      $("menuSort").focus();
    }
  }
}

function goback() {
  window.location.href = "<%=contextPath %>/yh/core/funcs/menu/act/YHSysMenuAct/listSysFunction.act?menuId=" + menuId ;
}

function selectFatherNode(){
  var URL = "<%=contextPath %>/core/funcs/menu/menuIframe.jsp";
  openDialogResize(URL, 300, 400);
  //window.open(URL, "", "width=300,height=400,toolbar =no, menubar=no,resizable=yes,top=100,left=300");
}

function clearFatherNode() {
  document.getElementById("menuId").value = "";
  document.getElementById("menuIdDesc").value = "";
}
</script>
</head>
<body onload="doInit()">
<form name="functionInfoForm" id="functionInfoForm" method="post">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big">
  <%
    if(seqId.equals("")) {
  %>   
    <img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img><span class="big3">&nbsp;添加子菜单项</span> 
  <%
    }else {
  %>    
    <img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img><span class="big3">&nbsp;修改子菜单项</span>
  <%
    }
  %>
  </td>
 </tr>
 </table>
  <input type="hidden" name="seqId" id="seqId" value="" />
   <input type="hidden" name="menuIdOld" id="menuIdOld" value="" />
   <input type="hidden" name="menuIdAll" id="menuIdAll" value="" />
   <input type="hidden" name="menuSortOld" id="menuSortOld" value="" />
   <input type="hidden" name="menuAdd" id="menuAdd" value=""/>
   <input type="hidden" name="menuLast" id="menuLast" value=""/>
   <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.menu.data.YHSysFunction"/>
    
  <table cellscpacing="1" cellpadding="3" width="500" align="center" class="TableList">
   <tr class="TableLine1">
    <td width="250">上级菜单：</td>
    <td>
      <font style="color:red">*</font>
      <textarea cols="20" id="menuIdDesc" name="menuIdDesc"  rows="1" class="SmallStatic" readonly></textarea>
      <input type="hidden" id="menuId" name="menuId" class="BigInput" size="25" maxlength="25" value="">
      &nbsp;&nbsp;&nbsp;<a href="javascript:;" onClick="selectFatherNode()">添加</a>
    </td>   
   </tr>
   
   <tr class="TableLine2">
    <td>子菜单项代码：</td>
    <td>
      <font style="color:red">*</font>
      <input type="text" id="menuSort" name="menuSort" class="BigInput" size="20" maxlength="2" value=""
        onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
      ><br/>
               说明：此代码为两位，作为排序之用。在同一父菜单下的平级菜单，该代码不能重复
    </td>
   </tr>
   
   <tr class="TableLine1">
    <td>子菜单名称：</td>
    <td>
      <font style="color:red">*</font>
      <input type="text" id="funcName" name="funcName" class="BigInput" size="20" maxlength="50" value="">
    </td>
   </tr>
   <tr class="TableLine1">
    <td>图片名：</td>
    <td>
      &nbsp;&nbsp;<input type="text" id="icon" name="icon" class="BigInput" size="20" value="">
    </td>
   </tr>
   <tr class="TableLine2">
    <td>子菜单模块路径：</td>
    <td>
      &nbsp;&nbsp;<input type="text" id="funcCode" name="funcCode" class="BigInput" size="40" value="">
    </td>
   </tr>
   
   <tr class="TableLine1">
    <td colspan="2">
      <b>说明：子菜单模块路径定义方式，应根据此菜单项的类型决定</b>：<br/><br/>
      <b>类型一，此菜单挂接OA系统中的模块：</b>则填写程序路径，格式如：email，则实际对应的路径是：OA安装目录/webroot/yh/email，对应的图片是：OA安装目录/webroot/yh/core/styles/imgs/menuIcon/email.gif<br/><br/>
      <b>类型二，此菜单挂接外部的B/S结构的系统：</b>则填写该系统网址，格式如：<%=StaticData.YH_WEB_SITE%>，注意：请一定要以http://开头<br/><br/>
   </td>
  </tr> 
  <tr class="TableLine2">
    <td colspan="2" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
      <input type="button" value="返回" class="SmallButton" onclick="window.history.back()">
    </td>
  </tr>
 </table>
 </form>
</body>
</html>