$(function() {
  var bodyLayout = $('body').layout({
    'listeners': {
      'resize': function(){
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
  
  initMenu(".left-menu > div");
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
        $('#desktop').hide();
        $('#workspace').show();
        window['workspace'].location = encodeURI(url);
      }
    }
  }
}

/**
 * 初始化主菜单
 * @param el
 * @return
 */
function initMenu(el) {
  var data = [{
      seqId:84,
      expand:0,
      id:"02",
      text:"树形控件",
      leaf:0,
      children: [{
        text: "zTree",
        leaf: true,
        url:"ui/zTree/"
      }],
      openFlag:""
    },{
      seqId:71,
      expand:0,
      id:"04",
      text:"列表控件",
      leaf:0,
      url:"",
      openFlag:"",
      children: [{
        text: "jqGrid",
        leaf: true,
        url: "ui/jqGrid/"
      }]
    },{
      seqId:75,
      expand:0,
      id:"05",
      text:"布局控件",
      leaf:0,
      url:"",
      children: [{
        text: "borderlayout",
        leaf: true,
        url: "ui/layout/borderlayout"
      }, {
        text: "jqueryUI标签页",
        leaf: true,
        url: "ui/jqueryui/tabs.html"
      }],
      openFlag:""
    },{
      seqId:111,
      expand:0,
      id:"09",
      children: [{
        text: "jqueryUI对话框",
        leaf: true,
        url: "ui/jqueryui/dialog.html"
      },{
        text: "气泡",
        leaf: true
      }],
      text:"窗口",
      icon:"comm.gif",
      leaf:0,
      url:"",
      openFlag:""
    },{
      seqId:112,
      expand:0,
      id:"10",
      children: [{
        text: "表单验证",
        leaf: true,
        url: "ui/uniform/"
      }],
      text:"表单",
      icon:"hrms.gif",
      leaf:0,
      url:"",
      openFlag:""
    }, {
      seqId:241,
      expand:0,
      id:"20",
      children: [{
        text: "期末结转",
        leaf: true
      }],
      text:"期末处理",
      icon:"comm.gif",
      leaf:0,
      url:"",
      openFlag:""
    },{
      seqId:241,
      expand:0,
      id:"30",
      children: [{
        text: "表单验证",
        leaf: true,
        url: contextPath + "/uifrm/demo/uniform/"
      }, {
        text: "弹出窗口",
        leaf: true,
        url: contextPath + "/uifrm/demo/window/"
      }, {
        text: "列表",
        leaf: true,
        url: contextPath + "/uifrm/demo/jqGrid/"
      }],
      text:"DEMO",
      icon:"comm.gif",
      leaf:0,
      url:"",
      openFlag:""
    }]
  new YH.Menu({
    id: "menu",
    classes: ['menu-lv1', 'menu-lv2', 'menu-lv3'],
    data: data,
    openUrl: function (node) {
      dispParts(node.url, node.openFlag);
    },
    el: el,
    expandType: 0,
    //isLazyLoad: true,
    lazyLoadData: function (menu) {
      return {};
    },
    liClass: [null, 'menu-close', null],
    selClass: 'menu-selected',
    expClass: ['menu-selected', 'menu-expand']
  });
}