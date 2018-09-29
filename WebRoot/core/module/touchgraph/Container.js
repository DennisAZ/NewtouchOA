var Container = Class.create();
Container.prototype = {
  initialize: function(parameters) {
    //注册input事件
    this.bindToLine =  $(parameters.bindToLine);
    this.text = parameters.relation;
    this.showCalEvent = this.showCalHandler.bindAsEventListener(this);
    this.closeCalEvent = this.closeCalHandler.bindAsEventListener(this);
    this.bindToLine.observe('click', this.showCalEvent);
  },
  showCalHandler:function(e){
    Event.stop(e);
    this.createCalDiv(e);
    document.observe( 'click', this.closeCalEvent);
  },
  closeCalHandler:function(e){
    var node = $("calendarDiv");
    if (node) {
      document.body.removeChild(node) ;
    }
  },
  createCalDiv:function(e){
    this.closeCalHandler();
    this.div = new Element("div");
    with(this.div){
      id = "calendarDiv";
      style.display = "";
      style.zIndex = 103;
      style.position = "absolute";
    }
    document.body.appendChild(this.div);
    this.div.onclick = function(event){
      if (window.event) {
        window.event.cancelBubble=true;
      } else {
        event.stopPropagation();
      } 
    }
    var locate = this.mousePosition(e);
    var temp="<div style='background-color:#fff;border:1px solid #ADCD3C;width:150px;'>" 
      + this.getHeader()
      + this.text
    	+ "</div>";
    $(this.div).update(temp);
    try{
      this.div.setStyle({top:locate.y+"px",left:locate.x+"px",display:"block"});
    }catch(e){
      this.div.style.top = locate.top+"px";
      this.div.style.left = locate.left+"px";
      this.div.style.display = "block";
    }
    $('closeImage').observe('click',this.cancel.bindAsEventListener(this));
  },
  cancel:function(e){
    this.closeCalHandler();
  },
  getHeader:function() {
    return "<div id='headerDiv' align='right' style='padding-right:3px;padding-top:3px'><image id='closeImage' src='"+imgPath+"/close.png'/></div>"
  },
  //兼容各种浏览器的,获取鼠标真实位置
  mousePosition:function(ev){
    if(!ev) ev=window.event;
      if(ev.pageX || ev.pageY){
        return {x:ev.pageX, y:ev.pageY};
    }
    return {
      x:ev.clientX + document.documentElement.scrollLeft - document.body.clientLeft,
      y:ev.clientY + document.documentElement.scrollTop  - document.body.clientTop
    };
  }
}
//给DATE类添加一个格式化输出字串的方法

Date.prototype.format = function(format)   
{   
   var o = {   
      "M+" : this.getMonth()+1, //month  
      "d+" : this.getDate(),    //day  
      "h+" : this.getHours(),   //hour  
      "m+" : this.getMinutes(), //minute  
      "s+" : this.getSeconds(), //second  ‘

    //quarter  
      "q+" : Math.floor((this.getMonth()+3)/3), 
      "S" : this.getMilliseconds() //millisecond  
   }   
   if(/(y+)/.test(format)) format=format.replace(RegExp.$1,(this.getFullYear()+"").substr(4 - RegExp.$1.length));   
    for(var k in o)if(new RegExp("("+ k +")").test(format))   
      format = format.replace(RegExp.$1,   
        RegExp.$1.length==1 ? o[k] :    
          ("00"+ o[k]).substr((""+ o[k]).length));   
    return format;   
 } 
