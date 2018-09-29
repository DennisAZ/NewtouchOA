var $ = function(id) {return document.getElementById(id);};
var userAgent = navigator.userAgent.toLowerCase();
var isSafari = userAgent.indexOf("Safari")>=0;
var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);

var allElements=document.getElementsByTagName("*");

String.prototype.trim = function()
{
  return this.replace(/(^[\s\t　]+)|([　\s\t]+$)/g, "");
};

function strlen(str)
{
  return str.replace(/[^\x00-\xff]/g, 'xx').length;
}
function getOpenner()
{
   if(is_moz)
      return parent.opener.document;
   else
      return parent.dialogArguments.document;
}

function isUndefined(variable) {
	return typeof variable == 'undefined' ? true : false;
}

function URLSpecialChars(str)
{
   var re = /%/g;
   str=str.replace(re,"%25");
   re = /\+/g;
   str=str.replace(re,"%20");
   re = /\//g;
   str=str.replace(re,"%2F");
   re = /\?/g;
   str=str.replace(re,"%3F");
   re = /#/g;
   str=str.replace(re,"%23");
   re = /&/g;
   str=str.replace(re,"%26");
   return str;
}
function fetchOffset(obj) {
	var left_offset = obj.offsetLeft;
	var top_offset = obj.offsetTop;
	while((obj = obj.offsetParent) != null) {
		left_offset += obj.offsetLeft;
		top_offset += obj.offsetTop;
	}
	return { 'left' : left_offset, 'top' : top_offset };
}

function new_dom()
{
   var DomType = new Array("microsoft.xmldom","msxml.domdocument","msxml2.domdocument","msxml2.domdocument.3.0","msxml2.domdocument.4.0","msxml2.domdocument.5.0");
   for(var i=0;i<DomType.length;i++)
   {
      try{
         var a = new ActiveXObject(DomType[i]);
         if(!a) continue;
         return a;
      }
      catch(ex){}
   }
   return null;
}

function new_req() {
	if (window.XMLHttpRequest) return new XMLHttpRequest;
	else if (window.ActiveXObject) {
		var req;
		try { req = new ActiveXObject("Msxml2.XMLHTTP"); }
		catch (e) { try { req = new ActiveXObject("Microsoft.XMLHTTP"); }
		catch (e) { return null; }}
		return req;
	} else return null;
}

function _get(url, args, fn, sync)
{
	sync=isUndefined(sync)?true:sync;
	var req = new_req();
	if (args != "") args = "?" + args;
	try{
	   req.open("GET", url + args, sync);
	}
	catch(ex){
	   alert(ex.description);
	   return;
	}
	if(false == isUndefined(fn))
	   req.onreadystatechange = function() { if (req.readyState == 4) fn(req);};
	req.send('');
}

function _post(url, args, fn, sync)
{
   sync=isUndefined(sync)?true:sync;
   var req = new_req();
	try{
	   req.open('POST', url,sync);
	}
	catch(ex){
	   alert(ex.description);
	   return;
	}
	req.setRequestHeader("Method", "POST " + url + " HTTP/1.1");
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	req.onreadystatechange = function() {
			if (req.readyState == 4){
				var s;
				try {s = req.status;}catch (ex) {
						alert(ex.description);
				}
				if (s == 200)fn(req);
			}
	}
	req.send(args);
}

function getCookie(name)
{
	 var arr = document.cookie.split("; ");
	 for(i=0;i<arr.length;i++)
		 if (arr[i].split("=")[0] == name)
			return unescape(arr[i].split("=")[1]);
	 return null;
}
function setCookie(name,value) {
   var today = new Date();
   var expires = new Date();
   expires.setTime(today.getTime() + 1000*60*60*24*2000);
   document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString();
}

function is_offline(uid) {
   var bOffline = true;
   var req = new_req();
	req.open("GET", "/inc/is_offline.php", false);
	req.onreadystatechange=function(){
         if(req.readyState==4 && req.status==200 && req.responseText == uid)
            bOffline = false;
   };
   req.send(null);
   
   return bOffline;
}
//php print_r函数的javascript实现，列出数组中的值，调试程序时调用
function print_r(theObj){
   if(theObj.constructor == Array || theObj.constructor == Object)
   {
     document.write("<ul>")
     for(var p in theObj){
     if(theObj[p].constructor == Array || theObj[p].constructor == Object)
     {
        document.write("<li>["+p+"] => "+typeof(theObj)+"</li>");
        document.write("<ul>")
        print_r(theObj[p]);
        document.write("</ul>")
     }
     else
     {
        document.write("<li>["+p+"] => "+theObj[p]+"</li>");
     }
   }
   document.write("</ul>")
  }
}