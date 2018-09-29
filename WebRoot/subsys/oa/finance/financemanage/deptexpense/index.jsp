<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Calendar cl =Calendar.getInstance();
  int curYear = cl.get(Calendar.YEAR);
  int nextYear = curYear - 1;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>财务预算管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/workflow.js"></script>
<script type="text/javascript">
var curYear = '<%=curYear%>';
function cost_apply(seqId){
  var requestURL = "<%=contextPath%>/subsys/oa/finance/financemanage/cheque.jsp?seqId="+seqId;
  myleft=(screen.availWidth-800)/2;
  window.open(requestURL,"","height=550,width=800,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,left="+myleft+",top=50");
}
function cost_charge(seqId){
  var requestURL = "<%=contextPath%>/subsys/oa/finance/financemanage/expense.jsp?seqId="+seqId;
  myleft=(screen.availWidth-800)/2;
  window.open(requestURL,"","height=550,width=800,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,left="+myleft+",top=50");
}
function cost_balance(seqId){
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetDeptTotalAct/expenseSelectByBudgetId.act?budgetId="+seqId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ;
  }
  var prcs = rtJson.rtData;
  var seqIds = ""; 
  var chargeStr = "";
  for(var i = 0; i<prcs.length;i++){
    var prc = prcs[i];
    seqIds = seqIds + prc.seqId + ",";
    var budgetItem = prc.budgetItem;
    var chargeMoney = prc.chargeMoney;
    var chargeMemo = prc.chargeMemo;
    chargeStr = chargeStr + budgetItem + "`" +  chargeMoney + "`" + chargeMemo+ "`\r\n";
  }
  //alert(chargeStr);
  var re1 = /\'/gi;
  chargeStr = chargeStr.replace(re1,"&lsquo;");
  createNewWork(encodeURIComponent('结算明细单'),'预算ID='+seqId+'&实际支出明细='+encodeURIComponent(chargeStr) );
}
function budget_detail(seqId,type){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetApplyAct/selectBudgetById.act?seqId="+seqId+"&type="+type;
	myleft=(screen.availWidth-800)/2;
  window.open(requestURL,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left="+myleft+",top=50");
}
function del_budget(seqId){
  var msg = "确定删除该预算吗,删除后不能在恢复！";
  if(window.confirm(msg)){
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetApplyAct/deleteBudgetById.act?seqId="+seqId;
    var rtJson = getJsonRs(requestUrl);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    alert("删除成功！");
    window.location.reload();
  }
}
function doOnload(){
  selectBudgetApply();//预算申请
}
var pageMgr = null;
var cfgs = null;
function selectBudgetApply(){
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetApplyAct/queryBudget.act?notAffair=0";
   cfgs = {
    dataAction: url,
    container: "budgetApply",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"budgetMoney", text:"预算金额", width: "8%",align:"center",render:budgetMoney,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"applyName", text:"申请人", width: "8%",align:"center"},
       {type:"hidden", name:"budgetYear", text:"年份", width: "6%",align:"center"},
       {type:"hidden", name:"deptId", text:"部门Id", width: "6%",align:"center"},
       {type:"text", name:"deptName", text:"所属部门", width: "6%",align:"center"},
       {type:"text", name:"budgetDate", text:"申请日期", width: "6%",align:"center",render:toDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"budgetItem", text:"项目",align:"center", width: "12%"},
       {type:"selfdef", name:"expenceMoneyTotal", text:"已领额",align:"center", width: "7%",render:chequeMoney},      
       {type:"selfdef", name:"chequeMoneyTotal", text:"已报额",align:"center", width: "7%",render:expenceMoney},       
       {type:"hidden", name:"isAudit", text:"审核状态", width: "1%",align:"center"},
       {type:"hidden", name:"settleFlag", text:"是否已结算", width: "1%",align:"center"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"20%",render:toOpts}]
  };
   pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total<1){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件信息!</div></td></tr>"
        );
    $('budgetApply').update(table); 
  }
}
function budgetMoney(cellData, recordIndex, columInde){
  var budgetMoney =  this.getCellData(recordIndex,"budgetMoney");
  return insertKiloSplit(budgetMoney,2);
}
function toDate(cellData, recordIndex, columInde){

  var budgetDate =  this.getCellData(recordIndex,"budgetDate");
  return budgetDate.substr(0,10);
}
function expenceMoney(cellData, recordIndex, columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var budgetMoney =  this.getCellData(recordIndex,"budgetMoney");
  var isChaoZhi = "";
  if(parseFloat(budgetMoney)<parseFloat(chequeTotalByBudgetId(seqId,2))){
    isChaoZhi = "<font color='red'>超支！</font>&nbsp;&nbsp;&nbsp;"
  }
  return insertKiloSplit(chequeTotalByBudgetId(seqId,2),2) + isChaoZhi;
}
function chequeMoney(cellData, recordIndex, columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var budgetMoney =  this.getCellData(recordIndex,"budgetMoney");
  var isChaoZhi = "";
  if(parseFloat(budgetMoney)<parseFloat(chequeTotalByBudgetId(seqId,1))){
    isChaoZhi = "<font color='red'>超支！</font>&nbsp;&nbsp;&nbsp;"
  }
  return insertKiloSplit(chequeTotalByBudgetId(seqId,1),2) + isChaoZhi;
}
function toOpts(cellData, recordIndex, columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var budgetYear =  this.getCellData(recordIndex,"budgetYear");
  var settleFlag =  this.getCellData(recordIndex,"settleFlag");
  var str = "";
  if(settleFlag==1){
    str = "<a href='javascript:;' onClick='budget_detail("+seqId+",1)'>预算详情</a>&nbsp;&nbsp;<font color='red'>已结算</font>"
  }else{
    str = "<a href='javascript:;' onClick='budget_detail("+seqId+",1)'>预算详情</a>&nbsp;&nbsp;"
    +"<a href='javascript:;' onClick='cost_apply("+seqId+");'>申请领用</a>&nbsp;&nbsp;"
    +"<a href='javascript:;' onClick='cost_charge("+seqId+");'>费用报销</a>&nbsp;&nbsp;"
    +"<a href='javascript:;' onClick='cost_balance("+seqId+");'>申请结算</a>&nbsp;&nbsp;"
  }

  return str;
}
function chequeTotalByBudgetId(budgetId,type){
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetApplyAct/chequeTotalByBudgetId.act?budgetId="+budgetId+"&type="+type;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  return prc.total;
}

</script>
</head>
<body  topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/@finance.gif" HEIGHT="20"><span class="big3"> 部门预算明细</span>
    </td>
  </tr>
</table>

<div align = "left" id="budgetApply" >
</div>
</body>
</html>