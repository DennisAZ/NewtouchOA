<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/setup/js/setup.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
function doSubmit(){
  var form = "from1";
  if(!checkDbConn()){
    alert("数据库无法连接，请配置数据库参数!");
    $("userName").select();
    return;
  }
  var url = imgPath + "/cmp/email/inbox_sending.gif";
  var temp = "<img src=\"" + url + "\" align=\"absmiddle\"> 已提交服务器，正在安装...";
  if($("erp").checked){
    installErp(form ,temp);
  }else{
    isErpInstalled = true;
  }
  if($("fis").checked){
    installFisAsyn(form ,temp);
  }else{
    isFisInstalled = true;
  }
  if($("ea").checked){
    installEaAsyn(form  ,temp);
  }else{
    isEaInstalled = true;
  }
}

function doInit(){
  var obj = findNotInstallSys();
  if(obj.erpInstall == 1){
    //已经安装，已经配置
    $("erp").style.display = "none";
    $("erpInfo").color = "green";
    $("erpInstallDesc").innerHTML = "<font color=\"green\">已安装</font>";
  }else if(obj.erpInstall == 2){
    //没有安装
    $("erp").disabled = true;
    $("erpInfo").color = "red";
    $("erpInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
    
  }else if(obj.erpInstall == 0){
    //已经安装，没有配置
    $("erp").checked = true;
    $("erp").disabled = true;
    $("erpInfo").color = "black";
    $("erpInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
  }
  
  if(obj.eaInstall == 1){
    //已经安装，已经配置
    if(obj.erpInstall == 2){
      $("ea").disabled = true;
      $("eaInfo").color = "red";
      $("eaInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
    }else{
      $("ea").style.display = "none";
      $("eaInfo").color = "green";
      $("eaInstallDesc").innerHTML = "<font color=\"green\">已安装</font>";
      
    }
  }else if(obj.eaInstall == 2){
    //没有安装
    $("ea").disabled = true;
    $("eaInfo").color = "red";
    $("eaInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
  }else if(obj.eaInstall == 0){
    //已经安装，没有配置
    if(obj.erpInstall == 2){
      $("ea").disabled = true;
      $("eaInfo").color = "red";
      $("eaInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
    }else{
      $("eaInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
    }
  }
  
  if(obj.fisInstall == 1){
    //已经安装，已经配置
    if(obj.erpInstall == 2){
      $("fis").disabled = true;
      $("fisInfo").color = "red";
      $("fisInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
      
    }else{
      $("fis").style.display = "none";
      $("fisInfo").color = "green";
      $("fisInstallDesc").innerHTML = "<font color=\"green\">已安装</font>";
    }
  }else if(obj.fisInstall == 2){
    //没有安装
    $("fis").disabled = true;
    $("fisInfo").color = "red";
    $("fisInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
  }else if(obj.fisInstall == 0){
    //已经安装，没有配置
    if(obj.erpInstall == 2){
      $("fis").disabled = true;
      $("fisInfo").color = "red";
      $("fisInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
    }else{
      $("fisInstallDesc").innerHTML = "<font color=\"red\">未安装</font>";
    }
  }
}
function checkDbConn(){
  var url = contextPath + "/yh/setup/act/YHSetupUtilAct/testDbConn.act";
  var rtJson = getJsonRs(url,$("from1").serialize());
  if(rtJson.rtData.testRt == "1"){
   return true;
  }else{
   return false;
  }
}
/**
 * 
 */
function testDb(){
  var url = contextPath + "/yh/setup/act/YHSetupUtilAct/testDbConn.act";
  var rtJson = getJsonRs(url,$("from1").serialize());
  if(rtJson.rtState == "0"){
    if(rtJson.rtData.testRt == "1"){
      alert("测试数据连接成功!");
     }else{
       alert("测试数据连接失败!\n" + "请查看MSSQLSERVER数据库服务是否启动，或者 请检查用户名密码是否正确!");
     }
  }else{
    alert("测试数据连接失败!" + rtJson.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit()">
<table style="padding-left:15px" border="0" width="95%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/task.gif" align="absmiddle"><span class="big3">YH进销存与财务数据库参数配置(MSSQLSERVER)</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" class="BigButtonW" value="测试数据库连接" onclick="testDb()">
    </td>
    <td><input type="button" class="BigButton" value="返回" onclick="location='../index.jsp'"></td>
  </tr>
</table>
<form id="from1" name = "from1">
<table class="TableList" width="95%" align="center" >
<tr class="TableLine1">
    <td>数据库驱动:</td>
    <td>
    com.microsoft.jdbc.sqlserver.SQLServerDriver
    <input type="hidden" id="driver" name="driver" class="SmallInput" maxlength="5" value="com.microsoft.jdbc.sqlserver.SQLServerDriver"/>
    </td>
  </tr>
  <tr class="TableLine2">
    <td>数据库地址:</td>
    <td>
    <input type="text" id="conIp" name="conIp"  class="SmallInput"  value="localhost"/> 端口号: <input type="text" id="conPort" name="conPort"  class="SmallInput"  value="1433"/>
   
<input type="hidden" id="conurl" name="conurl"  class="SmallInput"  value="localhost"/> 
</td>
  </tr>
    <tr class="TableLine1">
    <td>数据库登录名称:</td>
    <td>
    <input type="text" id="userName" name="userName" class="SmallInput"  value=""/>
    </td>
  </tr>
    <tr class="TableLine2">
    <td>数据库登录密码:</td>
    <td>
    <input type="password" id="passward" name="passward" class="SmallInput"  value=""/>
    </td>
  </tr>
</table>
</form>

<br>
<table style="padding-left:15px" border="0" width="95%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/task.gif" align="absmiddle"><span class="big3">YH进销存与财务数据库安装操作及状况</span>&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="button" class="BigButtonW" value="安装YHERP子系统" onclick="doSubmit()">
    </td>
  </tr>
</table>
<table class="TableList" width="95%" align="center">
  <thead class="TableHeader" align="center">
  <td width="50px">选择</td><td width="200px" >系统名称</td><td>系统安装情况</td>
  </thead>
  <tr class="TableLine1">
   <td><input type="checkbox" id="erp" value="1"></input></td><td align="center"><font id="erpInfo" color="black">YHERP运行平台</font></td><td id="erpInstallDesc" align="center"></td>
  </tr>
   <tr class="TableLine2">
   <td><input type="checkbox" id="fis" value="1"></input></td><td align="center"><font id="fisInfo" color="black">YH财务系统</font></td><td id="fisInstallDesc" align="center"></td>
  </tr>
   <tr class="TableLine1">
   <td><input type="checkbox" id="ea" value="1"></input></td><td align="center"><font id="eaInfo" color="black">YH进销存系统</font></td><td  id="eaInstallDesc" align="center"></td>
  </tr>
</table>
<br>
</body>
</html>