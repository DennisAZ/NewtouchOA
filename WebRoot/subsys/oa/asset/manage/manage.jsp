<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>固定资产管理</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script>
function doInit() { 
  showCalendar('listDate',false,'beginDateImg');//时间
  showCalendar('getDate',false,'endDateImg');
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpAssetTypeAct/assetTypeId.act";
  var json = getJsonRs(url);
  var rtData = json.rtData;
  for(var i = 0; i < rtData.length; i++) {
    var opt = document.createElement("option");
    opt.value = rtData[i].seqId;
    opt.text = rtData[i].typeName;   
    var selectObj = $('cptlSpec');
    selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0); 
  }
}
function isNumber(aValue) {
  var digitSrc = "0123456789";
  aValue = "" + aValue;
  if (aValue.substr(0, 1) == "-") {
    aValue = aValue.substr(1, aValue.length - 1);
  }
  var strArray = aValue.split(".");
  // 含有多个“.”

  if (strArray.length > 2) {
    return false;
  }
  var tmpStr = "";
  for (var i = 0; i < strArray.length; i++) {
    tmpStr += strArray[i];
  }
 
  for (var i = 0; i < tmpStr.length; i++) {
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i));
    if (tmpIndex < 0) {
      // 有字符不是数字

      return false;
    }
  }
  // 是数字

  return true;
}

//表单提交验证
function checkForm() {
  var num = /^[0-9]*$/;

  if(!isValidDateStr($("listDate").value)  && $("listDate").value != ""){
    alert("开始日期格式不对，应形如 1999-01-01"); 
    $("listDate").focus(); 
    $("listDate").select(); 
    return false; 
  }
  if(!isValidDateStr($("getDate").value)  && $("getDate").value != ""){
    alert("结束日期格式不对，应形如 1999-01-01"); 
    $("getDate").focus(); 
    $("getDate").select(); 
    return false; 
  }
  if (document.getElementById("getDate").value != "") {
    if (document.getElementById("listDate").value > document.getElementById("getDate").value ) {
      alert("开始时间不能大于结束时间！");
      document.getElementById("getDate").focus();
      document.getElementById("getDate").select();
      return false;
    }
  }
  if (document.getElementById("cptlVal").value != "") {
    var obj = document.getElementById("cptlVal");
    var val = obj.value;
    if (isNumber(val)) {
    }else {
      alert("非法数字");
      document.getElementById("cptlVal").focus();
      document.getElementById("cptlVal").select();
      return false;
    }
  }
  if (document.getElementById("cptlValMax").value != "") {
    var obj = document.getElementById("cptlValMax");
    var val2 = obj.value;
    if (isNumber(val2)) {
    }else {
      alert("非法数字");
      document.getElementById("cptlValMax").focus();
      document.getElementById("cptlValMax").select();
      return false;
    }
  }
  return true;
}
//查询提交
function checkForm2() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var param = encodeURI(pars);
    var url = "<%=contextPath %>/subsys/oa/asset/manage/query.jsp?" + param;
    window.location = url;
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 增加固定资产入库</span><br>
    </td>
  </tr>
</table>

<div align="center">
  <input type="button"  value=" 导入固定资产 " class="BigButtonC"  onclick="location.href='<%=contextPath%>/subsys/oa/asset/manage/import.jsp'" title="导入固定资产">
  <input type="button"  value=" 新增固定资产 " class="BigButtonC"  onclick="location.href='<%=contextPath%>/subsys/oa/asset/manage/newAsset.jsp'" title="新增固定资产">
</div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3"> 固定资产信息查询</span>
    </td>
  </tr>
</table>

 <form name="form1" id="form1" method="post">
<table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="100"> 资产编号：</td>
      <td class="TableData"> 
        <input type="text" name="cptlNo" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">资产名称：</td>
      <td class="TableData"> 
        <input type="text" name="cptlName" size="30" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产类别：</td>
      <td class="TableData"> 
      <select name="cptlSpec" id="cptlSpec">
        <option value="">所有类别</option>
        <option value="0">未指定类别</option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产值(单价)：</td>
      <td class="TableData"> 
        大于等于 <input type="text" name="cptlVal" id="cptlVal" size="10" maxlength="23" class="BigInput" value=""  style="text-align:right;"> &nbsp;&nbsp;&nbsp;&nbsp;
        小于等于 <input type="text" name="cptlValMax" id="cptlValMax" size="10" maxlength="23" class="BigInput" value=""  style="text-align:right;">       </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 单据日期：</td>
      <td class="TableData">&nbsp;开始日期： <input type="text" id="listDate" name="listDate" size="10"
maxlength="10" class="BigInput" value=""> <img
id="beginDateImg" src="<%=imgPath%>/calendar.gif"
align="absMiddle" border="0" style="cursor: hand"><br>&nbsp;结束日期： <input
type="text" id="getDate" name="getDate" size="10" maxlength="10"
class="BigInput" value=""> <img id="endDateImg"
src="<%=imgPath%>/calendar.gif" align="absMiddle"
border="0" style="cursor: hand"></td> 
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 专管员：</td>
      <td class="TableData"> 
        <input type="text" name="keeperName" id="keeperName" size="15" maxlength="20"  class="BigInput" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['keeper','keeperName']);">选择</a>
      <input type="hidden" name="keeper" id="keeper" value="">
      <a href="javascript:;" class="orgClear" onClick="$('keeper').value='';$('keeperName').value='';">清空</a>
      </td>
    </tr>
  <tr>
      <td nowrap class="TableData" width="100"> 保管地点：</td>
      <td class="TableData"> 
        <input type="text" name="safekeeping" size="25" maxlength="20" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 备注：</td>
      <td class="TableData"> 
        <textarea cols="35" name="remark" rows="2" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" onClick="checkForm2()" value="查询" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>