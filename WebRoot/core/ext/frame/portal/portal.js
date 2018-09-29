//屏蔽错误事件
window.onerror = function () {
  return true;
}

var module = new Object();

function addModule( key, value){
  module[key] = value;
}

function cfgModule(cfg){
  var ns = {
    pageInfo: {
      totalRecord: cfg.records,
      totalPage: Math.ceil(cfg.records / cfg.lines),
      pageIndex: 0,
      startIndex: 0,
      endIndex: cfg.lines - 1,
      recordCnt: cfg.lines,
      pageSize: cfg.lines
    },
    showPage: cfg.showPage,
    initPage: cfg.initPage,
    maxPage: cfg.maxPage
  };
  addModule(idMap[cfg.name], ns);
  pageModule(idMap[cfg.name]);
}

var idMap = {};

function pageModule(key){
  var cmp = Ext.getCmp(key);
  if (cmp){
    cmp.tools['up'].hide();
    cmp.tools['down'].hide();
    if (cmp.scroll == '0'){
      if (module[key]){
        var obj = module[key];
        if (obj.pageInfo.totalPage <= obj.pageInfo.pageIndex + 1){
          cmp.tools['down'].hide();
        }
        else{
          cmp.tools['down'].show();
        }
      }
    }
  }
}

Ext.onReady(function(){
  
  Ext.Component.prototype.stateful = true;
  Ext.state.Manager.setProvider(
    new Ext.state.CookieProvider({
     expires: new Date(new Date().getTime()+(1000*60*60*24*365)),
     path: contextPath + "/core/ext/frame/portal/"
    })
  );
  
  var viewport = new Ext.Viewport({
    layout: 'border',
    items:[{
      xtype: 'portal',
      id: 'mainContainer',
      bodyStyle: 'overflow-x:hidden;overflow-y:auto;padding:0px 10px 0px 0px;',
      region: 'center',
      layout: 'column',
      margins: '0 0 0 0',
      listeners: {
        'drop': savePosition,
        'afterrender': function(){
          Ext.Ajax.request({
            url: contextPath + '/yh/core/funcs/setdescktop/setports/act/YHMytableAct/getDesktopPorts.act',
            success: addDesktopPorts,
            failure: function(){
            }
          });
        }
      }
    }]
  }); 
  
  //定义SliderTip 用于显示像素值
  var tip = new Ext.ux.SliderTip({
    getText: function(slider){
      return String.format('<b>{0}px</b>', slider.getValue());
    }
  });
  
  var gearTool = {
    id: 'gear',
    handler: gearModule
  };
  
  var upTool = {
    id: 'up',
    hidden: true,
    handler: function(e, target, panel){
      var obj = module[panel.getId()];
      if (obj.pageInfo.pageIndex > 0){
        obj.showPage(--obj.pageInfo.pageIndex);
        
        if (obj.pageInfo.pageIndex <= 0){
          this.hide();
        }
        if (obj.pageInfo.pageIndex + 1 < obj.pageInfo.totalPage){
          panel.tools['down'].show();
        }
      }
    }
  };
  
  var downTool = {
    id: 'down',
    hidden: true,
    handler: function(e, target, panel){
      var obj = module[panel.getId()];
      if (obj.pageInfo.pageIndex + 1 < obj.pageInfo.totalPage){
        obj.showPage(++obj.pageInfo.pageIndex);
        if (obj.pageInfo.pageIndex + 1 >= obj.pageInfo.totalPage){
          this.hide();
        }
        if (obj.pageInfo.pageIndex > 0){
          panel.tools['up'].show();
        }
      }
    }
  };
  
  var closeTool = {
      id:'close',
      handler: function(e, target, panel){
        if (confirm('确认不显示该模块么?')){
          panel.ownerCt.remove(panel, true);
        }
        /*
        Ext.Msg.confirm('提示','确认不显示该模块么?',function(b){
          if (b == 'yes'){
            panel.ownerCt.remove(panel, true);
          }
        });
        */
      }
    }
  
  var tools = [upTool,downTool,gearTool];
  
  var columnWidth;
  
  function addDesktopPorts(o){
    var rtJson = Ext.decode(o.responseText);

    if(rtJson.rtState == '0'){
      Ext.MessageBox.progress("桌面载入中","请稍等...","正在载入桌面项");
      //是否允许用户做调整的属性
      var properties = rtJson.rtData.properties;

      if (!(properties.WIDTH | properties.LINES | properties.SCROLL)){
        tools.remove(gearTool);
      }

      if (properties.WIDTH * 1){
        Ext.getCmp('globeCfgFd').add(leftWidth);
      }

      if (properties.LINES * 1){
        Ext.getCmp('moduleCfgFd').add(moduleLines);
      }

      if (properties.SCROLL * 1){
        Ext.getCmp('moduleCfgFd').add(scrollCb);
      }

      var mytablePath = contextPath + '/yh/core/funcs/setdescktop/setports/act/YHMytableAct/viewDesktopPort.act';

      var mainContainer = Ext.getCmp('mainContainer');

      columnWidth = Ext.state.Manager.get(userName + 'WIDTH') || rtJson.rtData.leftWidth;

      var moduleState = {};

      mainContainer.add({
        id: 'leftContainer',
        columnWidth: columnWidth,
        style: 'padding:10px 10px 10px 10px'
      },{
        id: 'rightContainer',
        columnWidth: 1 - columnWidth*1,
        style: 'padding:10px 0 10px 10px'
      });

      var leftContainer = Ext.getCmp('leftContainer');
      var rightContainer = Ext.getCmp('rightContainer');

      var total = rtJson.rtData.total;

      var portal = new Array();
      
      Ext.each(rtJson.rtData.records,function(e,i){
        var file = e.moduleFile;
        var id = 'm' + e.seqId;
        var state = Ext.state.Manager.get(userName + 'M-PROPERTY-' + id) || {};
        var title = e.moduleFile.replace(/.jsp/, "");
        
        idMap[title] = id;
        
        var portlet = new Ext.ux.Portlet({
          title: '&nbsp;<b>' + title + '</b>',
          id: id,
          seqId: e.seqId,
          lines: state.lines || e.moduleLines,
          scroll: state.scroll || e.moduleScroll,
          autoHeight: true,
          cls: 'yh-panel',
          headerCfg: {
            style: 'margin:10px 0px;'
          },
          layout: 'fit',
          tools: e.viewType == '2' ? tools : tools.concat(closeTool),
          draggable: properties.POS == '1',
          collapsible: properties.EXPAND == '1',
          autoLoad: {
            url: mytablePath,
            method: 'post',
            params: {
              file: file,
              scroll: state.scroll || e.moduleScroll,
              lines: state.lines || e.moduleLines
            },
            scripts: true,
            title: id,
            callback: function(el,s,r,o){
              if (o.params.scroll == '1'){
                Ext.getCmp(o.title).tools['up'].hide();
                Ext.getCmp(o.title).tools['down'].hide();
              }
            }
          },
          listeners: {
            'destroy': savePosition
          }
        });
        if (e.modulePos == 'r'){
          rightContainer.add(portlet);
        }
        else{
          leftContainer.add(portlet);
        }
        Ext.MessageBox.updateProgress(i + 1 / total);
        mainContainer.doLayout();
      });
      
      moduleState['cookie-left-width'] = 0.5 || rtJson.rtData.leftWidth;

      //mainContainer.doLayout();
    }
    Ext.MessageBox.hide();
  }
  
  var scrollStore = new Ext.data.Store({
    proxy:new Ext.data.MemoryProxy([[1,'1','是'],[2,'0','否']]),
    reader:new Ext.data.ArrayReader({id:0},[{name:'value',mapping:1},{name:'text',mapping:2}])
  });
  scrollStore.load();
  
  var scrollCb = new Ext.form.ComboBox({
    name: 'scroll',
    hiddenName: 'scroll',
    fieldLabel: '列表上下滚动显示',
    editable: false,
    triggerAction: 'all',
    mode: 'local',
    anchor: '95%',
    store: scrollStore,
    displayField: 'text',
    valueField: 'value',
    value: '1',
    editable: false,
    allowBlank: false
  })
  
  //自定义验证1-99
  Ext.apply(Ext.form.VTypes, {
    hundred: function(val, field){
      return (/^[1-9]{1}[0-9]{0,1}$/).exec(val);
    }
  });
  
  
  var moduleLines = new Ext.form.NumberField({
    xtype: 'numberfield',
    name: 'lines',
    fieldLabel: '显示行数',
    anchor: '95%', 
    allowBlank: false,
    vtype: 'hundred',
    vtypeText: '请输入1-99之间整数'
  });
  
  var leftWidth = new Ext.form.NumberField({
    xtype: 'numberfield',
    name: 'leftWidth',
    fieldLabel: '左侧栏目宽度(%)',
    anchor: '95%', 
    allowBlank: false,
    vtype: 'hundred',
    vtypeText: '请输入1-99之间整数'
  });
  
  var cfgPanel = new Ext.form.FormPanel({
    labelAlign: 'right', 
    frame: true,
    border: false, 
    autoHeight: true,
    labelWidth: 100,
    autoScroll: true,
    bodyStyle: 'padding:0 0 0 0',
    items: [{
      xtype: 'fieldset',
      title: '模块选项',
      id: 'moduleCfgFd',
      autoHeight: true,
      collapsible: true,
      items: []
    },{ 
      xtype: 'fieldset',
      title: '全局选项',
      id: 'globeCfgFd',
      autoHeight: true,
      collapsible: true,
      items: []
    }],
    buttons:[{ 
        text: '确定',
        handler: function(){
          if (cfgPanel.getForm().isValid()){
            var moduleState = {
              scroll: scrollCb.getValue(),
              lines: moduleLines.getValue()
            };
            Ext.state.Manager.set(userName + 'M-PROPERTY-' + moduleCfgId, moduleState);
            Ext.state.Manager.set(userName + 'WIDTH', leftWidth.getValue() / 100);
            
            cfgWin.hide();
            
            window.location.reload();
          }
        }       
    },{ 
      text: '取消',
      handler: function(){
        cfgWin.hide();
      } 
    }]
  });
  
  var cfgWin = new Ext.Window({
    modal: true,
    title: '设置',
    id: 'moduleWindow',
    closable: true,
    resizable: false,
    headerCfg: {
      style: 'margin:10px 0px;'
    },
    closeAction: 'hide',
    border: false,
    plain: false,
    width: 350,
    autoHeight: true,
    items: [
      cfgPanel
    ],
    listeners: {
      'show': function(){
        var state = Ext.state.Manager.get(userName + 'M-PROPERTY-' + moduleCfgId) || {};
        var portlet = Ext.getCmp(moduleCfgId);
        
        moduleLines.setValue(state.lines || portlet.lines);
        scrollCb.setValue(state.scroll || portlet.scroll);
        
        leftWidth.setValue(columnWidth * 100);
      }
    }
  });

  var moduleCfgId = "";
  
  function gearModule(e, target, panel){
    moduleCfgId = panel.getId();
    cfgWin.show();
  }
  
  //记录portlet位置
  function savePosition(){
    
    var tableLeft = "";
    var tableRight = "";
    var portal = viewport.getComponent(0);
    var items = portal.items;
    for(var i = 0; i < items.getCount(); i++){
      var c = items.get(i);  
      c.items.each(function(portlet){
        if (i){
          tableRight += portlet.seqId + ',';
        }
        else{
          tableLeft += portlet.seqId + ',';
        }
      });
    }

    var actionPath = contextPath + "/yh/core/funcs/setdescktop/setports/act/YHDesktopDefineAct/setDesktopPortal.act";
    var paras = "?tableLeft=" + tableLeft + "&tableRight=" + tableRight;
    try {
      var json = getJsonRs(actionPath + paras);
    } catch (e) {
      
    }
  }
  
});

