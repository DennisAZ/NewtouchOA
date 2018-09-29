function alertWin(title, msg, w, h){   
	var titleheight = "22px"; //提示窗口标题高度 
	var bordercolor = "#4F96FF"; //提示窗口的边框颜色 
	var titlecolor = "#FFFFFF"; //提示窗口的标题颜色 
	var titlebgcolor = "#4F96FF"; 
	var bgcolor = "#FFFFFF"; 
	var iWidth = document.documentElement.clientWidth; 
	var iHeight = document.documentElement.clientHeight; 
	var bgObj = document.createElement("div"); 
	bgObj.id = "BgDiv"; 
	bgObj.style.cssText = "position:absolute;left:0px;top:0px;width:"+iWidth+"px;height:"+Math.max(document.body.clientHeight, iHeight)+"px;filter:Alpha(Opacity=40);opacity:0.3;background-color:#000000;z-index:100;"; 
	document.body.appendChild(bgObj); 
	var msgObj=document.createElement("div"); 
	msgObj.id = "MsgDiv"; 
	msgObj.style.cssText = "position:absolute;font:11pt '宋体';top:"+150+"px;left:"+230+"px;width:"+w+"px;height:"+h+"px;text-align:center;border:1px solid "+bordercolor+";background-color:"+bgcolor+";padding:1px;line-height:22px;z-index:102;"; 
	document.body.appendChild(msgObj); 
	var table = document.createElement("table"); 
	msgObj.appendChild(table); 
	table.style.cssText = "margin:0px;border:0px;padding:0px;"; 
	table.cellSpacing = 0; 
	var tr = table.insertRow(-1); 
	var titleBar = tr.insertCell(-1); 
	titleBar.style.cssText = "width:"+w+"px;height:"+titleheight+"px;text-align:left;padding:3px;margin:0px;font:bold 13px '宋体';color:"+titlecolor+";border:1px solid " + bordercolor + ";cursor:move;background-color:" + titlebgcolor+";"; 
	titleBar.style.paddingLeft = "10px"; 
	titleBar.innerHTML = title; 
	var mask = document.createElement("iframe");
	mask.src="javascript:false";
	mask.id = "mask";
	mask.scrolling="no";
	mask.frameborder="0";
	//alert(msgObj.style.width);
	mask.style.cssText = "position:absolute;z-index:"+msgObj.style.zIndex-1+";top:"+msgObj.style.top+";left:"+msgObj.style.left+";width:"+msgObj.style.width+";height:"+msgObj.style.height+";";
	mask.style.position="absolute";
	document.body.appendChild(mask);
	var moveX = 0; 
	var moveY = 0; 
	var moveTop = 0; 
	var moveLeft = 0; 
	var moveable = false;
	//addEvent(msgObj, "mousedown", mousedown);
	//addEvent(msgObj, "mouseup", mouseup);
	//addEvent(msgObj, "selectstart", function() {return false;});
	//addEvent(msgObj, "mousemove", mousemove);
	var resizeEvent;
	function mousedown() { 
		moveable = true; 
		resizeEvent=window.onresize;
		window.onresize = function() {
			bgObj.style.width = document.documentElement.clientWidth + "px";
			bgObj.style.height = documes3Qnt.documentElement.clientHeight + "px";
		}
		if (getBrowser()=="IE" && msgObj == this){
		  msgObj.setCapture();
		}
		var evt = getEvent(); 
		  moveX = evt.clientX; 
		  moveY = evt.clientY; 
		  moveTop = parseInt(msgObj.style.top); 
		  moveLeft = parseInt(msgObj.style.left);
		} 
		function mousemove() {
			if (moveable) { 
			  var evt = getEvent(); 
			  var x = moveLeft + evt.clientX - moveX; 
			  var y = moveTop + evt.clientY - moveY; 
			  msgObj.style.left = x + "px"; 
			  msgObj.style.top = y + "px"; 
			  mask.style.left = msgObj.style.left;
			  mask.style.top = msgObj.style.top;
			} 
		}
		function mouseup() {
			if (moveable) { 
			  moveX = 0; 
			  moveY = 0; 
			  moveTop = 0; 
			  moveLeft = 0; 
			  moveable = false; 
			  window.onresize = resizeEvent;
				if (getBrowser()=="IE") {
					msgObj.releaseCapture();
				}
			} 
		}
		var closeBtn = tr.insertCell(-1); 
		closeBtn.style.cssText = "cursor:pointer; padding:2px;background-color:" + titlebgcolor+";"; 
		closeBtn.innerHTML = "<span style='font-size:15pt; color:"+titlecolor+";'>×</span>"; 
		closeBtn.onclick = function(){ 
		  winClose();
		} 
		var msgBox = table.insertRow(-1).insertCell(-1); 
		msgBox.id = "MsgBox"; 
		msgBox.style.cssText = "font:10pt '宋体';"; 
		msgBox.colSpan = 2; 
		msgBox.innerHTML = msg; 
} 
function winClose(){
  var bgObj = $('BgDiv');
  
  var msgObj = $('MsgDiv');
  var mask = $('mask');
  document.body.removeChild(bgObj); 
  document.body.removeChild(msgObj); 
  document.body.removeChild(mask);
}
// 获得事件Event对象，用于兼容IE和FireFox 
function getEvent() { 
	return window.event || arguments.callee.caller.arguments[0]; 
} 
// 事件绑定
function addEvent(obj,eventTypeName, fn){
	if(obj.addEventListener){
		obj.addEventListener(eventTypeName,fn,true);
		return true;
	}else if(obj.attachEvent){
		return obj.attachEvent("on"+eventTypeName,fn);
	}else{
		return false;
	}
}
// 检测浏览器类型
function getBrowser() {
  var b;
  if ((navigator.userAgent.indexOf("MSIE") >= 0) 
      && (navigator.userAgent.indexOf('Opera') < 0)) {
    b = "IE"; 
  }else if (navigator.userAgent.indexOf("Firefox") >= 0) {
    b = "Firefox"; 
  }else if (navigator.userAgent.indexOf("Opera") >= 0){
    b = "Opera"; 
  }else {
    b = "Other";
  }
  return b;
}