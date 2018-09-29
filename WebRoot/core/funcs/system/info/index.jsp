<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=StaticData.SOFTTITLE%></title>
<link rel="stylesheet"  href="<%=cssPath%>/style.css">
<script type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
var machineCode;
function doInit() {
  var version;
  var url = '<%=contextPath%>/yh/core/funcs/system/info/act/YHInfoAct/getSystemInfo.act';
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    $('userAmount').innerHTML = '允许登录用户' 
      + (data.userAmount * 1 - data.userAmountNotLogin * 1)
      + '，禁止登录用户' + data.userAmountNotLogin * 1 
      + '，合计' + data.userAmount * 1 
      + '（禁止登录用户不限制人数）';
    $('serverInfo').innerHTML = data.serverInfo + " " + data.systemInfo;
    $('setupPath').innerHTML = data.setupPath;

    var userAmount = data.regUserAmount;
    if (userAmount >= 100000) {
      userAmount = "不限人数";
    }
    //OA限制用户的数量
  

    var imAmount = data.regImUserAmount;
    if (imAmount >= 100000) {
      imAmount = "不限人数";
    }
    $('regImUserAmount').innerHTML = imAmount;
    $('version').innerHTML = data.ver;
    version = data.ver;
    $('port').innerHTML = data.port;
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

    machineCode = data.machineCode;
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
      $("remainDays").style.display = "none";
      $('serialId').innerHTML = data.serialId;
      $('regOrNot').innerHTML = "已注册";
    }else {
      //triaReg();
      var rd = $$("#remainDays span")[0];
      if (data.remainDays*1 <= 0) {
        rd.innerHTML = "<font style='color: red;'>" + data.remainDays + " 天</font>";
      }
      else {
	      rd.innerHTML = data.remainDays + " 天";
      }
      
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
    $('sysname').innerHTML = (data['sysname.yh'] || '<%=StaticData.SOFTTITLE%>') + '&nbsp;20' + ver + '版';
  }else {
    var verArray = (version || "").split('.');
    var ver;
    if (verArray.length > 2) {
      ver = verArray[2].substring(0, 2);
    }
    $('sysname').innerHTML = '<%=StaticData.SOFTTITLE%>' + '&nbsp;20' + ver + '版';
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
	  $('bbsForm').submit();
  }
  else {
    alert('请上传注册文件');
  }
}

function triaReg() {
  $("triaRegTable").show();
  $("triaRegLink").href = "<%=StaticData.YH_MACHINECODE_SITE%>" + machineCode;
}

</script>
<style>
#triaRegLink {
	background: url(images/tria_reg.png);
	width: 120px;
	height: 32px;
	display: block;
}

#triaRegLink:hover {
	background: url(images/tria_reg_hover.png);
}
</style>
</head>

<!-- <body onload="doInit();"> -->
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"><%=StaticData.SOFTTITLE%></span>
    </td>
  </tr>
</table>
<table id="triaRegTable" style="width: 80%;display: none;" align="center">
  <tr>
    <td class="">
			<a id="triaRegLink" class="" href="<%=StaticData.YH_HERO_SITE%>" target="_blank"><span> </span></a>
			&nbsp;&nbsp;&nbsp;&nbsp;参加试用登记的用户，均可获得售前技术支持服务，还可获赠试用期延长和用户数限制增加的注册文件。

    </td>
  </tr>
</table>
<form action="uploadLicense.jsp" enctype="multipart/form-data" id="bbsForm" name="bbsForm" method="post">

<br>
<table class="TableTop" style="width: 80%" align="center">
  <tr>
    <td class="left">
    </td>
    <td class="center" align="center">
    <%=StaticData.SOFTTITLE%>
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<table class="TableBlock no-top-border" width="80%" align="center">
  <tr class="TableData">
    <td nowrap  width="150"><b>&nbsp;软件名称：</b></td>
    <!-- <td nowrap id="sysname"> </td> -->
    <td nowrap>   <%=StaticData.VERSIONDESC%> </td>
  </tr>
  <tr class="TableData">
    <td nowrap  width="150"><b>&nbsp;内部版本号：</b></td>
    <!--<td nowrap ><span id="version"></span>&nbsp;
    </td>-->
    <td nowrap ><span>v2.0</span>&nbsp;
    </td>
  </tr>
  <tr class="TableData">
    <td nowrap  width="150"><b>&nbsp;版权所有：</b></td>
    <td nowrap ><%=StaticData.SOFTCOMPANY%> <a href="http://<%=StaticData.YH_OA_SITE%>" target="_blank">http://<%=StaticData.YH_OA_SITE%></a></td>
  </tr>
  <tr  class="TableData" style="display: none;">
    <td nowrap width="150"><b>&nbsp;HTTP服务器软件：</b></td>
    <td nowrap id="serverInfo"></td>
  </tr>
  <tr class="TableData" style="display: none;">
    <td nowrap width="150"><b>&nbsp;数据库软件：</b></td>
    <td nowrap id="dbms"></td>
  </tr>
  <tr class="TableData" style="display: none;">
    <td nowrap width="150"><b>&nbsp;软件安装路径：</b></td>
    <td nowrap id="setupPath"></td>
  </tr>
  <tr class="TableData" style="display: none;">
    <td nowrap width="150"><b>&nbsp;软件安装时间：</b></td>
    <td nowrap id="installTime"></td>
  </tr>
  <tr id="remainDays" class="TableData" style="display: none;">
    <td nowrap width="150"><b>&nbsp;试用到期：</b></td>
    <td nowrap><span></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=contextPath %>/core/funcs/system/info/triaReg.jsp">增加试用人数延长试用期限</a></td>
  </tr>
  <tr class="TableData" style="display: none;">
    <td nowrap width="150"><b>&nbsp;端口号：</b></td>
    <td nowrap id="port"></td>
  </tr>
  <tr class="TableData">
    <td nowrap  width="150"><b>&nbsp;用户单位：</b></td>
    <!--<td nowrap  id="unitName"></td>-->
    <td nowrap><span>ps火星科技有限公司</span></td>
  </tr>
  <tr class="TableData" id="regUnitNameTr" style="display:none;">
    <td nowrap width="150"><b>&nbsp;注册信息用户单位：</b></td>
    <td nowrap id="regUnitName"></td>
  </tr>
  <tr class="TableData">
    <td nowrap width="150"><b>&nbsp;OA用户数限制：</b></td>
    <!--<td nowrap><span id="regUserAmount"></span></td>-->
    <td nowrap><span>无限制</span></td>
  </tr>
  <tr class="TableData" style="display:none;">
    <td nowrap width="150"><b>&nbsp;即时通讯用户数限制：</b></td>
    <td nowrap id="regImUserAmount"></td>
  </tr>
  <tr class="TableData" style="display:none;">
    <td nowrap width="150"><b>&nbsp;实际用户数：</b></td>
    <td nowrap id="userAmount"></td>
  </tr>
  <tr class="TableData" style="display:none;">
    <td nowrap width="150"><b>&nbsp;软件注册：</b></td>
    <td nowrap id="regOrNot"></td>
  </tr>
  <tr class="TableData" style="display:none;">
    <td nowrap width="150"><b>&nbsp;已注册可选组件：</b></td>
    <td nowrap>无</td>
  </tr>
  <tr class="TableData" style="display:none;">
    <td nowrap width="150"><b>&nbsp;机器码：</b></td>
    <td id="machineCode" nowrap></td>
  </tr>
  <tr class="TableData" id="regMachineCodeTr" style="display:none;">
    <td nowrap width="150"><b>&nbsp;注册信息机器码：</b></td>
    <td id="regMachineCode" nowrap></td>
  </tr>
  <tr class="TableData" style="display:none;">
    <td nowrap width="150"><b>&nbsp;系统标识码：</b></td>
    <td id="serialId" nowrap></td>
  </tr>
  <tr class="TableData" id="uploadFile">
    <td nowrap width="150"><b>&nbsp;上传注册文件：</b></td>
    <td nowrap>
      <input type="file" id="file" name="file">
      &nbsp;<a href="javascript:void(0)" onclick="submitForm();" class="ToolBtn" ><span>注册</span></a>
    </td>
  </tr>
  <tr class="TableData" style="display:none;">
    <td nowrap  colspan="2" align="center">
      <a href="javascript:void(0)" onclick="location='<%=contextPath%>/core/funcs/system/info/detail.jsp'" class="ToolBtn"><span>查看注册文件</span></a>
      &nbsp;&nbsp;<a href="javascript:void(0)" onclick="reloadRegInfo();return false;" class="ToolBtn"><span>重新加载注册信息</span></a>
    </td>
  </tr>
</table>
 </form>
</body>
</html>