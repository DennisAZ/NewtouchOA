<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <title>jsTree v.1.0 - full featured demo</title>
  <script type="text/javascript" src="js/jquery.js"></script>
  <script type="text/javascript" src="js/jquery.cookie.js"></script>
  <script type="text/javascript" src="js/jquery.hotkeys.js"></script>
  <script type="text/javascript" src="js/jquery.jstree.min.js"></script>
  <link href="css/default/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
  <style type="text/css">
  html, body { 
    margin:0; padding:0; 
    font: normal 12px verdana;
  }
  body, td, th, pre, code, select, option, input, textarea { font-family:verdana,arial,sans-serif; font-size:12px; }
  .demo, .demo input, .jstree-dnd-helper, #vakata-contextmenu { font-size:12px; font-family:Verdana; }
  #container {
  overflow:hidden;
  position:relative;
}
  #demo {
  font: normal 13px verdana;
  width:auto;
}

  #menu { height:30px; overflow:auto; }
  #text { margin-top:1px; }

  #alog { font-size:9px !important; margin:5px; border:1px solid silver; }
  
  .tree_header {
    cursor: pointer;
    height:27px;
    background: url(imgs/tree_header.jpg) no-repeat left top;
  }
  
  a {
    outline-style:none;
  }
  
  a:link,a:visited,a:active,a:hover {
    text-decoration: none;
  }
  
  .tree_header span{
    position:relative;
    left:30px;
    top:8px;
  }
  
  .node_input {
    display:block;
    height:18px;
    width:135px;
    border:none;
    position:relative;
    top:1px;
    left:30px;
    background: url(imgs/input_text_long.jpg) no-repeat left top;
  }
  
  .node_input input{
    width:135px;
    display:block;
    border:none;
    background-color:transparent;
  }
  
  </style>
  <%
    String tree = (String)request.getAttribute("treeData");
  %>
  <script type="text/javascript"><!--
  String.prototype.endWith = function(oString){
    var reg=new RegExp(oString+ "$");
    return reg.test(this);
  } 
  
  var selectedData = '';
    function showTree(t, i){
      var e = data[i];

      if (e.items.length > 0 && $(t).data('init') != 1){
        $("#" + e.id).jstree({ 
          "json_data" : {
            "data" : e.items
          },
          'callback' : {      
            'onselect' : function(node,tree_obj){
              alert(1);
            }
          },
          "types" : {
            // I set both options to -2, as I do not need depth and children count checking
            // Those two checks may slow jstree a lot, so use only when needed
            "max_depth" : -2,
            "max_children" : -2,
            // I want only `drive` nodes to be root nodes 
            // This will prevent moving or creating any other type as a root node
            "valid_children" : [ "drive" ],
            "types" : {
              // 值域类型
              "default" : {
                // I want this type to have no children (so only leaf nodes)
                // In my case - those are files
                "valid_children" : "none",
                // If we specify an icon for the default type it WILL OVERRIDE the theme icons
                "icon" : {
                  "image" : "imgs/node_lv3.jpg"
                }
              },
              // 子元素


              "nodeLv1" : {
                // can have files and other folders inside of it, but NOT `drive` nodes
                "valid_children" : [ "default", "folder" ],
                "icon" : {
                  "image" : "imgs/node_lv1.jpg"
                }
              },
              // 子元素的子元素（保留）


             /*"nodeLv2" : {
                // can have files and folders inside, but NOT other `drive` nodes
                "valid_children" : [ "default", "folder" ],
                "icon" : {
                  "image" : "imgs/node_lv2.jpg"
                }
              }, */
              //值域
              "nodeLv3" : {
                // can have files and folders inside, but NOT other `drive` nodes
                "valid_children" : [ "default", "folder" ],
                "icon" : {
                  "image" : "imgs/node_lv3.jpg"
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
          var s = str.substring(0, str.indexOf('-') + 1);
          var el2 = $("#" + e.id + ' #' + s);
  
          if (el1 && el1.length) {
            el = el1;
          }
          else if (el2 && el2.length) {
            el = el2;
          }
  
          if (el) {
            $("#" + e.id).jstree('check_node', el);
            if (el.attr('id').endWith('-')) {
              initInput(el.children('a'), str.replace(new RegExp('[a-zA-Z0-9]+-'),''));
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
          'background-image': 'url(imgs/tree_header.jpg)'
        });
        $(t).data("show", '0');
      }
      else{
        $(t).next("div").show(500);
        $(t).css({
          'background-image': 'url(imgs/tree_header_expand.jpg)'
        });
        $(t).data("show", '1');
      }
    }
 function nextjsp(para){  
  window.parent.setNode(para);//给父窗口赋值   
 }

    function ajax(){     
        $.ajax({
          url:contextPath + "/yh/subsys/inforesouce/act/YHMateTreeAct/findMymoth.act",
          dataType:"json",
          success:function(data){   
            window.data = data;        
            onload(data);       
          }
        });
    }
    $(document).ready(function(){
      ajax();
    });
     //$(function(){       
      // Settings up the tree - using $(selector).jstree(options);
      // All those configuration options are documented in the _docs folder        
      function onload(json){  
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
        
        $.each($('#demo .jstree-undetermined'), function(i, e){//迭代选中所有的
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
--></script>
</head>
<body>
<div id="container">
<!-- the tree container (notice NOT an UL node) -->
<div id="demo" style="width:165px;overflow:hidden;"></div>
<!-- JavaScript neccessary for the tree -->


</div>
</body>
</html>