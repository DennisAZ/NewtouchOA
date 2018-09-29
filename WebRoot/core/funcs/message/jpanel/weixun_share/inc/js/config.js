
function toPanelInBoxStr1(obj){

  var promptStr="<li id='"+obj.seqId+"' rel=''>";
      promptStr+="<div class='userPic'>";
      promptStr+="<a href='javascript:IM_open(\""+contextPath+"/core/funcs/message/jpanel/weixun_share/user.jsp?seqId="+obj.seqId+"\")' title='"+obj.userName+"' rel='' >";
      promptStr+="<img src='"+contextPath+"/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getPersonAvator.act?seqId="+obj.seqId+"' title='"+obj.userName+"'></a>";
      promptStr+="</div><div class='msgBox'><div class='userName' rel=''>";
      promptStr+="<strong><a href='javascript:;' title='' rel=''</a>"+obj.userName+": </strong> </div>";
      promptStr+="<div class='msgCnt'>";
      promptStr+=obj.content;
      promptStr+="</div>"; 
      var broadcast=obj.broadcast;
      var bcContent= broadcastContent(broadcast);
      promptStr+=bcContent;
      promptStr+= "<div class='pubInfo'>"; 
      promptStr+="<div class='funBox'>";
      promptStr+="<a href='javascript:;' wxid='"+obj.wxId+"' class='relay'>转播</a>";
	  promptStr+= "<span>|</span><a href='javascript:delWeixun("+obj.wxId+");' class='comt' num='0' wxid=''>删除</a>";
      promptStr+="</div>";
	  promptStr+="<span class='time'>"+getTime(obj.addtime)+"</span>";
	  promptStr+="      </div>";
	  promptStr+="     </div>";
	  promptStr+="     </li>";
  
    return promptStr;
  }



function toPanelInBoxStr2(obj){

  var promptStr="<li id='"+obj.wxId+"' rel=''>";
      promptStr+="<div class='userPic'>";
      promptStr+="<a href='javascript:IM_open(\""+contextPath+"/core/funcs/message/jpanel/weixun_share/user.jsp?seqId="+obj.seqId+"\")' title='"+obj.userName+"' rel='' > ";
      promptStr+="<img src='"+contextPath+"/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getPersonAvator.act?seqId="+obj.seqId+"' title='"+obj.userName+"'></a>";
      promptStr+="</div><div class='msgBox'><div class='userName' rel=''>";
      promptStr+="<strong><a href='javascript:;' title='' rel=''</a>"+obj.userName+" </strong><span class='time'>"+getTime(obj.addtime)+"</span> </div>";

      promptStr+= "<div class='pubInfo'>"; 
      promptStr+="<div class='funBox'>";
      promptStr+="<a href='javascript:;' wxid='"+obj.wxId+"' onclick='brocast("+obj.wxId+")' class='relay1'>转播</a>";
	//  promptStr+= "<span>|</span><a href='javascript:delWeixun("+obj.wxId+");' class='comt' num='0' wxid=''>删除</a>";
      promptStr+="</div>";
      promptStr+="<div class='msgCnt'>";
      promptStr+=obj.content;
      promptStr+="</div>";
	  promptStr+="      </div>";
      var broadcast=obj.broadcast;
      var bcContent= broadcastContent1(broadcast);
      promptStr+=bcContent;
	  promptStr+="     </div>";
	  promptStr+="     </li>";
  
    return promptStr;
  }

//删除微讯
function delWeixun(id){
	if(!confirm("确定删除该微讯吗？")){
		return;
	}
	  var url = contextPath+"/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/delWeiXun.act?seqId="+id;
   var rtJson = getJsonRs(url);
   if(rtJson.rtState == "0"){
   	 window.location.reload();
   }

}


function getTime(timeStr){
   if (timeStr==""){
       
        return;
      }
      var url = contextPath+"/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getTime.act";
      var json = getJsonRs(url,"timeStr="+timeStr);
      if (json.rtState == "0"){
         return json.rtData.data;
      } else{
        
   }
}

//兼容精灵
function broadcastContent1(data){


  var retuStr=""; 
  if(data!=""){
    data=parseObj(data);
    retuStr += "<div class='comment'>";
	if( data.id!="" )
	{
		
		retuStr += "<a href='user.jsp?seqId="+data.uid+"' rel='"+data.userName+"'>"+data.userName+":</a>";
		retuStr += "<em>"+data.content+"</em>";
		retuStr += " <div class='pubInfo'>";
		retuStr += " <span class='left'>"+getTime(data.time);
		retuStr += " <a href='#' class='allbroad'>转播"+data.num+"次</a>";
		retuStr += " </span>";
		retuStr += "   <div class='funBox'>";
		retuStr += "     <a href='javascript:;'  wxid='"+data.id+"' onclick='brocast("+data.id+")' class='relay1'>转播</a>";
		retuStr += "   </div>";
		retuStr += " </div>";
		
	
	 }else{
	  retuStr += "<em>该微讯已被原作者删除</em>";
     }

	retuStr += "<div class='clearfix'></div>"; 
	retuStr += " </div>";
  }

  return retuStr;
}   

function broadcastContent(data){


  var retuStr=""; 
  if(data!=""){
    data=parseObj(data);
    retuStr += "<div class='comment'>";
	if( data.id!="" )
	{
		
		retuStr += "<a href='user.jsp?seqId="+data.uid+"' rel='"+data.userName+"'>"+data.userName+":</a>";
		retuStr += "<em>"+data.content+"</em>";
		retuStr += " <div class='pubInfo'>";
		retuStr += " <span class='left'>"+getTime(data.time);
		retuStr += " <a href='#' class='allbroad'>转播"+data.num+"次</a>";
		retuStr += " </span>";
		retuStr += "   <div class='funBox'>";
		retuStr += "     <a href='javascript:;' wxid='"+data.id+"' class='relay'>转播</a>";
		retuStr += "   </div>";
		retuStr += " </div>";
		
	
	 }else{
	  retuStr += "<em>该微讯已被原作者删除</em>";
     }

	retuStr += "<div class='clearfix'></div>"; 
	retuStr += " </div>";
  }

  return retuStr;
}    






/* function addFace(){
   var tableStr="";
   var tdStr="";
   var row=1;
   var col=7;
   var total=0;
  for(var j=1;j<=col;j++){
	  if(total>72){
		  break;
	  }
	  
	  tdStr+="  <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/"+j+".gif' height=20 eicon='"+j+"' jQuery15107791795912095254='"+j+"'></TD> ";   
       total++;
       if(total/8==0 && total!=0){
    	   tableStr+="<TR>"+tdStr+"</TR>";
    	   tdStr="";
       }
   for(var i=0;i<10;i++){
	   if(total>72){
			  break;
		  }
       var num=j+""+i;
       tdStr+="  <TD><IMG class=emotion_icon src='/yh/core/frame/ispirit/n12/images/face/"+num+".gif' height=20 eicon='"+num+"' jQuery15107791795912095254='"+num+"'></TD> ";   
	   total++;
       if(total/8==0 && total!=0){
    	   tableStr+="<TR>"+tdStr+"</TR>";
    	   tdStr="";
       }
   }
 }
  
  tableStr="<table>"+tableStr+"</table>";
	alert(tableStr);
 }

*/

function setFace(){
//打开表情
jQuery("#insert_emotion").click(function(){
   
  
   
   if(jQuery("#emotionbox:visible").length > 0){
	   jQuery(this).removeClass("insert_emotion_cur");
	   jQuery("#emotionbox").hide();   
   }else{
	   jQuery(this).addClass("insert_emotion_cur");
      //$("#emotionbox").css("top","144px");
	   jQuery("#emotionbox").css("left","50px");
	   jQuery("#emotionbox").show();
   }   
});

jQuery("#emotionbox img.emotion_icon").hover(
   function(){jQuery(this).parent("td").addClass("hover");},
   function(){jQuery(this).parent("td").removeClass("hover");}
);

//插入表情动作
jQuery("#emotionbox img.emotion_icon").click(function(){
   if(jQuery("#wx_content").html() == config_no_msg){
	   jQuery(".wx_content_wrap").addClass("wx_content_wrap_ac"); 
	   jQuery("#wx_content").val("");      
   }
   o = document.createElement("IMG");
   o.src = jQuery(this).attr("src");
   o.style.height = "20px";
   o.style.width = "20px";
   o.eicon = jQuery(this).attr("eicon");
   jQuery("#wx_content").focus();
   jQuery("#wx_content").append(o);
   jQuery("#emotionbox").hide();
   jQuery("#insert_emotion").removeClass("insert_emotion_cur");

 /*  var Sel = document.selection.createRange();
   Sel.moveStart ('character', jQuery("#wx_content")[0].value.length + jQuery("#wx_content > img, #wx_content > input").length);
   Sel.collapse(true);
   Sel.select();*/
   //letter_stat(); 
});
}


function setPerson(){
	  
	
	 //@人员
	   jQuery("#insert_person").click(function(){
	      
	      resetUI('insert_person');
	      
	      if(jQuery("#userbox:visible").length > 0){
	    	  jQuery(this).removeClass("insert_person_cur");
	    	  jQuery("#userbox").hide();   
	      }else{
	    	  jQuery(this).addClass("insert_person_cur");
	    	  jQuery("#userbox").show();
	    	  jQuery("#userbox_input").select();
	      }        
	   });
	   
	   //人员选取滑动效果
	   jQuery("#userbox #userbox_result li").live("mouseover", function(){jQuery(this).addClass("cur");});
	   jQuery("#userbox #userbox_result li").live("mouseout", function(){jQuery(this).removeClass("cur");});
	   
	   jQuery("#userbox #userbox_result li").live("click",function(){
		   
		   var wx_content = $("wx_content").value;

		   if(wx_content=="来，说说你在做什么，想什么"){
		 	  $("wx_content").value="";
		   }
		
	      var flag = false;  
	      var uid = jQuery(this).attr("data_uid");
	      var objPers = jQuery("#wx_content > input.obj_person");
	      objPers.each(function(){
	         if(jQuery(this).attr("data_uid") == uid){
	            flag = true;
	            alert("此人已经添加");
	            return false;
	         }   
	      });
	      
	      if(flag){
	    	  jQuery("#wx_content").focus();
	         var Sel = document.selection.createRange();
	         Sel.moveStart ('character', jQuery("#wx_content")[0].value.length + jQuery("#wx_content > img, #wx_content > input").length);
	         Sel.collapse(true);
	         Sel.select();
	         return;   
	      }
	      
	      var user_name = jQuery(this).attr("data_username");
	      var strlen = Len(user_name);
	      var o_len=(36 + strlen*5)+"px";
	      o = jQuery("<input class='obj_person' width='"+o_len+"' value='"+user_name+"' data_username='"+user_name+"' data_uid='"+uid+"' disabled='disabled' />");
	      
	     //var obj_input="<input class='obj_person' width='"+o_len+"' value='"+user_name+"' data_username='"+user_name+"' data_uid='"+uid+"' disabled='disabled' />";
	     // alert(obj_input);
	      //jQuery("#wx_content").click().append(obj_input);
	      jQuery("#wx_content").append(o);
	      //letter_stat();
	   });
	   
	   //人员搜索
	   jQuery("#userbox_input").live("keyup",function(){
	      var key = jQuery(this).val();
	      if(key == ""){
	    	  jQuery("#userbox #userbox_result").empty().hide();
	         return;
	      }
	      jQuery.get(contextPath+"/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/search_user.act",{"KWORD":key},function(data){
	         if(data == ""){
	        	 jQuery("#userbox #userbox_result").empty().hide();      
	         }else{
	        	 jQuery("#userbox #userbox_result").empty().append(data);
	            if(data!="" && (data.split('li>').length-1) > 5){
	            	jQuery("#userbox #userbox_result").height((5 * 20)+'px');
	            	jQuery("#userbox #userbox_result").css('overflow-y','auto');   
	            }else{
	            	jQuery("#userbox #userbox_result").height('auto');            
	            }
	            jQuery("#userbox #userbox_result").show();   
	         }
	      });      
	   });
}


function resetUI(stype){
    
   // if(!mutiTalkBox) return;
    
    if(typeof(stype) == "undefined")
       stype = '';
    
    //清理表情面板
    if(stype!="insert_emotion"){
    	jQuery("#insert_emotion").removeClass("insert_emotion_cur");
    	jQuery("#emotionbox").hide();
    }
    
    //清理输入框样式
    if(stype!="wx_content_wrap"){
       s = jQuery("#wx_content").html();
       if(s == config_no_msg || jQuery("#wx_content").html() == ""){
    	   jQuery(".wx_content_wrap").removeClass("wx_content_wrap_ac");
    	   jQuery("#wx_content").val(config_no_msg);
       }   
    }
    
    //关闭选人面板
    if(stype!="insert_person"){
    	jQuery("#insert_person").removeClass("insert_person_cur");
    	jQuery("#userbox").hide();
    }
 
 }


function Len(str)
{
     var i,sum;
     sum=0;
     for(i=0;i<str.length;i++)
     {
         if ((str.charCodeAt(i)>=0) && (str.charCodeAt(i)<=255))
             sum=sum+1;
         else
             sum=sum+2;
     }
     return sum;
}

var config_no_msg ="来，说说你在做什么，想什么";
function submitAble(){
	  var d=jQuery("#wx_content");
	  var sHTML = d.html();

      var sv = d.val();
      
      if ($('wx_content').value=="来，说说你在做什么，想什么"){
    	  alert("你不想分享点什么吗？");
	     return false;
      }else{
    	  return true;
      }
         
}


function BroadCastBlock(){
/*jQuery("body").click(function(){
	      resetUI();
	   });*/
	
jQuery(".relay").live("click",function(){
    var wxid = jQuery(this).attr("wxid");
    if(!wxid){
       alert("无法获取wxid");
       return;
    }
    jQuery("#wxiframe").attr("src","broadcast.jsp?wxid="+wxid);
    show_msg("#BroadCastBlock");

 });

jQuery(".option").click(function(){
    hide_msg("#BroadCastBlock");      
 });
}


function show_msg(msgBlock)
{
	jQuery("#overlay").show();
	jQuery(msgBlock).show();
   var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
   jQuery("#overlay").css({"width":bb.scrollWidth+"px","height":bb.scrollHeight+"px"});
   var msgBlockLeft = ((bb.offsetWidth - jQuery(msgBlock)[0].offsetWidth)/2)+"px";
   var msgBlockTop  = (60 + bb.scrollTop)+"px";
   jQuery(msgBlock).css({"left":msgBlockLeft,"top":msgBlockTop});
}

function hide_msg(msgBlock)
{
	jQuery("#overlay").hide();
	jQuery(msgBlock).hide();
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


function brocast(wxid){
	
	 var url = contextPath+"/core/funcs/message/jpanel/weixun_share/broadcast1.jsp?wxid="+wxid;
	if(typeof(window.external.OA_SMS) != 'undefined'){
		  window.external.OA_SMS(url,'306,300', "OPEN_URL");
	}else{
     window.open (url, 'newwindow', 'height=340, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=700, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');

    }
	
}



function parseObj(str){
	var obj=eval("("+str+")") ;  
    return obj;
}


function IM_open(url){
	if(typeof(window.external.OA_SMS) != 'undefined'){
		  window.external.OA_SMS(url,'600,500', "OPEN_URL");
	}else{
   window.open (url, 'newwindow', 'height=500, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=600, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');

  }
	
	
	
}

