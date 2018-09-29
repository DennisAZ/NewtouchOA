<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人事档案</title>
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
    <img border=0 src="<%=imgPath%>/finance1.gif" WIDTH="18" HEIGHT="18" align="absmiddle"> <b>人事分析</b>
    </td>
    <td class="right">
    </td>
  </tr>
</table>
<table class="TableNav" width="100%" align="center">
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_INFO" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>人事档案</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_HT" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>合同管理</b></a></td>
   </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_JC" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>奖惩管理</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_ZZ" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>证照管理</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_XX" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>学习经历</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_JL" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>工作经历</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_JN" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>劳动技能</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_GX" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>社会关系</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_DD" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>人事调动</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_LZ" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>离职管理</b></a></td>
  </tr>
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_FZ" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>复职管理</b></a></td>
  </tr>  
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_ZC" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>职称评定</b></a></td>
  </tr>  
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/index1.jsp?module=HR_GH" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>员工关怀</b></a></td>
  </tr>  
 
 
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/select1.jsp?module=HR_LZ1" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>离职统计</b></a></td>
  </tr> 
  <tr class="TableData">
    <td><a href="<%=contextPath%>/subsys/oa/hr/manage/staffAnalysis/select1.jsp?module=HR_DD1" target="hrmain"><img border=0 src="<%=imgPath%>/hr_manage.gif" WIDTH="16" HEIGHT="16" align="absmiddle"> <b>人事调动统计</b></a></td>
  </tr> 
 
 
</table>
</body>
</html>