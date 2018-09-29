CKEDITOR.dialog.add( 'fileUpload', function( editor )
{
    
	
	return {
    title : '上传',
    minWidth : 400,
    minHeight : 200,
    contents : [
      {
        id : 'tab1',
        label : 'First Tab',
        title : 'First Tab',
        elements :
        [
          {
            id : 'file',
            type : 'file',
            label : '上传'
          }
        ]
      }
    ]
  };
} );
