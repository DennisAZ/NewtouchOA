<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>人士信息</title>
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
<script type="text/javascript">
var seqId = "<%=seqId%>";
function doInit() {
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/source/person/act/YHSourcePersonAct/showDetail.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  //alert(rsText);
  if (prc.seqId) {
    $("perNum").update(prc.perNum);
    $("perPosition").value = prc.perPosition;
    $("perName").update(prc.perName);
    if (prc.perSex == "1") {
      $("perSex").update("男");
    }
    if (prc.perSex == "2") {
      $("perSex").update("女");
    }
    if (prc.perBirthday != "" && prc.perBirthday != null) {
      $("perBirthday").update(prc.perBirthday.substr(0,10));
    }
    if ($("perPosition").value != "") {
      bindDesc([{cntrlId:"perPosition",dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
    }
    $("perNation").update(prc.perNation);
    $("perVocation").update(prc.perVocation);
    $("perResume").update(prc.perResume);
    $("perExperience").update(prc.perExperience);
    $("perNote").update(prc.perNote);
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
<body class="bodycolor" topmargin="5" onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 人士信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
 <br>
<form id="form1" name="form1">
<input value="" id="perPosition" name="perPosition" type="hidden">
<table class="TableBlock" width="80%" align="center">
    <tr>
     <td nowrap  class="TableControl" colspan="6" align="left">
      基本信息
     </td>
    </tr>
    <tr>
      <td nowrap class="TableContent"  width="90">人员编号：</td>
      <td nowrap class="TableData" id="perNum"></td>       
      <td nowrap class="TableContent"  width="90">职务：</td>
      <td nowrap class="TableData" id="perPositionDesc"></td>   
    </tr>
    <tr>
      <td nowrap class="TableContent">姓名：</td>
      <td nowrap class="TableData" id="perName"></td>
      <td nowrap class="TableContent" >性别：</td>
        <td nowrap class="TableData" id="perSex"></td> 
    </tr>
    <tr>
      <td nowrap class="TableContent">出生日期：</td>
      <td nowrap class="TableData" id="perBirthday"></td>
      <td nowrap class="TableContent">国籍：</td>
      <td nowrap class="TableData" id="perNation"></td>
    </tr>
    <tr>
     <td nowrap class="TableContent" width="90">职业：</td>
      <td class="TableData" colspan="3" id="perVocation"></td>      
   </tr>
   <tr>
      <td nowrap class="TableContent">简介：</td>
      <td class="TableData" colspan="3" id="perResume"></td>
    </tr>
    <tr>
      <td nowrap class="TableContent">来华经历：</td>
      <td class="TableData" colspan="3" id="perExperience"></td>
    </tr>   
    <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3" id="perNote"></td>
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
               <span id="fjian">无附件</span>
      <span id="showAtt"></span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="6" nowrap>
        <input type="button" value="关闭" class="BigButton" onclick="parent.window.close();">
      </td>
    </tr>
  </table>
 </form>
</body>
</html>