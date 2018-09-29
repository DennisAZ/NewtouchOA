<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
  String runId = request.getParameter("runId") == null ? "" : request.getParameter("runId") ;
  String runName = request.getParameter("runName") == null ? "" : request.getParameter("runName") ;
  runName = runName.replace("\"" , "\\\"");
  String startTime = request.getParameter("startTime") == null ? "" : request.getParameter("startTime") ;
  String endTime = request.getParameter("endTime") == null ? "" : request.getParameter("endTime") ;
  String userId = request.getParameter("userId") == null ? "" : request.getParameter("userId") ;
  String logType = request.getParameter("logType") == null ? "" : request.getParameter("logType") ;
  String ipAddrss = request.getParameter("ipAddrss") == null ? "" : request.getParameter("ipAddrss") ;
  String sortId = request.getParameter("sortId");
  if (sortId == null) {
    sortId = "";
  }
  String skin = request.getParameter("skin");
  String skinJs = "messages";
  if (skin != null && !"".equals(skin)) {
    skinJs = "messages_" + skin;
  } else {
    skin = "";
  }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/flowrun/log/js/flowRunLog.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var pageMgr = null;
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
function doInit(){
  skinObjectToSpan(flowrun_log_search);
  var param = "startTime=<%=startTime%>&endTime=<%=endTime%>&logType=<%=logType%>&ipAddrss=<%=ipAddrss%>&userId=<%=userId%>&flowId=<%=flowId%>&runId=<%=runId%>&runName=<%=runName%>&sortId=<%=sortId%>";
  param = encodeURI(param);
  var styleself = {background:"#cbe1f5"};
  var url =  contextPath + "<%=moduleSrcPath %>/act/YHWorkLogAct/getWorkLogList.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"doc",
    showRecordCnt: true,
    colums: [
       {type:"selfdef", text:"选择", width: "5%",render:checkBoxRender},
       {type:"hidden", name:"seqId"},
       {type:"data", name:"runId", text:"流水号", width: "5%"},
       {type:"data", name:"flowRunName", text:tooltipMsg1, width: "20%"},      
       {type:"data", name:"flowPrcsNo", text:"步骤号", width:"5%",dataType:"int"},  
       {type:"data", name:"flowPrcsName", text:"步骤名称", width: "12%",render:prcsNameRender},   
       {type:"data", name:"userName", text:"相关人员", width: "10%",render:userRender},           
       {type:"data", name:"beignTime", text:"开始时间", width: "12%",dataType:"dateTime",format:'yyyy-mm-dd'},
       {type:"data", name:"ipAddrss", text:"IP地址", width: "12%"},     
       {type:"data", name:"content", text:"内容", width: "12%"},
       {type:"hidden", name:"flowId"}
       ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    showCntrl('opTable');
  }else{
    WarningMsrg("没有检索到日志！", 'msrg',"info",400);
  }
}
</script>
<title>日志查询结果</title>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/green_arrow.gif" WIDTH="20" HEIGHT="20" align="absmiddle"><span class="big3"> 流程日志</span>
    &nbsp;<input type="button" class="BigButton" value="返回" onclick="location='index.jsp?sortId=<%=sortId %>&skin=<%=skin %>'">
    </td>
  </tr>
</table>
<div style="padding-top:10px"></div>
<div id="listContainer" style="margin-top:5px;display:none"></div>
<div id="msrg"  align="center"></div>
<table   border=0 width="100%" id="opTable" style="display:none">
<tr class="TableControl">
  <td colspan="10">
    &nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="checkAll(this);">
    <label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label> &nbsp;
    <a href="javascript:doDelete();" title="删除所选日志"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除</a>&nbsp;
  </td>
  </tr>
</table>


</body>
</html>