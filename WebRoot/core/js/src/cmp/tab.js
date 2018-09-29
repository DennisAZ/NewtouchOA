 //一段可以判断浏览器类型和版本的代码
window["MzBrowser"]={};
(function() {
  if(MzBrowser.platform) return;
  var ua = window.navigator.userAgent;
  MzBrowser.platform = window.navigator.platform;

  MzBrowser.firefox = ua.indexOf("Firefox")>0;
  MzBrowser.opera = typeof(window.opera)=="object";
  MzBrowser.ie = !MzBrowser.opera && ua.indexOf("MSIE")>0;
  MzBrowser.mozilla = window.navigator.product == "Gecko";
  MzBrowser.netscape= window.navigator.vendor=="Netscape";
  MzBrowser.safari  = ua.indexOf("Safari")>-1;

  if(MzBrowser.firefox) var re = /Firefox(\s|\/)(\d+(\.\d+)?)/;
  else if(MzBrowser.ie) var re = /MSIE( )(\d+(\.\d+)?)/;
  else if(MzBrowser.opera) var re = /Opera(\s|\/)(\d+(\.\d+)?)/;
  else if(MzBrowser.netscape) var re = /Netscape(\s|\/)(\d+(\.\d+)?)/;
  else if(MzBrowser.safari) var re = /Version(\/)(\d+(\.\d+)?)/;
  else if(MzBrowser.mozilla) var re = /rv(\:)(\d+(\.\d+)?)/;

  if("undefined"!=typeof(re)&&re.test(ua))
    MzBrowser.version = parseFloat(RegExp.$2);
})();

/**
 * 1,加入height,如不指定,显示内容高度会自适应。update by lh
 * 2,去掉了才用数组的形式添加BUTTON，改为更加灵活的HTML字符串。这样就可以添加不仅仅是BUTTON，还可以添加其它html元素.update by lh 2010-2-23
 * 3,在categories中每个categorie添加属性isShow，通过此属性，可以指定标签页默认打开那个页面。update by lh 2010-3-29
 */
var isWorkFlow = false;
function buildTab(categories, container, height , rightContent){
  var tabObj = new YHJsTab({catArray: categories, container: container , divHeight:height , rightDivContent:rightContent});
  tabObj.show();
  return tabObj;
}

var YHJsTab = Class.create();
YHJsTab.prototype = {
  initialize: function(cfgs) {
    this.config(cfgs);
    document.body.onresize = this.resetSize.bind(this);
  },
  config: function(cfgs) {
    this.catArray = cfgs.catArray;
    this.divHeight = cfgs.divHeight;
    this.container = $(cfgs.container) ? $(cfgs.container) : document.body;
    this.useMulContenDiv = cfgs.useMulContenDiv === false ? false : true;
    this.rightDivContent = cfgs.rightDivContent ? cfgs.rightDivContent : "";
    this.isShow = false;
    this.contentArear = null;
    this.currContentDiv = null;
  },
  /**
   * 自适应大小
   */
  resetSize: function() {
    if (!this.contentArear) {
      return;
    }
    var ajustWidth = 0;
    var ajustHeight = 40;
    if (!Prototype.Browser.IE) {
      ajustWidth = 8;
      ajustHeight = 43;
    }else if (Prototype.Browser.IE && MzBrowser.version == 6) {
      ajustWidth = 10;
      ajustHeight = 40;
    }
    this.container.style.overflow = "hidden";
    this.container.style.width = (document.viewport.getDimensions().width - ajustWidth - 10) + "px";
    var topPos = this.container.style.top ? parseInt(this.container.style.top.replace("px", "")) : 0;
    this.container.style.height = (document.viewport.getDimensions().height - topPos - 30) + "px";
    if (isWorkFlow) {
      if (this.contentArear.tagName.toLowerCase() != "body") {
        this.contentArear.style.position = "relative";
        this.contentArear.style.left = "0px";
        this.contentArear.style.top = "0px";
        this.contentArear.style.width = "0px";
        this.contentArear.style.height = "0px";
      }
    }else {
      if (this.contentArear.tagName.toLowerCase() != "body") {
        this.contentArear.style.position = "absolute";
        this.contentArear.style.left = "0px";
        this.contentArear.style.top = "37px";
        this.contentArear.style.border = "none";
        this.contentArear.style.width = (document.viewport.getDimensions().width - ajustWidth) + "px";
        this.contentArear.style.height = (document.viewport.getDimensions().height - ajustHeight) + "px";
      }
    }
    var iFrame = $("contentFrame");
    if (iFrame && iFrame.style.display == "block") {
      iFrame.style.position = "absolute";
      iFrame.style.left = "0px";
      iFrame.style.top = "0px";
      iFrame.style.width = (document.viewport.getDimensions().width - ajustWidth) + "px";
      iFrame.style.height = (document.viewport.getDimensions().height - ajustHeight) + "px";
      iFrame.style.border = "none";
      //iFrame.width = "100%";
      //iFrame.height = "100%";
    }
  },
  /**
   * 显示标签页

   */
  show: function() {
    if (this.isShow) {
      return;
    }
    var categories = this.catArray;
    var titleDiv = document.createElement("div");
    titleDiv.setAttribute("id", "title");
    titleDiv.className = "navPanel";
    this.container.appendChild(titleDiv);
    
    //默认显示第一页

    var firstTitle = categories[0].title;
    this.showPage = 0 ;
    
    for(var i = 0; i < categories.length; i++) {
      var titleLink = document.createElement("a");
      titleLink.setAttribute("href","javascript:void(0);");

      var title = categories[i].title;
      titleDiv.appendChild(titleLink);
      titleLink.title = title;
      titleLink.id = i;
      titleLink.onclick = this.doClickTab.bind(this, title, i);
     
      var span = document.createElement("span");
      span.width = 80;
      var imgUrl = categories[i].imgUrl;
      //取消图片
      imgUrl = null;
      if(imgUrl){
        var img = document.createElement("img");
        img.width = 16;
        img.height = 16;
        img.setAttribute('src', imgUrl);
        span.appendChild(img);
      }
          
      span.innerHTML += "&nbsp;" + categories[i].title;
      if (categories[i].title.length > 7) {
        span.style.width = categories[i].title.length * 15 + "px";
      }
      if (categories[i].title.length > 11) {
        span.style.width = categories[i].title.length * 12 + "px";
      }
      titleLink.appendChild(span);
      
      //是否默认显示此页
      if (categories[i].isShow) {
        firstTitle = categories[i].title;
        this.showPage = i;
      }
      span.appendChild(document.createTextNode("  "));
    }
   
    //画右边div主要用来装一些html代码
    var rightDiv = document.createElement("div");
    rightDiv.className = 'rightDiv';
    rightDiv.style.paddingTop = '4px';
    titleDiv.appendChild(rightDiv);
    $(rightDiv).update(this.rightDivContent);
    //画出内容栏

    var contentArear = document.createElement("div");
    this.contentArear = $(contentArear); 
    //contentArear.className = "s_b";
    contentArear.setAttribute("id", "content");
    this.resetSize();
    this.container.appendChild(contentArear);

    //默认显示第一个标题栏内容
    this.doClickTab(firstTitle, this.showPage, this.container);
  },
  setDisable: function(disableFlag ,id ) {
    this.catArray[id].disabled = disableFlag;
  },
  getContentDivId: function(tabId) {
    if (!this.useMulContenDiv) {
      return "contentDiv";
    }
    return "contentDiv_" + tabId;
  },
  displayTabText: function(tabId, contentText) {    
    var divId = this.getContentDivId(tabId);
    if (this.currContentDiv) {
      if (this.currContentDiv.id == divId) {
        return;
      }else {
        this.currContentDiv.style.display = "none";
      }
    }
    //关闭Iframe标签
    var contentFrame = document.getElementById("contentFrame");
    if (contentFrame) {
      contentFrame.style.display = "none";
    }
    var contentDiv = document.getElementById(divId);    
    if (!contentDiv) {
      contentDiv = document.createElement("div");
      contentDiv.id = divId;
      contentDiv.className = "tabContent";
      this.contentArear.appendChild(contentDiv);
      contentDiv.style.display = "block";   
      contentDiv.innerHTML = "&nbsp;" + contentText;
    }else {
      contentDiv.style.display = "block";
      if (!this.useMulContenDiv) {
        contentDiv.innerHTML = "";
        contentDiv.innerHTML = "&nbsp;" + contentText;
      }
    }
    this.currContentDiv = contentDiv;
  },
  /**
   * 响应标题栏单击事件

   * @param title
   * @param id
   * @return
   */
  doClickTab: function(title, id) {
    if (this.catArray[id].disabled) {
      if (this.catArray[id].disabledFun) {
        this.catArray[id].disabledFun();
      }
      return;
    }
    var titleLink = document.getElementById("title");
    var count = titleLink.childNodes.length;
    for(var i = 0; i < count; i++) {
      if(i == id) {
        titleLink.childNodes[i].className = "active" ;
      }else {
        titleLink.childNodes[i].className = "" ;  
      }                             
    }
    //采用DIV同步显示内容-Json
    if (this.catArray[id].content) {
      this.displayTabText(id, this.catArray[id].content);
      return;
    }
    //在DIV中异步加载内容
    if (!this.catArray[id].useIframe && !this.catArray[id].useTextContent) {
      if(this.catArray[id].contentUrl){
        getJsonRs(this.catArray[id].contentUrl, null, this.dispContent.bind(this, id));
      } 
      if (this.catArray[id].onload) {
        this.catArray[id].onload();
      }
      return;
    }
    if (!this.catArray[id].useIframe && this.catArray[id].useTextContent) {
      if(this.catArray[id].contentUrl){
        getTextRs(this.catArray[id].contentUrl, null, this.dispContentText.bind(this, id));
      } 
      if (this.catArray[id].onload) {
        this.catArray[id].onload();
      }
      return;
    }
    //关闭Div标签
    if (this.currContentDiv) {
      this.currContentDiv.style.display = "none";
      this.currContentDiv = null;
    }
    //使用Iframe加载显示内容
    var iFrame = $("contentFrame");
    if (!iFrame) {
      iFrame = document.createElement("iframe");
      iFrame.setAttribute("id", "contentFrame");
      iFrame.setAttribute("brder", "0");
      iFrame.setAttribute("frameBorder", "no");
      this.contentArear.innerHTML = "&nbsp;";
      this.contentArear.appendChild(iFrame);
    }
    iFrame.style.display = "block";
    iFrame.setAttribute("src", this.catArray[id].contentUrl);
    this.resetSize();

    if (this.catArray[id].onload) {
      this.catArray[id].onload();
    }
  },
  /**
   * 异步在DIV中显示内容

   * @return
   */
  dispContent: function() {
    var id = arguments[0];
    var rtJson = arguments[1];
    this.catArray[id].content = rtJson.content;
    this.displayTabText(id, rtJson.content);
  },
  /**
   * 异步在DIV中显示内容

   * @return
   */
  dispContentText: function() {
    var id = arguments[0];
    var rtText = arguments[1];
    this.catArray[id].content = rtText;
    this.displayTabText(id, rtText);
  } 
}