<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String url = request.getParameter("url");
  if (YHUtility.isNullorEmpty(url)) {
    url = "about:blank";
  }
%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/extResources/css/jq-yhtheme.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.window.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.tip.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/yh.layouts.autolayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var bodyLayout = $('body').layout({
	  'listeners': {
		},
	  north: {
	    size: 28
	  },
	  center: {
	    
	  }
	});
	var shortcutTop = -28;
	function fillShortcut(){
	  var url = '<%=contextPath %>/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/listShortCut.act';
	  $.ajax({
	    type: "GET",
	    dataType: "text",
	    url: url,
	    success: function(text){
	      var json = YH.parseJson(text);
	      if (!$.isArray(json)) {
	        return;
	      }

	      $('#bannerShortcutTb').empty();
	      
	      if (json.length > 5) {
	        $('#shortcutBtnDown').show();
	        $('#shortcutBtnUp').show();

	        $('#shortcutBtnDown').bind('click', function() {
	          if ( -shortcutTop >= $('#bannerShortcut').outerHeight()) {
	            shortcutTop = 0;
	            $('#bannerShortcut').css({'top': '30px'});
	          }
	          $('#bannerShortcut').animate({
	              'top': shortcutTop
	            }, 'normal',
	            function() {
	              shortcutTop += -28;
	            }
	          );
	        });

	        $('#shortcutBtnUp').bind('click', function() {
	          shortcutTop += 28 * 2;
	          if (shortcutTop > 0) {
	            shortcutTop = -$('#bannerShortcut').outerHeight() + 28;
	            $('#bannerShortcut').css({'top': '-' + $('#bannerShortcut').outerHeight()});
	          }
	          $('#bannerShortcut').animate({
	              'top': shortcutTop
	            }, 'normal',
	            function() {
	              shortcutTop += -28;
	            }
	          );
	        });
	      }

	      var tr = null;
	      
	      $.each(json, function(i, e) {
	        var td = $('<td class="banner_shortcut_bg" cellspacing="0" cellpadding="0" border="0"></td>');
	        var a = $('<a href="javascript:void(0)"></a>');
	        var img = $('<img align="absMiddle"></img>');
	        td.append(img);
	        td.append(a);
	        img.attr("src", contextPath + "/core/styles/imgs/menuIcon/" + e.icon);
	        a.append("&nbsp;" + e.text);
	        a.bind('click', function() {
	          dispParts(e.url);
	        });

	        if (!(i % 5)){
	          tr = $('<tr></tr>');
	          $('#bannerShortcutTb').append(tr);
	        }

	        tr.append(td);
	      });
	    }
	  });
	}

	function reloadShortcut(){
	  fillShortcut();
	}

	fillShortcut();
	dispParts("<%=url%>");
});

function dispParts(src) {
  var href = location.href;
  var a;
  if (/\?/.test(href)) {
    a = href.split('\?');
  }

  if (a && a[1]) {
    src += '?' + a[1];
  }
  window['content'].location.href = src;
}
function setBody(){
  var ajustHeight =  $('#northContainer').outerHeight();
  var iFrame =  $('#content');
  var width = getTotalWidth() + "px";
  var height = (getTotalHeight() - ajustHeight)+ "px";
  iFrame.height(height);
  iFrame.width(width);
  $('#mainContainer').css("width",width);
  $('#northContainer').css("width",width);
}
function getTotalHeight(){ 
  if($.browser.msie){ 
  return document.compatMode == "CSS1Compat"? document.documentElement.clientHeight : document.body.clientHeight; 
  } 
  else { 
  return self.innerHeight; 
  } 
  } 

  function getTotalWidth (){ 
  if($.browser.msie){ 
  return document.compatMode == "CSS1Compat"? document.documentElement.clientWidth : document.body.clientWidth; 
  } 
  else{ 
  return self.innerWidth; 
  } 
  } 

/**
 * 播放flash音频
 * @param name
 * @return
 */
function playSound(name) {
  var flashObjIE = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="../inc/swflash.cab"><param name="movie" value="' + name + '"><param name="quality" value="high"><param name="wmode" value="transparent"></object>';
  var flashObjOther = '<embed src="' + name + '" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash"></embed>';
  
  $('#flashPlayer').empty();
  if($.browser.msie) {
    $('#flashPlayer').append(flashObjIE);
  } else {
    $('#flashPlayer').append(flashObjOther);
  }
}

/**
 * 播放短信提示音


 * @return
 */
function smsCallSound() {
  if (sms.count < 1) {
    return;
  }
  if (sms.count == 1) {
    if (sms.smsAlertTimer) {
      clearTimeout(sms.smsAlertTimer);
    }
  }else if (sms.count > 1) {
    if (sms.smsAlertTimer) {
      clearTimeout(sms.smsAlertTimer);
    }
    sms.smsAlertTimer = setTimeout(smsCallSound, sms.smsInterval * 60 * 1000);
  }
  if (userinfo.callSound >= 0) {
    playSound(contextPath + '/core/frame/wav/' + userinfo.callSound + '.swf');
  }
  else {
    playSound(contextPath + '/theme/sound/' + userinfo.seqId + '.swf');
  }
  sms.count--;
}

</script>
<style>
html, body {
  width: 100%;
  height: 100%;
  font-fimily: 微软雅黑;
  font-size: 13px;
}

a {
}

.banner_shortcut_bg img {
  width: 20px;
  height: 20px;
}

a:link {
  color: white;
  text-decoration: none;
}
a:visited {
  text-decoration: none;
  color: white;
}

a:hover {
  color: white;
  text-decoration: underline;
}

a:active {
  text-decoration: none;
  color: white;
}

#northContainer {
  line-height: 28px;
  overflow: hidden;
  background: url('style/images/shortcut_bg.png') repeat-x left top;
}


.banner-shortcutlist-container {
  float: left;
  background: url('style/images/shortcut_bg.png') repeat-x left top;
}

.banner_scroll_down {
  float: right;
  width: 50px;
  height:100%;
}

.banner_scroll_down img {
  float: right;
  padding-top: 3px;
  display: none;
}

.banner_scroll_down:hover img {
  display: block;
}

#mainLink {
  float: right;
  width: 100px;
  text-align: center;
}

tr, td {
  height: 28px;
  height: 28px;
}

td {
  padding-left: 20px;
}
</style>
</head>
<body onload="setBody();" onresize="setBody()">
  <div class="ui-layout-north" id="northContainer" style="min-width:700px;">
    <div class="banner-shortcutlist-container">
      <table id="bannerShortcut" style="position:relative;top:0px;" cellspacing="0" cellpadding="0" border="0">
        <thead>
        </thead>
        <tbody id="bannerShortcutTb">
        </tbody>
      </table>
    </div>
    <div id="mainLink">
      <a href="<%=contextPath %>/core/frame/2/index.jsp">主界面</a>
    </div>
    <div class="banner_scroll_down">
      <a href="javascript:void(0);" onclick="return false;" style="display:none;" id="shortcutBtnUp">
        <img src="style/images/nav_r2.gif"></img>
      </a>
    </div>
    <div style="clear: both"></div>
  </div>
  <div class="ui-layout-center" id="mainContainer">
    <iframe name="content" id="content" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:99%;border:none;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>
  </div>
  <div id="flashPlayer" style="visibility: hidden;"></div>
</body>
</html>