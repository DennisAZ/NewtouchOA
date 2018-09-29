/**
 * 加载AIP数据
 */
function loadAIP() {
  var rtJson = getJsonRs(contextPath + "/test/core/act/YHAIPAct/loadAip.act", {T_NAME: "1"});
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg);
    return false;
  }
}

function addField()
{
  var obj = $("HWPostil1");
  var vRet = obj.Login( _userName , 5, 32767, "", "");
  if(0 == vRet) {
    obj.InDesignMode = true;
  }
}
function NotifyLineAction(lPage,lStartPos,lEndPos)
{
  if (!isOpenPropWindow) {
    var lStartY = (lStartPos>>16)& 0x0000ffff;
    var lStartX = ((lStartPos<<16)>>16) & 0x0000ffff; 

    var lEndY = (lEndPos>>16)& 0x0000ffff;
    var lEndX = ((lEndPos<<16)>>16) & 0x0000ffff; 
    //alert(lStartX);alert(lStartY);
    openWin();
    argObj = {"page":0,"StartX":lStartX,"StartY":lStartY,"EndX":lEndX,"EndY":lEndY};
  }
}
var fieldIndex = 0;
function setField(field_name
    ,field_type
    ,font_family
    ,font_size
    ,font_color
    ,border_style
    ,halign,valign){
  if(argObj == null)
    return;
  var obj = $("HWPostil1");
  var field_width = argObj.EndX - argObj.StartX;
  var field_height = argObj.EndY - argObj.StartY; 
  fieldIndex++;
  var fieldName = "field-" + fieldIndex;
  var vRet = obj.InsertNote(fieldName,argObj.page,field_type,argObj.StartX,argObj.StartY,field_width,field_height);
  if(vRet=="")
  {
    alert("此字段映射已经添加！");
    return true;
  }
  obj.setValue(fieldName , field_name + "(" + fieldName + ")");
  var note = {field:fieldName , des:field_name};
  notes.push(note);
  addFieldDiv(note);
  font_size = font_size.replace(/pt/ig,"");
  font_color = font_color.replace(/#/ig,"0x00");
  obj.SetValue(fieldName,":PROP:BORDER:"+border_style);
  obj.SetValue(fieldName,":PROP:FACENAME:"+font_family);
  obj.SetValue(fieldName,":PROP:FONTSIZE:"+font_size);
  obj.SetValue(fieldName,":PROP:FRONTCOLOR:618221");
  obj.SetValue(fieldName, ":PROP::LABEL:0");
  obj.SetValue(fieldName,":PROP:HALIGN:"+halign);
  obj.SetValue(fieldName,":PROP:VALIGN:"+valign);
  argObj = null;
}
function saveFile()
{
  var obj = $("HWPostil1");
  if($("T_NAME").value=="")
  {
    alert("请输入模板名称！");
    return;
  }

  var content = obj.GetCurrFileBase64();
  //保存文件到服务器
}
var isOpenPropWindow = false;
var win = null;
function openWin() {
  isOpenPropWindow = true;
  var w = 400 ;
  var h  = 300;
  var top =(jQuery(window).height() - h - 80) / 2 + document.documentElement.scrollTop;
  var left =  (jQuery(window).width() - w) / 2 ;
  createSim(w + 4 , h + 88, top , left);
  win = new YH.Window({
    'title' : '设置字段：',
   'draggable': false,
    'type': 'iframe',
    'width': w,
    'height': h,
    'modal':true,
    'closeAction': 'hide',
    'src': contextPath + "/test/core/aip/params.jsp",
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
  shim.style.width =  w;
  shim.style.height = h;
  document.body.appendChild(shim);
}
/**
 * aip操作函数
 * @param lActionType
 * @param lType
 * @param strName
 * @param strValue
 * @return
 */
function JSNotifyBeforeAction(lActionType,lType,strName,strValue) {
  //删除
  if (3 == lActionType) {
    delNote(strName);
  }
  //单击结点
  if(7==lActionType){
    selectedNote = strName;
    selectNote();
    var div = $(strName + "-div");
    selecteDiv(div);
    setNoteSelect(strName);
  }
}
/**
 * 选中结点设置属性面板
 */
function selectNote() {
  var obj = $('HWPostil1');
  var note = getNote(selectedNote);
  $('description').value = note.des;
  setPosValue(selectedNote , "");
  setFontStyleValue(selectedNote , "");
}
/**
 * 选中列表
 * @param div
 * @return
 */
function selecteDiv(div) {
  div.isSelected = true;
  div.className = "item select";
  var divs = $('fieldDiv').children;
  for (var i = 0 ;i < divs.length;i++) {
    var tmpDiv = divs[i];
    if (tmpDiv != div) {
      tmpDiv.className = "item";
      tmpDiv.isSelected = false;
    }
  }
}
/**
 * 选中aip里的结点
 * @param field
 * @return
 */
function setNoteSelect(field) {
  var obj = $('HWPostil1');
  for (var i = 0 ;i < notes.length;i++) {
    var tmp = notes[i];
    var tmpField = tmp.field;
    if (tmpField == field) {
      obj.SetValue(tmpField,":PROP:BACKCOLOR:999999");
    } else {
      obj.SetValue(tmpField,":PROP:BACKCOLOR:-1");
    }
  }
}
/**
 * 取得从结点数组里取得结点对象
 * @param field
 * @return
 */
function getNote(field) {
  for (var i = 0 ;i < notes.length;i++) {
    var tmp = notes[i];
    var tmpField = tmp.field;
    if (tmpField == field) {
      return tmp;
    }
  }
  return null;
}
//当前选中的结点
var selectedNote = "";
/**
 * 通过属性面板设置aip里的位置，宽度和高度
 * @return
 */
function setNotePos(){
  var l = parseInt($('noteX').value);
  var t = parseInt($('noteY').value);
  var w = parseInt($('noteW').value);
  var h = parseInt($('noteH').value);
  var obj = $('HWPostil1');
  obj.SetNotePos(selectedNote , l ,t , w , h);
}
/**
 * 通过aip设置属性面板里的字体
 * @return
 */
function setFontStyleValue(pcNoteName , prex) {
  var obj = $('HWPostil1');
  if (pcNoteName ==  prex + selectedNote) {
    //用结点名字取得结点的字体的相关信息还没有
  }
}
/**
 * 通过aip里的位置，宽度和高度设置属性面板
 * @param pcNoteName
 * @param prex
 * @return
 */
function setPosValue(pcNoteName , prex) {
  var obj = $('HWPostil1');
  if (pcNoteName ==  prex + selectedNote) {
    var noteH = obj.GetNoteHeight(pcNoteName);
    $('noteH').value = noteH;
    var noteW = obj.GetNoteWidth(pcNoteName);
    $('noteW').value = noteW;
    var noteX = obj.GetNotePosX(pcNoteName);
    $('noteX').value = noteX;
    var noteY = obj.GetNotePosY(pcNoteName);
    $('noteY').value = noteY;
  }
}
/**
 * 添加结点到列表面板
 * @param note
 * @return
 */
function addFieldDiv(note) {
  var noteField = note.field;
  var noteDes = note.des;

  var i = notes.length;
  var div = new Element("div" , {'class':'item'});
  div.isSelected = false;
  addEvent(div　, noteField);
  div.id = noteField + "-div";
  div.style.cursor = "hand";
  $('fieldDiv').appendChild(div);
  $('filedListField').show();
  var div1 = new Element("div" , {'class':'fieldName'});
  
  div1.align = "center";
  div1.id = noteField + "-div1";
  div.appendChild(div1);
  div1.update(noteField);
  var div2 = new Element("div", {'class':'fieldDes'});
  div.appendChild(div2);
  div2.update(noteDes);
} 
/**
 * 添加结点
 * @param div
 * @param field
 * @return
 */
function addEvent(div , field) {
  div.onmouseover = function() {
    if (!this.isSelected){
      this.className = "item hover";
    }
  }
  div.onmouseout = function() {
    if (!this.isSelected){
      this.className = "item";
    }
  }
  div.onclick = function() {
    selecteDiv(div);
    selectedNote = field;
    selectNote();
    //选中aip中的结点
    setNoteSelect(field);
  }
}

function unset() {
  $("noteX").value = '';
  $("noteY").value = '';
  $("noteH").value = '';
  $("noteW").value = '';
  $("description").value = '';
}
function delNote(noteName) {
  if (noteName == selectedNote) {
    unset();
    selectedNote = "";
  }
  var div = $(noteName + "-div");
  $('fieldDiv').removeChild(div);
  delNotes(noteName);
}
function delNotes(noteName) {
  var noteTmp = new Array();
  for (var i = 0 ;i < notes.length;i++) {
    var tmp = notes[i];
    var tmpField = tmp.field;
    if (tmpField != noteName) {
      noteTmp.push(tmp);
    }
  }
  notes = noteTmp;
}
function setNoteDes(textarea){
  var des = textarea.value;
  if (selectedNote) {
    var obj = $('HWPostil1');
    obj.setValue(selectedNote , "");
    obj.setValue(selectedNote , des + "(" + selectedNote + ")");
    for (var i = 0 ;i < notes.length;i++) {
      var tmp = notes[i];
      var tmpField = tmp.field;
      if (tmpField == selectedNote) {
        tmp.des = des;
      }
    }
    var div = $(selectedNote + "-div");
    div.lastChild.update(des);
  }
}