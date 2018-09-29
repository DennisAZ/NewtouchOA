var ExchangeSelectbox = Class.create();
ExchangeSelectbox.prototype = {
		initialize: function(el, selectedArray , disSelectedArray,isSort) {
				this.selectedArray = selectedArray;
				this.disSelectedArray = disSelectedArray;
				this.isSort = isSort;
				this.insertEvent = this.insert.bindAsEventListener(this);
				this.removeEvent = this.remove.bindAsEventListener(this);
				this.upEvent = this.up.bindAsEventListener(this)
				this.appear(el);

	},
	appear: function(el){
		var parentDiv = $(el);
		var sortDiv = document.createElement("div");
		var upButton = document.createElement("input");
		with(upButton){
			type = "button";
			value = "↑";
		}
		Event.observe($(upButton),"click",this.upEvent);
		sortDiv.appendChild(upButton);
		var downButton = document.createElement("input");
		with(downButton){
			type = "button";
			value = "↓";
		}
		sortDiv.appendChild(downButton);
		
		var selectedDiv = document.createElement("div"); 
		this.selected = document.createElement("select");
		with(this.selected){
		    name = "selected";
		    multiple = true;
		    id = "selected";
		}
		Event.observe($(this.selected),"dblclick",this.removeEvent);
		for(var i = 0 ;i<this.selectedArray.length;i++){
	    	var option = document.createElement("option");
	    	option.value = this.selectedArray[i].id;
	    	option.appendChild(document.createTextNode(this.selectedArray[i].name));
	    	this.selected.appendChild(option);
	    }
		var input = document.createElement("input");
		with(input){
		    type = "button";	
			value = "全选";
		}
		selectedDiv.appendChild(this.selected);
		selectedDiv.appendChild(input);
		
		var buttonDiv = document.createElement("div");
		var buttonLeft = document.createElement("input");
		with(buttonLeft){
			type = "button";
			value = " ← ";
		}
		Event.observe($(buttonLeft),"click",this.insertEvent);
		var buttonRight = document.createElement("input");
		with(buttonRight){
			type = "button";
			value = " → ";
		}
		Event.observe($(buttonRight),"click",this.removeEvent);
		buttonDiv.appendChild(buttonLeft);
		buttonDiv.appendChild(buttonRight);
		
		var disSelectedDiv = document.createElement("div");
		this.disSelected = document.createElement("select");
		with(this.disSelected){
			id = "disselected";
		    name = "disselected";
		    multiple = true;
		}
		Event.observe($(this.disSelected),"dblclick",this.insertEvent);
		for(var i = 0 ;i<this.disSelectedArray.length;i++){
	    	var option = document.createElement("option");
	    	option.value = this.disSelectedArray[i].id;
	    	option.appendChild(document.createTextNode(this.disSelectedArray[i].name));
	    	this.disSelected.appendChild(option);
	    }
		input = document.createElement("input");
		with(input){
		    type = "button";	
			value = "全选";
		}
		disSelectedDiv.appendChild(this.disSelected);
		disSelectedDiv.appendChild(input);
		
		var div = document.createElement("div");
		with(div){
			id = "exchange-div";
			appendChild(sortDiv);
			appendChild(selectedDiv);
			appendChild(buttonDiv);
			appendChild(disSelectedDiv);
		}
		
		parentDiv.appendChild(div);
		
	},
	insert:function(){
		var options = this.disSelected.getElementsByTagName("option");
		for (i=options.length-1; i>=0; i--) {
			if(options[i].selected){
				this.selected.appendChild(options[i]);
			}
		}
	},
	remove:function(){
		var options = this.selected.getElementsByTagName("option");
		for (i=options.length-1; i>=0; i--) {
			if(options[i].selected){
				this.disSelected.appendChild(options[i]);
			}
		}
	},
	selectedAll:function(object){
		var options = object.getElementsByTagName("option");
		for (i=options.length-1; i>=0; i--) {
			options[i].selected = true;
		}
	},
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
		
	},
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
			  this.selected.insertBefore(options[i],options[i+1]);
		  }
		
	}
}