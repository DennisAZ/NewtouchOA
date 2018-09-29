/**
 * 加载AIP数据
 */
function loadMinAIP(seqId) {
  var rtJson = getJsonRs(contextPath + "/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/selectById.act", {seqId: seqId});
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg);
    return false;
  }
  return  ;
}

// 用户登录，并把模式改为设计模式
function addField()
{
  var obj = $("HWPostil1");
  var vRet = obj.Login( _userName , 5, 32767, "", "");
  if(0 == vRet) {
    obj.InDesignMode = true;
  }
}
/**
 * 
 *InDesignMode = true 的情况下
    1 鼠标在文档上按住左键
    2.移动鼠标，此时会在页面上画出一个矩形的线框
    3.松开鼠标左键，此时会触发NotifyLineAction事件
 * @param lPage
 * @param lStartPos
 * @param lEndPos
 * @return
 */
function NotifyLineAction(lPage,lStartPos,lEndPos)
{
  if (!isOpenPropWindow && !isOpenSetWindow) {
    var lStartY = (lStartPos>>16)& 0x0000ffff;
    var lStartX = ((lStartPos<<16)>>16) & 0x0000ffff; 

    var lEndY = (lEndPos>>16)& 0x0000ffff;
    var lEndX = ((lEndPos<<16)>>16) & 0x0000ffff; 
    // alert(lStartX);alert(lStartY);
    openWin();
    argObj = {"page":lPage,"StartX":lStartX,"StartY":lStartY,"EndX":lEndX,"EndY":lEndY};
    
  }
}
var fieldIndex = 0;

// 新建节点并保存到数据库中
function setField(field_name
    ,field_type
    ,font_family
    ,font_size
    ,font_color
    ,border_style
    ,halign
    ,valign
    ,noteType
    ,childnoteCount
    ,fieldSpace
     ){
  if(argObj == null)
    return;
  var obj = $("HWPostil1");


  var field_width = argObj.EndX - argObj.StartX;
  var field_height = argObj.EndY - argObj.StartY; 
  var CurrPage = 0;
  CurrPage = argObj.page;//鼠标点击的当前页数a
  fieldIndex++;
  var num = obj.GetNoteNum(10);// 得到所有节点数
  var munber = (num+1);
  //var fieldNameDb = "page" + CurrPage + ".field-" + munber ;
  var fieldName =  "field-" + munber ;
  if(noteType=='1' && childnoteCount != ''){// 如果是身份证类型
    var noteCount = parseInt(childnoteCount,  10);
    if(fieldSpace == ''){
      fieldSpace = 0;
    }
    var fieldSpace = parseInt(fieldSpace,  10);
    
    for(var j = 0 ; ;j++){// 如果有的话就一直循环到没有的
      munber = (munber + j);
      fieldName =   "field-" + munber; 
      if(CheckNote(obj,fieldName)){
        vRet = obj.InsertNote(fieldName,CurrPage,field_type,argObj.StartX,argObj.StartY,field_width,field_height);　
        break;
      }
    }
    /*var vRet = obj.InsertNote(fieldName,CurrPage,field_type,argObj.StartX,argObj.StartY,field_width,field_height);　
    if(vRet==""){
      for(var j = 1 ; ;j++){// 如果有的话就一直循环到没有的
        munber = (munber + j);
        fieldName =   "field-" + munber; 
        vRet = obj.InsertNote(fieldName,CurrPage,field_type,argObj.StartX,argObj.StartY,field_width,field_height);　

        if(vRet != ''){
          break;
        }
      }
    }*/
    font_size = font_size.replace(/pt/ig,"");
    font_color = font_color.replace(/#/ig,"0x00");
    obj.SetValue(fieldName,":PROP:BORDER:"+border_style);
    obj.SetValue(fieldName,":PROP:FACENAME:"+font_family);
    obj.SetValue(fieldName,":PROP:FONTSIZE:"+font_size);
    obj.SetValue(fieldName,":PROP:FRONTCOLOR:618221");
    obj.SetValue(fieldName, ":PROP::LABEL:0");
    obj.SetValue(fieldName,":PROP:HALIGN:"+halign);
    obj.SetValue(fieldName,":PROP:VALIGN:"+valign);
    for(var i = 1; i < noteCount; i++){
      var fieldNameTemp = "";
      fieldNameTemp = fieldName + "_" + (i);
      //生成一摸一样的小格子
      var X = parseInt(argObj.StartX ,10) + (i*parseInt(field_width,10)) + ((i)*fieldSpace);
      //由一个大格子生成多个小格子
      
      var vRet = obj.InsertNote(fieldNameTemp,CurrPage,field_type,X,argObj.StartY,field_width,field_height);　
      if(vRet==""){
         
      }
      font_size = font_size.replace(/pt/ig,"");
      font_color = font_color.replace(/#/ig,"0x00");
      obj.SetValue(fieldNameTemp,":PROP:BORDER:"+border_style);
      obj.SetValue(fieldNameTemp,":PROP:FACENAME:"+font_family);
      obj.SetValue(fieldNameTemp,":PROP:FONTSIZE:"+font_size);
      obj.SetValue(fieldNameTemp,":PROP:FRONTCOLOR:618221");
      obj.SetValue(fieldNameTemp, ":PROP::LABEL:3");
      obj.SetValue(fieldNameTemp,":PROP:HALIGN:"+halign);
      obj.SetValue(fieldNameTemp,":PROP:VALIGN:"+valign);
    }
    
  }else{
    for(var j = 0 ; ;j++){// 如果有的话就一直循环到没有的
      munber = (munber + j);
      fieldName =   "field-" + munber; 
      if(CheckNote(obj,fieldName)){
        vRet = obj.InsertNote(fieldName,CurrPage,field_type,argObj.StartX,argObj.StartY,field_width,field_height);　
        break;
      }
    }
   
/*    var vRet = obj.InsertNote(fieldName,CurrPage,field_type,argObj.StartX,argObj.StartY,field_width,field_height);
    if(vRet==""){
      for(var j = 1 ; ;j++){// 如果有的话就一直循环到没有的
        munber = (munber + j);
        fieldName =   "field-" + munber; 
        vRet = obj.InsertNote(fieldName,CurrPage,field_type,argObj.StartX,argObj.StartY,field_width,field_height);　
        if(vRet != ''){
          break;
        }
      }
     
    }*/
    font_size = font_size.replace(/pt/ig,"");
    font_color = font_color.replace(/#/ig,"0x00");
    obj.SetValue(fieldName,":PROP:BORDER:"+border_style);
    obj.SetValue(fieldName,":PROP:FACENAME:"+font_family);
    obj.SetValue(fieldName,":PROP:FONTSIZE:"+font_size);
    obj.SetValue(fieldName,":PROP:FRONTCOLOR:618221");
    obj.SetValue(fieldName, ":PROP::LABEL:0");
    obj.SetValue(fieldName,":PROP:HALIGN:"+halign);
    obj.SetValue(fieldName,":PROP:VALIGN:"+valign);
  }
  // 与数据库同步，添加一个字段
  // alert(argObj.StartX);
  var modulField = addModulField(field_name,fieldName,argObj.StartX,argObj.StartY,field_width,field_height,
      font_family,font_size,halign,valign,munber,noteType,childnoteCount,fieldSpace,CurrPage);

  var sql = "";
  if(modulField.seqId){
    sql = modulField.seqId;
    argObj = null;
    var note = {field:fieldName , des:field_name,seqId:sql,noteType:noteType,childnoteCount:childnoteCount};
    notes.push(note);
    addFieldDiv(note);
  }
}

var isOpenPropWindow = false;
var win = null;
function openWin() {
  isOpenPropWindow = true;
  var w = 400 ;
  var h  = 360;
  var fontSizeType = "12";//默认字体大小
  if($("fontSizeType") && $("fontSizeType").value !="")
  { 
    fontSizeType = $("fontSizeType").value ;
  }
  var fontType = "宋体";//默认字体类型
  if($("fontType") && $("fontType").value !="")
  { 
    fontType = $("fontType").value ;
  }
  var top =(jQuery(window).height() - h - 80) / 2 + document.documentElement.scrollTop;
  var left =  (jQuery(window).width() - w) / 2 ;
  createSim(w - 3  , h + 45, top , left);
  win = new YH.Window({
    'title' : '设置字段：',
   'draggable': false,
    'type': 'iframe',
    'width': w,
    'height': h,
    'modal':true,
    'closeAction': 'hide',
    'src':paramsPath + "params.jsp?fontSizeType=" + fontSizeType + "&fontType=" + encodeURIComponent(fontType),
    listeners:{
      hide:function() {
        removeSim();
    }
  }
  });
  win.show();
}
function closeWinow() {
  win.hide();
}
window.removeSim = function(){
  isOpenPropWindow = false;
  var shim = document.getElementById('menuIframe');
  if (shim) {
    document.body.removeChild(shim);
  }
}
function createSim(w , h , x, y ) {
  var  shim = new Element("iframe");
  shim.id = "menuIframe";
  shim.scrolling = "no";
  shim.frameborder = "0"; 
  shim.src = contextPath + "/core/inc/emptyshim.html";
  shim.style.position = "absolute";
  shim.style.filter = "alpha(opacity=40)";
  shim.style.opacity = 0.4;
  shim.style.position = "absolute";
  shim.style.display = "block";
  shim.style.zIndex = 10;
  shim.style.top = x + "px";
  shim.style.left = y  + "px";
  shim.style.width =  w  + "px";
  shim.style.height = h  + "px";
  document.body.appendChild(shim);
}

/*
 *节点批量设置更新
 */
var isOpenSetWindow = false;
var setWin = null;
function setAllNote(modulId){
 // alert(modulId);
  if(!isOpenSetWindow && !isOpenPropWindow){
    openSetNoteWin();
  }
}
function openSetNoteWin() {
  isOpenSetWindow = true;
  var w = 400 ;
  var h  = 310;
  var top =(jQuery(window).height() - h - 80) / 2 + document.documentElement.scrollTop;
  var left =  (jQuery(window).width() - w) / 2 ;
  createSetNoteSim(w -4 , h + 44, top , left);

  setWin = new YH.Window({
    'title' : '节点批量设置更新：',
   'draggable': false,
    'type': 'iframe',
    'width': w,
    'height': h,
    'modal':true,
    'closeAction': 'hide',
    'src':paramsPath + "setNote.jsp",
    listeners:{
      hide:function() {
        removeSetNoteSim();
    }
  }
  });
  setWin.show();
}
function closeSetNoteWinow() {
  setWin.hide();
}
window.removeSetNoteSim = function(){
  isOpenSetWindow = false;
  var shim = document.getElementById('menuIframe');
  if (shim) {
    document.body.removeChild(shim);
  }
}
function createSetNoteSim(w , h , x, y ) {
  var  shim = new Element("iframe");
  shim.id = "menuIframe";
  shim.scrolling = "no";
  shim.frameborder = "0"; 
  shim.src = contextPath + "/core/inc/emptyshim.html";
  shim.style.position = "absolute";
  shim.style.filter = "alpha(opacity=40)";
  shim.style.opacity = 0.4;
  shim.style.position = "absolute";
  shim.style.display = "block";
  shim.style.zIndex = 10;
  shim.style.top = x + "px";
  shim.style.left = y  + "px";
  shim.style.width =  w + "px";
  shim.style.height = h + "px";
  document.body.appendChild(shim);

}
function setNoteField(font_family
    ,font_size
    ,font_color
    ,border_style
    ,halign
    ,valign  ){
  font_size = font_size.replace(/pt/ig,"");
  updateSetNote(font_family,font_size,font_color,border_style ,halign,valign);
  
}
/*
 * 节点批量设置更新结束
 */
/**
 * aip操作函数 设置JSEvn = 1时起作用
 * 
 * @param lActionType
 * @param lType
 * @param strName
 * @param strValue
 * @return
 */
var type ="0";
function JSNotifyBeforeAction(lActionType,lType,strName,strValue) {
  var obj = $("HWPostil1");
  //打印
  if (1 == lActionType  ) {
    
    if(isPrintSign == '0'){//不控制打印
      
    }else{
      if(type == '0'){//第一次
        if(printCountValue >=  lType){//如果打印份数超过
          obj.SetValue(printCountName,"");
          obj.SetValue(printCountName,parseInt(printCountValue,10)-lType);//
          var printStratNo =   obj.GetValueEx(printStratName,2,"",0,"");
          var printEndNo =   obj.GetValueEx(printEndNoName,2,"",0,"");
          if(printStratNo){//修改起始打印编号printEndNo
            var printStratNoNew = parseInt(printStratNo,10 ) + lType;
            var printStratNoNewTemp = parseInt(printStratNo,10 ) + lType -1;
            var startNum = printStratNoNew;
            var startNoStr = printStratNoNew;
            var printStratNoNewTempStr = printStratNoNewTemp;
            for(var i=1;i<=3;i++){
              if(startNum.toString().length<i){
                startNoStr = "0" + startNoStr;
                printStratNoNewTempStr = "0" + printStratNoNewTempStr;
              }
            }
            obj.SetValue(printStratName,"");
            obj.SetValue(printStratName,startNoStr);
            //addSelLogPrint(seqId,lType,printStratNo,printStratNoNewTempStr)
          }
          
      
          type ="1";
          obj.JSValue  = 0;//终止打印操作
          updateAipToService();//上传AIP
          forEachPrint(lType,printStratNo,strName,parseInt(printCountValue,10)-lType);
         // var printEndNo =  obj.GetValueEx(printEndNoName,2,"",0,"");
          
        }else{
          alert("打印份数不能超过" + printCountValue + "份!");
          obj.JSValue  = 0;//终止操作
          return;
        }
      }else{//第2--第N次

        //alert (lType);
      }
    }
    
  }
}

/**
 * 盖章触发时间
 */
function JSNotifyBeforeSealAction(lActionType,lType,strName,strValue) {
  var obj = $("HWPostil1");
  //盖章
  var noteTmp = new Array();
  if (4 == lActionType  ) {
   // alert("删除盖章");
   // alert(strName);
    var status = false;
    for(var i = 0;i<sealIdArr.length;i++){
      if(sealIdArr[i] == strName){//如果删除的是当前章
        if(status){
          noteTmp.push(sealIdArr[i]);
        }
        status = true;
      }else{
        noteTmp.push(sealIdArr[i]);
      }
    }
    sealIdArr = noteTmp;
   // sealSeqArr.push(seqId);
  }else if(5 == lActionType  ){
     sealIdArr.push(strName);
   // alert("盖章");
  }
  //alert(sealIdArr);
}
/**
 * 循环打印
 * printNum:打印数
 * printStratNo：编号
 * SYDYFS:剩余份数
 * @returns
 */
function forEachPrint(printNum,printStratNo,strName,SYDYFS){
  var obj = $("HWPostil1");
  var lastStratNo = parseInt(printStratNo,10) + printNum;
  var YJDYFS = parseInt(PRINT_COUNT_ALL,10) - SYDYFS;
  for(var j=0;j<printNum;j++){
   // alert(printNum +":" +printStratNo);
    var printStratNoNew = parseInt(printStratNo,10 ) + j;

    var startNum = printStratNoNew;
    var startNoStr = startNum;
    for(var i=1;i<=3;i++){
      if(startNum.toString().length<i){
        startNoStr = "0" + startNoStr;
        
      }
      if(lastStratNo.toString().length<i){
        lastStratNo = "0" + lastStratNo;
      }
    }

    obj.SetValue(printStratName,"");
    obj.SetValue(printStratName,startNoStr);
    obj.SetValue(printStratName, ":PROP:FONTSIZE:14");//字体为1
    obj.SetValue(printStratName,":PROP:FRONTCOLOR:000000");//败诉　
    var printStatus = obj.PrintDocEx(strName,1,0,100,0,100,1,1,1,1,0);
    if(printStatus && j==printNum-1){//打印成功，且是最后一次则更新到下一个号打印份数
      obj.SetValue(printStratName,"");
      obj.SetValue(printStratName,lastStratNo);
      obj.SetValue(printStratName, ":PROP:FONTSIZE:14");//字体为1
      obj.SetValue(printStratName,":PROP:FRONTCOLOR:000000");//败诉　
      
      /*
      *添加打印详情
      */
      $("printInfo").update("打印份数：" + YJDYFS + " / " + PRINT_COUNT_ALL);

    }
    //打印新建安全日志
    var seqId = $("seqId").value;
    addSelLogPrint(seqId,1,startNoStr,startNoStr);
    //obj.PrintDoc(1,0);
    //alert(printNum +":" +printStratNo);
  }
  
  updatePrintStatus($("seqId").value);
}
/**
 * aip操作函数设置JSEvn = 0时起作用
 * 
 * @param lActionType
 * @param lType
 * @param strName
 * @param strValue
 * @return
 */
function NotifyBeforeAction(lActionType,lType,strName,strValue,plContinue) {
  var obj = $("HWPostil1");
  //打印
  if (1 == lActionType) {
    //alert(lType);
    if(printCountValue >=  lType){
      obj.SetValue(printCountName,"");
      obj.SetValue(printCountName,parseInt(printCountValue,10)-lType);
    }else{
      alert("打印份数不能超过" + printCountValue + "份!");
      obj.plContinue = 0;
      obj.JSEnv = 0;
      return;
    }

  }

}
/**
 * aip操作函数设置 是触发事件后执行
 * 
 * @param lActionType
 * @param lType
 * @param strName
 * @param strValue
 * @return
 */
function NotifyAfterAction(lActionType,lType,strName,strValue,plContinue) {
  // 删除节点
  var obj = $("HWPostil1");
  if (3 == lActionType) {
    delNote(strName,0);
  }
}



/*
 * 
 *添加一页
 *
 */
function addMergeFileJs(){
  var obj = $('HWPostil1');
  //设置A4纸打印
  obj.ForceSignType2 = obj.ForceSignType2 | 0x10000000;
  obj.SetValue("PREDEF_PAPER_WIDTH", 793);
   obj.SetValue("PREDEF_PAPER_HEIGHT", 1123);
  obj.MergeFile(99,"");　  
}

/**
 * 加载附件Word或者图片
 * filePath:附件路径
 */
function loadFileToAIP(filePath){
  var obj = $("HWPostil1");
  obj.LoadFile(filePath);
}


/*
 * 加载AIP  object对象
 * divId：将AIP对象插入DIV或者其他元素的对象
 * width：：宽度
 * height:高度
 */
function loadAIPObject(divId,width,height){
  var s = "";
   s += "<OBJECT id=HWPostil1 style='WIDTH:" + width + "px;HEIGHT:" + height + "px;' " ;
   s += "classid='clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567'" ;
   s += " codeBase='/yh/subsys/jtgwjh/setting/aip/HWPostil.cab#version=3.0.9.4' >";
   s += "</OBJECT>";
   if(divId){
     divId.insert(s);
   }else{
     document.write(s) ; 
   }
 // 
}

/**
 * 加载word T0 AIP
 * @param obj
 * attachId:附件Id
 * attachName：附件名称
 * attachPath：yh /attach路径
 * serviceName:服务器IP
 * port：端口号
 * objID:点聚控件加载对象Id
 * @returns
 */
function loadWordToAIP(attachmentId,attachmentName,attachPath,serviceName,port,objID){
  //加载AIP
 // alert(attachmentId +":"+attachmentName)
  if(attachmentId && attachmentName){

    loadAIPObject(objID,0,0);
    var obj = $("HWPostil1");
    //obj.SilentMode =1 ;//1：安静模式。安静模式下不显示下载提示对话框、文档转化提示对话框和部份提示信息
    var attachmentIdArra = attachmentId.split("_");
    var attachmentIdDate = attachmentIdArra[0];
    var fileName =attachmentName;

    var attachmentIdDate = attachmentId.substr(0,4);
    var fileName = attachmentIdArra[1] + "_" + attachmentName;
    var filePath = attachPath +attachmentIdDate + "/" + fileName;
   // alert(filePath);
    //document.write("http://" + serviceName + ":" + port + contextPath+ "/getFile?uploadFileNameServer=" + encodeURIComponent(filePath));
    //obj.LoadFileEx("http://" + serviceName + ":" + port + contextPath+ "/getFile?uploadFileNameServer=" + encodeURIComponent(filePath),"",0,0);
    obj.LoadFile("http://" + serviceName + ":" + port + contextPath+ "/getFile?uploadFileNameServer=" + encodeURIComponent(filePath));
    
    
    return filePath;
  }
  return "";
}


/*
 *获取所有节点 (包括所有用户)
 */


function getAllUserNoteInfo(obj){
  var User = "";//用户
  var AllNoteInfoStr = "";//所有用户节点字符串
  while(User=obj.JSGetNextUser(User)){
    var sealNoteArray = new Array();
    var NoteInfo = "";
    var vRet = obj.Login( User , 5, 32767, "", "");//编辑模式
    while(NoteInfo=obj.GetNextNote(User,0,NoteInfo)){//循环所有节点
      var noteInfoStrOne = "";
      var noteInfoStr = "";
      var noteInfoContent = " ";
      var noteType = obj.GetValueEx(NoteInfo,12,"",0,"");//判断节点类型
      //alert(NoteInfo)
    
      if(noteType == 3 ){//图像/印章/二维码
        sealNoteArray.push(NoteInfo);
       // var t = obj.DeleteNote(NoteInfo);
   
      }else if(noteType == 10){//手写/文字区域 
        
      }
      
    }
    delNoteByArray(obj,sealNoteArray);
    obj.Logout();
  }
}

/**
 * 删除所有章
 * */
function delNoteByArray(obj,sealNoteArray){
  for(var i =0;i<sealNoteArray.length ;i++){
   // alert(sealNoteArray[i]);
    var t = obj.DeleteNote(sealNoteArray[i]);
  }
}


/*
 * 新建格子,打印份数、开始和结束编号 --syl
 */
var printCountName = "printCount";
var printStratName = "printStratNo";
var printEndNoName = "printEndNo";
var isPrintSignName = "printSign";
 /* printCountName:打印份数节点名称
 printStratName :开始编号
 printEndNoName:结束编号
  isPrintSignName:是否打印标志 0：不控制，1：控制
*/
function addAipNote(){
 var obj = $("HWPostil1");
 var printCount = obj.GetValue(printCountName);//打印份数
 var printStratNo = obj.GetValue(printStratName);//开始编号
 var printEndNo = obj.GetValue(printEndNoName);//结束编号
 var printSign = obj.GetValue(isPrintSignName);//结束编号
 
 if(!printSign){
   var vRet = obj.InsertNote(isPrintSignName,0,3,6667,333,2000,1000);　
   obj.SetValue(isPrintSignName,"1");
   obj.SetValue(isPrintSignName,":PROP:BORDER:0");//无边框
   obj.SetValue(isPrintSignName,":PROP:FRONTCOLOR:-1");//背景透明
   obj.SetValue(isPrintSignName, ":PROP::LABEL:3");//不可点击
   obj.SetValue(isPrintSignName, ":PROP:FONTSIZE:14");//字体为1
   obj.SetValue(isPrintSignName,":PROP:FRONTCOLOR:16777215");//败诉
 }
 
 if(!printCount){
   var vRet = obj.InsertNote(printCountName,0,3,6667,2333,3000,1000);　
   obj.SetValue(printCountName,"0");
   obj.SetValue(printCountName,":PROP:BORDER:0");//无边框
   obj.SetValue(printCountName,":PROP:FRONTCOLOR:-1");//背景透明
   obj.SetValue(printCountName, ":PROP::LABEL:3");//不可点击
   obj.SetValue(printCountName, ":PROP:FONTSIZE:14");//字体为1
   obj.SetValue(printCountName,":PROP:FRONTCOLOR:16777215");//败诉
 }
 if(!printStratNo){
   var vRet = obj.InsertNote(printStratName,0,3,6667,6033,3000,1500);
   obj.SetValue(printStratName,"")　;
   obj.SetValue(printStratName,":PROP:BORDER:0");//无边框
   obj.SetValue(printStratName,":PROP:FRONTCOLOR:-1");//背景透明
   obj.SetValue(printStratName, ":PROP::LABEL:3");//不可点击
   obj.SetValue(printStratName, ":PROP:FONTSIZE:16");//字体为16,三号
   obj.SetValue(printStratName,":PROP:FACENAME:方正姚体");
   obj.SetValue(printStratName,":PROP:FRONTCOLOR:16777215");//败诉
   obj.SetValue(printStratName,":PROP:FONTBOLD:1");//加粗
 }
 if(!printEndNo){
   var vRet = obj.InsertNote(printEndNoName,0,3,8000,4333,3000,1000);
   obj.SetValue(printEndNoName,"")　;
   obj.SetValue(printEndNoName,":PROP:BORDER:0");//无边框
   obj.SetValue(printEndNoName,":PROP:FRONTCOLOR:-1");//背景透明
   obj.SetValue(printEndNoName, ":PROP::LABEL:3");//不可点击
   obj.SetValue(printEndNoName, ":PROP:FONTSIZE:14");//字体为1
   obj.SetValue(printEndNoName,":PROP:FRONTCOLOR:16777215");//败诉　
 }
 obj.Logout();
}