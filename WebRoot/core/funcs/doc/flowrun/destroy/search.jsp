<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
  String startTime = request.getParameter("startTime") == null ? "" :  request.getParameter("startTime");
  String endTime = request.getParameter("endTime") == null ? "" : request.getParameter("endTime") ;
  String runId = request.getParameter("runId") == null ? "" : request.getParameter("runId") ;
  String toId = request.getParameter("toId") == null ? "" : request.getParameter("toId") ;
  String flowId = request.getParameter("flowId") == null ? "" : request.getParameter("flowId") ;
  String runName = request.getParameter("runName") == null ? "" : request.getParameter("runName") ;
  runName = runName.replace("\"" , "\\\"");
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
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/flowrun/destroy/js/destroy.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/lables/<%=skinJs %>.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/skin.js"></script>
<script type="text/javascript">
var pageMgr = null;
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
function doInit(){
  skinObjectToSpan(flowrun_destroy_search); 
  var param = "startTime=<%=startTime%>&endDate=<%=endTime%>&toId=<%=toId%>&flowId=<%=flowId%>&runId=<%=runId%>&runName=<%=runName%>&sortId=<%=sortId%>" ;
  param = encodeURI(param);
  var styleself = {background:"#cbe1f5"};
  var url =  contextPath + "<%=moduleSrcPath %>/act/YHWorkDestroyAct/getWorkList.act?" + param;
  var cfgs = {
    dataAction: url,
    container: "listContainer",
    moduleName:"doc",
    showRecordCnt: true,
    colums: [
       {type:"selfdef", text:"选择", width: "7%",render:checkBoxRender},
       {type:"hidden", name:"seqId"},
       {type:"data", name:"runId", text:"流水号", width: "8%" ,render:flowRunIdRender,selfStyle:styleself},
       {type:"data", name:"flowRunName", text:tooltipMsg14, width: "25%",render:flowNameRender},           
       {type:"data", name:"beignTime", text:"开始时间", width:"15%",dataType:"dateTime",format:'yyyy-mm-dd'},
       {type:"hidden", name:"attachId"},
       {type:"data", name:"attach", text:"公共附件", width: "25%", dataType:"attach"},
       {type:"hidden", name:"flowId"},
       {type:"selfdef", text:"操作", width: "20%",render:flowOpRender}
       ]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    showCntrl('opTab');
  }else{
    WarningMsrg('没有检索到记录!', 'msrg',"info",400);
  }
}
</script>
<title>工作查询</title>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3" id="span5"> </span>
    &nbsp;<input type="button" class="BigButton" value="返回" onclick="location='index.jsp?sortId=<%=sortId %>&skin=<%=skin %>'">
    </td>
  </tr>
</table>
<div style="padding-top:10px"></div>

<div id="listContainer" style="margin-top:5px;display:none"></div>

<table  border=0 width="100%" id="opTab" style="display:none">
  <tr class="TableControl">
  <td colspan="10">
    &nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="checkAll(this);">
    <label for="allbox_for" style="cursor:pointer;"><u><b>全选</b></u></label> &nbsp;
    <input type="button"  value="批量销毁" class="BigButton" onClick="destroy();"> &nbsp;
    <input type="button"  value="批量还原" class="BigButton" onClick="recover();"> &nbsp;
  </td></tr>
</table>
<div id="msrg"  align="center"></div>

</body>
</html>