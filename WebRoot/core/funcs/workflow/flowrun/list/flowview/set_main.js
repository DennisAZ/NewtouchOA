var prcsJson = [{prcsId:1,prcsName:'开始',flowPrcs:1,prcsTitle:'第1步.开始(刘涵)',leftVml:'21',topVml:'85',prcsTo:'2',state:4,user:[{userName:'ddd',state:4,beginTime:'',endTime:'',timeCount:'timeCount',isOp:true}]}
        ,{prcsId:2,prcsName:'审批',flowPrcs:2,prcsTitle:'第2步.审批(cc)',leftVml:'300',topVml:'85',prcsTo:'3',state:4,user:[{userName:'cc',state:4,beginTime:'',endTime:'',timeCount:'timeCount',isOp:true},{userName:'刘涵',state:4,beginTime:'22',endTime:'23',timeCount:'timeCount',isOp:false}]}
        ,{prcsId:3,prcsName:'审批2',flowPrcs:3,prcsTitle:'第3步.审批2(刘涵)',leftVml:'500',topVml:'85',state:1,user:[{userName:'ddd',state:2,beginTime:'2',endTime:'',timeCount:'',isOp:true}]}];
var requestURL;
function createVml(){
  requestURL = contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct";
  var url = requestURL + "/getPrcsList.act";
  var json = getJsonRs(url , 'flowId=' + flowId + "&runId=" + runId);
  if(json.rtState == '1'){
    $('content').hide();
    $('message').update(json.rtMsrg);
    $('message').show();
    return ;
  }
  document.title = '流程图-'+ json.rtData.runMsg.flowName +'-('+ runId +')-' + json.rtData.runMsg.runName;
  $('runMsg').update(json.rtData.runMsg.runName + " 流水号：" + runId);
  if (json.rtData.timeToId) {
    $('timeToId').value = json.rtData.timeToId;
  }
  prcsJson = json.rtData.prcsList;
  for(var i = 0 ;i < prcsJson.length ; i++){
    var prcs = prcsJson[i];
    createRoundrect(prcs);
  }
  drawLine();
  document.onmousedown = drags;  
  document.onmouseup = nodrags; 
}
function createRoundrect(prcs1){
  var list = prcs1.list;
  for (var j = 0 ;j < list.length ;j++) {
    var prcs = list[j];
    var fillcolor = "";
    switch (prcs.state)
    {
      case 1:
        fillcolor = "#FFBC18";
        break;
      case 2:
        fillcolor = "#50C625";
        break;
      case 3:
        fillcolor = "#F4A8BD";
        break;
      case 4:
        fillcolor = "#F4A8BD";
        break;
      case 5:
        fillcolor = "#D7D7D7";
        break;
    }
    var title =  "<b>" + prcs.prcsName + "</b><br>";
    //{userName:'ddd',state:,beginTime:'',endTime:'',timeUsed:'',isOp:true}
    for(var i = 0 ; i < prcs.user.length ;i++){
      var user = prcs.user[i];
      var prcsUserName = "";
      if(user.isOp){
        prcsUserName = '<span style="text-decoration:underline;font-weight:bold;color:red">'+ user.userName +' 主办</span>';
      }else{
        prcsUserName = '<span style="text-decoration:underline;font-weight:bold">'+ user.userName +'</span>';
      }
     
      var state = user.state;
      if(state == 1){
        title += "<img src='"+ imgPath +"/email_close.gif'  align='absmiddle'/>" + prcsUserName + "[<font color=green>未接收办理</font>]";
      //办理中    
      }else if(state == 2){
        title += "<img src='"+ imgPath +"/email_open.gif'  align='absmiddle'/>" + prcsUserName + "[<font color=green>办理中,已用时：" + user.timeUsed + "</font>]";
        
        if(user.timeOutFlag){
          title += '<br> <span style="color:red">限时'+ user.timeOut +"小时," + user.timeUsed + '</span>';
        }
        title += "<br> 开始于：" + user.beginTime;
      //已转交下步    
      }else if(state == 3){
        title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>"+ prcsUserName +"[<font color=green>已转交下步,用时：" + user.timeUsed + "</font>]";
        title += "<br> 开始于：" + user.beginTime;
        if(user.deliverTime){
          title += "<br> 结束于：" + user.deliverTime;
        }
        //已办结    
      }else if(state == 4){
        title += "<img  src='"+ imgPath +"/flow_next.gif' align='absmiddle'>"+ prcsUserName +"[<font color=green>已办结,用时：" + user.timeUsed + "</font>]";
        title += "<br> 开始于：" + user.beginTime;
        if(user.deliverTime){
          title += "<br> 结束于：" + user.deliverTime;
        }
      }else if(state == 5){
        title += prcsUserName + "[预设经办人]";
      }
      title += "<br><br>";
    }
    var dbclick = "";
    if (prcs.childRun != '0' && prcs.childFlowId != '0') {
      dbclick = "flowView("+prcs.childRun+" , "+prcs.childFlowId+")";
    }
    var sRoundrect = "<vml:roundrect inset='2pt,2pt,2pt,2pt' id='"
      + prcs.sourceId +  "' flowType='"
      + prcs.flowType + "' passCount='0' flowTitle='"
      + prcs.prcsTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
      + fillcolor + "' onDblClick='"+dbclick+"' style='LEFT:"
      + prcs.leftVml + "; TOP:"
      + prcs.topVml +"; WIDTH: 100; POSITION: absolute; HEIGHT: 50;vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title=''/>";
    var sShadow = "<vml:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
    var sTextbox = "<vml:textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'></vml:textbox>";
    var roundrect = document.createElement(sRoundrect);
    var shadow = document.createElement(sShadow);
    var textbox = document.createElement(sTextbox);
    roundrect.appendChild(shadow);
    roundrect.appendChild(textbox);
    textbox.innerHTML = prcs.prcsTitle ;
    document.body.appendChild(roundrect);
    new Tooltip(roundrect,{width:300,backgroundColor:'#FAFCFD', // Default background color
      borderColor:'#000', // Default border color
      textColor:'#000', // Default text color (use CSS value)
      maxWidth:250, // Default tooltip width
      mouseFollow:true}, title);
    var prcsTo = prcs.prcsTo;
    for (var b=0;b<prcsTo.length;b++) {
      var ss = prcsTo[b];
      if (ss){
        var line = {};
        line.sourceId = prcs.sourceId;
        line.prcsId = ss;
        createLine(line);
      }
    }
  }
}

function createLine(line){
  var sLine = "<vml:line mfrID='"+line.prcsId+"' title='' source='"+line.sourceId+ "' object='"+line.prcsId+"' from='0,0' to='0,0' style='position:absolute;display:none;z-index:2' arcsize='4321f' coordsize='21600,21600'/>"
  var sStroke = "<vml:stroke endarrow='block'></vml:stroke>";
  var sShadow = "<vml:shadow on='T' type='single' color='#b3b3b3' offset='1px,1px'/>";
  
  var line = document.createElement(sLine);
  var stroke = document.createElement(sStroke);
  var shadow = document.createElement(sShadow);
  
  line.appendChild(stroke);
  line.appendChild(shadow);
  document.body.appendChild(line);
}

var dragapproved = false;
var eventsource,x,y;
var popeventsource = "";
var temp1 = 0;
var temp2 = 0;

function nocontextmenu()
{
 event.cancelBubble = true
 event.returnValue = false;

 return false;
}

function nodrags()
{
   dragapproved = false;
}

function move()
{
  if (event.button == 1 && dragapproved)
  {
    var newleft = temp1 + event.clientX - x;
    var newtop = temp2 + event.clientY - y;
    eventsource.style.pixelLeft = newleft;
    eventsource.style.pixelTop = newtop;

    drawLine();
    return false;
  }
}

function drags()
{
  if (event.button != 1)
     return;

  var objRect = event.srcElement;
  if (event.srcElement.tagName.toLowerCase() == 'textbox') objRect = event.srcElement.parentElement;

  if ((objRect.tagName == 'roundrect' || objRect.tagName == 'shape' || objRect.tagName == 'oval') && (!event.ctrlKey))
  {
    dragapproved = true;
    eventsource = objRect;
    temp1 = eventsource.style.pixelLeft;
    temp2 = eventsource.style.pixelTop;
    x = event.clientX;
    y = event.clientY;
    document.onmousemove = move;
  }
}

function drawLine()
{
  var source;
  var object;
  var sourceObj;
  var objectObj;
  var x0,y0,x1,y1;
  var p0,p1;
  var a = document.getElementsByTagName('line');
  for(var i = 0; i < a.length; i++){
    source = a[i].getAttribute('source');
    object = a[i].getAttribute('object');
    
    if((source != null) && (object != null)){
      sourceObj = document.getElementById(source);
      objectObj = document.getElementById(object);
       
      if ((sourceObj == null) || (objectObj == null)) continue;

      if(sourceObj.style.pixelLeft > objectObj.style.pixelLeft){
        if((sourceObj.style.pixelLeft - objectObj.style.pixelLeft) <= objectObj.style.pixelWidth){
          x0 = sourceObj.style.pixelLeft + sourceObj.style.pixelWidth / 2;
          x1 = objectObj.style.pixelLeft + objectObj.style.pixelWidth / 2;
          if(sourceObj.style.pixelTop >  objectObj.style.pixelTop){
            y0 = sourceObj.style.pixelTop;
            y1 = objectObj.style.pixelTop  + objectObj.style.pixelHeight;
          }else{
            y0 = sourceObj.style.pixelTop + sourceObj.style.pixelHeight;
            y1 = objectObj.style.pixelTop;
          }
        }else{
          x0 = sourceObj.style.pixelLeft;
          x1 = objectObj.style.pixelLeft + objectObj.style.pixelWidth;
          y0 = sourceObj.style.pixelTop + sourceObj.style.pixelHeight / 2;
          y1 = objectObj.style.pixelTop + objectObj.style.pixelHeight / 2;
        }
       }else{
         if((objectObj.style.pixelLeft - sourceObj.style.pixelLeft) <= objectObj.style.pixelWidth){
           x0 = sourceObj.style.pixelLeft + sourceObj.style.pixelWidth / 2;
           x1 = objectObj.style.pixelLeft + objectObj.style.pixelWidth / 2;
           if (sourceObj.style.pixelTop >  objectObj.style.pixelTop){
             y0 = sourceObj.style.pixelTop;
             y1 = objectObj.style.pixelTop  + objectObj.style.pixelHeight;
           }else{
             y0 = sourceObj.style.pixelTop + sourceObj.style.pixelHeight;
             y1 = objectObj.style.pixelTop;
           }
         }else{
           x0 = sourceObj.style.pixelLeft + sourceObj.style.pixelWidth;
           x1 = objectObj.style.pixelLeft;
           y0 = sourceObj.style.pixelTop + sourceObj.style.pixelHeight / 2;
           y1 = objectObj.style.pixelTop + objectObj.style.pixelHeight / 2;
         }
       }

       a[i].from = String(x0) + ',' + String(y0);
       a[i].to = String(x1) + ',' + String(y1);

       a[i].style.pixelLeft = x0 + 'px';
       a[i].style.pixelTop = y0 + 'px';

       strIF = a[i].getAttribute('title');
       if((strIF != null) && (strIF != '')){
         var id = 'if_' + source + '_' + object;
         var obj = document.getElementById(id);
        
         var left = (x0 + (x1 - x0) / 2 - 30);
         var top = (y0 + (y1 - y0) / 2 - 15);

         if(obj != null){
           obj.style.pixelLeft = left + 'px';
           obj.style.pixelTop = top + 'px';
        
           obj.style.left = left + 'px';
           obj.style.top = top + 'px';
        
           obj.style.display = '';
         }
       }
       a[i].style.display = '';
     }
  }
}


//形成菜单行
function getMenuRow(s_Event, s_Html) {
var s_MenuRow = "";
s_MenuRow = "<tr><td align=center valign=middle nowrap><TABLE border=0 cellpadding=0 cellspacing=0 width=132><tr><td nowrap valign=middle height=20 class=MouseOut onMouseOver=this.className='MouseOver'; onMouseOut=this.className='MouseOut';";
s_MenuRow += " onclick=\"parent."+s_Event+";parent.oPopupMenu.hide();\"";
s_MenuRow += ">&nbsp;";
s_MenuRow += s_Html+"<\/td><\/tr><\/TABLE><\/td><\/tr>";
return s_MenuRow;
}


//-- 右键菜单 --
var sMenuHr = "<tr><td align=center valign=middle height=2><TABLE border=0 cellpadding=0 cellspacing=0 width=128 height=2><tr><td height=1 class=HrShadow><\/td><\/tr><tr><td height=1 class=HrHighLight><\/td><\/tr><\/TABLE><\/td><\/tr>";
var sMenu1 = "<TABLE onmousedown='if (event.button==1) return true; else return false;' border=0 cellpadding=0 cellspacing=0 class=Menu width=150><tr><td width=18 valign=bottom align=center style=''><\/td><td width=132 class=RightBg><TABLE border=0 cellpadding=0 cellspacing=0>";
var sMenu2 = "<\/TABLE><\/td><\/tr><\/TABLE>";
var oPopupMenu = null;
oPopupMenu = window.createPopup();

function showContextMenu(event,type)
{
var style = "";
style = "BODY {margin:0px;border:0px}";
style += " TD {font-size:9pt;font-family:宋体,Verdana,Arial}";
style += " TABLE.Menu {border-top:window 1px solid;border-left:window 1px solid;border-bottom:buttonshadow 1px solid;border-right:buttonshadow 1px solid;background-color:#0072BC}";
style += "TD.RightBg {background-color:buttonface}";
style += "TD.MouseOver {background-color:highlight;color:highlighttext;cursor:default;}";
style += "TD.MouseOut {background-color:buttonface;color:buttontext;cursor:default;}";
style += "TD.HrShadow {background-color:buttonshadow;}";
style += "TD.HrHighLight {background-color:buttonhighlight;}";
style = "<style>" + style + "</style>";

var width = 150;
var height = 0;
var lefter = event.clientX;
var topper = event.clientY;

var oPopDocument = oPopupMenu.document;
var oPopBody = oPopupMenu.document.body;

//object
var objRect = event.srcElement;
if (event.srcElement.tagName.toLowerCase() == 'textbox')
   objRect = event.srcElement.parentElement;

var Process_ID =  objRect.getAttribute('table_id');

var flowType = objRect.getAttribute('flowType');

var sMenu = style;

switch(type)
{
case 1:

 sMenu += getMenuRow("Edit_Process("+Process_ID+")", "步骤基本属性");
 height += 20;
 
 if (flowType != 'child')
 {
 sMenu += getMenuRow("set_user("+Process_ID+")", "经办权限");
 height += 20;

 sMenu += getMenuRow("set_item("+Process_ID+")", "可写字段");
 height += 20;
 
 sMenu += getMenuRow("set_hidden("+Process_ID+")", "保密字段");
 height += 20;
 }

 sMenu += getMenuRow("set_condition("+Process_ID+")", "条件设置");
 height += 20;

 sMenu += sMenuHr;
 height += 2;

 sMenu += getMenuRow("Clone_Process("+Process_ID+")", "克隆该步骤");
 height += 20;

 sMenu += getMenuRow("Del_Process("+Process_ID+")", "删除该步骤");
 height += 20;
 
 break;

case 2:

 sMenu += getMenuRow("Add_Process()", "新建步骤");
 height += 20;

 sMenu += sMenuHr;
 height += 2;

 sMenu += getMenuRow("SavePosition()", "保存布局");
 height += 20;

 sMenu += getMenuRow("Refresh()", "刷新视图");
 height += 20;

 break;
/*
case 3:
 var fid =  objRect.getAttribute('mfrID');
 sMenu += getMenuRow("DelFlowRun(" + fid + ")",  "删除连线");
 height += 20;
 break;
*/
}

sMenu = sMenu1 + sMenu + sMenu2;

height += 2;
if (lefter+width > document.body.clientWidth) lefter = lefter - width + 2;
if (topper+height > document.body.clientHeight) topper = topper - height + 2;

oPopupMenu.document.body.innerHTML = sMenu;
oPopupMenu.show(lefter, topper, width, height,document.body);

return false;
}

//-- 鼠标右击 --
function DoRightClick()
{
pub_x = event.clientX;
pub_y = event.clientY;

SetSel();

}


//-- 选择步骤 --
function SetSel()
{
var vml;
var flowType = '';
var flowID = 0;
var passCount = 0;
var flowColor = '';
var strStart="#50A625";
var strEnd="#F4A8BD";
var strChild="#70A0DD";
var strOut="#EEEEEE";
var strSelect ="#8E83F5";

vml= document.getElementsByTagName('oval');
for (var i = 0; i < vml.length; i++)
{
   flowType = vml[i].getAttribute('flowType');
   if (flowType == 'start')
      flowColor = strStart;
   else if (flowType == 'end')
      flowColor = strEnd;
   vml[i].fillcolor = flowColor;
}
           
vml = document.getElementsByTagName('roundrect');
for (var i = 0; i < vml.length; i++)
{
   flowType = vml[i].getAttribute('flowType');
   if (flowType == 'child')
      flowColor = strChild;
   else
      flowColor = strOut;
   vml[i].fillcolor = flowColor;
}

vml = document.getElementsByTagName('shape');
for (var i = 0; i < vml.length; i++)
{
   flowType = vml[i].getAttribute('flowType');
   if (flowType == 'child')
      flowColor = strChild;
   else
      flowColor = strOut;
   vml[i].fillcolor = flowColor;
}

var objRect = event.srcElement;
if(event.srcElement.tagName.toLowerCase() == 'textbox')
   objRect = event.srcElement.parentElement;

//步骤类型
//flowType = objRect.getAttribute('flowType');
try { if (objRect.tagName == 'roundrect' || objRect.tagName == 'oval' || objRect.tagName == 'shape') objRect.fillcolor = strSelect; } catch(e){}
}


//-- 删除流程线 --
function SetSqlDelFlow(fid)
{
var strSql = '';
strSql = "delete from office_missive_flow_run where office_missive_flow_run_id='" + fid + "' ";
document.all('tbSQL').value += strSql;
}

//-- 保存布局 --
function getSql(obj)
{
var strSql = '';
var mf_pixel_left = 0;
var mf_pixel_top = 0;   
for (var i = 0; i < obj.length; i++)
{
   table_id = eval(obj[i].getAttribute('table_id'));
   mf_pixel_left = obj[i].style.pixelLeft;
   mf_pixel_top = obj[i].style.pixelTop;

   if (table_id > 0)
   {
      strSql += "SET_LEFT=" + mf_pixel_left + ",SET_TOP=" + mf_pixel_top + " where ID="+table_id+";";
   }
}
return strSql;
}

function SavePosition()
{
var strSql = '';
a = document.getElementsByTagName('roundrect');
b = document.getElementsByTagName('shape');
c = document.getElementsByTagName('oval');

strSql = getSql(a)+getSql(b)+getSql(c);

document.form1.SET_SQL.value += strSql;
document.form1.submit();
}

//-- 删除流程线 --
function DelFlowRun(fid)
{
if ((fid == null) || (fid == 0)) return;

SavePosition();
SetSqlDelFlow(fid);

document.all('btnSave').click();
}

//-- 刷新 --
function Refresh()
{
location.href = location.href;
}

function LoadWindow(URL)
{
loc_x=(screen.availWidth-700)/2;
loc_y=(screen.availHeight-700)/2;
window.open(URL,"set_process","height=700,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}

//新建步骤
function Add_Process()
{
LoadWindow("viewlist/setproperty/index.jsp?flowId="+flowId);
}

//编辑步骤属性
function Edit_Process(seqId)
{
LoadWindow("viewlist/setproperty/index.jsp?flowId="+flowId+"&seqId="+seqId);
}

function set_item(seqId)
{
LoadWindow("viewlist/setField/setField.jsp?flowId="+flowId+"&seqId="+seqId);
}

function set_hidden(seqId)
{
LoadWindow("viewlist/setField/setHiddenField.jsp?flowId="+flowId+"&seqId="+seqId);
}

function set_condition(seqId)
{
LoadWindow("viewlist/setCondition/setCondition.jsp?flowId="+flowId+"&seqId="+seqId);
}

function set_user(seqId)
{
LoadWindow("viewlist/setPriv/setOperatePriv.jsp?flowId="+flowId+"&seqId="+seqId);
}

function set_dept(seqId)
{
  LoadWindow("viewlist/setPriv/setOperatePriv.jsp?flowId="+flowId+"&seqId="+seqId);
}

function set_priv(seqId)
{
  LoadWindow("viewlist/setPriv/setOperatePriv.jsp?flowId="+flowId+"&seqId="+seqId);
}

//删除步骤
function Del_Process(seqId)
{
  msg='确认要删除该步骤么？';
  if(window.confirm(msg)){
    var url = requestURL + "/delProc.act";
    var json = getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
      location.reload();
    }else{
      alert(json.rtMsrg); 
    }
  }
}
//克隆步骤
function Clone_Process(seqId)
{
  msg='确认要克隆该步骤么？';
  if(window.confirm(msg)){
    var url = requestURL + "/cloneProc.act";
    var json = getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
      location.reload();
    }else{
      alert(json.rtMsrg); 
    }
  }
}