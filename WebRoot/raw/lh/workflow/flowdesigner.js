var prcsJson = [{prcsId:1,prcsName:'开始',tableId:12,flowType:'start',flowTitle:'开始',fillcolor:'#50A625',leftVml:'21',topVml:'85',prcsTo:'2'}
        ,{prcsId:2,prcsName:'审批',tableId:13,flowType:'',flowTitle:'审批',fillcolor:'#EEEEEE',leftVml:'246',topVml:'299',prcsTo:'1,3,4'}
        ,{prcsId:4,prcsName:'审批2',tableId:16,flowType:'child',flowTitle:'审批2',fillcolor:'#70A0DD',leftVml:'344',topVml:'333',prcsTo:'5'}
        ,{prcsId:5,prcsName:'条件',tableId:17,flowType:'',flowTitle:'条件',fillcolor:'#EEEEEE',leftVml:'344',topVml:'333',prcsTo:'3',condition:'ddddd'}
        ,{prcsId:3,prcsName:'结束',tableId:14,flowType:'end',flowTitle:'结束',fillcolor:'#F4A8BD',leftVml:'454',topVml:'124',prcsTo:'0'}];


function createVml(){
  for(var i = 0 ;i < prcsJson.length ; i++){
    var prcs = prcsJson[i];
    if(prcs.flowType == 'start' || prcs.flowType == 'end'){
      createOval(prcs);
    }else if(prcs.flowType == 'child'){
      createRoundrect(prcs);
      
    }else if(!prcs.flowType){
      if(prcs.condition){
        createDiamond(prcs);
      }else{
        
        createRoundrect(prcs);
      }
    }
    var aPrcsTo = prcs.prcsTo.split(",");
    for(var j = 0 ; j < aPrcsTo.length ; j++){
      var prcsTo = aPrcsTo[j];
      var line = {};
      line.prcsId = prcsTo;
      line.sourceId = prcs.prcsId;
      createLine(line);
    }
  }
  drawLine();
  document.onmousedown = drags;  
  document.onmouseup = nodrags; 
}
function createOval(prcs){
  var sOval = "<vml:oval id="
    + prcs.prcsId +" table_id="
    + prcs.tableId + " flowType='"
    + prcs.flowType + "'  passCount='0'  flowTitle='"
    + prcs.flowTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
    + prcs.fillcolor + "' onDblClick='' style='LEFT:" 
    + prcs.leftVml + "; TOP:" 
    + prcs.topVml + ";WIDTH: 120; POSITION: absolute; HEIGHT: 60;vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title='▲"
    + prcs.flowTitle + "'/>"; 
  var sShadow = "<vml:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
  var sTextbox = "<vml:textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'></vml:textbox>";

  var oval = document.createElement(sOval);
  var shadow = document.createElement(sShadow);
  var textbox = document.createElement(shadow);
  textbox.innerHTML = prcs.prcsName;

  oval.appendChild(shadow);
  oval.appendChild(textbox);
  document.body.appendChild(oval);
}
function createRoundrect(prcs){
  var sRoundrect = "<vml:roundrect inset='2pt,2pt,2pt,2pt' id='"
    + prcs.prcsId +  "' table_id='" 
    + prcs.tableId + "' flowType='"
    + prcs.flowType + "'  passCount='0'  flowTitle='"
    + prcs.flowTitle + "' flowFlag='0'  readOnly='0'  receiverID=''  receiverName=''  fillcolor='"
    + prcs.fillcolor + "' onDblClick='' style='LEFT:"
    + prcs.leftVml + "; TOP:"
    + prcs.topVml +"; WIDTH: 100; POSITION: absolute; HEIGHT: 50;vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:1' arcsize='4321f' coordsize='21600,21600' title='▲"
    + prcs.flowTitle + "'/>";
  var sShadow = "<vml:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
  var sTextbox = "<vml:textbox inset='1pt,2pt,1pt,1pt' onselectstart='return false;'></vml:textbox>";
  
  var roundrect = document.createElement(sRoundrect);
  var shadow = document.createElement(sShadow);
  var textbox = document.createElement(shadow);
  textbox.innerHTML = prcs.prcsName;

  roundrect.appendChild(shadow);
  roundrect.appendChild(textbox);
  document.body.appendChild(roundrect);
}
function createDiamond(prcs){
  var sShapetype = "<vml:shapetype id='type" + prcs.prcsId + "' coordsize='21600,21600' o:spt='4' path='m10800,l,10800,10800,21600,21600,10800xe'/>";
  var sStroke = "<vml:stroke joinstyle='miter'/>";
  var sPath = "<vml:path gradientshapeok='t' o:connecttype='rect' textboxrect='5400,5400,16200,16200'/>";
    
  var shapetype = document.createElement(sShapetype);
  var stroke = document.createElement(sStroke);
  var path = document.createElement(sPath);
  
  var sShape = "<vml:shape type='#type" + prcs.prcsId + "' id='" + prcs.prcsId + "' table_id='" 
    + prcs.tableId + "' flowType='"
    + prcs.flowType + "'  passCount='0'  flowTitle='"
    + prcs.flowTitle + "' flowFlag='0'  readOnly='0' fillcolor='"
    + prcs.fillcolor + "' onDblClick='' style='LEFT: "+ prcs.leftVml +"; TOP:"
    + prcs.topVml +"; WIDTH: 140; POSITION: absolute; HEIGHT: 80;z-index:1;vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;' title='▲"+ prcs.flowTitle + "'/>";
  var sShadow = "<vml:shadow on='T' type='single' color='#b3b3b3' offset='3px,3px'/>";
  var sTextbox = "<vml:textbox inset='0pt,0pt,0pt,0pt' onselectstart='return false;'></vml:textbox>";
  var shape = document.createElement(sShape); 
  var shadow = document.createElement(sShadow);
  var textbox = document.createElement(sTextbox);
  textbox.innerHTML = prcs.prcsName;
  
  shapetype.appendChild(stroke);
  shapetype.appendChild(path);
  document.body.appendChild(shapetype);
  
  shape.appendChild(shadow);
  shape.appendChild(textbox);
  document.body.appendChild(shape);
  
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

var objRect = event.srcElement;
if (event.srcElement.tagName.toLowerCase() == 'textbox') objRect = event.srcElement.parentElement;

var vTagName = objRect.tagName.toLowerCase();
 if (vTagName == 'roundrect' || vTagName == 'shape' || vTagName == 'oval')
 {
     if (event.button == 2) return showContextMenu(event,1);
 }
 else
 {
     if (objRect.tagName.toLowerCase() == 'line')
     {
        //if (event.button == 2) return showContextMenu(event,3);
     }
     else
     {
        if (event.button == 2) return showContextMenu(event,2);
     }
}
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
URL+="&GRAPH=1";
window.open(URL,"set_process","height=700,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}

//新建步骤
function Add_Process()
{
LoadWindow("../view_list/edit.php?FLOW_ID="+flow_id);
}

//编辑步骤属性
function Edit_Process(Process_ID)
{
LoadWindow("../view_list/edit.php?FLOW_ID="+flow_id+"&ID="+Process_ID);
}

function set_item(Process_ID)
{
LoadWindow("../view_list/set_item.php?FLOW_ID="+flow_id+"&ID="+Process_ID);
}

function set_hidden(Process_ID)
{
LoadWindow("../view_list/set_hidden.php?FLOW_ID="+flow_id+"&ID="+Process_ID);
}

function set_condition(Process_ID)
{
LoadWindow("../view_list/set_condition.php?FLOW_ID="+flow_id+"&ID="+Process_ID);
}

function set_user(Process_ID)
{
LoadWindow("../view_list/set_user.php?FLOW_ID="+flow_id+"&ID="+Process_ID);
}

function set_dept(Process_ID)
{
LoadWindow("../view_list/set_dept.php?FLOW_ID="+flow_id+"&ID="+Process_ID);
}

function set_priv(Process_ID)
{
LoadWindow("../view_list/set_priv.php?FLOW_ID="+flow_id+"&ID="+Process_ID);
}

//删除步骤
function Del_Process(Process_ID)
{
msg='确认要删除该步骤么？';
if(window.confirm(msg))
   window.location="../view_list/delete.php?GRAPH=1&FLOW_ID="+flow_id+"&ID="+Process_ID;
}
//克隆步骤
function Clone_Process(Process_ID)
{
msg='确认要克隆该步骤么？';
if(window.confirm(msg))
   window.location="../view_list/clone.php?GRAPH=1&FLOW_ID="+flow_id+"&ID="+Process_ID;
}