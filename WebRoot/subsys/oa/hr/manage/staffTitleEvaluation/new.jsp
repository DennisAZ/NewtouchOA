<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建职称评定信息</title>
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
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffTitleEvaluation/js/staffTitleEvaluationLogic.js"></script>
<script type="text/javascript">
var fckContentStr = "";

function doInit(){
	getSecretFlag("HR_STAFF_TITLEEVALUATION1","getMethod");
	setDate();
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'reportTime',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
    inputId:'receiveTime',
    property:{isHaveTime:false}
    ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

  var date3Parameters = {
    inputId:'approveNextTime',
    property:{isHaveTime:false}
    ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);

  var date4Parameters = {
    inputId:'startDate',
    property:{isHaveTime:false}
    ,bindToBtn:'date4'
  };
  new Calendar(date4Parameters);
  
  var date5Parameters = {
    inputId:'endDate',
    property:{isHaveTime:false}
    ,bindToBtn:'date5'
  };
  new Calendar(date5Parameters);
}

function doSubmit(){
	var oEditor = FCKeditorAPI.GetInstance('fileFolder');
	$("remark").value = oEditor.GetXHTML();
  if(checkForm()){
    $("form1").submit();
  }
}

function checkForm(){
  if($("byEvaluStaffsDesc").value == ""){
    alert("评定对象不能为空！");
    $("byEvaluStaffsDesc").focus();
    return (false);
  }

  if($("approvePersonDesc").value == ""){
    alert("批准人不能为空！");
    $("approvePersonDesc").focus();
    return (false);
  }

  if($("postName").value == ""){
    alert("获取职称不能为空！");
    $("postName").focus();
    return (false);
  }

  var reportTime = $("reportTime").value;
  var receiveTime = $("receiveTime").value;
  var approveNextTime = $("approveNextTime").value;
  var startDate = $("startDate").value;
  var endDate = $("endDate").value;

  if(reportTime && receiveTime){
    if(reportTime > receiveTime){
      alert(" 获取时间不能小于申报时间！");
      $("receiveTime").focus(); 
      $("receiveTime").select(); 
      return false;
    }
  }

  if(receiveTime && approveNextTime){
    if(receiveTime > approveNextTime){
      alert(" 下次申报时间不能小于获取时间！");
      $("approveNextTime").focus(); 
      $("approveNextTime").select(); 
      return false;
    }
  }

  if(startDate && endDate){
    if(startDate > endDate){
      alert(" 聘用结束时间不能小于聘用开始时间！");
      $("endDate").focus(); 
      $("endDate").select(); 
      return false;
    }
  }

  if(receiveTime && startDate){
    if(receiveTime > startDate){
      alert(" 聘用开始时间不能小于获取时间！");
      $("startDate").focus(); 
      $("startDate").select(); 
      return false;
    }
  }
	return true;
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建职称评定信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/manage/titleEvaluation/act/YHHrStaffTitleEvaluationAct/addStaffTitleEvaluationInfo.act"  method="post" name="form1" id="form1" onsubmit="">
<table class="TableBlock" width="100%" align="center">
	  <tr>
	  	<td nowrap class="TableData">评定对象：<font color="red">*</font> </td>
	  	<td class="TableData">
	      <input type="hidden" name="byEvaluStaffs" id="byEvaluStaffs" value="" >
	      <input type="text" name="byEvaluStaffsDesc" id="byEvaluStaffsDesc" class="BigStatic" readonly size="15" >
	      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['byEvaluStaffs', 'byEvaluStaffsDesc'],null,null,1);">添加</a>
	    </td>
      <td nowrap class="TableData">批准人：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="hidden" name="approvePerson" id="approvePerson" value="" >
        <input type="text" name="approvePersonDesc" id="approvePersonDesc" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['approvePerson', 'approvePersonDesc'],null,null,1);">添加</a>
      </td>
	  </tr>
	  <tr>
	    <td nowrap class="TableData">获取职称：<font color="red">*</font> </td>
      <td class="TableData">
        <input type="text" name="postName" id="postName" class="BigInput" size="15">
      </td>
      <td nowrap class="TableData">获取方式： </td>
      <td class="TableData" >
        <select name="getMethod" id="getMethod"  title="获取方式可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">获取方式&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td>
	  </tr>
	  <tr>
      <td nowrap class="TableData">申报时间：</td>
      <td class="TableData">
        <input type="text" name="reportTime" id="reportTime" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">获取时间：</td>
      <td class="TableData">
        <input type="text" name="receiveTime" id="receiveTime" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
	  </tr>
	  <tr>
      <td nowrap class="TableData">下次申报职称：</td>
      <td class="TableData">
        <input type="text" name="approveNext" id="approveNext" class="BigInput" size="15" > 
      </td>
      <td nowrap class="TableData">下次申报时间：</td>
      <td class="TableData">
        <input type="text" name="approveNextTime" id="approveNextTime" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">聘用职务：</td>
      <td class="TableData">
        <input type="text" name="employPost" id="employPost" class="BigInput" size="15" > 
      </td>
      <td nowrap class="TableData">聘用单位：</td>
      <td class="TableData">
        <input type="text" name="employCompany" id="employCompany" class="BigInput" size="15" > 
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData">聘用开始时间：</td>
      <td class="TableData">
        <input type="text" name="startDate" id="startDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
      <td nowrap class="TableData">聘用结束时间：</td>
      <td class="TableData">
        <input type="text" name="endDate" id="endDate" size="11" maxlength="10"  class="BigInput" value="" readonly>
        <img id="date5" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr id="EDITOR">
      <td class="TableData" colspan="4"> 评定详情：
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
          oFCKeditor.Value = '' ;
          oFCKeditor.Create();
         </script>
        </div>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
        <input type="hidden" name="remark" id="remark" value="">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
      </td>
    </tr>
  </table>
</form>

</body>
</html>