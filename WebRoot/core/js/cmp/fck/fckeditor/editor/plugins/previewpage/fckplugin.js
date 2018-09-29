var oPreviewpage = new FCKToolbarButton( 'previewpage', "预览" ) ; 
oPreviewpage.IconPath = FCKPlugins.Items['previewpage'].Path + 'previewpage.jpg' ; 
FCKToolbarItems.RegisterItem( 'previewpage', oPreviewpage) ;
var oPreviewpageCmd = function()
{}
oPreviewpageCmd.prototype =
{
  Name : 'previewpage',

  Execute : function()
  {
    parent.jsonToInput();
    parent.document.submitForm.action = parent.actionUrl 
                            + "/previewPage.act";
    parent.document.submitForm.target = "preview";
    parent.document.submitForm.submit();
  },

  GetState : function()
  {
    return FCK_TRISTATE_OFF ;
  }
};

FCKCommands.RegisterCommand('previewpage',new oPreviewpageCmd());