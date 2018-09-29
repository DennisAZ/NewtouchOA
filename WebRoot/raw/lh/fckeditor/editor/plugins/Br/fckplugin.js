var oBr = new FCKToolbarButton( 'br', FCKLang.InsertLinkBtn ) ; 
oBr.IconPath = FCKPlugins.Items['Br'].Path + 'br.gif' ; 
FCKToolbarItems.RegisterItem( 'br', oBr) ;
var oBrCommand = function()
{}
oBrCommand.prototype =
{
  Name : 'br',

  Execute : function()
  {
    FCK.InsertHtml("<br/>") ;  
  },

  GetState : function()
  {
    return FCK_TRISTATE_OFF ;
  }
};

FCKCommands.RegisterCommand('br',new oBrCommand())