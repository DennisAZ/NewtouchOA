<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统信息</title>
<link rel="stylesheet"  href="<%=cssPath%>/style.css">
<script type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit() {
  var version;
  var url = '<%=contextPath%>/yh/core/funcs/system/info/act/YHInfoAct/getSystemInfo.act';
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    //$('serverInfo').innerHTML = data.serverInfo + " " + data.systemInfo;
    $('setupPath').innerHTML = data.setupPath;
    $('version').innerHTML = data.ver;
    version = data.ver;
    $('unitName').innerHTML = data.unitName;
    $('dbms').innerHTML = data.dbms;
    $('installTime').innerHTML = data.installTime;

    if (data.regUnitName) {
      var color = 'red';
      if (data.unitName == data.regUnitName) {
        color = 'black';
      }
      $('regUnitName').setStyle({
        'color': color
      });
      $('regUnitNameTr').show();
      var noteMsrg = "";
      if (data.unitName != data.regUnitName) {
        noteMsrg = "&nbsp;&nbsp;<提示：注册单位信息与目前系统单位信息不匹配>";
      }
	    $('regUnitName').innerHTML = data.regUnitName + noteMsrg;
     
    }

    if (data.regmachineCode) {
      var color = 'red';
      if (data.regmachineCode == data.machineCode) {
        color = 'black';
      }
      $('regMachineCodeTr').show();
      $('regMachineCode').setStyle({
        'color': color
      });
      var noteMsrg = "";
      if (data.regmachineCode != data.machineCode) {
        noteMsrg = "&nbsp;&nbsp;<提示：注册机器码与系统机器码不匹配>";
      }
      $('regMachineCode').innerHTML = data.regmachineCode + noteMsrg;
    }
    
    $('machineCode').innerHTML = data.machineCode;
    if (data.serialId) {
      $('serialId').innerHTML = data.serialId;
      $('regOrNot').innerHTML = "已注册";
    }else {
      $('serialId').innerHTML = "尚未注册";
      $('regOrNot').innerHTML = "尚未注册";
    }
  }

  var url = '<%=contextPath%>/yh/core/funcs/system/info/act/YHInfoAct/detailRegInfo.act';
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    var verArray = (version || "").split('.');
    var ver;
    if (verArray.length > 2) {
      ver = verArray[2].substring(0, 2);
    }
    $('sysname').innerHTML = (data['sysname.yh'] || '<%=StaticData.SOFTTITLE_SHORT%>数据交换平台') + '&nbsp;20' + ver + '版';
  }else {
    var verArray = (version || "").split('.');
    var ver;
    if (verArray.length > 2) {
      ver = verArray[2].substring(0, 2);
    }
    $('sysname').innerHTML = '<%=StaticData.SOFTTITLE_SHORT%>数据交换平台' + '&nbsp;1.0' + ver + '版';
  }
}

function reloadRegInfo() {
  var url = '<%=contextPath%>/yh/core/funcs/system/info/act/YHInfoAct/reloadRegInfo.act';
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    alert("重新加载注册信息成功!");
  }
  else{
    alert(rtJson.rtMsrg);
    window.location.reload();
  }
}

function submitForm() {
  if ($('file').value) {
	  $('form1').submit();
  }
  else {
    alert('请上传注册文件');
  }
}
</script>
</head>

<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>

    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">系统信息</span>
    </td>
  </tr>
</table>

<form action="<%=contextPath %>/yh/core/funcs/system/info/act/YHInfoAct/reg.act" enctype="multipart/form-data" id="form1" method="post">
<br>
<table class="TableBlock" width="80%" align="center" cellpadding="5">
  <tr class="TableLine2">
    <td nowrap class="TableHeader1" colspan="2" align="center" onclick="alert('通 -达 -科 -技')"><b>系统信息</b></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap  width="150"><b>&nbsp;软件名称：</b></td>
    <td nowrap id="sysname"><%=StaticData.SOFTTITLE_SHORT%>数据交换平台</td>
  </tr>
  <tr class="TableLine2">
    <td nowrap  width="150"><b>&nbsp;内部版本号：</b></td>
    <td nowrap ><span id="version">1.0</span>&nbsp;
    </td>
  </tr>
  <tr class="TableLine2">
    <td nowrap  width="150"><b>&nbsp;版权所有：</b></td>
    <td nowrap ><%=StaticData.SOFTCOMPANY%><a href="<%=StaticData.YH_WEB_SITE%>" target="_blank"><%=StaticData.YH_WEB_SITE%></a></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap width="150"><b>&nbsp;数据库软件：</b></td>
    <td nowrap id="dbms"></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap width="150"><b>&nbsp;软件安装路径：</b></td>
    <td nowrap id="setupPath"></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap width="150"><b>&nbsp;软件安装时间：</b></td>
    <td nowrap id="installTime"></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap  width="150"><b>&nbsp;用户单位：</b></td>
    <td nowrap  id="unitName"></td>
  </tr>
  <tr class="TableLine2" id="regUnitNameTr" style="display:none;">
    <td nowrap width="150"><b>&nbsp;注册信息用户单位：</b></td>
    <td nowrap id="regUnitName"></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap width="150"><b>&nbsp;软件注册：</b></td>
    <td nowrap id="regOrNot"></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap width="150"><b>&nbsp;机器码：</b></td>
    <td id="machineCode" nowrap></td>
  </tr>
  <tr class="TableLine2" id="regMachineCodeTr" style="display:none;">
    <td nowrap width="150"><b>&nbsp;注册信息机器码：</b></td>
    <td id="regMachineCode" nowrap></td>
  </tr>
  <tr class="TableLine2">
    <td nowrap width="150"><b>&nbsp;系统标识码：</b></td>
    <td id="serialId" nowrap></td>
  </tr>
  <tr class="TableLine2" id="uploadFile">
    <td nowrap width="150"><b>&nbsp;上传注册文件：</b></td>
    <td nowrap>
      <input type="file" id="file" name="file">
      &nbsp;<input type="button" onclick="submitForm()" class="BigButton" value="注册确认">
      &nbsp;<a href="<%=contextPath%>/core/esb/server/sysinfo/detail.jsp">查看注册文件</a>
      &nbsp;<a href="javascript:void(0)" onclick="reloadRegInfo();return false;">重新加载注册信息</a>
    </td>
  </tr>
</table>
 </form>
</body>
</html>