<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人事档案查询</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffInfo/js/staffInfoLogic.js"></script>
<script type="text/javascript">

function doInit(){
//初始化日期
	setDate();
//在职状态
	getSelectedCode("WORK_STATUS","workStatus");
//学历
	getSelectedCode("STAFF_HIGHEST_SCHOOL","staffHighestSchool");
//学位
	getSelectedCode("EMPLOYEE_HIGHEST_DEGREE","staffHighestDegree");
//籍贯	
	getSelectedCode("AREA","staffNativePlace");
//员工类型
	getSelectedCode("STAFF_OCCUPATION","staffOccupation");
//政治面貌
	getSelectedCode("STAFF_POLITICAL_STATUS","staffPoliticalStatus");
//职称
	getSelectedCode("PRESENT_POSITION","presentPosition");
	
}
//日期
function setDate(){
	var date1Parameters = {
		inputId:'birthdayMin',
		property:{isHaveTime:false},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
		inputId:'birthdayMax',
		property:{isHaveTime:false},
		bindToBtn:'date2'
	};
	new Calendar(date2Parameters);
	
	var date3Parameters = {
		inputId:'graduationMin',
		property:{isHaveTime:false},
		bindToBtn:'date3'
	};
	new Calendar(date3Parameters);
	
	var date4Parameters = {
		inputId:'graduationMax',
		property:{isHaveTime:false},
		bindToBtn:'date4'
	};
	new Calendar(date4Parameters);
	
	var date5Parameters = {
		inputId:'joinPartyMin',
		property:{isHaveTime:false},
		bindToBtn:'date5'
	};
	new Calendar(date5Parameters);
	
	var date6Parameters = {
		inputId:'joinPartyMax',
		property:{isHaveTime:false},
		bindToBtn:'date6'
	};
	new Calendar(date6Parameters);
	
	var date7Parameters = {
		inputId:'beginningMin',
		property:{isHaveTime:false},
		bindToBtn:'date7'
	};
	new Calendar(date7Parameters);
	
	var date8Parameters = {
		inputId:'beginningMax',
		property:{isHaveTime:false},
		bindToBtn:'date8'
	};
	new Calendar(date8Parameters);
	
	var date9Parameters = {
		inputId:'employedMin',
		property:{isHaveTime:false},
		bindToBtn:'date9'
	};
	new Calendar(date9Parameters);
	
	var date10Parameters = {
		inputId:'employedMax',
		property:{isHaveTime:false},
		bindToBtn:'date10'
	};
	new Calendar(date10Parameters);
	
}


function doSubmit(){
	var query = $("form1").serialize();
	location = "<%=contextPath%>/subsys/oa/hr/manage/query/search.jsp?"+query;
}

function exreport(){
	var query = $("form1").serialize();
	location = '<%=contextPath%>/subsys/oa/hr/manage/query/export.jsp?' + query;
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="middle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 人事档案查询</span></td>
  </tr>
</table>

<form action="#"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="770" align="center">
  <tr>
    <td nowrap class="TableHeader" colspan="6"><img src="<%=imgPath %>/green_arrow.gif"> &nbsp;查询范围
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 所属部门：</td>
    <td class="TableData" colspan="5">
     <input type="hidden" name="staffDept" id="staffDept" value="">
      <textarea cols=35 name="staffDeptDesc" id="staffDeptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept(['staffDept', 'staffDeptDesc']);">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('staffDept').value='';$('staffDeptDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">姓名：</td>
    <td class="TableData"><input type="text" name="staffName" id="staffName" class="BigInput"></td>  	
    <td nowrap class="TableData">英文名：</td>
    <td class="TableData"><input type="text" name="staffEName" id="staffEName" class="BigInput"></td>
    <td nowrap class="TableData" width="100">在职状态：</td>
    <td class="TableData"  width="180">
    	<select name="workStatus" id="workStatus">
        <option value=""></option>
      </select>    	
    </td>
  </tr>
  <tr>
  	<td nowrap class="TableData" width="100">编号：</td>
    <td class="TableData" width="180">
    	<input type="text" name="staffNo" id="staffNo" class="BigInput">
    </td>     
    <td nowrap class="TableData">工号：</td>
    <td class="TableData">
    	<input type="text" name="workNo" id="workNo" class="BigInput">
    </td> 
    <td nowrap class="TableData">性别：</td>
    <td class="TableData">
    	<select name="staffSex" id="staffSex" class="">
 			 <option ></option>
       <option value="0">男</option>
       <option value="1">女</option>
     	</select>
    </td>      
  </tr>
  <tr>    
    <td nowrap class="TableData">身份证号：</td>
    <td class="TableData">
    	<input type="text" name="staffCardNo" id="staffCardNo" class="BigInput">
    </td>
    <td nowrap class="TableData">出生日期：</td>
    <td class="TableData" nowrap colspan="3">
      <input type="text" name="birthdayMin" id="birthdayMin" size="10" maxlength="10" class="BigInput" value="" />
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至
      <input type="text" name="birthdayMax" id="birthdayMax" size="10" maxlength="10" class="BigInput" value="" />
       <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      日期格式形如 1999-1-2
    </td>    
  </tr>
  <tr>
    <td nowrap class="TableData">年龄：</td>
    <td class="TableData" nowrap>
      <input type="text" name="ageMin" id="ageMin" size="5" maxlength="10" class="BigInput" value="">
      至 <input type="text" name="ageMax" id="ageMax" size="5" maxlength="10" class="BigInput" value="">
    </td>
    <td nowrap class="TableData">民族：</td>
    <td class="TableData"><input type="text" name="staffNationality" id="staffNationality" class="BigInput"></td>
    <td nowrap class="TableData">籍贯：</td>
    <td class="TableData">
    	<select name="staffNativePlace" id="staffNativePlace" >
    		<option ></option>
      </select>
    </td>         
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">工种：</td>
    <td class="TableData"  width="180"><input type="text" name="workType" id="workType" class="BigInput"></td> 
    <td nowrap class="TableData" width="100">户口所在地</td>
		<td class="TableData"  width="180" colspan="3">
			<input type="text" name="staffDomicilePlace" id="staffDomicilePlace" size="40" class="BigInput">
		</td> 
  </tr>  
  <tr>
    <td nowrap class="TableData">婚姻状况：</td>
    <td class="TableData">
      <select name="staffMaritalStatus" id="staffMaritalStatus" >
      	<option value="" selected></option>    
        <option value="0" >未婚</option>
        <option value="1" >已婚</option>
        <option value="2" >离异</option>
      </select>    	
    </td>
    <td nowrap class="TableData" width="100">健康状况：</td>
		<td class="TableData"  width="180"><input type="text" name="staffHealth" id="staffHealth" class="BigInput"></td>				                 
    <td nowrap class="TableData">政治面貌：</td>
    <td class="TableData">
        <select name="staffPoliticalStatus" id="staffPoliticalStatus" >
          <option value="" selected></option>
        </select>  	
  </tr>   
	<tr>
    <td nowrap class="TableData" width="100">行政级别：</td>
    <td class="TableData"  width="180">
    	<input type="text" name="administrationLevel" id="administrationLevel" class="BigInput">
    </td>
    <td nowrap class="TableData" width="100"  >员工类型：</td>
    <td class="TableData">
        <select name="staffOccupation" id="staffOccupation" >
        	<option ></option>
        </select>    	
    </td>          
    <td nowrap class="TableData" width="100">计算机水平：</td>
    <td class="TableData"  width="180"><input type="text" name="computerLevel" id="computerLevel" class="BigInput"></td>                  
  </tr>   
  <tr>
    <td nowrap class="TableData" width="100">学历：</td>
    <td class="TableData"  width="180">
      <select name="staffHighestSchool" id="staffHighestSchool" >
      	<option ></option>
      </select>
    </td>
    <td nowrap class="TableData" width="100">学位：</td>
    <td class="TableData"  width="180">
    	<select name="staffHighestDegree" id="staffHighestDegree" class="">
			  <option value=""></option>
      </select>
    </td>   
    <td nowrap class="TableData" width="100">专业：</td>
    <td class="TableData"  width="180"><input type="text" name="staffMajor" id="staffMajor" class="BigInput"></td>                   
  </tr>     
  <tr>
  	<td nowrap class="TableData" width="100">毕业学校：</td>
    <td class="TableData"  width="180"><input type="text" name="graduationSchool" id="graduationSchool" class="BigInput"></td>
    <td nowrap class="TableData" width="100">职务：</td>
    <td class="TableData"  width="180"><input type="text" name="jobPosition" id="jobPosition" class="BigInput"></td>               
  	<td nowrap class="TableData" width="100">职称：</td>
    <td class="TableData"  width="180">
        <select name="presentPosition" id="presentPosition" class="">
        	<option ></option>
        </select>
  </tr>
  <tr>
  	<td nowrap class="TableData" width="100">毕业时间：</td>
    <td class="TableData" nowrap colspan="2">
      <input type="text" name="graduationMin" id="graduationMin" size="10" maxlength="10" class="BigInput" value="" />
      <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至 
      <input type="text" name="graduationMax" id="graduationMax" size="10" maxlength="10" class="BigInput" value="" />
      <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >   
    </td> 
    <td nowrap class="TableData" width="100">入党时间：</td>
    <td class="TableData"  nowrap colspan="2">
      <input type="text" name="joinPartyMin" id="joinPartyMin" size="10" maxlength="10" class="BigInput" value="" />  
      <img id="date5" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >   
      至
      <input type="text" name="joinPartyMax" id="joinPartyMax" size="10" maxlength="10" class="BigInput" value="" />  
      <img id="date6" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >   
    </td> 
  </tr>        
  <tr>
  	<td nowrap class="TableData" width="100">参加工作时间：</td>
    <td class="TableData"  nowrap colspan="2">
      <input type="text" name="beginningMin" id="beginningMin" size="10" maxlength="10" class="BigInput" value="" /> 
      <img id="date7" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >   
      至
      <input type="text" name="beginningMax" id="beginningMax" size="10" maxlength="10" class="BigInput" value="" />     	
      <img id="date8" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >   
    </td>
    <td nowrap class="TableData" width="100">入职时间：</td>
    <td class="TableData" nowrap colspan="2">
      <input type="text" name="employedMin" id="employedMin" size="10" maxlength="10" class="BigInput" value="" />  
      <img id="date9" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至 
      <input type="text" name="employedMax" id="employedMax" size="10" maxlength="10" class="BigInput" value="" />   
      <img id="date10" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >  	
    </td>      
  </tr>       
  <tr>
    <td nowrap class="TableData" width="100">总工龄：</td>
    <td class="TableData"  width="180"><input type="text" name="workAgeMin" size="5" class="BigInput"> 至 <input type="text" name="workAgeMax" size="5" class="BigInput"></td>    
    <td nowrap class="TableData" width="100">本单位工龄：</td>
    <td class="TableData"  width="180"><input type="text" name="jobAgeMin" id="jobAgeMin" size="4" class="BigInput"> 至 <input type="text" name="jobAgeMax" id="jobAgeMax" size="4" class="BigInput"></td>
    <td nowrap class="TableData" width="100">年休假：</td>
    <td class="TableData"><input type="text" name="leaveTypeMin" id="leaveTypeMin" size="5" class="BigInput"> 至 <input type="text" name="leaveTypeMax" id="leaveTypeMax" size="5" class="BigInput"></td>                    
  </tr>        
  <tr>
    <td nowrap class="TableData" width="100">外语语种1：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage1" id="foreignLanguage1" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种2：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage2" id="foreignLanguage2" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种3：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLanguage3" id="foreignLanguage3" class="BigInput"></td>                 
  </tr>       
  <tr>
    <td nowrap class="TableData" width="100">外语水平1：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLevel1" id="foreignLevel1" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语水平2：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLevel2" id="foreignLevel2" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语水平3：</td>
    <td class="TableData"  width="180"><input type="text" name="foreignLevel3" id="foreignLevel3" class="BigInput"></td>                 
  </tr>    
  <tr>
    <td class="TableHeader" colspan="6" >
         </td>
  </tr>
  <tr align="center" class="TableControl">
      <td colspan="6" nowrap>
        <input type="button" value="查询" class="BigButton" onClick="doSubmit()">&nbsp;&nbsp;
        <input type="button" value="导出" class="BigButton" onClick="exreport()">
      </td>
   </tr>          
</table>
</form>


</body>
</html>