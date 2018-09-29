<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>工作流</title>
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
<script type="text/javascript">
function tijiao(){
  $("adminname").submit();
}
</script>
<%
 String number = request.getParameter("number");
 String valueName = request.getParameter("valueName");
 String aid = request.getParameter("aid");
 String pId = request.getParameter("pId"); 
%>
</head>
<body topmargin="5" class="bodycolor">


<center>
  <form id="adminname" name="adminname" action="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateElementAct/updatevalue.act" method="post">
  <table width="40%" align="center" class="TableBlock">
  <tr class="TableData">
         <td><b>值域编号:</b>
          </td> 
          <td nowrap="" align="center"><input type="text" value="<%=number%>" name="valueId" id="valueId"/>
          </td>
        </tr>
     <tr class="TableData">
          <td>
			<b>值域名称:</b>
          </td>
          <td nowrap="" align="center">
          <input type="text" value="<%=valueName%>" name="valueName"  id="valueName"/>
          </td>
        </tr>
  <tr class="TableData">
  <td colspan=2 noWrap='nowrap'><input class="BigButton" type="button" onclick="javascript:tijiao();return false;" value="提交">&nbsp;&nbsp;&nbsp;
  <input class="BigButton" type="reset" value="清空">
  </td>
  </tr>
  </table>
  <input type="hidden" id="aid" name="aid" value="<%=aid %>"/>
  <input type="hidden" id="pId" name="pId" value="<%=pId %>"/>
  </form>
  
  <div><input type="button" onclick="history.back();" name="back" class="BigButton" value="返回"></div>
  
</center>
</body>
</html>