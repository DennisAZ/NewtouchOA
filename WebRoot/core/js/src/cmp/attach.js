var userAgent = navigator.userAgent.toLowerCase();
var is_opera = userAgent.indexOf('opera') != -1 && opera.version();
var is_moz = (navigator.product == 'Gecko') && userAgent.substr(userAgent.indexOf('firefox') + 8, 3);
var is_ie = (userAgent.indexOf('msie') != -1 && !is_opera) && userAgent.substr(userAgent.indexOf('msie') + 5, 3);
var imageType = "gif,jpg,jpeg,png,bmp,iff,jp2,jpx,jb2,jpc,xbm,wbmp";
var currFormFile = null;
function isUndefined(variable) {
  return typeof variable == 'undefined' ? true : false;
}

var jsmenu = new Array();
var ctrlobjclassName;
jsmenu['active'] = new Array();
jsmenu['timer'] = new Array();
jsmenu['iframe'] = new Array();

function initCtrl(ctrlobj, click, duration, timeout, layer) {
  if(ctrlobj && !ctrlobj.initialized) {
    ctrlobj.initialized = true;
    ctrlobj.unselectable = true;

    ctrlobj.outfunc = typeof ctrlobj.onmouseout == 'function' ? ctrlobj.onmouseout : null;
    ctrlobj.onmouseout = function() {
      if(this.outfunc) this.outfunc();
      if(duration < 3) jsmenu['timer'][ctrlobj.id] = setTimeout('hideMenu(' + layer + ')', timeout);
    }

    ctrlobj.overfunc = typeof ctrlobj.onmouseover == 'function' ? ctrlobj.onmouseover : null;
    ctrlobj.onmouseover = function(e) {
      doane(e);
      if(this.overfunc) this.overfunc();
      if(click) {
        clearTimeout(jsmenu['timer'][this.id]);
      } else {
        for(var id in jsmenu['timer']) {
          if(jsmenu['timer'][id]) clearTimeout(jsmenu['timer'][id]);
        }
      }
    }
  }
}

function initMenu(ctrlid, menuobj, duration, timeout, layer, drag) {
  if(menuobj && !menuobj.initialized) {
    menuobj.initialized = true;
    menuobj.ctrlkey = ctrlid;
    menuobj.onclick = ebygum;
    menuobj.style.position = 'absolute';
    if(duration < 3) {
      if(duration > 1) {
        menuobj.onmouseover = function() {
          clearTimeout(jsmenu['timer'][ctrlid]);
        }
      }
      if(duration != 1) {
        menuobj.onmouseout = function() {
          jsmenu['timer'][ctrlid] = setTimeout('hideMenu(' + layer + ')', timeout);
        }
      }
    }
    menuobj.style.zIndex = 50;
    if(is_ie) {
      menuobj.style.filter += "progid:DXImageTransform.Microsoft.shadow(direction=135,color=#CCCCCC,strength=2)";
    }
    if(drag) {
      menuobj.onmousedown = function(event) {try{menudrag(menuobj, event, 1);}catch(e){}};
      menuobj.onmousemove = function(event) {try{menudrag(menuobj, event, 2);}catch(e){}};
      menuobj.onmouseup = function(event) {try{menudrag(menuobj, event, 3);}catch(e){}};
    }
  }
}

var menudragstart = new Array();
function menudrag(menuobj, e, op) {
  if(op == 1) {
    if(in_array(is_ie ? event.srcElement.tagName : e.target.tagName, ['TEXTAREA', 'INPUT', 'BUTTON', 'SELECT'])) {
      return;
    }
    menudragstart = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
    menudragstart[2] = parseInt(menuobj.style.left);
    menudragstart[3] = parseInt(menuobj.style.top);
    doane(e);
  } else if(op == 2 && menudragstart[0]) {
    var menudragnow = is_ie ? [event.clientX, event.clientY] : [e.clientX, e.clientY];
    menuobj.style.left = (menudragstart[2] + menudragnow[0] - menudragstart[0]) + 'px';
    menuobj.style.top = (menudragstart[3] + menudragnow[1] - menudragstart[1]) + 'px';
    doane(e);
  } else if(op == 3) {
    menudragstart = [];
    doane(e);
  }
}

function showMenu(ctrlid, align, click, offset, duration, timeout, layer, showid, maxh, drag) {
  var ctrlobj = $(ctrlid);
  if(!ctrlobj) return;
  if(isUndefined(align)) align = 0;
  if(isUndefined(click)) click = false;
  if(isUndefined(offset)) offset = 0;
  if(isUndefined(duration)) duration = 2;
  if(isUndefined(timeout)) timeout = 200;
  if(isUndefined(layer)) layer = 0;
  if(isUndefined(showid)) showid = ctrlid;
  var showobj = $(showid);
  var menuobj = $(showid + '_menu');
  if(!showobj|| !menuobj) return;
  if(isUndefined(maxh)) maxh = 400;
  if(isUndefined(drag)) drag = false;

  if(click && jsmenu['active'][layer] == menuobj) {
    hideMenu(layer);
    return;
  } else {
    hideMenu(layer);
  }

  var len = jsmenu['timer'].length;
  if(len > 0) {
    for(var i=0; i<len; i++) {
      if(jsmenu['timer'][i]) clearTimeout(jsmenu['timer'][i]);
    }
  }

  initCtrl(ctrlobj, click, duration, timeout, layer);
  ctrlobjclassName = ctrlobj.className;
  ctrlobj.className += ' hover';
  initMenu(ctrlid, menuobj, duration, timeout, layer, drag);

  menuobj.style.display = 'block';
  if(!is_opera) {
    menuobj.style.clip = 'rect(auto, auto, auto, auto)';
  }

  setMenuPosition(showid, align, offset);

  if(is_ie && is_ie < 7) {
    if(!jsmenu['iframe'][layer]) {
      var iframe = document.createElement('iframe');
      iframe.style.display = 'none';
      iframe.style.position = 'absolute';
      iframe.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(style=0,opacity=0)';
      $('append_parent') ? $('append_parent').appendChild(iframe) : menuobj.parentNode.appendChild(iframe);
      jsmenu['iframe'][layer] = iframe;
    }
    jsmenu['iframe'][layer].style.top = menuobj.style.top;
    jsmenu['iframe'][layer].style.left = menuobj.style.left;
    jsmenu['iframe'][layer].style.width = menuobj.w;
    jsmenu['iframe'][layer].style.height = menuobj.h;
    jsmenu['iframe'][layer].style.display = 'block';
  }

  if(maxh && menuobj.scrollHeight > maxh) {
    menuobj.style.height = maxh + 'px';
    if(is_opera) {
      menuobj.style.overflow = 'auto';
    } else {
      menuobj.style.overflowY = 'auto';
    }
  }

  if(!duration) {
    setTimeout('hideMenu(' + layer + ')', timeout);
  }

  jsmenu['active'][layer] = menuobj;
}

function setMenuPosition(showid, align, offset) {
  var showobj = $(showid);
  var menuobj = $(showid + '_menu');
  if(isUndefined(align)) align = 0;
  if(isUndefined(offset)) offset = 0;
  if(showobj) {
     if(align==0)
     {
       showobj.pos = getMousePos();
       showobj.X = showobj.pos['left']-20;
       showobj.Y = showobj.pos['top'];
    }
    else
    {
       showobj.pos = fetchOffset(showobj);
       showobj.X = showobj.pos['left'];
       showobj.Y = showobj.pos['top']+showobj.scrollHeight-3;
    }
    var menu_offset = getMenuOffset(showobj.id);
    showobj.w = showobj.offsetWidth;
    showobj.h = showobj.offsetHeight;
    menuobj.w = menuobj.offsetWidth;
    menuobj.h = menuobj.offsetHeight;
    //if(align == 1)
       //menuobj.style.width = ((menuobj.clientWidth < showobj.w ? showobj.w : menuobj.clientWidth)+2) + 'px';
    if(offset < 3) {
      menuobj.style.left = ((showobj.X + menuobj.w > document.body.clientWidth) && (showobj.X + showobj.w - menuobj.w >= 0) ? showobj.X + showobj.w - menuobj.w : showobj.X) - menu_offset['left'] + 'px';
      if(offset == 1)
         menuobj.style.top = showobj.Y - menu_offset['top'] + 'px';
      else if(offset == 2)
         menuobj.style.top = (showobj.Y - menuobj.h - menu_offset['top']) + 'px';
      else
      {
         menuobj.style.top=showobj.Y - menu_offset['top'] + 'px';
         var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
            if(parseInt(menuobj.style.top) + menuobj.h > bb.clientHeight + bb.scrollTop)
               menuobj.style.top = showobj.Y - menuobj.h + 'px';
         }
    } else if(offset == 3) {
      menuobj.style.left = (document.body.clientWidth - menuobj.clientWidth) / 2 + document.body.scrollLeft - menu_offset['left'] + 'px';
      menuobj.style.top = (document.body.clientHeight - menuobj.clientHeight) / 2 + document.body.scrollTop - menu_offset['top'] + 'px';
    }
    if(menuobj.style.clip && !is_opera) {
      menuobj.style.clip = 'rect(auto, auto, auto, auto)';
    }//alert(menuobj.style.left+" "+menuobj.style.top)
  }
}

function hideMenu(layer) {
  if(isUndefined(layer)) layer = 0;
  if(jsmenu['active'][layer]) {
    try {
      $(jsmenu['active'][layer].ctrlkey).className = ctrlobjclassName;
    } catch(e) {}
    clearTimeout(jsmenu['timer'][jsmenu['active'][layer].ctrlkey]);
    jsmenu['active'][layer].style.display = 'none';
    if(is_ie && is_ie < 7 && jsmenu['iframe'][layer]) {
      jsmenu['iframe'][layer].style.display = 'none';
    }
    jsmenu['active'][layer] = null;
  }
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

function ebygum(eventobj) {
  if(!eventobj || is_ie) {
    window.event.cancelBubble = true;
    return window.event;
  } else {
    if(eventobj.target.type == 'submit') {
      eventobj.target.form.submit();
    }
    eventobj.stopPropagation();
    return eventobj;
  }
}

function doane(event) {
  e = event ? event : window.event;
  if(is_ie) {
    e.returnValue = false;
    e.cancelBubble = true;
  } else if(e) {
    e.stopPropagation();
    e.preventDefault();
  }
}

function in_array(needle, haystack) {
  if(typeof needle == 'string' || typeof needle == 'number') {
    for(var i in haystack) {
      if(haystack[i] == needle) {
          return true;
      }
    }
  }
  return false;
}
function getEvent() //同时兼容ie和ff的写法
{
    if(document.all)  return window.event;
    func=getEvent.caller;
    while(func!=null){
        var arg0=func.arguments[0];
        if(arg0)
        {
          if((arg0.constructor==Event || arg0.constructor ==MouseEvent) || (typeof(arg0)=="object" && arg0.preventDefault && arg0.stopPropagation))
          {
          return arg0;
          }
        }
        func=func.caller;
    }
    return null;
}
function getEventSrc()
{
   var event=getEvent();
   return event.srcElement ? event.srcElement : event.target;
}
function getMousePos()
{
   var mouseX = 0;
   var mouseY = 0;
   var e = getEvent();//alert(e.clientX)
   var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;

   mouseX = e.clientX + bb.scrollLeft;
   mouseY = e.clientY + bb.scrollTop;

   return {left:mouseX,top:mouseY};
}

function getMenuOffset(id)
{
   var dialogLeft=dialogTop=0;
   var el = document.getElementById(id);
   while(el)
   {
      el=el.parentElement ? el.parentElement : el.parentNode;
      if(el && el.className=="ModalDialog")
      {
         dialogLeft=parseInt(el.style.left);
         dialogTop=parseInt(el.style.top);
         break;
      }
   }
   return {left:dialogLeft, top:dialogTop};
}

function show_attach_op(id, a)
{
   var pos = fetchOffset(a);
   var el = document.getElementById(id);
   el.style.display="block";
   el.style.width=a.offsetWidth;
   el.style.left=pos['left'];
   el.style.top=pos['top']+a.offsetHeight-3;
   el.style.filter = "progid:DXImageTransform.Microsoft.shadow(direction=135,color=#CCCCCC,strength=4)";
   
   var bb = (document.compatMode && document.compatMode!="BackCompat") ? document.documentElement : document.body;
   if(parseInt(el.style.top) + el.offsetHeight > bb.offsetHeight + bb.scrollTop)
      el.style.top = pos['top'] - el.offsetHeight+5;
}
function hide_attach_op(id)
{
   var el = document.getElementById(id);
   el.style.display="none";
}
function SaveFile(ATTACHMENT_ID,ATTACHMENT_NAME)
{
  URL="/module/save_file?ATTACHMENT_ID="+ATTACHMENT_ID+"&ATTACHMENT_NAME="+ATTACHMENT_NAME+"&A=1";
  loc_x=screen.availWidth/2-200;
  loc_y=screen.availHeight/2-90;
  window.open(URL,null,"height=180,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}

function sel_attach(div_id,dir_field,name_field,disk_id,filter)
{
	//alert("div_id:"+div_id +"  dir_field:"+dir_field +"  name_field:"+name_field +"  disk_id:"+disk_id +"  filter:"+filter);
   if(!filter)
      filter="";
   //var URL="/module/sel_file?EXT_FILTER=" + filter + "&MULTI_SELECT=1&DIV_ID=" + div_id + "&DIR_FIELD=" + dir_field + "&NAME_FIELD=" + name_field + "&TYPE_FIELD=" + disk_id;
   var URL = contextPath + "/core/funcs/selfile/index.jsp?EXT_FILTER=" + filter + "&MULTI_SELECT=1&DIV_ID=" + div_id + "&DIR_FIELD=" + dir_field + "&NAME_FIELD=" + name_field + "&TYPE_FIELD=" + disk_id;
   //alert(URL);
   $(div_id).show();
   window.open(URL,null,"height=300,width=500,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,top=200,left=300,resizable=yes");
}

function upload_limit_check(file_name)
{
   if(upload_limit==0 || file_name=="")
      return true;
   
   file_name=file_name.substring(file_name.lastIndexOf("\\")+1).toLowerCase();
   var ext_name="";
   if(file_name.lastIndexOf(".")>=0)
      ext_name=file_name.substring(file_name.lastIndexOf(".")+1,file_name.length);
   if(ext_name=="" || ext_name==file_name)
      ext_name="*";
   
   var bFound=limit_type.indexOf(ext_name+",")==0 || limit_type.indexOf(","+ext_name+",")>0;
   if(upload_limit==1 && !bFound || upload_limit==2 && bFound)
      return true;
   
   if(ext_name=="*")
      alert("不允许上传无后缀名的文件");
   else
      alert("不允许上传后缀名为 "+ext_name+" 的文件");
   return false;
}

function GetParentEl(el, tagName)
{
   el=el.parentElement ? el.parentElement : el.parentNode;
   if(!el)
     return null;

   if(el.tagName.toLowerCase()==tagName)
      return el;
   else
      return GetParentEl(el, tagName);
}
function CreateFileEl(id,onchange)
{//alert(id  +"  " +onchange);
  if(isUndefined(onchange))
    onchange = AddFile;
  
   var attach=document.createElement("input");
   attach.type="file";
   attach.className="addfile";
   attach.name=id;
   attach.id=id;
   attach.size="1";
   attach.hideFocus="true";
   attach.onchange=onchange;
   return attach;
}
function AddFile(file_type)
{//alert(file_type);
   var file=getEventSrc();
   var prefix=file.id.substring(0,file.id.lastIndexOf("_"));
   if(!prefix) {
      prefix="ATTACHMENT";
   }
   if (prefix != "ATTACHMENT") {
     currFormFile = prefix + "_formFile";
   }
   
   var formFile = null;
   if (currFormFile) {
     formFile = $(currFormFile);
   }else {
     formFile = $("formFile");
   }
   var attach_div = document.getElementById(prefix+"_div");
   var form_el = formFile ? formFile : GetParentEl(file,"form");//alert(addFileLink.name)
   var addFileLink = GetParentEl(file,"a");
   if(!attach_div || !form_el || !addFileLink)
   {
      alert("参数无效");
      return;
   }
   
   if(!upload_limit_check(file.value) || file_type == "image" && !upload_image_check(file.value))
   {
      var attach = CreateFileEl(file.id, file.onchange);
      addFileLink.removeChild(file);
      addFileLink.appendChild(attach);
      return;
   }
   
   
   var id=parseInt(file.id.substring(prefix.length+1));
   var el=form_el.children ? form_el.children : form_el.childNodes;
   for(var i=0; i<el.length;i++)
   {
      if(el[i].tagName && el[i].tagName.toLowerCase()=="input" && el[i].type.toLowerCase()=="file" && el[i].id!=file.id && el[i].value==file.value)
      {
         alert("该文件已经添加");
         addFileLink.removeChild(file);
         var attach = CreateFileEl(file.id,file.onchange);
         addFileLink.appendChild(attach);
         return;
      }
   }
   
   var attach_name = file.value.substring(file.value.lastIndexOf("\\")+1);
   attach_div.innerHTML+="<span id='"+prefix+"_span_"+id+"' title='"+file.value+"'><img src='" + imgPath + "/attach.png' align='absMiddle'>"+attach_name+"<img src='" + imgPath + "/remove.png' onclick='RemoveFile(this)' align='absMiddle' style='cursor:hand;'>;&nbsp;</span>";
   file.style.zIndex = "-1";
   form_el.appendChild(file);
   
   id++;
   var attach = CreateFileEl(prefix+'_'+id,file.onchange);
   addFileLink.appendChild(attach);
   document.getElementById(prefix+"_upload_div").style.display="";
   //alert(file_type);
/*   if(file_type=="image")
   {
     //alert("file:>>>"+file.value);
     //alert("file.replace:>>>"+file.value.replace(/\\/g,"/"));
      //InsertImage("file://"+file.value.replace(/\\/g,"/"));
   }*/
}

function upload_limit_check(file_name)
{
   if(upload_limit==0 || file_name=="")
      return true;
   
   file_name=file_name.substring(file_name.lastIndexOf("\\")+1).toLowerCase();
   var ext_name="";
   if(file_name.lastIndexOf(".")>=0)
      ext_name=file_name.substring(file_name.lastIndexOf(".")+1,file_name.length);
   if(ext_name=="" || ext_name==file_name)
      ext_name="*";
   
   var bFound=limit_type.indexOf(ext_name+",")==0 || limit_type.indexOf(","+ext_name+",")>0;
   if(upload_limit==1 && !bFound || upload_limit==2 && bFound)
      return true;
   
   if(ext_name=="*")
      alert("不允许上传无后缀名的文件");
   else
      alert("不允许上传后缀名为 "+ext_name+" 的文件");
   return false;
}

function upload_image_check(file_name)
{
   if(file_name=="")
   {
      alert("图片只能为gif,jpg,png,bmp格式");
      return false;
   }
   
   file_name=file_name.substring(file_name.lastIndexOf("\\")+1).toLowerCase();
   var ext_name="";
   if(file_name.lastIndexOf(".")>=0)
      ext_name=file_name.substring(file_name.lastIndexOf(".")+1,file_name.length);
   if(ext_name=="" || ext_name==file_name)
   {
      alert("图片只能为gif,jpg,png,bmp格式");
      return false;
   }
   
   var file_types = "gif,jpg,jpeg,png,bmp,";
   if(file_types.indexOf(ext_name+",")<0 && file_types.indexOf(","+ext_name+",")<=0)
   {
      alert("图片只能为gif,jpg,png,bmp格式");
      return false;
   }
   
   return true;
}

function removeAllFile() {
  var form_el = null;
  var pre = "ATTACHMENT";
  if (currFormFile && currFormFile != "formFile") {
    pre = currFormFile.substring(0, currFormFile.lastIndexOf("_"));
  }
  if (currFormFile) {
    form_el = $(currFormFile);
  }else {
    form_el = $("formFile");
  }
  currFormFile = null;
  if (!form_el) {
    return;
  }
  var el = form_el.children ? form_el.children : form_el.childNodes;
  for (var i = el.length - 1; i >= 0; i--)
  {
     if(el[i].tagName && el[i].tagName.toLowerCase()=="input" && el[i].type.toLowerCase()=="file")
     {
       var fileId = el[i].id;
       var file = $(fileId);
       var prefix = fileId.substring(0, fileId.lastIndexOf("_"));
       if(!prefix) {
          prefix = "ATTACHMENT";
       }       
       var spanId = fileId.replace(prefix + "_", prefix + "_span_");
       //alert(fileId + "\t" + prefix + "\t" + spanId);
       var fileSpan = $(spanId);
       
       var parentNode = fileSpan.parentElement || fileSpan.parentNode;
       if(fileSpan && parentNode) {
         parentNode.removeChild(fileSpan);
       }
       parentNode = file.parentElement || file.parentNode;
       if(file && parentNode) {
         parentNode.removeChild(file);
       }
     }
  }
  var butn = $(pre + "_upload_div");
  butn.style.display = "none";
  
}
function RemoveFile(img)
{
   var span = GetParentEl(img, "span");
   var file = document.getElementById(span.id.replace("_span_","_"));
   if(span && span.parentElement)
      span.parentElement.removeChild(span);
   
   if(file && file.parentElement)
      file.parentElement.removeChild(file);
}

function ShowAddFile(postfix,show_sel_attach)
{	//alert("postfix:"+postfix +"  show_sel_attach:"+show_sel_attach);
   if(isUndefined(postfix)) postfix="";
   document.write('<span id="ATTACHMENT'+postfix+'_div"></span><span id="ATTACHMENT'+postfix+'_upload_div" style="display:none;"></span><div id="SelFileDiv'+postfix+'"></div><a id="linkAddAttach" class="addfile" href="javascript:;">添加附件<input class="addfile" type="file" name="ATTACHMENT'+postfix+'_0" id="ATTACHMENT'+postfix+'_0" size="1" hideFocus="true" onchange="AddFile();" /></a>');
   if(show_sel_attach!='0')
      document.write('&nbsp;|&nbsp;<a href="#" onclick="sel_attach(\'SelFileDiv'+postfix+'\',\'ATTACH_DIR'+postfix+'\',\'ATTACH_NAME'+postfix+'\',\'DISK_ID'+postfix+'\');" class="selfile">从文件柜和网络硬盘选择附件</a><input type="hidden" value="" id="ATTACH_NAME'+postfix+'" name="ATTACH_NAME'+postfix+'"><input type="hidden" value="" id="ATTACH_DIR'+postfix+'" name="ATTACH_DIR'+postfix+'"><input type="hidden" id="DISK_ID'+postfix+'" value="" name="DISK_ID'+postfix+'">');
}
function ShowAddImage(postfix,attachPre)
{
  //alert("aaa");
  // if(isUndefined(postfix)) postfix="";
  // document.write('&nbsp;&nbsp;<a id="add_image" href="javascript:;" onclick="ShowImage(this.id, \''+postfix+'\')" onfocus="this.blur();">插入正文'+(is_ie ? '<span style="font-family:Webdings">6</span>' : '<img src="/images/menu_arrow_down.gif" align="absMiddle">')+'</a><div id="add_image_menu" class="attach_div"></div>');
  if(isUndefined(postfix)) postfix="";
  if(isUndefined(attachPre)) attachPre = "";
  document.write('&nbsp;&nbsp;<a id="add_image" class="addimage" href="javascript:;"  >插入图片<input class="addfile" type="file" name="ATTACHMENT'+postfix+'_1000" id="ATTACHMENT'+postfix+'_1000" size="1" hideFocus="true" onchange="AddFile(\'image\');"/></a>');
  var cntrl = $('add_image');
  cntrl.observe("mouseover", this.addImagMenu.bindAsEventListener(this, 'add_image',attachPre));
}
function addImagMenu(cntrl,cntrlId,attachPre){
  cntrl = $(cntrl);
  var attchName = $(attachPre + 'attachmentName');
  var attchId = $( attachPre + 'attachmentId');
  var moduel = $('moduel').value;
  var attachNames = attchName.value;
  var attachIds = attchId.value;
  var idArray = attachIds.split(",");
  var nameArray = attachNames.split("*");
  var names = new Array();
  var ids = new Array();
  for (var i = 0; i < nameArray.length; i++) {
    if(!nameArray){
      continue;
    }
    var index = nameArray[i].lastIndexOf(".");
    var extName = "";
    if (index >= 0) {
      extName = nameArray[i].substring(index + 1).toLowerCase();
    }
    if(["gif", "png","jpg","bmp"].contains(extName)){
      names.push(nameArray[i]);
      ids.push(idArray[i]);
    }
  }
  if(ids.length > 0){
    var imgMenuItems = getImageMenu(names,ids,moduel);
    showImagMenu(event,cntrl,imgMenuItems);
  }
 // var attch
}

function showImagMenu(){
  var event = arguments[0];
  var cntrl = arguments[1];
  var menuItems = arguments[2];
  var menu = new Menu({bindTo: cntrl.id , menuData:menuItems});
  menu.show(event);
}
function getImageMenu(names,ids,moduel){
  var imageMenusItem = new Array(); 
  for(var i = 0 ; i < names.length ; i ++){
    var item = getImageMenuItem(names[i],ids[i],moduel);
    imageMenusItem.push(item);
  }
  return imageMenusItem;
}
function getImageMenuItem(name,id,moduel){
  var extData = {name:name,id:id,moduel:moduel};
  var Item = { name:"<div style=\"padding-top:5px;margin-left:10px\">" + name + "<div>",action:insertImg,extData:extData};
  return Item;
}
function insertImg(dom,cntrlId,extData){
  var src = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=" + encodeURIComponent(extData.name) + "&amp;attachmentId=" +extData.id +"&amp;module="+extData.moduel+"&amp;directView=1" ; 
  try{
    InsertImage(src);
  }catch(e){
    alert("");
  }
}
function ShowImage(id,postfix)
{
  //alert("id>>"+id +"  postfix>>>"+postfix);
   var prefix = "ATTACHMENT"+postfix+"_";
   $(id+'_menu').innerHTML="";
   var inputs=document.getElementsByTagName("INPUT");
   for(var i=0;i<inputs.length; i++)
   {
      var input = inputs[i];
      if(isUndefined(input.type) || input.type.toLowerCase() != "file" || isUndefined(input.name) || input.name.substr(0, prefix.length) != prefix)
         continue;
      
      var extName = input.value.substr(input.value.lastIndexOf(".")+1).toLowerCase();
      if(imageType.indexOf(extName+",") != 0 && imageType.indexOf(","+extName+",") < 0)
         continue;
      
      var a=/\\/g;
      var fileName = input.value.substr(input.value.lastIndexOf("\\")+1);
      $(id+'_menu').innerHTML += '<a href="javascript:InsertImage(\''+('file://'+(input.value.replace(a,"/")))+'\');" title="插入正文 '+input.value+'">'+fileName+'</a>';
   }
   
   var links=document.getElementsByTagName("A");
   for(var i=0;i<links.length; i++)
   {
      var link = links[i];
      if(isUndefined(link.id) || link.id.substr(0, 18) != "insert_image_link_")
         continue;
      
      $(id+'_menu').innerHTML += '<a href="'+link.href+'" title="'+link.title+'">'+link.title+'</a>';
   }
   if($(id+'_menu').innerHTML=="")
      $(id+'_menu').innerHTML="没有添加图片文件";
   showMenu(id,1);
}