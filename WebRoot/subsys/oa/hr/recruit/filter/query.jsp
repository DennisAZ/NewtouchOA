<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>招聘筛选查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/javascript">

function doInit(){
	
}



function doSubmit(){
	var query = $("form1").serialize();
	location = "<%=contextPath%>/subsys/oa/hr/recruit/filter/search.jsp?"+query;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="middle" src="<%=imgPath %>/infofind.gif"><span class="big3">招聘筛选查询</span></td>
  </tr>
</table>
<br>
<form enctype="" action="search.php"  method="post" name="form1" id="form1">
<table class="TableBlock" width="600" align="center">
  <tr>
    <td nowrap class="TableData">应聘者姓名：</td>
    <td class="TableData" >
      <INPUT type="text" name="EMPLOYEE_NAME" class=BigInput size="15"  value="">
    </td>
    <td nowrap class="TableData">计划名称：</td>
    <td class="TableData">
      <INPUT type="text"name="PLAN_NAME" class=BigInput size="15" value="">
      <INPUT type="hidden" name="PLAN_NO" value="">
    </td>  
  </tr>
  <tr>
    <td nowrap class="TableData">应聘岗位：</td>
    <td class="TableData" >
      <INPUT type="text" name="POSITION" class=BigInput size="15" value="">
    </td> 
    <td nowrap class="TableData">所学专业：</td>
    <td class="TableData" >
      <INPUT type="text"name="EMPLOYEE_MAJOR" class=BigInput size="15" value="">
    </td>    
  </tr>
  <tr>
    <td nowrap class="TableData">联系电话：</td>
    <td class="TableData">
      <INPUT type="text"name="EMPLOYEE_PHONE" class=BigInput size="15" value="">
    </td>
    <td nowrap class="TableData">发起人：</td>
    <td class="TableData" >
      <input type="hidden" name="transactorStep" id="transactorStep" value="">
       <INPUT type="text" name="transactorStepDesc" id="transactorStepDesc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['transactorStep', 'transactorStepDesc']);">添加</a>
    </td>     
  </tr>
  <tr>
    <td nowrap class="TableData">初选办理人：</td>
    <td class="TableData">
   	 		<input type="hidden" name="nextTransaStep1" id="nextTransaStep1" value="">
       <INPUT type="text" name="nextTransaStep1Desc" id="nextTransaStep1Desc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['nextTransaStep1', 'nextTransaStep1Desc']);">添加</a>
    
    </td>
    <td nowrap class="TableData">复选办理人：</td>
    <td class="TableData">
      <input type="hidden" name="nextTransaStep2" id="nextTransaStep2" value="">
       <INPUT type="text" name="nextTransaStep2Desc" id="nextTransaStep2Desc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['nextTransaStep2', 'nextTransaStep2Desc']);">添加</a>
    </td>
  </tr>
  <tr>
  	<td nowrap class="TableData">决选办理人：</td>
    <td class="TableData">
       <input type="hidden" name="nextTransaStep3" id="nextTransaStep3" value="">
       <INPUT type="text" name="nnextTransaStep3Desc" id="nextTransaStep3Desc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['nextTransaStep3', 'nextTransaStep3Desc']);">添加</a>
      
    </td>
    <td nowrap class="TableData">加试办理人：</td>
    <td class="TableData">
       <input type="hidden" name="nextTransaStep4" id="nextTransaStep4" value="">
       <INPUT type="text" name="nextTransaStep4Desc" id="nextTransaStep4Desc" size="15" class="BigStatic" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['nextTransaStep4', 'nextTransaStep4Desc']);">添加</a>
      
    </td>
  </tr>
    <tr>
  	<td nowrap class="TableData">筛选状态：</td>
    <td class="TableData" colspan=3>
      <select name="STATUS" >
         <option value=""></option>
         <option value="0">待筛选</option>
         <option value="1">未通过</option>	
         <option value="2">已通过</option>
      </select>
    </td>
  </tr>
	<tr align="center" class="TableControl">
	  <td colspan="6" nowrap>
	    <INPUT type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
	  </td>
 	</tr>          
</table>
</form>

</body>
</html>