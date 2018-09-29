(function($) {
  var opts = {
    el: $('<div></div>'),
    border: '5px solid transparent',
    items: [],
    sortable: {
      revert: true,
      'tolerance': 'pointer'        //通过鼠标的位置计算拖动的位置*重要属性*
    },
    cols: 3,
    cellpadding: 0,
    cellspacing: 0,
    designMode: false,
    selectable: {
      cancel: '.gridlayout-dragColumn,.extend-container,.' + this.designCls
    },
    designCls: 'gridlayout-dragColumn',
    cmpCls: 'jq-gridlayout',
    selectableCls: 'ui-selected',
    sortableCls: 'grid-sortable',
    droppableCls: 'grid-droppable',
    droppableCmps: [],
    sortableCmps: [],
    colsWidth: []
  };
  YH.createComponent.call(YH.layouts, "GridLayout", opts, YH.layouts.ColumnLayout.prototype, {
    initComponent: function() {
      YH.layouts.ColumnLayout.prototype.initComponent.call(this);
    },
    initTable: function() {
      this.tr = [];
      for (var i = 0; i < this.cols; i++ ) {
        var tr = $('<tr></tr>');
        this.tr.push(tr);
        this.table.append(tr);
        for (var j = 0; j < this.rows; j++ ) {
          var td = $('<td></td>');
          tr.append(td);
        }
      }
    },
    renderCells: function() {
      //this.initTable();
      var self = this;
      this.tr = [];
      var cols = 0;
      
      var colIndex = 0;
      var rowIndex = 0;
      
      this.tr.push({cols: this.cols, td: []});
      $.each(this.items, function(i, e) {
        e.colSpan = e.colSpan || 1;
        e.rowSpan = e.rowSpan || 1;
        e.index = i;
        e.colIndex = colIndex % self.cols;
        
        colIndex += e.colSpan
        while (colIndex > self.tr[rowIndex].cols) {
          rowIndex++;
          if (!self.tr[rowIndex]) {
            self.tr.push({cols: self.cols, td: []});
          }
          colIndex = e.colSpan;
        }
        
        e.rowIndex = rowIndex;
        if (e.rowSpan > 1) {
          for (var j = 1; j < e.rowSpan; j++) {
            if (!self.tr[rowIndex + j]) {
              self.tr.push({cols: self.cols - e.colSpan, td: []});
            }
            else {
              self.tr[rowIndex + j].cols -= e.colSpan;
            }
          }
        }
        
        self.tr[rowIndex].td.push(e);
        
        self.doItem(e);
      });
      
      var tr = $('<tr></tr>');
      tr.addClass(this.designCls);
      for (var i = 0; i < this.cols; i++) {
        var td = $('<td></td>');
        td.attr("width", self.colsWidth[i] || '');
        tr.append(td);
      }
      this.table.prepend(tr);
      
      this.renderTable();
      
      this.doDesign();
      
    },
    
    renderTable: function() {
      var self = this;
      
      $.each(this.tr, function(i, e) {
        var tr = $('<tr></tr>');
        self.table.append(tr);
        $.each(e.td, function(j, el) {
          var td = $('<td></td>');
          td.attr('valign', 'top');
          td.attr('colSpan', el.colSpan);
          td.attr('rowSpan', el.rowSpan);
          tr.append(td);
          el.layout = "fitlayout";
          el.height = el.rowSpan * 100;
          self.items[el.index] = el = self.renderItem(el, td);
          td.data('cmpId', el.id);
        });
      });
    },
    doItem: function(e) {
      if (YH.isCmp(e)) {
        e.leaf = true;
        this.sortable && this.sortableCmps.push(e);
        this.doDroppable(e);
      }
    },
    doDesign: function() {
      if (this.designMode) {
        //打开设计模式的时候也开启排序
        this.doSortable();
        this.table.selectable(this.selectable);
        this.el.addClass('gridlayout-designMode');
        this.doTipMenu();
        
        var self = this;
        var dragColumns  = this.table[0].rows[0].cells; // first row columns, used for changing of width
        if (!dragColumns){
          return; // return if no table exists or no one row exists
        }
      
        var dragColumnNo; // current dragging column
        var dragX;        // last event X mouse coordinate
        
        var saveOnmouseup;   // save document onmouseup event handler
        var saveOnmousemove; // save document onmousemove event handler
        var saveBodyCursor;  // save body cursor property
        
        /**
        * 取消事件的默认动作,阻止事件的冒泡传递

        */
        function preventEvent(e) {
          var ev = e || window.event;
          if (ev.preventDefault){
           ev.preventDefault();
          }
          else {
           ev.returnValue = false;
          }
          if (ev.stopPropagation){
           ev.stopPropagation();
          }
          return false;
        }
      
        this.changeColumnWidth = function(no, w) {
          if (!dragColumns){
            return false;
          }
          if (no < 0){ 
            return false;
          }
           
          if (dragColumns.length < no){
            return false;
          }
          
          if (parseInt(dragColumns[no].style.width) <= -w){ 
            return false;
          }
          if (dragColumns[no+1] && parseInt(dragColumns[no+1].style.width) <= w){
            return false;
          }
          var width =parseInt(dragColumns[no].style.width) + w +'px';
          dragColumns[no].style.width = width;
          
          //
          self.colsWidth[no] = width;
          
          if (dragColumns[no+1]){
            self.colsWidth[no + 1] = parseInt(dragColumns[no+1].style.width) - w + 'px';
            dragColumns[no+1].style.width = parseInt(dragColumns[no+1].style.width) - w + 'px';
          }
          return true;
        }
        
        // ============================================================
        // do drag column width
        this.columnDrag = function(e) {
          var e = e || window.event;
          var X = e.clientX || e.pageX;
          if (!self.changeColumnWidth(dragColumnNo, X-dragX)) {
           // stop drag!
           self.stopColumnDrag(e);
          }
        
          dragX = X;
          // prevent other event handling
          preventEvent(e);
          return false;
        }
        
        // ============================================================
        // stops column dragging
        this.stopColumnDrag = function(e) {
          var e = e || window.event;
          if (!dragColumns) return;
        
          // restore handlers & cursor
          document.onmouseup  = saveOnmouseup;
          document.onmousemove = saveOnmousemove;
          document.body.style.cursor = saveBodyCursor;
        
          // remember columns widths in cookies for server side
          var colWidth = '';
          var separator = '';
          for (var i=0; i<dragColumns.length; i++) {
            colWidth += separator + parseInt( $(dragColumns[i]).width() );
            separator = '+';
          }
          preventEvent(e);
        }
        
        // ============================================================
        // init data and start dragging
        this.startColumnDrag = function(e) {
          var e = e || window.event;
        
          // if not first button was clicked
          //if (e.button != 0) return;
        
          // remember dragging object
          dragColumnNo = (e.target || e.srcElement).parentNode.cellIndex;
          dragX = e.clientX || e.pageX;
        
          // set up current columns widths in their particular attributes
          // do it in two steps to avoid jumps on page!
          var colWidth = new Array();
          for (var i=0; i<dragColumns.length; i++) {
            colWidth[i] = parseInt( $(dragColumns[i]).width() );
          }
          for (var i=0; i<dragColumns.length; i++) {
            dragColumns[i].width = ""; // for sure
            dragColumns[i].style.width = colWidth[i] + "px";
          }
        
          saveOnmouseup = document.onmouseup;
          document.onmouseup = self.stopColumnDrag;
        
          saveBodyCursor = document.body.style.cursor;
          document.body.style.cursor = 'w-resize';
        
          // fire!
          saveOnmousemove = document.onmousemove;
          document.onmousemove = self.columnDrag;
        
          preventEvent(e);
        }
      
        for (var i=0; i<dragColumns.length; i++) {
          var evt = $('<div></div>');
          evt.css({
            'position': 'relative',
            'height': '20px',
            'background-color': 'gray',
            'width': '5px',
            'left': '100%',
            'z-index': YH.topZIndex(),
            'cursor': 'w-resize'
          });
          $(dragColumns[i]).empty().append(evt);
          evt.mousedown(self.startColumnDrag);
        }
      }
    },
    verifyMerge: function() {
      var self = this;
      var cls = '.' + this.selectableCls;
      var cells = 0;
      var tds = self.table.find('td' + cls);
      if (tds.length < 2) {
        return false;
      }
      var td = tds.first();
      /*
      var left = td.attr('cellIndex');
      var right = td.attr('cellIndex') + td.attr('colSpan');
      var top = td.parent().attr('rowIndex');
      var bottom = td.parent().attr('rowIndex') + td.attr('rowSpan');
      */
      var cmp = YH.getCmp(td.data("cmpId"));
      
      var top = cmp.rowIndex, left = cmp.colIndex, bottom = cmp.rowIndex + cmp.rowSpan, right = cmp.colIndex + cmp.colSpan;
      self.table.find('td' + cls).each(function(i, e) {
        var cmp = YH.getCmp($(e).data("cmpId"));
        
        left = Math.min(left, cmp.colIndex);
        
        top = Math.min(top, cmp.rowIndex);
        
        right = Math.max(right, (cmp.colIndex + cmp.colSpan));
        
        bottom = Math.max(bottom, (cmp.rowIndex + cmp.rowSpan));
        var col = e.colSpan || 0;
        var row = e.rowSpan || 0
        cells += col * row; 
        return;
        
        var rowIndex = e.parentNode.rowIndex;
        var cellIndex = e.cellIndex;
        
        if (rowIndex < top) {
          top = rowIndex;
        }
        if (rowIndex + row > bottom) {
          bottom = rowIndex + row;
        }
        if (cellIndex < left) {
          left = cellIndex;
        }
        if (cellIndex + col > right) {
          right = cellIndex + col;
        }
      });
      
      if (cells != (right - left) * (bottom - top)) {
        return false;
      }
      return true;
    },
    mergeCells: function() {
      if (!this.verifyMerge()) {
        return;
      }
      var self = this;
      var cls = '.' + this.selectableCls;
      var cols = 0, rows = 0, total = 0;
      self.table.find('td' + cls).eq(0).parent().children(cls).each(function(i, e) {
         cols += $(e).attr('colspan') || 0;
      });
      
      self.table.find('td' + cls).each(function(i, e) {
         var col = $(e).attr('colspan') || 0;
         var row = $(e).attr('rowspan') || 0;
         total += col * row;
      });
      rows = total / cols;
      var td = $('td' + cls).first();
      self.table.find('td' + cls).each(function(i, e) {
         if (i == 0) {
           $(e).attr('colspan', cols);
           $(e).attr('rowspan', rows);
           var cmp = YH.getCmp($(e).find('div:first').attr('id'));
           if (cmp) {
             cmp.colSpan = cols;
             cmp.rowSpan = rows;
             cmp.setStyle({height: rows * (100 + self.cellpadding + self.cellspacing)});
           }
         }
         else {
           var cmp = YH.getCmp($(e).find('div:first').attr('id'));
           cmp && cmp.destroy();
           $(e).remove();
         }
      });
    },
    addItem: function(renderTo) {
      renderTo = $(renderTo);
      var item = new YH.Container({
        renderTo: renderTo,
        autoIdPrefix: 'portal',
        layout: "fitlayout",
        //指定父组件,为了合并单元格的时候销毁没有用的容器
        parentCmp: this,
        style: {
          height: '100px'
        }
      });
      this.items.push(item);
      this.doItem(item);
    },
    splitCells: function() {
      var cls = '.' + this.selectableCls;
      var cols = 0, rows = 0, total = 0;
      var self = this;
      
      $('td' + cls).eq(0).parent().children(cls).each(function(i, e) {
        cols += $(e).attr('colspan') || 0;
      });
      
      $('td' + cls).each(function(i, e) {
        var col = $(e).attr('colspan') || 0;
        $(e).attr('colspan', 1);
        var cmp = YH.getCmp($(e).find('div:first').attr('id'));
        if (cmp) {
          cmp.colSpan = 1;
        }
        for (;col > 1; col--) {
          var copy = $(e).clone(true).empty();
          $(e).after(copy);
          self.addItem(copy);
        }
      });
      
      $('td' + cls).each(function(i, e) {
        var row = $(e).attr('rowspan') || 0;
        $(e).attr('rowspan', 1);
        var cmp = YH.getCmp($(e).find('div:first').attr('id'));
        if (cmp) {
          cmp.rowSpan = 1;
        }
        var tr = $(e).parent();
        for (; row > 1; row--) {
          var copy = $(e).clone().empty();
          tr = tr.next().append(copy);
          self.addItem(copy);
        }
      });
      if (self.sortable) {
        self.table.find('td').sortable(this.sortable);
      }
      this.sortItems();
      this.refresh();
    },
    unselect: function() {
      this.table.find('.' + this.selectableCls).removeClass(this.selectableCls);
    },
    sortItems: function(deep) {
      var self = this;
      $.each(this.items, function(i, e) {
        var r = e.parentEl.parent().index();
        var c = e.parentEl.index();
        e.sort = (r + 1) * self.cols + c;
        //e.sort = self.table.find('td').index(e.parentEl);
        deep && e.items && e.sortItems && e.sortItems(deep);
      });
      this.items.sort(function(a, b) {
        return a.sort - b.sort;
      });
    },
    doSortable: function() {
      //暂时gird布局的排序只在设计模式下启用
      if (!this.sortable || !this.designMode) {
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
        handle: '.drag-handle'
      };
      $.extend(cfg, self.sortable);
      
      $.each(this.sortableCmps, function(i, e) {
        e.contentEl.addClass(self.sortableCls);
      });
      
      if (self.sortConnect) {
        cfg.connectWith = self.el.find('.' + self.sortableCls);
      }
      this.el.find('.' + this.sortableCls).sortable("destroy");
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
    },
    addRow: function(position) {
      var cls = '.' + this.selectableCls;
      var self = this;
      position = position || 'after';
      var clone;
      if (typeof position === 'string') {
        cls = cls || '.ui-selected';
        var td = $('td' + cls).first();
        if (!td[0]) {
          return;
        }
        var tr = td.parent();
        //clone = tr.clone();
        //clone.children().empty();
        
        clone = $('<tr></tr>');
        for (var i = 0; i < this.cols; i++ ) {
          var td = $('<td></td>');
          clone.append(td);
        }
        if (position == 'before') {
          tr.before(clone);
        }
        else {
          tr.after(clone);
        }
        
        this.doDesign();
      }
      clone.children().each(function(i, e) {
        self.addItem(e);
      });
      this.sortItems();
      this.refresh();
    },
    addColumn: function(pos) {
      var self = this;
      this.table.find('tr').each(function(i, e) {
        var td = $('<td valign="top"></td>');
        if (pos == 'right') {
          td.appendTo(e);
        }
        else {
          td.prependTo(e);
        }
        
        //调整列宽的tr不加item
        if (!$(this).is("." + self.designCls)) {
          self.addItem(td);
        }
      });
      
      this.cols++;
      //同步cfg和layout的列数, 方便存储用      this.owner.layoutCfg.cols = this.cols;
      this.sortItems();
      this.refresh();
    },
    doTipMenu: function() {
      if (this.tip) {
        return;
      }
      var self = this;
      var menu = new YH.Menu({
        classes: ['menu-lv3'],
        data: [{
          text: "合并单元格",
          handleClick: function(node, t) {
            self.mergeCells();
            self.tip.hide();
          }
        }/*,{
          text: "拆分单元格",
          handleClick: function(node, t) {
            self.splitCells();
            self.tip.hide();
          }
        }*/,{
          text: "之前加入行",
          handleClick: function(node, t) {
            self.addRow('before');
            self.tip.hide();
          }
        },{
          text: "之后加入行",
          handleClick: function(node, t) {
            self.addRow('after');
            self.tip.hide();
          }
        },{
          text: "左边插入列",
          handleClick: function(node, t) {
            self.addColumn();
            self.tip.hide();
          }
        },{
          text: "右边插入列",
          handleClick: function(node, t) {
            self.addColumn('right');
            self.tip.hide();
          }
        },{
          text: "取消选择",
          handleClick: function(node, t) {
            self.unselect();
            self.tip.hide();
          }
        }],
        selClass: 'menu-selected',
        renderTo: 'body'
      });

      this.tip = new YH.Tip({
        event: 'rightClick',
        delay: 2,
        style: {
          width: '190px'
        },
        items: [menu],
        listeners: {
          'show': {
            'before': function(e, t) {
              if (!self.verifyMerge()) {
                t.items[0] && t.items[0].items[0].hide();
              }
              else {
                t.items[0] && t.items[0].items[0].show();
              }
            }
          }
        },
        target: self.el
      });
    },
    refresh: function() {
      this.el.empty();
      this.initComponent();
    }
  });
  
}) (jQuery);