{
  cls: "window-height",
  "items": [{
    xtype: "custom",
    style: {
      width: "100%",
      height: "100%",
      position: "relative",
      "overflow-y": "auto",
      "overflow-x": "hidden"
    },
    loader: {
      url: contextPath + "/yh/core/funcs/setdescktop/notes/act/YHNotesAct/getNotes.act"
    },
    initComponent: function() {
      this.textarea = $("<textarea></textarea>");
      this.textarea.css({
        width: "100%",
        height: "95%",
        border: "none",
        overflow: "auto",
        "line-height": "20px"
      });
      this.mask = $("<div></div>");
      this.mask.css({
        width: "100%",
        height: "100%",
        border: "none",
        overflow: "auto",
        "line-height": "20px",
        display: "block",
        "background-color": "transparent",
        position: "absolute",
        top: "0"
      });
      
      if (this.data) {
        this.textarea.val(this.data.replace(/&#13;&#10;/g,'\r\n'));
      }
      
      this.el.append(this.textarea);
      this.el.append(this.mask);
      
      var self = this;
      this.mask.click(function() {
        self.showArea();
      });
      
      this.textarea.blur(function() {
        self.showMask();
        self.submit();
      });
    },
    showMask: function() {
      this.mask.show();
    },
    showArea: function() {
      this.mask.hide();
      this.textarea.focus();
    },
    submit: function() {
      var self = this;
      var url = contextPath + "/yh/core/funcs/setdescktop/notes/act/YHNotesAct/saveNote.act";
      var param = {note: self.textarea.val()};
      $.post(url, param);
    }
  }],
  width: "500px",
  height: "125px",
  "xtype": "panel",
  "cmpCls": "jq-window",
  "title": "便签",
  listeners: {
    initComponent: {
      after: function(e, t) {
        if ($.browser.ie || /7/.test($.browser.version)) {
          t.items[0].textarea.css({height: '200px'});
        }
      }
    }
  }
}