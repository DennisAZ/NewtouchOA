<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
String date = dateFormat.format(new Date());
YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
%>

<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<title>新增支票领用</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<SCRIPT language=javascript> 
function checkForm() {
  if ($("aClaimer").value.trim() == "") {
    alert("领用人不能为空!");
    $("aClaimer").focus();
    $("aClaimer").select();
    return false;
  }
  if ($("budgetName").value.trim() == "") {
    alert("预算经费名称不能为空!");
    $("budgetName").focus();
    $("budgetName").select();
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
  if ($("deptDirectorName").value.trim() == "") {
    alert("部门主管不能为空!");
    $("deptDirectorName").focus();
    $("deptDirectorName").select();
    return false;
  }
  if ($("deptDirectorContent").value == "") {
    alert("主管审批内容不能为空!");
    $("deptDirectorContent").focus();
    $("deptDirectorContent").select();
    return false;
  }
  if ($("financeDirectorName").value == "") {
     alert("财务主管审批人不能为空!");
     $("financeDirectorName").focus();
     $("financeDirectorName").select();
     return false;
  }
  if ($("financeSignatoryName").value == "") {
     alert("财务签发人不能为空!");
     $("financeSignatoryName").focus();
     $("financeSignatoryName").select();
     return false;
  }
  document.getElementById("deptId").value = document.getElementById("userIdDesc").value;
  return true;
}
function checkForm2() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var url = contextPath + "/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/addRecord.act";
    var rtJson = getJsonRs(url,pars);
    if (rtJson.rtState == '0') {
      alert("保存成功！");
      parent.opener.location.reload();
      parent.window.close();
    } else {
      alert(rtJson.rtMsrg); 
    } 
  }
}
//验证数字
function isNumber(aValue) {
  var digitSrc = "0123456789";
  aValue = "" + aValue;
  if (aValue.substr(0, 1) == "-") {
    aValue = aValue.substr(1, aValue.length - 1);
  }
  var strArray = aValue.split(".");
  // 含有多个“.”
  if (strArray.length > 2) {
    return false;
  }
  var tmpStr = "";
  for (var i = 0; i < strArray.length; i++) {
    tmpStr += strArray[i];
  }
 
  for (var i = 0; i < tmpStr.length; i++) {
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i));
    if (tmpIndex < 0) {
      // 有字符不是数字
      return false;
    }
  }
  // 是数字
  return true;
}

function doInit() {
  //处理千份符
  //bindKiloSplitPrcBatch(["budgetMoney", "budgetInMoney","_PRICE","_MONEY","_PRICE_IN","_MONEY_IN"], "form1");
  bindKiloSplitPrcBatch(["applyMoney"], "form1");
  var date = new Date();
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  if (month < 10) {
    month = "0" + month;
  }
  if (day < 10) {
    day = "0" + day;
  }
  var zong = year + "-" + month + "-" + day;
  document.getElementById("applyDate").value = zong;
  document.getElementById("financeDirectorDate").value = zong; 
  document.getElementById("deptDirectorDate").value = zong;
  document.getElementById("signDate").value = zong;
  
  showCalendar('applyDate',false,'beginDateImg');
  showCalendar('deptDirectorDate',false,'beginDateImg2');
  showCalendar('financeDirectorDate',false,'beginDateImg3');
  showCalendar('signDate',false,'beginDateImg4');
  
  if(document.getElementById("userId").value.trim() != ""){
    bindDesc([{cntrlId:"userId", dsDef:"PERSON,SEQ_ID,DEPT_ID"}]);
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
/**
 * 打开模态窗口,能改变窗口大小
 */

</SCRIPT>
</head>
<body class="bodycolor" onload="doInit()" style="margin:0;padding:0;">
<div id="edit_body" style="overflow:auto;">
<div>
<img src="<%=imgPath%>/edit.gif" align="absmiddle">&nbsp;<span class="big3">新增支票领用</span>
</div>
<form method="post" id="form1" name="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.finance.data.YHFinanceApplyRecord">
<input type="hidden" name="runId" id="runId" value="0">
<input type="hidden" name="applyYear" id="applyYear" value="<%=date.substring(0,4)%>">
<input type="hidden" name="signType" id="signType" value="1">
<input type="hidden" name="expense" id="expense" value="0">
<input type="hidden" name="isPrint" id="isPrint" value="0">
<input type="hidden" name="operator" id="operator" value="<%=user.getSeqId()%>">
<input type="hidden" name="operateDate" id="operateDate" value="<%=date%>">
<input type="hidden" name="chargeId" id="chargeId" value="0">
<input type="hidden" name="applyProject" id="applyProject" value="0">
<input type="hidden" name="deptId" id="deptId" value="">

<input type="hidden" name="userId" id="userId" value="<%=user.getSeqId()%>">
<input type="hidden" name="userIdDesc" id="userIdDesc" value="">

<table width="90%" align="center" class="TableBlock">
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 支票领用信息 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;领用人姓名：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="hidden" name="applyClaimer"  id="applyClaimer" value="">
        &nbsp;<input type="text" name="aClaimer" id="aClaimer" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['applyClaimer','aClaimer']);" title="选择">
    </td>
   </tr>  
    <tr>
      <td nowrap class="TableContent">&nbsp;预算经费(项目)：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="text" name="budgetId" id="budgetId" size="30" maxlength="100" class="BigInput" value="" style="display:none;">
          &nbsp;<input type="text" name="budgetName" id="budgetName" size="30" maxlength="100" class="BigInput" value="">
              &nbsp;<input type="button" value="选择" class="SmallButton" onclick="toBudget('budgetId','budgetName');">
      </td>
    </tr>  
   <tr>
      <td nowrap class="TableContent">&nbsp;经费来源(项目)：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<input type="text" name="applyItem" id="applyItem" size="30" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;支票日期：</td>
      <td class="TableData">
     &nbsp;<input type="text" name="applyDate" id="applyDate" size="20" maxlength="19" class="BigInput" readonly>
        <img src="<%=imgPath%>/calendar.gif" border="0" align="absMiddle" style="cursor:pointer" id="beginDateImg" name="beginDateImg">&nbsp;
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;领用金额：</td>
      <td class="TableData">
      <!--  onblur="checkMoney('applyMoney2')" <input value="0" name="applyMoney" id="applyMoney" type="hidden">-->
      &nbsp;<input type="text" name="applyMoney" id="applyMoney" size="15" maxlength="15" class="BigInput" value="0" style="text-align:right;"/>&nbsp;只能为数字      
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;用途(备注)：</td>
      <td class="TableData">&nbsp;<textarea cols=50 name="applyMemo" id="applyMemo" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
  <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;支票信息 </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;支票号：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<input type="text" name="chequeAccount" id="chequeAccount" size="30" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;部门审批 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;部门主管：<label style="color: red">*</label></td>
      <td class="TableData"><input type="hidden" name="deptDirector" id="deptDirector" value="">
        &nbsp;<input type="text" name="deptDirectorName" id="deptDirectorName" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['deptDirector','deptDirectorName']);" title="选择" name="button">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;部门主管审批时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="deptDirectorDate" id="deptDirectorDate" size="20" maxlength="19" class="BigInput" readonly>
      <img src="<%=imgPath%>/calendar.gif" border="0" align="absMiddle" style="cursor:pointer" id="beginDateImg2" name="beginDateImg2">
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;部门主管审批内容：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="deptDirectorContent" id="deptDirectorContent" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;财务主管审批 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;财务主管审批：<label style="color: red">*</label></td>
      <td class="TableData"><input type="hidden" name="financeDirector" id="financeDirector" value="">
        &nbsp;<input type="text" name="financeDirectorName" id="financeDirectorName" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['financeDirector','financeDirectorName']);" title="选择" name="button">
    </td>
   </tr>
  <tr><td nowrap class="TableContent">&nbsp;财务主管审批时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="financeDirectorDate" id="financeDirectorDate" size="20" maxlength="19" class="BigInput" readonly>
      &nbsp;<img src="<%=imgPath%>/calendar.gif"  border="0" align="absMiddle" style="cursor:pointer" id="beginDateImg3" name="beginDateImg3">
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;财务主管审批内容：</td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="financeDirectorContent" id="financeDirectorContent" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;财务签发 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;财务签发人：<label style="color: red">*</label></td>
      <td class="TableData"><input type="hidden" name="financeSignatory" id="financeSignatory" value="">
        &nbsp;<input type="text" name="financeSignatoryName" id="financeSignatoryName" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['financeSignatory','financeSignatoryName']);" title="选择" name="button">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;签发时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="signDate" id="signDate" size="20" maxlength="19" class="BigInput" readonly>
      &nbsp;<img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="beginDateImg4" name="beginDateImg4">
      </td>
  </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
     <input type="hidden" name="aYear" id="aYear" value="" >
     <input type="hidden" name="aId" id="aId" value="" >
     <input type="button" onClick="checkForm2()" value="保存" class="BigButton" >&nbsp;&nbsp;
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
  </td>
  </tr>
   </table>
     </form>
</div>
</body>
</html>
