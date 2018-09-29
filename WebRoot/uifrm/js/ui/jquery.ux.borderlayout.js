(function ($) {
  $.fn.layout = function(cfg) {
    var prefix = 'ui-layout-';
    var self = this;
    $.fn.layout.defaults = {
      'allPanes': 'north,south,east,west,center',
      'listeners': {
        'resize': $.noop
      },
      'north': {
        'dir': 'v',
        'selector': '.' + prefix + 'north',
        'size': 0,
        overflow: "hidden",
        'hidden': false,
        'fxSpeed': 'normal'
      },
      'south': {
        overflow: "hidden",
        'dir': 'v',
        'selector': '.' + prefix + 'south',
        'size': 0,
        'hidden': false,
        'fxSpeed': 'normal'
      },
      'east': {
        overflow: "hidden",
        'dir': 'h',
        'selector': '.' + prefix + 'east',
        'size': 0,
        'hidden': false,
        'fxSpeed': 'normal'
      },
      'west': {
        overflow: "hidden",
        'dir': 'h',
        'selector': '.' + prefix + 'west',
        'size': 0,
        'hidden': false,
        'fxSpeed': 'normal'
      },      
      'center': {
        overflow: "hidden",
        'selector': '.' + prefix + 'center',
        'size': 'auto'
      }
    };
    
    var c = $.extend(true, $.fn.layout.defaults, cfg);
    
    var status = {};
    
    var $Ps = {};
    var $Container = $(this).css({ 
      'overflow': 'hidden' 
    });
    var cDims;
    var vSize, hSize;
    var sizes = {};
    
    function doAutoSize(panels) {
      $.each(panels, function(i, e) {
        var panel = c[e];
        if (panel && panel.size == 'auto') {
          if (panel.dir == 'v') {
            panel.size = self.children(panel.selector).height();
          }
          else if (panel.dir == 'h') {
            panel.size = self.children(panel.selector).width();
          }
        }
      });
    }
    
    function getSizes() {
      cDims = getElemDims($Container);
      vSize = cDims.height;
      hSize = cDims.width;
    }
    
    function initPanels() {
      doAutoSize(c.allPanes.split(","));
      sizePanels(c.allPanes.split(","));
    }
    
    function sizePanels(panels) {
      vSize = cDims.height;
      hSize = cDims.width;
      $.each(panels, function(i, panel) {
        var sel = c[panel].selector;
        var dir = c[panel].dir;
        var CSS = {};
        var hidden = c[panel].hidden;
        
        if (hidden) {
          CSS.display = 'none';
        }
        
        if (typeof($Ps[panel]) == 'undefined') {
          $Ps[panel] = $Container.children().filter(sel + ':first');
          sizes[panel] = hidden ? 0 : c[panel].size;
        }
        
        if (dir == 'h' && !isNaN(sizes[panel])) {
          hSize -= sizes[panel];
          CSS.width = sizes[panel];
        }
        
        if (dir == 'v' && !isNaN(sizes[panel])) {
          vSize -= sizes[panel];
          CSS.height = sizes[panel];
        }
        else {
          CSS.top = sizes['north'];
          CSS.height = vSize;
        }
        
        switch (panel) {
          case "north":
                  CSS.top   = cDims.top;
                  CSS.left  = cDims.left;
                  CSS.right = cDims.right;
                  CSS.width = cDims.width;
                  break;
          case "south":
                  CSS.bottom  = cDims.bottom;
                  CSS.left  = cDims.left;
                  CSS.right   = cDims.right;
                  break;
          case "west":
                  CSS.left  = cDims.left; // top, bottom & height set by sizeMidPanes()
                  break;
          case "east":
                  CSS.right   = cDims.right; // ditto
                  break;
          case "center":  // top, left, width & height set by sizeMidPanes()
                  CSS.width = hSize;
                  CSS.left = sizes['west'];
        }
        CSS.position = 'absolute';
        CSS.overflow = c[panel].overflow;
        CSS['z-index'] = 2;
        $Ps[panel].css(CSS);
      });
    }
    
    function resizePanels(panels) {
      vSize = cDims.height;
      hSize = cDims.width;
      $.each(panels, function(i, panel) {
        var dir = c[panel].dir;
        var CSS = {};
        
        if (dir == 'h' && !isNaN(sizes[panel])) {
          hSize -= sizes[panel];
          CSS.width = c[panel].size;
        }
        
        if (dir == 'v' && !isNaN(sizes[panel])) {
          vSize -= sizes[panel];
          CSS.height = c[panel].size;
        }
        else {
          CSS.top = sizes['north'];
          CSS.height = vSize;
        }
        
        switch (panel) {
          case "north":
                  CSS.left  = cDims.left;
                  CSS.right = cDims.right;
                  break;
          case "south":
                  CSS.left  = cDims.left;
                  CSS.right   = cDims.right;
                  break;
          case "west":
                  break;
          case "east":
                  break;
          case "center":  // top, left, width & height set by sizeMidPanes()
                  CSS.width = hSize;
                  CSS.left = sizes['west'];
        }
        $Ps[panel].css(CSS);
      });
      c.listeners.resize();
    }
    
    function initContainer() {
      try { // format html/body if this is a full page layout
        if ($Container[0].tagName == "BODY") {
          $("html").css({
            height:   "100%"
          , overflow: "hidden"
          });
          $("body").css({
            position: "relative"
          , height:   "100%"
          , overflow: "hidden"
          , margin:   0
          , padding:  0 
          , border: "none"
          });
          
          $(window).resize(resizeAll);
        }
        else {
          $Container.css({
            'position': 'absulute'
          });
        }
      } catch (ex) {
        
      }
    }
    
    function getElemDims ($E) {
      var d = {}; // dimensions hash

      $.each("Left,Right,Top,Bottom".split(","), function (i, e) {
        if ($E == $Container)
          d[e.toLowerCase()] = 0; 
      });
      
      d.height = $E.height();
      d.width = $E.width();
      
      return d;
    };
    
    function show(panel, animate) {
      if (!c[panel]) {
        return;
      }
      status.panel = {
        'closed': false
      };
      
      sizes[panel] = c[panel].size;
      
      var panels = c.allPanes.split(",");
      var opts = {
        'north': {
          'dir': 'top',
          'size': 0
        },
        'south': {
          'dir': 'top',
          'size': cDims.height - c[panel].size
        },
        'west': {
          'dir': 'left',
          'size': 0
        },
        'east': {
          'dir': 'left',
          'size': cDims.width - c[panel].size
        }
      };
      var CSS = {};
      
      $Ps[panel].css({
        'z-index': '10'
      });
      
      CSS[opts[panel].dir] = opts[panel].size;
      
      if (animate) {
        $Ps[panel].animate(CSS, c[panel].fxSpeed, function() {
          resizePanels(panels);
          $Ps[panel].css({
            'z-index': '2'
          });
        });
      }
      else {
        $Ps[panel].css(CSS);
        resizePanels(panels);
      }
    }
    
    function hide(panel, animate) {
      if (!c[panel]) {
        return;
      }
      
      status.panel = {
        'closed': true
      };
      
      sizes[panel] = 0;
      
      var panels = $.grep(c.allPanes.split(","), function(n, i){
        return n != panel;
      });
      resizePanels(panels);
      
      var opts = {
        'north': {
          'dir': 'top',
          'size': -c[panel].size || $(c.north.selector).height()
        },
        'south': {
          'dir': 'top',
          'size': cDims.height
        },
        'west': {
          'dir': 'left',
          'size': -c[panel].size
        },
        'east': {
          'dir': 'left',
          'size': cDims.width
        }
      };
      var CSS = {};
      
      $Ps[panel].css({
        'z-index': '10'
      });
      
      CSS[opts[panel].dir] = opts[panel].size;
      if (animate) {
        $Ps[panel].animate(CSS, c[panel].fxSpeed, function() {
          $Ps[panel].css({
            'z-index': '2'
          });
        });
      }
      else {
        $Ps[panel].css(CSS);
      }
    }

    function resizeAll() {
      getSizes();
      initPanels();
      c.listeners.resize();
    }
    
    function create() {
      initContainer();
      getSizes();
      initPanels();
    }
    
    create();
    
    return {
      hide: hide,
      show:show,
      resizeAll:resizeAll
    };
  }
})(jQuery);