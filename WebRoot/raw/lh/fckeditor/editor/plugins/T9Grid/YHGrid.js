var oldId = "";
var size = 9 ;
var widgetData = window.parent.parent.widgetData;
var contextPath = window.parent.parent.contextPath;
var dialog = window.parent ;
var oEditor = dialog.InnerDialogLoaded() ;
var FCKYHGrid = oEditor.FCKYHGrid;
var fckdocument =  oEditor.FCK.EditorDocument;
window.onload = function ()
{   
  getFunContext();
  LoadSelected();
  dialog.SetOkButton( true ) ;
  SelectField('tableName') ;
}

function Ok()
{
  if(!checkParameters()){
    return false;
  }
  var tableId = $F('tableName');
  grid = "<img widgetType=\"Grid\" id=\"" + tableId + "\"" 
       + " src=\"" + contextPath + "/raw/lh/fckeditor/editor/plugins/YHGrid/YHGrid.jpg\" width=\"20px\" height=\"20px\"/>";
  var headerMessage = readMessageTbody();
  var operate = readOperateTbody();
  if(operate){
    var hd = "[[" + headerMessage + "],{header:\"" + $F("title") + "\",oprates:[" + operate + "]}]";
  }else{
    var hd = "[[" + headerMessage + "],[]]";
  }
  var pars = "{hd:" + hd + ",url:\"" + $F("tableUrl") +  "\",numOfPage:" + $F("numOfPage") + ",rendTo:\"" + $F("bindId") + "\"}";
  widgetData[tableId + "Data"] = pars;
  
  FCKYHGrid.AddYHGrid(grid) ;
  return true;
}

var eSelected = dialog.Selection.GetSelectedElement() ;

function LoadSelected()
{
  if(!eSelected){
    return ;
  }
  if(eSelected.tagName == 'IMG'
           && eSelected.getAttribute("widgettype")=='Grid'){
    $('tableName').value = eSelected.id;
    oldId =  eSelected.id
    var parameters = widgetData[ oldId + "Data"];
    
   // var index = parameters.indexOf("oprates:");
    
//    if(index != -1){
//      start = parameters.substring(0,index);
//      start = start.substring(0,start.length-1);
//      operates = parameters.substr(index);
//      endIndex = operates.indexOf("}]");
//      end = operates.substr(endIndex);
//      parameters = start + end;
//      operates = operates.substring(0,endIndex);
//      doOperate(operates);
//    }
    var oParameters =  eval('(' + parameters + ')');
    
    if(oParameters.hd[1].length != 0){
      $('title').value = oParameters.hd[1].header;
      
    }
    for(var i = 0 ;i<oParameters.hd[0].length ;i++){
      var header = oParameters.hd[0][i];
      var headerStr = header.header;
      var name = header.name;
      var width =  "";
      if(header.width){
        width = header.width;
      }
      var hidden = header.hidden;
      addHeadMessage(headerStr,name,width,hidden);
    }
    $('title').value = "操作";
    if( typeof(oParameters.hd[1]) ==  'Object'){
      $('title').value = oParameters.hd[1].header;
    }
    $('tableUrl').value = oParameters.url;
    $('numOfPage').value = oParameters.numOfPage;
    $('bindId').value = oParameters.rendTo;
  }else{
    eSelected == null ;
  }
}
function doOperate(operates){
  var index = operates.indexOf("[");
  operates = operates.substring(index + 1 , operates.length-1);
  
  operates = operates.replace(/new YHOprate/gi,"");
  var op = operates.split(",");
 
  for(var i = 0 ;i< op.length; i += 3){
    var name = op[i].substring(2,op[i].length-1);
    var isDefault = op[i+1];
    var funName = op[i+2].substring(0,op[i+2].length-1);
    
    addOperate(name,isDefault,funName);
  }
  
}
function readMessageTbody(){
  var messageTbody = $('messageTbody');
  var messageLength = $('messageTmp')? messageTbody.rows.length - 1 :messageTbody.rows.length;
  var str = "";
  for(var i = 1 ; i < messageLength ; i++){
    var tr = messageTbody.rows[i];
    str += "{";
    str += "header:\"" + tr.cells[0].innerHTML.trim() + "\"";
    str += ",name:\"" + tr.cells[1].innerHTML.trim() + "\"";
    var width = tr.cells[2].innerHTML.trim();
    if(width){
      str += ",width:\"" + width + "\"";
    }
    str += ",hidden:" + tr.cells[3].innerHTML.trim() + "},";
  }
  str = str.substring(0, str.length-1);
  return str;
}
function readOperateTbody(){
  var operateTbody = $('operateTbody');
  var str = "";
  if($('operateHeader')){
    var operateLength =
            $('tmpOperateInput')? operateTbody.rows.length - 1 :operateTbody.rows.length;
    
    for(var i = 1 ;i < operateLength; i++){
      var tr = operateTbody.rows[i];
      
      str += "new YHOprate(\"" + tr.cells[0].innerHTML.trim() + "\"";
      
      str += "," + tr.cells[1].innerHTML.trim();
      
      str += "," + tr.cells[2].innerHTML.trim();
      
      str += "),";
    }
    
    
    if(str){
      str = str.substring(0, str.length-1);
    }
  }
  return str;
}

function checkParameters(){
  if(!$('tableName').present()){
    alert('没有指定表名');
    $('tableName').focus();
    return false;
  }
  if($F('tableName') != oldId){
    var isExistId = fckdocument.getElementById($F('tableName'));
    if(isExistId){
      alert('此tableName文档中已有，请换用其它表名');
      $('tableName').focus();
      return false;
    }
  }
//  if(!$("title").present()){
//    alert('操作标题不能为空！');
//    $("title").focus();
//    return false;
//  }
  if(!$("tableUrl").present()){
    alert('请求地址不能为空！');
    $("tableUrl").focus();
    return false;
  }
  if(!$("numOfPage").present()){
    alert('最多显示的条数不能为空！');
    $("numOfPage").focus();
    return false;
  }
  var messageTbody = $('messageTbody');
  var tmpInput = $('tmpInput');
  var messageInput = $('messageTmp');
  if(messageInput){
    alert('还有没有保存的表头信息!');
    return false;
  }
  if(messageTbody.rows.length == 2 && tmpInput){
    alert('没有设置表头信息');
    return false;
  }
  var operateTmp = $('operateTmp');
  if(operateTmp){
    alert('还有没有保存的操作信息!');
    return false;
  }
  if(!$('bindId').present()){
    
    alert('没有指定绑定容器的Id');
    $('bindId').focus();
    return false;
  }
  var isExistId = fckdocument.getElementById($F('bindId'));
  if(!isExistId || isExistId.tagName != 'DIV'){
    alert('此bindId文档中不存在或指定的bindId不是div容器');
    $('bindId').focus();
    return false;
  }
  return true;
}

function addHeadMessage(header,name,width,isHidden){
  var tbody = $('messageTbody');
  var tableHeader = $('tableHeader');
 
  var isChecked = ((isHidden == true) ? "checked" : "");
  if(!tableHeader){
    var tmp = "<td>header</td><td>name</td><td>width</td><td>isHidden</td><td> operate </td>";
    var tr = new Element('tr',{id:'tableHeader'});
    tr.className = 'TableHeader';
    tbody.appendChild(tr);
    tr.innerHTML = tmp;
  }
  if(!header){
    var tr = $('tmpInput');
    if(!tr){
      var tmpInput = "<td><input size='" + size + "' type='text' value='" + header + "'/> </td>"
        + "<td><input  size='" + size + "'  type='text' value='" + name + "'/> </td>"
        + "<td><input  size='" + size + "' type='text' value='" + width + "'/> </td>"
        + "<td><input type='checkbox' "+ isChecked +" /> </td>"
        + "<td><input type='button' value='delete' onclick='delTr(this)'/>"
        + "<input type='button' value='save' onclick='addTr(this,4)'/></td>";
      var newTr = new Element('tr');
      newTr.id = "tmpInput";
      newTr.name = "messageTr";
      tbody.appendChild(newTr);
      newTr.innerHTML = tmpInput;
    
    }else{
    return ;
    }
  }else{
    var tmpInput = "<td>" + header + "</td>"
      +"<td>" + name + "</td>"
      +"<td>" + width + "</td>"
      +"<td>" + isHidden + "</td>"
      +"<td><input type='button' value='delete' onclick='delTr(this)'/>"
      +"<input type='button' value='update' onclick='updateTr(this,4)'/></td>";
    var newTr = new Element('tr');
    newTr.name = "messageTr";
    tbody.appendChild(newTr);
    newTr.innerHTML = tmpInput;
  }
  if(tbody.rows.length%2 == 0){
    newTr.className = 'TableLine1';
  }else{
    newTr.className = 'TableLine2';
  } 
}
function delTr(input){
  if(confirm("是否删除此行！")){
    var tr = input.parentNode.parentNode;
    var tbody = input.parentNode.parentNode.parentNode;
    tbody.removeChild(tr);
    if(tbody.rows.length == 1){ //如果是最后一行的话
      tbody.removeChild(tbody.firstChild);
  }
  }
}

function addTr(input,count){
  var tr = input.parentNode.parentNode;
  if(!checkHeadMessage(tr)){
  return ;
  }
  for(var i = 0 ;i < count ;i++){
    changeInputToText(tr.childNodes[i]);
  }
  input.value = "update";
  input.onclick = function(){
    updateTr(this,count);
  }
  tr.id = "";
}
function updateTr(input,count){
  var tr = input.parentNode.parentNode;
  if(tr.name == 'messageTr'){
    tr.id == 'messageTmp';
  }else{
    tr.id == 'operateTmp';
  }
  for(var i = 0 ;i < count ;i++){
    changeTextToInput(tr.childNodes[i]);
  }
  input.value = "save";
  input.onclick = function(){
    addTr(input,count);
  }
}
function changeTextToInput(td){
  var text = td.innerHTML;
  td.innerHTML = '';
  var input = new Element('input');
  if(text != 'true' && text != 'false'){
    input.type = 'text';
    input.size = size;
    input.value = text;
  }else{
    input.type = 'checkbox';
    if(text == 'true'){
      input.checked = true;
    }
  }
  td.appendChild(input);
}
function changeInputToText(td){
  var input = td.firstChild;
  if(input.type == 'text'){
    var text = input.value;
    td.update(text); 
  }else{
    td.update(input.checked);
  }
}
function addOperate(name,isDefault,funName){
  var tbody = $('operateTbody');
  var tableHeader = $('operateHeader');
 
  var isChecked = ((isDefault == true) ? "checked" : "");
  if(!tableHeader){
    var tmp = "<th>name</th><th>isDefault</th><th>funName</th><th> operate </th>";
    var tr = new Element('tr',{id:'operateHeader'});
    tr.className = 'TableHeader';
    tbody.appendChild(tr);
    tr.innerHTML = tmp;
  }
  if(!name){
    var tr = $('tmpOperateInput');
    if(!tr){
      var tmpInput = "<td><input size='" + size + "' type='text' value='" + name + "'/> </td>"
        + "<td><input type='checkbox'"+ isChecked +" /> </td>"
        + "<td><input  size='" + size + "' type='text' value='" + funName + "'/> </td>"
        + "<td><input type='button' value='delete' onclick='delTr(this)'/>"
        + "<input type='button' value='save' onclick='addTr(this,3)'/></td>";
      var newTr = new Element('tr');
      newTr.id = "tmpOperateInput";
      newTr.name = "operateTr";
      
      tbody.appendChild(newTr);
      newTr.innerHTML = tmpInput;
    }else{
      return ;
    }
  }else{
    var tmpInput = "<td>" + name + "</td>"
     +"<td>" + isDefault + "</td>"
     +"<td>" + funName + "</td>"
     +"<td><input type='button' value='delete' onclick='delTr(this)'/>"
     +"<input type='button' value='update' onclick='updateTr(this,3)'/></td>";
   var newTr = new Element('tr');
   newTr.name = "operateTr";
   tbody.appendChild(newTr);
   newTr.innerHTML = tmpInput;
  
  }
  if(tbody.rows.length%2 == 0){
    newTr.className = 'TableLine1';
  }else{
    newTr.className = 'TableLine2';
  }  
}
function checkHeadMessage(tr){
  if(tr.name == 'messageTr'){
    var header = tr.cells[0].firstChild;
    var name = tr.cells[1].firstChild;
    
    if(!$(header).present()){
      header.focus();
      alert('header不能为空！');
      return false;
    }
    if(!$(name).present()){
      name.focus();
      alert('name不能为空！');
      return false;
    }
    return true;
  }else{
    var name = tr.cells[0].firstChild;
    var funName = tr.cells[2].firstChild;
    if(!$(funName).present()){
      funName.focus();
      alert('函数名不能为空！');
      return false;
    }else{
      var isExistFun = fckdocument.getElementById(funName.value);
      if(!isExistFun
          ||isExistFun.getAttribute('widgettype') != 'Function'){
      alert(' 指定的函数'+ funName.value +'不存在');
      funName.focus();
      return false;
      }
    }
    if(!$(name).present()){
      name.focus();
      alert('name不能为空！');
      return false;
    }
    return true;
  }  
}