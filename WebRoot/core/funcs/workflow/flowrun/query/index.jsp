<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
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
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
  <script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/skin.js"></script>
<script type="text/javascript"><!--
 var loginUserId = <%=loginUser.getSeqId()%>;
 var isAdmin = <%=loginUser.isAdmin()%>;
 var isOaAdmin = <%=loginUser.isAdminRole()%>;
 var cfgs = null;
 var pageMgr = null;
 var sortId = "<%=sortId%>";
 var sortName = "<%=sortName%>";
 var skin = "<%=skin%>";
  
 function doQuery(){
   var beginDate = document.getElementById('startTime');
   var endDate = document.getElementById("endTime");  
   var ss =  document.queryForm.runId.value;
  // alert(ss);
   if(isNaN(ss)){
     alert("流水号必须是数字!!");
     return false;
     }
 
   var beginInt;
   var endInt;
   var beginArray = beginDate.value.split("-");
   var endArray = endDate.value.split("-");
   for(var i = 0 ; i<beginArray.length; i++){
     beginInt = parseInt(" " + beginArray[i]+ "",10);  
     endInt = parseInt(" " + endArray[i]+ "",10);
     if((beginInt - endInt) > 0){
       alert("起始日期不能大于结束日期!");
       endDate.focus();
       endDate.select();
       return false;
     }else if(beginInt - endInt<0){
       break;
     }  
   }
   var header = getHeader();
   var column = getColumn(header);
   cfgs.colums = column;
  // if(!pageMgr){
     pageMgr = new YHJsPage(cfgs);
     pageMgr.show();
     pageMgr.search();
  // }else{
    // pageMgr.search();
  // }
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
     if( $('flowRunOpTab')){
       $('flowRunOpTab').style.display = "none";
     }
     $('listContainer').style.display = "none";
   }
 }
 function getHeader() {
   var flowId = $('flowList').value;
   if (flowId != '0') {
     var url = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct/getHeader.act?flowId=" + flowId;
     var json = getJsonRs(url);
     if (json.rtState == '0') {
       var header = json.rtMsrg;
       return header;
     }
   } else {
    return "";
   }
 }
 function query() {
   location.href='flowList.jsp?skin='+ skin +'&sortId=' + sortId ;
 }
 function doInit(){
   skinObjectToSpan(flowrun_query_index);
   if (sortName) {
     sortId = getSortIdsByName(sortName);
     $('sortId').value = sortId;
   }
   loadFlowType() ;
   var beginParameters = {
       inputId:'startTime',
       property:{isHaveTime:false}
       ,bindToBtn:'beginDateImg'
   };
   new Calendar(beginParameters);
   var endParameters = {
       inputId:'endTime',
       property:{isHaveTime:false}
       ,bindToBtn:'endDateImg'
   };
   new Calendar(endParameters);
   $('runId').focus();
   var param = getParam();
   var url =  contextPath + "/yh/core/funcs/workflow/act/YHWorkQueryAct/getWorkList1.act";
   var styleself = {background:"#cbe1f5"};
   var columns = getColumn();
   cfgs = {
     dataAction: url,
     container: "listContainer",
     paramFunc: getParam,
     moduleName:"flow",
     showRecordCnt: true,
     colums:columns
   };
}
 
function getColumn(column) {
  var cols =  [
               <% if (loginUser.isAdmin()) { %>
               {type:"selfdef", align:"center", text:"选择", width:"7%",render:checkBoxRender},
               <% } %>
               {type:"data", name:"runId", text:"流水号",width:"8%",render:flowRunIdRender},//,selfStyle:styleself 加上就会出现绿颜色
               {type:"data", name:"runName", text:tooltipMsg1, width:"26%",render:flowNameRender}, 
               {type:"data", name:"beginTime", text:"开始时间", width:"17%", dataType:"dateTime",format:'yyyy-mm-dd'},
               {type:"hidden", name:"endTime"},
               {type:"hidden", name:"attachId"},
               {type:"hidden", name:"attachName"},
               {type:"selfdef", name:"attach", text:"公共附件", width:"17%", render:attachRender},
               {type:"selfdef", name:"status", text:"状态", width:"7%",render:flowStautsRender}];
     if(column) {
       var headers = column.split(",");
       for (var i= 0 ; i< headers.length ; i++) {
         if (headers[i]) {
           cols.push({type:"data", name:headers[i], text:headers[i], width:"5%"});
         }
       }
     }
     cols.push({type:"selfdef", text:"操作",width:"18%", render:opRender});
     cols.push({type:"hidden", name:"flowId"});
     cols.push({type:"hidden", name:"listFldsStr"});
     cols.push({type:"hidden", name:"flowName"});
     cols.push({type:"hidden", name:"freeOther"});
     cols.push({type:"hidden", name:"flowType"});
     cols.push({type:"hidden", name:"manageUser"});
     cols.push({type:"hidden", name:"queryUser"});
     cols.push({type:"hidden", name:"commentPriv"});
     cols.push({type:"hidden", name:"focusUser"});
     cols.push({type:"hidden", name:"editPriv"});
     cols.push({type:"hidden", name:"flowType"});
     cols.push({type:"hidden", name:"manageUserDept"});
     cols.push({type:"hidden", name:"queryUserDept"});
  return cols;
}


 
 --></script>
 
</head>
<body onload="doInit()">
<form  name="queryForm" id="queryForm">
<input type=hidden value="<%=sortId %>" id="sortId" name="sortId"/>
<table id="flowTable" border="0" width="100%"  class="TableList"  >
  <tr class="TableLine1">
  <td align="left"> 
  流程：  <select name="flowList" onmouseover="this.setStyle({'width':'auto'})" onChange="this.setStyle({'width':'100px'})" id="flowList" style="width:100px">
  <option value="0" id="span1"></option>
  </select>&nbsp;&nbsp;
  
  状态：<select name="flowStatus" id="flowStatus">
  <option value="ALL">所有状态</option>
  <option value="0">正在执行</option>
  <option value="1">已经结束</option>
  </select>&nbsp;&nbsp;
   范围：
  <select name="flowQueryType" id="flowQueryType" onchange="setUserChange(this.value)">
  <option value="0">所有范围</option>
  <option value="1">我发起的</option>
  <option value="2" selected>我经办的</option>
  <option value="3" >我管理的</option>
  <option value="4" >我关注的</option>
  <option value="5" >指定发起人</option>
  </select>
  <span id="setUser" style="display:none">
  <INPUT style="VERTICAL-ALIGN: top" class=SmallStatic value="" readOnly size=10 id=toName name=toName>
   <A class=orgAdd onclick="selectSingleUser(['toId' , 'toName'])" href="javascript:;">选择</A> 
   <INPUT value=liuhan type=hidden name=toId id=toId> 
  </span>
  从  <input type="text" id="startTime" name="startTime" size="10" maxlength="19" readonly class="BigStatic" value="">
  <img id="beginDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      到
  <input type="text" id="endTime" name="endTime" size="10" maxlength="19"  readonly class="BigStatic">
  <img id="endDateImg" src="<%=imgPath %>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
  <a href="javascript:empty_date()">清空</a>
    </td> 
  </tr><tr class="TableLine2">
  <td align="left"> 
  流水号<input type="text" id="runId" name="runId" size="6" class="SmallInput" value="">&nbsp;
    名称/文号<input type="text" id="runName" name="runName" size="20" class="SmallInput" value="">
    <input id="queryBtn" type="button" class="BigButton" onclick="doQuery()" value="开始查询">
  <input id="queryBtn" type="button" title="高级查询,允许设定复杂查询条件" class="BigButton" onclick="query()" value="高级查询">
    </div>
  </td> 
  </tr>
  </table>
  </form>
  <div style="padding-top:10px"></div>
  
  <div id = "listContainer" style="display:none"> </div>
  <% if (loginUser.isAdmin()) { %>
  <table class="TableBlock no-top-border" border=0 width="100%" style="margin:0;display:none" id="flowRunOpTab" >
  <tr class="TableData">
  <td colspan="10">
 	&nbsp;&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);">
  <label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label> &nbsp;
   <input type="button"  onclick="exportZip()"  value="导出ZIP" class="BigButton"  title="批量导出"> &nbsp;
  <input type="button"  onclick="delWorkFlow()"  value="管理人员删除" class="BigButtonC" > &nbsp; 
  <input type="button"  onclick="endWorkFlow()"  value="强制结束" class="BigButton"  title="强制结束">
  </td>
  </tr>
  </table>
  <% } %>

  <div id="msrg" align=center style="display:none">
  </div>    
</body>
</html>