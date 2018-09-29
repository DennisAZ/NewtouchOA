package
{
	import flash.display.GradientType;
	import flash.display.SpreadMethod;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.text.TextField;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.core.UIComponent;
	import mx.effects.Move;
	import mx.effects.Zoom;
	import mx.effects.easing.Linear;
	import mx.events.EffectEvent;
	
	public class Node 
	{   
		private var bgNode:UIComponent = null;
		private var node:UIComponent = null;
		private var text:UIComponent = null;
		private var mask:UIComponent = null;
		
		private var centerPoint:Point = new Point(50 , 50);
		private var z:Zoom = new Zoom();
    public var nameStr:String = "";
    private var radom =  Math.random(); 
    private var colorIndex:Number =  Math.ceil( radom * Colors.colors.length ) - 1;
    private var index = 0;
  //  private var isOld = false;
    
		private var point:Object;
		
		private function setBgNodeStyle() {
		  this.bgNode.width = 100;
		  this.bgNode.height = 100;
		  bgNode.x = 0;
		  bgNode.y = 0 ;
		  this.bgNode.name = "bgNode-" + nameStr;
		}
		private function setTextStyle(x:Number , y:Number ) {
		  this.text.width = 100;
		  this.text.height = 100;
		  text.x = x;
		  text.y = y ;
		  this.text.name = "text-" + nameStr;
		}
		private function setMaskStyle(x:Number , y:Number ) {
		  this.mask.width = 50;
		  this.mask.height = 50;
		  this.mask.x = x;
		  this.mask.y = y;
		  this.mask.alpha = 0 ;
		  this.mask.useHandCursor = true;
			this.mask.buttonMode = true;
			this.mask.mouseChildren = true;
		  this.mask.name = "mask-" + nameStr;
		}
		private function createMask(canvas:Container) {
		  var mask1 = canvas.getChildByName("mask-" + nameStr) as UIComponent;
	    mask = new UIComponent();
	    if (mask1 == null ) {
	      this.setMaskStyle(centerPoint.x / 2 , centerPoint.y / 2);
	    } else {
	      this.setMaskStyle(mask1.x , mask1.y);
	    }
	    var sprite:Sprite = new Sprite();
			sprite.graphics.beginFill(0xFFFFFF,0);
			sprite.graphics.drawCircle(25,25,25);
			sprite.graphics.endFill();
			mask.addChild(sprite);
			mask.addEventListener(MouseEvent.CLICK ,doSearch);
		  canvas.addChild(mask);
		  if (mask1 != null) {
  		  canvas.removeChild(mask1);
		  }
		}
		private function doSearch(e:Event) {
		  if (Application.application.searchText != this.nameStr) {
		    doZoomHandler( false);
		    Application.application.startMove = true;
		    Application.application.searchText = this.nameStr;
		    Application.application.doSearch(e);
		  } 
		}
		private function setNodeStyle(x:Number , y:Number) {
		  this.node.width = 100;
		  this.node.height = 100;
		  node.x = x;
		  node.y = y ;
		  this.node.name = "node-" + nameStr;
		}
		private function createBgNode(canvas:Container){
		  bgNode = canvas.getChildByName("bgNode-" + nameStr) as UIComponent;
		  if (bgNode == null) {
		    bgNode = new UIComponent();
		    this.setBgNodeStyle();
		    var sprite:Sprite = new Sprite();
  			var matr:Matrix = new Matrix();
        var color1 = Colors.colors[this.colorIndex];
        //var color2 = Colors.colors2[this.colorIndex];
        matr.createGradientBox(100, 100, 0, 0, 0);
        sprite.graphics.beginGradientFill(GradientType.RADIAL, [color1 , 0x000000], [1, 0.1], [120,255], matr, SpreadMethod.PAD, "rgb", 0);
  			sprite.graphics.drawCircle(50,50,50);
  			sprite.graphics.endFill();
  			bgNode.addChild(sprite);
  			canvas.addChild(bgNode);
		  } 
		}
		private function createNode(canvas:Container){
		  var node1 = canvas.getChildByName("node-" + nameStr) as UIComponent;
	    node = new UIComponent();
	    if (node1 == null ) {
	      this.setNodeStyle(0 , 0 );
	    } else {
	      this.setNodeStyle(node1.x, node1.y );
	    }
	    var sprite:Sprite = new Sprite();
			var matr:Matrix = new Matrix();
      var color1 = Colors.colors[this.colorIndex];
      var color2 = Colors.colors2[this.colorIndex];
      matr.createGradientBox(100, 100, 0, 0, 0);
      sprite.graphics.beginGradientFill(GradientType.RADIAL, [color2,color1, 0x000000], [1,1, 0.1], [0, 128,255], matr, SpreadMethod.PAD, "rgb", 0);
  	  sprite.graphics.drawCircle(centerPoint.x,centerPoint.y,50);
  	  sprite.graphics.endFill();
			node.addChild(sprite);
		  canvas.addChild(node);
		  if (node1 != null) {
		    canvas.removeChild(node1);
		  }
		}
		public function createText(canvas:Container):void {
		   var text1 = canvas.getChildByName("text-" + nameStr) as UIComponent;
		   text = new UIComponent();
	     if (text1 == null) {
	       this.setTextStyle(0 , 0 );
	     } else {
	       this.setTextStyle(text1.x ,text1.y );
	     }
	     var textf:TextField = new TextField();
	     textf.height = 20;
  		 textf.htmlText = "<font color='#ffffff' size='14px'><b>"+this.nameStr+"</b></font>";
  		 var textLen = textf.textWidth ;
  		 var textHeight = textf.textHeight;
  		 textf.x = centerPoint.x - textLen / 2  ;
  		 textf.y = centerPoint.y - textHeight / 2 ;
  		 if (textf.x < 0) {
  		   textf.x = 0 ; 
  	   }
  	   text.addChild(textf);
		   canvas.addChild(text);
		   if (text1 != null) {
		     canvas.removeChild(text1);
		   }
		}
		private function createLine(canvas:Container):void {
		  var line:Line = new Line(canvas.centerNode , this);
		  var centerNode:UIComponent = canvas.centerNode.node;
		  var centerText:UIComponent = canvas.centerNode.text;
		  var centerMask:UIComponent = canvas.centerNode.mask;
		  if (canvas.contains(centerNode)) {
		    canvas.removeChild(centerNode);
		  }
		  if (canvas.contains(centerText)) {
		    canvas.removeChild(centerText);
		  }
		  if (canvas.contains(centerMask)) {
		    canvas.removeChild(centerMask);
		  }
		  line.name = "line-" + this.nameStr ;
		  canvas.addChild(line);
		  canvas.addChild(centerNode);
		  canvas.addChild(centerText);
		  canvas.addChild(centerMask);
		}
		public function Node(obj:Object , canvas:Container ) 
		{   
		  this.index = obj.index;
		  this.point = obj.point;
		  this.nameStr = obj.Term;
		  if (!obj.isCenter) {
		    createLine(canvas);
		  }
      this.createNode(canvas);	
      this.createText(canvas);	
      this.createMask(canvas);  
			this.setEffectStyle();
			this.point = point;
			var move:Move = new Move();
      move.duration = 1000;
      move.easingFunction = Linear.easeNone;
      move.xTo = point.x;
      move.yTo = point.y;
      
      var move2:Move = new Move();
      move2.duration = 1000;
      move2.easingFunction = Linear.easeNone;
      move2.xTo = point.x + centerPoint.x / 2;
      move2.yTo = point.y + centerPoint.y / 2;
      move.play([ this.text , this.node]);
      move2.play([this.mask]);
      Application.application.nowMoveCount++;
      Application.application.startMove = false;
      move2.addEventListener(EffectEvent.EFFECT_END , addZommEventHandler);
		}
		public function addZommEventHandler(event):void {
	    this.mask.addEventListener(MouseEvent.ROLL_OUT , this.doZoom);
		  this.mask.addEventListener(MouseEvent.ROLL_OVER , this.doZoom);
		  this.mask.addEventListener(MouseEvent.MOUSE_DOWN , stopEvent);
		  this.node.addEventListener(MouseEvent.MOUSE_DOWN , stopEvent);
		  this.text.addEventListener(MouseEvent.MOUSE_DOWN , stopEvent);
		  Application.application.moveFinishCount++;
		}
		public function stopEvent(e:Event):void {
		   e.stopPropagation();
		}
		public function setEffectStyle():void {
		  z.zoomWidthFrom = 1;
			z.zoomHeightFrom = 1;
			z.zoomHeightTo = 1.5;
			z.zoomWidthTo = 1.5;
		}
		public function doZoom(event:MouseEvent):void {
//		  if ( Application.application.startMove 
//		  || Application.application.moveFinishCount < Application.application.nowMoveCount ) {
//		     return; 
//		   }
		   this.doZoomHandler( event.type == MouseEvent.ROLL_OUT);
    }
    public function doZoomHandler( flag:Boolean){
      var zPoint:Point = centerPoint;
		  zPoint = this.node.localToContent(zPoint);
			z.originX = zPoint.x ;
			z.originY = zPoint.y ;
      if (z.isPlaying) {
        z.reverse();
      }
      else {
        z.play([this.mask , this.text , this.node], flag );
      }
    }
    public function getELPoint():Object {
       var point2:* = {x:point.x + 50 ,y :point.y + 50};
	    return point2;
	  }
	}
	
}