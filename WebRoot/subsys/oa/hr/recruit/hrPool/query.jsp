<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人才档案查询</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css"><script type="text/javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/orgselect.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css"	type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/hrPool/js/hrPoolLogic.js"></script>
<script type="text/javascript">

function doInit(){
//籍贯	
	getSelectedCode("AREA","EMPLOYEE_NATIVE_PLACE");
//政治面貌
	getSelectedCode("STAFF_POLITICAL_STATUS","EMPLOYEE_POLITICAL_STATUS");
//期望工作性质
	getSelectedCode("JOB_CATEGORY","JOB_CATEGORY");
//学历
	getSelectedCode("STAFF_HIGHEST_SCHOOL","EMPLOYEE_HIGHEST_SCHOOL");
	
	
}

function ControlContent(){
	if($("contentid").style.display == 'none'){
		$("contentid").style.display = '';
		document.getElementById("imgar").src = '<%=imgPath%>/arrow_up.gif';
	}else{
		$("contentid").style.display = 'none';
		document.getElementById("imgar").src = '<%=imgPath%>/arrow_down.gif';
	} 
}

function doSubmit(){
	var query = $("form1").serialize();
	location = "<%=contextPath%>/subsys/oa/hr/recruit/hrPool/search.jsp?"+query;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3"
	class="small">
	<tr>
		<td class="Big"><img align="middle"	src="<%=imgPath%>/infofind.gif"><span class="big3"> 人才档案查询</span></td>
	</tr>
</table>
<form enctype="multipart/form-data" action="" method="post" name="form1"	id="form1">
<table class="TableBlock" width="770" align="center">
	<tr>
		<td nowrap class="TableData" width="100">计划编号：</td>
		<td class="TableData" width="180"><input type="text"	name="PLAN_NO" value=""></td>
		<td nowrap class="TableData" width="100">应聘人姓名：</td>
		<td class="TableData" width="180"><input type="text"	name="EMPLOYEE_NAME" id="EMPLOYEE_NAME" class="BigInput"></td>
	</tr>
	<tr>
		<td nowrap class="TableData" width="100">性别：</td>
		<td class="TableData" width="180">
		<select name="EMPLOYEE_SEX"	>
			<option value="">性别&nbsp;&nbsp;</option>
			<option value="0">男</option>
			<option value="1">女</option>
		</select></td>
		<td nowrap class="TableData">籍贯：</td>
		<td class="TableData"><select name="EMPLOYEE_NATIVE_PLACE" id="EMPLOYEE_NATIVE_PLACE"		>
			<option value="">籍贯&nbsp;&nbsp;</option>
		</select></td>
	</tr>
	<tr>
		<td nowrap class="TableData">政治面貌：</td>
		<td class="TableData"><select name="EMPLOYEE_POLITICAL_STATUS" id="EMPLOYEE_POLITICAL_STATUS"		>
			<option value="">政治面貌</option>
		</select></td>
		<td nowrap class="TableData" width="100">应聘岗位：</td>
		<td class="TableData"><input type="text" name="POSITION"
			id="POSITION" class="BigInput"></td>
	</tr>
	<tr>
		<td nowrap class="TableData">期望工作性质：</td>
		<td class="TableData"><select name="JOB_CATEGORY"
			id="JOB_CATEGORY">
			<option value="">工作性质</option>
		</select></td>
		<td nowrap class="TableData">期望从事职业：</td>
		<td class="TableData"><input type="text" name="JOB_INTENSION"
			id="JOB_INTENSION" ></td>
	</tr>
	<tr>
		<td nowrap class="TableData">期望工作城市：</td>
		<td class="TableData"><input type="text" name="WORK_CITY"	id="WORK_CITY" class="BigInput"></td>
		<td nowrap class="TableData" width="100">期望薪水(税前)：</td>
		<td class="TableData" width="180"><input type="text"	name="EXPECTED_SALARY" id="EXPECTED_SALARY" size="10"
			class="BigInput">&nbsp;元</td>
	</tr>
	<tr>
		<td nowrap class="TableData" width="100">到岗时间：</td>
		<td class="TableData" width="180"><select name="START_WORKING"
			id="START_WORKING">
			<option value="">到岗时间</option>
			<option value="0">1周以内</option>
			<option value="1">1个月内</option>
			<option value="2">1~3个月</option>
			<option value="3">3个月后</option>
			<option value="4">随时到岗</option>
		</select></td>
		<td nowrap class="TableData" width="100">所学专业：</td>
		<td class="TableData" width="180"><input type="text"
			name="EMPLOYEE_MAJOR" id="EMPLOYEE_MAJOR" class="BigInput"></td>
	</tr>
	<tr>
		<td nowrap class="TableData" width="100">学历：</td>
		<td class="TableData" width="180"><select
			name="EMPLOYEE_HIGHEST_SCHOOL" id="EMPLOYEE_HIGHEST_SCHOOL">
			<option value="">学历&nbsp;&nbsp;</option>
		</select></td>
		<td nowrap class="TableData" width="100">现居住城市：</td>
		<td class="TableData" width="180"><input type="text"
			name="RESIDENCE_PLACE" id="RESIDENCE_PLACE" class="BigInput" value="">
		</td>
	</tr>
	<tr class="TableHeader" height="25">
		<td nowrap colspan="4"><span>更多查询选项</span>&nbsp;&nbsp;<img src="<%=imgPath%>/arrow_down.gif" border="0" id="imgar" title="展开/收缩"
			onClick="ControlContent();" style="cursor: hand"></td>
	</tr>
	<tbody  style="display: none" id="contentid">
			<tr>
				<td nowrap class="TableData" width="100">民族：</td>
				<td class="TableData" width="180"><input type="text"
					name="EMPLOYEE_NATIONALITY" id="EMPLOYEE_NATIONALITY"
					class="BigInput" value=""></td>
				<td nowrap class="TableData" width="100">健康状况：</td>
				<td class="TableData" width="180"><input type="text"
					name="EMPLOYEE_HEALTH " id="EMPLOYEE_HEALTH " class="BigInput"></td>
			</tr>
			<tr>
				<td nowrap class="TableData" width="100">婚姻状况：</td>
				<td class="TableData" width="180"><select
					name="EMPLOYEE_MARITAL_STATUS" >
					<option value="">婚姻状况</option>
					<option value="0">未婚&nbsp;&nbsp;</option>
					<option value="1">已婚</option>
					<option value="2">离异</option>
				</select></td>
				<td nowrap class="TableData" width="100">户口所在地：</td>
				<td class="TableData" width="180"><input type="text"
					name="EMPLOYEE_DOMICILE_PLACE" id="EMPLOYEE_DOMICILE_PLACE"
					class="BigInput"></td>
			</tr>
			<tr>
				<td nowrap class="TableData" width="100">毕业学校：</td>
				<td class="TableData" width="180"><input type="text"
					name="GRADUATION_SCHOOL" id="GRADUATION_SCHOOL" class="BigInput"></td>
				<td nowrap class="TableData" width="100">计算机水平：</td>
				<td class="TableData" width="180"><input type="text"
					name="COMPUTER_LEVEL" id="COMPUTER_LEVEL" class="BigInput"></td>
			</tr>
			<tr>
				<td nowrap class="TableData" width="100">外语语种1：</td>
				<td class="TableData" width="180"><input type="text"
					name="FOREIGN_LANGUAGE1" id="FOREIGN_LANGUAGE1" class="BigInput"></td>
				<td nowrap class="TableData" width="100">外语语种2：</td>
				<td class="TableData" width="180"><input type="text"
					name="FOREIGN_LANGUAGE2" id="FOREIGN_LANGUAGE2" class="BigInput"></td>
			</tr>
			<tr>
				<td nowrap class="TableData" width="100">外语水平1：</td>
				<td class="TableData" width="180"><input type="text"
					name="FOREIGN_LEVEL1" id="FOREIGN_LEVEL1" class="BigInput"></td>
				<td nowrap class="TableData" width="100">外语水平2：</td>
				<td class="TableData" width="180"><input type="text"
					name="FOREIGN_LEVEL2" id="FOREIGN_LEVEL2" class="BigInput"></td>
			</tr>
	</tbody>
	<tr align="center" class="TableControl">
		<td colspan="4" nowrap><input type="button" value="查询"
			class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;</td>
	</tr>
</table>
</form>


</body>
</html>