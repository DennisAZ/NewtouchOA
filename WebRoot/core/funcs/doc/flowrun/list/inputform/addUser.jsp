<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %> 
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String runId = request.getParameter("runId");
String flowId = request.getParameter("flowId");
String prcsId = request.getParameter("prcsId");
String flowPrcs = request.getParameter("flowPrcs");
String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
if (skin == null) {
  skin = "";
}
%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加经办人</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script  type="text/Javascript">
var parentWindowObj = window.dialogArguments;
var runId = '<%=runId%>';
var flowId = '<%=flowId%>';
var prcsId = '<%=prcsId%>';
var flowPrcs = '<%=flowPrcs%>';
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";
var requestUrl = contextPath + "<%=moduleSrcPath %>/act/YHWorkHandleAct";
function doInit() {
//取步骤列表}
function myClose() {
  close();
}
function addUser(){
  var user = $('user').value;
  if (!user) {
    alert('请选择人员!');
    return ;
  }
  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowRunAct/addUser.act";
  var json = getJsonRs(url , 'user='+ user +'&runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId + '&flowPrcs=' + flowPrcs+ "&sortId=" + sortId + "&skin=" + skin);
  if(json.rtState == '0'){
    $('message').update(json.rtMsrg);
    close();
  } else {
    $('message').update(json.rtMsrg);
  }
}
function selectUser(){
  var Url = contextPath + "/core/funcs/doc/flowrun/list/inputform/userSelect/MultiUserSelect.jsp?isSingle=true&flowId=<%=flowId%>&runId=<%=runId%>&prcsId=<%=prcsId%>&flowPrcs=<%=flowPrcs%>";
  openDialog(Url, 510, 400);
}
</script>
</head>
<body onload="doInit()">
 <div align=center><table class="TableList" width="300px">
    <tr class=TableHeader>
    <td align=left>
    <input type=hidden name="user" id="user"/>
    <input type=text name="userDesc" id="userDesc" readonly/>
    <a href="javascript:void(0);" class="orgAdd" onclick="selectUser()">添加</a>
     <a href="javascript:void(0);" class="orgClear" onclick="$('user').value='';$('userDesc').value='';">清空</a>
    </td>
    </tr>
    <tr><td align=left style="color:red" id='message'></td></tr>
    <tr><td align=center><input value='添加' type='button'  class="SmallButtonW" onclick='addUser()'>&nbsp;&nbsp;<input value='关闭' type='button' class="SmallButtonW"  onclick="myClose()"></td></tr>
    </table></div>
</body>
</html>