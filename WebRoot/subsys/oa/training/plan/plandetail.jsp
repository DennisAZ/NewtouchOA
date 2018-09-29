<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
	String filePathStr = YHSysProps.getAttachPath().replace("\\","/") + "/" + YHHrTrainingPlanAct.attachmentFolder + "/";

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.subsys.oa.training.act.YHHrTrainingPlanAct"%><html>
<head>
<title>会议详细信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/util.js"></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/training/act/YHTrainingApprovalAct/getPlanDetail.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    getOrgDescFunc();
    $("tBcws").innerHTML = insertKiloSplit(data.tBcws,2)
    $('tChannel').innerHTML = tChannelFunc(data.tChannel);
    $('tCourseTypes').innerHTML = tCourseTypesFunc(data.tCourseTypes);
    
		$('tPlanNo').innerHTML = data.tPlanNo;
		$('tPlanName').innerHTML = data.tPlanName;
		$('tJoinNum').innerHTML = data.tJoinNum;
		$('tAddress').innerHTML = data.tAddress;
		$('tInstitutionName').innerHTML = data.tInstitutionName;
		$('tInstitutionContact').innerHTML = data.tInstitutionContact;
		$('tInstitutionInfo').innerHTML = data.tInstitutionInfo;
		$('tInstituContactInfo').innerHTML = data.tInstituContactInfo;
		$('tCourseName').innerHTML = data.tCourseName;
		$('courseHours').innerHTML = data.courseHours;
		if(data.courseStartDate){
			$('courseStartDate').innerHTML =  data.courseStartDate.substr(0,10);
		} 
		if(data.courseEndDate){
			$('courseEndDate').innerHTML =  data.courseEndDate.substr(0,10);
		} 
		$('tRequires').innerHTML = data.tRequires;
		$('tDescription').innerHTML = data.tDescription;
		$('tContent').innerHTML = data.tContent;
		$('remark').innerHTML = data.remark;
    if(data.attachmentId){
			$("attachmentTr").show();
      $("returnAttId").value = data.attachmentId;
      $("returnAttName").value = data.attachmentName;
      var selfdefMenu = {
          office:["downFile","dump","read","edit"], 
          img:["downFile","play","dump"],  
          music:["downFile","play"],  
          video:["downFile","play"], 
          others:["downFile"]
      }
      attachMenuSelfUtil("attr","training",$('returnAttName').value ,$('returnAttId').value, '','',${param.seqId},selfdefMenu);
    }
    var attachmentId = data.attachmentId;
    var attachmentName = data.attachmentName;
    showImagesList(attachmentId,attachmentName);
  }else{
    //WarningMsrg('未找到相应记录！', 'msrg');
    alert(rtJson.rtMsrg); 
  }
}

function showImagesList(attachmentId,attachmentName){
	//alert("attachmentId>>"+attachmentId +"  attachmentName>"+attachmentName);
	var pictureStr = ["png","gif","jpg","bmp","PNG","GIF","JPG","BMP"]; 
  if(attachmentId){
  	var picType = "2";//不是图片 
    var attachmentIdArry = attachmentId.split(",");
    var attachmentNameArry = attachmentName.split("*");
    var imgDownStr = "";
    for(var i = 0;i<attachmentIdArry.length; i++){
   	  if(attachmentIdArry[i]){
   	  	var attachmentIdDate = attachmentId.substr(0,4);  //1109
   	  	var filePathStr = "<%=filePathStr%>" + attachmentIdDate;
   	  	var attachmentIdSubstr = attachmentIdArry[i].substr(5);
   	  	var attachmentNameSubstr = attachmentNameArry[i];
	   		var fileName = attachmentNameArry[i].lastIndexOf("\."); 
	      fileName = attachmentNameArry[i].substring(fileName+1,attachmentName.length); 
	      for(var j=0; j<pictureStr.length; j++){
		      if(pictureStr[j] == fileName){
		      	picType = "1"
		      	var imgStr1 = "<img src=<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=" + encodeURIComponent(attachmentNameSubstr) + "&attachmentId=" + attachmentIdArry[i] + "&module=training border='0'  width='160' height='100' title=\"文件名：" + attachmentNameSubstr + "\" ></img>";
		      	imgDownStr += "<a href='<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?attachmentId=" + attachmentIdArry[i] + "&attachmentName=" + encodeURIComponent(attachmentNameSubstr) + "&module=training' >" +imgStr1 + "</a>&nbsp;";
			    }
	      }
      }
    }
    if(picType == "1"){
			$("imgTr").show();
			$("showImageSpan").innerHTML = imgDownStr;
    }
  }
}

function tCourseTypesFunc(classCode){
  var classNo = "T_COURSE_TYPE";
  var urls = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId="+classCode;
  var rtJsons = getJsonRs(urls);
  var prc = rtJsons.rtData;
  if(rtJsons.rtState == '0'){
    if(prc.codeName){
      return prc.codeName;
    }
    return "";
  }else{
    alert(rtJson.rtMsrg);
  }
}

function getOrgDescFunc(){
  if($("chargePerson") && $("chargePerson").value.trim()){
    bindDesc([{cntrlId:"chargePerson", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
  if($("sponsoringDepartment") && $("sponsoringDepartment").value.trim() && $("sponsoringDepartment").value != "0" && $("sponsoringDepartment").value != "ALL_DEPT"){
    bindDesc([{cntrlId:"sponsoringDepartment", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }
  
  if($("tJoinDept") && $("tJoinDept").value.trim() && $("tJoinDept").value != "0" && $("tJoinDept").value != "ALL_DEPT"){
    bindDesc([{cntrlId:"tJoinDept", dsDef:"DEPARTMENT,SEQ_ID,DEPT_NAME"}]);
  }else if($("tJoinDept") && ($("tJoinDept").value == "0" || $("tJoinDept").value == "ALL_DEPT")){
    $("tJoinDept").value = "0";
    $("tJoinDeptDesc").innerHTML = "全体部门";
  }
  if($("tJoinPerson") && $("tJoinPerson").value.trim()){
    bindDesc([{cntrlId:"tJoinPerson", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="17" height="17"><span class="big3"> 培训计划详细信息</span><br>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训计划编号：</td>
      <td nowrap nowrap align="left" class="TableData" width="180" width="180"><div id="tPlanNo"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训计划名称：</td>
      <td nowrap nowrap align="left" class="TableData" width="180"><div id="tPlanName"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训渠道：</td>
      <td nowrap nowrap align="left" class="TableData" width="180"><div id="tChannel"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训形式：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="tCourseTypes"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">主办部门：</td>
      <td nowrap nowrap align="left" class="TableData" width="180"><div id="sponsoringDepartmentDesc"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">负责人：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="chargePersonDesc"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">计划参与培训人数：</td>
      <td nowrap nowrap align="left" class="TableData" width="180"><div id="tJoinNum"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训地点：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="tAddress"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训机构名称：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="tInstitutionName"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训机构联系人：</td>
      <td nowrap nowrap align="left" class="TableData" width="180"><div id="tInstitutionContact"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训机构相关信息：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="tInstitutionInfo"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训机构联系人相关信息：</td>
      <td nowrap nowrap align="left" class="TableData" width="180"><div id="tInstituContactInfo"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训课程名称：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="tCourseName"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">总课时：</td>
      <td nowrap nowrap align="left" class="TableData" width="180"><div id="courseHours"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">开课日期：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="courseStartDate"></div></td>
      <td nowrap nowrap align="left" width="120" class="TableContent">结课日期：</td>
      <td nowrap align="left" class="TableData" width="180"><div id="courseEndDate"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训预算：</td>
      <td nowrap nowrap align="left" colspan=3 class="TableData" width="180"><div id="tBcws"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">参与培训部门：</td>
      <td nowrap align="left" class="TableData" colspan="3"><div id="tJoinDeptDesc"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">参与培训人员：</td>
      <td nowrap nowrap align="left" class="TableData" colspan="3"><div id="tJoinPersonDesc"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训要求：</td>
      <td nowrap align="left" class="TableData" colspan="3"><div id="tRequires"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训说明：</td>
      <td nowrap align="left" class="TableData" colspan="3"><div id="tDescription"></div></td>
  </tr>
    <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">培训内容：</td>
      <td nowrap align="left" class="TableData" colspan="3"><div id="tContent"></div></td>
  </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">备注：</td>
      <td nowrap nowrap align="left" class="TableData" colspan="3"><div id="remark"></div></td>
  </tr>

  <tr id="attachmentTr" style="display: none" >
    <td nowrap align="left" width="120" class="TableContent">附件文档：</td>
    <td nowrap align="left" class="TableData" colspan="3">
    	<input type = "hidden" id="returnAttId" name="returnAttId"></input>
     	<input type = "hidden" id="returnAttName" name="returnAttName"></input>
      <span id="attr"></span>
    </td>
  </tr>

    <tr id="imgTr" style="display: none;">
     	<td nowrap nowrap align="left" width="120" class="TableContent"> 附件图片: <br><br>	</td>
		 	<td  class="TableData" colspan="3">	
		 	<span id="showImageSpan"></span></td>
   </tr>
  <tr>
      <td nowrap nowrap align="left" width="120" class="TableContent">登记日期：</td>
      <td nowrap nowrap align="left" class="TableData" colspan="3"></td>
  </tr>
  <tr align="center" class="TableControl">
      <td colspan="4">
      <input type = "hidden" id="sponsoringDepartment" name="sponsoringDepartment"></input>
      <input type = "hidden" id="chargePerson" name="chargePerson"></input>
      <input type = "hidden" id="tJoinDept" name="tJoinDept"></input>
      <input type = "hidden" id="tJoinPerson" name="tJoinPerson"></input>
        <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
      </td>
  </tr>
</table>
<div id="msrg">
</div>
</body>
</html>