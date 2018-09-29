
function lockScreen() {
  var unlock = YH.getCmp('unlock');
  if (!unlock) {
    unlock = new YH.Window({
      id: 'unlock',
      modal: true,
      width: 300,
      height: 150,
      tbar: [],
      maskOpacity: 1,
      modalStyle: {'background-image': $('body').css('background-image')},
      title: '已锁定',
      html: '<br><br>请输入解锁密码:&nbsp;<input type="password" id="unlockPw"><button style="margin: 10px auto;display: block;" onclick="unlockScreen()">解锁</button>',
      modalStyle: {'background-image': $('body').css('background-image')}
    });
  }
  
  var lock = YH.getCmp('lock');
  if (!lock) {
    lock = new YH.Window({
      id: 'lock',
      modal: true,
      width: 300,
      height: 150,
      title: '锁定',
      html: [
        '<br><br>请输入锁定密码:&nbsp;<input type="password" id="lockPw">',
        '<br>请重复锁定密码:&nbsp;<input style="margin-top: 3px;" type="password" id="relockPw">',
        '<button style="margin: 10px auto;display: block;" id="lockBtn">锁定</button>'].join(''),
      modalStyle: {'background-image': $('body').css('background-image')}
    });
    lock.active();
    $('#lockBtn').click(function() {
      if (!$('#lockPw').val()) {
        alert('请输入密码');
      }
      else if ($('#lockPw').val() != $('#relockPw').val()) {
        alert('两次密码相同,请重新输入');
        $('#relockPw')[0].focus()
      }
      else {
        unlockScreen.password = $('#lockPw').val();
        $('#desktop').hide();
        lock.hide();
        unlock.active();
        //增加防止刷新
      }
      $('#lockPw').val('');
      $('#relockPw').val('');
    });
  }
  else {
    lock.active();
  }
}

function unlockScreen() {
  if ($('#unlockPw').val() != unlockScreen.password) {
    alert('解锁密码不正确');
  }
  else {
    var cmp = YH.getCmp('unlock');
    if (YH.isCmp(cmp)) {
      cmp.hide();
      $('#desktop').show();
    }
  }
  $('#unlockPw').val('');
}

/**
 * 获取工作区的window对象
 */
function getWorkspaceWindow() {
  return window['main-body-parts'];
}

/**
 * 刷新桌面
 * @return
 */
function reloadDesktop(){
  location.reload();
}

/**
 * 显示桌面
 * @return
 */
function dispDesk(){
}

/**
 * 弹出询问窗口的注销
 */
function doLogoutMsg() {
  if (!window.alertMsrg){
    window.alertMsrg = "轻轻地您走了，正如您轻轻地来......";
  }
  if (confirm(alertMsrg)) {
    doLogout();
  }
}

/**
 * 无询问窗口直接注销
 * @return
 */
function doLogout() {
  var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/doLogout.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
    }
  });
  //window.location.href = contextPath + "/login.jsp";
  //处理回退按钮
  window.location.replace(contextPath + "/login.jsp");
}


function doLongStr(s, l) {
  if (!l || l < 1) {
    l = 6;
  }
  s = s || "";
  if (s.length > l) {
    s = s.substring(0, l - 1) + "...";
  }
  return s;
}

/**
 * 初始化用户信息 * 包括用户姓名/头像/部门/角色/在线时间/禁止显示桌面等 * @return
 */
function initUserInfo(data) {
  var minutes = Math.floor((data.onLine || 0) / 60);
  var hours = Math.floor(minutes / 60);
  minutes %= 60;
  var desc = '姓名:&nbsp;' + doLongStr(data.userName)
  + '<br>部门:&nbsp;' + doLongStr(data.deptName)
  + '<br>角色:&nbsp;' + doLongStr(data.privName);
  
  var src;
  if (/\.[a-z,A-Z]{1,}/.test(data.avatar)) {
    src = contextPath + '/attachment/avatar/' + data.avatar;
  }
  else {
    src = contextPath + '/core/styles/imgs/avatar/' + (data.avatar || 1) + '.gif';
  }
  $('#avatar').attr('src', src);
  //初始化头像
  $('#avatar').attr('title', '在线时长: ' + hours + "小时" + minutes + "分钟");
  
  $('#userStatus > input').val(data.myStatus || '');
  $('#userStatus > button').attr('title', data.myStatus || '')
  .html(doLongStr(data.myStatus, 8))
  .click(function() {
    $(this).hide();
    $('#userStatus > input').show();
    $('#userStatus > input')[0].focus();
  });
  $('#userInfo').html(desc);


  /**
   * 输入回车或者失去焦点时提交信息
   */
  $('#userStatus > input').unbind('keypress');
  $('#userStatus > input').bind('keypress', function(event) {
    if (event.keyCode == 13){
      //设置回车事件后的失去焦点事件无效
      $(this).unbind('blur');
      changeUserStatus();
    }
  });
  $('#userStatus > input').unbind('blur');
  $('#userStatus > input').bind('blur', changeUserStatus);
  var contextEl;
  if (data.sex && data.sex == '1') {
    contentEl = $('#contentTipWoman');
  } 
  else {
    contentEl = $('#contentTipMan');
  }
  
  var imgPath = contextPath + '/core/frame/webos/styles/style1/images';
  window.statusTip = new YH.Tip({
    'type': 'contextEl',
    style: {
      width: '122px'
    },
    'target': '#onlineStatus',
    items: [{
      xtype: 'button',
      icon: imgPath + '/status_1.png',
      normalCls: 'status-tip-btn',
      btnText: '在线',
      handler: function(e, t) {
        changeStatus(1);
      }
    }, {
      xtype: 'button',
      icon: imgPath + '/status_2.png',
      normalCls: 'status-tip-btn',
      btnText: '忙碌',
      handler: function(e, t) {
        changeStatus(2);
      }
    }, {
      xtype: 'button',
      icon: imgPath + '/status_3.png',
      normalCls: 'status-tip-btn',
      btnText: '离开',
      handler: function(e, t) {
        changeStatus(3);
      }
    }],
    'event': 'leftClick',
    'delay': 1,
    relative: {
      y: 20,
      x: 1
    }
  })
  changeStatus();;
}

function editUserInfo(){
  $('#editInfo').show();
  document.getElementById('editInfoText').focus();
}

/**
 * 修改mystatus
 * @return
 */
function changeUserStatus() {
  var userinfo = creator.units.userInfo.data;
  $('#userStatus > input').hide();
  $('#userStatus > button').show();
  var status = $('#userStatus > input').val();
  if (status != '我的留言'){
    var url = contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/modifyMyStatus.act';
    $.ajax({
      type: "GET",
      dataType: "text",
      url: url,
      data: {
        'myStatus': status
      },
      success: function(text){
        userinfo.myStatus = status;
        $('#userStatus > button').html(doLongStr(status, 8));
      }
    });
  }
}

/**
 * 设置用户状态 * 联机/忙碌/离开
 * @param index           不传递时为初始化状态 * @return
 */
function changeStatus(index){
  var userinfo = creator.units.userInfo.data;
  var url = contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/modifyOnStatus.act';
  if (index) {
    $.ajax({
      type: "POST",
      dataType: "text",
      data: {onStatus: index},
      url: url,
      success: function(text){
        userinfo.onStatus = index;
        var text = statusTip.items[(userinfo.onStatus || 1) - 1].btnText;
        var icon = statusTip.items[(userinfo.onStatus || 1) - 1].icon;
        $('#onlineStatus').html('<img align="absmiddle" src="' + icon + '"></img>&nbsp;' + text);
        statusTip && statusTip.hide();
      }
    });
  }
  else {
    var text = statusTip.items[(userinfo.onStatus || 1) - 1].btnText;
    var icon = statusTip.items[(userinfo.onStatus || 1) - 1].icon;
    $('#onlineStatus').html('<img align="absmiddle" src="' + icon + '"></img>&nbsp;' + text);
    
    var userinfo = creator.units.userInfo.data;
    var minutes = Math.floor((userinfo.onLine || 0) / 60);
    var hours = Math.floor(minutes / 60);
    minutes %= 60;
    $('#onlineTime').html("在线时长:" + hours + "小时");
  }
}

/**
 * 显示桌面模块
 * @param src   以javascript:开头则直接运行javascript:后边的内容
 *              带有openFlag=1的url在新窗口中打开(通过参数openWidth=?/openHeight=?设置新窗口宽高)
 * @return
 */
function dispParts(src, openFlag){
  if (src){
    if (/javascript:/.exec(src)){
      //当路径为javascript:则src为可执行的函数
      try {
        eval(src);
      } catch (e) {
        
      }
    }
    else{
      //使用?或者&分割URL
      var srcList = src.split(/[?&]/);
      var url = '';
      var paras = '';
      if (srcList.length > 1){
        $.each(srcList, function(i, e){
          if (e == 'openFlag=1'){
            openFlag = 1;
          }
          else if (/^openHeight=:/.exec(e)){
            paras += e.replace('openHeight','height') + ',';
          }
          else if (/^openWidth=:/.exec(e)){
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
      else {
         url = src;
      }
      if (openFlag == 1){
        //当openFlag=1时在新窗口中打开链接
        window.open(encodeURI(url),'',paras);
      }
      else{
        //在工作区打开连接
        openUrl({
          url: url
        })
      }
    }
  }
}

/**
 * 打开功能菜单的函数
 */
function openUrl(node) {
  if (node.openFlag == 1) {
    window.open(node.url);
    return;
  }
  winMgr.createWindow({
    id: node.id,
    containment: ctnInfo.ctn.el,
    tbar: [{
      id: 'close'
    }, {
      id: 'maximize'
    }, {
      id: 'plus',
      preventDefault: true,
      handler: function(e, t, p) {
        p.tools['restore'] && p.tools['restore'].show();
        p.tools['maximize'] && p.tools['maximize'].show();
        t.hide();
        p.maximize(p.containment);
      }
    }, {
      id: 'restore'
    }, {
      id: 'minimize'
    }],
    trayBtn: node.trayBtn,
    lazyContainer: true,
    animate: true,
    showEffect: function() {
      this.expand.apply(this, arguments);
    },
    hideEffect: function() {
      this.collapse.apply(this, arguments)
    },
    draggable: {
      containment: ctnInfo.ctn.el,
      iframeFix: true
    },
    isFillCtn: node.isFillCtn === false ? false : true,
    resizable: {},
    //renderTo: ctnInfo.ctn.el,
    icon: node.icon || winMgr.defaultIcon,
    //cls: 'window-1',
    //parentCmp: YH.getCmp('columnL'),
    iframeSrc: node.url,
    height: node.height || 300,
    width: node.width || 700,
    minWidth: 500,
    minHeight: 200,
    title: node.text
  });
}

/**
 * 播放flash音频
 * @param name
 * @return
 */
function playSound(name) {
  if (!$('#flashPlayer').html()) {
    var flashObjIE = '<object id="soundPlayer" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="../inc/swflash.cab"><param name="movie" value="' + name + '"><param name="quality" value="high"><param name="wmode" value="transparent"></object>';
    var flashObjOther = '<embed id="soundPlayer" src="' + name + '" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash"></embed>';
    $('#flashPlayer').empty();
    if($.browser.msie) {
      $('#flashPlayer').append(flashObjIE);
    } else {
      $('#flashPlayer').append(flashObjOther);
    }
  }
  else {
    if($.browser.msie) {
      $('#soundPlayer').attr("Movie", "refresh");
      $('#soundPlayer').attr("Movie", name);
    } else {
      $('#soundPlayer').attr("src", "");
      $('#soundPlayer').attr("src", name);
    }
  }
}

/**
 * 播放短信提示音 * @return
 */
function smsCallSound() {
  var userinfo = creator.units.userInfo.data;
  if (sms.count < 1) {
    return;
  }
  
  if (sms.count > 1) {
    clearTimeout(sms.smsAlertTimer);
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

function showOrg() {
  
  var orgCtn = new YH.Container({
    renderTo: 'body',
    style: {
      width: 245,
      height: 'auto',
      top: 90,
      //处理orgWin标签页高度问题
      bottom: 64,
      right: 0,
      overflow: 'visible',
      position: 'absolute'
    }
  });
  
  winMgr.createWindow({
    isFillCtn: true,
    draggable: false,
    renderTo: orgCtn.contentEl,
    layout: 'cardlayout',
    layoutCfg: {
      tabs: true
    },
    lazyContainer: true,
    showEffect: function() {
      this.expand.apply(this, arguments);
    },
    hideEffect: function() {
      this.collapse.apply(this, arguments)
    },
    tbar: [{
      id: 'close',
      preventDefault: true,
      handler: function(e, t, p) {
        p.hide();
      }
    }],
    trayBtn: YH.getCmp('orgTrayBtn'),
    items: [{
      title: '在线用户',
      tabBtn: {
        btnText: '在线',
        xtype: 'button',
        normalCls: 'org-online',
        activeCls: 'org-online-active'
      },
      iframeSrc: contextPath + "/core/frame/onlinetree.jsp"
    },{
      title: '所有用户',
      tabBtn: {
        btnText: '全部',
        xtype: 'button',
        normalCls: 'org-all',
        activeCls: 'org-all-active'
      },
      iframeSrc: contextPath + "/core/frame/allusertree.jsp"
    }],
    id: "orgWin",
    title: "组织机构"
  });
}

function classicHome() {
  var url = contextPath + "/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/updateUserParam.act";
  $.post(url,{
    name: 'classicHome',
    value: '1'
  }, function() {
    location.replace(contextPath + "/core/frame/2/index.jsp");
  });
}

/**
 * 单点登录
 * @return
 */
function otherSysLogin(){
  try {
    var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/loginOtherSys.act';
    $.ajax({
      type: "POST",
      dataType: "text",
      url: url,
      success: function(text){
        var json = YH.parseJson(text);
        if (json.rtState == "0") {
          window.open(ssoUrlGPower + '/userLogin.jsp?token=' + json.rtData, '');
        }
        else{
          alert(json.rtMsrg);
        }
      }
    });
  } catch (e) {
    
  }
}

var ctnInfo = {
  ctn: null,
  panels: []
};

var topMenu;
var winMgr;
$(document).ready(function() {

  creator.initialize();
  //生成托盘容器
  var tray = new YH.Container({
    style: {
        height: 'auto',
        width: 'auto',
        overflow: 'visible'
    },
    layout: 'floatlayout',
    items: [
      //菜单按钮
      new YH.Button({
        normalCls: 'btn-menu',
        id: 'menuBtn',
        activeCls: 'btn-menu-active',
        toggle: true,
        toggleHandler: [
          function(e, t) {
            var win = YH.getCmp('menu');
            if (win) {
              win.show();
              return;
            }
             
            var menuCtn = new YH.Container({
              renderTo: 'body',
              style: {
                width: 257,
                height: 'auto',
                top: 90,
                bottom: 44,
                left: 0,
                overflow: 'visible',
                position: 'absolute',
                'z-index': 9999,
                display: 'none'
              }
            });
            
            win = new YH.Window({
              id: 'menu',
              isFillCtn: true,
              draggable: false,
              borderHeight: 27,
              style: {
                'z-index': '9999'
              },
              cls: 'window-menu',
              tbar: [],
              showEffect: function(speed, callback) {
                this.el.show(speed, callback);
              },
              hideEffect: function(speed, callback) {
                this.el.hide(speed, function() {
                  callback && callback();
                  menuCtn.hide();
                });
              },
              trayBtn: t,
              left: 1,
              top: 90,
              resizable: false,
              //cls: 'window-1',
              /*draggable: {
                containment: 'parent'
              },*/
              items: [
                {
                  xtype: 'menu',
                  style: {
                    'overflow-y': 'auto',
                    'overflow-x': 'hidden',
                    'height': '100%'
                  },
                  activeMenu: creator.units.userInfo.data.menuExpand,
                  classes: ['menu-lv1', 'menu-lv2', 'menu-lv3'],
                  loader: {
                    url: contextPath + '/yh/core/funcs/system/act/YHSystemAct/listMenu.act',
                    dataRender: function(data) {
                      if (data.rtState == '0') {
                        data = data.rtData.menu;
                        $.each(data, function(i, e) {
                          data[i].icon = contextPath + '/core/frame/webos/styles/icons/' + e.icon;
                        });
                      }
                      else {
                        alert(data.rtMsrg);
                      }
                      return data;
                    }
                  },
                  openUrl: function(node) {
                    YH.getCmp('menu').hide();
                    openUrl(node);
                  },
                  isLazyLoad: true,
                  lazyLoadData: function (menu) {
                    var id = menu.id;
                    var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/lazyLoadMenu.act?parent=' + id;
                    var menuData = [];
                    $.ajax({
                      type: "GET",
                      dataType: "text",
                      url: url,
                      async: false,
                      success: function(text){
                        var rtjson = YH.parseJson(text);
                        if (rtjson.rtState == '0') {
                          menuData = rtjson.rtData;
                          $.each(menuData, function(i, e) {
                            menuData[i].icon = contextPath + '/core/frame/webos/styles/icons/' + e.icon;
                          });
                        }
                      }
                    });
                    return menuData;
                  },
                  liClass: [null, 'menu-close', null],
                  selClass: 'menu-selected',
                  expClass: ['menu-selected', 'menu-expand']
                }
              ],
              height: 400,
              renderTo: menuCtn.el,
              width: 240,
              minWidth: 200,
              minHeight: 300,
              listeners: {
                initComponent: {
                  after: function(e, t) {
                    var timer;
                    t.el.bind('mouseleave', function() {
                      if (YH.getCmp('menuBtn').status.isPressed) {
                        clearTimeout(timer);
                        timer = setTimeout(function() {
                          t.hide();
                        }, 1000);
                      }
                    }).bind('mouseover', function() {
                      clearTimeout(timer);
                    });
                  }
                },
                hide: {
                  'after': function() {
                    YH.getCmp('menuBtn').setStatus('default');
                    menuCtn.el.hide();
                  }
                },
                show: {
                  'before': function() {
                    menuCtn.el.show();
                  }
                }
              }
            });
            win.show();
          },
          function(e, t) {
            var win = YH.getCmp('menu');
            if (win) {
              win.hide();
            }
          }
        ]
      }),
      YH.packCmp({cls: 'btn-sp'})
    ]
  });

  tray.render('.south .left');
  
  var cmd = new YH.Container({
    style: {
      height: '',
      width: ''
    },
    renderTo: '.south .right',
    layout: 'floatlayout',
    items: [{
      xtype: 'button',
      cls: 'btn-portal',
      activeCls: 'br-btn-active',
      normalCls: 'br-btn',
      id: 'portalBtn',
      title: '门户切换',
      handler: function() {
      }    
    }, YH.packCmp({cls: 'btn-sp'}), {
      xtype: 'button',
      cls: 'btn-sms',
      normalCls: 'br-btn',
      id: 'smsBtn',
      activeCls: 'btn-sms-active',
      title: '消息',
      handler: sms.showShortMsrg
   }/*, YH.packCmp({cls: 'btn-sp'}), {
     xtype: 'button',
     cls: 'btn-lock',
     normalCls: 'br-btn',
     title: '锁定桌面',
     handler: lockScreen
  }*/, YH.packCmp({cls: 'btn-sp'}), {
    xtype: 'button',
    cls: 'btn-switch',
    normalCls: 'br-btn',
    title: '切换为经典界面',
    handler: classicHome
 }, YH.packCmp({cls: 'btn-sp'}), {
      xtype: 'button',
      id: 'themeBtn',
      cls: 'btn-theme',
      normalCls: 'br-btn',
      activeCls: 'br-btn-active',
      toggle: true,
      toggleHandler: [
      function() {
        winMgr.createWindow({
          modal: true,
          maskOpacity: 0,
          src: contextPath + "/core/funcs/setdescktop/theme/select.jsp",
          width: 780,
          height: 400,
          lazyContainer: true,
          trayBtn: YH.getCmp('themeBtn'),
          showEffect: function() {
            this.expand.apply(this, arguments);
          },
          hideEffect: function() {
            this.collapse.apply(this, arguments)
          },
          tbar: [{
            id: 'close',
            preventDefault: true,
            handler: function(e, t, p) {
              p.hide();
            }
          }],
          id: "themeSelect",
          title: "主题选择",
          listeners: {
            hide: {
              'before': function(e, t) {
                changeBg(t.isSave ? "save" : "reset");
              }
            }
          }
        });
      }, function() {
        YH.getCmp('themeSelect') && YH.getCmp('themeSelect').hide();
      }],
      title: '主题切换'
    }, YH.packCmp({cls: 'btn-sp'}), {
      xtype: 'button',
      id: 'controlTrayBtn',
      cls: 'btn-control',
      normalCls: 'br-btn',
      activeCls: 'br-btn-active',
      destroy: $.noop,
      title: '控制面板',
      toggleHandler: [function(e, t) {
        openUrl({
          id: creator.units.otherPara.data.controlId || 'control',
          trayBtn: YH.getCmp('controlTrayBtn'),
          url: contextPath + '/core/funcs/setdescktop/control.jsp',
          icon: contextPath + '/core/frame/webos/styles/icons/person_info.png',
          text: '控制面板'
        });
      },function(e, t){
        var cmp = YH.getCmp('control');
        if (cmp) {
          cmp.hide();
        }
      }],
      toggle: true
    }/*, YH.packCmp({cls: 'btn-sp'}), {
      xtype: 'button',
      cls: 'btn-shortcut',
      normalCls: 'br-btn',
      activeCls: 'br-btn-active',
      title: '快捷设置'
    }*/, YH.packCmp({cls: 'btn-sp'}), {
      xtype: 'button',
      id: 'orgTrayBtn',
      cls: 'btn-org',
      normalCls: 'br-btn',
      activeCls: 'br-btn-active',
      title: '组织机构',
      toggle: true,
      toggleHandler: [function(e, t) {
        showOrg();
      },function(e, t, callback){
        var cmp = YH.getCmp('orgWin');
        if (cmp) {
          cmp.hide();
        }
        callback && callback();
      }]
    }, YH.packCmp({cls: 'btn-sp'}),{
      xtype: 'button',
      cls: 'btn-logout',
      normalCls: 'br-btn',
      handler: doLogoutMsg,
      title: '注销'
    }]
  });
  //门户选择
  var url = contextPath + '/yh/core/funcs/portal/act/YHPortalAct/listAllPortals.act';
  var info = $('#info span');
  $.post(url, function(t) {
    var json = YH.parseJson(t);
    var items = [];
    if (json.rtState == '0') {
      json = json.rtData;
      var tip = new YH.Tip({
        target: '#portalBtn',
        id: 'portalTip',
        event: 'leftClick',
        cls: 'tip-1',
        pointerBc: true,
        layer: 'upper',
        relative: {
          y: -10,
          x: -75
        },
        height: 'auto',
        layout: 'floatlayout',
        listeners: {
          show: {
            after: function(e, t) {
              YH.getCmp('portalBtn').setStatus('active');
            }
          },
          hide: {
            after: function(e, t) {
              YH.getCmp('portalBtn').setStatus('default');
            }
          }
        },
        delay: 2,
        items: [{
          xtype: 'button',
          style: {
            padding: '10px 20px 10px 10px'
          },
          normalCls: 'portal-design',
          handler: function() {
            window.open(contextPath + '/core/frame/webos/design/index.jsp?type=design&id=personal');
          }
        }]
      });
      //为了兼容ie6/7写的代码!!!!
      tip.setWidth((json.records.length || 0) * 90 + 150);
      $.each(json.records, function(i, e) {
        var btn = new YH.Button({
          normalCls: 'portal',
          btnText: e.name,
          id: e.id,
          style: {
            'padding-top': '10px',
            "padding-left": '5px'
          },
          title: e.name,
          handler: function() {
            $.post(contextPath + '/yh/core/funcs/portal/act/YHPortalAct/setDefaultPortal.act', {id: this.id}, function(t) {
              var json = YH.parseJson(t);
              if (json.rtState == '0') {
                winMgr.removeAll();
                creator.units.portal.reload();
                tip.hide();
              }
            });
          }
        });
        /**暂时不需要浮动
        new YH.Tip({
          html: e.name || '门户',
          target: btn.el,
          relative: {
            x: 0,
            y: -75
          },
          width: 120
        });
        */
        items.push(btn);
      });
      tip.addItems(items);
    }
  });
  
  winMgr = new YH.WindowMgr({
    tray: tray,
    cls: 'window-group',
    //当没有指定图标时默认显示用的图标(在openUrl中处理的)
    defaultIcon: contextPath + '/core/frame/webos/styles/icons/default.png'
  });

  var menu = new YH.Menu({
    icon: true,
    id: "menuContent",
    openUrl: function() {
      topMenu.fold();
      openUrl.apply(this, arguments);
    },
    loader: {
      url : contextPath + '/yh/core/frame/act/YHClassicInterfaceAct/listShortCut.act',
      dataRender: function(data) {
        $.each(data, function(i, e) {
          data[i].icon = contextPath + '/core/frame/webos/styles/icons/' + e.icon;
        });
        return data;
      }
    },
    listeners: {
      initComponent: {
        after: function(e, t) {
          var onclick;
          t.el.find('ul').sortable({
            revert: true,
            //delay: 200,
            //distance: 10,               //延迟拖拽事件(鼠标移动十像素),便于操作性            handle: "img",
            tolerance: 'pointer',       //通过鼠标的位置计算拖动的位置*重要属性*
            stop: function(e, ui) {
              setTimeout(function() {
                ui.item.find('a')[0].onclick = onclick;
                //保存顺序
                var url = contextPath + '/yh/core/funcs/setdescktop/shortcut/act/YHShortcutAct/modifyShortCut.act';
                var idStr = '';
                t.el.find('li').each(function(i, e) {
                  idStr += $(e).data('id') + ',';
                });
                $.post(url, {
                  sortCut: idStr
                });
              }, 0);
            },
            start: function(e, ui) {
              onclick = ui.item.find('a')[0].onclick;
              ui.item.find('a')[0].onclick = null;
            }
          });
          /*
           * 鼠标经过的动画           */
           t.el.find('ul li a').hover(function() {
            //在图片放大时清除对象的动画队列,保证动画效果顺畅
            $(this).children('img').stop(true).animate({
              width: 64,
              height: 64,
              'margin-top': '-3px'
            }, 'fast');
          }, function() {
            $(this).children('img').animate({
              width: 60,
              height: 60,
              "margin-top": '0'
            }, 'fast');
          });
        }
      }
    }
  });

  var shortcutBtn = new YH.Button({
    toggle: true,
    id: 'shortcutBtn',
    normalCls: 'shortcut-a-btn',
    activeCls: 'shortcut-b-btn',
    toggleHandler: [function(e, t, callback) {
      topMenu.unfold(callback);
    }, function(e, t, callback) {
      topMenu.fold(callback);
    }]
  });
  
  topMenu = new YH.Container({
    id: 'topMenu',
    cls: 'north-center',
    renderTo: '.north .center',
    width: 'auto',
    cls: 'menu-container',
    layoutCfg: {
      pixel: true,
      style: {
        overflow: 'hidden',
        position: 'relative'
      },
      sortable: false
    },
    unfold: function() {
    //获取高度,为了兼容ie6/7
      var th = topMenu.el.find('table').height();
      var uh = topMenu.el.find('ul').height();
      var height = th > uh ? th : uh;
      if (height <= 0) {
        height = 80;
        return;
      }
      height = $(window).height() - 38;
      $(".north .center").animate({'height': height}, 'slow');
    },
    fold: function() {
      shortcutBtn.setStatus('default');
      $(".north .center").animate({'height': 82}, 'slow');
    },
    layout: 'columnlayout',
    items: [{
      items: [menu]
    }, {
      columnWidth: '80px',
      style: {
        'padding-top': '15px'
      },
      items: [shortcutBtn]
    }],
    listeners: {
      /**
       * 初始化完成的时候绑定快捷菜单收起的事件
       */
      initComponent: {
        after: function(e, t) {
          //获取高度,为了兼容ie6/7,暂时延时取高度          setTimeout(function() {
            var th = t.el.find('table').height();
            var uh = t.el.find('ul').height();
            var height = th > uh ? th : uh;
            if (height <= 100) {
              shortcutBtn.hide();
            }
            else {
              shortcutBtn.show();
            }
          }, 0);
          $(window).resize(function() {
            var th = t.el.find('table').height();
            var uh = t.el.find('ul').height();
            var height = th > uh ? th : uh;
            if (height <= 100) {
              shortcutBtn.hide();
            }
            else {
              shortcutBtn.show();
            }
          });
          
          var shortcutTimer;
          t.el.bind('mouseleave', function() {
            if (shortcutBtn.status.isPressed) {
              clearTimeout(shortcutTimer);
              shortcutTimer = setTimeout(function() {
                topMenu.fold();
              }, 1000);
            }
          });
          t.el.bind("mouseover", function() {
            clearTimeout(shortcutTimer);
          });
        }
      }
    }
  });
  
  //解决ie6/7中二级页面href="#"锚点导致页面上滚问题
  if (YH.browser.isIE6 || YH.browser.isIE7) {
    document.body.onscroll = document.documentElement.onscroll = function(e) {
      setTimeout(function() {
        document.body.scrollTop = document.documentElement.scrollTop = 0;
      }, 0);
    }
  }
});

/**
 * 重新载入菜单快捷组的方法
 */
function reloadShortcut() {
  YH.getCmp("menuContent").reload();
  setTimeout(function() {
    var th = topMenu.el.find('table').height();
    var uh = topMenu.el.find('ul').height();
    var height = th > uh ? th : uh;
    if (height <= 100) {
      YH.getCmp("shortcutBtn").hide();
    }
    else {
      YH.getCmp("shortcutBtn").show();
    }
  }, 0);
}

