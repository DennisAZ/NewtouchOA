FCKCommands.RegisterCommand( 'myUploadImage', new FCKDialogCommand( 'myUploadImage', '上传图片',  

FCKPlugins.Items['myUploadImage'].Path + 'myUploadImage.html', 340, 200 ) ) ; 

var myUploadImageItem = new FCKToolbarButton( 'myUploadImage', '上传图片' ) ; 

myUploadImageItem.IconPath = FCKPlugins.Items['myUploadImage'].Path + 'br.gif' ; 

FCKToolbarItems.RegisterItem( 'myUploadImage', myUploadImageItem ) ; 


var FCKIn = new Object() ; 


FCKIn.Add = function( address ){  
FCK.InsertHtml("<img src='" + address + "'/>"); 
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