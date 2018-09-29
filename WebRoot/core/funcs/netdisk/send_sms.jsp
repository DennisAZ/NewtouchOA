<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内部短信通知</title>
<script>
function CheckForm()
{
   if(document.getElementById('SMS_SELECT_REMIND0').checked && document.form1.SMS_SELECT_REMIND_TO_ID.value=="")
   {
      alert("请选择被提醒人员");
      return false;
   }
   if(document.form1.CONTENT.value=="")
   {
      alert("修改说明不能为空");
      return false;
   }
   return true;
}
</script>
</head>
<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/images/menu/netdisk.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 内部短信通知(通知可访问人该文件已被修改)</span>
    </td>
  </tr>
</table>
<br>
<table class="TableBlock" width="510" align="center">
  <form action="send_sms_submit.php"  method="post" name="form1" onsubmit="return CheckForm();">
   <tr>
    <td nowrap class="TableContent" width="90">文件名称：</td>
    <td class="TableData">
      <input type="text" name="FICHIER" size="27" readonly maxlength="100" class="BigStatic" value="<?=$FICHIER?>">
   </td>
   </tr>
   <tr>
    <td nowrap class="TableContent" width="90">内部短信提醒：</td>
    <td class="TableData">
      <?=sms_select_remind(17);?>
   </td>
   </tr>
   <tr>
     <td nowrap class="TableContent"> 修改说明：</td>
     <td class="TableData" colspan="1">
       <textarea name="CONTENT" class="BigInput" cols="45" rows="7"></textarea>
     </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" value="<?=$DISK_ID?>" name="DISK_ID">
      <input type="hidden" value="<?=urlencode(substr($URL,0,-1))?>" name="NETDISK_PATH_STR">
      <input type="submit" value="确定" class="BigButton">&nbsp;&nbsp;
      <input type="button" class="BigButton" value="关闭" onClick="window.close();" title="关闭窗口">
    </td>
  </form>
</table>

</body>
</html>