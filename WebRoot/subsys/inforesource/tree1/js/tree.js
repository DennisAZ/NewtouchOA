String.prototype.endWith = function(oString){
    var reg=new RegExp(oString+ "$");
    return reg.test(this);
  } 
  

var selectedData = '';
function showTree(t, i) { 
  showTreeInner(t, i);
  function showTreeInner(t, i) {
    var e = trData[i];
    if (e.items.length > 0 && $(t).data('init') != 1) {
      $("#" + e.id).jstree({ 
        "json_data" : {
          "data" : e.items
        },
        'callback' : {      
          'onselect' : function(node,tree_obj){
          }
        },
        "types" : {
          "max_depth" : -2,
          "max_children" : -2,
          "valid_children" : [ "drive" ],
          "types" : {
            // 值域类型
            "default" : {
              "valid_children" : "none",
              "icon" : {
                "image" : contextPath +  "/subsys/inforesource/tree1/imgs/node_lv3.jpg"
              }
            },
            // 子元素            "nodeLv1" : {
              "valid_children" : [ "default", "folder" ],
              "icon" : {
                "image" : contextPath + "/subsys/inforesource/tree1/imgs/node_lv1.jpg"
              }
            },
            // 子元素的子元素（保留）            //值域
            "nodeLv3" : {
              "valid_children" : [ "default", "folder" ],
              "icon" : {
                "image" : contextPath + "/subsys/inforesource/tree1/imgs/node_lv3.jpg"
              }
            }
          }
        },
        "plugins" : [ "themes", "json_data" , "ui" , "checkbox" , "crrm" , "types" ]
      }).bind("click.jstree", getSelected ).hide();
      $(t).data('init', 1);
    }
    $.each(selectedData.split(","), function(strIndex, str){
      if (str) {
        var el;
        var el1 = $("#" + e.id + ' #' + str);
        var el2 = $("#" + e.id + ' #' + str.replace(new RegExp('-[0-9a-zA-Z]+'),'-'));

        if (el1 && el1.length) {
          el = el1;
        }
        else if (el2 && el2.length) {
          el = el2;
        }

        if (el) {
          $("#" + e.id).jstree('check_node', el);
          if (el.attr('id').endWith('-')) {
            initInput(el.children('a'), str.replace(new RegExp('[0-9a-zA-Z]+-'),''));
          }
        }
      }
    });

    $.each($("#" + e.id + " a.input"), function(i, e){
      $(e).bind('click', function(){
        initInput($(e));
      }); 
    });
    if ($(t).data("show") == '1'){
      $(t).next("div").hide(500);
      $(t).css({
        'background': 'url('+ contextPath +'/subsys/inforesource/tree1/imgs/tree_header.jpg) no-repeat 0 0'
      });
      $(t).data("show", '0');
    }
    else{
      $(t).next("div").show(500);
      $(t).css({
        'background': 'url('+ contextPath +'/subsys/inforesource/tree1/imgs/tree_header_expand.jpg) no-repeat 0 0'
      });
      $(t).data("show", '1');
    }
  }
}

function nextjsp(para){     
  setNode(para);//给父窗口赋值   
  freshContent(); 
} 

function ajax(){     
    $.ajax({
      url:contextPath + "/yh/subsys/inforesouce/act/YHMateTreeAct/findMymoth.act",
      dataType:"json",
      success:function(data){   
        window.trData = data;        
        onload(data);       
      }
    });
}



function onload(json){  
  $("#demo").empty();
  for (var i = 0; i < json.length; i++){
    var e = json[i];
    $("#demo").append('<div class="tree_header" onclick="showTree(this, ' + i + ');"><span>' + e.name + '</span></div><div id="' + e.id + '" class="demo"></div>');
  }
}


function getSelected(){
  para = '';
  
  $.each($('#demo .jstree-checked'), function(i, e){//迭代选中所有的
    if ($(e).find('input')[0]) {
      para += ',' + e.id + $(e).find('input')[0].value;
    }
    else {
      para += ',' + e.id;
    }
  });
  
  $.each($('#demo .jstree-undetermined'), function(i, e){//迭代选中所有的值域
    para += ',' + e.id;
  });
  nextjsp(para.trim());
}

/**
 * 初始化树(自动选中文件的元数据)
 */ 
function  initMateTree(guid){
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/findFileMate.act",
    data:"guid="+guid,        
    success:function(data){          
     var rtJson = eval("("+data+")");
     window.selectedData = rtJson.rtData;
    }
  });
}
    
/**
 * 处理键盘按键press事件
 */
function documentKeypress(e){
  var tagName = document.activeElement.tagName;

  if (tagName != 'INPUT') {
    return;
  }
  
  var currKey = 0;
  var e = e || event;
  currKey = e.keyCode || e.which || e.charCode;

  if (currKey == 13){
    getSelected();
  }
}

document.onkeypress = documentKeypress;


function selectMateTree(json) {//选择后返回
  for (var str in json.split(",")) {
    var el;
    var el1 = $('#' + str);
    var el2 = $('#' + str.replace(new RegExp('-[0-9a-zA-Z]+')));
    if ($('#' + str)) {
      el = el1;
    }
    else if ($('#' + str.replace(new RegExp('-[0-9a-zA-Z]+')))) {
      el = el2;
    }
    $.each($('#demo > div'), function(i, e){
      $(e).jstree('select_node',el, true);
    });
  }
}

function initInput(el, value) {
  var val = '';
  if (value) {
    val = value;
  }
  if (el.data("input") != '1' && el.data("input") != '0'){
    
    el.after('<div class="node_input"><input type="text" onblur="getSelected()" value="' + val + '" name="' + el.parent()[0].id + '-input"></div>');
    el.data("input","1");
  }
  else {
    if (el.data("input") == '1'){
      el.next(".node_input").hide();
      el.data("input", "0");
    }
    else {
      el.next(".node_input").show();
      el.data("input", "1");
    }
 }
}


  
  