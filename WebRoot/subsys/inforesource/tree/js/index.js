function tomanage(){
  var url = contextPath + "/yh/subsys/inforesouce/act/YHMateShowAct/toManage.act";
  myleft=(screen.availWidth-500)/2;
  window.open(url,"read_news","height=800,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes"); 
}

function getIFrameDOM(id){//兼容IE、Firefox的iframe DOM获取函数
  return document.getElementById(id).contentDocument || document.frames[id].document;
}

function refreshMe(){
  var a = getIFrameDOM("treeSampleframe");
  a.location.reload();
}

function filtag(){//填写 自定义标签

  var url = contextPath + "/subsys/inforesource/tagname.jsp";   
   window.open(url, "window","height=200,width=350,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=220,top=180,resizable=no");
}
var jsonval = "";

function setJsonval(jsonval){
  this.jsonval=jsonval;
}
function getJsonval(){
  return jsonval;
}
/**
 * 树选择节点后，把值存放在这里
 */
var node="";
function setNode(node){  
  this.node=node;
}

function getNode(){
  return node;
}
//1,把iframe标签中的id获得    2,重新载入 Content.jsp 页面
function freshContent(){
  var treeNode = getNode();  
  if(treeNode){
    rightOpt(treeNode);
  }
  
  /*var a = getIFrameDOM("contentframe");
  a.location.reload(); //重新载入页面*/
}

/**
 * 树选择节点后，把值存放在这里
 */
 var seqId = "";
 var nodeIds = "";
function setNodeIds(seqId,nodeIds){
  seqId = seqId;  
  nodeIds = nodeIds;
}

function getChildMethod(){
  var a = getIFrameDOM("contentframe");
  a.location.reload(); //重新载入页面
}

$("document").ready(function(){
  $(".menu-r-input").bind("click",function(){   
    var modules = "";
  $(".menu-r-input").each(function(){
    if(this.checked == true){
      modules += this.value + ",";     
    }
  });
   setModules(modules);
   if(modules){
     leftOpt(modules);
   }   
});
});
var mods = "";
function setModules(mods){
  this.mods = mods;
}

function getModules(){
  return this.mods;
}

/**
 * 点击左侧的功能模块的功能
 */
function leftOpt(modules){
  //先判断有没有选择树,如果选择了树，则从选择得 列表中进行筛选，
  //如果没有选择树，则直接进行查询
  
  var treeNode = getNode();                     //看看选择树了么
  if(treeNode){  //没有选择树，则直接进行查询           
    window["contentframe"].modAjax(modules, treeNode);
  }else{     
    window["contentframe"].modAjax(modules, "");
  }
}
/**
 * 点击左边的树进行的操作
 */
function rightOpt(treeNode){
  var modules  = getModules(); 
  if(modules){  // 如果选择了左侧的功能列表   
    window["contentframe"].modAjax(modules, treeNode);
  }else{ 
    window["contentframe"].modAjax("", treeNode);
  }
}