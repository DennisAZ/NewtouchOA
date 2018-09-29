<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>通达T9智能管理平台官方网站</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="Keywords" content="ERP 进销存 财务管理软件 网络报销 通达财务 通达OA 协同 工作流 下载"/>
<link rel="stylesheet" type="text/css" href="../css/style.css"/>
<script type="text/javascript" src="../js/jquery-1.4.2.js"></script>
<script type="text/javascript" src="../js/jquery.ui.core.js"></script>
<script type="text/javascript" src="../js/ux/jquery.ux.t9.js"></script>
<script type="text/javascript" src="../js/ux/t9.layouts.autolayout.js"></script>
<script type="text/javascript" src="../js/ux/t9.layouts.floatlayout.js"></script>
<script type="text/javascript" src="../js/ux/t9.layouts.columnlayout.js"></script>
<script type="text/javascript" src="../js/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="../js/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="../js/ux/jquery.ux.menu.js"></script>
<script type="text/javascript" src="../js/ux/jquery.ux.button.js"></script>
<style>
iframe {
  height:100%;
  width:100%;
  border:none;
  background-color:transparent;
}
</style>
<script type="text/javascript">
function load(url) {
  window.location.href = url;
}
var activeTabIndex = <?=$index?>;
var activeTab;
$(document).ready(function() {
  $.get("../menu.txt", function(text) {
    var customMenu = T9.parseJson(text);
    function clickEvt(e, t) {
    t.setStatus('active');
    if (activeTab != t) {
      activeTab.setStatus('default');
      activeTab = t;
    load(t.href);
    }
  }
  
  var items = [{
    xtype: 'button',
    btnText: '首页',
    normalCls: 'nav_btn',
    activeCls: 'nav_btn_active',
    href: '../home/',
    handler: clickEvt
  }];
    
  $.each(customMenu, function(i, e) {
    e.xtype = "button";
    e.normalCls = 'nav_btn';
    e.activeCls = 'nav_btn_active';
    e.btnText = e.text;
    e.handler = e.handler || clickEvt;
    items.push(e);
  });
  
  var btnGroup = new T9.Container({
    renderTo: '.navigation',
    layout: 'floatlayout',
    items: items
  });
  activeTab = btnGroup.items[activeTabIndex] || btnGroup.items[0];
  activeTab.btn.click();
  });
});
</script>
<style>
iframe {
  height:100%;
  width:100%;
  border:none;
  background-color:transparent;
}



.jq-panel-mc {
  line-height: 24px;
  color: #3a3a3a;
  padding-bottom: 15px;
}

.jq-panel-mc h1,
.jq-panel-mc h2,
.jq-panel-mc h3,
.jq-panel-mc h4,
.jq-panel-mc h5,
.jq-panel-mc h6 {
  color: #0a1a51;
  line-height: 28px;
}

.jq-panel-header-text {
  font-size: 16px;
}

.jq-panel-header img {
  height: 14px;
  width: 14px;
}
.home .jq-panel-header img {
  position: relative;
  top: -24px;
  left: -5px;
  height: 50px;
  width: 103px;
}

.contact-icon {
  position: relative;
  top: -18px;
  width: 122px;
  height: 50px;
  background: url(../images/contact_icon.gif) no-repeat left top;
}

#toBuy .extend-container {
  width: 360px;
}

#toBuy .row {
  padding-top: 10px;
  height: 90px;
}

#toBuy .title {
  display: block;
  font-weight: bold;
  height: 35px;
  padding-top: 40px;
  width: 80px;
}

#toBuy .jq-panel-tc {
  height: 8px;
}

#toBuy .jq-panel-header {
  display: none;
}

#toBuy .extend-container img {
  margin-left: 100px;
}

.evaluation-btn {
  position: absolute;
  bottom: 10px;
  left: 880px;
  width: 200px;
  height: 30px;
  font-size: 16px;
  font-weight: bold;
}

/*通知*/
#notify{ width:890px; margin:0 auto; margin-top:10px; margin-bottom:10px; text-align:center; padding:10px; color:#cc3333; font-size:12pt; border:1px #cccc66 solid; background:#ffff99; font-family:微软雅黑;}

</style>
</head>
<body>
<div class="viewport-l">
<div class="viewport-r">
<div class="viewport">
  <div class="header">
    <a href="http://t9.go2oa.com:86/t9/login.jsp" target="_blank" class="evaluation"></a>
    <div id="visitorCount"><?=$COUNTER?></div>
    <div class="navigation">
    </div>
  </div>
  <div class="content">