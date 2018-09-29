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
