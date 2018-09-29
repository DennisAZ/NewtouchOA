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
  var url = "<%=contextPath%>/test/cy/code/act/YHFlowRunPrcsAct/show.act"; 
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
    url = "<%=contextPath%>/test/cy/code/act/YHFlowRunPrcsAct/updateField.act";
  }else {
    url = "<%=contextPath%>/test/cy/code/act/YHFlowRunPrcsAct/addField.act";
  }
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  alert(rtJson.rtMsrg);  
}
function goBack() {
  window.location.href = "<%=contextPath %>/test/code/flowrunprcs/list.jsp?pageNo=" + pageNo;
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
  <input type="hidden" id="dtoClass" name="dtoClass" value="test.cy.code.YHFlowRunPrcs"/>
  <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>RUN_ID:</td>
    <td>
<input type="text" id="runId" name="runId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_ID:</td>
    <td>
<input type="text" id="prcsId" name="prcsId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>USER_ID:</td>
    <td>
<input type="text" id="userId" name="userId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_TIME:</td>
    <td>
<input type="text" id="prcsTime" name="prcsTime" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>DELIVER_TIME:</td>
    <td>
<input type="text" id="deliverTime" name="deliverTime" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_FLAG:</td>
    <td>
<input type="text" id="prcsFlag" name="prcsFlag" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FLOW_PRCS:</td>
    <td>
<input type="text" id="flowPrcs" name="flowPrcs" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>OP_FLAG:</td>
    <td>
<input type="text" id="opFlag" name="opFlag" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>TOP_FLAG:</td>
    <td>
<input type="text" id="topFlag" name="topFlag" class="SmallInput" maxlength="5" value=""/>
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
    <td>PARENT:</td>
    <td>
<input type="text" id="parent" name="parent" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>CHILD_RUN:</td>
    <td>
<input type="text" id="childRun" name="childRun" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>TIME_OUT:</td>
    <td>
<input type="text" id="timeOut" name="timeOut" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FREE_ITEM:</td>
    <td>
<input type="text" id="freeItem" name="freeItem" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>CREATE_TIME:</td>
    <td>
<input type="text" id="createTime" name="createTime" class="SmallInput" maxlength="5" value=""/>
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