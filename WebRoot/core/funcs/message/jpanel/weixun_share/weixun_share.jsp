<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml10-transitional.dtd">
<html lang="zh-cn" xml:lang="zh-cn" xmlns="http://www.w3.org/1999/xhtml">
<%
  String totalRecord = request.getParameter("sizeNo");
  if(totalRecord == null){
    totalRecord = "0";
  }
  String pageNo = request.getParameter("pageNo");
  if(pageNo == null){
    pageNo = "0";
  }
  String pageSize = request.getParameter("pageSize");
  if(pageSize == null){
    pageSize = "6";
  }
  String im = request.getParameter("im");
  if(im == null){
    im = "";
  }
%>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="styles/weixunshare1.css" />
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
var page = 0;
var data_type = 'blog';
var ispirit = '';
var i_ver = '';
var config_no_msg = "来，说说你在做什么，想什么";
var config_max_letter = 200;

var pageNo = "<%=pageNo%>";
var pageSize = "<%=pageSize%>";
var totlePage = "";
var im = "<%=im%>";
var total=0;
var num=0;
var len=0;


function init(){
	  var url = "<%=contextPath%>/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getWeiXun.act?pageNo=" + pageNo  + "&pageSize=" + pageSize;
	  
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    bindJson2Cntrl(rtJson.rtData);
	    var promStr = "";
	   // alert(rtJson.rtData.pageData.length);
	   //len=rtJson.rtData.pageData.length;
	    if(rtJson.rtData.pageData.length < 1){
	      return;
	    }
	    totlePage = (rtJson.rtData.totalRecord%pageSize == 0)?Math.floor(rtJson.rtData.totalRecord/pageSize) - 1 : Math.floor(rtJson.rtData.totalRecord/pageSize);
		total=rtJson.rtData.totalRecord;
		if(total>5){
			$("get_more_btn").style.display="block";
		}
	    for(var i = 0; i < rtJson.rtData.pageData.length; i++){
	      var obj = rtJson.rtData.pageData[i];
	      promStr += toPanelInBoxStr2(obj);
	      num=num+1;
	    }
	    //alert(num);
	  var sss=  $('talkList').innerHTML;
	  promStr=sss+promStr;
	  $('talkList').innerHTML=promStr;
	   
	  }
}


function get_more(){
	if(num==total){
		alert("数据已经全部显示");
		return;	
	}
	pageNo=parseInt(pageNo)+1;
	pageSize=parseInt(pageSize);
	//if(pageNo*pageSize
	init();
}

function rescroll(){
   $("msg").style.top = 0;
   $("xoverlay").style.height = $("body").scrollHeight;
   $("msg").style.marginTop = ($("body").scrollTop + $("body").clientHeight/2 - $('msg').style.height/2) + "px";   
}

//计算数字
function letter_stat(){

    var d = $("wx_content").value;
    if(d==""){
       $("wx_letter").innerHTML= 200;
      return ;
    }
    if(d.length>200){
      alert("最大输入字数 为200字");
      $("wx_content").value=d.substring(0,200);
    }else{
      var len=d.length;
       $("wx_letter").innerHTML= (200-d.length);

    } 
    
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

function submitForm() {

    if ($('wx_content').value=="来，说说你在做什么，想什么" ||$('wx_content').innerHTML=="" ){
        alert('你不想分享点什么吗');
        return;
      }
	  var pars = "wx_content="+encodeURIComponent($("wx_content").innerHTML)+"&name=1";
	  var url = "<%=contextPath %>/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/addWeiXunShare.act";
	  var json = getJsonRs(url,pars);
	  if (json.rtState == "0"){

	   window.location.reload(true);
	  } else{
	    alert("添加失败");
	  }
	}
	
	
function getTime(timeStr){
	 if (timeStr==""){
	     
	      return;
	    }
	    var url = "<%=contextPath %>/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getTime.act";
	    var json = getJsonRs(url,"timeStr="+timeStr);
	    if (json.rtState == "0"){
	       return json.rtData.data;
	    } else{
	      
	    }
	
	
}
	
function oncl(){
	//alert("test");
  var content=$("wx_content").value;
	if(content=="来，说说你在做什么，想什么"){
		$("wx_content").value="";
		}
}

function show(){
	  var content=$("wx_content").value;
		if(content==""){
			$("wx_content").value="来，说说你在做什么，想什么";
		}
}

//添加表情


//插入话题
function insert_topic(){
   var con = "在这里插入话题";
var wx_content = $("wx_content").value;

if(wx_content=="来，说说你在做什么，想什么"){
  $("wx_content").value="";
}

if(jQuery("#emotionbox:visible").length > 0){
     jQuery("#insert_emotion").removeClass("insert_emotion_cur");
     jQuery("#emotionbox").hide();
       }
       
       if((jQuery("#wx_content").val() == config_no_msg) && (!jQuery(".wx_content_wrap").hasClass("wx_content_wrap_ac")))
         jQuery(".wx_content_wrap").click();
         
       jQuery("#wx_content").append("#在这里插入话题#");
       
       //剔除掉Img对象
       if(jQuery("#wx_content > img"))
         img_size = jQuery("#wx_content > img").length;
       
       //剔除掉Img对象  
       if(jQuery("#wx_content > input"))
         input_size = jQuery("#wx_content > input").length;
         
         var l = jQuery("#wx_content").val().length + img_size + input_size;
       
       //创建选择区域 
       if(jQuery("#wx_content")[0].createTextRange){//IE浏览器
           var range = jQuery("#wx_content")[0].createTextRange();
           range.moveEnd("character",-l)                     
           range.moveEnd("character",l-1);
           range.moveStart("character", l-1-con.length);
           range.select();
       }else{
         jQuery("#wx_content")[0].setSelectionRange(l-1-con.length,l-1);
         jQuery("#wx_content")[0].focus();
       }
  

}



//插入话题
function insert_topic(){
 var con = "在这里插入话题";
var wx_content = $("wx_content").value;

if(wx_content=="来，说说你在做什么，想什么"){
$("wx_content").value="";
}

if(jQuery("#emotionbox:visible").length > 0){
   jQuery("#insert_emotion").removeClass("insert_emotion_cur");
   jQuery("#emotionbox").hide();
     }
     
     if((jQuery("#wx_content").val() == config_no_msg) && (!jQuery(".wx_content_wrap").hasClass("wx_content_wrap_ac")))
       jQuery(".wx_content_wrap").click();
       
     jQuery("#wx_content").append("#在这里插入话题#");
     
     //剔除掉Img对象
     if(jQuery("#wx_content > img"))
       img_size = jQuery("#wx_content > img").length;
     
     //剔除掉Img对象  
     if(jQuery("#wx_content > input"))
       input_size = jQuery("#wx_content > input").length;
       
       var l = jQuery("#wx_content").val().length + img_size + input_size;
     
     //创建选择区域 
     if(jQuery("#wx_content")[0].createTextRange){//IE浏览器
         var range = jQuery("#wx_content")[0].createTextRange();
         range.moveEnd("character",-l)                     
         range.moveEnd("character",l-1);
         range.moveStart("character", l-1-con.length);
         range.select();
     }else{
       jQuery("#wx_content")[0].setSelectionRange(l-1-con.length,l-1);
       jQuery("#wx_content")[0].focus();
     }


}


function doInit(){
	  setFace();
	   //getUserInfo();
	     init();
	     setPerson();
	     BroadCastBlock();
}

</script>
</head>
<body onload="doInit()">
   <div id="sub_tabs" class="sub_tabs"></div>
   <div id="xoverlay"></div>
   <div id="msg"></div>
   <div class="share_box">
      <div class="wx_content_wrap">
         <textarea type="text" id="wx_content" onkeyup="letter_stat()" onclick="oncl();" onmouseover="show();" name="wx_content" >来，说说你在做什么，想什么</textarea>
      </div>
      <div class="wx_tool_wrap">
          <a id="insert_topic" href="#" hidefocus="hidefocus" onclick="insert_topic()" class="insert_tools" title="插入话题"></a>
          <a id="insert_person" href="#" hidefocus="hidefocus" class="insert_tools" title="提到他/她"></a>
          <a id="insert_emotion" href="#" hidefocus="hidefocus" class="insert_tools" title="插入表情"></a>
                             <span id="wx_letter">200 </span>字
                                
         <input id="share" type="button" value="分享" class="SmallButton" onclick="submitForm()"/>
        
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
               <div id="userbox_input_wrap"><input type="text" id="userbox_input" name="atuser_name" value="" onclick="" /></div>
               <ul id="userbox_result"></ul>
            </div>
 
   </div>
   
      
     
      <div class="AL">
         
         <ul id="talkList" class="LC">
            
    
         </ul>
         <div class="clearfix"></div>
        </div>

	<div id="get_more_btn" style="display:none" align="center"><input type="button"  value="查看更多" class="SmallButtonB" onclick="get_more();" /></div>
<div id="bottom"></div>

</body>
</html>