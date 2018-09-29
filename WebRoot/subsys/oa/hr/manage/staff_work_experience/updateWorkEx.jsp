<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.subsys.oa.hr.manage.staffWorkExperience.data.*"%> 
<html>
<head>
<%
List<YHHrStaffWorkExperience> workList = (List<YHHrStaffWorkExperience>)request.getAttribute("workExsInfoList");
YHHrStaffWorkExperience workEx = workList.get(0);
String userName = (String)request.getAttribute("userName");

String startdate="";//开始日期
String enddate = "";//结束日期
String startDate = String.valueOf(workEx.getStartDate());
if(!YHUtility.isNullorEmpty(startDate) && startDate!="null"){
	startdate =	startDate.substring(0,10);
	}
String endDate = String.valueOf(workEx.getEndDate());
if(!YHUtility.isNullorEmpty(endDate) && endDate!="null"){
	enddate =	endDate.substring(0,10);
	}
%>
<title>新建工作经历信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
var fckContentStr = "";
/*****************附件上传开始*****************************/
var upload_limit=1,limit_type=limitUploadFiles;
var isUploadBackFun = false;
/*****************附件上传结束*****************************/
function doOnload(){
	//时间
	  var parameters = {
	      inputId:'START_DATE',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date1'
	  };
	  new Calendar(parameters);
	  var parameters = {
	      inputId:'END_DATE',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date2'
	  };
	  new Calendar(parameters);
	  showAttachment();
}
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function CheckForm(){
	 var userName = $("userName").value;
	 if(userName.replaceAll(" ","") == "" || userName == "null"){
	      alert("单位员工不能为空");
	      return false;
   }
	if(document.form1.START_DATE.value!="" && document.form1.END_DATE.value!="" && document.form1.START_DATE.value > document.form1.END_DATE.value)
	   { 
	      alert("开始日期不能大于结束日期！");
	      return (false);
	   }
	 var oEditor = FCKeditorAPI.GetInstance('fileFolder');
	 $("incentiveDescription").value = oEditor.GetXHTML();
	 //alert($("incentiveDescription").value);
	 $("form1").submit();
}
//var seqid = "";
function doSubmit(){
	 var userName = $("userName").value;
	  //seqid = $("seqid").value;
	 if(userName.replaceAll(" ","") == "" || userName == "null"){
	      alert("单位员工不能为空");
	      return false;
   }
	if(document.form1.START_DATE.value!="" && document.form1.END_DATE.value!="" && document.form1.START_DATE.value > document.form1.END_DATE.value)
	   { 
	      alert("开始日期不能大于结束日期！");
	      return (false);
	   }
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		$("incentiveDescription").value = oEditor.GetXHTML();
		// alert($("incentiveDescription").value);
			$("form1").submit();
}

function showAttachment(){
	var attachmentId = "<%=YHUtility.null2Empty(workEx.getAttachmentId())%>";
	var attachmentName = "<%= YHUtility.encodeSpecial(YHUtility.null2Empty(workEx.getAttachmentName()))%>";
	
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
  	attachMenuSelfUtil("attr","hr",$('returnAttName').value ,$('returnAttId').value, '','','<%=workEx.getSeqId()%>',selfdefMenu);
	}
}

//删除附件
function deleteAttachBackHand(attachName,attachId,attrchIndex) { 
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffWorkExperience/act/YHNewWorkExperienceAct/delFloatFile.act?delAttachId=" + attachId + "&seqId=<%=workEx.getSeqId()%>";
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
    <td><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 新建工作经历信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/staffWorkExperience/act/YHNewWorkExperienceAct/updateWorkExperienceInfo.act"  method="post" name="form1" id="form1" onsubmit="">
<table class="TableBlock" width="80%" align="center">
   <tr>
      <td nowrap class="TableData">单位员工：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" name="userName" id="userName" size="12" class="BigStatic" readonly value="<%=userName ==null?"":userName %>">&nbsp;
        <INPUT type="hidden" name="userId" id="userId" value="<%=workEx.getStaffName()==null?"":workEx.getStaffName() %>">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName'],null,null,1)">选择</a>  
        <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
      </td>
      <td nowrap class="TableData">担任职务：</td>
      <td class="TableData">
      <input type="text" name="POST_OF_JOB" id="POST_OF_JOB" class=BigInput size="15" value="<%=workEx.getPostOfJob()==null?"":workEx.getPostOfJob() %>">
      <input type="hidden" name="seqid" id = "seqid" value="<%=workEx.getSeqId() %>"></input>
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">所在部门：
    	 	</td><td class="TableData"><input type="text" name="WORK_BRANCH" id="WORK_BRANCH" class=BigInput size="15" value="<%=workEx.getWorkBranch()==null?"":workEx.getWorkBranch() %>"></td>
    	<td nowrap class="TableData">证明人：</td>
      <td class="TableData"><input type="text" name="WITNESS" id="WITNESS" class=BigInput size="15" value="<%=workEx.getWitness()==null?"":workEx.getWitness() %>"></td>
    </tr>
    <tr>
      <td nowrap class="TableData">开始日期：</td>
      <td class="TableData"><input type="text" name="START_DATE" id="START_DATE" size="15" maxlength="10" class="BigInput" readonly value="<%=startdate==null?"":startdate %>" />
             <img src="<%=imgPath%>/calendar.gif" id="date1" name="date1" align="absMiddle" border="0" style="cursor:hand">
       </td>
      
      <td nowrap class="TableData">结束日期：</td>
      <td class="TableData"><input type="text" name="END_DATE" id="END_DATE" size="15" maxlength="10" class="BigInput" readonly value="<%=enddate==null?"":enddate %>" />
                   <img src="<%= imgPath%>/calendar.gif" id="date2" name="date2" align="absMiddle" border="0" style="cursor:hand">
      
      </td>
    </tr>
    <tr>
    	<td nowrap class="TableData">行业类别：</td>
      <td class="TableData" colspan=3><textarea name="MOBILE" id="MOBILE" cols="78" rows="3" class="BigInput"><%=workEx.getMobile()==null?"":workEx.getMobile() %></textarea></td>
    </tr>
    <tr>
      <td nowrap class="TableData">工作单位：</td>
      <td class="TableData" colspan=3><textarea name="WORK_UNIT" id="WORK_UNIT" cols="78" rows="3" class="BigInput" ><%=workEx.getWorkUnit()==null?"":workEx.getWorkUnit() %></textarea></td>
    </tr>
    <tr>
      <td nowrap class="TableData">工作内容：</td>
      <td class="TableData" colspan=3><textarea name="WORK_CONTENT" id="WORK_CONTENT" cols="78" rows="3" class="BigInput"><%=workEx.getWorkContent()==null?"":workEx.getWorkContent() %></textarea></td>
    </tr>
    <tr>
      <td nowrap class="TableData">离职原因：</td>
      <td class="TableData" colspan=3><textarea name="REASON_FOR_LEAVING" id="REASON_FOR_LEAVING" cols="78" rows="3" class="BigInput" ><%=workEx.getReasonForLeaving()==null?"":workEx.getReasonForLeaving() %></textarea></td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3><textarea name="REMARK" id="REMARK" cols="78" rows="3" class="BigInput" ><%=workEx.getRemark()==null?"":workEx.getRemark() %></textarea></td>
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
			<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			<%--插入图片 --%>
			<input type="hidden" id="moduel" name="moduel" value="">
      <input type="hidden" id="imgattachmentId" name="imgattachmentId">
			<input type="hidden" id="imgattachmentName" name="imgattachmentName">
      &nbsp;</td>
    </tr>
    <tr id="EDITOR">
      <td class="TableData" colspan="4"> 主要业绩：
      	<div>
		     <script language=JavaScript>    
		      var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
		      var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
		      oFCKeditor.BasePath = sBasePath ;
		      oFCKeditor.Height = 200;
		      var sSkinPath = sBasePath + 'editor/skins/office2003/';
		      oFCKeditor.Config['SkinPath'] = sSkinPath ;
		      oFCKeditor.Config['PreloadImages'] =
		                      sSkinPath + 'images/toolbar.start.gif' + ';' +
		                      sSkinPath + 'images/toolbar.end.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonarrow.gif' ;
		      //oFCKeditor.Config['FullPage'] = true ;
		       oFCKeditor.ToolbarSet = "fileFolder";
		      oFCKeditor.Value = '<%=workEx.getKeyPerformance()==null?"":workEx.getKeyPerformance()%>' ;
		      oFCKeditor.Create();
		     </script>
				</div>
			</td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      <input type="hidden" name="incentiveDescription" id="incentiveDescription" value="<%=workEx.getKeyPerformance() %>">
      <input type="button" onclick="javascript:CheckForm(); return false;" value="保存" name="button"  class="BigButton">  
    </td>
    </tr>
  </table>
</form>
</body>
</html>