<?
include_once("inc/auth.php");
include_once("inc/utility_all.php");

$query="delete from oa_online where UID='".$LOGIN_UID."'";
exequery($connection, $query);
add_log(22,"",$LOGIN_USER_ID);
clear_online_status();

session_start();
session_destroy();
//与UCenter集成
$SYS_PARA_ARRAY = get_sys_para('USE_DISCUZ');
$USE_DISCUZ = $SYS_PARA_ARRAY["USE_DISCUZ"];

if($USE_DISCUZ > 0)
{
    include_once("inc/uc_client/config.inc.php"); 
        
    if(defined("UC_APPID"))
    { 
        include_once("inc/uc_client/client.php"); 
        switch($USE_DISCUZ)
        {
            case 1:
                $UC_USER_NAME = $LOGIN_USER_NAME;
                break;
            case 2:
                $UC_USER_NAME = $LOGIN_USER_ID;
                break; 
            case 3:
                $UC_USER_NAME = $LOGIN_BYNAME;
                break; 
            default:
                $UC_USER_NAME = $LOGIN_USER_NAME; 
        }
        
        if($uc_data = uc_get_user($UC_USER_NAME)) 
        {
            $uc_synclogout_script = uc_user_synlogout($uc_data[0]);
        }
    }
}
?>

<html>
<head>
<title><?=_("正在注销系统...")?></title>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<?=$uc_synclogout_script?>
<script>
location="../"
</script>
</head>

</html>
