
Ext.ux.Window = Ext.extend(Ext.Window, {
  closeAction:'hide',
  isMaxSize:false,
  iframeId:this.id + '-iframe',
  panelId:this.id + 'panel',
  layout:'fit',
  constructor: function(config){
    Ext.apply(this, config);
    Ext.ux.Window.superclass.constructor.call(this, config);
    this.add({
      id:this.panelId,
      html:'<iframe id="' + this.iframeId + '" style="border:none;width:100%;height:100%;overflow:hidden;" src="' + this.src + '"></iframe>'
    });
  },
  refresh:function(){
    Ext.getDom(this.iframeId).src = this.src;
  },
  setSrc:function(src){
    this.src = src;
    Ext.getDom(this.iframeId).src = this.src;
  },
  getSrc:function(){
    return this.src;
  },
  getTitle:function(){
    return this.title;
  }
});

Ext.ux.Window.prototype.beforeDestroy = function(){
  Ext.destroy(
    this.focusEl,  // 新增
    this.bwrap, // 新增

    this.resizer,
    this.dd,
    this.proxy,
    this.mask
  );
  Ext.ux.Window.superclass.beforeDestroy.call(this);
  this.focusEl = null;  // 新增
  this.bwrap = null;  // 新增
  this.isMaxSize = null;
  this.iframeId = null;
  this.panelId = null;
}
