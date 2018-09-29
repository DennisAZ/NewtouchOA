<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<script type="text/javascript">
function gotos(){
	window.location.href= contextPath + "/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/findOfficeDepositoryInfo.act";
	//return false;
}
</script>
<title>新建办公用品库</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
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

</head>
<body topmargin="5" class="bodycolor">
<div align="center" id="returnDiv" style="">
 <table class="MessageBox" align="center" width="290" >
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt"><span>  您的申请登记已提交！</span>  </div>
    </td>
  </tr>
</table>
 </div>
   <br><center><input type="button" onclick="history.back();" value="返回" class="BigButton"></center>
</body>
</html>