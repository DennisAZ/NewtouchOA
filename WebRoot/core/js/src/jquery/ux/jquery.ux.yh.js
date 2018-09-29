(function($) {
  window.YH = {
    version: "2.1",
    /**
     * 注册组件类型,为了能够使用xtype快速初始化组件
     */
    register: {},
    /**
     * 是否为手持设备     */
    isTouchDevice: 'ontouchstart' in window,
    fitTouchDevice: function() {
      function simMouseEvt(type, target, pos) {
        var evt = document.createEvent('MouseEvents');
        evt.initMouseEvent(type, true, true, document, 
            0, pos.screenX, pos.screenY, pos.clientX, pos.clientY, 
            false, false, false, false, 
            0, target);
        return target.dispatchEvent(evt);
      }
      document.addEventListener("touchstart", function(e) {
        if (!simMouseEvt("mousedown", e.target, e.changedTouches[0])) {
          e.preventDefault();
          setTimeout(function() {
            if (e.target instanceof HTMLElement && !e.target.getAttribute("yhmovement")) {
              //simMouseEvt("mouseup", e.target, e.changedTouches[0]);
              simMouseEvt("click", e.target, e.changedTouches[0]);
            }
          }, 300);
        }
      });
      document.addEventListener("touchmove", function(e) {
        e.target instanceof HTMLElement && e.target.setAttribute("yhmovement", "move");
        simMouseEvt("mousemove", e.target, e.changedTouches[0]) || e.preventDefault();
      });
      document.addEventListener("touchend", function(e) {
        var timer;
        if (e.target instanceof HTMLElement) {
          if (e.target.getAttribute("yhdbclick")) {
            clearTimeout(timer);
            e.target.removeAttribute("yhdbclick");
            simMouseEvt("dblclick", e.target, e.changedTouches[0]);
          }
          else {
            e.target.setAttribute("yhdbclick", "dbclick");
            timer = setTimeout(function() {
              e.target.removeAttribute("yhdbclick");
            }, 1000);
          }
          e.target.removeAttribute("yhmovement");
        }
        simMouseEvt("mouseup", e.target, e.changedTouches[0]) || e.preventDefault();
      });
    },
    /**
     * 控制台     * 对浏览器控制台进行容错包装     * 替代出错时的alert,提升用户体验
     */
    console: {
      log: window.console && window.console.log || $.noop,
      info: window.console && window.console.info || $.noop,
      warn: window.console && window.console.warn || $.noop,
      error: window.console && window.console.error || $.noop,
      assert: window.console && window.console.assert || $.noop,
      dir: window.console && window.console.dir || $.noop,
      clear: window.console && window.console.clear || $.noop,
      profile: window.console && window.console.profile || $.noop,
      profileEnd: window.console && window.console.profileEnd || $.noop
    },
    /**
     * 解析JSON,支持畸形的JSON字符串     * @param text        支持畸形的JSON字符串     * @return            JSON对象
     */
    parseJson: function (text) {
      try {
        return eval("(" + text+ ")");
      } catch (e) {
        return {};
      }
    },
    /**
     * 浏览器信息     */
    browser: {
      isIE6: $.browser.msie && /^6./.test($.browser.version),
      isIE7: $.browser.msie && /^7./.test($.browser.version)
    },
    /**
     * 设置所有的动画效果开关     * @param open          boolean
     */
    fx: function(open) {
      $.fx.off = !open;
    },
    /**
     * 规格化JSON
     * @param t             规格化的模板JSON
     *                      支持正则表达式!
     * @param o             需要被规格化的JSON
     * @return              规格化后的数据     */
    formatJson: function (t, o) {
      if (!o || !t) {
        return;
      }
      var r = {};
      for (var k in t) {
        var v = t[k];
        //当属性是数组的时候递归调用规格化函数
        if (v && v instanceof RegExp) {
          if (o[k] && v.test(o[k])) {
            o[k] && (r[k] = o[k]);
          }
          else {
            return;
          }
        }
        else if (v && v instanceof Array) {
          if (!o[k]) {
            continue;
          }
          r[k] = [];
          $.each(o[k], function(i, e) {
            r[k].push(YH.formatJson(t, e));
          });
          r[k] = $.grep(r[k], function(e, i) {
            return !!e;
          });
        }
        else if (v && typeof v === "object") {
          o[k] && (r[k] = YH.formatJson(v, o[k]));
        }
        else if (v === "*") {
          r[k] = o[k];
        }
        else if (v === "not") {
          
        }
        else {
          o[k] && (r[k] = o[k]);
        }
      }
      return r;
    },
    /**
     * 注册组件
     */
    components: {},
    /**
     * 布局
     */
    layouts: {
      register: {}
    },
    /**
     * 通过id获取组件
     * @param id
     * @return            组件
     */
    getCmp: function(id) {
      return YH.components[id];
    },
    /**
     * 创建组件时自动调用的方法
     */
    addCmp: function(id, cmp) {
      if (typeof id == "string" || typeof id == "number"){
        YH.components[id] = cmp;
      }
    },
    /**
     * 销毁组件时自动调用的方法     */
    removeCmp: function(id) {
      delete YH.components[id];
    },
    /**
     * 自动生成的ID,当用户没有定义控件的ID时使用自动生成的ID
     * @return autoID
     */
    getAutoID: function(prefix) {
      YH.autoID = YH.autoID || 0;
      YH.autoID++;
      return (prefix || "yhAuto") + YH.autoID;
    },
    /**
     * json对象转化为String
     * @param o          JSON对象,对象属性可以是函数
     * @return             JSON字符串
     */
    jsonToString: function(o){
      var self = this;
      switch(typeof(o)){
        case "string":
            return '"' + o.replace(/(["\\])/g, "\\$1") + '"';
        case "function":
            return o;
        case "array":
            return "[" + o.map(self.jsonToString).join(",") + "]";
        case "object":
            if(o instanceof Array){
              var strArr = [];
              var len = o.length;
              for(var i = 0; i < len; i++){
                  strArr.push(self.jsonToString(o[i]));
              }
              return "[" + strArr.join(",") + "]";
            }
            else if(o == null){
              return "null";
            }
            else{
              var string = [];
              for (var k in o) {
                string.push(self.jsonToString(k) + ":" + self.jsonToString(o[k]));
              }
              return "{" + string.join(",") + "}";
            }
        case "number":
            return o;
        case false:
            return o;
        default:
            return o;
      }
    },
    /**
     * 向元素中添加属性     * @param el          [html元素,jquery元素,css选择器]
     * @param pros        属性的哈希表{"height": "100px", "src": "http://..."}
     */
    addPros: function(el, pros) {
      var el = $(el);
      if (!el[0] || !pros) {
        return false;
      }
      var tagName = el[0].tagName;
      
      //定义dom元素合法的属性
      var elPros = {
        "BODY": ["style"],
        "DIV": ["style", "onclick"],
        "SPAN": ["style"],
        "A": ["href", "style", "target", "onclick"],
        "IMG": ["style", "src"]
      };
      
      $.each(pros, function(k, v) {
        if ($.inArray(k, elPros[tagName] || []) >= 0) {
          el.attr(k, v);
        }
      });
    },
    /**
     * 组合函数
     */
    compose: function(cmp, subcmp) {
      var o = {};
      subcmp.compose() && $.each(subcmp.compose(), function(k, v) {
        if (typeof v === "function") {
          o[k] = function() {
            v.apply(subcmp, arguments);
          }
        }
      });
      $.extend(cmp, o);
    },
    /**
     * 判断是否为YH组件
     */
    isCmp: function(o) {
      return !!(YH.register[o && o.xtype] && o.status && o.render);
    },
    /**
     * 创建组件的函数[Panel, Window, Grid, ImgBox, Container...]
     * *****************重要方法*******************************
     * @arguments[0]          类型["Panel", "Window"...]
     * @arguments[1]          默认构造参数
     * @arguments[2~N]        组件的方法或公用数据 
     */
    createComponent: function() {
      var type = arguments[0], opts = arguments[1] || {}, i = 2, length = arguments.length;
      if (length > 0) {
        if (typeof name === "string") {
          this[type] = function(cfg) {
            cfg = cfg || {};
            cfg.status = cfg.status || {};
            //设置true参数,值传递
            //如果不设置true会导致多个控件的items引用同一个数组!!!
            $.extend(true, this, opts);
            //不设置true参数,地址传递
            //当Panel的items传递给Container再传递给layout,这样引用都是同一个数组,方便操作!
            $.extend(this, cfg);
            this.initialize();
            return (this.members && this.members()) || this;
          }
          for (;i < length; i++) {
            $.extend(this[type].prototype, arguments[i]);
          }
        }
        var xtype = type.toLowerCase();
        this.register[xtype] = this[type];
        this[type].prototype.xtype = xtype;
      }
    },
    /**
     * 包装方法,将传入的DOM元素/jQuery元素/选择器包装成组件
     * 可以直接通过items的方式加入容器组件中
     */
    packCmp: function(cfg) {
      cfg = cfg || {};
      cfg.el = $(cfg.el || cfg.html || "<div></div>")
      cfg.cls && cfg.el.addClass(cfg.cls);
      cfg.style && cfg.el.css(cfg.style);
      cfg.renderTo && cfg.el.appendTo(cfg.renderTo);
      
      return {
        el: cfg.el,
        render: function(el) {
          if (el) {
            cfg.el.appendTo(el);
          }
        }
      };
    },
    zIndex: 1,
    maxZIndex: 9999,
    /**
     * 获取某一层的更大的z-index值(z-index的管理分为三个层)
     */
    topZIndex: function(layer) {
      YH.zIndex++;
      return {
        "upper": YH.zIndex + 5000,
        "middle": YH.zIndex + 3000,
        "lower": YH.zIndex + 1000
      } [layer] || YH.zIndex;
    },
    /**
     * 关闭/打开js错误提示函数
     * @param type        close: 关闭错误
     *                    其他: 开启错误     */
    error: function(type) {
      if (type == "close") {
        YH.error.originalEvent = YH.error.originalEvent || window.onerror;
        window.onerror = function(e, url, line) {
          YH.console.error(url + "  " + line + "  " + e);
          return true;
        };
      }
      else {
        window.onerror = YH.error.originalEvent;
      }
    }
  };
  
  /**
   * 创建Component的超类,包括组件共有的方法   */
  YH.createComponent("Component", null, {
    /**
     * 默认调用的方法,统一由Component超类调用
     */
    "initialize": function() {
    
      this.status.initialized = false;
      //初始化组件容器
      this.el = $(this.el || "<div></div>")
      this.id = this.id || YH.getAutoID(this.autoIdPrefix);
      this.el.attr("id", this.id);
      //注册组件
      YH.addCmp(this.id, this);
      
      //调用初始化事件方法
      this.doListeners();
      this.doLoader();
      
      //统一调用render方法
      this.renderTo && this.render(this.renderTo);
      this.cls && this.el.addClass(this.cls);
      this.parentCmp && this.parentCmp.addItems(this);
      
      //调用组件子类的初始化方法
      this.initComponent();
      this.style && this.el.css(this.style);
      
      this.doDraggable();
      this.doResizable();
      this.doInitStatus();
      this.status.initialized = true;
    },
    /**
     * 初始化组件状态
     * 初始化函数的最后!!
     * (比如窗口默认最大化之类的问题)
     */
    doInitStatus: function() {
      
    },
    /**
     * 组件子类的初始化方法
     */
    "initComponent": $.noop,
    /**
     * 重新加载组件(处理的不够全面)
     */
    "reload": function() {
      this.doLoader();
      this.refresh();
    },
    /**
     * 刷新组件(不重新加载数据)
     */
    "refresh": function() {
      this.empty();
      this.initComponent();
    },
    /**
     * 处理拖动
     */
    "doDraggable": function() {
      if (this.draggable) {
        if (!this.modal) {
          var mask = $(".draggable-mask");
          if (!mask.length) {
            mask = $("<div class=\"draggable-mask\"></div>")
            $("body").append(mask);
          }
          this.draggable.start = function(e, ui) {
            mask.css({
              "z-index": self.zIndex
            });
            mask.show();
          }
        }
        
        this.el.draggable(this.draggable);
        var self = this;
        this.el.bind("dragstop", function() {
          self.left = self.el.css("left");
          self.top = self.el.css("top");
          if (!self.modal) {
            mask.hide();
          }
        });
        this.drag = {
          "destroy": function() {
            self.el.draggable("destroy");
          },
          "disable": function() {
            self.el.draggable("disable");
          },
          "enable": function() {
            self.el.draggable("enable");
          },
          /**
           * 设置/获取拖拽的选项
           * @param name          选项名称
           * @param value         选项值,当不传递值时方法为获取拖拽选项
           */
          "option": function(name, value) {
            return self.el.draggable("option", name, value); 
          }
        };
        this.status.draggable = true;
      }
    },
    /**
     * 处理resize
     */
    "doResizable": function() {
      if (this.resizable) {
        this.el.resizable(this.resizable);
        var self = this;
        this.resize = {
          "destroy": function() {
            self.el.resizable("destroy");
          },
          "disable": function() {
            self.el.resizable("disable");
          },
          "enable": function() {
            self.el.resizable("enable");
          },
          /**
           * 设置/获取拖拽的选项
           * @param name          选项名称
           * @param value         选项值,当不传递值时方法为获取拖拽选项
           */
          "option": function(name, value) {
            return self.el.resizable("option", name, value); 
          }
        };
        this.status.resizable = true;
      }
    },
    /**
     * 初始化事件     */
    "doListeners": function() {
      if (this.listeners) {
        var self = this;
        $.each(this.listeners, function(k, v) {
          if (typeof v === "function") {
            YH.Event.add(self, "bind", k, false, v);
          }
          else if (typeof v === "object"){
            $.each(v, function(key, value) {
              YH.Event.add(self, "bind", k, key, value);
            });
          }
        });
      }
    },
    /**
     * 清空组件dom元素
     */
    "empty": function() {
      this.el.empty();
    },
    /**
     * 销毁组件
     */
    "destroy": function() {
      if (this.items) {
        var items = this.items.slice();
        //这里不直接用this.items循环
        //因为this.items在this.parentCmp.removeItem(this, true)
        //后长度发生变化,$.each找到的项就不正确了

        $.each(items, function(i, e) {
          e && e.destroy && e.destroy();
        });
      }
      if (this.parentCmp) {
        //第二个参数true表示不再次调用组件的destroy
        this.parentCmp.removeItem(this, true);
      }
      this.el.empty();
      this.el.remove();
      YH.removeCmp(this.id);
      
    },
    /**
     * 隐藏组件
     */
    "hide": function(speed, callback) {
      if (this.status.hidden) {
        return;
      }
      this.hideEffect.call(this, speed, callback);
      this.status.hidden = true;
    },
    /**
     * 隐藏的效果(提供给用户自定义效果的借口)
     */
    "hideEffect": function(speed, callback) {
      this.el.hide(speed);
      callback && callback();
    },
    "addClass": function() {
      this.el.addClass.apply(this.el, arguments)
    },
    "removeClass": function() {
      this.el.removeClass.apply(this.el, arguments)
    },
    /**
     * 待进一步处理
     */
    "changeStyle": function(cls) {
      this.addClass(cls);
    },
    /**
     * 显示组件
     */
    "show": function() {
      if (this.status.hidden) {
        this.status.hidden = false;
        this.showEffect.apply(this, arguments);
      }
    },
    /**
     * 用户可以自定义显示的特效
     */
    showEffect: function() {
      this.el.show.apply(this.el, arguments);
    },
    "setPosition": function(pos) {
      this.el.offset(pos);
    },
    /**
     * 组件间组合的借口函数
     * @param el          将组件的DOM元素添加到节点el上
     *                    [DOM元素,JQUERY元素,JQUERY选择器]
     */
    "fadeIn": function() {
      this.el.fadeIn.apply(this.el, arguments)
    },
    "fadeOut": function(speed) {
      this.el.fadeOut.apply(this.el, arguments);
    },
    "render": function(el) {
      if (el) {
        this.el.appendTo(el);
        this.parentEl = el;
      }
    },
    /**
     * 获取组件ID
     */
    "getId": function() {
      return this.id;
    },
    /**
     * 修改组件ID,同时修改注册的组件ID
     */
    "setId": function(id) {
      YH.removeCmp(this.id);
      this.id = id;
      YH.addCmp(this.id, this);
    },
    /**
     * 获取配置参数中的宽度
     */
    "getWidth": function() {
      return this.width;
    },
    /**
     * 获取配置参数中的高度
     */
    "getHeight": function() {
      return this.height;
    },
    /**
     * 获取组件实际高度
     */
    "getAbsHeight": function() {
      return this.el.height() + "px";
    },
    /**
     * 获取组件实际宽度
     */
    "getAbsWidth": function() {
      return this.el.width() + "px";
    },
    "getPosX": function() {
      return this.el.position().left;
    },
    "getPosY": function() {
      return this.el.position().top;
    },
    "setStyle": function(style) {
      this.el.css(style);
    },
    /**
     * 配置数据Loader
     * 兼容YH.Loader类型和{url:""...}类型
     */
    "doLoader": function() {
      if (!this.loader || typeof this.loader !== "object") {
        return;
      }
      var o;
      this.loader.param = {
        totalRecords: this.maxRecords || 0
      }
      if (this.loader.xtype === "loader") {
        o = this.loader.load();
      }
      else {
        var loader = new YH.Loader(this.loader);
        o = loader.load();
      }
      
      this.data = o && o.data;
      
      this.pageInfo = o && o.pageInfo;
    },
    /**
     * 获取控件的父元素(jquery元素)
     */
    "getParentEl": function(flag) {
      if (flag) {
        return this.el.parent();
      }
      return this.parentEl || this.el.parent()[0] ? this.el.parent() : $("body");
    },
    /**
     * 控件事件的实际载体     * @return              jQuery对象
     */
    "getEvtActor": function() {
      return this.evtActor || this.el;
    },
    "setEvtActor": function(e) {
      this.evtActor = e;
    },
    /**
     * 设置控件的公有函数和只读属性     */
    "members": $.noop,
    /*
    "members": function() {
      var self = this;
      return {
        render: self.render,
        reload: self.reload
      }
    },
    */
    /**
     * 控件组合函数,定义组合控件时子控件对父控件透明的函数或者只读属性     */
    "compose": $.noop
  });
  
  
  /**
   * Loader组件,目前只支持JSON格式数据
   */
  YH.Loader = function(cfg) {
    var opts = {
      "url": "",
      "dataType": "json",
      "type": "POST",
      "param": {},
      "dataRender": null,
      "success": null,
      async: false,
      "error": null
    };
    $.extend(true, this, opts, cfg);
    this.initialize();
  };
  
  $.extend(YH.Loader.prototype, {
    initialize: function() {
    },
    load: function() {
      var t = this;
      var dataType = this.dataType;
      if (this.dataType.toLowerCase() == "json") {
        dataType = "text";
      }
      var data;
      $.ajax({
        type: t.type,
        url: t.url,
        async: t.async,
        dataType: dataType,
        data: t.param,
        success: function(text) {
          if (t.dataType.toLowerCase() == "json") {
            data = (t.dataRender || (function(data) {
              if (data.rtState == "0") {
                data = data.rtData;
              }
              else {
                console.info(data.rtMsrg);
              }
              return data;
            })) (YH.parseJson(text));
          }
          t.success && t.success(data);
        },
        error: t.error
      });
      return {
        data: data
      };
    }
  });
  
  /**
   * 适应各种控件的插件,是一种方便形式
   */
  YH.Plugins = {
  };
  
  /**
   * 使用jQuery对象装载事件,对jQuery事件机制简单封装

   */
  YH.Event = {
    /**
     * 向控件添加事件的方法
     * @param cmp                 控件的引用     * @param name                事件名称
     * @param moment              触发的时刻[before, after, ""]
     *                            当moment为空时,代表事件不是由控件的标准函数触发
     * @param event               事件的函数,参数为(event, 控件的引用, 设置事件时自定义的参数)
     * @param scope               事件函数执行时的this
     * @param params              自定义的事件函数的参数     */
    add: function(cmp, type, name, moment, event, scope, params) {
      if (cmp.getEvtActor && ((cmp[name] && (moment === "before" || moment === "after")) || !moment)) {
          var evtName = name + (moment || "");
          if (type == "bind") {
            cmp.getEvtActor().bind(evtName, {
              cmp: cmp,
              params: params
            }, function(e) {
              var args = Array.prototype.slice.call(arguments, 0);
              args.push(e.data.cmp, e.data.params);
              event.apply(scope || cmp, args);
            });
          }
          else if (type == "one"){
            cmp.getEvtActor().one(evtName, {
              cmp: cmp,
              params: params
            }, function(e) {
              event.call(scope || cmp, args);
            });
          }
        if (moment === "before" || moment === "after") {
          if (cmp[name] && !cmp[name].original) {
            var tmp = cmp[name];
            cmp[name] = function() {
              cmp.getEvtActor().trigger(name + "before");
              cmp[name].original.apply(cmp, arguments);
              cmp.getEvtActor().trigger(name + "after");
            }
            cmp[name].original = tmp;
          }
        }
      }
    },
    /**
     * 删除事件,目前是删除按name和moment匹配的所有事件
     * @param cmp                 控件的引用
     * @param name                事件名称
     * @param moment              触发的时刻[before, after, ""]
     */
    del: function(cmp, name, moment) {
      //当没传递moment时,删除dom事件;当传递moment时,判断是否有自定义事件,如果有则删除
      if (cmp.getEvtActor && (cmp[name] && (moment === "before" || moment === "after") || !moment)) {
        var evtName = name + (moment || "");
        cmp.getEvtActor().unbind(evtName);
      }
    }
  };
  
  YH.Tools = {
    "toggle": {
      defaultHandler: function(event, toolEl, panel, params) {
        panel.toggle();
      }
    },
    "close": {
      title: "关闭",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.close();
      }
    },
    "minimize": {
      title: "最小化",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.minimize();
      }
    },
    "maximize": {
      title: "全屏最大化",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["restore"] && panel.tools["restore"].show();
        panel.tools["plus"] && panel.tools["plus"].show();
        toolEl.hide();
        panel.maximize();
      }
    },
    "restore": {
      title: "还原",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["maximize"] && panel.tools["maximize"].show();
        panel.tools["plus"] && panel.tools["plus"].show();
        toolEl.hide();
        panel.restore();
      }
    },
    "gear": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "pin": {
      title: "置顶",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["pin"] && panel.tools["pin"].hide();
        panel.tools["unpin"] && panel.tools["unpin"].show();
        panel.pin();
      }
    },
    "unpin": {
      title: "浮动",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["pin"] && panel.tools["pin"].show();
        panel.tools["unpin"] && panel.tools["unpin"].hide();
        panel.unpin();
      }
    },
    "right": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "left": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "up": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "down": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "refresh": {
      title: "刷新",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.refresh();
        panel.status.hidden && panel.show();
      }
    },
    "minus": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    }, 
    "plus": {
      title: "工作区最大化",
      defaultHandler: function(event, toolEl, panel, params) {
        panel.tools["restore"] && panel.tools["restore"].show();
        panel.tools["maximize"] && panel.tools["maximize"].show();
        toolEl.hide();
        panel.maximize("parent");
      }
    },
    "help": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "search": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "save": {
      defaultHandler: function(event, toolEl, panel, params) {
      }
    },
    "more": {
      title: "更多",
      defaultHandler: function(event, toolEl, panel, params) {
      }
    }
  };
  
  if (YH.isTouchDevice) {
    YH.fitTouchDevice();
  }
  
  function parseFeatures(features) {
    if (!features) {
      return;
    }
    var cfg = {};
    var pros = features.split(/[,;]/);
    for (var i = 0; i < pros.length; i++) {
      if (pros[i]) {
        var kv = pros[i].split(/[=:]/);
        if (kv.length > 1) {
          var key = {
            dialogheight: "height",
            dialogwidth: "width",
            //dialogleft: "left",
            //dialogtop: "top",
            height: "height",
            width: "width",
            resizable: "resizable"
          } [kv[0].toLowerCase()];
          if (key) {
            cfg[key] = kv[1];
          }
        }
      }
    }
    return cfg;
  }
  
  if (top == window && YH.isTouchDevice) {
    window.showModalDialog = function(url, args, features) {
      var win;
      var cfg = {
        src: url,
        cls: "",
        layer: "upper",
        modal: true,
        draggable: {
          containment: false
        },
        listeners: {
          doContainer: {
            after: function(e, t) {
              try {
                var i = t.container.iframeDom;
                var w = i.contentWindow;
                w.dialogArguments = args;
                var id = t.getId();
                i.onload = function() {
                  w.dialogArguments = w.dialogArguments || args;
                  try {
                    var s = w.document.createElement("script");
                    s.innerHTML = "function close() {parent.YH.getCmp('" + id + "').close();}";
                    var head = w.document.getElementsByTagName("head").item(0)
                    s.setAttribute("type", "text/javascript"); 
                    head.appendChild(s);
                  } catch (e) {
                    w.close = function() {
                      t.close();
                    }
                  } finally {
                    
                  }
                }
              } catch (e) {
                
              } finally {
                
              }
            }
          }
        }
      };
      
      $.extend(true, cfg, parseFeatures(features));
      win = new YH.Window(cfg);
      win.show();
    }
    window.open = function (url, name, features, replace) {
      var self = this;
      var cfg = {
        src: url,
        title: name,
        width: 800,
        height: 600,
        layer: "upper",
        draggable: {
          containment: false
        },
        resizable: {},
        listeners: {
          doContainer: {
            after: function(e, t) {
              try {
                var i = t.container.iframeDom;
                var w = i.contentWindow;
                w.opener = self;
                var id = t.getId();
                i.onload = function() {
                  try {
                    w.opener = w.opener || self;
                    var s = w.document.createElement("script");
                    s.innerHTML = "function close() {parent.YH.getCmp('" + id + "').close();}";
                    var head = w.document.getElementsByTagName("head").item(0)
                    s.setAttribute("type", "text/javascript");
                    head.appendChild(s);
                  } catch (e) {
                    try {
                      w.close = function() {
                        t.close();
                      }
                    } catch (e) {
                      d.d
                      YH.console.info(e);
                    } finally {
                      
                    }
                  } finally {
                    
                  }
                }
              } catch (e) {
                
              } finally {
                
              }
            }
          }
        }
      };
      $.extend(true, cfg, parseFeatures(features));
      var win = new YH.Window(cfg);
      win.show();
    }
    
  }
  else if (YH.isTouchDevice){
    window.showModalDialog = function () {
      top.YH && top.showModalDialog.apply(this, arguments);
    }

    window.open = function () {
      top.YH && top.open.apply(this, arguments);
    }
  }
})(jQuery);
