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
<title>回退</title>
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
//取步骤列表
  var param = 'runId=' + runId + '&flowId=' + flowId +'&prcsId=' + prcsId ;
  var url = requestUrl + "/getPreRunPrcs.act";
  var json = getJsonRs(url , param);
  if (json.rtState != '0') {
    alert(json.rtMsrg);
    return ;
  }
  var prcsList = json.rtData;
  var tmp = new Array();
  var i = 0 ;
  for (; i <  prcsList.length;i ++) {
    var prcs = prcsList[i];
    var la = "<label>"
    var radio = "<input type=\"radio\" name=\"selectedPrcs\" value=\""+ prcs.id +"\" id=\"selectedPrcs_"+ i +"\" />";
    la +=  radio  + " 步骤名：" + prcs.prcsName;
    la += "</label><br/>";
    tmp[i] = la;
  }
  $('prcsList').update(tmp.join(""));
}
/**
 * 回退到

 */
function backTo(id){
  if (!id) {
    var radios = $('prcsList').getElementsByTagName('input');
    for (var i = 0 ;i < radios.length ;i++) {
      var radio = radios[i];
      if(radio.checked){
        id = radio.value;
      }
    }
  }
  if (!id) {
    alert("请选择流程步骤");
    return ;
  }
  parentWindowObj.backTo(id , true);
  var par = contextPath + "<%=moduleContextPath %>/flowrun/list/index.jsp?skin="+ skin +"&sortId=" + sortId;
  parentWindowObj.mouse_is_out = false;
  parentWindowObj.parent.location = par;
  myClose();
}
function myClose() {
  close();
}
</script>
</head>
<body onload="doInit()">
 <div align=center><table class="TableList" width="300px">
    <tr class=TableHeader><td align=left><img src='<%=imgPath %>/green_arrow.gif'/>请选择回退步骤：</td></tr>
    <tr><td align=left id='prcsList'></td></tr>
    <tr><td align=center><input value='确认' type='button'  class="SmallButtonW" onclick='backTo()'>&nbsp;&nbsp;<input value='关闭' type='button' class="SmallButtonW"  onclick="myClose()"></td></tr>
    </table></div>
</body>
</html>