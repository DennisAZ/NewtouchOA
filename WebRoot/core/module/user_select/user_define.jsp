<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String MODULE_ID = request.getParameter("MODULE_ID");
  String FORM_NAME = request.getParameter("FORM_NAME");
  String MANAGE_FLAG = request.getParameter("MANAGE_FLAG");
  String USER_PRIV = request.getParameter("USER_PRIV");
  String POST_PRIV = request.getParameter("POST_PRIV");
  String PRIV_NAME = request.getParameter("PRIV_NAME");
  String POST_DEPT = request.getParameter("POST_DEPT");
  String GROUP_ID = request.getParameter("GROUP_ID");
  String GROUP_NAME = request.getParameter("GROUP_NAME");
  String EVECTION_ID_STR = request.getParameter("EVECTION_ID_STR");
  String USER_ID = request.getParameter("USER_ID");
  String USER_NAME = request.getParameter("USER_NAME");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script src="/inc/js/utility.js"></script>
<script src="/inc/js/user_select.js"></script>
<script Language="JavaScript">
var parent_window = "";
var to_id = "<%=TO_ID%>";
var to_name = "<%=TO_NAME%>";
function click_user()
{}
function add_all()
{}
function del_all()
{}
</script>
</head>
<body class="" topmargin="0" leftmargin="0" onload="">

<!--
include_once("inc/my_priv.php");

if($MODULE_ID=="2")
   $EXCLUDE_UID_STR=my_exclude_uid();

 $EXCLUDE_UID_STR = td_trim($EXCLUDE_UID_STR);
//============================ 显示已定义用户 =======================================
$query = "SELECT * from USER_GROUP where GROUP_ID='$GROUP_ID'";
$cursor= exequery($connection,$query);
if($ROW=mysql_fetch_array($cursor))
{
   $GROUP_NAME=$ROW["GROUP_NAME"];
   $USER_STR=$ROW["USER_STR"];
}
-->
<table class="TableBlock" width="100%">
<tr class="TableHeader">
  <td colspan="2" align="center"><%="自定义组"%></td>
</tr>
<!-- 
if($USER_STR=="")
{
-->
<tr class="TableControl">
  <td style="cursor:pointer" align="center" colspan="2">尚未添加用户</td>
</tr>
<!-- 
   exit;
}
-->
<tr class="TableControl">
  <td onclick="javascript:add_all();" style="cursor:pointer" align="center" colspan="2">全部添加</td>
</tr>
<tr class="TableControl">
  <td onclick="javascript:del_all();" style="cursor:pointer" align="center" colspan="2">全部删除</td>
</tr>
<!-- 
$query = "SELECT UID,USER_ID,USER_NAME,USER.DEPT_ID,DEPT_NAME,PRIV_NO from USER,USER_PRIV,oa_department where USER.USER_PRIV=USER_PRIV.USER_PRIV and USER.DEPT_ID=DEPARTMENT.DEPT_ID and NOT_LOGIN!='1' and find_in_set(USER_ID,'$USER_STR')";
if($DEPT_PRIV=="3"||$DEPT_PRIV=="4")
   $query .= " and find_in_set(USER.USER_ID,'$USER_ID_STR')";
if($EXCLUDE_UID_STR!="")
   $query .= " and USER.UID not in ($EXCLUDE_UID_STR)";
$query .= " order by DEPT_NO,PRIV_NO,USER_NO,USER_NAME";
$cursor= exequery($connection,$query);
while($ROW=mysql_fetch_array($cursor))
{
   $UID=$ROW["UID"];
   $USER_ID=$ROW["USER_ID"];
   $USER_NAME=$ROW["USER_NAME"];
   $DEPT_ID_I=$ROW["DEPT_ID"];
   $DEPT_NAME=$ROW["DEPT_NAME"];
   $PRIV_NO_I=$ROW["PRIV_NO"];
   
   if(!is_dept_priv($DEPT_ID_I, $DEPT_PRIV, $DEPT_ID_STR, true) || ($ROLE_PRIV=="0" && $PRIV_NO_I<=$MY_PRIV_NO || $ROLE_PRIV=="1" && $PRIV_NO_I< $MY_PRIV_NO || $ROLE_PRIV=="3" && !find_id($PRIV_ID_STR, $PRIV_NO_I)))
      continue;
-->
<tr class="TableData" onclick="javascript:click_user('<?=$USER_ID?>')" style="cursor:pointer" align="center">
  <td id="<%=USER_ID%>" title="<%=USER_NAME%>" class="menulines"><%="OA管理员"%><span id="UID" title="<%="USER_ID"%>" style="color:#FF0000;"><%out.println("(在线)");%></span></td><td><%="系统管理员"%></td>
</tr>
<!-
}
-->
</table>
</body>
</html>