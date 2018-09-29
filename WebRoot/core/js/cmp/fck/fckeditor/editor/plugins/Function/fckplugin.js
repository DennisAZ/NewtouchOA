
FCKCommands.RegisterCommand( 'Function', new FCKDialogCommand( 'Function', '设置函数的属性', FCKPlugins.Items['Function'].Path + 'fck_Function.jsp', 500, 350 ) ) ;


var oFunctionItem = new FCKToolbarButton( 'Function', '添加js函数' ) ;
oFunctionItem.IconPath = FCKPlugins.Items['Function'].Path + 'Function.jpg' ;

FCKToolbarItems.RegisterItem( 'Function', oFunctionItem ) ;


// The object used for all Placeholder operations.
var FCKFunction = new Object() ;

// Add a new placeholder at the actual selection.
FCKFunction.AddFun = function(fun)
{
  FCK.InsertHtml(fun);  
}

var oDelFunCommand = new Object() ;
oDelFunCommand.Name = 'DeleteFunction' ;
oDelFunCommand.Execute = function(){
  if(confirm("删除此函数将导致一些控件对此函数引用的失效或产生错误，确定删除此函数吗？")){
    FCK.InsertHtml("");
  }
}
oDelFunCommand.GetState = function(){
  return FCK_TRISTATE_OFF ;
}
FCKCommands.RegisterCommand( 'DeleteFunction', oDelFunCommand) ;
var oFunctionMenuListener = new Object() ;
oFunctionMenuListener.AddItems = function( contextMenu, tag, tagName ){
if(tagName == 'IMG'
              && tag.getAttribute("widgettype") == 'Function'){
  contextMenu.AddSeparator();
  contextMenu.AddItem('Function','编辑函数',oFunctionItem.IconPath);
  contextMenu.AddItem('DeleteFunction','删除函数');
  }
}
FCK.ContextMenu.RegisterListener(oFunctionMenuListener) ;