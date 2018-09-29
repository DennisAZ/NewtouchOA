package com
{
  import flash.accessibility.AccessibilityProperties;
  import flash.display.DisplayObject;
  import flash.display.DisplayObjectContainer;
  import flash.display.LoaderInfo;
  import flash.display.Sprite;
  import flash.display.Stage;
  import flash.events.Event;
  import flash.geom.Point;
  import flash.geom.Rectangle;
  import flash.geom.Transform;
  
  import mx.core.IToolTip;
  import mx.core.UIComponent;
  import mx.managers.ISystemManager;
  public class BubbleToolTip extends UIComponent implements IToolTip
  {
    public static var maxWidth:Number = 300;
    public function BubbleToolTip()
    {
      super();
      mouseEnabled = false;
    }
    /**
     *  @private
     *  Storage for the text property.
     */
    private var _text:String;

    /**
     *  @private
     */
    private var textChanged:Boolean;

    
    public function set text(value:String):void
    {
        _text = value;
        textChanged = true;

        invalidateProperties();
        invalidateSize();
        invalidateDisplayList();
    }
    
   
    override protected function updateDisplayList( 
           unscaledWidth:Number, unscaledHeight:Number ) : void 
    {   
      super.updateDisplayList( unscaledWidth, unscaledHeight ); 
 
 
    this.graphics.clear(); 
 
    this.graphics.beginFill( getStyle('backgroundColor'), 1 ); 
    this.graphics.lineStyle(2, getStyle('borderColor'), 1); 
    this.graphics.drawRoundRect(0, 35, 6, 6, 24, 24); 
    this.graphics.endFill(); 
 
    this.graphics.beginFill( getStyle('backgroundColor'), 1 ); 
    this.graphics.lineStyle(2, getStyle('borderColor'), 1); 
    this.graphics.drawRoundRect(10, 25, 15, 15, 24, 24); 
    this.graphics.endFill(); 
 } 

  }
}