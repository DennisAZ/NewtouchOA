<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String sortName = request.getParameter("sortName");
if (sortName == null) {
  sortName = "";
}
String skin = request.getParameter("skin");
String skinJs = "messages";
if (skin != null && !"".equals(skin)) {
  skinJs = "messages_" + skin;
} else {
  skin = "";
}
%>
<html>
<head>
<title>工作监控</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/workflow/flowrun/manage/js/flowRunManager.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript">
var cfgs = null;
var pageMgr = null;
var sortId = "<%=sortId%>";
var sortName = "<%=sortName%>";
var skin = "<%=skin%>";
function doInit(){ 
  skinObjectToSpan(flowrun_manage_index);
  if (sortName) {
    sortId = getSortIdsByName(sortName);
    $('sortId').value = sortId;
  }
  loadFlowType() ;
  
  var url =  contextPath + "/yh/core/funcs/workflow/act/YHMyWorkControlAct/getMyManagerWork1.act";
  cfgs = {
    dataAction: url, 
    container: "listContainer",
    paramFunc: getParam,
    moduleName:"flow",       
    showRecordCnt: true,
//    <td nowrap align="center" id="subject" name="subject" onClick="javascript:order_by('SUBJECT');" style="cursor:pointer;"><u>标题</u></td>
    colums: [
       {type:"selfdef", text:"状态", width:"5%",render:flowStautsRender},
       //{type:"data", name:"runId", style:"cursor:pointer;", text:"<a onclick='orderby(\"runId\");',id:\'runIds\',style:'cursor:pointer'>流水号</a>",width:"5%",render:flowRunIdRender},
       //{type:"data", name:"flowName", text:"<a onclick='orderby(\"flowName\");',style:'cursor:pointer'>流程名称</a>",width:"17%",render:flowNameRender},
       {type:"data", name:"runId", id:"runId",text:"流水号",width:"5%",render:flowRunIdRender},
       {type:"hidden", name:"prcsId"},
       {type:"data", name:"flowName", id:"flowName", text:"流程名称",width:"17%",render:flowNameRender},
       {type:"data", name:"runName", text:tooltipMsg1, width:"22%",render:flowRunNameRender}, 
       {type:"data", name:"prcsName", text:"当前步骤 ", width:"11%",render:flowRunPrcsRender},
       {type:"data", name:"userName", text:"当前主办人", width:"8%",render:flowRunUserRender},
       {type:"selfdef", id:"handleTime",name:"handleTime",text:"办理时间",width:"16%",render:flowTimeRender},
       {type:"selfdef", id:"precTime",name:"precTime",text:"操作",width:"15%",render:flowOpRender},
       {type:"hidden", name:"prcsFlag"},
       {type:"hidden", name:"prcsTime",dataType:"dateTime"},
       {type:"hidden", name:"flowPrcs"},
       {type:"hidden", name:"createTime",dataType:"dateTime"},
       {type:"hidden", name:"flowType"},
       {type:"hidden", name:"userId"},
       {type:"hidden", name:"deliverTime",dataType:"dateTime"},
       {type:"hidden", name:"timeOut"},
       {type:"hidden", name:"timeOutType"},
       {type:"hidden", name:"freeOther"},
       {type:"hidden", name:"flowId"},
       {type:"hidden", name:"endTime"}
     ]
  };
}
/*
var filed="runId"; 
var ascDesc = '1';
function orderby(filedtmp){
  alert(filedtmp);
 if(filed == filedtmp){
   if(ascDesc == '1'){
      ascDesc = '0';
    }else{
    ascDesc = '1';
   }
  }else{
    filed = filedtmp;
  ascDesc = '1';
  } doDesc();
 //doDesc();
 //return false;
}*/
function doDesc(){  
  if(filed=="runId"){
    if(ascDesc == '0'){ alert("ff");
   //$('runId').update("<u>流水号</u>");
   $('runIds').update("<u>流水号</u>");
      // <img border=0 src=\"<%=imgPath%>/arrow_up.gif\" width=\"11\" height=\"10\">");
   //$('flowName').update("<u>流程名称</u>");
   }
  }else{
  //alert("eee");
 }
  
}
function loadFlowType(){
  var url = contextPath+'/yh/core/funcs/workflow/act/YHFlowManageAct/getFlowTypeJson.act?sortId='+ sortId;
  var json = getJsonRsAsyn(url , "", doFlowTypeJson);
}
function doFlowTypeJson(json) {
  var rtData = json.rtData;   
  for(var i = 0 ;i < rtData.length ; i ++) {      
    var opt = document.createElement("option") ;      
    opt.value = rtData[i].seqId ;      
    opt.innerHTML = rtData[i].flowName ;      
    $('flowList').appendChild(opt) ;                        
  }    
}
function mySearch(){
 // 
  var ss =  document.form1.runId.value;
  // alert(ss);
   if(isNaN(ss)){
     alert("流水号必须是数字!!");
     return false;
     }
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  //alert("dd");
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('listContainer').style.display = "";
    if( $('flowRunOpTab')){
       $('flowRunOpTab').style.display = "";
     }
    $('msrg').style.display = "none";
  }else{
    WarningMsrg('没有检索到数据!', 'msrg','info');
    $('msrg').style.display = "";
    $('listContainer').style.display = "none";
  }
}
  //工作监控查询 
function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}
</script>
</head>

<body onload="doInit()">
<form  name="form1" id="form1">
<input type=hidden value="<%=sortId %>" id="sortId" name="sortId"/>
<!-- <table id="flow_table" border="0" width="1215" height="60"  class="TableList" >  -->
<table id="flow_table" border="0" width="100%" height="60"  class="TableList" >
  <tr class=TableLine1>
  <td><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle">请选择要监控的流程：
   <select name="flowList" onmouseover="this.setStyle({'width':'auto'})" onChange="this.setStyle({'width':'100px'})" id="flowList" style="width:100px">
  <option value="All" id="span1"></option>
  </select>&nbsp;&nbsp;
  <select name="userType" id="userType">
  <option value="0">当前主办人</option>
  <option value="1">流程发起人</option>
  </select>
&nbsp;&nbsp;
  <input type="hidden" name="user" id="user" value="" />
  <input type="text" name="userDesc" id="userDesc" style="vertical-align: top;" size="10" class="SmallStatic" size="10" value="" READONLY>
  <!--selectUser() 这个函数是多选  -->
  <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['user', 'userDesc']);">选择</a>
  <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
  </td> 
  </tr>
   <tr class="TableLine2">
    <td valign="absmiddle" >
    	&nbsp;流水号： <input type="text" id="runId" name="runId" class="SmallInput" size="5" value="" onkeypress=""> <!--if(event.keyCode==13)  zan bu kao lv -->
    	&nbsp;名称/文号：<input type="text" id="runName" name="runName" size="20" class="SmallInput">&nbsp;
      &nbsp;<input type="button" id="queryBtn" class="BigButton" onClick="mySearch();" value="查询">
      &nbsp;<input type="button" id="smsBtn" class="SmallButton" style="display:none" value="催办超时流程" onclick="">
    </td>
  </tr>   
 </table>
 </form> 
 <div style="padding-top:10px"></div>
 <div id = "listContainer" style="display:none"> </div>
  <div id="msrg" align=center style="display:none">
  </div> 
</body>
</html>