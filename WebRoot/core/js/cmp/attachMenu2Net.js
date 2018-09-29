var YHJsAttachMenu2Net = Class.create();
YHJsAttachMenu2Net.prototype = {
    
 initialize: function(cfgs) {
  this.config(cfgs);    
 },
 /**
   * 附件菜单定义
  */
  docMenus:{
    downFile: { name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'0'},
    dump : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'},
    read : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">阅读<div>',action:readOffice,extData:'2'},
    readNoPrint : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">阅读<div>',action:readOfficeNoPrint,extData:'2'},
    readText : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">编辑<div>',action:editText,extData:'2'},
    edit : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">编辑<div>',action:editOffice,extData:'3'},
    setSign : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">设置标引<div>',action:setSign,extData:'4'},
    deleteFile : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:deleteAttach,extData:'5'},
    play : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'6'},
    readpdf : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">阅读<div>',action:playVideoAttach,extData:'7'}
  },
  extNames:{
    office:["doc", "xls", ,"ppt", "docx", "xlsx", "pptx"],
    img:["gif", "png", "jpg", "bmp"],
    music:["mp3","mht"],
    text:["txt","text"],
    pdf: ["pdf"],
    video:["rm","wav","wmv","avi", "mpg","mpeg","mp4","swf"]
  },
  /**
   * 类的配置
   */
  config : function(cfgs) {
    this.container = cfgs.container ? $(cfgs.container) : $(document.body);
    this.deleteAction = cfgs.deleteAction;
    this.filePaths = cfgs.filePaths;
    this.fileNames = cfgs.fileNames;
    this.readOnly = cfgs.readOnly;
    this.print = cfgs.print ? cfgs.print : "";
    this.canDown = cfgs.canDown;
    this.prec = cfgs.prec;
    this.opFlag = cfgs.opFlag ? cfgs.opFlag : "";
    this.code = cfgs.code ? cfgs.code : "";
    this.seqId = cfgs.seqId ? cfgs.seqId : "";
    this.selfdefMenu = cfgs.selfdefMenu ? cfgs.selfdefMenu : "";
  },
  show: function(){
    if(this.container){
      var attachHTML = this.attachHtml(this.filePaths,this.fileNames);
      $(this.container).innerHTML = attachHTML;
      this.bindAttachMenu(this.filePaths,this.fileNames);
    }
  },
  /**
   * 显示浮动菜单
   */
  showMenu: function() {
    var event = arguments[0];
    var cntrl = arguments[1];
    var menuItems = arguments[2];
    var attachCntrl = arguments[3];
    if (!attachCntrl && attachCntrl !== false) {
      attachCntrl = true;
    }
    if(menuItems.length <= 0){
      return;
    }
    var menu = new Menu({bindTo: cntrl.id , menuData:menuItems});
    menu.show(event);
  },
  /**
   * 绑定附件的浮动菜单
   */
  bindAttachMenu: function(filePaths, fileName) {
    if (!filePaths) {
      return;
    }
    var rtStr = "";
    var nameArray = fileName.split("*");
    var idArray = filePaths.split(",");
    for (var i = 0; i < nameArray.length; i++) {
      if(!nameArray){
        continue;
      }
      var name = nameArray[i];
      var id = idArray[i];
      var prcsName = "";
      if(this.prec){
        prcsName = i + "_" +  this.prec;
      }else{
        prcsName = i;
      }
      var cntrlId = "attachLink_" + this.code + prcsName;
      
      var cntrl = $(cntrlId);
      var floatMenu = this.getDocOptMenu(name, this.readOnly,this.canDown,this.selfdefMenu);
      if (cntrl && floatMenu) {
        cntrl.observe("mouseover", this.showMenu.bindAsEventListener(this, cntrl, floatMenu, true));
      }
    }
  },/**
   * 绑定附件的浮动菜单
   */
  getImg: function(extName) {
    var imgArray = ["ai", "avi","bmp","cs", "dll", "doc", "docx","exe", "fla", "gif", "htm", "html"
                    , "jpg","js","mdb", "mp3", "pdf", "png","ppt", "pptx", "rar", "rdp", "swf"
                    , "swt","txt","vsd", "xls", "xlsx", "xml","zip","dot" ,"mht","text"];
    extName = extName.toLowerCase();
     if(imgArray.contains(extName)){
       return extName;
     }else{
       return "default";
     }
                         
  },
  /**
   * 取得附件的HTML
   */
  attachHtml: function(filePaths, fileName) {
    if (!filePaths) {
      return "";
    }
    var rtStr = "";
    var nameArray = fileName.split("*");
    var idArray = filePaths.split(",");
    for (var i = 0; i < nameArray.length; i++) {
      var name = trim(nameArray[i]);
      if(!name){
        continue;
      }
      var id = trim(idArray[i]);
      var prcsName = "";
      if(this.prec){
        prcsName = i + "_" +  this.prec;
      }else{
        prcsName = i;
      }
      var index = name.lastIndexOf(".");
      var extName = "";
      if (index >= 0) {
        extName = name.substring(index + 1).toLowerCase();
      }
      var imgName = this.getImg(extName);
      
      rtStr += "<div>" 
        + "<input type=\"hidden\" value=\"" + id + "\" id=\"attachLink_" + this.code   + prcsName+ "_path\">"
        + "<input type=\"hidden\" value=\"" + name + "\" id=\"attachLink_" + this.code   + prcsName + "_fileName\">";
      if(["doc", "xls", ,"ppt", "docx", "xlsx", "pptx"].contains(extName)){
        rtStr += "<input type=\"hidden\" value=\"" +  this.print  + "\" id=\"attachLink_" + this.code  + prcsName + "_print\">";
        rtStr += "<input type=\"hidden\" value=\"" +  this.opFlag  + "\" id=\"attachLink_" + this.code  + prcsName + "_opFlag\">";
      }
      if(this.seqId){
        rtStr += "<input type=\"hidden\" value=\"" +  this.seqId  + "\" id=\"attachLink_" + this.code  + prcsName + "_seqId\">";
      }
      rtStr += "<img src=\"" + imgPath + "/fileExt/" + imgName + ".gif\" style=\"width:12px;height:12px\" title=\"" + name + "\">&nbsp;"
        + "<a id=\"attachLink_" + this.code + prcsName + "\" attachName=\"" + name+ "\" attachId=\"" + id + "\" href=\"#\">" + name + "</a>" 
        + "</div>";
    }
    return rtStr;
  },
  /**
   * 取得文件操作类型
   */
  getDocOptType: function(name, readOnly,canDown,selfdefMenu) {
    if (!name) {
      return null;
    }
    var extNames = this.extNames;
    //alert(name);
    var index = name.lastIndexOf(".");
    var extName = "";
    if (index >= 0) {
      extName = name.substring(index + 1).toLowerCase();
    }
    if (extNames["office"].contains(extName)) {
      if(selfdefMenu.office){
        return selfdefMenu.office;
      }
      if (readOnly) {
        if(canDown){
          return ["downFile","read"];
        }else{
         return ["read"];
        }
      }else {
        if(canDown){
          return ["downFile","read","edit"];
        }else{
          return ["read","edit"];
        }
      }
    }else if (extNames["img"].contains(extName)) {
      if(selfdefMenu.img){
        return selfdefMenu.img;
      }
      if (readOnly) {
        if(canDown){
          return ["downFile","play"];
        }else{
         return ["play"];
        }
      }else {
        if(canDown){
          return ["downFile","play"];
        }else{
         return ["play"];
        }
      }
    }else if (extNames["music"].contains(extName)) {
      if(selfdefMenu.music){
        return selfdefMenu.music;
      }
      if (readOnly) {
        if(canDown){
          return ["downFile","play"];
        }else{
         return ["play"];
        }
      }else {
        if(canDown){
          return ["downFile","play"];
        }else{
         return ["play"];
        }
      }
    }else if (extNames["video"].contains(extName)) {
      if(selfdefMenu.video){
        return selfdefMenu.video;
      }
      if (readOnly) {
        if(canDown){
          return ["downFile","play"];
        }else{
         return ["play"];
        }
      }else {
        if(canDown){
          return ["downFile","play"];
        }else{
         return ["play"];
        }
      }
    }else if (extNames["text"].contains(extName)) {
      if(selfdefMenu.text){
        return selfdefMenu.text;
      }
      if (readOnly) {
        if(canDown){
          return ["downFile","readText"];
        }else{
         return ["readText"];
        }
      }else {
        if(canDown){
          return ["downFile","readText"];
        }else{
         return ["readText"];
        }
      }
    } else if (extNames["pdf"].contains(extName)) {
      if(selfdefMenu.pdf){
        return selfdefMenu.pdf;
      }
      if (readOnly) {
        if(canDown){
          return ["downFile","readpdf"];
        }else{
          return ["readpdf"];
        }
      }else {
        if(canDown){
          return ["downFile","readpdf"];
        }else{
          return ["readpdf"];
        }
      }
    }else {
      if(selfdefMenu.others){
        return selfdefMenu.others;
      }
      if (readOnly) {
        if(canDown){
          return ["downFile"];
        }else{
         return null;
        }
      }else {
        if(canDown){
          return ["downFile"];
        }else{
         return null;
        }
      }
    }
  },
  /**
   * 取得文件操作菜单
   */
  getDocOptMenu: function(name, readOnly,canDown,selfdefMenu) {
    var docMenusItem = new Array();
    var docOptType = this.getDocOptType(name, readOnly,canDown,selfdefMenu);
    if (!docOptType) {
      return null;
    }
    for(var i = 0 ; i < docOptType.length ; i ++){
      var item = this.docMenus[docOptType[i]];
      docMenusItem.push(item);
    }
    return docMenusItem;
  }
}
function set_status() {
  alert("set_status");
}
/**
 * 下载附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function downLoadAttach(dom,cntrlId,extData){
  cntrlId = $(cntrlId).id;
  var cntrlattpath = $(cntrlId + "_path");
  var cntrlattfileName = $(cntrlId + "_fileName");
  //cntrl =  document.getElementById(cntrlId);
  var path = cntrlattpath.value;
  var fileName = cntrlattfileName.value;
  downLoadFile(fileName,path);
}

/**
 * 阅读pdf附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function readPdfAttach(dom,cntrlId,extData){
  cntrlId = $(cntrlId).id;
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  //cntrl =  document.getElementById(cntrlId);
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var module = cntrlMoudel.value;
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&module=" + module;
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/readPdf.act?" + param;
  window.open(url);
}

/**
 * 转存附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function archivedAttach(dom,cntrlId,extData){
  cntrlId = $(cntrlId).id;
  var cntrlattpath = $(cntrlId + "_path");
  var cntrlattfileName = $(cntrlId + "_fileName");
  //cntrl =  document.getElementById(cntrlId);
  var path = cntrlattpath.value;
  var fileName = cntrlattfileName.value;
  archived(fileName,path);
}
/**
 * 播放附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function playVideoAttach(dom,cntrlId,extData){
  cntrlId = $(cntrlId).id;
  var cntrlattpath = $(cntrlId + "_path");
  var cntrlattfileName = $(cntrlId + "_fileName");
  //cntrl =  document.getElementById(cntrlId);
  var path = cntrlattpath.value;
  var fileName = cntrlattfileName.value;
  playVideo(fileName,path);
}
/**
 * 删除附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function deleteAttach(dom,cntrlId,extData){
  cntrlId = $(cntrlId).id;
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  var cntrlSeqId = $(cntrlId + "_seqId");
  var attachId = cntrlattId.value;
  var seqId = "";
  if(cntrlSeqId){
   seqId = cntrlSeqId.value;
  }
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  var cntrl = $(cntrlId);
  var params = cntrlId.split("_");
  var attrchIndex = params[1];
  var pra = cntrl.up();
  var isDel =   deleteAttachBackHand(attachName,attachId,seqId);
  if(isDel){
    deleteFile(attachName,attachId,moudel);
    pra.remove();
  }
}

/**
 * 编辑offic文档
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function editOffice(dom,cntrlId,extData){
  var cntrl = $(cntrlId);
  cntrlId = $(cntrlId).id;
  var cntrlattpath = $(cntrlId + "_path");
  var cntrlattfileName = $(cntrlId + "_fileName");
  var cntrlopFlag = $(cntrlId + "_opFlag");
  var cntrlprint = $(cntrlId + "_print");
  var print = cntrlprint.value;
  var path = cntrlattpath.value;
  var fileName = cntrlattfileName.value;
  var opFlag = cntrlopFlag.value;
  if(!opFlag){
    opFlag = 4;
  }
  office(fileName,path,opFlag,'',print);
}
/**
 * 设置标引
 */
function setSign(dom,cntrlId,extData) {
 
}
/**
 * 
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function playVideo(fileName,filePath){
  var mediaType = isMedia(fileName);
  var video = isVideo(fileName);
  var param = "fileName=" + encodeURIComponent(fileName) + "&path=" + encodeURIComponent(filePath) + "&mediaType=" + mediaType + "&video=" + video;
  var url = contextPath + "/core/module/mediaplayer/netDiskIndex.jsp?" + param;
  //url = encodeURI(url);
  openWindow(url,'在线播放器',900,600);
}
/**
 * 
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function archived(attachName,attachId,moudle){
  var URL = contextPath + "/core/funcs/savefile/index.jsp?attachId=" + attachId + "&attachName=" + encodeURIComponent(attachName) +"&module=" + moudle;
  var loc_x = screen.availWidth/2-200;
  var loc_y = screen.availHeight/2-90;
  window.open(URL,null,"height=180,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}
 /**
  * 只读offic文档
  * @param dom
  * @param cntrlId
  * @param extData
  * @return
  */
 function readOffice(dom,cntrlId,extData){
   var cntrl = $(cntrlId);
   cntrlId = $(cntrlId).id;
   var cntrlattpath = $(cntrlId + "_path");
   var cntrlattfileName = $(cntrlId + "_fileName");
   var cntrlprint = $(cntrlId + "_print");
   var cntrlopFlag = $(cntrlId + "_opFlag");
   var path = cntrlattpath.value;
   var fileName = cntrlattfileName.value;
   var print = cntrlprint.value;
   var opFlag = cntrlopFlag.value;
   if(!opFlag){
     opFlag = 7;
   }
   office(fileName,path,opFlag,'',print);
 }
 /**
  * 只读offic文档
  * @param dom
  * @param cntrlId
  * @param extData
  * @return
  */
 function readOfficeNoPrint(dom,cntrlId,extData){
   var cntrl = $(cntrlId);
   cntrlId = $(cntrlId).id;
   var cntrlattpath = $(cntrlId + "_path");
   var cntrlattfileName = $(cntrlId + "_fileName");
   //var cntrlMoudel = $(cntrlId + "_moudel");
   var cntrlprint = $(cntrlId + "_print");
   var cntrlopFlag = $(cntrlId + "_opFlag");
   var path = cntrlattpath.value;
   var fileName = cntrlattfileName.value;
   //var moudel = cntrlMoudel.value;
   var print = cntrlprint.value;
   var opFlag = cntrlopFlag.value;
   office(fileName,path,5,'',print);
 }
/**
 * 删除（附件）文件
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function deleteFile(fileName,filePath){
  var param = "fileName=" + encodeURIComponent(fileName) + "&path=" + encodeURIComponent(filePath );
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/deleteFile.act?" + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert("附件删除成功！");
  }else{
    alert(rtJson.rtMsrg);
  }
}
function editText(dom,cntrlId,extData){ 
  var cntrl = $(cntrlId); 
  cntrlId = $(cntrlId).id; 
  var cntrlattpath = $(cntrlId + "_path"); 
  var cntrlattfileName = $(cntrlId + "_fileName"); 
  var path = cntrlattpath.value; 
  var fileName = cntrlattfileName.value; 
  doEditText(fileName,path); 
} 

function doEditText(fileName,filePath) { 
  var url = contextPath + "/core/funcs/netdisk/editText.jsp?fileName=" + encodeURIComponent(fileName) + "&filePath=" + encodeURIComponent(filePath); 
  openWindow(url,'在线编辑器',900,600);
   //location.href = url; 
}
/**
 * 下载附件
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function downLoadFile(fileName,filePath){
  //var param = encodeURI("fileName=" + fileName + "&path=" + filePath );
  var param = "fileName=" + encodeURIComponent(fileName) + "&path=" + encodeURIComponent(filePath);
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/downFileByLocal2.act?" + param;
  location = url;
}
/**
 * NTKOword操作
 * @param attachmentName 附件名称
 * @param attachmentId  附件ID
 * @param moudle 附件模块名称
 * @param op  操作类型
 * @param signKey 验证
 * @param print 打印类型
 * @return
 */
function office(fileName,filePath,op,signKey,print){
  //print = print ? "" : "1";
  var param = "fileName=" + encodeURIComponent(fileName) 
     + "&path=" + encodeURIComponent(filePath)
     + "&op=" + op 
     + "&signKey=" + signKey 
     + "&print=" + print ; 
  var url = contextPath + "/core/funcs/office/netDiskNtko/index.jsp?" + param;
  //url = encodeURI(url);
  openWindow(url,'在线编辑',900,600);
}

function attachMenuUtil(container,deleteAction,fileNames,filePaths,readOnly,prec , code,isCanPrint,canDown,opFlag,seqId){
  var cfgs = {container:container
      ,deleteAction:deleteAction
      ,filePaths:filePaths
      ,fileNames:fileNames
      ,prec:prec
      ,readOnly:readOnly
      ,code:code
      ,print:isCanPrint
      ,canDown:canDown
      ,opFlag:opFlag
      ,seqId:seqId
  };
  var attachmenu = new YHJsAttachMenu2Net(cfgs);
  attachmenu.show();
}
function attachMenuSelfUtil(container,fileNames,filePaths,prec , code,seqId,selfdefMenu){
  var cfgs = {container:container
        ,filePaths:filePaths
        ,fileNames:fileNames
        ,prec:prec
        ,code:code
        ,seqId:seqId
        ,selfdefMenu:selfdefMenu
    };
  var attachmenu = new YHJsAttachMenu2Net(cfgs);
  attachmenu.show();
}
function isMedia(fileName){
   var MEDIA_REAL_TYPE = ["rm", "rmvb","ram","ra", "mpa", "mpv", "mps","m2v", "m1v", "mpe", "mov", "smi"];
   var MEDIA_MS_TYPE = ["wmv", "asf", "mp3", "mpg", "mpeg", "mp4", "avi", "wmv", "wma", "wav", "dat"];
   var MEDIA_FLASH_TYPE = ["flv", "fla"];
   var DIRECT_VIEW_TYPE = ["jpg", "jpeg", "bmp", "gif", "png", "xml", "xhtml", "html", "htm", "mid", "mht", "pdf", "swf"];
   var index = fileName.lastIndexOf(".");
   var extName = "";
 
   if (index >= 0) {
     extName = fileName.substring(index + 1).toLowerCase();
   }
   if(MEDIA_REAL_TYPE.contains(extName))
      return 1;
   if(MEDIA_MS_TYPE.contains(extName))
      return 2;
   if(MEDIA_FLASH_TYPE.contains(extName))
      return 4;
   if(DIRECT_VIEW_TYPE.contains(extName))
      return 3;
   return 0;
}

function isVideo(fileName){
  var videoType = ["mp4", "mpg","mpeg","avi", "wmv", "asf", "dat"];
  var index = fileName.lastIndexOf(".");
  var extName = "";
  if (index >= 0) {
    extName = fileName.substring(index + 1).toLowerCase();
  }
  if(videoType.contains(extName)){
    return 1;
  }else{
   return 0;
  }
}