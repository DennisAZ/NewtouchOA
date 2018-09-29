<?

   include_once("inc/auth.php");
   include_once("inc/utility_org.php");

   $config['no_msg'] = _("来，说说你在做什么，想什么");
   
   $config['pers_msg'] = _("输入拼音首字母或者汉字");
   
   //最大输入长度
   $config['max_letter'] = 200;
   
   //表情存放文件夹
   $config['emotion_folder'] = '/images/face/';
   
   function format($str,$extral = "0"){
      if(!$str) return;
      global $config;
      //表情替换
      $emotion_pattern = '(\[em\](.*?)\[/em])';
      $replacement = "<img src='".$config['emotion_folder']."\${1}.gif' height='20' width='20' align='absmiddle'/>";
      
      //人和话题
      $tag_pattern = "/\#([^\#|.]+)\#/";
      preg_match_all($tag_pattern, $str, $tagsarr);
      $tags = implode(',',$tagsarr[1]);
      $user_pattern = "/\[@=([0-9]*\d)](.*?)\[\/@]/";
      if($extral=="1"){
         $str = preg_replace($emotion_pattern,$replacement,$str);
         $str = preg_replace($user_pattern, '<a href="javascript:;" onclick="parent.openURL(\'/general/ipanel/smsbox/user.php?uid=${1}&user_name=${2}\')">@${2}</a>', $str );
         $str = preg_replace($tag_pattern, '<a href="javascript:;" onclick="parent.openURL(\'/general/ipanel/smsbox/topic.php?q=${1}\')">#${1}#</a>', $str);  
      }else if($extral=="2"){
         $str = preg_replace($emotion_pattern,$replacement,$str);
         $str = preg_replace($user_pattern, '<a href="javascript:;">@${2}</a>', $str);
         $str = preg_replace($tag_pattern, '<a href="javascript:;">#${1}#</a>', $str);   
      }else if($extral=="3"){
         $str = preg_replace($emotion_pattern,'',$str);
         $str = preg_replace($user_pattern, '@${2}', $str);
         $str = preg_replace($tag_pattern, '#${1}#', $str);   
      }else{
         $str = preg_replace($emotion_pattern,$replacement,$str);
         $str = preg_replace($user_pattern, '<a href="user.php?uid=${1}&user_name=${2}">@${2}</a>', $str);
         $str = preg_replace($tag_pattern, '<a href="topic.php?q=${1}">#${1}#</a>', $str);   
      }
      return $str;
   }
   
   //遍历表情
   function list_files_array($dir) {
      global $ROOT_PATH;
      $files = array();
      $file_location = $ROOT_PATH.$dir;
      $arr_file =  scandir($file_location);
      $img = array( 'jpeg', 'jpg', 'bmp', 'gif', 'png', 'tiff', 'pcx');
      foreach($arr_file as $file){
      $ext = pathinfo($file);
         if(in_array(strtolower($ext['extension']),$img)){
            $files[] = array("url" => $file, "name" => substr($file,0,strpos($file,'.',1)));
         }
      }
      return  $files;
   }
   
   //完成内容表情替换
   function format_emotion($str){
      if(!$str) return;
      global $config;
      //表情替换
      $emotion_pattern = '(\[em\](.*?)\[/em])';
      $replacement = "<img src='".$config['emotion_folder']."\${1}.gif' />";
      $str = preg_replace($emotion_pattern,$replacement,$str);   
      return $str;
   }
   
   //获取内容主题词
   function getTopic($str){
      $tag_pattern = "/\#([^\#|.]+)\#/";
      preg_match_all($tag_pattern, $str, $tagsarr);
      $tags = implode('|||||',$tagsarr[1]);
      return $tags;
   }
   
   //提取内容中提及的人的uid
   function getMentioned_ids($str){
      $userarr = array();
      $useridstr = '';
      $user_pattern = "/\[@=([0-9]*\d)](.*?)\[\/@]/";
      preg_match_all($user_pattern, $str , $userarr);
      if(count($userarr[1]) > 0){
         $useridstr = implode(",",$userarr[1]);   
      }
      return $useridstr;
   }
   
   //获取所有转播的内容
   function getBroadcastContent($ids){
      if($ids=="") return;
      global $connection;
      $ids_str = $spt = '';
      $data_arr = array();
      $ids_arr = explode(",",$ids);   
      foreach($ids_arr as $v){
         $ids_str .= $spt."'".$v."'";
         $spt = ",";      
      }
      
      $query = "SELECT WEIXUN_ID,UID,CONTENT,ADDTIME from WEIXUN_SHARE where WEIXUN_ID IN ($ids_str) ORDER BY WEIXUN_ID DESC";
      $cursor = exequery($connection,$query);
      while($ROW=mysql_fetch_array($cursor)){
         $ADDTIME=$ROW["ADDTIME"];
         $CONTENT=$ROW["CONTENT"];
         $UID=$ROW["UID"];
         $WEIXUN_ID=$ROW["WEIXUN_ID"];
         $data_arr[] = array(
            'WEIXUN_ID' => $WEIXUN_ID,
            'UID' => $UID,
            'ADDTIME' => $ADDTIME,
            'CONTENT' => $CONTENT,
            'USER_NAME' => trim(GetUserNameByUid($UID),",")
         );
      }
      return $data_arr;
   }
   
   //获取所有转播的内容
   function broadcastExist($id){
      if($id=="") return;
      global $connection;
      $query = "SELECT WEIXUN_ID from WEIXUN_SHARE where WEIXUN_ID = $id";
      $cursor = exequery($connection,$query);
      if(mysql_affected_rows() > 0){
         return true;   
      }else{
         return false;   
      }
   }
   
   //获取微讯转播次数
   function getBroadcastNum($id){
      if($id=="") return;
      global $connection;
      $query = "SELECT count(*) from WEIXUN_SHARE where BROADCAST_IDS!='' and FIND_IN_SET('$id',BROADCAST_IDS)";
      $cursor = exequery($connection,$query);
      $rs = mysql_fetch_array($cursor);
      return $rs[0];
   }
   
   //显示微讯
   function showWxContent($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS)
   {
      global $LOGIN_UID,$LOGIN_USER_PRIV;
      $return = '';
      if(!$WEIXUN_ID) return;
      
      //查询转播数
      if($BROADCAST_IDS!="")
      {
         $_ORGT_ARR = array();
         $_DATA = getBroadcastContent($BROADCAST_IDS);
         $_T = count($_DATA);
         $_ORGT_ARR = explode(",",$BROADCAST_IDS);
         $_ORGT = $_ORGT_ARR[0];
      }
               
      $return .= '<li id="'.$WEIXUN_ID.'" rel="'.$ADDTIME.'">
      <div class="userPic">
      <a href="user.php?uid='.$UID.'" title="'.$USER_NAME.'" rel="'.$USER_NAME.'">
      <img src="'.avatar_path($AVATAR).'" title="'.$USER_NAME.'">
      </a>
      </div>
      <div class="msgBox">
      <div class="userName" rel="'.$USER_ID.'">
      <strong><a href="javascript:;" title="'._("部门:").$DEPT_NAME.'" rel="'.$USER_NAME.'">'.$USER_NAME.'</a>:</strong>
      </div>
      <div class="msgCnt">'.format($CONTENT,'');
      if($_T >= 1){
         foreach($_DATA as $k => $v){
            if($k >= 5) continue;
            if($v['WEIXUN_ID'] == $_ORGT) break;
            $return .= ' || <a href="javascript:;" rel="'.$v['USER_NAME'].'">'.$v['USER_NAME'].'</a>:'.format($v['CONTENT'],'');
         }
      }
      $return .= '</div>';
      if($BROADCAST_IDS!="")
      {
         $return .= '<div class="comment">';
         if(broadcastExist($_ORGT))
         {
            $BroadcastNum = getBroadcastNum($_ORGT);
            $return .= '<a href="javascript:;" rel="'.$_DATA[$_T-1]['USER_NAME'].'">'.$_DATA[$_T-1]['USER_NAME'].'</a>:
            <em>'.format($_DATA[$_T-1]['CONTENT'],'').'</em>
            <div class="pubInfo">
            <span class="left">'.timeintval($_DATA[0]['ADDTIME'],'','m-d H:i').'
            <a href="msg.php?wxid='.$_ORGT.'" class="allbroad">'.sprintf(_("转播次数(%s)"), $BroadcastNum).'</a>
            </span>
            <div class="funBox">
            <a href="javascript:;" wxid="'.$_DATA[$_T-1]['WEIXUN_ID'].'" class="relay">'._("转播").'</a>
            </div>
            </div>';
         }else{
            $return .= '<em>'._("该微讯已被原作者删除").'</em>';
         }
         $return .= '<div class="clearfix"></div>';
         $return .= '</div>';
      }    
      $return .= '<div class="pubInfo">
      <div class="funBox">
      <a href="javascript:;" wxid="'.$WEIXUN_ID.'" class="relay">'._("转播").'</a>';
      if($UID == $LOGIN_UID || $LOGIN_USER_PRIV == 1){
         $return .= '<span>|</span><a href="javascript:;" class="comt" num="0" wxid="'.$WEIXUN_ID.'">'._("删除").'</a>';
      }
      $return .='</div>
      <span class="time">'.timeintval($ADDTIME,'','m-d H:i').'</span>
      </div>
      </div>
      </li>';
      echo $return;
   }
   
   //显示微讯
   function showWxContentNoUserPic($WEIXUN_ID,$ADDTIME,$UID,$AVATAR,$USER_NAME,$USER_ID,$DEPT_NAME,$CONTENT,$BROADCAST_IDS)
   {
      global $LOGIN_UID,$LOGIN_USER_PRIV;
      $return = '';
      if(!$WEIXUN_ID) return;
      
      //查询转播数
      if($BROADCAST_IDS!="")
      {
         $_ORGT_ARR = array();
         $_DATA = getBroadcastContent($BROADCAST_IDS);
         $_T = count($_DATA);
         $_ORGT_ARR = explode(",",$BROADCAST_IDS);
         $_ORGT = $_ORGT_ARR[0];
      }
               
      $return .= '<li id="'.$WEIXUN_ID.'" rel="'.$ADDTIME.'">
      <div class="msgBox fixmsgBox">
      <div class="userName" rel="'.$USER_ID.'">
      <strong><a href="javascript:;" title="'._("部门:").$DEPT_NAME.'" rel="'.$USER_NAME.'">'.$USER_NAME.'</a>:</strong>
      </div>
      <div class="msgCnt">'.format($CONTENT,'');
      if($_T >= 1){
         foreach($_DATA as $k => $v){
            if($k >= 5) break;
            if($v['WEIXUN_ID'] == $_ORGT) break;
            $return .= ' || <a href="javascript:;" rel="'.$v['USER_NAME'].'">'.$v['USER_NAME'].'</a>:'.format($v['CONTENT'],'');
         }
      }
      $return .= '</div>';
      if($BROADCAST_IDS!="")
      {
         $return .= '<div class="comment">';
         if(broadcastExist($_ORGT))
         {
            $BroadcastNum = getBroadcastNum($_ORGT);
            $return .= '<a href="javascript:;" rel="'.$_DATA[$_T-1]['USER_NAME'].'">'.$_DATA[$_T-1]['USER_NAME'].'</a>:
            <em>'.format($_DATA[$_T-1]['CONTENT'],'').'</em>
            <div class="pubInfo">
            <span class="left">'.timeintval($_DATA[0]['ADDTIME'],'','m-d H:i').'
            <a href="msg.php?wxid='.$_ORGT.'" class="allbroad">'.sprintf(_("转播次数(%s)"), $BroadcastNum).'</a>
            </span>
            <div class="funBox">
            <a href="javascript:;" wxid="'.$_DATA[$_T-1]['WEIXUN_ID'].'" class="relay">'._("转播").'</a>
            </div>
            </div>';
         }else{
            $return .= '<em>'._("该微讯已被原作者删除").'</em>';
         }
         $return .= '<div class="clearfix"></div>';
         $return .= '</div>';
      }    
      $return .= '<div class="pubInfo">
      <div class="funBox">
      <a href="javascript:;" wxid="'.$WEIXUN_ID.'" class="relay">'._("转播").'</a>';
      if($UID == $LOGIN_UID || $LOGIN_USER_PRIV == 1){
         $return .= '<span>|</span><a href="javascript:;" class="comt" num="0" wxid="'.$WEIXUN_ID.'">'._("删除").'</a>';
      }
      $return .='</div>
      <span class="time">'.timeintval($ADDTIME,'','m-d H:i').'</span>
      </div>
      </div>
      </li>';
      echo $return;
   }
?>