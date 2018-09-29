<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设备查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">

function doInit(){
 　var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "mrId"
   , tableName: "MEETING_ROOM"
   , codeField: "SEQ_ID"
   , nameField: "MR_NAME"
   , value: "<%=0%>", isMustFill: "0"
   , filterField: " "
   , filterValue: ''
   , order: ""
   , reloadBy: ""
   , actionUrl: ""
   });
  mgrSec.loadData();
  mgrSec.bindData2Cntrl();
}

function doSubmit(){
  var query = $("form1").serialize();
  location = "<%=contextPath%>/subsys/oa/meeting/equipment/search.jsp?"+query;
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/infofind.gif"><span class="big3"> 设备查询</span>
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" method="post" name="form1" id="form1" action="">
<table class="TableBlock" width="550" align="center">
    <tr>
      <td nowrap class="TableData" width="100"> 设备编号：</td>
      <td class="TableData">
        <input type="text" name="equipmentNo" id="equipmentNo" maxlength="100" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 设备名称：</td>
      <td class="TableData">
        <input type="text" name="equipmentName" id="equipmentName" maxlength="100" class="BigInput">
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData" width="100"> 设备状态：</td>
      <td class="TableData">
        <select name="equipmentStatus" id="equipmentStatus">
          <option value="1">可用</option>
          <option value="0">不可用</option>         
        </select>
      </td>
    </tr>  
    <tr>
      <td nowrap class="TableData" width="100"> 会议室：</td>
      <td class="TableData">
     <select name="mrId" id="mrId" >
      </select>
      </td>
    </tr> 
    <tr>
      <td nowrap class="TableData" width="100"> 设备描述：</td>
      <td class="TableData">
        <input type="text" name="remark" id="remark" maxlength="100" class="BigInput">
      </td>
    </tr>   
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="确定" class="BigButton" onclick="doSubmit();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>