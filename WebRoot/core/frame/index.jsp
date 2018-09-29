<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
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

%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>

<link rel="stylesheet" type="text/css" href="<%=contextPath %>/core/ext/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath %>/coverExt.css" />
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/index.css"/>

<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/prototype/ext-prototype-adapter.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/ext-all.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/frame/ux/Window.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/ext/frame/ux/CardPanelNoHeader.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/frame/js/indexframe.js"></script>
<script type="text/javascript" src="js/lazyMenu.js"></script>
<script type="text/javascript" src="js/leafMenu.js"></script>
<script type="text/javascript">

var sessionToken = "<%=sessionToken%>";
var checkTimeout = parseNumber("<%=smsRef%>", 30);
var shortMsrgTimer = null;
var tipsWin;
var smsCallCount = parseNumber("<%=smsCallCount%>", 3);
var smsInterval = parseNumber("<%=smsInterval%>", 3);
var smsAlertTimer = null;

function resetSmsCallCount() {
  smsCallCount = parseNumber("<%=smsCallCount%>", 3);
}

function parseNumber(value, defValue) {
  if (isNaN(value)) {
    return defValue;
  }
  return value * 1;
}

queryUserInfo();

MainPanel = function(){
  MainPanel.superclass.constructor.call(this,{
    id:'main-body',
    cls: 'index-frame',
    region:'center',
    border:false,
    minWidth:500,
    minHeight:500,
    html:String.format("<iframe src='{0}' id='main-body-desktop' name='main-body-desktop' frameborder='0' style='width:100%;height:100%;position:absolute;'></iframe><iframe id='main-body-parts' frameborder='0' style='border:none;display:none;width:100%;height:100%;position:absolute;'></iframe>",
      '<%=contextPath %>/core/ext/frame/portal/desktop.jsp')
  });
};

Ext.extend(MainPanel,Ext.Panel,{             
  initEvents:function(){
    MainPanel.superclass.initEvents.call(this);
  }
});

function reloadScMenu(){
  var scUrl = '<%=contextPath %>/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/listShortCut.act';
  try {
	  scMenu.data = getJsonRs(scUrl);
	  scMenu.show();
  } catch (e) {

  }
}

function reloadFavMenu(){
  var fUrl = '<%=contextPath %>/yh/core/funcs/setdescktop/fav/act/YHFavAct/list.act';
  try {
	  fMenu.data = getJsonRs(fUrl);
	  fMenu.show();
	} catch (e) {
	
	}
}

function getIeTitle() {
  var url = '<%=contextPath %>/yh/core/funcs/system/act/YHSystemAct/getIeTitle.act';
  try {
    var json = getJsonRs(url);
    document.title = json.rtData;
  } catch (e) {

  }
}

getIeTitle();
</script>
</head>
<body>

<div style="display:none;">
   <div id="contentTipMan">
       <ul>
           <li><a href="javascript:void(0);" onclick="changeStatus(1);return false;"><img src="<%=imgPath%>/U01.gif"/>联机</a></li>
           <li><a href="javascript:void(0);" onclick="changeStatus(2);return false;"><img src="<%=imgPath%>/U02.gif"/>忙碌</a></li>
           <li><a href="javascript:void(0);" onclick="changeStatus(3);return false;"><img src="<%=imgPath%>/U03.gif"/>离开</a></li>
       </ul>
   </div>
   
   <div id="contentTipWoman">
       <ul>
           <li><a href="javascript:void(0);" onclick="changeStatus(1);return false;"><img src="<%=imgPath%>/U11.gif"/>联机</a></li>
           <li><a href="javascript:void(0);" onclick="changeStatus(2);return false;"><img src="<%=imgPath%>/U12.gif"/>忙碌</a></li>
           <li><a href="javascript:void(0);" onclick="changeStatus(3);return false;"><img src="<%=imgPath%>/U13.gif"/>离开</a></li>
       </ul>
   </div>
   
   <div id="leftBar"></div>
   
   <div id="leftMenu" class="mainframe-left-leftMenu"></div>
   
   <div id="shortcutMenu" style="overflow-y:auto;overflow-x:hidden;height:100%;"></div>
   
   <div id="favMenu" style="overflow-y:auto;overflow-x:hidden;height:100%;"></div>
   
   
   <div id="rightHeaderBar" style="position:adsolute;" class="mainframe-right-explorer">
     
     <div class="mainframe-right-callright">
       <a id="callRight" href="javascript:void(0)" onclick="collapseLeft();return false;"></a>
     </div>
     
     <div class="mainframe-right-logout">
       <a href="javascript:void(0)" onclick="doLogoutMsg()"></a>
     </div>
     
     <div class="mainframe-right-control">
       <a href="javascript:void(0)" onclick="dispParts('<%=contextPath %>/core/funcs/setdescktop/control.jsp');return false;"></a>
     </div>
     
     <div class="mainframe-right-desktop">
       <a href="javascript:void(0)" onclick="dispDesk()"></a>
     </div>
     
     <div class="mainframe-rightbar-up">
       <div style="cursor:pointer;"><img onclick="collapseUp()" id="rightBarUp" src="<%=imgPath%>/mainframe/call_up.jpg"/></div>
     </div>
     
     <div style="position:absolute;left:30px;top:0px;">
       <img id="rightBarUser" style="float:left;margin:7px 0px;"/><div class="mainframe-right-username" style="position:absolute;top:0px;left:20px;" onclick="editUserInfo()" id="username"><input type="text" id="usernameText" readonly onfocus="this.blur()"></div>
     </div>
     
     <div style="position:absolute;left:50px;top:0px;z-index:10;">
       <div class="mainframe-right-editInfo" id="editInfo"><input type="text" id="editInfoText" onkeypress="userInfoKeypress()" onblur="submitUserInfo()"/></div>
     </div>
     
     <div style="position:absolute;left:165px;top:0px;">
       <img id="rightBarStatus" style="float:left;margin:7px 0px;" src="<%=imgPath%>/U01.gif"/><div style="position:absolute;top:0px;left:20px;" class="mainframe-right-userstatus" id="userstatus" style=""><input type="text" id="userStatusText" readonly onfocus="this.blur()"></div>
     </div>
   
     <div class="mainframe-right-timeinfo">
       <div style="float:left;"><img id="" style="" src="<%=imgPath%>/mainframe/clock.jpg"/></div>
       <div style="float:left;margin:8px 0px;">&nbsp;<span id="date"></span><span id="localtime"></span>&nbsp;</div>
       <script type="text/javascript">
       var now = new Date();

       function timeview(){
         $('localtime').innerHTML = new Date().toLocaleTimeString();
         window.setTimeout( "timeview()", 1000 );
       }
       timeview();
       var day = ['日','一','二','三','四','五','六'];
       $('date').innerHTML = String.format('{0}年{1}月{2}日 星期{3}&nbsp&nbsp',now.getFullYear(),now.getMonth() + 1,now.getDate(),day[now.getDay()]);
       
       </script>
       <div style="float:left;"><img id="" style="" src="<%=imgPath%>/mainframe/header_sp.jpg"/></div>
     </div>
     <div style="display:none;" id="flashPlayer"></div>
   </div>
</div>
<div id="smsFlash" style="display:none;cursor:pointer;position:absolute;left:214px;bottom:0px;z-index:9999;width:65px;height:55px;">
  <button onclick="showShortMsrg()" style="cursor:pointer;width:65px; height:55px;background:transparent; border:0px; padding:0px; "> 
    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="inc/swflash.cab" width="65" height="55" style="cursor:pointer;">
      <param name="movie" value="wav/sms.swf" />
      <param name="quality" value="high" />
      <param name="wmode" value="transparent">
      <embed src="images/mail1.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="128" height="58" wmode="transparent"></embed>
     </object>
  </button>
</div>
</body>
<script>
Ext.onReady(function(){
  //生成菜单
  initLeftBar(imgsSrc, userinfo.panel);

  $('leftMenu').update(getLeftMenus());
  if (window.expandId){
    locate('mainframe-menu-lv1', window.expandId);
  }

  var scUrl = '<%=contextPath %>/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/listShortCut.act';

  window.scMenu = new LeafMenu({
    data: getJsonRs(scUrl),
    target: 'shortcutMenu',
    cls: 'mainframe-menu-lv2',
    selCls: 'mainframe-menu-selected',
    setUrl: '<%=contextPath %>/core/funcs/setdescktop/control.jsp?path=shortcut'
  });
  
  scMenu.show();
  
  var fUrl = '<%=contextPath %>/yh/core/funcs/setdescktop/fav/act/YHFavAct/list.act';

  window.fMenu = new LeafMenu({
    data: getJsonRs(fUrl),
    target: 'favMenu',
    cls: 'mainframe-menu-lv2',
    selCls: 'mainframe-menu-selected',
    setUrl: '<%=contextPath %>/core/funcs/setdescktop/control.jsp?path=fav'
  });
  
  fMenu.show();
  
  Ext.QuickTips.init();
  
  var mainPanel = new MainPanel();

  //菜单列表
  var leftMenu = new Ext.Panel({
    bodyBorder: false,
    border: false,
    autoScroll: false,
    contentEl: 'leftMenu'
  });

  
  var shortcutMenu = new Ext.Panel({
    bodyBorder: false,
    border: false,
    autoScroll: false,
    contentEl: 'shortcutMenu'
  });

  
  var favMenu = new Ext.Panel({
    bodyBorder: false,
    border: false,
    autoScroll: false,
    contentEl: 'favMenu'
  });

  
  //左侧导航标签
  var navigation = new Ext.ux.CardPanelNoHeader({
    id: 'navigation',
    bodyBorder: false,
    border: false,
    items:[
      leftMenu,
      shortcutMenu,
      favMenu
    ]
  });
  
  //左侧组织标签
  var organization = new Ext.ux.CardPanelNoHeader({
    width: 199,
    id: 'organization',
    bodyBorder: false,
    border: false,
    items:[{
      margins: '0 0 5 0',
      html:'<iframe id="iframeOnline" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:100%;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>'
    },{
      margins: '0 0 5 0',
      style: 'overflow:auto;',
      html:'<iframe id="iframeAll" border="0" frameborder="0" cellspacing="0" style="overflow:auto;width:100%;height:100%;border:none;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>'
    }]
  });
  
  //左侧短信标签
  var sms = new Ext.ux.CardPanelNoHeader({
    bodyBorder: false,
    id: 'sms',
    border: false,
    items:[{
      margins: '0 0 5 0',
      autoScroll: false,
      html:'<iframe id="iframeRecive" border="0" frameborder="0" cellspacing="0" style="width:100%;height:100%;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>'
    },{
      margins: '0 0 5 0',
      autoScroll: false,
      html:'<iframe id="iframeSend" border="0" frameborder="0" cellspacing="0" style="width:100%;height:100%;background-color:transparent;" allowTransparency="true" src="about:blank"></iframe>'
    }]
  });
  
  //左侧搜索标签
  var search = new Ext.ux.CardPanelNoHeader({
    region: 'center',
    border: false,
    bodyBorder: false,
    html: '<iframe id="iframeSearch" border="0" frameborder="0" cellspacing="0" style="width:100%;height:100%;background-color:transparent;" allowTransparency="true" src="<%=contextPath%>/core/funcs/search/index.jsp"></iframe>'
  });

  //左侧侧边栏
  var westTablePanelWest = {
    region: 'west',
    width: 2,
    html: '<div class="west-tabpanel-west"></div>'
  };
  var westTablePanelEast = {
    region: 'east',
    width: 7,
    html: '<div class="west-tabpanel-east"></div>'
  };
  
  var westTabPanel = new Ext.ux.CardPanelNoHeader({
    region : 'center',
    id: 'westTabPanel',
    bodyBorder: false,
    //bodyCls: 'mainframe-left-bc-bg',
    border: false,
    activeItem: userinfo.panel,
    items: [{
      margins: '0 0 0 0',
      split: true,
      layout: 'border',
      items:[
        {
          region: 'center',
          cls: 'mainframe-leftContent',
          bodyStyle: 'border:none',
          layout: 'fit',
          items: navigation
        },{
          region: 'north',
          cls: 'mainframe-leftExplorer',
          bodyStyle: 'border:none',
          contentEl: 'leftExplorer0'
        },
        westTablePanelWest,
        westTablePanelEast
      ]
    },{
      margins: '0 0 0 0',
      split: true,
      layout: 'border',
      items: [
        {
          region: 'center',
          cls: 'mainframe-leftContent',
          bodyStyle: 'border:none',
          layout: 'fit',
          width: 200,
          items: organization
        }
        ,{
          region: 'north',
          cls: 'mainframe-leftExplorer',
          bodyStyle: 'border:none',
          contentEl: 'leftExplorer1'
        },
        westTablePanelWest,
        westTablePanelEast
      ]
    },{
      margins: '0 0 0 0',
      split: true,
      layout: 'border',
      items: [
        {
          region: 'center',
          cls: 'mainframe-leftContent',
          bodyStyle: 'border:none',
          width: 200,
          layout: 'fit',
          items: sms
        }
        ,{
          region: 'north',
          cls: 'mainframe-leftExplorer',
          bodyStyle: 'border:none',
          contentEl: 'leftExplorer2'
        },
        westTablePanelWest,
        westTablePanelEast
      ]
    },{
      margins: '0 0 0 0',
      split: true,
      layout: 'border',
      items: [
        {
          region: 'center',
          cls: 'mainframe-leftContent',
          width: 200,
          layout: 'fit',
          items: search
        }
        ,
        westTablePanelWest,
        westTablePanelEast
      ]
    }],
    fbar: [{
      iconCls: 'icon-window',
      id: 'windowState',
      hidden: true,
      scale: 'medium',
      style: 'position:relative;left:-80px;top:0px;display:none;',
      handler: function(){
          top.win.show();
          Ext.getCmp('windowState').hide();
          Ext.getCmp('westTabPanel').doLayout();
        }
    },{
      iconCls: 'icon-sms',
      id: 'smsButton',
      hidden: true,
      scale: 'medium',
      style: 'position:relative;left:-50px;top:2px;',
      handler: function(){
        showShortMsrg();
      }
    }]
  }); 

  //上面功能条
  var headerPanel = new Ext.Panel({
    layout: 'form',
    id: 'headerPanel',
    bodyCssClass: 'mainframe-right-explorer',
    border: false,
    region: 'north',
    contentEl: 'rightHeaderBar'
  });
  
  //整个页面的容器viewport
  var viewport = new Ext.Viewport({
    layout: 'border',
    id: 'viewport',
    bodyStyle: 'border:none;',
    cls: 'main-frame',
    border: false,
    bodyBorder: false,
    items: [{
      region: 'north',
      id: 'northContainer',
      border: false,
      height: 64,
      margins: '0 0 0 0',
      autoLoad: {url:'<%=contextPath %>/core/frame/banner.jsp',scope:this,scripts: true}
    }, {
      region: 'west',
      id: 'westContainer',
      animCollapse: false,
      layout: 'border',
      width: 209,
      border:false,
      bodyBorder: false,
      items: [{
        region: 'north',
        style: 'position:absolute;left:0px;top:0px;height:32px;border:none;',
        bodyStyle: 'border:none',
        contentEl: 'leftExplorer'
      },
      westTabPanel
      ]
    }, {
      region: 'south',
      height: 21,
      border: false,
      autoLoad: {url:'<%=contextPath %>/core/frame/statusbar.jsp',scope:this,scripts: true}
    }, {
      region: 'center',
      border: false,
      bodyBorder: false,
      layout: 'border',
      items:[
        headerPanel,
        mainPanel
      ]
    }
    ]
  });

  initUserInfo();
  initLeftMenu(userinfo.panel);
  initOnStatus();

  //store.load({callback:initBtnGroup});

  /**
  var ssoLogin = function(url) {
    var iFrame = document.createElement("iframe");
    iFrame.width = 0;
    iFrame.height = 0;
    iFrame.style.display = "none";
    document.body.appendChild(iFrame);
    iFrame.src = url;
  };
  ssoLogin("http://192.168.0.126/yh/test/core/sso/1.jsp?sessionToken=" + sessionToken);
  **/

  //检查内部短信
  setTimeout(checkShortMsrg, 5000);
  setTimeout(queryLogoutText, 5000);
});
</script>
</html>