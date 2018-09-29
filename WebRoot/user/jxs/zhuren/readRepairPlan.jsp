<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
String isIframe = request.getParameter("isIframe");
if ("".equals(isIframe ) || isIframe == null) {
  isIframe = "0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看维修信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function writeRecord() {
  if (confirm("确认根据标准检测库生成检测记录？")) {
    openDialog("../xiuliyuan/jianche.jsp",  600, 400);
  }
}
</script>
</head>
<body>

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">装备维修计划单&nbsp;&nbsp;    </td>
  </tr>
</table>
<br>

<table class="TableBlock" width="80%" align="center">
	<tr>
		<td nowrap class="TableData">送修通知单号：</td>
      <td class="TableContent">
        20110321001     
      </td>
 	  <td nowrap class="TableData">主修人：</td>
      <td class="TableContent">
      修理员1
    </td>
	 	<td nowrap class="TableData">辅修人：</td>
    <td>
     修理员2
    </td>
	</tr>
	<tr>
    <td nowrap class="TableData">送修部队：</td>
     <td class="TableContent" colspan="5">
  部门1
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">承修部门：</td>
     <td class="TableContent" colspan="2">
      部门2
     </td>
    <td nowrap class="TableData">送修时间：</td>
     <td class="TableContent" colspan="2">
2011年03月15日
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">维修流程</td>
     <td class="TableData" align="center" colspan=3>
      预计范围时间
     </td>
	<td class="TableData" align="center" colspan=2>
      参与人员
     </td>
  </tr>
  <tr>
    <td nowrap class="TableData">装备检测：</td>
     <td class="TableContent" >
     2011-03-15
     </td>
	 <td class="TableData">至</td>
	  <td class="TableContent" >
     2011-03-20
     </td>
	  <td class="TableContent" colspan="2">修理员1 ,修理员2</td>
  </tr> <tr>
      <td nowrap class="TableData">装备维修：</td>
     <td class="TableContent" >
     2011-03-15
     </td>
	 <td class="TableData">至</td>
	  <td class="TableContent" >
     2011-03-20
     </td>
	    <td class="TableContent" colspan="2">修理员1 ,修理员2
    </td>
    </tr><tr>
      <td nowrap class="TableData">装备质检：</td>
     <td class="TableContent" >
     2011-03-15
     </td>
	 <td class="TableData">至</td>
	  <td class="TableContent" >
     2011-03-20
     </td>
	   <td class="TableContent" colspan="2">修理员1 ,修理员2
    </td>
  </tr> 
  <tr>
      <td nowrap class="TableData">整机质检：</td>
     <td class="TableContent" >
     2011-03-15
     </td>
	 <td class="TableData">至</td>
	  <td class="TableContent" >
     2011-03-20
     </td>
	   <td class="TableContent" colspan="2">修理员1 ,修理员2
    </td>
  </tr> 
      <td nowrap class="TableData">装备试验：</td>
     <td class="TableContent" >
     2011-03-15
     </td>
	 <td class="TableData">至</td>
	  <td class="TableContent" >
     2011-03-20
     </td>
	    <td class="TableContent" colspan="2">修理员1 ,修理员2
    </td>
  </tr> 
  <tr>
   <td class="TableData" colspan="3" align="left">装备名称：东风汽车</td>
    <td class="TableData"  align="left">装备编号：002</td>
   <td class="TableData" colspan="2" align="left">修理等级：大修</td>
  </tr>
  <tr>
  <td colspan="2">签发机关（盖章）</td><td colspan="4"></td>
  </tr>
  <tr><td colspan="6" ></td></tr>
  <tr><td colspan="6" ></td></tr>
    <tr><td colspan="6" ></td></tr>
  <tr>
  <td colspan="4"></td><td colspan="2" align="center">业务处</td>
  </tr> 
    <tr>
	<td colspan="6" ></td>
	</tr>
  <tr>
  <td colspan="6" ></td>
  </tr>
    <tr>
	<td colspan="6" ></td>
	</tr>
  <tr>
  <td colspan="4"></td><td colspan="2" align="center">签发日期</td>
  </tr> 
  <tr>
  <td>承办人</td><td colspan="5"></td>
  </tr>
    <tr>
  <td colspan="4"></td><td colspan="2" align="center">有效时间</td>
  </tr> 
  <% if ("1".equals(isIframe)) { %>
  <tr class="TableControl">
  <td colspan="4"></td><td colspan="2" align="center"><input type=button value="检测记录" class="SmallButtonW2" onclick="writeRecord()"></td>
  </tr> 
  <% } %>
</table>
</body>
</html>