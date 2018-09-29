<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.finance.data.YHChargeExpense"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
String date = dateFormat.format(new Date());
%>
<html>
<head>
<title>新增费用报销信息&nbsp;&nbsp;</title>
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
  if($("deptId").value == "") {
     alert("部门名称不能为空!");
     $("deptId").focus();
     $("deptId").select();
     return false;
  }
  if($("chargeUserDesc").value == "") {
     alert("报销申请人不能为空!");
     $("chargeUserDesc").focus();
     $("chargeUserDesc").select();
     return false;
  }
  if ($("budgetIdDesc").value.trim() == "") {
    alert("预算经费名称不能为空!");
    $("budgetIdDesc").focus();
    $("budgetIdDesc").select();
    return false;
  }
  if($("deptAuditUserDesc").value == "") {
     alert("部门审批人不能为空!");
     $("deptAuditUserDesc").focus();
     $("deptAuditUserDesc").select();
     return false;
  }
  if($("deptAuditDate").value == "") {
    alert("部门审批时间不能为空!");
    $("deptAuditDate").focus();
    $("deptAuditDate").select();
    return false;
  }
  if($("deptAuditContent").value == "") {
     alert("部门审批内容不能为空!");
     $("deptAuditContent").focus();
     $("deptAuditContent").select();
     return false;
  }
  if($("financeAuditUserDesc").value == "") {
     alert("财务审批人不能为空!");
     $("financeAuditUserDesc").focus();
     $("financeAuditUserDesc").select();
     return false;
  }
  if($("financeAuditDate").value == "") {
    alert("财务审批时间不能为空!");
    $("financeAuditDate").focus();
    $("financeAuditDate").select();
    return false;
  }
  if($("financeAuditContent").value == "") {
     alert("财务审批内容不能为空!");
     $("financeAuditContent").focus();
     $("financeAuditContent").select();
     return false;
  }
  tbOutput();
  //处理千份符
  var re1 = /\,/gi;
  document.getElementById("chargeMoney").value = document.getElementById("chargeMoney2").value.replace(re1,'');
  return true;
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
function checkForm1() {
  if ($("_MONEY").value == "") {
    alert("金钱不能为空!");
    $("_MONEY").focus()
    return false;
  }
  if ($("_MONEY").value != "") {
    if (isNumber($("_MONEY").value)) {
    }else {
      alert("金钱应为数字!");
      $("_MONEY").focus()
      $("_MONEY").select();
      return false;
    }
  }
  return true;
}
function tb_addnew() {
if (checkForm1()) {
  var sumMoney = document.getElementById('chargeMoney2');
  var re1 = /\,/gi;
  var moneyTemp = sumMoney.value.replace(re1,'');
  if (moneyTemp == '') {
    moneyTemp = 0;
  }
  var now_money = moneyTemp;
  if(document.getElementById("chargeMoney2").value != ""){
    now_money = parseFloat(moneyTemp) - parseFloat(document.getElementById("chargeMoney2").value);
  }
  if (document.getElementById('_MONEY').value != "") {
    sumMoney.value = insertKiloSplit(parseFloat(now_money) + parseFloat(document.getElementById("_MONEY").value),2); //转换为带千位符号的
  }
  if(document.getElementById("MODIFY_ROWINDEX").value != "-1") {
    currRowIndex = Number(document.getElementById("MODIFY_ROWINDEX").value);
    lv_tb_id = "MYTABLE";
    $(lv_tb_id).rows[currRowIndex].cells[0].innerHTML = document.getElementById("_ITEM").value;
    $(lv_tb_id).rows[currRowIndex].cells[1].innerHTML = document.getElementById("_MEMO").value;
    $(lv_tb_id).rows[currRowIndex].cells[2].innerHTML = document.getElementById("_CHEQUE").value;
    $(lv_tb_id).rows[currRowIndex].cells[3].innerHTML = document.getElementById("_MONEY").value;
    
   document.getElementById("chargeMoney2").value = insertKiloSplit(parseFloat(moneyTemp) - parseFloat(document.getElementById("chargeMoney").value) + parseFloat(document.getElementById("_MONEY").value),2);

   document.getElementById("MODIFY_ROWINDEX").value = "-1";
   document.getElementById("_ITEM").value = "";
   document.getElementById("_MEMO").value = "";
   document.getElementById("_CHEQUE").value = "";
   document.getElementById("_MONEY").value = "";
  }else {
  var mytable = $("MYTABLE");
  rownum = mytable.rows.length;
  mynewrow = mytable.insertRow(rownum);
  mynewrow.className = "TableControl";

  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = document.getElementById("_ITEM").value;
  mynewcell.innerHTML = cell_html;
  document.getElementById("_ITEM").value = "";

  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = document.getElementById("_MEMO").value;
  mynewcell.innerHTML = cell_html;
  document.getElementById("_MEMO").value = "";

  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = document.getElementById("_CHEQUE").value;
  mynewcell.innerHTML = cell_html;
  document.getElementById("_CHEQUE").value = "";

  if(document.getElementById("_MONEY").value != ""){
    sumMoney.value = insertKiloSplit(parseFloat(moneyTemp) + parseFloat(document.getElementById("_MONEY").value),2);
  }

  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = document.getElementById("_MONEY").value;
  mynewcell.innerHTML = cell_html;
  document.getElementById("_MONEY").value = "";
  
  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = "<a href=#this onclick=\"tb_edit(this);\">编辑</a> <a href=#this onclick=\"ke_long(this);\">克隆</a> <a href=#this onclick=\"tb_delete(this);\">清除</a>";
  mynewcell.innerHTML = cell_html;
 }
}
}

function tb_delete(objra) {
  var objTD = objra.parentNode;
  var objTR = objTD.parentNode;
  var sumMoney = document.getElementById('chargeMoney2');//得到支出金额 
  var money = objTR.cells[3].innerHTML;
  //去掉千位符号
  var re1 = /\,/gi;
  var moneyTemp = sumMoney.value.replace(re1,'');
  if(moneyTemp == ''){
    moneyTemp = 0;
  }
  if(money != ""){
    sumMoney.value = insertKiloSplit(parseFloat(moneyTemp) - parseFloat(money,10),2);//
  }
  document.getElementById("MODIFY_ROWINDEX").value = "-1";
  var objTable = objTR.parentNode;
  currRowIndex = objTR.rowIndex;
  var mytable = $("MYTABLE");
  mytable.deleteRow(currRowIndex);
}
function ke_long(objra) {
  var objTD = objra.parentNode;
  var objTR = objTD.parentNode;
  var objTable = objTR.parentNode;
  currRowIndex = objTR.rowIndex;
  document.getElementById("MODIFY_ROWINDEX").value = currRowIndex;
  var mytable = $("MYTABLE");
  lv_tb_id = "MYTABLE";
  rownum = mytable.rows.length;
  mynewrow = mytable.insertRow(rownum);
  mynewrow.className="TableControl";

  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = $(lv_tb_id).rows[currRowIndex].cells[0].innerHTML;
  mynewcell.innerHTML = cell_html;

  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align="center";
  cell_html = $(lv_tb_id).rows[currRowIndex].cells[1].innerHTML;
  mynewcell.innerHTML = cell_html;

  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = $(lv_tb_id).rows[currRowIndex].cells[2].innerHTML;
  mynewcell.innerHTML=cell_html;
  
  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = $(lv_tb_id).rows[currRowIndex].cells[3].innerHTML;
  mynewcell.innerHTML = cell_html;

  var sumMoney = document.getElementById('chargeMoney2');//得到支出金额 
  //去掉千位符号
  var re1 = /\,/gi;
  var moneyTemp = sumMoney.value.replace(re1,'');
  if(moneyTemp == ''){
    moneyTemp = 0;
  }
  if(cell_html != ""){
    sumMoney.value = insertKiloSplit(parseFloat(moneyTemp) + parseFloat(cell_html,10),2);//
  }
  
  var cellnum = mynewrow.cells.length;
  mynewcell = mynewrow.insertCell(cellnum);
  mynewcell.align = "center";
  cell_html = "<a href=#this onclick=\"tb_edit(this);\">编辑</a> <a href=#this onclick=\"ke_long(this);\">克隆</a> <a href=#this onclick=\"tb_delete(this);\">清除</a>";
  mynewcell.innerHTML = cell_html;
}
function tb_edit(objra) {
   var objTD = objra.parentNode;
   var objTR = objTD.parentNode;
   var objTable = objTR.parentNode;
   currRowIndex = objTR.rowIndex;
   document.getElementById("MODIFY_ROWINDEX").value = currRowIndex;
   var mytable= $("MYTABLE");
   lv_tb_id= "MYTABLE";
   var data_str = "";

   document.getElementById("_ITEM").value = $(lv_tb_id).rows[currRowIndex].cells[0].innerHTML;
   document.getElementById("_MEMO").value = $(lv_tb_id).rows[currRowIndex].cells[1].innerHTML;
   document.getElementById("_CHEQUE").value = $(lv_tb_id).rows[currRowIndex].cells[2].innerHTML;
   document.getElementById("_MONEY").value = $(lv_tb_id).rows[currRowIndex].cells[3].innerHTML;
   document.getElementById("chargeMoney").value = $(lv_tb_id).rows[currRowIndex].cells[3].innerHTML;
}
function tbOutput() {
  lv_tb_id = "MYTABLE";
  var data_str = "";
  for (i=1; i < $(lv_tb_id).rows.length; i++) {
    for (j=0; j < $(lv_tb_id).rows[i].cells.length-1; j++) {
      data_str+=$(lv_tb_id).rows[i].cells[j].innerHTML+"`";
    }
    data_str += "\n";
  }
  document.getElementById("chargeItem").value = data_str;
}
function dept(){
//得到部门
  var deptId = '1';
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/selectDeptToGift.act";
  var rtJson = getJsonRs(requestUrl);
  if (rtJson.rtState == "1") {
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i = 0; i < prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value;
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
}
//时间
function doInit() {
  bindKiloSplitPrcBatch(["chargeMoney2"],"form1");
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
  document.getElementById("deptAuditDate").value = zong;
  document.getElementById("financeAuditDate").value = zong; 
  
  showCalendar('deptAuditDate',false,'beginDateImg');
  showCalendar('financeAuditDate',false,'endDateImg');
  dept();
}
var budgetIdField = null;
function toBudget(budgetId,budgetName){
  budgetIdField = budgetId;
  budgetNameField = budgetName;
  var URL= contextPath + "/subsys/oa/finance/budget/plan/budgetlist.jsp";
  openDialogResize(URL , 500, 355);
 // window.open(URL,this,"height=355px,width=320px,directories=no,menubar=no,toolbar=no,status=no,scrollbars=yes,location=no,top="+loc_y+",left="+loc_x+""); 
}
function checkForm2() {
  if (checkForm()) {
    var pars = $('form1').serialize() ;
    var url = contextPath + "/yh/subsys/oa/finance/act/YHChargeExpenseAct/addExpense.act";
    var rtJson = getJsonRs(url,pars);
    if (rtJson.rtState == '0') {
      alert("添加成功！");
      parent.opener.location.reload();
      parent.window.close();
      } else {
        alert(rtJson.rtMsrg); 
      } 
  }
}
</SCRIPT>
</head>
<body style="margin:0;padding:0;" onLoad="doInit()">
<div id="edit_body" style="overflow:auto;" SCROLL=auto>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3"> 新增费用报销信息</span>&nbsp;<br>
    </td>
  </tr>
</table>
<form method="post" name="form1" id="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.finance.data.YHChargeExpense">
<input value="<%=date%>" type="hidden" name="chargeDate" id="chargeDate">
<input value="0" type="hidden" name="runId" id="runId">
<input value="<%=date.substring(0,4)%>" type="hidden" name="chargeYear" id="chargeYear">
<input value="0" type="hidden" name="costId" id="costId">
<input value="0" type="hidden" name="projSign" id="projSign">
<input value="0" type="hidden" name="isPrint" id="isPrint">
<input value="0" name="expense" id="expense" type="hidden">
<input value="0" name="makeWaste" id="makeWaste" type="hidden">
<input value="0" name="ofEx" id="ofEx" type="hidden">
<input value="0" name="settleFlag" id="settleFlag" type="hidden">

<table width="90%" align="center" class="TableBlock">
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 新增费用报销信息 </td>
    </tr>
   <tr>
      <td nowrap class="TableContent">&nbsp;部门名称：<label style="color: red">*</label></td>
      <td class="TableData">
      &nbsp;<select name="deptId" id="deptId">
      <option value="">--请选择部门--</option>
      </select>
      </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;报销申请人：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="hidden" name="chargeUser" id="chargeUser" value="">
        &nbsp;<input type="text" name="chargeUserDesc" id="chargeUserDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['chargeUser','chargeUserDesc']);" title="选择" name="button">
    </td>
   </tr> 
    <tr>
      <td nowrap class="TableContent">&nbsp;预算项目：<label style="color: red">*</label></td>
      <td class="TableData">
      <input type = "hidden" id = "budgetId" name="budgetId" value=""></input>
      &nbsp;<input type = "text" id = "budgetIdDesc" name="budgetIdDesc" value="" readonly></input>
       &nbsp;<input type = "button" title="选择" value="选择" class="SmallButton" onclick="toBudget('budgetId','budgetIdDesc');"></input>
      </td>
    </tr>
   <tr>
    <td nowrap colspan="2">
  <table id="MYTABLE" width="100%"  class="small">
        <tr  class="TableHeader"  align="center">
           <td>项目</td>
           <td>摘要</td>
           <td>支票号</td>
           <td>金额</td>
           <td>操作</td>
        </tr>
       </table>
       <br>
       &nbsp;项目：
        <input type="text" name="_ITEM" id="_ITEM" value="" size='15'>&nbsp;
       &nbsp;
       摘要：
        <input type="text" name="_MEMO" id="_MEMO" value="" size='30'>&nbsp;
       &nbsp;
       支票号：
        <input type="text" name="_CHEQUE" id="_CHEQUE" value="" size='6'>&nbsp;
       &nbsp;
       金额：
        <input type="text" name="_MONEY" id="_MONEY" value="" size='10'>&nbsp;
        <input type="button"  class="SmallButton" value="保存" onclick="tb_addnew();">
        <INPUT type="hidden" name="MODIFY_ROWINDEX"  id="MODIFY_ROWINDEX" value="-1">
    </td>
   </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;报销金额：</td>
      <td class="TableData">
      &nbsp;<input type="text"  name="chargeMoney2" id="chargeMoney2" value="0" size="15" maxlength="15" class="BigInput" style="text-align:right;"/>&nbsp;只能为数字            <input value="0" name="chargeMoney" id="chargeMoney" type="hidden">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;备注：</td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="chargeMemo" id="chargeMemo" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    
      <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 部门审批 </td>
    </tr>
 
   <tr>
    <td nowrap class="TableContent">&nbsp;部门审批人：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="hidden" name="deptAuditUser" id="deptAuditUser" value="">
        &nbsp;<input type="text" name="deptAuditUserDesc" id="deptAuditUserDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['deptAuditUser','deptAuditUserDesc']);" title="选择" name="button">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;部门审批时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="deptAuditDate" id="deptAuditDate" size="20" maxlength="19" class="BigInput" value="" readonly>
      <img src="<%=imgPath%>/calendar.gif"  id="beginDateImg" name="beginDateImg" border="0" align="absMiddle" style="cursor:pointer">
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;部门审批内容：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="deptAuditContent" id="deptAuditContent" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 财务审批 </td>
    </tr>
 
   <tr>
    <td nowrap class="TableContent">&nbsp;财务审批人：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="hidden" name="financeAuditUser" id="financeAuditUser" value="">
        &nbsp;<input type="text" name="financeAuditUserDesc" id="financeAuditUserDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
        &nbsp;<input type="button" value="选 择" class="SmallButton" onClick="selectSingleUser(['financeAuditUser','financeAuditUserDesc']);" title="选择" name="button">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;财务审批时间：</td>
      <td class="TableData">
      &nbsp;<input type="text" name="financeAuditDate" id="financeAuditDate" size="20" maxlength="19" class="BigInput" value="" readonly>
      <img src="<%=imgPath%>/calendar.gif" name="endDateImg" id="endDateImg" border="0" align="absMiddle" style="cursor:pointer">
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;财务审批内容：<label style="color: red">*</label></td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="financeAuditContent" id="financeAuditContent" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      <input type="hidden" name="chargeItem" id="chargeItem" >
     <input type="button" onClick="checkForm2()" value="保存" class="BigButton" >&nbsp;&nbsp;
     <input type="reset"  value="重填 " class="BigButton" >&nbsp;&nbsp;
     <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
  </td>
  </tr>
   </table>
      </form>
</div>
</body>
</html>
