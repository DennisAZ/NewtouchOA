<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核指标集</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreItemLogic.js"></script>
<script type="text/javascript">

function doInit(){

}

function doSubmit(){
  if($("itemName").value.trim() == ""){ 
    alert("考核项目选项不能空！！！");
    $("itemName").focus();
    $("itemName").select();
    return false;
  }
  if($("scoreA").value){
    if(!isNumbers($("scoreA").value)){
     alert("分值格式错误,应形如  100.00");
     $("scoreA").focus();
     $('scoreA').select();  
     return false;
    }
  }
  if($("scoreB").value){
    if(!isNumbers($("scoreB").value)){
     alert("分值格式错误,应形如  100.00");
     $("scoreB").focus();
     $('scoreB').select();  
     return false;
    }
  }
  if($("scoreC").value){
    if(!isNumbers($("scoreC").value)){
     alert("分值格式错误,应形如  100.00");
     $("scoreC").focus();
     $('scoreC').select();  
     return false;
    }
  }
  if($("scoreD").value){
    if(!isNumbers($("scoreD").value)){
     alert("分值格式错误,应形如  100.00");
     $("scoreD").focus();
     $('scoreD').select();  
     return false;
    }
  }
  if($("scoreE").value){
    if(!isNumbers($("scoreE").value)){
     alert("分值格式错误,应形如  100.00");
     $("scoreE").focus();
     $('scoreE').select();  
     return false;
    }
  }
  if($("scoreF").value){
    if(!isNumbers($("scoreF").value)){
     alert("分值格式错误,应形如  100.00");
     $("scoreF").focus();
     $('scoreF').select();  
     return false;
    }
  }
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/hr/score/act/YHScoreAnswerAct/addItemAnswer.act?itemId=${param.seqId}";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    var parentWindow = window.opener;
    parentWindow.reloadFunc();
    window.location.reload();
    window.close();
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" WIDTH="22" HEIGHT="20" align="absmiddle"> <span class="big3">新建考核选项</span>
    </td>
  </tr>
</table>
<br>
<form method="post" name="form1" id="form1">
<input type="hidden" name="seqId" id="seqId" value="">
<table class="TableBlock" width="70%" align="center">
    <tr>
      <td nowrap class="TableData"> 考核项目：<font style='color:red'>*</font></td>
      <td class="TableData">
         <textarea name="itemName" id="itemName" class="BigInput" cols="50" rows="3"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选项A：</td>
      <td class="TableData">
        <input type="text" name="answerA" id="answerA" size="40" maxlength="100" class="BigInput" value="">&nbsp;&nbsp;
        <input type="text" name="scoreA" id="scoreA" size="3" maxlength="5" class="BigInput" value="">分值
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选项B：</td>
      <td class="TableData">
        <input type="text" name="answerB" id="answerB" size="40" maxlength="100" class="BigInput" value="">&nbsp;&nbsp;
        <input type="text" name="scoreB" id="scoreB" size="3" maxlength="5" class="BigInput" value="">分值
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选项C：</td>
      <td class="TableData">
        <input type="text" name="answerC" id="answerC" size="40" maxlength="100" class="BigInput" value="">&nbsp;&nbsp;
        <input type="text" name="scoreC" id="scoreC" size="3" maxlength="5" class="BigInput" value="">分值
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选项D：</td>
      <td class="TableData">
        <input type="text" name="answerD" id="answerD" size="40" maxlength="100" class="BigInput" value="">&nbsp;&nbsp;
        <input type="text" name="scoreD" id="scoreD" size="3" maxlength="5" class="BigInput" value="">分值
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选项E：</td>
      <td class="TableData">
        <input type="text" name="answerE" id="answerE" size="40" maxlength="100" class="BigInput" value="">&nbsp;&nbsp;
        <input type="text" name="scoreE" id="scoreE" size="3" maxlength="5" class="BigInput" value="">分值
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备选项F：</td>
      <td class="TableData">
        <input type="text" name="answerF" id="answerF" size="40" maxlength="100" class="BigInput" value="">&nbsp;&nbsp;
        <input type="text" name="scoreF" id="scoreF" size="3" maxlength="5" class="BigInput" value="">分值
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存" class="BigButton" onClick="doSubmit();">&nbsp;&nbsp;
        <input type="button" value="关 闭" class="BigButton" onclick="window.close()">
      </td>
    </tr>
  </table>
</form>
</body>
</html>