//得到控件对象
var FCKObject = plugins({comName:"NTextField"
  ,item:"NTextField"
  ,page:"ntextfield.jsp"
  ,inco:"textfield.gif"
  ,width:340
  ,height:300
  ,dlgTitle:"单行输入框"
  ,btnTitle:"单行输入框"});
//为控件对象注测行为

//注册add 方法
function addTextField(code){
  
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addTextField);
//注册右键菜单命令
addMenuItem(new Object(), "deleteDataSelect", deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object() ;
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
//alert(tag.type);   //tag是当前控件 控件.type可以获取他的类型
  if(tagName == 'INPUT' && tag.type == 'text'
     && (tag.className != "AUTO")&& (tag.className != "CALC")
    ){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NTextField','单行文本属性');
    if (parent.useInfoResSubsys == '1') {
      contextMenu.AddItem('NMetadata','元数据属性');
    }
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);