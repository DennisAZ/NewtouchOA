<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="<%=contextPath %>/raw/lh/ckeditor/ckeditor.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
	<style id="styles" type="text/css">

.cke_button_fileUploadCmd .cke_icon
{
	display : none !important;
}

.cke_button_fileUploadCmd .cke_label
{
	display : inline !important;
}

	</style>
</head>

<body>
<textarea cols="80" id="editor1" name="editor1" rows="10">&lt;p&gt;This is some &lt;strong&gt;sample text&lt;/strong&gt;. You are using &lt;a href="http://www.fckeditor.net/"&gt;CKEditor&lt;/a&gt;.&lt;/p&gt;</textarea>
				<script type="text/javascript">
				//<![CDATA[
					var editor = CKEDITOR.replace( 'editor1' ,{
						//skin:'v2',
		       	     	toolbar :[['Source','Preview','-','Maximize', 'ShowBlocks','-','Undo','Redo'],
						  ['Bold','Italic','Underline','Strike','-','Subscript','Superscript'],
					    ['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'],
					    ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
					    ['Link','Unlink','Anchor'],
					    ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
					    '/',
					    ['Styles','Format','Font','FontSize'],
					    ['TextColor','BGColor'],
					    ['Checkbox','Radio','TextField','Textarea','Select','Button','ImageButton','HiddenField','fileupload']]
					});
					editor.on( 'pluginsLoaded', function( ev )
							{
								if ( !CKEDITOR.dialog.exists( 'fileUpload' ) )
								{
									var href = '/yh/raw/lh/FileUpload.js';
									CKEDITOR.dialog.add( 'fileUpload', href );
								}
								editor.addCommand( 'fileUploadCmd', new CKEDITOR.dialogCommand( 'fileUpload' ) );

								// Add the a custom toolbar buttons, which fires the above
								// command..
								editor.ui.addButton( 'fileupload',
									{
										label : 'FileUpload',
										command : 'fileUploadCmd'
									} );
							});
				//]]>
				</script>
</body>
</html>