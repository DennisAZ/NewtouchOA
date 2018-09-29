
//-- 滚动条 --
(function($){
$.fn.extend({
   mousewheel:function(Func){
      return this.each(function(){
         var _self = this;
          _self.D = 0;//滚动方向
         if($.browser.msie||$.browser.safari){
            _self.onmousewheel=function(){_self.D = event.wheelDelta;event.returnValue = false;Func && Func.call(_self);};
         }else{
            _self.addEventListener("DOMMouseScroll",function(e){
               _self.D = e.detail>0?-1:1;
               e.preventDefault();
               Func && Func.call(_self);
            },false); 
         }
      });
   }
});
})(jQuery);

jQuery.noConflict();

//通知中心自动关闭时间，秒
var nocbox_close_timeout = 3;
var closeNocPanel = null;

(function($){


   $(document).ready(function($){
   	
   //lp 事务提醒初始化事件
	function initNocbox(){
		
		//绑定打开事务提醒
      var openBtn = $('#nocbox');
      openBtn.click(function(){
      	var nocboxPanel = $('#new_noc_panel').css("display");
      	nocboxPanel == "none" ? noc_mon() : CloseNoc();	
      });
      
		//内容块hover效果
	 	var nocBlocks = $('#new_noc_list > div.noc_iterm > ul.noc_iterm_data > li');
	   nocBlocks.live('mouseenter', function(){$(this).addClass('noc_iterm_hover');});
	   nocBlocks.live('mouseleave', function(){$(this).removeClass('noc_iterm_hover');});
		
		//点击查看提醒
	   $('#new_noc_list > div.noc_iterm > ul.noc_iterm_data > li').live('click', function(){
	      var url = $(this).attr('url');
	      var sms_id = $(this).attr('sms_id');
	      var type_id = $(this).attr('type_id');
         RemoveNoc($(this),sms_id, 0);
        	if(url!=""){
        		openURL(url);
      	}
	   });
	   
	   $('#new_noc_list > div.noc_iterm > ul.noc_iterm_data > li > a').click(function(e){
	      e.stopPropagation();   
	   })
	   
	   //点击查看历史记录
	   $('#check_remind_histroy').live('click', function(){
         openURL("/general/sms/remind_center");   
	   });
	   
	   //点击全部已阅
	   $('#ViewAllNoc').live('click',function(){
	   	var idstr = get_noc_idstr();
	   	jQuery.ajax({
		      type: 'POST',
		      url: '/general/status_bar/sms_submit.php',
		      data: {'SMS_ID': idstr},
		      cache: false,
		      success: function(){
		      	jQuery('#new_noc_list').empty();
				   var datanum = get_noc_num();
	            jQuery("#new_noc_panel > #new_noc_title > .noc_iterm_num >span").html(datanum);
	            jQuery("#new_noc_panel > .button").find("#ViewAllNoc,#ViewDetail").hide();
	            CloseNoc();
				},
		      error: function(request, textStatus, errorThrown){
		      	jQuery('#new_noc_list').hide();
		         jQuery('#nocbox_tips').html('<div class="error">'+td_lang.inc.msg_24+'(' + request.status + ')：' + textStatus + '</div>').show();
		      }
		   });
	   });
	   
	   //点击全部详情
	   $('#ViewDetail').live('click',function(){
	   	var idstr = firsturl = separator = "";
	   	var idobj = $('#new_noc_list > .noc_iterm > .noc_iterm_data > li');
	   	var readobj = $('#new_noc_list > div.noc_iterm > .noc_iterm_title > .noc_iterm_read');
	   	var idstr_all = get_noc_idstr();
	   	idobj.each(function(){
	   		url = $(this).attr("url");
	   		sms_id = $(this).attr("sms_id");
	   		if(url!="" && firsturl==""){
	   			firsturl = url;
	   		}
	   		if(url!=""){
	   			idstr += separator + jQuery(this).attr("sms_id");
	   			separator = ",";
	   		}
	   	});
	   	
	   	window.open('/module/nav/?SMS_ID_STR='+idstr+'&NAV_MAIN_URL='+encodeURIComponent(firsturl));
	   	RemoveNoc(readobj,idstr_all, 0);
         CloseNoc();
	   });
	   
	   $('#CloseBtn').live('click',function(){
	      close_window();      
	   });
	   
	     //点击类型，类型全部详情
	   $('#new_noc_list > div.noc_iterm > .noc_iterm_title > .noc_iterm_read').live('click',function(){
	   	var idstr = idstr_all = firsturl = "";
	   	var separator = ",";
	   	var type_id = $(this).attr('type_id');
	   	var idstr_all = get_noc_idstr(type_id);
	   	RemoveNoc($(this),idstr_all, 0);
	   	var idobj = jQuery("#new_noc_panel > #new_noc_list > .noc_iterm_" + type_id + " > .noc_iterm_data > li");
	   	idobj.each(function(){
	   		url = $(this).attr("url");
	   		sms_id = $(this).attr("sms_id");
	   		if(url!="" && firsturl==""){
	   			firsturl = url;
	   		}
	   		if(url!=""){
	   			idstr += separator + jQuery(this).attr("sms_id");
	   			separator = ",";
	   		}
	   	});
	   	url = '/module/nav/?SMS_ID_STR='+idstr+'&NAV_MAIN_URL='+encodeURIComponent(firsturl);
        	openURL(url);
	   });
	   
	   //点击类型，类型全部已阅
	   $('#new_noc_list > div.noc_iterm > .noc_iterm_title > .noc_iterm_cancel').live('click',function(){
	   	var type_id = $(this).attr('type_id');
	   	var idstr_all = get_noc_idstr(type_id);
	   	RemoveNoc($(this),idstr_all, 0);
	   });
	   
	};
	
	//修复setTimeout bug，使用window.setTimeout调用
	if(!+'\v1') {
	    (function(f){
	        window.setTimeout =f(window.setTimeout);
	        window.setInterval =f(window.setInterval);
	    })(function(f){
	        return function(c,t){
	            var a=[].slice.call(arguments,2);
	            return f(function(){
	                c.apply(this,a)},t)
	            }
	    });
	}
	   
   $(document).ready(function($){
   	initNocbox();
   });   
         
   LoadNoc();
         
   });   
})(jQuery);

// lp 读取通知数据信息
function LoadNoc(flag)
{
   jQuery('#nocbox_tips').html('<div class="loading">'+td_lang.inc.msg_30+'</div>').show();//正在加载，请稍候……
   jQuery('#new_noc_list').hide();
   flag = typeof(flag) == "undefined" ? "1" : "0";
   jQuery.ajax({
      type: 'GET',
      url: '/general/status_bar/get_noc.php',
      data: {'FLAG': flag},
      dataType: "json",
      cache: false,
      success: function(data){
      	jQuery('#nocbox_tips').empty().hide();
	      if(data){
	      	FormatNoc(data);
	      }else{
      		jQuery("#new_noc_panel").css("padding-bottom","0px");
	      	autocloseNoc();
	      }
      },
      error: function(request, textStatus, errorThrown){
         jQuery('#nocbox_tips').html('<div class="error">'+td_lang.inc.msg_34+'(' + request.status + ')：' + textStatus + '</div>').show();//获取事务提醒数据失败
      }
   });
}

//lp 格式化提醒数据
function FormatNoc(data)
{
   var html = totalnum ='';
   jQuery('#new_noc_list').empty();
	jQuery.each(data,function(key, item){ 
		if(item.type_id == "") return false;
		
		//取提醒内容的前2行内容显示
      var content = decodeURIComponent(item.content);
      var pos = content.indexOf('<br />');
      if(pos >= 0)
      {
         var pos2 = content.indexOf('<br />', pos + 6);
         if(pos2 >= 0)
            content = content.substr(0, pos2);
      }
      
		if(jQuery('#new_noc_list').find('.noc_iterm_'+item.type_id).size()!=0){
			html = '<li id="noc_li_'+item.sms_id+'" sms_id="'+item.sms_id+'" url="'+item.url+'" type_id="'+item.type_id+'">';
				html += '<p class="noc_iterm_info"><span class="noc_iterm_time">'+decodeURIComponent(item.send_time)+'</span>'+decodeURIComponent(item.from_name)+'</p>';
				html += '<p class="noc_iterm_content">'+content+'</p>';
			html += '</li>';
			jQuery('.noc_iterm_'+item.type_id+' > ul').append(html);	
		}else{
			html = '<div class="noc_iterm noc_iterm_'+item.type_id+'">';
			html += '<div class="noc_iterm_title"><span class="noc_iterm_read" type_id="'+item.type_id+'" title='+td_lang.inc.msg_35+'></span><span class="noc_iterm_cancel" type_id="'+item.type_id+'" title='+td_lang.inc.msg_36+'></span>'+decodeURIComponent(item.type_name)+'</div>';
			html += '<ul class="noc_iterm_data">';
				html += '<li id="noc_li_'+item.sms_id+'" sms_id="'+item.sms_id+'" url="'+item.url+'" type_id="'+item.type_id+'">';
					html += '<p class="noc_iterm_info"><span class="noc_iterm_time">'+decodeURIComponent(item.send_time)+'</span>'+decodeURIComponent(item.from_name)+'</p>';
					html += '<p class="noc_iterm_content">'+content+'</p>';
				html += '</li>';	 
			html += '</ul>';
			html += '</div>';			
			jQuery('#new_noc_list').append(html);
		}
	});
	
	var num = get_noc_num();
	
	if(num == 1){
		var _obj = jQuery('#new_noc_list > div.noc_iterm > ul.noc_iterm_data > li');
		if(_obj.attr("url")!=""){
			_obj.click();
			autocloseNoc();
		}
	}
	
	jQuery('#new_noc_list').show(); 
	
	var num = get_noc_num();
	jQuery("#new_noc_panel > #new_noc_title > .noc_iterm_num >span").html(num);
	if(num >= 1){
		jQuery('#new_noc_panel').css("padding-bottom","0px");
		jQuery('#new_noc_panel > .button > a').show();
	}
}

//lp 计算目前有多少条提醒
function get_noc_num(){
	var totalnum = '';
	totalnum = jQuery("#new_noc_panel > #new_noc_list > .noc_iterm > .noc_iterm_data > li").length;
	return totalnum;	
}

//lp 即时计算尚未提醒的提醒
function get_noc_idstr(type_id){
	var idstr = '';
	var separator = '';
	if(type_id!="" && typeof(type_id)!=="undefined")
	{
		var idsobj =jQuery("#new_noc_panel > #new_noc_list > .noc_iterm_" + type_id + " > .noc_iterm_data > li");
	}else
	{
		var idsobj = jQuery("#new_noc_panel > #new_noc_list > .noc_iterm > .noc_iterm_data > li");
	}	
	jQuery.each(idsobj,function(){
		idstr += separator + jQuery(this).attr("sms_id");
		separator = ",";
	});
	return idstr;
}	

function RemoveNoc(obj,recvIdStr, del){
	if(!recvIdStr) return;
	jQuery.ajax({
      type: 'POST',
      url: '/general/status_bar/sms_submit.php',
      data: {'SMS_ID':recvIdStr},
      success: function(data){
      		var lis = obj.parents(".noc_iterm").find("ul").find("li").size();
      		if(recvIdStr.indexOf(",")!='-1'){
      			obj.parents(".noc_iterm").remove()
      		}else{
      			lis == 1 ? obj.parents(".noc_iterm").remove() :	obj.remove();
      		}
      		var num = get_noc_num();
      		jQuery("#new_noc_panel > #new_noc_title > .noc_iterm_num >span").html(num);
      		if(num == 0){
      			jQuery("#new_noc_panel > .button").find("#ViewAllNoc,#ViewDetail").hide();
      		}
      		autocloseNoc();
      },
      error: function (request, textStatus, errorThrown){
         alert( td_lang.inc.msg_104+ textStatus);//'操作失败：'
      }
   });
}

//lp 如果没有提醒信息，则自动隐藏盒子
function autocloseNoc(){
	var datanum = get_noc_num();
	jQuery("#new_noc_panel > #new_noc_title > .noc_iterm_num >span").html(datanum);
	if(!(datanum > 0)){
		jQuery('#new_noc_list').hide();
		var msg1 = sprintf(td_lang.inc.msg_113,"<span class='red'>"+nocbox_close_timeout+"</span>");
		jQuery('#nocbox_tips').html("<h2>"+td_lang.inc.msg_40+"</h2><br />"+msg1).show();
		window.clearInterval(closeNocPanel);
		closeNocPanel = window.setInterval(timerclose,1000);	
	}
}

//lp 倒计时
function timerclose(){
	var time = jQuery('#nocbox_tips > span').text();
	if(time > 1){
		jQuery('#nocbox_tips > span').text(time-1);
	}else{
		CloseNoc();	
	}
}

function CloseNoc(){
   jQuery('#new_noc_panel').hide();
   if(closeNocPanel){
   	window.clearInterval(closeNocPanel);
   	closeNocPanel = null;
   }
   close_window();
}     
     
function openURL(url)
{
   var mytop = (screen.availHeight-500)/2-30;
   var myleft = (screen.availWidth-780)/2;
   window.open(url, new Date().getTime(), "height=548,width=780,status=0,toolbar=no,menubar=yes,location=no,scrollbars=yes,top="+mytop+",left="+myleft+",resizable=yes");
}

function close_window()
{
   window.clearTimeout(closeNocPanel);
   if(typeof(window.external.OA_SMS) == 'undefined' || !window.external.OA_SMS("", "", "GET_VERSION") || window.external.OA_SMS("", "", "GET_VERSION") < '20110223')
   {
      window.open('','_self','');
      window.close();
   }
   else
   {
      window.external.OA_SMS("", "", "CLOSE_WINDOW")
   }
}