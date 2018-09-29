package
{ 
  import flash.display.CapsStyle;
  import flash.display.JointStyle;
  import flash.display.LineScaleMode;
  import flash.display.Sprite;
  import flash.events.MouseEvent;
  
  import mx.controls.Alert;
  import mx.core.UIComponent;
  public class Line extends UIComponent
  {
    public var sourceNode:Node;
    public var objectNode:Node;
    private  var sprite2:Sprite = null;
    private var sprite:Sprite = null;
    private var sprite3:Sprite = null;
    
    public function Line(sourceNode:Node , objectNode:Node){
      this.sourceNode = sourceNode;
      this.objectNode = objectNode;
      var sourcePoint = this.sourceNode.getELPoint();
      var objectPoint = this.objectNode.getELPoint();
       sprite = new Sprite();
     var centerX = (objectPoint.x + sourcePoint.x) / 2 ;
     var centerY = (objectPoint.y + sourcePoint.y) / 2 ;
     var sqr:Number = Math.pow(( objectPoint.x - sourcePoint.x ), 2) + Math.pow(( objectPoint.y - sourcePoint.y ), 2)
     var length:Number = Math.pow(sqr , 1/2);
     
     sprite.graphics.lineStyle(10, 0x000000, 0, false, LineScaleMode.VERTICAL,
                               CapsStyle.NONE, JointStyle.MITER, 10);
      sprite.graphics.moveTo(sourcePoint.x , sourcePoint.y);
      sprite.graphics.curveTo(centerX, centerY , objectPoint.x , objectPoint.y);
      sprite.graphics.endFill();
      
       sprite2 = new Sprite();
      //sprite.graphics.beginFill(0xffffff);
      sprite2.graphics.lineStyle(1,  0x999999);
      sprite2.graphics.moveTo(sourcePoint.x , sourcePoint.y);
      sprite2.graphics.curveTo(centerX, centerY,objectPoint.x , objectPoint.y);
      sprite2.graphics.endFill();
      
      sprite3 = new Sprite();
      //sprite.graphics.beginFill(0xffffff);
      sprite3.graphics.lineStyle(1, 0xffffff);
      sprite3.graphics.moveTo(sourcePoint.x , sourcePoint.y);
      sprite3.graphics.curveTo(centerX, centerY,objectPoint.x , objectPoint.y);
      sprite3.graphics.endFill();
      
      this.addChild(sprite);
      this.addChild(sprite2);
      
      this.toolTip = "关系";
      this.useHandCursor = true;
			this.buttonMode = true;
			this.mouseChildren = true;
    }
    private function rollOutHandler(event:MouseEvent):void {
      this.removeChild(sprite3);
      this.addChild(sprite2);
    }
    private function rollOverHandler(event:MouseEvent):void {
      this.removeChild(sprite2);
      this.addChild(sprite3);
    }
    
  }
}