<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>支票领用管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/finance/js/finance.js"></script>
<script type="text/javascript">
var pageMgr = null;
var cfgs = null;
function doInit(){
  var timestamps = new Date().valueOf();
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/applySelect.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    afterShow:addTr,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"selfdef", name:"Id", text:"选择",align:"center", width:"3%",render:toMing},
       {type:"text", name:"applyClaimer", text:"领用人姓名", width: "7%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"applyMoney", text:"领用金额", width: "7%",align:"center",render:toMoney,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"chequeAccount", text:"支票号", width: "6%",align:"center"},
       {type:"text", name:"applyMemo", text:"用途", width: "6%",align:"center"},
       {type:"text", name:"applyItem", text:"经费来源",align:"center", width: "6%"},
       {type:"text", name:"applyDate", text:"领用日期",align:"center", width: "6%",render:toDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"runId", text:"操作",align:"center",width:"12%",render:toRunId},
       {type:"text", name:"expense", text:"报销状态", width:"6%",align:"center",render:toExpense},
       {type:"text", name:"isPrint", text:"打印状态",width:"6%",align:"center",render:toIsPrint}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件可领用的支票</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }else {
    //var div = new Element('div',{"class":"TableControl"}).update("&nbsp;&nbsp;&nbsp;<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>"
       // + "&nbsp;全选&nbsp;<input type='button'  value=' 批量打印 ' class='BigButton' onClick='printMail();' title='批量打印'>");
    //$('giftList').appendChild(div);
   //document.getElementById("deleteAll").style.display = "";
  }
  doInitTime();
}
function getParam(){
  queryParam = $("form1").serialize();
  return queryParam;
}
//返回操作 项
function toMing(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox' name='emailSelect' value='" + seqId +"' onClick='javscript:checkOne(self);'>";
}
function toMoney(cellData, recordIndex,columInde){
  var applyMoney =  this.getCellData(recordIndex,"applyMoney");
  return insertKiloSplit(applyMoney,2);
}
//时间前10
function toDate(cellData, recordIndex, columIndex){
  var createDate = this.getCellData(recordIndex,"applyDate");
  return createDate.substr(0,10);
}
//runId
function toRunId(cellData, recordIndex, columIndex){
  var runId = this.getCellData(recordIndex,"runId");
  var seqId =  this.getCellData(recordIndex,"seqId");
  if (runId != null && runId != "" && runId > 0) {
    var str = "'支票领用申报'";
    return "<a href=javascript:formViewByName('" + runId + "'," + str + ")>工作流详情</a>";
  }
  if (runId == null || runId == "" || runId <= 0) {
    var paramStr = "<a href='javascript:chequeDetail(" + seqId +")'>查看详情</a>&nbsp;<a href='javascript:editCheque(" + seqId +")'>编辑</a>&nbsp;<a href='javascript:delCheque(" + seqId +")'>删除记录</a>";
    return paramStr;
  }
}
function toExpense(cellData, recordIndex, columIndex){
  var expense = this.getCellData(recordIndex,"expense");
  if (expense == 0) {
    return "<font color='red'>未报销</font>";
  }
  if (expense == 1) {
    return "<font color='blue'>已报销 </font>";
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
//时间
function doInitTime() { 
  showCalendar('statrTime',false,'beginDateImg');
  showCalendar('endTime',false,'endDateImg');
}
//查询
function queryGift(){
  //if (document.getElementById("statrTime").value > document.getElementById("endTime").value) {
    //alert("开始时间不能大于结束时间！");
    //document.getElementById("statrTime").focus();
    //return false;
  //}
  if (document.getElementById("money").value != "") {
    var obj = document.getElementById("money");
    var val = obj.value;
    if (isNumber(val)) {
    }else {
      alert("非法数字");
      document.getElementById("money").focus();
      document.getElementById("money").select();
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
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件可领用的支票</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').style.display=""; 
    $('returnNull').update(table);  
  }
}
//支票领用
function addCheque(year,deptId) {
  myleft = (screen.availWidth-800)/2;
  window.open("<%=contextPath%>/subsys/oa/finance/cheque/news/addCheque.jsp?year="+year+"&deptId="+deptId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=620,left="+myleft+",top=50");
}
//编辑
function editCheque(seqId) {
  myleft = (screen.availWidth-800)/2;
  window.open("<%=contextPath%>/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/editCheque.act?seqId=" + seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=700,left="+myleft+",top=50");
}
//查看详情
function chequeDetail(seqId) {
  var myleft = (screen.availWidth-800)/2;
  window.open("<%=contextPath%>/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/chequeDetail.act?seqId=" + seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=700,left="+myleft+",top=50");
}
//删除记录
function delCheque(seqId) {
  URL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/delCheque.act?seqId=" + seqId;
  if (window.confirm("确认要删除该记录吗？")) {
    window.location = URL;
    location.reload();
  }
}
//批量打印
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
    URL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/printOut.act?printStr=" + printStr
    window.open(URL);
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
//有一个不选中就不是全选
function checkOne(el) {
  if (!el.checked) {
     document.getElementById("allbox").checked = false;
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
function addTr(){
  var table = pageMgr.getDataTableDom();
  chequeTr(table);
}
</script>
</head>
<body onLoad="doInit();" topmargin="5">
<br>
<div>&nbsp;&nbsp;<img src="<%=contextPath %>/core/styles/imgs/menuIcon/@finance.gif" HEIGHT="20" align="absmiddle"><span class="big3"> 支票领用管理</span></div>
<br>
<div style="padding-left: 10px;">
<form method="post" name="form1" id="form1">
<table class="TableList" align="center">
  <tr class="TableContent">
    <td nowrap>&nbsp;领用人姓名：
        <input type="hidden" id="claimerName" name="claimerName" value="">
        <input type="text" name="claimer" id="claimer" size="20" class="BigInput" maxlength="20"  value="" readonly>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['claimerName','claimer']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('claimerName').value='';$('claimer').value='';">清空</a>&nbsp;领用项目：
    <input type="text"  name="item" id="item" size="10" class="BigInput">&nbsp;领用金额：<input  type="text"  name="money" id="money" size="15" maxlength="15"  value="" class="BigInput">
         &nbsp;领用时间：<input  type="text" class="BigInput" name="statrTime" id="statrTime" size="10" readonly>
      <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
       &nbsp;至
       <input  type="text" class="BigInput" name="endTime" id="endTime" size="10" readonly>
       <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
      </td>
  </tr>
  
  <tr class="TableContent">
    <td nowrap>&nbsp;支票号：<input  type="text"  name="chequeAccount"  id="chequeAccount"  size="10" class="BigInput">
     &nbsp;财务审批人：<input type="hidden" id="financeDirectorName" name="financeDirectorName" value="">
      <input type="text" name="financeDirector" id="financeDirector" size="20" class="BigInput" maxlength="20"  value="" readonly>
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['financeDirectorName','financeDirector']);">选择</a>
      <a href="javascript:;" class="orgClear" onClick="$('financeDirectorName').value='';$('financeDirector').value='';">清空</a>
     </td>
    </tr>
  <tr class="TableControl">
    <td rowspan=2 align="center">
      <input value="查询" type="button" onclick="queryGift()" class="BigButton" title="查询 " name="button">&nbsp;&nbsp;
       <input type="button" onClick="addCheque('','');" class="BigButton" id="btnRefresh" value="  支票领用  ">
    </td>
  </tr>   
</table>
</form>
</div>
<br>
<div id="giftList" style="padding-left: 10px;"></div>
<div id="returnNull">
</div>
<br>
</body>
</html>