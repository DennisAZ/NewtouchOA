var xmlhttp;

function init(categories) {
	var nevPanel = document.createElement("div");
	nevPanel.setAttribute("id", "navPanel");
	document.body.appendChild(nevPanel);
	
	var div_first = document.createElement("div");
	div_first.setAttribute("id", "title");
	div_first.className = "navPanel";
	nevPanel.appendChild(div_first);
	
	var div_second = document.createElement("div");
	div_second.className = "s_b";
	div_second.setAttribute("id", "content");
	document.body.appendChild(div_second);
	
	var div_inner = document.createElement("div");
	div_inner.id = "content_div";
	div_second.appendChild(div_inner);
	
	var div_inner1 = document.createElement("div");
	div_inner1.style.display = "none";
	div_inner.appendChild(div_inner1);
	
	var firstTitle = categories[0].title;
	for(var i=0; i<categories.length; i++) {
		var a = document.createElement("a");
		a.setAttribute("href","#");
		
		//将选中的标题栏传入方法中

		var title = categories[i].title;
		a.title = title;
		a.id = i;
		aOnclick(a,title, i);
		div_first.appendChild(a);
		
		var span = document.createElement("span");
		span.width = 80;			
		span.innerHTML = "&nbsp;&nbsp;" + categories[i].title;
		a.appendChild(span);
		
		var img = document.createElement("img");
		var imgUrl = categories[i].imgUrl;
		img.setAttribute("src", imgUrl);
		span.appendChild(img);
		span.appendChild(document.createTextNode("  "));
		
		var div_inner = document.createElement("div");
		div_inner.className = (i == 0 ? "block" : "none");
		div_inner.setAttribute("id","s" + i);
		div_second.appendChild(div_inner);
		
		var div_inner1 = document.createElement("div");
		var contentDiv = document.getElementById(categories[i].content);
		contentDiv.style.display ="";
		div_inner1.appendChild(contentDiv);
		div_inner.appendChild(div_inner1);
		
	}
	aa(firstTitle, 0);
}
    
function aa(title, id) {
	var title_contentUL = document.getElementById("title");
	var count = title_contentUL.childNodes.length;
	
	for(var i=0;i<count;i++) {
		var contentDiv = document.getElementById("s"+i);
		if(i == id) {
			title_contentUL.childNodes[i].className = "active" ;
			contentDiv.className="block"; 
		}else {
			title_contentUL.childNodes[i].className = "" ;
			contentDiv.className="none"; 		
		}
	}
	
}
function aOnclick(obj,title, id){
	
	obj.onclick = function(){
		aa(title,id);
	}

}
