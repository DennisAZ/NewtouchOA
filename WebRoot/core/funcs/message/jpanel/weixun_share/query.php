<?php
include_once('function.php');
include_once("inc/conn.php");
//仅需这两句
/*
header("Content-Type: text/html; charset=utf-8");
mysql_query("set names utf8",$connection);
*/
$Key=mysql_real_escape_string(strip_tags(unEscape($Key)),$connection); //过滤HTML标签，并转义特殊字
$query="select WEIXUN_SHARE.* from WEIXUN_SHARE,USER where WEIXUN_SHARE.UID=USER.UID  and (CONTENT like '%$Key%' or USER_NAME like '%$Key%') order by WEIXUN_ID desc limit 0,8";
$cursor= exequery($connection,$query);

while($ROW=mysql_fetch_array($cursor))
{
	$WeixunList.=GetWeixun($ROW['WEIXUN_ID'],$ROW['CONTENT'],$ROW['ADDTIME'],$ROW['UID']);
}
echo $WeixunList;
?>
