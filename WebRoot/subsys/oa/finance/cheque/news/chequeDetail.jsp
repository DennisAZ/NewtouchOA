<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.finance.data.YHFinanceApplyRecord"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHFinanceApplyRecord applyRecord = (YHFinanceApplyRecord)request.getAttribute("record");
%>
<html>
<head>
<title>支票领用详情</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit() {
    if(document.getElementById("applyClaimer").value.trim() != ""){
      bindDesc([{cntrlId:"applyClaimer", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("deptDirector").value.trim() != ""){
      bindDesc([{cntrlId:"deptDirector", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("financeDirector").value.trim() != ""){
      bindDesc([{cntrlId:"financeDirector", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    if(document.getElementById("financeSignatory").value.trim() != ""){
      bindDesc([{cntrlId:"financeSignatory", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
}
</script>
</head>
<body class="bodycolor" style="margin:0;padding:0;" onLoad="doInit();">
<%if (applyRecord != null) {%>
<div id="edit_body" style="overflow:auto;">
<div>
<img src="<%=imgPath%>/edit.gif" align="absmiddle">&nbsp;<span class="big3">支票领用详情</span>
</div>
<form method="post" id="form1" name="form1">
<table width="90%" align="center" class="TableBlock">
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 支票领用信息 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;领用人姓名：</td>
      <td class="TableData" id="applyClaimerDesc">&nbsp;
      <input type="hidden" value="<%=applyRecord.getApplyClaimer() %>" id="applyClaimer" name="applyClaimer">
    </td>
   </tr>    
   <tr>
      <td nowrap class="TableContent">&nbsp;经费来源(项目)：</td>
      <td class="TableData">&nbsp;<%=applyRecord.getApplyItem() %>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;领用日期：</td>
      <td class="TableData">&nbsp;<%=applyRecord.getApplyDate().toString().substring(0,10) %> 
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;领用金额：</td>
      <td class="TableData">
      &nbsp;<%=YHUtility.getFormatedStr(applyRecord.getApplyMoney(),2)  %>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;用途(备注)：</td>
      <td class="TableData">&nbsp;<textarea cols=50 name="aMemo" id="aMemo" rows="3" class="SmallStatic"  wrap="yes" readonly><%=applyRecord.getApplyMemo() %></textarea>
      </td>
    </tr>
 
  <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;支票信息 </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;支票号：</td>
      <td class="TableData">
       &nbsp;<%=applyRecord.getChequeAccount() %>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;部门审批 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;部门主管：</td>
      <td class="TableData" id="deptDirectorDesc">&nbsp;
      <input type="hidden" value="<%=applyRecord.getDeptDirector() %>" id="deptDirector" name="deptDirector">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;部门主管审批时间：</td>
      <td class="TableData">&nbsp;<%=applyRecord.getDeptDirectorDate().toString().substring(0,10) %>
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;部门主管审批内容：</td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="deptDirectorContent" id="deptDirectorContent" rows="3" class="SmallStatic"  wrap="yes" readonly><%=applyRecord.getDeptDirectorContent() %></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;财务主管审批 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;财务主管审批：</td>
      <td class="TableData" id="financeDirectorDesc">&nbsp;
      <input type="hidden" value="<%=applyRecord.getFinanceDirector()%>" name="financeDirector" id="financeDirector">
    </td>
   </tr>
  <tr><td nowrap class="TableContent">&nbsp;财务主管审批时间：</td>
      <td class="TableData">
     &nbsp;<%=applyRecord.getFinanceDirectorDate().toString().substring(0,10) %>
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;财务主管审批内容：</td>
      <td class="TableData">
        &nbsp;<textarea cols=50 name="financeDirectorContent" id="financeDirectorContent" rows="3"  class="SmallStatic" wrap="yes" readonly><%=applyRecord.getFinanceDirectorContent() %></textarea>
      </td>
    </tr>
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle">&nbsp;财务签发 </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;财务签发人：</td>
      <td class="TableData" id="financeSignatoryDesc">
    &nbsp;
    <input type="hidden" value="<%=applyRecord.getFinanceSignatory() %>" id="financeSignatory" name="financeSignatory">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;签发时间：</td>
      <td class="TableData">
    &nbsp;<%=applyRecord.getSignDate().toString().substring(0,10)%>
    </td>
  </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close()">&nbsp;
  </td>
  </tr>
   </table>
     </form>
</div>
<%}else { %>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>无符合条件的支票领用</div>
    </td>
  </tr>
</table>
<div align="center"> <input type="button" value="关闭" class="BigButton" onclick="javascript:window.close()">&nbsp;</div>
 <input type="hidden" value="" id="applyClaimer" name="applyClaimer">
 <input type="hidden" value="" id="financeSignatory" name="financeSignatory">
 <input type="hidden" value="" id="financeDirector" name="financeDirector">
 <input type="hidden" value="" id="deptDirector" name="deptDirector">
<%} %>
</body>
</html>
