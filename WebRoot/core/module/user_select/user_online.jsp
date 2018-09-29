<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.menulines{}
</style>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
</head>
<script src="/inc/js/utility.js"></script>
<script src="/inc/js/user_select.js"></script>
<script Language="JavaScript">
/*
var parent_window = getOpenner();
var to_form = parent_window.<!--=$FORM_NAME-->;
var to_id =   to_form.<!--=$TO_ID-->;
var to_name = to_form.<!--=$TO_NAME-->;
*/
</script>
<body class="" topmargin="0" leftmargin="0" onload="">

<!--
if(count($SYS_ONLINE_USER)==0)
{
   Message("","无在线人员!","blank");
   exit;
}
-->
<table class="TableBlock" width="100%">
<tr class="TableHeader">
  <td colspan="2" align="center"><b>全部在线人员</b></td>
</tr>
<tr class="TableControl">
  <td onclick="javascript:add_all();" style="cursor:pointer" align="center" colspan="2">全部添加</td>
</tr>
<tr class="TableControl">
  <td onclick="javascript:del_all();" style="cursor:pointer" align="center" colspan="2">全部删除</td>
</tr>
<!--
include_once("inc/my_priv.php");

if($MODULE_ID=="2")
   $EXCLUDE_UID_STR=my_exclude_uid();

while(list($UID, $USER) = each($SYS_ONLINE_USER))
{
   if(!find_id($EXCLUDE_UID_STR, $UID))
      $ONLINE_UID_STR.=$UID.",";
}

$ONLINE_UID_STR = td_trim($ONLINE_UID_STR);
if($ONLINE_UID_STR != "")
{
   $query = "SELECT USER_ID,USER_NAME,USER.DEPT_ID,PRIV_NO from USER,USER_PRIV where USER.USER_PRIV=USER_PRIV.USER_PRIV and NOT_LOGIN!='1' and UID in ($ONLINE_UID_STR)";
   if($DEPT_PRIV=="3"||$DEPT_PRIV=="4")
      $query .= " and find_in_set(USER.USER_ID,'$USER_ID_STR')";
   $query .= " order by PRIV_NO,USER_NO,USER_NAME";
   $cursor= exequery($connection,$query);
   while($ROW=mysql_fetch_array($cursor))
   {
      $USER_ID=$ROW["USER_ID"];
      $USER_NAME=$ROW["USER_NAME"];
      $DEPT_ID_I=$ROW["DEPT_ID"];
      $PRIV_NO_I=$ROW["PRIV_NO"];
      
      if(!is_dept_priv($DEPT_ID_I, $DEPT_PRIV, $DEPT_ID_STR, true) || ($ROLE_PRIV=="0" && $PRIV_NO_I<=$MY_PRIV_NO || $ROLE_PRIV=="1" && $PRIV_NO_I< $MY_PRIV_NO || $ROLE_PRIV=="3" && !find_id($PRIV_ID_STR, $PRIV_NO_I)))
         continue;
-->
<tr class="TableData" onclick="javascript:click_user('<%="USER_ID"%>')" style="cursor:pointer" align="center">
  <td class="menulines" id="<%="USER_ID"%>" title="<%="USER_NAME"%>"><%="系统管理员"%></td><td><%="OA管理员"%></td>
</tr>
<!--
   }
}
-->
</table>
</body>
</html>