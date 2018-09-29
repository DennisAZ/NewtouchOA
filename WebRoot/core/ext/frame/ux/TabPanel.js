Ext.namespace("Ext.ux");

/**
 * @class Ext.ux.TabPanel
 * @extends Ext.TabPanel
 * @author Lackhurt
 *
 */
 
Ext.ux.TabPanel = function(cfg) {
  if (cfg.tabPosition == 'right' || cfg.tabPosition == 'left') {
		cfg.tabAbout = cfg.tabPosition;
		cfg.tabPosition = 'top'
  }
  Ext.ux.TabPanel.superclass.constructor.call(this, cfg);
};

Ext.extend(Ext.ux.TabPanel, Ext.TabPanel, {
  initTab: function(item, index){
  	Ext.ux.TabPanel.superclass.initTab.call(this, item, index);
  	item.setHeight(this.height || this.container.getHeight());
  },
  adjustBodyWidth: function(w){
  	if (this.tabAbout){
		  if (this.header){
		  	this.header.setHeight(w);
	    }
	    if (this.footer){
	      this.footer.setHeight(w);
	    }
	    return w - 25;
	  }
	  else{
	  	return Ext.ux.TabPanel.superclass.adjustBodyWidth.call(this, w);
	  }
  },
  /*
  adjustBodyHeight : function(h){
    this.items.each(function(e,i){
      e.setHeight(h);
    });
    return this.height || this.container.getHeight();
  },
  */
  afterRender: function(){
    Ext.ux.TabPanel.superclass.afterRender.call(this);
    if (this.tabAbout){
    	var st = this[this.stripTarget];
    	if(Ext.isIE){
    	  st.dom.style.filter = 'progid:DXImageTransform.Microsoft.BasicImage(rotation=1)';
    		//st.dom.filters[0] = 'progid:DXImageTransform.Microsoft.BasicImage(rotation=1)';
    	  //st.dom.filters.item("DXImageTransform.Microsoft.BasicImage").rotation = 1;
    	}
      else{
	      st.applyStyles('-webkit-transform: rotate(90deg);-moz-transform: rotate(90deg);');
	    }
	    
      st.applyStyles('overflow-x:hidden;overflow-y:visible;');
      
      var el = Ext.fly(this.strip.id).dom.parentNode;
      el.style.overflow = 'visible';
      
      el = el.parentNode;
      el.style.overflow = 'visible';
      
      el = el.parentNode;
      el.style.overflow = 'visible';
      
      this.setHeight(this.height || this.container.getHeight());
      
      if (this.tabAbout == 'right'){
      	this.bwrap.applyStyles('position:absolute;top:0px;left:0px;');
      }
      else if (this.tabAbout == 'left'){
				this.bwrap.applyStyles('position:absolute;top:0px;left:25px;');
				el = Ext.fly(this.strip.id).dom.parentNode;
	      el.style.position = 'relative';
	      el.style.top = (this.width || this.container.getWidth() - 25) + 'px';
	    }
    }
  }
});