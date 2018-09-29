<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.global.YHRegistProps" %>
<%@ page import="yh.core.util.auth.YHRegistUtility" %>
<%
String sessionToken = (String)session.getAttribute("sessionToken");

String smsRef = YHSysProps.getString("$SMS_REF_SEC");
if (smsRef == null || "".equals(smsRef.trim())) {
  smsRef = "30";
}

String smsCallCount = YHSysProps.getString("$SMS_REF_MAX");
if (smsCallCount == null || "".equals(smsCallCount.trim())) {
  smsCallCount = "3";
}

String smsInterval = YHSysProps.getString("$SMS_CALLSOUND_INTERVAL");
if (smsInterval == null || "".equals(smsInterval.trim())) {
  smsInterval = "3";
}
response.setHeader("PRagma","No-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0);
String statusRefStr = YHSysProps.getString("$STATUS_REF_SEC");
if (statusRefStr == null || "".equals(statusRefStr.trim())) {
  statusRefStr = "3600";
}
int remainDays = YHRegistUtility.remainDays();
String onlineRefStr = YHSysProps.getString("$ONLINE_REF_SEC");
if (onlineRefStr == null || "".equals(onlineRefStr.trim())) {
  onlineRefStr = "3600";
}

String machineCode = "";
try {
	machineCode = YHRegistUtility.getMchineCode();
}	catch (Exception e) {
  
}

%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="<%=cssPathOA %>/index1.css"/>
<link rel="stylesheet" type="text/css" href="<%=cssPathOA %>/oa.css"/>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/core/frame/webos/styles/style1/cmp/css/cmp-all.css" />
<style>
body, html {
  font-family: "微软雅黑";
}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.droppable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.selectable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
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
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.borderlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/frame/common/sms/sms.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/frame/webos/js/common.js"></script>
<script type="text/javascript" src="js/jquery.plugins.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/frame/common/sms/sms.js"></script>
<script type="text/javascript">
var sessionToken = "<%=sessionToken%>";
var tabsContainer = $('#tabs_container');

function resetSmsCallCount() {
  smsCallCount = parseNumber("<%=smsCallCount%>", 3);
}
function parseNumber(value, defValue) {
  if (isNaN(value)) {
    return defValue;
  }
  return value * 1;
}
var bodyCards = [];

$(document).ready(function() {
  getTitle();
  initSearch();
  queryInfo();
  initTheme();
  window.bodyLayout = $('body').layout({
    'listeners': {
      'resize': function(){
        mainLayout.resizeAll();
      }
    },
    north: {
      'fxSpeen': 'slow',
      size: 'auto'
    },
    south: {
      size: 'auto'
    },
    west: {
      'fxSpeed': 'slow',
      size: 'auto'
    },
    center: {
      
    }
  });
  
  initLeftTbar($('#menuExplorer'), imgsSrc, 0, function(i) {
    card.setActiveItem(i);
  });
  
  window.mainLayout = $('#mainContainer').layout({
    north: {
      size: 'auto'
    },
    center: {
    }
  });
  
  //$('#northContainer').load('banner.jsp');
  //$('#southContainer').load('statusbar.jsp');
  window.leftLayout = $('#leftPanel').layout({
    'listeners': {
      'resize': function(){
        $('#leftPanel .ui-layout-center:first').resize();
      }
    },
    south: {
      size: 'auto'
    },
    west: {
      size: 'auto'
    },
    east: {
      size: 'auto'
    },
    center: {
    }
  });
  
  var loadHandler = [
      function(el, index) {
        var body = el.children().eq(1);
        var height = el.children().eq(0).height();
        body.css({'height': (el.innerHeight() - height) + 'px'});
        if (!el.data('resize')) {
          $(el.parent()).resize(function() {
            body.css({'height': (el.innerHeight() - height) + 'px'});
          });
          el.data('resize', true);
        }
        bodyCards[0] = new YH.layouts.CardLayout({
          el: body,
          lazyLoad: true,
          loadHandler: [
            initMenu,
            initShortcut,
            initFav
          ],
          activeItem: 0
        });
      },
      function(el, index) {
        var body = el.children().eq(1);
        var height = el.children().eq(0).height();
        body.css({'height': (el.innerHeight() - height) + 'px'});
        if (!el.data('resize')) {
          $(window).resize(function() {
            body.css({'height': (el.innerHeight() - height) + 'px'});
          });
          el.data('resize', true);
        }
        bodyCards[1] = new YH.layouts.CardLayout({
          el: body,
          lazyLoad: true,
          reload: true,
          loadHandler: [
            function(element, elindex) {
              if (!window['onlineIframe']) {
                element.append('<iframe name="onlineIframe" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:99%;border:none;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>');
              }
              window['onlineIframe'].location.href = '<%=contextPath%>/core/frame/onlinetree.jsp';
            },
            function(element, elindex) {
              if (!window['allusersIframe']) {
                element.append('<iframe name="allusersIframe" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:99%;border:none;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>');
              }
              window['allusersIframe'].location.href = '<%=contextPath%>/core/frame/allusertree.jsp';
            }
          ],
          activeItem: 0
        });
      },
      function(el, index) {
        var body = el.children().eq(1);
        var height = el.children().eq(0).height();
        body.css({'height': (el.innerHeight() - height) + 'px'});
        if (!el.data('resize')) {
          $(window).resize(function() {
            body.css({'height': (el.innerHeight() - height) + 'px'});
          });
          el.data('resize', true);
        }
        bodyCards[2] = new YH.layouts.CardLayout({
          el: body,
          lazyLoad: true,
          loadHandler: [
            function(element, elindex) {
              if (!window['inboxIframe']) {
                var iframe = '<iframe name="inboxIframe" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:99%;border:none;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>';
                element.append(iframe);
              }
              window['inboxIframe'].location.href = '<%=contextPath%>/core/funcs/sms/jpanel/inboxSms.jsp?pageNo=0&pageSize=5';
            },
            function(element, elindex) {
              if (!window['sentboxIframe']) {
                var iframe = '<iframe name="sentboxIframe" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:99%;border:none;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>';
                element.append(iframe);
              }
              window['sentboxIframe'].location.href = '<%=contextPath%>/core/funcs/sms/jpanel/sentboxSms.jsp?pageNo=0&pageSize=5';
            }
          ],
          activeItem: 0
        });
      },
      function(el, index) {
        var body = el.children().eq(1);
        var height = el.children().eq(0).height();
        body.css({'height': (el.innerHeight() - height) + 'px'});
        if (!el.data('resize')) {
          $(window).resize(function() {
            body.css({'height': (el.innerHeight() - height) + 'px'});
          });
          el.data('resize', true);
        }
        bodyCards[3] = new YH.layouts.CardLayout({
          el: body,
          lazyLoad: true,
          loadHandler: [
            function(element, elindex) {
              if (!window['searchIframe']) {
                element.append('<iframe name="searchIframe" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:99%;border:none;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>');
                window['searchIframe'].location.href = '<%=contextPath%>/core/funcs/search/index.jsp';
              }
            }
          ],
          activeItem: 0
        });
      }
    ];
  
  if (useSearchFunc === "0") {
    loadHandler.pop();
    $('#search').remove();
  }
  window.card = new YH.layouts.CardLayout({
    el: $('#tabpanelLv1'),
    lazyLoad: true,
    loadHandler: loadHandler,
    activeItem: 0
  });
  
  //initUserInfo();
  initUserStatus();
  initTime();
  fillShortcut();
  //window['main-body-desktop'].location.href = "<%=contextPath %>/core/frame/2/desktop.jsp";
  queryLogoutMsrg();
  
  function initSms() {
    setTimeout(function() {
	    if (!userinfo.seqId) {
	      initSms();
	      return;
	    }
      sms.initialize({
        type: "oa2011",
        smsOn: userinfo.smsOn,
        callSound: userinfo.callSound,
        userId: userinfo.seqId,
        smsCallCount: parseNumber("<%=smsCallCount%>", 3),
        smsInterval: parseNumber("<%=smsInterval%>", 3),
        smsRef: parseNumber("<%=smsRef%>", 30)
      });
    }, 100);
  }
  initSms();
  initMenu();
  initTabs();
  addTab("personal", "个人桌面", "portal/", "selected", 1);
  //控制面板点击事件
  $('#person_info').bind('click', function(){
     window.setTimeout(function(){
        addTab('0240', '控制面板', '<%=contextPath %>/core/funcs/setdescktop/control.jsp', 'selected');
     }, 1);
	});
  
  //点击overlay_panel时自动收回门户或者切换主题的面板
  $("#overlay_panel").click(function(){
     if($("#theme_panel:visible").length){
        $("#theme").trigger("click");   
     }
  });
  
  tabsContainer = $('#tabs_container');
});

function initUserCound() {
  var url = "<%=contextPath%>/yh/core/frame/act/YHClassicInterfaceAct/queryUserCount.act";
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
          document.getElementById("userCountInput").innerHTML = json.rtData;
        //$('#userCountInput').val(json.rtData);
      }
    }
  });
}

function initStatus() {
  var url = "<%=contextPath%>/yh/core/frame/act/YHClassicInterfaceAct/queryStatusText.act";
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
        $('#statusContent').html(json.rtData.TEXT || '<%=StaticData.SOFTTITLE%>');
	      var lineHeight = $("#status").height();
	      var top = 0;
	      var height = $('#statusContent').height();
	      
	      function scroll() {
	        if (top < 2 * lineHeight - height) {
            top = lineHeight;
          }
	        $('#statusContent').animate({
	          'top': top -= lineHeight
		      }, 'slow');
	      }
	      
	      var marquee = 5000;
	      
	      if (!isNaN(json.rtData.MARQUEE)) {
	        maruqee = json.rtData.MARQUEE * 1000;
	      }
	      
	      var t = setInterval(scroll, marquee);
      }
    }
  });
}
initStatus();
initUserCound();

 
var statusRef = '<%=statusRefStr%>';
if (isNaN(statusRef)) {
  statusRef = 3600;
}
setInterval(initStatus, statusRef * 1000);

 var onlineRef = '<%=onlineRefStr%>';
if (isNaN(onlineRef)) {
  onlineRef = 120;
}
setInterval(initUserCound, onlineRef * 1000);
</script>
</head>
<body>
  <div class="ui-layout-north" id="northContainer" style="min-width:700px;">
	  <div class="banner_bg">
		  <div id="logo">
		    <img id="logoImg" src="/yh/yh/core/frame/act/YHWebosAct/queryHeaderImg.act" style="display:none"/>
		  </div>
		  <div id="wthAndTime">
		    <div id="time">
		      <div class="left"></div>
		      <div class="right"></div>
		      <div class="clear"></div>
		    </div>
		  </div>
	  </div>
  </div>
  <div class="ui-layout-south" id="southContainer">
  
	  <table style="cellspacing:0; cellpadding:0; border:0; width=:100%;" class="status-content">
		  <tr>
		    <td width="209" align="center" class="statusbar-usercount" id="usercount">
		       <a onclick="selectTab(1);return false;" href="javascript:void(0)">
		             共&nbsp;<span id="userCountInput" style="text-align:center;border:none;background-color:transparent;width:30px;"></span>&nbsp;人在线
		       </a>
		    </td>
		    <td>
			    <div id="status" style="">
					  <div id="statusContent" style="position:relative;top:0px;font-size: 14px;font-weight: bold;color: #393939;"></div>
					</div>
		    </td>
	      <td class="right" width="350">
	        <a title="微讯盒子" href="javascript:;" class="ipanel_tab" id="smsbox" onclick="sms.showShortMsrg()">
	          <div id="smsFlash"></div>
	        </a>
	        <a title="组织" href="javascript:;" class="ipanel_tab" id="org" onclick="showOrg()"></a>
				</td>
		  </tr>
		</table>
  
  </div>
  <div class="ui-layout-center" id="mainContainer">
    <div id="rightHeaderBar" class="ui-layout-north right-funcsbar">
    <div>
      <div id="taskbar_left">
    		<a id="start_menu" href="javascript:void(0);" onclick=""></a>
    	</div>
 	    <div id="taskbar_center" >
	    	<div id="tabs_container" >
	    	</div>
    	</div>
 			<div id="taskbar_right">
	      <a title="控制面板" href="javascript:;" id="person_info"></a>
	      <a title="更换皮肤" href="javascript:;" id="theme"></a>
	      <a title="注销登录" href="javascript:;" id="logout" onclick="doLogoutMsg()"></a>
	      <a title="隐藏顶部" href="javascript:;" id="rightBarUp" onclick="collapseUp()"></a>
	    </div>
	    </div>
	     	<div id="funcbar" style="clear: both;">
			    <div id="funcbar_left"></div>
			    <div id="funcbar_right">
			      <div class="search">
			        <div class="search-body">
			          <div class="search-input">
	             	  <input type="text" value="" id="keyword" class="ui-autocomplete-input" autocomplete="off" role="textbox" aria-autocomplete="list" aria-haspopup="true">
	              </div>
		            <div onclick="document.getElementById('keyword').value = '';" style="" class="search-clear" id="search_clear"></div>
		          </div>
					 </div>
				 </div>
			 </div>
     </div>
		 <div class="ui-layout-center" id="workspace">
		 </div>
		  <div id="center" style="z-index: 100; display: none;">
	    <!-- 主题切换 -->
	    <div id="theme_panel" class="over-mask-layer">
	       <div class="center" id="theme_slider"></div>
	       <div class="close">
	          <a class="btn-black-a" href="javascript:;" onClick="jQuery('#theme').click();" hidefocus="hidefocus">关闭</a>
	       </div>
	       <div class="bottom"></div>
	    </div>
		  <div id="overlay_panel" style="display: none;"></div>
	  </div>
  </div>
  <div id="contentTipMan" class="content-tip">
    <ul>
        <li><a href="javascript:void(0);" onclick="changeStatus(1);return false;"><img src="<%=imgPath%>/U01.gif"/>&nbsp;联机</a></li>
        <li><a href="javascript:void(0);" onclick="changeStatus(2);return false;"><img src="<%=imgPath%>/U02.gif"/>&nbsp;忙碌</a></li>
        <li><a href="javascript:void(0);" onclick="changeStatus(3);return false;"><img src="<%=imgPath%>/U03.gif"/>&nbsp;离开</a></li>
    </ul>
  </div>
  <div id="contentTipWoman" class="content-tip">
    <ul>
        <li><a href="javascript:void(0);" onclick="changeStatus(1);return false;"><img src="<%=imgPath%>/U11.gif"/>&nbsp;联机</a></li>
        <li><a href="javascript:void(0);" onclick="changeStatus(2);return false;"><img src="<%=imgPath%>/U12.gif"/>&nbsp;忙碌</a></li>
        <li><a href="javascript:void(0);" onclick="changeStatus(3);return false;"><img src="<%=imgPath%>/U13.gif"/>&nbsp;离开</a></li>
    </ul>
  </div>
  <div id="start_menu_mask">
	  <div id="start_menu_panel" style="top: 112px;">
	      <div class="panel-head"></div>
	      <div class="panel-user">
	         <div class="avatar">
	            <img id="usernameTextImg" src="">
	            <div class="status_icon" id="userStatusText"></div>
	            <div id="on_status">
	               <a class="on_status_1" href="javascript:changeStatus(1);">在线</a>
	               <a class="on_status_2" href="javascript:changeStatus(2);">忙碌</a>
	               <a class="on_status_3" href="javascript:changeStatus(3);">离开</a>
	            </div>
	         </div>
	         <div id="usernameText" class="name"></div>
	         <div class="tools">
	            <a title="注销" hidefocus="hidefocus" onclick="doLogoutMsg();" href="###" class="logout"></a>
	         </div>
	      </div>
      <div class="panel-menu">
      	<div></div>
      </div>
      <div class="panel-foot"></div>
		</div>
		<iframe id="start_menu_iframe" frameborder="0" style="z-index: 20;margin-top: 120px;width:270px;height:415px;border: 0px;"></iframe>
	</div>
	<ul class="ui-autocomplete ui-menu" style="display: none;">
		<li class="ui-menu-item">
		</li>
	</ul>
</body>
</html>