/**
 * 1,修正点击上移下移按扭不能排序问题
 * 2,增加必选数据项,即数据必选参数
 */
var ExchangeSelectbox = Class.create();
ExchangeSelectbox.prototype = {
		//初始化/*{containerId,selectedArray,disSelectedArray,isSort,isOneLevel,title,selectName,selectedChange,titleText}
 * containerId 字符串 互选框所依附的div的id值,不指定默认为body
 * selectedArray 对象数组  初始化时默认选中的对象
 * disSelectedArray 对象数组  初始化时默认没有选中的对象
 * isSort boolean 是否排序
 * isOneLevel boolean 指定控件把展示数据的层数，现控件只支持两级
 * title 字符串 指定控件的标题,不指定默认不显示
 * selectName 字符串 指定控件选中框的name属性，主要方便form 表单中些控件的提交。
 * titleText 标题的字{selectedTitle:'ddd',disSelectedTitle:'ddd'}默认为选中,备选
 */
initialize: function(parameters) {
	
  this.selectedArray = parameters.selectedArray;
	this.disSelectedArray = parameters.disSelectedArray;
	this.isSort = parameters.isSort;
	this.title = parameters.title ;
	this.selectName = parameters.selectName;
	this.selectedChange = parameters.selectedChange;
  this.selectedTitle = '选中';
  this.disSelectedTitle = '备选';
	if(parameters.titleText){
	  if(parameters.titleText.selectedTitle){
	    this.selectedTitle = parameters.titleText.selectedTitle;
	  }
	  if(parameters.titleText.disSelectedTitle){
      this.disSelectedTitle = parameters.titleText.disSelectedTitle;
    }
	}
	
	
	this.isOneLevel = parameters.isOneLevel;
	this.insertEvent = this.insert.bindAsEventListener(this);
	this.removeEvent = this.remove.bindAsEventListener(this);
	this.upEvent = this.up.bindAsEventListener(this);
	this.downEvent = this.down.bindAsEventListener(this);
	this.appear(parameters.containerId);

},
	//画 一个互换选框
appear: function(el){
	var parentDiv = $(el);
	//指的父结点为空的话,默认为body
	if(parentDiv==null){
	    parentDiv = $(document.body);
	}
	//var table = document.createElement("table");
	var table = new Element("table", {"class": "TableBlock"});
	table.id = "exchange-div";
	var tbody = document.createElement("tbody");
	table.appendChild(tbody);
	//第一行数据  if(this.title){
    table.className = "TableBlock no-top-border";
    //增加标题时,添加了一个表格,为了实现圆角效果
    var titleTable = new Element("table", {
      "class": "TableTop",
      "width": "450px"
    });
    var topTr = new Element("tr", {});
    var topBody = new Element("tbody", {});
    var topTdL = new Element("td", {
      "class": "left"
    });
    var topTd = new Element("td", {
      "class": "center"
    });
    var topTdR = new Element("td", {
      "class": "right"
    });
    topTd.innerHTML = this.title;
    topTr.insert(topTdL);
    topTr.insert(topTd);
    topTr.insert(topTdR);
    titleTable.insert(topBody.insert(topTr));
    parentDiv.appendChild(titleTable);
  }
	//第二行数据
	tr = document.createElement("tr");
	tr.className = "TableContent";
	if(this.isSort){
		td = document.createElement("td");
		//td.className = "exchangeSelectTd";
		td.innerHTML = "<font color='#000000'>排序</font>";
		tr.appendChild(td);
	}
	
	td = document.createElement("td");
	
	td.innerHTML =  "<font color='#000000'>"+this.selectedTitle+"</font>";
	tr.appendChild(td);
	
	td = document.createElement("td");
	td.className = "exchangeSelectTd";
	
	td.innerHTML =  "<font color='#000000'>选择</font>";
	tr.appendChild(td);
	
	td = document.createElement("td");
	td.innerHTML =  "<font color='#000000'>"+this.disSelectedTitle+"</font>";
	tr.appendChild(td);
	tbody.appendChild(tr);
	//第三行数据	//tr = document.createElement("tr");
	tr = new Element("tr", {"class": "TableData"});
	//排序列	if(this.isSort){
	td = document.createElement("td");
	td.className = "exchangeSelectTd";
	var sortDiv = document.createElement("div");
	var upButton = document.createElement("input");
	with(upButton){
		type = "button";
		value = "↑";
		className = "SmallButtonA";
	}
	Event.observe($(upButton),"click",this.upEvent);
	sortDiv.appendChild(upButton);
	td.appendChild(sortDiv);
	var br = document.createElement("br");
	td.appendChild(br);
	sortDiv = document.createElement("div");
	var downButton = document.createElement("input");
	with(downButton){
		type = "button";
		value = "↓";
		className = "SmallButtonA";
	}
	Event.observe($(downButton),"click",this.downEvent);
	sortDiv.appendChild(downButton);
	td.appendChild(sortDiv);
	tr.appendChild(td);
	}
	//选中列	td = document.createElement("td");
	var selectedDiv = document.createElement("div"); 
	this.selected = document.createElement("select");
	this.selected.setAttribute("name" , this.selectName);
	with(this.selected){
	    className = "exchangeSelect";
	    multiple = true;
	    id = this.selectName;
	}
	Event.observe($(this.selected),"dblclick",this.removeEvent);
	for(var i = 0 ;i<this.selectedArray.length;i++){
    	var option = document.createElement("option");
    	option.value = this.selectedArray[i].value;
    	//如果是必选的话
    	if (this.selectedArray[i].isMustSelect) {
    	  option.isMustSelect = true;
    	}
    	option.appendChild(document.createTextNode(this.selectedArray[i].text));
    	this.selected.appendChild(option);
    }
	var input = document.createElement("input");
	with(input){
		type = "button";	
		value = "全选";
		className = "SmallButtonW";
	}
	$(input).observe("click",this.selectedAll.bindAsEventListener(this,this.selected));
	selectedDiv.appendChild(this.selected);
	td.appendChild(selectedDiv);
	selectedDiv = document.createElement("div"); 
	selectedDiv.appendChild(input);
	td.appendChild(selectedDiv);
	tr.appendChild(td);
	//选择列	td = document.createElement("td");
	td.className = "exchangeSelectTd";
	var buttonDiv = document.createElement("div");
	var buttonLeft = document.createElement("input");
	with(buttonLeft){
		type = "button";
		value = " ← ";
		className = "SmallButtonA";
	}
	Event.observe($(buttonLeft),"click",this.insertEvent);
	var buttonRight = document.createElement("input");
	with(buttonRight){
		type = "button";
		value = " → ";
		className = "SmallButtonA";
	}
	Event.observe($(buttonRight),"click",this.removeEvent);
	buttonDiv.appendChild(buttonLeft);
	td.appendChild(buttonDiv);
	var br = document.createElement("br");
	td.appendChild(br);
	buttonDiv = document.createElement("div");
	buttonDiv.appendChild(buttonRight);
	td.appendChild(buttonDiv);
	tr.appendChild(td);
	//备选列
	td = document.createElement("td");
	var disSelectedDiv = document.createElement("div");
	this.disSelected = document.createElement("select");
	this.disSelected.setAttribute("name" , this.disselected);
	with(this.disSelected){
		//id = "disselected";
	    className = "exchangeSelect";
	    name = "disselected";
	    multiple = true;
	}
	Event.observe($(this.disSelected),"dblclick",this.insertEvent);
	this.setDisSelected();
	input = document.createElement("input");
	with(input){
	  type = "button";	
		value = "全选";
		className = "SmallButtonW";
	}
	$(input).observe("click",this.selectedAll.bindAsEventListener(this,this.disSelected));
	disSelectedDiv.appendChild(this.disSelected);
	td.appendChild(disSelectedDiv);
	disSelectedDiv = document.createElement("div");
	disSelectedDiv.appendChild(input);
	td.appendChild(disSelectedDiv);
	tr.appendChild(td);
	tbody.appendChild(tr);
	parentDiv.appendChild(table);
},
	//构造备选框
setDisSelected:function(){
	if(this.isOneLevel){
		for(var i = 0 ;i<this.disSelectedArray.length;i++){
    	var option = document.createElement("option");
    	option.value = this.disSelectedArray[i].value;
    	option.appendChild(document.createTextNode(this.disSelectedArray[i].text));
    	this.disSelected.appendChild(option);
	  }
	}else{
		for(var i = 0 ;i<this.disSelectedArray.length;i++){
	    	if(this.disSelectedArray[i].isParent){
	    	  var optgroup = document.createElement("optgroup");
	    	  optgroup.label = this.disSelectedArray[i].text;
	    	  optgroup.id = "optgroup-"+this.disSelectedArray[i].id;
	    	  this.disSelected.appendChild(optgroup);
	    	}
	    }
		for(var i = 0 ;i<this.disSelectedArray.length;i++){
		  if(!this.disSelectedArray[i].isParent){
		    var option = document.createElement("option");
		    option.value = this.disSelectedArray[i].value;
		    option.appendChild(document.createTextNode(this.disSelectedArray[i].text));
		    
		    var optgroups = this.disSelected.getElementsByTagName("optgroup");
		    var k = 0; 
		    for(var j = 0 ;j < optgroups.length;j++){
		       var id = optgroups[j].id.substr(9);
		       if(id==this.disSelectedArray[i].parentId){
		         optgroups[j].appendChild(option);
		         k = 1;
		        }
		     }
		    if(k==0){
		      this.disSelected.appendChild(option);
		    }
		  }
    }
		
	}
},
//选中数据
insert:function(){
	var options = this.disSelected.getElementsByTagName("option");
	for (i=options.length-1; i>=0; i--) {
		var condition = options[i].selected;
		if(condition){
			this.selected.appendChild(options[i]);
		}
	}
	this.selectChangeHandler();
},
//进入备选栏
remove:function(){
	var options = this.selected.getElementsByTagName("option");
	if(this.isOneLevel){
	  for (i=options.length-1; i>=0; i--) {
	    if(options[i].selected && !options[i].isMustSelect){
	      this.disSelected.appendChild(options[i]);
	    }
	  }
	}else{
	  for (i=options.length-1; i>=0; i--) {
      if(options[i].selected && !options[i].isMustSelect){
        var parentId = this.findParentId(options[i].value);
        if(parentId==null||parentId==""){
          this.disSelected.appendChild(options[i]);
        }else{
          var optgroups = this.disSelected.getElementsByTagName("optgroup");
          for(var j = 0 ;j < optgroups.length;j++){
            var id = optgroups[j].id.substr(9);
            if(id==parentId){
              optgroups[j].appendChild(options[i]);
            }
          }
          
        }
        
      }
    }
	  
	}
	this.selectChangeHandler();
},
selectChangeHandler:function(){
  var ids = "";
  var text = "";
  var options = this.selected.getElementsByTagName("option");
  for(var i = 0 ;i < options.length ;i ++){
    var option = options[i];
    ids += option.value + ",";
    text += option.text + ",";
  }
  if(this.selectedChange){
    this.selectedChange(ids , text);
  }
},
//选择所有selectedAll:function(){
	var object = arguments[1];
	var options = object.getElementsByTagName("option");
	for (i=options.length-1; i>=0; i--) {
		options[i].selected = true;
	}
},
//上移
up:function(){
	  var sel_count=0;
	  var options = this.selected.getElementsByTagName("option");
	  for(i=options.length-1; i>=0; i--){
	    if(options[i].selected)
	       sel_count++;
	  }
	  if(sel_count==0){
	     alert("调整项目顺序时，请选择其中一项！");
	     return;
	  }
	  else if(sel_count>1){
	     alert("调整项目顺序时，只能选择其中一项！");
	     return;
	  }
	  i=this.selected.selectedIndex;

	  if(i!=0){
		  this.selected.insertBefore(options[i],options[i-1]);
	  }
	  this.selectChangeHandler();
},
//下移
down:function(){
	  var sel_count=0;
	  var options = this.selected.getElementsByTagName("option");
	  for(i=options.length-1; i>=0; i--){
	    if(options[i].selected)
	       sel_count++;
	  }
	  if(sel_count==0){
	     alert("调整项目顺序时，请选择其中一项！");
	     return;
	  }
	  else if(sel_count>1){
	     alert("调整项目顺序时，只能选择其中一项！");
	     return;
	  }
	  i=this.selected.selectedIndex;
       
	  if(i!=options.length-1){
		  this.selected.insertBefore(options[i+1],options[i]);
	  }
	  this.selectChangeHandler();
},
//查询所属组结点Id
findParentId:function(value){
  for(var i = 0 ;i < this.selectedArray.length;i++){
    var option = this.selectedArray[i];
    if(option.value == value){
      return option.parentId;
    }
  }
  for(var i = 0 ;i < this.disSelectedArray.length;i++){
    var option = this.disSelectedArray[i];
    if(option.value == value){
      return option.parentId;
    }
  }
  return "";
	}
}