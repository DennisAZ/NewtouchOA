<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml10-transitional.dtd">
<html lang="zh-cn" xml:lang="zh-cn" xmlns="http://www.w3.org/1999/xhtml">
<head>
<%

  String wxid = request.getParameter("wxid");
%>
<title>分享微讯</title>
<link rel="stylesheet" type="text/css" href="styles/weixunshare.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/js/messageutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/jpanel/weixun_share/inc/js/config.js"></script>
<script type="text/javascript">
var broadcast_id = "<%=wxid%>";
var config_no_msg = "";
var config_max_letter = 200;
var config_max_msg = "";

window.onload = function(){
   document.getElementById("wx_content").click();    
  // getUserInfo();
  // init();
   setFace();
  // getWeiXunShare();
   setPerson();
   getWxShare();
} 

function oncl(){
	  //alert("test");
	  var content=$("wx_content").value;
	  if(content=="来，说说你在做什么，想什么"){
	    $("wx_content").value="";
	    }
	}

function submitForm() {
    if (!$('wx_content')){
      alert('请添加内容');
      return;
    }
    var pars = "wx_content="+encodeURIComponent($("wx_content").innerHTML)+"&shareId="+broadcast_id;

    var url = "<%=contextPath %>/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/addWeiXunShare.act";
    var json = getJsonRs(url,pars);
    if (json.rtState == "0"){
    	window.location.href="submit.jsp";
    } else{
      alert("添加失败");
    }
  }

function getWxShare(){
	 var contStr="";
	    var url = "<%=contextPath%>/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getWeiXunById.act";
	    var rtJson = getJsonRs(url,"wxid="+broadcast_id);
	    if(rtJson.rtState == "0"){
	      var data = rtJson.rtData;
	      $("userImg").src=contextPath+"/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getPersonAvator.act?seqId="+data.userId;
	      $("userInfo").src=contextPath+"/core/funcs/userinfo/person.jsp?userId="+data.userId+"&windows=1";
	      $("cast_name").innerHTML="<span class=\"cast_time\" id=\"cast_time\">"+getTime(data.ADD_TIME)+"</span>"+data.userName;
	      
	      $("cast_content").innerHTML=data.content;

	      

	  }
	
	
}


jQuery(document).keypress(function(e){
  　 var currKey = e.keyCode||e.which||e.charCode;
   if(currKey==10){
     submitForm();
   }
   
 });

</script>
</head>
<body >
<div id="body1">
   <div id="xoverlay"></div>
   <div id="msg"></div>   
   <div id="my_cast_box">
         <div class="share_box">
            <div class="wx_tool_wrap">
               <a id="insert_topic" href="#" hidefocus="hidefocus" onclick="insert_topic()" class="insert_tools" title="插入话题"></a>
               <a id="insert_person" href="#" hidefocus="hidefocus" class="insert_tools" title="提到他/她"></a>
               <a id="insert_emotion" href="#" hidefocus="hidefocus" class="insert_tools" title="插入表情"></a>
               <div class="sendFunSmall">
                  <span class="countTxt">快捷键 Ctrl+Enter发送 <em id="wx_letter">200</em></span>
                <input type="button" class="SmallButtonB" value="分享" id="share_iframe" onclick="submitForm()" title="快捷键 Ctrl+Enter" tabindex="2">
               </div> 
            </div>

            <div id="emotionbox">
                       <TABLE><TBODY>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/0.gif' height=20 eicon='0' jQuery15107791795912095254='7'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/1.gif' height=20 eicon='1' jQuery15107791795912095254='8'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/10.gif' height=20 eicon='10' jQuery15107791795912095254='9'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/11.gif' height=20 eicon='11' jQuery15107791795912095254='10'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/12.gif' height=20 eicon='12' jQuery15107791795912095254='11'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/13.gif' height=20 eicon='13' jQuery15107791795912095254='12'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/14.gif' height=20 eicon='14' jQuery15107791795912095254='13'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/15.gif' height=20 eicon='15' jQuery15107791795912095254='14'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/16.gif' height=20 eicon='16' jQuery15107791795912095254='15'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/17.gif' height=20 eicon='17' jQuery15107791795912095254='16'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/18.gif' height=20 eicon='18' jQuery15107791795912095254='17'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/19.gif' height=20 eicon='19' jQuery15107791795912095254='18'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/2.gif' height=20 eicon='2' jQuery15107791795912095254='19'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/20.gif' height=20 eicon='20' jQuery15107791795912095254='20'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/21.gif' height=20 eicon='21' jQuery15107791795912095254='21'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/22.gif' height=20 eicon='22' jQuery15107791795912095254='22'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/23.gif' height=20 eicon='23' jQuery15107791795912095254='23'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/24.gif' height=20 eicon='24' jQuery15107791795912095254='24'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/25.gif' height=20 eicon='25' jQuery15107791795912095254='25'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/26.gif' height=20 eicon='26' jQuery15107791795912095254='26'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/27.gif' height=20 eicon='27' jQuery15107791795912095254='27'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/28.gif' height=20 eicon='28' jQuery15107791795912095254='28'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/29.gif' height=20 eicon='29' jQuery15107791795912095254='29'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/3.gif' height=20 eicon='3' jQuery15107791795912095254='30'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/30.gif' height=20 eicon='30' jQuery15107791795912095254='31'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/31.gif' height=20 eicon='31' jQuery15107791795912095254='32'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/32.gif' height=20 eicon='32' jQuery15107791795912095254='33'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/33.gif' height=20 eicon='33' jQuery15107791795912095254='34'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/34.gif' height=20 eicon='34' jQuery15107791795912095254='35'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/35.gif' height=20 eicon='35' jQuery15107791795912095254='36'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/36.gif' height=20 eicon='36' jQuery15107791795912095254='37'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/37.gif' height=20 eicon='37' jQuery15107791795912095254='38'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/38.gif' height=20 eicon='38' jQuery15107791795912095254='39'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/39.gif' height=20 eicon='39' jQuery15107791795912095254='40'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/4.gif' height=20 eicon='4' jQuery15107791795912095254='41'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/40.gif' height=20 eicon='40' jQuery15107791795912095254='42'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/41.gif' height=20 eicon='41' jQuery15107791795912095254='43'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/42.gif' height=20 eicon='42' jQuery15107791795912095254='44'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/43.gif' height=20 eicon='43' jQuery15107791795912095254='45'></TD>
   <TD class=hover><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/44.gif' height=20 eicon='44' jQuery15107791795912095254='46'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/45.gif' height=20 eicon='45' jQuery15107791795912095254='47'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/46.gif' height=20 eicon='46' jQuery15107791795912095254='48'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/47.gif' height=20 eicon='47' jQuery15107791795912095254='49'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/48.gif' height=20 eicon='48' jQuery15107791795912095254='50'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/49.gif' height=20 eicon='49' jQuery15107791795912095254='51'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/5.gif' height=20 eicon='5' jQuery15107791795912095254='52'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/50.gif' height=20 eicon='50' jQuery15107791795912095254='53'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/51.gif' height=20 eicon='51' jQuery15107791795912095254='54'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/52.gif' height=20 eicon='52' jQuery15107791795912095254='55'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/53.gif' height=20 eicon='53' jQuery15107791795912095254='56'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/54.gif' height=20 eicon='54' jQuery15107791795912095254='57'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/55.gif' height=20 eicon='55' jQuery15107791795912095254='58'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/56.gif' height=20 eicon='56' jQuery15107791795912095254='59'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/57.gif' height=20 eicon='57' jQuery15107791795912095254='60'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/58.gif' height=20 eicon='58' jQuery15107791795912095254='61'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/59.gif' height=20 eicon='59' jQuery15107791795912095254='62'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/6.gif' height=20 eicon='6' jQuery15107791795912095254='63'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/60.gif' height=20 eicon='60' jQuery15107791795912095254='64'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/61.gif' height=20 eicon='61' jQuery15107791795912095254='65'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/62.gif' height=20 eicon='62' jQuery15107791795912095254='66'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/63.gif' height=20 eicon='63' jQuery15107791795912095254='67'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/64.gif' height=20 eicon='64' jQuery15107791795912095254='68'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/65.gif' height=20 eicon='65' jQuery15107791795912095254='69'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/66.gif' height=20 eicon='66' jQuery15107791795912095254='70'></TD></TR>
   <TR>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/67.gif' height=20 eicon='67' jQuery15107791795912095254='71'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/68.gif' height=20 eicon='68' jQuery15107791795912095254='72'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/69.gif' height=20 eicon='69' jQuery15107791795912095254='73'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/7.gif' height=20 eicon='7' jQuery15107791795912095254='74'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/70.gif' height=20 eicon='70' jQuery15107791795912095254='75'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/71.gif' height=20 eicon='71' jQuery15107791795912095254='76'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/72.gif' height=20 eicon='72' jQuery15107791795912095254='77'></TD>
   <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/73.gif' height=20 eicon='73' jQuery15107791795912095254='78'></TD></TR></TBODY></TABLE>

   
               
            </div>

            
            <div id="userbox">
               <div id="userbox_input_wrap"><input type="text" id="userbox_input" name="atuser_name" value="" onclick=""/></div>
               <ul id="userbox_result"></ul>   
            </div>
            <div class="wx_content_wrap">
              <textarea id="wx_content" onclick="oncl();" onkeyup="letter_stat();" name="content">来，说说你在做什么，想什么</textarea>
            </div>
                   
         </div>
         
   </div>
   

   <div id="theader" style="padding:20px 10px;background:url('/images/wx_share/wx_foot_bg.jpg') repeat-x top center;">
      <div class="cast_box">
         <a class="cast_pic" href="" id="userInfo" target="_blank" hidefocus="hidefocus" title="点击查看个人信息"><img id="userImg" src="" width="40" align="absmiddle" /></a>
         <span class="cast_info">
          <p class="cast_name" id="cast_name"></p>
            <p class="cast_content" id="cast_content"></p>
         </span>
      </div>
      <div class="clearfix"></div>
   </div>

</div>
</body>
</html>
