//得到控件对象
var FCKObject = plugins({comName:"NModule"
  ,item:"NModule"
  ,page:"nmodule.jsp"
  ,inco:"data.gif"
  ,width:340
  ,height:230
  ,dlgTitle:"业务组件"
  ,btnTitle:"业务组件"});
//为控件对象注测行为

//注册add 方法
function NModule(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',NModule);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object();
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
//alert(tag.type);   //tag是当前控件    tag.type可以获取他的类型
  if(tag && tag.className == 'MODULE' && tagName == 'IMG'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NModule','业务组件属性');
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);