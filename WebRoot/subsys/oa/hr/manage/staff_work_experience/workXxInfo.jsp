<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.manage.staffWorkExperience.data.*"%> 
<%@ page  import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String userName = (String)request.getAttribute("userName");
List<YHHrStaffWorkExperience> workList = (List<YHHrStaffWorkExperience>)request.getAttribute("onefindWork");
YHHrStaffWorkExperience work = workList.get(0);

String startdate="";//开始日期
String enddate = "";//结束日期
String startDate = String.valueOf(work.getStartDate());
if(!YHUtility.isNullorEmpty(startDate) && startDate!="null"){
	startdate =	startDate.substring(0,10);
	}
String endDate = String.valueOf(work.getEndDate());
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
		window.location.href= contextPath + "/yh/subsys/oa/hr/manage/staffWorkExperience/act/YHNewWorkExperienceAct/findWorkExInfo.act";
	}


function showAttachment(){
	var attachmentId = "<%=YHUtility.null2Empty(work.getAttachmentId())%>";
	var attachmentName = "<%= YHUtility.encodeSpecial(YHUtility.null2Empty(work.getAttachmentName()))%>";
	
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
  	attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=work.getSeqId()%>',selfdefMenu);
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
    <td class="Big"><img height="17" width="17" src="<%=imgPath %>/meeting.gif"><span class="big3"> 工作经历详细信息</span><br>
    </td>
  </tr>
</tbody></table>
<br>
<table width="90%" align="center" class="TableBlock">
  <tbody><tr>
    <td nowrap="" width="120" align="left" class="TableContent">姓名：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=userName==null?"":userName %></td>
    <td nowrap="" width="120" align="left" class="TableContent">工作单位：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=work.getWorkUnit()==null?"":work.getWorkUnit() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">开始日期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=startdate==null?"":startdate %></td>
    <td nowrap="" width="120" align="left" class="TableContent">结束日期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=enddate==null?"":enddate %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">行业类别：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=work.getMobile()==null?"":work.getMobile() %></td>
    <td nowrap="" width="120" align="left" class="TableContent">所在部门：</td>
    <td width="180" align="left" class="TableData"><%=work.getWorkBranch()==null?"":work.getWorkBranch() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">担任职务：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=work.getPostOfJob()==null?"":work.getPostOfJob() %></td>
    <td nowrap="" width="120" align="left" class="TableContent">证明人：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=work.getWitness()==null?"":work.getWitness() %></td>      
  </tr>
    <tr>
    <td nowrap="" width="120" align="left" class="TableContent">工作内容：</td>
    <td align="left" colspan="3" class="TableData"><%=work.getWorkContent()==null?"":work.getWorkContent() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">主要业绩：</td>
    <td nowrap="" align="left" colspan="3" class="TableData"><%=work.getKeyPerformance()==null?"":work.getKeyPerformance() %></td>
  </tr>

   <%
  	if(!YHUtility.isNullorEmpty(work.getAttachmentId())){
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
    <td nowrap="" width="120" align="left" class="TableContent">离职原因：</td>
    <td align="left" colspan="3" class="TableData"><%=work.getReasonForLeaving()==null?"":work.getReasonForLeaving() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">备注：</td>
    <td align="left" colspan="3" class="TableData"><%=work.getRemark()==null?"":work.getRemark() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">登记日期：</td>
    <td nowrap="" align="left" colspan="3" class="TableData"><%=work.getAddTime()==null?"":work.getAddTime() %></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
    <input type="button" value="返回" class="BigButton" onClick="javascript:goto(); return false;">
    </td>
  </tr>
</tbody></table>
</html>