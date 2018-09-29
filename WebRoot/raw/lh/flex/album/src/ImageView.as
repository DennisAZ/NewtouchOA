      private var isDebug = false;
      import com.adobe.serialization.json.JSON;
      
      import mx.collections.ArrayCollection;
      import mx.containers.HBox;
      import mx.core.DragSource;
      import mx.effects.Effect;
      import mx.events.DragEvent;
      import mx.events.EffectEvent;
      import mx.events.FlexEvent;
      import mx.events.ResizeEvent;
      import mx.managers.CursorManager;
      import mx.managers.DragManager;
      import mx.rpc.events.ResultEvent;
      import mx.rpc.http.HTTPService;
      
      import org.efflex.mx.viewStackEffects.CoverFlowPapervision3D;
      import org.efflex.mx.viewStackEffects.FadeZoom;
      import org.efflex.mx.viewStackEffects.FlipPapervision3D;
      import org.efflex.mx.viewStackEffects.GridZoom;
      import org.efflex.mx.viewStackEffects.List;
      import org.efflex.mx.viewStackEffects.Pixelate;
      import org.efflex.mx.viewStackEffects.Squash;
      [Embed(source="image/gb.png")]
      private var closeTip:Class;
      [Embed(source="image/ck.png")]
      private var openTip:Class;
      [Bindable]
      private var nowImage:Class = closeTip;
      [Bindable]
      private var isOpened:Boolean = true;
      private var newsId:String = "1282";
      public var baseUrl:String = ""; 
      [Bindable]
      private var startIndex = 0;
      [Bindable]
      private var imageWidth = 88;
      [Bindable]
      private var imageHeight = 58;
      [Bindable]
      private var smallPic:ArrayCollection = new ArrayCollection(); 
      [Bindable]
      private var imageList:ArrayCollection = new ArrayCollection(); 
      
      private var selectedPic:String = "";
      [Bindable]
      private var selectedIndex:int = -1;
      [Bindable]
      private var maxLength = 0;
      [Embed(source="image/《.png")]
      public var imgClss1:Class;
      [Embed(source="image/》.png")]
      public var imgClss2:Class;
      [Embed(source="image/titlebg.png")]
      public var titlebg:Class;
      public var effect:Effect;
      private var contextPath = "t9";
      private var isLeft = true; //拖动时在那边
      [Bindable]
      [Embed(source="image/move.png")]
      private var Icon1:Class;
      private var cursorID:int;
      public var bigImageWidth = 800;
      public var otherWidth = 206 + 110 + 15 + 27;
      [Bindable]
      private var imageListCount:uint = 8;
      [Bindable]
      [Embed(source="image/close.png")]
      private var close:Class;
      
      private function createComplete():void {
        this.addEventListener(FlexEvent.UPDATE_COMPLETE, setNowStage);
        this.addEventListener(ResizeEvent.RESIZE , resizeHandler);
        
      }
      private var closeImage:Image = new Image();
      private function fullScreenHandler(event:FullScreenEvent):void {
        if (event.fullScreen){
           mainBox.removeChild(operateBox);
           mainBox.removeChild(showList);
           closeImage.source = close;
           var x = this.stage.fullScreenWidth - 32;
           closeImage.y = 10 ;
           closeImage.x = x;
           this.addChild(closeImage);
        } else {
          mainBox.addChild(operateBox);
          mainBox.addChild(showList);
          mainBox.addChild(rightMessage);
          this.removeChild(closeImage);
        }
      }
      private function closeFullScreen(e:Event) {
        this.stage.displayState = StageDisplayState.NORMAL;
      }
      private function addImageList(event:ResultEvent) {
        var data:String = event.result.toString();
      	var json:Object = JSON.decode(data);
      	var rtData:Array = json.list as Array;
      	imageList = new ArrayCollection(rtData);
      }
      private function resizeHandler(event:Event):void {
        var width:Number = this.stage.stageWidth ;
        imageListCount = (width - 50) / 130;
        bigImageWidth = width - otherWidth ;
        if (bigImageWidth < 0) {
          bigImageWidth = 800;
        }
        this.setImagesWidth();
      }
      private function setImagesWidth():void {
        var imageBoxs:Array = viewStack.getChildren();
        for (var i = 0 ;i < imageBoxs.length ; i++) {
          var imageBox:ImageBox = imageBoxs[i] as ImageBox;
          imageBox.setImg(bigImageWidth);
        }
      }
       private function setNowStage(event:Event):void {
        var width:Number = this.stage.stageWidth ;
        imageListCount = (width - 50) / 130;
        bigImageWidth = width - otherWidth;
      }
      private function doInit():void{
        closeImage.addEventListener(MouseEvent.CLICK , closeFullScreen);
        this.stage.addEventListener(FullScreenEvent.FULL_SCREEN ,fullScreenHandler);
        var contextMenu:ContextMenu = new ContextMenu();
        contextMenu.hideBuiltInItems();
        var contextMenuItem:ContextMenuItem = new ContextMenuItem("下载");
        contextMenuItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT , downImage);
        contextMenu.customItems.push(contextMenuItem);
        mainCanvas.contextMenu = contextMenu;
        if (this.isDebug) {
          baseUrl = "http://localhost/t9/getFile?uploadFileNameServer=" ;
        } else {
          var info:Object = Application.application.loaderInfo.parameters;
          var tmp = info["path"] + "";
          if (!tmp == "") {;
            contextPath = tmp ;
          }
          newsId = info["newsId"] + "";
          baseUrl = contextPath +  "/getFile?uploadFileNameServer=" ;
        }
        effect = this.createEffect();
        getSmallPic(newsId);
        getImageViewList();
      }
      private function addSmallPic(event:ResultEvent):void {
        var data:String = event.result.toString();
      	var json:Object = JSON.decode(data);
      	this.title = json.subject;
      	ExternalInterface.call("setTitle" , this.title);
      	this.info = "发表日期：" + json.time;
      	this.message = json.content;
      	this.nextTitle = "下一图集：" + json.nextTitle;
      	this.nextId = json.nextId;
      	
      	var rtData:Array = json.images as Array;
      	smallPic = new ArrayCollection(rtData);
      	maxLength = smallPic.length;
      	if (maxLength > 0) {
          selectedPic = smallPic.getItemAt(0).id;
          if (selectedPic != "") {
            var selectedBox:HBox = picContainer.getChildByName(selectedPic) as HBox;
            this.selectedImage(selectedBox , null , true , selectedPic);
            this.selectedIndex = 0;
          }
        }
      }
      private function getImageViewList():void {
        var http:HTTPService = new HTTPService();
        if (this.isDebug) {
          http.url = "http://localhost/t9/t9/core/funcs/news/act/T9ImgNewsAct/getNewsList.act";
        } else {
          http.url = this.contextPath + "/t9/core/funcs/news/act/T9ImgNewsAct/getNewsList.act";
        }
        http.addEventListener(ResultEvent.RESULT , addImageList);
    		http.send();
      }
      private function getSmallPic(newsIdStr:String):void{
        var http:HTTPService = new HTTPService();
        if (this.isDebug) {
          http.url = "http://localhost/t9/t9/core/funcs/news/act/T9ImgNewsAct/getNews.act";
        } else {
          http.url = this.contextPath + "/t9/core/funcs/news/act/T9ImgNewsAct/getNews.act";
        }
        http.addEventListener(ResultEvent.RESULT , addSmallPic);
    		var obj:* = {newsId:newsIdStr};
    		http.send(obj);
      }
      private function topPre():void{
        if (startIndex > 0) {
          startIndex--;
        }
        if (selectedPic != "") {
          var selectedBox:HBox = picContainer.getChildByName(selectedPic) as HBox;
          this.selectedImage(selectedBox , null , false , selectedPic);
        } 
      }
      private function bottomNext():void{
        if (startIndex + 4 >=  smallPic.length) {
          return;
        }
        startIndex++;
        if (selectedPic != "") {
          var selectedBox:HBox = picContainer.getChildByName(selectedPic) as HBox;
          this.selectedImage(selectedBox , null , false , selectedPic);
        } 
      }
      
      private function selectedImage(nowSelected:HBox, oldSelected:HBox , isLoadBigPic:Boolean , nowSelectedName:String):void{
        if (oldSelected != null) {
          resize2.play([oldSelected] , false);
          oldSelected.setStyle("borderColor" , 0x666666);
          oldSelected.alpha = 0.7;
        }
        if (nowSelected != null) {
          nowSelected.setStyle("borderColor" , 0xcccccc);
          nowSelected.alpha = 1;
          resize1.play([nowSelected ] , false);
          if (isLoadBigPic) {
            clickDisplayImage(nowSelectedName);
          }
        } 
      }
      private function getNowIndex(str:String):int {
        var index = -1;
        for (var i = 0 ;i < smallPic.length ;i++ ) {
          var tmp:Object = smallPic.getItemAt(i) as Object;
          if (str == tmp.id) {
            index = i;
            break;
          }
        }
        return index
      }
      private function clickSelectedImage(nowSelected:HBox ):void{
        var nowSelectedName:String = nowSelected.name;
        var index = this.getNowIndex(nowSelectedName);
        if (nowSelectedName == selectedPic) {
          return ; 
        }
        if (selectedPic == "") {
          selectedPic = nowSelectedName; 
          selectedImage(nowSelected, null , true , nowSelectedName);
        } else {
          if (index  - 1  >= 0) {
            startIndex = index - 1;
          } 
          var oldSelected:HBox = picContainer.getChildByName(selectedPic) as HBox;
          var newSelected:HBox = picContainer.getChildByName(nowSelectedName) as HBox;
          selectedPic = nowSelectedName; 
          selectedImage(newSelected, oldSelected , true , nowSelectedName);
        }
      }
      private function clickDisplayImage(selectedPic:String):void {
        var hbox:ImageBox = viewStack.getChildByName(selectedPic + "-Box") as ImageBox;
        if (hbox == null) {
           var path = this.getImagePath(selectedPic);
           hbox = new ImageBox(viewStack , selectedPic , path);
        } else {
          this.displayImage(hbox);
        }
      }
      private function getImagePath(str:String):String {
        var path = "";
        for (var i = 0 ;i < smallPic.length ;i++ ) {
          var tmp:Object = smallPic.getItemAt(i) as Object;
          if (str == tmp.id) {
            path = tmp.path;
            return path;
          }
        }
        return path;
      }
      public function displayImage(hbox:ImageBox):void {
        viewStack.selectedChild = hbox;
      }
      private function leftClick():void {
        if (selectedIndex == 0) {
          return ;
        } 
        selectedIndex--;
        if (selectedIndex  - 1  >= 0) {
           startIndex = selectedIndex - 1;
         } 
        var nowSelectedName = smallPic.getItemAt(selectedIndex).id as String;
        var oldSelected:HBox = picContainer.getChildByName(selectedPic) as HBox;
        var newSelected:HBox = picContainer.getChildByName(nowSelectedName) as HBox;
        selectedPic = nowSelectedName; 
        selectedImage(newSelected, oldSelected , true ,selectedPic);
        this.selectedIndex = selectedIndex;
      }
      private function rightClick():void {
        if (selectedIndex >= this.smallPic.length - 1) {
          return ;
        } 
        selectedIndex++;
        if (selectedIndex  - 1  >= 0) {
           startIndex = selectedIndex - 1;
        } 
        var nowSelectedName = smallPic.getItemAt(selectedIndex).id as String;
        var oldSelected:HBox = picContainer.getChildByName(selectedPic) as HBox;
        var newSelected:HBox = picContainer.getChildByName(nowSelectedName) as HBox;
        selectedPic = nowSelectedName; 
        selectedImage(newSelected, oldSelected , true,selectedPic);
        this.selectedIndex = selectedIndex;
      }
      private function closeOrOpen():void {
        if (this.isOpened) {
          this.isOpened = false;
          this.nowImage = openTip;
          this.currentState = "noTip";
          bigImageWidth +=206;
          otherWidth -= 206;
        } else { 
          this.isOpened = true;
          this.nowImage = closeTip;
          this.currentState = ""; 
          bigImageWidth -=206;
          otherWidth += 206;
        }
        resizeHandler(null);
      }
      import mx.managers.CursorManagerPriority;
      import mx.managers.CursorManager;
      import flash.events.FullScreenEvent;
      import mx.core.IUIComponent;
      import mx.effects.easing.Bounce;
      import mx.controls.Image;
      import flash.events.MouseEvent;
      import flash.events.Event;
      import mx.containers.Box;
      private function createEffect():Effect {
        var i =  Math.random(); 
        var index = Math.ceil( i * 7 ) - 1;
        var e:Effect = null;
        switch(index) {
          case 0 : var e1:FlipPapervision3D = new FlipPapervision3D()  ;
                   e1.transparent = true;
                   e = e1;
                   break;
          case 1 : var e2:CoverFlowPapervision3D =  new CoverFlowPapervision3D();
                   e2.transparent = true;
                   e = e2;
                   break;
          case 2 : var e3:FadeZoom =  new FadeZoom();
                   e3.transparent = true;
                   e = e3;
                   break;
          case 3 :   var e4:Pixelate =  new Pixelate();
                   e4.transparent = true;
                   e = e4;
                   break;
          case 4 :  var e5:GridZoom=  new GridZoom() ;
                   e5.transparent = true;
                   e = e5;
                   break;
          case 5 :   var e6:Squash =  new Squash() ;
                   e6.transparent = true;
                   e = e6;
                   break;
          default :   var e7:List =  new List();
                  e7.transparent = true;
                   e = e7;
                   break;    
        }
        e.addEventListener(EffectEvent.EFFECT_END , setPicPosistion);
        return e;
      }
      public function setPicPosistion(event:Event):void {
        var hbox:ImageBox = viewStack.getChildByName(selectedPic + "-Box") as ImageBox;
        var box:HBox = hbox.box;
        var height = box.stage.height;
        if (height > 800) {
          height = 800;
        }
      }
      [Bindable]
      private var title:String = "";
      [Bindable]
      private var info:String = "";
      [Bindable]
      private var message:String = "";
      private function fullScr():void{ 
        this.stage.displayState =  StageDisplayState.FULL_SCREEN;      
      }
           //拖动初始器  
      private function dragSource(e:MouseEvent, format:String):void  
      {  
          var ds:DragSource = new DragSource();  
          ds.addData(smallImagePalBox, format); //设置一个标号format  
          DragManager.doDrag(smallImagePalBox, ds, e); // 开始拖动这个物体  
      }
      //当拖进去时  
     private function onEnter(e:DragEvent, format:String):void  
    {  
        if (e.dragSource.hasFormat(format)) //如果标号为format则接受拖来的物体  
        {  
            DragManager.acceptDragDrop(IUIComponent(e.target)); // 接受被拖进来的物体
        }  
    }  
     
    //当拖完成时  
    private function onDrop(e:DragEvent, format:String):void  {  
       var width = this.stage.width / 2;
       if (isLeft && e.stageX > width) {
         var box:VBox=VBox(e.dragInitiator); //如果扩展到其他组件，改这里Box就可以了。
         var hbox:HBox = box.parent as HBox;
         hbox.removeChild(box);
         hbox.addChildAt(tipBox , 1);
         hbox.addChild(box);
         isLeft = false;
       } else if(!isLeft && e.stageX < width){
         var box:VBox=VBox(e.dragInitiator); //如果扩展到其他组件，改这里Box就可以了。
         var hbox:HBox = box.parent as HBox;
         hbox.removeChild(box);
         hbox.addChildAt(box , 1);
         hbox.addChild(tipBox);
         isLeft = true;
       }
     }  
     public function downImage(evnet:Event){
       if (selectedPic != "") {
         var path = this.getImagePath(selectedPic);
         var url = baseUrl + path ;
         var urlReq:URLRequest = new URLRequest(url);
         navigateToURL(urlReq, "_blank");
       }
     }
     private function imageListClick(event:Event) { 
       var im:ImageList = event.currentTarget as ImageList;
       this.openImageWindow(im.idStr);
     }
     private function openImageWindow(idStr) {
      var viewUrl = "";
      if (isDebug) {
        viewUrl = "http://localhost/t9/core/funcs/news/imgNews/imageWindow.jsp?id=1303";
      } else {
        viewUrl = contextPath + "/core/funcs/news/imgNews/imageWindow.jsp?id=" + idStr;
      }
      var urlReq:URLRequest = new URLRequest(viewUrl);
      navigateToURL(urlReq, "_blank");
    }
    [Bindable]
    private var nextTitle:String = "下一图集： ";
    private var nextId:String = "" ;
    private function nextImageView(){
      if (nextId != "") {
        this.openImageWindow(nextId);
      }
    }