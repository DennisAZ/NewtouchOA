var Tooltip = Class.create();
Tooltip.prototype = {
	initialize: function(el, options,message) {
		this.el = $(el);
		this.initialized = false;
		this.setOptions(options);
		
		// Event handlers
		this.showEvent = this.show.bindAsEventListener(this);
		this.hideEvent = this.hide.bindAsEventListener(this);
		this.updateEvent = this.update.bindAsEventListener(this);
		Event.observe(this.el, "mouseover", this.showEvent );
		Event.observe(this.el, "mouseout", this.hideEvent );
		
		// Removing title from DOM element to avoid showing it
		this.content = message;
		this.el.title = "";

		
	},
	setOptions: function(options) {
		this.options = {
			backgroundColor:'#F2FDDB', // Default background color
			borderColor:'#ADCD3C', // Default border color
			textColor:'#999999', // Default text color (use CSS value)
			maxWidth:300,	// Default tooltip width
			mouseFollow:true,// Tooltips follows the mouse moving
			width:150
		};
		Object.extend(this.options, options || {});
	},
	show: function(e) {
		this.xCord = Event.pointerX(e);
		this.yCord = Event.pointerY(e);
       // alert(this.xCord+":"+this.yCord);
		this.appear();
	},
	hide: function(e) {
		
		if(this.initialized) {
			if(this.options.mouseFollow)
				Event.stopObserving(this.el, "mousemove", this.updateEvent);
			    Element.remove(this.tooltip);
		}
	},
	update: function(e){
		this.xCord = Event.pointerX(e);
		this.yCord = Event.pointerY(e);
		this.setup();
	},
	appear: function() {
		// Building tooltip container
		this.tooltip = document.createElement("div");
		
		with(this.tooltip){
                        style.backgroundColor = this.options.backgroundColor;
                       
			style.border = "1px solid "+this.options.borderColor;
			style.color = ((this.options.textColor != '') ? this.options.textColor : "");
			style.width = this.options.width;
                        style.fontSize = '12px';
                        style.display = "none";
			style.zIndex = 99;
			 
			style.textAlign = "left";
			
			//alert('dd');
			style.position = "absolute";
                        style.padding = "2px 2px 2px 2px";
            appendChild(document.createTextNode(this.content));
            
		}
		document.body.insertBefore(this.tooltip, document.body.childNodes[0]);
		
		$(this.tooltip); // IE needs element to be manually extended
		
		//this.options.width =this.tooltip.style.width;
				//alert(this.tooltip.style.width);
		//this.tooltip.style.width = this.options.width + 'px'; // IE7 needs width to be defined
		
		
		this.setup();
		
		if(this.options.mouseFollow)
			Event.observe(this.el, "mousemove", this.updateEvent);
			
		this.initialized = true;
		
		this.tooltip.style.display = "";
	},
	setup: function(){

		// If content width is more then allowed max width, set width to max
		if(this.options.width > this.options.maxWidth) {
			this.options.width = this.options.maxWidth;
			this.tooltip.style.width = this.options.width + 'px';
		}
			
		// Tooltip doesn't fit the current document dimensions
		//if(this.xCord + this.options.width >= document.body.style.width) {
			//this.options.align = "right";
			//this.xCord = this.xCord - this.options.width + 20;
	///	}
	        this.tooltip.style.left = this.xCord - 7 + "px";
		this.tooltip.style.top = this.yCord + 12 + "px";

	}
};