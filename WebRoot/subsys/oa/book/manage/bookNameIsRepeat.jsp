<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>图书名称已存在</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
</script>
 <% String bookName = request.getParameter("bookName")== null ? "" :request.getParameter("bookName");
 System.out.println("3+::: "+bookName.equals(""));
    String bookNo = request.getParameter("bookNo") == null ? "" :  request.getParameter("bookNo");
 %>
 
</head>
<body class="bodycolor">
<div align="center" style="" id="noData">
	  <table width="300" class="MessageBox">
			  <tbody>
					  <tr>
							 <td class="msg info" id="msgInfo">图书名称   <%=bookName %>  已存在</td>
					  </tr>
			  </tbody>
	  </table>
	  </div>
	 <center>
	<table>
	  <tbody>
            <tr>
						   <td>
							    <input type="button" onclick="history.back();" name="back" class="BigButton" value="返回">
						   </td>
					  </tr>
			  </tbody>
	</table>
  </center>
</body>
</html>