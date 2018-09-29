<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.profsys.data.YHProject"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
%>
<html>
<head>
<title>项目基本信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
<script type="text/javascript">
var seqId = "<%=seqId%>";
var statrTime = "<%=sf.format(new Date())%>";
var endTime = "<%=sf.format(new Date())%>";
function doInit() {
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHProjectAct/showDetail.act?seqId=" + seqId;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  if (prc.seqId) {
    $("projNum").update(prc.projNum);
    $("projStatus2").value = prc.projStatus;
    $("budgetId").value = prc.budgetId;
    $("projLeader").value = prc.projLeader;
    $("projStartTime2").update(prc.projStartTime.substr(0,10));
    $("projEndTime2").update(prc.projEndTime.substr(0,10));
    $("purposeCountry").update(prc.purposeCountry);
    $("countryTotal").update(prc.countryTotal);
    $("pTotal").update( prc.pTotal);
    $("pYx").update(prc.pYx);
    $("pCouncil").update(prc.pCouncil);
    $("projVisitType").value = prc.projVisitType;
    $("projActiveType").value = prc.projActiveType;
    $("pGuest").update(prc.pGuest);
    $("projNote").update(prc.projNote);
    
    $("projStartTime").value= prc.projStartTime.substr(0,10);
    $("projEndTime").value= prc.projEndTime.substr(0,10);
    $("attachmentName").value = prc.attachmentName;
    $("attachmentId").value = prc.attachmentId;
    attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,true);
  }
  doVal();
}
//各种不一样条件
function doVal() {
  //自动 判断状态
  if ($("projStatus2").value == "1") {
    $("projStatus").update("<font color='red'><b>已结束</b></font>");
  }
  if ($("projStatus2").value == "0" && ($("projStartTime").value <= statrTime && $("projEndTime").value > endTime)) {
    $("projStatus").update("<font color='blue'><b>进行中</b></font>");
  }
  if ($("projStatus2").value == "0" && ($("projStartTime").value > statrTime && $("projEndTime").value > endTime)) {
    $("projStatus").update("<font color='red'><b>准备中</b></font");
  }
  if ($("projStatus2").value == "0" && endTime >= $("projEndTime").value) {
    $("projStatus").update("<font color='red'><b>已结束</b></font>");
  }
  if ($("budgetId").value != "") {
    bindDesc([{cntrlId:"budgetId",dsDef:"BUDGET_APPLY,SEQ_ID,BUDGET_ITEM"}]);
  }
  if ($("projLeader").value != "") {
    bindDesc([{cntrlId:"projLeader",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if ($("projVisitType").value != "") {
    bindDesc([{cntrlId:"projVisitType",dsDef:"CODE_ITEM,SEQ_ID,CLASS_DESC"}]);
  }
  if ($("projActiveType").value != "") {
    bindDesc([{cntrlId:"projActiveType",dsDef:"CODE_ITEM,SEQ_ID,CLASS_DESC"}]);
  }
}
</script>
</head>
<body onLoad="doInit()" class="bodycolor">
<input type="hidden" id="projStartTime" name="projStartTime">
<input type="hidden" id="projEndTime" name="projEndTime">
<input type="hidden" name="projStatus2" id="projStatus2">
 <table class="TableBlock" border="0" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent" width="90">项目编号：</td>
      <td nowrap class="TableData" id="projNum">
       </td>
      <td nowrap class="TableContent" width="90">项目状态：</td>
      <td nowrap class="TableData" id="projStatus">
      </td>               
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">团组名称：</td>
      <td nowrap class="TableData" colspan="3" id="budgetIdDesc">
      <input type="hidden" id="budgetId" name="budgetId" value="" >
    </td>
    </tr> 
    <tr>
     <td nowrap class="TableContent" width="90">负责人：</td>
      <td nowrap class="TableData" colspan="3" id="projLeaderDesc">
      <input type="hidden" name="projLeader" id="projLeader"  value="">   
    </td> 
    </tr> 
    <tr>
      <td nowrap class="TableContent" width="90">出访类别：</td>
      <td nowrap class="TableData" id="projVisitTypeDesc">
      <input type="hidden" id="projVisitType" name="projVisitType" value="" >
      </td>
      <td nowrap class="TableContent" width="90">项目类别：</td>
      <td nowrap class="TableData"  id="projActiveTypeDesc">
      <input type="hidden" id="projActiveType" name="projActiveType" value="" >
      </td>     
      </tr>
      <tr>
      <td nowrap class="TableContent" width="90">起始时间：</td>
      <td nowrap class="TableData" id="projStartTime2"> 
      </td>
      <td nowrap class="TableContent" width="90">结束时间：</td>
      <td nowrap class="TableData" id="projEndTime2">  
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent">出访国家：</td>
      <td nowrap class="TableData"  colspan="3" id="purposeCountry">
      </td>    
    </tr>
    <tr>
      <td nowrap class="TableContent">出访国家总数：</td>
      <td nowrap class="TableData"  colspan="3"  id="countryTotal">
      </td>    
    </tr>
    <tr>
      <td nowrap class="TableContent">参与总人数：</td>
      <td nowrap class="TableData" id="pTotal">
      </td>    
      <td nowrap class="TableContent">参与外办人员：</td>
      <td nowrap class="TableData" id="pYx">
     </td>    
     </tr>
    <tr>
      <td nowrap class="TableContent">参与理事人数：</td>
      <td nowrap class="TableData" id="pCouncil">
      </td>    
      <td nowrap class="TableContent">参与外宾人数：</td>
      <td nowrap class="TableData" id="pGuest">
      </td>    
    </tr>
    <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3" id="projNote">
</td>
    </tr>
      <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type="hidden" id="attachmentName" name="attachmentName"></input>
       <input type="hidden" id="attachmentId" name="attachmentId"></input>
        <span id="showAtt"></span>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;
      </td>
  </tr>
 </table>
</body>
</html>