
FCKCommands.RegisterCommand( 'NTemplate', new FCKDialogCommand('NTemplate'
	    ,'使用模板'
	    ,FCKPlugins.Items['NTemplate'].Path + 'ntemplate.jsp',500,500
	    ));
	var oDtreeWidgetItem = new FCKToolbarButton( 'NTemplate', '使用模板' ) ;
	oDtreeWidgetItem.IconPath = FCKPlugins.Items['NTemplate'].Path + 'ntemplate.gif' ;
	FCKToolbarItems.RegisterItem( 'NTemplate', oDtreeWidgetItem ) ;
	var FCKDTreeWidget = new Object() ;
	FCKDTreeWidget.AddDTree = function( dtreeImg ){
	  FCK.InsertHtml(dtreeImg) ;
	}
	oDelWDtreeCommand.GetState = function(){
	  return FCK_TRISTATE_OFF ;
	}
