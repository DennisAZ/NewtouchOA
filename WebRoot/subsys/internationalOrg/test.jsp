<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%
String seqId = request.getParameter("seqId");
if(seqId == null) {
  seqId = "";
}
String projId = request.getParameter("projId");
if(projId == null) {
  projId = "";
}
String pageNo = request.getParameter("pageNo");
if(pageNo == null) {
  pageNo = "";
}
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
String dayTime = sf.format(new Date());
YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);

SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String dayTime2 = sf2.format(new Date()).substring(0,4) + sf2.format(new Date()).substring(5,7);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href = "<%=contextPath %>/core/styles/style1/css/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>

<script type="text/javascript">


function test(){
  url = "<%=contextPath%>/yh/subsys/internationalOrg/act/YHInternationalOrgAct/englishTochina.act";
  var rtJson = getJsonRs(url);
  alert(rtJson.rtMsrg); 
}


</script>
</head>
<body>
<form name="form1" id="form1" method="post">
  <table class="TableBlock" cellpadding="1" width="80%" align="center" colspan="3">
  
  <tr class="TableLine1">
    <td colspan="3" align="center">
      <input type="button" value="导入" class="SmallButton" onclick="test()">
    </td>
  </tr>
  </table>
</form>
</body>
</html>