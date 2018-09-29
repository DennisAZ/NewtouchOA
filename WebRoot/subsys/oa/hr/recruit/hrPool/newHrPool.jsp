<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建人才档案</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/swfupload.queue.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/fileprogress.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/swfupload/handlers.js"></script>
<script type="text/javascript"	src="<%=contextPath%>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/hrPool/js/hrPoolLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";
//附件上传
var upload_limit=1,limit_type=limitUploadFiles;
var oa_upload_limit=limitUploadFiles;
var swfupload;
function doInit(){
//初始化日期
	setDate();
//籍贯	
	getSelectedCode("AREA","EMPLOYEE_NATIVE_PLACE");
//政治面貌
	getSelectedCode("STAFF_POLITICAL_STATUS","EMPLOYEE_POLITICAL_STATUS");
//期望工作性质
	getSelectedCode("JOB_CATEGORY","JOB_CATEGORY");
//应聘岗位
	getSelectedCode("POOL_POSITION","POSITION");
//所学专业
	getSelectedCode("POOL_EMPLOYEE_MAJOR","EMPLOYEE_MAJOR");
//学历
	getSelectedCode("STAFF_HIGHEST_SCHOOL","EMPLOYEE_HIGHEST_SCHOOL",6);
//学位
	getSelectedCode("EMPLOYEE_HIGHEST_DEGREE","EMPLOYEE_HIGHEST_DEGREE",6);
	
}
//日期
function setDate(){
	var date1Parameters = {
		inputId:'EMPLOYEE_BIRTH',
		property:{isHaveTime:false},
		bindToBtn:'date1'
	};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
		inputId:'JOB_BEGINNING',
		property:{isHaveTime:false},
		bindToBtn:'date2'
	};
	new Calendar(date2Parameters);
	
	var date3Parameters = {
		inputId:'GRADUATION_DATE',
		property:{isHaveTime:false},
		bindToBtn:'date3'
	};
	new Calendar(date3Parameters);
}
function clearValue(str){
	if(str){
		str.value = "";
	}
}
function LoadWindow2(){
	var url= contextPath + "/subsys/oa/hr/recruit/hrPool/plannoinfo/index.jsp";
	loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
	loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
	window.showModalDialog(url,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}
function checkForm(){
	if(document.form1.PLAN_NAME.value.trim() == ""){
		alert("计划名称不能为空");
		$('PLAN_NAME').focus();
		$('PLAN_NAME').select();
		return false;
	}
	if(document.form1.EMPLOYEE_NAME.value.trim() == ""){
		alert("应聘人姓名不能为空");
		$('EMPLOYEE_NAME').focus();
		$('EMPLOYEE_NAME').select();
		return false;
	}
	if(document.form1.EMPLOYEE_SEX.value.trim() == ""){
		alert("应聘人性别不能为空");
		$('EMPLOYEE_SEX').focus();
		$('EMPLOYEE_SEX').select();
		return false;
	}
	if(document.form1.EMPLOYEE_BIRTH.value.trim() == ""){
		alert("应聘人出生日期不能为空");
		$('EMPLOYEE_BIRTH').focus();
		$('EMPLOYEE_BIRTH').select();
		return false;
	}
	if(document.form1.EMPLOYEE_PHONE.value.trim() == ""){
		alert("应聘人联系电话不能为空");
		$('EMPLOYEE_PHONE').focus();
		$('EMPLOYEE_PHONE').select();
		return false;
	}
		if(document.form1.EMPLOYEE_EMAIL.value.trim() == ""){
		alert("应聘人E_mail不能为空");
		$('EMPLOYEE_EMAIL').focus();
		$('EMPLOYEE_EMAIL').select();
		return false;
	}
	if(document.form1.JOB_CATEGORY.value.trim() == ""){
		alert("期望工作性质不能为空");
		$('JOB_CATEGORY').focus();
		$('JOB_CATEGORY').select();
		return false;
	}
	if(document.form1.EXPECTED_SALARY.value.trim() == ""){
		alert("期望薪水不能为空");
		$('EXPECTED_SALARY').focus();
		$('EXPECTED_SALARY').select();
		return false;
	}
	if(!isNumbers($('EXPECTED_SALARY').value)){
		alert("期望薪水格式错误,应形如 10000.00"); 
		$('EXPECTED_SALARY').focus();
		$('EXPECTED_SALARY').select();
		return false;
	}
	if(document.form1.EMPLOYEE_HIGHEST_SCHOOL.value.trim() == ""){
		alert("学历不能为空");
		$('EMPLOYEE_HIGHEST_SCHOOL').focus();
		$('EMPLOYEE_HIGHEST_SCHOOL').select();
		return false;
	}
	if(document.form1.EMPLOYEE_MAJOR.value.trim() == ""){
		alert("专业不能为空");
		$('EMPLOYEE_MAJOR').focus();
		$('EMPLOYEE_MAJOR').select();
		return false;
	}
	if(document.form1.POSITION.value.trim() == ""){
		alert("应聘岗位不能为空");
		$('POSITION').focus();
		$('POSITION').select();
		return false;
	}
	return true;
}
function doSubmit(){
	if(checkForm()){
		var oEditor = FCKeditorAPI.GetInstance('fileFolder');
		//alert(oEditor.GetXHTML());
		$("resume").value = oEditor.GetXHTML();
		$("form1").submit();
	}
}

</script>

</head>
<body onload="doInit();">
<table border="0" width="770" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建人才档案</span>&nbsp;&nbsp;</td>
  </tr>
</table>
<form enctype="multipart/form-data" action="<%=contextPath %>/yh/subsys/oa/hr/recruit/hrPool/act/YHHrRecruitPoolAct/addHrPoolInfo.act" method="post" name="form1" id="form1" >
<table class="TableBlock" width="770" align="center">
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;基本信息：</b></td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">计划名称：<font color="red">*</font></td>
    <td class="TableData" width="180">
      <INPUT type="text"name="PLAN_NAME" id="PLAN_NAME" readonly="readonly" class=BigInput size="12">
      <INPUT type="hidden" name="PLAN_NO" id="PLAN_NO" value="">
      <a href="javascript:;" class="orgAdd" onClick="LoadWindow2()">选择</a>	
    </td>
    <td nowrap class="TableData" width="100">应聘人姓名：<font color="red">*</font></td>
    <td class="TableData" width="180" colspan="2">
    	<input type="text" name="EMPLOYEE_NAME" id="EMPLOYEE_NAME" class="BigInput"  value="">
    </td>
    <td class="TableData" rowspan="7" colspan="1" align="center"><center>暂无照片</center>
   </td> 
  </tr>
  <tr>
   <td nowrap class="TableData">性别：<font color="red">*</font></td>
   <td class="TableData">
    <select name="EMPLOYEE_SEX" id="EMPLOYEE_SEX" >
         <option value="0">男</option>
         <option value="1">女</option>
     </select>
   </td>
   <td nowrap class="TableData" width="100">出生日期：<font color="red">*</font></td>
    <td class="TableData" width="180" colspan="2">
      <input type="text" name="EMPLOYEE_BIRTH" id="EMPLOYEE_BIRTH" size="12" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);" />
      <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">民族：</td>
    <td class="TableData"  width="180">
      <input type="text" name="EMPLOYEE_NATIONALITY" id="EMPLOYEE_NATIONALITY" class="BigInput" value="">
    </td> 	
    <td nowrap class="TableData" width="100">现居住城市：</td>
    <td class="TableData" width="180" colspan="2">
    	<input type="text" name="RESIDENCE_PLACE" id="RESIDENCE_PLACE" class="BigInput" value="">
    </td>           
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">联系电话：<font color="red">*</font></td>
    <td class="TableData"  width="180">
    	<input type="text" name="EMPLOYEE_PHONE" id="EMPLOYEE_PHONE" class="BigInput">
    </td>
    <td nowrap class="TableData" >E_mail：<font color="red">*</font></td>
     <td class="TableData"  width="180" colspan="2">
     	<input type="text" name="EMPLOYEE_EMAIL" id="EMPLOYEE_EMAIL" class="BigInput">
     </td>
  </tr>
  <tr>
    <td nowrap class="TableData">籍贯：</td>
    <td class="TableData">
    	<select name="EMPLOYEE_NATIVE_PLACE" id="EMPLOYEE_NATIVE_PLACE" >
      </select>
    </td>
     <td nowrap class="TableData" width="100">户口所在地：</td>
    <td class="TableData"  width="180" colspan="2">
    	<input type="text" name="EMPLOYEE_DOMICILE_PLACE" id="EMPLOYEE_DOMICILE_PLACE" size="35" class="BigInput">
    </td>             
  </tr>
  <tr>
    <td nowrap class="TableData">婚姻状况：</td>
    <td class="TableData">
      <select name="EMPLOYEE_MARITAL_STATUS" >
        <option value="" ></option>
        <option value="0" >未婚&nbsp;&nbsp;</option>
        <option value="1" >已婚</option>
        <option value="2" >离异</option>
      </select>    	
    </td>
    <td nowrap class="TableData">政治面貌：</td>
    <td class="TableData" colspan="2">
        <select name="EMPLOYEE_POLITICAL_STATUS" id="EMPLOYEE_POLITICAL_STATUS" >
          <option value="">政治面貌</option>
        </select>
    </td>    	
  </tr>
  <tr>
  	<td nowrap class="TableData" width="100">健康状况：</td>
    <td class="TableData"  width="180">
    	<input type="text" name="EMPLOYEE_HEALTH" id="EMPLOYEE_HEALTH" class="BigInput" value=>
    </td>
     <td nowrap class="TableData" width="100">参加工作时间：</td>
    <td class="TableData"  width="180" colspan="2">
      <input type="text" name="JOB_BEGINNING" id="JOB_BEGINNING" size="12" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);"  />
      <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
  </tr>   
 <tr>
  <td nowrap class="TableData">期望工作性质：<font color="red">*</font></td>
    <td class="TableData">
    	<select name="JOB_CATEGORY" id="JOB_CATEGORY" >
       </select>
    </td> 
    <td nowrap class="TableData" width="100"><?=$PHOTO_STR?></td>
    <td class="TableData"  width="180" colspan="3">
       <input type="file" name="headPic" id="headPic" size="40"  class="BigInput" title="选择附件文件" >
       
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">期望从事行业：</td>
    <td class="TableData">
    	<input type="text" name="JOB_INDUSTRY" id="JOB_INDUSTRY" class="BigInput">
    </td> 
    <td nowrap class="TableData">期望从事职业：</td>
    <td class="TableData">
    	<input type="text" name="JOB_INTENSION" id="JOB_INTENSION" class="BigInput">
    </td>
    <td nowrap class="TableData">期望工作城市：</td>
    <td class="TableData">
    	<input type="text" name="WORK_CITY" id="WORK_CITY" class="BigInput">
    </td>                  
  </tr>   
    <tr>
    <td nowrap class="TableData" width="110">期望薪水(税前)：<font color="red">*</font></td>
    <td class="TableData"  width="180" >
    	<input type="text" name="EXPECTED_SALARY" id="EXPECTED_SALARY" size="10" maxlength="10" class="BigInput">&nbsp;元
    </td>
    <td nowrap class="TableData" width="100">应聘岗位：<font color="red">*</font></td>
    <td class="TableData">
     <select name="POSITION" id="POSITION" >
			<option value="">应聘岗位</option>
     </select>
    </td> 
    <td nowrap class="TableData" width="100">到岗时间：</td>
    <td class="TableData"  width="180">
      <select name="START_WORKING" >
        <option value="" ></option>
        <option value="0" >1周以内</option>
        <option value="1" >1个月内</option>
        <option value="2" >1~3个月</option>
        <option value="3" >3个月后</option>
        <option value="4" >随时到岗</option>
      </select>     
    </td>              
  </tr> 
  <tr>
    <td nowrap class="TableData" width="100">毕业时间：</td>
    <td class="TableData"  width="180">
      <input type="text" name="GRADUATION_DATE" id="GRADUATION_DATE" size="10" maxlength="10" class="BigInput" value="" readonly="readonly" onfocus="clearValue(this);"  />
      <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
    <td nowrap class="TableData" width="100">毕业学校：</td>
    <td class="TableData"  width="180" colspan="3">
    	<input type="text" name="GRADUATION_SCHOOL" size="60" id="GRADUATION_SCHOOL" class="BigInput">
    </td>                
  </tr> 
    <tr>
    <td nowrap class="TableData" width="100">所学专业：<font color="red">*</font></td>
    <td class="TableData"  width="180">
    	<select name="EMPLOYEE_MAJOR" id="EMPLOYEE_MAJOR">
			<option value="">所学专业</option>
     </select>
    </td>   
    <td nowrap class="TableData" width="100">学历：<font color="red">*</font></td>
    <td class="TableData"  width="180">
        <select name="EMPLOYEE_HIGHEST_SCHOOL" id="EMPLOYEE_HIGHEST_SCHOOL" >
        	<option value="">学历</option>
        </select>
    </td>
    <td nowrap class="TableData" width="100">学位：</td>
    <td class="TableData"  width="180">
    	<select name="EMPLOYEE_HIGHEST_DEGREE" id="EMPLOYEE_HIGHEST_DEGREE" >
    		<option value="">学位</option>
      </select>
    </td>              
  </tr>   
  <tr>
    <td nowrap class="TableData" width="100">外语语种1：</td>
    <td class="TableData"  width="180"><input type="text" name="FOREIGN_LANGUAGE1" id="FOREIGN_LANGUAGE1" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种2：</td>
    <td class="TableData"  width="180"><input type="text" name="FOREIGN_LANGUAGE2" id="FOREIGN_LANGUAGE2" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语语种3：</td>
    <td class="TableData"  width="180"><input type="text" name="FOREIGN_LANGUAGE3" id="FOREIGN_LANGUAGE3" class="BigInput"></td>                 
  </tr> 
  <tr>
    <td nowrap class="TableData" width="100">外语水平1：</td>
    <td class="TableData"  width="180"><input type="text" name="FOREIGN_LEVEL1" id="FOREIGN_LEVEL1" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语水平2：</td>
    <td class="TableData"  width="180"><input type="text" name="FOREIGN_LEVEL2" id="FOREIGN_LEVEL2" class="BigInput"></td>
    <td nowrap class="TableData" width="100">外语水平3：</td>
    <td class="TableData"  width="180"><input type="text" name="FOREIGN_LEVEL3" id="FOREIGN_LEVEL3" class="BigInput"></td>                 
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">计算机水平：</td>
    <td class="TableData"  width="180">
    	<input type="text" name="COMPUTER_LEVEL" id="COMPUTER_LEVEL" class="BigInput">
    </td>
    <td nowrap class="TableData">年龄：</td>
    <td class="TableData" width="180" colspan="3">
    	<input type="text" name="EMPLOYEE_AGE" id="EMPLOYEE_AGE" size="8" maxlength="3" class="BigInput">&nbsp;岁
    </td>          
  </tr>  
  <tr>
    <td nowrap class="TableData" width="100">特长：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="EMPLOYEE_SKILLS" cols="100" rows="3" class="BigInput" value=""></textarea>             
   </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">职业技能：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="CAREER_SKILLS" id="CAREER_SKILLS" cols="100" rows="3" class="BigInput" value=""></textarea>
    </td>            
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">工作经验：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="WORK_EXPERIENCE" id="WORK_EXPERIENCE" cols="100" rows="3" class="BigInput" value=""></textarea>            
   </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">项目经验：</td>
    <td class="TableData"  width="180" colspan="5">
    	<textarea name="PROJECT_EXPERIENCE" id="PROJECT_EXPERIENCE" cols="100" rows="3" class="BigInput" value=""></textarea>             
   </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">备注：</td>
    <td class="TableData"  width="180" colspan="5"><textarea name="REMARK" id="REMARK" cols="100" rows="3" class="BigInput" value=""></textarea>             
    </td>
  </tr>
  <tr>
    <td nowrap class="TableHeader" colspan="6"><b>&nbsp;附件简历：</b></td>
  </tr>
  <tr style="display: none">
    <td class="TableData" colspan="6"></td>
  </tr>
  <tr height="25">
    <td nowrap class="TableData">附件选择：</td>
    <td class="TableData" colspan="6">
       <script>ShowAddFile();</script>
	    <script></script>
	    <script></script> 
			<input type="hidden" name="ATTACHMENT_ID_OLD" id="ATTACHMENT_ID_OLD" value="">
			<input type="hidden" name="ATTACHMENT_NAME_OLD"	id="ATTACHMENT_NAME_OLD" value="">
			<%--插入图片 --%>
    </td>
  </tr>     
  <tr>
    <td nowrap class="TableData" colspan="6">简历：</td> 
  </tr>
    <tr>
    <td nowrap class="TableData" colspan="6">
			<div>
		     <script language=JavaScript>    
		      var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
		      var oFCKeditor = new FCKeditor( 'fileFolder' ) ;
		      oFCKeditor.BasePath = sBasePath ;
		      oFCKeditor.Height = 400;
		      var sSkinPath = sBasePath + 'editor/skins/office2003/';
		      oFCKeditor.Config['SkinPath'] = sSkinPath ;
		      oFCKeditor.Config['PreloadImages'] =
		                      sSkinPath + 'images/toolbar.start.gif' + ';' +
		                      sSkinPath + 'images/toolbar.end.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
		                      sSkinPath + 'images/toolbar.buttonarrow.gif' ;
		      //oFCKeditor.Config['FullPage'] = true ;
		       oFCKeditor.ToolbarSet = "fileFolder";
		      oFCKeditor.Value = '' ;
		      oFCKeditor.Create();
		     </script>
				</div>   
    </td>                 
  </tr>            
  <tr align="center" class="TableControl">
    <td colspan=6 nowrap>
			<input type="hidden" name="toId" id="toId" value="">
			<input type="hidden" name="resume" id="resume" value="">
			<input type="button" value="保存" onclick="doSubmit();" class="BigButton" >&nbsp;&nbsp;
    </td>
  </tr>             
</table>
<form>



</body>
</html>