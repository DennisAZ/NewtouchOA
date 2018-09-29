<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String type = request.getParameter("type");
  if (type == null){
    type = "";
  }
  String id = request.getParameter("id");
  if (id == null){
    id = "";
  }
%>
<head>
<title>菜单定义指南</title>
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
var type = "<%=type%>";
var id = "<%=id%>";
var flowName ="";
function doInit(){
  if(type == "FLOW"){
    $("moduleName").innerHTML = "工作流";
    var url = "<%=contextPath%>/yh/core/module/menucode/act/YHMenuCodeAct/getFlowName.act";
    var rtJson = getJsonRs(url, "seqId="+id);
    if (rtJson.rtState == "0") {
      flowName = rtJson.rtData;
    } else {
      alert(rtJson.rtMsrg); 
    }
    var count = 0;
    var radioDiv = $("radioDiv");
    var radioDiv1 = $("radioDiv1");
    var radioDiv2 = $("radioDiv2");
    var radioDiv3 = $("radioDiv3");
    var radioDiv4 = $("radioDiv4");
    var menuArry = new Array("新建工作", "待办工作", "工作查询", "工作监控"); 
    radioDiv.update("&nbsp;&nbsp;<input type='radio' id='menuItem1' name='menuItem' value='menuItem1' checked><label for='menuItem1'>"+flowName+"</label>&nbsp;&nbsp;&nbsp;(二级菜单)");
    radioDiv1.update("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;<input type='radio' id='menuItem_0' name='menuItem' value='0'><label for='menuItem_0'>新建工作</label>&nbsp;&nbsp;&nbsp;(三级菜单)");
    radioDiv2.update("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<input type='radio' id='menuItem_1' name='menuItem' value='1'><label for='menuItem_1'>待办工作</label>&nbsp;&nbsp;&nbsp;(三级菜单)");
    radioDiv3.update("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<input type='radio' id='menuItem_2' name='menuItem' value='2'><label for='menuItem_2'>工作查询</label>&nbsp;&nbsp;&nbsp;(三级菜单)");
    radioDiv4.update("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp<input type='radio' id='menuItem_3' name='menuItem' value='3'><label for='menuItem_3'>工作监控</label>&nbsp;&nbsp;&nbsp;(三级菜单)");
  }else if(type == "FILE_SORT"){
    var sortName = "";
    $("moduleName").innerHTML = "公共文件柜";
    var url = "<%=contextPath%>/yh/core/module/menucode/act/YHMenuCodeAct/getSortName.act";
    var rtJson = getJsonRs(url, "seqId="+id);
    if (rtJson.rtState == "0") {
      sortName = rtJson.rtData;
    } else {
      alert(rtJson.rtMsrg); 
    }
    var radioDiv = $("radioDiv");
    radioDiv.update("<input type='radio'id='menuItem' value='' checked>"+sortName+" (二级或三级菜单)");
    $("funcName").value = sortName;
    $("funcCode").value = "/core/funcs/filefolder/index.jsp?sortId="+id;
    $("menuId").value = 12;
  }else if(type == "NETDISK"){
    var diskName = "";
    $("moduleName").innerHTML = "网络硬盘";
    var url = "<%=contextPath%>/yh/core/module/menucode/act/YHMenuCodeAct/getDiskName.act";
    var rtJson = getJsonRs(url, "seqId="+id);
    if (rtJson.rtState == "0") {
      diskName = rtJson.rtData;
    } else {
      alert(rtJson.rtMsrg); 
    }
    var radioDiv = $("radioDiv");
    radioDiv.update("<input type='radio'id='menuItem' value='' checked>" + diskName + " (二级或三级菜单)");
    $("funcName").value = diskName;
    $("funcCode").value = "/core/funcs/netdisk/index.jsp?diskId="+id;
    $("menuId").value = 12;

  }else if(type == "PICTURE"){
    var picName = "";
    $("moduleName").innerHTML = "图片浏览";
    var url = "<%=contextPath%>/yh/core/module/menucode/act/YHMenuCodeAct/getPicName.act";
    var rtJson = getJsonRs(url, "seqId="+id);
    if (rtJson.rtState == "0") {
      picName = rtJson.rtData;
    } else {
      alert(rtJson.rtMsrg); 
    }
    var radioDiv = $("radioDiv");
    radioDiv.update("<input type='radio'id='menuItem' value='' checked>" + picName + " (二级或三级菜单)");
    $("funcName").value = picName;
    $("funcCode").value = "/core/funcs/picture/index.jsp?picId="+id;
    $("menuId").value = 12;
  }else if(type == "CONFIDENTIAL"){
    var sortFileName = "";
    $("moduleName").innerHTML = "机要文件";
    var url = "<%=contextPath%>/yh/core/module/menucode/act/YHMenuCodeAct/getConfidentialFile.act";
    var rtJson = getJsonRs(url, "seqId="+id);
    if (rtJson.rtState == "0") {
      sortFileName = rtJson.rtData;
    } else {
      alert(rtJson.rtMsrg); 
    }
    var radioDiv = $("radioDiv");
    radioDiv.update("<input type='radio'id='menuItem' value='' checked>" + sortFileName + " (二级或三级菜单)");
    $("funcName").value = sortFileName;
    $("funcCode").value = "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/index.jsp?sortId="+id;
    $("menuId").value = 12;
  }
}

function nexts(){
  if(type == "FLOW"){
    if($("menuItem1").checked == true){
      $("funcName").value = flowName;
      $("funcCode").value = "/core/funcs/workflow/flowrun/list/index.jsp?type="+id;
      $("menuId").value = 12;
    }
    if($("menuItem_0").checked == true){ 
      $("funcName").value = "新建工作";
      
      $("funcCode").value = "/core/funcs/workflow/flowrun/new/edit.jsp?isMenu=1&flowId="+id;
      $("menuId").value = 12;
    }
    if($("menuItem_1").checked == true){
      $("funcName").value = "待办工作";
      $("funcCode").value = "/core/funcs/workflow/flowrun/list/index.jsp?flowId="+id;
      $("menuId").value = 12;
    }
    if($("menuItem_2").checked == true){
      $("funcName").value = "工作查询";
      $("funcCode").value = "/core/funcs/workflow/flowrun/query/index.jsp?flowId="+id;
      $("menuId").value = 12;
    }
    if($("menuItem_3").checked == true){
      $("funcName").value = "工作监控";
      $("funcCode").value = "/core/funcs/workflow/flowrun/manage/index.jsp?flowId="+id;
      $("menuId").value = 12;
    }
  }
  var funcName = $("funcName").value;
  var funcCode = $("funcCode").value;
  var menuId = $("menuId").value;
  var flag = "1";
  location = contextPath + "/core/funcs/menu/sysfunctioninput.jsp?funcName="+encodeURIComponent(funcName)+"&funcCode="+encodeURIComponent(funcCode)+"&flag="+flag+"&menuIdCode="+menuId;
  //openDialog(URL,'600', '350');
}

function menuCode(type, id){
  var title = "菜单定义指南";
  var URL = contextPath + "/core/module/menucode/index.jsp?type=" + type + "&id=" + id;
  showModalWindow(URL , title , "menuWindow" ,600,350);
}

</script>
</head>
<body onload="doInit();" topmargin="5">
<div class="Big1"><img border=0 src="<%=imgPath %>/system.gif" align="absmiddle"> <b>新建菜单</b></div>
<br>
<form name="form1" method="post" id="form1">
<table  class="TableBlock" align="center" width="400">
  <tr class="TableHeader">
    <td><span id="moduleName"></span></td>
  </tr>
  <tr class="TableData" height="30">
    <td>
    <div id="radioDiv"></div>
    <div id="radioDiv1"></div>
    <div id="radioDiv2"></div>
    <div id="radioDiv3"></div>
    <div id="radioDiv4"></div>
    </td>
  </tr>
  <tr class="TableControl" align="center">
    <td>
      <input type="hidden"  value="" name="menuId" id="menuId">
      <input type="hidden"  value="" name="funcName" id="funcName">
      <input type="hidden"  value="" name="funcCode" id="funcCode">
      <input type="button"  value="下一步" class="BigButton" onClick="nexts();">&nbsp;&nbsp;
      <input type="button"  value="关闭" class="BigButton" onClick="closeModalWindow('menuWindow');">
    </td>
  </tr>
  </table>
</form>
</body>
</html>