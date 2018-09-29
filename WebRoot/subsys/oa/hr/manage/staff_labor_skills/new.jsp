<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>新建劳动技能信息</title>
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
/*****************附件上传开始*****************************/
var upload_limit=1,limit_type=limitUploadFiles;
var isUploadBackFun = false;
/*****************附件上传结束*****************************/
function doOnload(){
	//时间
	  var parameters = {
	      inputId:'ISSUE_DATE',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date1'
	  };
	  new Calendar(parameters);
	  var parameters = {
	      inputId:'EXPIRE_DATE',
	      property:{isHaveTime:false}
	      ,bindToBtn:'date2'
	  };
	  new Calendar(parameters);
}
/** 
 * 替换s1为s2 
 */ 
 String.prototype.replaceAll = function(s1,s2){ 
    return this.replace(new RegExp(s1,"gm"),s2); 
 }
function doSubmit(){
	 var userName = $("userName").value;
	 var abilityName = $("ABILITY_NAME").value;
	 var issueDate = $("ISSUE_DATE").value;
	 var expires = $("EXPIRES").value;
   
	 if(userName.replaceAll(" ","") == "" || userName == "null"){
	      alert("单位员工不能为空");
	      return false;
   }
   
	 if(abilityName.replaceAll(" ","") == "" || abilityName == "null"){
	      alert("技能名称不能为空");
	      return false;
   }  
	 if(issueDate.replaceAll(" ","") == "" || issueDate == "null"){
	      alert("发证日期不能为空");
	      return false;
   }
	 if(expires.replaceAll(" ","") == "" || expires == "null"){
	      alert("有效期不能为空");
	      return false;
   }
	 var reg1 = /[^\d]/g;
   if (expires && expires.match(reg1)) {
     alert("有效期只能为数字");
     return false;
   }
	 $("form1").submit();
}

</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 新建劳动技能信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/staffLaborSkills/act/YHNewLaborSkillsAct/addLaborSkillsInfo.act"  method="post" name="form1" id="form1">
<table class="TableBlock" width="80%" align="center">
   <tr>
      <td nowrap class="TableData">单位员工：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" name="userName" id="userName" size="12" class="BigStatic" readonly value="">&nbsp;
        <input type="hidden" name="userId" id="userId">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName'],null,null,1)">选择</a>  
        <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
      </td>
      <td nowrap class="TableData">技能名称：<font style="color:red">*</font></td>
      <td class="TableData">
        <INPUT type="text" name="ABILITY_NAME" id="ABILITY_NAME" class=BigInput size="15" value="">
      </td>
    </tr>
    <tr>
    	 <td nowrap class="TableData">特种作业：</td>
      <td class="TableData">
       <INPUT type="radio" name="SPECIAL_WORK" id="SPECIAL_WORK" value="1" checked> 是&nbsp;&nbsp;  
			 <INPUT type="radio" name="SPECIAL_WORK" id="SPECIAL_WORK" value="0"> 否 
      </td>
    	<td nowrap class="TableData">级别：</td>
      <td class="TableData">
       <INPUT type="text" name="SKILLS_LEVEL" id="SKILLS_LEVEL" class=BigInput size="15" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">技能证：</td>
      <td class="TableData">
        <INPUT type="radio" name="SKILLS_CERTIFICATE" id="SKILLS_CERTIFICATE" value="1" checked> 是&nbsp;&nbsp;  
			  <INPUT type="radio" name="SKILLS_CERTIFICATE" id="SKILLS_CERTIFICATE" value="0"> 否 
      </td>
      <td nowrap class="TableData">发证日期：<font style="color:red">*</font></td>
      <td class="TableData">
       <input type="text" name="ISSUE_DATE" id="ISSUE_DATE" size="15" maxlength="10" class="BigInput" readonly value=""/>
       <img src="<%=imgPath%>/calendar.gif" id="date1" name="date1"  align="absMiddle"  border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">有效期：<font style="color:red">*</font></td>
      <td class="TableData">
        <INPUT type="text" name="EXPIRES" id="EXPIRES" class=BigInput size="15" value="">&nbsp;年
      </td>
      <td nowrap class="TableData">到期日期：</td>
      <td class="TableData">
       <input type="text" name="EXPIRE_DATE" id="EXPIRE_DATE" size="15" maxlength="10" class="BigInput" readonly value=""/>
       <img src="<%=imgPath%>/calendar.gif" id="date2" name="date2"  align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">发证机关/单位：</td>
      <td class="TableData" colspan=3>
        <textarea name="ISSUING_AUTHORITY" id="ISSUING_AUTHORITY" cols="78" rows="3" class="BigInput" ></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备注：</td>
      <td class="TableData" colspan=3>
        <textarea name="REMARK"  id="REMARK" cols="78" rows="3" class="BigInput" ></textarea>
      </td>
    </tr> 
    <tr id="attr_tr">
      <td noWrap="nowrap" class="TableData">附件文档: </td>
      <td class="TableData" noWrap="nowrap" colspan="3">
        <input type="hidden" id="attachmentId" name="attachmentId">
        <input type="hidden" id="attachmentName" name="attachmentName">
        <input type="hidden" id="ensize" name="ensize">
        <input type="hidden" id="moduel" name="moduel" value="news">
        <span id="showAtt">
        </span>
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
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
         <input type="button" value="保存" class="BigButton" name="button" onclick="doSubmit();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>