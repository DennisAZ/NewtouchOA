<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title></title>
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
  function CheckForm(){ //模糊查詢
     var bookNo = $("BOOK_NO").value;
     var url = contextPath + "/yh/subsys/oa/book/act/YHBookTypeEnterAct/blurFindBookType.act?bookNo="+bookNo;
     document.form1.action = url;
     document.form1.submit();
     return true;
   }
</script>
</head>
<body bgcolor="#E8E8E8" topmargin="5">
	<center>
	 <form  method="post" action="#" target="bookno_info" id="form1" name="form1">
		     编号/书名：
		  <input type="text" name="BOOK_NO" id ="BOOK_NO" size="10" class="BigInput">
		  <input type="hidden" name="BOOK_NAME" id ="BOOK_NAME" size="10" class="BigInput">
		  <input type="button" onclick="CheckForm();" value="模糊查询" class="BigButton">
		 </form>
	</center>

</body>
</html>