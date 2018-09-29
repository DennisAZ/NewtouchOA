<?
include_once("inc/auth.php");
include_once("inc/utility_all.php");
include_once("inc/utility_org.php");
include_once("config.php");
ob_end_clean();

$PAGE_SIZE = 10;
$CUR_TIME=time();
if($STYPE=="index")
{
   $CURSORID = intval($CURSORID);
   $return = '';
   $query = "SELECT WEIXUN_ID,CONTENT,BROADCAST_IDS,WEIXUN_SHARE.UID,ADDTIME,USER_ID,USER_NAME,AVATAR,DEPT_ID from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID and WEIXUN_ID < $CURSORID order by WEIXUN_ID desc limit ".$PAGE_SIZE;
   $cursor = exequery($connection,$query);
   
   while($ROW=mysql_fetch_array($cursor))
   {
      $WEIXUN_ID=$ROW["WEIXUN_ID"];
      $CONTENT=$ROW["CONTENT"];
      $ADDTIME=$ROW["ADDTIME"];
      $BROADCAST_IDS=$ROW["BROADCAST_IDS"];
      $USER_NAME=$ROW["USER_NAME"];
      $AVATAR=$ROW["AVATAR"];
      $DEPT_ID=$ROW["DEPT_ID"];
      $DEPT_NAME = dept_long_name($DEPT_ID);
      $UID = $ROW["UID"];
      $USER_ID = $ROW["USER_ID"];
   
      $return .= showWxContent($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS);
   
   }
   echo $return; 
}else if($STYPE=="user")
{
   $UID = intval($SEARCHKEY);
   $CURSORID = intval($CURSORID);
   
   //获取个人信息
   $query = "SELECT USER_ID,USER_NAME,AVATAR,DEPT_ID from USER where UID = '$UID' limit 1";
   $cursor= exequery($connection,$query);
   if($ROW=mysql_fetch_array($cursor)){
      $USER_ID = $ROW["USER_ID"];
      $USER_NAME=$ROW["USER_NAME"];
      $DEPT_ID=$ROW["DEPT_ID"];
      $DEPT_NAME = dept_long_name($DEPT_ID); 
   }
   
   $return = '';
   $query = "SELECT WEIXUN_ID,CONTENT,ADDTIME,BROADCAST_IDS from WEIXUN_SHARE where UID = '$UID' and WEIXUN_ID < $CURSORID order by ADDTIME desc limit ".$PAGE_SIZE;
   $cursor = exequery($connection,$query);   
   while($ROW=mysql_fetch_array($cursor))
   {
      $WEIXUN_ID = $ROW["WEIXUN_ID"];
      $CONTENT = $ROW["CONTENT"];
      $ADDTIME = $ROW["ADDTIME"];
      $BROADCAST_IDS = $ROW["BROADCAST_IDS"];
      $return.= showWxContentNoUserPic($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS);      
   
   }
   echo $return; 
}else if($STYPE=="topic")
{
   $q = td_iconv(strip_tags($SEARCHKEY),'utf-8',$MYOA_CHARSET);
   $CURSORID = intval($CURSORID);

   $COUNT = 0;
   $query = "SELECT WEIXUN_ID,CONTENT,ADDTIME,BROADCAST_IDS,USER_NAME,AVATAR,DEPT_ID,USER.UID,USER_ID from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID and TOPICS IS NOT NULL and (TOPICS like '%|||||$q' or TOPICS like '%|||||$q|||||%' or TOPICS like '$q|||||%' or TOPICS='$q') and WEIXUN_ID < $CURSORID order by ADDTIME desc limit ".$PAGE_SIZE;
   $cursor= exequery($connection,$query);
   while($ROW=mysql_fetch_array($cursor))
   {
      $COUNT++;
      $WEIXUN_ID=$ROW["WEIXUN_ID"];
      $CONTENT=$ROW["CONTENT"];
      $ADDTIME=$ROW["ADDTIME"];
      $USER_NAME=$ROW["USER_NAME"];
      $AVATAR=$ROW["AVATAR"];
      $DEPT_ID=$ROW["DEPT_ID"];
      $DEPT_NAME = dept_long_name($DEPT_ID);
      $UID = $ROW["UID"];
      $USER_ID = $ROW["USER_ID"];
      $BROADCAST_IDS = $ROW["BROADCAST_IDS"];
      
      $return .= showWxContent($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS);
   }
   echo $return;
}else if($STYPE=="msg")
{

   $CURSORID = intval($CURSORID);
   $SEARCHKEY = intval($SEARCHKEY);

   $COUNT = 0;
   $query = "SELECT WEIXUN_ID,CONTENT,BROADCAST_IDS,WEIXUN_SHARE.UID,ADDTIME,USER_ID,USER_NAME,AVATAR,DEPT_ID from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID and (BROADCAST_IDS = '$SEARCHKEY' or BROADCAST_IDS like '$SEARCHKEY,%') and WEIXUN_ID < $CURSORID order by ADDTIME desc limit 0,$PAGE_SIZE";
   $cursor= exequery($connection,$query);
   while($ROW=mysql_fetch_array($cursor))
   {
      $COUNT++;
      $WEIXUN_ID=$ROW["WEIXUN_ID"];
      $CONTENT=$ROW["CONTENT"];
      $ADDTIME=$ROW["ADDTIME"];
      $USER_NAME=$ROW["USER_NAME"];
      $AVATAR=$ROW["AVATAR"];
      $DEPT_ID=$ROW["DEPT_ID"];
      $DEPT_NAME = dept_long_name($DEPT_ID);
      $UID = $ROW["UID"];
      $USER_ID = $ROW["USER_ID"];
      $BROADCAST_IDS = $ROW["BROADCAST_IDS"];
      
      $return.= showWxContentNoUserPic($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS);
   }
   echo $return;
}else if($STYPE=="atme")
{
   $UID = intval($SEARCHKEY);
   $CURSORID = intval($CURSORID);
   
   //获取个人信息
   $query = "SELECT WEIXUN_ID,CONTENT,ADDTIME,BROADCAST_IDS,USER_NAME,AVATAR,DEPT_ID,USER.UID,USER_ID from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID and WEIXUN_SHARE.UID!='$UID' and MENTIONED_IDS IS NOT NULL and FIND_IN_SET('$UID',MENTIONED_IDS) and WEIXUN_ID < $CURSORID ORDER BY ADDTIME DESC LIMIT ".$PAGE_SIZE;
   $cursor= exequery($connection,$query);
   while($ROW=mysql_fetch_array($cursor))
   {
      $COUNT++;
      $WEIXUN_ID=$ROW["WEIXUN_ID"];
      $CONTENT=$ROW["CONTENT"];
      $ADDTIME=$ROW["ADDTIME"];
      $USER_NAME=$ROW["USER_NAME"];
      $AVATAR=$ROW["AVATAR"];
      $DEPT_ID=$ROW["DEPT_ID"];
      $DEPT_NAME = dept_long_name($DEPT_ID);
      $UID = $ROW["UID"];
      $USER_ID = $ROW["USER_ID"];
      $BROADCAST_IDS = $ROW["BROADCAST_IDS"];
   
      $return .= showWxContent($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS);   
   }
   echo $return; 
}

?>