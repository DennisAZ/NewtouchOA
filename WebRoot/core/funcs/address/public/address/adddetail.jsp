<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>联系人详情</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getDetail.act";
  var rtJson = getJsonRs(url, "seqId="+seqId);
  if(rtJson.rtState == "0"){
    $("telNoHomes").innerHTML = rtJson.rtData.telNoHome;
    bindJson2Cntrl(rtJson.rtData);
    $("telNoHome").value = rtJson.rtData.telNoHome;
    $("psnName2").innerHTML = rtJson.rtData.psnName;
    $('birthday').innerHTML = rtJson.rtData.birthday.substr(0,10);
    
    if(rtJson.rtData.sex == "0"){
      document.getElementById("sex").innerHTML = "男";
    }else if(rtJson.rtData.sex == "1"){
      document.getElementById("sex").innerHTML = "女";
    }else{
      document.getElementById("sex").innerHTML = "";
    }
  }else{
	alert(rtJson.rtMsrg); 
 }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;联系人详情 －&nbsp;<span id="psnName2"></span></span><br>
    </td>
    </tr>
</table>

 <table class="TableBlock" width="100%" align="center">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.address.data.YHAddress"/>
    <tr>
      <td nowrap class="TableHeader" colspan="5"><b>&nbsp;联系人信息</b></td>
    </tr>
    <tr>
    	<!-- 名片单元格 -->
    	<td class="TableData" width="40%" rowspan="6" style="background-color:lightcyan;">
    		&nbsp;&nbsp;&nbsp;<u><font style="FONT-SIZE:9pt;" color="darkred"><b><span id="psnName"></span></b></font></u><br>
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;手机：</b></font><font color=blue><span id="mobilNo"></span></font><br>
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;电子邮件：</b></font><font color=blue><span id="email"></span></font><br>
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;QQ号码：</b></font><font color=blue><span id="oicqNo"></span></font><br>
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;MSN：</b></font><font color=blue><span id="icqNo"></span></font><br>
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;工作电话：</b></font><font color=blue><span id="telNoDept"></span></font><br>
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;工作传真：</b></font><font color=blue><span id="faxNoDept"></span></font><br>
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;家庭电话：</b></font><font color=blue><span id="telNoHomes"></span></font>&nbsp;&nbsp;
    		<font color=midnightblue><b>&nbsp;&nbsp;&nbsp;小灵通：</b></font><font color=blue><span id="bpNo"></span></font><br>
    	</td>
      <td nowrap class="TableData" width="100"> 性别：</td>
      <td class="TableData" id="sex"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 生日：</td>
      <td class="TableData" id="birthday"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 昵称：</td>
      <td class="TableData" id="nickName"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 职务：</td>
      <td class="TableData" id="ministration"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 配偶：</td>
      <td class="TableData" id="mate"></td>    
    </tr>
    <tr>
      <td nowrap class="TableData"> 子女：</td>
      <td class="TableData" id="child"></td>    
    </tr>
 </table>
 <table class="TableBlock" width="100%" align="center">
    <tr>
      <td nowrap class="TableHeader" colspan="5"><b>&nbsp;工作单位信息</b></td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 单位名称：</td>
      <td class="TableData" colspan=4 id="deptName"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 单位地址：</td>
      <td class="TableData" colspan=4 id="addDept"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 单位邮编：</td>
      <td class="TableData" colspan=4 id="postNoDept"></td>
    </tr>
  </table>
 <table class="TableBlock" width="100%" align="center">
    <tr>
      <td nowrap class="TableHeader" colspan="5"><b>&nbsp;家庭信息</b></td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 家庭住址：</td>
      <td class="TableData" colspan=4 id="addHome"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 家庭邮编：</td>
      <td class="TableData" colspan=4 id="postNoHome"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 家庭电话：</td>
      <td class="TableData" colspan=4 id="telNoHome" name="telNoHome"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备注：</td>
      <td class="TableData" colspan=4 id="notes"></td>
    </tr>
    <tr>
      <td nowrap class="TableData" colspan="5">
        <?=get_field_table(get_field_text("ADDRESS",$ADD_ID))?>
      </td>
    </tr>
  </table>
  <center><input type="button" value="打印" class="BigButton" onclick="document.execCommand('Print');" title="直接打印表格页面">&nbsp;&nbsp;&nbsp;&nbsp;
  <input type="button" value="关闭" class="BigButton" onClick="window.close();"></center>

</body>
</body>
</html>