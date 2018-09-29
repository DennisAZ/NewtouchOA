function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
  //getSwf("smalltouchgraph")).doSearch("天气");
}
function getSwf(swfID) {
  if (navigator.appName.indexOf("Microsoft") != -1) {
    return window[swfID];
  } else {
    return document[swfID];
  }
}  
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}

/**
 *  替换s1为s2
 */
 String.prototype.replaceAll  = function(s1,s2){   
   return this.replace(new RegExp(s1,"gm"),s2);   
 }
 function getUrl(fName, file_id, moudle){
   var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
   readUrl += "&attachmentId="+ file_id +"&moudle="+ moudle +"&op=5&signKey=&print=";  
   return readUrl;
 }
 
 function openWin(url){  
   var widths = document.body.clientWidth;  
   var heights = document.body.clientHeight;  
   myleft=(screen.availWidth-500)/5;
   window.open(url, "window","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=10,top=10,resizable=yes,height="+ heights +", width="+ widths +"");
 }
 
 function getUrlWindow(fName, modual, file_id){    
   var url = getUrl(fName, file_id, modual);
   openWin(url);
 }