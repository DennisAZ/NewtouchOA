<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.manage.staffLaborSkills.data.*"%> 
<%@ page  import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String userName = (String)request.getAttribute("userName");
List<YHHrStaffLaborSkills> laborSkills = (List<YHHrStaffLaborSkills>)request.getAttribute("onefindLaborSkill");
YHHrStaffLaborSkills laborSkill = laborSkills.get(0);

 String issuedate = "";//发证日期
 String expiredate = "";//到期日期
 String issueDate = String.valueOf(laborSkill.getIssueDate());
 if(!YHUtility.isNullorEmpty(issueDate) && issueDate!="null"){
	 issuedate =	issueDate.substring(0,10);
	}
 String expireDate = String.valueOf(laborSkill.getExpireDate());
 if(!YHUtility.isNullorEmpty(expireDate) && expireDate!="null"){
	 expiredate =	expireDate.substring(0,10);
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
		window.location.href= contextPath + "/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/findLaborSkillsInfo.act";
	}


function showAttachment(){
	var attachmentId = "<%=YHUtility.null2Empty(laborSkill.getAttachmentId())%>";
	var attachmentName = "<%= YHUtility.encodeSpecial(YHUtility.null2Empty(laborSkill.getAttachmentName()))%>";
	
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
  	attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=laborSkill.getSeqId()%>',selfdefMenu);
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
    <td class="Big"><img height="17" width="17" src="<%=imgPath %>/meeting.gif"><span class="big3"> 劳动技能详细信息</span><br>
    </td>
  </tr>
</tbody></table>
<br>
<table width="90%" align="center" class="TableBlock">
  <tbody><tr>
    <td nowrap="" width="120" align="left" class="TableContent">姓名：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=userName==null?"":userName %></td>
    <td nowrap="" width="120" align="left" class="TableContent">技能名称：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=laborSkill.getAbilityName()==null?"":laborSkill.getAbilityName() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">特种作业：</td>
    <% if(laborSkill.getSpecialWork().equalsIgnoreCase("1")){ %>
    <td nowrap="" width="180" align="left" class="TableData">是</td>
    <%}else{ %>
    <td nowrap="" width="180" align="left" class="TableData">否</td>
    <%} %>
    <td nowrap="" width="120" align="left" class="TableContent">级别：</td>
    <td width="180" align="left" class="TableData"><%=laborSkill.getSkillsLevel()==null?"":laborSkill.getSkillsLevel() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">技能证：</td>
    <%if(laborSkill.getSkillsCertificate().equalsIgnoreCase("1")){ %>
    <td nowrap="" width="180" align="left" class="TableData">是</td>
   <%}else{ %>
   <td nowrap="" width="180" align="left" class="TableData">否</td>
   <%} %>
    <td nowrap="" width="120" align="left" class="TableContent">发证日期：</td>
    <td width="180" align="left" class="TableData"><%=issuedate==null?"":issuedate %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">有效期：</td>
    <td nowrap="" width="180" align="left" class="TableData"><%=laborSkill.getExpires()==null?"":laborSkill.getExpires() %>（年）</td>
    <td nowrap="" width="120" align="left" class="TableContent">到期日期：</td>
    <td width="180" align="left" class="TableData"><%=expiredate==null?"":expiredate %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">发证机关/单位：</td>
    <td nowrap="" align="left" colspan="3" class="TableData"><%=laborSkill.getIssuingAuthority()==null?"":laborSkill.getIssuingAuthority() %></td>
  </tr>
  <tr>
    <td nowrap="" width="120" align="left" class="TableContent">备注：</td>
    <td align="left" colspan="3" class="TableData"><%=laborSkill.getRemark()==null?"":laborSkill.getRemark() %></td>
  </tr>
    <%
  	if(!YHUtility.isNullorEmpty(laborSkill.getAttachmentId())){
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
    <td nowrap="" align="left" colspan="3" class="TableData"><%=laborSkill.getAddTime()==null?"":laborSkill.getAddTime() %></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="返回" class="BigButton" onClick="javascript:goto(); return false;">
    </td>
  </tr>
</tbody></table>
</html>