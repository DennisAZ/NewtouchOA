<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ page import="yh.core.global.YHConst" %>
 <%@ include file="/core/inc/header.jsp" %>
 <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
 <% 
 String flowId = request.getParameter("flowId");
 String runId = request.getParameter("runId");
 YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
 %>
<html>
<head>
<title>点评意见</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript">
var flowId = "<%=flowId%>";
var runId = "<%=runId%>";
var prcsId = 0 ;
var commentPriv = false;
var userId = "<%=loginUser.getSeqId()%>" ;
var userDept = "<%=loginUser.getDeptId()%>" ;
var userRole = "<%=loginUser.getUserPriv()%>" ;
function doInit() {
  var url = contextPath+'<%=moduleSrcPath %>/act/YHFlowManageAct/getCommentMsg.act';
  var json = getJsonRs(url , "flowId=" + flowId + "&runId=" + runId);
  if (json.rtState =='0') {
    $("prcsId").value = json.rtData.prcsId;
    commentPriv = json.rtData.hasPriv;
   
    if (commentPriv) {
      $('hasPriv').show();
      if (json.rtData.smsRemind) {
        $('smsRemind').checked = true;
      }
    } else {
      $('noCommentPriv').show();
    }
  }  
}
function saveComment() {
  if (!$('comment').value) {
    alert('请填写点评意见！');
    $('comment').focus();
    return ;
  }
  var url = contextPath+'<%=moduleSrcPath %>/act/YHFlowManageAct/saveComment.act';
  var json = getJsonRs(url , $('form1').serialize());
  if (json.rtState =='0') {
    alert(json.rtMsrg);
    close();
  }
}
</script>
</head>
<body onload="doInit()">
<div id="hasPriv" style="display:none">
<form  action="" method="post" id="form1" name="form1">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td height=24><img src="<%=imgPath %>/green_arrow.gif" align="absmiddle"><b> 点评意见</b></td>
  </tr>
</table>
 <table  width="100%" class="TableList">
    <tr class="TableContent">
      <td>
        <textarea cols="65" id="comment" name="comment" rows="4" class="BigInput" wrap="on"></textarea><br>
        <div>
       <span id="smsRemindSpan"><input type="checkbox" name="smsRemind" id="smsRemind"><img src="<%=imgPath %>/sms.gif"><label for="smsRemind"><u style="cursor:pointer">内部短信</u></label>
      </span>        
      </div>
      </td>
    </tr>


    <tr class="TableFooter">
      <td align="center">   
        <input type="button" class="SmallButtonW" value="提交点评" onclick="saveComment()">
        <input type="button" class="SmallButtonW" value="关  闭" onclick="window.close();">
        <input type="hidden"  name="runId" id="runId" value="<%=runId %>">
        <input type="hidden"  name="flowId" id="flowId" value="<%=flowId %>">
        <input type="hidden"  name="prcsId" id="prcsId" value="">
      </td>
    </tr>
</table>
</form>
</div>
<div id="noCommentPriv" align=center style='display:none'>
<table class="MessageBox" width="300" >
    <tbody>
        <tr>
            <td class="msg info">抱歉，您无点评权限，请与OA管理员联系！</td>
        </tr>
    </tbody>
</table>
</div>
</body>
</html>