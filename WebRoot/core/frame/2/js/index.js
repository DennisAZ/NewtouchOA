
/**
 * 显示桌面模块
 * @param src   以javascript:开头则直接运行javascript:后边的内容 *              带有openFlag=1的url在新窗口中打开(通过参数openWidth=?/openHeight=?设置新窗口宽高)
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

function openUrl(node) {
  dispParts(node.url, node.openFlag);
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
    src: imgPath + '/mainframe/remind.jpg',
    selectedSrc: imgPath + '/mainframe/remind_selected.jpg'
  },{
    src: imgPath + '/mainframe/send.jpg',
    selectedSrc: imgPath + '/mainframe/send_selected.jpg'
  },
  {
	src: imgPath + '/mainframe/recive.jpg',
	selectedSrc: imgPath + '/mainframe/recive_selected.jpg'
  },{
	src: imgPath + '/mainframe/share.jpg',
	selectedSrc: imgPath + '/mainframe/share_selected1.jpg'
   }
  ]
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
function initMenu(el) {
  var url = contextPath + '/yh/core/funcs/system/act/YHSystemAct/listMenu.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
		var menus = json.rtData.menu;
		var  str = [];
		for(var i = 0; i<menus.length;i++){
			str.push('<a id="'+menus[i].id+'" href="javascript:void(0)"');
			if(i == 0){
				str.push('class="ml10"');
			}
			if(i == menus.length - 1){
				str.push('class="br"');
			}
			str.push('><span>');
			str.push(json.rtData.menu[i].text);
			str.push('</span></a>');
		}
		$("#nmenu").html(str.join(""));
		
		$("#nmenu a").each(function(index){
			$(this).click(function(){
				if(YH.smenu){
					YH.smenu.removeClass("cur");
				}
				YH.smenu = $(this);
				YH.smenu.addClass("cur");
				var id = this.id;
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
						var s = [];
						for(var i = 0 ;i < menuData.length;i++){
							var cl = menuData[i].leaf == 0 ? "menu-close" : "";
							s.push('<li id="'+menuData[i].id+'" class="pmenu '+ cl +'" leaf="'+menuData[i].leaf+'" url="'+ menuData[i].url +
								'" openFlag="'+menuData[i].openFlag+'" ><a href="javascript:void(0)">');
							s.push("<table style='margin-left:50px'><tr><td style='padding-top:5px;'><img src='");
							s.push(contextPath + "/core/styles/imgs/menuIcon/" + menuData[i].icon);
							s.push("' width='20px' height='20px' /></td><td>");
							s.push(menuData[i].text);
							s.push("</td></tr></table></a></li>");
							if(menuData[i].leaf  == 0 && 
								menuData[i].children && menuData[i].children.length > 0){
								s.push('<li style="height: auto; display: none;"><ul class="menu-lv3">')
								for(var j = 0;j < menuData[i].children.length;j++){
									var c = menuData[i].children;
									s.push('<li  leaf="'+c[j].leaf+'" url="'+ c[j].url +
										'" openFlag="'+c[j].openFlag+'" ><a href="javascript:void(0)"><span>');
									s.push(c[j].text);
									s.push("</span></a></li>");
								}
								s.push('</ul></li>');
							}
						}
						$("#menu").html(s.join(""));
						$("#menu .pmenu").each(function(){
							$(this).click(function(){
								if(YH.cmenu){  
									if(YH.cmenu.attr("leaf") == 0){	 
										if(YH.cmenu.attr("id") == $(this).attr("id")){
											YH.cmenu.next().toggle("slow");
											return;
										}
										YH.cmenu.next().hide(true);
									}	
									YH.cmenu.removeClass("menu-selected");										
								}
								YH.cmenu = $(this);
								YH.cmenu.addClass("menu-selected"); 
								if($(this).attr("leaf") == 0){ 
									$(this).next().show(true);
									return;
								}else{
									if(YH.ccmenu){
										YH.ccmenu.removeClass("menu-selected");
										YH.ccmenu = null;
									} 
								}
								dispParts($(this).attr("url"), $(this).attr("openFlag"));
							});
						});
						$("#menu .menu-lv3 li").each(function(){
							$(this).click(function(){
								if(YH.ccmenu){
									YH.ccmenu.removeClass("menu-selected");
								} 
								YH.ccmenu = $(this);
								YH.ccmenu.addClass("menu-selected");
								dispParts($(this).attr("url"), $(this).attr("openFlag"));
							});
						})
					}
				  }
				});	
			});
			if(index == 0){
				$(this).click();
			}
		});
		
		/*
        new YH.Menu({
          id: "menu",
          classes: ['menu-lv1', 'menu-lv2', 'menu-lv3'],
          data: json.rtData.menu,
          el: el,
          activeMenu: userinfo.menuExpand,
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
		*/
      }
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
  $('#editInfoText').val(userinfo.myStatus);
  
  var src;
  if (/\.[a-z,A-Z]{1,}/.test(userinfo.avatar)) {
    src = contextPath + '/attachment/avatar/' + userinfo.avatar;
  }
  else {
    src = contextPath + '/core/styles/imgs/avatar/' + (userinfo.avatar || 1) + '.gif';
  }
  $('#rightBarUser').attr('src', src);
  
  try {
    $('#userStatusText').val(status[(userinfo.onStatus || 1) - 1].text);
    $('#rightBarStatus').attr('src', status[(userinfo.onStatus || 1) - 1].icon[userinfo.sex]);
  } catch (e) {
    $('#userStatusText').val(status[0].text);
    $('#rightBarStatus').attr('src', status[0].icon[0]);
  }
  
  var minutes = Math.floor((userinfo.onLine || 0) / 60);
  var hours = Math.floor(minutes / 60);
  minutes %= 60;
  
  var desc = '姓名:&nbsp;' + (userinfo.userName || '')
  + '<br>部门:&nbsp;' + (userinfo.deptName || '')
  + '<br>角色:&nbsp;' + (userinfo.privName || '')
  + '<br>在线时长:&nbsp;' + hours + "小时" + minutes + "分钟"
  + '<br><br>' + (userinfo.myStatus || '');

  if (!window.userTip) {
    window.userTip = new YH.Tip({
      'type': 'html',
      'event': 'mouseOver',
      'html': desc,
      'target': $('#username'),
      'style': {
        'height': 'auto',
        'width': '200px',
        'style': 'word-break:break-all;',
        'padding': '5px'
      }
    });
  }
  else {
    window.userTip.setHtml(desc);
  }
  //绑定回车事件和失去焦点事件
  userInfoEvent();
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
          $('#logo').find('img').attr("src", contextPath + "/yh/core/frame/act/YHClassicInterfaceAct/queryHeaderImg.act?para=" + Math.random());
          //$('#logo').find('img').attr("style","height:50px;width:300px;left:50px;position:absolute;");
        }
        //if (d.bannerText) {
        //  $('#logo').append(d.bannerText).attr("style", d.bannerFont).css({
          //  "padding-left": "0px",
        //    "line-height": "30px"
        //  });
        //}
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
  YH.editing = false;
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
		
		  $('#editInfoText').css("border","none");
		  $('#editInfoText').attr("readonly",false);
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
  $('#editInfoText').bind('click', function() {
	 $(this).attr("readonly",false);
     YH.editing = true;
  });
  $('#editInfoText').bind('mouseover', function() {
     $(this).css("border","1px solid #ccc");
  });
  $('#editInfoText').bind('mouseout', function() {
     if(!YH.editing) $(this).css("border","none");
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
      $('#rightBarStatus').attr('src', status[index - 1].icon[userinfo.sex]);
      $('#userStatusText').val(status[index - 1].text);
      statusTip.hide();
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
  $('#time').append(date.YYMMDD() + "&nbsp;&nbsp;&nbsp;");
  $('#time').append(date.solarDay2() + "&nbsp;&nbsp;&nbsp;");
  
  $('#time').append(date.weekday() + "&nbsp;&nbsp;&nbsp;");
  $('#time').append("<font id='st'></font>");  
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
	$('#time font').html(h + ":" + m);
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

function showWeather() {
  if (userinfo.SHOW_WEATHER) {
    var iframe = $('<iframe id="weather" style="" allowTransparency="true" frameborder="0"></iframe>').attr("src", contextPath +"/core/frame/2/weather.jsp");
    $('#wthAndTime').append(iframe);
  }
  else {
    $('#wthAndTime').addClass('timeonly');
  }
}
