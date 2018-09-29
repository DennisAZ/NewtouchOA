 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品登记报表</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js" ></script>

<script type="text/javascript"> 
function SumReport(flag){
	alert(flag==1);
    if(flag==1){
    var	URL="deptSum.jsp";
     var myleft=(screen.availWidth-500)/2;
      window.open(URL,"deptSum","height=500,width=680,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
    }

}
</script>
</head>
<body topmargin="5">
<table class="TableBlock" align="center" >
  <tr>
    <td nowrap  class="TableLine2" colspan="2" align="left">
      <a href="javascript:SumReport('1');">部门领用物品汇总</a>
    </td>
   </tr>
</table>
</body>
</html>