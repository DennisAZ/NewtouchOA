<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑培训记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/training/act/YHTrainingRecordAct/getRecordInfoDetail.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    $("trainningCost2").value = insertKiloSplit(data.trainningCost,2);
    $("tExamResults2").value = insertKiloSplit(data.tExamResults,2);
    if($("staffUserId") && $("staffUserId").value.trim()){
      bindDesc([{cntrlId:"staffUserId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  if($("tPlanName").value == ""){ 
    alert("培训计划名称不能为空！");
    $("tPlanName").focus();
	return false;
  }

  if($("tInstitutionName").value == ""){ 
    alert("培训机构不能为空！");
    $("tInstitutionName").focus();
	return false;
  }
  
  var re1 = /\,/gi;
  $("trainningCost").value = $("trainningCost2").value.replace(re1,'');
  if($("trainningCost").value){
    if(!isNumbers($("trainningCost").value)){
      alert("您填写的培训费用格式错误，应形如 10000.00");
      $("trainningCost2").focus();
      $("trainningCost2").select();
      return false;
    }
  }
  
  $("tExamResults").value = $("tExamResults2").value.replace(re1,'');
  if($("tExamResults").value){
    if(!isNumbers($("tExamResults").value)){
      alert("您填写的培训考核成绩格式错误，应形如 10000.00");
      $("tExamResults2").focus();
      $("tExamResults2").select();
      return false;
    }
  }

  var reg = /^[0-9]*$/;
  if($("tExamLevel").value){
    if(!reg.test($("tExamLevel").value)){
      alert("培训考核等级只能输入整数！");
      $("tExamLevel").focus();
      $("tExamLevel").select();
      return false;
    }
  }
  
  var seqId = $("seqId").value;
  
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/training/act/YHTrainingRecordAct/updateRecord.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    location = "<%=contextPath %>/subsys/oa/training/record/update.jsp?seqId="+seqId;
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 编辑培训记录</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData">受训人：<font style='color:red'>*</font></td>
      <td class="TableData" >
      	<input type="hidden" name="staffUserId" id="staffUserId" value="">
        <INPUT type="text" name="staffUserIdDesc" id="staffUserIdDesc" class=BigStatic size="15" value=""  readonly>
      <td nowrap class="TableData">培训计划名称：<font style='color:red'>*</font></td>
      <td class="TableData" >
      	<input type="hidden" name="tPlanNo" id="tPlanNo" value="">
        <INPUT type="text" name="tPlanName" id="tPlanName" class=BigStatic size="20"  value="">
      </td>
    </tr>
    <tr>    	
    	<td nowrap class="TableData">培训机构：<font style='color:red'>*</font></td>
      <td class="TableData" >
       <input type="text" name="tInstitutionName" id="tInstitutionName" size="30" class="BigInput" value="">
      </td>
      <td nowrap class="TableData">培训费用：</td>
      <td class="TableData">
       <input type="hidden" name="trainningCost" id="trainningCost" size="10" class="BigInput" value="">
       <input type="text" name="trainningCost2" id="trainningCost2" size="10" class="BigInput" value="">
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">培训考核成绩：</td>
      <td class="TableData">
       <input type="hidden" name="tExamResults" id="tExamResults" size="10" maxlength="10" class="BigInput" value="">
       <input type="text" name="tExamResults2" id="tExamResults2" size="10" maxlength="10" class="BigInput" value="">
      </td>
      <td nowrap class="TableData">培训考核等级：</td>
      <td class="TableData">
       <input type="text" name="tExamLevel" id="tExamLevel" size="10" maxlength="10" class="BigInput" value="">
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">出勤情况：</td>
      <td class="TableData" colspan=3>
        <textarea name="dutySituation" id="dutySituation" cols="66" rows="5" class="BigInput" value=""></textarea>
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">总结完成情况：</td>
      <td class="TableData" colspan=3>
        <textarea name="trainningSituation" id="trainningSituation" cols="66" rows="5" class="BigInput" value=""></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">评论：</td>
      <td class="TableData" colspan=3>
        <textarea name="tComment" id="tComment" cols="66" rows="5" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="remark" id="remark" cols="66" rows="5" class="BigInput" value=""></textarea>
      </td>
    </tr> 
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      	<input type="hidden" name="seqId" id="seqId"  value="">
        <input type="button" value="保存" class="BigButton" onclick="doSubmit();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>