/**
 * 1.解决即时鼠标放在菜单上时，只显示一会儿问题 update by lh 2010-3-4
 */
var Menu = Class.create();
Menu.prototype = {
  /**
   * parameters,divStyle
   * parameters:{bindTo,menuData,attachCtrl,requestUrl,hasScoll}
   * bindTo 字符串 指定绑定的控件id
   * attachCtrl boolean 是否显示在绑定的控件的下方
   * requestUrl 字符串 指定异步加载菜单项的地址
   * hasScoll boolean 当弹出菜单的高度大于300是否出滚动条
   * menuData 对象数组 指定menu每一项的数据
   *    [{name:,action:,icon:,extData:},.....]
   *    name 字符串 每一项的label
   *    action 函数 单击每一项时执行的函数
   *    icon 字符串 每一项的图片地址，可以没有
   *    extData 任一类型 第一项所附带的数据，可以没有
   *    
   * divStyle:{border:'',width:'',backgroundColor:'',padding:''}
   * 其中每一属性和css的样式对应
   */
  initialize: function(parameters , divStyle) {
    var isIE=!!window.ActiveXObject;
    this.isIE6=isIE&&!window.XMLHttpRequest;
    this.bindTo = parameters.bindTo ? parameters.bindTo:document.body;
    this.menuData = parameters.menuData ;
    this.extData = parameters.extData;
    this.attachCtrl = parameters.attachCtrl;
    this.requestUrl = parameters.requestUrl;
    this.hasScoll = parameters.hasScoll ? parameters.hasScoll : false;
    this.closeMenuEvent = this.closeMenuHandler.bindAsEventListener(this);
    this.setTimeMenuEvent = this.setTimeHandler.bindAsEventListener(this);
    this.showMenuEvent = this.showMenuHandler.bindAsEventListener(this);
    
    this.divStyle = divStyle ? divStyle : {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',padding:'5px',display:"block"};
  },
  show:function(event){
    var event = event || window.event;
    Event.stop(event);
    this.createMenuDiv(event);
    document.observe('click', this.closeMenuEvent);
  },
  
  createMenuDiv:function(event){
    if( $('RightMenu')){
      document.body.removeChild($('RightMenu'));
    }
    if (this.isIE6) {
      if ($('menuIframe')) {
        document.body.removeChild($('menuIframe'));
      }
    }
    this.div = document.createElement("div");
    this.div.id = "RightMenu";
    this.div = $(this.div);
    
    this.div.style.zIndex = 99;
    if(!this.attachCtrl){
      var locate = this.mousePosition(event);
    }else{
      var locate = this.getElCoordinate($(this.bindTo));
      locate.y += $(this.bindTo).offsetHeight;
    }
    this.div.setStyle(this.divStyle);
    this.div.style.top = locate.y+"px";
    this.div.style.left = locate.x+"px";
    
    //可果menudata为空的话，才用url的方式加载
    if (!this.menuData) {
      this.getMenuData();
    }
    for(var i = 0 ; i < this.menuData.length;i++ ){
      var item = this.menuData[i];
      this.addItem(item);
    }
    //this.setTimeHandler();
    $(this.div).observe('mouseover', this.showMenuEvent);
    $(this.bindTo).observe('mouseout', this.setTimeMenuEvent);
    $(this.div).observe('mouseleave', this.closeMenuEvent);
    document.body.appendChild(this.div);
    if (this.hasScoll) {
      if (this.div.clientHeight > 300) {
        this.div.style.height = 300 + "px";
        this.div.style.overflow = "scroll";
      }
    }
    if (this.isIE6) {
      this.createShim(locate);
    }
  },
  getMenuData:function() {
    if (!this.requestUrl) {
      return ;
    }
    var loadingImg = "<div align=center><img src='"+ imgPath + "/dtree/loading.gif'></div>";
    this.div.update(loadingImg);
    var json = getJsonRs(this.requestUrl);
    if (json.rtState == '0') {
      this.div.update("");
      this.menuData = json.rtData;
    } else {
      this.menuData = [{name:"无数据"}];
    }
  },
  setTimeHandler:function(){
    window.closeTime = setTimeout(this.closeMenuHandler.bind(this),800); 
  },
  showMenuHandler:function(){
    clearTimeout(window.closeTime); 
  },
  addItem:function(item){
    if(item == "-"){
      this.div.appendChild(document.createElement("hr"));
    }else{
      var name = item.name;
      var icon = item.icon ? item.icon : "";
      var action = item.action;
      var div = document.createElement('div');
      div.style.cursor = "pointer";
      div.style.backgroundColor='#FFFFFF';
      div.onmouseover = function(){
        this.style.backgroundColor='#D6E4EF';
      }
      div.onmouseout = function(){
        this.style.backgroundColor='#FFFFFF';
      }
      var bindTo = this.bindTo;
      if (action) {
        $(div).observe('click',action.bindAsEventListener(this,bindTo,item.extData,item));
      }
      
      $(div).observe('mouseover', this.showMenuEvent);
      if(icon){
        var img  = document.createElement('img');
        img.src = icon;
        div.appendChild(img);
      }
      div.innerHTML += name;
      this.div.appendChild(div);
    }
  },
  closeMenuHandler:function(){
    if (this.div != null && this.div == $('RightMenu')){
      document.body.removeChild(this.div);
    }
    if (this.isIE6) {
      if (this.shim != null && this.shim == $('menuIframe')){
        document.body.removeChild(this.shim);
      }
      this.shim = null;
    }
    this.div = null;
    if (this.setTimeMenuEvent) {
      if($(this.bindTo)){
        $(this.bindTo).stopObserving('mouseout', this.setTimeMenuEvent);
      }
    }
    if (this.closeMenuEvent) {
      document.stopObserving('click', this.closeMenuEvent);
    }
    if(window.closeTime){
      clearTimeout(window.closeTime);
    }
  },
  mousePosition:function(ev){
    if(!ev) ev=window.event;
    if(ev.pageX || ev.pageY){
      return {x:ev.pageX, y:ev.pageY};
    }
    return {
      x:ev.clientX + document.documentElement.scrollLeft - document.body.clientLeft,
      y:ev.clientY + document.documentElement.scrollTop  - document.body.clientTop
    };
  },
  getElCoordinate:function(dom) {
    var t = dom.offsetTop;
    var l = dom.offsetLeft;
    dom=dom.offsetParent;
    while (dom) {
      t += dom.offsetTop;
      l += dom.offsetLeft;
    dom=dom.offsetParent;
    }; return {
      y: t,
      x: l
    };
  },
  createShim:function(locate) {
    this.shim = new Element("iframe");
    this.shim.id = "menuIframe";
    this.shim.scrolling = "no";
    this.shim.frameborder = "0"; 
    this.shim.src = contextPath + "/core/inc/emptyshim.html";
    this.shim.style.position = "absolute";
    this.shim.style.filter = "alpha(opacity=40)";
    this.shim.style.opacity = 0.4;
    this.shim.style.position = "absolute";
    this.shim.style.display = "block";
    this.shim.style.zIndex = 10;
    this.shim.style.top = locate.y+"px";
    this.shim.style.left= locate.x+"px";
    this.shim.style.width = this.div.clientWidth;
    this.shim.style.height = this.div.clientHeight;
    document.body.appendChild(this.shim);
  }
}