<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  XHTML  1.0  Transitional//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html  xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=utf-8">
<title>编辑模块</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/datastructs.js"></script>
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/sys.js"></script>
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/prototype.js"></script>
<script  type="text/javascript"  src="<%=contextPath  %>/core/js/smartclient.js"></script>
<script type="text/javaScript">

function check(e){
  if(!isPositivInteger(e.value)){
    alert("请输入正整数!");
    e.select();
  }
}

function doInit(){
	var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHMytableAct/listFiles.act");
	if (rtJson.rtState == "0"){
		initSelectOption($('MODULE_FILE'),rtJson);
	}
	
	var rtDetail = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHMytableAct/detail.act?seqId=${param.seqId}");
	if (rtDetail.rtState == "0"){
		initSelectState($('MODULE_POS'),rtDetail.rtData.modulePos);
    initSelectState($('VIEW_TYPE'),rtDetail.rtData.viewType);
    initSelectState($('MODULE_FILE'),rtDetail.rtData.moduleFile);
    $('MODULE_LINES').value = rtDetail.rtData.moduleLines;
    $('MODULE_NO').value = rtDetail.rtData.moduleNo;
    $('MODULE_SCROLL').checked = rtDetail.rtData.moduleScroll == "1" ? true:false;
	}
	else{
    alert("初始化表单失败");    
	}
	
}

//初始化select标签

var initSelectState = function(e,value){
  e.childElements().each(function(e,i){
    if(e.value == value){
      e.selected = true;
    }
  });
}

var initSelectOption = function(element,rtJson){
	rtJson.rtData.each(function(e,i){
	  element.options.add(new Option(e.content,e.url));
	});
	
}

var submitForm = function(){
	var pars = Form.serialize($('form1'));
	var url = "<%=contextPath %>/yh/core/funcs/setdescktop/setports/act/YHMytableAct/setMytable.act";

	var json = getJsonRs(url,pars);
	if(json.rtState == "0"){
	  top.reloadDesktop && top.reloadDesktop();
    window.location.href = "<%=contextPath %>/core/funcs/setdescktop/setports/index.jsp";
	}else{
		alert("属性更改失败");
	}
}


</script>
</head>


<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" >
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 编辑模块</span>
    </td>
  </tr>
</table>

<form action="" method="post" name="form1" id="form1" >
<table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">默认位置：</td>
    <td nowrap class="TableData">
        <select name="MODULE_POS" id="MODULE_POS">
          <option value="l" selected>左侧</option>
          <option value="r" >右侧</option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">序号：</td>
    <td nowrap class="TableData">
        <input type="text" name="MODULE_NO" id="MODULE_NO" value="1" class="BigInput" size="10" maxlength="100" onblur="javascript:check($('MODULE_NO'))">
    </td>
   <tr>
    <td nowrap class="TableData">文件：</td>
    <td nowrap class="TableData">
        <select name="MODULE_FILE" id="MODULE_FILE">
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">显示属性：</td>
    <td nowrap class="TableData">
        <select name="VIEW_TYPE" id="VIEW_TYPE">
          <option value="1" selected>用户可选</option>
          <option value="2" >用户必选</option>
          <option value="3" >暂停显示</option>
        </select>
    </td>
   </tr>
   <tr class="TableData">
    <td width="130">显示行数：</td>
    <td><input type="text" name="MODULE_LINES" id="MODULE_LINES" class="BigInput" size="5" value="5" onblur="javascript:check($('MODULE_LINES'))"></td>
   </tr>
   <tr class="TableData">
    <td>滚动显示：</td>
    <td><input type="checkbox" name="MODULE_SCROLL" id="MODULE_SCROLL" /><label for="MODULE_SCROLL">列表上下滚动显示</label></td>
   </tr>
   <tr class="TableData">
    <td>添加模块：</td>
    <td><input type="checkbox" name="MODULE_ADD" id="MODULE_ADD" /><label for="MODULE_ADD">为所有用户添加此模块</label></td>
   </tr>
   <tr   class="TableControl">
   <input type="hidden" value="${param.seqId}" name="SEQ_ID" id="SEQ_ID">
    <td nowrap colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" onclick="submitForm()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="location='index.jsp'">
    </td>
</table>
</form>

</body>
</html>