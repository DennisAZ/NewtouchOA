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
    
    var index = parameters.indexOf("oprates:");
    
    if(index != -1){
      start = parameters.substring(0,index);
      start = start.substring(0,start.length-1);
      operates = parameters.substr(index);
      endIndex = operates.indexOf("}]");
      end = operates.substr(endIndex);
      parameters = start + end;
      operates = operates.substring(0,endIndex);
      doOperate(operates);
    }
    var oParameters =  eval('(' + parameters + ')');
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