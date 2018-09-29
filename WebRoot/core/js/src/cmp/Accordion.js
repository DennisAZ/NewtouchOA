var Accordion = Class.create();
Accordion.prototype = {
/*attachCtrl:'div',accordionId:'subfield',data:[{title:'',isExpland:false,icon:'',action:actionFuntion,extData:'',data:[],actionUrl:''},{title:'',icon:'',action:actionFuntion,extData:'',data:[]}],property:{}
 * 
 */
  initialize: function(par) {
    this.accordionId = par.accordionId ? par.accordionId : "";
    this.attachCtrl = par.attachCtrl ? par.attachCtrl : document.body;
    this.data = par.data ? par.data : [];
    this.property = par.property ? par.property : {width:'189px',backgroundImage:imgPath + ''};
    // this.extData = parameters.extData;
    this.createRootUl();
  },
  createRootUl:function(){
    this.rootUl = new Element('ul',{'id':this.accordionId,'class':'menu'});
    this.rootUl.style.width = this.property.width;
    for(var i = 0 ;i < this.data.length ; i++){
      var liData = this.data[i];
      this.createLi(liData, i);
    }
    $(this.attachCtrl).appendChild(this.rootUl);
  },
  createLi:function(el, index){
    var li  = new Element('li',{'class':'L1', 'id':'L1-'+index});
    var a = new Element('a', {'id':'a-'+index}).update("<span>"+ el.title +"</span>");
    a.href = "javascript:void(0)";
    a.observe('click',this.showItem.bindAsEventListener(this,a,el,li));
  //增加圆角---by pjn
    if (index == 0) {
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
      li.addClassName("head");
    }
    //End---by pjn
    

    //如果给的是字符串的话。
    if(typeof(el.data) == 'string'){
      if(el.data){
        var tmpUl = new Element('ul').update(el.data);
        li.appendChild(tmpUl);
      }
     //为数组时
    }else{
       //{title:'',icon'',action:function,iconAction:function,extData:''}
      var ul = this.showData(li , el , a);
      li.appendChild(ul);
    }
    this.rootUl.appendChild(li);
  },
  showData:function(li , el ,a) {
    var ul = new Element('ul');
    if(el.isExpland){
      ul.style.display = '';
      a.className = 'active';
    }else{
      ul.style.display = 'none';
    }
    for(var i = 0 ;i < el.data.length ;i ++){
      var tmp = el.data[i];
      var childLi = new Element('li',{'class' : 'L22'});
      
      var childDiv = new Element('div');
      childDiv.style.paddingTop = '4px';
      //childDiv.style.paddingLeft = '20px';
//      childDiv.style.height = '20px';
      childDiv.style.verticalAlign = "middle";
      //childDiv.style.lineHeight = "0px";
      
      childDiv.observe('mouseover' , this.mouseOverHander.bindAsEventListener(this , childDiv));
      childDiv.observe('mouseout' , this.mouseOutHander.bindAsEventListener(this , childDiv))
      childDiv.observe('click',this.clickHander.bindAsEventListener(this,tmp , false));
      if(tmp.icon){
        var childImage = new Element('img');
        childImage.src = tmp.icon;
        childImage.observe('click',this.clickHander.bindAsEventListener(this,tmp , true));
        childDiv.appendChild(childImage);
      }
      var text = tmp.title;
      text = text.trim();
      childDiv.title = text;
      if (text.length > 10) {
        text = text.substring(0 , 9);
        text += "...";
      }
      childDiv.insert(" " + text);
      childLi.appendChild(childDiv);
      ul.appendChild(childLi);
    }
    return ul;
  },
  showItem:function(){
    var a = arguments[1];
    var tmp = arguments[2];
    var li = arguments[3];
    //获取下级的ul,兼容第一个是圆角的情况---by pjn
    var ul = li.down("ul");
    //End
    if(ul && ul.style.display == ''){
      a.className = '';
      ul.hide();
    }else{
      a.className = 'active';
      if (tmp.actionUrl && tmp.data.length <= 0) {
        var json = getJsonRs(tmp.actionUrl);
        if (json.rtState == '0') {
          tmp.data = json.rtData;
          if (ul) {
            li.removeChild(ul);
          }
          ul = this.showData(li , tmp , a);
          li.appendChild(ul);
        }
      } 
      ul.show();
    }
    if(tmp.action){
      tmp.action(tmp);
    }
  },
  clickHander:function(){
    var event  = arguments[0];
    var tmp = arguments[1];
    var isIcon = arguments[2];//是点击的图标
    
    if(isIcon){
      Event.stop(event);
      if(tmp.iconAction){
        tmp.iconAction(tmp);
      }  
    }else{
      if(tmp.action){
        tmp.action(tmp);
      }
    }
    
  },
  mouseOverHander:function(){
    var div = arguments[1];
    div.className = 'mouseoverDiv';
  },
  mouseOutHander:function(){
    var div = arguments[1];
    div.className = '';
  }
  
}