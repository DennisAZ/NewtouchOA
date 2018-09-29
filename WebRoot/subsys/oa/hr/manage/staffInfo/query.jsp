<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人事档案查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffInfo/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffInfo/js/staffInfoLogic.js"></script>
<script type="text/javascript">

function doInit(){
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
	deptFunc();
	setDate();
}
//部门下拉框
function deptFunc(){
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

//日期
function setDate(){
	var date1Parameters = {
		inputId:'staffBirth',
		property:{isHaveTime:false},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);

	var date2Parameters = {
			inputId:'datesEmployed',
			property:{isHaveTime:false},
			bindToBtn:'date2'
		};
		new Calendar(date2Parameters);
	
	var date3Parameters = {
		inputId:'joinPartyTime',
		property:{isHaveTime:false},
		bindToBtn:'date3'
	};
	new Calendar(date3Parameters);
	
	var date4Parameters = {
		inputId:'beginSalsryTime',
		property:{isHaveTime:false},
		bindToBtn:'date4'
	};
	new Calendar(date4Parameters);
	
	var date5Parameters = {
		inputId:'jobBeginning',
		property:{isHaveTime:false},
		bindToBtn:'date5'
	};
	new Calendar(date5Parameters);
	
	var date6Parameters = {
		inputId:'graduationDate',
		property:{isHaveTime:false},
		bindToBtn:'date6'
	};
	new Calendar(date6Parameters);
}

function clearValue(str){
	if(str){
		str.value = "";
	}
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
  location = "<%=contextPath%>/subsys/oa/hr/manage/staffInfo/search.jsp?"+query;
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="middle" src="<%=imgPath%>/infofind.gif"><span class="big3"> 人事档案查询</span></td>
  </tr>
</table>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="770" align="center">
  <tr>
    <td nowrap class="TableData" width="100">OA用户名：</td>
    <td class="TableData" width="180"><input type="text" name="userId" id="userId" value=""></td>
    <td nowrap class="TableData">姓名：</td>
    <td class="TableData"><input type="text" name="staffName" id="staffName"  class="BigInput"></td>
    <td nowrap class="TableData">英文名：</td>
    <td class="TableData"><input type="text" name="staffEName" id="staffEName" class="BigInput"></td> 	           
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">编号：</td>
    <td class="TableData" width="180"><input type="text" name="staffNo" id="staffNo" class="BigInput"></td>
    <td nowrap class="TableData">工号：</td>
    <td class="TableData"><input type="text" name="workNo" id="workNo" class="BigInput"></td> 
    <td nowrap class="TableData" width="100">部门：</td>
    <td class="TableData"  width="180">
    	<select name="deptId" id="deptId" class="inputSelect">
				<option value="" ></option>
			</select>
    </td>        
  </tr>
  <tr>    
    <td nowrap class="TableData">身份证号：</td>
    <td class="TableData"><input type="text" name="staffCardNo" id="staffCardNo" class="BigInput"></td>
    <td nowrap class="TableData">出生日期：</td>
    <td class="TableData">
    	<input type="text" name="staffBirth" id="staffBirth" size="10" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
    	<img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
    <td nowrap class="TableData">性别：</td>
    <td class="TableData">
    	<select name="staffSex" id="staffSex" >
    			<option ></option>
          <option value="0">男</option>
          <option value="1">女</option>
     	</select>
    </td> 
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
    	<select name="staffHighestDegree" id="staffHighestDegree" >
			  <option value=""></option>
      </select>
    </td>
    <td nowrap class="TableData" width="100">专业：</td>
		<td class="TableData"  width="180"><input type="text" name="staffMajor" id="staffMajor" class="BigInput"></td>              
  </tr>
  <tr>
  	<td nowrap class="TableData">民族：</td>
    <td class="TableData"><input type="text" name="staffNationality" id="staffNationality" class="BigInput"></td>
    <td nowrap class="TableData">籍贯：</td>
    <td class="TableData">
    	<select name="staffNativePlace" id="staffNativePlace" >
    		<option ></option>
      </select>
    </td>
    <td nowrap class="TableData">婚姻状况：</td>
    <td class="TableData">
      <select name="staffMaritalStatus" id="staffMaritalStatus" >
      	<option value=""></option>    
        <option value="0">未婚</option>
        <option value="1">已婚</option>
        <option value="2">离异</option>
        <option value="3">丧偶</option>
      </select>    	
    </td>                   
  </tr>
  <tr>
   <td nowrap class="TableData" width="100"  >员工类型：</td>
    <td class="TableData">
        <select name="staffOccupation" id="staffOccupation" >
        	<option ></option>
        </select> 
		</td>
		<td nowrap class="TableData">政治面貌：</td>
    <td class="TableData">
        <select name="staffPoliticalStatus" id="staffPoliticalStatus" >
          <option value=""></option>
        
        </select>
    </td>          
    <td nowrap class="TableData" width="100">工种：</td>
    <td class="TableData"  width="180"><input type="text" name="workType" id="workType" class="BigInput"></td>            
  </tr>  
  <tr>
    <td nowrap class="TableData" width="100">入职时间：</td>
    <td class="TableData"  width="180">
    	<input type="text" name="datesEmployed" id="datesEmployed" size="10" maxlength="10" class="BigInput" value=""  readonly="readonly" onfocus="clearValue(this);" />
    	<img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    	</td>
    <td nowrap class="TableData" width="100">入党时间：</td>
    <td class="TableData"  width="180">
    	<input type="text" name="joinPartyTime" id="joinPartyTime" size="10" maxlength="10" class="BigInput" value="" />
    	<img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>  
    <td nowrap class="TableData" width="100">联系电话：</td>
    <td class="TableData"  width="180"><input type="text" name="staffPhone" id="staffPhone" class="BigInput"></td>     
  </tr>   
  <tr class="TableHeader" height="25">
    <td nowrap colspan="6"><span>更多查询选项</span>&nbsp;&nbsp;<img src="<%=imgPath%>/arrow_down.gif" border="0" id="imgar" title="展开/收缩" onClick="ControlContent();" style="cursor:pointer"></td>
  </tr>
  <tr  style="display:none"  id="contentid">
  	<td colspan="6" valign="top">
			<div>
				<table class="TableBlock" align="center">
  				<tr>
				    <td nowrap class="TableData" width="100">行政级别：</td>
				    <td class="TableData"  width="180"><input type="text" name="administrationLevel" id="administrationLevel" class="BigInput"></td>
				    <td nowrap class="TableData" width="100">手机号码：</td>
            <td class="TableData"  width="180"><input type="text" name="staffMobile" id="staffMobile" class="BigInput"></td>
				    <td nowrap class="TableData" width="100">电子邮件：</td>
            <td class="TableData"  width="180"><input type="text" name="staffEmail" id="staffEmail" class="BigInput"></td>				                    
				  </tr>   
				  <tr>
				    <td nowrap class="TableData" width="100">小灵通：</td>
				    <td class="TableData"  width="180"><input type="text" name="staffLittleSmart" id="staffLittleSmart" class="BigInput"></td>
				    <td nowrap class="TableData" width="100">MSN：</td>
				    <td class="TableData"  width="180"><input type="text" name="staffMsn" id="staffMsn" class="BigInput"></td>
				    <td nowrap class="TableData" width="100">QQ：</td>
            <td class="TableData"  width="180"><input type="text" name="staffQq" id=""staffQq"" class="BigInput"></td>  				                   
				  </tr>   				  
				  <tr>				    
				    <td nowrap class="TableData" width="100">家庭地址：</td>
				    <td class="TableData"  width="180"><input type="text" name="homeAddress" id="homeAddress" class="BigInput"></td> 
				    <td nowrap class="TableData" width="100">职务：</td>
				    <td class="TableData"  width="180"><input type="text" name="jobPosition" id="jobPosition" class="BigInput"></td>               
				  	<td nowrap class="TableData" width="100">职称：</td>
				    <td class="TableData"  width="180">
				        <select name="presentPosition" id="presentPosition" >
				        	<option ></option>
				        </select>
				  </tr>
				  <tr>
				    <td nowrap class="TableData" width="100">参加工作时间：</td>
				    <td class="TableData"  width="180">
				    	<input type="text" name="jobBeginning" id="jobBeginning" size="10" maxlength="10" class="BigInput" value=""  readonly="readonly" onfocus="clearValue(this);"  />  	
				    	<img id="date5" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
				    </td>
				    <td nowrap class="TableData" width="100">毕业时间：</td>
				    <td class="TableData"  width="180">
				    	<input type="text" name="graduationDate" id="graduationDate" size="10" maxlength="10" class="BigInput" value=""  readonly="readonly" onfocus="clearValue(this);" />
				    	<img id="date6" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
				    </td> 
				    <td nowrap class="TableData" width="100">本单位工龄：</td>
            <td class="TableData"  width="180"><input type="text" name="jobAge" id="jobAge" class="BigInput"></td>   	  
				  </tr>        
				  <tr>
				    <td nowrap class="TableData" width="100">总工龄：</td>
				    <td class="TableData"  width="180"><input type="text" name="workAge" id="workAge" class="BigInput"></td>
				    <td nowrap class="TableData" width="100">健康状况：</td>
				    <td class="TableData"  width="180"><input type="text" name="staffHealth" id="staffHealth" class="BigInput"></td>
				    <td nowrap class="TableData" width="100">户口所在地：</td>
				    <td class="TableData"  width="180"><input type="text" name="staffDomicilePlace" id="staffDomicilePlace" class="BigInput"></td>                 
				  </tr>       
				  <tr>
				    <td nowrap class="TableData" width="100">毕业学校：</td>
				    <td class="TableData"  width="180"><input type="text" name="graduationSchool" id="graduationSchool" class="BigInput"></td>				    
				    <td nowrap class="TableData" width="100">计算机水平：</td>
				    <td class="TableData"  width="180"><input type="text" name="computerLevel" id="computerLevel" class="BigInput"></td>
				    <td nowrap class="TableData" width="100">起薪时间：</td>
				    <td class="TableData"  width="180">
				    <input type="text" name="beginSalsryTime" id="beginSalsryTime" size="10" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);"  />
				    	<img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
				    </td>                 
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
				    <td nowrap class="TableData" width="100">特长：</td>
				    <td class="TableData"  width="180" ><input type="text" name="staffSkills" id="staffSkills"  class="BigInput"></td>    
				    <td nowrap class="TableData" width="100">社保号：</td>
            <td class="TableData"  width="180" colspan="3"><input type="text" name="insureNum" id="insureNum"  class="BigInput"></td>                   
				  </tr>  
          <tr>
            <td class="TableHeader" colspan="6" >
            </td>
          </tr>         
			</table>
		</div>
	</td>	
  </tr> 
  <tr align="center" class="TableControl">
	  <td colspan="6" nowrap><input type="button" value="查询" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;</td>
 </tr>          
</table>
</form>
</table>
</body>
</html>