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
  getVisitType($('projVisitType'));
  getActiveType($("projActiveType"));
  //时间
  var parameters = {
      inputId:'projStartTime1',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'projStartTime2',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'projEndTime1',
      property:{isHaveTime:false}
      ,bindToBtn:'date3'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'projEndTime2',
      property:{isHaveTime:false}
      ,bindToBtn:'date4'
  };
  new Calendar(parameters);
  selectInProject();

}

var pageMgr = null;
var cfgs = null;

function selectInProject(){
  pYxTotal = 0;
  pAllTotal = 0;
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectAct/queryInProject.act?projType=0&projStatus=1";
   cfgs = {
    dataAction: url,
    container: "projectDiv",
    paramFunc: getParam,
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
       {type:"text", name:"projStartTime", text:"到京时间", width: "6%",align:"center",render:toStartDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projEndTime", text:"离京时间", width: "6%",align:"center",render:toEndDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"pYx", text:"参与外办人数", width: "7%",align:"center",render:topYx,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"pTotal", text:"参与总人数", width: "6%",align:"center",render:topTotal,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"printStatus", text:"打印状态", width: "5%",align:"center",render:toPrint,sortDef:{type:0, direct:"asc"}},
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
function getProject(){
  if(!isValidDateStr($("projStartTime1").value) && $("projStartTime1").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("projStartTime1").focus(); 
    $("projStartTime1").select(); 
    return ; 
  }
  if(!isValidDateStr($("projStartTime2").value)  && $("projStartTime2").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("projStartTime2").focus(); 
    $("projStartTime2").select(); 
    return ; 
  }
  if(!isValidDateStr($("projEndTime1").value)  && $("projEndTime1").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("projEndTime1").focus(); 
    $("projEndTime1").select(); 
    return ; 
  }
  if(!isValidDateStr($("projEndTime2").value)  && $("projEndTime2").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("projEndTime2").focus(); 
    $("projEndTime2").select(); 
    return ; 
  }
 // pYxTotal = 0;
 // pAllTotal = 0;
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  
  if(total>0){
    //$("allDiv").style.display = "";
    $("projectDiv").style.display = "";
    $('returnNull').style.display = "none";
   }else{
     $("allDiv").style.display = "none";
     $("projectDiv").style.display = "none";
     var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
         + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件信息!</div></td></tr>"
         );
     $('returnNull').update(table); 
   }
}
function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return   "<a href='#' onclick='openShowDetilIndex(" + seqId + ");'>详细信息</a> "
           +"<a href='#' onclick='updateStatus(" + seqId + ");'>恢复执行</a> "
           +"<a href='#' onclick='delProj(" + seqId + ");'> 删除 </a> ";
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
//申请
function openNewsIndex(seqId) {
  var myleft = (screen.availWidth - 800)/2;
  window.open("<%=contextPath%>/subsys/oa/profsys/in/baseinfo/news/index.jsp","","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=950,height=700,left=" + myleft + ",top=50");
}
//打印数据
function openPrint(seqStr) {
  var myleft = (screen.availWidth - 800)/2
  window.open("<%=contextPath%>/subsys/oa/profsys/in/baseinfo/news/print.jsp","","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=600,left=" + myleft + ",top=50");
}
//详细信息
function openShowDetilIndex(seqId) {
  var myleft = (screen.availWidth - 800)/2;
  window.open("<%=contextPath%>/subsys/oa/profsys/in/baseinfo/news/index.jsp?seqId="+seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=950,height=700,left=" + myleft + ",top=50");
}
//删除记录
function delProj(seqId) {
  URL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/delProj.act?seqId=" + seqId;
  if (window.confirm("确认要删除该记录吗？")) {
    window.location = URL;
    location.reload();
  }
}
//恢复执行
function updateStatus(seqId) {
  URL = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/updateStatus.act?seqId=" + seqId + "&status=0";
  if (window.confirm("确认要恢复执行此项目吗？")) {
    window.location = URL;
    location.reload();
  }
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

</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<br>
<form action="#"  method="post" name="form1" id="form1">
<table class="TableList" align="center">
  <tr class="TableContent">
        <td nowrap style="padding:3px">
         &nbsp;项目编号：

          <INPUT type="text"  name="projNum" id="projNum" size="10" value="" class="BigInput">
         &nbsp;项目类别：

        <select name="projActiveType" id="projActiveType">
        <option value="">全部类别</option>
        </select>
      &nbsp;到京时间：

       <INPUT type="text" class="BigInput" name="projStartTime1" id="projStartTime1" size="10" value="">
       <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
       &nbsp;至

       <INPUT type="text" class="BigInput" name="projStartTime2" id="projStartTime2" size="10" value="">
       <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
  </tr>
  <tr class="TableContent">
    <td nowrap>&nbsp;团组名称：

    <INPUT type="text"  name="projGropName" id="projGropName" size="10" value="" class="BigInput">
    &nbsp;出访类别：

     <select name="projVisitType" id="projVisitType" >
     <option value="">全部类别</option>

     </select>
    &nbsp;离京时间：

     <INPUT type="text" class="BigInput" name="projEndTime1" id="projEndTime1" size="10" value="">
     <img id="date3" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
     &nbsp;至

     <INPUT type="text" class="BigInput" name="projEndTime2" id="projEndTime2" size="10" value="">
     <img id="date4" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
    </td>
  </tr>
  <tr class="TableContent">
     <td nowrap>
       &nbsp;负责人：
       <input type="hidden" name="projLeader" id="projLeader" value="">   
        <input type="text" name="projLeaderName" id="projLeaderName"  size="13" class="BigStatic" value="" readonly>&nbsp;
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['projLeader','projLeaderName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('projLeader').value='';$('projLeaderName').value='';">清空</a>
       &nbsp;所属部门：
        <input type="hidden" name="deptId" id="deptId" value="">
        <input type="text" name="deptIdName" id="deptIdName" value="" class=BigStatic size=20 maxlength=100 readonly>
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['deptId','deptIdName']);">选择</a>
       <a href="javascript:;" class="orgClear" onClick="$('deptId').value='';$('deptIdName').value='';">清空</a>
     </td>
    </tr>  
  <tr class="TableControl">
    <td rowspan=2 align="center">
         <input type="hidden" name="deptId" id="deptId" value="">
              <input type="hidden" name="deptId" id="deptId" value="">
      <input value="查询" type="button" class="BigButton" title="查询" name="button" onclick="getProject();">&nbsp;&nbsp;
       <input type="button"  class="BigButton" value="立项申请" onClick="openNewsIndex()">
    </td>
  </tr>   
</table>
</form>

<br>
<div id="projectDiv">
</div>
<div id="allDiv" style="display:none">
</div>
<div id="returnNull"></div>
<br>
</body>
</html>
