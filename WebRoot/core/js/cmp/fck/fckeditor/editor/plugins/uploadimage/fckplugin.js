FCKCommands.RegisterCommand( 'uploadimage', new FCKDialogCommand( 'uploadimage', '上传图片',  

FCKPlugins.Items['uploadimage'].Path + 'chinaz6.jpg', 340, 200 ) ) ; 

var myUploadImageItem = new FCKToolbarButton( 'uploadimage', '上传图片' ) ; 

//myUploadImageItem.IconPath = FCKPlugins.Items['myUploadImage'].Path + 'br.gif' ; 

FCKToolbarItems.RegisterItem( 'uploadimage', myUploadImageItem ) ; 


var FCKIn = new Object() ; 


FCKIn.Add = function(address){  
FCK.InsertHtml("<img src='" + address + "'/>"); 
} 
