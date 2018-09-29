<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>信息资源管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript"><!--
function tijiao(){
  $("form1").submit();
}

--></script>
<%
	String seqId = (String)request.getParameter("seqId");
  String number = (String)request.getParameter("number");
%>
</head>
<body topmargin="5" class="bodycolor">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img width="22" height="20" src="<%=imgPath%>/system.gif"><span class="big3">添加值域</span>
    </td>
  </tr>
</tbody></table>
<center>
  <form  id="form1" name="form1" action="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateElementAct/addvalue.act" method="get">
  <table width="40%" align="center" class="TableBlock">
 
  <tr class="TableData">
         <td><b>值域编号:</b>
          </td> 
          <td nowrap="" align="center"><input type="text" name="value_id" id="value_id" value="<%=number%>" readonly/>
          </td>
        </tr>
     <tr class="TableData" >
          <td noWrap='nowrap'>
			<b>值域名称:</b>
          </td>
          <td nowrap="" align="center">
          <input type="text" name="value_name" id="value_name"/>
          </td>
        </tr>
  <tr class="TableData"> <!-- onclick="javascript:tijiao();return false;" -->
  <td colspan=2 noWrap='nowrap'><input class="BigButton" type="submit"  value="提交">&nbsp;&nbsp;&nbsp;
  <input class="BigButton" type="reset" value="清空">
  </td>
  </tr>
  </table>
  <input type="hidden" name="seqId" value="<%=seqId %>"/>
  </form>
  
  <div><input type="button" onclick="window.history.go(-1);" name="back" class="BigButton" value="返回"></div>
  
</center>
</body>
</html>