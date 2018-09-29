<?php
$ID_OR_NAME = 1; // 1 用户名，2 真实姓名

/*
	[Discuz!] (C)2001-2009 Comsenz Inc.
	This is NOT a freeware, use is subject to license terms

	$Id: index.php 16688 2008-11-14 06:41:07Z cnteacher $
*/

define('CURSCRIPT', 'index');


//echo $LOGIN_USER_ID."<br>";
//echo $USERNAME."<br>";
//echo $EMAIL."<br>";
//echo $BIRTH."<br>";

//exit;

require_once './include/common.inc.php';
require_once DISCUZ_ROOT.'./include/forum.func.php';
// echo $bbs_user.":".$discuz_user;
// echo "<br>";

///------- oa ------------
if(!stristr($HTTP_REFERER,"logging.php"))
{
   require_once 'inc/session.php';
   session_start();
   ob_start();
 if($ID_OR_NAME==1)
   {
         $bbs_user = $LOGIN_USER_ID;
  
   }
    else if($ID_OR_NAME==2)
   {
     
         $bbs_user = $USERNAME;
      
   }
   
   else
      exit;
   
//echo $bbs_user.":".$discuz_user;
 //  exit;
   
   
   if($bbs_user!="" && (empty($discuz_user)||$discuz_user!=$bbs_user))
   {   
     	$query = $db->query("select * from {$tablepre}members where username='$bbs_user'");
   
    //  echo "select * from {$tablepre}members where username='$bbs_user'";exit;
     
     	if($db->num_rows($query) <= 0)   //查看    没有就添加用户{$tablepre}members；
     	{     
        
  //插入添加信息
     	   	
     	   //	 echo "INSERT INTO {$tablepre}members (username, password, secques, gender, adminid, groupid, groupexpiry, extgroupids, regip, regdate, lastip, lastvisit, lastactivity, lastpost, posts, digestposts, oltime, pageviews, credits, extcredits1, extcredits2, extcredits3, extcredits4, extcredits5, extcredits6, extcredits7, extcredits8, email, bday, sigstatus, tpp, ppp, styleid, dateformat, timeformat, pmsound, showemail, newsletter, invisible, timeoffset, accessmasks) VALUES ('$bbs_user', '".md5($bbs_user)."', '', 0, 0, 10, 0, '', '".$REMOTE_ADDR."', ".time().", '', 0, ".time().", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '".$EMAIL."', '".$BIRTH."', 0, 0, 0, 0, '', 0, 1, 0, 1, 0, '9999', 0);";
     	  // 	exit;
     	   
     	      $db->query("INSERT INTO {$tablepre}members (username, password, secques, gender, adminid, groupid, groupexpiry, extgroupids, regip, regdate, lastip, lastvisit, lastactivity, lastpost, posts, digestposts, oltime, pageviews, credits, extcredits1, extcredits2, extcredits3, extcredits4, extcredits5, extcredits6, extcredits7, extcredits8, email, bday, sigstatus, tpp, ppp, styleid, dateformat, timeformat, pmsound, showemail, newsletter, invisible, timeoffset, accessmasks) VALUES ('$bbs_user', '".md5($bbs_user)."', '', 0, 0, 10, 0, '', '".$REMOTE_ADDR."', ".time().", '', 0, ".time().", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '".$EMAIL."', '".$BIRTH."', 0, 0, 0, 0, '', 0, 1, 0, 1, 0, '9999', 0);");
           
   	        $query = $db->query("select uid as id from {$tablepre}members where username='$bbs_user' ");
   	 
            $ROW = $db->fetch_array($query);
     	      $member_uid = $ROW["id"];

     	      if($member_uid > 0)
     	      {
     	          //添加{$tablepre}memberfields表
     	         // echo "INSERT INTO {$tablepre}memberfields (uid) VALUES (".$member_uid.");";exit;
     	         $db->query("INSERT INTO {$tablepre}memberfields (uid) VALUES (".$member_uid.");");
     	      }
     	      require_once DISCUZ_ROOT.'./include/cache.func.php';
   	        $_DCACHE['settings']['totalmembers']++;   //修改设置
   	        $_DCACHE['settings']['lastmember'] = $bbs_user;
   	        updatesettings();
   
     	}
     	
     	
          //查看    没有就添加用户.UC_DBTABLEPRE."members
     	$query = $db->query("select * from ".UC_DBTABLEPRE."members where username='$bbs_user'");
     	
     	    
     
     	
 	  	if($db->num_rows($query) <= 0)
      {   
         $salt = substr(uniqid(rand()), 0, 6);
         $db->query("INSERT INTO ".UC_DBTABLEPRE."members (`username`, `password`, `email`, `myid`, `myidkey`, `regip`, `regdate`, `lastloginip`, `lastlogintime`, `salt`) VALUES ('$bbs_user', '".md5(md5($bbs_user).$salt)."', '$EMAIL', '', '', '".$REMOTE_ADDR."', ".time().", 0, 0, '".$salt."');");
                 //插入添加信息
         $member_uid = mysql_insert_id();
     	      if($member_uid > 0)
     	      {  //添加.UC_DBTABLEPRE."memberfields
     	         $db->query("INSERT INTO ".UC_DBTABLEPRE."memberfields (uid) VALUES (".$member_uid.");");
            }  
      }
      
      
      
      
  
     	$field="username";
     	$query = $db->query("SELECT m.uid AS discuz_uid, m.username AS discuz_user, m.password AS discuz_pw, m.secques AS discuz_secques,
   					m.adminid, m.groupid, m.styleid AS styleidmem, m.lastvisit, m.lastpost, u.allowinvisible
   					FROM {$tablepre}members m LEFT JOIN {$tablepre}usergroups u USING (groupid)
   					WHERE m.$field='$bbs_user'");
     //  echo "SELECT m.uid AS discuz_uid, m.username AS discuz_user, m.password AS discuz_pw, m.secques AS discuz_secques,m.adminid, m.groupid, m.styleid AS styleidmem, m.lastvisit, m.lastpost, u.allowinvisible FROM {$tablepre}members m LEFT JOIN {$tablepre}usergroups u USING (groupid)	WHERE m.$field='$bbs_user'";
   		 //exit;
   		$member = $db->fetch_array($query);
  
   			
   		if($member['discuz_uid'])
   		{ 		

   	
   	
   			if($member['discuz_secques'] == $secques)
   			{  
   				extract($member);        ////////////////////////////////////////////////////////////////////////////////////////sss
   
   				$discuz_userss = $discuz_user;
   				$discuz_user = addslashes($discuz_user);
            //////////////设置登录模式
   				if(($allowinvisible && $loginmode == 'invisible') || $loginmode == 'normal') {
   					$db->query("UPDATE {$tablepre}members SET invisible='".($loginmode == 'invisible' ? 1 : 0)."' WHERE uid='$member[discuz_uid]'", 'UNBUFFERED');
   				}
   
   				$styleid = intval(empty($_POST['styleid']) ? ($styleidmem ? $styleidmem :
   						$_DCACHE['settings']['styleid']) : $_POST['styleid']);
   
   				$cookietime = intval(isset($_POST['cookietime']) ? $_POST['cookietime'] :
   						($_DCOOKIE['cookietime'] ? $_DCOOKIE['cookietime'] : 0));
   
   				dsetcookie('cookietime', $cookietime, 31536000);
   				dsetcookie('auth', authcode("$discuz_pw\t$discuz_secques\t$discuz_uid", 'ENCODE'), $cookietime);
   
   				$sessionexists = 0;
   			}
   			showmessage('login_succeed', "index.php");
       }
       else//INSERT INTO cdb_members (username, password, secques, gender, adminid, groupid, groupexpiry, extgroupids, regip, regdate, lastip, lastvisit, lastactivity, lastpost, posts, digestposts, oltime, pageviews, credits, extcredits1, extcredits2, extcredits3, extcredits4, extcredits5, extcredits6, extcredits7, extcredits8, avatarshowid, email, bday, sigstatus, tpp, ppp, styleid, dateformat, timeformat, pmsound, showemail, newsletter, invisible, timeoffset, newpm, accessmasks) VALUES ('$USER_NAME', '$PWD', '', 0, 0, 10, 0, '', '', strtotime($REG_DATE), '', strtotime($LAST_VISIT_DATE), time(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '$EMAIL', '0000-00-00', 0, 0, 0, 0, '', 0, 1, 0, 1, 0, '9999', 0, 0);
         echo "用户名或密码错误，登录失败!";
       exit;
   }
}
///------- oa ------------

$discuz_action = 1;

if($cacheindexlife && !$discuz_uid && $showoldetails != 'yes' && (!$_DCACHE['settings']['frameon'] || $_DCACHE['settings']['frameon'] && $_GET['frameon'] != 'yes') && empty($gid)) {

	$indexcache = getcacheinfo(0);

	if($timestamp - $indexcache['filemtime'] > $cacheindexlife) {
		@unlink($indexcache['filename']);
		define('CACHE_FILE', $indexcache['filename']);
		$styleid = $_DCACHE['settings']['styleid'];
	} elseif($indexcache['filename']) {
		@readfile($indexcache['filename']);
		$debug && debuginfo();
		$debug ? die('<script type="text/javascript">document.getElementById("debuginfo").innerHTML = " '.($debug ? 'Updated at '.gmdate("H:i:s", $indexcache['filemtime'] + 3600 * 8).', Processed in '.$debuginfo['time'].' second(s), '.$debuginfo['queries'].' Queries'.($gzipcompress ? ', Gzip enabled' : '') : '').'";</script>') : die();
	}
}


if(isset($showoldetails)) {
	switch($showoldetails) {
		case 'no': dsetcookie('onlineindex', 0, 86400 * 365); break;
		case 'yes': dsetcookie('onlineindex', 1, 86400 * 365); break;
	}
} else {
	$showoldetails = false;
}

$currenttime = gmdate($timeformat, $timestamp + $timeoffset * 3600);
$lastvisittime = dgmdate("$dateformat $timeformat", $lastvisit + $timeoffset * 3600);

$memberenc = rawurlencode($lastmember);
$newthreads = round(($timestamp - $lastvisit + 600) / 1000) * 1000;
$rsshead = $rssstatus ? ('<link rel="alternate" type="application/rss+xml" title="'.$bbname.'" href="'.$boardurl.'rss.php?auth='.$rssauth."\" />\n") : '';

$customtopics = '';
if($qihoo['maxtopics']) {
	foreach(explode("\t", isset($_DCOOKIE['customkw']) ? $_DCOOKIE['customkw'] : '') as $topic) {
		$topic = dhtmlspecialchars(trim(stripslashes($topic)));
		$customtopics .= '<a href="topic.php?keyword='.rawurlencode($topic).'" target="_blank">'.$topic.'</a> &nbsp;';
	}
}

$catlist = $forumlist = $sublist = $forumname = $collapseimg = $collapse = array();
$threads = $posts = $todayposts = $fids = $announcepm = 0;
$postdata = $historyposts ? explode("\t", $historyposts) : array();

foreach(array('forumlinks', 'sidebar') as $key) {
	if(!isset($_COOKIE['discuz_collapse']) || strpos($_COOKIE['discuz_collapse'], $key) === FALSE) {
		$collapseimg[$key] = 'collapsed_no';
		$collapse[$key] = '';
	} else {
		$collapseimg[$key] = 'collapsed_yes';
		$collapse[$key] = 'display: none';
	}
}

if(!empty($gid)) {
	$infosidestatus[2] = !empty($infosidestatus['f'.$gid][0]) ? $infosidestatus['f'.$gid][0] : $infosidestatus[2];
}
$infosidestatus['allow'] = $infosidestatus['allow'] && $infosidestatus[2] && $infosidestatus[2] != -1 ? (!$collapse['sidebar'] ? 2 : 1) : 0;


$gid = !empty($gid) ? intval($gid) : 0;

if(!$gid) {
	$announcements = '';
	if($_DCACHE['announcements']) {
		$readapmids = !empty($_DCOOKIE['readapmid']) ? explode('D', $_DCOOKIE['readapmid']) : array();
		foreach($_DCACHE['announcements'] as $announcement) {
			if(empty($announcement['groups']) || in_array($groupid, $announcement['groups'])) {
				if(empty($announcement['type'])) {
					$announcements .= '<li><a href="announcement.php?id='.$announcement['id'].'">'.$announcement['subject'].
						'<em>('.gmdate($dateformat, $announcement['starttime'] + $timeoffset * 3600).')</em></a></li>';
				} elseif($announcement['type'] == 1) {
					$announcements .= '<li><a href="'.$announcement['message'].'" target="_blank">'.$announcement['subject'].
						'<em>('.gmdate($dateformat, $announcement['starttime'] + $timeoffset * 3600).')</em></a></li>';
				}
			}
		}
	}
	unset($_DCACHE['announcements']);

	$sql = !empty($accessmasks) ?
				"SELECT f.fid, f.fup, f.type, f.name, f.threads, f.posts, f.todayposts, f.lastpost, f.inheritedmod, f.forumcolumns, f.simple, ff.description, ff.moderators, ff.icon, ff.viewperm, ff.redirect, a.allowview FROM {$tablepre}forums f
					LEFT JOIN {$tablepre}forumfields ff ON ff.fid=f.fid
					LEFT JOIN {$tablepre}access a ON a.uid='$discuz_uid' AND a.fid=f.fid
					WHERE f.status>0 ORDER BY f.type, f.displayorder"
				: "SELECT f.fid, f.fup, f.type, f.name, f.threads, f.posts, f.todayposts, f.lastpost, f.inheritedmod, f.forumcolumns, f.simple, ff.description, ff.moderators, ff.icon, ff.viewperm, ff.redirect FROM {$tablepre}forums f
					LEFT JOIN {$tablepre}forumfields ff USING(fid)
					WHERE f.status>0 ORDER BY f.type, f.displayorder";

	$query = $db->query($sql);

	while($forum = $db->fetch_array($query)) {
		$forumname[$forum['fid']] = strip_tags($forum['name']);
		if($forum['type'] != 'group') {

			$threads += $forum['threads'];
			$posts += $forum['posts'];
			$todayposts += $forum['todayposts'];

			if($forum['type'] == 'forum' && isset($catlist[$forum['fup']])) {
				if(forum($forum)) {
					$catlist[$forum['fup']]['forums'][] = $forum['fid'];
					$forum['orderid'] = $catlist[$forum['fup']]['forumscount']++;
					$forum['subforums'] = '';
					$forumlist[$forum['fid']] = $forum;
				}

			} elseif(isset($forumlist[$forum['fup']])) {

				$forumlist[$forum['fup']]['threads'] += $forum['threads'];
				$forumlist[$forum['fup']]['posts'] += $forum['posts'];
				$forumlist[$forum['fup']]['todayposts'] += $forum['todayposts'];
				if($subforumsindex && $forumlist[$forum['fup']]['permission'] == 2 && !($forumlist[$forum['fup']]['simple'] & 16) || ($forumlist[$forum['fup']]['simple'] & 8)) {
					$forumlist[$forum['fup']]['subforums'] .= '<a href="forumdisplay.php?fid='.$forum['fid'].'">'.$forum['name'].'</a>&nbsp;&nbsp;';
				}

			}

		} else {

			if(!isset($_COOKIE['discuz_collapse']) || strpos($_COOKIE['discuz_collapse'], 'category_'.$forum['fid']) === FALSE) {
				$forum['collapseimg'] = 'collapsed_no.gif';
				$collapse['category_'.$forum['fid']] = '';
			} else {
				$forum['collapseimg'] = 'collapsed_yes.gif';
				$collapse['category_'.$forum['fid']] = 'display: none';
			}

			if($forum['moderators']) {
			 	$forum['moderators'] = moddisplay($forum['moderators'], 'flat');
			}
			$forum['forumscount'] 	= 0;
			$catlist[$forum['fid']] = $forum;
		}
	}

	foreach($catlist as  $catid => $category) {
		if($catlist[$catid]['forumscount'] && $category['forumcolumns']) {
			$catlist[$catid]['forumcolwidth'] = floor(100 / $category['forumcolumns']).'%';
			$catlist[$catid]['endrows'] = '';
			if($colspan = $category['forumscount'] % $category['forumcolumns']) {
				while(($category['forumcolumns'] - $colspan) > 0) {
					$catlist[$catid]['endrows'] .= '<td>&nbsp;</td>';
					$colspan ++;
				}
				$catlist[$catid]['endrows'] .= '</tr>';
			}

		} elseif(empty($category['forumscount'])) {
			unset($catlist[$catid]);
		}
	}
	unset($catid, $category);

	if(isset($catlist[0]) && $catlist[0]['forumscount']) {
		$catlist[0]['fid'] = 0;
		$catlist[0]['type'] = 'group';
		$catlist[0]['name'] = $bbname;
		$catlist[0]['collapseimg'] = 'collapsed_no.gif';
	} else {
		unset($catlist[0]);
	}

	if($whosonlinestatus == 1 || $whosonlinestatus == 3) {
		$whosonlinestatus = 1;

		$onlineinfo = explode("\t", $onlinerecord);
		if(empty($_DCOOKIE['onlineusernum'])) {
			$onlinenum = $db->result_first("SELECT COUNT(*) FROM {$tablepre}sessions");
			if($onlinenum > $onlineinfo[0]) {
				$_DCACHE['settings']['onlinerecord'] = $onlinerecord = "$onlinenum\t$timestamp";
				$db->query("UPDATE {$tablepre}settings SET value='$onlinerecord' WHERE variable='onlinerecord'");
				require_once DISCUZ_ROOT.'./include/cache.func.php';
				updatesettings();
				$onlineinfo = array($onlinenum, $timestamp);
			}
			dsetcookie('onlineusernum', intval($onlinenum), 300);
		} else {
			$onlinenum = intval($_DCOOKIE['onlineusernum']);
		}
		$onlineinfo[1] = gmdate($dateformat, $onlineinfo[1] + ($timeoffset * 3600));

		$detailstatus = $showoldetails == 'yes' || (((!isset($_DCOOKIE['onlineindex']) && !$whosonline_contract) || $_DCOOKIE['onlineindex']) && $onlinenum < 500 && !$showoldetails);

		if($detailstatus) {
			@include language('actions');

			$discuz_uid && updatesession();
			$membercount = $invisiblecount = 0;
			$whosonline = array();

			$maxonlinelist = $maxonlinelist ? $maxonlinelist : 500;

			$query = $db->query("SELECT uid, username, groupid, invisible, action, lastactivity, fid FROM {$tablepre}sessions ".(isset($_DCACHE['onlinelist'][7]) ? '' : 'WHERE uid <> 0')." ORDER BY uid DESC LIMIT ".$maxonlinelist);
			while($online = $db->fetch_array($query)) {
				if($online['uid']) {
					$membercount ++;
					if($online['invisible']) {
						$invisiblecount++;
						continue;
					} else {
						$online['icon'] = isset($_DCACHE['onlinelist'][$online['groupid']]) ? $_DCACHE['onlinelist'][$online['groupid']] : $_DCACHE['onlinelist'][0];
					}

				} else {
					$online['icon'] = $_DCACHE['onlinelist'][7];
					$online['username'] = $_DCACHE['onlinelist']['guest'];
				}

				$online['fid'] = $online['fid'] ? $forumname[$online['fid']] : 0;
				$online['action'] = $actioncode[$online['action']];
				$online['lastactivity'] = gmdate($timeformat, $online['lastactivity'] + ($timeoffset * 3600));
				$whosonline[] = $online;
			}
			unset($actioncode, $online);

			if($onlinenum > $maxonlinelist) {
				$membercount = $db->result_first("SELECT COUNT(*) FROM {$tablepre}sessions WHERE uid <> '0'");
				$invisiblecount = $db->result_first("SELECT COUNT(*) FROM {$tablepre}sessions WHERE invisible = '1'");
			}

			if($onlinenum < $membercount) {
				$onlinenum = $db->result_first("SELECT COUNT(*) FROM {$tablepre}sessions");
				dsetcookie('onlineusernum', intval($onlinenum), 300);
			}

			$guestcount = $onlinenum - $membercount;

			$db->free_result($query);
			unset($online);
		}

	} else {
		$whosonlinestatus = 0;
	}

} else {
	require_once DISCUZ_ROOT.'./include/category.inc.php';

}

$lastvisit = $lastvisit ? dgmdate("$dateformat $timeformat", $lastvisit + 3600 * $timeoffset) : 0;

include template('discuz');

?>
