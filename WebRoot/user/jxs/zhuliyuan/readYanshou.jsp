<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
String isRead = request.getParameter("isRead");
if ( "".equals(isRead ) || isRead == null) {
  isRead = "0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>验收方案</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
</head>
<body>
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"> 验收方案通知单&nbsp;&nbsp;    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/addWelfareInfo.act"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="80%" align="center">
	<tr>
		<td nowrap class="TableData">送修通知单号：</td>
      <td class="TableContent" colspan="5" >
        20110113110       
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
     <td class="TableContent" colspan="2" >
     部门2
     </td>
    <td nowrap class="TableData">送修时间：</td>
     <td class="TableContent" colspan="2" >
     2011年01月12日
     </td>
  </tr>
   <tr>
   <td class="TableData">主修人：</td>
      <td class="TableContent" colspan="5">
      修理员1
	  </td>
   </tr>
	<tr>
    <td  class="TableData" colspan="3" align="center">装备名称：东风汽车</td>
     <td class="TableData" align="center" >
      装备编号：110
     </td>
	<td class="TableData" align="center" colspan=2>
      修理等级：中修
     </td>
  </tr>
  <tr>
      <td nowrap class="TableData">检验日期：</td>
     <td class="TableContent" colspan="2">
     2011年10月1日
     </td>
    <td nowrap class="TableData">总工：</td>
     <td class="TableContent" colspan="2">
     总工1
	    </td>
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
 
  <tr  class="TableControl">
  <td colspan="6" align="right">
  <input type="button" value="返  回" onclick="history.go(-1)" class="BigButton"></input>
 <input type="button" value="下一步" class="BigButton"></input>
  </td>
  </tr> 

</table>
</form>

</body>
</html>