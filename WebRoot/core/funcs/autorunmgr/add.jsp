<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/email.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/autorunmgr/js/autorunmgr.js"></script>
<script type="text/javascript">

  function doSubmit(){
    if(!checkForm()){
      return;
    }
    if(!checkServiceIsInValidity($("cls").value)){
     var msg = "无法找到该服务实体类，确认不修改直接保存吗?点击“确认”将直接保存，此服务将定义为“停用”状态";
      if(window.confirm(msg)){
        $("isUsed_0").checked = true;
      }else{
        $("cls").select();
        return;
      }
    }
    $('intervalSecond').value = $("intervalSecondTemp").value*60;
    var param = $("form1").serialize();
    var url = contextPath + "/yh/core/funcs/autorunmgr/act/YHAutoRunManagerAct/addAutoRunCfg.act";
    var rtJson = getJsonRs(url,param);
    if(rtJson.rtState == "0"){
      location='index.jsp';
    }else{
      alert("注册服务失败：" + rtJson.rtMsrg);
   }
  }

  function checkForm(){
    if(!$("name").value){
       $("name").focus();
       alert("服务名称不能为空!");
       return false;
     }
    if(!$("cls").value){
      $("cls").focus();
      alert("执行实体类不能为空!");
      return false;
    }
    if(!$("name").value){
      $("name").focus();
      alert("服务名称不能为空!");
      return false;
    }
    if(!$("intervalSecondTemp").value){
      alert("执行间隔不能为空!");
      $("intervalSecondTemp").focus();
      return false;
    }
    if(!trans2intervalSecond($("intervalSecondTemp").value)){
      alert("执行间隔必须为大于0的整数!");
      $("intervalSecondTemp").select();
      return false;
    }
    return true;
  }
  function trans2intervalSecond(value){
    var reg = /^\d*$/; 
    if((!reg.test(value)) || value == "0" ){
      return false;
    }
    return true;
  }
</script>
<title>Insert title here</title>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 注册间隔任务</span>
    </td>
  </tr>
</table>
<form  name="form1" id="form1">
<table class="TableBlock" width="450" align="center">
   <tr class="TableData">
    <td width="80">任务名称：</td>
    <td ><input type="text" id="name" name="name" class="BigInput" size="40" ><font color="red">*</font></td>
   </tr>
    <tr class="TableData">
    <td width="80">执行实体类：</td>
    <td ><input type="text" id="cls" name="cls" class="BigInput"  size="40" > <font color="red">*</font></td>
   </tr>
   <tr class="TableData">
    <td>执行间隔：</td>
    <td>
        <input type="hidden" id="intervalSecond" name="intervalSecond" class="BigInput" size="5" maxlength="3" value="300"> 
        <input type="text" id="intervalSecondTemp"  class="BigInput" size="5" maxlength="3" value="5"> 分钟  <font color="red">*</font>  </td>
   </tr>
   <tr class="TableData">
    <td>是否启用：</td>
    <td>
        <input type="radio" name="isUsed" id="isUsed_1" value="1" checked><label for="isUsed_1">启用</label>
        <input type="radio" name="isUsed" id="isUsed_0" value="0"><label for="isUsed_0">停用</label>
    </td>
   </tr>
   <tr class="TableControl">
    <td colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" onclick="doSubmit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="location='index.jsp'">
    </td>
</table>
</form>
 
</body>

</html>