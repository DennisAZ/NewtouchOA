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
<title>不准原因</title>
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
<script type="text/javascript">
var curYear = '<%=curYear%>';
function add_budget(){
  var year = curYear;
  myleft=(screen.availWidth-800)/2;
  window.open("addbudget.jsp?year="+year,"","height=550,width=800,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,left="+myleft+",top=50");
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
  deptTotal(curYear);//得到本部门的总预算金额
  selectBudgetApply();//预算申请
}
function deptTotal(curYear){
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetApplyAct/selectBudget.act?year="+curYear;
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  
  var table = new Element('table',{"class":"TableList","width":"85%"}).update(" <tr class='TableHeader'>"
      +"<td nowrap align='center' width='20%'>年份</td>"
      +"<td nowrap align='center' width='33%'>预算计划金额</td>"
      +"</tr>"   
      +"<tr class='TableLine1'>"
      +"<td nowrap align='center'>"+prc.year+"</td>"
      +"<td nowrap align='right'>"+prc.deptTotal+"</td>"
      +"</tr>");
  $("deptTotal").appendChild(table);
  var isDpetTotal = prc.isDpetTotal;
  $("isDpetTotal").value = isDpetTotal;
}
var pageMgr = null;
var cfgs = null;
function selectBudgetApply(){
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetApplyAct/queryDeptBudget.act";
   cfgs = {
    dataAction: url,
    container: "budgetApply",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"budgetMoney", text:"预算金额", width: "7%",align:"center",render:budgetMoney,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"applyName", text:"申请人", width: "7%",align:"center"},
       {type:"hidden", name:"budgetYear", text:"年份", width: "1%",align:"center"},
       {type:"hidden", name:"deptId", text:"部门Id", width: "1%",align:"center"},
       {type:"text", name:"deptName", text:"所属部门", width: "6%",align:"center"},
       {type:"text", name:"budgetDate", text:"申请日期", width: "6%",align:"center",render:toDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"budgetItem", text:"项目",align:"center", width: "9%"},
       {type:"text", name:"Memo", text:"备注",align:"center", width: "12%" },           
       {type:"hidden", name:"isAudit", text:"审核状态", width: "1%",align:"center"},
       {type:"hidden", name:"settleFlag", text:"结算状态", width: "1%",align:"center"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"20%",render:toOpts}]
  };
   pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
}
function budgetMoney(cellData, recordIndex, columInde){
  var budgetMoney =  this.getCellData(recordIndex,"budgetMoney");
  return insertKiloSplit(budgetMoney,2);
}
function toDate(cellData, recordIndex, columInde){
  var budgetDate =  this.getCellData(recordIndex,"budgetDate");
  return budgetDate.substr(0,10);
}
function toOpts(cellData, recordIndex, columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var budgetYear =  this.getCellData(recordIndex,"budgetYear");
  var settleFlag = this.getCellData(recordIndex,"settleFlag");
  var str = "";
  var isDpetTotal = $("isDpetTotal").value;
  
  if(settleFlag==1){
    str = "<a href='javascript:;' onClick='budget_detail("+seqId+",1)'>查看详细</a>&nbsp;&nbsp;"
    +"<a href='javascript:;' onClick='budget_detail("+seqId+",2)'>克隆</a>&nbsp;&nbsp;"
    +"<a href='javascript:;' onClick='budget_detail("+seqId+",4)'>打印预览</a>&nbsp;&nbsp;"
    + "<font color='red' >已结算</font>";

   }else{
     str = "<a href='javascript:;' onClick='budget_detail("+seqId+",1)'>查看详细</a>&nbsp;&nbsp;"
     +"<a href='javascript:;' onClick='budget_detail("+seqId+",2)'>克隆</a>&nbsp;&nbsp;"
     +"<a href='javascript:;' onClick='budget_detail("+seqId+",3);'>编辑</a>&nbsp;&nbsp;"
     +"<a href='javascript:;' onClick='del_budget("+seqId+")'>删除</a>&nbsp;&nbsp;"
     +"<a href='javascript:;' onClick='budget_detail("+seqId+",4)'>打印预览</a>&nbsp;&nbsp;"
   }
  return str;
}

</script>
 
</head>
<body  topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/@finance.gif" HEIGHT="20"><span class="big3"> 财务预算管理
(所有)</span>
    </td>
  </tr>
</table>
<div align="center">
<table width="75%">
<tr>
<td align='center'>
      <input type="button"  value="增加预算" class="SmallButtonW" onClick="add_budget();" title="增加预算"> &nbsp;
</td>
</tr>
</table>
<br>
<div id="deptTotal"></div>
</div>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/@finance.gif" HEIGHT="20"><span class="big3"> 预算详细
	</span>
    </td>
  </tr>
</table>
<input type="text" id="isDpetTotal" name ="isDpetTotal" style="display:none" value=""></input>
<div align = "left" id="budgetApply" >

</div>
</body>
</html>