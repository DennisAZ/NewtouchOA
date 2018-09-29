;(function($) {
  /**
   * 快速生成iframe标签页,延时加载
   */
  $.fn.pageTabs = function(cfg) {
    var self = this;
    cfg = cfg || {};
    if (cfg.iframeTabs instanceof Array) {
      var ul = $("<ul></ul>");
      this.append(ul)
      $.each(cfg.iframeTabs, function(i, e) {
        //一个页面应该不会有id的重复,暂时不做处理
        ul.append($("<li></li>").append("<a href='#tabs-" + i + "'>" + e.title + "</a>"));
        var div = $("<div></div>");
        div.attr("id", "tabs-" + i);
        var iframe = $('<iframe allowTransparency="true" border="0" frameborder="0" cellspacing="0"></iframe>');
        if ((cfg.selected || 0) == i) {
          iframe.attr("src", e.src);
        }
        else {
          iframe.attr("src", "about:blank")
        }
        self.append(div.append(iframe));
      });
      
      //解决iframe高度问题
      var create = cfg.create;
      cfg.create = function() {
        $(".ui-tabs-panel > iframe").height(Math.round($(window).height() - 80));
        $(window).resize(function() {
          $(".ui-tabs-panel > iframe").height(Math.round($(window).height() - 80));
        });
        create && create.apply && create.apply(this, arguments);
      }
      
      //解决延时加载问题
      var show = cfg.show;
      cfg.show = function(e, item) {
        var iframe = $(item.panel.children[0]);
        if (iframe.attr("src") == "about:blank") {
          iframe.attr("src", cfg.iframeTabs[item.index].src || "about:blank");
        }
        show && show.apply && show.apply(this, arguments);
      }
      
    }
    return $.fn.tabs.call(this, cfg);
  }
  
  $.fn.toolbar = function(cfg) {
    var self = this;
    this.addClass("toolbar");
    if (cfg.btns instanceof Array) {
      $.each(cfg.btns, function(i, e) {
        var a = $("<a class='" + (cfg.btnCls || "toolbar-btn") + "' href='javascript: void(0)'></a>");
        if (!!e.id) {
          a.attr("id", e.id);
        }
        var span = $("<span></span>");
        if (e.icon) {
          span.append("<img src='" + e.icon + "'>");
        }
        span.append(e.text || "");
        a.append(span);
        self.append(a);
        a.click(function(evt) {
          (e.handler || $.noop) (evt, self, a);
        });
      });
    }
    return this;
  }
  
  //配置datepicker
  $.datepicker._defaults.dayNamesMin = ['日', '一', '二', '三', '四', '五', '六'];
  $.datepicker._defaults.monthNames = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
  $.datepicker._defaults.monthNamesShort = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
  $.datepicker._defaults.nextText = "下个月";
  $.datepicker._defaults.prevText = "上个月";
  
  
  /**
   * tabbbedDialog扩展,标签弹窗
   */
  $.fn.tabbedDialog = function (tabOpts, dialogOpts) {
    this.tabs(tabOpts);
    this.dialog(dialogOpts);
    this.find('ul.ui-tabs-nav').append($('a.ui-dialog-titlebar-close'));
    this.find('.ui-tab-dialog-close').css({'position':'absolute','right':'0', 'top':'23px'});
    this.find('.ui-tab-dialog-close > a').css({'float':'none','padding':'0'});
    var tabul = this.find('ul:first');
    this.parent().addClass('ui-tabs').prepend(tabul).draggable('option','handle',tabul); 
    this.siblings('.ui-dialog-titlebar').remove();
    tabul.addClass('ui-dialog-titlebar');
    return this;
  }
})(jQuery);


/**
 * 选择科目的方法
 */
function selectSubject(cfg) {
  var opts = {
    callback: $.noop,
    data: null
  };
  var c = $.extend(true, opts, cfg);
  var subjects = [{
    text: '资产',
    dataUrl: contextPath + '/uifrm/data/subject.js'
  }, {
    text: '负债',
    dataUrl: contextPath + '/uifrm/data/subject.js'
  }, {
    text: '共同',
    dataUrl: contextPath + '/uifrm/data/subject.js'
  }, {
    text: '权益',
    dataUrl: contextPath + '/uifrm/data/subject.js'
  }, {
    text: '成本',
    dataUrl: contextPath + '/uifrm/data/subject.js'
  }, {
    text: '损益',
    dataUrl: contextPath + '/uifrm/data/subject.js'
  }];
  var sel = $("<div class='subject-select'></div>");
  var ul =$("<ul></ul>");
  sel.append(ul)
  $.each(subjects, function(i, e) {
    //避免id重复
    while ($("#subject" + i).length) {
      i = i + 10
    }
    ul.append("<li><a href='#subject" + i + "'>" + e.text + "</a></li>");
    var div = $("<div class='ztree' id='subject" + i + "'></div>");
    sel.append(div);
  });
  
  $("body").append(sel);
  sel.tabbedDialog({
    show: function(evt, item) {
      var t = subjects[item.index];
      if (t.initialized) {
        return;
      }
      t.initialized = true;
      var setting = {
        view: {
          dblClickExpand: false,
          showLine: false
        },
        data: {
          simpleData: {
            enable: true
          }
        },
        callback: {
          onClick: onClick
        }
      };

      function onClick(e,treeId, treeNode) {
        if (treeNode.isParent) {
          return;
        }
        else {
          cfg.callback(e, treeId, treeNode);
          sel.dialog("close");
        }
      }
      
      $.getJSON(t.dataUrl, function(json) {
        $.fn.zTree.init($(item.panel), setting, json);
      });
    }
  }, {
    width: 600,
    height: 400,
    modal: true,
    buttons: {
      "新建": function() {
        $('<iframe src="' + contextPath + '/fis/funcs/subject/new.jsp"></iframe>').dialog({
          title: "新建科目",
          height: 450,
          width: 500,
          modal: true
        }).css({
          width: "100%"
        });
      },
      "取消": function() {
        sel.dialog("close");
      }
    }
  });
}
/**
 * 弹出消息
 */
function alertMsg(msg) {
  if ($("#dialog-alertmsg").length) {
    $("#dialog-alertmsg").dialog("open").text(msg);
  }
  else {
    $("<div id='dialog-alertmsg'></div>").appendTo("body").text(msg).dialog({
      modal: true,
      buttons: {
        Ok: function() {
          $(this).dialog("close");
        }
      }
    });
  }
}