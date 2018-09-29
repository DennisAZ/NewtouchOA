FCKCommands.RegisterCommand( 'YHGrid', new FCKDialogCommand('YHGrid'
    ,'设置分页表的属性'
    ,FCKPlugins.Items['YHGrid'].Path + 'fck_YHGrid.jsp',500,600
    ));
var oYHGridItem = new FCKToolbarButton( 'YHGrid', '添加分页表' ) ;
oYHGridItem.IconPath = FCKPlugins.Items['YHGrid'].Path + 'YHGrid.jpg' ;
FCKToolbarItems.RegisterItem( 'YHGrid', oYHGridItem ) ;
var FCKYHGrid = new Object() ;
FCKYHGrid.AddYHGrid = function( gridImg ){
  FCK.InsertHtml(gridImg) ;
}
var oDelYHGridCommand = new Object() ;
oDelYHGridCommand.Name = 'DeleteYHGrid' ;
oDelYHGridCommand.Execute = function(){
  if(confirm("确定删除列表吗？")){
    FCK.InsertHtml("") ;
  }
}
oDelYHGridCommand.GetState = function(){
  return FCK_TRISTATE_OFF ;
}
FCKCommands.RegisterCommand( 'DeleteYHGrid', oDelYHGridCommand) ;
var oYHGridWidgetMenuListener = new Object() ;
oYHGridWidgetMenuListener.AddItems = function( contextMenu, tag, tagName ){
if(tagName == 'IMG'
           && tag.getAttribute("widgettype")=='Grid'){
  contextMenu.AddSeparator();
  contextMenu.AddItem( 'YHGrid', '编辑表' ,oYHGridItem.IconPath);
  contextMenu.AddItem( 'DeleteYHGrid' ,'删除表');
  }
}
FCK.ContextMenu.RegisterListener( oYHGridWidgetMenuListener ) ;