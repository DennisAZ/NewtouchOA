<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>不限制IP用户设置 </title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHAccesscontrolAct/getAccessControl.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    document.getElementById("accessControlId").value = document.getElementById("paraValue").value;
//    if(document.getElementById("accessControlId").value!=""){
//      bindDesc([{cntrlId:"accessControlId", dsDef:"PERSON,SEQ_ID,USER_NAME"}
//    	        ]);
//    }
    if($("accessControlId") && $("accessControlId").value.trim()){
      bindDesc([{cntrlId:"accessControlId", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function commit(){
  var accessControlId = document.getElementById("accessControlId").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHAccesscontrolAct/updateAccessControl.act?accessControlId="+accessControlId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  location = "<%=contextPath %>/core/funcs/system/accesscontrol/user/submit.jsp";
}

function SelectUser(userId,domId){ 
  URL = "/yh/core/funcs/dept/userselect.jsp?TO_ID=" + userId + "&TO_NAME=" + domId; 
  openDialog(URL,'400', '350'); 
}

function ClearUser(){ 
  var args = $A(arguments); 
  for(var i = 0; i < args.length; i++ ){ 
    var cntrl = $(args[i]); 
    if(cntrl){ 
      if (cntrl.tagName.toLowerCase() == "td" 
        || cntrl.tagName.toLowerCase() == "div" 
        || cntrl.tagName.toLowerCase() == "span") { 
        cntrl.innerHTML = ''; 
      } else{ 
        cntrl.value =''; 
      } 
    } 
  } 
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Small1"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">不限制IP用户设置</span>
    &nbsp;
    指定登录和考勤不限制IP的用户
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" method="post" name="form1" id="form1">
  <table class="TableBlock" width="750" align="center" >
  <input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" name="paraName" id="paraName" value="">
  <input type="hidden" name="paraValue" id="paraValue" value="">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.accesscontrol.data.YHAccesscontrol.java"/>
    <tr>
      <td class="TableData">
        <input type="hidden" name="accessControlId" id="accessControlId" value="">
        <textarea cols="105" name="accessControlIdDesc" id="accessControlIdDesc" rows="10" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['accessControlId', 'accessControlIdDesc'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('accessControlId', 'accessControlIdDesc')">清空</a>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" class="BigButton" onclick="commit()">&nbsp;&nbsp;
      </td>
    </tr>
     </table>
   </form>
</body>
</html>