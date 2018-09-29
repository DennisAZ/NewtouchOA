(function($){
  YH.TableOperation = function (cfg){
    var selCls = '.ui-selected';
    $.extend(true, this, cfg);
    this.table = $(this.table);
    //if (!table[0] || table[0].tagName != 'TABLE') return;
    this.id = this.table.attr('id');

    // ============================================================
    // private data
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
      
      dragColumns[no].style.width = parseInt(dragColumns[no].style.width) + w +'px';
      if (dragColumns[no+1]){
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
        'height': '30px',
        'background-color': 'gray',
        'width': '5px',
        'left': '100%',
        'z-index': '1',
        'cursor': 'w-resize'
      });
      $(dragColumns[i]).append(evt);
      evt.mousedown(self.startColumnDrag);
    }
  
    this.table.selectable(this.selectable);
  
    if (this.sortable) {
      this.table.find('td').sortable(this.sortable);
    }
    /**
    * 合并选中的单元格
    * @param append          是否将内容也合并
    * @param cls             选中的单元格class
    * @return
    */
    var mergeCells = function(append, cls) {
      if (!verifyMerge()) {
        return;
      }
      cls = cls || '.ui-selected';
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
         }
         else {
           if (append) {
             $(e).children().each(function(j, el) {
               td.append(el);
             });
           }
           $(e).remove();
         }
      });
    }
    
    function verifyMerge(cls) {
      cls = cls || '.ui-selected';
      var cells = 0;
      var td = self.table.find('td' + cls).first();
      var left = td.attr('cellIndex');
      var right = td.attr('cellIndex') + td.attr('colSpan');
      var top = td.parent().attr('rowIndex');
      var bottom = td.parent().attr('rowIndex') + td.attr('rowSpan');
      
      self.table.find('td' + cls).each(function(i, e) {
        var col = e.colSpan || 0;
        var row = e.rowSpan || 0
        cells += col * row;
        
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
    }
    
    /**
     * 拆分选中的单元格
     * @param cls             选中的单元格class
     * @return
     */
    var splitCells = function(cls) {
      cls = cls || '.ui-selected';
      var cols = 0, rows = 0, total = 0;
      $('td' + cls).eq(0).parent().children(cls).each(function(i, e) {
        cols += $(e).attr('colspan') || 0;
      });
      
      $('td' + cls).each(function(i, e) {
        var col = $(e).attr('colspan') || 0;
        $(e).attr('colspan', 1);
        for (;col > 1; col--) {
          var copy = $(e).clone(true).empty();
          $(e).after(copy);
        }
      });
      
      $('td' + cls).each(function(i, e) {
        var row = $(e).attr('rowspan') || 0;
        $(e).attr('rowspan', 1);
        var tr = $(e).parent();
        for (; row > 1; row--) {
          var copy = $(e).clone().empty();
          tr = tr.next().append(copy);
        }
      });
      if (self.sortable) {
        self.table.find('td').sortable(this.sortable);
      }
    }
  
    /**
     * 增加行
     * @param position        增加新行的位置[数字, 'before', 'after']
     *                        数字代表行数
     *                        before是在选中的行前加入
     *                        after是在选中的行后加入
     * @param cls             单元格选中的样式
     */
    var addRow = function(position, cls) {
      position = position || 'after';
      if (typeof position === 'number') {
        if (this.table[0] && this.table[0].rows.length >= position) {
          var tr = this.table.find('tr').eq(position);
          var clone = tr.clone();
          clone.children().empty();
          tr.after(clone);
        }
        else {
          var tr = this.table.find('tr').eq(0);
          var clone = tr.clone();
          clone.children().empty();
          this.table.append(clone);
        }
      }
      else if (typeof position === 'string') {
        cls = cls || '.ui-selected';
        var td = $('td' + cls).first();
        if (!td[0]) {
          return;
        }
        var tr = td.parent();
        var clone = tr.clone();
        clone.children().empty();
        if (position == 'before') {
          tr.before(clone);
        }
        else {
          tr.after(clone);
        }
      }
    }
    
    function unselect() {
      self.table.find(selCls).removeClass(selCls.replace(".", ""));
    }
    
    return {
      mergeCells: mergeCells,
      splitCells: splitCells,
      addRow: addRow,
      unselect: unselect,
      verifyMerge: verifyMerge
    };
  }
})(jQuery)