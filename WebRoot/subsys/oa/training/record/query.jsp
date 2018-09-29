<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>培训记录查询</title>
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
<script type="text/javascript">

function doInit(){
}

function LoadWindow(){
  var URL= contextPath + "/subsys/oa/training/approval/plannoinfo/index.jsp";
  loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
  loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}

function LoadWindow2(){
  var URL= contextPath + "/subsys/oa/training/record/staffuserselect/index.jsp";
  loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
  loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
  window.showModalDialog(URL,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}

function doSubmit(){
  if($("trainningCost").value){
    if(!isNumbers($("trainningCost").value)){
      alert("您填写的培训费用格式错误，应形如 10000.00");
      $("trainningCost").focus();
      $("trainningCost").select();
      return false;
    }
  }
  var query = $("form1").serialize();
  location = "<%=contextPath%>/subsys/oa/training/record/search.jsp?"+query;
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/infofind.gif"><span class="big3">培训记录查询</span></td>
  </tr>
</table>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="70%" align="center">
   <tr>
      <td nowrap class="TableData">受训人：</td>
      <td class="TableData" >
        <input type="hidden" name="userId" id="userId" value="">
        <input type="hidden" name="perSeqId" id="perSeqId" value="">
        <INPUT type="text"name="userName" id="userName" class=BigStatic size="12" value=""  readonly>
        <a href="javascript:;" class="orgAdd" onClick="LoadWindow2()">选择</a>
      </td>
      <td nowrap class="TableData">培训计划名称：</td>
      <td class="TableData" colspan=3>
      <input type="hidden" name="tPlanNo" id="tPlanNo" value="">
        <INPUT type="text" name="tPlanName" id="tPlanName" class=BigStatic size="16" value="">
        <a href="javascript:;" class="orgAdd" onClick="LoadWindow()">选择</a>
  <a href="javascript:;" class="orgClear" onClick="ClearUser('tPlanNo', 'tPlanName')">清除</a>
    </tr>
    </tr>
    <tr>
     <td nowrap class="TableData">培训机构：</td>
      <td class="TableData">
       <input type="text" name="tInstitutionName" id="tInstitutionName" size="20" class="BigInput" >
      </td>
      <td nowrap class="TableData">培训费用：</td>
      <td class="TableData">
       <input type="text" name="trainningCost" id="trainningCost" size="10" class="BigInput" >
      </td>
    </tr>
  <tr align="center" class="TableControl">
       <td colspan="6" nowrap>
       <input type="hidden" name="toId" id="toId" value=""> 
         <input type="button" value="查询" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
       </td>
   </tr>          
</table>
</form>
</body>
</html>