var MenuList = Class.create();
MenuList.prototype = {
/*  panel:'div',index:'cur',data:[{title:'',action:actionFuntion,data:[]}]
 * 
 */
  initialize: function(param) {
    this.panel = param.panel ? param.panel : "_listPanel";
    this.data = param.data ? param.data : [];
    this.curIndex = param.index ? param.index : 1;
    this.fix = param.fix ? false : true;
    this.notExpand = param.notExpand ? true : false;
    this.createRootUl();
  },
  createRootUl:function(){
    this.rootUl = new Element('ul');
    for(var i = 0 ;i < this.data.length ; i++){
      var liData = this.data[i];
      this.createLi(liData, i + 1);
    }
    $(this.panel).appendChild(this.rootUl);
  },
  createLi:function(el, index){
    var li  = new Element('li');
    var a = new Element('a', {id:'link_' + index, 'class':''}).update("<span>" + el.title + "</span>");
    var div = new Element('div', {'id':"module_" + index, 'class':"moduleContainer"});
    div.style.display = 'none';
    if (el.isCloseAll) {
      a.isCloseAll = el.isCloseAll;
    } else {
      a.isCloseAll = false;
    }
    
    a.href = "javascript: void(0)";
    //bind a event showItem 
    if (this.fix) {
      a.observe('click', this.showItem1.bindAsEventListener(this, el, index));
    } else {
      a.observe('click', this.clickHander.bindAsEventListener(this, el, index));
    }
    if(el.data)
    {
      var ul = new Element('ul');
      for(var i = 0 ;i < el.data.length ;i ++){
        var tmp = el.data[i];
        var childLi = new Element('li');
        var childDiv = new Element('div', {'align':'center'});
        //bind event ,
        childDiv.observe('mouseover', this.mouseOverHander.bindAsEventListener(this, childDiv));
        childDiv.observe('mouseout', this.mouseOutHander.bindAsEventListener(this, childDiv))
        childDiv.observe('click', this.clickHander.bindAsEventListener(this, tmp, false));
        childDiv.appendChild(document.createTextNode(tmp.data));
        childLi.appendChild(childDiv);
        ul.appendChild(childLi);
      }
      div.appendChild(ul);
    }
    this.rootUl.appendChild(li);
    this.rootUl.appendChild(div);
    //增加圆角---by pjn
    if (index == 1) {
      var t = new Element("table", {
        "class": "BlockTop",
        "width": "100%"
      });
      var tr = t.insertRow(0);
      var tdl = tr.insertCell(0);
      var td = tr.insertCell(1);
      var tdr = tr.insertCell(2);
      tdl.className = "left";
      td.className = "center";
      tdr.className = "right";
      td.appendChild(a);
      li.insert(t);
    }
    else {
      li.appendChild(a);
      li.className = "head";
    }
    //End---by pjn
  },
  showItem1:function(){
    var tmp = arguments[1];
    var cur = arguments[2];
    if (!this.notExpand ) {
      var el = $("module_" + this.curIndex);
      var link = $("link_" + this.curIndex);
      if (cur == this.curIndex) { 
        if (el.style.display == "none") {
          link.className = "active";
           el.show();
        } else {
          link.className = "";
          el.hide();
        }
      } else {
         var divs = this.rootUl.getElementsByTagName("div"); 
         if (divs) {
           for (var i = 0 ;i < divs.length ; i++) {
             var tmpDiv = divs[i];
             var id = tmpDiv.id.substr("module_".length);
             if (tmpDiv.hide) {
               tmpDiv.hide();
             }
           }
         }
         link.className = "";
         $("module_" + cur).show();
         $("link_" + cur).className = "active";
      }
      this.curIndex = cur;
    } 
    if (tmp.action) {               //执行action
      tmp.action(tmp);
    }
  },
  showItem:function(){
    var tmp = arguments[1];
    var cur = arguments[2];
    var el = $("module_" + this.curIndex);
    var link = $("link_" + this.curIndex);
    if (cur == this.curIndex) {             
      if (el.style.display == "none") {
        link.className = "active";
         el.show();
      } else {
        link.className = "";
        el.hide();
      }
    } else {
       //el.hide();
      //var link2 = $("link_" + cur);
      // if (link2.isCloseAll) {
         var divs = this.rootUl.getElementsByTagName("div"); 
         if (divs) {
           for (var i = 0 ;i < divs.length ; i++) {
             var tmpDiv = divs[i];
             var id = tmpDiv.id.substr("module_".length);
             tmpDiv.hide();
           }
         }
         
       //}
       link.className = "";
       $("module_" + cur).show();
       $("link_" + cur).className = "active";
    }
    this.curIndex = cur;
    if (tmp.action) {               //执行action
      tmp.action(tmp);
    }
  },
  clickHander:function(){
    var event  = arguments[0];
    var tmp = arguments[1];
    
     if(tmp.action){
        tmp.action(tmp);
    }
  },
  mouseOverHander:function(){
    var div = arguments[1];
    div.className = 'mouseoverDiv';
  },
  mouseOutHander:function(){
    var div = arguments[1];
    div.className = '';
  },
  getContainerId:function(index){
    return "module_" + index;
  }
  
}