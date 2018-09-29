// ActionScript file
import com.adobe.serialization.json.JSON;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.controls.LinkButton;
import mx.core.DragSource;
import mx.core.IUIComponent;
import mx.events.DragEvent;
import mx.events.FlexEvent;
import mx.formatters.DateFormatter;
import mx.managers.DragManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.http.HTTPService;
[Bindable]
private var toValue:Number = 1;
[Bindable]
private var fromValue:Number = 1;
[Bindable]
private var searchData:String = "";
[Bindable]
private var searchedData:ArrayCollection = new ArrayCollection();

private var Ax:int = 0;
private var Ay:int = 0;
private var centerPoint:* = null;
public var isDebug:Boolean = true;
[Bindable]
private var bw :Number = 0;
[Bindable]
private var bh :Number = 0;
[Bindable]
private var la1 :String = "";
[Bindable]
private var la2 :String = "";
private var nowDateTime:Number = 0;
private var nowPreYear:Number = 0 ;
private var nowNextYear:Number = 0 ;
private var setDateMethod:String = "setDate";
private var dayTime:Number = 24 * 60 * 60 * 1000;
private var maxCount:int = 5;
	
private function removeCon(event:Event):void {
  var relCon = this.getChildByName("relCon");
  if (relCon != null) {
	  this.removeChild(relCon);
  }
}
private function doInit():void {
  var nowDate:Date = new Date();
  nowDateTime = nowDate.getTime();
  var format:DateFormatter = new DateFormatter();
  format.formatString = "YYYY年MM月DD日";
  la2 = format.format(nowDate);
  hSlider.maximum = nowDateTime / dayTime;
  this.setHSlider(2005);
  setHSliderValue(nowDateTime,nowDateTime);
  this.addEventListener(FlexEvent.UPDATE_COMPLETE, setNowStage);
}
private function createInit():void {
  var info:Object = Application.application.loaderInfo.parameters;
  if (info != null ) {
    if (this.isDebug) {
      searchInput.text = "火";
    } else {
	    searchInput.text = info["data"] + "";
    }
    this.doSearch(null);
  }
}
private function setNowStage(event:Event):void {
  var x1:Number = canvas.stage.stageWidth / 2;
  var y1:Number = canvas.stage.stageHeight / 2;
  centerPoint = {x:x1-50,y:y1-50};
}
public function addNode(event:ResultEvent):void {
  var data:String = event.result.toString();
	data = data.replace(/s/g,'');
	var json:Object = JSON.decode(data);
	var rtData:Array = json.rtData as Array;
	var myArray:ArrayCollection = new ArrayCollection(rtData);
	var myArray2:ArrayCollection = new ArrayCollection();
	for (var i = 0 ;i < myArray.length ;i ++ ) {
	  var str:String = myArray.getItemAt(i) as String;
	  var obj:* = {Term:str , Count:i};
	  myArray2.addItem(obj);
	}
	myArray = myArray2;
	var center:Node = new Node(centerPoint, searchData);
	var nowMax = 0 ;
	var nowMin = 0;
	var b = 0 ;
	for (var i = 0 ;i < myArray.length ; i++) {
    var tmp:* =  myArray.getItemAt(i);
    if (searchData == tmp.Term) {
      continue;
  } else {
    b++;
  }
  if ( b == 1 ) {
    nowMin = tmp.Count;
    nowMax = tmp.Count;
  } else {
    if (nowMax < tmp.Count) {
      nowMax = tmp.Count;
    }
    if (nowMin > tmp.Count) {
      nowMin = tmp.Count;
    }
  }
}
 var degress:Number = 360 / b;
 
	for (var i = 0 ;i < myArray.length ; i++) {
	  var obj:* = myArray.getItemAt(i);
	  if (searchData == obj.Term){
	    continue;
	  }
	  var count:Number = obj.Count;
	  var countTmp:Number = getCountTmp(nowMin , nowMax ,count);
	  var y1:Number = Math.sin(degress * (i + 1)) * countTmp;
    var x1:Number = Math.cos(degress * (i + 1)) * countTmp;
    y1 = y1 + centerPoint.y;
    x1 = x1 +  centerPoint.x;
	  var n:Node = new Node({x:x1 , y:y1}, obj.Term);
	  var line:Line = new Line(center , n);
	  canvas.addChild(line);
	  canvas.addChild(n);
	}
  canvas.addChild(center);
}
	function getCountTmp(minCount:Number , maxCount:Number , countTmp:Number):Number {
  var y = maxCount - minCount;
  if (y == 0) {
    return 200;
  }
  var min = 120;
  var max = centerPoint.x;
  if (centerPoint.x > centerPoint.y) {
    max = centerPoint.y;
  }
  var x = max - min ;
  var z = countTmp - minCount;
  countTmp = (x/y * z) + min ;
  return countTmp;
}
	public function doZoom(value:Number):void {
    if (zoomAll.isPlaying) {
       zoomAll.reverse();
    }
    else {
        toValue += value;
        if (toValue > 2) {
          toValue = 2;
        } 
        if (toValue < .5) {
          toValue = .5;
        } 
        zoomAll.play([canvas]);
        fromValue = toValue;
    }
 }
 //拖动初始器  
private function dragSource(e:MouseEvent, format:String):void  
{  
    var iu:IUIComponent=e.currentTarget as IUIComponent;  
    var ds:DragSource=new DragSource();  
    Ax = e.localX;
    Ay = e.localY;
    ds.addData(iu, format); //设置一个标号format  
    DragManager.doDrag(iu, ds, e); // 开始拖动这个物体  
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
private function onDrop(e:DragEvent, format:String):void  
{  
   var box:HBox=HBox(e.dragInitiator); //如果扩展到其他组件，改这里Box就可以了。  
   box.x = e.stageX-Ax;
   box.y = e.stageY-Ay;  
 }  
 
 public function fadeOutHandler(e:Event):void {
  canvas.removeAllChildren();
  canvas.alpha = 1.0;
  try {
    var http:HTTPService = new HTTPService();
    if (this.isDebug) {
      http.url =  "http://192.168.0.126:9000/BjfaoWeb/FullText/GetRelationWords";
    } else {
  		  http.url =  "/t9/t9/subsys/inforesouce/act/T9OutURLAct/getRelationWords.act";
    }
		http.addEventListener(ResultEvent.RESULT , addNode);
		http.resultFormat = "text";
		searchData = searchInput.text;
		if (searchData == "" ) {
		  return ; 
		}
		var obj:* = {q:searchData};
		http.send(obj);
		this.addEventListener(MouseEvent.CLICK , removeCon);
		var index = -1;
		for (var i = 0 ;i < searchedData.length ; i++) {
		  var s:String = searchedData.getItemAt(i) as String;
		  if (s == searchData) {
		    index = i;
		    break;
		  }
		}
		if (index == -1) {
//		  if (searchedData.length == 5) {
//		    for (var i = 1 ;i < searchedData.length ;i++ ) {
//		      var str:String = searchedData.getItemAt(i) as String;
//		      searchedData.setItemAt(str , i - 1);
//		    }
//		    searchedData.setItemAt(searchData , i -1 );
//		  } else {
  			searchedData.addItem(searchData);
//		  }
		} 
  } catch(e) {
    Alert.show(e);
  }
 }
 public function doSearch(e:Event):void {
   fadeOut.play([canvas]);
 }
 
 private function enterKey(e:KeyboardEvent):void {
 var c =  e.charCode;
 if (c == 13) {
   doSearch(e);
 }
 }
 private function labelLinkbutton(event:Event):void {
 var label:LinkButton = event.currentTarget as LinkButton;
 searchInput.text = label.label;
 doSearch(event);
 }    
 private function preYears():void {
	nowPreYear  -= 1;
	var date1:Date = new Date(nowPreYear,0,1);
	var iDate1:Number = date1.getTime();
	hSlider.minimum = iDate1 / dayTime;
	la1 =  nowPreYear + "年";
}   
private function changeSize(event : Object):void{
	 var val1:Number = event.target.values[0];
	 var val2:Number = event.target.values[1];
}
private function format(value:Number):String {
	value = value * dayTime;
	var date:Date = new Date(value);
	var format:DateFormatter = new DateFormatter();
	format.formatString = "YYYY-MM-DD";
	return format.format(date);
}
public function setHSliderValue(value1:Number , value2:Number) {
	if (value1 != 0 ) {
		bw = value1 / dayTime;
	}
	if (value2 != 0) {
		bh = value2 / dayTime;
	}
}
private function setHSlider(year1:Number):void {
	nowPreYear  = year1 ;
	var date1:Date = new Date(year1,0,1);
	var iDate1:Number = date1.getTime();
	
	hSlider.minimum = iDate1 / dayTime;
	la1 = year1 + "年";
}