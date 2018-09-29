<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = YHUtility.null2Empty(request.getParameter("seqId"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<title>新增数据表</title>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var requestUrl = contextPath + "/raw/lh/generate/act/YHSysTableAct";
var url = requestUrl + "/getTable.act";
var seqId = "<%=seqId %>";
function doInit() {
  if (seqId) {
    var url2 = requestUrl + "/getTableInfo.act";
    var json = getJsonRs(url2 , "seqId=" + seqId);
    if (json.rtState == '0') {
      var obj = json.rtData;
      var str = "TABLE_NAME, NO_FIELD_PRE, NO_FIELD_LENGTH, NO_FIELD_START, NO_FIELD_DEFAULT";
      var strs = str.split(",");
      for (var i = 0 ; i < strs.length ; i++) {
        var ctrl = document.form1[strs[i].trim()];
        if (strs[i].trim()=='NO_FIELD_DEFAULT') {
          if (obj[strs[i].trim()] == '1') {
             $('IS_DEFAULT_1').checked = true;
          } else {
            $('IS_DEFAULT_0').checked = true;
          }
        } else {
          ctrl.value = obj[strs[i].trim()];
        }
      }
    }
  }
}
function checkForm() {
  if (!$('TABLE_NAME').value) {
    alert("表名不能为空！");
    return false;
  }
  return true;
}
function saveTable(){
  if (!checkForm()) {
    return ;
  }
  var url2 = requestUrl + "/addOrUpdateTable.act";
  var json = getJsonRs(url2 ,$('form1').serialize());
  if (json.rtState == '0') {
    alert("保存成功！");
    document.location.href = contextPath + "/raw/lh/generate/index.jsp";;
  }
}
function radio_click(obj){
  if (obj.value == '1') {
    $('NO_FIELD_START').value = '1';
    $('NO_FIELD_LENGTH').value = '1';
    $('NO_FIELD_LENGTH').className = 'BigStatic';
    $('NO_FIELD_START').className = 'BigStatic';
    $('NO_FIELD_LENGTH').readOnly = true;
    $('NO_FIELD_START').readOnly = true;
  } else {
    $('NO_FIELD_LENGTH').className = 'BigInput';
    $('NO_FIELD_START').className = 'BigInput';
    $('NO_FIELD_LENGTH').readOnly = false;
    $('NO_FIELD_START').readOnly = false;
  }
}

</script>
</head>
<body onload="doInit()">

<div id="total"><table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">新增加数据表
    </td>
  </tr>
</table></div><br>
<div id="query" style="padding-left:5px">
<form action="" id="form1" name="form1">
<table class="TableBlock" width="70%" align="center">
<tr>
   <td nowrap class="TableData"> 表名：<font color="red">*</font></td>
   <td class="TableData" colspan="3">
     <input type="text" name="TABLE_NAME" id="TABLE_NAME" maxlength="100" class="BigInput" value="">
   </td>
</tr>
<tr>
   <td nowrap class="TableData"> 编码前缀：</td>
   <td class="TableData">
     <input type="text" name="NO_FIELD_PRE" id="NO_FIELD_PRE"  class="BigInput" value="">
   </td>
     <td nowrap class="TableData"> 是否默认：</td>
   <td class="TableData">
   <input type="radio" id="IS_DEFAULT_1" onclick="radio_click(this)" name="NO_FIELD_DEFAULT" value="1" checked><label for="IS_DEFAULT_1">是</label>
   <input type="radio" id="IS_DEFAULT_0" onclick="radio_click(this)" name="NO_FIELD_DEFAULT" value="0"><label for="IS_DEFAULT_0">否</label>
   
   </td>
</tr>
<tr>
   <td nowrap class="TableData"> 流水号位数：<font color="red">*</font></td>
   <td class="TableData" >
     <input type="text" name="NO_FIELD_LENGTH"  class="BigStatic" readonly  id="NO_FIELD_LENGTH" size="4"  value="1">
   </td>
   <td nowrap class="TableData"> 起始流水号：<font color="red">*</font></td>
   <td class="TableData">
     <input type="text" name="NO_FIELD_START"  class="BigStatic" readonly id="NO_FIELD_START" size="4" value="1">
   </td>
</tr>
<tr>
<td nowrap class="TableData"> 范例：</td>
   <td class="TableData"  colspan="3">
     <input type="text"  name="NO_FIELD_SAMPLE" id="NO_FIELD_SAMPLE"  class="BigStatic" readonly value="">
   </td>

</tr>
        <tr align="center" class="TableControl">
      <td colspan="4" >
      <input type="hidden"  name="SEQ_ID" id="SEQ_ID"   value="<%=seqId %>">
        <input type="button" value="保存" class="BigButton" onclick="saveTable()">&nbsp;&nbsp;
        <input type="button" onclick="location.href='<%=contextPath %>/raw/lh/generate/index.jsp'"  value="返回" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</div>

</body>
</html>