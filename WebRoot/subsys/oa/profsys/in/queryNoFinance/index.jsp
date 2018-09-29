<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>来访团队信息</title>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/subsys/oa/profsys/js/profsys.js" ></script>
<script type="text/javascript"> 
var pYxTotal = 0;
var pAllTotal = 0;
function doInit() {
  selectProject();
}
var pageMgr = null;
var cfgs = null;

function selectProject(){
  pYxTotal = 0;
  pAllTotal = 0;
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectAct/queryProject.act?projType=0";
   cfgs = {
    dataAction: url,
    container: "projectDiv",
    afterShow:getTotal2,
    colums: [
       {type:"selfdef",name:"email_select", text:"选择",align:"center", width:"3%",render:toCheck},
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"projNumT", text:"项目编号", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projGropNameT", text:"团组名称", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"hidden", name:"deptIdT", text:"部门ID", width: "1%",align:"center"},
       {type:"text", name:"deptNameT", text:"所属部门", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projVisitTypeT", text:"出访类别", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projLeaderNameT", text:"负责人", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projActiveTypeT", text:"项目类别", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projStartTime", text:"起始时间", width: "6%",align:"center",render:toStartDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projEndTime", text:"结束时间", width: "6%",align:"center",render:toEndDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"pYx", text:"参与外办人数", width: "7%",align:"center",render:topYx,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"pTotal", text:"参与总人数", width: "6%",align:"center",render:topTotal,sortDef:{type:0, direct:"asc"}},

       {type:"text", name:"printStatus", text:"打印状态", width: "6%",align:"center",render:toPrint,sortDef:{type:0, direct:"asc"}},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"7%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total>0){
   //$("allDiv").style.display = "";
  }else{
    $("allDiv").style.display = "none";
    $("projectDiv").style.display = "none";
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件信息!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return   "<a href='#' onclick='openShowDetilIndex(" + seqId + ");'>详细信息</a> ";
}
function topYx(cellData, recordIndex, columInde){
  var pYx = this.getCellData(recordIndex,"pYx");
  pYxTotal = pYxTotal + pYx;

  return pYx;
}
function topTotal(cellData, recordIndex, columInde){
  var pTotal = this.getCellData(recordIndex,"pTotal");
  pAllTotal = pAllTotal + pTotal;
  //$("pAllTotalIn").value = pAllTotal;
  return pTotal;
}
function toStartDate(cellData, recordIndex, columInde){
  var projStartTime = this.getCellData(recordIndex,"projStartTime");
  return projStartTime.substr(0,10);
}
function toEndDate(cellData, recordIndex, columInde){
  var projEndTime = this.getCellData(recordIndex,"projEndTime");
  return projEndTime.substr(0,10);
}
function toPrint(cellData, recordIndex, columInde){
  var printStatus = this.getCellData(recordIndex,"printStatus");
  var printStatusDesc = "<font color='#FF0000'><b>未打印</b></font>";
  if(printStatus==1){
    printStatusDesc = "<font color='blue'><b>已打印</b></font>";
  }
  return printStatusDesc;
}
//详细信息
function openShowDetilIndex(seqId) {
  var myleft = (screen.availWidth - 800)/2;
  window.open("<%=contextPath%>/subsys/oa/profsys/in/baseinfo/showdetail/index.jsp?seqId="+seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=950,height=700,left=" + myleft + ",top=50");
}
//批量打印
function openPrint(){
  var printStr=get_checked();
  if(printStr==""){
     alert("请至少选择其中一条。");
     return;
  }
  var myleft = (screen.availWidth - 800)/2;
  msg = '确认要打印所选记录？';
  if(window.confirm(msg)) {
    URL = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectAct/printIn.act?printStr=" + printStr
    window.open(URL);
  }
}
function getTotal2(){
  var table = pageMgr.getDataTableDom();
  getTotal(table,pYxTotal,pAllTotal);
  pYxTotal = 0;
  pAllTotal = 0;
  insertTr(table);
}
function openIndex() {
  var url = "<%=contextPath%>/subsys/oa/profsys/in/queryNoFinance/index1.jsp";
  window.location.href = url;
}
</script>
</head>
<body  topmargin="5px" onLoad="doInit()">
<br>
<div>
<input type="button" value="查询" class="BigButton" onClick="javascript:openIndex()"></div>
<br>
<div id="projectDiv">
</div>
<div id="allDiv" style="display:none"><!--
  <table class="TableList" width="100%">

      <tr class="TableControl">
      <td colspan = "13">	  
      <input type="hidden" name="pYxTotalIn" id="pYxTotalIn" value = "0">
           <input type="hidden" name="pAllTotalIn" id="pAllTotalIn" value = "0">  &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="check_all();">
      <label for="allbox_for">全选</label>&nbsp;
      <input type="button" value="汇总打印" class="BigButton" onclick="openPrint();" title='打印所选工作事物'></input></td>
    </tr>
</table>--></div>
<div id="returnNull"></div>
<br>
</body>
</html>
