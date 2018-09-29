<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  Calendar cl =Calendar.getInstance();
  int curYear = cl.get(Calendar.YEAR);
  int nextYear = curYear - 1;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html class="hiddenRoll">
<head>
<title>预算项目</title>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<style>
      html {
             overflow:auto;  /*这个可以去掉IE6,7的滚动*/
             _overflow-x:hidden;/*去掉IE6横向滚动*/
          }
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
function getOpenner(){
   if(is_moz){
     return parent.opener;
   }else{
     return parent.dialogArguments;
   }
}
var parent_window = getOpenner();
var budgetIdField = null;
var budgetNameField = null
function doOnload(){
  parent_window = getOpenner();
  selectBudgetApply();//预算申请
  budgetIdField = parent_window.$(parent_window.budgetIdField);//初始化对象
  budgetNameField = parent_window.$(parent_window.budgetNameField);//初始化对象
}
var pageMgr = null;
var cfgs = null;
function selectBudgetApply(){

  var budgetPropose = $("budgetPropose").value;
  var budgetItem = $("budgetItem").value;
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetApplyAct/queryBudgetListToProject.act";
   cfgs = {
    dataAction: url,
    container: "budgetApply",
    paramFunc: getParam,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"budgetMoney", text:"预算金额", width: "6%",align:"center",render:toMoney,sortDef:{type:0, direct:"asc"}},

       {type:"text", name:"budgetItem", text:"项目",align:"center", width: "10%"},
       {type:"text", name:"userName", text:"经手人",align:"center", width: "10%"},
       {type:"hidden", name:"notAffair", text:"预算类型", width: "1%",align:"center"},
       {type:"hidden", name:"settleFlag", text:"是否已结算", width: "1%",align:"center"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
}
function toMoney(cellData, recordIndex, columInde){
  var budgetMoney =  this.getCellData(recordIndex,"budgetMoney");
  return insertKiloSplit(budgetMoney,2);
  
}
function toOpts(cellData, recordIndex, columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var settleFlag =  this.getCellData(recordIndex,"settleFlag");
  var budgetItem =  this.getCellData(recordIndex,"budgetItem");
  var str = "已结算";
  //budgetItem = encodeURIComponent(budgetItem);
  var re1 = /\'/gi;
  var re2 = /\"/gi;
  var re3 = /\!/gi;
  var re4 = / /gi;
  budgetItem = budgetItem.replace(re2,"&bdquo;");
  budgetItem = budgetItem.replace(re1,"&lsquo;");
  if(settleFlag!=1){
   str = "<a href='#' onclick='ToBudgetId("+seqId+",\""+budgetItem+"\");'>选择数据</a>&nbsp;";
  }
  return str;
}
function ToBudgetId(seqId,budgetItem){	
  //上级页面更新数据
  budgetIdField.value = seqId;
  budgetNameField.value = budgetItem;
  close();
}

function getBudget(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
   // $('giftList').style.display="";
   // $('returnNull').style.display="none"; 
  }else{
   // var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
      //  + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的信息</div></td></tr>"
      //  );
   // $('budgetApply').style.display="none";
   // $('returnNull').style.display=""; 
    //$('returnNull').update(table);  
  }
}

function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}
</script>
 
</head>
<body  topmargin="5" onload="doOnload();">
<br>
<table border="0" width="500" cellspacing="0" cellpadding="3" >
  <tr>
    <td class="Big"><img src="<%=imgPath%>/@finance.gif" HEIGHT="20"><span class="big3"> 预算详细
	</span>
    </td>
  </tr>
</table>
<fieldset style="width:90%;margin-left:22px;padding-bottom:5px;">
	<legend class="small" align=left>
      <b>快速查询</b>
	</legend>
	  <form action="#"  method="post" name="form1" id="form1">
<table cellspacing="1" class="small" align="left" cellpadding="3" width="100%">

  <tr>
</tr><tr>	<td nowrap align="center">项目名称</td>
	<td nowrap class="TableData">
        <input type="text" id="budgetItem" name="budgetItem" class="SmallInput" size="15" maxlength="100" value="">
    </td>
	<td nowrap align="center">经手人</td>
	<td nowrap class="TableData">
        <input type="text" id="budgetPropose" name="budgetPropose" class="SmallInput" size="15" maxlength="100" value="">
    </td>
   </tr>
   <tr>
   	  <td nowrap class="TableData" colspan="6" align="right">
		<input type="hidden" name="setvaluename" value="PROJ_GROUP_NAME,BUDGET_ID">
   	  	<input value="查询" type="button" class="SmallButton" title="查询" name="button" onclick="getBudget();">
     </td>
  </tr>

</table>
</form>
</fieldset>

<div align = "left" id="budgetApply" style="padding: 22px;">
<div id="returnNull"></div>
</div>
</body>
</html>