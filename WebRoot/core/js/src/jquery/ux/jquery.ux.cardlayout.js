(function($) {
  var opts = {
    el: $('<div></div>'),
    activeItem: 0,
    tabs: false,
    lazyLoad: false,
    fit: true,
    loadHandler: [],
    sp: '&nbsp;|&nbsp;'
  };
  YH.createComponent.call(YH.layouts, "CardLayout", opts, YH.layouts.AutoLayout.prototype, {
    initComponent: function() {
      var self = this;
      var activeItem = this.activeItem || 0;
      this.lastActive = this.activeItem;
      var lazyLoad = this.lazyLoad;
      var reload = this.reload;
      this.loadHandler = this.loadHandler || [];
      var loadHandler = this.loadHandler;
      
      if (this.items && this.items.length) {
        this.renderItems();
        $.each(this.items, function(i, e) {
          e.setStyle({
            'width': '100%'
          });
          if (self.fit) {
            e.setStyle({
              'height': '100%'
            });
          }
          if (activeItem != i) {
            e.hide();
          }
          else {
            if (lazyLoad) {
              if (loadHandler[i]) {
                loadHandler[i](e, i);
                eloaded = true;
              }
            }
          }
          
          if (!lazyLoad) {
            if (loadHandler[i]) {
              loadHandler[i](e, i);
            }
          }
        });
      }
      //兼容以前的cardlayout
      else {
        $.each(this.el.children(), function(i, e) {
          $(e).css({
            'width': '100%',
            'height': '100%'
          });
          if (activeItem != i) {
            $(e).hide();
          }
          else {
            if (lazyLoad) {
              if (loadHandler[i]) {
                loadHandler[i]($(e), i);
                $(e).data('loaded', true);
              }
            }
          }
          
          if (!lazyLoad) {
            if (loadHandler[i]) {
              loadHandler[i]($(e), i);
            }
          }
        });
      }
      this.doTabs();
    },
    /**
     * 处理cardlayout的标签问题
     */
    doTabs: function() {
      if (this.tabs) {
        var self = this;
        var tabHeader = new YH.Container({
          layout: 'floatlayout',
          height: 'auto',
          cls: 'tab-header',
          items: []
        });
        
        //判断是否是panel类的容器
        if (self.owner.owner) {
          //解决了tabpanel的内容高度显示不全的问题!!!
          self.owner.owner.tabHeader = tabHeader;
          self.owner.owner.contentEl.before(tabHeader.el);
        }
        else {
          self.owner.owner.tabHeader = tabHeader;
          self.owner.el.prepend(tabHeader.el);
        }
        if (this.items && this.items.length) {
          $.each(this.items, function(i, e) {
            if (e.tabBtn) {
              if (self.activeItem == i) {
                e.tabBtn.status = e.tabBtn.status || {};
                e.tabBtn.status.isPressed = true;
              }
              e.tabBtn.style = e.tabBtn.style || {};
              e.tabBtn.style["float"] = 'left';
              e.tabBtn.style.width = 'auto';
              e.tabBtn.handler = function() {
                if (self.items[self.lastActive].tabBtn) {
                  tabHeader.items[self.lastActive].setStatus('default');
                }
                if (self.items[i].tabBtn) {
                  tabHeader.items[i].setStatus('active');
                }
                self.setActiveItem(i);
              }
              tabHeader.addItems(self.items[i].tabBtn);
            }
            else {
              var a = $('<a href="javascript:void(0)"></a>');
              a.html(e.title || ('tab' + i));
              a.click(function() {
                self.setActiveItem(i)
              });
              tabHeader.el.append(a);
            }
          });
        }
      }
    },
    setActiveItem: function(i) {
      this.el.children().eq(this.lastActive).hide();
      this.el.children().eq(i).show();
      this.lastActive = i;
      
      var el = this.el.children().eq(i);
      if (this.lazyLoad && this.reload) {
        if (this.loadHandler[i]) {
          this.loadHandler[i](el, i);
        }
      }
      else if (this.lazyLoad){
        if (this.loadHandler[i] && !el.data('loaded')) {
          this.loadHandler[i](el, i);
          el.data('loaded', true)
        }
      }
    }
  });
})(jQuery);