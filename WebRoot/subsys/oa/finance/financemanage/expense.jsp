<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String budgetId = request.getParameter("seqId");
  if(budgetId==null){
    budgetId = "";
   }
  List<Map<String,String>> expenseList = (List<Map<String,String>>)request.getAttribute("expenseList");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>费用报销管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/style.css">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/tree.css">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/style1/css/diary.css">
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/workflow.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js" ></script>
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




//加载数据
var pageMgr = null;
var cfgs = null;
var budgetId = '<%=budgetId%>';
function doCheque(){
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/applySelectByBudgetId.act?budgetId="+budgetId;
   cfgs = {
    dataAction: url,
    container: "giftList",
    afterShow:addTr,
    colums: [
             {type:"selfdef", name:"Id", text:"选择",align:"center", width:"3%",render:toMing},
             {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
             {type:"hidden", name:"applyName", text:"领用人姓名", width: "7%",align:"center"},
             {type:"hidden", name:"applyDate", text:"领用日期",align:"center", width: "6%",render:toDate},
             {type:"text", name:"applyItem", text:"领用项目",align:"center", width: "6%"},
             {type:"text", name:"applyMemo", text:"摘要", width: "6%",align:"center"},
             {type:"text", name:"chequeAccount", text:"支票号",align:"center", width: "6%"},
             {type:"text", name:"applyMoney", text:"支票金额", width: "7%",align:"center",render:applyMoney},
             {type:"text", name:"expense", text:"状态", width: "6%",align:"center",render:isExpense},
             {type:"hidden", name:"runId", text:"操作",align:"center",width:"12%",render:toRunId}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合操作记录！</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }else {
    //var div = new Element('div',{"class":"TableControl" ,"align":"left"}).update("&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>"
      //  + "&nbsp;全选&nbsp;");
   //$('giftList').appendChild(div); 
  }
}
function addTr(){
  var table = pageMgr.getDataTableDom();
  insertTr(table);
}
function insertTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align='left';
  mynewcell.colSpan = '6';
  mynewcell.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>&nbsp;全选";

}
//返回操作 项
function applyMoney(cellData, recordIndex,columInde){
  var applyMoney =  this.getCellData(recordIndex,"applyMoney");
  return insertKiloSplit(applyMoney,2);
}
function isExpense(cellData, recordIndex,columInde){
  var expense =  this.getCellData(recordIndex,"expense");
  var expenseStatus = "<font color='red'><b>未报销</b></font>";
  if(expense=='1'){
    expenseStatus = "<font color='red'><b>已报销</b></font>";
  }
  return expenseStatus;
}
function toMing(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var expense =  this.getCellData(recordIndex,"expense");
  if(expense!=1){
    return "<input type='checkbox' name='emailSelect' value='" + seqId +"' onClick='javscript:checkOne(self);'>";
  }else{
    return "";
  }
}
//时间前10
function toDate(cellData, recordIndex, columIndex){
  var createDate = this.getCellData(recordIndex,"applyDate");
  return createDate.substr(0,10);
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
    return "<font color='red'>已作废 </font>";
  }
}


function delete_mail(){	
  expense_str="";
  var chequeStr = "";
  for(var i = 0; i < document.getElementsByName("emailSelect").length;i++) {
    var el = document.getElementsByName("emailSelect").item(i);
    if (el.checked) {
      var val = el.value;
      expense_str += val + ",";
    // var item =  el.getCellData(val,"applyItem");
      var objTD =el.parentElement;
      var objTR =objTD.parentElement;
      var item = objTR.cells(1).down('span').innerHTML;
      var applyMemo = objTR.cells(2).down('span').innerHTML;
      var chequeAccount = objTR.cells(3).down('span').innerHTML;
      var applyMoneyStr = objTR.cells(4).innerHTML;
      var re1 = /\'/gi;
      var re2 = /\"/gi;
      var re3 = /\!/gi;
      var re4 = / /gi;
      var re5 = /\,/gi;
      applyMoneyStr = applyMoneyStr.replace(re5,"");
      //giftName = giftName.replace(re2,"&quot;");
      //giftName = giftName.replace(re3,"\\\!");
      //gifeName = giftName.replace(re4,"&nbsp;");
      //giftName = encodeURIComponent(giftName);
      chequeStr =  chequeStr + item + "`" + applyMemo + "`" + chequeAccount + "`" + applyMoneyStr+ "\r\n";
    }
  }
 createNewWork('费用报销申请','预算ID=<%=budgetId %>&支票ID串='+expense_str + '&费用报销信息='+chequeStr );
  	//myleft=(screen.availWidth-800)/2;
    //window.open("index1.php?expense_str="+expense_str+"&PROJ_ID=58&COST_ID=6&DEPT_ID=1&YEAR=2010","","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left="+myleft+",top=50");
}
//报销费用

function doExpense(){
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetDeptTotalAct/expenseSelectByBudgetId.act?budgetId="+budgetId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ;
  }
  var prcs = rtJson.rtData;
  if(prcs.length>0){
    var table = new Element('table',{"class":"TableList" ,"align":"center", "width":"95%"}).update("<tbody id='tbody'>"
        +"<tr class='TableHeader'>"
        +"<td nowrap align='center' width='3%'>选择</td>"
        +"<td nowrap align='center' width='11%'>部门名称</td>"
        +"<td nowrap align='center' width='18%'>报销申请人</td>"
        +"<td nowrap align='center' width='12%'>报销金额</td>"
        +"<td nowrap align='center' width='16%'>报销日期</td>"
        +"<td nowrap align='center' width='18%'>财务审批人</td>"
        +"<td nowrap align='center' width='22%'>操作</td>"
        +" </tr><tbody>");
    $("expenseDiv").appendChild(table);
    for(var i = 0; i< prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var isPrint = prc.isPrint;
      var expense = prc.expense;
      var isPrintStr = "未打印";
      var color = "red";
      if(isPrint=='1'){
        isPrintStr = "已打印";
        color = "green";
      }
      var runId = prc.runId;
      var runIdStr = "<a href='javascript:;' onClick='chargeDetail("+seqId+")'>查看详情</a>";
      if(parseInt(runId,10)>0){
        runIdStr = "<a href='javascript:;' onClick='formViewByName("+runId+" ,\"费用报销申请\");'>工作流详情</a>"
      }
       var tr = new Element('tr',{"class":"TableLine1"}).update(""
        	 +"<td>&nbsp; <input type='checkbox' name='email_select1' value='"+seqId+"'></td>"
             +"<td nowrap align='center'>"+prc.deptName+"</td>"
             +"<td nowrap align='center' >"+prc.userName+"</td>"
             +"<td nowrap align='center' >"+insertKiloSplit(prc.chargeMoney,2)+"</td>"
             +"<td nowrap align='center' >"+prc.chargeDate.substr(0,10)+"</td>"
             +"<td nowrap align='center' >"+prc.financeAuditUser+"</td>"
             +"<td nowrap align='center' >" +runIdStr + "&nbsp;"
         	 +"<font color='"+color+"'><b>"+isPrintStr+"</b></font></td>");
       $("tbody").appendChild(tr);
    }
    var printAll = new Element('tr',{"class":"TableLine1"}).update("<td colspan='7''><input type='button' value='批量打印' onclick='print_mail();' class='BigButtonW'></td>");
    $("tbody").appendChild(printAll);
  }else{
    $("nullDiv").style.display = "";
  }  
}
//查看详情
function chargeDetail(seqId) {
  myleft = (screen.availWidth-800)/2;
  window.open("<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/selectExpense.act?seqId=" + seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=820,height=650,left="+myleft+",top=50");
}


function print_mail(){
  print_str="";
  for(var i = 0; i < document.getElementsByName("email_select1").length;i++) {
    var el = document.getElementsByName("email_select1").item(i);
    if (el.checked) {
      var val = el.value;
      print_str += val + ",";
    }
  }
  if(print_str==""){
     alert("请至少选择其中一条记录。");
     return;
  }
  msg='确认要打印所选记录？';
  if(window.confirm(msg)) {
    URL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHChargeExpenseAct/updatePrint.act?printStr=" + print_str
    window.open(URL);
  }
}

function doOnload(){
  doCheque();//和预算budgetId挂钩的领用记录
  doExpense();//和预算budgetId挂钩的报销记录（去掉作废的记录 ）
}
</script>
</head>
<body  topmargin="5px" onLoad="doOnload();">
<br>
<table border="0" width="95%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/styles/style1/img/notify_new.gif" width="24" height="24" align="absmiddle"><span class="big3"> 领用详细列表</span><br>
    </td>
  </tr>
</table>


<br>
<div id="giftList" align="center" style="padding-left: 10px;"></div>
<div id="returnNull" align="center"></div>
<center>
	  <input type="button"  value="申请报销" class="BigButton" onClick="delete_mail()" title="申请报销"> &nbsp;
	  <input type="button" value="关闭" class="BigButton" onclick="window.close();">&nbsp;
</center>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=contextPath %>/core/styles/style1/img/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/cost.gif" width="24" height="24" align="absmiddle"><span class="big3"> 费用报销列表</span><br>
    </td>
  </tr>
</table>
<div id="expenseDiv" ></div>
<div id="nullDiv" align="center" style="display:none">
<table class="MessageBox" align="center" width="440">

  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的报销记录！</div>
    </td>
  </tr>
</table>
</div>
</body>
</html>
