//实时汇率的页面
var rateUrl = "http://www.safe.gov.cn/model_safe/tjsj/rmb_list.jsp?id=5&ID=110200000000000000";

var view = {
  activeRow: 0,
  rowsCount: 0,
  setActiveRow: function(no) {
    $("#grid tbody tr.active").removeClass("active");
    $("#grid tbody tr").eq(no).addClass("active");
    view.activeRow = no;
  },
  editRow: function(no) {
    resetOptRange();
    var row = formData.entries[no] || [];
    $("#itemsOpt > div").hide();
    row.itemId && $(".opt-range #" + row.itemId).show();
    $.each(row, function(k, v) {
      k && $(".opt-range #" + k).val(v);
    });
    view.setActiveRow(no);
  }
};

var subject = {
    
};

//表单数据
var formData = {
    date: "",
    entries: []
};

$(function() {
  initToolbar();
  initHeaderTools();
  $("#date").datepicker();
  
  //获取初始化数据
  var initUrl = contextPath + "/uifrm/data/voucher.js";
  $.ajax({
    url: initUrl,
    dataType: "json",
    success: function(json) {
      if (json.rtState == "0") {
        var data = json.rtData;
        
        //初始化摘要
        initSummay(data.summary);
        
        //初始化币别汇率
        initCurrency(data.currency);
        
        //初始化辅助核算
        initItems(data.items);
        
        //初始化表单元素事件
        initChangeEvt();
        
        //初始化键盘操作
        //initKeyboardOp();
        
        view.setActiveRow(0);
      }
      else {
        alertMsg("获取表单信息失败!");
      }
    },
    error: function() {
      alertMsg("初始化表单失败!");
    }
  });
});

function initSummay(tags) {
  $( "#summary" ).autocomplete({
    source: tags
  });
}

function initCurrency(array) {
  var currency = $("#currency");
  var map = {};
  $.each(array, function(i, e) {
    var option = $("<option></option>").text(e.currency).attr("value", e.currency).attr("rate", e.rate);
    currency.append(option);
    map[e.currency] = e.rate;
  });
  $("#rate").val(array ? array[0].rate : '');
  currency.change(function(e) {
    $("#rate").val(map[this.value] || '');
  });
}

function InputCmp(cfg) {
  var opts = {
    attributes: {
      type: "text",
      "class": "small resettable"
    },
    label: "",
    id: "",
    name: "",
    value: null,
    renderTo: null
  };
  var c = $.extend(true, opts, cfg);
  
  var el = $("<div class='form-element-cmp'></div>");
  var label = $("<label></label>");
  label.attr("for", c.id);
  label.text(c.label);
  el.append(label);
  var input = $("<input/>");
  
  $.each(c.attributes || [], function(k, v) {
    input.attr(k, v);
  });
  input.attr("id", c.id);
  input.attr("name", c.name);
  el.append(input);
  
  var unit = $("<span class='form-element-unit'></span>");
  
  unit.text(c.unit || "");
  el.append(unit);
  if (c.renderTo) {
    el.appendTo(c.renderTo);
  }
}

/**
 * 初始化键盘操作
 */
function initKeyboardOp() {
  $(document).keypress(function(e) {
    if (e) {
      
    }
  });
}

function initItems(items) {
  $("#subjectSel").click(function() {
    selectSubject({ 
      callback: function(e,treeId, treeNode) {
        $("#subject").val(treeNode.value);
        $("#subjectText").text(treeNode.text);
        refreshGrid();
        var item = items[treeNode.item];
        if (item && item.id) {
          $("#itemId").val(item.id);
          if ($("#itemsOpt #" + item.id).length) {
            $("#itemsOpt #" + item.id).show();
            return;
          }
          var el = $("<div id='" + item.id + "'></div>");
          $("#itemsOpt").append(el);
          $("#grid .temp").remove();
          $.each(item.elements || [], function(i, e) {
            InputCmp({
              renderTo: el,
              label: e.text,
              id: e.id,
              name: e.name,
              attributes: e.attributes || {}
            });
            if (e.viewInGrid && e.name) {
              addColumn(e.name, e.text);
            }
          });
          $(el).append("<div class='clear'></div>");
        }
      }
    });
    return false;
  });
}

/**
 * 增加数据列表一列
 */
function addColumn(name, text) {
  if (!$("." + name, $("#grid")).length) {
    $("#grid thead .rate").before("<td class='" + name + " temp'>" + text + "</td>");
    $("#grid tbody tr").each(function(i, e) {
      var td = $("<td></td>");
      td.addClass(name);
      td.addClass("temp");
      $(".rate", e).before(td);
    });
  }
}

function initChangeEvt() {
  $(".opt-range input:enabled").live("change", function() {
    refreshGrid();
  });
}

/**
 * 根据表单的数据更新表格
 */
function refreshGrid() {
  formData.entries[view.activeRow] = formData.entries[view.activeRow] || {};
  var row = formData.entries[view.activeRow];
  $(".opt-range input,.opt-range select").each(function(i, e) {
    if (e.name) {
     row[e.name] = e.value;
     if (e.name == "subject") {
       $("." + e.name, "#grid tbody tr.active").text(e.value + " " + $("#subjectText").text());
     }
     else {
       $("." + e.name, "#grid tbody tr.active").text(e.value);
     }
    }
  });
}

function nextRow() {
  saveRow();
  addOpts4Row(view.activeRow);
  view.setActiveRow(view.activeRow + 1);
}

/**
 * 保存一行
 */
function saveRow() {
  refreshGrid();
  //重置表单
  resetOptRange();
  $("#grid .temp").removeClass("temp").addClass("item");
  //辅助核算选项清空
  $("#itemsOpt > div").hide();
}

function initToolbar() {
  $("#tools").toolbar({
    btns: [{
      text: "保存并新增",
      icon: '',
      handler: function() {
        
      }
    }, {
      text: "保存",
      icon: '',
      handler: function(e, t, a) {
      }
    }, {
      text: "存为模板",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "新增",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "复制",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "删除",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "审核",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "上一条",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "下一条",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "跳转",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "查询",
      icon:'',
      handler: function(e, t, a) {
      }
    }, {
      text: "打印",
      icon:'',
      handler: function(e, t, a) {
      }
    }]
  });
}

function initHeaderTools() {
  $("#headerOpt").toolbar({
    btns: [{
      text: "计算器"
    }, {
      text: "实时汇率",
      handler: function() {
        if (!window.rateWin) {
          window.rateWin = $("<iframe></iframe>").attr("src", rateUrl).dialog({
            title: "实时汇率",
            width: 600,
            height: 400
          }).css({
            width: "100%"
          });
        }
        else {
          rateWin.dialog("open").css({
            width: "100%"
          });;
        }
      }
    }, {
      text: "快捷录入指南"
    }]
  });
}

function addOpts4Row(rowNo) {
  var opt = $(".opt", $("#grid tbody tr").eq(rowNo));
  if (opt.find(".add").length) {
    return;
  }
  var add = $("<a href='javascript:void(0)' class='add'></a>").append("<span>新增</span>");
  add.click(function() {
    
  });
  
  var del = $("<a href='javascript:void(0)' class='del'></a>").append("<span>删除</span>");
  del.click(function() {
    
  });
  
  var edit = $("<a href='javascript:void(0)' class='edit'></a>").append("<span>编辑</span>");
  edit.click(function() {
    view.editRow(rowNo);
  });
  
  opt.append(edit).append(del).append(add);
}

function resetOptRange() {
  $(".opt-range .resettable").each(function(i, e) {
    if (e.tagName == "INPUT" || e.tagName == "SELECT") {
      e.value = "";
    }
    else {
      e.innerHTML = "";
    }
  });
}