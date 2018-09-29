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
  var url = "<%=contextPath%>/test/cy/code/act/YHFlowTypeAct/show.act"; 
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
    url = "<%=contextPath%>/test/cy/code/act/YHFlowTypeAct/updateField.act";
  }else {
    url = "<%=contextPath%>/test/cy/code/act/YHFlowTypeAct/addField.act";
  }
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  alert(rtJson.rtMsrg);  
}
function goBack() {
  window.location.href = "<%=contextPath %>/test/code/flowtype/list.jsp?pageNo=" + pageNo;
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
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.workflow.data.YHFlowType"/>
  <table cellscpacing="1" cellpadding="3" width="450">
  <tr class="TableLine1">
    <td>FLOW_NAME:</td>
    <td>
<input type="text" id="flowName" name="flowName" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FORM_SEQ_ID:</td>
    <td>
<input type="text" id="formSeqId" name="formSeqId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FLOW_DOC:</td>
    <td>
<input type="text" id="flowDoc" name="flowDoc" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FLOW_TYPE:</td>
    <td>
<input type="text" id="flowType" name="flowType" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>MANAGE_USER:</td>
    <td>
<input type="text" id="manageUser" name="manageUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FLOW_NO:</td>
    <td>
<input type="text" id="flowNo" name="flowNo" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FLOW_SORT:</td>
    <td>
<input type="text" id="flowSort" name="flowSort" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_NAME:</td>
    <td>
<input type="text" id="autoName" name="autoName" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_NUM:</td>
    <td>
<input type="text" id="autoNum" name="autoNum" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_LEN:</td>
    <td>
<input type="text" id="autoLen" name="autoLen" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>QUERY_USER:</td>
    <td>
<input type="text" id="queryUser" name="queryUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FLOW_DESC:</td>
    <td>
<input type="text" id="flowDesc" name="flowDesc" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>AUTO_EDIT:</td>
    <td>
<input type="text" id="autoEdit" name="autoEdit" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>NEW_USER:</td>
    <td>
<input type="text" id="newUser" name="newUser" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>QUERY_ITEM:</td>
    <td>
<input type="text" id="queryItem" name="queryItem" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>COMMENT_PRIV:</td>
    <td>
<input type="text" id="commentPriv" name="commentPriv" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>DEPT_ID:</td>
    <td>
<input type="text" id="deptId" name="deptId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FREE_PRESET:</td>
    <td>
<input type="text" id="freePreset" name="freePreset" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>FREE_OTHER:</td>
    <td>
<input type="text" id="freeOther" name="freeOther" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>QUERY_USER_DEPT:</td>
    <td>
<input type="text" id="queryUserDept" name="queryUserDept" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>MANAGE_USER_DEPT:</td>
    <td>
<input type="text" id="manageUserDept" name="manageUserDept" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>EDIT_PRIV:</td>
    <td>
<input type="text" id="editPriv" name="editPriv" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>LIST_FLDS_STR:</td>
    <td>
<input type="text" id="listFldsStr" name="listFldsStr" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>ALLOW_PRE_SET:</td>
    <td>
<input type="text" id="allowPreSet" name="allowPreSet" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>MODEL_ID:</td>
    <td>
<input type="text" id="modelId" name="modelId" class="SmallInput" maxlength="5" value=""/>
    </td>
    <td></td>
  </tr>
  <tr class="TableLine1">
    <td>MODEL_NAME:</td>
    <td>
<input type="text" id="modelName" name="modelName" class="SmallInput" maxlength="5" value=""/>
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