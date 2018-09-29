(function($) {
  var autoOpts = {
    el: $('<div></div>'),
    items: [],
    cmpCls: 'jq-autolayout'
  };
  
  YH.createComponent.call(YH.layouts, "AutoLayout", autoOpts, {
    initialize: function() {
      this.el.addClass(this.cmpCls);
      this.initComponent();
      this.style && this.el.css(this.style);
      //this.doDroppable();
    },
    initComponent: function() {
      this.renderItems();
      var self = this;
      $.each(this.items, function(i, e) {
        self.doItem(e);
      });
    },
    doDroppable: function(item) {
      if (this.droppable) {
        if (item) {
          var self = this;
          item.contentEl.droppable(this.droppable);
          item.contentEl.addClass(this.droppableCls);
          item.setEvtActor(item.contentEl);
          
          YH.Event.del(item, "drop");
          YH.Event.add(item, 'bind', 'drop', null, function(evt, ui, t) {
            if (ui.draggable.filter(self.droppable.accept)[0]) {
              self.droppable.dropStop && self.droppable.dropStop(evt, ui, t);
            }
          });
        }
        /*暂时不用
        var dropCtn = this.el.find('.' + this.droppableCls);
        if (!dropCtn[0] && this.el.filter('.' + this.droppableCls)) {
          dropCtn = this.el;
        }
        $(dropCtn).droppable(this.droppable);
        */
        
        //droppable的drop停止事件
        var drop = this.droppable.drop;
        this.droppable.drop = null;
        var self = this;
        $.each(this.droppableCmps, function(i, e) {
          e.contentEl.droppable(self.droppable);
          e.contentEl.addClass(self.droppableCls);
          e.setEvtActor(e.contentEl);
          YH.Event.add(e, 'bind', 'drop', null, function(evt, ui, t) {
            if (ui.draggable.filter(self.droppable.accept)[0]) {
              drop && drop(evt, ui, t);
            }
          });
        });
        /*
        //扩展droppable中的drop事件,增加控件的参数传递

        $(dropCtn).bind('drop', function(e, ui) {
          if (!cmp) {
            for(var e in self.items) {
              if (YH.isCmp(e) && e.contentEl == ui.draggable) {
                cmp = e;
                break;
              }
            }
          }
          alert(cmp)
          self.droppable.drop && self.droppable.drop(e, ui, cmp);
        });*/
      }
    },
    doSortable: function() {
      if (!this.sortable) {
        return;
      }
      var self = this;
      var cfg = {
        /**
         * 处理拖拽内容宽度为%的情况         */
        start: function(e, ui) {
          if (/%/.test(ui.item.css('width'))) {
            ui.item.data('sortWidth', ui.item.css('width'));
            ui.item.css({
              'width': ui.placeholder.innerWidth()
            });
          
            ui.placeholder.css({
              'height': ui.item.innerHeight()
            });
            ui.item.addClass('ui-draggable-dragging');
          }
        },
        stop: function(e, ui) {
          if (ui.item.data('sortWidth')) {
            ui.item.removeData('sortWidth');
            ui.item.css({
              'width': ui.item.data('sortWidth')
            });
            ui.item.removeClass('ui-draggable-dragging');
          }
          //排序结束的触发事件
          self.listeners && self.listeners.sortStop && self.listeners.sortStop(e, ui);
        },
        containment: self.el,
        handle: '.drag-handle'
      };
      $.extend(cfg, self.sortable);
      
      $.each(this.sortableCmps, function(i, e) {
        e.contentEl.addClass(self.sortableCls);
      });
      
      if (self.sortConnect) {
        cfg.connectWith = self.el.find('.' + self.sortableCls);
      }
      
      this.el.find('.' + this.sortableCls).sortable(cfg);
      
      $.each(this.sortableCmps, function(i, e) {
        e.setEvtActor(e.contentEl);
        YH.Event.add(e, 'bind', 'sortremove', null, function(e, ui, cmp) {
          cmp.removeItem(YH.getCmp(ui.item.attr('id')), true);
        });
        
        YH.Event.add(e, 'bind', 'sortreceive', null, function(e, ui, cmp) {
          cmp.receiveItem(YH.getCmp(ui.item.attr('id')));
        });
        
        e.el.css({
          'overflow': 'visible'
        });
      });
      self.el.css({
        'overflow-y': 'hidden',
        'overflow-x': 'hidden'
      });
    },
    doItem: function(e) {
      if (YH.isCmp(e)) {
        e.setStyle({
          width: '100%'
        });
      }
    },
    /**
     * 添加内部组件
     */
    addItems: function(items) {
      if (items instanceof Array) {
        var self = this;
        $.each(items, function(i, e) {
          self.addItems(e);
        });
      }
      else {
        items = this.renderItem(items);
        this.doItem(items);
        this.items.push(items);
      }
    },
    /**
     * 移除所有内部组件
     */
    removeItems: function() {
      var temp = this.items.slice();
      $.each(temp, function(i, e) {
        if (YH.isCmp(e)) {
          e.destroy();
        }
        else if (e.el) {
          e.el.remove();
        }
      });
      this.items.splice(0);
    },
    /**
     * 载入内部组件
     */
    renderItems: function(items) {
      var self = this;
      if (items instanceof Array) {
        this.removeItems();
      }
      else {
        items = this.items;
      }
      
      
      if ($.isArray(this.items) && !this.status.renderItems) {
        $.each(items, function(i, e) {
          self.items[i] = self.renderItem(e);
        });
        this.status.renderItems = true;
      }
    },
    renderItem: function(item, renderTo) {
      if (!item) {
        return;
      }
      if (!YH.isCmp(item) && !item.render) {
        if (!item.xtype && item.constructor == Object ) {
          item.xtype = 'container';
        }
        if (typeof item.xtype === 'string') {
          item = new YH.register[item.xtype](item);
        }
      }
      
      if (YH.isCmp(item)) {
        item.render(renderTo || this.el);
        item.parentCmp = this.owner;
      }
      else if (item.render){
        item.render(renderTo || this.el);
      }
      else {
        item.parentCmp = this.owner;
      }
      this.doItem(item);
      return item;
    },
    /**
     * 接收组件,组件已经存在并且已经在dom节点中插入到this.el
     */
    receiveItem: function(item) {
      item.parentCmp = this.owner;
      item.parentEl = this.el;
      this.items.push(item);
    },
    /**
     * 移除组件
     * @param item          [number/cmp]
     * @param flag          false  调用item的destroy方法
     *                      true   只在this.items中移除item
     */
    removeItem: function(item, flag) {
      var self = this;
      if (typeof item === 'number') {
        item = this.items[item];
        this.items.splice(item, 1);
      }
      else {
        $.each(this.items, function(i, e) {
          if (e == item) {
            self.items.splice(i, 1);
          }
        });
      }
      
      if (!flag) {
        if (YH.isCmp(item)) {
          item.destroy();
        }
        else if (item.el) {
          item.el.remove();
        }
      }
    },
    /**
     * 子控件按照dom中的顺序排序(仅在autolayout中排序才有意义)
     * @param deep        是否深度排序(是否子控件迭代排序)
     */
    sortItems: function(deep) {
      $.each(this.items, function(i, e) {
        e.sort = e.parentEl && e.parentEl.children().index(this.el) || 0;
        deep && e.items && e.sortItems && e.sortItems(deep);
      });
      this.items.sort(function(a, b) {
        return a.sort - b.sort;
      });
    },
    /**
     * 打开/关闭设计模式
     * @param flag        true或者不传递:打开设计模式
     *                    false:关闭设计模式
     */
    designLayout: function(flag) {
      if (flag === false) {
        //关闭设计模式,待实现
      }
      else {
        this.designMode = true;
        this.doDesign();
      }
    },
    doDesign: $.noop,
    /**
     * 把container的items处理移交到layout中     */
    compose: function() {
      var self = this;
      return {
        addItems: self.addItems,
        renderItems: self.renderItems,
        renderItem: self.renderItem,
        removeItem: self.removeItem,
        receiveItem: self.receiveItem,
        sortItems: self.sortItems,
        removeItems: self.removeItems,
        designLayout: self.designLayout
      }
    }
  });
}) (jQuery);