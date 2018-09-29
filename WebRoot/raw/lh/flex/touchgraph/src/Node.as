package
{
	import flash.display.GradientType;
	import flash.display.SpreadMethod;
	import flash.display.Sprite;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.text.TextField;
	
	import mx.core.Application;
	import mx.core.UIComponent;
	import mx.effects.Glow;
	import mx.effects.Move;
	import mx.effects.Zoom;
	import mx.effects.easing.Linear;
	import mx.events.EffectEvent;
	
	public class Node extends UIComponent
	{   
		private var z:Zoom = new Zoom();
		private var g:Glow = new Glow();
		private var g2:Glow = new Glow();
		//private var fadeOut:Fade = new Fade();
    public var nameStr:String = "";
		private var point:Object;
		private var text:TextField = new TextField();
		private var sprite2:Sprite = new Sprite();
		
		public function Node(point:Object , str:String) 
		{   
		  this.width = 100;
		  this.height = 100;
			this.setEffectStyle();
			this.point = point;
			var sprite:Sprite = new Sprite();
			var matr:Matrix = new Matrix();
			var i =  Math.random(); 
      var index = Math.ceil( i * Colors.colors.length ) - 1;
      var color1 = Colors.colors[index];
      var color2 = Colors.colors2[index];
      matr.createGradientBox(100, 100, 0, 0, 0);
      sprite.graphics.beginGradientFill(GradientType.RADIAL, [color2,color1, 0x000000], [1,1, 0.1], [0, 128,255], matr, SpreadMethod.PAD, "rgb", 0);
			sprite.graphics.drawCircle(50,50,50);
			sprite.graphics.endFill();
			this.addChild(sprite);
			
			
			this.x = 0;
			this.y = 0;
      this.setText(str);
      sprite2.graphics.beginFill(0xffffff , 0);
			sprite2.graphics.drawCircle(50,50,25);
			sprite2.graphics.endFill();
			this.addChild(sprite2);
			sprite2.addEventListener(MouseEvent.CLICK  ,  doSearch);
			sprite2.useHandCursor = true;
			sprite2.buttonMode = true;
			sprite2.mouseChildren = true;
			
      this.toolTip = str;
			var move:Move = new Move();
      move.target = this;
      move.duration = 1000;
      move.easingFunction = Linear.easeNone;
      move.xTo = point.x;
      move.yTo = point.y;
      move.play();
      move.addEventListener(EffectEvent.EFFECT_END , addZommEventHandler);
		}
		public function addZommEventHandler(event):void {
		  this.sprite2.addEventListener(MouseEvent.ROLL_OUT , this.doZoom);
			this.sprite2.addEventListener(MouseEvent.ROLL_OVER , this.doZoom);
			this.addEventListener(MouseEvent.MOUSE_DOWN , function(event){
			  event.stopPropagation();
			  });
			
		}
		public function setText(str:String):void {
			text.height = 20;
			nameStr = str;
			text.htmlText = "<font color='#ffffff' size='14px'><b>"+str+"</b></font>";
			var textLen = text.textWidth ;
			
			var textHeight = text.textHeight;
			text.x = 50 - textLen / 2  ;
			text.y = 50 - textHeight / 2 ;
			if (text.x < 0) {
			  text.x = 0 ; 
			}
			//var img:DisplayObject = text.getImageReference("img-" + nameStr);
			this.addChild(text);
		}
		public function doSearch(event):void {
		  Application.application.searchInput.text = nameStr;
		  Application.application.doSearch(event);
		}
		public function setEffectStyle():void {
		  z.zoomWidthFrom = 1;
			z.zoomHeightFrom = 1;
			z.zoomHeightTo = 1.5;
			z.zoomWidthTo = 1.5;
//			fadeOut.duration = 1000;
//			fadeOut.alphaFrom = 1.0;
//			fadeOut.alphaTo = 0;
			
			this.setGlowStyle(g);
			this.setGlowStyle(g2);
		}
		public function setGlowStyle(g1:Glow) :void {
		  g1.duration = 1000
			g1.alphaFrom = 0.3;
			g1.alphaTo = 1;
			g1.blurXFrom = 50;
			g1.blurXTo = 0;
			g1.blurYFrom = 50;
			g1.blurYTo = 0 ;
			g1.color = 0x0000FF;
		}
		public function doZoom(event:MouseEvent):void {
		  var zPoint:Point = new Point(50 , 50);
		  zPoint = this.localToContent(zPoint);
			z.originX = zPoint.x ;
			z.originY = zPoint.y ;
      if (z.isPlaying) {
        z.reverse();
      }
      else {
        z.play([this], event.type == MouseEvent.ROLL_OUT ? true : false);
      }
    }
     public function getELPoint():Object {
       var point2:* = {x:point.x + 50 ,y :point.y + 50};
	    return point2;
	  }
	}
	
}