<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>钥匙盘授权</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">

</script>
</head>
<body topmargin="5">
<fieldset style="width:300px;padding-bottom:5px;" align="center">
<legend class="small" align=left>
<b><font color="black">钥匙盘管理</font></b>
</legend>
<table width="300px" border="0" align="center" cellpadding="3" cellspacing="1" class="TableList">
  <form action="lic_update.php" method="post" name="form1">
  <tr >
    <td class="TableContent">钥匙盘ID</td>
    <td class="TableData"><input name="KeyID" id="KeyID" type="text" size=20 class="BigStatic" id="KeyID" value=""/>&nbsp;
  </tr>
  <tr class="TableControl">
    <td colspan="2" align="center">
      <input name="Lic" type="hidden" id="Lic" value="">
      <input type="button" value="入库" class="BigButton" onclick="insert_key();">
      <input type="button" value="授权" class="BigButton" onclick="key_lic();">
      
      </td>
    </tr> 
  </form>
</table>

</fieldset>


<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3"> 印章授权记录</span>&nbsp;
    </td>
    <td align="right" valign="bottom" class="small1"><?=page_bar($start,$TOTAL_ITEMS,$ITEMS_IN_PAGE)?></td>
  </tr>
</table>

<table class="TableList" width="100%">
  <tr class="TableHeader">
      <td nowrap align="center">钥匙盘ID</td>
      <td nowrap align="center">使用者</td>
      <td nowrap align="center">授权时间</td>
  <tr>

  <tr class="<?=$TableLine?>">
      <td nowrap align="center"><?=$KeyID?></td>
      <td nowrap align="center"><?=$USER_NAME?></td>
      <td nowrap align="center"><?=$LIC_TIME?></td>
  <tr>  

</table>
</body>
</html>