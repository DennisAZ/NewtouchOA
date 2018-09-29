/**
 * 自定义的cardpanel组件
 * 通过tbar控制items的显示和隐藏,初步达到tabpanel的效果
 */
Ext.ux.CardPanel = Ext.extend(Ext.Panel, {
  layout: 'card',
  activeItem: 0,
  constructor: function(config){
    Ext.apply(this, config);
    
    if (this.tbar){
      //this.tbar[0].text = '<b>' + this.tbar[0].text + '</b>';
    }
    
    Ext.ux.CardPanel.superclass.constructor.call(this, config);
    if (this.items){
      var t = this;
      
      var items = this.items;
      
      var toolbar = this.getTopToolbar();
      
      toolbar.get(0).text = '<b>' + toolbar.get(0).text + '</b>';
      
      var obj = {};
      obj.text = toolbar.get(0).text.replace("<b>","").replace("</b>","");
      
      Ext.each(toolbar, function(e,i,all){
        e.handler = function(){
          if (obj.btn){
            obj.btn.setText(obj.text);
          }
          else{
            t.getTopToolbar().get(0).setText(obj.text);
            toolbar[0].text = obj.text;
          }
          
          obj.btn = this;
          obj.text = e.text;
          
          this.setText('<b>' + e.text + '</b>');
          
          //因为在tbar中添加的'-'分割是tbar与items的index不对应
          if (items.getCount() > i / 2){
            t.getLayout().setActiveItem(i / 2);
          }
        };
      },this);
    }
  }
});

