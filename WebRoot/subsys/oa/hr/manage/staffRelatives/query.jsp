<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>社会关系信息查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffRelatives/js/staffRelativesLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getSecretFlag("HR_STAFF_RELATIVES1","relationship");
}

function doSubmit(){
	var query = $("form1").serialize(); 
	location.href = "<%=contextPath %>/subsys/oa/hr/manage/staffRelatives/search.jsp?" + query
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 社会关系信息查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData">单位员工：</td>
      <td class="TableData">
        <input type="hidden" name="staffName" id="staffName" value="">
         <input type="text" name="staffNameDesc" id="staffNameDesc" size="12" class="BigStatic" readonly value="">&nbsp;
	      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['staffName', 'staffNameDesc'],null,null,1);">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 成员姓名：</td>
      <td class="TableData">
        <input type="text" name="member" id="member" size="12" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 与本人关系：</td>
      <td class="TableData" >
        <select name="relationship" id="relationship" title="与本人关系可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">与本人关系&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData"> 职业：</td>
      <td class="TableData">
        <input type="text" name="jobOccupation" id="jobOccupation" size="15" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 工作单位：</td>
      <td class="TableData">
        <input type="text" name="workUnit" id="workUnit" size="15" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</body>
</html>