//屏蔽错误事件
/*
window.onerror = function () {
  return true;
}*/

/**
 * 检查内部短信 */
function checkShortMsrg() {
  if (shortMsrgTimer) {
    clearTimeout(shortMsrgTimer);
  }
  if (!checkTimeout) {
    return;
  }
  //alert("checkShortMsrg");
  var rtJson = null;
  //处理服务器故障或者断网情况
  try {
    rtJson = getJsonRs(contextPath + "/yh/core/funcs/sms/act/YHSmsAct/remindCheck.act");
  }catch (e) {
    //轮训调用下一次检查
    shortMsrgTimer = setTimeout(checkShortMsrg, checkTimeout * 1000);
    return;
  }
  if (rtJson && rtJson.rtState == "0") {
    //没有未读短信，轮训调用下一次检查    shortMsrgTimer = setTimeout(checkShortMsrg, checkTimeout * 1000);

    if (rtJson.rtData == 0) {
    //存在未读短信，打开短信展示窗口，然后返回（注意，关闭窗口后，轮训调用下一次检查）
    } else if (rtJson.rtData > 0) {
      //显示短信提示按钮
      showSmsButton();
      //showShortMsrg();
    } else if (rtJson.rtData < 0){
      //空闲强制自动离线
      doLogout();
    }
  }else {
    //轮训调用下一次检查    shortMsrgTimer = setTimeout(checkShortMsrg, checkTimeout * 1000);
  }
}

/**
 * 展示内部短信窗口
 */
function showShortMsrg() {
  
  var left = 5;
  var top = document.viewport.getDimensions().height - 320 - 5;
  var url = contextPath + "/core/funcs/sms/remind.jsp";
  
  clearTimeout(smsAlertTimer);
  resetSmsCallCount();
  
  if (!window.tipsWin){
    window.tipsWin = new Ext.ux.Window({
      src: url,
      width: 450,
      height: 320,
      layout: 'fit',
      title: '通达YH平台',
      x: left,
      y: top,
      //animateTarget: 'smsButton',
      closeAction: 'close',
      listeners: { 
        'close': function(t){
          hideShortMsrg();
          t.destroy();
          tipsWin = null;
        }
      }
    }).show();
    hideSmsButton();
  }
}
/**
 * 关闭内部短信窗口
 */
function hideShortMsrg() {
  if (window.tipsWin){
    window.tipsWin.destroy();
    tipsWin = null;
  }
  window.smsAlert = false;
  shortMsrgTimer = setTimeout(checkShortMsrg, checkTimeout * 1000);
}


/**
 * 显示桌面
 * @return
 */
function dispDesk(){
  
  //给工作区的iframe内的window添加onunload事件
  //处理在显示工作流/待办工作时,点击桌面按钮时,离开的确认信息不生效的问题
  Event.observe($('main-body-parts').contentWindow, 'unload', function(event) { 
    $('main-body-parts').hide();
    $('main-body-desktop').setStyle({
      'visibility': 'visible'
    });
  });
  
  $('main-body-parts').contentWindow.location.href = "about:blank";
}

/**
 * 注销
 */
function doLogoutMsg() {
  if (!window.alertMsrg){
    window.alertMsrg = "轻轻地您走了，正如您轻轻地来......";
  }
  Ext.MessageBox.show({
    title : '确定注销吗?',
    msg : alertMsrg,
    buttons : {
    yes : '确定',
    no : '取消'
    },
    width : 300,
    fn : function(btn, text) {
      if (btn == 'yes') {
        doLogout();
      }
    }
  });
}

function doLogout() {
  var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/doLogout.act';
  try {
    var rtJson = getJsonRs(url);
    window.location = contextPath + "/login.jsp";
  } catch (e) {
    
  }
}

/**
 * 显示桌面模块
 * @param src
 * @return
 */
function dispParts(src){
  if (src){
    if (/javascript:/.exec(src)){
      //当路径为javascript:则src为可执行的函数      eval(src);
    }
    else{
      //使用?或者&分割URL
      var srcList = src.split(/[?&]/);
      var openFlag = 0;
      var url = '';
      var paras = '';
      
      if (srcList.length > 1){
        srcList.each(function(e, i){
          if (e == 'openFlag=1'){
            openFlag = 1;
          }
          else if (e.startsWith('openHeight=')){
            paras += e.replace('openHeight','height') + ',';
          }
          else if (e.startsWith('openWidth=')){
            paras += e.replace('openWidth','width') + ',';
          }
          else if (url.indexOf('?') > 0){
            url += e + '&';
          }
          else{
            url += e + '?';
          }
        });
      }
      else{
        url = src;
      }
      
      if (openFlag){
        //当openFlag=1时在新窗口中打开链接
        window.open(encodeURI(url),'',paras);
      }
      else{
        //在工作区打开连接
        $('main-body-desktop').setStyle({
          'visibility':'hidden'
        });
        $('main-body-parts').show();
        $('main-body-parts').contentWindow.location = encodeURI(url);
      }
    }
  }
  //$('main-body-parts').contentWindow.location = "/yh/core/funcs/menu/menu.jsp";
}

/**
 * 刷新桌面
 * @return
 */
function reloadDesktop(){
  window['main-body-desktop'].location.reload();
}

/**
 * 显示左下状态栏短信按钮
 * @return
 */
function showSmsButton(){
  if (userinfo.smsOn == '0') {
    if (!window.smsAlert) {
      window.smsAlert = true;
      $('smsFlash').setStyle({'display': 'block'});
      clearTimeout(smsAlertTimer);
      smsCallSound();
    }
  }
  else {
    showShortMsrg();
  }
}

function smsCallSound() {
  if (smsCallCount > 1) {
    clearTimeout(smsAlertTimer);
    smsAlertTimer = setTimeout(smsCallSound, smsInterval * 60 * 1000);
  }
  
  if (userinfo.callSound > 0) {
    playSound('wav/' + userinfo.callSound + '.swf');
  }
  else {
    playSound(contextPath + '/theme/sound/' + userinfo.seqId + '.swf');
  }
  smsCallCount--;
}

/**
 * 隐藏左下状态栏短信按钮
 * @return
 */
function hideSmsButton(){
  $('smsFlash').setStyle({'display': 'none'});
}

//存储用户信息

function queryUserInfo() {
 
  try {
    window.userinfo = {};
    var url = contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/queryInfo.act';
    var rtJson = getJsonRs(url);
  
    if (rtJson.rtState == "0" ) {
      window.userinfo = null;
      window.userinfo = rtJson.rtData;
      
      if (userinfo && isNaN(userinfo.panel)) {
        userinfo.panel = 0;
      } else {
        userinfo.panel = userinfo.panel * 1 - 1;
      }
      
      if (userinfo.panel > 3 || userinfo.panel < 0) {
        userinfo.panel = 0;
      }
    }
  } catch (e) {
    
  }
}

/**
 * 初始化用户信息 * 包括用户姓名/头像/部门/角色/在线时间/禁止显示桌面等 * @return
 */
function initUserInfo(){
  var userName = Ext.getCmp('userName');
  
  $('usernameText').value = userinfo.userName;
  $('rightBarUser').src = contextPath + '/core/styles/imgs/avatar/' + (userinfo.avatar || 1) + '.gif';
  
  var desc = String.format('姓名:&nbsp;{0}<br>部门:&nbsp;{1}<br>角色:&nbsp;{2}<br><br>{3}',
    userinfo.userName || '',
    userinfo.deptName || '',
    userinfo.privName || '',
    userinfo.myStatus || ''
  );
  
  if (window.tooltip) {
    window.tooltip.destroy();
    window.tooltip = null;
  }

  window.tooltip = new Ext.ToolTip({
    target: 'username',
    width: 200,
    html: desc,
    trackMouse:true
  });
  
  
  $('editInfoText').value = userinfo.myStatus || '我的留言';
  
  if (userinfo.notViewTable * 1){
    window['main-body-desktop'].location = contextPath + '/core/ext/frame/portal/noneDesktop.jsp';
  }
}

var status = [{text: '联机',icon:[ imgPath + '/U01.gif',imgPath + '/U11.gif']},
                 {text: '忙碌',icon:[ imgPath + '/U02.gif',imgPath + '/U12.gif']},
                 {text: '离开',icon:[ imgPath + '/U03.gif',imgPath + '/U13.gif']}];

/**
 * 初始化用户状态 * 联机/忙碌/离开
 * @return
 */

function initOnStatus(){
  Ext.Ajax.request({
    url: contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/getOnStatus.act',
    method:'post',
    failure:function(o){},
    success:function(o){
      
      var json = Ext.decode(o.responseText);
      
      window.sex = json.sex;
      
      if (json.sex == '0'){
        new Ext.ToolTip({        
          id: 'content-anchor-tip',
          target: 'userstatus',
          anchor: 'left',
          width: 80,
          autoHide: false,
          closable: true,
          contentEl: 'contentTipMan'
        });
      }
      else{
        new Ext.ToolTip({        
          id: 'content-anchor-tip',
          target: 'userstatus',
          anchor: 'left',
          width: 80,
          autoHide: false,
          closable: true,
          contentEl: 'contentTipWoman'
        });
      }
      
      $('rightBarStatus').src = status[(json.status * 1 - 1) || 0].icon[sex];
      
      $('userStatusText').value = status[(json.status * 1 - 1) || 0].text;
      
    }
  });
}

/**
 * 收起左侧菜单栏 * @return
 */
function collapseLeft(){
  if (window.leftCollapsed){
    $('callRight').setStyle({'visibility':'hidden'});
    Ext.getCmp('westContainer').show();
    Ext.getCmp('viewport').doLayout();
    $('usercount').addClassName('statusbar-usercount');
    window.leftCollapsed = false;
  }
  else{
    $('callRight').setStyle({'visibility':'visible'});
    $('usercount').removeClassName('statusbar-usercount');
    Ext.getCmp('westContainer').hide();
    Ext.getCmp('viewport').doLayout();
    window.leftCollapsed = true;
  }
}

/**
 * 收起页面上部
 * @return
 */
function collapseUp(){
  if (window.upCollapsed){
    $('rightBarUp').src = imgPath + '/mainframe/call_up.jpg';
    Ext.getCmp('northContainer').setHeight(64);
    Ext.getCmp('northContainer').show();
    Ext.getCmp('viewport').doLayout();
    window.upCollapsed = false;
  }
  else{
    $('rightBarUp').src = imgPath + '/mainframe/call_down.jpg';
    Ext.getCmp('northContainer').setHeight(0);
    Ext.getCmp('northContainer').hide();
    Ext.getCmp('viewport').doLayout();
    window.upCollapsed = true;
  }
}

/**
 * 设置用户状态 * 联机/忙碌/离开
 * @param t
 * @param update
 * @return
 */
function changeStatus(index){
  Ext.Ajax.request({
    url: contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/modifyOnStatus.act',
    method:'post',
    params: {onStatus:index},
    failure:function(o){Ext.Msg.alert("提示","获取状态失败");},
    success:function(o){
      $('rightBarStatus').src = status[index - 1].icon[sex];
      $('userStatusText').value = status[index - 1].text;
    }
  });
  Ext.getCmp('content-anchor-tip').hide();
}


/**
 * 显示mystatus输入框
 * @return
 */
function showMyStatus(){
  var myStatus = Ext.getCmp('myStatus');
  myStatus.show();
  Ext.getCmp('userinfo').hide();
  myStatus.focus(true);
}


function editUserInfo(){
  $('editInfo').setStyle({'display':'block'});
  $('editInfoText').focus();
}

function submitUserInfo(){
  
  $('editInfo').setStyle({'display':'none'});
  
  if ($('editInfoText').value != '我的留言'){
  
    Ext.Ajax.request({
      url: contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/modifyMyStatus.act',
      method:'post',
      params: {myStatus:$('editInfoText').value},
      failure:function(o){Ext.Msg.alert("提示","获取状态失败");},
      success:function(o){
        queryUserInfo();
        initUserInfo();
      }
    });
  }
}

function userInfoKeypress(){
  if (event.keyCode==13){
    submitUserInfo();
  }
}


/**
 * 查询注销时的提示信息
 */
function queryLogoutText(){
  try {
    var url = contextPath + '/yh/core/funcs/setdescktop/syspara/act/YHSysparaAct/queryLogoutText.act';
    var rtJson = getJsonRs(url);
  
    if (rtJson && rtJson.rtState == "0" && rtJson.rtData.trim().length > 0) {
      window.alertMsrg = rtJson.rtData;
    }
  } catch (e) {
    
  }
}



/**
 * 点击标签更换一级tab页 */
 
function selectTabs(index){

  var el = $$('#leftExplorer img')[index];
  
  el.src = imgsSrc[index].selectedSrc;
  if (index != lastSelectTab.index){
    lastSelectTab.el.src = imgsSrc[lastSelectTab.index].src;
  }
  
  lastSelectTab = {
    el: el,
    index: index
  };
  
  Ext.getCmp('westTabPanel').setActiveTab(index);
  initLeftMenu(index);
}

/**
 * 更换二级tab页 */
function selectCard(tab, index){
  Ext.getCmp('westTabPanel').items.get(tab).items.get(0).items.get(0).setActiveTab(index);

  var imgs = $$('#leftExplorer' + tab + ' li a img');

  imgs[index].src = imgsSrc[tab].imgs[index].selectedSrc;

  imgs.each(function(e, i){
    if (i != index){
      e.src = imgsSrc[tab].imgs[i].src;
    }
  });

  //异步加载短信和组织,只在第一次点击时加载
  switch(tab){
  case 1: {
      if (index == 0){
        $('iframeOnline').src = contextPath + '/core/frame/onlinetree.jsp';
      }
      else if ($('iframeAll').src == "about:blank" && index == 1){
        $('iframeAll').src = contextPath + '/core/frame/allusertree.jsp';
      }
      break;
    }
  case 2:{
    /*
      if ($('iframeRecive').src == "about:blank" && index == 1){
        $('iframeRecive').src = contextPath + '/core/funcs/sms/jpanel/inboxSms.jsp?pageNo=0&pageSize=5';
      }
      */
      if (index == 0){
        $('iframeRecive').src = contextPath + '/core/funcs/sms/jpanel/inboxSms.jsp?pageNo=0&pageSize=5';
      }
      else if (index == 1){
        $('iframeSend').src = contextPath + '/core/funcs/sms/jpanel/sentboxSms.jsp?pageNo=0&pageSize=5';
      }
      break;
    }
  }
}

//定义tab页标签的数组
var imgsSrc = [{
  src: imgPath + '/mainframe/explorer.jpg',
  selectedSrc: imgPath + '/mainframe/explorer_selected.jpg',
  imgs: [{
    src: imgPath + '/mainframe/all.jpg',
    selectedSrc: imgPath + '/mainframe/all_selected.jpg'
  },{
    src: imgPath + '/mainframe/shortcut.jpg',
    selectedSrc: imgPath + '/mainframe/shortcut_selected.jpg'
  },{
    src: imgPath + '/mainframe/fav.jpg',
    selectedSrc: imgPath + '/mainframe/fav_selected.jpg'
  }]
}, {
  src: imgPath + '/mainframe/org.jpg',
  selectedSrc: imgPath + '/mainframe/org_selected.jpg',
  imgs:[{
    src: imgPath + '/mainframe/online.jpg',
    selectedSrc: imgPath + '/mainframe/online_selected.jpg'
  },{
    src: imgPath + '/mainframe/all.jpg',
    selectedSrc: imgPath + '/mainframe/all_selected.jpg'
  }]
}, {
  src: imgPath + '/mainframe/sms.jpg',
  selectedSrc: imgPath + '/mainframe/sms_selected.jpg',
  imgs:[{
    src: imgPath + '/mainframe/recive.jpg',
    selectedSrc: imgPath + '/mainframe/recive_selected.jpg'
  },{
    src: imgPath + '/mainframe/send.jpg',
    selectedSrc: imgPath + '/mainframe/send_selected.jpg'
  }]
}, {
  src: imgPath + '/mainframe/search.jpg',
  selectedSrc: imgPath + '/mainframe/search_selected.jpg'
}];

/**
 * 通过定义的菜单数组生成菜单 */
function initLeftBar(btns, index){
  index = index || 0;
  var ul = new Element('ul',{
    'class': 'mainframe-left-bg',
    'id': 'leftExplorer'
  });
  
  btns.each(function(e, i){
    var li = new Element('li',{});
    
    var a = new Element('a',{
      'href':'javascript:void(0)'
    });
    
    var img = new Element('img',{
      'src': i == index ? e.selectedSrc : e.src
    });

    if (i == index){
      window.lastSelectTab = {
        el: img,
        index: index
      };
    }
    a.onclick = function(){
      selectTabs(i);
      return false;
    };

    a.insert(img);
    li.insert(a);
    ul.insert(li);

    var ulx = new Element('ul',{
      'class': 'mainframe-left-lv2-bg',
      'id': 'leftExplorer' + i
    });

    if (!e.imgs){
      return;
    }
    
    e.imgs.each(function(el, k){
      if (k){
        var lix = new Element('li',{});
        var imgx = new Element('img',{
          'src': imgPath + '/mainframe/sp.jpg'
        });
        lix.insert(imgx);
        ulx.insert(lix);
      }
      
      var lix = new Element('li',{});
      var ax = new Element('a',{
        'href':'javascript:void(0)'
      });

      ax.onclick = function(){
        selectCard(i, k);
        return false;
      }
      
      var imgx = new Element('img',{
        'src': !k ? el.selectedSrc : el.src
      });

      ax.insert(imgx);
      lix.insert(ax);
      ulx.insert(lix);
    });

    $('leftBar').insert(ulx);
  });

  var li = new Element('li',{});
  
  var aimg = new Element('a',{
    'class': 'mainframe-left-callLeft',
    'href': 'javascript:void(0)'
  });

  aimg.onclick = function(){
    collapseLeft();
    return false;
  };
  
  li.insert(aimg);
  
  ul.insert(li);
  
  $('leftBar').insert(ul);
  
}

function initLeftMenu(index) {
  switch(index){
  case 1: {
      //用户禁止查看用户列表
      if (!userinfo.notViewUserFlag){
        if (userinfo.notViewUser == '1'){
          $$('#leftExplorer1 li').each(function(e, i){
            e.hide();
          });
          $('iframeOnline').src = contextPath + '/core/frame/notViewUser.jsp';
          userinfo.notViewUserFlag = true;
        }
        else{
          $('iframeOnline').src = contextPath + '/core/frame/onlinetree.jsp';
        }
        break;
      }
    }
  case 2:{
      $('iframeRecive').src = contextPath + '/core/funcs/sms/jpanel/inboxSms.jsp?pageNo=0&pageSize=5';
      break;
    }
  }
}

function playSound(name) {
  var flashObjIE = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="inc/swflash.cab"><param name="movie" value="' + name + '"><param name="quality" value="high"><param name="wmode" value="transparent"></object>';
  var flashObjOther = '<object data="' + name + '" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" type="application/x-shockwave-flash" loop="false" quality="best"></object>';

  if(Prototype.Browser.IE) {
    $('flashPlayer').innerHTML = flashObjIE;
  } else {
    $('flashPlayer').innerHTML = flashObjOther;
  }
}

function otherSysLogin(){
  try {
    var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/loginOtherSys.act';
    var rtJson = getJsonRs(url);
    
    if (rtJson.rtState == "0") {
      window.open(ssoUrlGPower + '/userLogin.jsp?token=' + rtJson.rtData, '');
    }
    else{
      alert(rtJson.rtMsrg);
    }
  } catch (e) {
    
  }
}