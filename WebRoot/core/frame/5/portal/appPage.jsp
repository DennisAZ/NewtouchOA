<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>个人桌面</title>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/core/frame/webos/styles/style1/cmp/css/cmp-all.css" />
<link rel="stylesheet" type="text/css" href="<%=cssPathOA %>/appPage.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript">
var APP_ITEM_HEIGHT = 28;
var MIN_PNAEL_HEIGHT = 11 * APP_ITEM_HEIGHT; 
var SCROLL_HEIGHT = 4 * APP_ITEM_HEIGHT;

//菜单滚动箭头事件,id为app_cate_list
function initAppScroll(id){
	//菜单向上滚动箭头事件
	$('#' + id + ' > .scroll-up:first').hover(
		function(){$(this).addClass('scroll-up-hover');},
		function(){$(this).removeClass('scroll-up-hover');}
	);
	//点击向上箭头
	$('#' + id + ' > .scroll-up:first').click(function(){
			var ul = $('#' + id + ' > ul:first');
			ul.animate({'scrollTop':(ul.scrollTop()-SCROLL_HEIGHT)}, 600);
	});
	//向下滚动箭头事件
	$('#' + id + ' > .scroll-down:first').hover(
		function(){$(this).addClass('scroll-down-hover');},
		function(){$(this).removeClass('scroll-down-hover');}
	);
	//点击向下箭头
	$('#' + id + ' > .scroll-down:first').click(function(){
		var ul = $('#' + id + ' > ul:first');
		ul.animate({'scrollTop':(ul.scrollTop()+SCROLL_HEIGHT)}, 600);
	});
} 


function getChild(menuId){
  $.ajax({
    type: "POST",
    dataType: "text",
    url: contextPath + "/yh/core/funcs/system/act/YHSystemAct/lazyLoadMenu.act?parent="+menuId,
    data: {
      iconFolder: "/core/frame/5/styles/style1/css/images/app_icons/"
    },
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == "0") {
        $('.current').removeClass("current");
        $('#m'+menuId).addClass("current");
        var data = json.rtData;
        $('#menuChild').empty();
        
        $.each(data, function(i, e) {
          if (e.children) {
	          $.each(e.children, function(j, m) {
	            if(!parent.$("#block_"+m.id).length > 0){
		            var a = $('<a title="'+m.text+'" href="javascript:void(0);" id="'+m.id+'"></a>').append('<img width="48" height="48"  src="../styles/style1/css/images/app_icons/'+m.icon+'"><span class="lleft"><span class="lright">'+m.text+'</span></span>');
	              $('#menuChild').append($("<li></li>").append(a));
	              a.click(function() {
	                parent.addModule(parent.slidebox.getCursor(), m);
	                $(a).parent().remove();
	                parent.serializeSlide()
	              });
	            }
	          })
          }
          else {
            if(!parent.$("#block_"+e.id).length > 0){
	            var a = $('<a title="'+e.text+'" href="javascript:void(0);" id="'+e.id+'"></a>').append('<img width="48" height="48"  src="../styles/style1/css/images/app_icons/'+e.icon+'"><span class="lleft"><span class="lright">'+e.text+'</span></span>');
	            $('#menuChild').append($("<li></li>").append(a));
	            a.click(function() {
	              parent.addModule(parent.slidebox.getCursor(), e);
	              $(a).parent().remove();
	              parent.serializeSlide();
	            });
	          }
          }
        });
      }
    }
  });
}

$(function(){
  
  $.ajax({
    type: "POST",
    dataType: "text",
    url: contextPath + "/yh/core/funcs/system/act/YHSystemAct/listMenu.act",
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == "0") {
        var data = json.rtData;
        var menu = data.menu;
        for(var i=0; i<menu.length; i++){
			  	$('#menuParent').append('<li><a title="'+menu[i].text+'"  href="javascript:getChild(\''+menu[i].id+'\');" id="m'+menu[i].id+'" ' +(i == 0 ? 'class="current"' : '' )+'><img width="20" height="20" align="absMiddle" src="'+contextPath+'/core/styles/imgs/menuIcon/'+menu[i].icon+'"> '+menu[i].text+'</a></li>')
        }
    	  var su = $("#app_cate_list .scroll-up:first");
    	  var sd = $("#app_cate_list .scroll-down:first");
    	  var scrollHeight = $("#app_cate_list ul").attr('scrollHeight');
    	  var orgheight = $("#app_cate_list ul").height();
    	  if(orgheight < scrollHeight){
    		  var height = scrollHeight > MIN_PNAEL_HEIGHT ? MIN_PNAEL_HEIGHT : scrollHeight;
    		  $("#app_cate_list ul").height(height);
    	  }
    	  if(orgheight >= scrollHeight){
    		  su.hide();
    		  sd.hide();
    	  }
    	 	initAppScroll('app_cate_list');
    	 	
    	 	getChild(menu[0].id);
      }
    }
  });
});
</script>
<style>
</style>
</head>
<body>
<div class="appPage" id="appPageDom" style="overflow: hidden; display: block;">
	<div class="ui-layout-west" id="app_cate_list">
		<div class="scroll-up"></div>
		<ul id="menuParent"></ul>
		<div class="scroll-down"></div>
	</div>
	<div class="ui-layout-center" id="app_list_box">
		<div id="app_list_record"></div>
		<ul id="menuChild"></ul>
		<div class="clearfix"></div>
	</div>
</div>
</body>
</html>
