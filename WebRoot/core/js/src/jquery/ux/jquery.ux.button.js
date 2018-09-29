(function($) {
  var btnOpts = {
    adaptive : false,
    cls : "",
    toggle : false,
    toggleHandler : [],
    icon : "",
    title : "",
    btnText : "",
    event : "click",
    handler : $.noop,
    normalCls : "",
    activeCls : "",
    title : ""
  };
  YH.createComponent("Button", btnOpts, YH.Component.prototype, {
    initComponent : function() {
      var self = this;
      this.btn = $('<button class="jq-button"></button>');
      this.btn.attr("title", this.title);
      this.btn.addClass(this.btnCls);
      if (this.status.isPressed) {
        this.btn.addClass(this.activeCls)
      } else {
        this.btn.addClass(this.normalCls)
      }
      this.el.append(this.btn);
      if (this.icon) {
        var img = $('<img align="absmiddle"></img>');
        img.attr("src", this.icon);
        this.btn.append(img)
      }
      if (this.btnText) {
        var span = $("<span></span>").text(this.btnText);
        this.btn.append(span)
      }
      this.toggleHandler[0] = this.toggleHandler[0] || $.noop;
      this.toggleHandler[1] = this.toggleHandler[1] || $.noop;
      if (this.toggle) {
        this.btn.click(function(e) {
          var btn = this;
          if (!self.status.isPressed) {
            self.status.isPressed = true;
            self.btn.removeClass(self.normalCls).addClass(self.activeCls);
            self.toggleHandler[0](e, self)
          } else {
            self.status.isPressed = false;
            self.btn.addClass(self.normalCls).removeClass(self.activeCls);
            self.toggleHandler[1](e, self)
          }
        })
      } else {
        this.btn.bind(this.event, function(e) {
          self.handler(e, self)
        })
      }
    },
    disable : function() {
      this.btn[0].setAttribute("disabled", true)
    },
    enable : function() {
      this.btn[0].removeAttribute("disabled")
    },
    setStatus : function(status, disabled) {
      this.status.isPressed = !(status == "default");
      if (this.status.isPressed) {
        this.btn.removeClass(this.normalCls).addClass(this.activeCls)
      } else {
        this.btn.removeClass(this.activeCls).addClass(this.normalCls);
      }
      disabled || this.btn[0] && this.btn[0].removeAttribute("disabled")
    }
  })
})(jQuery);