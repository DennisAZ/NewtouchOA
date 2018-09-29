(function($) {
  var opts = {
    el: $('<div></div>'),
    border: '5px solid transparent',
    items: [],
    sortable: {
      revert: true,
      'tolerance': 'pointer'        //通过鼠标的位置计算拖动的位置*重要属性*
    },
    pixel: false,
    designMode: false,
    cellpadding: 0,
    cellspacing: 0,
    cmpCls: 'jq-columnlayout',
    sortableCls: 'column-sortable',
    droppableCls: 'column-droppable',
    droppableCmps: [],
    sortableCmps: [],
    style: {
      'position': 'relative',//重要!
      'overflow-y': 'auto',
      'overflow-x': 'hidden'
    }
  };
  YH.createComponent.call(YH.layouts, "ColumnLayout", opts, YH.layouts.AutoLayout.prototype, {
    initComponent: function() {
    
      this.table = $('<table></table>');
      this.table.attr('cellpadding', this.cellpadding);
      this.table.attr('cellspacing', this.cellspacing);
      this.el.append(this.table);
      this.renderCells();
      this.doSortable();
    },
    renderCells: function() {
      var self = this;
      var tr = $('<tr></tr>');
      this.table.append(tr);
      $.each(this.items, function(i, e) {
        var td = $('<td valign="top"></td>');
        
        var width;
        if (self.pixel) {
          if (e.columnWidth) {
            width = parseInt(e.columnWidth) +  'px';
          }
          else {
            width = 'auto'
          }
        }
        else {
          width = Math.floor((e.columnWidth || 0.5) * 100) + '%';
        }
        
        td.css({
          width: width
        });
        self.items[i] = e = self.renderItem(e, td);
        tr.append(td);
      });
      this.doDesign();
    },
    doDesign: function() {
      if (this.designMode) {
        
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
            
            self.items[i].columnWidth = Math.floor( $(dragColumns[i]).width() * 100 / self.table.width()) / 100;
            
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
          $(dragColumns[i]).prepend(evt);
          evt.mousedown(self.startColumnDrag);
        }
      }
    },
    doItem: function(e) {
      /**
       * 向子元素增加设置列宽度的函数
       */
      e.setColumnWidth = function(width) {
        e.parentEl.css({
          width: Math.floor((width || 0.5) * 100) + '%'
        });
      }
      e.leaf = true;
      //通过YH库的事件处理,避免绑定到jquery元素的事件找不到YH组件
      if (YH.isCmp(e)) {
        
        //只有可排序的列布局才会有下边距
        if (this.sortable) {
          e.contentEl.css({
            "padding-bottom": "100px"
          });
        }
        
        this.doDroppable(e);
        this.sortable && this.sortableCmps.push(e);
      }
    }
  });
  
}) (jQuery);