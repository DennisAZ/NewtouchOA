<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>收藏夹管理</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script  type="text/Javascript">

function doInit(){

  //解码request中参数
  var url = "${param.url}";
  var urlDesc = "${param.urlDesc}";
  var urlNo = "${param.urlNo}";
  var seqId = "${param.seqId}";
  
  $('urltext').value = url.replace(/1:/,'');
  $('urlDesc').value = urlDesc;
  $('urlNo').value = urlNo;
  $('seqId').value = seqId;

  $('openWindow').checked = (/1:/).exec(url);
}

function submitForm(){
  if(!(/^[0-9]+$/).exec(($F('urlNo')))){
    alert('序号必须为数字!');
    $('urlNo').select();
    return;
  }
  if(!$F('urlDesc')){
    alert('名称不能为空');
    $('urlDesc').select();
    return;
  }
  if(!$F('urltext')){
    alert('网址不能为空');
    $('urltext').select();
    return;
  }
  $('url').value = ($('openWindow').checked ? "1:" : "") + $F('urltext');
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/fav/act/YHFavAct/modifyUrl.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    location = "<%=contextPath%>/core/funcs/setdescktop/fav/index.jsp";
  }else{
    alert("修改失败");
  }
}
</script>
</head>

<body onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 添加收藏</span>
    </td>
  </tr>
</table>

<form action=""  method="post" name="form1" id="form1">
  <table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">序号：<span style="color: red;">*</span></td>
    <td nowrap class="TableData">
        <input type="text" name="urlNo" id="urlNo" class="BigInput" size="10" maxlength="25">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">名称：<span style="color: red;">*</span></td>
    <td nowrap class="TableData">
        <input type="text" name="urlDesc" id="urlDesc" class="BigInput" size="25" maxlength="200">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">网址：<span style="color: red;">*</span></td>
    <td nowrap class="TableData">
        <input type="text" name="urltext" id="urltext" class="BigInput" size="50" maxlength="200" value="">
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">选项：</td>
    <td nowrap class="TableData">
        <input type="checkbox" id="openWindow" name="openWindow"><label for="openWindow">在新窗口打开</label>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" value="yh.core.funcs.system.url.data.YHUrl" name="dtoClass" id="dtoClass">
      <input type="hidden" name="url" id="url">
      <input type="hidden" name="seqId" id="seqId">
      <input type="hidden" name="urlType" id="urlType" value="2">
      <input type="button" value="确定" class="BigButton" onclick="submitForm()">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onclick="history.back()">&nbsp;&nbsp;
    </td>
   </tr>
  </table>
</form>
</body>
</html>