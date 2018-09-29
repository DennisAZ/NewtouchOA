<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String personId = request.getParameter("personId");
if(personId == null){
	personId = "0";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.subsys.oa.hr.manage.staffInfo.act.YHHrStaffInfoAct"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人事档案</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/manage/staffInfo/js/staffInfoListLogic.js"></script>
<script type="text/javascript">

function doInit(){
	var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoQueryAct";
	var url = requestURLStr + "/getStaffInfoByPersonId.act";
	var rtJson = getJsonRs(url, "seqId=<%=personId%>");
	//alert(rsText);
	if (rtJson.rtState == "0") {
		var prcs = rtJson.rtData;
		var isHaveFlag = rtJson.rtMsrg;
		if(isHaveFlag == "0"){
			$("msrg").show();
		}else{
			$("tableDiv").show();
			bindJson2Cntrl(rtJson.rtData);
			if(prcs.staffBirth){
				$("staffBirth").innerHTML = prcs.staffBirth.substr(0, 10);
			}
			if(prcs.joinPartyTime){
				$("joinPartyTime").innerHTML = prcs.joinPartyTime.substr(0, 10);
			}
			if(prcs.datesEmployed){
				$("datesEmployed").innerHTML = prcs.datesEmployed.substr(0, 10);
			}
			if(prcs.beginSalsryTime){
				$("beginSalsryTime").innerHTML = prcs.beginSalsryTime.substr(0, 10);
			}
			if(prcs.jobBeginning){
				$("jobBeginning").innerHTML = prcs.jobBeginning.substr(0, 10);
			}
			if(prcs.graduationDate){
				$("graduationDate").innerHTML = prcs.graduationDate.substr(0, 10);
			}
			getDeptName();
			$("staffMaritalStatus").innerHTML = getMaritalStatus(prcs.staffMaritalStatus)
			if(prcs.photoName){
				var photoName = prcs.photoName;
				var filePathStr = "<%=YHSysProps.getAttachPath().replace('\\','/') + "/" + YHHrStaffInfoAct.headPicFolder%>" ;
				var imgStr = "<img src=<%=contextPath %>/getFile?uploadFileNameServer=" + encodeURIComponent(filePathStr) + "/" +  encodeURIComponent(photoName) + " border='0'  width='150'  ></img>";
				$("showImageSpan").innerHTML = imgStr;
			}else{
				$("showImageSpan").innerHTML = "暂无照片";
			}
			if(prcs.staffSex == "0"){
				$("staffSex").innerHTML = "男";
			}else{
				$("staffSex").innerHTML = "女";
			}
			$("staffAge").innerHTML = staffAgeFunc(prcs.seqId)
			$("staffNativePlace").innerHTML = getSelectedCode("AREA", prcs.staffNativePlace);
			$("staffPoliticalStatus").innerHTML = getSelectedCode("STAFF_POLITICAL_STATUS", prcs.staffPoliticalStatus);
			$("staffType").innerHTML = getSelectedCode("HR_STAFF_TYPE", prcs.staffType);
			$("staffOccupation").innerHTML = getSelectedCode("STAFF_OCCUPATION", prcs.staffOccupation);
			$("presentPosition").innerHTML = getSelectedCode("PRESENT_POSITION", prcs.presentPosition);
			$("workStatus").innerHTML = getSelectedCode("WORK_STATUS", prcs.workStatus);
			$("staffHighestSchool").innerHTML = getSelectedCode("STAFF_HIGHEST_SCHOOL", prcs.staffHighestSchool);
			$("staffHighestDegree").innerHTML = getSelectedCode("EMPLOYEE_HIGHEST_DEGREE", prcs.staffHighestDegree);
			if(prcs.attachmentId){
				$("returnAttId").value = prcs.attachmentId;
				$("returnAttName").value = prcs.attachmentName;
				var selfdefMenu = {
	          office:["downFile","read","edit"], 
	          img:["downFile","play"],  
	          music:["downFile","play"],  
	          video:["downFile","play"], 
	          others:["downFile"]
	      }
	      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','',prcs.seqId,selfdefMenu);
			}else{
				$('attr').innerHTML = "无附件";
			}

		}
		
	}else{
		alert(rtJson.rtMsrg);
	}
}


function getDeptName(){
	if($("deptId") && $("deptId").value.trim() && $("deptId").value != "0" && $("deptId").value != "ALL_DEPT"){
		bindDesc([{cntrlId:"deptId", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
	}else if($("deptId") && ($("deptId").value == "0" || $("deptId").value == "ALL_DEPT")){
		$("deptId").value = "0";
		$("deptIdDesc").innerHTML = "全体部门";
	}
}

function getMaritalStatus(status){
	var str = "";
	if(status == "0"){
		str = "未婚";
	}
	if(status == "1"){
		str = "已婚";
	}
	if(status == "2"){
		str = "离异";
	}
	return str;
}
/**
 * 年龄
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffAgeFunc(seqId){
	var str = "";
	if(seqId){
		var requestURLStr = contextPath+ "/yh/subsys/oa/hr/manage/staffInfo/act/YHHrStaffInfoAct";
		var url = requestURLStr + "/getStaffAge.act";
		var rtJson = getJsonRs(url, "seqId=" + seqId);
		if (rtJson.rtState == "0") {
			var prcs = rtJson.rtData;
			str = prcs.staffAge;
		} else {
			alert(rtJson.rtMsrg);
		}
	}
	return str;
}
/**
 * 获取下拉框选项 getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * @param parentNo
 *          代码编号
 * @param optionType
 *          绑定的div
 * @param extValue
 *          要选中的值
 * @return
 */
function getSelectedCode(parentNo, extValue) {
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo=" + parentNo;
	var rtJson = getJsonRs(requestURLStr);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		if (extValue && (extValue == prc.seqId)) {
			str = prc.codeName;
			break;
		}
	}
	return str;
}

</script>
</head>
<body onload="doInit();">
<div id="tableDiv" style="display: none;">
<table class="TableBlock" width="80%" align="center" id="" >
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;基本信息</b></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">OA用户名：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="userId"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">部门：</td>
    <td class="TableData"  colspan="2"><input type="hidden" id="deptId"><span id="deptIdDesc"></span> </td>
    <td class="TableData" align="center" rowspan="6" colspan="1">
			<center><span id="showImageSpan"></span> </center>    
    </td>           
  </tr>
  <tr>
  	<td nowrap align="left" width="120" class="TableContent">编号：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="staffNo"></span> </td>  
    <td nowrap align="left" width="120" class="TableContent">工号：</td>
    <td class="TableData" width="180" colspan="2"><span id="workNo"></span> </td>          
  </tr>
  <tr>
  	<td nowrap align="left" width="120" class="TableContent">姓名：</td>
    <td class="TableData" width="180"><span id="staffName"></span> </td>  	
    <td nowrap align="left" width="120" class="TableContent">英文名：</td>
    <td class="TableData" width="180" colspan="2"><span id="staffEName"></span> </td>         
  </tr>
  <tr>
  	<td nowrap align="left" width="120" class="TableContent">身份证号：</td>
    <td class="TableData" width="180" ><span id="staffCardNo"></span> </td>  
    <td nowrap align="left" width="120" class="TableContent">出生日期：</td>
    <td class="TableData" width="180" colspan="2"><span id="staffBirth"></span>  </td>             
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">年龄：</td>
    <td class="TableData" width="180"><span id="staffAge"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">性别：</td>
    <td class="TableData" width="180" colspan="2"><span id="staffSex"></span> </td>       
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">籍贯：</td>
    <td class="TableData" width="180" ><span id="staffNativePlace"></span> </td>    
    <td nowrap align="left" width="120" class="TableContent">民族：</td>
    <td class="TableData" width="180" colspan="2"><span id="staffNationality"></span> </td>            
  </tr>  
  <tr>
    <td nowrap align="left" width="120" class="TableContent">婚姻状况：</td>
    <td class="TableData" width="180"><span id="staffMaritalStatus"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">健康状况：</td>
    <td class="TableData"  width="180" colspan="3"><span id="staffHealth"></span> </td>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">政治面貌：</td>
    <td class="TableData" width="180" ><span id="staffPoliticalStatus" ></span></td>
    <td nowrap align="left" width="120" class="TableContent">入党时间：</td>
    <td class="TableData"  width="180" colspan="3"><span id="joinPartyTime"></span> </td>
  </tr>
  <tr>
  	<td nowrap align="left" width="120" class="TableContent">户口类别：</td>
    <td class="TableData"  width="180"><span id="staffType"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">户口所在地:</td>
    <td class="TableData"  width="180" colspan="3"><span id="staffDomicilePlace"></span> </td> 
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">年休假:</td>
    <td class="TableData"  width="180" colspan="5"><span id="leaveType"></span> </td>  
  </tr>
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;职位情况及联系方式：</b> </td>
  </tr>
  <tr>
  	<td nowrap align="left" width="120" class="TableContent">工种：</td>
    <td class="TableData"  width="180"><span id="workType"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">行政级别：</td>
    <td class="TableData"  width="180"><span id="administrationLevel"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">员工类型:</td>
    <td class="TableData"><span id="staffOccupation"></span>  </td>        
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">职务：</td>
    <td class="TableData"  width="180"><span id="jobPosition"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">职称：</td>
    <td class="TableData"  width="180"><span id="presentPosition"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">入职时间：</td>
    <td class="TableData"  width="180"><span id="datesEmployed"></span> </td>                 
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">本单位工龄:</td>
    <td class="TableData"  width="180"><span id="jobAge"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">起薪时间:</td>
    <td class="TableData"  width="180"><span id="beginSalsryTime"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">在职状态:</td>
    <td class="TableData"  width="180"><span id="workStatus"></span> </td>               
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">总工龄：</td>
    <td class="TableData"  width="180"><span id="workAge"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">参加工作时间：</td>
    <td class="TableData"  width="180"><span id="jobBeginning"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">联系电话：</td>
    <td class="TableData"  width="180"><span id="staffPhone"></span> </td>                
  </tr>     
  <tr>        
    <td nowrap align="left" width="120" class="TableContent">手机号码：</td>
    <td class="TableData"  width="180" ><span id="staffMobile"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">小灵通：</td>
    <td class="TableData"  width="180"><span id="staffLittleSmart"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">MSN：</td>
    <td class="TableData"  width="180" ><span id="staffMsn"></span> </td>                   
  </tr>
<tr>
    <td nowrap align="left" width="120" class="TableContent">电子邮件：</td>
    <td class="TableData"  width="180"><span id="staffEmail"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">家庭地址：</td>
    <td class="TableData"  width="180" colspan="3"><span id="homeAddress"></span> </td>                
  </tr>  
  <tr>     
    <td nowrap align="left" width="120" class="TableContent">QQ：</td>
    <td class="TableData"  width="180"><span id="staffQq"></span> </td> 
    <td nowrap align="left" width="120" class="TableContent">其他联系方式：</td>
    <td class="TableData"  width="180" colspan="3"><span id="otherContact"></span> </td>                
  </tr>
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;教育背景：</b> </td>
  </tr>              
  <tr>
    <td nowrap align="left" width="120" class="TableContent">学历：</td>
    <td class="TableData"  width="180"><span id="staffHighestSchool"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">学位：</td>
    <td class="TableData"  width="180"><span id="staffHighestDegree"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">毕业时间：</td>
    <td class="TableData"  width="180"> <span id="graduationDate"></span>  </td>                 
  </tr>       
  <tr>
    <td nowrap align="left" width="120" class="TableContent">毕业学校：</td>
    <td class="TableData"  width="180"><span id="graduationSchool"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">专业：</td>
    <td class="TableData"  width="180"><span id="staffMajor"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">计算机水平：</td>
    <td class="TableData"  width="180"><span id="computerLevel"></span> </td>                 
  </tr>       
  <tr>
    <td nowrap align="left" width="120" class="TableContent">外语语种1：</td>
    <td class="TableData"  width="180"><span id="foreignLanguage1"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">外语语种2：</td>
    <td class="TableData"  width="180"><span id="foreignLanguage2"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">外语语种3：</td>
    <td class="TableData"  width="180"><span id="foreignLanguage3"></span> </td>                 
  </tr>       
  <tr>
    <td nowrap align="left" width="120" class="TableContent">外语水平1：</td>
    <td class="TableData"  width="180"><span id="foreignLevel1"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">外语水平2：</td>
    <td class="TableData"  width="180"><span id="foreignLevel2"></span> </td>
    <td nowrap align="left" width="120" class="TableContent">外语水平3：</td>
    <td class="TableData"  width="180"><span id="foreignLevel3"></span> </td>                 
  </tr>  
  <tr>
    <td nowrap align="left" width="120" class="TableContent">特长：</td>
    <td class="TableData"  width="180" colspan="5"><span id="staffSkills"></span> </td>             
  </tr>
  <tr>
    <td nowrap align="left" colspan="3" class="TableHeader">职务情况：</td>
    <td nowrap align="left" colspan="3" class="TableHeader">担保记录：</td> 
  </tr>
  <tr>
    <td class="TableData" colspan="3">
    	<span id="certificate"></span>
    </td>
    <td class="TableData" colspan="3">
    	<span id="surety"></span>
    </td>
  </tr>
  <tr>
  	<td nowrap class="TableHeader" colspan="3"><b>&nbsp;社保缴纳情况：</b></td>
  	<td nowrap class="TableHeader" colspan="3"><b>&nbsp;体检记录：</b></td>
  </tr>
  <tr>
    <td class="TableData" colspan="3">
    	<span id="insure"></span>
    </td>
    <td class="TableData" colspan="3">
    	<span id="bodyExamim"></span>
    </td>
  </tr>           
  <tr>
    <td nowrap align="left" colspan="6" class="TableHeader">备注：</td>
  </tr>   
  <tr>
    <td nowrap class="TableData" colspan="6">
    	<span id="remark"></span>
    </td>               
  </tr>                                    
  <tr>
  	<td nowrap  class="TableHeader" colspan="6">附件文档：</td>
  </tr>
  <tr>    
    <td nowrap align="left" class="TableData" colspan="6">
			<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span> 
	</td>
  </tr>         
  <tr>
    <td nowrap align="left" class="TableHeader" colspan="6">简历:</td>
  </tr>       
  <tr>
    <td nowrap class="TableData"  colspan="6" height="100"><span id="resume"></span> </td>
  </tr>
</table>
<br>
<center><input type="button" value="打印" class="SmallButton" onclick="window.print()"></center>
</div>
<br>
<div id="msrg" style="display: none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">该用户未建档</div>
    </td>
  </tr>
</table>
</div>

</body>
</html>