<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
		<!-- 封装表单的数据提交 -->
		<script type="text/javascript"src="<%=contextPath%>/springViews/js/jquery-1.4.2.js">
		jQuery.noConflict();
		</script>
		<script type="text/javascript" src="<%=contextPath%>/springViews/js/json.js" /></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
	<script type="text/javascript">
	function short(){
	var shortName=jQuery("#shortName").val();
		parent.product.location='selectPro.jsp?shortName='+shortName;
	}
	</script>
	</head>
	<body topmargin="5" >
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				<td class="Big">
					<img src="<%=imgPath%>/infofind.gif" align="absMiddle">
					<span class="big3">&nbsp;
						产品选择</span>
				</td>
				<td><input type="text" id="shortName" name="shortName" value=""></td>
				<td><input type="button" value="搜索"   onclick="short()"></td>
			</tr>
			<tr>
				<td colspan="2" align="center"  style="color: red">&nbsp&nbsp&nbsp&nbsp&nbsp请输入产品简称（产品名称首字母）或者产品名称查询</td>
			</tr>
		</table>
		<br>
	</body>
</html>