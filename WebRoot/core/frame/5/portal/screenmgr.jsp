<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>个人桌面</title>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/core/frame/webos/styles/style1/cmp/css/cmp-all.css" />
<link rel="stylesheet" type="text/css" href="<%=cssPathOA %>/personal.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPathOA %>/appbox.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript">
$(function() {
  
  var screenCount = parent.slidebox.getCount();
  for (var i = 0; i < screenCount; i++) {
    addScreen(i);
  }
  $("#screenPageDom ul").append('<li title="添加屏幕" class="no-draggable-holder" id="btnAddScreen" style=""></li>');
  
  //鼠标滑过屏幕样式
  $("#screenPageDom ul li.minscreenceil'").live('mouseenter', function() {
     $(this).css({"font-size":"60px"});
     if($('span.closebtn', this).length <= 0)
        $(this).append("<span class='closebtn' title='移除此屏'></span>");//'移除此屏'
     $('span.closebtn', this).show();
  });
  
  $("#screenPageDom ul li.minscreenceil").live('mouseleave', function() {
     $(this).css({"font-size":""});
     $('span.closebtn', this).hide();
  });
  
  //删除屏幕
  $("#screenPageDom ul li.minscreenceil span").live("click",function(){
     if(confirm("删除桌面，将删除桌面全部应用模块，确定要删除吗？")) {
        var currentDom = $(this).parent("li");
        parent.slidebox.removeScreen(currentDom.index("li.minscreenceil"));
        var flag = parent.serializeSlide();
        if(flag)
        {
           parent.portalMessage("桌面删除成功！");
           currentDom.remove();
           reSortMinScreen();
        }
     }   
  });
  
  $("#screenPageDom ul").sortable({
    cursor: 'move', 
    tolerance: 'pointer',
    cancel: '#btnAddScreen',
    stop: function(){
       var arrScreen = new Array();
       $(this).find("li").each(function(){
          arrScreen.push($(this).attr("index"));
       });
       parent.slidebox.sortScreen(arrScreen);
       $(this).find("li").each(function(i){
          $(this).attr("index",i);
       });
       var flag = parent.serializeSlide();
       if(flag)   
         parent.portalMessage("桌面顺序已设置成功！");
    }
  });

	//添加屏幕
	$("#btnAddScreen").live("click",function(){
	  parent.addScreen();
		
		var screenlist = $("#screenPageDom ul");
		var _max = 0;
		screenlist.find("li.minscreenceil").each(function(){
		   _max = _max > parseInt($(this).attr("index")) ? _max : parseInt($(this).attr("index"));      
		});
		screenlist.find("#btnAddScreen").remove();
		screenlist.append("<li class='minscreenceil' index='"+ (_max+1) +"'>"+(_max+2)+"</li><li id='btnAddScreen' class='no-draggable-holder' title='添加屏幕'></li>");
		var flag = parent.serializeSlide();
		if(flag) parent.portalMessage("屏幕添加成功！");
	});
});

var screens = [];

function addScreen(index) {
  $("#screenPageDom ul").append('<li index="' + index
      + '" class="minscreenceil" style="">' + (index + 1)
      + '<span title="移除此屏" class="closebtn" style="display: none;"></span></li>');
}


function reSortMinScreen(){
  $("#screenPageDom ul li.minscreenceil").each(function(i){
     $(this).text(i+1);
     $(this).attr("index",i);      
  });      
}
</script>
<style>
</style>
</head>
<body>
  <div id="screenPageDom">
    <div id="portalSettingMsg"></div>
    <ul>
      
    </ul>
    <div style="clear: both"></div>
  </div>
</body>
</html>
