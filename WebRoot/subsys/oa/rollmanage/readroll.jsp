<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>查看案卷</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

function doInit(){

  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollDetail.act?seqId=${param.seqId}";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    var data = json.rtData;
    bindJson2Cntrl(json.rtData);
    $('beginDate').innerHTML = data.beginDate.substr(0, 10);
    $('endDate').innerHTML = data.endDate.substr(0, 10);
    $('deptId').innerHTML = getDeptNameFunc(data.deptId);
    if (data.priority != 0){
      $('priority').innerHTML =data.priority;
    }else{
    	$('priority').innerHTML = "";
    }
    $('roomId').innerHTML = getRmsRollRoomNameFunc(data.roomId);
    $('certificateKind').innerHTML = getCodeNameKindFunc(data.certificateKind);
    $('secret').innerHTML = getCodeNameSecretFunc(data.secret);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function getCodeNameSecretFunc(classCode){
  var classNo = "RMS_SECRET";
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getCodeName.act";
  var rtJsons = getJsonRs(urls , "classCode=" + classCode + "&classNo=" + classNo);
  if(rtJsons.rtState == '0'){
    return rtJsons.rtData;
  }else{
    alert(rtJson.rtMsrg);
  }
}


function getCodeNameKindFunc(classCode){
  var classNo = "RMS_CERTIFICATE_KIND";
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getCodeName.act";
  var rtJsons = getJsonRs(urls , "classCode=" + classCode + "&classNo=" + classNo);
  if(rtJsons.rtState == '0'){
    return rtJsons.rtData;
  }else{
    alert(rtJson.rtMsrg);
  }
}

function getRmsRollRoomNameFunc(roomId){
  var urls = contextPath + "/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollRoomName.act";
  var rtJsons = getJsonRs(urls , "seqId=" + roomId);
  if(rtJsons.rtState == '0'){
    return rtJsons.rtData ;
  }else{
    alert(rtJson.rtMsrg);
  }
}

function getDeptNameFunc(deptId){
    var urls = contextPath + "/yh/core/funcs/person/act/YHPersonAct/getDeptName.act";
    var rtJsons = getJsonRs(urls , "deptId=" +  deptId);
    if(rtJsons.rtState == '0'){
      return rtJsons.rtData;
    }else{
      alert(rtJson.rtMsrg); 
    }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 查看案卷</span>
    </td>
  </tr>
</table>


<table class="TableList"  width="85%" align="center">
  <form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
  <TR>
      <TD class="TableData">案 卷 号：</TD>
      <TD class="TableData"><div id="rollCode"></div></TD>
      <TD class="TableData">案卷名称：</TD>
      <TD class="TableData"><div id="rollName"></div></TD>
  </TR>
  <TR>
      <TD class="TableData">所属卷库：</TD>
      <TD class="TableData"><div id="roomId"></div></TD>
      <TD class="TableData">归卷年代：</TD>
      <TD class="TableData"><div id="years"></div></TD>
  </TR>
  <TR>
      <TD class="TableData">起始日期：</TD>
      <TD class="TableData"><div id="beginDate"></div></TD>
      <TD class="TableData">终止日期：</TD>
      <TD class="TableData"><div id="endDate"></div></TD>
  </TR>
   <TR>
      <TD class="TableData">所属部门：</TD>
      <TD class="TableData" ><div id="deptId"></div>
      </TD>
      <TD class="TableData">编制机构：</TD>
      <TD class="TableData"><div id="editDept"></div></TD>
  </TR>
   <TR>
      <TD class="TableData">保管期限：</TD>
      <TD class="TableData" ><div id="deadline"></div></TD>
      <TD class="TableData">案卷密级：</TD>
      <TD class="TableData"><div id="secret"></div></TD>
  </TR>
   <TR>
      <TD class="TableData">全 宗 号：</TD>
      <TD class="TableData"><div id="categoryNo"></div></TD>
      <TD class="TableData">目 录 号：</TD>
      <TD class="TableData"><div id="catalogNo"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">档案馆号：</TD>
      <TD class="TableData"><div id="archiveNo"></div></TD>
      <TD class="TableData">保险箱号：</TD>
      <TD class="TableData"><div id="boxNo"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">缩 微 号：</TD>
      <TD class="TableData"><div id="microNo"></div></TD>
      <TD class="TableData">凭证类别：</TD>
      <TD class="TableData"><div id="certificateKind"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">凭证编号(起)：</TD>
      <TD class="TableData"><div id="certificateStart"></div></TD>
      <TD class="TableData">凭证编号(止)：</TD>
      <TD class="TableData"><div id="certificateEnd"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">页    数：</TD>
      <TD class="TableData"><div id="rollPage"></div></TD>
      <TD class="TableData">借阅审批：</TD>
      <TD class="TableData"><div id="beginDate"></div></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">备注：</TD>
      <TD class="TableData" ><div id="remark"></div></TD>
        <td nowrap class="TableData">档案分类优先级:
      </td>
      <td class="TableData">
         <div id="priority"></div>
      </td>
   </TR>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>