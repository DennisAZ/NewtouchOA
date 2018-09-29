<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>查看组织信息</title>
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
function doInit() {
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/source/nation/act/YHSourceNationAct/showDetail.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  //alert(rsText);
  if (prc.seqId) {
    $("natNum").update(prc.natNum);
    $("natName").update(prc.natName);
    $("natStatus").update(prc.natStatus);
    $("natCustom").update(prc.natCustom);
    $("natBackground").update(prc.natBackground);
    $("natNote").update(prc.natNote);
    $("attachmentId").value = prc.attachmentId;
    $("attachmentName").value = prc.attachmentName;
    if ($("attachmentId").value != "") {
      $("fjian").style.display = "none";
    }
    attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,true);
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 国家信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="add.php"  method="post" name="form1">
<table class="TableBlock" width="80%" align="center">
    <tr>
     <td nowrap  class="TableControl" colspan="6" align="left">
      基本信息
     </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">国家编号：</td>
      <td nowrap class="TableData" id="natNum"></td>       
      <td nowrap class="TableContent" width="90">国家名称：</td>
      <td nowrap class="TableData" id="natName"></td>       
    </tr>
    <tr>
      <td nowrap class="TableContent">国家情况：</td>
      <td class="TableData" colspan="3" id="natStatus"></td>
    </tr>
    <tr>
      <td nowrap class="TableContent">风土人情：</td>
      <td class="TableData" colspan="3" id="natCustom"></td>
    </tr>
    <tr>
      <td nowrap class="TableContent">政治背景：</td>
      <td class="TableData" colspan="3" id="natBackground"></td>
    </tr>   
    <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3" id="natNote"></td>
    </tr>
    <tr>
     <td nowrap  class="TableControl" colspan="6" align="left">
      其他信息
     </td>
    </tr>
    <tr id="attr_tr">
      <td class="TableData" colspan="6">
      <input type="hidden" id="attachmentName" name="attachmentName" value="">
      <input type="hidden" id="attachmentId" name="attachmentId" value="">
      <span id="showAtt"></span>
       <span id="fjian">无附件</span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="6" nowrap>
        <input type="button" value="关闭" class="BigButton" onclick="parent.window.close();">
      </td>
    </tr>
  </table>
  <input type="hidden" name="FORMAT" value="0">
</form>
</body>
</html>