<?
   include_once("inc/auth.php");
   include_once("inc/utility_all.php");
   include_once('config.php');
   ob_clean();
   $wxid = intval($wxid);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml10-transitional.dtd">
<html lang="zh-cn" xml:lang="zh-cn" xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><?=_("微讯分享")?></title>
<meta http-equiv="Content-Type" content="text/html; charset=<?=$MYOA_CHARSET?>">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" /> 
<link rel="stylesheet" type="text/css" href="/theme/11/weixunshare.css" />
<script type="text/javascript" src="/inc/js_lang.php"></script>
<script src="/inc/js/jquery/jquery.min.js"></script>
<script type="text/javascript">
var page = 0;
var stype = 'msg';
var searchkey = '<?=$wxid?>';
</script>
</head>
<?
   $PAGE_SIZE = 10;
   
   //获取微讯对应的原作者
   $query = "SELECT WEIXUN_ID,UID,CONTENT,ADDTIME from WEIXUN_SHARE where WEIXUN_ID = '$wxid' limit 1";
   $cursor= exequery($connection,$query);
   if($ROW=mysql_fetch_array($cursor)){
      $ADDTIME=$ROW["ADDTIME"];
      $CONTENT=$ROW["CONTENT"];
      $UID=$ROW["UID"];
      $WEIXUN_ID=$ROW["WEIXUN_ID"];
   }
   
   //获取总条数
   $query = "SELECT count(WEIXUN_ID) from WEIXUN_SHARE where (BROADCAST_IDS = '$wxid' or BROADCAST_IDS like '$wxid,%')";
   $cursor = exequery($connection,$query);
   $ROW = mysql_fetch_array($cursor);
   $TOTAL_COUNT =  $ROW[0];
   
   //获取个人信息
   $query = "SELECT UID,USER_NAME,AVATAR,DEPT_ID from USER where UID = '$UID' limit 1";
   $cursor= exequery($connection,$query);
   if($ROW=mysql_fetch_array($cursor)){
      $USER_NAME=$ROW["USER_NAME"];
      $AVATAR=$ROW["AVATAR"];
      $DEPT_ID=$ROW["DEPT_ID"];
      $DEPT_NAME = dept_long_name($DEPT_ID);
      $USER_ID = $ROW["USER_ID"];   
   }

?>
<body>

   <div class="main sub_main">
      <div class="clearfix"></div>
      
      <div class="talkBox" id="talkBox">
         <h2 class="hrline"><a  class="fr" href="index.php"><?=_("返回")?></a><em><label for="msgTxt"><a href="index.php"><?=_("微讯分享")?></a></label></em></h2>
            <div id="theader">
               <span class="user_box">
                  <img src="<?=avatar_path($AVATAR)?>" width="50" align="absmiddle" class="user_pic"/>
                  <span class="user_info msg_plus">
                     <span class="arrow-l"></span>
                     <p class="user_name"><?=$USER_NAME?></p>：
                     <p><?=format($CONTENT,'')?></p>
                  </span>
               </span>
               <div class="pubInfo msg_pubInfo">
                  <div class="funBox">
                     <a href="javascript:;" class="show"><?=sprintf("共 %d 条转播",$TOTAL_COUNT)?></a>
                     <span>|</span>
                     <a href="javascript:;" wxid="<?=$wxid?>" class="relay"><?=_("我来说两句")?></a>
                  </div>
               </div>
               <div class="clearfix"></div>
            </div>
   
         <span class="bg"></span>   
      </div>
      
      <?
         if($TOTAL_COUNT == 0)
         {
      ?>
         <div class="dC"><span class="SMessage"><?=_("该微讯还未被转播")?></span></div>
      <?
         }
      ?>
      
      <div class="AL">
         
         <ul id="talkList" class="LC">
            
         <?
            $COUNT = 0;
            $query = "SELECT WEIXUN_ID,CONTENT,BROADCAST_IDS,WEIXUN_SHARE.UID,ADDTIME,USER_ID,USER_NAME,AVATAR,DEPT_ID from WEIXUN_SHARE,USER where USER.UID=WEIXUN_SHARE.UID and (BROADCAST_IDS = '$wxid' or BROADCAST_IDS like '$wxid,%') order by ADDTIME desc limit 0,$PAGE_SIZE";
            $cursor= exequery($connection,$query);
            while($ROW=mysql_fetch_array($cursor))
            {
               $COUNT++;
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
               
               echo showWxContent($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS);  
            } 
         ?>

         </ul>
         <div class="clearfix"></div>
      </div>
      
      <?
         if($TOTAL_COUNT > 10)
         {   
      ?>
      <div class="moreFoot" id="moreList">
         <a href="javascript:void(0)" hidefocus="hidefocus">
            <em class="ico_load"></em><?=_("更多")?><em></em>
         </a>
      </div>
      <?
         }
      ?>      
   </div>
   
<div id="overlay"></div>
<div id="BroadCastBlock" class="dialogBlock module" style="display: none;width:400px;">
  <h4 class="moduleHeader"><span class="title"><?=_("分享这条微讯")?></span><a class="option" href="javascript:;"><img src="/images/close.png"/></a></h4>
  <div class="module_body" style="height:270px;">
   <iframe id="wxiframe" src="broadcast.php?wxid=1" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
  </div>
</div>
<script src="/inc/js/weixunshare.js"></script>
</body>
</html>