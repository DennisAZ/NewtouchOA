<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建模板</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/cms/template/js/templateLogic.js"></script>
<script type="text/javascript">

function doInit(){
 	var requestURLStr = contextPath + "/yh/cms/template/act/YHTemplateAct/selectStationName.act";
 	var rtJson = getJsonRs(requestURLStr);
 	if(rtJson.rtState == "1"){
 	  alert(rtJson.rtMsrg); 
 	  return ;
 	}
 	var prcs = rtJson.rtData;
 	var selects = document.getElementById("stationId");
 	for(var i = 0; i< prcs.length; i++){
 	  var prc = prcs[i];
 	  var option = document.createElement("option"); 
 	  option.value = prc.seqId; 
 	  option.innerHTML = prc.stationName; 
 	  selects.appendChild(option);
 	}
}

function doSubmit(){
  if(checkForm()){
    $("form1").submit();
  }
}

function checkForm(){
  if($("templateName").value == ""){
    alert("模板名称不能为空！");
    $("templateName").focus();
    return (false);
  }

  if($("templateFileName").value == ""){
    alert("模板文件名不能为空！");
    $("templateFileName").focus();
    return (false);
  }
  
  if($("templateFile").value == ""){
    alert("请选择模板文件！");
    $("templateFile").focus();
    return (false);
  }
  return true;
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建模板</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/cms/template/act/YHTemplateAct/addTemplate.act" method="post" name="form1" id="form1" enctype="multipart/form-data">
	<table class="TableBlock" width="80%" align="center">
	  <tr>
        <td nowrap class="TableData">名称：<font color="red">*</font></td>
        <td class="TableData">
          <input type="text" name="templateName" id="templateName" class="BigInput" size="15">
        </td>
        <td nowrap class="TableData">文件名：<font color="red">*</font></td>
        <td class="TableData">
          <input type="text" name="templateFileName" id="templateFileName" class="BigInput" size="15">
        </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">模板类型：<font color="red">*</font> </td>
        <td class="TableData">
          <select name="templateType" id="templateType">
          	<option value="1">索引</option>
          	<option value="2">文章</option>
          	<option value="3">包含</option>
          </select>
        </td>
   	    <td nowrap class="TableData">所属站点：<font color="red">*</font> </td>
        <td class="TableData">
          <select name="stationId" id="stationId" style="width: 130px;">
          </select>
        </td>
	  </tr>
    <tr>
      <td nowrap class="TableData">模板文件： <font color="red">*</font></td>
      <td class="TableData" colspan=3>
        <input type="file" id="templateFile" name="templateFile" size="40">
      </td>
    </tr> 
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="hidden" name="dtoClass" id="dtoClass" value="yh.cms.template.data.YHCmsTemplate">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>