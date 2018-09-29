
/**
 * 显示桌面模块
 * @param src   以javascript:开头则直接运行javascript:后边的内容 *              带有openFlag=1的url在新窗口中打开(通过参数openWidth=?/openHeight=?设置新窗口宽高)
 * @return
 */
function dispParts(src, openFlag, id, title){
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
      else{
        url = src;
      }
      
      if (openFlag == 1){
        //当openFlag=1时在新窗口中打开链接
        window.open(encodeURI(url),'',paras);
      }
      else{
        //在工作区打开连接
        $('#main-body-desktop').hide();
        $('#main-body-parts').show();
        //getWorkspaceWindow().location = encodeURI(url);
        addTab(id, title, url, "selected");
      }
    }
  }
}

function openUrl(node) {
  dispParts(node.url, node.openFlag, node.id, node.text);
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
function reloadDesktop() {
  window['main-body-desktop'].location.reload();
}

/**
 * 显示桌面
 * @return
 */
function dispDesk() {
  //每次点桌面的时候都刷新桌面
  reloadDesktop();
  try {
    //给工作区的iframe内的window添加onunload事件
    //处理在显示工作流/待办工作时,点击桌面按钮时,离开的确认信息不生效的问题    $(getWorkspaceWindow()).one('unload', function(event) {
      $('#main-body-parts').hide();
      $('#main-body-desktop').show();
    });
  } catch (e) {
    $('#main-body-parts').hide();
    $('#main-body-desktop').show();
  }
  getWorkspaceWindow().location.href = "about:blank";
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
 * 查询注销时显示的信息
 * @return
 */
function queryLogoutMsrg() {
  var url = contextPath + '/yh/core/frame/act/YHClassicInterfaceAct/queryLogoutText.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == "0") {
        window.alertMsrg = json.rtData;
      }
    }
  });
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

/**
 * 收起页面上部
 * @return
 */
function collapseUp(){
  if ($('#rightBarUp').data('up')){
    $('#rightBarUp').removeClass("up");
    bodyLayout.show('north', true);
    $('#rightBarUp').data('up', false);
  }
  else{
    $('#rightBarUp').addClass("up");
    bodyLayout.hide('north', true);
    $('#rightBarUp').data('up', true);
  }
}


/**
 * 收起/展开左侧菜单栏
 * 同时设置底部栏的背景与左侧菜单栏的动画同步
 * @return
 */
function collapseLeft(){
  if ($('#callRight').data('left')){
    $('#callRight').removeClass("hideleft");
    bodyLayout.show('west', true);
    $('#usercount').addClass('statusbar-usercount');
    $('#usercount').animate({
      'background-position': '0px 0px'
    }, 'slow');
    $('#callRight').data('left', false);
  }
  else{
    $('#callRight').addClass('hideleft');
    bodyLayout.hide('west', true);
    $('#usercount').animate({
      'background-position': '-209px 0px'
    }, 'slow', function() {
      $('#usercount').removeClass('statusbar-usercount');
    });
    $('#callRight').data('left', true);
  }
}

/**
 * 初始设置导航菜单收起的状态(不使用动画)
 * @return
 */
function initCollapseLeft() {
  $('#callRight').addClass('hideleft');
  bodyLayout.hide('west');
  $('#usercount').css({
    'background-position': '-209px 0px'
  });
  $('#usercount').removeClass('statusbar-usercount');
  $('#callRight').data('left', true);
}

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

if (useSearchFunc === "0") {
  imgsSrc.pop();
}

/**
 * 初始化左侧图标按钮
 * @param el
 * @param imgsSrc
 * @param active
 * @param func
 * @return
 */
function initLeftTbar(el, imgsSrc, active, func) {

　var lastEl, lastSrc;
  var ul = $('<ul></ul>');
  el.append(ul);
  $.each(imgsSrc, function(i, e){
    var img = $('<img></img>');
    var a = $('<a href="javascript:void(0)" onclick="return false;"></a>').append(img);
    var li = $('<li></li>').append(a);
    var sp = $('<div class="leftbar-sp"></div>');

    if (i > 0) {
      ul.append(sp);
    }
    ul.append(li);
    if (active == i) {
      lastEl = img;
      lastSrc = e.src;
      img.attr('src', e.selectedSrc);
    }
    else {
      img.attr('src', e.src);
    }
    
    a.click(function(){
      if (lastEl == img) {
      }
      else {
        lastEl.attr('src', lastSrc);
        img.attr('src', e.selectedSrc);
        lastEl = img;
        lastSrc = e.src;
        if (func) {
          func(i);
        }
      }
    });

    if (e.imgs && e.imgs.length > 0) {
      initLeftTbar($('.leftmenu-north-lv2').eq(i), e.imgs, 0, function(j) {
        bodyCards[i].setActiveItem(j);
      });
    }

  });
}

function selectTab(i) {
  $('#menuExplorer li>a').eq(i).click();
}

/**
 * 初始化主菜单
 * @param el
 * @return
 */
function initMenu() {
  var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/listMenu.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
        new YH.Menu({
          id: "menu",
          classes: ['menu-lv1', 'menu-lv2', 'menu-lv3'],
          data: json.rtData.menu,
          el: $(".panel-menu > div"),
          activeMenu: userinfo.menuExpand,
          openUrl: function (node) {
            dispParts(node.url, node.openFlag, node.id, node.text);
          },
          expandType: !json.rtData.expandType,
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
                }
              }
            });
            return menuData;
          },
          liClass: [null, 'menu-close', null],
          selClass: 'menu-selected',
          expClass: ['menu-selected', 'menu-expand']
        });
      }
    }
  });
  $("#start_menu").click(function() {
    $(".panel-menu > div").height($(".mainContainer").height() - 50);
    $("#start_menu_mask").show("slow");
    $("#start_menu_panel").css({
      top: parseInt($("#mainContainer").css("top")) ? "112px" : "36px"
    });
  });
  $("#start_menu_mask").click(function(e) {
    e = window.event || e;
    if ((e.srcElement || e.target) == $("#start_menu_mask")[0]) {
      $(this).hide("slow");
    }
  });
}

/**
 * 初始化菜单快捷组
 * @param el
 * @return
 */
function initShortcut(el) {
  var url = contextPath + '/yh/core/frame/act/YHClassicInterfaceAct/listShortCut.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      new YH.Menu({
        classes: ['menu-lv2'],
        data: json,
        el: el,
        id: "shortcut",
        openUrl: function (node) {
          dispParts(node.url, node.openFlag);
        },
        liClass: [],
        selClass: 'menu-selected',
        expClass: ['menu-selected', 'menu-expand']
      });
    }
  });
}

/**
 * 初始化收藏夹目录
 * @param el
 * @return
 */
function initFav(el) {
  var url = contextPath + '/yh/core/funcs/setdescktop/fav/act/YHFavAct/list.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      new YH.Menu({
        classes: ['menu-lv2'],
        data: json,
        el: el,
        id: "fav",
        openUrl: function (node) {
          dispParts(node.url, node.openFlag);
        },
        liClass: [],
        selClass: 'menu-selected',
        expClass: ['menu-selected', 'menu-expand']
      });
    }
  });
}

var userinfo = {};

/**
 * 查询用户信息
 * @return
 */
function queryInfo() {
  var url = contextPath + '/yh/core/frame/act/YHClassicInterfaceAct/queryInfo.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
        $.extend(userinfo, json.rtData);
        initUserInfo();
        initTab();
        showWeather && showWeather();
        if (userinfo.nevMenuOpen != '1') {
          initCollapseLeft && initCollapseLeft();
        }
      }
    }
  });
}

function initTab() {
  if (!initTab.init) {
    initTab.init = true;
    selectTab((userinfo.panel || 1) - 1);
  }
}

/**
 * 初始化用户信息
 * 包括用户姓名/头像/部门/角色/在线时间/禁止显示桌面等
 * @return
 */
function initUserInfo() {
  var status = [{text: '联机',icon:[ imgPath + '/U01.gif',imgPath + '/U11.gif']},
                {text: '忙碌',icon:[ imgPath + '/U02.gif',imgPath + '/U12.gif']},
                {text: '离开',icon:[ imgPath + '/U03.gif',imgPath + '/U13.gif']}];
  
  var name = userinfo.userName || "";
  if (name.length > 7) {
    name = name.substring(0, 6) + "...";
  }
  $('#usernameText').html(name);
  var userInfoTemp = '部门:' + (userinfo.deptName || '')  + ' 角色:' + (userinfo.privName || '')
  $('#usernameText').attr('title', userInfoTemp);
  
  var src;
  if (/\.[a-z,A-Z]{1,}/.test(userinfo.avatar)) {
    src = contextPath + '/attachment/avatar/' + userinfo.avatar;
  }
  else {
    src = contextPath + '/core/styles/imgs/avatar/' + (userinfo.avatar || 1) + '.gif';
  }
  $('#usernameTextImg').attr('src', src);
  
  try {
    $('#userStatusText').addClass('status_icon_'+(userinfo.onStatus || 1));
  } catch (e) {
    $('#userStatusText').addClass('status_icon_1');
  }
  
  bindEvenOnStatus();
  
//  var minutes = Math.floor((userinfo.onLine || 0) / 60);
//  var hours = Math.floor(minutes / 60);
//  minutes %= 60;
//  
//  var desc = '姓名:&nbsp;' + (userinfo.userName || '')
//  + '<br>部门:&nbsp;' + (userinfo.deptName || '')
//  + '<br>角色:&nbsp;' + (userinfo.privName || '')
//  + '<br>在线时长:&nbsp;' + hours + "小时" + minutes + "分钟"
//  + '<br><br>' + (userinfo.myStatus || '');
//
//  if (!window.userTip) {
//    window.userTip = new YH.Tip({
//      'type': 'html',
//      'event': 'mouseOver',
//      'html': desc,
//      'target': $('#username'),
//      'style': {
//        'height': 'auto',
//        'width': '200px',
//        'style': 'word-break:break-all;',
//        'padding': '5px'
//      }
//    });
//  }
//  else {
//    window.userTip.setHtml(desc);
//  }
//  //绑定回车事件和失去焦点事件//
//  userInfoEvent();
}

function bindEvenOnStatus(){
  $('#usernameTextImg').click(
    function(){
      $('#on_status').show('slow');
    }
  );
  $('#userStatusText').click(
    function(){
      $('#on_status').show('slow');
    }
  );
  var timer;
  $('.avatar').bind('mouseleave', function() {
    clearTimeout(timer);
    timer = setTimeout(function() {
      $('#on_status').hide();
    }, 1000);
  }).bind('mouseover', function() {
    clearTimeout(timer);
  });
}

/**
 * 初始化用户状态和被选状态
 * 判断用户性别使用不同图标
 * @return
 */
function initUserStatus() {
  var contextEl;
  if (userinfo.sex && userinfo.sex == '1') {
    contextEl = $('#contentTipWoman');
  } 
  else {
    contextEl = $('#contentTipMan');
  }
  window.statusTip = new YH.Tip({
    'target': '#userstatus',
    'content': contextEl,
    'event': 'leftClick',
    'delay': 3,
    'style': {
      'width': '80px',
      'padding': '5px'
    },
    relative: {
      x: -10,
      y: 10
    }
  });
}

function editUserInfo(){
  $('#editInfo').addClass("editing");
  document.getElementById('editInfoText').focus();
}

/**
 * 获取浏览器的标题
 * @return
 */
function getTitle() {
  var url = contextPath + '/yh/core/frame/act/YHClassicInterfaceAct/getInterfaceInfo.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
        var d = json.rtData;
        document.title = d.title;
        if (!d.hideLogo) {
          $('#logo').find('img').show();
        }
        if (d.bannerText) {
          $('#logo').append(d.bannerText).attr("style", d.bannerFont).css({
            "padding-left": "10px",
            "line-height": "60px"
          });
        }
      }
    }
  });
}

/**
 * 修改mystatus
 * @return
 */
function submitUserInfo() {
  $('#editInfo').removeClass("editing");
  if ($('#editInfoText').val() != '我的留言'){
    
    var url = contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/modifyMyStatus.act';
    $.ajax({
      type: "GET",
      dataType: "text",
      url: url,
      data: {
        'myStatus': $('#editInfoText').val()
      },
      success: function(text){
        queryInfo();
        initUserInfo();
      }
    });
  }
}

/**
 * 输入回车或者失去焦点时提交信息
 * @return
 */
function userInfoEvent(){
  $('#editInfoText').unbind('keypress');
  $('#editInfoText').bind('keypress', function(event) {
    if (event.keyCode == 13){
      //设置回车事件后的失去焦点事件无效
      $('#editInfoText').unbind('blur');
      submitUserInfo();
    }
  });
  $('#editInfoText').unbind('blur');
  $('#editInfoText').bind('blur', submitUserInfo);
  $('#editInfo input').bind('focus', function() {
    $('#editInfo').addClass("editing");
  });
}

/**
 * 设置用户状态

 * 联机/忙碌/离开
 * @param index
 * @return
 */
function changeStatus(index){
  var status = [{text: '联机',icon:[ imgPath + '/U01.gif',imgPath + '/U11.gif']},
                {text: '忙碌',icon:[ imgPath + '/U02.gif',imgPath + '/U12.gif']},
                {text: '离开',icon:[ imgPath + '/U03.gif',imgPath + '/U13.gif']}];
  var url = contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/modifyOnStatus.act';
  $.ajax({
    type: "POST",
    dataType: "text",
    data: {onStatus: index},
    url: url,
    success: function(text){
      $('#userStatusText').removeClass('status_icon_1 status_icon_2 status_icon_3');
      $('#userStatusText').addClass('status_icon_'+(index || 1));
      $('#on_status').hide();
    }
  });
}

var shortcutTop = -64;
function fillShortcut(){
  var url = contextPath + '/yh/core/frame/act/YHClassicInterfaceAct/listShortCut.act';
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
      if (json.length > 4) {
        $('#shortcutBtnDown').show();
        $('#shortcutBtnUp').show();

        $('#shortcutBtnDown').bind('click', function() {
          if ( -shortcutTop >= $('#bannerShortcut').outerHeight()) {
            shortcutTop = 0;
            $('#bannerShortcut').css({'top': '64px'});
          }
          $('#bannerShortcut').animate({
              'top': shortcutTop
            }, 'normal',
            function() {
              shortcutTop += -64;
            }
          );
        });
        $('#shortcutBtnUp').bind('click', function() {
          shortcutTop += 64 * 2;
          if (shortcutTop > 0) {
            shortcutTop = -$('#bannerShortcut').outerHeight() + 64;
            $('#bannerShortcut').css({'top': '-' + $('#bannerShortcut').outerHeight() + "px"});
          }
          $('#bannerShortcut').animate({
              'top': shortcutTop
            }, 'normal',
            function() {
              shortcutTop += -64;
            }
          );
        });
      }
      var tr = null;
      $.each(json, function(i, e) {
        var a = $('<a href="javascript:void(0)"></a>');
        a.html(e.text);
        a.bind('click', function() {
          dispParts(e.url);
        });

        if (!(i % 4)){
          var tr = $('<tr></tr>');
          $('#bannerShortcutTb').append(tr);
          for (var j =0; j < 4; j++) {
            var td = $('<td class="banner_shortcut_bg" cellspacing="0" cellpadding="0" border="0"></td>');
            td.addClass("td" + j);
            tr.append(td);
          }
        }
        var td = $('#bannerShortcutTb td').eq(i);
        td.append(a);
      });
    }
  });
}

function reloadShortcut(){
  fillShortcut();
  initShortcut($('#shortcut').empty());
}

/**
 * 重新加载个人收藏夹网址
 * @return
 */
function reloadFavMenu() {
  initFav($('#fav').empty());
}


function webosHome() {
  var url = contextPath + "/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/updateUserParam.act";
  $.post(url,{
    name: 'classicHome',
    value: '0'
  }, function() {
    location.replace(contextPath + "/core/frame/webos/index.jsp");
  });
}

/**
 * 初始化时间日期显示 * @return
 */
function initTime() {
  var date = new LunarCalendar();
  $('#time .left').append(date.YYMMDD() + "<br>");
  $('#time .left').append(date.solarDay2());
  
  $('#time .right').append(date.weekday() + "<br>");
  
  var time = $('<span></span>');
  $('#time .right').append(time);
  this.timeview = function (){
    var now = new Date();
    var h = now.getHours();
    var m = now.getMinutes();
    if (h < 10) {
      h = "0" + h;
    }
    if (m < 10) {
      m = "0" + m;
    }
    time.html(h + ":" + m);
    window.setTimeout(timeview, 1000 * 30);
  }
  this.timeview();
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

function initTabs() {
  $('#tabs_container').tabs({
    tabsLeftScroll:'tabs_left_scroll',
    tabsRightScroll:'tabs_right_scroll',
    panelsContainer:'workspace',
    secondTabsContainer: 'funcbar_left'
  });
}

function addTab(id, title, url, selected, closableFlag) {
  if(closableFlag == 1){
    closableFlag = false;
  }
  else{
    closableFlag = true;
  }
  $('#tabs_container').tabs('add', {
    id: id,
    title: title,
    closable: closableFlag,
    selected: selected,
    style: 'height: 100%;',
    content: '<iframe name="tabs_'+id+'_iframe1" src="' + url + '?menuModleId=' + id + '" style="height: 100%;width: 100%;" allowTransparency="true" frameborder="0"></iframe>'
  });
  $("#start_menu_mask").hide("slow");
}

function showWeather() {
  if (userinfo.SHOW_WEATHER) {
    var iframe = $('<iframe id="weather" style="" allowTransparency="true" frameborder="0"></iframe>').attr("src", contextPath +"/core/frame/5/weather.jsp");
    $('#wthAndTime').append(iframe);
  }
  else {
    $('#wthAndTime').addClass('timeonly');
  }
}

function showOrg() {
  
  var orgCtn = new YH.Container({
    renderTo: 'body',
    style: {
      width: 245,
      height: 'auto',
      top: 150,
      //处理orgWin标签页高度问题

      bottom: 62,
      right: 0,
      overflow: 'visible',
      position: 'absolute'
    }
  });
  
  var orgWin = YH.getCmp("orgWin");
  if (orgWin && !orgWin.status.hidden) {
    orgWin.hide();
  }
  else if (orgWin) {
    orgWin.show();
  }
  else {
    new YH.Window({
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
    }).show();
  }
}

var themeArray = [];
themeArray["1"] = {src:contextPath +"/core/frame/5/styles/themeicons/1.jpg", title:"云端科技"};
themeArray["2"] = {src:contextPath +"/core/frame/5/styles/themeicons/2.jpg", title:"绿色科技"};
themeArray["3"] = {src:contextPath +"/core/frame/5/styles/themeicons/3.jpg", title:"科技之光"};
function initTheme(){
   //创建主题图片
   for(var id in themeArray)   {
      if(themeArray[id].src=="") return; 
      var aobj =   $('<a class="theme_thumb" hidefocus="hidefocus"><img src="'+themeArray[id].src+'" width="107" height="54" /></a>');
      aobj.attr("index",id);
      
      aobj.append("<span>"+themeArray[id].title+"</span>")
      $('#theme_slider').append(aobj);
   }
   
   $('#theme_slider').append('<div style="clear:both;"></div>');
   //主题切换
   $('#theme_slider a.theme_thumb').live("click",function(){
      var index = $(this).attr("index");
      if(ostheme == index){return;}
      setTheme(index);
   });
   
   $("#theme_slider a").hover(
      function(){$(this).addClass("current");},
      function(){$(this).removeClass("current");}
   )
   
   //主题面板显示
   $('#theme').bind('click', function(){
      if($('#'+this.id+'_panel:visible').length){
         $('#'+this.id+'_panel').animate({top:-$('#'+this.id+'_panel').outerHeight()}, 600, function(){$(this).hide();$('#center').hide();});
         $('#overlay_panel').hide();
         return;
      }
      
      $('#center').show();

      //面板位置
      //$('.over-mask-layer').hide();
      //$('#overlay_panel').show();
      //$('#'+this.id+'_panel').css('left', ($(document).width()-$('#'+this.id+'_panel').width())/2);
      var top = $('#'+this.id+'_panel').outerHeight() > $('#center').outerHeight() ? -10 : 0;
      $('#'+this.id+'_panel').css({top: -$('#'+this.id+'_panel').outerHeight()});
      $('#'+this.id+'_panel').show().animate({top:top}, 600);
      
      //常用任务图标设为active状态
      $(this).addClass('active');
      window.setTimeout(checkActive, 300, this.id);
      
   });
}

//设置主题样式
function setTheme(theme) { 
   var wins = [];
    
   function iteratWins(win) { 
      try { 
         var iframes = win.document.getElementsByTagName("iframe"); 
         for (var i = 0; i < iframes.length; i++) { 
            var iframe = iframes[i];
            wins.push(iframe.contentWindow);
            iteratWins(iframe.contentWindow);
         }
      } catch (e) {
      
      } 
   }
   function setPerWin(w) { 
      try { 
         var imgs = w.document.getElementsByTagName("img"); 
         for (var i = 0; i < imgs.length; i++) { 
            var e = imgs[i]; 
            if (/5\/styles\/style[0-9]{1,}\//.test(e.src)) { 
               e.src = e.src.replace(/5\/styles\/style[0-9]{1,}\//, "5\/styles\/style" + theme + "/"); 
            }
         } 
     
         var links = w.document.getElementsByTagName("LINK"); 
         for (var i = 0; i < links.length; i++) { 
            var e = links[i];
            if (/5\/styles\/style[0-9]{1,}\//.test(e.href)) { 
               e.href = e.href.replace(/5\/styles\/style[0-9]{1,}\//, "5\/styles\/style" + theme + "/"); 
            }
         } 
      } catch (e) {
          
      } 
   }
   iteratWins(top); 
   wins.push(top); 
   for (var i = 0; i < wins.length; i++) { 
      setPerWin(wins[i]); 
   }
   
   var flag = updateTheme(theme);
   if(flag)
      ostheme = theme; 
}

function updateTheme(themeid){
  var flag = false;
  $.ajax({
     async: false,
     data: {"oaStyle": themeid},
     url: contextPath + '/yh/core/frame/act/YHTdoaAct/updateUserParamStyle.act',
     success: function(text) {
       var json = YH.parseJson(text);
       if (json.rtState == "0") {
           flag = true;  
        }
     }
  });
  return flag;
}

function checkActive(id){
  if($('#'+id+'_panel:hidden').length > 0)
     $('#'+id).removeClass('active');
  else
     window.setTimeout(checkActive, 300, id);
};

function initSearch(){
  $("#keyword").bind('keyup',function(){
    $('.ui-menu-item').empty();
    $.ajax({
      async: false,
      data: {"keyWord": $('#keyword').val(), "pageStart": "0", "pageNum": "10"},
      url: contextPath + "/yh/core/funcs/search/act/YHFrameSerach/searchUser.act",
      success: function(text) {
        var json = YH.parseJson(text);
        if (json.rtState == "0") {
          $.each(json.rtData.records, function(i,e){
            var a = $('<a class="ui-corner-all" tabindex="-1">'+e.userName+'</a>');
            a.click(function(){
              openUser(e.seqId);
            });
            $('.ui-menu-item').append(a);
          });
        }
      }
    });
    $(".ui-autocomplete").css({
      top: parseInt($("#mainContainer").css("top")) ? "142px" : "64px"
    });
    $(".ui-autocomplete").show();
  });
  $("#keyword").bind('blur',function(){
    setTimeout(function(){
      $(".ui-autocomplete").hide();
    },1000)
  });
}

function openUser(userId){
  var url = contextPath + "/core/funcs/userinfo/person.jsp?userId=" + userId;
  window.open(url);
}

function getWorkspaceHeigt() {
  return $("#mainContainer .ui-layout-center").height();
}