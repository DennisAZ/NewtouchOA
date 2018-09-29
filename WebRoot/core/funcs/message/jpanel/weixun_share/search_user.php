<?
   include_once("inc/auth.php");
   include_once("inc/utility_all.php");
   include_once("inc/utility_org.php");
   ob_end_clean();
   
   $KWORD = td_iconv($KWORD, "utf-8", $MYOA_CHARSET);
   
   $KWORD=trim(urldecode($KWORD));
   
   $query = "SELECT NOT_VIEW_USER from USER where UID='$LOGIN_UID'";
   $cursor = exequery($connection,$query);
    if($ROW=mysql_fetch_array($cursor))
    {
       $NOT_VIEW_USER = $ROW["NOT_VIEW_USER"];
       if($NOT_VIEW_USER=="1")
          break;
    }

    //如果输入为不为中文名，才转换字母前缀 lp
    if(!preg_match("/^[\x7f-\xff]+$/", $KWORD)){
      require_once 'inc/mb.php';
      $PREFIX = getChnprefix($KWORD);
      $sql_str = " or USER.USER_NAME_INDEX  like '%$PREFIX%'";    
    }
    
    $query = "SELECT UID,USER_NAME,DEPT_ID from USER where DEPT_ID!=0 and (USER.USER_ID like '%$KWORD%' ".$sql_str." or USER.BYNAME like '%$KWORD%' or USER.USER_NAME like '%$KWORD%' or (MOBIL_NO like '%$KWORD%' and MOBIL_NO_HIDDEN='0') or TEL_NO_DEPT like '%$KWORD%' or TEL_NO_HOME like '%$KWORD%' or REMARK like '%$KWORD%') order by USER_NO,USER_NAME";
    $cursor = exequery($connection,$query);
    $SEARCH_COUNT=0;
    $line = "";
    while($ROW=mysql_fetch_array($cursor))
    {
      $SEARCH_COUNT++;
      $UID = $ROW["UID"];
      $USER_ID = $ROW["USER_ID"];
      $USER_NAME = $ROW["USER_NAME"];
      $DEPT_ID = $ROW["DEPT_ID"];
      $DEPT_NAME=str_replace("/"," - ",dept_long_name($DEPT_ID));
      $SHOW_STR="";
      $SEARCH_COUNT%2 == 0 ? $line = "" : $line = " class=\"line2\"";
      $_e = $USER_NAME."(".$DEPT_NAME.")";
      if(strlen($_e) >= 20)
         $_e = csubstr($_e,0,20)."...";
      $OUTPUT_HTML .= "<li".$line." title=\"".$DEPT_NAME."\" data_uid=\"".$UID."\" data_username=\"".$USER_NAME."\">".$_e."</li>\n";	 
    }

   echo $OUTPUT_HTML;
?>