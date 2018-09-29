<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/core/inc/t6.jsp" %>
<title>zTree</title>
<script type="text/javascript" src="../../js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../../js/ui/jquery-ui-1.8.17.custom.min.js"></script>
<script type="text/javascript" src="niceforms.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/style.css"/>
<link rel="stylesheet" type="text/css" href="niceforms-default.css"/>
<script type="text/javascript">
</script>
<style>

</style>
</head>
<body>

	<form action="vars.php" method="post" class="niceform">
	<div class="formctn">
	      <fieldset>
	<table>
		<tr>
			<td>
				  <dl>
	          <dt><label for="code">代码:</label></dt>
	          <dd><input type="text" name="code" id="code" size="32" maxlength="128" /></dd>
	        </dl>
				  <dl>
	          <dt><label for="name">名称:</label></dt>
	          <dd><input type="text" name="name" id="name" size="32" maxlength="128" /></dd>
	        </dl>
				  <dl>
	          <dt><label for="type">类别:</label></dt>
	          <dd><select>
	            <option>
	              现金
	            </option>
	            <option>
	              银行存款
	            </option>
	            <option>
	              存货
	            </option>
	          </select></dd>
	        </dl>
				  <dl>
	          <dt><label for="email">辅助核算:</label></dt>
	          <dd><input type="text" name="email" id="email" size="32" maxlength="128" /></dd>
	        </dl>
			</td>
			<td>
				  <dl>
	          <dt><label for="code">外币核算:</label></dt>
	          <dd><input type="text" name="code" id="code" size="32" maxlength="128" /></dd>
	        </dl>
				  <dl>
	          <dt><label for="qmth">期末调汇:</label></dt>
	          <dd><input type="checkbox" name="qmth" id="qmth" size="32" maxlength="128" /></dd>
	        </dl>
				  <dl>
	          <dt><label for="type">余额方向:</label></dt>
	          <dd><label for="jie">借</label><input id="jie" name="yue" type="radio">
	           <label for="dai">贷</label><input id="dai" name="yue" type="radio">
	        </dl>
				  <dl>
	          <dt><label for="email">辅助核算:</label></dt>
	          <dd><input type="text" name="email" id="email" size="32" maxlength="128" /></dd>
	        </dl>
			</td>
		</tr>
	</table>
	    </fieldset>
    <fieldset class="action">
      <input type="submit" name="submit" id="submit" value="保存" />
      <input type="button" name="submit" id="submit" value="复制" />
      <input type="button" name="submit" id="submit" value="重置" />
    </fieldset>
</div>
</form>
</body>
</html>