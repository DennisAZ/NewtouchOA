(function($) {
  var opts = {
    layout: 'autolayout',
    //通过left/right/top/bottom定位(解决兼容性问题!),相对于offsetParent
    location: false,
    /*
    location: {
      left: 0,
      right: 0,
      top: 100,
      buttom: 100
    },
    */
    layoutCfg: {},
    items: [],
    height: '100%',
    width: '100%',
    iframeSrc: null,
    plugins: [],
    contentEl: null
  };
  
  YH.createComponent('Container', opts, YH.Component.prototype, {
    initComponent: function() {
      this.locate();
      this.el.css({
        width: this.width,
        height: this.height,
        overflow: 'hidden'
      });
      
      if (this.contentEl) {
        this.contentEl = $(this.contentEl).show();
      }
      else {
        this.contentEl = $('<div class="jq-container"></div>');
      }
      this.el.append(this.contentEl);
      //Container的几种内容
      if (this.html) {
        this.contentEl.html(this.html);
      }
      else if (this.url) {
        this.autoLoad();
      }
      else if (this.iframeSrc) {
        this.loadIframe();
      }
      else if (this.items) {
        this.doLayout();
      }
      this.doPlugins();
    },
    /**
     * 以AJAX的方式将目标页面载入到容器中
     */
    autoLoad: function() {
      if (this.contentEl[0]) {
        new Ajax.Updater(this.contentEl[0], this.url, {
          parameters: this.param,
          evalScripts: true
        });
      }
    },
    /**
     * 根据设置的top/bottom/left/right控制控件高宽
     */
    locate: function() {
      if (this.location && $.browser.msie && /[7,6]/.test($.browser.version)) {
        //ff中局部函数要在调用前声明,否则会出错
        function parseNumber(n) {
          n = parseInt(n);
          return isNaN(n) ? false : n;
        }
        
        this.el.css({
          'position': 'absolute'
        });
        
        //this.location忘记了this导致页面呢跳转了!!
        (typeof this.location == 'object') || (this.location = {});
        var op = this.el.offsetParent();
        this.location.top = parseNumber(this.location.top || this.el.css('top'));
        this.location.bottom = parseNumber(this.location.bottom || this.el.css('bottom'));
        this.location.left = parseNumber(this.location.left || this.el.css('left'));
        this.location.right = parseNumber(this.location.right || this.el.css('right'));
        
        if (this.location.top !== 'false' && this.location.bottom !== 'false') {
          var height = op.height() - this.location.top - this.location.bottom;
          this.height = isNaN(height) ? this.height : height;
        }
        if (this.location.left !== 'false' && this.location.right !== 'false') {
          var width = op.width() - this.location.left - this.location.right;
          this.width = isNaN(width) ? this.width : width;
        }
        this.el.css($.grep(this.location, function(k, v) {
          return v === 'false'
        }));
        var self = this;
        //当窗口resize时重新计算高宽
        function resizeEvt() {
          if (self.location.top !== 'false' && self.location.bottom !== 'false') {
            self.height = op.height() - self.location.top - self.location.bottom;
          }
          if (self.location.left !== 'false' && self.location.right !== 'false') {
            self.width = op.width() - self.location.left - self.location.right;
          }
          
          var css = {};
          isNaN(self.height) || (css.height = self.height);
          isNaN(self.width) || (css.width = self.width);
          
          self.el.css(css);
        }
        $(window).resize(resizeEvt);
      }
    },
    /**
     * 加载iframe
     */
    loadIframe: function(iframeSrc) {
      this.iframeSrc = iframeSrc || this.iframeSrc;
      var contentEl = this.contentEl;
      this.iframe = $('<iframe border="0" frameborder="0" cellspacing="0" src="about:blank" style="overflow:auto;width:100%;height:100%;border:none;background-color:transparent;" allowTransparency="true"></iframe>');
      this.iframe.attr('id', 'iframe' + this.id);
      this.iframe.attr('name', 'iframe' + this.id);
      this.contentEl.addClass('jq-container-iframe');
      this.iframeDom = this.iframe[0];
      this.iframeDom.src = this.iframeSrc;
      this.contentEl.append(this.iframe);
    },
    reloadIframe: function() {
      this.iframeDom.src = this.iframeSrc;
    },
    setSrc: function(src) {
      this.iframeSrc = src;
      this.iframeDom.src = src;
    },
    setHtml: function (html) {
      this.html = html;
      this.contentEl.html(this.html);
    },
    doLayout: function() {
      var self = this;
      if (this.layout) {
        //暂时的容错处理        if (YH.layouts.register[this.layout]) {
          if (!YH.isCmp(this.layoutMgr)) {
            this.layoutCfg.el = this.contentEl;
            this.layoutCfg.items = this.items;
            this.layoutCfg.owner = this;
            this.layoutMgr = new YH.layouts.register[this.layout](this.layoutCfg);
          }
 
          YH.compose(this, this.layoutMgr);
        }
      }
    },
    /**
     * 组件的插件(没用到)
     */
    doPlugins: function() {
      var t = this;
      $.each(this.plugins, function(i, e){
        if (YH.Plugins[e]) {
          YH.Plugins[e].apply(t, arguments);
        }
      });
    },
    compose: function() {
      var self = this;
      return {
        addItems: self.addItems,
        doSortable: self.doSortable,
        doLayout: self.doLayout,
        reloadIframe: self.reloadIframe,
        loadIframe: self.loadIframe,
        setSrc: self.setSrc,
        autoLoad: self.autoLoad,
        sortItems: self.sortItems,
        setHtml: self.setHtml
      }
    },
    destroy: function() {
      if (this.iframeDom) {
        this.iframeDom.src = 'about: blank';
      }
      YH.Component.prototype.destroy.call(this);
    }
  });
}) (jQuery);