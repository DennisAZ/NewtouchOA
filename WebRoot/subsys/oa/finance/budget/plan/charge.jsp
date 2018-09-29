<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>费用报销管理</title>
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
<script type="text/javascript"> 
function checkOne(el) {
  if(!el.checked) {
    document.getElementById("allbox").checked = false;
  }
}
//全部选中
function checkAll() {
  for (var i = 0;i < document.getElementsByName("emailSelect").length;i++) {
    if (document.getElementsByName("allbox")[0].checked) {
      document.getElementsByName("emailSelect").item(i).checked=true;
    }else {
      document.getElementsByName("emailSelect").item(i).checked=false;
    }
  }
  if (i == 0) {
    if (document.getElementsByName("allbox")[0].checked) {
      document.getElementsByName("emailSelect").checked = true;
    }else {
      document.getElementsByName("emailSelect").checked = false;
    }
  }
}
//打印
function printMail() {
  var printStr = "";
  for(var i = 0; i < document.getElementsByName("emailSelect").length;i++) {
    var el = document.getElementsByName("emailSelect").item(i);
    if (el.checked) {
      var val = el.value;
      printStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("emailSelect");
    if (el.checked) {
      var val = el.value;
      printStr += val + ",";
    }
  }
  if(printStr == "") {
    alert("请至少选择其中一条记录。");
    return;
  }
  msg = '确认要打印所选记录？';
  if(window.confirm(msg)) {
    URL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/updatePrint.act?printStr=" + printStr
    window.open(URL);
  }
}

//报销
function reimbursemenMail() {
  var printStr = "";
  for(var i = 0; i < document.getElementsByName("emailSelect").length;i++) {
    var el = document.getElementsByName("emailSelect").item(i);
    if (el.checked) {
      var val = el.value;
      printStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("emailSelect");
    if (el.checked) {
      var val = el.value;
      printStr += val + ",";
    }
  }
  if(printStr == "") {
    alert("请至少选择其中一条记录。");
    return;
  }
  msg = '确认要报销所选记录？';
  if (window.confirm(msg)) {
    URL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/expenseOut.act?printStr=" + printStr
    window.open(URL);
  }
}
 //作废
function makeWaste() {
  var printStr = "";
  for(var i = 0; i < document.getElementsByName("emailSelect").length;i++) {
    var el = document.getElementsByName("emailSelect").item(i);
    if (el.checked) {
      var val = el.value;
      printStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("emailSelect");
    if (el.checked) {
      var val = el.value;
      printStr += val + ",";
    }
  }
  if(printStr == "") {
    alert("请至少选择其中一条记录。");
    return;
  }
  msg = '确认要作废所选记录？';
  if(window.confirm(msg)) {
    URL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/makeWaste.act?printStr=" + printStr
    window.open(URL);
  }
}
//时间
function doInitTime() { 
  showCalendar('chargeDate',false,'beginDateImg');
  showCalendar('chargeDate2',false,'endDateImg');
  dept();
  var opt = <%=request.getParameter("deptId")%>
  if (opt == null) {
    opt = 0;
  }
  selectValue(opt)
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
function selectValue(val){
  var otypo = document.getElementById("deptId");
  for (var i = 0; i < otypo.options.length; i++) {
      if (otypo.options[i].value == val) {
        otypo.options[i].selected = true;
      }
  }
}
//加载数据
function doInit(){
  doInitTime();
  var timestamps = new Date().valueOf();
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/expenseSelect2.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"selfdef", name:"Id", text:"选择",align:"center", width:"3%",render:toMing},
       {type:"text", name:"deptId", text:"部门名称", width: "7%",align:"center"},
       {type:"text", name:"chargeUser", text:"报销申请人", width: "8%",align:"center"},
       {type:"text", name:"projId", text:"团组名称", width: "6%",align:"center"},
       {type:"text", name:"chargeMoney", text:"报销金额", width: "5%",align:"center"},
       {type:"text", name:"chargeDate", text:"报销日期",align:"center", width: "6%",render:toDate},
       {type:"text", name:"runId", text:"操作",align:"center", width: "12%",render:toRunId},
       {type:"text", name:"isPrint", text:"打印状态",align:"center",width:"7%",render:toIsPrint},
       {type:"text", name:"expense", text:"报销状态", width:"5%",align:"center",render:toExpense},
       {type:"text", name:"ofEx", text:"作废标记",width:"5%",align:"center",render:toOfEx}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的报销支票</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }
    var div = new Element('div',{"class":"TableControl"}).update("&nbsp;&nbsp;<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>"
        + "&nbsp;全选&nbsp;<input type='button'  class='BigButton' value='  批量打印  ' onClick='printMail();' title='批量打印'> &nbsp;"
        + "<input type='button'  class='BigButton' value=' 报销处理  'onClick='reimbursemenMail();' title='报销状态'> &nbsp;");
    $('giftList').appendChild(div); 
}
//返回操作 项
function toMing(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox' name='emailSelect' value='" + seqId +"' onClick='javscript:checkOne(self);'>";
}
//时间前10
function toDate(cellData, recordIndex, columIndex){
  var createDate = this.getCellData(recordIndex,"chargeDate");
  return createDate.substr(0,10);
}
function getParam(){
  queryParam = $("form1").serialize();
  return queryParam;
}
function toRunId(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var seqId =  this.getCellData(recordIndex,"seqId");
  if (runId != null && runId != "" && runId > 0) {
    return "<a href='javascript:runCreate(" + runId +")'>工作流详情</a>";
  }
  if (runId == null || runId == "" || runId <= 0) {
    var paramStr = "<a href='javascript:chargeDetail(" + seqId +")'>查看详情</a>&nbsp;<a href='javascript:editCharge(" + seqId +")'>编辑</a>&nbsp;<a href='javascript:delCharge(" + seqId +")'>删除记录</a>";
    return paramStr;
  }
}

function toIsPrint(cellData, recordIndex, columIndex) {
  var isPrint = this.getCellData(recordIndex,"isPrint");
  if (isPrint == 0) {
    return "<font color='red'>未打印</font>";
  }
  if (isPrint == 1) {
    return "<font color='blue'>已打印 </font>";
  }
}
function toExpense(cellData, recordIndex, columIndex){
  var expense = this.getCellData(recordIndex,"expense");
  if (expense == 0) {
    return "<font color='red'>未报销</font>";
  }
  if (expense == 1) {
    return "<font color='blue'>已报销</font>";
  }
}
function toOfEx(cellData, recordIndex, columIndex){
  var ofEx = this.getCellData(recordIndex,"ofEx");
  if (ofEx == 0) {
    return "<font color='blue'>正常使用</font>";
  }
  if (ofEx == 1) {
    return "<font color='red'>已作废</font>";
  }
}
function chargeDetail(seqId) {
  myleft = (screen.availWidth-800)/2;
  window.open("<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/selectExpense.act?seqId=" + seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
}
function editCharge(seqId) {
  myleft = (screen.availWidth-800)/2;
  window.open("<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/selectCharge.act?seqId=" + seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
}
function delCharge(seqId) {
  URL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/delCharge.act?seqId=" + seqId;
  if (window.confirm("确认要删除该记录吗？")) {
    window.location = URL;
    location.reload();
  }
}
function runCreate(runId){
  return;
}

//查询
function queryGift(){
  if (document.getElementById("chargeMoney").value != "") {
    var obj = document.getElementById("chargeMoney");
    var val = obj.value;
    if (isNumber(val)) {
    }else {
      alert("非法数字");
      document.getElementById("chargeMoney").focus();
      return false;
    }
  }
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('giftList').style.display="";
    $('returnNull').style.display="none"; 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的报销支票</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').style.display=""; 
    $('returnNull').update(table);  
  }
} 
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
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<br>
<div style="padding-left: 10px;">
<form method="post" name="form1" id="form1">
<input type="hidden" id="year" name="year" value="<%=request.getParameter("year")%>">
<input type="hidden" value="0" name="expense" id="expense">
<table class="TableList" align="center">
  <tr class="TableContent">
    <td nowrap style="padding:3px">
          部门名称：
      <select name="deptId" id="deptId">
      </select>
         &nbsp;报销申请人：
        <input type="text" name="chargeUserName" id="chargeUserName" value="" size="10" class="BigInput" readonly>
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['chargeUser','chargeUserName']);">选择</a>
      <input type="hidden" name="chargeUser" id="chargeUser" value="">
      <a href="javascript:;" class="orgClear" onClick="$('chargeUserName').value='';$('chargeUser').value='';">清空</a>
         &nbsp;报销申请时间：
       <INPUT type="text" class="BigInput" name="chargeDate" id="chargeDate" size="10" readonly>
       <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" name="beginDateImg" id="beginDateImg">
       &nbsp;至
       <INPUT type="text" class="BigInput" name="chargeDate2" id="chargeDate2" size="10" readonly>
       <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" name="endDateImg" id="endDateImg">  
      </td>
  </tr>
  <tr class="TableContent">
    <td nowrap>
    &nbsp;报销项目：
    <INPUT type="text"  name="chargeItem" id="chargeItem" size="10" class="BigInput">
     &nbsp;财务审批人：
      <input type="text" name="financeAuditUserId" id="financeAuditUserId" value="" size="10" class="BigInput" readonly>
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['financeAuditUser','financeAuditUserId']);">选择</a>
      <input type="hidden" name="financeAuditUser" id="financeAuditUser" value="">
      <a href="javascript:;" class="orgClear" onClick="$('financeAuditUser').value='';$('financeAuditUserId').value='';">清空</a>
      &nbsp;报销金额：
     <INPUT type="text"  name="chargeMoney" id="chargeMoney" size="10" class="BigInput">
     </td>
    </tr>  
  <tr class="TableControl">
    <td rowspan=2 align="center">
      <input value="查询" type="button" class="BigButton" title="查询" onClick="queryGift()">&nbsp;&nbsp;
    </td>
  </tr>   
</table>
</form>
</div>
<br>
<div id="giftList" style="padding-left: 10px;"></div>
<div id="returnNull"></div>
</body>
</html>
