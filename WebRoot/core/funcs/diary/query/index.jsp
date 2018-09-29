<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
function doInit(){
  showCalendar('startDate',false,'startDateImg');
  showCalendar('endDate',false,'endDateImg');
}
function doSearch(){
  if(checkDate("startDate") == false){
    $("startDate").focus();
    $("startDate").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  if(checkDate("endDate") == false){
    $("endDate").focus();
    $("endDate").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  var query = "";
  query = $("form1").serialize();
 // $("from1").subject.value = query;
  //alert(query);
  location =  contextPath + "/core/funcs/diary/query/search.jsp?" + query;
}
function exportTOexcel(){
  if(checkDate("startDate") == false){
    $("startDate").focus();
    $("startDate").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  if(checkDate("endDate") == false){
    $("endDate").focus();
    $("endDate").select();
    alert("日期格式不对，请输入形：2010-04-09");
    return;
  }
  var query = "";
  query = $("form1").serialize();
  location =  contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/exportExcel.act?" + query;
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3"> 日志查询</span>
    </td>
  </tr>
</table>
<br>
<form id="form1" name="form1">
<table class="TableBlock" width="80%" align="center">
  <tr>
    <td nowrap class="TableData">起始日期：</td>
    <td class="TableData"><input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" id="startDateImg" >
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">截止日期：</td>
    <td class="TableData"><input type="text" name="endDate" id="endDate"size="10" maxlength="10" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer"  id="endDateImg">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">日志类型：</td>
    <td class="TableData">
      <select name="diaType" class="BigSelect">
        <option value="0">所有类型</option>
        <option value="1">工作日志</option>
        <option value="2">个人日志</option>
      </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">标题：</td>
    <td class="TableData"><input type="text" name="subject" class="BigInput" size="40"></td>
  </tr>
  <tr>
    <td nowrap class="TableData">关键词1：</td>
    <td class="TableData"><input type="text" name="key1" class="BigInput" size="40"></td>
  </tr>
  <tr>
    <td nowrap class="TableData">关键词2：</td>
    <td class="TableData"><input type="text" name="key2" class="BigInput" size="40"></td>
  </tr>
  <tr>
    <td nowrap class="TableData">关键词3：</td>
    <td class="TableData"><input type="text" name="key3" class="BigInput" size="40"></td>
  </tr>
  <tr>
    <td nowrap class="TableData">附件名称：</td>
    <td class="TableData"><input type="text" name="attachmentName" class="BigInput" size="40"></td>
  </tr>
  <tr >
    <td nowrap class="TableControl" colspan="2" align="center">
        <input type="hidden" name="userId" value=""></input>
        <input type="button" value="查询" class="SmallButton" title="进行查询" onclick="doSearch()">&nbsp;
        <input type="button"  value="导出" class="SmallButton"  title="导出word文件" onclick="exportTOexcel()">&nbsp;&nbsp;
        <input type="button" value="返回" class="SmallButton" onClick="location='../diaryBody.jsp'">
    </td>
  </tr>
</table>
  </form>

</body>
</html>