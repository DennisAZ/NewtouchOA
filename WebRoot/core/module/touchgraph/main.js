var centerNode = null;
var centerColor = "#fff";
var selectedNode = "";

var a = 0 ;
var group = null;
var colors = ["#ffe600","#c76968","#bed742","#7bbfea","#73b9a2","#ffce7b","#ffc20e","#fcf16e","#df9464","#fab27b","#f7acbc","#f8aba6","#f3704b","#afdfe4","#c77eb5","#9b95c9","#426ab3"];
var colors2 = ["#f0dc70","#bb505d","#7fb80e","#33a3dc","#00ae9d","#fcaf17","#dea32c","#decb00","#b76f40","#faa755","#ef5b9c","#f05b72","#f15a22","#008792","#ea66a6","#6950a1","#102b6a"];
//{"Term":"北京大学","Count":13}
function doInit(){
  group = $('group1');
  width = document.body.offsetWidth ;
  height = document.body.offsetHeight - $('flash').offsetHeight - 4;
  setTimeDiv();
  if (width > height) {
    a = height;
  } else {
    a = width;
  } 
  showTouchGraph();
  loadLeft();
  window.onresize = setTimeDiv; 
  jQuery('#group1').draggable();
}
function setTimeDiv() {
  $('timeDiv').style.top = document.body.offsetHeight - $('timeDiv').offsetHeight - 4 + "px";
}
function getDefaultSize() {
  var defaultSize = 2160 - a;
  return defaultSize;
}
function createOval(node , point , isCenter , isLeft) {
  var len1 = 100;
  var len2 = 60;
  var len3 = 80;
  var id = node.Term;
  var handler = "clickHandler(event,\""+id+"\");";
  var dbHandler = "dbClickHandler(event,\""+id+"\");";
  if (isLeft) {
    dbHandler = "";
    handler = "clickSubjectHandler(event,\""+id+"\");";
  }
  if (isCenter) {
    handler = "cClickHandler(event,\""+id+"\");";
    if (isLeft) {
      handler = ""; 
    }
    dbHandler = "cDbClickHandler(event,\""+id+"\");";
  } 
  if (point.centerY < 0 ) {
    point.centerY = 0;
  }
  if (point.centerX < 0 ) {
    point.centerX = 0;
  }
  //var fillcolor = getColor();
  var i =  Math.random(); 
  var index = Math.ceil( i * colors.length ) - 1;
  var fillcolor = colors[index];
  if (isCenter) {
    centerColor = fillcolor; 
  }
  var fillcolor2 = colors2[index];
  var str =  node.Term ;
  if (!str) {
    str = node.Term;
  }
  var point2 = getOvalPoint(point , len1, len2);
  var point3 = getOvalPoint(point , len1, len3 , true);
  var sOval = "<vml:oval id=" + id 
    + " style='LEFT:" 
    + point.centerX + "; TOP:" 
    + point.centerY + ";filter:alpha(opacity=90,style=2);WIDTH:"+len1+";POSITION: absolute; HEIGHT:"+len1+";vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:2;'   fillcolor=\""+fillcolor+"\" stroked='f'  title='▲"
    + str + "'/>";
  
  var sOval2 = "<vml:oval id='oval-" + id 
    + "'  onDblClick='"+dbHandler+"' onClick='"+ handler +"' style='LEFT:" 
    + point2.centerX + "; TOP:" 
    + point2.centerY + ";display:none;WIDTH:"+len2+";POSITION: absolute; HEIGHT:"+len2+";vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:3;'   strokeweight=\"2px\" strokecolor=\""+fillcolor2+"\" fillcolor=\""+fillcolor+"\" stroked='t'/>";
  
  var sOval3 = "<vml:oval id='oval2-" + id 
  + "'  onDblClick='"+dbHandler+"' onClick='"+ handler +"' style='LEFT:" 
  + point3.centerX + "; TOP:" 
  + point3.centerY + ";display:none;WIDTH:"+len3+";POSITION: absolute; HEIGHT:"+len3+";vertical-align:middle;cursor:pointer;TEXT-ALIGN:center;z-index:3;'  strokeweight=\"2px\" strokecolor=\""+fillcolor2+"\"  fillcolor=\""+fillcolor+"\" stroked='t'/>";
  
  var sTextbox = "<vml:textbox inset='1pt,2pt,1pt,1pt' style='vertical-align:middle;line-height:"+(len2/2 )+"px' onselectstart='return false;'></vml:textbox>";
  var oval = document.createElement(sOval);
  var oval2 = document.createElement(sOval2);
  var oval3 = document.createElement(sOval3);
  
  var textbox = document.createElement(sTextbox);
  var textbox2 = document.createElement(sTextbox);
  
  oval2.appendChild(textbox);
  oval3.appendChild(textbox2);
  textbox.innerHTML =  "<b id='nameText-"+id+"' style='color:#fff'>" + node.Term + "</b>";
  textbox2.innerHTML =  "<b id='nameText-"+id+"' style='color:#fff'>" + node.Term + "</b>";
  group.appendChild(oval);
  group.appendChild(oval2);
  group.appendChild(oval3);
  register(oval2 , oval3);
  oval2.style.display = "";
}
function getOvalPoint(point , len1 , len2 , flag) {
  var point2 = {};
  var len = len1 - len2;
  var x = Math.sin(45) * len;
  if (!flag) {
    point2.centerX = point.centerX + x - 13 ;
    point2.centerY = point.centerY + x - 13;
  } else {
    point2.centerX = point.centerX + x - 5 ;
    point2.centerY = point.centerY + x - 5;
  }
  
  return point2;
}
function register(oval2 , oval3) {
  oval2.onmouseover = function () {
    this.style.display = "none";
    oval3.style.display = "";
  }
  oval3.onmouseout = function () {
    this.style.display = "none";
    oval2.style.display = "";
  }
}
function closeDiv(e){
  var div = $("msgDiv");
  if (div){
    document.body.removeChild(div) ;
  }
}
function getColor() {
  var str = "123456789abcdef"; 
  var t = "#"; 
  for(j=0;j<6;j++) 
  {t = t+ str.charAt(Math.random()*str.length);} 
  return t;
}
function createLine(line , relation , name){
  if (!relation){
    relation = "";
  }
  var i =  Math.random(); 
  var index = Math.ceil( i * colors.length ) - 1;
  var fillcolor = colors[index];
  
  var sLine = "<vml:line fillColor='"+fillcolor+"' mfrID='"+line.toId+"' title='"+relation+"' source='"+line.sourceId+ "' object='"+line.toId+"' from='0,0' to='0,0' style='position:absolute;display:none;z-index:2;cursor:pointer;' arcsize='4321f' coordsize='21600,21600'/>"
  var sStroke = "<vml:stroke endarrow='block'></vml:stroke>";
  var sShadow = "<vml:shadow on='T' type='single' color='#b3b3b3' offset='1px,1px'/>";
  
  var line = document.createElement(sLine);
  var shadow = document.createElement(sShadow);
  
  line.appendChild(shadow);
  line.onmouseover = function() {
    line.strokeWeight = '3px';
  }
  line.onmouseout = function() {
    line.strokeWeight = '1px';
  }
  var text = "<div style='padding-left:3px;color:#000'><b>" + centerNode.Term + "," + name + "</b></div><div style='padding-right:3px;padding-left:3px;padding-top:3px;color:#333'>"+relation+"</div>"
  var c = new Container({bindToLine:line , relation: text});
  group.appendChild(line);
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
function drawLine() {
  var centerObj = document.getElementById(centerNode.Term);
  var sourcePoint = getElCoordinate(centerObj);
  var a = document.getElementsByTagName('line');
  var objs = [];
  
  for(var i = 0; i < a.length; i++){
    var object = a[i].getAttribute('object');
    objectObj = document.getElementById(object);
    if (objectObj == null) continue;
    
    var objectPoint = getElCoordinate(objectObj);
    objs.push(objectPoint);
  }
  for(var i = 0; i < objs.length; i++){
    var objectPoint = objs[i];
    a[i].from =  (sourcePoint.left + 100/2) + "," + (sourcePoint.top);
    a[i].to =  (objectPoint.left + 100/2) + "," + (objectPoint.top );
    a[i].style.display = '';
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

var objRect = event.srcElement;
if (event.srcElement.tagName.toLowerCase() == 'textbox')
   objRect = event.srcElement.parentElement;

var userId = objRect.getAttribute("id");

var sMenu = style;

switch(type)
{
case 1:

 sMenu += getMenuRow("empty()", "无相关信息");
 height += 20;
//
// sMenu += getMenuRow("alert("+userId+")", "相关信息");
// height += 20;
 
 break;

case 2:


 sMenu += getMenuRow("Refresh()", "刷新视图");
 height += 20;
 
 sMenu += getMenuRow("Refresh()", "刷新视图");
 height += 20;
 
 break;
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
   if (event.button == 2) {
     return showContextMenu(event,1);
   }
 } else {
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
//步骤类型
//flowType = objRect.getAttribute('flowType');
try { if (objRect.tagName == 'roundrect' || objRect.tagName == 'oval' || objRect.tagName == 'shape') objRect.fillcolor = strSelect; } catch(e){}
}

//-- 刷新 --
function Refresh()
{
location.href = location.href;
}

function showDiv(event,sourceId , toId) {
  var div = new Element("div");
  with(div){
    id = "msgDiv";
    style.backgroundColor = "#F2FDDB";
    style.border = "1px solid #ADCD3C";
    style.width = "150px";
    style.position = 'absolute';
    style.zIndex = '101';
  }
  var point = mousePosition(event);
  div.style.left = point.x + "px";
  div.style.top = point.y + "px";
  document.body.appendChild(div);
  div.onclick = function(event){
    if (window.event) {
      window.event.cancelBubble=true;
    } else {
      event.stopPropagation();
    } 
  }
  var temp="";
  div.update(temp);
  document.body.onclick = closeDivHandler;
}

function closeDivHandler(e){
   var node = $("msgDiv");
   if (node) {
     document.body.removeChild(node) ;
   }
}

function mousePosition(ev){
  if(!ev) ev=window.event;
    if(ev.pageX || ev.pageY){
      return {x:ev.pageX, y:ev.pageY};
  }
  return {
    x:ev.clientX + document.documentElement.scrollLeft - document.body.clientLeft,
    y:ev.clientY + document.documentElement.scrollTop  - document.body.clientTop
  };
}
function getElCoordinate(dom) {
  var t = dom.offsetTop;
  var l = dom.offsetLeft;
  dom=dom.offsetParent;
  while (dom) {
    t += dom.offsetTop;
    l += dom.offsetLeft;
  dom=dom.offsetParent;
  }; return {
    top: t,
    left: l
  };
}
function empty() {
}

/**处理事件**/
function clickHandler(event, id) {
  if (event.ctrlKey) {
    var ids = selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      selectedNode = ids;
    }
  } else {
    showTouchGraph(id);
  }
}
function clickSubjectHandler(event , id) {
  loadRight(id);
}
function dbClickHandler(event, id) {
  if (event.ctrlKey) {
    var ids = childWindow.selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      selectedNode = ids;
    }
    showTouchGraph(selectedNode);
  　　//取得最新的
  }
}
function cDbClickHandler(event, id) {
  
}
function cClickHandler(event, id) {
  if (event.ctrlKey) {
    var ids = selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      selectedNode = ids;
    }
  }
}

function showTouchGraph( id , url , isLeft , subject) {
  group.update("");
  group.style.top = "48px";
  group.style.left = "0px";
  group.style.width = a + "px";
  group.style.height = a + "px";
  group.coordsize = a + "," + a;
  if (getSwf("setGroup").setGroupValue) {
    getSwf("setGroup").setGroupValue(getDefaultSize());
  } 
  if (!id) {
    id = searchData; 
  }
  if (!url) {
    url = "http://192.168.0.126:9000/BjfaoWeb/FullText/GetRelationWords";
  }
  document.body.style.cursor = "wait";
  var json = getJsonRs(url , "q=" +  encodeURIComponent(id));
  if (json.rtState == '1'){
    return ; 
  }
  document.body.style.cursor = "";
  nodes = json.rtData;
  var nodes2 = new Array();
  for (var i = 0 ;i < nodes.length ; i++) {
    var str = nodes[i];
    var obj = {Term:str , Count : 30};
    nodes2.push(obj);
  }
  nodes = nodes2;
  centerNode = {Term:id};
  var point = {centerX: a / 2 + ( width - height - 100) / 2 , centerY:a / 2 - 100};
  var count = nodes.length;
  var degress = 360 / count;
  var nowMax = 0;
  var nowMin = 0;
  for (var i = 0 ;i < count ; i++) {
    var tmp = nodes[i];
    if (centerNode.Term == tmp.Term) {
      continue;
    }
    if (nowMax < tmp.Count) {
      nowMax = tmp.Count;
    }
    if (nowMin > tmp.Count) {
      nowMin = tmp.Count;
    }
  }
  for(var i = 0 ;i < count ; i++){
    var tmp = nodes[i];
    if (centerNode.Term == tmp.Term) {
      continue;
    }
    var countTmp = getCountTmp(nowMin , nowMax ,tmp.Count )
    var y = Math.sin(degress * (i + 1)) * countTmp;
    var x = Math.cos(degress * (i + 1)) * countTmp;
    y += point.centerY;
    x += point.centerX;
    var pointTmp = {centerX : x , centerY : y};
    createOval(tmp , pointTmp, false , isLeft);
    var line = {};
    line.toId =tmp.Term;
    line.sourceId = centerNode.Term ;
    createLine(line , tmp.Term , tmp.Term);
  }
  createOval(centerNode , point , true);
  drawLine();
  loadRight(subject);
}
function getCountTmp(minCount , maxCount , countTmp) {
  var y = maxCount - minCount;
  var x = 500 - 100 ;
  var z = countTmp - minCount;
  countTmp = (x/y * z) + 100 ;
  return countTmp;
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
/****************/

/***左边*****/

function loadLeft() {
  var nodes = [{value:"文种"},{value:"业务行为"},{value:"相关人名"},{value:"相关地名"},{value:"组织机构"}];
  for (var i = 0 ;i < nodes.length;i ++) {
    var node = nodes[i];
    addLeftRow(node , i);
  }
}
function addLeftRow(node , i) {
  var tr = new Element("tr");
  var td = new Element("td");
  tr.appendChild(td);
  $("list").appendChild(tr);
  var checkbox = new Element("input");
  checkbox.type = "checkbox";
  checkbox.value = node.value;
  td.style.cursor = "hand";
  td.style.color = "#ff9c00";
  td.onmouseover = function() {
    td.style.backgroundColor = "#333";
  }
  td.onmouseout = function() {
    td.style.backgroundColor = "";
  }
  td.onclick = function() {
    checkbox.checked = true;
    if (checkbox.checked) {
      setOtherDis(checkbox);
      getAllNodes(checkbox.value);
    }
  }
  td.appendChild(checkbox);
  td.insert(node.value);
}
function setOtherDis(selected) {
  var checkbox = document.getElementsByTagName("input");
  for (var i = 0 ;i < checkbox.length;i ++) {
    var c = checkbox[i];
    if (c != selected ) {
      c.checked = false;
    }
  }
}
function getAllNodes(subject) {
  var url = contextPath + "/yh/subsys/inforesouce/act/YHTouchGraphAct/getSubject.act";
  var id = centerNode.Term;
  showTouchGraph( id + "&subject=" + encodeURIComponent(subject) , url , true , subject);
}
function comeBack() {
  setOtherDis();
  showTouchGraph(centerNode.Term);
}
/***左边*****/

