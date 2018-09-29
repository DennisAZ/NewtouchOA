<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ include file="/core/inc/header.jsp" %>
      <%@ page  import="yh.subsys.inforesouce.docmgr.data.YHDocConst,yh.core.util.YHUtility"%>
<%
String grade = request.getParameter("grade");  //等级
String migrade = request.getParameter("migrade");  //密级
String reDate = request.getParameter("reDate");//收文日期
String toUserName = YHUtility.encodeSpecial(request.getParameter("toUserName"));//承办人
String fromUnits = YHUtility.encodeSpecial(request.getParameter("fromUnits"));  //来文单位
String oppDocNo = YHUtility.encodeSpecial(request.getParameter("oppDocNo")); //原文编号
String title = YHUtility.encodeSpecial(request.getParameter("title"));  //标题
String pishi = YHUtility.encodeSpecial(request.getParameter("pishi")); //领导批示
String seqId = request.getParameter("seqId");//收文ID
String docNo = YHUtility.encodeSpecial(request.getParameter("docNo"));//文号
String useName = YHUtility.encodeSpecial(request.getParameter("useName"));//联系人
String deptName = YHUtility.encodeSpecial(request.getParameter("deptName"));//部门
String runId = request.getParameter("runId");//部门
String flowName = YHUtility.encodeSpecial(request.getParameter("flowName"));//部门

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var isParent = true;
var grade = "<%=grade%>";
var migrade = "<%=migrade%>";
var reDate = "<%=reDate%>";
var toUserName = "<%=toUserName%>";
var fromUnits = "<%=fromUnits%>";
var oppDocNo = "<%=oppDocNo%>";
var title = "<%=title%>";
var pishi = "<%=pishi%>";
var seqId = "<%=seqId%>";
var docNo = "<%=docNo%>";
var useName = "<%=useName%>";
var deptName = "<%=deptName%>";
var runId = "<%=runId%>";
var flowName = "<%=flowName%>";

var param = '等级='+ grade + "&密级="+ encodeURIComponent(migrade) +"&收文日期="+ reDate +"&承办人="+encodeURIComponent(toUserName)+
"&来文单位="+encodeURIComponent(fromUnits)+"&原文编号="+encodeURIComponent(oppDocNo)+"&标题="+encodeURIComponent(title)+
"&领导批示="+encodeURIComponent(pishi)+"&收文ID="+seqId +"&文号="+ docNo+ "&联系人="+encodeURIComponent(useName)+
"&部门="+encodeURIComponent(deptName) ;

function createNewWork1(flowName , par){
  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowRunAct/createWork.act";
  if (par) {
    par = "flowName=" + flowName + "&" + par;
  } else {
    par = 'flowName=' + flowName;
  }
  var json = getJsonRs(url ,  par);
  if(json.rtState == "0"){
    var flowId = json.rtData.flowId;
    var runId = json.rtData.runId;
    var url2 =  contextPath + "/core/funcs/workflow/flowrun/list/inputform/index.jsp?runId=" + runId + "&flowId=" + flowId + "&prcsId=1&flowPrcs=1&isNew=1&skin=receive";
    location.href = url2;
  }else{
    alert(json.rtMsrg);
  }
}



function doInit(){
  
    createNewWork1(flowName, param);
 
}
</script>
</head>
<body onload="doInit();">
</body>
</html>