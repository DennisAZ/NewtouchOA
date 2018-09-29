<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String budgetId = request.getParameter("seqId");
  if(budgetId==null){
    budgetId = "";
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>支票领用管理</title>
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/workflow.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js" ></script>
<script type="text/javascript">
var budgetId = '<%=budgetId%>';
var pageMgr = null;
var cfgs = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHFinanceApplyRecordAct/applySelectByBudgetId.act?budgetId="+budgetId;
   cfgs = {
    dataAction: url,
    container: "giftList",
    colums: [

       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"applyName", text:"领用人姓名", width: "7%",align:"center"},
       {type:"text", name:"applyDate", text:"领用日期",align:"center", width: "6%",render:toDate},
       {type:"text", name:"applyItem", text:"领用项目",align:"center", width: "6%"},
       {type:"text", name:"applyMemo", text:"用途", width: "6%",align:"center"},

       {type:"hidden", name:"applyItem", text:"支票号",align:"center", width: "6%"},
       {type:"text", name:"applyMoney", text:"领用金额", width: "7%",align:"center"},
       {type:"hidden", name:"expense", text:"状态", width: "6%",align:"center"},
       {type:"text", name:"runId", text:"操作",align:"center",width:"12%",render:toRunId}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
}
//返回操作 项

function toMing(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox' name='emailSelect' value='" + seqId +"' onClick='javscript:checkOne(self);'>";
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
  var paramStr = "";
  if (runId != null && runId != "" && runId > 0) {
     paramStr = "<a href='javascript:;' onClick='formViewByName("+runId+" ,\"支票领用申报\");'>工作流详情</a>"
  }
  if (runId == null || runId == "" || runId <= 0) {
    paramStr = "<a href='javascript:chequeDetail(" + seqId +")'>查看详情</a>&nbsp;<a href='javascript:editCheque(" + seqId +")'>编辑</a>&nbsp;<a href='javascript:delCheque(" + seqId +")'>删除记录</a>";

  }
  return paramStr;
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
function runCreate(runId){
  return;
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

</script>
</head>
<body onLoad="doInit();" topmargin="5">
<br>
<div>&nbsp;&nbsp;<img src="<%=contextPath%>/core/styles/imgs/menuIcon/@finance.gif" HEIGHT="20" align="absmiddle"><span class="big3"> 支票领用列表</span></div>
<br>

<br>
<div id="giftList" style="padding-left: 10px;"></div>
<div id="returnNull">
</div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" width="24" height="24" align="absmiddle"><span class="big3"> 支票领用申请</span><br>
    </td>
  </tr>
</table>
<center>  
  		<input type="hidden" name="COST_ID" value="6">
  		<input type="hidden" name="PROJ_ID" value="58">
  		  		<input type="button" class="BigButton" value="申领支票" onclick="createNewWork('支票领用申报','预算ID=<%=budgetId %>');">&nbsp;
  		<input type="button" value="关闭" class="BigButton" onclick="window.close();">&nbsp;
</center>

</body>
</html>