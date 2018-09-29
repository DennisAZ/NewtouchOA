<?
   include_once("inc/auth.php");
   include_once("inc/utility_all.php");
   include_once('config.php');
   ob_clean();
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml10-transitional.dtd">
<html lang="zh-cn" xml:lang="zh-cn" xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?=_("微讯分享管理")?></title>
<meta http-equiv="Content-Type" content="text/html; charset=<?=$MYOA_CHARSET?>">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" /> 
<link rel="stylesheet" type="text/css" href="/theme/11/weixunshare.css" />
<script type="text/javascript" src="/inc/js_lang.php"></script>
<script src="/inc/js/jquery/jquery.min.js"></script>
<script type="text/javascript">
var page = 0;
var stype = 'cp';
var searchkey = '';
</script>
</head>
<?
   $PAGE_SIZE = 10;
   
   $query = "SELECT count(WEIXUN_ID) from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID";
   $cursor = exequery($connection,$query);
   $ROW = mysql_fetch_array($cursor);
   $TOTAL_COUNT =  $ROW[0];
   
   $query = "SELECT WEIXUN_ID,CONTENT,BROADCAST_IDS,WEIXUN_SHARE.UID,ADDTIME,USER_ID,USER_NAME,AVATAR,DEPT_ID from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID order by ADDTIME desc limit 0,$PAGE_SIZE";
   $cursor= exequery($connection,$query);

?>
<body>
<div id="mainWrapper">
   <div class="clearfix"></div>
   <div class="main">
      <div class="clearfix"></div>
   </div>
   
   <div class="side">
      <div id="UIn">
         <div class="userDetail">
            <a title="<?=$LOGIN_USER_NAME?>" accessKey="x" href="user.php?uid=<?=$LOGIN_UID?>">
               <img src="<?=avatar_path($LOGIN_AVATAR)?>" title="<?=$LOGIN_USER_NAME?>">
            </a>
            <p><strong><?=$LOGIN_USER_NAME?></strong></p>
            <p>(<?=rtrim(GetDeptNameById($LOGIN_DEPT_ID),",");?>)</p>
         </div>
      </div>
      <div class="SC">
         <ul class="SM">
            <li class="index" id="x1"><i></i><a href="index.php"><b class="ico_index"><em></em></b><?=_("微讯大厅")?></a></li>
            <li class="mypub"><i></i><a href="user.php?uid=<?=$LOGIN_UID?>" hidefocus="true"><b class="ico_mypub"><em></em></b><?=_("我的广播")?></a></li>
            <li class="about"><i></i><a href="at.php" hidefocus="true"><b class="ico_about"><em></em></b><?=_("提到我的")?></a></li>
            <?
               if($LOGIN_USER_PRIV == 1)
               {
            ?>
            <li class="myfav current"><i></i><a href="cp.php" hidefocus="true"><b class="ico_myfav"><em></em></b><?=_("微讯管理")?></a></li>
            <?
               }
            ?>
         </ul>
      </div>
      
      <?
         //如果有新主题
         $query = "select * from weixun_share_topic order by rand() limit 7";
         $cursor = exequery($connection,$query);
         $TOPICS_COUNT =  mysql_num_rows($cursor);
         
         if($TOPICS_COUNT > 0)
         {
      ?>
      <div class="SC">
         <h3><?=_("随机话题")?></h3>
         <ul class="hotTopicList" id="hotTopic">
            <?
               while($ROW=mysql_fetch_array($cursor))
               {
                  $ID = $ROW["TOPIC_ID"];
                  $TOPIC_NAME = $ROW["TOPIC_NAME"];
            ?>
            <li><a title="<?=$TOPIC_NAME?>" href="topic.php?q=<?=urlencode($TOPIC_NAME)?>">#<?=$TOPIC_NAME?>#</a></li>
            <?
               }
            ?>   
         </ul>
      </div> 
      <?
         }
      ?>    
   </div>
   <div class="clearfix"></div>
</div>

</body>
</html>