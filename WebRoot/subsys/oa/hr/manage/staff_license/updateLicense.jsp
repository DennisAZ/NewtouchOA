<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="yh.subsys.oa.hr.manage.staff_license.data.*"%> 
<%@ page  import="java.util.List"%>
<html>
<head>
<%
String userName = (String)request.getAttribute("userName");
List<YHHrStaffLicense> licenseList = (List<YHHrStaffLicense>)request.getAttribute("licensesInfoList");
YHHrStaffLicense license = licenseList.get(0);
System.out.print(license.getAttachmentId() + "====" + license.getAttachmentName());
String isTime = "";
if(license.getExpirationPeriod()!=null && license.getExpirationPeriod()!=""){
	isTime = license.getExpirationPeriod();
}
String licenseType="";//证照类型
String licenseState ="";//证照状态
if(!YHUtility.isNullorEmpty(license.getLicenseType())){//证照类型
	licenseType = license.getLicenseType();
}
if(!YHUtility.isNullorEmpty(license.getStatus())){//证照类型
	licenseState = license.getStatus();
}
%>
<title>新建证照信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href ="<%=cssPath %>/style.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/notify/js/openWin.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/setting/codeJs/hrCodeJs.js"></script>
<script type="text/javascript">
var isTime ="<%=isTime%>";
var licenseType1 ="<%=licenseType%>";//证照类型
var licenseSate1="<%=licenseState%>";
/*****************附件上传开始*****************************/
var upload_limit=1,limit_type=limitUploadFiles;
var isUploadBackFun = false;

/*****************附件上传结束*****************************/


function doOnload(){
	
	licenseType();//查询码表相关信息(证照类型)
	licenseState();//证照状态
	//时间
	  var parameters = {
	      inputId:'GET_LICENSE_DATE',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date1'
	  };
	  new Calendar(parameters);
	  var parameters = {
	      inputId:'EFFECTIVE_DATE',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date2'
	  };
	  new Calendar(parameters);
	  var parameters = {
		      inputId:'EXPIRE_DATE',
		      property:{isHaveTime:false}
		      ,bindToBtn:'date3'
		  };
		  new Calendar(parameters);
		  var parameters = {
			      inputId:'REMIND_TIME',
			      property:{isHaveTime:true}
			      ,bindToBtn:'date4'
			  };
		  new Calendar(parameters);
    if(isTime){ //判断是否有限制  
       if(isTime == '1'){
    	    whichEl =document.getElementById("menu");
    	    document.form1.EXPIRATION_PERIOD[0].checked = true 
    	    whichEl.style.display = '';
      }
    }
    showAttachment();
      //$("LICENSE_TYPE").value=licenseType1;//给证照类型赋值
    $("STATUS").value=licenseSate1;//给证照状态赋值
    $("LICENSE_TYPE").value = licenseType1;

   // 证照类型
		var licenseTyp = $("LICENSE_TYPE").value;
		$("licenseType").value = licenseTyp;
	
	//证照状态
		var licenseState1 = $("STATUS").value;
		$("licenseState").value = licenseState1;
	
    
}
//证照类型
function licenseType(){
	var codeObject = getChildCode("HR_STAFF_LICENSE1");
	var selectObj = $("LICENSE_TYPE");
  for(var i=0; i<codeObject.length; i++){
    var code = codeObject[i];
    var codeId = code.seqId;
    //alert(codeId);
    var codeNo = code.codeNo;
    var codeDesc = code.codeName;
    var myOption = document.createElement("option");
    
    myOption.value = codeId; //codeId是编号
   
    myOption.text = codeDesc;
   
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
    //selectObj.options.add(new Option(codeObject[i], codeObject[i]));
   // $("LICENSE_TYPE").value = myOption.text;
	}
	
}
//证照状态
function licenseState(){ 
  var codeObject = getChildCode("HR_STAFF_LICENSE2");
	var selectObj = $("STATUS");
	
	for(var i=0; i<codeObject.length; i++){
    var code = codeObject[i];
    var codeId = code.seqId;
    var codeNo = code.codeNo;
    var codeDesc = code.codeName;
    
    var myOption = document.createElement("option");
    myOption.value = codeId;
    myOption.text = codeDesc;
  
    selectObj.options.add(myOption, selectObj.options ? selectObj.options.length : 0);
	}
}
function expandIt() 
{
  whichEl =document.getElementById("menu");
  if (document.form1.EXPIRATION_PERIOD[0].checked == true) 
  {
   whichEl.style.display = '';
  }
  if (document.form1.EXPIRATION_PERIOD[1].checked == true)
  { 
  	whichEl.style.display = 'none';  
  }
}

/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
 function CheckForm(){
	 var seqid =  $("seqid").value;
	 var userName =  $("userName").value;
	 var licenseType =  $("LICENSE_TYPE").value;
	 var licenseNo =  $("LICENSE_NO").value;
	 var licenseName =  $("LICENSE_NAME").value;
	 var notifiedBody =  $("NOTIFIED_BODY").value;
	 
	//alert(licenseNo + licenseType);
		 if(userName.replaceAll(" ","") == "" || userName == "null"){
		      alert("单位员工不能为空");
		      return false;
	 }
	if(licenseType.replaceAll(" ","") == "" || licenseType == "null"){
		      alert("证照类型不能为空");
		      return false;
	 }  
		if(licenseNo.replaceAll(" ","") == "" || licenseNo == "null"){
		      alert("证照编号不能为空"); 
		      return false;
	 }
		if(licenseName.replaceAll(" ","") == "" || licenseName == "null"){
		      alert("证照名称不能为空"); 
		      return false;
		}
		if(notifiedBody.replaceAll(" ","") == "" || licenseName == "null"){
		      alert("发证机构不能为空"); 
		      return false;
		}
		document.form1.action = contextPath +"/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/updateLicenseInfo.act?seqid="+seqid;
		document.form1.submit();
		return true;
 }
 //证照类型
 function licenseTp(){
		var licenseTyp = $("LICENSE_TYPE").value;
		$("licenseType").value = licenseTyp;
	}	
	//证照状态
 function getState(){
		var licenseState1 = $("STATUS").value;
		$("licenseState").value = licenseState1;
	}


 function showAttachment(){
		var attachmentId = "<%=YHUtility.null2Empty(license.getAttachmentId())%>";
		var attachmentName = "<%= YHUtility.encodeSpecial(YHUtility.null2Empty(license.getAttachmentName()))%>";
		
		if(attachmentId){
			$("returnAttId").value = attachmentId;
			$("returnAttName").value = attachmentName;
			var selfdefMenu = {
			          office:["downFile","dump","read","edit","deleteFile"], 
			          img:["downFile","dump","play","deleteFile"],  
			          music:["downFile","play","deleteFile"],  
			          video:["downFile","play","deleteFile"], 
			          others:["downFile","deleteFile"]
	 	 }
	  	attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=license.getSeqId()%>',selfdefMenu);
		}
	}

//删除附件
 function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
   var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staff_license/act/YHNewLicenseInfoAct/delFloatFile.act?delAttachId=" + attachId + "&seqId=<%=license.getSeqId()%>";
   //var json = getJsonRs(url);
   var json=getJsonRs(url);
   if(json.rtState =='1'){
     alert(json.rtMsrg);
     return false;
   }else{
     prcsJson=json.rtData;
     var updateFlag=prcsJson.updateFlag;
     if(updateFlag == "1"){
       return true;
     }else{
       return false;
     }
   }
 }
</script>

</head>
<body class="bodycolor" topmargin="5" onLoad="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 新建证照信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/addStaffincentiveInfo.act"  method="post" name="form1" id="form1" onsubmit="">
<!--
<form action="#" id="form1" method="post" name="form1">
  -->
<table class="TableBlock" width="80%" align="center">
   <tr>
      <td nowrap class="TableData">单位员工：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" name="userName" id="userName" size="12" class="BigStatic" readonly value="<%=userName==null?"":userName %>">&nbsp;
        <INPUT type="hidden" name="userId" id="userId" value="<%=license.getStaffName()==null?"":license.getStaffName() %>">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName'],null,null,1)">选择</a>  
        <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
      </td>
      <td nowrap class="TableData">证照类型：<font style="color:red">*</font></td>
      <td class="TableData" >
        <select name="LICENSE_TYPE" id="LICENSE_TYPE" onchange="licenseTp();" title="证照类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">证照类型</option>
        </select>
        <input type="hidden" id="licenseType" name="licenseType">
      </td> 
    </tr>
    <tr>
    	 <td nowrap class="TableData">证照编号：<font style="color:red">*</font></td>
      <td class="TableData">
        <INPUT type="text" name="LICENSE_NO" id="LICENSE_NO" class=BigInput size="15" value="<%=license.getLicenseNo() %>">
        <input type="hidden" name="seqid" id = "seqid" value="<%=license.getSeqId() %>"></input>
      </td>
    	<td nowrap class="TableData">证照名称：<font style="color:red">*</font></td>
      <td class="TableData">
       <INPUT type="text" name="LICENSE_NAME" id="LICENSE_NAME" class=BigInput size="15" value="<%=license.getLicenseName() %>">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">取证日期：</td>
      <td class="TableData">
       <input type="text" name="GET_LICENSE_DATE" id="GET_LICENSE_DATE" size="15" maxlength="10" class="BigInput" readonly value="<%=license.getGetLicenseDate()==null?"":license.getGetLicenseDate() %>"/>
       <img src="<%=imgPath%>/calendar.gif" id="date1" name="date1" align="absMiddle" border="0" style="cursor:hand">
      </td>
      <td nowrap class="TableData">生效日期：</td>
      <td class="TableData">
       <input type="text" name="EFFECTIVE_DATE"  id="EFFECTIVE_DATE" size="15" maxlength="10" class="BigInput" readonly value="<%=license.getEffectiveDate()==null?"":license.getEffectiveDate() %>"/>
       <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:hand">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">状态：</td>
      <td class="TableData">
        <select name="STATUS" id="STATUS" onchange="getState();" title="状态可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">状态</option>
        </select>
        <input type="hidden" id="licenseState" name="licenseState">
      </td>
      <td nowrap class="TableData">期限限制：</td>
      <td class="TableData">
        <INPUT type="radio" name="EXPIRATION_PERIOD" value="1" onclick="expandIt()"> 是&nbsp;&nbsp;  
        <INPUT type="radio" name="EXPIRATION_PERIOD" value="0" onclick="expandIt()" checked> 否 
      </td> 
    </tr>
    <tr style="display:none" id="menu">
      <td nowrap class="TableData">到期日期：</td>
      <td class="TableData">
       <input type="text" name="EXPIRE_DATE" id="EXPIRE_DATE" size="15" maxlength="10" class="BigInput" readonly value="<%=license.getExpireDate()==null?"":license.getExpireDate() %>"/>
       <img src="<%=imgPath%>/calendar.gif" id="date3" name="date3" align="absMiddle" border="0" style="cursor:hand">
      </td>
      <td nowrap class="TableData">到期提醒时间：</td>
      <td class="TableData">
        <input type="text" name="REMIND_TIME" id="REMIND_TIME" size="20" maxlength="20" class="BigInput" readonly value=""/>
        &nbsp;&nbsp;
        <img src="<%=imgPath%>/calendar.gif" id="date4" name="date4" align="absMiddle" border="0" style="cursor:hand">
        （为空则不提醒）
      </td>
    </tr>
   <tr>
      <td nowrap class="TableData">发证机构：<font style="color:red">*</font></td>
      <td class="TableData" colspan=3>                                                                       
        <textarea name="NOTIFIED_BODY" id="NOTIFIED_BODY" cols="70" rows="3" class="BigInput" ><%=license.getNotifiedBody()%></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="REMARK"  id="REMARK"  cols="70" rows="3" class="BigInput"><%=license.getRemark()==null?"":license.getRemark() %></textarea>
      </td>
    </tr> 
    <tr id="attr_tr">
      <td noWrap="nowrap" class="TableData">附件文档: </td>
      <td class="TableData" noWrap="nowrap" colspan="3">
        <input type = "hidden" id="returnAttId" name="returnAttId"></input>
        <input type = "hidden" id="returnAttName" name="returnAttName"></input>
      <span id="attr"></span>
      </td>
    </tr>
    <tr id="fileShowId">
      <td nowrap class="TableContent">附件上传：</td>
      <td class="TableData" id="fsUploadRow" colspan="3">
          <script>ShowAddFile();</script>
      <script></script>
      <script></script> 
      <input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
      <input type="hidden" name="ATTACHMENT_NAME_OLD" id="ATTACHMENT_NAME_OLD" value="">
      <%--插入图片 --%>
      <input type="hidden" id="moduel" name="moduel" value="">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
      <input type="hidden" id="imgattachmentName" name="imgattachmentName">
      &nbsp;</td>
    </tr>
 
   <tr>
      <td nowrap class="TableData"> 提醒：</td>
      <td class="TableData" colspan=3>
      <input id="SMS_REMIND"  type="checkbox" name="SMS_REMIND" value="1"><label for="SMS_REMIND">使用内部短信提醒</label>&nbsp;&nbsp;
      </td>
   </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
       <input type="button" onclick="javascript:CheckForm(); return false;" value="保存" name="button"  class="BigButton">
      </td>
    </tr>
  </table>
</form>
</body>
</html>