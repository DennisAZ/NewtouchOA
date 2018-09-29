//topOffSet:顶部偏移量（顶部灰色导航高度）   leftOffSet:左侧偏移量（左侧固定模块宽度）
var IE=false,FF=false,W=window,D=document,B,GET="getElementsByTagName",GEI="getElementById",GEN="getElementsByName",topOffSet=20,leftOffSet=280;

//弹出层
function popDiv(n,arg1){
	var width;
	var height;
	var idPre="";
	var titleTxt="";
	var tips="（带<span class='red'>*</span>为必填字段）";
	var hasScroll = false;
	
	switch(n){
		case 1:
			height=330;
			titleTxt="新建日程";
			url="messageAction.do?op=fowardSch";
			break;
		
		case 2:
			height=300;
			titleTxt="查看日程";
			hasScroll = true;
			url="messageAction.do?op=showScheduleInfo&isEdit=0&id="+arg1;
			break;
			
		case 3:
			height=520;
			width=650;
			titleTxt="全国天气";
			url="http://flash.weather.com.cn/wmaps/index.swf";
			break;
	}
	createPopWindow(idPre,titleTxt,url,width,height,true,hasScroll);
}		


//收缩层
function fold(){
	var e;
	e=fixE(e);
	if(e)
		element=fixElement(e);
	element=element.parentNode.parentNode.parentNode;
	var imgObj=element.childNodes[0].childNodes[0].childNodes[0];
	if(element.className.indexOf("hide")>0){
		element.className=element.className.substring(0,7);// 模块样式名不能大于7位 
		imgObj.src="images/content/whiteHide.gif";
		imgObj.alt="收起";
	}
	else{
		element.className+=" hide";
		imgObj.src="images/content/whiteShow.gif";
		imgObj.alt="展开";
	}
	// 保存cookie
	ModPos.saveInf();
}

//关闭层
function closeMod(){
	if(confirm("确定要关闭这个模块吗？")){
		var e;
		e=fixE(e);
		if(e)
			element=fixElement(e);
		element=element.parentNode.parentNode.parentNode;
		element.style.display="none";
		//把该模块移到tempDiv
		$("tempDiv").appendChild(element);
		// 保存cookie
		ModPos.saveInf();
	}
	else {
		return false;
	}
}
	
//drag对象	
var Drag={
	draging : false,
	x : 0,
	y : 0,
	element : null,
	fDiv : null,
	ghost : null,
	addEvent : function(){
		var a=D[GET]("li");
		for(var i=a.length-1;i>-1;i--){
			if(a[i].className.substring(0,6)=="module")
				a[i].onmousedown=Drag.dragStart;
		}
	},
	ix:2,iy:7,
	ox:6,oy:7,
	fx:6,fy:6,
	dragStart : function (e){
		if(Drag.draging)
			return;
		var e;
		e=fixE(e);
		if(e)
			element=fixElement(e);
		  
		if(element.className.substring(0,5)!="title")
			return;
		element=element.parentNode;
		Drag.element=element;//element为li对象
		//以上获得当前移动的模块
		Drag.x=e.layerX?e.layerX+Drag.fx:(IE?e.x+Drag.ix:e.offsetX+Drag.ox);
		Drag.y=e.layerY?e.layerY+Drag.fy:(IE?e.y+Drag.iy:e.offsetY+Drag.oy);
		//鼠标相对于模块的位置
		Drop.measure();
		if(e.layerX){
			Drag.floatIt(e);Drag.drag(e);
		}//fix FF
		B.style.cursor="move";
		D.onmousemove=Drag.drag;
		D.ondragstart=function(){
			window.event.returnValue = false;
		};
		D.onselectstart=function(){
			window.event.returnValue = false;
		};
		D.onselect=function(){
			return false
		};
		D.onmouseup=element.onmouseup=Drag.dragEnd;
		element.onmousedown=null;
	},
	
	drag : function (e){
		var e;
		e=fixE(e);
		if(!Drag.fDiv)
			Drag.floatIt(e);//for IE & Opera
		var x=e.clientX+B.scrollLeft,y=e.clientY+B.scrollTop;
		
		
		if(y-Drag.y<topOffSet){
			Drag.fDiv.style.top=topOffSet;
		}
		else{
			Drag.fDiv.style.top=y-Drag.y+"px";
		}
		if(x-Drag.x<leftOffSet){
			Drag.fDiv.style.left=leftOffSet;
		}
		else{
			Drag.fDiv.style.left=x-Drag.x+"px";
		}
		Drop.drop(x,y);
	},
	
	dragEnd : function (e){
		B.style.cursor="";
		D.ondragstart=D.onmousemove=D.onselectstart=D.onselect=D.onmouseup=null;
		Drag.element.onmousedown=Drag.dragStart;
		if(!Drag.draging)
			return;
		Drag.ghost.parentNode.insertBefore(Drag.element,Drag.ghost);
		Drag.ghost.parentNode.removeChild(Drag.ghost);
		B.removeChild(Drag.fDiv);
		Drag.fDiv=null;
		Drag.draging=false;
		Drop.init(D[GEI]("container"));
		
		// 保存cookie
	   	ModPos.saveInf();
	   
	},
	
	floatIt : function(e){
		var e,element=Drag.element;
		var ghost=D.createElement("LI");
		Drag.ghost=ghost;
		ghost.className="module ghost";
		ghost.style.height=element.offsetHeight-2+"px";
		element.parentNode.insertBefore(ghost,element);

		//创建模块占位框
		var fDiv=D.createElement("UL");
		Drag.fDiv=fDiv;
		fDiv.className="float";
		B.appendChild(fDiv);
		fDiv.style.width=ghost.parentNode.offsetWidth+"px";
		fDiv.appendChild(element);
		//创建容纳模块的浮动层
		Drag.draging=true;
	}
}

//drop对象
var Drop={
	root : null,
	index : null,
	column : null,
	init : function(it){
		if(!it)return;
		Drop.root=it;
		it.firstItem=it.lastItem=null;
		
		var a=it[GET]("ul");
		for(var i=0;i<a.length;i++){
			if(a[i].className!="column")
				continue;
			if(it.firstItem==null){
				it.firstItem=a[i];
				a[i].previousItem=null;
			}else{
				a[i].previousItem=a[i-1];
				a[i-1].nextItem=a[i];
			}
			a[i].nextItem=null;
			it.lastItem=a[i];
			a[i].index=i;
			a[i].firstItem=a[i].lastItem=null;
			var b=a[i][GET]("li");
			for(var j=0;j<b.length;j++){
				if(b[j].className.indexOf("module")==-1)
					continue;
				if(a[i].firstItem==null){
					a[i].firstItem=b[j];
					b[j].previousItem=null;
				}else{
					b[j].previousItem=b[j-1];
					b[j-1].nextItem=b[j];
				}
				b[j].nextItem=null;
				a[i].lastItem=b[j];
				b[j].index=i+","+j;
			}
		}
	},
	
	measure : function(){
		if(!Drop.root)
			return;
		var currentColumn=Drop.root.firstItem;
		while(currentColumn){
			var currentModule=currentColumn.firstItem;
			while(currentModule){
				 currentModule.minY=currentModule.offsetTop;
				 currentModule.maxY=currentModule.minY+currentModule.offsetHeight;
				 currentModule=currentModule.nextItem;
			}
			currentColumn.minX=currentColumn.offsetLeft;
			currentColumn.maxX=currentColumn.minX+currentColumn.offsetWidth;
			currentColumn=currentColumn.nextItem;
		}
		Drop.index=Drag.element.index;
	},
	drop : function(x,y){
		if(!Drop.root)
			return;
		var x,y,currentColumn=Drop.root.firstItem;
		while(x>(currentColumn.maxX+leftOffSet)){
			if(currentColumn.nextItem){
				currentColumn=currentColumn.nextItem;
			}
			else 
				break;
		}
				
		var currentModule=currentColumn.lastItem;
		if(currentModule)
			while(y<currentModule.maxY){
				if(y>currentModule.minY-12){
					if(Drop.index==currentModule.index){
						return;
					}
					Drop.index=currentModule.index;
					if(currentModule.index==Drag.element.index){
						if(currentModule.nextItem)
							currentModule=currentModule.nextItem;
						else {
							break;
						}
					}
				currentColumn.insertBefore(Drag.ghost,currentModule);
				Drop.column=null;
				return;
			}else if(currentModule.previousItem)
				currentModule=currentModule.previousItem;
			else {
				return;
			}
		}
		if(Drop.column==currentColumn.index){
			return;
		}
		currentColumn.appendChild(Drag.ghost);
		Drop.index=0;
		Drop.column=currentColumn.index;
	} 
}

var ModPos={
	cookiePre:null,
	init:function(id,cPre){ 
	   var container = D[GEI](id);
	   ModPos.cookiePre = cPre;
	   //读取cookie 
	   /*
	   		格式： |{ul_id}:{li_id}.{li_isFold},... >:{li_id}.{li_isFold},...
	   */
	   var pos = ModPos.readCookie("modPos"); 
	   if(pos != ""){
			var allcontainer = pos.split(">");//分离已关闭的模块
			var subcontainer = allcontainer[0].split("|");
			for(var i=0 ; i < subcontainer.length; i++){ 
				var subcontainerItem = subcontainer[i].split(":");//分离ul
				if(D[GEI](subcontainerItem[0])){ 
					var items = subcontainerItem[1].split(","); //分离li
					for(var j=0; j< items.length; j++){ 
						var mod = items[j].split(".");//分离是否收缩
						if(D[GEI](mod[0])) {
							if(mod[1]=="0"){
								D[GEI](mod[0]).className=D[GEI](mod[0]).className.substring(0,7);
								var imgObj=D[GEI](mod[0]).childNodes[0].childNodes[0].childNodes[0];
								imgObj.src="images/content/whiteHide.gif";
								imgObj.alt="收起";
							}
							D[GEI](mod[0]).style.display="";
							D[GEI](subcontainerItem[0]).appendChild(D[GEI](mod[0]));
						}
					} 
				} 
			} 
			
			//加载已被关闭的模块
			if(allcontainer[1].length>0){
				var closedmods = allcontainer[1].split(","); 
				for(var k=0; k<closedmods.length;k++){
					var cmod = closedmods[k].split(".");
					D[GEI](cmod[0]).style.display="none";
					if(D[GEI](cmod[0])) {
						if(cmod[1]=="0"){
							D[GEI](cmod[0]).className=D[GEI](cmod[0]).className.substring(0,7);
							var imgObj2=D[GEI](cmod[0]).childNodes[0].childNodes[0].childNodes[0];
							imgObj2.src="images/content/whiteHide.gif";
							imgObj2.alt="收起";
						}
						$("tempDiv").appendChild(D[GEI](cmod[0]));
					}
				}
			}
	   } 
	   else {
		   //未找到cookies文件
		   	for(var c1=0;c1<$("col1").childNodes.length;c1++){
			   	var e1=$("col1").childNodes[c1];
				e1.className=e1.className.substring(0,7);
				var imgObj1=e1.childNodes[0].childNodes[0].childNodes[0];
				imgObj1.src="images/content/whiteHide.gif";
				imgObj1.alt="收起";
				e1.style.display="";
		   	}
			for(var c2=0;c2<$("col2").childNodes.length;c2++){
				var e2=$("col2").childNodes[c2];
				e2.className=e2.className.substring(0,7);
				var imgObj2=e2.childNodes[0].childNodes[0].childNodes[0];
				imgObj2.src="images/content/whiteHide.gif";
				imgObj2.alt="收起";
				e2.style.display="";
			}
	   }
	   //清除空白节点 
	   cleanWhitespace(D[GEI](id));
	}, 
	
	//保存模块位置（显示顺序），显示状态
	saveInf:function(){
		var str=""; 
		var container=D[GEI]("container");
	   	for(var i=0; i<container.childNodes.length; i++){ 
			var o = container.childNodes[i]; 
			if(i>0)
				str += "|"; 
			str += o.id + ":"; 
			for(var j=0; j < o.childNodes.length; j++){ 
				if(j>0)
					str += ","; 
			 	str += o.childNodes[j].id ; 
				//保存显示状态
				if(o.childNodes[j].className.indexOf("hide")>0){
					str += ".1";//模块已收缩
				}
				else{
					str += ".0";
				}
			} 
	   	} 
		//保存已关闭的模块
		str += ">";
		if($("tempDiv").childNodes.length>0){
			for(var k=0; k<$("tempDiv").childNodes.length;k++){
				if(k>0)
					str += ","; 
				var closeMod = $("tempDiv").childNodes[k]; 
				str += closeMod.id; 
				if(closeMod.className.indexOf("hide")>0){
					str += ".1";//模块已收缩
				}
				else{
					str += ".0";
				}
			}
		}

		ModPos.saveCookie("modPos",str,24*30); //cookie保存一个月
	},
	
	//保存cookie: hours=cookie失效时间
	saveCookie:function(name, value, hours){ 
		name = ModPos.cookiePre + name;
	   	var expire = ""; 
	   	if(hours != null){ 
		   	expire = new Date((new Date()).getTime() + hours * 3600000); 
		   	expire = "; expires=" + expire.toGMTString(); 
	   	} 
	   	document.cookie = name + "=" + escape(value) + expire; 
	}, 
	
	//读取cookie: name=cookie名
	readCookie:function(name){ 
		name = ModPos.cookiePre + name;
	   	var cookieValue = ""; 
	   	var search = name + "="; 
	   	if(document.cookie.length > 0){  
		   	offset = document.cookie.indexOf(search); 
		   	if (offset != -1) 
		   	{  
			 	offset += search.length; 
			 	end = document.cookie.indexOf(";", offset); 
			 	if (end == -1) end = document.cookie.length; 
			 	cookieValue = unescape(document.cookie.substring(offset, end)) 
		   	} 
	   	} 
	   	return cookieValue; 
	} 
}

/*//清除空白,换行节点 
function cleanWhitespace(node) { 
	var notWhitespace = /\S/; 
    for (var i=0; i < node.childNodes.length; i++) { 
          var childNode = node.childNodes[i]; 
          if ((childNode.nodeType == 3)&&(!notWhitespace.test(childNode.nodeValue))) { 
              node.removeChild(node.childNodes[i]); 
              i--; 
          } 
          if (childNode.nodeType == 1) { 
              cleanWhitespace(childNode); 
          } 
      } 
} */
	
function fixE(e){
	var e;e=e?e:(window.event?window.event:null);
	return e;
}

function fixElement(e){
	var e;
	return e.target?(e.target.nodeType==3?e.target.parentNode:e.target):e.srcElement;
}


/* 自定义工作台 */ 
//初始化多选框
function initDefDeskTop(){
	var array=D[GEN]("isClose");
	//读取cookie 
	var str = ModPos.readCookie("modPos"); 
	//读取已关闭的模块
	if(str != ""){
		var allcontainer = str.split(">"); 
		var mods = allcontainer[1].split(","); 
		for(var i=0 ; i < mods.length; i++){ 
			var modItem = mods[i].split("."); 
			if(D[GEI](modItem[0])){ 
				for(var k=0;k<array.length;k++){
					if(array[k].value==modItem[0]){
						array[k].checked=false;
						break;
					}
				}
			} 
		} 
   } 

}

//保存
function setDeskTop(){
	var array=D[GEN]("isClose");
	for(var i=0;i<array.length;i++){
		if(array[i].checked==false){
			D[GEI](array[i].value).style.display="none";
			//把该模块移到tempDiv
			$("tempDiv").appendChild($(array[i].value));
		}
		else{
			if($(array[i].value).parentNode.id=="tempDiv"){
				$(array[i].value).style.display="";
				$(array[i].value).addClassName("hide");
				var imgObj=$(array[i].value).childNodes[0].childNodes[0].childNodes[0];
				imgObj.src="images/content/whiteShow.gif";
				imgObj.alt="展开";
				$("col1").insertBefore($(array[i].value),$("col1").firstDescendant());
			}
		}
	}
	// 保存cookie
	ModPos.saveInf();
	alert("保存成功！");
	//刷新
	//history.go(0);
}