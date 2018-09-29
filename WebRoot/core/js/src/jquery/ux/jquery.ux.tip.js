(function ($) {
  var tipOpts = {
    'event': 'mouseOver',
    'html': '',
    'content': null,
    'delay': 5,
    relative: {
      x: 10,
      y: 10
    },
    'float': true,
    'items': null,
    'cmpCls': 'jq-tip',
    'extendContainer': true,
    'layout': 'autolayout',
    'layoutCfg': {},
    'icon': '',
    'cls': '',
    'renderTo': 'body',
    'style': {},
    'tools': []
  };
  
  YH.createComponent('Tip', tipOpts, YH.Panel.prototype, {
    'initComponent': function() {
      if (!this.target) {
        this.destroy();
        return;
      }
      this.target = $(this.target);

      YH.Panel.prototype.initComponent.apply(this, arguments);
      
      this.events[this.event].call(this);
    },
    doInitStatus: function() {
      this.el.css({
        position: 'absolute',
        'z-index': YH.topZIndex('upper'),
        display: 'none'
      });
      this.status.hidden = true;
    },
    /**
     * TIP触发显示的事件
     */
    'events': {
      /**
       * 鼠标移过时显示

       */
      'mouseOver': function() {
        var self = this;
        this.target.bind('mousemove', function(evt) {
          self.setPosition({
            top: evt.clientY + self.relative.y,
            left: evt.clientX + self.relative.x
          });
          self.el.css({
            display: 'block'
          });
        });
        
        this.target.bind('mouseout', function() {
          self.el.css({
            'display': 'none'
          });
        });
      },
      /**
       * 鼠标单击显示
       */
      'leftClick': function() {
        var self = this;
        this.target.bind('click', function(evt) {
          self.setPosition({
            'top': self.target.offset().top  + self.relative.y,
            'left': self.target.offset().left + self.target.width() + self.relative.x
          });
          self.show();
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
        });
        
        self.el.bind('mouseover', function() {
          clearTimeout(self.timer);
          return false;
        }).bind('mouseout', function() {
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
        });
      },
      /**
       * 鼠标右键单击显示
       */
      'rightClick': function() {
        var self = this;
        this.target.bind('contextmenu', function(e) {
          self.show();
          self.setPosition({
            left: e.clientX,
            top: e.clientY
          });
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
          return false;
        });

        self.el.bind('mouseover', function() {
          clearTimeout(self.timer);
        }).bind('mouseout', function() {
          clearTimeout(self.timer);
          self.timer = setTimeout(function() {
            self.hide();
          }, self.delay * 1000);
        });
      }
    },
    /**
     * 重写父类的设置位置方法, 解决了组件被部分遮挡的问题     * @param pos         位置信息
     */
    setPosition: function(pos) {
      var h = parseInt(this.getAbsHeight());
      var w = parseInt(this.getAbsWidth());
      var cw = parseInt((document.documentElement || document.body).clientWidth);
      var ch = parseInt((document.documentElement || document.body).clientHeight);
      var l = parseInt(pos.left);
      var t = parseInt(pos.top);
      if (l + w > cw - 5) {
        pos.left = l + (cw - 5 - l - w);
      }
      if (t + h > ch - 5) {
        pos.top = t - h;
      }
      this.el.css(pos);
    }
  });
})(jQuery);