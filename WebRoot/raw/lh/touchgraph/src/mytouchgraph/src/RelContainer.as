package
{
  import com.adobe.serialization.json.JSON;
  
  import flash.events.Event;
  import flash.events.MouseEvent;
  import flash.external.ExternalInterface;
  
  import mx.collections.ArrayCollection;
  import mx.containers.VBox;
  import mx.controls.Alert;
  import mx.controls.HRule;
  import mx.controls.Image;
  import mx.controls.LinkButton;
  import mx.controls.SWFLoader;
  import mx.controls.Text;
  import mx.core.Application;
  import mx.rpc.events.ResultEvent;
  import mx.rpc.http.HTTPService;
  
  public class RelContainer extends VBox
  { 
    [Embed(source="28.gif")]
    public var imgCls:Class;
     [Embed(source="loading.swf")]
     public var loadingCls:Class;
    public var content:VBox = new VBox();
    public var contentWidth:Number = 300;
    public var contentHeight:Number = 300;
    public var s:SWFLoader = new SWFLoader();
    public function RelContainer(point:*,sourceNode:Node , objectNode:Node)
    { 
      this.styleName = "relContainer";
      this.setPoint(point);
      setTitleContainer(sourceNode.nameStr , objectNode.nameStr);
      this.addChild(content);
      content.width = contentWidth;
      content.height = contentHeight;
      this.name = "relCon";
      content.setStyle("paddingTop" , 0 );
      var image:Image = new Image();
      image.source = imgCls;
      var http:HTTPService = new HTTPService();
      if (Application.application.isDebug) {
        http.url = "http://192.168.0.126:9000/BjfaoWeb/FullText/GetFullTextDocList";
      } else {
  			http.url = "/t9/t9/subsys/inforesouce/act/T9OutURLAct/getFullTextDocList.act";
      }
			http.addEventListener(ResultEvent.RESULT , setData);
			http.resultFormat = "text";
			var obj:* = {q:sourceNode.nameStr + " " + objectNode.nameStr};
			http.send(obj);
      this.addEventListener(MouseEvent.CLICK , function(event:MouseEvent) {
        event.stopPropagation();
      });
    }
    public function setTitleContainer(snameStr , onameStr):void {
      var closeBox:VBox = new VBox();
      closeBox.width = contentWidth;
      //closeBox.setStyle("backgroundColor" , 0xB7BABC);
      closeBox.setStyle("horizontalAlign","right");
      closeBox.setStyle("verticalAlign","top");
      var closeLink:LinkButton =  new LinkButton();
      closeLink.label = "×";
      closeLink.addEventListener(MouseEvent.CLICK , close);
      s.source = this.loadingCls;
      content.addChild(s);
//       var text:Text = new Text();
//      text.htmlText = "<b>" + snameStr + "</b>:<b>" + onameStr + "</b>";
//      closeBox.addChild(text);
//      var s:Spacer = new Spacer();
//      s.width = this.width - 200;
//      closeBox.addChild(s);
      closeBox.addChild(closeLink);
      this.addChild(closeBox);
    }
    public function setData(e:ResultEvent):void {
      content.removeChild(s);
      var data:String = e.result.toString();
			data = data.replace(/\\u0027red\\u0027/g, '\\"#FF0000\\"');
			try {
  			var json:Object = JSON.decode(data);
  			var rtData:Array = json.items as Array;
  			var myArray:ArrayCollection = new ArrayCollection(rtData);
  			if (myArray.length == 0 ) {
  			  this.setText("无相关文章","","");
  			  content.height = 200;
  			  return ;
  			}
  			for (var i=0;i < myArray.length ;i++ ) {
  			  var o:* = myArray.getItemAt(i);
  			  var description:String = o.Description as String;
  			  var titleStr:String = o.Title as  String;
  			  var link:String = o.FILE_ID as String;
  			  if (description != "") {
    			  this.setText(description,titleStr, link);
    			  var h:HRule = new HRule();
    			  h.width = contentWidth - 20;
    			  if (i != myArray.length - 1) {
    			    content.addChild(h);
    			  }
  			  }
  			}
			} catch (e) {
			  //Alert.show(e);
			}
    }
    public function close(e:Event):void {
      this.parent.removeChild(this);
    }
    public function setPoint(point:*):void {
      this.x = point.x + 10;
      this.y = point.y + 10;
    }
    public function setText(str:String  , title:String , link:String) :void {
      if (title != "") {
        var titleText:Text = new Text();
        titleText.width = contentWidth - 20;
        titleText.setStyle("fontSize",14);
        titleText.htmlText = "标题：<b>" + title + "...</b>";
        titleText.setStyle("color",0x228296);
        content.addChild(titleText);
        var contentText:Text = new Text();
        contentText.setStyle("color",0x000000);
  		  contentText.width = contentWidth - 20;
  		  contentText.setStyle("fontSize",14);
  		  contentText.htmlText = "摘要：" + str + "...";
  		  content.addChild(contentText);
  		  var linkText:LinkButton = new LinkButton();
        linkText.setStyle("color",0x000000);
  		  linkText.width = contentWidth - 20;
  		  linkText.setStyle("fontSize",14);
  		  linkText.label = "阅读文档";
  		  content.addChild(linkText);
  		  linkText.addEventListener(MouseEvent.CLICK , function(event:MouseEvent){
  		    openWindow(event , link);
  		  });
      } else {
        content.setStyle("verticalAlign" , "middle");
        var contentText:Text = new Text();
        contentText.setStyle("color",0x000000);
  		  contentText.width = contentWidth - 20;
  		  contentText.setStyle("fontSize",14);
  		  contentText.htmlText = "<b>" + str + "</b>"  ;
  		  content.addChild(contentText);
      }
    }
    public function openWindow(event:MouseEvent,fileIdStr:String):void {
      if (Application.application.isDebug) {
        
      } else {
         ExternalInterface.call("openFileWindow" , fileIdStr);
      }
    }
    public function doOpenWindow(event:ResultEvent):void {
      var http:HTTPService = new HTTPService();
      	http.url =  "/t9/t9/subsys/inforesouce/act/T9OutURLAct/getDoc.act";
    		http.addEventListener(ResultEvent.RESULT , doOpenWindow);
    		http.resultFormat = "text";
    		var obj:* = {fileId:""};
    		http.send(obj);
      var data:String = event.result.toString();
      
      var json:Object = JSON.decode(data);
      //if (json.rtState == "0") {
     
        var filePath:String = json.rtData as String;
  		  ExternalInterface.call("openFileWindow" , filePath);
     // }
      
    }
   }
}