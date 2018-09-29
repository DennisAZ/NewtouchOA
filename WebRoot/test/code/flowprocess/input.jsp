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
  var url = "<%=contextPath%>/test/cy/code/act/YHFlowProcessAct/show.act"; 
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
    url = "<%=contextPath%>/test/cy/code/act/YHFlowProcessAct/updateField.act";
  }else {
    url = "<%=contextPath%>/test/cy/code/act/YHFlowProcessAct/addField.act";
  }
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  alert(rtJson.rtMsrg);  
}
function goBack() {
  window.location.href = "<%=contextPath %>/test/code/flowprocess/list.jsp?pageNo=" + pageNo;
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
  <input type="hidden" id="dtoClass" name="dtoClass" value="test.cy.code.YHFlowProcess"/>
  <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>FLOW_SEQ_ID:</td>
    <td>
<input type="text" id="flowSeqId" name="flowSeqId" class="SmallInput" maxlength="5" value=""/>
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
    <td>PRCS_NAME:</td>
    <td>
<input type="text" id="prcsName" name="prcsName" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_USER:</td>
    <td>
<input type="text" id="prcsUser" name="prcsUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_ITEM:</td>
    <td>
<input type="text" id="prcsItem" name="prcsItem" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>HIDDEN_ITEM:</td>
    <td>
<input type="text" id="hiddenItem" name="hiddenItem" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_DEPT:</td>
    <td>
<input type="text" id="prcsDept" name="prcsDept" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_PRIV:</td>
    <td>
<input type="text" id="prcsPriv" name="prcsPriv" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_TO:</td>
    <td>
<input type="text" id="prcsTo" name="prcsTo" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>SET_LEFT:</td>
    <td>
<input type="text" id="setLeft" name="setLeft" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>SET_TOP:</td>
    <td>
<input type="text" id="setTop" name="setTop" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PLUGIN:</td>
    <td>
<input type="text" id="plugin" name="plugin" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_ITEM_AUTO:</td>
    <td>
<input type="text" id="prcsItemAuto" name="prcsItemAuto" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_IN:</td>
    <td>
<input type="text" id="prcsIn" name="prcsIn" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_OUT:</td>
    <td>
<input type="text" id="prcsOut" name="prcsOut" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FEEDBACK:</td>
    <td>
<input type="text" id="feedback" name="feedback" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_IN_SET:</td>
    <td>
<input type="text" id="prcsInSet" name="prcsInSet" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>PRCS_OUT_SET:</td>
    <td>
<input type="text" id="prcsOutSet" name="prcsOutSet" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_TYPE:</td>
    <td>
<input type="text" id="autoType" name="autoType" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_USER_OP:</td>
    <td>
<input type="text" id="autoUserOp" name="autoUserOp" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_USER:</td>
    <td>
<input type="text" id="autoUser" name="autoUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>USER_FILTER:</td>
    <td>
<input type="text" id="userFilter" name="userFilter" class="SmallInput" maxlength="5" value=""/>
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
    <td>TIME_EXCEPT:</td>
    <td>
<input type="text" id="timeExcept" name="timeExcept" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>SIGNLOOK:</td>
    <td>
<input type="text" id="signlook" name="signlook" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>TOP_DEFAULT:</td>
    <td>
<input type="text" id="topDefault" name="topDefault" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>USER_LOCK:</td>
    <td>
<input type="text" id="userLock" name="userLock" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>MAIL_TO:</td>
    <td>
<input type="text" id="mailTo" name="mailTo" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>SYNC_DEAL:</td>
    <td>
<input type="text" id="syncDeal" name="syncDeal" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>SYNC_DEAL_CHECK:</td>
    <td>
<input type="text" id="syncDealCheck" name="syncDealCheck" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>TURN_PRIV:</td>
    <td>
<input type="text" id="turnPriv" name="turnPriv" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>CHILD_FLOW:</td>
    <td>
<input type="text" id="childFlow" name="childFlow" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>GATHER_NODE:</td>
    <td>
<input type="text" id="gatherNode" name="gatherNode" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>ALLOW_BACK:</td>
    <td>
<input type="text" id="allowBack" name="allowBack" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>ATTACH_PRIV:</td>
    <td>
<input type="text" id="attachPriv" name="attachPriv" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_BASE_USER:</td>
    <td>
<input type="text" id="autoBaseUser" name="autoBaseUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>CONDITION_DESC:</td>
    <td>
<input type="text" id="conditionDesc" name="conditionDesc" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>RELATION:</td>
    <td>
<input type="text" id="relation" name="relation" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>REMIND_FLAG:</td>
    <td>
<input type="text" id="remindFlag" name="remindFlag" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>DISP_AIP:</td>
    <td>
<input type="text" id="dispAip" name="dispAip" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>TIME_OUT_TYPE:</td>
    <td>
<input type="text" id="timeOutType" name="timeOutType" class="SmallInput" maxlength="5" value=""/>
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