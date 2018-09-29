function AssistInput(id,requestUrl,fun){
	//-----------------初始化--------
	var input = $(id);
	if(input == null){
		alert('没有找到要的input标签');
		return ;	
	}
	if(input.getAttribute('type')!='text'){
		alert('不是text类型的');
		return ;	
	}
	input.onkeyup = assist;
	input.onfocus = assist;
	input.onblur = onblurEvent1;
	//-------------------------------
	var selectedId=-1;
  
function assist(e){
	var div = $("div-assist");
	if(input.value==""){
		if(div!=null){
			div.style.display = "none";
		}
		return ;
	}
	
	 var e=e||event;
	　 var currKey=e.keyCode||e.which||e.charCode;
	 if(currKey==38||currKey==40){
		 if(div==null&&div.style.display=="none"){
			 return ;
		 }
		 var isUp=false;
		 if(currKey==38){
			 isUp=true;
		 }else{
			 isUp=false;
		 }
		 chageSelection(isUp); 
		 return ;
	 }
	if(currKey==13){
		if((div==null&&div.style.display=="none")||selectedId==-1){
			 return ;
		 }
		if(selectedId >= 10){
			//alert('dddd');
			fun();
		}else{
			input.value = $("div-"+selectedId).getAttribute("name");
		}
	}
	
	if(div==null){
	var left = input.offsetLeft;
	var top = input.offsetTop+input.offsetHeight;
	var div = document.createElement("div");
	div.style.position = "absolute";
	div.style.border = "1px solid black";
	div.style.backgroundColor = "#cccccc";
	//GetXYWH(input,div);
	
	div.setAttribute("id","div-assist");
	div.style.width = "200px";
	document.body.appendChild(div);
	showDiv(input,div);
	}else{
		//GetXYWH(input,div);
		showDiv(input,div);
		div.style.display = "";
	}
	//去掉以前的记录
	var childs =  div.getElementsByTagName("div");
	for(var i = childs.length-1 ;i >= 0 ;i--){
		div.removeChild(childs[i]);
		
	}
	
	var loading = "image/loading.gif";
	var img = $("load-img");
	if(img == null){
	 img = document.createElement("img");
	img.src = loading;
	img.setAttribute("id","load-img");
	div.appendChild(img);
	}
	getList(input.value);
}
function getList(str){
	var get =new Ajax.Request(
			requestUrl+str,
			{
				method:'get',
				onComplete: getListResponse
			 });
}
function getListResponse(response){
if(response.readyState==4){
		if(response.status==200){
			var div = $("div-assist");
			var img = $("load-img");
			if(img!=null){
				div.removeChild(img);
			}
			var count = response.responseXML.getElementsByTagName("count")[0].firstChild.data;
			if(count<1){
				div.style.display = "none";
				return ;
			}
			var lis = response.responseXML.getElementsByTagName("li");
			var i=0;
			for(i=0; i<lis.length; i++){
				var li = lis[i];
				var d1 = $("div-"+i);
				if(d1==null){
				var id = li.getElementsByTagName("id")[0].firstChild.data;
				var name = li.getElementsByTagName("name")[0].firstChild.data;
				
				var d = document.createElement("div");
				d.setAttribute("id", "div-"+i);
				d.setAttribute("name",name)
				d.onmouseover = function(){
					selectedId = this.id.substr(4);
					var divs = $("div-assist").childNodes;
					for(var i=0;i<divs.length;i++){
						if(divs[i].id.substr(4)==selectedId){
							divs[i].className = "div-onmouseover";
						}else{
							divs[i].className = "";
						}
						
					}
				}
				d.onmouseout = function(){
					this.className = "";
				}
				d.onmousedown = function(){
					//var input =	$("test");
					input.value = $("div-"+selectedId).getAttribute("name");
						
				}
				d.appendChild(document.createTextNode(name+":"+id));
				div.appendChild(d);
				}
			}
			if(parseInt(count)>10){
				var d1 = $("div-"+i);
				if(d1==null){
				var d = document.createElement("div");
				d.setAttribute("id", "div-"+i);
				d.onmouseover = function(){
					selectedId = this.id.substr(4);
					var divs = $("div-assist").childNodes;
					for(var i=0;i<divs.length;i++){
						if(divs[i].id.substr(4)==selectedId){
							divs[i].className = "div-onmouseover";
						}else{
							divs[i].className = "";
						}
						
					}
				}
				d.onmouseout = function(){
					this.className = "";
				}
				var a = document.createElement("a");
				a.setAttribute("href", "#");
				a.onmousedown = function(){
					// alert('dddddd');	
					
				}
				a.appendChild(document.createTextNode("...."));
				d.appendChild(a);
				div.appendChild(d);
				}
			}
			selectedId = -1;
		}else if(response.status==404){
				alert("404 错误");
		}else if(response.status==403){
				alert("403 错误");
		}else if(response.status==401){
				alert("401 错误");
		}else {
				alert("未知错误");
		}
	}
}
function onblurEvent1(){
	selectedId = -1;
	var div = $("div-assist");
	if(div!=null){
		div.style.display = "none";
	}	
}
function chageSelection(isUp){
	var divs = $("div-assist").childNodes;
	if (isUp){
		selectedId--;
	}
	else{
		selectedId++;
	}
	if(selectedId<0){
		selectedId = divs.length-1;
	}
	if(selectedId>=divs.length){
		selectedId = 0;
	}
	for(var i=0;i<divs.length;i++){
		if(divs[i].id.substr(4)==selectedId){
			divs[i].className = "div-onmouseover";
		}else{
			divs[i].className = "";
		}
	   }
	}

var w3c=(document.getElementById)? true:false;
var agt=navigator.userAgent.toLowerCase();
var ie = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1) && (agt.indexOf("omniweb") == -1));
var ie5=(w3c && ie)? true : false;
var ns6=(w3c && (navigator.appName=="Netscape"))? true: false;
var op8=(navigator.userAgent.toLowerCase().indexOf("opera")==-1)? false:true;

function Obj(o){
 return document.getElementById(o)?document.getElementById(o):o;
}
function showDiv(obj,div){
	if(document.all){
		showIEDiv(obj,div);
	 }else{
		showFFDiv(obj,div);
	 }	
}
function showFFDiv(o,div){
var nLt=0;
var nTp=0;
 var offsetParent = o;
 while (offsetParent!=null && offsetParent!=document.body) {
 nLt+=offsetParent.offsetLeft;
 nTp+=offsetParent.offsetTop;
 offsetParent=offsetParent.offsetParent;
 }
 var height = nTp + o.offsetHeight;
 div.style.top = height+"px";
 div.style.left = nLt+"px";
}
function showIEDiv(obj,div) {
	// 保存元素
	var el = obj;
	// 获得元素的左偏移量
	var left = obj.offsetLeft;
	// 获得元素的顶端偏移量
	var top = obj.offsetTop;

	// 循环获得元素的父级控件，累加左和顶端偏移量
	while (obj = obj.offsetParent) {
	left += obj.offsetLeft;
	top += obj.offsetTop;
	}
	// 设置层的坐标并显示
	div.style.pixelLeft = left;
	 var height = top + el.offsetHeight;
	// 层的顶端距离为元素的顶端距离加上元素的高
	div.style.pixelTop = height;
	
	}
}

