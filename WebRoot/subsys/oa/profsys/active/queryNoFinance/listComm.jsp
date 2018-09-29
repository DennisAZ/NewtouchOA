<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String commNum = request.getParameter("commNum") == null ? "" : request.getParameter("commNum");
String commMemCn = request.getParameter("commMemCn") == null ? "" : request.getParameter("commMemCn");
String commMemFn = request.getParameter("commMemFn") == null ? "" : request.getParameter("commMemFn");
String commName = request.getParameter("commName") == null ? "" : request.getParameter("commName");
String commTime = request.getParameter("commTime") == null ? "" : request.getParameter("commTime");
String commPlace = request.getParameter("commPlace") == null ? "" : request.getParameter("commPlace");
String projCommType = request.getParameter("projCommType") == null ? "" : request.getParameter("projCommType");
%>
<html>
<head>
<title>出访团队信息</title>
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
<script type="text/Javascript" src="<%=contextPath%>/subsys/oa/profsys/js/profsys.js" ></script>
<script type="text/javascript">
var pageMgr = null;
var cfgs = null;
var pYxTotal = 0;
var pAllTotal = 0;
function doInit(){
  pYxTotal = 0;
  pAllTotal = 0;
  var param = "commNum=<%=commNum%>&commMemCn=<%=commMemCn%>&commMemFn=<%=commMemFn%>"
    + "&commName=<%=commName%>&commTime=<%=commTime%>&commPlace<%=commPlace%>"
    + "&projCommType=<%=projCommType%>";
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectCommAct/profsysSelectComm.act?" + param;
   cfgs = {
    dataAction: url,
    container: "giftList",
    afterShow:getTotal2,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"selfdef", name:"Id", text:"选择",align:"center", width:"3%",render:toMing},
       {type:"text", name:"PROJ_NUM", text:"项目编号", width: "7%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"PROJ_GROUP_NAME", text:"团组名称", width: "7%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"DEPT_ID", text:"所属部门", width: "7%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"hidden", name:"PROJ_VISIT_TYPE", text:"出访类别", width: "7%",align:"center",render:toProjVisit,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"PROJ_LEADER", text:"负责人",align:"center", width: "6%",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"PROJ_ACTIVE_TYPE", text:"项目类别",align:"center", width: "7%",render:toProjActive,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"PROJ_START_TIME", text:"起始时间",align:"center",width:"8%",render:toStatrTime,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"PROJ_END_TIME", text:"结束时间", width:"8%",align:"center",render:toEndTime,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"P_YX", text:"参与外办人数",width:"10%",align:"center",render:toPyx,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"P_TOTAL",text:"参与总人数", width:"9%",align:"center",render:toPtotal,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"PRINT_STATUS",text:"打印状态", width:"7%",align:"center",render:toPrintStatus,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"caozuo",text:"操作", width:"7%",align:"center",render:toCaoZuo}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件的信息!</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }else {
    //var table = new Element('table',{"class":"TableList","width":"100%"}).update("<tr class='TableControl'><td width='100%' colspan='3'>&nbsp;<input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>"
     //   + "&nbsp;全选&nbsp;<input type='button'  value=' 汇总打印  ' class='BigButton' onClick='printMail();' title='汇总打印'></td></tr>");
    //$('giftList').appendChild(table);
  }
}

//返回出访类别
function toProjVisit(cellData, recordIndex,columInde){
  var projVisitType =  this.getCellData(recordIndex,"PROJ_VISIT_TYPE");
  if (projVisitType == "") {
    return "无";
  } else {
    return projVisitType;
  }
}
//返回项目类别
function toProjActive(cellData, recordIndex,columInde){
var projActiveType =  this.getCellData(recordIndex,"PROJ_ACTIVE_TYPE");
if (projActiveType == "") {
  return "无";
} else {
  return projActiveType;
}
}
//返回外事办人数
function toPyx(cellData, recordIndex,columInde){
  var pYx =  this.getCellData(recordIndex,"P_YX");
  pYxTotal = parseInt(pYxTotal) + parseInt(pYx);
  return pYx;
}
//返回总人数
function toPtotal(cellData,recordIndex,columInde){
  var pTotal =  this.getCellData(recordIndex,"P_TOTAL");
  pAllTotal = parseInt(pAllTotal) + parseInt(pTotal);
  return pTotal;
}
//返回操作 项
function toMing(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  return "<input type='checkbox' name='outSelect' value='" + seqId +"' onClick='javscript:checkOne(self);'>";
}
//返回开始时间
function toStatrTime(cellData, recordIndex,columInde){
  var projStartTime =  this.getCellData(recordIndex,"PROJ_START_TIME");
  return projStartTime.substr(0,10);
}
//返回结束时间
function toEndTime(cellData, recordIndex,columInde){
  var projEndTime =  this.getCellData(recordIndex,"PROJ_END_TIME");
  return projEndTime.substr(0,10);
}
//返回打印状态
function toPrintStatus(cellData, recordIndex,columInde){
  var printStatus =  this.getCellData(recordIndex,"PRINT_STATUS");
  if (printStatus == "0") {
    return "<font color='#FF0000'><b>未打印</b></font>";
  }
  if (printStatus == "1") {
    return "<font color='blue'><b>已打印</b></font>";
  }
}
//返回操作项
function toCaoZuo(cellData, recordIndex,columInde) {
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href='#' onClick=javascript:openShowDetilIndex(" + seqId + ")>详细信息</a>";
}
//详细信息
function openShowDetilIndex(seqId) {
  var myleft = (screen.availWidth - 800)/2;
  window.open("<%=contextPath%>/subsys/oa/profsys/active/baseinfo/showdetail/index.jsp?seqId="+seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=950,height=700,left=" + myleft + ",top=50");
}
//批量打印
function printMail() {
  var printStr = "";
  for(var i = 0; i < document.getElementsByName("outSelect").length;i++) {
    var el = document.getElementsByName("outSelect").item(i);
    if (el.checked) {
      var val = el.value;
      printStr += val + ",";
    }
  }
  //当没有数据时
  if(i == 0) {
    var el = document.getElementsByName("outSelect");
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
    URL = "<%=contextPath%>/yh/subsys/oa/profsys/act/active/YHActiveProjectAct/printActive.act?printStr=" + printStr
    window.open(URL);
  }
}
//有一个不选中就不是全选
function checkOne(el) {
  if (!el.checked) {
     document.getElementById("allbox").checked = false;
  }
}
//全部选中
function checkAll() {
  for (var i = 0;i < document.getElementsByName("outSelect").length;i++) {
    if (document.getElementsByName("allbox")[0].checked) {
      document.getElementsByName("outSelect").item(i).checked=true;
    }else {
      document.getElementsByName("outSelect").item(i).checked=false;
    }
  }
  if (i == 0) {
    if (document.getElementsByName("allbox")[0].checked) {
      document.getElementsByName("outSelect").checked = true;
    }else {
      document.getElementsByName("outSelect").checked = false;
    }
  }
}
function getTotal2(){
  var table = pageMgr.getDataTableDom();
  getTotal3(table,pYxTotal,pAllTotal);
  pYxTotal = 0;
  pAllTotal = 0;
  insertActiveTr2(table);
}
function getTotal3(table,pYxTotal,pTotal){
  //合计
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行

  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.colSpan="8";
  mynewcell.innerHTML = "&nbsp;合计：";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align="center";
  mynewcell.innerHTML = pYxTotal;

  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align="center";
  mynewcell.innerHTML = pTotal;

  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align="center";
  mynewcell.colSpan="2";
}
function insertActiveTr2(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='hidden' name='pYxTotalIn' id='pYxTotalIn' value = '0'>"
    +"<input type='hidden' name='pAllTotalIn' id='pAllTotalIn' value = '0'><input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.colSpan="11";
  mynewcell.innerHTML = "<label for='allbox_for'>全选</label>&nbsp"
    + "  <input type='button' value='汇总打印' class='BigButton' onclick='printMail();' title='打印所选工作事物'></input>";

}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<br>
<br>
<div id="giftList" style="padding-left: 10px; padding-right: 10px"></div>
<div id="returnNull">
</div>
<table align="center">
    <tr>
      <td>
        <input type="button" value="返回" class="BigButton" onclick="javascript:history.back()">
      </td>
   </tr>
</table>
</body>
</html>
