<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<head>
<title>修改界面设置</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/tab.js" ></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getRemindPriv.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    $("user").value = rtJson.rtData.remindPriv;
    $("seqId").value = rtJson.rtData.seqId;
    //alert($("user").value);
    if( $("user").value &&  $("user").value.trim()){
      bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"}
    	        ]);
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function doSubmit(){
  var seqId = $("seqId").value;
  var remindPriv = $("user").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/updateRemindPriv.act";
  var rtJson = getJsonRs(url, "seqId="+seqId+"&remindPriv="+remindPriv);
  if (rtJson.rtState == "0") {
      location = "<%=contextPath%>/core/funcs/system/mobilesms/submit.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit();" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Small1"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;指定被提醒权限</span>
    &nbsp;以下指定的用户可以接收到手机短信提醒，例如来自工作流的手机短信提醒
    </td>
  </tr>
</table>
<br>
 <form enctype="multipart/form-data" method="post" name="form1" id="form1">
 <table class="TableBlock" width="100%" align="center">
 
  <input type="hidden" id="seqId" name="seqId" value=""/>
    <tr>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols="75" name="userDesc" id="userDesc" rows="10" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" class="BigButton" onClick="doSubmit();">&nbsp;&nbsp;
      </td>
    </tr>
  
  </table>
   </form>
</body>
</html>