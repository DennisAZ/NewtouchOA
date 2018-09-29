//得到控件对象
var FCKObject = plugins({comName:"NTextArea"
  ,item:"NTextArea"
  ,page:"ntextarea.jsp"
  ,inco:"textarea.gif"
  ,width:340
  ,height:270
  ,dlgTitle:"多行输入框属性"
  ,btnTitle:"多行输入框"});
//为控件对象注测行为


//注册add 方法
function addTextArea(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addTextArea);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object();
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
//alert(tag.type);   //tag是当前控件 控件.type可以获取他的类型
  if(tagName == 'TEXTAREA'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NTextArea','多行输入框属性');
    if (parent.useInfoResSubsys == '1') {
      contextMenu.AddItem('NMetadata','元数据属性');
    }
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener) ;