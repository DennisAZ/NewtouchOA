package
{  
    import flash.geom.Rectangle;  
  
    import mx.graphics.BitmapFill;  
    import mx.skins.ProgrammaticSkin;  
    import flash.text.StyleSheet;
  
    public class RepeatBackground extends ProgrammaticSkin {  
        public function RepeatBackground() {
        }

        override protected function updateDisplayList(w:Number, h:Number):void {  
            super.updateDisplayList(w,h);  
            
            graphics.clear();
            var b:BitmapFill = new BitmapFill();  
            b.source = getStyle("backgroundImage");;  
            b.begin(graphics,new Rectangle(0,0,w,h));  
            graphics.drawRect(0,0,w,h);  
            b.end(graphics);  
        }  
    }
}  
