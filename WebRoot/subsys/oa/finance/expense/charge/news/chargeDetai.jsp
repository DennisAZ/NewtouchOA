<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.finance.data.YHChargeExpense"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHChargeExpense charge = (YHChargeExpense)request.getAttribute("expense");
%>
<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<title>新增费用报销信息&nbsp;&nbsp;</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script>
function doInit() {
  if(document.getElementById("chargeUser").value.trim() != ""){
    bindDesc([{cntrlId:"chargeUser", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("budgetId").value.trim() != ""){
    bindDesc([{cntrlId:"budgetId", dsDef:"budget_apply,SEQ_ID,BUDGET_ITEM"}]);
  }
  if(document.getElementById("deptAuditUser").value.trim() != ""){
    bindDesc([{cntrlId:"deptAuditUser", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("financeAuditUser").value.trim() != ""){
    bindDesc([{cntrlId:"financeAuditUser", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("deptId").value.trim() != ""){
    bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }
}
</SCRIPT>
</head>
<body class="bodycolor" style="margin:0;padding:0;" onLoad="doInit()">
<%if(charge != null){ %>
<div id="edit_body" style="overflow:auto;" SCROLL=auto>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3">费用报销信息</span>&nbsp;<br>
    </td>
  </tr>
</table>
<table width="90%" align="center" class="TableBlock">
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 新增费用报销信息 </td>
    </tr>
   <tr>
      <td nowrap class="TableContent">&nbsp;部门名称：</td>
      <td nowrap class="TableData" id="deptIdDesc">
      <input type="hidden"  name="deptId" id="deptId" value="<%=charge.getDeptId()%>">
      </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">&nbsp;报销申请人：</td>
      <td nowrap class="TableData" id="chargeUserDesc">
        <input type="hidden" name="chargeUser" id="chargeUser" value="<%=charge.getChargeUser()%>">
    </td>
   </tr> 
    <tr>
      <td nowrap class="TableContent">&nbsp;预算项目：</td>
      <td nowrap class="TableData" id = "budgetIdDesc">
      <input type = "hidden" id = "budgetId" name="budgetId" value="<%=charge.getBudgetId()%>"></input>
      </td>
    </tr>
   <tr>
  <tr>
    <td nowrap colspan="2">
    <% if(charge.getChargeItem()!=null&&!charge.getChargeItem().equals("")){ %>
  <table id="MYTABLE" width="100%"  class="small">
        <tr  class="TableHeader"  align="center">
           <td>项目</td>
           <td>摘要</td>
           <td>支票号</td>
           <td>金额</td>
        </tr>
      <%
           String detailContent = charge.getChargeItem();
           String dcs[] = detailContent.split("\n");
           for(int i = 0; i<dcs.length;i++){
             String dc= dcs[i];
             String[] temp = dc.split("`");
             String item = "";
             String person = "";
             String days = "";
             String price ="";
             for (int j = 0; j<temp.length;j++) {
               if(j==0){
                  item = temp[0];
               }
                if(j==1){
                  person = temp[1];
                }
                if(j==2){
                  days = temp[2];
                }
                if(j==3){
                  price = temp[3];
                }
              }
             %> 
           <tr class="TableControl" align="center">
            <td><%=item %></td>
            <td><%=person%></td>
            <td><%=days%></td>
            <td><%=price%></td>
         </tr>
         <%}
          %>
       </table>
       <%} %>
       </td>
   </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;报销金额：</td>
      <td class="TableData">
      &nbsp;<%=YHUtility.getFormatedStr(charge.getChargeMoney(),2)%>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;备注：</td>
      <td nowrap class="TableData">
<textarea cols=50 name="chargeMemo" id="chargeMemo" rows="3" class="BigInput" wrap="yes" readonly><%if(charge.getChargeMemo() != null){%><%=charge.getChargeMemo()%><%}%></textarea>
      </td>
    </tr>
    
      <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 部门审批 </td>
    </tr>
 
   <tr>
    <td nowrap class="TableContent">&nbsp;部门审批人：</td>
      <td class="TableData" id="deptAuditUserDesc">
        <input type="hidden" name="deptAuditUser" id="deptAuditUser" value="<%=charge.getDeptAuditUser()%>">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;部门审批时间：</td>
      <td nowrap class="TableData"><%=charge.getDeptAuditDate().toString().substring(0,10)%>
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;部门审批内容：</td>
      <td class="TableData">
       <textarea cols=50 name="deptAuditContent" id="deptAuditContent" rows="3" class="BigInput" wrap="yes" readonly><%if(charge.getDeptAuditContent() != null){%><%=charge.getDeptAuditContent()%><%}%></textarea>
      </td>
    </tr>
    
    <tr>
      <td colspan=2 class="TableHeader"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"> 财务审批 </td>
    </tr>
 
   <tr>
    <td nowrap class="TableContent">&nbsp;财务审批人：</td>
      <td class="TableData" id="financeAuditUserDesc">
        <input type="hidden" name="financeAuditUser" id="financeAuditUser" value="<%=charge.getFinanceAuditUser() %>">
    </td>
   </tr>
  <tr>
      <td nowrap class="TableContent">&nbsp;财务审批时间：</td>
      <td class="TableData"><%=charge.getFinanceAuditDate().toString().substring(0,10) %>
      </td>
  </tr>
    <tr>
      <td nowrap class="TableContent">&nbsp;财务审批内容：</td>
      <td class="TableData">
       <textarea cols=50 name="financeAuditContent" id="financeAuditContent" rows="3" class="BigInput" wrap="yes" readonly><%if(charge.getFinanceAuditContent() != null){%><%=charge.getFinanceAuditContent()%><%}%></textarea>
      </td>
    </tr>
    
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
     <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
  </td>
  </tr>
   </table>
</div>
<%}else { %>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>无符合条件的信息</div>
    </td>
  </tr>
</table>
<div align="center"> <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;</div>
<input type="hidden"  name="deptId" id="deptId" value="">
<input type="hidden"  name="chargeUser" id="chargeUser" value="">
<input type="hidden"  name="budgetId" id="budgetId" value="">
<input type="hidden"  name="deptAuditUser" id="deptAuditUser" value="">
<input type="hidden"  name="financeAuditUser" id="financeAuditUser" value="">
<%} %>
</body>
</html>
