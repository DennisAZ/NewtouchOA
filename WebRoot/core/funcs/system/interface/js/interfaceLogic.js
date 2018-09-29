/**
 * 字体
 * @param event
 * @return
 */
var privObjFont = {};
menuDataFont = [{ name:'<div  style="padding-top:5px;margin-left:10px">默认字体</div>',action:setAction,extData:'默认字体,默认字体'}
   ,{ name:'<div  style="font-family:黑体;padding-top:5px;margin-left:10px">黑体</div>',action:setAction,extData:'黑体,黑体'}
   ,{ name:'<div  style="font-family:楷体;padding-top:5px;margin-left:10px">楷体</div>',action:setAction,extData:'楷体,楷体'}
   ,{ name:'<div  style="font-family:隶书;padding-top:5px;margin-left:10px">隶书</div>',action:setAction,extData:'隶书,隶书'}
   ,{ name:'<div  style="font-family:幼圆;padding-top:5px;margin-left:10px">幼圆</div>',action:setAction,extData:'幼圆,幼圆'}
   ,{ name:'<div  style="font-family:Arial;padding-top:5px;margin-left:10px">Arial</div>',action:setAction,extData:'Arial,Arial'}
   ,{ name:'<div  style="font-family:Courier New;padding-top:5px;margin-left:10px">Courier New</div>',action:setAction,extData:'Courier New,Courier New'}
   ,{ name:'<div  style="font-family:Fixedsys;padding-top:5px;margin-left:10px">Fixedsys</div>',action:setAction,extData:'Fixedsys,Fixedsys'}
   ,{ name:'<div  style="font-family:Georgia;padding-top:5px;margin-left:10px">Georgia</div>',action:setAction,extData:'Georgia,Georgia'}
   ,{ name:'<div  style="font-family:Tahoma;padding-top:5px;margin-left:10px">Tahoma</div>',action:setAction,extData:'Tahoma,Tahoma'}
   ,{ name:'<div  style="font-family:Verdana;padding-top:5px;margin-left:10px">Verdana</div>',action:setAction,extData:'Verdana,Verdana'}
   ]

function setAction(){
     //取一下权限
  var name = arguments[2].split(',')[1];
  var act = arguments[2].split(',')[0];
  $('actionFont').value = act;
  $('actionNameFont').update(name);
  var objFont = eval("privObjFont." + act);
}

function showFont(event){
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:$('showMeunA') , menuData:menuDataFont, attachCtrl:true }, divStyle);
  menu.show(event);
}
/**
 * 大小
 * @param event
 * @return
 */
var privObjSize = {};
menuDataSize = [{ name:'<div  style="padding-top:5px;margin-left:10px">默认大小</div>',action:setActionSize,extData:'默认大小,默认大小'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">五号</div>',action:setActionSize,extData:'10pt,五号'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">小四</div>',action:setActionSize,extData:'12pt,小四'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">四号</div>',action:setActionSize,extData:'14pt,四号'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">小三</div>',action:setActionSize,extData:'15pt,小三'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">三号</div>',action:setActionSize,extData:'16pt,三号'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">小二</div>',action:setActionSize,extData:'18pt,小二'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">二号</div>',action:setActionSize,extData:'22pt,二号'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">小一</div>',action:setActionSize,extData:'24pt,小一'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">一号</div>',action:setActionSize,extData:'26pt,一号'}
   ]

function setActionSize(){
     //取一下权限
  var name = arguments[2].split(',')[1];
  var act = arguments[2].split(',')[0];
  $('actionSize').value = act;
  $('actionNameSize').update(name);
  //var objSize = eval("privObjSize." + act);
    
}

function showSize(event){
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:$('showMeunB') , menuData:menuDataSize, attachCtrl:true }, divStyle);
  menu.show(event);
}

/**
 * 发光
 */

var privObjLight = {};
menuDataLight = [{ name:'<div  style="padding-top:5px;margin-left:10px">默认效果</div>',action:setActionLight,extData:'默认效果,默认效果'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">投影</div>',action:setActionLight,extData:'1,投影'}
   ,{ name:'<div  style="padding-top:5px;margin-left:10px">发光</div>',action:setActionLight,extData:'2,发光'}
   ]

function setActionLight(){
     //取一下权限
  var color = "";
  var name = arguments[2].split(',')[1];
  
  var act = arguments[2].split(',')[0];
  $('actionLight').value = act;
  if($('actionLight').value == "1"){
    color = window.prompt("请输入投影的颜色", "#000000");
    $('actionLights').value = color;
    $('actionLightFlag').value = "Shadow";
  }else if($('actionLight').value == "2"){
    color = window.prompt("请输入发光的颜色", "#000000");
    $('actionLights').value = color;
    $('actionLightFlag').value = "Glow";
  }
  $('actionNameLight').update(name);
  //var objLight = eval("privObjLight." + act);
    
}

function showLight(event){
  var divStyle = {border:'1px solid #69F',width:'100px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:$('showMeunD') , menuData:menuDataLight, attachCtrl:true }, divStyle);
  menu.show(event);
}

/**
 * 颜色
 * @param event
 * @return
 */
var privObjColor = {};
  menuDataColor = [{ name:LoadForeColorTable('set_font_color')}
   ]

function setActionColor(){
     //取一下权限
  var name = arguments[2].split(',')[1];
  var act = arguments[2].split(',')[0];
  $('actionColor').value = act;
  $('actionNameColor').update(name);
  var objColor = eval("privObjColor." + act);
}

function set_font_color(color){
   document.form1.FONT_COLOR.value = color;
   $('showMeunC').style.color = color;
   $('actionColor').value = color;
   //if(banner_text) banner_text.style.color = color;
   //hideMenu();
}

function LoadForeColorTable(ClickFunc){
  var tColor = "";
  var tRowNum = 8;
  var tColorAry = new Array();
  tColorAry[0]="#000000";tColorAry[1]="#993300";tColorAry[2]="#333300";tColorAry[3]="#003300";
  tColorAry[4]="#003366";tColorAry[5]="#000080";tColorAry[6]="#333399";tColorAry[7]="#333333";

  tColorAry[8]="#800000";tColorAry[9]="#FF6600";tColorAry[10]="#808000";tColorAry[11]="#008000";
  tColorAry[12]="#008080";tColorAry[13]="#0000FF";tColorAry[14]="#666699";tColorAry[15]="#808080";

  tColorAry[16]="#FF0000";tColorAry[17]="#FF9900";tColorAry[18]="#99CC00";tColorAry[19]="#339966";
  tColorAry[20]="#33CCCC";tColorAry[21]="#3366FF";tColorAry[22]="#800080";tColorAry[23]="#999999";

  tColorAry[24]="#FF00FF";tColorAry[25]="#FFCC00";tColorAry[26]="#FFFF00";tColorAry[27]="#00FF00";
  tColorAry[28]="#00FFFF";tColorAry[29]="#00CCFF";tColorAry[30]="#993366";tColorAry[31]="#CCCCCC";

  tColorAry[32]="#FF99CC";tColorAry[33]="#FFCC99";tColorAry[34]="#FFFF99";tColorAry[35]="#CCFFCC";
  tColorAry[36]="#CCFFFF";tColorAry[37]="#99CCFF";tColorAry[38]="#CC99FF";tColorAry[39]="#FFFFFF";

  var tColorTableHTML = '<table cellpadding="0" cellspacing="0" class="ColorTable">';
  tColorTableHTML += '  <tr>';
  for (var ti = 0; ti < tColorAry.length; ti++){
        tColorTableHTML +='    <td onmouseover="this.className=\'Selected\';" onmouseout="this.className=\'\';" onclick="' + ClickFunc + '(\'' + tColorAry[ti] + '\');"';
        if(tColor.toUpperCase() == tColorAry[ti])
           tColorTableHTML +=' class="Selected"';
        tColorTableHTML +='><div style="width:11px;height:11px;background-color:' + tColorAry[ti] + ';"></div></td>';
        if ((ti+1) % tRowNum == 0 && ti+1 != tColorAry.length){
          tColorTableHTML += '  </tr>';
          tColorTableHTML += '  <tr>';
        };
  }; 
  tColorTableHTML += '  </tr>';
  tColorTableHTML += '</table>';
  
  return tColorTableHTML;
}


function showColor(event){
  var divStyle = {border:'1px solid #69F',width:'145px',position :'absolute',backgroundColor:'#FFFFFF',fontSize:'10pt',display:"block"};
  var menu = new Menu({bindTo:$('showMeunC') , menuData:menuDataColor, attachCtrl:true }, divStyle);
  menu.show(event);
}

function showModel(seqId){
  var url = contextPath + "/yh/core/funcs/system/interfaces/act/YHInterFacesAct/showModel.act";
  var rtJson = getJsonRs(url,"seqId=" + seqId);
  if(rtJson.rtState == "0"){
    //bindJson2Cntrl(rtJson.rtData);
    if(rtJson.rtData.attachmentName){
      if(rtJson.rtData.attachmentName != ""){
        $("attachmentNameShow").innerHTML = rtJson.rtData.attachmentName;
      }else{
        $("attachmentNameShow").innerHTML = "";
      }
    }
    if(rtJson.rtData.attachmentName1){
      if(rtJson.rtData.attachmentName1 != ""){
        $("attachmentNameShow1").innerHTML = rtJson.rtData.attachmentName1;
      }else{
        $("attachmentNameShow1").innerHTML = "";
      }
    }
  }else{
    alert(rtJson.rtMrsg);
  }
}



function showPic(attachmentId){
  if(attachmentId){
    $('editPic').style.display = "";
    $('showPic').style.display = "none";
  }else{
    $('showPic').style.display = "";
    $('editPic').style.display = "none";
  }
}

function showPic1(attachmentId1){
  if(attachmentId1){
    $('editPic1').style.display = "";
    $('showPic1').style.display = "none";
  }else{
    $('showPic1').style.display = "";
    $('editPic1').style.display = "none";
  }
}

/**
 * 判断标题样式字体大小
 * @param bannerValue
 * @return
 */

function fontSize(bannerValue){
  var fontSizeValue = "";
  if(bannerValue == "10pt"){
    fontSizeValue = "五号";
  }else if(bannerValue == "12pt"){
    fontSizeValue = "小四";
  }else if(bannerValue == "14pt"){
    fontSizeValue = "四号";
  }else if(bannerValue == "15pt"){
    fontSizeValue = "小三";
  }else if(bannerValue == "16pt"){
    fontSizeValue = "三号";
  }else if(bannerValue == "18pt"){
    fontSizeValue = "小二";
  }else if(bannerValue == "22pt"){
    fontSizeValue = "二号";
  }else if(bannerValue == "24pt"){
    fontSizeValue = "小一";
  }else if(bannerValue == "26pt"){
    fontSizeValue = "一号";
  }else{
    fontSizeValue ="大小";
  }
  $("actionNameSize").innerHTML = fontSizeValue;
  $('actionSize').value = bannerValue;
}

/**
 * 主界面-顶部大标题样式－数据绑定到客户端
 * @param bannerFont
 * @return
 */

function bannerFone(bannerFont){
  var bannerSign = bannerFont.split(';'); 
  for(var i = 0; i < bannerSign.length - 1; i++){
    var bannerStr = bannerSign[i].split(':'); 
    for(var x = 0; x < bannerStr.length; x++){
      var banners = bannerStr[0];
      if(banners == "font-family"){
        if(!bannerStr[1]){
          bannerStr[1] = "字体";
        }
        $("actionNameFont").innerHTML = bannerStr[1];
        $('actionFont').value = bannerStr[1];
      }else if(banners == "font-size"){
        fontSize(bannerStr[1]);
      }else if(banners == "color"){
        //$("actionNameColor").innerHTML = bannerStr[1];
        $("actionColor").value = bannerStr[1];
        $('showMeunC').style.color = bannerStr[1];
      }else if(banners == "filter"){
        var filterValue = "";
        var actFlag = "";
        var fls = bannerStr[1].substr(bannerStr[1].indexOf("color")+6, 7);
        $('actionLights').value = fls;
        if(bannerStr[1].indexOf("Shadow") != -1){
          actFlag = "Shadow";
          filterValue = "投影";
        }else if(bannerStr[1].indexOf("Glow") != -1){
          filterValue = "发光";
          actFlag = "Glow";
        }else{
          filterValue = "效果";
        }
        $("actionNameLight").innerHTML = filterValue;
        $("actionLightFlag").value = actFlag;
        //$('actionLights').value = bannerStr[1];
      }
    }
  }
}

