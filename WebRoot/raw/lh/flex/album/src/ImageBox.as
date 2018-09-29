package
{
  import flash.events.Event;
  
  import mx.containers.HBox;
  import mx.containers.ViewStack;
  import mx.controls.Image;
  import mx.controls.ProgressBar;
  import mx.core.Application;
  
  public class ImageBox extends HBox
  { 
    private var idStr:String = "";
    private var image:Image = new Image();
    public var box:HBox = new HBox();
    public function ImageBox(v:ViewStack , id:String , path:String)
    {
        this.idStr = id;
        this.name = idStr + "-Box";
        this.setStyle("hideEffect" , Application.application.effect);
        this.setStyle("showEffect" ,Application.application.effect); 
        this.percentWidth = 100;
        this.percentHeight = 100;
        this.styleName = "ImageBoxStyle";
        this.horizontalScrollPolicy = "off";
        this.verticalScrollPolicy = "off";
        this.createThisHbox(path);
        v.addChild(this);
    }
    public function createThisHbox(path:String) {
      box.name = this.idStr  + "-HBox";
      this.addChild(box);
      box.setStyle("verticalGap",0);
      box.setStyle("horizontalGap",0);
      box.setStyle("borderStyle","solid");
      box.setStyle("borderColor",0x333333);
      var loadProgressBar:ProgressBar = Application.application.loadProgressBar;
      loadProgressBar.visible = true;
      loadProgressBar.source = image;
      loadProgressBar.addEventListener(Event.COMPLETE , displayImage);
      var baseUrl = Application.application.baseUrl;
      image.addEventListener(Event.COMPLETE , setMainCanvas);
      image.load(baseUrl + path);
      box.addChild(image);
    }
    public function setMainCanvas(event:Event):void {
        var bigWidth =  Application.application.bigImageWidth;
        this.setImg(bigWidth);
    }
    public function displayImage(event:Event):void {
      var loadProgressBar:ProgressBar = event.currentTarget as ProgressBar;
      loadProgressBar.visible = false;
      loadProgressBar.indeterminate = true;
      loadProgressBar.removeEventListener(Event.COMPLETE , displayImage);
      Application.application.displayImage(this);
    }
    public function setImg(iwidth):void {
       var width = image.content.width;   //图片原始宽度
       var height = image.content.height; //图片原始高度
       var iheight = 640;
       var newwidth = 0,newheight = 0;//初始化新的宽度和高度
       if(width > 0 && height > 0){ //如果图片的宽高都大于0
        if (width / height >= iwidth / iheight) {   //如果原图片的比例大于或等于需要适应的比例
           if (width > iwidth){  //如果原图片宽度大于容器的宽度，那么就按宽度压缩
             newwidth = iwidth;
             newheight = (height * iwidth) / width;
           } else {
              newwidth = width;
              newheight = height;
           }
           if (newheight > iheight) {//如果压缩后的图片高度仍然大于容器高度，那么再继续将图片宽度/高度压缩
             newwidth = newwidth * iheight / newheight;
             newheight = iheight;
           }
         } else  { //如果原图片比例小于需要适应的比例
            if (height > iheight) {      //如果原图片高度大于需要容器的高度，那么就按高度压缩
              newheight = iheight;
              newwidth = (width * iheight) / height;              
            } else {
              newwidth = width;
              newheight = height;
            }
            if (newwidth > iwidth) //如果压缩后的图片宽度仍然大于容器宽度，那么再继续将图片宽度/高度压缩
            {
                newheight = newheight * iwidth / newwidth;
                newwidth = iwidth;
            }
         }
      }
      image.width = newwidth;
      image.height = newheight;
    }
  }
}