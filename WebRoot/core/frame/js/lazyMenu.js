function getLeftMenus() {
  var actionPath = contextPath + "/yh/core/funcs/system/act/YHSystemAct/listMenu.act";
  var json = getJsonRs(actionPath);
  window.menuJson = json.rtData.menu;
  window.expandType = json.rtData.expandType;
  return parseMenu(menuJson, 'mainframe-menu-lv1');
}

function parseMenu(json, cls){
  
  if (!(json instanceof Array)){
    return;
  }
  
  var ulL1 = new Element('ul',{
    'class': cls
  });
  
  json.each(function(e, i){
    var divL1;
    if (e.children){
      divL1 = new Element('a',{
        'id': 'm' + e.id,
        'href': 'javascript:void(0)',
        'class': 'mainframe-menu-close'
      }).update(e.text);
      divL1.onclick = function() {
        locate(cls, 'm' + e.id);
        return false;
      }
    }
    else{
      divL1 = new Element('a',{
        'id': 'm' + e.id,
        'href': 'javascript:void(0)'
      }).update(e.text);
      
      divL1.onclick = function(){
        locate(cls, 'm' + e.id, e.url, e.openFlag);
        return false;
      }
      
      if (e.expand){
        window.expandId = 'm' + e.id;
      }
    }
    
    var liL1 = new Element('li',{
    });
    
    liL1.insert(divL1);
    
    ulL1.insert(liL1);
    
    if (!e.leaf){
      if (e.children){
        
        var el = parseMenu(e.children, 'mainframe-menu-lv3');
        
        el.hide();
        
        ulL1.insert(el);
        
      }
      else{
        var liDiv = new Element('div',{
          'style': 'height:auto;display:none;'
        });
        
        ulL1.insert(liDiv);
      }
    }
  });
  
  return ulL1;
}

var menuState = {};
var lastExpand = "";

function locate(cls, id, url, openFlag){
  if (cls == 'mainframe-menu-lv1'){
    
    if (!menuState[id]){
      menuState[id] = {
        selected: false,
        children: false
      };
    }
    
    menuState[id].selected = !menuState[id].selected;
  
    var selected = window.menuState[id].selected;
    
    var el = $(id);
    
    if (selected){
      if (expandType){
        if (lastExpand && lastExpand != id){
          menuState[lastExpand].selected = false;
          $(lastExpand).removeClassName('mainframe-menu-selected');
          if ($(lastExpand).up('li',0).next()) {
            $(lastExpand).up('li',0).next().setStyle({'display':'none'});
          }
        }
        
        lastExpand = id;
      }
      
      el.addClassName('mainframe-menu-selected');
      
      if (el.up('li',0).next()){
        el.up('li',0).next().setStyle({'display':'block'});
      }
      else{
        return;
      }
      
      if (!menuState[id].children){
        try {
          var actionPath = contextPath + "/yh/core/funcs/system/act/YHSystemAct/lazyLoadMenu.act?parent=" + id.replace('m','');
          var json = getJsonRs(actionPath);
          if (json.rtState){
            menuState[id].children = true;
            var temp = parseMenu(json.rtData, 'mainframe-menu-lv2');
            el.up('li',0).next().update(temp);
          }
        } catch (e) {
          
        }
      }
    }
    else{
      el.removeClassName('mainframe-menu-selected');
      if (el.up('li',0).next()){
        el.up('li',0).next().setStyle({'display':'none'});
      }
      else{
        return;
      }
    }
  }
  else{
    
    var el = $(id);
    if (url){
      if (openFlag == '1'){
        window.open(url);
      }
      else{
        dispParts(url);
      }
    }
    else {
      if (el.hasClassName('mainframe-menu-expand')) {
        el.up('li').next('ul').hide();
        el.removeClassName('mainframe-menu-expand');
        el.addClassName('mainframe-menu-close');
      }
      else {
        el.up('li').next('ul').show();
        el.addClassName('mainframe-menu-expand');
        el.removeClassName('mainframe-menu-close');
      }
      return;
    }
    
    if (window.lastClickEl){
      lastClickEl.removeClassName('mainframe-menu-selected');
    }
    
    el.addClassName('mainframe-menu-selected');
    
    window.lastClickEl = el;
  }
}

