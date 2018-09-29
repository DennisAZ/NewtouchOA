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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" >
function doInit(){
  showCalendar('startDate',false,'startDateImg');
  showCalendar('endDate',false,'endDateImg');
}
function doSearch(){
  var query = "";
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
  query = $("from1").serialize();
 // $("from1").subject.value = query;
  //alert(query);
  location =  contextPath + "/core/funcs/diary/info/userSearch.jsp?" + query;
}
</script>
<title>员工工作日志查询</title>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3"> 员工日志查询</span>
    </td>
  </tr>
</table>
<br>
<form name="from1" id="from1">
<table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableData">起始日期：</td>
      <td class="TableData"><input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="startDateImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">截止日期：</td>
      <td class="TableData"><input type="text" name="endDate" id="endDate"  size="10" maxlength="10" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer"  id="endDateImg">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">日志类型：</td>
      <td class="TableData">
      <select name="diaType" class="BigSelect">
      	<option value="">所有类型</option>
        <option value="1">工作日志</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">日志标题：</td>
      <td class="TableData"><input type="text" name="subject" class="BigInput" size="40"></td>
    </tr> 
    <tr>
  	  <td nowrap class="TableData">日志作者：</td>
      <td class="TableData">
         <input type="hidden" name="toId1" id="toId1" value="" />
        <textarea id="toId1Desc"  rows="2" cols=30 readOnly wrap="yes" class="BigStatic"></textarea>
        <a href="#" class="orgAdd" onClick="selectUser(['toId1', 'toId1Desc'],4);">添加</a>
        <a href="#" class="orgClear" onClick="Clear('toId1','toId1Desc')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">查询范围（部门）：</td>
      <td class="TableData">
        <input type="hidden" name="toId" id="dept" value="">
        <textarea cols=30 id="deptDesc"  rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept(['dept', 'deptDesc'],4);">添加</a>
       <a href="javascript:;" class="orgClear" onClick="Clear('dept','deptDesc')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">查询范围（角色）：</td>
      <td class="TableData">
        <input type="hidden" name="privId" id="role" value="">
        <textarea cols=30  id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole(['role', 'roleDesc'],4);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="Clear('role', 'roleDesc')">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData">查询范围（人员）：</td>
      <td class="TableData">
        <input type="hidden" name="copyToId" id="toId2" value="">
        <textarea cols=30  rows=2 id="toId2Desc"  class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['toId2', 'toId2Desc'],4);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="Clear('toId2', 'toId2Desc')">清空</a>
      </td>
    </tr>  
    <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
        <input type="button" value="查询" class="SmallButton" title="进行查询" onclick="doSearch()">&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>