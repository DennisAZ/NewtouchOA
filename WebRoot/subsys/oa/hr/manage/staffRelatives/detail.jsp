<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>社会关系详细信息</title>
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
	var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/relatives/act/YHHrStaffRelativesAct/getRelativesDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		bindJson2Cntrl(rtJson.rtData);
		$('staffName').innerHTML = staffNameFunc(data.staffName);
		$('member').innerHTML = data.member;
		$('relationship').innerHTML = getRelativesItem(data.relationship);
    if(data.birthday){
        $("birthday").innerHTML = data.birthday.substr(0,10);
      }		
		$("politics").innerHTML = getPoliticsItem(data.politics);
		$("jobOccupation").innerHTML = data.jobOccupation;
		$("workUnit").innerHTML = data.workUnit;
		$("officeTel").innerHTML = data.officeTel;
		$("personalTel").innerHTML = data.personalTel;
		$("homeTel").innerHTML = data.homeTel;
		$("unitAddress").innerHTML = data.unitAddress;
		$("homeAddress").innerHTML = data.homeAddress;
		$("remark").innerHTML = data.remark;
		$("addTime").innerHTML = data.addTime;

		if(data.attachmentId){
			$("returnAttId").value = data.attachmentId;
			$("returnAttName").value = data.attachmentName;
			var selfdefMenu = {
          office:["downFile","read","edit"], 
          img:["downFile","play"],  
          music:["downFile","play"],  
          video:["downFile","play"], 
          others:["downFile"]
      }
      attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=seqId%>',selfdefMenu);
		}else{
			$('attr').innerHTML = "无附件";
		}
	}else{
		alert(rtJson.rtMsrg);
	}
}

/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(cellData){
  var url = contextPath + "/yh/subsys/oa/hr/manage/relatives/act/YHHrStaffRelativesAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function getRelativesItem(seqId){
  var urls = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId="+seqId;
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

function getPoliticsItem(seqId){
	  var urls = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId="+seqId;
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
</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 社会关系详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">姓名：</td>
    <td align="left" class="TableData" width="180"><div id="staffName"></div> </td>
    <td align="left" width="120" class="TableContent">成员姓名：</td>
    <td align="left" class="TableData" width="180"><div id="member"></div> </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">与本人关系：</td>
    <td align="left" class="TableData" width="180"><div id="relationship"></div> </td>
    <td align="left" width="120" class="TableContent">出生日期：</td>
    <td align="left" class="TableData Content" width="180"><div id="birthday"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">政治面貌：</td>
    <td align="left" class="TableData" width="180"><div id="politics"></div></td>
    
    <td align="left" width="120" class="TableContent">职业：</td>
    <td align="left" class="TableData Content" width="180"><div id="jobOccupation"></div></td>
  </tr>
  
  <tr>
    <td align="left" width="120" class="TableContent">工作单位：</td>
    <td align="left" class="TableData Content" width="180"><div id=workUnit></div></td>
    <td align="left" width="120" class="TableContent">联系电话（单位）：</td>
    <td align="left" class="TableData" width="180"><div id="officeTel"></div></td>
  </tr>
  
  <tr>
    <td align="left" width="120" class="TableContent">联系电话（个人）：</td>
    <td align="left" class="TableData" width="180"><div id="personalTel"></div></td>
    <td align="left" width="120" class="TableContent">联系电话（家庭）：</td>
    <td align="left" class="TableData" width="180"><div id="homeTel"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">单位地址：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="unitAddress"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">家庭住址：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="homeAddress"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">备注：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="remark"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">附件文档：</td>
    <td align="left" class="TableData" colspan="3">
			<input type = "hidden" id="returnAttId" name="returnAttId"></input>
			<input type = "hidden" id="returnAttName" name="returnAttName"></input>
			<span id="attr"></span>
    </td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">登记时间：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="addTime"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>