<?php

   include_once("inc/auth.php");
   ob_clean();
  
   $WXID = intval($_POST['WXID']);

   $query="delete from WEIXUN_SHARE where WEIXUN_ID = $WXID";
   $cursor= exequery($connection,$query);
   if($cursor){
      echo "OK";   
   }
   exit;   

?>
