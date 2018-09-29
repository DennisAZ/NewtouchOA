<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.asset.data.YHCpcptl"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
List<YHCpcptl> list = (List<YHCpcptl>) request.getAttribute("list");
List<YHCpcptl> list2 = (List<YHCpcptl>) request.getAttribute("list2");
YHCpcptl cp = new YHCpcptl();
%>
<html>
<head>
<title>固定资产查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<style media = print>  
.Noprint  {display:none;}  
.PageNext  {page-break-after: always;}  
 </style>
 
<script type="text/javascript">
function doInit() {
  if(document.getElementById("keeper").value.trim() != ""){
    bindDesc([{cntrlId:"keeper", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("cpreUser").value.trim() != ""){
    bindDesc([{cntrlId:"cpreUser", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("keeper2").value.trim() != ""){
    bindDesc([{cntrlId:"keeper2", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("cpreUser2").value.trim() != ""){
    bindDesc([{cntrlId:"cpreUser2", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if(document.getElementById("cptlSpec2").value.trim() != ""){
    bindDesc([{cntrlId:"cptlSpec2", dsDef:"CP_ASSET_TYPE,SEQ_ID,TYPE_NAME"}]);
  }
  if(document.getElementById("cptlSpec").value.trim() != ""){
    bindDesc([{cntrlId:"cptlSpec", dsDef:"CP_ASSET_TYPE,SEQ_ID,TYPE_NAME"}]);
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" align="center"><span class="big3">
  固定资产使用明细表(<%=request.getAttribute("infoName")%>) 
  </span><br>
    </td>
  </tr>
</table>
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3">
  固定资产领用单明细(<%=request.getAttribute("infoName")%>) 
  </span><br>
    </td>
  </tr>
</table>
 <%if(list.size() > 0){%>
<table class="TableList" width="95%">
  <tr class="TableHeader">
      <td nowrap align="center">编号</td>
      <td nowrap align="center">固定资产名称</td>
    <td nowrap align="center">规格型号</td>
      <td nowrap align="center">数量</td>
      <td nowrap align="center">使用地点</td>
      <td nowrap align="center">使用人签字</td>
      <td nowrap align="center">专管员签字</td>
      <td nowrap align="center">备注</td>
      <td nowrap align="center">工作流详情</td>
    </tr>
 <%
     cp = list.get(list.size()-1);
 %>   
  <tr class="TableLine1">
      <td nowrap align="center"><%=cp.getCptlNo()%></td>
      <td nowrap align="center"><%=cp.getCptlName() %></td>
    <td nowrap align="center" id="cptlSpecDesc">
     <input type="hidden" name="cptlSpec" id="cptlSpec" value="<%=cp.getCptlSpec() %>">
    </td>
      <td nowrap align="center"><%=cp.getCptlQty() %></td>
     <td nowrap align="center">
      <%if(!YHUtility.isNullorEmpty(cp.getUseDept())){%>
     <%=cp.getUseDept() %>
     <%} %>
     </td>
     
      <td nowrap align="center" id="cpreUserDesc">
      <input type="hidden" name="cpreUser" id="cpreUser" value="<%=cp.getCpreUser()%>">
      </td>
      
      <td nowrap align="center" id="keeperDesc">
      <input type="hidden" name="keeper" id="keeper" value="<%=cp.getKeeper()%>">
      </td>
      
      <td nowrap align="center"><%if(cp.getCpreMemo() != null) {%><%=cp.getCpreMemo()%><%}%></td>
      <td nowrap align="center"><%if(cp.getRunId() >= 1){%><a href="javascript:formViewByName('<%=cp.getRunId()%>','固定资产申请领用')">点击查看</a><%} %></td>
    </tr>
</table>
 <%} else {%>
 <table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>未找到相应的记录!</div>
    </td>
  </tr>
</table>
 <input type="hidden" name="cptlSpec" id="cptlSpec" value="">
 <input type="hidden" name="deptId" id="deptId" value="">
 <input type="hidden" name="cpreUser" id="cpreUser" value="">
 <input type="hidden" name="keeper" id="keeper" value="">
 <%} %>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3">
  固定资产返库单明细(<%=request.getAttribute("infoName")%>) 
  </span><br>
    </td>
  </tr>
</table>
  <%if(list2.size() > 0){%>
<table class="TableList" width="95%">
  <tr class="TableHeader">
      <td nowrap align="center">编号</td>
      <td nowrap align="center">固定资产名称</td>
    <td nowrap align="center">规格型号</td>
      <td nowrap align="center">数量</td>
      <td nowrap align="center">保管地点</td>
      <td nowrap align="center">领用会签</td>
      <td nowrap align="center">返库会签</td>
      <td nowrap align="center">备注</td>
      <td nowrap align="center">工作流详情</td>
    </tr>
    <!-- for (int i = 0; i < list2.size(); i ++) { -->
   <%
     cp = list2.get(list2.size()-1);
 %>     
 <tr class="TableLine1">
      <td nowrap align="center"><%=cp.getCptlNo()%></td>
      <td nowrap align="center"><%=cp.getCptlName() %></td>
    <td nowrap align="center" id="cptlSpec2Desc">
    <input type="hidden" name="cptlSpec2" id="cptlSpec2" value="<%=cp.getCptlSpec() %>">
    </td>
      <td nowrap align="center"><%=cp.getCptlQty() %></td>
     <td nowrap align="center">
     <%if(!YHUtility.isNullorEmpty(cp.getUseDept())){%>
      <%=cp.getUseDept() %>
     <%} %>
     </td>
      <td nowrap align="center" id="cpreUser2Desc">
      <input type="hidden" name="cpreUser2" id="cpreUser2" value="<%=cp.getCpreUser()%>">
      </td>
      <td nowrap align="center" id="keeper2Desc">
      <input type="hidden" name="keeper2" id="keeper2" value="<%=cp.getKeeper()%>">
</td>
      <td nowrap align="center"><%if(cp.getCpreMemo() != null) {%><%=cp.getCpreMemo() %><%}%></td>
      <td nowrap align="center"><%if(cp.getRunId() >= 1){%><a href="javascript:formViewByName('<%=cp.getRunId()%>','固定资产返库')">点击查看 </a><%} %></td>
    </tr>
</table>
<%} else {%>
 <table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>未找到相应的记录!</div>
    </td>
  </tr>
</table>
 <input type="hidden" name="cptlSpec2" id="cptlSpec2" value="">
 <input type="hidden" name="cpreUser2" id="cpreUser2" value="">
 <input type="hidden" name="keeper2" id="keeper2" value="">
 <%} %>
 
<center class = " Noprint ">
<p><OBJECT id = WebBrowser classid = CLSID:8856F961 - 340A - 11D0 - A96B - 00C04FD705A2 height = 0  width = 0> </OBJECT>  
<input type="button" value="打印" class="BigButton" onclick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;&nbsp;&nbsp;
<input type="button" value="关闭" class="BigButton" onClick="javascript:window.close();" title="关闭窗口">
<div id=MyWebBrowser><OBJECT id=wb classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 width=0 VIEWASTEXT></OBJECT></div>
</center>

</body>
</html>