FCKCommands.RegisterCommand( 'DtreeWidget', new FCKDialogCommand('DtreeWidget'
    ,'设置树的属性'
    ,FCKPlugins.Items['DtreeWidget'].Path + 'fck_DtreeWidget.jsp',500,500
    ));
var oDtreeWidgetItem = new FCKToolbarButton( 'DtreeWidget', '添加树' ) ;
oDtreeWidgetItem.IconPath = FCKPlugins.Items['DtreeWidget'].Path + 'dtree.jpg' ;
FCKToolbarItems.RegisterItem( 'DtreeWidget', oDtreeWidgetItem ) ;
var FCKDTreeWidget = new Object() ;
FCKDTreeWidget.AddDTree = function( dtreeImg ){
  
  FCK.InsertHtml(dtreeImg) ;
  
}
var oDelWDtreeCommand = new Object() ;
oDelWDtreeCommand.Name = 'DeleteDtree' ;
oDelWDtreeCommand.Execute = function(){
  if(confirm("确定删除树吗？")){
    FCK.InsertHtml("") ;
  }
}
oDelWDtreeCommand.GetState = function(){
  return FCK_TRISTATE_OFF ;
}
FCKCommands.RegisterCommand( 'DeleteDtree', oDelWDtreeCommand) ;
var oDtreeWidgetMenuListener = new Object() ;
oDtreeWidgetMenuListener.AddItems = function( contextMenu, tag, tagName ){
if(tagName == 'IMG'&& tag.getAttribute("widgettype")=='DTree'){
  contextMenu.AddSeparator();
  contextMenu.AddItem( 'DtreeWidget', '编辑树' ,oDtreeWidgetItem.IconPath);
  contextMenu.AddItem( 'DeleteDtree' ,'删除树');
  }
}
FCK.ContextMenu.RegisterListener( oDtreeWidgetMenuListener ) ;