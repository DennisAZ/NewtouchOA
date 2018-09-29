<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.manage.staffLearnExperience.data.*"%> 
<%@ page  import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String userName = (String)request.getAttribute("userName");
List<YHHrStaffLearnExperience> learnList = (List<YHHrStaffLearnExperience>)request.getAttribute("onefindLearn");
YHHrStaffLearnExperience learn = learnList.get(0);

String startdate="";//开始日期
String enddate = "";//结束日期
String startDate = String.valueOf(learn.getStartDate());
if(!YHUtility.isNullorEmpty(startDate) && startDate!="null"){
	startdate =	startDate.substring(0,10);
	}
String endDate = String.valueOf(learn.getEndDate());
if(!YHUtility.isNullorEmpty(endDate) && endDate!="null"){
	enddate =	endDate.substring(0,10);
	}

%>
<title>Insert title here</title>
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
function goto(){ 
		window.location.href= contextPath + "/yh/subsys/oa/hr/manage/staffLearnExperience/act/YHNewLearnExperienceAct/findLearnExInfo.act";
	}


function showAttachment(){
	var attachmentId = "<%=YHUtility.null2Empty(learn.getAttachmentId())%>";
	var attachmentName = "<%= YHUtility.encodeSpecial(YHUtility.null2Empty(learn.getAttachmentName()))%>";
	
	if(attachmentId){
		$("returnAttId").value = attachmentId;
		$("returnAttName").value = attachmentName;
		var selfdefMenu = {
      office:["downFile","read","edit"], 
      img:["downFile","play"],  
      music:["downFile","play"],  
      video:["downFile","play"], 
      others:["downFile"]
 	 }
  	attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=learn.getSeqId()%>',selfdefMenu);
	}
}

function doInit(){
	showAttachment();
}

</script>


</head>
<body topmargin="5" class="bodycolor" onload="doInit();">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img height="17" width="17" src="<%=imgPath %>/meeting.gif"><span class="big3"> 学习经历详细信息</span><br>
    </td>
  </tr>
</tbody></table>
<br>
<table width="90%" align="center" class="TableBlock">
  <tbody><tr>
    <td nowrap="" width="120" align="left" class="TableContent">姓名：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=userName==null?"":userName %></td>
    <td nowrap="" width="120" align="left" class="TableContent">所学专业：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=learn.getMajor()==null?"":learn.getMajor() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">所获学历：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=learn.getAcademyDegree()==null?"":learn.getAcademyDegree() %></td>
    <td nowrap="" width="120" align="left" class="TableContent">所获学位：</td>
    <td width="180" align="left" class="TableData"><%=learn.getDegree()==null?"":learn.getDegree() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">曾任班干：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=learn.getPosition()==null?"":learn.getPosition() %></td>
    <td nowrap="" width="120" align="left" class="TableContent">证明人：</td>
    <td width="180" align="left" class="TableData"><%=learn.getWitness()==null?"":learn.getWitness() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">所在院校：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=learn.getSchool()==null?"":learn.getSchool() %></td>
    <td nowrap="" width="120" align="left" class="TableContent">院校所在地：</td>
    <td width="180" align="left" class="TableData"><%=learn.getSchoolAddress()==null?"":learn.getSchoolAddress() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">开始日期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=startdate==null?"":startdate %></td>
    <td nowrap="" width="120" align="left" class="TableContent">结束日期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=enddate==null?"":enddate %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">获奖情况：</td>
    <td nowrap="" align="left" colspan="3" class="TableData"><%=learn.getAwarding()==null?"":learn.getAwarding() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">所获证书：</td>
    <td nowrap="" align="left" colspan="3" class="TableData"><%=learn.getCertificates()==null?"":learn.getCertificates() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">备注：</td>
    <td align="left" colspan="3" class="TableData"><%=learn.getRemark()==null?"":learn.getRemark() %></td>
  </tr>
    <%
  	if(!YHUtility.isNullorEmpty(learn.getAttachmentId())){
  %>
  
  <tr>
    <td nowrap="" align="left" width="120" class="TableContent" >附件文档：</td>
     <td nowrap align="left" class="TableData" colspan="3">
			<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
    </td>
  </tr>
  <%
  	}
  
  %>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">登记日期：</td>
    <td nowrap="" align="left" colspan="3" class="TableData"><%=learn.getAddTime()==null?"":learn.getAddTime() %></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
       <input type="button" value="返回" class="BigButton" onClick="javascript:goto(); return false;">
    </td>
  </tr>
</tbody>
</table>
</html>