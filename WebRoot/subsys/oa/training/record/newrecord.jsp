<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增培训记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/trainingapprovallogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/trainingrecordlogic.js"></script>
<script type="text/javascript">

function doInit(){
}

function LoadWindow(){
  var URL= contextPath + "/subsys/oa/training/approval/plannoinfo/index.jsp";
  loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
  loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}


function doSubmit(){
  if($("staffUserId").value == ""){ 
    alert("受训人不能为空！");
    $("staffUserIdDesc").focus();
    return false;
  }
  if($("tPlanName").value == ""){ 
    alert("培训计划名称不能为空！");
    $("tPlanName").focus();
    return false;
  }

  if($("trainningCost").value != ""){
    if(!isNumbers($("trainningCost").value)){
      alert("您填写的培训费用格式错误，应形如 10000.00");
      $("trainningCost").focus();
      $("trainningCost").select();
      return false;
    }
  }
  var userIdStr = $("staffUserId").value;
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/training/act/YHTrainingRecordAct/addRecord.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    window.location.href = "<%=contextPath %>/subsys/oa/training/record/add.jsp?userIdStr="+userIdStr+"&data="+data;
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新增培训记录</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData">受训人：<font style='color:red'>*</font></td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="staffUserId" id="staffUserId" value="">
        <textarea cols=40 name="staffUserIdDesc" id="staffUserIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['staffUserId', 'staffUserIdDesc'],null,null,1);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('staffUserId', 'staffUserIdDesc')">清空</a>
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">培训计划名称：<font style='color:red'>*</font></td>
      <td class="TableData" >
      	<input type="hidden" name="tPlanNo" id="tPlanNo" value="">
        <INPUT type="text" name="tPlanName" id="tPlanName" class=BigStatic size="12"  value="">
        <a href="javascript:;" class="orgAdd" onClick="LoadWindow()">选择</a>
      </td>
      <td nowrap class="TableData">培训费用：</td>
      <td class="TableData">
       <input type="text" name="trainningCost" id="trainningCost" size="10" maxlength="10" class="BigInput" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">培训机构：</td>
      <td class="TableData" colspan=3>
       <input type="text" name="tInstitutionName" id="tInstitutionName" size="20" class="BigInput" >
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      <input type="hidden" name="toId" id="toId" value=""> 
      <input type="hidden" name="userIdStr" id="userIdStr" value=""> 
      <input type="hidden" name="seqId" id="seqId" value=""> 
        <input type="button" value="保存" class="BigButton" onclick="doSubmit();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>