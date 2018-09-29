var LeafMenu = function(cfg){
  this.data = cfg.data;
  this.target = cfg.target;
  this.cls = cfg.cls;
  this.selCls = cfg.selCls;
  this.setUrl = cfg.setUrl;
}

LeafMenu.prototype = {
  show: function(){
    var ul = new Element('ul',{
      'class': this.cls
    });
    
    if (!(this.data instanceof Array)){
      return;
    }
    
    this.data.each(function(e, i){
      
      var li = new Element('li',{
      });
      
      var a = new Element('a',{
        'href': 'javascript:void(0)'
      }).update(e.text);
      
      a.onclick = function(){
        if (window.lastClickLeaf){
          window.lastClickLeaf.removeClassName('mainframe-menu-selected');
        }
        
        dispParts(e.url);
        a.addClassName('mainframe-menu-selected');
        window.lastClickLeaf = a;
        
        return false;
      };
      
      ul.insert(li);
      li.insert(a);
    });
    $(this.target).innerHTML = '';  
    $(this.target).insert(ul);
    
    var center = new Element('center',{});
    
    var set = new Element('a',{
      'href': 'javascript:void(0)',
      'style': 'display:block;margin:10px 20px'
    }).update('设置');
    
    center.insert(set);
    
    var setUrl = this.setUrl;
    
    set.onclick = function(){
      dispParts(setUrl);
      return false;
    }
    
    $(this.target).insert(ul);
    $(this.target).insert(center);
  }
}