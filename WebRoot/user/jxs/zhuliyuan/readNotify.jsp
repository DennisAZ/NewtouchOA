<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<%
SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM");
String date=curTime.format(new Date());
String flag = request.getParameter("flag");
if ( "".equals(flag ) || flag == null) {
  flag = "0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看通知单</title>
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

<!-- 文件上传 -->
<link href="<%=cssPath%>/cmp/swfupload.css" rel="stylesheet"	type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/js/welfaremanageLogic.js"></script>
<script type="text/javascript">

function doInit(){

	setDate();	

}


//日期
function setDate(){
  var date1Parameters = {
     inputId:'repairDate',
     property:{isHaveTime:false}
     ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

}

</script>
</head>
<body onLoad="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle">查看通知单&nbsp;&nbsp;    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action="<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/addWelfareInfo.act"  method="post" name="form1" id="form1" >
<table class="TableBlock" width="50%" align="center">
	<tr>
		<td nowrap class="TableData">送修通知单号：</td>
      <td class="TableContent" colspan="3">
      213242423 
      </td>
	</tr>
	<tr>
    <td nowrap class="TableData">装备名称：</td>
     <td class="TableContent" >
    坦克
      </td>
	 <td class="TableContent" ></td>
	 <td class="TableContent" ></td>
  </tr>
      <td nowrap class="TableData">装备编号：</td>
     <td class="TableContent" >
  dse32423
     </td>
	 <td class="TableContent" ></td>
	 <td class="TableContent" ></td>
  </tr>
	<tr>
    <td nowrap class="TableData">入所时间：</td>
     <td class="TableContent" colspan="1">
      2011-02-23  </td>
    <td nowrap class="TableData">物理级别：</td>
     <td class="TableContent" colspan="1">
     中
     </td>
  </tr>
	<tr>
    <td nowrap class="TableData">送修部队：</td>
     <td class="TableContent" >
   维修队1
     </td>
    <td nowrap class="TableData">承修单位承办人：</td>
     <td class="TableContent" >
      承办单位1
       </td>
  </tr>
  <tr>
    <td nowrap class="TableData">送修部队承办人：</td>
     <td class="TableContent" >
       张三
         </td>
	    <td nowrap class="TableData">职务：</td>
     <td class="TableContent" >
    维修员
     </td>
  </tr> 
  </tr> 
      <td nowrap class="TableData">承修部门：</td>
     <td class="TableContent" >
      维修部门   </td>
	 <td class="TableData">电话：</td>
	  <td class="TableContent" >
      	010-343253
     </td>
  
  </tr> 
     
</table>
</form>
 <div align="center">
 <br>
 <br>
  <input type="button" value="返  回" onclick="history.go(-1)" class="BigButton"></input>
  <% if ("1".equals(flag)) { %>
  <input type="button" value="审批通过" onclick="window.close()" class="BigButton">
  <% } %>
 </div>
</body>
</html>