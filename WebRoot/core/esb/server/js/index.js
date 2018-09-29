/**
 * 弹出询问窗口的注销
 */
function doLogoutMsg() {
  if (!window.alertMsrg){
    window.alertMsrg = "您确认要注销吗？";
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
  window.location.replace(contextPath + "/core/esb/login.jsp");
}

/**
 * 收起页面上部
 * @return
 */
function collapseUp(){
  if ($('#rightBarUp').data('up')){
    $('#rightBarUp').attr('src', imgPath + '/mainframe/call_up.jpg');
    bodyLayout.show('north', true);
    $('#rightBarUp').data('up', false);
  }
  else{
    $('#rightBarUp').attr('src', imgPath + '/mainframe/call_down.jpg');
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
    $('#callRight').css({'visibility':'hidden'});
    bodyLayout.show('west', true);
    $('#usercount').addClass('statusbar-usercount');
    $('#usercount').animate({
      'background-position': '0px 0px'
    }, 'slow');
    $('#callRight').data('left', false);
  }
  else{
    $('#callRight').css({'visibility':'visible'});
    bodyLayout.hide('west', true);
    $('#usercount').animate({
      'background-position': '-209px 0px'
    }, 'slow', function() {
      $('#usercount').removeClass('statusbar-usercount');
    });
    $('#callRight').data('left', true);
  }
}


function initMenu(el) {
  var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/listMenu.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
        new YH.Menu({
          classes: ['menu-lv1', 'menu-lv2', 'menu-lv3'],
          data: json.rtData.menu,
          el: el,
          openUrl: function (node) {
            dispParts(node.url, node.openFlag);
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
}

/**
 * 初始化时间日期显示
 * @return
 */
function initTime() {
  this.timeview = function (){
    $('#localtime').html(new Date().toLocaleTimeString());
    window.setTimeout(timeview, 1000 );
  }
  this.timeview();
  var now = new Date();
  var day = ['日','一','二','三','四','五','六'];
  $('#date').html((now.getMonth() + 1) + '月' + now.getDate() + '日 星期' + day[now.getDay()] + '&nbsp&nbsp');
}

var bodyCards = [];

$(document).ready(function() {
  window.bodyLayout = $('body').layout({
    'listeners': {
      'resize': function(){
        westLayout.resizeAll();
        mainLayout.resizeAll();
      }
    },
    north: {
      'fxSpeen': 'slow',
      size: 64
    },
    south: {
      size: 21
    },
    west: {
      'fxSpeed': 'slow',
      size: 209
    },
    center: {
      
    }
  });
  
  window.westLayout = $('#westContainer').layout({
    'listeners': {
      'resize': function(){
        leftLayout.resizeAll();
      }
    },
    north: {
      size: 35
    },
    center: {
    }
  });

  window.mainLayout = $('#mainContainer').layout({
    north: {
      size: 35
    },
    center: {
    }
  });
  
  //$('#northContainer').load('banner.jsp');
  window.leftLayout = $('#leftPanel').layout({
    'listeners': {
      'resize': function(){
        $('#leftPanel .ui-layout-center:first').resize();
      }
    },
    south: {
      size: 12
    },
    west: {
      size: 2
    },
    east: {
      size: 7
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
            initMenu
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
  
  window['main-body-desktop'].location.href = "about:blank";
  initTime();
});


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
        getWorkspaceWindow().location = encodeURI(url);
      }
    }
  }
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
  window['main-body-desktop'].location.reload();
}

/**
 * 显示桌面
 * @return
 */
function dispDesk(){
  try {
    //给工作区的iframe内的window添加onunload事件
    //处理在显示工作流/待办工作时,点击桌面按钮时,离开的确认信息不生效的问题

    $(getWorkspaceWindow()).one('unload', function(event) {
      $('#main-body-parts').hide();
      $('#main-body-desktop').show();
    });
  } catch (e) {
    $('#main-body-parts').hide();
    $('#main-body-desktop').show();
  }
  getWorkspaceWindow().location.href = "about:blank";
}