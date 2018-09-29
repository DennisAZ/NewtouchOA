<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>办公用品报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit(){

}

</script>
</head>
<body topmargin="5" onload="doInit()" scroll="yes">
<table width="100%" class="BlockTop">
  <tr>
    <td class="left">
    </td>
    <td class="center">
      <img border=0 src="<%=imgPath%>/notify_open.gif" WIDTH="18" HEIGHT="18" align="absmiddle"> <b>办公用品登记报表</b>
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<table class="TableNav" width="100%" align="center">
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_WPZB" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>物品总表 </b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_CGWP&mapType=1" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>采购物品报表 </b></a></td>
   </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_LYWP&mapType=1" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>部门、人员领用物品报表</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_JYWP" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>借用物品报表 </b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_GHWP" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>归还物品报表 </b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_WGHWP" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>未归还物品报表</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_BFWP" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>报废物品报表 </b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_WHJL" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>维护记录报表 </b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/officeProduct/report/index1.jsp?module=OFFICE_TZ" target="hrmain"><img border=0 src="<%=imgPath%>/office_Product.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>台帐报表 </b></a></td>
  </tr>
</table>
</body>
</html>