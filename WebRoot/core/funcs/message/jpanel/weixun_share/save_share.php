<?php

   include_once("inc/auth.php");
   include_once("inc/utility_msg.php");
   include_once("inc/utility_all.php");
   include_once("config.php");
   
   ob_clean();
   $CONTENT = trim(strip_tags(td_iconv($_POST['CONTENT'], "utf-8", $MYOA_CHARSET)));
   $CONTENT = csubstr($CONTENT, 0, 400);
   
   if(isset($LASTID) && $LASTID!="")
      $LASTID = intval($LASTID); 
   
   if($CONTENT == "")
      exit;

   $NOW = time();
   $CONTENT_SHOW = $CONTENT;
   $topics = getTopic($CONTENT);
   $mentioned_ids = getMentioned_ids($CONTENT);
   
   //转发
   $BROADCAST_IDS = "";
   if($WXID && is_numeric($WXID))
   {
      //查询转发的转播信息      
      $query = "SELECT BROADCAST_IDS from WEIXUN_SHARE where WEIXUN_ID = $WXID limit 1";
      $cursor= exequery($connection,$query);
      if($ROW = mysql_fetch_array($cursor))
      {
         $BROADCAST_IDS = $ROW['BROADCAST_IDS'];      
      }
      
      $BROADCAST_IDS = $BROADCAST_IDS=="" ? $WXID : $BROADCAST_IDS.",".$WXID;
   }
   
   if($WXID && $BROADCAST_IDS!=""){
      $query="insert into weixun_share(UID,CONTENT,ADDTIME,TOPICS,MENTIONED_IDS,BROADCAST_IDS)values('$LOGIN_UID','$CONTENT','$NOW','$topics','$mentioned_ids','$BROADCAST_IDS')";   
   }else{
      $query="insert into weixun_share(UID,CONTENT,ADDTIME,TOPICS,MENTIONED_IDS)values('$LOGIN_UID','$CONTENT','$NOW','$topics','$mentioned_ids')";
   }
   
   exequery($connection,$query);
   $_id = mysql_insert_id();
   
   //--如果有话题，则更新到话题库中
   if($topics!="")
   {
      $topicArr = explode("|||||",$topics);
      foreach($topicArr as $v){
         if($v=="") continue;
         $query = "SELECT 1 from weixun_share_topic where TOPIC_NAME = '$v'";
         $cursor = exequery($connection,$query);
         if(!(mysql_num_rows($cursor) > 0)){
            $query = "insert into weixun_share_topic(TOPIC_NAME)values('$v')";
            exequery($connection,$query);      
         }   
      }      
   }

   //发送微讯
   if($mentioned_ids != ''){
      $CONTENT = format($CONTENT,"3");
      $CONTENT = str_replace("'","\'",$CONTENT);
      send_msg($LOGIN_UID, $mentioned_ids, 0 , $CONTENT);        
   }
   
   //计算间隔的未显示的微讯
   $_diffRs = $_id - $LASTID;
      
   if(isset($LASTID))
   {
      
      $new_wx_num = $return = '';
      $query = "SELECT WEIXUN_ID,CONTENT,BROADCAST_IDS,WEIXUN_SHARE.UID,ADDTIME,USER_ID,USER_NAME,AVATAR,DEPT_ID from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID and WEIXUN_ID > $LASTID order by WEIXUN_ID desc";
      $cursor = exequery($connection,$query);
      $new_wx_num = mysql_affected_rows();
      
      if($_diffRs == 1)
      {
         $return = showWxContent($_id,$NOW,$LOGIN_UID,$LOGIN_AVATAR,$LOGIN_USER_NAME,$LOGIN_USER_ID,GetDeptNameById($LOGIN_DEPT_ID),$CONTENT_SHOW,$BROADCAST_IDS);
      }else
      {
         
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
         
      }
      
      echo $return;
      
   }

?>
