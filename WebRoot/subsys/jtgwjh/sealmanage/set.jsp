<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设置印章权限</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";

function doInit(){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/getSealPriv.act";
  var rtJson = getJsonRs(url, "seqId="+seqId);
  if (rtJson.rtState == "0") {
    bindJson2Cntrl(rtJson.rtData);
    $('user').value = rtJson.rtData[0].userStr;
    if($('user').value!=""){
      bindDesc([{cntrlId:"user", dsDef:"PERSON,SEQ_ID,USER_NAME"}
    	        ]);
    }
    var prc = getOutDeptNameByDeptIds(rtJson.rtData[0].deptStr);
    $("deptStr").value = prc.esbDept;
    $("deptName").value = prc.esbDeptName;

  }else {
    alert(rtJson.rtMsrg); 
  }
}
function getOutDeptNameByDeptIds(deptIds){
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/getOutDeptNameByDeptIds.act";
  var rtJson = getJsonRs(url, {deptIds:deptIds});
  if (rtJson.rtState == "0") {
    var prc = rtJson.rtData;
    return prc;
  }else {
    alert(rtJson.rtMsrg); 
  }

  
  
}
function commit(){
  var userStr = $('user').value;
  var userName = $('userDesc').value;
 //var userStr = $('deptStr').value;
 //var userName = $('deptName');
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/sealmanage/act/YHJhSealAct/updateSealPriv.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId +"&userStr=" + userStr +"&userName="+encodeURIComponent(userName) + "&deptName=" + $("deptName").value + "&deptStr=" + $("deptStr").value);
  if (rtJson.rtState == "0") {
    location = "<%=contextPath%>/subsys/jtgwjh/sealmanage/manage.jsp";
  }else {
    alert(rtJson.rtMsrg); 
  }
}


</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;设置印章权限</span>
    </td>
  </tr>
</table>

<table class="TableBlock"  width="600" align="center" >
  <form method="post" name="form1" id="form1">
   <tr>
      <td nowrap class="TableData">已绑定证书列表：</td>
      <td class="TableData">
        <br>(若印章制作时选择与数字证书绑定，则必须从绑定证书列表中选择授权范围)
      </td>
   </tr>
   <tr style="display:none">
      <td nowrap class="TableData">印章授权范围(人员)：</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=30 name="userDesc" id="userDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">选择</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData">印章授权范围(部门)：</td>
      <td class="TableData">
        <input type="hidden" name="deptStr" id="deptStr" value="">
        <textarea cols=30 name="deptName" id="deptName" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
       <a href="javascript:void(0);" class="orgAdd" onClick="selectOutDept2(['deptStr', 'deptName'])">添加</a>
        <a href="javascript:void(0);" class="orgClear" onClick="$('deptStr').value='';$('deptName').value='';">清空</a>
       </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
    	  <input type="hidden" name="ID" value="<?=$ID?>">
        <input type="button" value="确定" class="BigButton" name="button" onclick="commit();">&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="location='manage.jsp'">
    </td>
   </tr>
  </form>
</table>

</body>
</html>