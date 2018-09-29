jQuery.noConflict();
(function($){
 
   $(".module_body").live("mouseover", function(){
      $(this).css("background-color","#F5F5F5");
      //$(this).find(".time_broadcast").show();
      //$(this).find(".time_time").hide();
   });
   $(".module_body").live("mouseout", function(){
      $(this).css("background-color","");
      //$(this).find(".time_broadcast").hide();
      //$(this).find(".time_time").show();
   });
   
   $("#body").click(function(){
      resetUI();
   });
   
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
   
   $(".wx_content_wrap").click(function(e){
      e.stopPropagation();
      resetUI('wx_content_wrap');
      var v = $(this).find("#wx_content");
      var s = v.val();
      $(".wx_content_wrap").addClass("wx_content_wrap_ac");
      if(s!=config_no_msg)
         return;
      else
         v.focus();v.val("");
   });
   
   $(".userpic a").live("click",function(){
      var uid = user_id = '';
      var uid = $(this).attr("data_uid");
      var user_id = $(this).attr("data_user_id");
      if(parent)
         parent.openURL("/general/ipanel/user/person.php?I_VER="+i_ver+"&UID="+uid+"&USER_ID="+unescape(user_id));    
   });
   
   $(".time_broadcast a").live("click",function(){
      var wxid = $(this).attr("data_wxid");
      if(ispirit!=""){
         parent.openURL("/general/ipanel/smsbox/broadcast.php?I_VER="+i_ver+"&wxid="+wxid);
      }else{
         mytop=(screen.availHeight-405)/2;
         myleft=(screen.availWidth-400)/2;
         window.open ("/general/ipanel/smsbox/broadcast.php?I_VER="+i_ver+"&wxid="+wxid, "newwindow2", "height=405, width=400, top="+mytop+", left="+myleft+",toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
      }    
   });
   
   $("#insert_topic, #insert_person, #userbox_input, #insert_emotion, #share, #emotionbox img.emotion_icon").click(function(e){
       e.stopPropagation();      
   });
   
   //插入话题
   $("#insert_topic").click(function(){
      var con = td_lang.inc.msg_56;//"请在这里输入话题"

       if($("#emotionbox:visible").length > 0){
         $("#insert_emotion").removeClass("insert_emotion_cur");
         $("#emotionbox").hide();
       }
       
       if(($("#wx_content").val() == config_no_msg) && (!$(".wx_content_wrap").hasClass("wx_content_wrap_ac")))
            $(".wx_content_wrap").click();
         
       $("#wx_content").append("#"+con+"#");
       
       //剔除掉Img对象
       if($("#wx_content > img"))
         img_size = $("#wx_content > img").length;
       
       //剔除掉Img对象  
       if($("#wx_content > input"))
         input_size = $("#wx_content > input").length;
         
         var l = $("#wx_content").val().length + img_size + input_size;
       
       //创建选择区域	
       if($("#wx_content")[0].createTextRange){//IE浏览器
           var range = $("#wx_content")[0].createTextRange();
           range.moveEnd("character",-l)                     
           range.moveEnd("character",l-1);
           range.moveStart("character", l-1-con.length);
           range.select();
       }else{
           $("#wx_content")[0].setSelectionRange(l-1-con.length,l-1);
           $("#wx_content")[0].focus();
       }
   });
   
   //打开表情
   $("#insert_emotion").click(function(){
	   alert("test");
      
   resetUI('insert_emotion');
      
      if($("#emotionbox:visible").length > 0){
         $(this).removeClass("insert_emotion_cur");
         $("#emotionbox").hide();   
      }else{
         $(this).addClass("insert_emotion_cur");
            
         if($(".wx_content_wrap").hasClass("wx_content_wrap_ac"))
            $("#emotionbox").css("top","92px");
         else
            $("#emotionbox").css("top","62px");
         $("#emotionbox").show();
      }
   });
   
   $("#emotionbox img.emotion_icon").hover(
      function(){$(this).parent("td").addClass("hover");},
      function(){$(this).parent("td").removeClass("hover");}
   );
   
   //插入表情动作
   $("#emotionbox img.emotion_icon").click(function(){
      if($("#wx_content").val() == config_no_msg){
         $(".wx_content_wrap").addClass("wx_content_wrap_ac"); 
         $("#wx_content").val("");      
      }
      o = document.createElement("IMG");
      o.src = $(this).attr("src");
      o.eicon = $(this).attr("eicon");
      $("#wx_content").append(o);
      $("#emotionbox").hide();
      $("#insert_emotion").removeClass("insert_emotion_cur");
      $("#wx_content").focus();
      var Sel = document.selection.createRange();
      Sel.moveStart ('character', $("#wx_content")[0].value.length + $("#wx_content > img, #wx_content > input").length);
      Sel.collapse(true);
      Sel.select();
      letter_stat(); 
   });
   
   //@人员
   $("#insert_person").click(function(){
      
      resetUI('insert_person');
      
      if($("#userbox:visible").length > 0){
         $(this).removeClass("insert_person_cur");
         $("#userbox").hide();   
      }else{
         $(this).addClass("insert_person_cur");
         $("#userbox").show();
         $("#userbox_input").select();
      }        
   });
   
   //人员选取滑动效果
   $("#userbox #userbox_result li").live("mouseover", function(){$(this).addClass("cur");});
   $("#userbox #userbox_result li").live("mouseout", function(){$(this).removeClass("cur");});
   
   $("#userbox #userbox_result li").live("click",function(){
      var uid = $(this).attr("data_uid");
      var user_name = $(this).attr("data_username");
      var strlen = Len(user_name);
      o = $("<input class='obj_person' value='"+user_name+"' data_username='"+user_name+"' data_uid='"+uid+"' disabled='disabled' />");
      o.width((36 + strlen*5)+"px");
      $("#wx_content").click().append(o);
      letter_stat();
   });
   
   //人员搜索
   $("#userbox_input").live("keyup",function(){
      var key = $(this).val();
      if(key == ""){
         $("#userbox #userbox_result").empty().height("0px").hide();
         return;
      }
      $.get("search_user.php",{"KWORD":key},function(data){
         if(data == ""){
            $("#userbox #userbox_result").empty().height("0px").hide();      
         }else{
            $("#userbox #userbox_result").empty().append(data);
            if(data!="" && (data.split('li>').length-1) > 5){
               $("#userbox #userbox_result").height((5 * 20)+'px');
               $("#userbox #userbox_result").css('overflow-y','auto');   
            }else{
               $("#userbox #userbox_result").height('auto');            
            }
            $("#userbox #userbox_result").show();   
         }
      });      
   });
      
   function resetUI(stype){
      
      if(typeof(stype) == "undefined")
         stype = '';
      
      //清理表情面板
      if(stype!="insert_emotion"){
         $("#insert_emotion").removeClass("insert_emotion_cur");
         $("#emotionbox").hide();
      }
      
      //清理输入框样式
      if(stype!="wx_content_wrap"){
         s = $("#wx_content").val();
         if(s == config_no_msg || $("#wx_content").html() == ""){
            $(".wx_content_wrap").removeClass("wx_content_wrap_ac");
            $("#wx_content").val(config_no_msg);
         }   
      }
      
      //关闭选人面板
      if(stype!="insert_person"){
         $("#insert_person").removeClass("insert_person_cur");
         $("#userbox").hide();
      }
   
   }
   
   $("#share").click(function(e){
      e.stopPropagation();
      var content = $("#wx_content").html();
      var img = $("#wx_content > img").length; 
      if(content == config_no_msg || content == ""){
         if(img == 0){
            Message(td_lang.inc.msg_55);//"输入点什么再分享吧:"
            return;
         }   
      }
      
      //表情替换
      content = content.replace(/<img.*?eicon=\"(.*?)\".*?>/ig,"[em]"+'$1'+"[/em]");

      //@替换
      content = content.replace(/<input.*?data_uid=\"(.*?)\".*?data_username=\"(.*?)\".*?>/ig,"[@=$1]"+'$2'+"[/@]");
     
      $.get("save_share.php",{"CONTENT":content},function(data){location.reload();});      
   });   
})(jQuery);


   