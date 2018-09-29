<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>My JSP 'newList.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		<script type="text/javascript" src="table.js">
			
	</script>
		<form action="MyJsp.jsp" name="dataList" onSubmit='return check()'>
			<input type=hidden name="id" id="count" value=0>
			<table border=1 id="table" bgcolor=ddddd>
				<tr>
					<td>
						表编码
					</td>
					<td>
						<input type=text name=tableNo1 size=7>
					</td>
					<td>
						表名称
					</td>
					<td>
						<input type=text name=tableName size=7>
					</td>
				</tr>
				<tr>
					<td>
						表描述
					</td>
					<td>
						<input type=text name=tableDesc size=7>
					</td>
					<td>
						表类型
					</td>
					<td>
						<input type=text name=categoryNo size=7>
					</td>
				</tr>
			</table>
			<input type=button value="添加子表" onclick="add()">
			<table border=1 id="tableF" bgcolor=ddddd>
				<thead>
					<tr>
						<td>
							字段编码
						</td>
						<td>
							字段名称
						</td>
						<td>
							字段描述
						</td>
						<td>
							按钮
						</td>
					</tr>
				</thead>
				<tbody id="tbody">
				</tbody>
			</table>

			<div id="table_div" style="display:none" name="tableN_div">
				<input type="hidden" id="divId" value="" />
				<table border=1 id="tableDiv" bgcolor=ddddd>
					<thead>
						<tr>
							<td>
								字段编码
							</td>
							<td>
								<input type=text id="fieldNo" name="fieldNo" value="">
							</td>


							<td>
								字段名称
							</td>
							<td>
								<input type=text id="fieldName" name="fieldName" value="">
							</td>
						</tr>
						<tr>
							<td>
								字段描述
							</td>
							<td>
								<input type=text id="fieldDesc" name="fieldDesc" value="">
							</td>

							<td>
								参照编码
							</td>
							<td>
								<input type=text id="fkTableNo" name="fkTableNo" value="">
							</td>
						</tr>
						<tr>
							<td>
								参照编码2
							</td>
							<td>
								<input type=text id="fkTableNo2" name="fkTableNo2" value="">
							</td>

							<td>
								参照编码
							</td>
							<td>
								<input type=text id="fkRelaFieldNo" name="fkRelaFieldNo"
									value="">
							</td>
						</tr>
						<tr>
							<td>
								参照名称
							</td>
							<td>
								<input type=text id="fkNameFieldNo" name="fkNameFieldNo"
									value="">
							</td>

							<td>
								筛选条件
							</td>
							<td>
								<select id="fkFilter">
									<option value="number">
										数字
									</option>
									<option value="text">
										文本
									</option>
									<option value="data">
										日期
									</option>
									<option value="amt">
										金额
									</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								小编码
							</td>
							<td>
								<input type=text id="codeClass" name="codeClass" value="">
							</td>

							<td>
								缺省值
							</td>
							<td>
								<input type=text id="defaultValue" name="defaultValue" value="">
							</td>
						</tr>
						<tr>
							<td>
								显示数值
							</td>
							<td>
								<input type=text id="formatRule" name="formatRule" value="">
							</td>

							<td>
								格式化
							</td>
							<td>
								<input type=text id="formatRule" name="formatRule" value="">
							</td>
						</tr>
						<tr>
							<td>
								错误消息
							</td>
							<td>
								<input type=text id="errorMsrg" name="errorMsrg" value="">
							</td>

							<td>
								数位长度
							</td>
							<td>
								<input type=text id="fieldPrecision" name="fieldPrecision"
									value="">
							</td>
						</tr>
						<tr>
							<td>
								小数位数
							</td>
							<td>
								<input type=text id="fieldScale" name="fieldScale" value="">
							</td>

							<td>
								数据类型
							</td>
							<td>
								<select id="dataType" name="dataType">
									<option value="-7">
										BIT
									</option>
									<option value="-6">
										TINYINT
									</option>
									<option value="5">
										SMALLINT
									</option>
									<option value="4">
										INTEGER
									</option>
									<option value="-5">
										BIGINT
									</option>
									<option value="6">
										FLOAT
									</option>
									<option value="7">
										REAL
									</option>
									<option value="8">
										DOUBLE
									</option>
									<option value="2">
										NUMERIC
									</option>
									<option value="3">
										DECIMAL
									</option>
									<option value="1">
										CHAR
									</option>
									<option value="12">
										VARCHAR
									</option>
									<option value="-1">
										LONGVARCHAR
									</option>
									<option value="91">
										DATE
									</option>
									<option value="92">
										TIME
									</option>
									<option value="93">
										TIMESTAMP
									</option>
									<option value="-2">
										BINARY
									</option>
									<option value="-3">
										VARBINARY
									</option>
									<option value="-4">
										LONGVARBINARY
									</option>
									<option value="0">
										NULL
									</option>
									<option value="1111">
										OTHER
									</option>
									<option value="2000">
										JAVA_OBJECT
									</option>
									<option value="2001">
										DISTINCT
									</option>
									<option value="2002">
										STRUCT
									</option>
									<option value="2003">
										ARRAY
									</option>
									<option value="2004">
										BLOB
									</option>
									<option value="2005">
										CLOB
									</option>
									<option value="2006">
										REF
									</option>
									<option value="70">
										DATALINK
									</option>
									<option value="16">
										BOOLEAN
									</option>
								</select>
						</tr>
						<tr>
							<td>
								主键
							</td>
							<td>
								<select id="isPrimKey" name="isPrimKey">
									<option value="1">
										是
									</option>
									<option value="0">
										否
									</option>
								</select>
							</td>

							<td>
								自增
							</td>
							<td>
								<select id="isIdentity" name="isIdentity">
									<option value="1">
										是
									</option>
									<option value="0">
										否
									</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								显示长度
							</td>
							<td>
								<input type="text" id="displayLen" name="displayLen">
							</td>

							<td>
								必填
							</td>
							<td>
								<select id="isMustFill" name="isMustFill">
									<option value="1">
										是
									</option>
									<option value="0">
										否
									</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>
								<input type="button" name="saveDiv" value="保存" onclick="save()">
							</td>
							<td>
								<input type="button" name="close" value="关闭"
									onclick="closeDiv()">
							</td>
						</tr>
					</thead>
					<tbody id="tbodyDiv">
					</tbody>
				</table>
			</div>
			<br>
			<input type=submit name=submit value="提交">
		</form>
	</body>
</html>
