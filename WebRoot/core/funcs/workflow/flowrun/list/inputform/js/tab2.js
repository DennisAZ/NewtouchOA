function buildTab2(categories, container , content , rightContent){
  var tabObj = new YHJsTab2({catArray: categories, container: container , contentDiv:content  , rightDivContent:rightContent});
  tabObj.show();
  return tabObj;
}
var YHJsTab2 = Class.create();
YHJsTab2.prototype = {
  initialize: function(cfgs) {
    this.config(cfgs);
  },
  config: function(cfgs) {
    this.catArray = cfgs.catArray;
    this.container = $(cfgs.container) ? $(cfgs.container) : document.body;
    this.contentDiv = $(cfgs.contentDiv) ? $(cfgs.contentDiv) : null;
    
    this.rightDivContent = cfgs.rightDivContent ? cfgs.rightDivContent : "";
    this.contentArear = null;
    this.currContentDiv = null;
    
    this.titleConWidth = 24;
    this.textTitleHeight = 35;
  },
  createDivTiltle: function() {
    var div = new Element("div");
    div.style.height = this.textTitleHeight + "px";
    //div.style.borderTop = "1px solid red";
    div.style.background= "url(img/right_exporer_bg.jpg) repeat-x left top";
    //div.style.borderBottom = "1px solid red";
    var span = new Element("span");
    span.style.paddingLeft = "10px";
    span.update("&lt;&lt;");
    div.appendChild(span);
    div.id = "title-div";
    div.insert(this.rightDivContent);
    
    var div2 = new Element("div");
    div2.style.height = this.textTitleHeight + "px";
    //div.style.borderTop = "1px solid red";
    div2.style.width = this.titleConWidth + "px";
    //div2.style.background= "url(img/right_exporer_bg.jpg) repeat-x left top";
    div2.update("&nbsp;>>");
    div2.style.display = "none";
    div2.id = "title-div2";
    div2.onclick = this.setContentDivTop.bind(window , this.contentDiv , this.textTitleHeight , div2 , div , true);
    span.onclick = this.setContentDivTop.bind(window , this.contentDiv , this.textTitleHeight , div2 , div , false);
    this.container.appendChild(div2);
    return div;
  },
  setContentDivTop:function(obj , height , div2 , div , flag) {
    if (flag) {
      div2.hide();
      div.show();
      obj.style.top =(height + 2) + "px"; 
    } else {
      div2.show();
      div.hide();
      obj.style.top = "0px";
    }
  },
  setContentDiv:function() {
    if (!this.contentDiv) {
      return ;
    }
    this.contentDiv.style.width = '96%';
    this.contentDiv.style.heigth = '100%';
    this.contentDiv.style.left = (this.titleConWidth ) + "px";
    this.contentDiv.style.top =(this.textTitleHeight + 2) + "px";
    this.contentDiv.style.position = "absolute";
    
  },
  setTitleConHeight:function( obj , height) {
    obj.style.height = (document.viewport.getDimensions().height - height - 2 )  + "px";
  },
  setTitleConPosition:function (obj , height) {
    var top = document.documentElement.scrollTop;
    if (top > height) {
      obj.style.top = top + "px";
    } else {
      obj.style.top = (height + 1) + "px";
    }
  },
  /**
   * 显示标签页
   */
  show: function() {
    this.container.appendChild(this.createDivTiltle());
    this.titleCon = new Element("div");
    this.titleCon.style.left = "0px";
    this.titleCon.style.position = "absolute"; 
    //this.titleCon.style.backgroundColor = '#ebebeb';
    this.titleCon.style.top =( this.textTitleHeight + 1) + "px";
    this.titleCon.style.width = this.titleConWidth + "px";
    this.setTitleConHeight( this.titleCon , this.textTitleHeight);
    this.titleCon.id =  "titleConDiv";
    this.container.appendChild(this.titleCon);
    this.setContentDiv();
    
    for (var i = 0 ;i < this.catArray.length ;i ++) {
      var cat = this.catArray[i];
      this.createTitle(cat , i);
    }
    window.onresize = this.setTitleConHeight.bind(window , this.titleCon , this.textTitleHeight);
    window.onscroll = this.setTitleConPosition.bind(window, this.titleCon , this.textTitleHeight);
  },
  createTitle:function(cat , i ){
    var title = cat.title;
    var onload = cat.onload;
    var div = new Element("div");
    div.style.color = '#000';
    div.style.paddingTop = "2px";
    div.style.paddingBottom = "2px";
    div.style.fontSize = '11pt';
    div.style.cursor = "hand";
    //div.style.height = "30px";
   // div.style.height = "100%";
    
    var div1 = new Element("div");
    div1.style.background = "url(img/tab1.png)";
    div1.style.backgroundRepeat= "no-repeat";
    div1.style.backgroundPosition  = "bottom";
    div1.style.width = this.titleConWidth + "px";
    div1.style.paddingBottom = "0px";
    div1.style.height = "11px";
    
    var div2 = new Element("div");
    div2.style.paddingTop = "0px";
    div2.style.background = "url(img/tab2.png) repeat-y left top";
    div2.style.width = this.titleConWidth + "px";
    div2.style.paddingLeft = "3px";
    div2.update(title);
    
    var div3 = new Element("div");
    div3.style.background = "url(img/tab3.png)";
    div3.style.backgroundRepeat= "no-repeat";
    div3.style.width = this.titleConWidth + "px";
    div3.style.height = "10px";
    
    div.appendChild(div1);
    div.appendChild(div2);
    div.appendChild(div3);
    
    var object = this;
    div.onclick = function () {
      object.clickHandler(this);
      if (onload) {
        onload(); 
      }
    }
    div.onmouseover = function() {
      object.mouseHandler(this , true);
    }
    div.onmouseout = function() {
      object.mouseHandler(this , false);
    }
    this.titleCon.appendChild(div);
    if (i == 0) {
      object.clickHandler(div);
      if (onload) {
        onload(); 
      }
    }
  },
  mouseHandler:function(div , flag) {
    if (flag) {
      div.style.borderLeft = "2px solid blue";
      div.style.color = "#fff";
    } else {
      div.style.borderLeft = "";
      div.style.color = "#000";
    }
  },
  setTabStyle:function(div, selected) {
    var divs = div.getElementsByTagName("div");
    var val1 = [1,2,3];
    if (selected) {
      val1 = [4,5,6];
    }
    divs[0].style.background = "url(img/tab"+val1[0]+ ".png)";
    divs[0].style.backgroundRepeat= "no-repeat";
    divs[0].style.backgroundPosition  = "bottom";
    divs[1].style.background = "url(img/tab"+val1[1]+ ".png) repeat-y left top";
    divs[2].style.background = "url(img/tab"+val1[2]+ ".png)";
    divs[2].style.backgroundRepeat= "no-repeat";
  },
  clickHandler:function(div) {
    window.scrollTo(0,0);
    this.setTabStyle(div , true);
    this.mouseHandler(div , false);
    div.onmouseover = function() {
    }
    div.onmouseout = function() {
    }
    var divs = this.titleCon.childNodes;
    var object = this;
    for (var i = 0; i < divs.length ;i++) {
      var tmp = divs[i];
      if (tmp != div) {
        tmp.style.color = "#000";
        this.setTabStyle(tmp , false);
        tmp.onmouseover = function() {
          object.mouseHandler(this , true);
        }
        tmp.onmouseout = function() {
          object.mouseHandler(this , false);
        }
      }
    }
    div.style.color = "#fff";
  }
}