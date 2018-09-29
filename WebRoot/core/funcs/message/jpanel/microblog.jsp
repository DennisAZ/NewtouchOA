<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.sms.data.*" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml10-transitional.dtd">
<html lang="zh-cn" xml:lang="zh-cn" xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/cmp/smsbox.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>

<script type="text/javascript">
var page = 0;
var data_type = 'blog';
var ispirit = '<?=$ISPIRIT?>';
var i_ver = '<?=$I_VER?>';
var config_no_msg = "<?=_("来，说说你在做什么，想什么")?>";
var config_max_letter = 200;

function init()
{
   $("body").style.height=(document.body.clientHeight-$("sub_tabs").clientHeight-$("bottom").clientHeight)+"px";
}

function rescroll(){
   $("msg").style.top = 0;
   $("xoverlay").style.height = $("body").scrollHeight;
   $("msg").style.marginTop = ($("body").scrollTop + $("body").clientHeight/2 - $('msg').style.height/2) + "px";   
}

function letter_stat(){
   var sHTML = $("wx_content").innerHTML;
   var sv = $("wx_content").value;
   if(sv == config_no_msg && sv == sHTML)
      return;
   var sl = sv.length;
   var simgl = $("wx_content").getElementsByTagName("img").length;
   var speol = $("wx_content").getElementsByTagName("input").length;
   var all_l = sl + simgl + speol;
   var max_l = config_max_letter;
   if(all_l > max_l) {
      alert("<?=sprintf(_("输入内容长度不能超过%s字!"), $config['max_letter'])?>");
      $("wx_content").value = $("wx_content").value.substr(0, max_l);
   }
   $("wx_letter").innerHTML = max_l - all_l;
}

function letter_stat_mon(){
   setInterval(letter_stat,100);   
}

document.onkeydown = function (e) { 
   if(!e) var e = window.event; 
   if(e.ctrlKey && e.keyCode == 13){ 
       $("share").click(); 
   }
   
   if(e.keyCode==8)
   {
     var id = document.activeElement.id;
     if(id!="wx_content"){ 
         e.keyCode=0; 
         e.returnValue=false;
      } 
   }   
} 
</script>
</head>
<body onload="init();letter_stat_mon();">

<div id="body" onscroll="rescroll();">
   <div id="xoverlay"></div>
   <div id="msg"></div>
   <div class="share_box">
      <div class="wx_content_wrap">
         <textarea id="wx_content" name="content">来，说说你在做什么，想什么</textarea>
      </div>
      <div class="wx_tool_wrap">
         <a id="insert_topic" href="#" hidefocus="hidefocus" class="insert_tools" title="插入话题"> </a>
         <a id="insert_person" href="#" hidefocus="hidefocus" class="insert_tools" title="提到他/她"></a>
         <a id="insert_emotion" href="#" hidefocus="hidefocus" class="insert_tools" title="插入表情"></a>
         <input id="share" type="button" value="分享" class="SmallButton"/>
         <span id="wx_letter">200</span>
      </div>
     
      <div id="emotionbox">
         <table>
  
           
    
         </table>
      </div>


</div>
<div id="bottom"></div>
</body>
</html>