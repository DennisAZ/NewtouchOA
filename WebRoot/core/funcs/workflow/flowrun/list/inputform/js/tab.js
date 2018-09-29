function buildTab(categories, container, height , rightContent){
  var tabObj = new YHJsTab({catArray: categories, container: container , divHeight:height , rightDivContent:rightContent});
  tabObj.show();
  return tabObj;
}
var YHJsTab = Class.create();
YHJsTab.prototype = {
  initialize: function(cfgs) {
    this.config(cfgs);
  },
  config: function(cfgs) {
    this.catArray = cfgs.catArray;
    this.divHeight = cfgs.divHeight;
    this.container = $(cfgs.container) ? $(cfgs.container) : document.body;
    this.useMulContenDiv = cfgs.useMulContenDiv === false ? false : true;
    this.rightDivContent = cfgs.rightDivContent ? cfgs.rightDivContent : "";
    this.contentArear = null;
    this.currContentDiv = null;
  },
  /**
   * 显示标签页
   */
  show: function() {
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
      titleLink.setAttribute("href","javascript:void(0)");

      var title = categories[i].title;
      titleDiv.appendChild(titleLink);
      titleLink.title = title;
      titleLink.id = i;
      titleLink.onclick = this.doClickTab.bind(this, title, i);
     
      var span = document.createElement("span");
      span.width = 80;  
      var imgUrl = categories[i].imgUrl;
      if(imgUrl){
      	var img = document.createElement("img");
      	img.width = 16;
      	img.height = 16;
        img.setAttribute('src', imgUrl);
        span.appendChild(img);
      }
          
      span.innerHTML += "&nbsp;" + categories[i].title;
      titleLink.appendChild(span);
      
      //是否默认显示此页
      if (categories[i].isShow) {
        firstTitle = categories[i].title;
        this.showPage = i;
      }
      //$(span).update("  ");
    }
   
    //画右边div主要用来装一些html代码
    var rightDiv = document.createElement("div");
    rightDiv.className = 'rightDiv';
    rightDiv.style.paddingTop = '4px';
    titleDiv.appendChild(rightDiv);
    $(rightDiv).update(this.rightDivContent);

    //默认显示第一个标题栏内容
    this.doClickTab(firstTitle, this.showPage, this.container);
  },
  /**
   * 响应标题栏单击事件
   * @param title
   * @param id
   * @return
   */
  doClickTab: function(title, id) {
    var titleLink = document.getElementById("title");
    var count = titleLink.childNodes.length;
    for(var i = 0; i < count; i++) {
      if(i == id) {
        titleLink.childNodes[i].className = "active" ;
      }else {
        titleLink.childNodes[i].className = "" ;  
      }                             
    }
    if (this.catArray[id].onload) {
      this.catArray[id].onload();
    }
  }
}