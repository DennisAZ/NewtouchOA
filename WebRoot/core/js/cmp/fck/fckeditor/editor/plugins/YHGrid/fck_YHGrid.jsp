<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置表</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script src="../../dialog/common/fck_dialog_common.js" type="text/javascript"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/grid.js" ></script>
<script language="javascript" src="YHGrid.js"></script></head>		
<body scroll="no" style="OVERFLOW: hidden">
		<table height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
			<tr>
				<td>
					<span class="big">表名：</span>
					<input id="tableName"   class="BigInput"  name="tableName" type="text">
					<span style="color:red">*</span>
		    	</td>
			</tr>
			<tr>
				<td>
					<span class="big">绑定的容器：</span>
					<input id="bindId"  class="BigInput"  name="bindId" type="text">
					<span style="color:red">*</span>
		    	</td>
			</tr>
			<tr>
				<td>
					<span class="big">请求地址：</span>
					<input id="tableUrl"  class="BigInput"  name="tableUrl" type="text">
					<span style="color:red">*</span>
		    	</td>
			</tr>
			<tr>
				<td>
					<span class="big">最多显示：</span>
					<input id="numOfPage"  style="ime-mode:Disabled" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"   class="BigInput"  name="numOfPage" type="text"><span class="big">条</span>
					<span style="color:red">*</span>
		    	</td>
			</tr>
			<tr>
				<td>
					<span class="big">表头信息</span>
					<div id="headMessage">
					<div><input value='增加' onclick="addHeadMessage('','','','')" type="button"/></div>
					 <table cellspacing="1" cellpadding="3"><tbody id="messageTbody"></tbody></table>
					</div>
		    	</td>
			</tr>
			<tr>
				<td>
					<span class="big">操作标题 :<input type="text" value="操作" id="title" name="title"/></span>
					<div id="operateMessage">
					<div><input value='增加' onclick="addOperate('','','')" type="button"/></div>
					 <table cellspacing="1" cellpadding="3"><tbody id="operateTbody"></tbody></table>
					</div>
		    	</td>
			</tr>
		</table>
	</body>
</html>