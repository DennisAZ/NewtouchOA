var actionPath = contextPath + "/yh/core/funcs/system/act/YHSystemAct";
var D = Ext.lib.Dom;
/*
var menuData = [{name:'menuName',attachCtrl:true,isHaveChild:true,childMenu:[
       { name:'ddd',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
       , '-'
       , {name:'dd',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
       , {name:'sss',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
       , '-'
       ,{name:'dssss',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
]},{name:'dddd',isHaveChild:true,attachCtrl:true,childMenu:[{name:'adad',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                          , '-'
                                          , {name:'dada',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                          , {name:'dafdas',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                          , '-'
                                          , {name:'daf',action:test,icon:'/yh/raw/lh/rightmenu/image/addStep.gif',extData:'d'}
                                          ]}
,{name:'dddddd',isHaveChild:false,address:''}];
*/
function doInit(){
  doResize();
  var url = actionPath + "/getMenu.act";
  
  var json = getJsonRs(url);
  
  if(json.rtState == "0"){
    createMenu(json.rtData);
  }
}
function createMenu(menuData){
  var navTrees = $('navTrees');
  var ul = document.createElement('ul');
  navTrees.appendChild(ul);
  for(var i = 0 ; i < menuData.length ; i++){
    var menu = menuData[i];
   // "<li style="display:none;"><a href="#">浮动菜单1</a> <img src="./img/navdown.gif" align="absmiddle"/></li>"
    var li = document.createElement('li');
    with(li){
      id = 'yhFunc-' + i;
      //style.display = 'none';
    }
    var a = document.createElement("a");
   
    a.innerHTML = menu.name;
    a.href = "javascript:emptyFunc()";
    var isHaveChild = menu.isHaveChild;
    if(!isHaveChild){
      $(a).observe('click',open.bindAsEventListener(this,menu.address));
      li.appendChild(a);
      ul.appendChild(li);
    }else{
     //<img src="./img/navdown.gif" align="absmiddle"/> 
      var img = document.createElement("img");
      img.src = imgPath + "/frame/navdown.gif";
      img.align = "absmiddle";
      img.id = "yhFuncImg-" + i ;
      li.appendChild(a);
      li.appendChild(img);
      ul.appendChild(li);
      setEventHandler(li, menu.childMenu , menu.attachCtrl);
      
    }
  } 
}
function setEventHandler(node, data ,attach){
  node.onclick = function(event){
    var menu = new Menu({bindTo:node , menuData:data , attachCtrl:attach});
    menu.show(event);
  }
  
}

function open(){
  $("mainFrame").src = arguments[1];
}

//更换NAV导航菜单 
function DisplayNav(){ 
  var tagDom = document.getElementById('navTrees'); 
  var arrNavs = tagDom.getElementsByTagName('li'); 
  //初始化 
  //if(!Get()){Set('1');} 
  local = local >= Math.ceil((arrNavs.length)/length) ? 1 : local+1; 
  var nextOrder = local; 
  var nextOrderStart = (parseInt(local)-1)*length; 
  var nextOrderEnd = parseInt(local)*length-1 > arrNavs.length ? arrNavs.length : parseInt(local)*length-1; 
  for(i=0;i<arrNavs.length;i++){ 
    if(nextOrderStart <= i && nextOrderEnd >= i){ 
    arrNavs[i].style.display = ''; 
    }else if(nextOrderStart > i || i>nextOrderEnd){ 
      arrNavs[i].style.display = 'none'; 
    } 
  } 
}
function test(){
  $("mainFrame").src = arguments[2]; 
}


function doLoginOut(){
  window.location = actionPath + "/doLoginOut.act";
}
function getHeight() {
  var topRegion = D.getRegion($("header"));
  var rtHeight = D.getViewportHeight() - topRegion.bottom;
  if (!Ext.isIE) {
    rtHeight = rtHeight - 10;
  }
  //rtHeight = rtHeight - 25;
  return rtHeight;
}
function getWidth() {
  var rtWidth = D.getViewportWidth();
  if (!Ext.isIE) {
    rtWidth = rtWidth - 25;
  }
  return rtWidth;
}
function doResize() {
  $("mainFrame").height = getHeight();
  $("mainFrame").width = getWidth();
  
}