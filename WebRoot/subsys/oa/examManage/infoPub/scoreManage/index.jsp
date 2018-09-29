<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考试数据查询</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/infoPub.js"></script>
<script type="text/javascript">
function doInit() {
  //时间
  var parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'beginDate1',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date3'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'endDate1',
      property:{isHaveTime:false}
      ,bindToBtn:'date4'
  };
  new Calendar(parameters);
  doTitle2();
}
//查询
function selectList() {
  var pars = $('form1').serialize() ;
  var param = encodeURI(pars);
  var requestURL = contextPath + "/subsys/oa/examManage/infoPub/scoreManage/list.jsp?" + param;
  window.location.href = requestURL;
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 考试信息查询</span>
    </td>
  </tr>
</table>
<br>
 <form id="form1" method="post" name="form1" >
<table class="TableBlock" align="center" >
  <tr>
    <td nowrap class="TableContent">考试名称： </td>
    <td nowrap class="TableData">
        <input type="text" name="flowTitle" id="flowTitle" class="BigInput">
    </td>
  </tr>
  <tr>
      <td nowrap class="TableContent">参加考试人：</td>
      <td class="TableData">
        <input type="hidden" name="participant" id="participant" value="">
        <textarea cols=40 name="participantDesc" id="participantDesc" rows=5 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['participant', 'participantDesc'])">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('participant').value='';$('participantDesc').value='';">清空</a>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent">试卷：</td>
      <td class="TableData">
         <select name="paperId" id="paperId">
         <option value="" selected></option>
        </select>
      </td>
    </tr>
   <tr>
    <td nowrap class="TableContent">生效日期： </td>
    <td nowrap class="TableData">
        <input type="text" name="beginDate" id="beginDate" class="BigInput" size="15" maxlength="10" value="" readonly>&nbsp;
        <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
       <input type="text" name="beginDate1" id="beginDate1" class="BigInput" size="15" maxlength="10" value="" readonly>&nbsp;
       <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
   </tr>
      <tr>
    <td nowrap class="TableContent">终止日期： </td>
    <td nowrap class="TableData">
        <input type="text" name="endDate" id="endDate" class="BigInput" size="15" maxlength="10" value="" readonly="readonly">&nbsp;
        <img id="date3" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
        <input type="text" name="endDate1" id="endDate1" class="BigInput" size="15" maxlength="10" value="" readonly>&nbsp;
        <img id="date4" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
     </td>
   </tr>
   <tr>
      <td nowrap class="TableContent"> 查询范围：</td>
      <td class="TableData">
         <input type="radio" name="cd" id="cd" value="1">全部&nbsp;&nbsp;
         <input type="radio" name="cd" id="cd" value="2" checked>已终止&nbsp;&nbsp;
      </td>
    </tr>
   <tr class="TableControl">
    <td nowrap align="center" colspan="2">
     <input type="button" value="查询" class="BigButton" title="模糊查询"  onClick="javascript:selectList();">
    </td>
   </tr>
</table>
</form>
</body>
</html>