function plugins(config){
  var commandName = config.comName;
  var item = config.item;
  var httpPage = config.page;
  var inco =  config.inco;
  var title = config.dlgTitle;
  var btnTitle = config.btnTitle;
  var width = config.width;
  var height = config.height;
//注册命令
FCKCommands.RegisterCommand( commandName
    , new FCKDialogCommand( commandName
        , title
        , FCKPlugins.Items[item].Path + httpPage
        , width, height ) ) ;
//注册工具栏按钮
var oCodeItem = new FCKToolbarButton( commandName, btnTitle ) ;
oCodeItem.IconPath = FCKPlugins.Items[item].Path + inco ;
FCKToolbarItems.RegisterItem( commandName, oCodeItem ) ;
//创建FCKPlugins对象，通过FCKPlugins对象处理工具栏按钮的操作
var FCKCodes = new Object() ;
FCKCodes.RegisterAction = function(actName,func){
  FCKCodes[actName] = func;
};
return FCKCodes;
}
//注册右键菜单
//注册命令
function addMenuItem(menuCommand , name , fun){
  menuCommand.Name = name ;
  menuCommand.Execute = fun;
  menuCommand.GetState = function(){
    return FCK_TRISTATE_OFF ;
  }
  FCKCommands.RegisterCommand( name, menuCommand) ;
}

//function checkUnique(title,fckdocument){
 // var doms = fckdocument.all;
////  for(var i = 0 ;i < doms.length ; i++){
 //   var dom = doms[i];
 //   var reltitle = "";
  //  if(dom.className == "SIGN" 
 //     || dom.className == "USER"
  //    || dom.className == "DATE" ){
  //    continue;
 //   }else{
 //     reltitle = dom.title;
 //   }
 //   if(title == reltitle){
 //     alert("控件名已被使用");
 //   }
 // }
//}
//注册监听器
//var oYHGridWidgetMenuListener = new Object() ;
//oYHGridWidgetMenuListener.AddItems = function( contextMenu, tag, tagName ){
//if(tagName == 'INPUT' && tag.type == 'checkbox'){
//  contextMenu.AddSeparator();
//  contextMenu.AddItem('DeleteNCheckBox','删除选择框');
//  contextMenu.AddItem('NCheckBox','选择框属性');
//  }
//}
//FCK.ContextMenu.RegisterListener( oYHGridWidgetMenuListener ) ;
//function getIndexOfPreName(pre,fckdocument)
//{
//  var max = 0;
//  if(pre && pre == "DATA"){
//    var inputDoms = fckdocument.getElementsByTagName("input");
//    var textareaDoms = fckdocument.getElementsByTagName("textarea");
//    var selectsDoms = fckdocument.getElementsByTagName("select");
//    for(var i = 0 ;i < inputDoms.length ; i++){
//      var input = inputDoms[i];
//      var tem = (input.name).replace("DATA_","");
//      max = Math.max(max,parseInt(tem));
//    }
//    for(var i = 0 ;i < textareaDoms.length ; i++){
//      var textarea = textareaDoms[i];
//      var tem = (textarea.name).replace("DATA_","");
//      max = Math.max(max,parseInt(tem));
//    }
//   for(var i = 0 ;i < selectsDoms.length ; i++){
//      var select = selectsDoms[i];
//      var tem = (select.name).replace("DATA_","");
//      max = Math.max(max,parseInt(tem));
//    }
//  }else if(pre && pre == "OTHER"){
//    var imagesDoms = fckdocument.getElementsByTagName("IMG");
//    for(var i = 0 ;i < imagesDoms.length ; i++){
//      var image = imagesDoms[i];
//      var tem = (image.name).replace("OTHER_","");
//      max = Math.max(max,parseInt(tem));
//    }
//  }
//    return max + 1;
//}