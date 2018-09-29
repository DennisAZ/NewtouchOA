var YHJsAttachMenu = Class.create();
YHJsAttachMenu.prototype = {
    
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
    edit : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">编辑<div>',action:editOffice,extData:'3'},
    setSign : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">设置标引<div>',action:setSign,extData:'4'},
    deleteFile : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:deleteAttach,extData:'5'},
    play : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'6'},
    insertImg : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">插入到正文<div>',action:InserImgToCentent,extData:'7'},
    rename : { name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">重命名<div>',action:renameAttach,extData:'8'},
    readpdf: { name:'<div style="padding-top:5px;margin-left:10px">阅读<div>',action:readPdfAttach,extData:'9'}
  },
  extNames:{
    office:["doc", "xls", ,"ppt", "docx", "xlsx", "pptx"],
    img:["gif", "png","jpg","bmp"],
    music:["mp3","mht"],
    video:["rm","wav","wmv","avi", "mpg","mpeg","mp4","swf"],
    pdf: ["pdf"]
  },
  /**
   * 类的配置
   */
  config : function(cfgs) {
    this.container = cfgs.container ? $(cfgs.container) : $(document.body);
    this.moduleName = cfgs.moduleName;
    this.deleteAction = cfgs.deleteAction;
    this.attachNames = cfgs.attachNames;
    this.attachIds = cfgs.attachIds;
    this.readOnly = cfgs.readOnly;
    this.print = cfgs.print ? cfgs.print : "";
    this.canDown = cfgs.canDown;
    this.prec = cfgs.prec;
    this.opFlag = cfgs.opFlag ? cfgs.opFlag : "";
    this.code = cfgs.code ? cfgs.code : "";
    this.seqId = cfgs.seqId ? cfgs.seqId : "";
    this.selfdefMenu = cfgs.selfdefMenu ? cfgs.selfdefMenu : "";
    this.uploadNum = cfgs.uploadNum ? cfgs.uploadNum : "0";
    this.attachSize = cfgs.attachSize ? cfgs.attachSize : "";
    this.reciveDeptId = cfgs.reciveDeptId ? cfgs.reciveDeptId : "";
    this.reciveDeptDesc = cfgs.reciveDeptDesc ? cfgs.reciveDeptDesc : "";
    this.checked = cfgs.checked ? cfgs.checked : false;
  },
  show: function(){
    if(this.container){
      var attachHTML1 = this.attachHtml(this.attachNames,this.attachIds);
      if (attachHTML1) {
        $(this.container).innerHTML = attachHTML1;
        this.bindAttachMenu(this.attachNames,this.attachIds);
      }
    }
  },
  showTable: function(){
    if(this.container){
      var attachHTML1 = this.attachHtmlArray(this.attachNames,this.attachIds,this.uploadNum);
      if (attachHTML1.length) {
        
        var td1 = $C('td');
        td1.innerHTML = attachHTML1;
        
        var td2 = $C('td');
        td2.align = 'right';
        td2.innerHTML = (this.attachSize/1024).toFixed(0) + 'K'
                      + '<input type="hidden" id="attach_Size_'+this.uploadNum+'" name="attach_Size_'+this.uploadNum+'" value="'+this.attachSize+'">';
        
        var td3 = $C('td');
        td3.align = 'right';
        var tempDeptId = '0';
        var tempDept = '已选网络接收单位';
        if(this.reciveDeptDesc){
          tempDept = this.reciveDeptDesc;
          tempDeptId = this.reciveDeptId
        }
        td3.innerHTML = '<input type="hidden" id="reciveDept_'+this.uploadNum+'_Attach" name="reciveDept_'+this.uploadNum+'_Attach" value="'+tempDeptId+'">'
                      + '<textarea id="reciveDept_'+this.uploadNum+'_AttachDesc" name="reciveDept_'+this.uploadNum+'_AttachDesc" class="BigStatic" style="width: 95%;height: 100%;" onclick="selectOutDept3([\'reciveDept_'+this.uploadNum+'_Attach\', \'reciveDept_'+this.uploadNum+'_AttachDesc\'])" readonly>'+tempDept+'</textarea>';
        
        var td4 = $C('td');
        td4.align = 'center';
        td4.innerHTML = '<a class="progressCancel1" href="javascript: deleteAttachDel('+this.uploadNum+')"></a>';
        
        var tr = $C('tr');
        tr.id = "attachment_tr_" + this.uploadNum;
        tr.name = "attachment_tr_" + this.uploadNum;
        
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        $(this.container).appendChild(tr);
        this.bindAttachMenuTable(this.attachNames,this.attachIds,this.uploadNum);
      }
    }
  },
  showTableDetail: function(){
    if(this.container){
      var attachHTML1 = this.attachHtmlArray(this.attachNames,this.attachIds,this.uploadNum);
      if (attachHTML1.length) {
        
        var td1 = $C('td');
        td1.innerHTML = attachHTML1;
        
        var td2 = $C('td');
        td2.align = 'right';
        td2.innerHTML = (this.attachSize/1024).toFixed(0) + 'K';
        
        var td3 = $C('td');
        td3.align = 'right';
        td3.innerHTML = '<input type="hidden" id="reciveDept_'+this.uploadNum+'_Attach" name="reciveDept_'+this.uploadNum+'_Attach" value="0">'
                      + '<textarea id="reciveDept_'+this.uploadNum+'_AttachDesc" name="reciveDept_'+this.uploadNum+'_AttachDesc" class="BigStatic" style="width: 95%;height: 100%;" readonly>'+this.reciveDeptDesc+'</textarea>';
        
        var td4 = $C('td');
        td4.align = 'center';
        var temp = '';
        if(this.checked){
          temp = '√';
        }
        td4.innerHTML = temp;
        //td4.innerHTML = '<input type="checkbox" id="attachDocBox_'+this.uploadNum+'" name="attachDocBox" '+temp+'>';
        
        
        var tr = $C('tr');
        tr.id = "attachment_tr_" + this.uploadNum;
        tr.name = "attachment_tr_" + this.uploadNum;
        
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        $(this.container).appendChild(tr);
        if(!this.checked){
          this.bindAttachMenuTable(this.attachNames,this.attachIds,this.uploadNum);
        }
      }
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
   // alert(menuItems);
    var menu = new Menu({bindTo: cntrl.id , menuData:menuItems});
    menu.show(event);
  },
  /**
   * 绑定附件的浮动菜单

   */
  bindAttachMenuTable: function(attachNames, attachIds, uploadNum) {
    if (!attachNames) {
      return;
    }
    var rtStr = "";
    var nameArray = attachNames.split("*");
    var idArray = attachIds.split(",");
    for (var i = 0; i < nameArray.length; i++) {
      if(!nameArray){
        continue;
      }
      var name = nameArray[i];
      var id = idArray[i];
      var prcsName = "";
      if(this.prec){
        prcsName = uploadNum + "_" +  this.prec;
      }else{
        prcsName = uploadNum;
      }
      var cntrlId = "attachLink_" + this.code + prcsName;
      
      var cntrl = $(cntrlId);
      var floatMenu = this.getDocOptMenu(name, this.readOnly,this.canDown,this.selfdefMenu);
      if (cntrl && floatMenu) {
        cntrl.observe("mouseover", this.showMenu.bindAsEventListener(this, cntrl, floatMenu, true));
      }
    }
  },
  /**
   * 绑定附件的浮动菜单

   */
  bindAttachMenu: function(attachNames, attachIds) {
    if (!attachNames) {
      return;
    }
    var rtStr = "";
    var nameArray = attachNames.split("*");
    var idArray = attachIds.split(",");
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
  },
  /**
   * 绑定附件的浮动菜单

   */
  getImg: function(extName) {
    var imgArray = ["ai", "avi","bmp","cs", "dll", "doc", "docx","exe", "fla", "gif", "htm", "html"
                    , "jpg","js","mdb", "mp3", "pdf", "png","ppt", "pptx", "rar", "rdp", "swf"
                    , "swt","txt","vsd", "xls", "xlsx", "xml","zip","dot","mht","text", "pdf"];
    extName = extName.toLowerCase().trim();
     if(imgArray.contains(extName)){
       return extName;
     }else{
       return "default";
     }
                         
  },
  /**
   * 取得附件的HTML
   */
  attachHtml: function(attachNames, attachIds) {
    if (!attachNames) {
      return "";
    }
    var rtStr = "";
    var nameArray = attachNames.split("*");
    var idArray = attachIds.split(",");
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
        extName = name.substring(index + 1).toLowerCase().trim();
      }
      var imgName = this.getImg(extName);
      
      rtStr += "<div>" 
        + "<input type=\"hidden\" value=\"" + name + "\" id=\"attachLink_" + this.code   + prcsName+ "_name\">"
        + "<input type=\"hidden\" value=\"" + id + "\" id=\"attachLink_" + this.code   + prcsName + "_id\">"
        + "<input type=\"hidden\" value=\"" +  this.moduleName  + "\" id=\"attachLink_" + this.code  + prcsName + "_moudel\">";
      if(["doc", "xls", ,"ppt", "docx", "xlsx", "pptx"].contains(extName)){
        rtStr += "<input type=\"hidden\" value=\"" +  this.print  + "\" id=\"attachLink_" + this.code  + prcsName + "_print\">";
        rtStr += "<input type=\"hidden\" value=\"" +  this.opFlag  + "\" id=\"attachLink_" + this.code  + prcsName + "_opFlag\">";
      }
      if(this.seqId){
        rtStr += "<input type=\"hidden\" value=\"" +  this.seqId  + "\" id=\"attachLink_" + this.code  + prcsName + "_seqId\">";
      }
      rtStr += "<img src=\"" + imgPath + "/fileExt/" + imgName + ".gif\" style=\"width:12px;height:12px\" title=\"" + name + "\">&nbsp;"
        + "<a id=\"attachLink_" + this.code + prcsName + "\" attachName=\"" + name+ "\" attachId=\"" + id + "\" moudel=\"" + this.moduleName + "\" href=\"#\">" + name + "</a>" 
        + "</div>";
    }
    return rtStr;
  },
  /**
   * 取得附件的HTML
   */
  attachHtmlArray: function(attachNames, attachIds, uploadNum) {
    if (!attachNames) {
      return "";
    }
    var myArray = new Array();
    var nameArray = attachNames.split("*");
    var idArray = attachIds.split(",");
    for (var i = 0; i < nameArray.length; i++) {
      var rtStr = "";
      var name = trim(nameArray[i]);
      if(!name){
        continue;
      }
      var id = trim(idArray[i]);
      var prcsName = "";
      if(this.prec){
        prcsName = uploadNum + "_" +  this.prec;
      }else{
        prcsName = uploadNum;
      }
      var index = name.lastIndexOf(".");
      var extName = "";
      if (index >= 0) {
        extName = name.substring(index + 1).toLowerCase().trim();
      }
      var imgName = this.getImg(extName);
      
      rtStr += "<div>" 
        + "<input type=\"hidden\" value=\"" + name + "\" id=\"attachLink_" + this.code   + prcsName+ "_name\">"
        + "<input type=\"hidden\" value=\"" + id + "\" id=\"attachLink_" + this.code   + prcsName + "_id\">"
        + "<input type=\"hidden\" value=\"" +  this.moduleName  + "\" id=\"attachLink_" + this.code  + prcsName + "_moudel\">";
      if(["doc", "xls", ,"ppt", "docx", "xlsx", "pptx"].contains(extName)){
        rtStr += "<input type=\"hidden\" value=\"" +  this.print  + "\" id=\"attachLink_" + this.code  + prcsName + "_print\">";
        rtStr += "<input type=\"hidden\" value=\"" +  this.opFlag  + "\" id=\"attachLink_" + this.code  + prcsName + "_opFlag\">";
      }
      if(this.seqId){
        rtStr += "<input type=\"hidden\" value=\"" +  this.seqId  + "\" id=\"attachLink_" + this.code  + prcsName + "_seqId\">";
      }
      rtStr += "<img src=\"" + imgPath + "/fileExt/" + imgName + ".gif\" style=\"width:12px;height:12px\" title=\"" + name + "\">&nbsp;"
        + "<a id=\"attachLink_" + this.code + prcsName + "\" attachName=\"" + name+ "\" attachId=\"" + id + "\" moudel=\"" + this.moduleName + "\" href=\"#\">" + name + "</a>" 
        + "</div>";
      myArray.push(rtStr);
    }
    return myArray;
  },
  /**
   * 取得文件操作类型
   */
  getDocOptType: function(name, readOnly,canDown,selfdefMenu) {
    var extNames = this.extNames;
    if (!name) {
      return null;
    }
    //alert(name);
    var index = name.lastIndexOf(".");
    var extName = "";
    if (index >= 0) {
      extName = name.substring(index + 1).toLowerCase().trim();
    }
    if (extNames["office"].contains(extName)) {
      if(selfdefMenu.office){
        return selfdefMenu.office;
      }
      if (readOnly) {
        if(canDown == false){
          return ["readNoPrint"];
        }else{
         return ["downFile","dump","read"];
        }
      }else {
        if(canDown == false){
          return ["read","edit","deleteFile"];
        }else{
          return ["downFile","dump","read","edit","deleteFile"];
        }
      }
    }else if (extNames["img"].contains(extName)) {
      if(selfdefMenu.img){
        return selfdefMenu.img;
      }
      if (readOnly) {
        return ["downFile","dump","play"];
      }else {
        return ["downFile","dump","play","deleteFile","insertImg"];
      }
    }else if (extNames["music"].contains(extName)) {
      if(selfdefMenu.music){
        return selfdefMenu.music;
      }
      if (readOnly) {
        return ["downFile","dump","play"];
      }else {
        return ["downFile","dump","play","deleteFile"];
      }
    }else if (extNames["video"].contains(extName)) {
      if(selfdefMenu.video){
        return selfdefMenu.video;
      }
      if (readOnly) {
        return ["downFile","dump","play"];
      }else {
        return ["downFile","dump","play","deleteFile"];
      }
    } else if (extNames["pdf"].contains(extName)) {
      if(selfdefMenu.pdf){
        return selfdefMenu.pdf;
      }
      if (readOnly) {
        return ["readpdf", "downFile","dump"];
      }else {
        return ["readpdf", "downFile", "dump"];
      }
    }else {
      if(selfdefMenu.others){
        return selfdefMenu.others;
      }
      if (readOnly) {
        return ["downFile","dump"];
      }else {
        return ["downFile","dump","deleteFile"];
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
function renameAttach(dom,cntrlId,extData){
  cntrlId = $(cntrlId).id;
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  //cntrl =  document.getElementById(cntrlId);
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  renameFile(attachName,attachId,moudel);
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
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  //cntrl =  document.getElementById(cntrlId);
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  downLoadFile(attachName,attachId,moudel);
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
   // var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&module=" + module;
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&module=" + module + "&mediaType=3";
  var url =  contextPath + "/core/module/mediaplayer/index1.jsp?" + param;
  //var url = contextPath + "/yh/core/funcs/office/ntko/act/T9NtkoAct/readPdf.act?" + param;
  window.open(url);
}
/**
 * 
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function InserImgToCentent(dom,cntrlId,extData){
  cntrlId = $(cntrlId).id;
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  //cntrl =  document.getElementById(cntrlId);
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  insertImgc(attachName,attachId,moudel);
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
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  //cntrl =  document.getElementById(cntrlId);
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  archived(attachName,attachId,moudel);
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
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  //cntrl =  document.getElementById(cntrlId);
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  playVideo(attachName,attachId,moudel);
}
/**
 * 删除附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function deleteAttach(dom,cntrlId,extData,j){
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
  var isDel =   deleteAttachBackHand(attachName,attachId,seqId,j);
  if(isDel){
    deleteFile(attachName,attachId,moudel);
    pra.remove();
  }
  if (swfupload) {
    swfupload.delFile(attachName);
  }
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
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  var cntrlprint = $(cntrlId + "_print");
  var cntrlopFlag = $(cntrlId + "_opFlag");
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  var print = cntrlprint.value;
  var opFlag = cntrlopFlag.value;
  if(!opFlag){
    opFlag = 7;
  }
  office(attachName,attachId,moudel,opFlag,'',print);
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
   var cntrlattName = $(cntrlId + "_name");
   var cntrlattId = $(cntrlId + "_id");
   var cntrlMoudel = $(cntrlId + "_moudel");
   var cntrlprint = $(cntrlId + "_print");
   var cntrlopFlag = $(cntrlId + "_opFlag");
   var attachId = cntrlattId.value;
   var attachName = cntrlattName.value;
   var moudel = cntrlMoudel.value;
   var print = cntrlprint.value;
   var opFlag = cntrlopFlag.value;
   office(attachName,attachId,moudel,5,'',print);
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
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  var cntrlopFlag = $(cntrlId + "_opFlag");
  var cntrlprint = $(cntrlId + "_print");
  var print = cntrlprint.value;
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
  var opFlag = cntrlopFlag.value;
  if(!opFlag){
    opFlag = 4;
  }
  office(attachName,attachId,moudel,opFlag,'',print);
}
/**
 * 设置标引
 */
function setSign(dom,cntrlId,extData) {
  var cntrl = $(cntrlId);
  cntrlId = $(cntrlId).id;
  var cntrlattName = $(cntrlId + "_name");
  var cntrlattId = $(cntrlId + "_id");
  var cntrlMoudel = $(cntrlId + "_moudel");
  var attachId = cntrlattId.value;
  var attachName = cntrlattName.value;
  var moudel = cntrlMoudel.value;
}
/**
 * 
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function playVideo(attachName,attachId,moudle){
  var mediaType = isMedia(attachName);
  var video = isVideo(attachName);
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&moudle=" + moudle + "&mediaType=" + mediaType + "&video=" + video;
  var url = contextPath + "/core/module/mediaplayer/index.jsp?" + param;
  //url encodeURIComponentRI(url);
  openWindow(url,'在线播放器',900,600);
}
/**
 * 
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function insertImgc(attachName,attachId,moudle){
  var src = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?attachmentName=" + encodeURIComponent(attachName) + "&amp;attachmentId=" +attachId +"&amp;module="+moudle+"&amp;directView=1" ; 
  try{
    InsertImage(src);
  }catch(e){
    alert("");
  }
}
/**
 * 
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function archived(attachName,attachId,moudle){
  //alert("id>>>"+attachId +"  attachName>>"+attachName + "  moudle>>"+moudle);
  var URL = contextPath + "/core/funcs/savefile/index.jsp?attachId=" + attachId + "&attachName=" + encodeURIComponent(attachName) +"&module=" + moudle;
  var loc_x = screen.availWidth/2-200;
  var loc_y = screen.availHeight/2-90;
  //var kkcc=encodeURIComponent(URL);
  //alert(URL);
  window.open(URL,null,"height=200,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}

/**
 * 删除（附件）文件
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function deleteFile(attachName,attachId,moudle){
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&moudle=" + moudle;
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/deleteFile.act?" + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert("附件删除成功！");
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 下载附件
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function downLoadFile(attachName,attachId,moudle){
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&module=" + moudle;
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?" + param;
  var loc_x = screen.availWidth/2-200;
  var loc_y = screen.availHeight/2-90;
  window.location.href = url;
  //(url,null,"height=200,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
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
function office(attachmentName,attachmentId,moudle,op,signKey,print){
  //print = print ? "" : "1";
  var param = "attachmentName=" + encodeURIComponent(attachmentName)
     + "&attachmentId=" + attachmentId 
     + "&moudle=" + moudle 
     + "&op=" + op 
     + "&signKey=" + signKey 
     + "&print=" + print ; 
  var url = contextPath + "/core/funcs/office/ntko/indexNtko.jsp?" + param;
  //url = encodeURIComponent(url);
  openWindow(url,'在线编辑',900,600);
}

function renameFile(attachName,attachId,moudle){
  
}
/**
 * 
 * @param container  容器名称
 * @param moduleName 模块名称
 * @param deleteAction 
 * @param attachNames
 * @param attachIds
 * @param readOnly  office文档是否只读
 * @param prec   附件唯一前缀
 * @param code
 * @param isCanPrint  是否可打印

 * @param canDown   是否允许下载
 * @param opFlag  office文档操作标记（主要配合是否可打印处理，如果需要可打印这次参数值为5）

 * @param seqId  特定参数主要是处理删除时的参数

 * @return
 */
function attachMenuUtil(container,moduleName,deleteAction,attachNames,attachIds,readOnly,prec , code,isCanPrint,canDown,opFlag,seqId){
  var cfgs = {container:container
      ,moduleName:moduleName
      ,deleteAction:deleteAction
      ,attachIds:attachIds
      ,attachNames:attachNames
      ,prec:prec
      ,readOnly:readOnly
      ,code:code
      ,print:isCanPrint
      ,canDown:canDown
      ,opFlag:opFlag
      ,seqId:seqId
  };
  var attachmenu = new YHJsAttachMenu(cfgs);
  attachmenu.show();
}
/**
 * 自定义浮动菜单类型

 * @param container
 * @param moduleName
 * @param attachNames
 * @param attachIds
 * @param prec
 * @param code
 * @param seqId
 * @param selfdefMenu 形如  
 * selfdefMenu:{
    office:["downFile","dump","read","edit","setSign","deleteFile"],   //office 文档的浮动菜单项
    img:["downFile","dump","insertImg","play"],    //图片的浮动菜单项
    music:["downFile","dump","play"],  //MP3等的浮动菜单项

    video:["downFile","dump","play"], //视频等的浮动菜单项

    others:["downFile","dump"]//其他格式文档等的浮动菜单项

  }
  ["downFile","dump","read","edit","setSign","deleteFile","play"] 分别对应["下载","转存","阅读","编辑","设置标引","删除","播放"]
 * @return
 */
function attachMenuSelfUtil(container,moduleName,attachNames,attachIds,prec , code,seqId,selfdefMenu){
  var cfgs = {container:container
      ,moduleName:moduleName
      ,attachIds:attachIds
      ,attachNames:attachNames
      ,prec:prec
      ,code:code
      ,seqId:seqId
      ,selfdefMenu:selfdefMenu
  };
  var attachmenu = new YHJsAttachMenu(cfgs);
  attachmenu.show();
}

function attachMenuSelfUtilTable(container, moduleName, attachNames, attachIds, attachSize, prec, code, seqId, selfdefMenu, uploadNum, reciveDeptId, reciveDeptDesc, checked){
  var cfgs = {container:container
      ,moduleName:moduleName
      ,attachIds:attachIds
      ,attachNames:attachNames
      ,prec:prec
      ,code:code
      ,seqId:seqId
      ,selfdefMenu:selfdefMenu
      ,uploadNum:uploadNum
      ,attachSize:attachSize
      ,reciveDeptId:reciveDeptId
      ,reciveDeptDesc:reciveDeptDesc
      ,checked:checked
  };
  var attachmenu = new YHJsAttachMenu(cfgs);
  attachmenu.showTable();
}

function attachMenuSelfUtilTableDetail(container, moduleName, attachNames, attachIds, attachSize, prec, code, seqId, selfdefMenu, uploadNum, reciveDeptDesc, checked){
  var cfgs = {container:container
      ,moduleName:moduleName
      ,attachIds:attachIds
      ,attachNames:attachNames
      ,prec:prec
      ,code:code
      ,seqId:seqId
      ,selfdefMenu:selfdefMenu
      ,uploadNum:uploadNum
      ,attachSize:attachSize
      ,reciveDeptDesc:reciveDeptDesc
      ,checked:checked
  };
  var attachmenu = new YHJsAttachMenu(cfgs);
  attachmenu.showTableDetail();
}

function isMedia(fileName){
   var MEDIA_REAL_TYPE = ["rm", "rmvb","ram","ra", "mpa", "mpv", "mps","m2v", "m1v", "mpe", "mov", "smi"];
   var MEDIA_MS_TYPE = ["wmv", "asf", "mp3", "mpg", "mpeg", "mp4", "avi", "wmv", "wma", "wav", "dat"];
   var MEDIA_FLASH_TYPE = ["flv", "fla"];
   var DIRECT_VIEW_TYPE = ["jpg", "jpeg", "bmp", "gif", "png", "xml", "xhtml", "html", "htm", "mid", "mht", "pdf", "swf"];
   var index = fileName.lastIndexOf(".");
   var extName = "";
 
   if (index >= 0) {
     extName = fileName.substring(index + 1).toLowerCase().trim();
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
    extName = fileName.substring(index + 1).toLowerCase().trim();
  }
  if(videoType.contains(extName)){
    return 1;
  }else{
   return 0;
  }
}

function checkboxClick(el,uploadNum){
  
  var attachmentDocName = $('attachLink_'+uploadNum+'_name').value;
  if(!(endWith(attachmentDocName,'.doc') || endWith(attachmentDocName,'.docx'))){
    el.checked = false;
    alert('请选择word格式的文件做为正文！');
    return;
  }
  var attachDocs =  document.getElementsByName("attachDocBox");
  for (var i = 0; i < attachDocs.length; i++) {
    attachDocs[i].checked = false;
  }
  
  $('mainDocId').value = $('attachLink_'+uploadNum+'_id').value;
  $('mainDocName').value = $('attachLink_'+uploadNum+'_name').value;
  el.checked = true;
}

function deleteAttachDel(i){
  
  if(!window.confirm("确认要删除该附件 ？")) {
    return ;
  } 
  
  if($('mainDocId').value == $('attachLink_'+i+'_id').value){
    $('mainDocId').value = '';
  }
  deleteAttach(null, 'attachLink_' + i, null, i);
}

function endWith(s1,s2){  
  if(s1.length < s2.length)  
    return false;  
  if(s1 == s2)  
    return true;  
  if(s1.substring(s1.length - s2.length) == s2)  
    return true;  
  return false;  
}