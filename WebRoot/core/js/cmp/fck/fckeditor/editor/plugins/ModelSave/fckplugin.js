var oModelSave = new FCKToolbarButton( 'ModelSave', FCKLang.InsertLinkBtn ) ; 
oModelSave.IconPath = FCKPlugins.Items['ModelSave'].Path + 'ModelSave.jpg' ; 
FCKToolbarItems.RegisterItem( 'ModelSave', oModelSave) ;
var oModelSaveCommand = function()
{}
oModelSaveCommand.prototype =
{
  Name : 'ModelSave',

  Execute : function()
  { 
    var path = parent.nowFilePath;
    if(path){
      parent.jsonToInput();
     
      parent.document.submitForm.action = parent.actionUrl
                            + "/savePage.act?path="
                            + path;
      parent.document.submitForm.target = "deployIframe";
      parent.document.submitForm.submit();
    }
  },

  GetState : function()
  {
    return FCK_TRISTATE_OFF ;
  }
};

FCKCommands.RegisterCommand('ModelSave',new oModelSaveCommand())
