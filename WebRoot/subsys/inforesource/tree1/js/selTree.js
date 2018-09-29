var dataStr = "{'name':['张三', '李四'], 'address':['北京','南京'], 'org':[]}";
var dataPrvider;
var selTreeData1;


function  showSelTag(){
  dataPrvider = dataProvidor();
  selTreeData1 = parseData(dataPrvider);
  var obj = eval("(" + selTreeData1 +")");
  onloadSel(obj);
}
function getSelected2(){
   para = '';
  
  $.each($('#demo2 .jstree-checked'), function(i, e){//迭代选中所有的
      para += ',' +e.id+ '-'+ $(e).find('a').text();
  });
  
  $.each($('#demo2 .jstree-undetermined'), function(i, e){//迭代选中所有的值域
    para += ',' + e.id;
  });
  nextjsp2(para);
}

function test(obj){
  alert(obj.title);
}

function nextjsp2(para){
 alert(para+"\n调用袁工的借口");
} 

function dataProvidor(){
  var ret;
  $.ajax({
    url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/personTag.act",
    async: false,
    success:function(data){  
     var t = eval("(" + data +")");
     ret = t;
    }, 
    error:function(n,e){
      alert(e);
    }
  });
  return ret;
}

function  parseData(dataPrvider){
  if(dataPrvider){
    var pname = dataPrvider.name;
    var paddr = dataPrvider.address;
    var porg  = dataPrvider.org;
    var name = "{'name':'人名',";
        name += "'id':'M26',";
        name += "'items':[";
          for(var i=0; i<pname.length; i++){
            name += "{";
            name += "'attr':{";
            name +=   "'id':'M26',";
            name +=   "'rel':'nodeLv1'";
            name +=  "},";
            name += "'data':{";
            name += "      'title':'" + pname[i] + "',";
            name += "      'attr':{'href':'#'}";
            name += " }";
            if(i == pname.length-1){
              name += " }";
            }else{
              name += " },";
            }
          }
        name += "]";    
        name += "}";
        
        var addr = "{'name':'地名',";
        addr += "'id':'M27',";
        addr += "'items':[";
          for(var i=0; i<paddr.length; i++){
            addr += "{";
            addr += "'attr':{";
            addr +=   "'id':'M27',";
            addr +=   "'rel':'nodeLv1'";
            addr +=  "},";
            addr += "'data':{";
            addr += "      'title':'" + paddr[i] + "',";
            addr += "      'attr':{'href':'#'}";
            addr += " }";
            if(i == paddr.length-1){
              addr += " }";
            }else{
              addr += " },";
            }
          }
          addr += "]";    
          addr += "}";   
          
          var org = "{'name':'组织机构名',";
          org += "'id':'M28',";
          org += "'items':[";
            for(var i=0; i<porg.length; i++){
              org += "{";
              org += "'attr':{";
              org +=   "'id':'M27',";
              org +=   "'rel':'nodeLv1'";
              org +=  "},";
              org += "'data':{";
              org += "      'title':'" + porg[i] + "',";
              org += "      'attr':{'href':'#'}";
              org += " }";
              if(i == porg.length-1){
                org += " }";
              }else{
                org += " },";
              }
            }
            org += "]";    
            org += "}";   
            selTreeData1 = "[" + name + "," + addr  + "," + org +"]"; 
  }
  return selTreeData1;
}


function onloadSel(json){  
  window.seltrData = json;
  $("#demo2").empty();
  for (var i = 0; i < json.length; i++){
    var e = json[i];
    $("#demo2").append('<div class="tree_header" onclick="showTree3(this,' + i + ');"><span>' + e.name + '</span></div><div id="' + e.id + '" class="demo"></div>');
  }
}


function showTree3(t, i) { 
  showTreeInner(t, i);
  function showTreeInner(t, i) {
    var e = seltrData[i];
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
            // 子元素
            "nodeLv1" : {
              "valid_children" : [ "default", "folder" ],
              "icon" : {
                "image" : contextPath + "/subsys/inforesource/tree1/imgs/node_lv1.jpg"
              }
            },
            // 子元素的子元素（保留）
            //值域
            "nodeLv3" : {
              "valid_children" : [ "default", "folder" ],
              "icon" : {
                "image" : contextPath + "/subsys/inforesource/tree1/imgs/node_lv3.jpg"
              }
            }
          }
        },
        "plugins" : [ "themes", "json_data" , "ui" , "checkbox" , "crrm" , "types" ]
      }).bind("click.jstree", getSelected2 ).hide();
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