<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件检索</title>
<script>
function CheckForm()
{
   if(document.form1.ATTACHMENT_NAME.value==""&&document.form1.key.value=="")
   { alert("请指定至少一个查询条件！");
     return (false);
   }

   return true;
}

</script>
</head>
<body class="bodycolor" topmargin="5" onload="document.form1.ATTACHMENT_NAME.focus();">
<?
$query = "select * from NETDISK where DISK_ID='$DISK_ID'";
$cursor=exequery($connection,$query);
if($ROW=mysql_fetch_array($cursor))
   $DISK_NAME=$ROW["DISK_NAME"];
?>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/images/menu/infofind.gif" align="absmiddle"><b><span class="Big1"> <?=$DISK_NAME?>/<?=$URL?> - 文件检索</span></b><br>
    </td>
  </tr>
</table>

<br>

<table class="TableBlock" width="450" align="center">
<form action="search.php" name="form1" onsubmit="return CheckForm();">
  <tr class="TableData">
      <td nowrap align="center">文件名包含文字：</td>
      <td nowrap><input type="text" name="ATTACHMENT_NAME" class="BigInput" size="20"></td>
  </tr>
  <tr class="TableData">
      <td nowrap align="center">文件内容包含文字：</td>
      <td nowrap><input type="text" name="key" class="BigInput" size="20"><br>
      默认只检查txt及html文件
      </td>
  </tr>
  <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="hidden" name="DISK_ID" value="<?=$DISK_ID?>">
          <input type="hidden" name="URL" value="<?=urlencode($URL)?>">
          <input type="hidden" name="ORDER_BY" value="<?=$ORDER_BY?>">
          <input type="hidden" name="ASC_DESC" value="<?=$ASC_DESC?>">
          <input type="hidden" name="start" value="<?=$start?>">
          <input type="submit" value="查询" class="BigButton" title="进行文件查询">&nbsp;&nbsp;
          <input type="button" value="返回" class="BigButton" onclick="history.back();">
      </td>
  </tr>
</table>
</form>

</body>
</html>