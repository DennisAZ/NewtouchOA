<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>个人桌面</title>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/core/frame/webos/styles/style1/cmp/css/cmp-all.css" />
<link rel="stylesheet" type="text/css" href="<%=cssPathOA %>/personal.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.slidebox.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.autolayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.fitlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.freelayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.floatlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.gridlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.module.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.window.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.tip.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.button.js"></script>
<script type="text/javascript">
var modules;
var slidebox;

$(function() {
  $.ajax({
    type: "POST",
    dataType: "text",
    url: contextPath + "/yh/core/frame/act/YHTdoaAct/showParam.act",
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == "0") {
        modules = json.rtData;
        slidebox = $("#container").slideBox({
          control: ".control-c",
          count: modules.length,
          cancel: ".block",
          listeners: {
            afterScroll: function(i) {
            },
            beforeScroll: function(i) {
               jQuery(".background").animate({
                  left: - i * 70   
               }, "normal");
            }
          }
        });
        initScreens();
      }
    }
  });
  initTrash();
  
  if (YH.isTouchDevice) {
	  $("body").width((top.document.documentElement || top.document.body).clientWidth);
	  $(window).resize(function() {
	    $("body").width((top.document.documentElement || top.document.body).clientWidth);
	  });
  }
});

function initScreens() {
  $("#container .screen").append("<ul class='blocks'></ul><div style='clear: both;'></div>");
  $.each(modules, function(i, e) {
    $.each(e, function(j, m) {
      addModule(i, m);
    })
    var s = slidebox.getScreen(i); 
    var ul = s.find("ul"); 
    var click;
		ul.sortable({
		  revert: true,
		  //delay: 200,
		  //distance: 10,               //延迟拖拽事件(鼠标移动十像素),便于操作性
		  tolerance: 'pointer',       //通过鼠标的位置计算拖动的位置*重要属性*
		  connectWith: ".screen ul",
		  scroll: false,
		  stop: function(e, ui) {
		    setTimeout(function() {
          $(".block.remove").remove();
          $("#trash").hide();
          //ui.item[0].onclick = click;
          serializeSlide();
					ui.item.removeAttr("clickdisabled");
		    }, 0);
		  },
		  start: function(e, ui) {
				$("#trash").show();
				ui.item.attr("clickdisabled", true);
		  }
	  });
  });
}

function addScreen() {
  slidebox.addScreen();
  slidebox.scroll(slidebox.getCount() - 1);
  var newScreen = slidebox.getScreen(slidebox.getCount() - 1);
  var ul = $("<ul class='blocks'></ul>");
  newScreen.append(ul);
  ul.sortable({
    revert: true,
    //delay: 200,
    //distance: 10,               //延迟拖拽事件(鼠标移动十像素),便于操作性
    tolerance: 'pointer',       //通过鼠标的位置计算拖动的位置*重要属性*
    connectWith: ".screen ul",
    scroll: false,
    stop: function(e, ui) {
      setTimeout(function() {
            $(".block.remove").remove();
            $("#trash").hide();
            //ui.item.click(openUrl);
            serializeSlide();
      }, 0);
    },
    start: function(e, ui) {
       $("#trash").show();
       //ui.item.unbind("click");
    }
  });
}

function addModule(screen, module) {
  var el = slidebox.getScreen(screen).find("ul.blocks");
  var _id = module.id;
  var li = $("<li class=\"block\"></li>");
  var img = $("<div class='img'><p><img src='" + contextPath + "/core/frame/5/styles/style1/css/images/app_icons/" + module.icon + "' /></p></div>");
  var divT = $("<div class=\"count\"></div>");
  li.attr("id", "block_" + module.id);
  li.attr("title", module.text);
  li.attr("index", module.id);
  divT.attr("id", "count_" + module.id);
  if(module.count > 0){
     divT.addClass("count" + module.count);   
  }
  var a = $("<a class=\"icon-text\" href=\"javascript: void(0)\"></a>"); 
  var span = $("<span></span>").text(module.text); 
  li.append(img.append(divT)).append(a.append(span)); 
  el.append(li);
  if((module.url).startWith("/yh")){
	  var url = module.url;
  }
  else{
	  var url = new RegExp("^\/").test(module.url) ? contextPath + module.url : module.url;
  }
  li.click(function() {
    if (li.attr("clickdisabled")) {
      return;
    }
    top.openUrl({
      text: module.text,
      url: url,
      id: module.id
    });
  });
}

String.prototype.startWith = function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
		return true;
	else
	  return false;
	return true;
}

function openAppBox() {
  var appbox = YH.getCmp("appbox");
  if (!appbox) {
	  appbox = new YH.Window({
	    title: "应用盒子",
	    modal: true,
	    id: "appbox",
	    layout: 'cardlayout',
	    layoutCfg: {
	      tabs: true
	    },
	    draggable: false,
//	    {
//	      containment: "body"
//	    },
	    width: 800,
	    height: 330,
	    items: [{
	      title: '应用设置',
	      tabBtn: {
	        btnText: '应用设置',
	        xtype: 'button',
	        normalCls: 'app-set',
	        activeCls: 'app-set'
	      },
	      iframeSrc: "appPage.jsp"
	     },{
	       title: '分屏设置',
	       tabBtn: {
	         btnText: '分屏设置',
	         xtype: 'button',
	         normalCls: 'screen-set',
	         activeCls: 'screen-set'
	       },
	       iframeSrc: "screenmgr.jsp"
	     }]
	  });
	  appbox.el.find(".tab-header").append("<span id='portalSettingMsg'></span>");
  };
  appbox.show();
}


function portalMessage(msg){
  if(!msg) return;
  msgObj = $("#portalSettingMsg");
  msgObj.html(msg).show();
  setTimeout(function(){msgObj.empty().hide()},5000);
}

function serializeSlide() {
  var s = "";
  jQuery("#container .screen").each(function(i, e) {
     jQuery(this).find("li.block").each(function(j, el) {
        if(!$(el).attr("index")) return true;
        s += $(el).attr("index");
        s += ",";
     });
     s += "-|";
  });
  if (s.length) {
     s = s.replace(/\|$/, "");   
  }
  var flag = false;
  $.ajax({
     async: false,
     data: {"oaItem": s},
     type: "POST",
     url: contextPath + '/yh/core/frame/act/YHTdoaAct/updateUserParam.act',
     success: function(r) {
       var json = YH.parseJson(r);
        if (json.rtState == "0") {
           flag = true;
        }
     }
  });
  return flag;
}

function initTrash() {
  $("#trash").droppable({
     over: function() {
        $("#trash").addClass("hover");
     },
     out: function() {
        $("#trash").removeClass("hover");
     },
     drop: function(event, ui) {
        ui.draggable.addClass("remove").hide();
        delModule(ui.draggable.attr("index"));
        $(".ui-sortable-placeholder").animate({
           width: "0"
        }, "normal", function() {
        });
        $("#trash").removeClass("hover");
     }
  });   
}

function delModule(el){
  var pObj = jQuery("#container .screen ul li.block");
  pObj.each(function(){
     var index = jQuery(this).attr("index");
     if(el == index){
        jQuery(this).remove();
        var flag = serializeSlide();
     }
  });
}


function getCount(){
  $.ajax({
    type: "POST",
    dataType: "text",
    url: contextPath + "/yh/core/frame/act/YHTdoaAct/showParam.act",
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == "0") {
        modules = json.rtData;
        $.each(modules, function(screen, e) {
          $.each(e, function(j, module) {
            var divT = $("#count_" + module.id);
            divT.removeClass(divT.attr("class"));
            if(module.count > 0){
              divT.addClass("count count" + module.count);
            }
          })
        });
      }
    }
  });
}

setInterval('getCount()',1000*10); //指定1秒刷新一次
</script>
<style>
table {
  border-collapse: collapse;
  padding: 0;
  margin: 0;
}
html, body {
  height: 100%;
}

body {
  position: relative;
  text-align: center;
  margin: 0;
  padding: 0;
}

.slidebox-container {
  margin: 0;
  padding: 0;
  overflow: hidden;
}

#control {
  height: 26px;
  left: 0;
  position: absolute;
  top: 10px;
  z-index: 3;
  width: 100%;
  text-align: center;
}
</style>
</head>
<body>
  <div id="trash"></div>
  
  <div id="backgroundCon1">
  <div id="backgroundCon2">
  <div id="container"></div>
  </div>
  </div>
  
  <div id="control"> 
    <table align="center" cellpadding="0" cellspacing="0">
       <tr>
          <td class="control-l"></td>
          <td class="control-c"></td>
          <td class="control-r">
            <a id="openAppBox" title="打开应用盒子" onclick="openAppBox()" href="javascript: void(0)" class="cfg"></a>
          </td>
       </tr>
    </table>
	</div>
	<div class="background" style="left: 0px;"></div>
</body>
</html>
