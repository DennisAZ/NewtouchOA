<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.subsys.oa.finance.data.YHFinanceApplyRecord"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHFinanceApplyRecord applyRecord = (YHFinanceApplyRecord)request.getAttribute("record");
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
String date = dateFormat.format(new Date());
%>
<html>
<head>
<title>修改支票领用</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>

<SCRIPT language=javascript> 
function checkForm() {
  if ($("applyClaimerDesc").value.trim() == "") {
    alert("领用人不能为空!");
    $("applyClaimerDesc").focus();
    $("applyClaimerDesc").select();
    return false;
  }
  if ($("budgetIdDesc").value.trim() == "") {
    alert("预算经费名称不能为空!");
    $("budgetIdDesc").focus();
    $("budgetIdDesc").select();
    return false;
  }
  if ($("applyItem").value.trim() == "") {
    alert("项目名称不能为空!");
    $("applyItem").focus();
    $("applyItem").select();
    return false;
  }
  if ($("chequeAccount").value.trim() == "") {
    alert("收款方账号不能为空!");
    $("chequeAccount").focus();
    $("chequeAccount").select();
    return false;
  }
  if ($("deptDirectorDesc").value.trim() == "") {
    alert("部门主管不能为空!");
    $("deptDirectorDesc").focus();
    $("deptDirectorDesc").select();
    return false;
  }
  if ($("deptDirectorContent").value == "") {
    alert("主管审批内容不能为空!");
    $("deptDirectorContent").focus();
    $("deptDirectorContent").select();
    return false;
  }
  if ($("financeDirectorDesc").value == "") {
     alert("财务主管审批人不能为空!");
     $("financeDirectorDesc").focus();
     $("financeDirectorDesc").select();
     return false;
  }
  if ($("financeSignatoryDesc").value == "") {
     alert("财务签发人不能为空!");
     $("financeSignatoryDesc").focus();
     $("financeSignatoryDesc").select();
     return false;
  }
  return true;
}
function doInit() {
  bindKiloSplitPrcBatch(["applyMoney"],"form1");
  showCalendar('applyDate',false,'beginDateImg');
  showCalendar('deptDirectorDate',false,'beginDateImg2');
  showCalendar('financeDirectorDate',false,'beginDateImg3');
  showCalendar('signDate',false,'beginDateImg4');
  checkName();
}

function checkForm2() {
  if (checkForm()) {
      var pars = $('form1').serialize() ;
      var url = contextPath + "/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/editRecord.act";
      var rtJson = getJsonRs(url,pars);
      if (rtJson.rtState == '0') {
        alert("修改成功！");
        parent.opener.location.reload();
        parent.window.close();
      } else {
          alert(rtJson.rtMsrg); 
      } 
  }
}
function checkName() {
  if(document.getElementById("applyClaimer").value.trim() != ""){
    bindDesc([{cntrlId:"applyClaimer", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("deptDirector").value.trim() != ""){
    bindDesc([{cntrlId:"deptDirector", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("financeDirector").value.trim() != ""){
    bindDesc([{cntrlId:"financeDirector", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("financeSignatory").value.trim() != ""){
    bindDesc([{cntrlId:"financeSignatory", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("budgetId").value.trim() != ""){
    bindDesc([{cntrlId:"budgetId", dsDef:"budget_apply,SEQ_ID,BUDGET_ITEM"}]);
  }
}
var budgetIdField = null;
var budgetNameField = null;
function toBudget(budgetId,budgetName){
  budgetIdField = budgetId;
  budgetNameField = budgetName;
  var URL= contextPath + "/subsys/oa/finance/budget/plan/budgetlist.jsp";
  openDialogResize(URL , 500, 355);
 // window.open(URL,this,"height=355px,width=320px,directories=no,menubar=no,toolbar=no,status=no,scrollbars=yes,location=no,top="+loc_y+",left="+loc_x+"");
  
}
</SCRIPT>
</head>
<body class="bodycolor" onload="doInit()" style="margin:0;padding:0;">
<%if (applyRecord !=null) {%>
<div id="edit_body" style="overflow:auto;">
<div>
<img src="<%=imgPath%>/edit.gif" align="absmiddle">&nbsp;<span class="big3">修改支票领用</span>
</div>
<form method="post" id="form1" name="form1" onSubmit="return checkForm();">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.finance.data.YHFinanceApplyRecord">
<input type="hidden" name="deptId" id="deptId" value="<%=applyRecord.getDeptId()%>">
<input type="hidden" name="applyYear" id="applyYear" value="<%=date.substring(0,4)%>">
<input type="hidden" name="signType" id="signType" value="<%=applyRecord.getSignType()%>">
<input type="hidden" name="seqId" id="seqId" value="<%=applyRecord.getSeqId()%>">
<input type="hidden" name="runId" id="runId" value="<%=applyRecord.getRunId()%>">
<input type="hidden" name="expense" id="expense" value="<%=applyRecord.getExpense()%>">
<input type="hidden" name="isPrint" id="isPrint" value="<%=applyRecord.getIsPrint()%>">
<input type="hidden" name="operator" id="operator" value="<%=applyRecord.getOperator()%>">
<input type="hidden" name="operateDate" id="operateDate" value="<%=applyRecord.getOperateDate().toString().substring(0,10)%>">
<table width="90%" align="center" class="TableBlock">
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 支票领用信息 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;领用人姓名：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="hidden" name="applyClaimer"  id="applyClaimer" value="<%=applyRecord.getApplyClaimer()%>">
        &nbsp;<input type="text" name="applyClaimerDesc" id="applyClaimerDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['applyClaimer','applyClaimerDesc']);" title="选择">
    </td>
   </tr>
   <tr>
      <td nowrap class="TableContent">&nbsp;预算经费(项目)：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="hidden" name="budgetId" id="budgetId" size="30" maxlength="30" class="BigInput" value="<%=applyRecord.getBudgetId()%>">
          &nbsp;<input type="text" name="budgetIdDesc" id="budgetIdDesc" size="30" maxlength="100" class="BigInput" value="">
              &nbsp;<input type="button" value="选择" class="SmallButton" onclick="toBudget('budgetId','budgetIdDesc');">
      </td>
    </tr>  
   <tr>
      <td nowrap class="TableContent">&nbsp;经费来源(项目)：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<input type="text" name="applyItem" id="applyItem" size="30" maxlength="100" class="BigInput" value="<%=applyRecord.getApplyItem().replace("\"","'")%>">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;领用日期：</td>
      <td class="TableData">
     &nbsp;<input type="text" name="applyDate" id="applyDate" size="20" value="<%=applyRecord.getApplyDate().toString().substring(0,10) %>" maxlength="19" class="BigInput" readonly>
        <img src="<%=imgPath%>/calendar.gif"  border="0" align="absMiddle" style="cursor:pointer" id="beginDateImg" name="beginDateImg">&nbsp;
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;领用金额：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="applyMoney" id="applyMoney" value="<%=YHUtility.getFormatedStr(applyRecord.getApplyMoney(),2)%>" size="15" maxlength="15" class="BigInput" style="text-align:right;"/>&nbsp;只能为数字
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;用途(备注)：</td>
      <td class="TableData">&nbsp;<textarea cols=50 name="applyMemo" id="applyMemo" rows="3" class="BigInput" wrap="yes"><%=applyRecord.getApplyMemo() %></textarea>
      </td>
    </tr>
 
  <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;支票信息 </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;支票号：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<input type="text" name="chequeAccount" id="chequeAccount" size="30" maxlength="100" class="BigInput" value="<%=applyRecord.getChequeAccount().replace("\"","'")%>">
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;部门审批 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;部门主管：<label style="color: red">*</label></td>
      <td class="TableData"><input type="hidden" name="deptDirector" id="deptDirector" value="<%=applyRecord.getDeptDirector() %>">
        &nbsp;<input type="text" name="deptDirectorDesc" id="deptDirectorDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['deptDirector','deptDirectorDesc']);" title="选择" name="button">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;部门主管审批时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="deptDirectorDate" id="deptDirectorDate" size="20" maxlength="19" value="<%=applyRecord.getDeptDirectorDate().toString().substring(0,10)%>" class="BigInput" readonly>
      <img src="<%=imgPath%>/calendar.gif" border="0" align="absMiddle" style="cursor:pointer" id="beginDateImg2" name="beginDateImg2">
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;部门主管审批内容：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="deptDirectorContent" id="deptDirectorContent" rows="3" class="BigInput" wrap="yes"><%=applyRecord.getDeptDirectorContent() %></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;财务主管审批 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;财务主管审批：<label style="color: red">*</label></td>
      <td class="TableData"><input type="hidden" name="financeDirector" id="financeDirector" value="<%=applyRecord.getFinanceDirector() %>">
        &nbsp;<input type="text" name="financeDirectorDesc" id="financeDirectorDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['financeDirector','financeDirectorDesc']);" title="选择" name="button">
    </td>
   </tr>
  <tr><td nowrap class="TableContent">&nbsp;财务主管审批时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="financeDirectorDate" id="financeDirectorDate" size="20" maxlength="19" value="<%=applyRecord.getFinanceDirectorDate().toString().substring(0,10)%>" class="BigInput" readonly>
      &nbsp;<img src="<%=imgPath%>/calendar.gif" border="0" align="absMiddle" style="cursor:pointer" id="beginDateImg3" name="beginDateImg3">
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;财务主管审批内容：</td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="financeDirectorContent" id="financeDirectorContent" rows="3" class="BigInput" wrap="yes"><%=applyRecord.getFinanceDirectorContent() %></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;财务签发 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;财务签发人：<label style="color: red">*</label></td>
      <td class="TableData"><input type="hidden" name="financeSignatory" id="financeSignatory" value="<%=applyRecord.getFinanceSignatory() %>">
        &nbsp;<input type="text" name="financeSignatoryDesc" id="financeSignatoryDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['financeSignatory','financeSignatoryDesc']);" title="选择" name="button">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;签发时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="signDate" id="signDate" size="20" maxlength="19" class="BigInput" value="<%=applyRecord.getSignDate().toString().substring(0,10)%>" readonly>
      &nbsp;<img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="beginDateImg4" name="beginDateImg4">
      </td>
  </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
     <input type="button"  value="保存" class="BigButton" onClick="checkForm2();">&nbsp;&nbsp;
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;
  </td>
  </tr>
   </table>
     </form>
</div>
<%}else { %>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>无符合条件的支票领用</div>
    </td>
  </tr>
</table>
<div align="center"> <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;</div>
 
 <input type="hidden" value="" id="applyClaimer" name="applyClaimer">
 <input type="hidden" value="" id="budgetId" name="budgetId">
 <input type="hidden" value="" id="financeSignatory" name="financeSignatory">
 <input type="hidden" value="" id="financeDirector" name="financeDirector">
 <input type="hidden" value="" id="deptDirector" name="deptDirector">
<%} %>
</body>
</html>
