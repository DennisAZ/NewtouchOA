<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script src="/inc/js/utility.js"></script>
<script src="/inc/js/user_select.js"></script>
<body class="" topmargin="5" leftmargin="2" onload="">
<table class="TableBlock" width="100%">
<tr class="TableHeader">
  <td align="center">人员查询</td>
</tr>
<tr class="TableData">
  <td onclick="" style="cursor:pointer" align="center">全部添加</td>
</tr>
<tr class="TableData">
  <td onclick="" style="cursor:pointer" align="center">全部删除</td>
</tr>
<?
    }
?>
<tr class="TableData" onclick="javascript:click_user('<?=$USER_ID?>')" style="cursor:pointer" align="center">
  <td class="menulines" id="<?=$USER_ID?>" title="<?=$USER_NAME?>">
  <span id="attend_status_<?=$UID?>" title="<?=$USER_ID?>" style="color:#FF0000;">
  </span>
  </td>
</tr>

<tr class="TableData">
  <td align="center">未查询到用户</td>
</tr>
</table>
</body>
</html>