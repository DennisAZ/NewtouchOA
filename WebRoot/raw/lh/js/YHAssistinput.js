/*
 * version 1.0
 * class:AssistInuput
 * 1.对象化形式,所有配置信息，放置到parameters中，提供缺省值设置,类中的方法，采用了封装形式写
 * 2.标准化了数据结构,与服务器交互统一采用json对象形式
 */
/*
     提供的数据对象

  {rtState:"0", rtMsrg:"成功返回!", rtData:{count:12,lis:[{id:12,string:'ddd'},{id:24:string:'ddss'}]}}
*/
var AssistInuput = Class.create();
AssistInuput.prototype = {
  /*{bindToId,requestUrl,showLength,func}
   * bindToId 字符串  所要邦定的输入控件
   * requestUrl 字符串 请求路径
   * showLength int 最多显示的条数
   * func 函数 超出最多显示条数时点击'...'所执行的函数
   */
  initialize:function(parameters) {
    this.bindToId = parameters.bindToId;
    if(!this.bindToId||!$(this.bindToId)||$(this.bindToId).type != 'text'){
      alert('找不到指定的input标签或者指定的控件不是输入框');
      return ;
    }
    this.requestUrl = 
      parameters.requestUrl ? parameters.requestUrl : contextPath+'/raw/lh/assistinput/YHAssistInputAct/assistInput.act?str=';
    if(typeof parameters.func != 'function'){
      alert('指定的参数func类型不是函数')
      return ;
    }
    this.func = parameters.func;
    this.showLength = parameters.showLength ? parameters.showLength:10;
    this.bindInput = $(this.bindToId);
    this.blurEvent = this.blurHandler.bindAsEventListener(this);
    this.assistEvent = this.assist.bindAsEventListener(this);
    Event.observe(this.bindInput, "keyup", this.assistEvent );
    Event.observe(this.bindInput, "focus", this.assistEvent );
    Event.observe(this.bindInput, "blur", this.blurEvent );
    this.assistDivEncode = this.bindToId + "-div-assist";
    this.singleItemEncode = this.bindToId + "-div-";
    this.assistDiv = null;
    this.loadImage = null;
    this.loadingEncode = "load-image";
    this.loading = imgPath + "/cmp/asstinput/loading.gif";
    this.mouseoverClass =  "div-onmouseover";
    this.selectedId = -1;
  },
  assist:function(e){
    if(!this.bindInput.value){
      if(this.assistDiv){
        this.assistDiv.style.display = "none";
      }
      return ;
    }
    var e = e||event;
    　 var currKey = e.keyCode||e.which||e.charCode;
     if(currKey == 38||currKey == 40){
       if(!this.assistDiv && this.assistDiv.style.display == "none"){
         return ;
       }
       var isUp = false;
       if(currKey == 38){
         isUp = true;
       }else{
         isUp = false;
       }
       this.chageSelection(isUp); 
       return ;
     }
    if(currKey == 13){
      if((!this.assistDiv
              && this.assistDiv.style.display=="none")
              || this.selectedId == -1){
         return ;
       }
      if(this.selectedId >= this.showLength){
        this.func(this.bindInput.value);
        return;
      }else{
        this.bindInput.value = $(this.singleItemEncode + this.selectedId).name;
        return ;
      }
    }
    if(!this.assistDiv){
      var left = this.bindInput.offsetLeft;
      var top = this.bindInput.offsetTop + this.bindInput.offsetHeight;
      this.assistDiv = document.createElement("div");
      with(this.assistDiv){
        style.position = "absolute";
        style.border = "1px solid black";
        style.width = "200px";
        id = this.assistDivEncode;
      }
      document.body.appendChild(this.assistDiv);
      this.showDiv();
    }else{
      this.showDiv();
      this.assistDiv.style.display = "";
    }
    var childs =  this.assistDiv.getElementsByTagName("div");
    for(var i = childs.length-1 ;i >= 0 ;i--){
      this.assistDiv.removeChild(childs[i]);
    }
    
    if(!this.loadImage){
      this.loadImage = document.createElement("img");
      this.loadImage.src = this.loading;
      this.loadImage.id = this.loadingEncode;
      this.assistDiv.appendChild(this.loadImage);
    }
    this.getList();
  },
  getList:function(){
	var par = encodeURIComponent(this.bindInput.value);
    var url = this.requestUrl + par + "&length=" + this.showLength ;
    var json = getJsonRs(url);
    if(this.loadImage){
      this.assistDiv.removeChild(this.loadImage);
      this.loadImage = null;
    }
    if(json.rtState != '0'){
      alert(json.rtMsrg);
      return ;
    }
    var count = json.rtData.count;
    if(count<1){
      this.assistDiv.style.display = "none";
      return ;
    }
    var lis =  json.rtData.lis;
    var i = 0;
    for(; i < lis.length; i++){
      var li = lis[i];
      var d1 = $(this.singleItemEncode + i);
      if(!d1){
        var id = li.id;
        var str = li.string;
        var d = document.createElement("div");
        d.id = this.singleItemEncode + i;
        d.name = str;
        $(d).observe("mouseover", this.mouseoverHandler.bindAsEventListener(this,i));
        $(d).observe("mousedown", this.mousedownHandler.bindAsEventListener(this));
        d.onmouseout = function(){
          this.className = "";
        }
        d.appendChild(document.createTextNode(str));
        this.assistDiv.appendChild(d);
      }
    }
    if(parseInt(count) > this.showLength){
      var d1 = $( this.singleItemEncode + i);
      if(!d1){
        var d = document.createElement("div");
        d.id = this.singleItemEncode + i;
        $(d).observe("mouseover", this.mouseoverHandler.bindAsEventListener(this,i));
        d.onmouseout = function(){
          this.className = "";
        }
        var a = document.createElement("a");
        a.setAttribute("href", "#");
        a.appendChild(document.createTextNode("...."));
        d.appendChild(a);
        this.assistDiv.appendChild(d);
      }
    }
    this.selectedId = -1;
  },
  blurHandler:function(e){
    this.selectedId = -1;
    if(this.assistDiv){
      this.assistDiv.style.display = "none";
    } 
  },
  chageSelection:function(isUp){
    var divs = this.assistDiv.childNodes;
    if (isUp){
      this.selectedId--;
    }
    else{
      this.selectedId++;
    }
    if(this.selectedId<0){
      this.selectedId = divs.length-1;
    }
    if(this.selectedId>=divs.length){
      this.selectedId = 0;
    }
    for(var i=0;i<divs.length;i++){
      if(divs[i].id.substr(this.singleItemEncode.length)==this.selectedId){
        divs[i].className = this.mouseoverClass;
      }else{
        divs[i].className = "";
      }
    }
  },
  showDiv:function(){
    if(document.all){
      this.showIEDiv(this.bindInput,this.assistDiv);
    }else{
      this.showFFDiv(this.bindInput,this.assistDiv);
    }  
  },
  showFFDiv:function(o,div){
    var nLt = 0;
    var nTp = 0;
    var offsetParent = o;
    while (offsetParent!=null && offsetParent!=document.body) {
      nLt += offsetParent.offsetLeft;
      nTp += offsetParent.offsetTop;
      offsetParent=offsetParent.offsetParent;
    }
    var height = nTp + o.offsetHeight;
    div.style.top = height + "px";
    div.style.left = nLt + "px";
  },
  showIEDiv:function(obj,div) {
    var el = obj;
    var left = obj.offsetLeft;
    var top = obj.offsetTop;
    while (obj = obj.offsetParent) {
      left += obj.offsetLeft;
      top += obj.offsetTop;
    }
    div.style.pixelLeft = left;
    var height = top + el.offsetHeight;
    div.style.pixelTop = height;
  },
  mouseoverHandler:function(){
    this.selectedId = arguments[1];
    var divs = this.assistDiv.childNodes;
    for(var i=0; i<divs.length; i++){
      if(divs[i].id.substr(this.singleItemEncode.length) == this.selectedId){
        divs[i].className = this.mouseoverClass;
      }else{
        divs[i].className = "";
      }
    }
  },
  mousedownHandler:function(){
    this.bindInput.value = $(this.singleItemEncode + this.selectedId).name;
  }
}