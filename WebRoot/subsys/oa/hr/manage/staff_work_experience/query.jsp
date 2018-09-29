<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>人事调动信息查询</title>
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
function CheckForm(){
	 
 	var src = contextPath+"/yh/subsys/oa/hr/manage/staffWorkExperience/act/YHNewWorkExperienceAct/queryWorkInfo.act";
 	  document.form1.action = src;
 	  document.form1.submit();
 	  return true;   
   
}

</script>
</head>
<body class="bodycolor" topmargin="5">

<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 工作经历信息查询</span></td>
  </tr>
</table>
<br>
<form action="#"  method="post" name="form1" onsubmit="return CheckForm();" >
 <table class="TableBlock" width="450" align="center">
     <tr>
      <td nowrap class="TableData">单位员工：</td>
      <td class="TableData">
        <input type="text" name="userName" id="userName" size="12" class="BigStatic" readonly value="">&nbsp;
        <INPUT type="hidden" name="userId" id="userId">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['userId', 'userName'],null,null,1)">选择</a>  
        <a href="javascript:;" class="orgClear" onClick="$('userId').value='';$('userName').value='';">清空</a>
      </td>
    </tr>
    <tr>
    <td nowrap class="TableData" width="100"> 担任职务：</td>
      <td class="TableData">
        <input type="text" name="POST_OF_JOB" id="POST_OF_JOB" size="15" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 工作单位：</td>
      <td class="TableData">
        <input type="text" name="WORK_UNIT" id="WORK_UNIT" size="34" maxlength="200" class="BigInput" value="">
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData" width="100"> 行业类别：</td>
      <td class="TableData">
        <input type="text" name="MOBILE" id="MOBILE" size="34" maxlength="200" class="BigInput" value="">
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData" width="100"> 工作内容：</td>
      <td class="TableData">
        <input type="text" name="WORK_CONTENT" id="WORK_CONTENT" size="34" maxlength="200" class="BigInput" value="">
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData" width="100"> 主要业绩：</td>
      <td class="TableData">
        <input type="text" name="KEY_PERFORMANCE" id="KEY_PERFORMANCE" size="34" maxlength="200" class="BigInput" value="">
      </td>
   </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" onclick="javascript:CheckForm(); return false;" value="查询" name="button"  class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
   </form>
</body>
</html>