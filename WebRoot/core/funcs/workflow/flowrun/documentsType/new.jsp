<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文件类型</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript"  src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript"  src="<%=contextPath%>/core/funcs/workflow/flowrun/documentsType/js/documentsTypeLogic.js"></script>
<!-- 文件上传 -->
<script type="text/javascript">
function doInit(){
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/getDocModule.act";  
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var type = json.rtData;
    var selected = [];
    for (var i = 0 ;i < type.length ;i++) {
      var t = type[i];
      var tmp = {value:t,text:t};
      selected.push(tmp);
    }
    new ExchangeSelectbox({containerId:'selectedDiv', selectedArray:[], disSelectedArray:selected ,isSort:false ,selectName:'selectedName',selectedChange:exchangeHandler}); 
  }
  getFlowType("");
}
function getFlowType() {
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHDocAct/getFlowType.act";  
  var json = getJsonRs(url);
  if (json.rtState == "0") {
    var type = json.rtData;
    if (type.length > 0) {
      for (var i = 0 ;i < type.length ;i++) {
        var name = type[i].flowName;
        var seqId = type[i].seqId;
        var option = new Element("option");
        option.value = seqId;
        option.update(name);
        $('flowType').appendChild(option);
      }
    } else {
      $('flowType').update("<option value=''>未定义流程</option>");
    }
  }
}
function exchangeHandler(ids){
  $('documentsWordModel').value = ids;
}
function doSubmit(){
  if(checkForm()){
    $("form1").submit();
  }
}

function checkForm(){
  if($("documentsName").value == ""){
    alert("文件名称不能为空！");
    $("documentsName").focus();
    return (false);
  }

  if($("documentsFont").value == ""){
    alert("文件字不能为空！");
    return (false);
  }

  if($("documentsWordModel").value == ""){
    alert("套红模板路径不能为空！");
    $("documentsWordModel").focus();
    return (false);
  }
  return true;
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建文件类型</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/core/funcs/workflow/act/YHDocumentsTypeAct/addDocumentsTypeInfo.act"  method="post" name="form1" id="form1" onsubmit="">
	<table class="TableBlock" width="80%" align="center">
	  <tr>
	    <td nowrap class="TableData">文件名称：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="documentsName" id="documentsName" class="BigInput" size="15">
      </td>
      <td nowrap class="TableData">绑定流程：<font color="red">*</font> </td>
      <td class="TableData">
        <select name="flowType" id="flowType" style="width:124px;" >
          
        </select>
      </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">文件字：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="documentsFont" id="documentsFont" value="">
        <textarea cols="40" name="documentsFontDesc" id="documentsFontDesc" rows="2" style="overflow-y:auto;" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="loadWindow()">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('documentsFont').value='';$('documentsFontDesc').value='';">清空</a>
      </td>
    </tr> 
        <tr>
      <td nowrap class="TableData">套红模板：<font color="red">*</font> </td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="documentsWordModel" id="documentsWordModel" class="BigInput" size="51">
        <div id='selectedDiv'></div>
      </td>
    </tr> 
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>