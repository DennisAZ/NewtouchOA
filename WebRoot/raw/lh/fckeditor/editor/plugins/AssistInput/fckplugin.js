FCKCommands.RegisterCommand( 'AssistInput', new FCKDialogCommand('AssistInput'
    ,'设置辅助输入控件属性'
    ,FCKPlugins.Items['AssistInput'].Path + 'fck_AssistInput.jsp',500,600
    ));
var oAssistInputItem = new FCKToolbarButton( 'AssistInput', '添加辅助输入控件' ) ;
oAssistInputItem.IconPath = FCKPlugins.Items['AssistInput'].Path + 'YHGrid.jpg' ;
FCKToolbarItems.RegisterItem( 'AssistInput', oAssistInputItem ) ;
var FCKAssistInput = new Object();
FCKAssistInput.AddAssistInput = function( gridImg ){
  FCK.InsertHtml(gridImg) ;
}
var oDelAssistInputCommand = new Object() ;
oDelAssistInputCommand.Name = 'DeleteAssistInput' ;
oDelAssistInputCommand.Execute = function(){
  if(confirm("确定删除此控件吗？")){
    FCK.InsertHtml("") ;
  }
}
oDelAssistInputCommand.GetState = function(){
  return FCK_TRISTATE_OFF ;
}
FCKCommands.RegisterCommand( 'DeleteAssistInput', oDelAssistInputCommand) ;
var oAssistInputWidgetMenuListener = new Object() ;
oAssistInputWidgetMenuListener.AddItems = function( contextMenu, tag, tagName ){
if(tagName == 'IMG'
           && tag.getAttribute("widgettype")=='AssistInput'){
  contextMenu.AddSeparator();
  contextMenu.AddItem( 'AssistInput', '编辑辅助输入控件' ,oAssistInputItem.IconPath);
  contextMenu.AddItem( 'DeleteAssistInput' ,'删除控件');
  }
}
FCK.ContextMenu.RegisterListener( oAssistInputWidgetMenuListener ) ;