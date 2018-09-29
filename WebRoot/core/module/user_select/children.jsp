<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
.menulines{}
</style>
<script src="/inc/js/utility.js"></script>
<script src="/inc/js/user_select.js"></script>
<script Language="JavaScript">
var parent_window = getOpenner();
var to_form = parent_window.<?=$FORM_NAME?>;
var to_id =   to_form.<?=$TO_ID?>;
var to_name = to_form.<?=$TO_NAME?>;
</script>
</head>

<body class="bodycolor" topmargin="1" leftmargin="0" onload="begin_set();<?if($CHECKED=="true") echo "add_all('1');";elseif($CHECKED=="false") echo "del_all('1');";?>">

<?
include_once("inc/my_priv.php");

if($MODULE_ID=="2")
   $EXCLUDE_UID_STR=my_exclude_uid();

 if($DEPT_ID=="")
    $DEPT_ID=$LOGIN_DEPT_ID;

 //============================ 显示人员信息 =======================================
 $TITLE=$SYS_DEPARTMENT[$DEPT_ID]["DEPT_NAME"];
 
 if($TITLE=="")
    $TITLE="全部人员";
?>

<table class="TableBlock" width="100%">
<tr class="TableHeader">
  <td colspan="2" align="center"><b><?=$TITLE?></b></td>
</tr>

<?
 $EXCLUDE_UID_STR = td_trim($EXCLUDE_UID_STR);
 $query = "SELECT UID,USER_ID,USER_NAME,PRIV_NO from USER,USER_PRIV where (DEPT_ID='$DEPT_ID' or find_in_set('$DEPT_ID',DEPT_ID_OTHER)) and DEPT_ID!=0 and USER.USER_PRIV=USER_PRIV.USER_PRIV";
 if(!$MANAGE_FLAG)
    $query .= " and NOT_LOGIN!='1'";
 if($DEPT_PRIV=="3"||$DEPT_PRIV=="4")
    $query .= " and find_in_set(USER.USER_ID,'$USER_ID_STR')";
 if($EXCLUDE_UID_STR!="")
    $query .= " and USER.UID not in ($EXCLUDE_UID_STR)";
 $query .= " order by PRIV_NO,USER_NO,USER_NAME";
 $cursor= exequery($connection,$query);
 $USER_COUNT=0;
 while($ROW=mysql_fetch_array($cursor))
 {
    $UID=$ROW["UID"];
    $USER_ID=$ROW["USER_ID"];
    $USER_NAME=$ROW["USER_NAME"];
    $PRIV_NO_I=$ROW["PRIV_NO"];
    
    if(find_id($UID_ALL, $UID) || $ROLE_PRIV=="0" && $PRIV_NO_I<=$MY_PRIV_NO || $ROLE_PRIV=="1" && $PRIV_NO_I< $MY_PRIV_NO || $ROLE_PRIV=="3" && !find_id($PRIV_ID_STR, $PRIV_NO_I))
       continue;

    $UID_ALL.=$UID.",";
    
    $USER_COUNT++;
    if($USER_COUNT==1)
    {
?>
<tr class="TableControl">
  <td onclick="javascript:add_all('1');" style="cursor:pointer" align="center">全部添加</td>
</tr>
<tr class="TableControl">
  <td onclick="javascript:del_all('1');" style="cursor:pointer" align="center">全部删除</td>
</tr>
<?
    }
?>

<tr class="TableData" style="cursor:pointer">
  <td class="menulines1" id="<?=$USER_ID?>" title="<?=$USER_NAME?>" align="center" onclick="javascript:click_user('<?=$USER_ID?>')">
  <?=htmlspecialchars($USER_NAME)?><span id="attend_status_<?=$UID?>" title="<?=$USER_ID?>" style="color:#FF0000;"><?if(array_key_exists($UID,$SYS_ONLINE_USER)) echo "(在线)";?></span>
  </td>
</tr>

<?
 }

if($CHECKED) //显示子部门人员

{
    if($DEPT_ID==0)
    {
?>
<tr class="TableControl">
  <td onclick="javascript:add_all('1');" style="cursor:pointer" align="center">全部添加</td>
</tr>
<tr class="TableControl">
  <td onclick="javascript:del_all('1');" style="cursor:pointer" align="center">全部删除</td>
</tr>
<?
    }
    echo user_tree_list($DEPT_ID);
}

if($USER_COUNT==0)
{
?>
<tr class="TableControl">
  <td align="center">未定义用户</td>
</tr>
<?
}

///////////////////////////////////////////////////////////////////////////////////////
function user_tree_list($DEPT_ID)
{
  global $connection,$DEEP_COUNT,$USER_COUNT,$MANAGE_FLAG,$DEPT_PRIV,$ROLE_PRIV,$PRIV_ID_STR,$USER_ID_STR,$MY_PRIV_NO,$EXCLUDE_UID_STR,$SYS_ONLINE_USER,$UID_ALL;

  $query = "SELECT DEPT_ID,DEPT_NAME from oa_department where DEPT_PARENT='$DEPT_ID' order by DEPT_NO";
  $cursor1= exequery($connection,$query);
  $OPTION_TEXT="";
  $DEEP_COUNT1=$DEEP_COUNT;
  $DEEP_COUNT.="　";
  while($ROW=mysql_fetch_array($cursor1))
  {
      $COUNT++;
      $DEPT_ID=$ROW["DEPT_ID"];
      $DEPT_NAME=$ROW["DEPT_NAME"];
      $DEPT_NAME=htmlspecialchars($DEPT_NAME);

      $OPTION_TEXT.="<tr class='TableHeader'><td><b>".$DEEP_COUNT1."├".$DEPT_NAME."</b></td></tr>";

      $query = "SELECT UID,USER_ID,USER_NAME,PRIV_NO from USER,USER_PRIV where (DEPT_ID='$DEPT_ID' or find_in_set('$DEPT_ID',DEPT_ID_OTHER)) and USER.USER_PRIV=USER_PRIV.USER_PRIV";
      if(!$MANAGE_FLAG)
         $query .= " and NOT_LOGIN!='1'";
      if($DEPT_PRIV=="3"||$DEPT_PRIV=="4")
         $query .= " and find_in_set(USER.USER_ID,'$USER_ID_STR')";
      
      if($EXCLUDE_UID_STR!="")
         $query .= " and USER.UID not in ($EXCLUDE_UID_STR)";
      $query .= " order by PRIV_NO,USER_NO,USER_NAME";
      $cursor= exequery($connection,$query);
      while($ROW=mysql_fetch_array($cursor))
      {
         $UID=$ROW["UID"];
         $USER_ID=$ROW["USER_ID"];
         $USER_NAME=$ROW["USER_NAME"];
         $PRIV_NO_I=$ROW["PRIV_NO"];

         if(find_id($UID_ALL, $UID) || $ROLE_PRIV=="0" && $PRIV_NO_I<=$MY_PRIV_NO || $ROLE_PRIV=="1" && $PRIV_NO_I< $MY_PRIV_NO || $ROLE_PRIV=="3" && !find_id($PRIV_ID_STR, $PRIV_NO_I))
            continue;

         $UID_ALL.=$UID.",";
         $USER_COUNT++;
         $OPTION_TEXT.="<tr class='TableData' onclick=\"javascript:click_user('".$USER_ID."');\" style='cursor:pointer' align='center'><td class='menulines1' id='".$USER_ID."' title='".$USER_NAME."'>".htmlspecialchars($USER_NAME).'<span id="attend_status_'.$UID.'" title="'.$USER_ID.'" style="color:#FF0000;">'.(array_key_exists($UID,$SYS_ONLINE_USER) ? "(在线)" : "")."</span></td></tr>\n";
      }
      $OPTION_TEXT.=user_tree_list($DEPT_ID);
  }//while

  $DEEP_COUNT=$DEEP_COUNT1;
  return $OPTION_TEXT;
}//end function
?>

</table>
</body>
</html>