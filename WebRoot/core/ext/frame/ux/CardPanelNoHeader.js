/**
 * 自定义的没有header的cardpanel组件
 * 通过tbar控制items的显示和隐藏,初步达到tabpanel的效果
 */
Ext.ux.CardPanelNoHeader = Ext.extend(Ext.Panel, {
  layout: 'card',
  activeItem: 0,
  setActiveTab: function(i){
    this.getLayout().setActiveItem(i);
  }
});

