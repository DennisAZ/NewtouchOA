<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
if(seqId == null) {
  seqId = "";
}
String pageNo = request.getParameter("pageNo");
if(pageNo == null) {
  pageNo = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加或修改页面</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/rad/CodeUtil/radio.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var pageNo = "<%=pageNo%>";
var smgr ;
var rmgr ;
function doInit() {
  var url = "<%=contextPath%>/test/cy/code/act/YHFlowRunAct/show.act"; 
  if(seqId){
    var rtJson = getJsonRs(url, "seqId="  + seqId); 
    if (rtJson.rtState == "0") {
      bindJson2Cntrl(rtJson.rtData);
      document.getElementById("seqId").value = seqId;
    } else{  
      alert(rtJson.rtMsrg);
    }  
  }
}

function check(el) {
  var flag = true;
  var cntrl = document.getElementById(el);
  if(!cntrl.value) {
	  alert("标记编号不能为空！");
	  cntrl.focus();
	  flag = false;
  }
  if(!isNumber(cntrl.value)){
	  alert("标记编号必须填入数字！");
	  cntrl.focus();
  	flag = false;
  }
  cntrl = document.getElementById("flagDesc");
  if(!cntrl.value) {
  	alert("标记描述不能为空！");
  	cntrl.focus();
  	flag = false;
  }
  return flag;
}

function commitItem() {
//  if(!check()){
//    return;
//  }
  var url = "";
  if(seqId) {
    url = "<%=contextPath%>/test/cy/code/act/YHFlowRunAct/updateField.act";
  }else {
    url = "<%=contextPath%>/test/cy/code/act/YHFlowRunAct/addField.act";
  }
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  alert(rtJson.rtMsrg);  
}
function goBack() {
  window.location.href = "<%=contextPath %>/test/code/flowrun/list.jsp?pageNo=" + pageNo;
}
</script>
</head>
<body onload="doInit()">
<form name="form1" id="form1" method="post">
  <%
    if(seqId.equals("")) {
  %>   
    <h2><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>添加</h2> 
  <%
    }else {
  %>    
    <h2><img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img>修改</h2>
  <%
    }
  %>
  <input type="hidden" name="seqId" id="seqId" value="" />
  <input type="hidden" id="dtoClass" name="dtoClass" value="test.cy.code.YHFlowRun"/>
  <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>RUN_ID:</td>
    <td>
<input type="text" id="runId" name="runId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FLOW_ID:</td>
    <td>
<input type="text" id="flowId" name="flowId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>BEGIN_USER:</td>
    <td>
<input type="text" id="beginUser" name="beginUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>BEGIN_DEPT:</td>
    <td>
<input type="text" id="beginDept" name="beginDept" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>BEGIN_DEPT_NAME:</td>
    <td>
<input type="text" id="beginDeptName" name="beginDeptName" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>RUN_NAME:</td>
    <td>
<input type="text" id="runName" name="runName" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>BEGIN_TIME:</td>
    <td>
<input type="text" id="beginTime" name="beginTime" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>END_TIME:</td>
    <td>
<input type="text" id="endTime" name="endTime" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>ATTACHMENT_ID:</td>
    <td>
<input type="text" id="attachmentId" name="attachmentId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>ATTACHMENT_NAME:</td>
    <td>
<input type="text" id="attachmentName" name="attachmentName" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>DEL_FLAG:</td>
    <td>
<input type="text" id="delFlag" name="delFlag" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FOCUS_USER:</td>
    <td>
<input type="text" id="focusUser" name="focusUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PARENT_RUN:</td>
    <td>
<input type="text" id="parentRun" name="parentRun" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRE_SET:</td>
    <td>
<input type="text" id="preSet" name="preSet" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AIP_FILES:</td>
    <td>
<input type="text" id="aipFiles" name="aipFiles" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
<%
  if(seqId.equals("")) {
%>
  <tr class="TableLine1">
    <td colspan="3" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
    </td>
  </tr>
<%
  }else {
%>
  <tr class="TableLine1">
    <td colspan="3" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
      <input type="button" value="返回" class="SmallButton" onclick="goBack()">
    </td>
  </tr>
<%
  }
%>
  </table>
</form>
</body>
</html>