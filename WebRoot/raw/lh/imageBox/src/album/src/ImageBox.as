package
{
  import flash.events.Event;
  
  import mx.containers.HBox;
  import mx.containers.ViewStack;
  import mx.controls.Image;
  import mx.controls.ProgressBar;
  import mx.core.Application;
  import mx.effects.Effect;
  import mx.events.EffectEvent;
  
  public class ImageBox extends HBox
  { 
    private var idStr:String = "";
    private var image:Image = new Image();
    public var box:HBox = new HBox();
    public function ImageBox(v:ViewStack , id:String , name:String)
    {
        this.idStr = id;
        this.name = idStr + "-Box";
        this.setStyle("hideEffect" , Application.application.effect);
        this.setStyle("showEffect" ,Application.application.effect); 
        this.percentWidth = 100;
        this.percentHeight = 100;
        this.styleName = "ImageBoxStyle";
        this.createThisHbox(name);
        v.addChild(this);
    }
    public function createThisHbox(name:String) {
      box.name = this.idStr  + "-HBox";
      this.addChild(box);
      box.setStyle("verticalGap",0);
      box.setStyle("horizontalGap",0);
      var loadProgressBar:ProgressBar = Application.application.loadProgressBar;
      loadProgressBar.visible = true;
      loadProgressBar.source = image;
      loadProgressBar.addEventListener(Event.COMPLETE , displayImage);
      image.maxHeight = 500;
      image.maxWidth = 800;
      var bigUrl = Application.application.bigUrl;
      image.load(bigUrl + name);
      box.addChild(image);
    }
    public function displayImage(event:Event):void {
      var loadProgressBar:ProgressBar = event.currentTarget as ProgressBar;
      loadProgressBar.visible = false;
      loadProgressBar.removeEventListener(Event.COMPLETE , displayImage);
      Application.application.displayImage(this);
    }
  }
}