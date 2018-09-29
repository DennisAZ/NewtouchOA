<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<title>Insert title here</title>
</head>
<script Language="JavaScript">
function CheckForm(){
   if($('userId').value == ""){ alert("请选择强制离线人员！");
     return false;
   }
   return true;
}
function userRepair(){
  var url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/userRepair.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
     location = contextPath + "/core/funcs/mysqldb/onlineuserinfo.jsp?type=1&msrg=" + rtJson.rtMsrg;
   }else{
     location = contextPath + "/core/funcs/mysqldb/onlineuserinfo.jsp?type=2&msrg=" + rtJson.rtMsrg;
   }
}
function userRepairById(){
  var userId = $('userId').value;
  if(!CheckForm()){
    return;
  }
  var url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/userRepairById.act?userIds=" + userId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
     location = contextPath + "/core/funcs/mysqldb/onlineuserinfo.jsp?type=1&msrg=" + encodeURIComponent(rtJson.rtMsrg);
   }else{
     location = contextPath + "/core/funcs/mysqldb/onlineuserinfo.jsp?type=2&msrg=" + encodeURIComponent(rtJson.rtMsrg);
   }
}
/**
 * 清空选择的用户

 */
function ClearUser(){
  var args = $A(arguments);
  for(var i = 0; i < args.length; i++ ){
    var cntrl = $(args[i]);
    if(cntrl){
      if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {
        cntrl.innerHTML =  '';
      } else{
        cntrl.value ='';
      }
    }
  }
}
</script>
<body  topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3"> 修正在线人员</span>
    </td>
  </tr>
</table>
<div align="center">
   <input type="button" value="修正在线人员" class="BigButtonC" onclick="userRepair()">   
</div>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3"> 强制离线</span>
    </td>
  </tr>
</table>
<form id="form1"  method="post" name="form1" >
 <table class="TableBlock" width="600" align="center">
    <tr  align="center">
      <td class="TableContent">强制离线人员：</td>
      <td class="TableData" align="left">
        <input type="hidden" name="userId" id="userId" value="">
        <textarea cols=40 name="userIdDesc" id="userIdDesc" rows=5 class="BigStatic" wrap="yes" readonly></textarea>
        &nbsp;<a href="javascript:void(0)" class="orgAdd" onClick="selectUser(['userId', 'userIdDesc']);" title="添加收件人">添加</a>
        &nbsp; <a href="javascript:void(0)" class="orgClear" onClick="ClearUser('userId', 'userIdDesc')" title="清空收件人">清空</a><br>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" class="BigButton" onclick="userRepairById()">
      </td>
    </tr>
</table>
</form>
</body>
</html>