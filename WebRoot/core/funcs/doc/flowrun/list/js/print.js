var priv = "";
var isPrint = true;
function doInit(){
  var url = contextPath +moduleSrcPath+"/act/YHFlowFormPrintAct/getFormPrintMsg.act";
  var json = getJsonRs(url , "runId=" + runId +"&flowId=" + flowId + "&flowView=" + flowView);
  //alert(rsText);
  if(json.rtState == '0'){
    var data = json.rtData;
    //取出form字符串,如不为空有值，给id=form的div赋上表单字符串并设置它的显示
    window.style = data.css;
    if (window.style) {
      var elmSty = document.createElement('STYLE');
      elmSty.setAttribute("type", "text/css");
      if (elmSty.styleSheet) { 
        elmSty.styleSheet.cssText=window.style;  
      } else { 
        elmSty.appendChild(document.createTextNode(styCss));  
      } 
      document.getElementsByTagName("head")[0].appendChild(elmSty); 
    }
    var formStr = data.form;
    if(formStr){
      $('form').insert(formStr);
      $('form').show();//相当于display:''
    }
    var js = data.js;
    if (js) {
      try {
        window.execScript(js);
      } catch(e) {
        
      }
    }
    //取出attachment数组,如不为空有值，给id=attachment的div加上数据
    var attachment = data.attachment;
    if(attachment && attachment.length > 0){
      for(var i = 0 ;i < attachment.length ;i ++){
      var attach = attachment[i];
      addAttachment(attach);
    }
    $('attachment').show();
    }
    var prcsList = data.prcsList;
    if(prcsList && prcsList.length > 0 ){
      for(var i = 0 ;i < prcsList.length ; i++){
        var prcs = prcsList[i];
        createTr(prcs , (i + 1 == prcsList.length ? true : false));
      } 
      $('prcss').show();
    }
    var feedbacks = data.feedbacks;
    if (feedbacks && feedbacks.length > 0) {
      for (var i = 0 ;i < feedbacks.length ;i ++) {
        var feedback = feedbacks[i];
        addFeedback(feedback);
      }
      $('feedBack').show();
    }
  }
}


/**
 * 添加一个会签
 */
function addFeedback(feedback){
  var tr = new Element('tr' , {'class' : 'TableData'});
  $('feedbackList').appendChild(tr);
  var td = new Element('td',{'align' : 'left','width' : '100%'});
  tr.appendChild(td);
  var tmp = new Array();
  tmp[0] = "第";
  tmp[1] = feedback.prcsId;
  tmp[2] = "步&nbsp;&nbsp;";
  tmp[3] = "【" + feedback.feedName + "】" +  feedback.feedDesc ;
  tmp[4] = "&nbsp;&nbsp;<a href='javascript:' title='";
  tmp[5] = feedback.deptName;
  tmp[6] = "'>" ;
  tmp[7] = feedback.userName ;
  tmp[8] = "</a>&nbsp;&nbsp;" ;
  tmp[9] = feedback.editTime ;
  tmp[10] = "&nbsp;&nbsp;" ;
  if (feedback.hasSignData) {
    tmp[11] = "<img src='" + imgPath +"/focus.gif' style=\"cursor:pointer\" alt=\"查看手写签章\" align=\"absmiddle\" onClick=\"showSign('"+feedback.feedId+"');\">&nbsp;";
  }
  var title = tmp.join("");
  var content = feedback.content;
  var attachment = "";
  var div = new Element('div');
  td.appendChild(div);
  div.innerHTML = title;
  div = new Element('div');
  td.appendChild(div);
  div.id = 'feedback-' + feedback.feedId;
  div.style.lineHeight = "normal";
  div.innerHTML = content;

  for (var i = 0 ; i < feedback.attachment.length ;i ++) {
      var tmp = feedback.attachment[i];
      div = new Element('div');
      td.appendChild(div);
      var str = addFeedBackAttach(tmp);
      div.innerHTML = str;
    }
}

 function createTr(prcs1 , isLast){
   var clazz = 'TableLine2';
   //是最后一行的话颜色不一样
   if(isLast){
     clazz = 'TableLine1';
   }
   var tr = new Element("tr",{'class':clazz});
   //第一列
   var td = new Element("td").update("第<font color=red>"+ prcs1.prcsId +"</font>步");
   
   tr.appendChild(td);
   var list = prcs1.list;
   if (list.length > 0) {
     td.rowSpan = list.length; 
   }
   $('listTbody').appendChild(tr);
   for (var j = 0 ;j < list.length ;j++) {
     //第二列
     var prcs = list[j];
     var td2 = new Element("td").update("<img src='"+ imgPath +"/arrow_down.gif'/>&nbsp;序号"+ prcs.flowPrcs +"：" + prcs.prcsName);
     if (j != 0) {
       tr = new Element("tr",{'class':clazz});
     }
     tr.appendChild(td2);
     //第三列
     var title =  "";
     for(var i = 0 ; i < prcs.user.length ;i++){
       var user = prcs.user[i];
       var prcsUserName = "";
       if(user.isOp){
         prcsUserName = '<span style="text-decoration:underline;font-weight:bold;color:red;cursor:pointer"  title="部门：'+user.deptName+'">'+ user.userName +' 主办</span>';
       }else{
         prcsUserName = '<span style="text-decoration:underline;font-weight:bold;cursor:pointer" title="部门：'+user.deptName+'">'+ user.userName +'</span>';
       }
       var state = user.state;
       if(state == 1){
         title += "<img src='"+ imgPath +"/email_close.gif'  align='absmiddle'/>&nbsp;" + prcsUserName + "&nbsp;[<font color=green>未接收办理</font>]";
       //办理中
       }else if(state == 2){
         title += "<img src='"+ imgPath +"/email_open.gif'  align='absmiddle'/>&nbsp;" + prcsUserName + "&nbsp;[<font color=green>办理中,已用时：" + user.timeUsed + "</font>]";
         
         if(user.timeOutFlag){
           title += '<br><span style="color:red">限时'+ user.timeOut +"小时," + user.timeUsed + '</span>';
         }
         title += "<br> 开始于：" + user.beginTime;
       //已转交下步
       }else if(state == 3){
         title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>&nbsp;"+ prcsUserName +"&nbsp;[<font color=green>已转交下步,用时：" + user.timeUsed + "</font>]";
         title += "<br> 开始于：" + user.beginTime;
         if(user.deliverTime){
           title += "<br> 结束于：" + user.deliverTime;
         }
         //已办结
       }else if(state == 4){
         title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>&nbsp;"+ prcsUserName +"&nbsp;[<font color=green>已办结,用时：" + user.timeUsed + "</font>]";
         title += "<br>开始于：" + user.beginTime;
         if(user.deliverTime){
           title += "<br> 结束于：" + user.deliverTime;
         }
       }else if(state == 5){
         title += prcsUserName + "&nbsp;[预设经办人]";
       }
       title += "<br><br>";
     }
     var td3 = new Element("td").update(title);
     tr.appendChild(td3);
     if (j != 0) {
       $('listTbody').appendChild(tr);
     }
   }
   
   
 }
/**
 * 添加附件
 * attachment:{attachmentName:'', attachmentId:'',ext:''}
 */
function addAttachment(attachment){
  var tr = new Element('tr' , {'class' : 'TableData'});
  $('attachmentsList').appendChild(tr);
  var td = new Element('td',{'align' : 'left','width' : '100%'});
  tr.appendChild(td);
  var imgSrc = "";
  var ext = attachment.ext;
  var imgStr = getAttachImage(ext);
  priv = attachment.priv;
  var str = "<a href='javascript:' onmouseover='createDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.ext +"\")'><img src='" + imgStr + "'/>&nbsp;" + attachment.attachmentName + "</a>";
  td.innerHTML = str; 
}
/**
 * 创建右建菜单
 * 
 */
function createDiv(event , node , attachId , attachName , ext){
  var down = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">下载<div>',action:downAction ,extData: ""};
  var save = { attachmentId:attachId , attachmentName: attachName ,name:'<div style="padding-top:5px;margin-left:10px">转存<div>',action:saveAction,extData: ""};
  var read = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:readAction,extData: ""};
  var print = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:printAction,extData: ""};
  var preview = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">预览<div>',action:previewAction,extData: ""};
  var play = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">播放<div>',action:playAction,extData: ""};
  var menuD = [];
  var pr = priv.split(",");
  if ( isMedia(attachName) || isVideo(attachName)){
    menuD.push( play );
    menuD.push( down );
    menuD.push( save );
  }else if( findIsIn(docEnd , ext ) ){
    if (pr[0] == '1') {
      menuD.push( down );
      menuD.push( save );
    }
    if (pr[1] == '1' ) {
      menuD.push( print );
    } else {
      menuD.push( read );
    }
   } else {
     menuD.push( down );
     menuD.push( save );
   }
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:node , menuData:menuD , attachCtrl:true},divStyle);
  menu.show(event);
}
/**
 * 查看后缀ext是否在exts中
 */
function findIsIn(exts , ext){
  for(var i = 0 ;i < exts.length ; i++){
    var tmp = exts[i];
    if(tmp == ext){
      return true;
    }
  }
  return false;
}

function addFeedBackAttach(attachment) {
  var ext = attachment.ext;
  var imgStr = getAttachImage(ext);
  var str = "<a href='javascript:' onmouseover='createAttachDiv(event  ,this , \""+ attachment.attachmentId +"\" , \""+ attachment.attachmentName +"\" , \""+ attachment.ext +"\")'><img src='" + imgStr + "'/>&nbsp;" + attachment.attachmentName + "</a>";
  return str;
}
/**
 * 创建会签附件右建菜单
 * 
 */
function createAttachDiv(event , node , attachId , attachName , ext ){
  var down = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">下载<div>',action:downAction ,extData: ""};
  var save = { attachmentId:attachId , attachmentName: attachName ,name:'<div style="padding-top:5px;margin-left:10px">转存<div>',action:saveAction,extData: ""};
  var read = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:readAction,extData: ""};
  var print = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">阅读<div>',action:printAction,extData: ""};
  var play = { attachmentId:attachId , attachmentName: attachName ,name:'<div  style="padding-top:5px;margin-left:10px">播放<div>',action:playAction,extData: ""};
  var menuD = [];
  if ( findIsIn ( imgEnd , ext)){
    menuD.push( down );
    menuD.push( save );
    menuD.push( play );
  }else if( findIsIn(docEnd , ext ) ){
    menuD.push( read );
    menuD.push( down );
    menuD.push( save );
  } else {
    menuD.push( down );
    menuD.push( save );
  }
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:node , menuData:menuD , attachCtrl:true},divStyle);
  menu.show(event);
}
var cursor = "";
function LoadSignDataSign(data,count,content,version)
{
  var vDWebSignSeal=document.getElementById("DWebSignSeal");
  try {
    if(!vDWebSignSeal)
       return;
    vDWebSignSeal.SetStoreData(data);
    var strObjectName ;
    strObjectName = vDWebSignSeal.FindSeal(cursor,0);
    while(strObjectName  != "")
    {
      if(strObjectName.indexOf("SIGN_INFO")>=0)
      {
         vDWebSignSeal.MoveSealPosition(strObjectName,0, -30, "personal_sign"+count);
         break;
      }
      strObjectName = vDWebSignSeal.FindSeal(strObjectName,0);
    }
    vDWebSignSeal.ShowWebSeals();
    if(version==0)
      vDWebSignSeal.SetSealSignData (strObjectName,"中国兵器工业信息中心");
    else
      vDWebSignSeal.SetSealSignData (strObjectName,content);
    vDWebSignSeal.SetMenuItem(strObjectName,4);
    sign_info_object += strObjectName+",";
    strObjectName = vDWebSignSeal.FindSeal(strObjectName,0);
  } catch (err) {
    //alert('websign控件没有加载成功！')
  }
}