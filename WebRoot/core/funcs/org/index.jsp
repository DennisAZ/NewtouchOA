<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>单位管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
function doInit() {
  var url = "<%=contextPath%>/yh/core/funcs/org/act/YHOrgAct/getOrganization.act";
  var rtJson = getJsonRs(url);

  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function isValidEmail(str) {
  var re = /@/;
  return str.match(re)!=null;
}

function checkStr(str){ 
  var re=/[\\"']/; 
  return str.match(re); 
}
//验证邮编
function isZip(zipCode) {
    return (/^[1-9]\d{5}$/).test(zipCode);
}
var patrnFix = /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/; 
function check() {
  var reg = /^[0-9]*$/;
  var patrn=/^[a-zA-Z0-9 ]{3,12}$/;
  var patrn2=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/; 
  var emailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
  var cntrl = document.getElementById("unitName");
  if (!cntrl.value.trim()) {
	alert("单位名称不能为空！");
	cntrl.focus();
    return false;
  }
  if(checkStr($("unitName").value)){
    alert("您输入的单位名称含有'双引号'、'单引号 '或者 '\\' 请从新填写");
    $('unitName').focus();
    $('unitName').select();
    return false;
  }
  if ($('postcode').value ) {
    if (  !isZip($('postcode').value)) {
      alert("你输入的邮编不对,邮编只能为6位数字！");
      $("postcode").focus();
  	  $("postcode").select();
      return false;
    }
  }
  
  if($('max').value){
    if(!patrnFix.exec($('max').value) || $('max').value.length > 12){
      alert("您输入的传真格式不正确，请重新填写！");
      $("max").focus();
	  $("max").select();
	  return false;
    }
  }
//  if($("telephone").value){
//    if(!patrn2.exec($("telephone").value)){
//  	  alert("您输入的电话号码格式不正确，请重新填写！");
//  	  $("telephone").focus();
//  	  $("telephone").select();
//  	  return false;
//    }
//  }
//  if($("max").value){
//    if(!patrn2.exec($("max").value)){
//  	  alert("您输入的传真格式不正确，请重新填写！");
//  	  $("max").focus();
//  	  $("max").select();
//  	  return false;
//    }
//  }
  if($("email").value){
    if(!emailReg.exec($("email").value)){
  	  alert("请输入有效的E-mail地址！");
  	  $("email").focus();
  	  $("email").select();
  	  return false;
    }
  }
//  if(!patrn.exec($("postcode").value)){
//  	alert("邮编只能输入数字！");
//  	$("postcode").focus();
//  	$("postcode").select();
//  	return false;
//  }
//  if(!reg.test($("account").value)){
//  	alert("账号只能输入数字！");
//  	$("account").focus();
//  	$("account").select();
//  	return false;
//  }
  return true;
}

function commitItem() {
  if(!check()){
    return;
  }
  var url = null;
  var rtJson = null;
  var seq = document.getElementById("seqId").value;
  url = "<%=contextPath%>/yh/core/funcs/org/act/YHOrgAct/";
  if (seq && seq != 0) {
    url += "updateOrganization.act";
  }else{
    url += "addOrganization.act";
  }
  rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if (rtJson.rtState == "0"){
  	location = "<%=contextPath%>/core/funcs/org/update.jsp"
  }
  else{
  	alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit();">
  <div>
    <img src="<%=imgPath%>/sys_config.gif"></img>
    <font size="3">&nbsp;单位管理</font>
  </div>
  <form name="form1" id="form1" method="post">
   <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.org.data.YHOrganization"/>
 	 <input type="hidden" id="seqId" name="seqId" value=""/>
   <br/>
   <table class="TableBlock" cellscpacing="1" cellpadding="3" width="90%" align="center">
    <tr  class="TableLine1">
      <td>单位名称：<font style='color:red'>*</font></td>
      <td nowrap class="TableData">
        <input type="text" id="unitName" name="unitName" class="BigInput" size="40" value=""><br>
                   软件注册后，请不要随意修改单位名称，这会导致需要重新注册

      </td>
    </tr>
    <tr  class="TableLine2">
      <td>电话：</td>
      <td nowrap class="TableData">
        <input type="text" id="telephone" name="telephone" class="BigInput" size="40" value="">&nbsp;
      </td>
    </tr>
    <tr  class="TableLine1">
      <td nowrap class="TableData">传真：</td>
      <td nowrap class="TableData">
        <input type="text" id="max" name="max" class="BigInput" size="40" value="">&nbsp;
      </td>
    </tr>
    <tr  class="TableLine2">
      <td nowrap class="TableData">邮编：</td>
      <td nowrap class="TableData">
        <input type="text" id="postcode" name="postcode" class="BigInput" size="40" value="">&nbsp;
      </td>
    </tr>
    <tr  class="TableLine1">
      <td nowrap class="TableData">地址：</td>
      <td nowrap class="TableData">
        <input type="text" id="address" name="address" class="BigInput" size="40" value="">&nbsp;
      </td>
    </tr>
    <tr  class="TableLine2">
      <td nowrap class="TableData">网站：</td>
      <td nowrap class="TableData">
        <input type="text" id="website" name="website" class="BigInput" size="40" value="">&nbsp;
      </td>
    </tr>
    <tr  class="TableLine1">
      <td nowrap class="TableData">电子信箱：</td>
      <td nowrap class="TableData">
        <input type="text" id="email" name="email" class="BigInput" size="40" value="">&nbsp;
      </td>
    </tr>
    <tr  class="TableLine2">
      <td nowrap class="TableData">开户行：</td>
      <td nowrap class="TableData">
        <input type="text" id="signInUser" name="signInUser" class="BigInput" size="40" value="">&nbsp;
      </td>
    </tr>
    <tr  class="TableLine1">
      <td nowrap class="TableData">账号：</td>
      <td nowrap class="TableData">
        <input type="text" id="account" name="account" class="BigInput" size="40" value="">&nbsp;
      </td>
   </tr>
   <tr  class="TableLine2">
      <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="保存单位设置" class="BigButtonC" onclick="commitItem()">
      </td>
   </tr>  
  </table>
 </form>
</body>
</html>