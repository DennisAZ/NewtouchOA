<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.asset.data.YHCpCptlInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHCpCptlInfo cpcp = (YHCpCptlInfo) request.getAttribute("cpcp");
%>
<html>
<head>
<title>固定资产详情</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
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
<%if(cpcp != null) {%>
<script type="text/javascript">
function doInit() {
  if(document.getElementById("useUser").value.trim() != ""){
    bindDesc([{cntrlId:"useUser", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("keeper").value.trim() != ""){
    bindDesc([{cntrlId:"keeper", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("deptId").value.trim() != ""){
    bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }
  if(document.getElementById("cptlSpec").value.trim() != ""){
    bindDesc([{cntrlId:"cptlSpec", dsDef:"CP_ASSET_TYPE,SEQ_ID,TYPE_NAME"}]);
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3">固定资产详情 - <%=cpcp.getCptlName()%></span><br>
    </td>
    </tr>
</table>
<hr width="100%" height="1" align="left" color="#FFFFFF">
<table class="TableBlock" width="100%">
  <tr>
      <td nowrap align="center" class="TableContent" width="80">资产编号</td>
      <td align="left" class="TableData"><%=cpcp.getCptlNo()%></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">资产名称</td>
      <td align="left" class="TableData"><%=cpcp.getCptlName()%></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">单据日期</td>
      <td align="left" class="TableData"><%if(cpcp.getListDate() != null){%><%=cpcp.getListDate().toString().substring(0,10)%><%}%></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">未处置</td>
      <td align="left" class="TableData">
      <%if(!YHUtility.isNullorEmpty(cpcp.getNoDeal())){%>
      <%if(cpcp.getNoDeal().equals("1")){%>是<%} %>
      <%if(cpcp.getNoDeal().equals("2")){%>否<%} %>
      <%} %>
      </td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">已入帐</td>
      <td align="left" class="TableData">
      <%if(!YHUtility.isNullorEmpty(cpcp.getInFinance())){%>
      <%if(cpcp.getInFinance().equals("1")){%>是<%} %>
      <%if(cpcp.getInFinance().equals("2")){%>否<%} %>
      <%} %>
      </td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">使用人</td>
      <td align="left" class="TableData" id="useUserDesc">
      <input type="hidden" name="useUser" id="useUser" value="<%=cpcp.getUseUser()%>">
      </td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">使用状况</td>
      <td align="left" class="TableData">
      <%if(!YHUtility.isNullorEmpty(cpcp.getUseState())){%>
      <%if(cpcp.getUseState().equals("1")){%>未使用<%}%>
      <%if(cpcp.getUseState().equals("2")){%>不需用<%}%>
      <%if(cpcp.getUseState().equals("3")){%>在用<%}%>
      <%} %>
      </td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">使用方向</td>
      <td align="left" class="TableData"><%if(cpcp.getUseFor()!=null){%><%=cpcp.getUseFor()%><%} %></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">保修截至日</td>
      <td align="left" class="TableData"> <%if(cpcp.getAfterIndate()!=null){%><%=cpcp.getAfterIndate().toString().substring(0,10)%><%} %></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">取得日期</td>
      <td align="left" class="TableData"><%if(cpcp.getGetDate()!=null){%><%=cpcp.getGetDate().toString().substring(0,10)%><%}%></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">分类代码</td>
      <td align="left" class="TableData"><%if(cpcp.getTypeId()!=null){%><%=cpcp.getTypeId()%><%} %></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">资产值</td>
      <td nowrap align="left" class="TableData"><%=YHUtility.getFormatedStr(cpcp.getCptlVal(),2)  %></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">数量</td>
      <td nowrap align="left" class="TableData"><%=cpcp.getCptlQty()%></td>
  </tr>  
  <tr>
      <td nowrap align="center" class="TableContent" width="80">资产类别</td>
      <td align="left" class="TableData" id="cptlSpecDesc">
      <input type="hidden" name="cptlSpec" id="cptlSpec" value="<%=cpcp.getCptlSpec()%>">
      </td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">使用部门 </td>
      <td nowrap align="left" class="TableData" id="deptIdDesc">
      <input type="hidden" name="deptId" id="deptId" value="<%=cpcp.getUseDept()%>">
</td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">录入日期</td>
      <td nowrap align="left" class="TableData"><%if(cpcp.getCreateDate()!=null) {%><%=cpcp.getCreateDate().toString().substring(0,10)%><%}%></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">专管员</td>
      <td nowrap align="left" class="TableData" id="keeperDesc">
      <input type="hidden" name="keeper" id="keeper" value="<%=cpcp.getKeeper()%>"></td>
  </tr>  
  <tr>
      <td nowrap align="center" class="TableContent" width="80">保管地点</td>
      <td nowrap align="left" class="TableData"><%if(cpcp.getSafekeeping()!=null){%><%=cpcp.getSafekeeping()%><%} %></td>
  </tr>
  <tr>
      <td nowrap align="center" class="TableContent" width="80">备注</td>
      <td align="left" class="TableData"><%if(cpcp.getRemark()!=null){%><%=cpcp.getRemark()%><%} %></td>
  </tr>
  <tr class="TableControl">
      <td colspan="9" align="center">
        <input type="button" value="打印" class="BigButton" onClick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
  </tr>
</table>
 <%} else {%>
 <table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>无符合条件的信息</div>
    </td>
  </tr>
</table>
<div align="center"> <input type="button" value="关闭" class="BigButton" onClick="window.close();">&nbsp;</div>
<input type="hidden"  name="deptId" id="deptId" value="">
<input type="hidden"  name="useUser" id="useUser" value="">
<input type="hidden"  name="cptlSpec" id="cptlSpec" value="">
 <%} %>
</body>
</html>
