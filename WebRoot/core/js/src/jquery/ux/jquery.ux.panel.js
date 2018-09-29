(function($) {
  var panelOpts = {
    'extendContainer': true,
    'width': 300,
    'height': 300,
    'layout': 'autolayout',
    'layoutCfg': {},
    'icon': '',
    'tbar': [{id: 'close'}],
    'cls': '',
    'left': 0,
    containment: 'parent',
    'top': 0,
    autoWidth: false,
    'cmpCls': 'jq-panel',
    'renderTo': null,
    borderHeight: 50,
    'style': {},
    //float属性代表是否时浮动在别的元素只上,window/tip都是浮动的
    'float': false,
    'tools': [],
    activeCls: 'window-active',
    lazyContainer: false,
    'autoHideBorders': false,
    'autoHideToolbar': false,
    'isMaximize': false,
    'listeners': {
      resize: $.noop
    }
  };
  
  /**
   * 当窗口resize时触发设置最大化组件的大小的函数
   */
  $(window).resize(function() {
    $.each(YH.components, function(k, v) {
      if (v.status && v.status.fillCtn || v.status.maximize) {
        v.maxEvt && v.maxEvt();
      }
    });
  });
  
  YH.createComponent("Panel", panelOpts, YH.Component.prototype, {
    'initComponent': function() {
      this.el.addClass('extend-container');
      
      if (typeof this.height === "string" && /^[0-9]{1,}$/.test(this.height)) {
        this.height *= 1;
      }
      
      if (typeof this.width === "string" && /^[0-9]{1,}$/.test(this.width)) {
        this.width *= 1;
      }
      
      this.doRender();
      
      //解决ie6的select框问题

      if (YH.browser.isIE6 && this["float"]) {
        this.ie6Mask = $('<iframe border="0" frameborder="0" cellspacing="10" class="ie6mask" src="about:blank"></iframe>');
        var self = this;
        self.el.append(self.ie6Mask);
        setTimeout(function() {
          self.ie6Mask.css({
            height: self.el.innerHeight() - 12,
            width: self.el.innerWidth() - 12
          });
        }, 100);
      }
    },
    "refresh": function() {
      this.status.container = false;
      YH.Component.prototype.refresh.apply(this, arguments);
    },
    'doRender': function() {
      var css = {'height': 'auto'};
      
      if (!this.autoWidth) {
        css.width = this.width;
      }
      
      this.el.css(css);
      this.createHeader();
      this.createBody();
      this.lazyContainer || this.doContainer();
      this.createFooter();
      
      if (this.draggable) {
        this.clickActive();
      }
    },
    doInitStatus: function() {
      if (this.autoHideBorders) {
        this.setAutoHideBorders();
      }
      
      if (this.autoHideTools) {
        this.setAutoHideTools();
      }
      
      if (this.isPin) {
        this.pin();
        this.tools.pin && this.tools.pin.hide();
      }
      else {
        this.tools.unpin && this.tools.unpin.hide();
      }
      
      if (this.top || this.left || this.right || this.bottom) {
        this.posCenter = false;
      }
      
      if (this.posCenter) {
        var ctn;
        if (!this.containment || this.containment == 'parent') {
          ctn = (this.parentCmp || this.parentEl) ? 'parent' : null;
        }
        else {
          ctn = $(this.containment);
        }
        this.setPosCenter(ctn);
      }
      else {
        this.el.css({
          left: this.left,
          top: this.top,
          bottom: this.bottom,
          right: this.right
        });
      }
     
      //初始化最大化最小化填充满父元素和还原图标的显示状态

      if (!this.isFillCtn && !this.isMaximize) {
        this.tools.restore && this.tools.restore.hide();
      }
      else if (this.isFillCtn) {
        this.tools.plus && this.tools.plus.hide();
        this.maximize(this.containment);
      }
      else if (this.isMaximize) {
        this.tools.maximize && this.tools.maximize.hide();
        this.maximize();
      }
      
      this.doDbclick();
    },
    /**
     * 双击窗口标题栏的最大化的处理


     */
    doDbclick: function() {
      var self = this;
      if (this.tools.plus && this.tools.restore) {
        this.headerEl.dblclick(function() {
          if (self.status.maximize) {
            return;
          }
          if (self.status.fillCtn) {
            self.restore();
            self.tools.restore && self.tools.restore.hide();
            self.tools.plus && self.tools.plus.show();
          }
          else {
            self.maximize(self.containment);
            self.tools.restore && self.tools.restore.show();
            self.tools.plus && self.tools.plus.hide();
          }
        });
      }
      else if (this.tools.maximize) {
        this.headerEl.dblclick(function() {
          if (self.status.maximize) {
            self.restore();
          }
          else {
            self.maximize();
          }
        });
      }
    },
    'doDraggable': function() {
      if (this.draggable) {
        if (typeof this.draggable == 'boolean') {
          this.draggable = {};
        }
        //this.draggable.handle = this.draggable.handle || '.' + this.cmpCls + '-header';
        if (typeof this.draggable.containment === "undefined") {
          this.draggable.containment = 'body';
        }
      }
      YH.Component.prototype.doDraggable.call(this);
    },
    'clickActive': function() {
      this.status.clickActive = true;
      //当点击的时候,窗口变为激活状态


      var self = this;
      this.el.mousedown(function() {
        self.active();
      });
    },
    'doResizable': function() {
      var self = this;
      if (this.resizable) {
        if (!this.modal) {
          var mask = $(".draggable-mask");
          if (!mask.length) {
            mask = $("<div class=\"draggable-mask\"></div>")
            $("body").append(mask);
          }
          (typeof this.resizable === "object") || (this.resizable = {});
          this.resizable.start = function(e, ui) {
            mask.css({
              "z-index": self.zIndex
            });
            mask.show();
          }
        }
        this.resizable.alsoResize = this.resizable.alsoResize || this.contentEl;
        this.resizable.minWidth = this.resizable.minWidth || this.minWidth || 200;
        this.resizable.minHeight = this.resizable.minHeight || this.minHeight || 100;
        
        YH.Component.prototype.doResizable.call(this);
        
        //当resize后设置contentEl宽度,防止最大化还原后contentEl宽度超出
        this.el.resizable('option', {stop: function(e, ui) {
          self.contentEl.css({
            width: '100%'
          });
          self.height = self.contentEl.height();
          self.width =self.el.width();
          self.el.css({height: 'auto'});
          if (!self.modal) {
            $(".draggable-mask").hide();
          }
        }});
        
        this.el.css({
          'position': 'absolute'
        });
      }
    },
    'setPosCenter': function(ctn) {
      ctn = ctn || $('body');
      if (ctn == 'parent') {
        ctn = this.getParentEl();
      }
      ctn = $(ctn);
      
      //窗口居中暂时处理办法
      if (ctn[0] && ctn[0].tagName == 'BODY') {
        ctn = $('body');
      }
      YH.Panel.lastLeft = YH.Panel.lastLeft || 0;
      YH.Panel.lastTop = YH.Panel.lastTop || 0;
      var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      //暂时不处理窗口打开位置叠加的问题
      this.top = (ctn.height() - this.el.height()) / 2 + scrollTop + (YH.Panel.lastTop += 0);
      this.left = (ctn.width() - this.el.width()) / 2 + (YH.Panel.lastLeft += 0);
      this.top = this.top < 0 ? 0 : this.top;
      var pos = ctn.position();
      this.top += pos.top;
      this.left += pos.left;
      this.el.css({
        'top': this.top,
        'left': this.left
      });
    },
    'toggle': function(speed) {
      this.el.toggleClass(this.cmpCls + '-collapsed');
      this.contentEl.toggle(speed);
    },
    'createHeader': function() {
      this.headerEl = $('<div class="' + this.cmpCls + '-header"></div>');
      //拖动的class
      this.headerEl.addClass('drag-handle');
      this.initTools();
      var title = $('<span></span>');
      if (this.icon) {
        var img = $('<img></img>');
        img.attr('src', this.icon);
        img.attr('align', 'absmiddle');
        this.headerEl.append(img);
      }
      else if (this.iconCls) {
        var div = $('<div style="float: left;"></div>');
        div.addClass(this.iconCls);
        this.headerEl.append(div);
      }
      title.attr('class', this.cmpCls + '-header-text');
      title.append(this.title);
      this.headerEl.append(title);
      var tl = $('<div class="' + this.cmpCls + '-tl"></div>');
      var tr = $('<div class="' + this.cmpCls + '-tr"></div>');
      var tc = $('<div class="' + this.cmpCls + '-tc"></div>');
      tl.append(tr);
      tr.append(tc);
      tc.append(this.headerEl);
      this.el.append(tl);
    },
    'createBody': function() {
      this.contentEl = $('<div class="' + this.cmpCls + '-content"></div>');
      this.contentEl.css({
        height: this.height
      });
      
      var mc = $('<div class="' + this.cmpCls + '-mc"></div>');
      var ml = $('<div class="' + this.cmpCls + '-ml"></div>');
      var mr = $('<div class="' + this.cmpCls + '-mr"></div>');
      
      this.el.append(ml.append(mr.append(mc)));
      mc.append(this.contentEl);
    },
    doContainer: function(reload) {
      if (!this.status.container || reload) {
        var t = this;
        this.container = new YH.Container({
          renderTo: t.contentEl,
          items: t.items,
          url: t.url,
          html: t.html,
          loadCallback: t.loadCallback,
          iframeSrc: t.iframeSrc || t.src,
          layout: t.layout,
          param: t.param,
          owner: t,
          layoutCfg: t.layoutCfg,
          contentEl: t.content
        });
        YH.compose(this, this.container);
        this.status.container = true;
      }
    },
    'createFooter': function() {
      var bl = $('<div class="' + this.cmpCls + '-bl"></div>');
      var br = $('<div class="' + this.cmpCls + '-br"></div>');
      var bc = $('<div class="' + this.cmpCls + '-bc"></div>');
      var bcP = $('<div class="' + this.cmpCls + '-pointer-bc"></div>');
      bl.append(br);
      br.append(bc);
      this.el.append(bl);
      this.pointerBc && this.el.append(bcP);
    },
    'maximize': function(ctn) {
      var type = !!ctn ? 'fillCtn' : 'maximize';
      ctn = ctn || $('body');
      if (ctn == 'parent') {
        ctn = this.parentEl;
      }
      ctn = $(ctn);
      //从正常窗口到最大化时,记录正常位置,绑定window的resize事件
      if (!this.status.fillCtn && !this.status.maximize) {
        //在初始化完毕的状态下才记录模块原来的位置
        if (this.status.initialized) {
          this.left = this.el.position().left;
          this.top = this.el.position().top;
        }
        this.replacer = {};
        this.replacer.offset = this.el.offset();
        this.replacer.css = {
          height: this.contentEl.css('height'),
          width: this.el.css('width'),
          position: this.el.css('position')
        };
      }
      
      this.drag && this.drag.disable();
      this.resize && this.resize.disable();
      
      if (type == 'maximize') {
        this.layer = 'middle';
      }
      else {
        this.layer = 'lower';
      }
      //因为显示和隐藏需要修改status,不能直接改变show函数,需要专门增加效果函数

      !this.status.hidden && this.active();
      
      //当ctn是父元素的时候不用append
      //(this.el.parent()[0] != ctn[0]) && this.el.appendTo(ctn);
      this.maximize.height = ctn.height() - this.borderHeight || 50
      var css = ctn.position();
      
      var h;
      if (this.layout == "cardlayout" && this.layoutCfg.tabs) {
        //todo-处理tabpanel在fillCtn时的高度问题
        h = this.maximize.height;
      }
      else {
        h = this.maximize.height;
      }
      if (this.animate && !this.status.hidden) {
        var self = this;
        this.contentEl.find('.jq-container-iframe').hide();
        this.el.animate({
          width: ctn.width(),
          left: css.left
        }, 'fast', function() {
          self.contentEl.animate({
            height: h
          }, 'fast');
          self.el.animate({
            top: css.top
          }, 'fast', function() {
            self.contentEl.find('.jq-container-iframe').show();
          });
        });
      }
      else {
        this.contentEl.height(h);
        css.width = '100%';
        this.el.css(css);
      }
      this.status.fillCtn = this.status.maximize = false;
      this.status[type] = true;
    },
    getCrtHeight: function() {
      if (this.status.fillCtn || this.status.maximize) {
        return this.maximize.height;
      }
      return this.height;
    },
    'restore': function() {
      this.status.fillCtn = this.status.maximize = false;
      if (this.replacer) {
        this.el.css({
          height: 'auto',
          position: this.replacer.css.position
        });
        if (this.animate && !this.status.hidden) {
          var self = this;
          this.contentEl.find('.jq-container-iframe').hide();
          this.el.animate({
            top: this.top
          }, 'fast');
          this.contentEl.animate({
            height: this.replacer.css.height
          }, 'fast', function() {
            self.el.animate({
              left: self.left,
              width: self.replacer.css.width
            }, 'fast', function() {
              self.contentEl.find('.jq-container-iframe').show();
            });
          });
        }
        else {
          this.contentEl.css({
            height: this.replacer.css.height
          });
          this.el.css({left: this.left, top: this.top, width: this.replacer.css.width});
        }
        //当ctn是父元素的时候不用append
        (this.el.parent()[0] != $(this.parentEl)[0]) && this.el.appendTo(this.parentEl);
      }
      this.drag && this.drag.enable();
      this.drag && this.resize.enable();
      this.active('lower');
    },
    /**
     * 模块最大化后

     */
    maxEvt: function() {
      var ctn;
      if (this.status.fillCtn) {
        var ctn;
        if (!this.containment || this.containment == 'parent') {
          ctn = this.parentEl;
        }
        else {
          ctn = $(this.containment);
        }
      }
      else if (this.status.maximize) {
        ctn = $('body')
      }
      else {
        return;
      }
      this.el.width(ctn.width());
      this.maximize.height = ctn.height() - this.borderHeight || 50;
      this.contentEl.height(this.maximize.height);
    },
    'minimize': function() {
      this.hide();
    },
    'pin': function() {
      this.active('upper');
    },
    'unpin': function() {
      this.active('lower');
    },
    /**
     * 为了兼容目前桌面模块的实现方法,以后再用更好的处理

     */
    'autoLoad': function() {
    },
    'getAbsContentHeight': function() {
      return this.contentEl.height() + 'px';
    },
    'getTitle': function() {
      return this.title || '';
    },
    'setTitle': function(title) {
      this.title = title || '';
      this.headerEl.find('span').html(this.title);
    },
    'setWidth': function(width) {
      this.width = width;
      this.el.css({
        'width': this.width
      });
    },
    'setHeight': function(height) {
      this.height = height;
      this.contentEl.css({
        'height': this.height
      });
    },
    'scrollIn': function(speed, callback) {
      var top = (this.status.fillCtn || this.status.maximize) ? 0 : this.top;
      this.el.css({
        top: (document.documentElement || document.body).clientHeight + 'px'
      });
      this.el.show();
      this.el.animate({
        top: top
      }, speed || 'slow', function() {
        callback && callback();
      });
    },
    'scrollOut': function(speed, callback) {
      var self = this;
      var top = (this.status.fillCtn || this.status.maximize) ? 0 : this.top;
      this.el.animate({
        top: (document.documentElement || document.body).clientHeight + 'px'
      }, speed || 'slow', function() {
        self.el.hide();
        self.el.css({
          top: top
        });
        callback && callback();
      });
    },
    collapse: function(speed, callback) {
      var self = this;
      var top = this.getPosY();
      var height = this.getCrtHeight();
      //这里的stop很重要,避免了collapse和enpand同时运行产生显示不正常的问题
      this.el.stop().animate({
        top: parseInt(top + this.getCrtHeight()) / 2
      }, speed);
      this.contentEl.stop().animate({
        height: 0
      }, speed, function() {
        self.el.css({
          display: 'none'
        });
        self.contentEl.css({
          height: height
        });
        self.el.css({top: top});
        callback && callback();
      });
      this.contentEl.find('.jq-container-iframe').hide();
    },
    expand: function(speed, callback) {
      var self = this;
      var height = this.getCrtHeight();
      this.contentEl.css({
        height: 0
      });
      this.el.show();
      var top = this.getPosY();
      this.el.css({top: top + parseInt(this.getCrtHeight()) / 2});
      this.el.stop().animate({
        top: top
      }, speed, function() {
      });
      this.contentEl.stop().animate({
        height: height
      }, speed, function() {
        self.doContainer();
        self.contentEl.find('.jq-container-iframe').show();
        callback && callback()
      });
    },
    /**
     * 关闭函数先hide再destroy
     */
    'close': function(speed) {
      var self = this;
      
      //为了处理iframe中的页面存在询问是否确定离开的情况
      if (this.container && this.container.iframeDom) {
        var button = document.getElementById("buttonId");
        function unload() {
          //避免ie9下出现问题
          setTimeout(function() {
            self.hide(speed, function() {
              self.destroy();
            });
          }, 0);
        }
        //使用try,解决跨域的问题

        try {
          var conWin = this.container.iframeDom.contentWindow;
          if (conWin && this.iframeUnload) {
            //再一次点关闭的时候不重复注册事件
          }
          else if (conWin.addEventListener) {
            conWin.addEventListener("unload", unload, false);
          }
          else if (conWin.attachEvent) {
            conWin.attachEvent("onunload", unload);
          }
          else {
            //当页面不能注册unload事件时,直接关闭页面
            this.hide(speed, function() {
              self.destroy();
            });
          }
        } catch (e) {
          //跨域的情况

          unload();
        } finally {
          
        }
        this.container.iframeDom.src = 'about:blank';
        this.iframeUnload = true;
      }
      else {
        this.hide(speed, function() {
          self.destroy();
        });
      }
    },
    'initTools': function() {
      var tools = this.tbar || [];
      var t = this;
      t.tools = {};
      $.each(tools, function(i, e){
        if (YH.Tools[e.id]) {
          t.tools[e.id] = {
            el: $('<a href="javascript:void(0)">&nbsp;</a>'),
            show: function() {
              this.el.show();
            },
            hide: function() {
              this.el.hide();
            },
            initTool: function() {
              t.headerEl.append(this.el);
              this.el.attr('class', 'jq-tool jq-tool-' + e.id);
              this.el.attr('title', YH.Tools[e.id].title);
              if (e.hidden) {
                this.hide();
              }
              this.el.bind('click', function(){
                if (!e.preventDefault) {
                  YH.Tools[e.id].defaultHandler(e, t.tools[e.id].el, t, e.params);
                }
                if (e.handler){
                  e.handler(e, t.tools[e.id].el, t, e.params);
                }
                //阻止默认的动作
                return false;
              });
            }
          };
          t.tools[e.id].initTool();
        }
      });
    },
    'active': function(layer) {
      this.layer = layer || this.layer;
      this.zIndex = YH.topZIndex(this.layer);
      this.el.css({
        'z-index': this.zIndex
      });
      this.addClass(this.activeCls);
      this.status.hidden && this.show();
    },
    deactive: function() {
      this.removeClass(this.activeCls);
    },
    'hide': function(speed, callback) {
      if (this.status.hidden) {
        callback && callback();
        return;
      }
      var self = this;
      this.trayBtn && this.trayBtn.disable();
      this.hideEffect(speed, function() {
        self.trayBtn && self.trayBtn.enable();
        callback && callback();
      });
      this.status.hidden = true;
    },
    'show': function(speed, callback) {
      if (this.status.hidden) {
        this.status.hidden = false;
        var self = this;
        this.trayBtn && this.trayBtn.disable();
        this.showEffect(speed, function() {
          self.trayBtn && self.trayBtn.enable();
          callback && callback()
        });
      }
    },
    /**
     * 设置自动隐藏工具栏

     */
    'setAutoHideTools': function() {
      var self = this;
      this.headerEl.hide();
      this.el.hover(function() {
        self.headerEl.show();
      }, function() {
        self.headerEl.hide();
      });
    },
    /**
     * 设置自动隐藏边框
     */
    'setAutoHideBorders': function() {
      this.el.addClass('window-tsp');
      this.el.hover(function() {
        $(this).removeClass('window-tsp');
      }, function() {
        $(this).addClass('window-tsp');
      });
    },
    /**
     * 重写容器的销毁函数

     */
    destroy: function() {
      //如果存在repalcer就先销毁replacer
      //(this.status.fillCtn || this.status.maximize) && $(window).unbind('resize', this.maxEvt);
      //调用通用的销毁函数
      if (this.container && this.container.iframeDom) {
        this.container.iframeDom.src = 'about:blank';
      }
      YH.Component.prototype.destroy.call(this);
    }
  });
})(jQuery);
