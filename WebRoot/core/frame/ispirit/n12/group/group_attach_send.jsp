<?
include_once("inc/auth.php");
include_once("inc/utility_all.php");
include_once("inc/utility_file.php");

ob_end_clean();
$query = "SELECT GROUP_UID from IM_GROUP where GROUP_ID='$MSG_GROUP_ID'";
$cursor = exequery($connection,$query);
if($ROW=mysql_fetch_array($cursor))
{
	 if(!find_id($ROW["GROUP_UID"],$LOGIN_UID))
	 {
      echo "-ERR "._("您不在该群中，无权操作");
      exit;
	 }
}

$MODULE = 'im';
if(count($_FILES)>=1)
{
   $ATTACHMENTS=upload("ATTACHMENT", $MODULE);
   ob_end_clean();

   $ATTACHMENT_ID = substr($ATTACHMENTS["ID"], 0, -1);
   $ATTACHMENT_NAME = substr($ATTACHMENTS["NAME"], 0, -1);
}
else
{
   echo "-ERR "._("无文件上传");
   exit;
}

$FILE_SIZE = attach_size($ATTACHMENT_ID, $ATTACHMENT_NAME, $MODULE);
if(!$FILE_SIZE)
{
   echo "-ERR "._("文件上传失败");
   exit;
}

$CUR_TIME=time();

if($MIX_FLAG == 1)//旧版本截图
{
	 $ATTACHMENT_N_ARRAY=explode("*",$ATTACHMENT_NAME);
	 $ATTACHMENT_N0 = $ATTACHMENT_N_ARRAY[0];

   $query = "SELECT MSG_ID,MSG_CONTENT,MSG_TIME,MSG_GROUP_ID,ATTACHMENT_ID,ATTACHMENT_NAME,MSG_USER_NAME,FROM_UNIXTIME(MSG_TIME) as MSG_TIME from IM_GROUP_MSG where MSG_GROUP_ID='$MSG_GROUP_ID' and MSG_UID='$LOGIN_UID' and MSG_CONTENT like '%$ATTACHMENT_N0%' order by MSG_ID desc";
   $cursor = exequery($connection,$query);
   if($ROW=mysql_fetch_array($cursor))
   {
   	  $MSG_ID = $ROW["MSG_ID"];
   	  $MSG_CONTENT = $ROW["MSG_CONTENT"];

      $ATTACHMENT_ID_ARRAY=explode(",",$ATTACHMENT_ID);
      $ATTACHMENT_NAME_ARRAY=explode("*",$ATTACHMENT_NAME);
      $ARRAY_COUNT=sizeof($ATTACHMENT_ID_ARRAY);

      if($ATTACHMENT_ID_ARRAY[0]=="")
         continue;

      $MODULE="im";
      $ATTACHMENT_ID1=$ATTACHMENT_ID_ARRAY[0];
      $YM=substr($ATTACHMENT_ID1,0,strpos($ATTACHMENT_ID1,"_"));
      if($YM)
         $ATTACHMENT_ID1=substr($ATTACHMENT_ID1,strpos($ATTACHMENT_ID,"_")+1);
      $ATTACHMENT_ID_ENCODED=attach_id_encode($ATTACHMENT_ID1,$ATTACHMENT_NAME_ARRAY[0]);

      $temp_str=$MSG_CONTENT;
      while(1)
      {
         $pos1 = strpos($temp_str,'src="')+5;
         $text1 = substr($temp_str,$pos1);
         $pos2 = strpos($text1,'"');
         $text2 = substr($text1,0,$pos2);
         if($text2=="")
            break;
         if(strpos($text2,$ATTACHMENT_N0)>0)
            break;

         $temp_str= substr($text1,$pos2);
      }

      $tr="/inc/attach.php?MODULE=".$MODULE."&YM=".$YM."&ATTACHMENT_ID=".$ATTACHMENT_ID_ENCODED."&ATTACHMENT_NAME=".$ATTACHMENT_NAME_ARRAY[0];
      $MSG_CONTENT = str_replace("onclick=imgClick(this);","",$MSG_CONTENT);
      $MSG_CONTENT = str_replace("onerror=imgErr(this);","",$MSG_CONTENT);
      $MSG_CONTENT = addslashes(str_replace($text2,$tr,$MSG_CONTENT));

      $query="update IM_GROUP_MSG set MSG_CONTENT='$MSG_CONTENT' where MSG_ID='$MSG_ID'";
      exequery($connection,$query);
   }
}
else if($MIX_FLAG == 2)//新版本截图
{
	  $ATTACHMENT_ID_ARRAY=explode(",",$ATTACHMENT_ID);
    $ATTACHMENT_NAME_ARRAY=explode("*",$ATTACHMENT_NAME);
    $ARRAY_COUNT=sizeof($ATTACHMENT_ID_ARRAY);

    $MODULE="im";
    $ATTACHMENT_ID1=$ATTACHMENT_ID_ARRAY[0];
    $YM=substr($ATTACHMENT_ID1,0,strpos($ATTACHMENT_ID1,"_"));
    if($YM)
       $ATTACHMENT_ID1=substr($ATTACHMENT_ID1,strpos($ATTACHMENT_ID,"_")+1);
    $ATTACHMENT_ID_ENCODED=attach_id_encode($ATTACHMENT_ID1,$ATTACHMENT_NAME_ARRAY[0]);
    //$TMP_C = file_get_contents("C:/111.TXT")."\n";
    //$TMP_C = $TMP_C.$ATTACHMENT_ID."|".$ATTACHMENT_ID_ENCODED;
    //file_put_contents("C:/111.TXT", $TMP_C);

    $tr="/inc/attach.php?MODULE=".$MODULE."&YM=".$YM."&ATTACHMENT_ID=".$ATTACHMENT_ID_ENCODED."&ATTACHMENT_NAME=".$ATTACHMENT_NAME_ARRAY[0];
 		echo $tr;
		exit;
}
else//直接上传文件
{
   $query="insert into IM_GROUP_MSG(MSG_UID,MSG_CONTENT,MSG_TIME,MSG_GROUP_ID,ATTACHMENT_ID,ATTACHMENT_NAME,MSG_USER_NAME) values ('$LOGIN_UID','$MSG_CONTENT','$CUR_TIME','$MSG_GROUP_ID','$ATTACHMENT_ID','$ATTACHMENT_NAME','$LOGIN_USER_NAME')";
   $cursor = exequery($connection,$query);
   if($cursor === FALSE)
   {
      echo "-ERR "._("数据库操作失败");
      exit;
   }

   $MSG_ID=mysql_insert_id();
   if($MSG_ID == 0)
   {
      echo "-ERR "._("数据库操作失败2");
      exit;
   }

   $query = "SELECT MAX_MSG_ID from IM_GROUP_MAXMSGID where GROUP_MEMBER_UID='$LOGIN_UID' and GROUP_ID='$MSG_GROUP_ID'";
   $cursor= exequery($connection,$query);
   if($ROW=mysql_fetch_array($cursor))
      $query="update IM_GROUP_MAXMSGID set MAX_MSG_ID='$MSG_ID' where GROUP_MEMBER_UID='$LOGIN_UID' and GROUP_ID='$MSG_GROUP_ID'";
   else
      $query="insert into IM_GROUP_MAXMSGID (GROUP_MEMBER_UID,GROUP_ID,MAX_MSG_ID) values ('$LOGIN_UID','$MSG_GROUP_ID','$MSG_ID')";
   exequery($connection,$query);

}

echo "+OK ".$MSG_ID;
exit;
?>