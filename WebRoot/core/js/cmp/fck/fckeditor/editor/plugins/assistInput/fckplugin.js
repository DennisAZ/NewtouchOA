FCKCommands.RegisterCommand( 'assistInput', new FCKDialogCommand( 'assistInput', '输入提示',  

FCKPlugins.Items['assistInput'].Path + 'test.html', 340, 200 ) ) ; 

var oInsertLinkItem = new FCKToolbarButton( 'assistInput', '输入提示' ) ; 

oInsertLinkItem.IconPath = FCKPlugins.Items['assistInput'].Path + 'cake.gif' ; 

FCKToolbarItems.RegisterItem( 'assistInput', oInsertLinkItem ) ; 


var FCKIn = new Object() ; 


FCKIn.Add = function( inputId, address ,fun)

{ 
  
 // FCK.EditorDocument.getElementById();
//FCK.InsertHtml("<link rel='stylesheet' href = '/yh/core/styles/style1/css/cmp/AssistInput.css'>");
//FCK.InsertHtml("<script type='text/javascript' src='/yh/core/js/prototype.js'></script><script type='text/javascript' src='/yh/core/js/cmp/AssistInput1.0.js'></script>");
//FCK.InsertHtml("<script type='text/javascript'>new AssistInput('"+inputId+"','"+address+"',test)</script>") ; 

} 

//## 1. Define the command to be executed when selecting the context menu item.
//var oMyCommand = new Object() ;
//oMyCommand.Name = 'EditInput' ;
//
//// This is the standard function used to execute the command (called when clicking in the context menu item).
//oMyCommand.Execute = function()
//{
//  
//  FCKCommands.GetCommand( 'createInput' ).Execute() ;
//}
//
//// This is the standard function used to retrieve the command state (it could be disabled for some reason).
//oMyCommand.GetState = function()
//{
//  // Let's make it always enabled.
//  return FCK_TRISTATE_OFF ;
//}
//
//// ## 2. Register our custom command.
//FCKCommands.RegisterCommand( 'EditInput', oMyCommand ) ;
//
//// ## 3. Define the context menu "listener".
//var oMyContextMenuListener = new Object() ;
//
//// This is the standard function called right before sowing the context menu.
//oMyContextMenuListener.AddItems = function( contextMenu, tag, tagName )
//{
//  // Let's show our custom option only for images  
//  
//  if ( tagName == 'INPUT'&&tag.type == 'text')
//  {
//    contextMenu.AddSeparator() ;
//    contextMenu.AddItem( 'EditInput', '编辑输入框' ) ;
//  }
//}
//
//// ## 4. Register our context menu listener.
//FCK.ContextMenu.RegisterListener( oMyContextMenuListener ) ;