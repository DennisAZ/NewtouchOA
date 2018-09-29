<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
	String filePathStr = YHSysProps.getAttachPath().replace("\\","/") + "/" + YHHrRecruitPoolAct.attachmentFolder + "/";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="yh.subsys.oa.hr.recruit.hrPool.act.YHHrRecruitPoolAct"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人才档案详细信息
</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/recruit/hrPool/act/YHHrRecruitPoolAct/getHrPoolnDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	//alert(rsText);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
        if (data.startWorking == '0') {
          $("startWorking").innerHTML = "1周以内";
        } else if (data.startWorking == '1') {
          $("startWorking").innerHTML = "1个月内";
        } else if (data.startWorking == '2') {
          $("startWorking").innerHTML = "1~3个月";
        }else if (data.startWorking == '3') {
          $("startWorking").innerHTML = "3个月后";
        }else if (data.startWorking == '4') {
          $("startWorking").innerHTML = "随时到岗";
        } else {
          $("startWorking").innerHTML = "";
        }
		//alert(data.employeeMajor);
		if(data.addTime){
			$("addTime1").innerHTML = data.addTime; 
		}
		if(data.employeeSex == "0"){
			$("employeeSex").innerHTML = "男";
		}else{
			$("employeeSex").innerHTML = "女";
		}
		if(data.employeeNativePlace){
			$("employeeNativePlace").innerHTML = selectCodeById(data.employeeNativePlace);
		}
		if(data.employeePoliticalStatus){
			$("employeePoliticalStatus").innerHTML = selectCodeById(data.employeePoliticalStatus);
		}
		if(data.employeeHighestSchool){
			$("employeeHighestSchool").innerHTML = selectCodeById(data.employeeHighestSchool);
		}
		if(data.employeeHighestDegree){
			$("employeeHighestDegree").innerHTML = selectCodeById(data.employeeHighestDegree);
		}
		if(data.employeeMajor){
			$("employeeMajor").innerHTML = selectCodeById(data.employeeMajor);
		}
		$("employeeMaritalStatus").innerHTML = getMaritalStatus(data.employeeMaritalStatus);
		if(data.attachmentId){
			$("attachmentTr").show();
			$("returnAttId").value = data.attachmentId;
			$("returnAttName").value = data.attachmentName;
			var selfdefMenu = {
				office:["downFile","dump","read"], 
        img:["downFile","play","dump"],  
        music:["downFile","play"],  
        video:["downFile","play"], 
        others:["downFile"]
      }
      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
		}
		var attachmentId = data.attachmentId;
		var attachmentName = data.attachmentName;
		showImagesList(attachmentId,attachmentName);

	}else{
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
		      	var imgStr1 = "<img src=<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=" + encodeURIComponent(attachmentNameSubstr) + "&attachmentId=" + attachmentIdArry[i] + "&module=<%=YHHrRecruitPoolAct.attachmentFolder%> border='0'  width='160' height='100' title=\"文件名：" + attachmentNameSubstr + "\" ></img>";
		      	imgDownStr += "<a href='<%=contextPath%>/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?attachmentId=" + attachmentIdArry[i] + "&attachmentName=" + encodeURIComponent(attachmentNameSubstr) + "&module=<%=YHHrRecruitPoolAct.attachmentFolder%>' >" +imgStr1 + "</a>&nbsp;";
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
/**
 * 获取代码名称
 * @param seqId
 * @return
 */
function selectCodeById(seqId) {
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId=" + seqId;
	var rtJson = getJsonRs(requestURLStr);
	//alert(rsText);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	if(prcs.codeName){
		str = prcs.codeName;
	}
	return str;
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
</script>
</head>
<body onload="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 人才档案详细信息</span><br>
    </td>
  </tr>
</table>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">计划名称：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"> <span id="planName"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">应聘时间：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="addTime"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">应聘者姓名：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeeName"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">性别：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeeSex"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">出生日期：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="employeeBirth"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">籍贯：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeeNativePlace"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">现居住城市：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="residencePlace"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">户口所在地：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="employeeDomicilePlace"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">民族：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeeNationality"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">婚姻状况：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="employeeMaritalStatus"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">健康状况：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeeHealth"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">政治面貌：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="employeePoliticalStatus"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">联系电话：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeePhone"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">E_MAIL：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeeEmail"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">参加工作时间：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="jobBeginning"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">期望从事职业：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="jobIntension"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">应聘岗位：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="positionName"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">期望工作性质：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="jobCategory"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">期望从事行业：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="jobIndustry"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">期望工作城市：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="workCity"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">期望薪水：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="expectedSalary"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">到岗时间：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="startWorking"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">学历：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="employeeHighestSchool"></span></td>
    <td nowrap nowrap align="left" width="120" class="TableContent">学位：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="employeeHighestDegree"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">毕业时间：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="graduationDate"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">毕业学校：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="graduationSchool"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">专业：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="employeeMajor"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">计算机水平：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="computerLevel"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">外语语种1：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="foreignLanguage1"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">外语水平1：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="foreignLevel1"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">外语语种2：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="foreignLanguage2"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">外语水平2：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="foreignLevel2"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">外语语种3：</td>
    <td nowrap align="left" class="TableData" width="180"><span id="foreignLanguage3"></span> </td>
    <td nowrap nowrap align="left" width="120" class="TableContent">外语水平3：</td>
    <td nowrap nowrap align="left" class="TableData" width="180"><span id="foreignLevel3"></span> </td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">职业技能：</td>
    <td nowrap align="left" class="TableData" colspan="3"><span id="careerSkills"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">工作经验：</td>
    <td nowrap nowrap align="left" class="TableData" colspan="3"><span id="workExperience"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">项目经验：</td>
    <td nowrap nowrap align="left" class="TableData" colspan="3"><span id="projectExperience"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">特长：</td>
    <td nowrap align="left" class="TableData" colspan="3"><span id="employeeSkills"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">备注：</td>
    <td nowrap align="left" class="TableData" colspan="3"><span id="remark"></span></td>
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
    <td nowrap nowrap align="left" class="TableData" colspan="3"><span id="addTime1"></span></td>
  </tr>
  <tr>
    <td nowrap nowrap align="left" width="120" class="TableContent">简历：</td>
    <td nowrap align="left" class="TableData" colspan="3"><span id="resume"></span></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>


</body>
</html>