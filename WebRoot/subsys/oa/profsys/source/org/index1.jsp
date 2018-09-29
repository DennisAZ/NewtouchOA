<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>友好组织信息</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function showOrg(seqId) {
  var myleft = (screen.availWidth - 800)/2;
  window.open("<%=contextPath%>/subsys/oa/profsys/source/org/info.jsp?seqId="+seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=500,left=" + myleft + ",top=50");
}
function doOnload() {
  selectSourceOrg();
//时间
  var parameters = {
      inputId:'orgEstablishTime',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  var parameters = {
      inputId:'orgEstablishTime1',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
}
var pageMgr = null;
var cfgs = null;
function selectSourceOrg(){
  pYxTotal = 0;
  pAllTotal = 0;
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/source/org/act/YHSourceOrgAct/queryOrg.act";
   cfgs = {
    dataAction: url,
    container: "orgDiv",
    paramFunc: getParam,
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"orgNumT", text:"组织编号", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"orgNameT", text:"组织名称", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"hidden", name:"deptIdT", text:"国别", width: "1%",align:"center"},
       {type:"text", name:"deptNameT", text:"领导人", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"projVisitType", text:"规模", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"orgEstablishTimeT", text:"成立时间", width: "5%",align:"center",render:toDate,sortDef:{type:0, direct:"asc"}},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total>0){
  }else{
    $("orgDiv").style.display = "none";
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件信息!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
}
function getOrg(){
  if(!isValidDateStr($("orgEstablishTime").value) && $("orgEstablishTime").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("orgEstablishTime").focus(); 
    $("orgEstablishTime").select(); 
    return ; 
  }
  if(!isValidDateStr($("orgEstablishTime1").value)  && $("orgEstablishTime1").value != ""){
    alert("日期格式不对，应形如 1999-01-01"); 
    $("orgEstablishTime1").focus(); 
    $("orgEstablishTime1").select(); 
    return ; 
  }
   if(!pageMgr){
     pageMgr = new YHJsPage(cfgs);
     pageMgr.show();
   }else{
     pageMgr.search();
   }
   var total = pageMgr.pageInfo.totalRecord;
   if(total>0){
     $("orgDiv").style.display = "";
     $('returnNull').style.display = "none";
    }else{
      $("orgDiv").style.display = "none";
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
   return   "<a href='#' onclick='showOrg(" + seqId + ");'>详细信息</a> "
            +"<a href='#' onclick='updateOrg(" + seqId + ");'>编辑</a> "
            +"<a href='#' onclick='delOrg(" + seqId + ");'> 删除 </a> ";
}
function toDate(cellData, recordIndex, columInde){
   var orgEstablishTimeT = this.getCellData(recordIndex,"orgEstablishTimeT");
   return orgEstablishTimeT.substr(0,10);
}
function delOrg(seqId){
 if(window.confirm("确认要删除该记录吗？")){
   var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/source/org/act/YHSourceOrgAct/delOrg.act?seqId="+seqId; 
   var json=getJsonRs(requestURL); 
   if (json.rtState == '1') { 
     alert(json.rtMsrg); 
     return ; 
   }
   window.location.reload();
 }
}
function updateOrg(seqId){
  window.location.href = "<%=contextPath%>/subsys/oa/profsys/source/org/news/update.jsp?seqId="+seqId; 
}
</script>
</head>
<body onLoad="doOnload();" topmargin="5px">
<br>
<form id="form1" method="post" name="form1">
<table class="TableList" align="center">
  <tr class="TableContent">
    <td nowrap style="padding:3px">
         &nbsp;组织编号：
          <INPUT type="text"  name="orgNum" id="orgNum" size="10" class="BigInput">
         &nbsp;国别：
        <INPUT type="text"  name="orgNation" id="orgNation" size="10" class="BigInput">
         &nbsp;领导人：
       <input type="text" name="orgLeader" id="orgLeader" value="" size="10"  >   
      </td>
  </tr>
  <tr class="TableContent">
    <td nowrap>
    &nbsp;组织名称：
    <INPUT type="text"  name="orgName" id="orgName" size="10" class="BigInput">
    &nbsp;规模：
     <INPUT type="text"  name="orgScale" id="orgScale" size="10" class="BigInput">
    &nbsp;成立时间：
     <INPUT type="text"  name="orgEstablishTime" id="orgEstablishTime" class=BigInput size="10" value="">
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
     &nbsp;至
     <INPUT type="text"  name="orgEstablishTime1" id="orgEstablishTime1" class=BigInput size="10" value="">
        <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
    </tr>
  <tr class="TableControl">
    <td rowspan=2 align="center">
      <input class="BigButton" value="查询"  type="button" name="button" onclick="getOrg();">&nbsp;&nbsp;
    </td>
  </tr>   
</table>
</form>
<br>
<div id="orgDiv" style="padding:10px;"></div>
<div id="returnNull"></div>
</body>
</html>