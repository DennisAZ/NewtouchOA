<%@ page language="java" import="java.util.*,java.sql.*,yh.core.funcs.attendance.manage.logic.*,yh.core.data.YHRequestDbConn,yh.core.global.YHBeanKeys" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
  Connection dbConn = requestDbConn.getSysDbConn();
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  int userId = user.getSeqId();
  String year = request.getParameter("year");
  String deptId = request.getParameter("deptId");
  Calendar cl =Calendar.getInstance();
  int curYear = cl.get(Calendar.YEAR);
  int nextYear = curYear - 1;
  if(year==null||year.equals("")){
    year = curYear + "";
  }
  if(deptId==null||deptId.equals("")){
    deptId = user.getDeptId() + "";
  }
%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title> 预算申请</title>
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
  selectBudgetApply();//预算申请
}
var pageMgr = null;
var cfgs = null;
var year = '<%=year%>';
var deptId = '<%=deptId%>';
function selectBudgetApply(){
  var url = "<%=contextPath%>/yh/subsys/oa/finance/act/YHBudgetDeptTotalAct/queryDeptBudget.act?year="+year+"&deptId="+deptId;
   cfgs = {
    dataAction: url,
    container: "budgetApply",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"budgetMoney", text:"预算金额", width: "6%",align:"center"},
       {type:"text", name:"applyName", text:"申请人", width: "8%",align:"center"},
       {type:"hidden", name:"budgetYear", text:"年份", width: "6%",align:"center"},
       {type:"hidden", name:"deptId", text:"部门Id", width: "6%",align:"center"},
       {type:"text", name:"deptName", text:"所属部门", width: "6%",align:"center"},
       {type:"text", name:"budgetDate", text:"申请日期", width: "6%",align:"center",render:toDate},
       {type:"text", name:"budgetItem", text:"项目",align:"center", width: "10%"},
       {type:"text", name:"Memo", text:"备注",align:"center", width: "12%" },           
       {type:"hidden", name:"isAudit", text:"审核状态", width: "1%",align:"center"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"16%",render:toOpts}]
  };
   pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
}
function toDate(cellData, recordIndex, columInde){

  var budgetDate =  this.getCellData(recordIndex,"budgetDate");
  return budgetDate.substr(0,10);
}
function toOpts(cellData, recordIndex, columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var budgetYear =  this.getCellData(recordIndex,"budgetYear");
  var str = "<a href='javascript:;' onClick='budget_detail("+seqId+",1)'>查看详细</a>&nbsp;&nbsp;"
         +"<a href='javascript:;' onClick='budget_detail("+seqId+",4)'>打印预览</a>&nbsp;&nbsp;"
  return str;
}

</script>
 
</head>
<body  topmargin="5" onload="doOnload();">
<div align = "left" id="budgetApply" >
</div>
<br></br>
<div align = "center"  >
      <input type="button" value="关闭" class="BigButton" onclick="window.close();">&nbsp;
</div>
</body>
</html>