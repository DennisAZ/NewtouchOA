
FCKCommands.RegisterCommand( 'NTemplate', new FCKDialogCommand('NTemplate'
	    ,'使用模板'
	    ,FCKPlugins.Items['NTemplate'].Path + 'ntemplate.jsp',500,500
	    ));
	var oNTemplate = new FCKToolbarButton( 'NTemplate', '使用模板' ) ;
	oNTemplate.IconPath = FCKPlugins.Items['NTemplate'].Path + 'ntemplate.gif' ;
	FCKToolbarItems.RegisterItem( 'NTemplate', oNTemplate ) ;
